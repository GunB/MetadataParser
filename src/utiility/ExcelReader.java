package utiility;

import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *  Lector de excel utilizando las librerías de Apach (Apache poi).
 * @author hangarita
 */
public final class ExcelReader {

    private XSSFSheet xssActualSheet = null;
    private XSSFWorkbook xssActualBook = null;
    
    String strPath;
    String strName;
    File fileData = null;

    /**
     *  Arreglo de hojas leídas de un libro de excel
     */
    private ArrayList<String> arrSheetNames = null;
    
    /**
     *  Constructor del lector. Define automáticamente el nombre de las hojas y 
     * prepara el programa para tener el objeto excel.
     * @param fileData
     * @throws IOException
     */
    public ExcelReader(File fileData) throws IOException {
        this.strPath = fileData.getParent();
        this.strName = fileData.getName();
        this.fileData = fileData;
        ReadFile();
        getArrSheetNames();
    }

    public XSSFWorkbook getXssActualBook() {
        return xssActualBook;
    }

    public String getStrPath() {
        return strPath;
    }

    public String getStrName() {
        return strName;
    }

    public File getFileData() {
        return fileData;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getArrSheetNames() {
        arrSheetNames = new ArrayList<>();
        Iterator<XSSFSheet> iteSheet = xssActualBook.iterator();

        while (iteSheet.hasNext()) {
            XSSFSheet tempSheet = iteSheet.next();
            this.arrSheetNames.add(tempSheet.getSheetName());
            //System.out.println(tempSheet.getSheetName());
        }

        return this.arrSheetNames;
    }

    /**
     *
     * @return Obtiene una hoja en formato Apache poi
     */
    public XSSFSheet getXssActualSheet() {
        return xssActualSheet;
    }

    /**
     *
     * @param xssSheet Hoja en formato Apache poi a transformar.
     * @return  Regresa la hoja en un Hashmap Clave, Valor siendo la clave el 
     * valor ingresado en el lado izquierdo y todo lo demas como valor. Si la clave
     * y el valor no son vacios, se reconocen como una nueva entrada correcta para el
     * Hashmap
     */
    public static HashMap turnSheetToObject(XSSFSheet xssSheet) {
        HashMap<String, String> objSheet = new HashMap();
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = xssSheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();

            String objKey = "";
            ArrayList<String> objValue = new ArrayList();

            while (cellIterator.hasNext()) {

                String objTemp = "";
                Cell cell = cellIterator.next();
                //Check the cell type and format accordingly
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        objTemp = cell.getNumericCellValue() + "";
                        break;
                    case Cell.CELL_TYPE_STRING:
                        objTemp = cell.getStringCellValue();
                        break;
                }
                
                objTemp = objTemp.trim();
                //objTemp = new String(objTemp.getBytes(StandardCharsets.UTF_8));

                if (cell.getColumnIndex() == 0) {
                    objKey = objTemp;
                } else {
                    objValue.add(objTemp);
                }
            }

            if (!objKey.isEmpty() && !objValue.isEmpty()) {
                objSheet.put(objKey, Joiner.on("").join(objValue).trim());
            }

            //System.out.println("");
        }

        return objSheet;
    }

    private void ReadFile() throws FileNotFoundException, IOException {
        FileInputStream file = new FileInputStream(fileData);
        //Create Workbook instance holding reference to .xlsx file
        this.xssActualBook = new XSSFWorkbook(file);
        file.close();
        //return this.xssActualBook;
    }

    /**
     *
     * @param strName nombre de hoja de calculo.
     * @return Regresa una hoja en formato Apache poi y además laa signa como 
     * la hoja actual a trabajar en la variable @xssActualSheet
     */
    public XSSFSheet ReadSheetbyName(String strName) {
        this.xssActualSheet = this.xssActualBook.getSheet(strName);
        return xssActualSheet;
    }

    /**
     *
     * @param intId 
     * @return
     */
    public XSSFSheet ReadSheetbyId(int intId) {
        this.xssActualSheet = this.xssActualBook.getSheetAt(intId);
        return xssActualSheet;
    }

    /**
     *
     */
    public void ReadnShowFile() {
        try {
            FileInputStream file = new FileInputStream(fileData);

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                    }
                }
                //System.out.println("");
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
