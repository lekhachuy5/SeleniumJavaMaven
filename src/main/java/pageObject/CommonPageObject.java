package pageObject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.BeforeClass;
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

import javax.xml.xpath.XPath;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CommonPageObject extends PageObject {
    @FindBy(how = How.XPATH, using = "//div[@class='sc-124al1g-2 dwOYCh']//button[@class='sc-124al1g-0 jCsgpZ']")
    public WebElement button;
    public static WebDriver edriver;
    public static WebDriver supDriver = PageObject.edriver;
    public static String webUrl = "";
    public boolean setUpIsDone = false;

    public CommonPageObject(WebDriver driver) throws Exception {
        System.out.println(setUpIsDone);
        if(!setUpIsDone) {
            PageFactory.initElements(edriver, this);
            setDriver(edriver);
            setUpIsDone = true;
        }
    }
    public CommonPageObject(WebDriver driver, String st) throws Exception {
        this(driver);
    }

    @BeforeClass
    public static void setInitialDriver() throws Exception {
        System.out.println(1);
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
            edriver = new ChromeDriver(options);
        } else if (driverType.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(Boolean.parseBoolean(prop.getProperty("driver.headless")));
            edriver = new FirefoxDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            options.setHeadless(Boolean.parseBoolean(prop.getProperty("driver.headless")));
            edriver = new EdgeDriver(options);
        }
        edriver.get(webUrl);

    }

    public void closeWeb() throws Exception {
        edriver.close();
    }

    public void sendKeys(WebElement Element) throws Exception{
        waitUntilElementIsVisible(Element);
        sendKeys(Element);
    }
    public void sendKeys(String xpath) throws Exception{
        waitUntilElementIsVisible(xpath);
        sendKeys(xpath);
    }
}