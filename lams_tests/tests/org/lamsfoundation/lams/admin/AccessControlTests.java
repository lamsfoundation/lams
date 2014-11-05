/****************************************************************
 * Copyright (C) 2014 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 

package org.lamsfoundation.lams.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.admin.util.AdminConstants;
import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.admin.CourseManagementPage;
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.AddLessonPage;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/**
 *  LAMS Security tests
 *  
 *  - creates a course
 *  - creates three users within a course:
 *  	* author 
 *  	* monitor
 *  	* learner
 *  - login as Author
 *  - create a lesson
 *  - start lesson
 *  
 *  Author checks
 *   - can access lesson (shouldn't)
 *   
 *  
 *  Learner checks can access:
 *   - author (shouldn't)
 *   - monitor dialog
 *   - notifications
 *   - lesson gradebook
 *   - conditions
 *   - sysadmin menu
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class AccessControlTests {

	private static final String NO_ROLE_RESPONSE = "Your current role does not allow you to view this page";
	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);

	private static final String COURSE_NAME = "Course " + RANDOM_INT;
	private static final String AUTHOR_USER = "author" + RANDOM_INT;
	private static final String MONITOR_USER = "monitor" + RANDOM_INT;
	private static final String LEARNER_USER = "learner" + RANDOM_INT;
	private static final String DESIGN_NAME = "design" + RANDOM_INT;
	
	
	private static WebDriver driver = null;
	private LoginPage onLogin;
	private IndexPage index;
	private FLAPage fla;
	private AddLessonPage addLesson;
	private AbstractPage sysAdmin;
	private CourseManagementPage cMgt;

	@BeforeClass	
	public void beforeClass() {
		
    	driver = new FirefoxDriver();
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    	
		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		cMgt = PageFactory.initElements(driver, CourseManagementPage.class);
		fla = PageFactory.initElements(driver, FLAPage.class);
		addLesson = PageFactory.initElements(driver, AddLessonPage.class);
		sysAdmin = PageFactory.initElements(driver, AbstractPage.class);
		onLogin.navigateToLamsLogin().loginAs("sysadmin", "sysadmin");
		
    	
	}

	@AfterClass
	public void afterClass() {
    	driver.quit();
	}
	
	/**
	 * Opens Course management interface
	 */
	@Test(description="Opens Course management interface")
	public void openCourseMgt() {

		CourseManagementPage cMgt = new CourseManagementPage(driver);
		cMgt = index.openCourseManagement();
		cMgt.maximizeWindows();
		Assert.assertEquals(cMgt.getTitle(), AdminConstants.COURSE_MGT_TITLE, 
				"The expected title is not present");
		
	}
	
	/**
	 * Creates a new course 
	 */
	@Test(dependsOnMethods="openCourseMgt", description="Creates a new course")
	public void createCourse() {
		
		String courseCode = "CF" + RANDOM_INT;
		String courseDescription = "Some description " + RANDOM_INT;
		String courseLocale = "English (Australia)";
		String status = "Active";

		cMgt.createEditCourse()
		.setCourseName(COURSE_NAME)
		.setCourseCode(courseCode)
		.setDescription(courseDescription)
		.setLocale(courseLocale)
		.setStatus(status)
		.setEnableCourseNotifications()
		.setEnableGradebookForMonitors()
		.setEnableGradebookForLearners()
		.setEnableSingleActivityLessons()
		.save();
		
		cMgt.clickIdSorter();
		cMgt.clickIdSorter();
		
		
		Boolean assertExists = cMgt.courseExists(COURSE_NAME);
		Assert.assertTrue(assertExists, "Course doesn't exist!");
		
	}
	
	/**
	 *    Data provider to create users
	 *    
	 */
	@DataProvider(name = "Users")
	public static Object[][] users() {
		return new Object[][] {
				new Object[] { AUTHOR_USER, "Author", "User", "author@lams.com" }, 
				{ MONITOR_USER, "Monitor", "User", "monitor@lams.com" }, 
				{ LEARNER_USER, "Learner", "User", "learner@lams.com" }
		};
	}
	
	
	/**
	 * Creates users based on data provider
	 *
	 * @param login
	 * @param firstName
	 * @param lastName
	 * @param email 
	 */
	@Test(dependsOnMethods="createCourse", dataProvider="Users", description="Creates users based on data provider")
	public void createUser(String login, String firstName, String lastName, String email) {
		
		cMgt.createEditUser()
		.setLogin(login)
		.setPassword(RANDOM_INT)
		.setPasswordConfirmation(RANDOM_INT)	
		.setFirstName(firstName)
		.setLastName(lastName)
		.setEmail(email)
		.save()
		.manageCourses()
		.clickIdSorter()
		.clickIdSorter();
		
		
		
	}
	
	/**
	 *    Data provider to create users
	 *    
	 */
	@DataProvider(name = "UsersRoles")
	public static Object[][] usersRoles() {
		return new Object[][] {
				new Object[] { AUTHOR_USER, "Author Monitor"}, 
				{ MONITOR_USER, "Monitor" }, 
				{ LEARNER_USER, "Learner"}
		};
	}
	
	/**
	 * Add users to course with specific roles
	 *
	 * @param login username
	 * @param roles List of roles
	 */
	@Test(dependsOnMethods="createUser", dataProvider="UsersRoles", description="Add users to course with specific roles")
	public void addUserToCourse(String login, String roles) {
		
		List<String> roleList = new ArrayList<String>();
		
		for (String role : roles.split(" ")) {
			roleList.add(role);
		}
		
		cMgt.getCourse(COURSE_NAME)
		.manageUsers()
		.addRemoveUser(login, roleList);

		
	}
	
	/**
	 * Logins as Author and creates a one activity design
	 */
	
	@Test(dependsOnMethods="addUserToCourse", description="Logins as Author and creates a one activity design")
	public void createDesign() {
		
		cMgt.closeWindow();
		String windowHandle = getPopUpWindowId(driver);
		
		driver.switchTo().window(windowHandle);
		index.logOut();
		onLogin.loginAs(AUTHOR_USER, RANDOM_INT);
		
		fla = index.openFla();
		fla.maximizeWindows();
		Assert.assertEquals(AuthorConstants.FLA_TITLE, fla.getTitle(), 
				"The expected title is not present");
		
		fla.dragActivityToCanvas(AuthorConstants.FORUM_TITLE);
		
		String saveResult = fla.saveAsDesign(DESIGN_NAME);
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"There was an error saving the design " + saveResult );

		fla.closeWindow();
	}

	/**
	 *  Starts lesson based on design
	 */
	@Test(dependsOnMethods="createDesign", description="Starts lesson based on previously created design")
	public void startLesson() {
		
		String windowHandle = getPopUpWindowId(driver);
		
		driver.switchTo().window(windowHandle);
		
		addLesson = index.addLesson();
		
		List<WebElement> designNodes = addLesson
		.openLessontab()
		.getFolderNodes("user");
		
		addLesson
		.openLessontab()
		.clickDesign(designNodes.get(0));
		
		addLesson.addLessonNow();
		
		boolean assertLessonExists = index.isLessonPresent(DESIGN_NAME);
				
		Assert.assertTrue(assertLessonExists, 
				"The lesson is not present");

	}

	/**
	 *  Author test if it can access lesson as learner via JS
	 *  
	 */
	@Test(dependsOnMethods="startLesson", description="Author test if it can access lesson as learner via JS")
	public void authorAccessLesson() {
		
		// This checks if the lesson is available to Author in the page 
		// essentially if the link is clickable
		
		boolean assertAccessLessonAsLearner = index.isLessonAvailableAsLearner(DESIGN_NAME);
		
		Assert.assertFalse(assertAccessLessonAsLearner, 
				"The lesson is accessible to author as learner. It shouldn't");

	
		// now we do the check if we can access this via JS:
		
		AbstractPage learnerPage = index.openLessonAsLearnerJS(DESIGN_NAME);
		
		String windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);
		
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Sysadmin access didn't return the correct error message");
		
		
		learnerPage.closeWindow();
		
		windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);
		index.logOut();
		
		
	}
	
	/**
	 *  Security learner tests
	 *  
	 *  - attempt to access:
	 *    + Author
	 *    + Monitor
	 *    + Gradebook 
	 *    + Notifications
	 *    + Conditions
	 *    + Sysadmin menus
	 *    + Course admin
	 *  
	 */

	
	/**
	 *  Is lesson accessible to learner? 
	 */
	@Test(dependsOnMethods="startLesson", description="Is lesson accessible to learner?")
	public void learnerLessonAccess() {
		
		//index.logOut();
		String windowHandle = getPopUpWindowId(driver);
		
		driver.switchTo().window(windowHandle);
		onLogin.loginAs(LEARNER_USER, RANDOM_INT);
		
		// First assertion: 
		
		boolean assertAccessLessonAsLearner = index.isLessonAvailableAsLearner(DESIGN_NAME);
		
		Assert.assertTrue(assertAccessLessonAsLearner, 
				"The lesson is not accessible to leaner. It should");
	
		
	}
	
	
	/**
	 *  Learner able to jump as Author
	 */
	@Test(dependsOnMethods="learnerLessonAccess", description="Learner able to jump as Author")
	public void learnerAuthorAccess() {

		// we check first if the author tab is visible
		boolean assertAuthorVisible = index.isAuthorLinkVisible();
		
		Assert.assertFalse(assertAuthorVisible, "Author link visible for learner");
		
		
		// Now check if we are able to open FLA
		// We've do this temporarily as this will change once we switch to proper FLA
		fla  = index.openFlaJS();
		
		driver.switchTo().window("FlashlessAuthoring");
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), "Author loaded! It shouldn't");
		
		
		fla.closeWindow();

		
		
	}

	/**
	 *  Learner able to jump as Monitor in a lesson
	 */
	@Test(dependsOnMethods="learnerAuthorAccess", description="Learner able to jump as Monitor in a lesson")
	public void leanerMonitorAccess() {
		
		String windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);

		index.openMonitorLessonJS(DESIGN_NAME);
		
		driver.switchTo().frame("dialogFrame");
		
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Monitor access didn't return the correct error message");
		
		// we've got to use this -while it ain't pretty, to remove the frame
		// as there's no other way
		driver.navigate().refresh();
		
	}

	/**
	 * Learner able to jump into lesson's notifications
	 */
	@Test(dependsOnMethods="leanerMonitorAccess", description="Learner able to jump into lesson's notifications")
	public void leanerMonitorNotifications() {
		
		index.openMonitorNotificationsJS(DESIGN_NAME);
		
		//String windowHandle = getPopUpWindowId(driver);
		driver.switchTo().frame("notificationsDialogFrame");
		
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Notifications access didn't return the correct error message");
		
		
		driver.navigate().refresh();
	
		
	}
	
	/**
	 * Learner able to jump into lesson's gradebook
	 */
	@Test(dependsOnMethods="leanerMonitorNotifications", description="Learner able to jump into lesson's gradebook")
	public void leanerMonitorGradebook() {
		
		index.openMonitorGradebookJS(DESIGN_NAME);
		
		//String windowHandle = getPopUpWindowId(driver);
		driver.switchTo().frame("dialogFrame");
		
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Gradebook access didn't return the correct error message");
		
		
		driver.navigate().refresh();
	
		
	}
	
	/**
	 * Learner able to jump into lesson's conditions
	 */
	@Test(dependsOnMethods="leanerMonitorGradebook", description="Learner able to jump into lesson's conditions")
	public void leanerMonitorConditions() {
		
		index.openMonitorConditionsJS(DESIGN_NAME);
		
		driver.switchTo().frame("dialogFrame");
		
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Conditions access didn't return the correct error message");
		
		
		driver.navigate().refresh();
	
		index.logOut();
		
	}
		
	
	/**
	 *  Access control to Sysadmin and course management
	 *  
	 *  - attempt to access as Author, Monitor and Learner to:
	 *    + Sysadmin menus
	 *    + Course admin
	 */
	
	
	@Test(dependsOnMethods="leanerMonitorConditions", dataProvider="UsersRoles", 
			description="Access control test for Sysadmin and course management pages as Author, Learner, Monitor")
	public void adminAccessTests(String login, String roles) {
		
		onLogin.loginAs(login, RANDOM_INT);
		sysAdmin = index.openSysadminJS();
		
		String windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);
		
		String assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Sysadmin access didn't return the correct error message");
		
		
		sysAdmin.closeWindow();
		
		windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);
		
		sysAdmin = index.openCourseManagementJS();
		
		windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);
		
		assertError = driver.findElement(By.tagName("h1")).getText();
		
		Assert.assertTrue(assertError.contains(NO_ROLE_RESPONSE), 
				"Course Management access didn't return the correct error message");
		
		
		sysAdmin.closeWindow();
		
		windowHandle = getPopUpWindowId(driver);
		driver.switchTo().window(windowHandle);
		index.logOut();
		
	}
	
	
	
	
	
	
	
	/**
	 * Returns the correct popup id
	 * 
	 * @param driver
	 * @return 
	 */
	private String getPopUpWindowId(WebDriver driver) {

		String authorToolWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> iterator = handles.iterator();

		while (iterator.hasNext()){
			authorToolWindow = iterator.next();
		}

		return authorToolWindow;
	}
	
	
}
