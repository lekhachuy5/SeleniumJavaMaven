package common;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;
import utils.CommonUtils;

public class VerifyUI extends PageObject {
    private static Logger logger = LoggerFactory.getLogger(VerifyUI.class);
    private String[] elementsToVerify;
    private String[] elementsNotToVerify;
    private CucumberContext context;
    private boolean suppressDefaultTextOnUI;
    private int rowCount = -1;
    private String verifyKeyWord = "VerifyUI";
    private String actionDeleteKeyword = "Action_Delete";

    public VerifyUI(CucumberContext context) throws Exception {
        this.context = context;
        TestCaseAPI.onAfterTestCase(context);
        setDriver(this.context.edriver);
    }

    public VerifyUI(CucumberContext context, boolean doExecuteOnAfterLifeCycleQueries) throws Exception {
        this.context = context;
        if (doExecuteOnAfterLifeCycleQueries) {
            TestCaseAPI.onAfterTestCase(context);
        }

        setDriver(this.context.edriver);
    }

    public VerifyUI(CucumberContext context, String queryNo) throws Exception {
        this.context = context;
        this.verifyKeyWord = this.verifyKeyWord + queryNo;
        this.actionDeleteKeyword = this.actionDeleteKeyword + queryNo;
        TestCaseAPI.onAfterTestCase(context, queryNo);
    }

    public void setSuppressDefaultTextOnUI(boolean suppressDefaultTextOnUI) {
        this.suppressDefaultTextOnUI = suppressDefaultTextOnUI;
    }

    public void setElementsToVerify(String[] elementsToVerify) {
        this.elementsToVerify = elementsToVerify;
    }

    public void setElementsNotToVerify(String[] elementsNotToVerify) {
        this.elementsNotToVerify = elementsNotToVerify;
    }

    public void compareUIElements() throws Exception {
        String dbValue = "";
        String uiValue = "";
        SoftAssert softAssert = new SoftAssert();
        Iterator var4 = this.context.getDataField().getCache().entrySet().iterator();

        while(true) {
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

                if (StringUtils.containsIgnoreCase((CharSequence)entry.getKey(), "totalRow")) {
                    uiValue = this.getUIValue(0, (String)entry.getKey(), dbValue);
                    if (StringUtils.contains(uiValue, " record(s) found")) {
                        uiValue = uiValue.split(" ")[0];
                    }

                    dbValue = this.getDBValue(0, (String)entry.getValue());
                    softAssert.assertEquals(uiValue.trim(), dbValue.trim(), "UI and DB values are not matching for :" + (String)entry.getKey() + " ui value :" + uiValue.trim() + " - db value:" + dbValue.trim());
                    ScenarioLogger.addStepLog("UI and DB values are matched for " + (String)entry.getKey() + ":" + uiValue.trim() + ", " + dbValue.trim());
                } else {
                    this.setRowCount();

                    for(int i = 0; i < this.rowCount; ++i) {
                        dbValue = this.getDBValue(i, (String)entry.getValue());
                        uiValue = this.getUIValue(i, (String)entry.getKey(), dbValue);
                        if (StringUtils.contains((CharSequence)entry.getKey(), this.actionDeleteKeyword + ".")) {
                            softAssert.assertNotEquals(uiValue, dbValue, "UI element with value " + dbValue + " is not deleted UI");
                            ScenarioLogger.addStepLog("UI element with value " + dbValue + " is deleted on UI");
                        } else {
                            softAssert.assertEquals(uiValue.trim(), dbValue.trim(), "UI and DB values are not matching for :" + (String)entry.getKey() + " ui value :" + uiValue.trim() + " - db value:" + dbValue.trim());
                            ScenarioLogger.addStepLog("UI and DB values are matched for " + (String)entry.getKey() + ":" + uiValue.trim() + ", " + dbValue.trim());
                        }

                        if (!StringUtils.contains((String)entry.getValue(), "[i]")) {
                            break;
                        }

                        uiValue = "";
                        dbValue = "";
                    }
                }
            }
        }
    }

    private Object getField(Object classObj, String fieldName) throws Exception {
        return classObj.getClass().getField(fieldName).get(classObj);
    }

    private String getDBValue(int index, String entryValue) throws Exception {
        String dbValue = "";
        dbValue = CommonUtils.replaceDynamicData(entryValue);
        dbValue = ContextFunction.replaceContextFunctions(dbValue, this.context.getTestDataContext(), index);
        if (StringUtils.equalsIgnoreCase(dbValue, "Null")) {
            dbValue = "";
        }

        return dbValue;
    }

    private String getUIValue(int index, String testDataName, String dbValue) throws Exception {
        String fieldName = StringUtils.split(testDataName, ".")[StringUtils.split(testDataName, ".").length - 1].trim();
        Object uiElements = this.getField(this.context.getVerifyPageObj(), fieldName);
        List<Integer> tracedList = new ArrayList();
        List<WebElement> webEles = null;
        String uiValue = "";
        if (uiElements instanceof String) {
            uiElements = ContextFunction.replaceContextFunctions(uiElements.toString(), this.context.getTestDataContext(), index);
            uiElements = StringUtils.replace(uiElements.toString(), "'null'", "''");
            String[] xpaths = ((String)uiElements).split("@OR");
            String[] var10 = xpaths;
            int var11 = xpaths.length;

            for(int var12 = 0; var12 < var11; ++var12) {
                String xpath = var10[var12];
                if (this.isElementExistWithMinimumImplicitWait(xpath, 150)) {
                    webEles = this.getElements(xpath);
                    break;
                }

                if (this.isElementExistWithMinimumImplicitWait(StringUtils.replace(xpath, " ", " "), 150)) {
                    xpath = StringUtils.replace(xpath, " ", " ");
                    webEles = this.getElements(xpath);
                    break;
                }
            }
        } else if (uiElements instanceof List) {
            webEles = (List)uiElements;
        } else if (uiElements instanceof WebElement) {
            webEles = Arrays.asList((WebElement)uiElements);
            System.out.println("Web Element path " + (WebElement)uiElements);
        }

        if (webEles == null) {
            return uiValue;
        } else {
            for(int j = 0; j < webEles.size(); ++j) {
                if (webEles.size() == 1 || !tracedList.contains(j)) {
                    uiValue = this.retrieveElementValueBasedOnType((WebElement)webEles.get(j));
                    if (this.suppressDefaultTextOnUI) {
                        uiValue = StringUtils.replace(uiValue, "(default)", "");
                    }

                    uiValue = StringUtils.replace(uiValue, " ", " ");
                    uiValue = StringUtils.replace(uiValue, "…", "...");
                    if (StringUtils.contains(uiValue, ",") && StringUtils.contains(uiValue, ",")) {
                        if (this.matchSeperatedValues(uiValue, dbValue)) {
                            tracedList.add(j);
                            return uiValue;
                        }
                    } else if (StringUtils.equals(uiValue, dbValue)) {
                        tracedList.add(j);
                        return uiValue;
                    }
                }
            }

            return uiValue;
        }
    }

    private boolean matchSeperatedValues(String uiValue, String dbValue) throws Exception {
        String[] uiValues = uiValue.split(",");
        String[] dbValues = dbValue.split(",");
        if (uiValues.length == dbValues.length) {
            String[] var5 = uiValues;
            int var6 = uiValues.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String value = var5[var7];
                if (!ArrayUtils.contains(dbValues, value)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void setRowCount() throws Exception {
        if (this.rowCount == -1) {
            this.rowCount = StringUtils.isNotEmpty(this.context.getDataField().getData("VerifyUI.totalRow")) ? Math.min(Integer.parseInt(this.context.getDataField().getData("VerifyUI.totalRow")), 10) : 1;
        }

    }

    public void setRowCount(int rowCount) throws Exception {
        this.rowCount = Math.min(rowCount, 10);
    }
}
