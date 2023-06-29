package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Browser {
    private static DesiredCapabilities dr = null;
    private static String FIREFOX_BROWSER_NAME = "firefox";
    private static String CHROME_BROWSER_NAME = "chrome";
    private static String EDGE_BROWSER_NAME = "edge";
    private String CurrentDirectory;

    public Browser() {
    }

    public WebDriver setLocalDriver(String browserName) throws Exception {
        this.CurrentDirectory = System.getProperty("user.dir");
        if (browserName.equalsIgnoreCase(FIREFOX_BROWSER_NAME)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            if (BaseSetup.environmentSetup.isHeadless()) {
                firefoxBinary.addCommandLineOptions(new String[]{"--headless"});
            }

            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(firefoxBinary);
            return new FirefoxDriver(firefoxOptions);
        } else if (browserName.equalsIgnoreCase(EDGE_BROWSER_NAME)) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            if (BaseSetup.environmentSetup.isHeadless()) {
                edgeOptions.addArguments(new String[]{"headless"});
            }

            return new EdgeDriver(edgeOptions);
        } else {
            if (System.getProperty("webdriver.chrome.driver") == null) {
                WebDriverManager.chromedriver().setup();
            }

            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(new String[]{"--disable-site-isolation-trials"});
            if (BaseSetup.environmentSetup.isHeadless()) {
                chromeOptions.addArguments(new String[]{"--headless", "start-maximized", "enable-automation", "--no-sandbox", "--disable-dev-shm-usage", "--disable-browser-side-navigation", "--disable-gpu"});
            }

            if (BaseSetup.environmentSetup.getDownloadPath() != null && !BaseSetup.environmentSetup.getDownloadPath().isEmpty()) {
                Map<String, Object> prefs = new HashMap();
                prefs.put("download.default_directory",BaseSetup.environmentSetup.getDownloadPath());
                chromeOptions.setExperimentalOption("prefs", prefs);
                System.out.println(prefs);
            }

            return new ChromeDriver(chromeOptions);
        }
    }
}
