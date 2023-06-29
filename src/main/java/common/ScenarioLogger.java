package common;

import base.BaseSetup;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import utils.FileSystem;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScenarioLogger {
    public ScenarioLogger() {
    }

    public static void addStepLog(String logMsg) throws Exception {
        try {
            BaseSetup.scenario.log(logMsg + "\n");
        } catch (NullPointerException var2) {
            BaseSetup.context.getScenario().log(logMsg);
        }

    }

    public static void addScenarioLog(String logMsg) throws Exception {
        try {
            BaseSetup.scenario.log(logMsg + "\n");
        } catch (NullPointerException var2) {
            BaseSetup.context.getScenario().log(logMsg);
        }

    }

    public static void addScreenShot() throws Exception {
        try {
            File sourcePath = (File)((TakesScreenshot)BaseSetup.driver).getScreenshotAs(OutputType.FILE);
            File destinationPath;
            if (StringUtils.isNotEmpty(BaseSetup.environmentSetup.getScreenshotPath())) {
                destinationPath = new File(BaseSetup.environmentSetup.getScreenshotPath() + sourcePath.getName());
            } else {
                destinationPath = new File(System.getProperty("user.dir") + "/target/cucumber-reports/" + sourcePath.getName());
            }

            System.out.println("Source path" + sourcePath);
            System.out.println("destination path" + destinationPath);
            FileUtils.copyFile(sourcePath, destinationPath);
            BaseSetup.scenario.attach(Files.readAllBytes(Paths.get(String.valueOf(sourcePath))), "image/png", "test");
        } catch (NullPointerException var2) {
            byte[] screenshot = (byte[])((TakesScreenshot)BaseSetup.driver).getScreenshotAs(OutputType.BYTES);
            BaseSetup.context.getScenario().attach(screenshot, "image/png", "error");
        }

    }

    public static void takeFullPageScreenShot(CucumberContext context, WebDriver driver) throws Exception {
        String screenShotPath = capture(context, driver);

        try {
            BaseSetup.context.getScenario().attach(Files.readAllBytes(Paths.get(screenShotPath)), "image/png", screenShotPath);
        } catch (NullPointerException var4) {
            BaseSetup.context.getScenario().attach(Files.readAllBytes(Paths.get(screenShotPath)), "image/png", "nullpointer");
        }

    }

    public static String capture(CucumberContext context, WebDriver driver) throws Exception {
        String screenShotName = TestCaseAPI.getTestCaseDetails(context) + ".png";
        Screenshot screenshot = (new AShot()).shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        String destinationPath;
        if (StringUtils.isNotEmpty(BaseSetup.environmentSetup.getScreenshotPath())) {
            if (!FileSystem.isFileExist(BaseSetup.environmentSetup.getScreenshotPath())) {
                (new File(BaseSetup.environmentSetup.getScreenshotPath())).mkdirs();
            }

            destinationPath = String.valueOf(new File(BaseSetup.environmentSetup.getScreenshotPath() + screenShotName));
        } else {
            destinationPath = System.getProperty("user.dir") + "\\target\\cucumber-reports\\" + screenShotName;
        }

        ImageIO.write(screenshot.getImage(), "PNG", new File(destinationPath));
        return destinationPath;
    }
}
