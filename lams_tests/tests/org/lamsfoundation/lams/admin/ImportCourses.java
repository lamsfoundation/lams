package org.lamsfoundation.lams.admin;

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ImportCourses {

	private static WebDriver driver = null;

	@Test(description = "Create courses using excel file")
	public void importCourses() {

		String lastCourseName = "Topics In Middle Class Latino Drama";

		driver.get(LamsConstants.ADMIN_MENU_URL);
		Assert.assertEquals("Maintain LAMS", driver.getTitle());

		// Click import courses
		driver.findElement(By.linkText("Import courses")).click();
		Assert.assertEquals("Import courses", driver.getTitle());

		// Upload courses files
		
		WebElement upload = driver.findElement(By.name("file"));
		String filePath = LamsConstants.RESOURCES_PATH + "lams_groups_template.xls";
		upload.sendKeys(filePath);
		driver.findElement(By.id("importButton")).click();

		// Verify that the last course was created
		
		Boolean courseExists = ((driver.findElements(By.xpath("//th[contains(text(), '" + lastCourseName +"')]")).size() > 0));

		// if course exists in page, then we are good
		Assert.assertTrue(courseExists, "The " + lastCourseName + " course is not listed as imported. Something went wrong");


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
