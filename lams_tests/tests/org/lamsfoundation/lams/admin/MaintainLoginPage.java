package org.lamsfoundation.lams.admin;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MaintainLoginPage {

	private static WebDriver driver = null;
    
    @Test
    public void changeLoginPage() {
    	
    	// Test data
        String insertHeader = "<h1>Welcome to LAMS</h1>";
        String insertText   = "Text inserted by drone test";
        
    	driver.get(LamsConstants.ADMIN_MENU_URL);
    	
    	// Click on maintain login page
    	driver.findElement(By.linkText("Maintain login page")).click();
    	assertEquals("Maintain login page", driver.getTitle());
    	
    	// Upload logo image
    	WebElement upload = driver.findElement(By.name("logo"));
    	upload.sendKeys(LamsConstants.RESOURCES_PATH + "lams_login.gif");
    	
    	// Insert text to CKEditor via javascript 
    	((JavascriptExecutor) 
    			driver).executeScript("CKEDITOR.instances['news'].setData('" + insertHeader + "<div>" + insertText +"</div>');"); 
    	
    	driver.findElement(By.id("saveButton")).click();
    	
    	// Now logout and check that the image and text are correct
    	closeBrowser();
    	openBrowser();
    	AdminUtil.logout(driver);
    	
    	String expectedText = driver.findElement(By.xpath("//*[@id=\"login-left-col\"]/div")).getText().trim();
    	assertEquals(expectedText, insertText);
    	
    }
    
    @BeforeClass
    public static void openBrowser(){
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    	// Login
        AdminUtil.loginAsSysadmin(driver);
    	
       } 
    
    @AfterClass
    public static void closeBrowser(){
    	
    	AdminUtil.logout(driver);
        driver.quit();
    }
        
	
}
