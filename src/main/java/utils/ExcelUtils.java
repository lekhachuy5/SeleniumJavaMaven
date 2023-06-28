package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    FileInputStream excelFile;
    private XSSFSheet excelWSheet;
    private XSSFWorkbook excelWBook;
    private XSSFCell cell = null;
    private XSSFRow row;
    private String filePath;

    public ExcelUtils() {
    }

    public ExcelUtils(String path, String SheetName) {
        try {
            this.excelFile = new FileInputStream(path);
            this.excelWBook = new XSSFWorkbook(this.excelFile);
            this.excelWSheet = this.excelWBook.getSheet(SheetName);
            this.filePath = path;
        } catch (Exception var4) {
            Log.error("Error happened when call constructor function ExcelUtils(String path:" + path + "String SheetName:" + SheetName);
            Log.error(Log.getStackTrace(var4));
        }

    }

    public void closeFile() throws Exception {
        this.excelWBook.close();
        this.excelFile.close();
        this.excelWSheet = null;
        this.excelWBook = null;
        this.excelFile = null;
        System.gc();
    }

    public String getCellData(int RowNum, int ColNum) throws Exception {
        try {
            this.cell = this.excelWSheet.getRow(RowNum).getCell(ColNum);
            this.cell.setCellType(CellType.STRING);
            String CellData = this.cell.getStringCellValue();
            return CellData;
        } catch (Exception var4) {
            return "";
        }
    }

    public int getRowCount() throws Exception {
        try {
            int rowCount = this.excelWSheet.getLastRowNum() + 1;
            return rowCount;
        } catch (Exception var2) {
            return -1;
        }
    }

    public String getCellDatas(String RowName, int colNum) throws Exception {
        try {
            int MaxRowNumber = this.excelWSheet.getLastRowNum();
            int rowNum = 0;

            for(int i = 0; i <= MaxRowNumber; ++i) {
                if (this.excelWSheet.getRow(i).getCell(0) != null && this.excelWSheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(RowName)) {
                    rowNum = i;
                    break;
                }
            }

            this.cell = this.excelWSheet.getRow(rowNum).getCell(colNum);
            this.cell.setCellType(CellType.STRING);
            String CellData = this.cell.getStringCellValue();
            return CellData;
        } catch (Exception var6) {
            return "";
        }
    }

    public Integer getCellData(String RowName, int colNum) throws Exception {
        try {
            int MaxRowNumber = this.excelWSheet.getLastRowNum();
            int rowNum = 0;

            for(int i = 0; i <= MaxRowNumber; ++i) {
                if (this.excelWSheet.getRow(i).getCell(0) != null && this.excelWSheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(RowName)) {
                    rowNum = i;
                    break;
                }
            }

            this.cell = this.excelWSheet.getRow(rowNum).getCell(colNum);
            this.cell.setCellType(CellType.NUMERIC);
            Double CellData = this.cell.getNumericCellValue();
            return CellData.intValue();
        } catch (Exception var6) {
            return -1;
        }
    }

    public String getCellData(String RowName, String ColName) throws Exception {
        int MaxRowNumber = this.excelWSheet.getLastRowNum();
        int MaxColNumber = 100;
        int rowNum = 0;
        int colNum = 0;

        int i;
        for(i = 1; i <= MaxColNumber; ++i) {
            this.cell = this.excelWSheet.getRow(0).getCell(i);
            this.cell.setCellType(CellType.STRING);
            if (this.cell != null && this.cell.toString().trim().equalsIgnoreCase(ColName)) {
                colNum = i;
                break;
            }
        }

        for(i = 1; i <= MaxRowNumber; ++i) {
            this.cell = this.excelWSheet.getRow(i).getCell(0);
            this.cell.setCellType(CellType.STRING);
            if (this.cell != null && this.cell.toString().trim().equalsIgnoreCase(RowName)) {
                rowNum = i;
                break;
            }
        }

        this.cell = this.excelWSheet.getRow(rowNum).getCell(colNum);
        String CellData = "";
        if (this.cell != null) {
            this.cell.setCellType(CellType.STRING);
            CellData = this.cell.getStringCellValue().trim();
        } else {
            CellData = "";
        }

        return CellData;
    }

    public int getColumnNumber(String ColName) {
        try {
            int MaxColNumber = 100;
            int colNum = 0;

            for(int i = 0; i < MaxColNumber; ++i) {
                this.cell = this.excelWSheet.getRow(0).getCell(i);
                if (this.cell != null && this.cell.toString().equalsIgnoreCase(ColName)) {
                    colNum = i;
                    break;
                }
            }

            return colNum;
        } catch (Exception var5) {
            return -1;
        }
    }

    public int getMaxColNumber() throws Exception {
        try {
            int MaxColNumber = this.excelWSheet.getRow(0).getLastCellNum();
            return MaxColNumber;
        } catch (Exception var2) {
            return -1;
        }
    }

    public ArrayList<String[]> getDataSet() throws Exception {
        try {
            int MaxRowNumber = this.excelWSheet.getLastRowNum();
            int MaxColNumber = this.getMaxColNumber();
            ArrayList<String[]> dataset = new ArrayList();
            String[] arr = new String[MaxColNumber];

            for(int i = 0; i < MaxRowNumber; ++i) {
                for(int j = 0; j < MaxColNumber; ++j) {
                    this.cell = this.excelWSheet.getRow(i).getCell(j);
                    if (this.cell == null) {
                        this.cell.setCellValue("");
                    }

                    String str = this.getCellData(i, j);
                    arr[i] = str;
                }

                dataset.add(arr);
            }

            return dataset;
        } catch (Exception var8) {
            return null;
        }
    }

    public String getInputData(int RowNum, String ColName) throws Exception {
        try {
            int ColNum = this.getColumnNumber(ColName);
            String str = this.getCellData(RowNum, ColNum).toString();
            return str;
        } catch (Exception var5) {
            return "";
        }
    }

    public void setCellData(String Result, int RowNum, int ColNum) throws Exception {
        try {
            this.row = this.excelWSheet.getRow(RowNum);
            this.cell = this.row.getCell(ColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (this.cell == null) {
                this.cell = this.row.createCell(ColNum);
                this.cell.setCellValue(Result);
            } else {
                this.cell.setCellValue(Result);
            }

        } catch (Exception var5) {
            throw var5;
        }
    }

    public void setCellData(String Result, String RowName, String ColName) throws Exception {
        int MaxRowNumber = this.excelWSheet.getLastRowNum();
        int MaxColNumber = 100;
        int RowNum = 0;
        int ColNum = 0;

        int i;
        for(i = 0; i <= MaxColNumber; ++i) {
            if (this.excelWSheet.getRow(0).getCell(i).toString().equals(ColName)) {
                ColNum = i;
                break;
            }
        }

        for(i = 0; i <= MaxRowNumber; ++i) {
            if (this.excelWSheet.getRow(i).getCell(0).toString().equals(RowName)) {
                RowNum = i;
                break;
            }
        }

        try {
            this.row = this.excelWSheet.getRow(RowNum);
            this.cell = this.row.getCell(ColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (this.cell == null) {
                this.cell = this.row.createCell(ColNum);
                this.cell.setCellValue(Result);
            } else {
                this.cell.setCellValue(Result);
            }

            FileOutputStream fileOut = new FileOutputStream(this.filePath, false);
            this.excelWBook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception var9) {
            Log.error(Log.getStackTrace(var9));
            throw var9;
        }
    }

    public XSSFSheet readExcelFile(String Path, String FileName, String SheetName) throws Exception {
        try {
            FileInputStream ExcelFile = new FileInputStream(Path + "\\" + FileName);
            this.excelWBook = new XSSFWorkbook(ExcelFile);
            this.excelWSheet = this.excelWBook.getSheet(SheetName);
        } catch (Exception var5) {
            Log.error("Error happened while working on the tools retirement test result sheet");
            Log.error(Log.getStackTrace(var5));
        }

        return this.excelWSheet;
    }

    public void setResultData(String Result, String RowName, int ColNum) throws Exception {
        int MaxRowNumber = this.excelWSheet.getLastRowNum();
        int MaxColNumber = 100;
        int RowNum = 0;

        for(int i = 0; i <= MaxRowNumber; ++i) {
            if (this.excelWSheet.getRow(i).getCell(0) != null && this.excelWSheet.getRow(i).getCell(0).toString().equals(RowName)) {
                RowNum = i;
                break;
            }
        }

        try {
            this.row = this.excelWSheet.getRow(RowNum);
            this.cell = this.row.getCell(ColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (this.cell == null) {
                this.cell = this.row.createCell(ColNum);
                this.cell.setCellValue(Result);
            } else {
                this.cell.setCellValue(Result);
            }

        } catch (Exception var8) {
            Log.error("Exception in writing result value in file");
            Log.error(Log.getStackTrace(var8));
            throw var8;
        }
    }

    public void reEvaluateExcelFormula() {
        this.excelWBook.getCreationHelper().createFormulaEvaluator().clearAllCachedResultValues();
        this.excelWBook.getCreationHelper().createFormulaEvaluator().evaluateAll();
    }

    public Integer getCellDataPercentage(String RowName, int colNum) throws Exception {
        try {
            int MaxRowNumber = this.excelWSheet.getLastRowNum();
            int rowNum = 0;

            for(int i = 0; i <= MaxRowNumber; ++i) {
                if (this.cell != null && this.excelWSheet.getRow(i).getCell(0) != null && this.excelWSheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(RowName)) {
                    rowNum = i;
                    break;
                }
            }

            this.cell = this.excelWSheet.getRow(rowNum).getCell(colNum);
            this.cell.setCellType(CellType.NUMERIC);
            Double CellData = this.cell.getNumericCellValue();
            CellData = CellData * 100.0D;
            return CellData.intValue();
        } catch (Exception var6) {
            return -1;
        }
    }

    public boolean isRowNameExistsInFile(String rowName) {
        try {
            Iterator rows = this.excelWSheet.rowIterator();

            while(rows.hasNext()) {
                XSSFRow row = (XSSFRow)rows.next();
                Iterator cells = row.cellIterator();

                while(cells.hasNext()) {
                    XSSFCell cell = (XSSFCell)cells.next();
                    cell.setCellType(CellType.STRING);
                    String richTextString = cell.getRichStringCellValue().toString();
                    if (richTextString.equalsIgnoreCase(rowName)) {
                        System.out.println("\n Row Name - " + rowName + " exists in the File ");
                        return true;
                    }
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        System.out.println("\n Row Name - " + rowName + " does not exists in the file");
        return false;
    }
}