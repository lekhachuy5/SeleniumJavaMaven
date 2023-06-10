package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
//        Properties prop = new Properties();
//        BufferedReader reader;
//        try {
//            reader = new BufferedReader(new FileReader("src/config.properties"));
//            prop.load(reader);
//            reader.close();
//        }catch  (FileNotFoundException e){
//            throw new FileNotFoundException("File config.properties is not found in the class path");
//        }
//        WebDriverManager web = WebDriverManager.chromedriver();
//        web.setup();
//        ChromeDriver dr = new ChromeDriver();
//        String url = prop.getProperty("data.url");
//                dr.get(url);
//                dr.quit();

        ChromeDriver driver = new ChromeDriver();
        driver.get("https://google.com");
//        dr.quit();
        WebElement gmail = driver.findElement(By.xpath("//a[text()='Gmail']"));
        System.out.println(gmail.getText());


    }
}