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

package org.lamsfoundation.lams.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.pages.admin.CourseManagementPage;
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.pages.learner.LearnerPage;
import org.lamsfoundation.lams.pages.monitor.MonitorPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.AddLessonPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * IndexPage 
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class IndexPage extends AbstractPage {

	@FindBy(id = "openFla")
	private WebElement flaLink;	
	
	@FindBy(id = "Author") // Eventually this will be replace by FLA only!
	private WebElement flashAuthor;

	@FindBy(id = "My Profile")
	private WebElement myProfileTab; 

	@FindBy(className = "add-lesson-button")
	private WebElement addLessonButton; 

	@FindBy(id = "refreshButton")
	private WebElement refreshButton;
	
	@FindBy(id = "logoutButton")
	private WebElement logoutButton;
	
	@FindBy(className = "lesson-table")
	private WebElement lessonTable;

	@FindBy(id = "Course Mgt")
	private WebElement courseMgtButton;
	
	public IndexPage(WebDriver driver) {
		super(driver);
	}

	public FLAPage openFla() {
		flaLink.click();
		driver.switchTo().window("FlashlessAuthoring");
		return PageFactory.initElements(driver, FLAPage.class);		
	}
	
	/**
	 * Checks if the link to Author UI is visible
	 * @return boolean
	 */
	public boolean isAuthorLinkVisible() {
		
		return isElementPresent(driver, flashAuthor);
			}
	

	/**
	 * Opens the AddLesson UI
	 * 
	 * @return {@link AddLessonPage}
	 */
	public AddLessonPage addLesson() {

		addLessonButton.click();
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("dialogFrame")));

		return PageFactory.initElements(driver, AddLessonPage.class);		
	}

	public Boolean isLessonPresent(String lessonName) {
		
		Boolean present = false;
		
		int isLessonIn = driver.findElements(By.linkText(lessonName)).size();
		
		if (isLessonIn > 0) {
			present = true;
		}
		
		return present;
		
/*		List<String> allLessons = getAllLessonNames();
		
		
		Boolean isLessonInCourse = allLessons.contains(lessonName);
		System.out.println("All lessons: "+ allLessons);
		System.out.println("Is it true? :" + isLessonInCourse);
		return isLessonInCourse;
		*/
		// return ((driver.findElements(By.linkText(lessonName)).size() > 0) ? true : false);

	}

	/**
	 * Monitoring 
	 * 
	 */
	
	/**
	 * Opens Monitoring for a given lesson name
	 * 
	 * @param lessonName the name of the lesson to open 
	 * @return {@link MonitorPage}
	 */
	public MonitorPage openMonitorLessonByLessonName(String lessonName) {

		String lessonId = getLessonId(lessonName);
		
		openMonitorLessonById(lessonId);
		
		return PageFactory.initElements(driver, MonitorPage.class);
	}
	
	/**
	 * Open monitoring for a given lesson ID
	 * 
	 * @param id
	 * @return
	 */
	public MonitorPage openMonitorLessonById(String lessonId) {

		WebElement lessonMonitor = lessonTable.findElement(By.id(lessonId));
		
		lessonMonitor.findElement((By.className("mycourses-monitor-img"))).click();
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("dialogFrame")));

		return PageFactory.initElements(driver, MonitorPage.class);
	}
	
	
	
	/**
	 * Gets all the lesson names on a list
	 * 
	 * @return List<String> lesson names 
	 */
	public List<String> getAllLessonNames() {

		List<String> allLessonNames = new ArrayList<String>();

		List<WebElement> lessons = lessonTable.findElements(By.className("j-single-lesson"));

		for (WebElement lesson : lessons) {

			String lessonName = lesson.findElement(By.tagName("a")).getText();

			allLessonNames.add(lessonName);

		}

		return allLessonNames;

	}
	
	/**
	 * Returns a string with lesson actions available for the given lesson
	 * 
	 * All actions available:
	 *  - Remove
	 *  - Conditions
	 *  - Mark
	 *  - Notifications
	 *  - Monitor
	 * 
	 * @param lessonName
	 * @return List of string with lesson actions
	 */
	public List<String> getAvailableLessonActions(String lessonName) {
		
		List<String> lessonActions = new ArrayList<String>();

		// Get lesson row
		WebElement lesson = getLessonRowByName(lessonName);
				
		// Get lesson actions
		WebElement lessonActionDiv = lesson.findElement(By.className("lesson-actions"));
		List<WebElement> actions = lessonActionDiv.findElements(By.tagName("span"));

		// iterate thru actions and put them in the list
		for (WebElement action : actions) {
			lessonActions.add(action.getAttribute("innerHTML").trim());
			
		}
		
		return lessonActions;
	}

	/**
	 * Checks if the lesson displays as completed
	 * 
	 * @param lessonName
	 * @return 
	 */
	public Boolean isLessonCompleted(String lessonName) {
		
		WebElement lesson = getLessonRowByName(lessonName);
		
		List<WebElement> isCompleted = lesson.findElements(By.className("mycourses-completed-img"));
		
		return ((isCompleted.size() > 0) ? true : false);
		
	}
	
	/**
	 * Refreshes index page with button
	 * 
	 * @return {@link IndexPage}
	 */
	public IndexPage refresh() {
		
		refreshButton.click();
		
		return PageFactory.initElements(driver, IndexPage.class);
		
	}
	
	
	public LearnerPage openLessonAsLearner(String lessonName) {
		
		WebElement lesson = getLessonRowByName(lessonName);
		
		WebElement lessonLink = lesson.findElement(By.tagName("a"));
		
		lessonLink.click();
		
		driver.switchTo().window("lWindow");
		return PageFactory.initElements(driver, LearnerPage.class);

		
	}
	
	/**
	 * Checks if the lesson is available to jump as learner 
	 * 
	 * @param lessonName
	 * @return true/false
	 */
	public Boolean isLessonAvailableAsLearner(String lessonName) {
		
		WebElement lesson = getLessonRowByName(lessonName);
		
		WebElement lessonLink = lesson.findElement(By.tagName("a"));
		
		String classAttribute = lessonLink.getAttribute("class");
		
		return ((classAttribute.equals("disabled-sequence-name-link")) ? false : true);
		
	}
	
	
	/**
	 * Course Management 
	 * 
	 */
	public CourseManagementPage openCourseManagement() {

		courseMgtButton.click();
		driver.switchTo().window("omWindow");
		return PageFactory.initElements(driver, CourseManagementPage.class);
	}
	
	
	/**
	 * Logs out
	 * @return {@link LoginPage}
	 */
	public LoginPage logOut() {
		
		logoutButton.click();
	
		return PageFactory.initElements(driver, LoginPage.class);
	}
	
	
	
	/**
	 * Javascript methods
	 * 
	 * We use this javascript methods to execute js on the testing browser just as if we click on an actual
	 * web element. 
	 * 
	 * The main reason we use this is to test JS but also to simulate access. For instance we can test if a
	 * learner user can open sysadmin menus. We can't do this by clicking as the web elements aren't there, 
	 * but thru JS we can. (see AccessControlTests.java) 
	 * 
	 */
	
	/**
	 * Uses javascript to open author
	 */
	public FLAPage openFlaJS() {
		
        ((JavascriptExecutor) 
                driver).executeScript("window.open('authoring/author.do?method=openAuthoring', 'FlashlessAuthoring','resizable=yes,scrollbars=yes,left=10,top=10,width=1280,height=800');");
		
        return PageFactory.initElements(driver, FLAPage.class);
	}
	
	
	/**
	 * Attempts to open monitor lesson via Javascript
	 * @param string 
	 */
	public MonitorPage openMonitorLessonJS(String lessonName) {

		String lessonId = getLessonId(lessonName);
		((JavascriptExecutor) 
                driver).executeScript("showMonitorLessonDialog(" + lessonId +")");
		
		
		return PageFactory.initElements(driver, MonitorPage.class);
		
	}
	
	
	/**
	 * Attempts to open monitor lesson notifications
	 * 
	 * Currently we return {@link AbstractPage} but one we have a page for notifications, this must
	 * be changed to it. 
	 * 
	 * @param lessonName
	 * @return {@link AbstractPage}
	 */
	public AbstractPage openMonitorNotificationsJS(String lessonName) {
		
		String lessonId = getLessonId(lessonName);
		((JavascriptExecutor) 
                driver).executeScript("showNotificationsDialog(null," + lessonId +")");
		
		return PageFactory.initElements(driver, MonitorPage.class);
		
	}
	
	
	
	/**
	 * Attempts to open monitor lesson gradebook
	 * 
	 * Currently we return {@link AbstractPage} but one we have a page for lesson gradebook, this must
	 * be changed to it. 
	 * 
	 * @param lessonName
	 * @return 
	 */
	public AbstractPage openMonitorGradebookJS(String lessonName) {

		String lessonId = getLessonId(lessonName);
		((JavascriptExecutor) 
                driver).executeScript("showGradebookLessonDialog(" + lessonId +")");
		
		return PageFactory.initElements(driver, MonitorPage.class);
		
		
	}


	/**
	 * Attempts to open monitor lesson conditions
	 * 
	 * Currently we return {@link AbstractPage} but one we have a page for lesson conditions, this must
	 * be changed to it. 
	 * 
	 * @param lessonName
	 * @return 
	 */
	public AbstractPage openMonitorConditionsJS(String lessonName) {

		String lessonId = getLessonId(lessonName);
		((JavascriptExecutor) 
                driver).executeScript("showConditionsDialog(" + lessonId +")");
		
		return PageFactory.initElements(driver, MonitorPage.class);
		
		
	}
	
	
	/**
	 * Attempts to open sysadmin menu
	 * 
	 * @return {@link AbstractPage}
	 */
	public AbstractPage openSysadminJS() {

		((JavascriptExecutor) 
                driver).executeScript("openSysadmin()");
		
		return PageFactory.initElements(driver, AbstractPage.class);
	}
	
	
	/**
	 * Attempts to open course management
	 * 
	 * @return {@link CourseManagementPage} 
	 */
	public CourseManagementPage openCourseManagementJS() {

		((JavascriptExecutor) 
                driver).executeScript("openOrgManagement(1)");
		
		return PageFactory.initElements(driver, CourseManagementPage.class);
	}

	/**
	 * Attempts to open a lesson via JS
	 * 
	 * @return {@link AbstractPage}
	 */
	public AbstractPage openLessonAsLearnerJS(String lessonName) {

		String lessonId = getLessonId(lessonName);
		((JavascriptExecutor) 
                driver).executeScript("openLearner(" + lessonId +")");
		
		
		return PageFactory.initElements(driver, AbstractPage.class);

		
	}

	
	
	
	
	/**
	 * Returns all the lesson nodes found on the page
	 * 
	 * @return List<WebElement> of nodes
	 */
	private List<WebElement> getAllLessonNodes() {

		return lessonTable.findElements(By.className("j-single-lesson"));

	}

	


	/**
	 * Returns the lessonId for a given lesson name
	 * 
	 * @param lessonName
	 * @return lessonID as string
	 */
	private String getLessonId(String lessonName) {

		List<WebElement> allLessons = getAllLessonNodes();
		
		String lessonId = "";

		for (WebElement lesson : allLessons) {

			String name = lesson.findElement(By.tagName("a")).getText().trim();
			
			if (name.equals(lessonName.trim())) {
				
				lessonId = lesson.getAttribute("id");
				break;
			}
			

		}
		
		return lessonId;

	}
	
	

	private WebElement getLessonRowByName(String lessonName) {
		
		WebElement lessonRow = null;
		
		String lessonId = getLessonId(lessonName);
		
		lessonRow = getLessonRowById(lessonId);
		
		return lessonRow;
		
	}
	
	private WebElement getLessonRowById(String lessonId) {
		
		return lessonTable.findElement(By.id(lessonId));
		
	}

	
	private static boolean isElementPresent(WebDriver webdriver, WebElement webelement) {	
		boolean exists = false;

		webdriver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		
		try {
			webelement.getTagName();
			exists = true;
		} catch (NoSuchElementException e) {
			// nothing to do.
		}

		webdriver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		return exists;
	}










	

}
