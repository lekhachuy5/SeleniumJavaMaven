package common;


;
import java.util.Iterator;
import java.util.Map.Entry;

import base.BaseSetup;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;
import utils.CommonUtils;

public class VerifyDB {
    private static Logger logger = LoggerFactory.getLogger(VerifyDB.class);
    private String[] elementsToVerify;
    private String[] elementsNotToVerify;
    private CucumberContext context;
    private String verifyKeyWord = "VerifyDB";
    private String resultKey = "onAfterTestCaseQueryResult";
    private String actionDeleteKeyword = "Action_Delete";
    private int rowCount = -1;

    public VerifyDB(CucumberContext context) throws Exception {
        this.context = context;
        TestCaseAPI.onAfterTestCase(context);
    }

    public VerifyDB(CucumberContext context, String queryNo) throws Exception {
        this.context = context;
        this.verifyKeyWord = this.verifyKeyWord + queryNo;
        this.resultKey = this.resultKey + queryNo;
        this.actionDeleteKeyword = this.actionDeleteKeyword + queryNo;
        TestCaseAPI.onAfterTestCase(context, queryNo);
    }

    public VerifyDB(CucumberContext context, boolean doExecuteOnAfterLifeCycleQueries) throws Exception {
        this.context = context;
        if (doExecuteOnAfterLifeCycleQueries) {
            TestCaseAPI.onAfterTestCase(context);
        }

    }

    public void setElementsToVerify(String[] elementsToVerify) {
        this.elementsToVerify = elementsToVerify;
    }

    public void setElementsNotToVerify(String[] elementsNotToVerify) {
        this.elementsNotToVerify = elementsNotToVerify;
    }

    public void compareDB() throws Exception {
        String expectedValue = "";
        String actualValue = "";
        SoftAssert softAssert = new SoftAssert();
        Iterator var4 = this.context.getDataField().getCache().entrySet().iterator();

        while(true) {
            Entry entry;
            do {
                do {
                    do {
                        do {
                            if (!var4.hasNext()) {
                                softAssert.assertAll();
                                return;
                            }

                            entry = (Entry)var4.next();
                        } while(!StringUtils.contains((CharSequence)entry.getKey(), this.verifyKeyWord + "."));
                    } while(this.elementsToVerify != null && !ArrayUtils.contains(this.elementsToVerify, ((String)entry.getKey()).trim()));
                } while(this.elementsNotToVerify != null && ArrayUtils.contains(this.elementsNotToVerify, ((String)entry.getKey()).trim()));
            } while(!StringUtils.isNotEmpty((String)entry.getValue()));

            this.setRowCount();

            for(int i = 0; i < this.rowCount; ++i) {
                expectedValue = (String)entry.getValue();
                if (StringUtils.equalsIgnoreCase(expectedValue, "Null")) {
                    expectedValue = "";
                }

                String actualKey = "${__Context(" + this.resultKey + "." + StringUtils.substring((String)entry.getKey(), StringUtils.indexOf((CharSequence)entry.getKey(), "["), StringUtils.lastIndexOf((CharSequence)entry.getKey(), "]") + 1) + "." + StringUtils.split((String)entry.getKey(), ".")[StringUtils.split((String)entry.getKey(), ".").length - 1] + ")}";
                actualValue = ContextFunction.replaceContextFunctions(StringUtils.replace(actualKey, "[i]", "[" + String.valueOf(i) + "]"), this.context.getTestDataContext());
                if (actualValue == null) {
                    actualValue = "";
                }

                expectedValue = CommonUtils.replaceDynamicData(expectedValue);
                if (StringUtils.startsWith(expectedValue, "${__")) {
                    expectedValue = StringUtils.replace(expectedValue, "[i]", "[" + String.valueOf(i) + "]");
                    expectedValue = ContextFunction.replaceContextFunctions(expectedValue, this.context.getTestDataContext());
                    expectedValue = BaseSetup.context.getExecuteTestData().getReplacementValue(expectedValue, this.context.getSqlStatements(), this.context);
                }

                if (StringUtils.contains((CharSequence)entry.getKey(), this.actionDeleteKeyword + ".")) {
                    softAssert.assertNotEquals(actualValue, expectedValue, "DB record with value " + expectedValue + " is not deleted");
                    ScenarioLogger.addStepLog("DB record with value " + expectedValue + " is deleted");
                } else {
                    softAssert.assertEquals(actualValue, expectedValue, "DB and UI values are not matching for " + (String)entry.getKey() + ": " + actualValue + ", " + expectedValue);
                    ScenarioLogger.addStepLog("DB and UI values are matched for " + (String)entry.getKey() + ": " + actualValue + ", " + expectedValue);
                }

                if (!StringUtils.contains((String)entry.getValue(), "[i]")) {
                    break;
                }

                actualValue = "";
                expectedValue = "";
            }
        }
    }

    private void setRowCount() throws Exception {
        if (this.rowCount == -1) {
            this.rowCount = 1;
        }

    }

    public void setRowCount(int rowCount) throws Exception {
        this.rowCount = rowCount;
    }
}