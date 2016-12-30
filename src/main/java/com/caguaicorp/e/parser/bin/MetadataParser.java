package com.caguaicorp.e.parser.bin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import com.caguaicorp.e.parser.model.SharableContentObject;
import org.xml.sax.SAXException;
import com.caguaicorp.e.parser.utiility.ExcelReader;
import com.caguaicorp.e.parser.utiility.FilesUtility;
import com.caguaicorp.e.parser.utiility.JFolderChooser;
import java.util.TreeMap;

public class MetadataParser implements Runnable {

    private boolean isCopy = true;
    private String strPath;
    private JLabel lblData = null;
    private boolean isIgnoreFather = false;

    PrintWriter newLog;
    String strNameLog = "READLOG";

    long unixTime = System.currentTimeMillis() / 1000L;
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());

    public MetadataParser(Object params) {
        Object[] objData = (Object[]) params;

        this.isCopy = ((Boolean) objData[2]);
        this.isIgnoreFather = ((Boolean) objData[3]);

        this.strPath = ((String) objData[0]);
        this.lblData = ((JLabel) objData[1]);
    }

    public MetadataParser() {
    }

    public static void main(String[] args) {
        new MetadataParser().Exec(args);
    }

    public void Exec(String[] args) {
        System.out.println("Program Arguments:");
        for (String arg : args) {
            System.out.println("\t" + arg);
        }

        File baseFileDirectory = new File(args[0]);

        this.strNameLog = baseFileDirectory.getPath().concat(File.separator).concat(this.strNameLog + "_" + this.unixTime).concat(".txt");
        try {
            this.newLog = new PrintWriter(this.strNameLog, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<File> listFilesForFolder = JFolderChooser.listRawFilesForFolder(baseFileDirectory, true);

        TreeMap<String, SharableContentObject> arrRec = new TreeMap();
        TreeMap<String, SharableContentObject> arrObj = new TreeMap();
        TreeMap<String, SharableContentObject> arrLec = new TreeMap();
        TreeMap<String, SharableContentObject> arrLvl = new TreeMap();

        ArrayList<ExcelFormat> arrExcel = new ArrayList();

        String strMessage = "Leyendo SCO(s)... ";
        String strMessage2;
        Log(strMessage);

        //<editor-fold defaultstate="collapsed" desc="Reading SCO(s)">
        for (File strNameFolder : listFilesForFolder) {
            SharableContentObject scoData = null;

            if (strNameFolder.getName().startsWith("~$")) {
                continue;
            }

            if (strNameFolder.getName().endsWith(".zip")) {
                try {
                    scoData = new SharableContentObject(new ZipReader(strNameFolder));
                    if (scoData.getStrType().equals("VACIO")) {
                        String strName = strNameFolder.getName().substring(0, strNameFolder.getName().lastIndexOf('.'));
                        scoData.setStrID(strName);
                    }
                    Log(strMessage + scoData.getStrID());
                } catch (IOException | SAXException | ParserConfigurationException | NullPointerException ex) {
                    System.err.println("ERROR [NOT METADATA]: " + strNameFolder.getName());
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (strNameFolder.getName().endsWith(".xml")) {
                try {
                    scoData = new SharableContentObject(new XMLReader(strNameFolder));
                    if (scoData.getStrType().equals("VACIO")) {
                        scoData.setStrID(strNameFolder.getName().substring(0, strNameFolder.getName().lastIndexOf('.')));
                    }
                    Log(strMessage + scoData.getStrID());
                } catch (IOException | SAXException | ParserConfigurationException | NullPointerException ex) {
                    System.err.println("ERROR [NOT METADATA]: " + strNameFolder.getName());
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (strNameFolder.getName().endsWith(".xls") || strNameFolder.getName().endsWith(".xlsx")) {
                try {
                    ExcelReader excelReader = new ExcelReader(strNameFolder);
                    ExcelFormat excelFormat;
                    try {
                        excelFormat = new ExcelFormat(excelReader);
                        arrExcel.add(excelFormat);
                    } catch (ParserConfigurationException | SAXException ex) {
                        Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Log(strMessage + excelReader.getStrName());
                } catch (IOException ex) {
                    System.err.println("ERROR [NOT READABLE]: " + strNameFolder.getName());
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                if ((strNameFolder.getName().endsWith(".xml")) || (strNameFolder.getName().endsWith(".zip"))) {
                    switch (scoData.getStrType()) {
                        case "NIVEL":
                            arrLvl.put(scoData.getStrID(), scoData);
                            break;
                        case "LECCION":
                            arrLec.put(scoData.getStrID(), scoData);
                            break;
                        case "OBJETO":
                            arrObj.put(scoData.getStrID(), scoData);
                            break;
                        case "RECURSO":
                            arrRec.put(scoData.getStrID(), scoData);
                            break;
                    }
                }
            } catch (NullPointerException ex) {
                System.err.println("Failed: " + strNameFolder.getPath());
                Log("Failed: " + strNameFolder.getPath() + " " + ex);
                Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //</editor-fold>

        strMessage = "Interpretando Excel... ";
        strMessage2 = "FILL: ";
        Log(strMessage);

        //<editor-fold defaultstate="collapsed" desc="Extra data from Excel">
        for (ExcelFormat reader : arrExcel) {

            Log(strMessage2 + reader.getName());

            for (Entry<String, HashMap<String, String>> entry : reader.getArrObjects().entrySet()) {
                String key = entry.getKey();
                //HashMap value = entry.getValue();
                String type = SharableContentObject.GetType(key);

                TreeMap arrActual = null;

                //<editor-fold defaultstate="collapsed" desc="Typing">
                switch (type) {
                    case "NIVEL":
                        arrActual = (TreeMap) arrLvl.clone();
                        //arrLvl.put(scoData.getStrID(), scoData);
                        break;
                    case "LECCION":
                        arrActual = (TreeMap) arrLec.clone();
                        //arrLec.put(scoData.getStrID(), scoData);
                        break;
                    case "OBJETO":
                        arrActual = (TreeMap) arrObj.clone();
                        //arrObj.put(scoData.getStrID(), scoData);
                        break;
                    case "RECURSO":
                        arrActual = (TreeMap) arrRec.clone();
                        //arrRec.put(scoData.getStrID(), scoData);
                        break;
                    case "VACIO":
                        continue;
                }
                //</editor-fold>

                if (arrActual.containsKey(key)) {

                    try {
                        SharableContentObject get = (SharableContentObject) arrActual.get(key);
                        reader.SharableContentObjectCompleter(get);

                        Log(strMessage2 + key);
                    } catch (ParserConfigurationException | SAXException | IOException ex) {
                        Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                        Log("XML BASE ERROR!!!");
                        JOptionPane.showMessageDialog(null, "XML BASE ERROR!!!", "Mensaje", JOptionPane.ERROR_MESSAGE);
                        //System.exit(0);
                        return;
                    }

                } else {
                    Log("EL ELEMENTO " + key + " NO EXISTE!");
                }
                // do what you have to do here
                // In your case, an other loop.
            }
        }
        //</editor-fold>

        strMessage = "Creando relaciones en los recursos... ";
        strMessage2 = "REL: ";
        Log(strMessage);

        //<editor-fold defaultstate="collapsed" desc="Rels">
        for (Entry<String, SharableContentObject> entryscoData : arrRec.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();
            if (scoData.isRelationed()) {
                Log("YA SE ENCUENTRA CON RELACIONES, SCO NO ACTUALIZADO " + scoData.getStrID());
            } else {
                Log(strMessage + scoData.getStrID());
                int cont = 0;
                for (Entry<String, SharableContentObject> entryscoSCO : arrObj.entrySet()) {
                    SharableContentObject scoSCO = entryscoSCO.getValue();

                    if (scoData.getStrID().contains(scoSCO.getStrID())) {
                        cont++;
                        Log(strMessage2 + cont + " Es parte de \t" + scoSCO.getStrID());
                        scoData.SetRelation(scoSCO, "Es parte de");
                    }
                }
            }
        }

        strMessage = "Creando relaciones en los Objetos... ";

        for (Entry<String, SharableContentObject> entryscoData : arrObj.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();
            if (scoData.isRelationed()) {
                Log("YA SE ENCUENTRA CON RELACIONES, SCO NO ACTUALIZADO " + scoData.getStrID());
            } else {
                boolean bulPadre = false;

                int cont = 0;
                for (Entry<String, SharableContentObject> entryscoSCO : arrLec.entrySet()) {
                    SharableContentObject scoSCO = entryscoSCO.getValue();

                    if (scoData.getStrID().contains(scoSCO.getStrID())) {
                        bulPadre = true;
                        cont++;
                        Log(strMessage2 + cont + " Es parte de \t" + scoSCO.getStrID());
                        scoData.SetRelation(scoSCO, "Es parte de");
                    }
                }

                if (bulPadre || isIgnoreFather) {
                    cont = 0;
                    Log(strMessage + scoData.getStrID());

                    for (Entry<String, SharableContentObject> entryscoSCO : arrRec.entrySet()) {
                        SharableContentObject scoSCO = entryscoSCO.getValue();
                        if (scoSCO.getStrID().contains(scoData.getStrID())) {
                            cont++;
                            Log(strMessage2 + cont + " Está compuesto por \t" + scoSCO.getStrID());
                            scoData.SetRelation(scoSCO, "Está compuesto por");
                        }
                    }
                } else {
                    Log("No fué identificado el padre... relaciones abortadas: " + scoData.getStrID());
                }
            }
        }

        strMessage = "Creando relaciones en las Lecciones... ";

        for (Entry<String, SharableContentObject> entryscoData : arrLec.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();
            if (scoData.isRelationed()) {
                Log("YA SE ENCUENTRA CON RELACIONES, SCO NO ACTUALIZADO " + scoData.getStrID());
            } else {
                Log(strMessage + scoData.getStrID());
                boolean bulPadre = false;

                int cont = 0;
                for (Entry<String, SharableContentObject> entryscoSCO : arrLvl.entrySet()) {
                    SharableContentObject scoSCO = entryscoSCO.getValue();
                    if (scoData.getStrID().contains(scoSCO.getStrID())) {
                        bulPadre = true;
                        cont++;
                        Log(strMessage2 + cont + " Es parte de \t" + scoSCO.getStrID());
                        scoData.SetRelation(scoSCO, "Es parte de");
                    }
                }

                if (bulPadre || isIgnoreFather) {
                    cont = 0;
                    for (Entry<String, SharableContentObject> entryscoSCO : arrObj.entrySet()) {
                        SharableContentObject scoSCO = entryscoSCO.getValue();
                        if (scoSCO.getStrID().contains(scoData.getStrID())) {
                            cont++;
                            Log(strMessage2 + cont + " Está compuesto por \t" + scoSCO.getStrID());
                            scoData.SetRelation(scoSCO, "Está compuesto por");
                        }
                    }
                } else {
                    Log("No fué identificado el padre... relaciones abortadas: " + scoData.getStrID());
                }
            }
        }

        strMessage = "Creando relaciones en los niveles... ";

        for (Entry<String, SharableContentObject> entryscoData : arrLvl.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();
            if (scoData.isRelationed()) {
                Log("YA SE ENCUENTRA CON RELACIONES, SCO NO ACTUALIZADO " + scoData.getStrID());
            } else {
                Log(strMessage + scoData.getStrID());

                int cont = 0;
                for (Entry<String, SharableContentObject> entryscoSCO : arrLec.entrySet()) {
                    SharableContentObject scoSCO = entryscoSCO.getValue();
                    if (scoSCO.getStrID().contains(scoData.getStrID())) {
                        cont++;
                        Log(strMessage2 + cont + " Está compuesto por \t" + scoSCO.getStrID());
                        scoData.SetRelation(scoSCO, "Está compuesto por");
                    }
                }

            }
        }
        //</editor-fold>

        strMessage = "Guardando cambios... \t";
        strMessage2 = "NO HAY CAMBIOS... \t";

        //<editor-fold defaultstate="collapsed" desc="Saving changes...">
        for (Entry<String, SharableContentObject> entryscoData : arrLvl.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();

            if (scoData.getIsChanged()) {
                try {
                    Log(strMessage + scoData.getStrID());
                    scoData.SaveChanges();
                } catch (IOException | TransformerException ex) {
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex);
                    System.exit(6);
                }
            } else {
                Log(strMessage2 + scoData.getStrID());
            }

        }

        for (Entry<String, SharableContentObject> entryscoData : arrLec.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();

            if (scoData.getIsChanged()) {
                try {
                    Log(strMessage + scoData.getStrID());
                    scoData.SaveChanges();
                } catch (IOException | TransformerException ex) {
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex);
                    System.exit(6);
                }
            } else {
                Log(strMessage2 + scoData.getStrID());
            }
        }

        for (Entry<String, SharableContentObject> entryscoData : arrObj.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();

            if (scoData.getIsChanged()) {
                try {
                    Log(strMessage + scoData.getStrID());
                    scoData.SaveChanges();
                } catch (IOException | TransformerException ex) {
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex);
                    System.exit(6);
                }
            } else {
                Log(strMessage2 + scoData.getStrID());
            }
        }

        for (Entry<String, SharableContentObject> entryscoData : arrRec.entrySet()) {
            SharableContentObject scoData = entryscoData.getValue();

            if (scoData.getIsChanged()) {
                try {
                    Log(strMessage + scoData.getStrID());
                    scoData.SaveChanges();
                } catch (IOException | TransformerException ex) {
                    Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex);
                    System.exit(6);
                }
            } else {
                Log(strMessage2 + scoData.getStrID());
            }
        }
        //</editor-fold>

        this.newLog.close();
        JOptionPane.showMessageDialog(null, "Terminado exitosamente", "Mensaje", 1);
        System.exit(0);
    }

    private void Log(String strLog) {
        try {
            this.lblData.setText(strLog);
        } catch (NullPointerException localNullPointerException1) {
        }

        try {
            this.newLog.println(timeStamp + " - " + strLog);
        } catch (NullPointerException ex) {
            Logger.getLogger(MetadataParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        if (this.isCopy) {
            File CopyFolder = FilesUtility.CopyFolder(this.strPath);
            //Log("Copiando archivos...");
            this.strPath = CopyFolder.getPath();
            String[] strparams = {this.strPath};

            Exec(strparams);
        } else {
            String[] strparams = {this.strPath};
            Exec(strparams);
        }
    }
}
