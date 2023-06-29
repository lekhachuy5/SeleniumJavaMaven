package base;


import common.CucumberContext;
import common.EnvironmentSetup;
import common.ScenarioLogger;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.hu.Ha;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class BaseSetup {
    public static WebDriver driver;
    public static boolean reopenBrowser = false;
    public static boolean login = true;
    public static CucumberContext context;
    public static Browser browser = new Browser();
    public static Scenario scenario;
    public static Properties properties;
    public static boolean shutdownAll = false;

    public static EnvironmentSetup environmentSetup;

    public BaseSetup() {
    }

    @Before(order = 1)
    public void setInitialDriver(Scenario scenario) throws Exception {
        System.out.println("Loading...");
        if (properties == null) {
            this.initialProperties();
            reopenBrowser = !StringUtils.isEmpty(this.getProperties("driver.reOpenForEveryTest")) && BooleanUtils.toBoolean(this.getProperties("driver.reOpenForEveryTest"));
        }
        context = new CucumberContext();
        this.setContextConfig();
        BaseSetup.scenario = scenario;
        context.setScenario(scenario);
        if (!shutdownAll) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (driver != null) {
                    try {
                        driver.close();
                        driver.quit();
                    } catch (NoSuchSessionException nsse) {
                        System.out.println("Session is closed");
                    }
                }
            }));
            shutdownAll = true;
        }
        if (!reopenBrowser && driver != null) {
            driver.manage().deleteAllCookies();
            driver.get(environmentSetup.getWebUrl());
            context.setEdriver(driver);
        } else {
            this.setUpDriver();
            driver.manage().deleteAllCookies();
            context.setEdriver(driver);
        }
    }

    public void setContextConfig() throws Exception {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("username", environmentSetup.getUsername());
        context.getTestDataContext().put("config", hashMap);
    }

    private void setUpDriver() throws Exception {
        try {
            String browserName = environmentSetup.getBrowserType();
            driver = browser.setLocalDriver(browserName);
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(20L, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(20L, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            try {
                driver.get(environmentSetup.getWebUrl());
            } catch (WebDriverException we) {
                if (we.getMessage().contains("-32000")) {
                    System.out.println("inside timeout is blocked");
                    if (!driver.getCurrentUrl().equals(environmentSetup.getWebUrl()))
                        driver.get(environmentSetup.getWebUrl());
                }
            }
        } catch (TimeoutException te) {
            this.setUpDriver();
        }

    }

    private void initialProperties() throws Exception {
        if (properties == null) {
            properties = new Properties();
            properties.load(new FileInputStream("src/config.properties"));
        }
        if (environmentSetup == null) {
            environmentSetup = new EnvironmentSetup();
            environmentSetup.setWebUrl(this.getProperties("data.url"));
            environmentSetup.setUsername(this.getProperties("data.password"));
            environmentSetup.setPassword(this.getProperties("data.username"));
            environmentSetup.setDbType(this.getProperties("database.jdbcType"));
            environmentSetup.setDbConnection(this.getProperties("database.jdbcConnection"));
            environmentSetup.setBrowserType(this.getProperties("driver.type"));
            environmentSetup.setHeadless(!StringUtils.isEmpty(this.getProperties("driver.headless")) && BooleanUtils.toBoolean(this.getProperties("driver.headless")));
            environmentSetup.setScreenshotPath(this.getProperties("data.screentshot.path"));
            environmentSetup.setDownloadPath(this.getProperties("driver.download.path"));

        }
    }

    private String getProperties(String name) throws Exception {
        return StringUtils.isNotEmpty(System.getProperty(name)) ? System.getProperty(name) : properties.getProperty(name);
    }

    @After(order = 1)
    public void tearDown() throws Exception {
        try {
            if (scenario.isFailed()) ScenarioLogger.addStepLog("Fails url is " + driver.getCurrentUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reopenBrowser) driver.quit();
            } catch (Exception e) {
                ScenarioLogger.addScenarioLog("Exceptions...." + e.getMessage());
            }
        }
        driver.quit();
    }
}
