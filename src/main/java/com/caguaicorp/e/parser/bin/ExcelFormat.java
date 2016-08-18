/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caguaicorp.e.parser.bin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.caguaicorp.e.parser.model.SharableContentObject;
import com.caguaicorp.e.parser.model.XMLTag;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.caguaicorp.e.parser.utiility.ExcelReader;
import com.caguaicorp.e.parser.utiility.FilesUtility;
import static com.caguaicorp.e.parser.utiility.XMLUtility.AddNodeList2Node;
import static com.caguaicorp.e.parser.utiility.XMLUtility.ChangeNode;

/**
 *
 * @author hangarita
 */
public class ExcelFormat {

    private final ExcelReader filExcel;
    private HashMap<String, HashMap<String, String>> arrObjects = new HashMap<>();
    Document doc = null;

    String[][] arrNames
            = {
                {"identifier", "Nomenclatura"},
                {"title", "Título"},
                {"description", "Descripción"},
                {"keyword", "Palabras Claves"},
                {"learningGoal", "Objetivo de Aprendizaje\n" + " (Learning Goal)"},
                {"triggerQuestion", "Pregunta Detonante\n" + "(Trigger Question)"},
                {"pedagogicalAspect", "Aspectos Pedagógicos \n" + "(Pedagogical Aspects)"},
                {"recommendedUse", "Sugerencia de Uso\n" + "(Recommended Use)"},};

    public String getName() {
        return filExcel.getStrName();
    }

    public ExcelFormat(ExcelReader filExcel) throws ParserConfigurationException, SAXException, IOException {
        this.filExcel = filExcel;
        String xmlUrl = FilesUtility.strRoot.concat(File.separator).concat("metadata.xml");
        XmlFormatBase(xmlUrl);
        ExcelParser();
    }

    public ExcelFormat(ExcelReader filExcel, String strUrlXMLBase) throws ParserConfigurationException, SAXException, IOException {
        this.filExcel = filExcel;
        XmlFormatBase(strUrlXMLBase);
        ExcelParser();
    }

    private void XmlFormatBase(String strBase) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        //FileInputStream in = new FileInputStream(new File(strBase));
        //doc = dBuilder.parse(in, "UTF-8");
        doc = dBuilder.parse(strBase);
    }

    private void ExcelParser() {
        for (String strNameSheet : filExcel.getArrSheetNames()) {
            XSSFSheet readedSheet = filExcel.ReadSheetbyName(strNameSheet);
            //filExcel.arrSheetNames.remove(0);
            HashMap<String, String> objSheet = ExcelReader.turnSheetToObject(readedSheet);
            arrObjects.put(objSheet.get(arrNames[0][1]), objSheet);
        }
    }

    public HashMap<String, HashMap<String, String>> getArrObjects() {
        return arrObjects;
    }

    public Document SharableContentObjectCompleter(SharableContentObject scoSco) throws ParserConfigurationException, SAXException, IOException {
        Document docu = (Document) this.doc.cloneNode(true);
        Node staff = docu.getElementsByTagName("lom").item(0);
        NodeList list = staff.getChildNodes();

        HashMap<String, String> objObjeto = arrObjects.get(scoSco.getStrID());
        ArrayList arrResp;

        for (String[] arrName : arrNames) {
            if (objObjeto.containsKey(arrName[1])) {
                switch (arrName[0]) {
                    case "title":
                        arrResp = new ArrayList<>(Arrays.asList("general", "title"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        scoSco.setStrNombre(objObjeto.get(arrName[1]));
                        break;
                    case "description":
                        arrResp = new ArrayList<>(Arrays.asList("general", "description"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        scoSco.setStrDesc(objObjeto.get(arrName[1]));
                        break;
                    case "identifier":
                        arrResp = new ArrayList<>(Arrays.asList("general", "identifier", "catalog"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        scoSco.setStrID(objObjeto.get(arrName[1]));
                        break;
                    case "keyword":
                        String[] arrKeyWords = objObjeto.get(arrName[1]).split(",");
                        arrResp = new ArrayList<>(Arrays.asList("general", "keyword"));
                        list = AddNodeList2Node(list, docu, arrResp, arrKeyWords, new XMLTag("li"));
                        break;
                    case "learningGoal":
                        arrResp = new ArrayList<>(Arrays.asList("educational", "description", "learningGoal"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        break;
                    case "triggerQuestion":
                        arrResp = new ArrayList<>(Arrays.asList("educational", "description", "triggerQuestion"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        break;
                    case "pedagogicalAspect":
                        arrResp = new ArrayList<>(Arrays.asList("educational", "description", "pedagogicalAspect"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        break;
                    case "recommendedUse":
                        arrResp = new ArrayList<>(Arrays.asList("educational", "description", "recommendedUse"));
                        list = ChangeNode(list, arrResp, objObjeto.get(arrName[1]));
                        break;
                }
            }
        }

        scoSco.setDocXML(docu);
        return docu;
    }
}
