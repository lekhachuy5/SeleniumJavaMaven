package common;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import base.BaseSetup;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import utils.CommonUtils;
import utils.ExcelUtils;

public class DataField {
    private static final Logger log = (Logger) LogManager.getLogger();
    public String testCaseName;
    public ExcelUtils excelTestResult;
    public ExcelUtils excelDataPool;
    private HashMap<String, Object> cache;
    private HashMap<String, Object> originalCache;
    private HashMap<String, String> cacheResult;
    private Properties dbExcelMapping;
    private String testDataColumnPreText;
    CucumberContext context;

    public DataField(ExcelUtils dataPool, ExcelUtils testResult, String strTestCaseName) throws Exception {
        this.testCaseName = null;
        this.excelTestResult = null;
        this.excelDataPool = null;
        this.cache = null;
        this.originalCache = null;
        this.cacheResult = null;
        this.dbExcelMapping = null;
        this.testDataColumnPreText = "";
        this.testCaseName = strTestCaseName;
        this.excelDataPool = dataPool;
        this.excelTestResult = testResult;
        this.cache = new HashMap();
        this.cacheResult = new HashMap();
        int rowCount = dataPool.getRowCount();
        int colNum = dataPool.getColumnNumber(this.testCaseName);

        for (int i = 0; i < rowCount; ++i) {
            String rowName = dataPool.getCellData(i, 0);
            if (!rowName.isEmpty()) {
                String inputData = dataPool.getCellData(i, colNum);
                this.cache.put(rowName, inputData);
            }
        }

    }

    public DataField(ExcelUtils dataPool, String strTestCaseName) throws Exception {
        this.testCaseName = null;
        this.excelTestResult = null;
        this.excelDataPool = null;
        this.cache = null;
        this.originalCache = null;
        this.cacheResult = null;
        this.dbExcelMapping = null;
        this.testDataColumnPreText = "";

        try {
            this.excelDataPool = dataPool;
            this.testCaseName = strTestCaseName;
            this.excelTestResult = null;
            this.cache = new HashMap();
            int rowCount = dataPool.getRowCount();
            int colNum = dataPool.getColumnNumber(this.testCaseName);

            for (int i = 0; i < rowCount; ++i) {
                String rowName = dataPool.getCellData(i, 0);
                if (!rowName.isEmpty()) {
                    String inputData = dataPool.getCellData(i, colNum);
                    this.cache.put(rowName.trim(), inputData.trim());
                }
            }

            this.originalCache = new HashMap(this.cache);
        } finally {
            this.excelDataPool.closeFile();
        }

    }

    public DataField(ExcelUtils dataPool, String strTestCaseName, String dbExcelMappingFileName) throws Exception {
        this(dataPool, strTestCaseName);
        this.loadProperties(dbExcelMappingFileName);
    }

    public void loadProperties(String dbExcelMappingFileName) throws FileNotFoundException, IOException {
        this.dbExcelMapping = new Properties();
        this.dbExcelMapping.load(this.getClass().getClassLoader().getResourceAsStream(dbExcelMappingFileName));
    }

    public String getTestDataColumnPreText() throws Exception {
        return this.testDataColumnPreText;
    }

    public void setTestDataColumnPreText(String testDataColumnPreText) {
        this.testDataColumnPreText = testDataColumnPreText;
    }

    public HashMap<String, Object> getCache() throws Exception {
        Entry entry;
        String cellData;
        for (Iterator var1 = this.cache.entrySet().iterator(); var1.hasNext(); this.putData((String) entry.getKey(), cellData)) {
            entry = (Entry) var1.next();
            cellData = null;
            if (entry.getValue() instanceof String) {
                cellData = CommonUtils.replaceDynamicData((String) entry.getValue());
            }
        }

        return this.cache;
    }

    public HashMap<String, Object> getOriginalCache() throws Exception {
        return this.originalCache;
    }

    public String getData(String rowName) throws Exception {
        String appendedRowName = this.testDataColumnPreText + rowName;
        String cellData = null;

        try {
            if (this.cache.get(appendedRowName) != null && this.cache.get(appendedRowName) instanceof String) {
                cellData = CommonUtils.replaceDynamicData((String) this.cache.get(appendedRowName));
                cellData = this.parseTestData(cellData);
                this.putData(rowName, cellData);
                return cellData;
            } else {
                return cellData;
            }
        } catch (Exception var5) {
            log.error("cannot get data from datapool file for this row name" + appendedRowName, var5);
            throw var5;
        }
    }

    public String getDataForIndex(String rowName, int i) throws Exception {
        String appendedRowName = this.testDataColumnPreText + rowName;
        String cellData = null;

        try {
            if (this.cache.get(appendedRowName) != null && this.cache.get(appendedRowName) instanceof String) {
                cellData = CommonUtils.replaceDynamicData((String) this.cache.get(appendedRowName));
                cellData = this.parseTestData(cellData.replace("[i]", "[" + i + "]"));
                return cellData;
            } else {
                return cellData;
            }
        } catch (Exception var6) {
            log.error("cannot get data from datapool file for this row name" + appendedRowName, var6);
            throw var6;
        }
    }

    public String getData(String rowName, int i) throws Exception {
        rowName = rowName + i;
        String cellData = "";

        try {
            if (this.cache.get(rowName) != null && this.cache.get(rowName) instanceof String) {
                cellData = CommonUtils.replaceDynamicData((String) this.cache.get(rowName));
                this.putData(rowName, cellData);
                return cellData;
            } else {
                return cellData;
            }
        } catch (Exception var5) {
            log.error("cannot get data from datapool file for this row name" + rowName, var5);
            throw var5;
        }
    }

    public Object getObject(String rowName) throws Exception {
        String appendedRowName = this.testDataColumnPreText + rowName;

        try {
            return this.cache.get(appendedRowName) != null ? this.cache.get(appendedRowName) : null;
        } catch (Exception var4) {
            log.error("cannot get data from datapool file for this row name" + appendedRowName, var4);
            throw var4;
        }
    }

    public String parseTestData(String testData) throws Exception {
        if (StringUtils.startsWith(testData, "${__")) {
            testData = BaseSetup.context.getExecuteTestData().getReplacementValue(testData, BaseSetup.context.getSqlStatements(), BaseSetup.context);
            testData = StringUtils.defaultString(testData);
        }

        return testData;
    }

    public void putData(String rowName, Object value) throws Exception {
        String appendedRowName = this.testDataColumnPreText + rowName;

        try {
            this.cache.put(appendedRowName, value);
        } catch (Exception var5) {
            log.error("cannot put data into datapool file for this row name" + appendedRowName, var5);
            throw var5;
        }
    }

    public void removeData(String rowName) throws Exception {
        String appendedRowName = this.testDataColumnPreText + rowName;

        try {
            if (this.cache.get(appendedRowName) != null) {
                this.cache.remove(appendedRowName);
            }

        } catch (Exception var4) {
            log.error("cannot remove data from datapool file for this row name" + appendedRowName, var4);
            throw var4;
        }
    }

    public String getInputValueBasedOnDBColumn(String dbkey, int exceldataindex) throws Exception {
        try {
            if (this.dbExcelMapping.getProperty(dbkey) != null) {
                if (exceldataindex >= this.dbExcelMapping.getProperty(dbkey).split(",").length) {
                    exceldataindex = 0;
                }

                if (StringUtils.isNotEmpty(this.getData(this.dbExcelMapping.getProperty(dbkey).split(",")[exceldataindex].trim()))) {
                    return this.getData(this.dbExcelMapping.getProperty(dbkey).split(",")[exceldataindex].trim());
                }
            }

            return "";
        } catch (Exception var4) {
            log.error("get data failed, Key: " + this.dbExcelMapping.getProperty(dbkey));
            throw var4;
        }
    }

    public String getInputKeyBasedOnDBColumn(String dbkey, int exceldataindex) throws Exception {
        try {
            if (this.dbExcelMapping.getProperty(dbkey) != null) {
                return exceldataindex >= this.dbExcelMapping.getProperty(dbkey).split(",").length ? "" : this.dbExcelMapping.getProperty(dbkey).split(",")[exceldataindex];
            } else {
                return "";
            }
        } catch (Exception var4) {
            log.error("get input key failed, Key: " + this.dbExcelMapping.getProperty(dbkey));
            throw var4;
        }
    }

    public boolean isRowNameExists(String rowName) throws Exception {
        this.context = BaseSetup.context;
        String filename = TestCaseAPI.getFilePrefix(this.context).replaceFirst("/", "") + ".xlsx";
        ExcelUtils dataPool = new ExcelUtils(this.getClass().getClassLoader().getResource(filename).toURI().getPath(), "Sheet1");
        return dataPool.isRowNameExistsInFile(rowName);
    }


}