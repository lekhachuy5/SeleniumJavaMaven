package pageObject;

import base.BaseSetup;
import common.DataField;
import common.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class CommonPageObject extends PageObject {
    public static WebDriver edriver;
    public static DataField datafield;
    @FindBy(how = How.XPATH, using = "//div[@class='sc-124al1g-2 dwOYCh']//button[@class='sc-124al1g-0 jCsgpZ']")
    public WebElement button;
    public CommonPageObject(WebDriver driver) throws Exception {
        edriver = driver;
        PageFactory.initElements(edriver, this);
        setDriver(edriver);
    }

    public CommonPageObject(WebDriver driver, DataField dataField) throws Exception {
        this(driver);
        this.datafield = dataField;
    }


    public void closeWeb() throws Exception {
        edriver.close();
    }

    public void sendKeys(WebElement Element) throws Exception {
        waitUntilElementIsVisible(Element);
        sendKeys(Element);
    }

    public void sendKeys(String xpath) throws Exception {
        waitUntilElementIsVisible(xpath);
        sendKeys(xpath);
    }

}