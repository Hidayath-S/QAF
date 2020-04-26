package com.first;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;

public class SeleniumTest {
	
	@QAFDataProvider(dataFile="C://Users//Hidayath//Desktop//Data.xlsx")
	@Test()
	public void test(Map<String ,Object> data){
		String input=data.get("iData").toString();
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Hidayath\\Downloads\\chromedriver.exe");
		//System.setProperty(webdriver.ie.driver", "C:\\Users\\Hidayath\\Downloads\\IEDriverServer.exe");	
			WebDriver driver= new ChromeDriver();
			//driver.get("https://phptravels.com/demo/");
			driver.get("https://www.google.com/");
			
			driver.manage().window().maximize();
			driver.findElement(By.xpath("//*[@id='tsf']/div[2]/div/div[1]/div/div[1]/input")).sendKeys(input);
			driver.findElement(By.xpath("//*[@id='tsf']/div[2]/div/div[2]/div[2]/ul/li[1]/div/div[1]/div/span")).click();
			
			driver.findElement(By.xpath("//*[@id='tsf']/div[2]/div/div[3]/center/input[1]")).click();
			
			//*[@id="tsf"]/div[2]/div/div[2]/div[2]/ul/li[1]/div/div[1]/div/span
			//driver.close();
	}

}
