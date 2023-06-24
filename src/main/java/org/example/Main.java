package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import jdk.jfr.Timespan;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        ChromeDriver driver = new ChromeDriver();
        driver.get("https://computer-database.gatling.io/computers");
//
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
 //
        WebElement btnFilter = driver.findElement(By.xpath("//input[@id='searchsubmit']"));
        wait.until(ExpectedConditions.visibilityOf(btnFilter));
        wait.until(ExpectedConditions.elementToBeClickable(btnFilter));
                btnFilter.click();
    }
}