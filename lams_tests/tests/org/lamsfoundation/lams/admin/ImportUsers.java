package org.lamsfoundation.lams.admin;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class ImportUsers {

	private static WebDriver driver = null;
	
    @Test (description = "Validates title in sysadmin menu page")
    public void validateSysadminPage() {
    	
    	// Hit sysadmin menu
    	
    	driver.get(LamsConstants.ADMIN_MENU_URL);
    	assertEquals(driver.getTitle(), "Maintain LAMS", "Wrong page");
    	
    }
    
    @Test (description = "Imports users from excel file",  dependsOnMethods={"validateSysadminPage"})
    public void importUsersFromFile() {
    	
    	// Click import users
    	driver.findElement(By.linkText("Import users")).click();
    	assertEquals("User management", driver.getTitle());
    	
    	
    	// Upload users files
    	WebElement upload = driver.findElement(By.name("file"));
    	upload.sendKeys(LamsConstants.RESOURCES_PATH + "lams_users_template.xls");
    	driver.findElement(By.id("importButton")).click();
    	
    	// Verify all users were created successfully
    	String result = driver.findElement(By.xpath("//*[@id=\"content\"]/p[1]")).getText();    	
    	
    	assertEquals(result, "375 users were created successfully." ,"Expected users failed!");
	
    }
    
    @Test (description = "Import users' roles from excel file", dependsOnMethods="importUsersFromFile")
    public void importUsersRolesFromFile() {
    	
    	// Sysadmin menu
    	driver.get(LamsConstants.ADMIN_MENU_URL);
    	
    	// Click import users
    	driver.findElement(By.linkText("Import users")).click();
    	assertEquals("User management", driver.getTitle());	
    	
    	// Upload roles import
    	WebElement upload = driver.findElement(By.name("file"));
    	upload.sendKeys(LamsConstants.RESOURCES_PATH + "lams_roles_template.xls");
    	driver.findElement(By.id("importButton")).click();
    	
    	// Verify all users were added to courses
        String result = driver.findElement(By.xpath("//*[@id=\"content\"]/p[1]")).getText();
        assertEquals("111 users were added to course/subcourse.", result);
    	
    }
    
	/**
	 *    We implement a data provider so we reuse the same test for verifying users
	 *    
	 */
	@DataProvider(name = "VerifyUsers")
	public static Object[][] verifyUsers() {
		return new Object[][] {
				new Object[] { "groupadmin", "Playpen", "Course Admin  Course Manager" }, { "author02", "Playpen", "Author" }, 
				{ "monitor01", "Playpen", "Learner  Monitor" }, { "donitaco", "Playpen", "Learner" }
		};
	}

    
    @Test (description = "Verifies that groupadmin user was created with the correct permissions", dataProvider="VerifyUsers", 
    		dependsOnMethods="importUsersRolesFromFile")
    public void verifyUserRoles(String userLogin, String expectedCourse, String expectedRoles) {
    	
        // Checks if user exists
        Assert.assertTrue(AdminUtil.userExists(driver, userLogin));
        
        //
    	driver.get(LamsConstants.ADMIN_MENU_URL);
    	
    	// Click find users
    	driver.findElement(By.linkText("Find users")).click();
    	assertEquals(driver.getTitle(), "User management", "Wrong page");
    	
    	// Search for groupadmin
    	driver.findElement(By.name("term")).sendKeys(userLogin);
    	driver.findElement(By.name("term")).sendKeys(Keys.RETURN);
    	
    	// Verify that user has been created with the appropriate permissions
    	driver.findElement(By.linkText("Edit")).click();
    	String course = driver.findElement(By.xpath("//*[@id=\"content\"]/form/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td[1]")).getText().trim();
    	String roles = driver.findElement(By.xpath("//*[@id=\"content\"]/form/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td[2]/small")).getText().trim();
    	
    	assertEquals(course, expectedCourse, "Course is not correct");
    	assertEquals(roles, expectedRoles, "Roles aren't correc for " + userLogin);
    	   	
    	
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
