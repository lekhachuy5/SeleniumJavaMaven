package utils;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class CommonUtils {
    public CommonUtils() {
    }

    public static String handleStr(String str) throws Exception {
        if (str.contains("//")) {
            str = str.replace("//", "");
        }

        if (str.contains("(")) {
            str = str.replace("(", "");
        }

        if (str.contains(")")) {
            str = str.replace(")", "");
        }

        return str;
    }

    public static final String generateRandomNumber(long len) {
        if (len > 18L) {
            throw new IllegalStateException("To many digits");
        } else {
            long tLen = (long)Math.pow(10.0D, (double)(len - 1L)) * 9L;
            long number = (long)(Math.random() * (double)tLen) + (long)Math.pow(10.0D, (double)(len - 1L)) * 1L;
            String tVal = number + "";
            if ((long)tVal.length() != len) {
                throw new IllegalStateException("The random number '" + tVal + "' is not '" + len + "' digits");
            } else {
                return tVal;
            }
        }
    }

    public static int getRandomNumber(int numberOfDigits) {
        return numberOfDigits < 1 ? 0 : (new Random()).nextInt(9 * (int)Math.pow(10.0D, (double)(numberOfDigits - 1)) - 1) + (int)Math.pow(10.0D, (double)(numberOfDigits - 1));
    }

    public static HashMap<String, String> getRandomDate(int startYear, int EndYear) {
        Random random = new Random();
        int minDay = (int)LocalDate.of(startYear, 1, 1).toEpochDay();
        int maxDay = (int)LocalDate.of(EndYear, 1, 1).toEpochDay();

        long randomDay;
        try {
            randomDay = (long)(minDay + random.nextInt(maxDay - minDay));
        } catch (IllegalArgumentException var12) {
            randomDay = (long)(minDay + random.nextInt(-(maxDay - minDay)));
        }

        LocalDate randomBirth = LocalDate.ofEpochDay(randomDay);
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM");
        DateTimeFormatter YearFormat = DateTimeFormatter.ofPattern("uuuu");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d");
        HashMap<String, String> dob = new HashMap();
        dob.put("month", randomBirth.format(monthFormat).equals("Sep") ? "Sept" : randomBirth.format(monthFormat));
        dob.put("year", randomBirth.format(YearFormat));
        dob.put("day", randomBirth.format(dateFormat));
        return dob;
    }

    public static String replaceTimestamp(String text) throws Exception {
        if (text.matches("(?s).*\\{TIMESTAMP_.*\\}.*")) {
            int startIndex = text.indexOf("{TIMESTAMP_") + "{TIMESTAMP_".length();
            int endIndex = text.indexOf("}", startIndex);
            String format = text.substring(startIndex, endIndex);
            return text.replaceAll("\\{TIMESTAMP_" + format + "\\}", getCurrentDate(format));
        } else if (StringUtils.contains(text, "{TIMESTAMP}")) {
            return StringUtils.replace(text, "{TIMESTAMP}", (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime()));
        } else {
            return StringUtils.contains(text, "${timestamp}") ? StringUtils.replace(text, "${timestamp}", (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime())) : text;
        }
    }

    public static String replaceRandomNumber(String text) throws Exception {
        if (text.matches("(?s).*\\{NUMBER_.*\\}.*")) {
            int startIndex = text.indexOf("{NUMBER_") + "{NUMBER_".length();
            int endIndex = text.indexOf("}", startIndex);
            String length = text.substring(startIndex, endIndex);
            return text.replaceAll("\\{NUMBER_" + length + "\\}", generateRandomNumber((long)Integer.parseInt(length)));
        } else {
            return text;
        }
    }

    public static String replaceRandomText(String text) throws Exception {
        if (text.matches("(?s).*\\{TEXT_.*\\}.*")) {
            int startIndex = text.indexOf("{TEXT_") + "{TEXT_".length();
            int endIndex = text.indexOf("}", startIndex);
            String length = text.substring(startIndex, endIndex);
            return text.replaceAll("\\{TEXT_" + length + "\\}", generateRandomString(Integer.parseInt(length)));
        } else {
            return text;
        }
    }

    public static String replaceDynamicData(String text) throws Exception {
        text = replaceTimestamp(text);
        text = replaceRandomNumber(text);
        text = replaceRandomText(text);
        return text;
    }

    public static String getDateForComingDay(int day, String format) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(5) >= day) {
            calendar.add(2, 1);
        }

        calendar.set(5, day);
        Date date = calendar.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        return sdf1.format(date);
    }

    public static String addNumberOfBusinessDaysToCurrentDate(int days) {
        new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        int i = 0;

        while(i < days) {
            calendar.add(5, 1);
            if (calendar.get(7) >= 2 && calendar.get(7) <= 6) {
                ++i;
            }
        }

        Date date = calendar.getTime();
        return s.format(date);
    }

    public static String getPreviousDate(int days, String format) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format);
        cal.add(5, -days);
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(cal.getTime());
    }

    public static int getDifferenceOfYears(String format, String pastDate) throws ParseException {
        Calendar currentDate = Calendar.getInstance();
        Calendar fromDate = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format);
        fromDate.setTime(dateFormat.parse(pastDate));
        return currentDate.get(1) - fromDate.get(1);
    }

    public static String getUTCCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(cal.getTime());
    }

    public static String formatNumberString(String format, String value) {
        return StringUtils.isNotEmpty(value) ? String.format(format, new BigDecimal(value)) : String.format(format, new BigDecimal("0"));
    }

    public static String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        String dayName = calendar.getDisplayName(2, 1, Locale.US);
        return dayName.toUpperCase();
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(1);
    }

    public static String getMonthNumber(String monthName) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputFormat.parse(monthName));
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM");
        return outputFormat.format(cal.getTime());
    }

    public static boolean verifyElementAbsent(WebDriver driver, By by) throws Exception {
        try {
            driver.findElement(by);
            return false;
        } catch (NoSuchElementException var3) {
            return true;
        }
    }

    public static boolean verifyElementPresent(WebDriver driver, By by) throws Exception {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException var3) {
            return false;
        }
    }

    public static void takeScreenshot(WebDriver driver, String fileName) throws Exception {
        try {
            WebDriver augmentedDriver = (new Augmenter()).augment(driver);
            File screenshot = (File)((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
            String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(Calendar.getInstance().getTime());
            File file = new File(".." + File.separator + "cweb" + File.separator + "TestResult" + File.separator + fileName + "_" + timeStamp + ".png");
            FileUtils.copyFile(screenshot, file);
        } catch (Exception var6) {
            throw var6;
        }
    }

    public static boolean verifyBiggerThanNumber(String str, int number) throws Exception {
        try {
            if (str != null && !str.isEmpty() && !str.endsWith("a") && !str.endsWith("A")) {
                int iParseFromStr = Integer.parseInt(str);
                return iParseFromStr > number;
            } else {
                return false;
            }
        } catch (Exception var3) {
            return false;
        }
    }

    public static int parseInt(String str) throws Exception {
        try {
            if (str != null && !str.isEmpty() && !str.endsWith("a") && !str.endsWith("A")) {
                int iParseFromStr = Integer.parseInt(str);
                return iParseFromStr;
            } else {
                return 0;
            }
        } catch (Exception var2) {
            return 0;
        }
    }

    public static String removeEndStr(String fullStr, String endStr) throws Exception {
        try {
            String processedStr = fullStr;
            if (fullStr != null && endStr != null && fullStr.endsWith(endStr)) {
                int len = fullStr.length() - endStr.length();
                processedStr = fullStr.substring(0, len);
            }

            return processedStr;
        } catch (Exception var4) {
            return fullStr;
        }
    }

    public static String convertFloatToStringWithFormat(float fValue) throws Exception {
        try {
            return String.format("%.2f", fValue);
        } catch (Exception var2) {
            return null;
        }
    }

    public static String getLastSubString(String fullStr) throws Exception {
        try {
            String lastSubString = "";
            String[] array = null;
            int length;
            if (fullStr != null) {
                array = fullStr.split("\\.");
            }

            length = array.length;
            if (length > 0) {
                lastSubString = array[length - 1];
            }

            return lastSubString;
        } catch (Exception var4) {
            return fullStr;
        }
    }

    public static Integer convertStringToNum(String str) throws Exception {
        NumberFormat format = NumberFormat.getNumberInstance();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat)format).setParseBigDecimal(true);
        }

        return format.parse(str.replaceAll("[^\\d.,]", "")).intValue();
    }

    public static Double convertStringToDoubleNum(String str) throws Exception {
        NumberFormat format = NumberFormat.getNumberInstance();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat)format).setParseBigDecimal(true);
        }

        return format.parse(str.replaceAll("[^\\d.,]", "")).doubleValue();
    }

    public static Integer truncateDecimals(String str) throws Exception {
        Double val = Double.parseDouble(str);
        return val.intValue();
    }

    public static final String generateNINORandomNumber(long len) {
        if (len > 18L) {
            throw new IllegalStateException("To many digits");
        } else {
            long tLen = (long)Math.pow(10.0D, (double)(len - 1L)) * 9L;
            long number = (long)(Math.random() * (double)tLen) + (long)Math.pow(10.0D, (double)(len - 1L)) * 1L;
            String tVal = number + "";
            tVal = "AA" + tVal + "A";
            return tVal;
        }
    }

    public static final String generateRandomString(int len) {
        String RandomCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        while(sb.length() < len) {
            int index = (int)(rnd.nextFloat() * (float)RandomCHARS.length());
            sb.append(RandomCHARS.charAt(index));
        }

        String randomStr = sb.toString();
        return randomStr;
    }
}