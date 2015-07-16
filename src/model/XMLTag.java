package model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLTag {

    String strName;
    ArrayList<String> arrAttr;

    public XMLTag(String strName) {
        this.strName = strName;
    }

    public String getStrName() {
        return this.strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public ArrayList<String> getArrAttr() {
        return this.arrAttr;
    }

    public void setArrAttr(ArrayList<String> arrAttr) {
        this.arrAttr = arrAttr;
    }

    public String returnFullTagData(String strData) {

        return new String(("<" + this.strName + ">" + strData + "</" + this.strName + ">").getBytes());

        //return null;
    }

    public Node returnFullNode(String strData) throws ParserConfigurationException, SAXException, IOException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(returnFullTagData(strData).getBytes())).getDocumentElement();
    }
}
