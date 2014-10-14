package org.lamsfoundation.lams.admin;


import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;

public class ConfigurationSettings {

	
	private static WebDriver driver = null;
	
    @Test
    public void validateSysadminPage() {
    	
    	// Hit sysadmin menu
    	
    	driver.get(LamsConstants.ADMIN_MENU_URL);
    	assertEquals("Maintain LAMS", driver.getTitle());
    	
    }
    
    @Test 
    public void changeSettings() {
    	
    	// Test data
    	String dateText = "2014-11-21";
    	String falseText = "false";
    	
    	// ids
    	String idLanguagePackDate = "DictionaryDateCreated";
    	String idEnforceEmail = "UserValidationEmail";
    	String idEnforceNames = "UserValidationFirstLastName";
    	String idEnforceUsername = "UserValidationUsername";
    	    	
    	// Hit sysadmin menu
    	  	
    	driver.get(LamsConstants.ADMIN_MENU_URL);
    	driver.findElement(By.linkText("Edit configuration settings")).click();
    	assertEquals("Edit configuration settings", driver.getTitle());
    	
    	// Change the date for the "Language pack install date"
    	WebElement element = driver.findElement(By.id(idLanguagePackDate));
    	element.clear();
    	element.sendKeys(dateText);
    	
    	// Change "Enforce properly formatted emails" to false	
    	WebElement selectEmail = driver.findElement(By.id(idEnforceEmail));
    	Select selectEmailEnforce = new Select(selectEmail);
    	selectEmailEnforce.selectByValue(falseText);
    	
    	// Change "Enforce first and last name validation" to false
    	WebElement selectName = driver.findElement(By.id(idEnforceNames));
    	Select selectNameEnforce = new Select(selectName);
    	selectNameEnforce.selectByValue(falseText);
    	
    	// Change "Enforce username validation" to false
    	WebElement selectUsername = driver.findElement(By.id(idEnforceUsername));
    	Select selectUsernameEnforce = new Select(selectUsername);
    	selectUsernameEnforce.selectByValue(falseText);
    	
    	// Submit changes
    	driver.findElement(By.id("saveButton")).click();
    	
    	// Now we check if the changes did take place by going to Edit config settings again.
    	
    	driver.findElement(By.linkText("Edit configuration settings")).click();
    	
    	String savedDate = driver.findElement(By.id(idLanguagePackDate)).getAttribute("value");
 	
    	// Assert change of Language date    	
    	assertEquals(dateText, savedDate);
    	
    	// Assert change of Email enforcement to false
    	assertEquals(falseText, getSelectedText(idEnforceEmail).trim());
    	
    	//Assert change of Names enforcement to false
    	assertEquals(falseText, getSelectedText(idEnforceNames).trim());
    	
    	//Assert change of Usernames enforcement to false
    	assertEquals(falseText, getSelectedText(idEnforceUsername).trim());
    }
    
    private String getSelectedText(String id) {
    	
    	WebElement selectTag = driver.findElement(By.id(id));
    	Select select = new Select(selectTag);
    	    	
		return select.getFirstSelectedOption().getText();
		
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
    	LamsUtil.logout(driver);
        driver.quit();
    }    
    

}
