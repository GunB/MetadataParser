package com.caguaicorp.e.parser.utiility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.caguaicorp.e.parser.model.XMLTag;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtility {

    public static NodeList AddNodeList2Node(NodeList listNode, Document docFather, ArrayList<String> arrStrCompare, String[] arrNewData, XMLTag xmlTag) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> arrTempList = arrStrCompare;
        Iterator<String> iterator = arrTempList.iterator();

        while (iterator.hasNext()) {
            String strCompare = (String) iterator.next();
            iterator.remove();

            for (int i = 0; i < listNode.getLength(); i++) {
                Node node = listNode.item(i);

                if (strCompare.equals(node.getNodeName())) {
                    if (iterator.hasNext()) {
                        node = AddNodeList2Node(node.getChildNodes(), docFather, arrTempList, arrNewData, xmlTag).item(0);
                        break;
                    }

                    for (String s : arrNewData) {
                        s = s.trim();
                        s = s.replaceAll("&([^;]+(?!(?:\\w|;)))", "&amp;$1");

                        if (!s.isEmpty()) {
                            Node item = xmlTag.returnFullNode(s);
                            docFather.adoptNode(item);
                            //item.appendChild(item);
                            node.appendChild(item);
                        }
                    }

                    break;
                }
            }
        }

        return listNode;
    }

    public static NodeList ChangeNode(NodeList listNode, ArrayList<String> arrStrCompare, String strNewValue, HashMap<String, String> mapAttributes) {
        ArrayList<String> arrTempList = (ArrayList<String>) arrStrCompare.clone();
        Iterator<String> iterator = arrTempList.iterator();

        while (iterator.hasNext()) {
            String strCompare = (String) iterator.next();
            iterator.remove();

            for (int i = 0; i < listNode.getLength(); i++) {
                Node node = listNode.item(i);

                if (strCompare.equals(node.getNodeName())) {
                    if (iterator.hasNext()) {
                        node = ChangeNode(node.getChildNodes(), arrTempList, strNewValue, mapAttributes).item(0);
                        break;
                    }
                    ChangeData(node, strNewValue, mapAttributes);
                    break;
                }
            }
        }
        return listNode;
    }

    private static void ChangeData(Node node, String strNewValue, HashMap<String, String> mapAttributes) {
        if (strNewValue != null) {
            node.setTextContent(strNewValue);
        }
        if (mapAttributes != null) {
            Iterator it = mapAttributes.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ((Element) node).setAttribute(pair.getKey().toString(), pair.getValue().toString());
                String attributes = ((Element) node).getAttribute((String) pair.getKey());
                //System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
    }

    public static Node ReadNode(NodeList listNode, ArrayList<String> arrStrCompare) {
        ArrayList<String> arrTempList = (ArrayList<String>) arrStrCompare.clone();
        Iterator<String> iterator = arrTempList.iterator();

        while (iterator.hasNext()) {
            String strCompare = (String) iterator.next();
            iterator.remove();
            int length = listNode.getLength();

            for (int i = 0; i < length; i++) {
                Node node = listNode.item(i);

                if (strCompare.equals(node.getNodeName())) {
                    if (iterator.hasNext()) {
                        return ReadNode(node.getChildNodes(), arrTempList);
                    }
                    String strResp = node.getTextContent();
                    System.out.println("Read NODE [" + strCompare + "]: " + strResp);
                    return node;
                }
            }
        }

        return null;
    }

    public static Node SearchNode(NodeList listNode, ArrayList<String> arrStrCompare) {
        ArrayList<String> arrTempList = arrStrCompare;
        Iterator<String> iterator = arrTempList.iterator();

        while (iterator.hasNext()) {
            String strCompare = (String) iterator.next();
            iterator.remove();

            for (int i = 0; i < listNode.getLength(); i++) {
                Node node = listNode.item(i);

                if (strCompare.equals(node.getNodeName())) {
                    if (iterator.hasNext()) {
                        return SearchNode(node.getChildNodes(), arrTempList);
                    }
                    String strResp = node.getTextContent();
                    System.out.println("Found DATA [" + strCompare + "]: " + strResp);
                    return node;
                }
            }
        }

        return null;
    }

    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }

    public static Document newDocumentFromInputStream(InputStream in)
            throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document ret = builder.parse(new InputSource(in));
        return ret;
    }

    public static InputStream newInputStreamFromDocument(Document doc) throws TransformerConfigurationException, TransformerException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(doc);
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static String newStringFromDocument(Document doc) throws TransformerConfigurationException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty("omit-xml-declaration", "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
    }
}
