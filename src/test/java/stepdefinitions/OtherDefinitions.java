package stepdefinitions;

import base.BaseSetup;
import common.CucumberContext;
import common.DataField;
import common.TestCaseAPI;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pageObject.CommonPageObject;
import pageObject.OtherPage;
import utils.ExcelUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherDefinitions {

    public WebDriver driver;
    public Scenario scenario;
    ExcelUtils dataPool;
    DataField dataField;
    CucumberContext context;
    OtherPage otherPage;

    public OtherDefinitions() throws Exception {
        driver = BaseSetup.driver;
        this.scenario = BaseSetup.scenario;
        this.context = BaseSetup.context;
        this.dataField = context.getDataField();
        this.otherPage = new OtherPage(driver, dataField);
    }



    @When("^Enter the Username and Password$")
    public void enter_the_Username_and_Password() throws Throwable {
        otherPage.printExcelData();
    }

}
