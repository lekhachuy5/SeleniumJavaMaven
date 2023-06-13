package TestPackage;

import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import pageObject.CommonPageObject;
import pageObject.PageObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class TestCla extends CommonPageObject{
    WebDriver driver;

    public TestCla() throws Exception {
        super(supDriver);
    }
//    public TestCla(WebDriver driver) {
//        super(driver);
//    }

    @Before
    public void setUp() throws Exception {
        startWeb();
    }
    @Test
    public void checkClick() throws Exception {
        waitUntilElementIsVisible(button);
      click(button);
    }

}
