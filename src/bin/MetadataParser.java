/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import model.Leccion;
import model.Objeto;
import model.Recurso;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.simple.parser.ParseException;

/**
 *
 * @author hangarita
 */
public class MetadataParser {

    ExcelReader xlsRead;
    HashMap<String, HashMap> jConfig;
    public Leccion lecData;

    public void ReadingConfig() throws IOException, ParseException {
        //String strUrl = System.getProperty("user.dir").concat("\\config.json");
        //System.out.println(strUrl);
        //File f = new File(strUrl);
        //this.jConfig = new Gson().fromJson(new FileReader(f).toString(), HashMap.class);
        //System.out.println(jObject);
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
                objSheet.get("Nomenclatura"),
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
            Recurso createRecurso = createRecurso(objSheet);
            recData.add(createRecurso);
        }
        
        objTemp.setArrRecursos(recData);
        lecData.setObjObjeto(objTemp);

    }

    private Recurso createRecurso(HashMap<String, String> objSheet) {

        HashMap<String, String> objData = new HashMap<>();

        objData.put("keyWord", objSheet.get("Palabras Claves"));
        objData.put("learningGoal", objSheet.get("Objetivo de Aprendizaje\n" + " (Learning Goal)"));
        objData.put("triggerQuestion", objSheet.get("Pregunta Detonante\n" + "(Trigger Question)"));
        objData.put("pedagogicalAspect", objSheet.get("Aspectos Pedagógicos \n" + "(Pedagogical Aspects)"));
        objData.put("recommendedUse", objSheet.get("Sugerencia de Uso\n" + "(Recommended Use)"));

        Recurso recTemp = new Recurso(objSheet.get("Título"),
                objSheet.get("Nomenclatura"),
                objSheet.get("Descripción"));
        
        recTemp.setArrData(objData);
        
        return recTemp;
    }
}
