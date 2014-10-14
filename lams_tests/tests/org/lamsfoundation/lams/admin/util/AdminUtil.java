package org.lamsfoundation.lams.admin.util;

import java.util.List;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class AdminUtil {

	public static void createCourse(WebDriver driver,String courseName, String courseCode, String courseDescription, Boolean enableOrgNotifications, Boolean enableGradebookMonitor, 
			Boolean enableGradebookLearners, Boolean EnableSingleActivityLessons) {
		
		driver.get(LamsConstants.TEST_SERVER_URL + "admin/organisation.do?method=create&typeId=2&parentId=1");
		Assert.assertEquals(driver.getTitle(), "Course/Subcourse entry");

		// Fill out form
		driver.findElement(By.name("name")).sendKeys(courseName);
		driver.findElement(By.name("code")).sendKeys(courseCode);
		driver.findElement(By.name("description")).sendKeys(courseDescription);

		if (enableGradebookMonitor) {
			driver.findElement(By.name("enableGradebookForMonitors")).click();
		}

		if (enableGradebookLearners) {
			driver.findElement(By.name("enableGradebookForLearners")).click();
		}

		if (EnableSingleActivityLessons) {
			driver.findElement(By.name("enableSingleActivityLessons")).click();
		}

		driver.findElement(By.id("saveButton")).click();

	}

	public static Boolean userExists(WebDriver driver, String username) {

		driver.get(LamsConstants.ADMIN_MENU_URL);

		// Click find users
		driver.findElement(By.linkText("Find users")).click();
		Assert.assertEquals("User management", driver.getTitle());

		// Search for username
		driver.findElement(By.name("term")).sendKeys(username);
		driver.findElement(By.name("term")).sendKeys(Keys.RETURN);

		return (driver.findElements(By.xpath("//td[2][contains(text(), '"+ username +"')]")).size() > 0);

	}

	public static String rolesInCourse(WebDriver driver, String username, String courseName) {

		// Go to search interface
		driver.get(LamsConstants.ADMIN_SEARCH_USER_URL);

		// Search for username
		driver.findElement(By.name("term")).sendKeys(username);
		driver.findElement(By.name("term")).sendKeys(Keys.RETURN);
		

		int resultSize = (driver.findElements(By.xpath("//td[2][contains(text(), '"+ username +"')]")).size());

		if  (resultSize == 0) {
			// can't find username		
			return "error: Can't find username";
		} else if (resultSize > 1) {
			// more than one user with this username
			return "error: More than 1 user returned with username " + username;
		}

		// Click on the edit link for the user
		driver.findElement(By.linkText("Edit")).click();

		// Get all the cells in the table 
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"tableRoles\"]/tbody//tr//td"));

		String rolesFound ;
		// iterate the cells to find the correct one and return the roles that are found for the course
		for (int i = 0; i < rows.size(); i++) {

			if (courseName.equals(rows.get(i).getText().trim())) {
				// System.out.println("Roles: " +rows.get(i+1).getText());
				// get roles found
				rolesFound = rows.get(i+1).getText().trim();
				// trim double spaces
				rolesFound = rolesFound.replace("  ", " ");
				// return back
				return rolesFound;
			}

		}

		return "error: No roles for " + username + " in course " + courseName;
	}
	

	public static void loginAsSysadmin(WebDriver driver) {
		// Login
		LamsUtil.loginAs(driver, LamsConstants.SYSADMIN_USER, LamsConstants.SYSADMIN_PASSWD);

	}

	public static boolean createUser(WebDriver driver, String username,
			String password, String firstName, String lastName, String email) {
		
		// Launch create user URL
		driver.get(LamsConstants.ADMIN_CREATE_USER_URL);

		// Populate form with data
		driver.findElement(By.name("login")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("password2")).sendKeys(password);
		driver.findElement(By.name("firstName")).sendKeys(firstName);
		driver.findElement(By.name("lastName")).sendKeys(lastName);
		driver.findElement(By.name("email")).sendKeys(email);
		
		// Submit form
		driver.findElement(By.id("saveButton")).click();
		
		// Let's check that the user was created
		return (userExists(driver, username));
		
	}



}
