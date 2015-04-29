/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import model.Recurso;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.json.simple.parser.ParseException;

/**
 *
 * @author hangarita
 */
public class MetadataParser {

    ExcelReader xlsRead;

    Recurso recData;

    public Object ReadingConfig() throws IOException, ParseException {
        String strUrl = System.getProperty("user.dir").concat("\\config.json");
        System.out.println(strUrl);
        File f = new File(strUrl);
        Object jObject = new Gson().fromJson(new FileReader(f).toString(), Object.class);
        //System.out.println(jObject);
        return jObject;
    }

    public void ReadObject(String strUrl) throws IOException {
        xlsRead = new ExcelReader(strUrl);

        XSSFSheet readedSheet = xlsRead.ReadSheetbyId(0);
        HashMap<String, String> objSheet = ExcelReader.turnSheetToObject(readedSheet);

        for (Entry<String, String> entry : objSheet.entrySet()) {

            System.out.println("Key : " + entry.getKey());
            System.out.println("Value : " + entry.getValue());
            System.out.println("\n");
        }
        //xlsRead.ReadnShowFile();
    }
}
