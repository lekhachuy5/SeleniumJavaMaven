package stepdefinitions;

import common.CucumberContext;
import common.DataField;
import common.TestCaseAPI;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import base.BaseSetup;
import pageObject.CommonPageObject;
import utils.ExcelUtils;

import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonStepDefinitions {

    public WebDriver driver;
    public Scenario scenario;
    ExcelUtils dataPool;
    DataField dataField;
    CucumberContext context;

    public CommonPageObject commonPageObject;

    public CommonStepDefinitions() {
        driver = BaseSetup.driver;
        this.scenario = BaseSetup.scenario;
        context = BaseSetup.context;
    }

    @Given("^scenario executes TC(\\d+) to verify for journey$")
    public void scenario_executes_tc1_to_verify_for_journey(String testCase) throws Throwable {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
        Date date = new Date();
        System.out.println("Current datetime: " + df.format(date));
        context.setTestCaseId("TC"+testCase);
        System.out.println(TestCaseAPI.getFilePrefix(context));
        String fileName = TestCaseAPI.getFilePrefix(context).replaceFirst("/", "") + ".xlsx";
        System.out.println("file name: " + fileName);
        dataPool = new ExcelUtils(getClass().getClassLoader().getResource(fileName).toURI().getPath(),"Sheet1");
        dataField = new DataField(dataPool,"TC"+testCase);
        context.getTestDataContext().put("testCaseData",dataField.getCache());
        context.setDataField(dataField);
        TestCaseAPI.onBeforeTestCase(context);
    }


    @Then("^Reset the credential$")
    public void Reset_the_credential() throws Throwable {
        System.out.println("This step click on the Reset button.");
    }

    @Given("Open the browser then lunch application")
    public void openTheBrowserThenLunchApplication() {
        System.out.println("Open browser");

    }

    @Given("scenario executes <TestCase> for journey$")
    public void scenarioExecutesTestCaseForJourney$() {
    }
}
