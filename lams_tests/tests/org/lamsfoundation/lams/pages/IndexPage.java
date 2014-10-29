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

import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.pages.monitor.MonitorPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.AddLessonPage;
import org.openqa.selenium.By;
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

	public IndexPage(WebDriver driver) {
		super(driver);
	}

	public FLAPage openFla() {
		flaLink.click();
		driver.switchTo().window("FlashlessAuthoring");
		return PageFactory.initElements(driver, FLAPage.class);		
	}

	public AddLessonPage addLesson() {

		addLessonButton.click();
		WebDriverWait wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("dialogFrame")));

		return PageFactory.initElements(driver, AddLessonPage.class);		
	}

	public Boolean isLessonPresent(String lessonName) {
		
		return ((driver.findElements(By.linkText(lessonName)).size() > 0) ? true : false);

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

	public Boolean isLessonCompleted(String lessonName) {
		
		WebElement lesson = getLessonRowByName(lessonName);
		
		List<WebElement> isCompleted = lesson.findElements(By.className("mycourses-completed-img"));
		
		return ((isCompleted.size() > 0) ? true : false);
		
	}
	
	public IndexPage refresh() {
		
		refreshButton.click();
		
		return PageFactory.initElements(driver, IndexPage.class);
		
	}
	
	
	public void openLessonAsLearner() {
		
	}
	
	public Boolean isLessonAvailableAsLearner(String lessonName) {
		
		WebElement lesson = getLessonRowByName(lessonName);
		
		WebElement lessonLink = lesson.findElement(By.tagName("a"));
		
		String classAttribute = lessonLink.getAttribute("class");
		
		return ((classAttribute.equals("disabled-sequence-name-link")) ? false : true);
		
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
	

}
