package utils;

import base.BaseSetup;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBUtils {
    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet result = null;
    private static int resultRowCount = 0;
    private static List<String> outputs = new ArrayList();

    public final static  Logger Log = (Logger)LogManager.getLogger();

    public DBUtils() {
    }

    public static void initializeConnection() throws Exception {
        Class.forName(BaseSetup.environmentSetup.getDbType());
        connection = DriverManager.getConnection(BaseSetup.environmentSetup.getDbConnection());
        statement = connection.createStatement();
    }

    public static void executeQuery(String query) throws Exception {
        initializeConnection();

        try {
            result = statement.executeQuery(query);
            connection.commit();
        } finally {
            closeConnection();
        }

    }

    public static ResultSet executeQueryAndFetchResult(String query) throws Exception {
        initializeConnection();

        try {
            Log.info("executed query:{" + query + "}");
            return statement.executeQuery(query);
        } catch (SQLException var2) {
            throw var2;
        }
    }

    public static void executeBatch(List<String> queryList) throws Exception {
        initializeConnection();
        statement = connection.createStatement(1005, 1008);
        connection.setAutoCommit(false);
        Iterator var1 = queryList.iterator();

        while(var1.hasNext()) {
            String query = (String)var1.next();
            statement.addBatch(query);
        }

        try {
            statement.executeBatch();
            connection.commit();
            Log.info("Executed queries in the batch:{" + queryList.toString() + "}");
        } finally {
            closeConnection();
        }

    }

    public static void execute(String query) throws Exception {
        initializeConnection();

        try {
            statement.execute(query);
            connection.commit();
            Log.info("executed query:{" + query + "}");
        } finally {
            closeConnection();
        }

    }

    public static void executeUpdateQuery(String query) throws Exception {
        initializeConnection();

        try {
            statement.executeUpdate(query);
            connection.commit();
            Log.info("executed query:{" + query + "}");
        } finally {
            closeConnection();
        }

    }

    public static void executeUpdateQueryThrowErrorIfAny(String query) throws Exception {
        initializeConnection();

        try {
            statement.executeUpdate(query);
            connection.commit();
            Log.info("executed query:{" + query + "}");
        } finally {
            closeConnection();
        }

    }

    public static String getValueFromFirstRecord(String query, String fieldName) throws Exception {
        initializeConnection();

        String str;
        try {
            ResultSet rs = null;
            rs = statement.executeQuery(query);
            if (rs.next()) {
                str = rs.getString(fieldName).trim();
                rs.close();
                String var4 = str;
                return var4;
            }

            str = null;
        } finally {
            closeConnection();
        }

        return str;
    }

    public static String getValueFromNthRecord(String query, String fieldName, int occurrence) throws Exception {
        initializeConnection();

        String str;
        try {
            ResultSet rs = null;

            for(rs = statement.executeQuery(query); occurrence > 1; --occurrence) {
                rs.next();
            }

            if (rs.next()) {
                str = rs.getString(fieldName);
                rs.close();
                String var5 = str;
                return var5;
            }

            str = null;
        } finally {
            closeConnection();
        }

        return str;
    }

    public static HashMap<String, String> getFirstRecord(String query) throws Exception {
        initializeConnection();

        HashMap data;
        try {
            ResultSet rs = null;
            rs = statement.executeQuery(query);
            if (rs.next()) {
                data = new HashMap();
                ResultSetMetaData rsmd = rs.getMetaData();

                for(int j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), rs.getString(rsmd.getColumnName(j)) != null ? rs.getString(rsmd.getColumnName(j)).trim() : null);
                }

                rs.close();
                HashMap var8 = data;
                return var8;
            }

            data = null;
        } finally {
            closeConnection();
        }

        return data;
    }

    public static HashMap<String, String> getFirstRow(String query) throws Exception {
        initializeConnection();

        HashMap var8;
        try {
            ResultSet rs = null;
            rs = statement.executeQuery(query);
            HashMap<String, String> data = new HashMap();
            ResultSetMetaData rsmd = rs.getMetaData();
            int j;
            if (!rs.next()) {
                for(j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), null);
                }
            } else {
                for(j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), rs.getString(rsmd.getColumnName(j)));
                }
            }

            rs.close();
            var8 = data;
        } finally {
            closeConnection();
        }

        return var8;
    }

    public static HashMap<String, String> getNthRecord(String query, int occurrence) throws Exception {
        initializeConnection();

        try {
            ResultSet rs = null;

            for(rs = statement.executeQuery(query); occurrence > 1; --occurrence) {
                rs.next();
            }

            HashMap data;
            if (!rs.next()) {
                data = null;
                return data;
            } else {
                data = new HashMap();
                ResultSetMetaData rsmd = rs.getMetaData();

                for(int j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), rs.getString(rsmd.getColumnName(j)));
                }

                rs.close();
                HashMap var9 = data;
                return var9;
            }
        } finally {
            closeConnection();
        }
    }

    public static HashMap<String, String> getNthRow(String query, int occurrence) throws Exception {
        initializeConnection();

        try {
            ResultSet rs = null;

            for(rs = statement.executeQuery(query); occurrence > 1; --occurrence) {
                rs.next();
            }

            HashMap<String, String> data = new HashMap();
            ResultSetMetaData rsmd = rs.getMetaData();
            int j;
            if (!rs.next()) {
                for(j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), null);
                }
            } else {
                for(j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), rs.getString(rsmd.getColumnName(j)));
                }
            }

            rs.close();
            HashMap var9 = data;
            return var9;
        } finally {
            closeConnection();
        }
    }

    public static Map<String, List<String>> getAllRecords(String query) throws Exception {
        initializeConnection();

        HashMap data;
        try {
            PreparedStatement statementLocal = null;
            ResultSet rs = null;
            statementLocal = connection.prepareStatement(query, 1004, 1007);
            rs = statementLocal.executeQuery();
            if (rs.next()) {
                data = new HashMap();
                ResultSetMetaData rsmd = rs.getMetaData();
                List<List<String>> recordsList = new ArrayList();

                int j;
                for(j = 1; j <= rsmd.getColumnCount(); ++j) {
                    ArrayList celldataList = new ArrayList();

                    do {
                        celldataList.add(rs.getString(rsmd.getColumnName(j)));
                    } while(rs.next());

                    recordsList.add(celldataList);
                    rs.first();
                }

                for(j = 1; j <= rsmd.getColumnCount(); ++j) {
                    data.put(rsmd.getColumnName(j), recordsList.get(j - 1));
                }

                statementLocal.close();
                rs.close();
                HashMap var11 = data;
                return var11;
            }

            data = null;
        } finally {
            closeConnection();
        }

        return data;
    }

    public static Object getListOfRecords(String query) throws Exception {
        initializeConnection();
        List<List<Map<String, String>>> resultSetsData = new ArrayList();
        PreparedStatement statementLocal = null;

        try {
            ResultSet rs = null;
            statementLocal = connection.prepareStatement(query, 1004, 1007);
            boolean results = statementLocal.execute();

            while(true) {
                ArrayList records;
                if (!results) {
                    if (statementLocal.getUpdateCount() == -1) {
                        records = (ArrayList) resultSetsData;
                        return records;
                    }

                    results = statementLocal.getMoreResults();
                } else {
                    rs = statementLocal.getResultSet();
                    if (!rs.next()) {
                        rs.close();
                        results = statementLocal.getMoreResults();
                    } else {
                        records = new ArrayList();
                        ResultSetMetaData rsmd = rs.getMetaData();

                        do {
                            Map<String, String> data = new HashMap();

                            for(int j = 1; j <= rsmd.getColumnCount(); ++j) {
                                data.put(rsmd.getColumnName(j), rs.getString(rsmd.getColumnName(j)) != null ? rs.getString(rsmd.getColumnName(j)).trim() : rs.getString(rsmd.getColumnName(j)));
                            }

                            records.add(data);
                        } while(rs.next());

                        rs.close();
                        results = statementLocal.getMoreResults();
                        if (!results && resultSetsData.size() == 0) {
                            statementLocal.close();
                            ArrayList var12 = records;
                            return var12;
                        }

                        resultSetsData.add(records);
                    }
                }
            }
        } finally {
            if (statementLocal != null) {
                statementLocal.close();
            }

            closeConnection();
        }
    }

    public static Map<String, List<String>> getAllRecordsFromStoreProcedure(String functionNameWithArgs) throws Exception {
        initializeConnection();

        try {
            CallableStatement statementLocal = connection.prepareCall(functionNameWithArgs, 1004, 1007);
            ResultSet rs = null;
            HashMap<String, List<String>> data = new HashMap();
            boolean results = statementLocal.execute();
            int count = 1;

            HashMap var13;
            while(results) {
                rs = statementLocal.getResultSet();
                if (!rs.next()) {
                    var13 = data;
                    return var13;
                }

                if (count == 1) {
                    ++count;
                    rs.close();
                    results = statementLocal.getMoreResults();
                } else {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    List<List<String>> recordsList = new ArrayList();

                    int j;
                    for(j = 1; j <= rsmd.getColumnCount() && !StringUtils.equals(rsmd.getColumnName(j), "number_of_rows"); ++j) {
                        ArrayList celldataList = new ArrayList();

                        do {
                            celldataList.add(rs.getString(rsmd.getColumnName(j)));
                        } while(rs.next());

                        recordsList.add(celldataList);
                        rs.first();
                    }

                    for(j = 1; j <= rsmd.getColumnCount() && !StringUtils.equals(rsmd.getColumnName(j), "number_of_rows"); ++j) {
                        data.put(rsmd.getColumnName(j), recordsList.get(j - 1));
                    }

                    rs.close();
                    results = statementLocal.getMoreResults();
                }
            }

            statementLocal.close();
            rs.close();
            var13 = data;
            return var13;
        } finally {
            closeConnection();
        }
    }

    public static void closeConnection() throws Exception {
        try {
            if (result != null) {
                result.close();
                Log.info("Oracle driver connection's resultset closed");
            }

            if (statement != null) {
                statement.close();
                Log.info("Oracle driver connection's statement closed");
            }

            if (connection != null) {
                connection.close();
                Log.info("Oracle driver connection is closed.");
            }

        } catch (SQLException var1) {
            throw var1;
        }
    }

    public static String getValuesFromRecord(String query, String fieldName1, String fieldName2, String fieldName3) throws Exception {
        initializeConnection();

        String var10;
        try {
            ResultSet rs = null;
            rs = statement.executeQuery(query);
            String str;
            if (!rs.next()) {
                str = null;
                return str;
            }

            String str1 = rs.getString(fieldName1).trim();
            String str2 = rs.getString(fieldName2).trim();
            String str3 = rs.getString(fieldName3).trim();
            rs.close();
            str = str1 + "," + str2 + "," + str3;
            var10 = str;
        } finally {
            closeConnection();
        }

        return var10;
    }
}
