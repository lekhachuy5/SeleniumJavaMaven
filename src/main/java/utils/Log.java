package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {
    private static final Logger Log = (Logger) LogManager.getLogger();
    public Log(){}

    public static void startTestCase(String testCaseName){
        Log.info(testCaseName+": Start Test Case");
        Thread.currentThread().setName("testCaseName");
    }

    public static void endTestCase(String testCaseName){
        Log.info(testCaseName+": End Test Case");
    }

    public static void info(String message){
        Log.info(message);
    }
    public static void warn(String message){
        Log.info(message);
    }   public static void error(String message){
        Log.info(message);
    }   public static void fatal(String message){
        Log.info(message);
    }   public static void debug(String message){
        Log.info(message);
    }   public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw,true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
