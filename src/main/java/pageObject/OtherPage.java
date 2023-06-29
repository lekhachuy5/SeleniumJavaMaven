package pageObject;

import common.DataField;
import org.openqa.selenium.WebDriver;

public class OtherPage extends CommonPageObject{
    public static WebDriver edriver;
    public static DataField datafield;
    public OtherPage(WebDriver driver, DataField dataField) throws Exception {
        super(driver);
        edriver = driver;
        this.datafield = dataField;
    }

    public void printExcelData() throws Exception {
        String data = datafield.getData("User Id");
        String data2 = datafield.getData("Data");
        System.out.println(data2);
        System.out.println(data);
    }
}
