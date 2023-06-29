package common;

import utils.DBUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class TestCaseAPI {
    private static Logger logger = LoggerFactory.getLogger(TestCaseAPI.class);

    public TestCaseAPI() {
    }

    public static void onBeforeTestCase(CucumberContext context) throws Exception {
        System.out.println("onBeforeTestCaseQuery");
        runSqlAndPopulateContext("onBeforeTestCaseQuery", "onBeforeTestCaseQueryResult", context);
        System.out.println("onBeforeTestCaseQuery_");
        runSqlAndPopulateContext("onBeforeTestCaseQuery_" + context.getTestCaseId(), "onBeforeTestCaseQueryResult", context);
    }

    public static void onAfterTestCase(CucumberContext context) throws Exception {
        System.out.println("onAfterTestCaseQuery");
        runSqlAndPopulateContext("onAfterTestCaseQuery", "onAfterTestCaseQueryResult", context);
        System.out.println("onAfterTestCaseQuery_");
        runSqlAndPopulateContext("onAfterTestCaseQuery_" + context.getTestCaseId(), "onAfterTestCaseQueryResult", context);
    }

    public static void onAfterTestCase(CucumberContext context, String queryNo) throws Exception {
        runSqlAndPopulateContext("onAfterTestCaseQuery" + queryNo, "onAfterTestCaseQueryResult" + queryNo, context);
        runSqlAndPopulateContext("onAfterTestCaseQuery" + queryNo + "_" + context.getTestCaseId(), "onAfterTestCaseQueryResult" + queryNo, context);
    }

    public static void runSqlAndPopulateContext(String key, String resultKey, CucumberContext context) throws Exception {
        Map<String, String> sqlStatements = null;
        sqlStatements = loadSqlStatements((CucumberContext)context, (String)null);
        if (sqlStatements != null && sqlStatements.size() > 0) {
            context.setSqlStatements(sqlStatements);
            if (sqlStatements.containsKey(key)) {
                String query = ContextFunction.replaceContextFunctions((String)sqlStatements.get(key)
                        , context.getTestDataContext());
                System.out.println(query);
                context.addFinalQuery(key, query);
                Object result = DBUtils.getListOfRecords(query);
                System.out.println("\n Result sets ::" + result);
                context.getTestDataContext().put(resultKey, result);
            }
        }

    }

    public static Map<String, String> loadSqlStatements(CucumberContext context, String fileNamePrefix) throws Exception {
        String level = "feature";
        String sqlStatementFileName = getSqlStatementFileName(context, fileNamePrefix, level);
        Map<String, String> sqlStatements = loadSqlStatements(sqlStatementFileName, context);
        logger.debug("Loaded {} SQL statements from file {}", sqlStatements.size(), sqlStatementFileName);
        level = "scenario";
        sqlStatementFileName = getSqlStatementFileName(context, fileNamePrefix, level);
        Map<String, String> scenarioSqlStmts = loadSqlStatements(sqlStatementFileName, context);
        logger.debug("Loaded {} SQL statements from file {}", scenarioSqlStmts.size(), sqlStatementFileName);
        sqlStatements.putAll(scenarioSqlStmts);
        return sqlStatements;
    }

    public static String getSqlStatementFileName(CucumberContext context, String fileNamePrefix, String level) {
        String fileName;
        if (fileNamePrefix == null) {
            if (level.equals("feature")) {
                fileName = getFeatureDirectory(context);
                fileName = fileName + "/" + fileName.split("/")[2] + ".sql";
            } else {
                fileName = getFilePrefix(context);
                fileName = fileName + ".sql";
            }
        } else {
            fileName = getFeatureDirectory(context) + "/" + fileNamePrefix + ".sql";
        }

        return fileName;
    }

    public static String getFeatureDirectory(CucumberContext context) {
        return "/features/" + context.getScenario().getId().split(";")[0].replace("-", "_");
    }

    public static String getFilePrefix(CucumberContext context) {
        String filePath = StringUtils.substringAfter(context.getScenario().getUri().toString(), "features/");
        filePath = StringUtils.substringBeforeLast(filePath, "/");
        filePath = filePath + "/" + StringUtils.substringBefore(context.getScenario().getName(), "-").trim().replace(" ", "_");
        filePath = filePath + "/" + StringUtils.substringBefore(context.getScenario().getName(), "-").trim().replace(" ", "_");
        return "/features/" + StringUtils.lowerCase(filePath);
    }

    public static String getTestCaseDetails(CucumberContext context) {
        int lineNum = context.getScenario().getLine();
        String testcaseNameId = getFilePrefix(context);
        String[] scenarioName = testcaseNameId.split("/");
        int count = countOccurences(testcaseNameId, '/', 0);
        testcaseNameId = scenarioName[count - 2].toUpperCase() + "_" + scenarioName[count - 1] + "_" + lineNum;
        return testcaseNameId;
    }

    public static int countOccurences(String someString, char searchedChar, int index) {
        if (index >= someString.length()) {
            return 0;
        } else {
            int count = someString.charAt(index) == searchedChar ? 1 : 0;
            return count + countOccurences(someString, searchedChar, index + 1);
        }
    }

    public static Map<String, String> loadSqlStatements(String fileName, CucumberContext context) throws Exception {
        HashMap sqlStatements = new HashMap();

        try {
            ClassPathResource cpr = new ClassPathResource(fileName);
            if (cpr.exists()) {
                File sqlFile = cpr.getFile();
                context.getScenario().attach(IOUtils.toByteArray(new FileInputStream(sqlFile)), "application/sql",fileName);
                String sqlStr = IOUtils.toString(new FileReader(sqlFile));
                String[] sqlArr = sqlStr.split("--");
                String[] var8 = sqlArr;
                int var9 = sqlArr.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    String statement = var8[var10].trim();
                    if (statement.length() > 0) {
                        String key = statement.substring(0, statement.indexOf(10)).trim();
                        sqlStatements.put(key, statement.substring(statement.indexOf(10) + 1));
                    }
                }

                logger.debug("Loaded {} SQL statements from file {}", sqlStatements.size(), fileName);
            }

            return sqlStatements;
        } catch (IOException var12) {
            throw var12;
        }
    }
}