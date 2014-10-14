package org.lamsfoundation.lams.admin;

import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;
import java.math.BigInteger;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.admin.util.*;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class Signup {

	private static WebDriver driver = null;	
	private SecureRandom random = new SecureRandom();

	// Test data
	private String courseName = "Course " + (new BigInteger(25, random).toString(64));
	private String courseKey = "73Hi54sdxt029sJ";
	private String courseCode = "cf" + courseName.substring(courseName.length() -3);
	
	// Testing user data 
	private String 	username = "randomstriker"+ (new BigInteger(25, random).toString(64));
	String password = "Nn40V1eaSIwNz5E";
	
	@Test(description="Creates a signup page for Playpen course")
	public void createSignupPage() {

		// Test data
		String description  = "Signup for course " + courseName + " via test framework"; 

		// Create a random course
		AdminUtil.createCourse(driver,courseName,courseCode,description,true,true,true,true);
				
		// Open Sysadmin menu
		driver.get(LamsConstants.ADMIN_MENU_URL);
		Assert.assertEquals("Maintain LAMS", driver.getTitle());

		// Open Signup pages
		driver.findElement(By.linkText("Signup pages")).click();
		Assert.assertEquals("Signup pages", driver.getTitle());

		// Click on Add signup page
		driver.findElement(By.linkText("Add a new signup page")).click();
		Assert.assertEquals("Signup pages", driver.getTitle());

		// Fills up form

		driver.findElement(By.name("courseKey")).sendKeys(courseKey);
		driver.findElement(By.name("confirmCourseKey")).sendKeys(courseKey);
		driver.findElement(By.name("blurb")).clear();
		driver.findElement(By.name("blurb")).sendKeys(description);
		driver.findElement(By.name("context")).sendKeys(courseCode.toLowerCase());
		// Select Playpen from course list
		WebElement selectCourse = driver.findElement(By.name("organisationId"));
		Select selectCourseTag = new Select(selectCourse);
		selectCourseTag.selectByVisibleText(courseName);
		
		// Submit
		driver.findElement(By.id("submitButton")).click();
		
		// Check that the course is now displaying
		Assert.assertTrue(driver.findElements(By.xpath("//*[contains(text(), '"+ courseName +"')]")).size() > 0);
		
	}

	@Test(description="Test to signup student", dependsOnMethods={"createSignupPage"})
	public void signUpStudent() {
		
		// test data
		String firstName = "Random";
		String lastName = "Striker";
		String email = (firstName + "." + lastName + "@lams.com").toLowerCase();
				
		// first let's logout
		LamsUtil.logout(driver);

		// Not hiw the signup page
		driver.get(LamsConstants.SIGNUP_URL + courseCode.toLowerCase());

		// Click on signup tab
		driver.findElement(By.id("tabs-1")).click();
		
		// Populate form
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("confirmPassword")).sendKeys(password);
		driver.findElement(By.name("firstName")).sendKeys(firstName);
		driver.findElement(By.name("lastName")).sendKeys(lastName);
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("confirmEmail")).sendKeys(email);
		driver.findElement(By.name("courseKey")).sendKeys(courseKey);
	
		// Submit
		driver.findElement(By.name("submit")).click();
		
		// Check if Login now string shows, if it is create assertion
		Assert.assertTrue((driver.findElements(By.linkText("Login now")).size() > 0), "User created successfully");

		
	}

	@Test(description="Test to signup student", dependsOnMethods={"signUpStudent"})
	public void loginStudent() {
		
		//Login with the new details
		LamsUtil.loginAs(driver, username, password);
			
		// deal with new user pop-up
		WebDriverWait wait = new WebDriverWait(driver, 5 /*timeout in seconds*/);
		if (wait.until(ExpectedConditions.alertIsPresent())==null) {
		       System.out.println("alert was not present");
		} else {
		       System.out.println("alert was present");
				Alert alert=driver.switchTo().alert();
				alert.accept();
		}

		// Check that the tag with the coursename exists
		Boolean existsCourseName = (driver.findElements(By.linkText(courseName)).size() > 0);
		Assert.assertTrue(existsCourseName, "Student logged in successfully and in correct class");
		
		
		
	}
	
	@BeforeClass
	public void beforeClass() {

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Login
		AdminUtil.loginAsSysadmin(driver);
		

	}

	@AfterClass
	public void afterClass() {

		LamsUtil.logout(driver);
		driver.quit();

	}

}
