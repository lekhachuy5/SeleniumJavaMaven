package TestPackage;

import org.checkerframework.checker.units.qual.C;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import pageObject.CommonPageObject;
import pageObject.DataField;
import pageObject.PageObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;


public class TestCla extends CommonPageObject{
    WebDriver driver;
    @FindBy(how = How.XPATH, using = "//*[@id=\"user-name\"]")
    public WebElement username;
    @FindBy(how = How.XPATH, using = "//*[@id=\"password\"]")
    public WebElement password;
    @FindBy(how = How.XPATH, using = "//*[@id=\"login-button\"]")
    public WebElement btnlogin;
    public DataField dataField;
    public TestCla() throws Exception {
        super(supDriver);
    }
//    public TestCla(WebDriver driver) {
//        super(driver);
//    }


    @Test
    public void checkClick() throws Exception {
        dataField = new DataField("src/main/java/TestPackage/test.xlsx");
        int rowCount = dataField.getRowCount(0);
        System.out.println(rowCount);
        sendKeys(username, dataField.getData(1,1));
        sendKeys(password, dataField.getData(1,2));
        dataField.write("test",1,4);
        waitUntilElementIsVisible(btnlogin);
      click(btnlogin);
    }
    @After
    public void tearDown() throws Exception{
        dataField.closeWorkBook();
        closeWeb();
    }

}
