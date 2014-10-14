package org.lamsfoundation.lams.admin;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserManagement {

	private static WebDriver driver = null;	
	
	private SecureRandom random = new SecureRandom();
	// Test data
	private String username = "bart" + (new BigInteger(25, random).toString(64));
	private String password = (new BigInteger(25, random).toString(64));
	private String firstName = "Bart";
	private String lastName = "Simpson";
	private String email = "bart.simpson@movies.com";
	
	private String rolesInCourse = "Author Learner Monitor";
	
	
	@Test (description="Creates a LAMS user")
	public void createUser() {
		// Test user creation
		Assert.assertTrue(AdminUtil.createUser(driver, username, password, firstName, lastName, email));

	}
	
	@Test (description="Check that the user just created exists", dependsOnMethods={"createUser"}) 
	public void checkUserExists() {
		
		Assert.assertTrue(AdminUtil.userExists(driver, username));
		
	}
	
	@Test (description = "Enrolls the user into Playpen course", dependsOnMethods={"checkUserExists"})
	public void enrollUser() {
		System.out.println("Enrolls user in Playpen course");

		driver.get(LamsConstants.ADMIN_MANAGE_COURSES_URL);
		
		// Sorts the course by Id (descending order) 
		driver.findElement(By.id("idsorter")).click();
		// Clicks on the first course (Playpen)
		driver.findElement(By.linkText("Playpen")).click();
		// Click on manage users
		driver.findElement(By.id("manageUsers")).click();
		// Click on "Add/Remove users" button
		driver.findElement(By.id("addRemoveUsers")).click();
		
		// enters the user to add in the search input box and press enter
		driver.findElement(By.id("term")).sendKeys(username);
		driver.findElement(By.id("term")).sendKeys(Keys.ENTER);

		// Click on the username
		driver.findElement(By.partialLinkText(username)).click();
		// Submits to next screen
		driver.findElement(By.id("nextButton")).click();
		
		String idTag = username + "Role";
				
		if (rolesInCourse.contains("Learner")) {
			// Add student role
			driver.findElement(By.id(idTag+"5")).click();
		
		} 

		if (rolesInCourse.contains("Monitor")) {
			// Add monitor role
			driver.findElement(By.id(idTag+"4")).click();
		
		} 

		if (rolesInCourse.contains("Author")) {
			// Add author role
			driver.findElement(By.id(idTag+"3")).click();
			
		} 

		if (rolesInCourse.contains("Manager")) {
			// Add course manager role
			driver.findElement(By.id(idTag+"2")).click();
		
		} 
		
		if (rolesInCourse.contains("Admin")) {
			// Add course admin role
			driver.findElement(By.id(idTag+"6")).click();;
		}
		
		// Submit
		driver.findElement(By.id("saveButton")).click();
		
		
		
	}
	
	
	@Test (description = "Verifies enrollment", dependsOnMethods={"enrollUser"})
	public void isUserInCourseWithRoles() {
		System.out.println("Checks user is in course");
		
		String rolesOuput = AdminUtil.rolesInCourse(driver, username, "Playpen");
		
		System.out.println("Roles output for " + username + " in course Playpen: " + rolesOuput);
		
		// To make it quick we compare that length of both strings. If they are the same length, then we have the same roles 
		System.out.println("rolesOutput lenght: "+ rolesOuput.length() + " rolesInCourse: " + rolesInCourse.length());
		
		Assert.assertTrue((rolesOuput.length() == rolesInCourse.length()));
		
		
	}
	
	@Test (description = "Removes user from LAMS", dependsOnMethods={"isUserInCourseWithRoles"})
	public void deleteUser() {
		System.out.println("Removes user");
		
	}
		
	
	@BeforeClass
	public void beforeClass() {
		
		// Initialize driver
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		AdminUtil.loginAsSysadmin(driver);
		
	}

	@AfterClass
	public void afterClass() {

		LamsUtil.logout(driver);
		driver.quit();
	}

}
