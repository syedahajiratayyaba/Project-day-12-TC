package com.ibm.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.pages.UserPage;
import com.ibm.utilities.DBUtil;
import com.ibm.utilities.ExcelReader;
import com.ibm.utilities.ExcelUtil;
import com.ibm.utilities.PropertiesFileHandler;

public class BaseTest extends ExcelReader{
	WebDriverWait wait;
	WebDriver driver;	
	
    @Test()
    
    public void testcase12() throws InterruptedException, IOException, SQLException{
    	
    	FileInputStream file = new FileInputStream("./TestData/data.properties");
    	Properties prop = new Properties();
    	prop.load(file);
    	String url = prop.getProperty("url");
    	String username = prop.getProperty("user");
    	String password = prop.getProperty("password");
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 60);
		
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Login login = new Login(driver, wait);
		driver.get(url);
		
		login.enterEmailAddress(username);
		login.enterPassword(password);
		login.clickOnLogin();
		
		WebElement marketingEle=driver.findElement(By.linkText("Marketing"));
		marketingEle.click();
		
		WebElement pushNotificationEle=driver.findElement(By.linkText("Push Notification"));
		pushNotificationEle.click();

		Thread.sleep(3000);
		
		WebElement newNotificationEle=driver.findElement(By.cssSelector("i.fa.fa-plus"));
		newNotificationEle.click();
		
		WebElement nameEle=driver.findElement(By.name("name"));
		nameEle.sendKeys("Test_Syeda_TC12");
		
		WebElement messageEle=driver.findElement(By.name("message"));
		messageEle.sendKeys("Message for the newly added notification");
		
		WebElement imageEle=driver.findElement(By.xpath("//*[@id=\"page-wrapper\"]/div/form/div[2]/div/div/div[2]/div[3]/span"));
		imageEle.click();
		
		String winHandleBefore = driver.getWindowHandle();
		
		WebElement frame=driver.switchTo().activeElement();
		
		frame.sendKeys("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
		//codeEle.sendKeys("C:/Users/Public/Pictures/Sample Pictures/Chrysanthemum.jpg");
		Thread.sleep(3000);
		driver.switchTo().window(winHandleBefore);
		
		WebElement saveEle=driver.findElement(By.cssSelector("i.fa.fa-save"));
		saveEle.click();
		Thread.sleep(2000);
		
		//Assertion in the admin page
		String successMsg=login.AddWeightClassSuccMsg().trim();
		Assert.assertEquals(successMsg,"Success: You have successfully sent push notification to your app!"); 
		
		System.out.println("Push Notification added success message in the admin page");

		
		Thread.sleep(2000);
		String exp="Test_Syeda_TC12";
		String act= DBUtil.singleDataQuery("SELECT name from as_pushnotification where name=\"Test_Syeda_TC12\"");
		
		//validation of database
		Assert.assertEquals(act,exp);
		Reporter.log("Assertion on phone number change present in database");
		WebElement logOutEle=driver.findElement(By.cssSelector("i.fa.fa-sign-out"));
		logOutEle.click();
		
		
    }

}