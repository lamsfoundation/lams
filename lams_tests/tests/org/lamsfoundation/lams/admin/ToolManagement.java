package org.lamsfoundation.lams.admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class ToolManagement {

	private static WebDriver driver = null;

	@Test (description = "Enables and disables a tool.")
	public void toggleEnableDisableTool() {

		// Test data
		String toolName = "Wiki";

		driver.get(LamsConstants.ADMIN_MENU_URL);

		// Click on tool management link
		driver.findElement(By.linkText("Tool management")).click();
		// Assert that we are in the correct page
		assertEquals("Edit default tool content", driver.getTitle());

		// Get tool state by checking if the disable link for Notebook is in the page. 
		Boolean isEnabled = (driver.findElements(By.id("disable"+toolName)).size() > 0);

		// Now we toggle the tool state 
		if (isEnabled) {
			driver.findElement(By.id("disable"+toolName)).click();
		} 
		
		// Verify disabled new state
		Boolean isDisabled = (driver.findElements(By.id("enable"+toolName)).size() > 0);
		
		assertTrue(isDisabled, "Error: notebook still remains enabled");

		// Now let's make sure that this tool does not show in Author. 
		driver.get(LamsConstants.AUTHOR_FLASHLESS_URL);

		// If we can't find the tool in author then we are good. 
		Boolean isInAuthor = (driver.findElements(By.xpath("//*[contains(text(), '"+ toolName +"')]")).size() > 0);
		// System.out.println("isInAuthor:" +isInAuthor);
		assertTrue(!isInAuthor, "Tool "+ toolName + " is displaying in Author");

		// Revert back to original state
		driver.get(LamsConstants.ADMIN_TOOL_MANAGEMENT_MENU_URL);
		driver.findElement(By.id("enable"+toolName)).click();


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
		// Logout before quitting 
		LamsUtil.logout(driver);
		driver.quit();
	}


}
