/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Leccion;
import model.Objeto;
import model.Recurso;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hangarita
 */
public class MetadataParser {

    ExcelReader xlsRead;
    HashMap<String, HashMap> jConfig;
    public Leccion lecData;
    public Document xmlMetaBase;
    File fXmlFile;

    public void ReadingConfig() throws IOException, ParseException {
        //String strUrl = System.getProperty("user.dir").concat("\\config.json");
        //System.out.println(strUrl);
        //File f = new File(strUrl);
        //this.jConfig = new Gson().fromJson(new FileReader(f).toString(), HashMap.class);
        //System.out.println(jObject);
    }

    public void ReadMetadataBase() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        String strUrl = System.getProperty("user.dir").concat("\\metadata.xml");
        System.out.println(strUrl);
        fXmlFile = new File(strUrl);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        //this.jConfig = new Gson().fromJson(new FileReader(f).toString(), HashMap.class);
        //System.out.println(jObject);

        //printDocument(doc, System.out);
        xmlMetaBase = doc;
    }

    public void CreateXMLFull() throws TransformerException {
        CreateFullXMLObjeto();
    }

    public void CreateFullXMLObjeto() throws TransformerException {
        /*try {
         printDocument(xmlMetaBase, System.out);
         } catch (IOException | TransformerException ex) {
         Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
         }//*/

        // Get the staff element by tag name directly
        Node staff = xmlMetaBase.getElementsByTagName("lom").item(0);
        NodeList list = staff.getChildNodes();

        Objeto objObjeto = lecData.getObjObjeto();

        list = ChangeNode(list, new ArrayList<>(Arrays.asList("general", "title")), objObjeto.getStrID());
        list = ChangeNode(list, new ArrayList<>(Arrays.asList("general", "description")), objObjeto.getStrDesc());
        list = ChangeNode(list, new ArrayList<>(Arrays.asList("general", "identifier", "catalog")), objObjeto.getArrData().get("keyWord"));

        // write the content into xml file
        lecData.setObjObjeto(objObjeto);

        //xmlMetaBase.appendChild(node);
        //pruebaPrint();
        //*/
    }

    public void SaveChangesXML() throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xmlMetaBase);
        StreamResult result = new StreamResult(fXmlFile);
        transformer.transform(source, result);
    }

    private void pruebaPrint() {
        try {
            printDocument(xmlMetaBase, System.out);
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private NodeList ChangeNode(NodeList listNode, ArrayList<String> arrStrCompare, String strNewValue) {

        NodeList tempNode = listNode;
        ArrayList<String> arrTempList = arrStrCompare;
        Iterator<String> iterator = arrTempList.iterator();

        while (iterator.hasNext()) {
            String strCompare = iterator.next();
            iterator.remove();

            for (int i = 0; i < listNode.getLength(); i++) {
                Node node = listNode.item(i);
                // get the salary element, and update the value

                System.out.println(node.getNodeName() + " " + strCompare);

                if (strCompare.equals(node.getNodeName())) {

                    if (iterator.hasNext()) {
                        node = ChangeNode(node.getChildNodes(), arrTempList, strNewValue).item(0);
                    } else {
                        node.setTextContent(strNewValue);
                    }

                    break;
                }
            }

        }

        return listNode;
    }

    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }

    public void ReadObject(String strUrl) throws IOException {
        xlsRead = new ExcelReader(strUrl);

        XSSFSheet readedSheet = xlsRead.ReadSheetbyId(0);
        xlsRead.arrSheetNames.remove(0);

        HashMap<String, String> objSheet = ExcelReader.turnSheetToObject(readedSheet);

        lecData = new Leccion(
                objSheet.get("Título"),
                objSheet.get("Nomenclatura"),
                objSheet.get("Descripción")
        );

        readedSheet = xlsRead.ReadSheetbyId(1);
        xlsRead.arrSheetNames.remove(0);

        objSheet = ExcelReader.turnSheetToObject(readedSheet);

        Objeto objTemp = new Objeto(
                objSheet.get("Título"),
                lecData.getStrID() + objSheet.get("Nomenclatura"),
                objSheet.get("Descripción")
        );

        HashMap<String, String> objData = new HashMap<>();

        objData.put("keyWord", objSheet.get("Palabras Claves"));
        objData.put("learningGoal", objSheet.get("Objetivo de Aprendizaje\n" + " (Learning Goal)"));
        objData.put("triggerQuestion", objSheet.get("Pregunta Detonante\n" + "(Trigger Question)"));
        objData.put("pedagogicalAspect", objSheet.get("Aspectos Pedagógicos \n" + "(Pedagogical Aspects)"));
        objData.put("recommendedUse", objSheet.get("Sugerencia de Uso\n" + "(Recommended Use)"));

        objTemp.setArrData(objData);

        //lecData.setObjObjeto(objTemp);
        ArrayList<Recurso> recData = new ArrayList<>();

        for (String strName : xlsRead.arrSheetNames) {
            readedSheet = xlsRead.ReadSheetbyName(strName);
            objSheet = ExcelReader.turnSheetToObject(readedSheet);
            Recurso createRecurso = createRecurso(objSheet, objTemp);
            recData.add(createRecurso);
        }

        objTemp.setArrRecursos(recData);
        lecData.setObjObjeto(objTemp);

    }

    private Recurso createRecurso(HashMap<String, String> objSheet, Objeto objLeccion) {

        HashMap<String, String> objData = new HashMap<>();

        objData.put("keyWord", objSheet.get("Palabras Claves"));
        objData.put("learningGoal", objSheet.get("Objetivo de Aprendizaje\n" + " (Learning Goal)"));
        objData.put("triggerQuestion", objSheet.get("Pregunta Detonante\n" + "(Trigger Question)"));
        objData.put("pedagogicalAspect", objSheet.get("Aspectos Pedagógicos \n" + "(Pedagogical Aspects)"));
        objData.put("recommendedUse", objSheet.get("Sugerencia de Uso\n" + "(Recommended Use)"));

        Recurso recTemp = new Recurso(objSheet.get("Título"),
                objLeccion.getStrID() + objSheet.get("Nomenclatura"),
                objSheet.get("Descripción"));

        recTemp.setArrData(objData);

        return recTemp;
    }
}
