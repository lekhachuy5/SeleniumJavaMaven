package common;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class CucumberContext {
    private static Logger logger = LoggerFactory.getLogger(CucumberContext.class);
    public WebDriver edriver;
    private ExecuteTestData executeTestData;
    private Scenario scenario;
    private String testCaseId;
    private DataField dataField;
    private Map<String, Object> testDataContext = new HashMap();
    private Map<String, String> sqlStatements;
    private Object verifyPageObj;
    private Map<String, Object> executedQueryAndResult = new HashMap();
    private HashMap<String, String> finalQueries = new HashMap();

    public CucumberContext() {
    }

    public void addFinalQuery(String key, String value) {
        this.finalQueries.put(key, value);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        CucumberContext.logger = logger;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getTestCaseId() {
        return this.testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public Map<String, Object> getTestDataContext() {
        return this.testDataContext;
    }

    public void setTestDataContext(Map<String, Object> testDataContext) {
        this.testDataContext = testDataContext;
    }

    public Map<String, String> getSqlStatements() {
        return this.sqlStatements;
    }

    public void setSqlStatements(Map<String, String> sqlStatements) {
        this.sqlStatements = sqlStatements;
    }

    public DataField getDataField() {
        return this.dataField;
    }

    public void setDataField(DataField dataField) {
        this.dataField = dataField;
    }

    public Object getVerifyPageObj() {
        return this.verifyPageObj;
    }

    public void setVerifyPageObj(Object verifyPageObj) {
        this.verifyPageObj = verifyPageObj;
    }

    public Map<String, Object> getExecutedQueryAndResult() {
        return this.executedQueryAndResult;
    }

    public void setExecutedQueryAndResult(Map<String, Object> executedQueryAndResult) {
        this.executedQueryAndResult = executedQueryAndResult;
    }

    public ExecuteTestData getExecuteTestData() throws Exception {
        return this.executeTestData == null ? new ExecuteTestData() : this.executeTestData;
    }

    public WebDriver getEdriver() {
        return this.edriver;
    }

    public void setEdriver(WebDriver edriver) {
        this.edriver = edriver;
    }

    public void setFinalQueries(HashMap<String, String> finalQueries) {
        this.finalQueries = finalQueries;
    }

    public HashMap<String, String> getFinalQueries() {
        return this.finalQueries;
    }
}