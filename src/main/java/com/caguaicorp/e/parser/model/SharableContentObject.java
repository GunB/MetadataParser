package com.caguaicorp.e.parser.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.caguaicorp.e.parser.utiility.XMLUtility;

public class SharableContentObject {

    String strType;
    String strNombre;
    String strID;
    String strDesc;
    Document docXML;
    Node ndRelation;
    Node ndContribute;
    HashMap<String, String> arrRelations = new HashMap();

    Boolean isChanged = false;

    ArrayList<SharableContentObject> objElements = new ArrayList();

    HashMap<String, String> arrData = new HashMap();

    ElementHandler eleData;

    public String getStrType() {
        return this.strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
        isChanged = true;
    }

    public String getStrNombre() {
        return this.strNombre;
    }

    public void setStrNombre(String strNombre) {
        isChanged = true;
        this.strNombre = strNombre;
    }

    public String getStrID() {
        return this.strID;
    }

    public void setStrID(String strID) {
        isChanged = true;
        this.strType = GetType(strID);
        this.strID = strID;
    }

    public String getStrDesc() {
        return this.strDesc;
    }

    public void setStrDesc(String strDesc) {
        isChanged = true;
        this.strDesc = strDesc;
    }

    public Document getDocXML() {
        return this.docXML;
    }

    public void setDocXML(Document docXML) {
        isChanged = true;
        this.docXML = (Document) docXML.cloneNode(true);
    }

    public Boolean getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(Boolean isChanged) {
        this.isChanged = isChanged;
    }

    public static String GetType(String strID1) throws NullPointerException {
        //String strID1 = scoData.getStrID();
        String strResp;

        if (strID1 == null || strID1.isEmpty()) {
            strResp = "Vacio".toUpperCase();
        } else if (strID1.contains("re")) {
            strResp = "Recurso".toUpperCase();
        } else if (strID1.contains("ob")) {
            strResp = "Objeto".toUpperCase();
        } else if (strID1.contains("le")) {
            strResp = "Leccion".toUpperCase();
        } else {
            strResp = "Nivel".toUpperCase();
        }

        System.out.println("Found DATA [Type]: " + strResp);

        return strResp;
    }

    public SharableContentObject(ElementHandler eleData) throws IOException, SAXException, ParserConfigurationException, NullPointerException {
        this.eleData = eleData;

        this.docXML = eleData.Read(true);

        this.docXML.getDocumentElement().normalize();
        
        System.out.println("Root element :" + this.docXML.getDocumentElement().getNodeName());

        String strNode = "<relation><kind schema=\"\"/><resource><identifier>"
                + "<catalog catName=\"\" catSource=\"\"/></identifier><description lang=\"\"/>"
                + "</resource></relation>";

        this.ndRelation = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(strNode.getBytes(StandardCharsets.UTF_8))).getDocumentElement();

        this.ndRelation.normalize();

        /*String strLyfeCicle = ""
                + "    <contribute>\n"
                + "      <role schema=\"CEM\">Productor ejecutivo</role>\n"
                + "      <entity entityForm=\"24933\" type=\"Persona\" src=\"info@elltechnologies.com\" "
                + "         institution=\"ELL Technologies Ltd.\" country=\"CA\">Martin, Bill</entity>\n"
                + "      <date>30-04-2015</date>\n"
                + "    </contribute>";

        this.ndContribute = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(strLyfeCicle.getBytes(StandardCharsets.UTF_8))).getDocumentElement();*/

        this.ndContribute.normalize();

        ReadElement();
    }

    private void ReadElement() {
        Node ndRoot = this.docXML.getDocumentElement().cloneNode(true);
        NodeList list = ndRoot.getChildNodes();

        this.strID = XMLUtility.ReadNode(list, new ArrayList(Arrays.asList(new String[]{"general", "identifier", "catalog"})));
        this.strNombre = XMLUtility.ReadNode(list, new ArrayList(Arrays.asList(new String[]{"general", "title"})));
        this.strDesc = XMLUtility.ReadNode(list, new ArrayList(Arrays.asList(new String[]{"general", "description"})));
        this.strType = GetType(this.getStrID());
    }

    public boolean isRelationed() {
        Node ndRoot = this.docXML.getDocumentElement().cloneNode(true);
        NodeList list = ndRoot.getChildNodes();
        String ReadNode = XMLUtility.ReadNode(list, new ArrayList(Arrays.asList(new String[]{"relation"})));

        return !(ReadNode == null);
    }

    public void SetRelation(SharableContentObject scoObjeto, String strKind) {
        isChanged = true;
        Node ndData = this.ndRelation.cloneNode(true);
        NodeList ndListTemp = ndData.getChildNodes();
        XMLUtility.ChangeNode(ndListTemp, new ArrayList(Arrays.asList(new String[]{"kind"})), strKind, null);
        XMLUtility.ChangeNode(ndListTemp, new ArrayList(Arrays.asList(new String[]{"resource", "identifier", "catalog"})), scoObjeto.strID, null);
        XMLUtility.ChangeNode(ndListTemp, new ArrayList(Arrays.asList(new String[]{"resource", "description"})), scoObjeto.strDesc, null);

        this.docXML.adoptNode(ndData);
        this.docXML.getDocumentElement().appendChild(ndData);
    }

    public void SaveChanges() throws IOException, TransformerException {
        isChanged = false;
        this.eleData.WriteFinish(this.docXML);
    }
}
