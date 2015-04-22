package bin;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

    private String strUri = null;
    private XSSFSheet xssActualSheet = null;
    private XSSFWorkbook xssActualBook = null;
    private Object objSheet = null;
    private ArrayList<String> arrSheetNames = null;

    public ExcelReader(String strUri) {
        this.strUri = strUri;
        ReadFile();
    }

    public ArrayList<String> getArrSheetNames() {
        arrSheetNames = new ArrayList<>();
        Iterator<XSSFSheet> iteSheet = xssActualBook.iterator();

        while (iteSheet.hasNext()) {
            XSSFSheet tempSheet = iteSheet.next();
            this.arrSheetNames.add(tempSheet.getSheetName());
            System.out.println(tempSheet.getSheetName());
        }

        return this.arrSheetNames;
    }

    public String getStrUri() {
        return strUri;
    }

    public XSSFSheet getXssActualSheet() {
        return xssActualSheet;
    }

    public Object turnSheetToObject() {
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = this.xssActualSheet.iterator();
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
            System.out.println("");
        }

        return this.objSheet;
    }

    private void ReadFile() {
        try {
            FileInputStream file = new FileInputStream(new File(this.strUri));
            //Create Workbook instance holding reference to .xlsx file
            this.xssActualBook = new XSSFWorkbook(file);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return this.xssActualBook;
    }

    public XSSFSheet ReadSheetbyName(String strName) {
        this.xssActualSheet = this.xssActualBook.getSheet(strName);
        return xssActualSheet;
    }

    public XSSFSheet ReadSheetbyId(int intId) {
        this.xssActualSheet = this.xssActualBook.getSheetAt(intId);
        return xssActualSheet;
    }

    public void ReadnShowFile() {
        try {
            FileInputStream file = new FileInputStream(new File(this.strUri));

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
                System.out.println("");
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
