package pageObject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CommonPageObject extends PageObject {
    @FindBy(how = How.XPATH, using = "//div[@class='sc-124al1g-2 dwOYCh']//button[@class='sc-124al1g-0 jCsgpZ']")
    public WebElement button;
    public WebDriver edriver;
    public static WebDriver supDriver = PageObject.edriver;
    public String webUrl = "";

    public CommonPageObject(WebDriver driver) throws Exception {
        driver = setInitialDriver();
        this.edriver = driver;
        PageFactory.initElements(driver, this);
        setDriver(edriver);
        edriver.get(webUrl);
    }

    public CommonPageObject(WebDriver driver, String st) throws Exception {
        this(driver);
    }

    public WebDriver setInitialDriver() throws Exception {
        Properties prop = new Properties();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/config.properties"));
            prop.load(reader);
            reader.close();
        } catch (IOException e) {
            throw new FileNotFoundException("File config.properties is not found in the class path");
        }
        webUrl = prop.getProperty("data.url");
        WebDriver driver;
        String driverType = prop.getProperty("driver.type");
        if (driverType.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(Boolean.parseBoolean(prop.getProperty("driver.headless")));
            driver = new ChromeDriver(options);
        } else if (driverType.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(Boolean.parseBoolean(prop.getProperty("driver.headless")));
            driver = new FirefoxDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            options.setHeadless(Boolean.parseBoolean(prop.getProperty("driver.headless")));
            driver = new EdgeDriver(options);
        }
        return driver;
    }

    public void closeWeb() throws Exception {
        edriver.close();
    }

}