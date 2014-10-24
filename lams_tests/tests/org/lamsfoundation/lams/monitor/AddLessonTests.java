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

package org.lamsfoundation.lams.monitor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.AddLessonPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.LessonTab;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class AddLessonTests {

	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);

	private LoginPage onLogin;
	private IndexPage index;
	private AddLessonPage addLesson;
	
	WebDriver driver;
	
	
	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		addLesson = PageFactory.initElements(driver, AddLessonPage.class);
		onLogin.navigateToLamsLogin().loginAs("test3", "test3");
		
	}
	
	@AfterClass
	public void afterClass() {
		//driver.quit();
	}

	/**
	 * Opens Add lesson interface
	 */
	@Test
	public void openAddLesson() {
		
		final String addButton= "Add now";
		
		AddLessonPage addLesson = new AddLessonPage(driver);
		addLesson = openDialog(index);
		
		String assertFrameOpen = addLesson.checkAddLessonButton();
		
		Assert.assertEquals(assertFrameOpen, addButton, 
				"It doesn't seem that the add lesson UI has loaded properly");

	}
	

	@Test(dependsOnMethods="openAddLesson")
	public void checksLandingtTab() {
		
		String lessonTabTitle = addLesson.openLessontab().getLessonTabTitle();
		
		Assert.assertEquals(lessonTabTitle, LessonTab.lessonTitle, 
				"It doesn't seem that we are in the lesson tab.");
		
	}
	
	@Test(dependsOnMethods="checksLandingtTab")
	public void getUserDesigns() {
		
		List<WebElement> designs = addLesson.openLessontab().getFolderNodes("user");
		
		for (WebElement design : designs) {
			
			design.click();
			
			Assert.assertTrue(addLesson.openLessontab().isDesignImageDisplayed(), 
					"Design image is not displayed");

		}
		
	}

	/**
	 * Lesson tab tests
	 */
	
	/**
	 * Creates a lesson picking up the first design and gives it a name
	 * 
	 */
	@Test(dependsOnMethods="getUserDesigns")
	public void createLessonWithFirstDesign() {
		
		String lessonName  = "First lesson " + RANDOM_INT;
		
		// Gets all the nodes in the user's folder
		List<WebElement> designs = addLesson.openLessontab().getFolderNodes("user");
		
		// pick the first design
		WebElement firstDesign = designs.get(0);
		
		// Select design
		addLesson
		.openLessontab()
		.clickDesign(firstDesign)
		.setLessonName(lessonName);
		
		
		// Assert if design image is displayed
		boolean isImageDisplayed = addLesson.openLessontab().isDesignImageDisplayed();
		Assert.assertTrue(isImageDisplayed, 
				"Design image is not displayed");
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
		
		
	}
	
	/**
	 * Class tab tests
	 */

	/**
	 * By default LAMS monitor only includes the user that
	 * is starting the lesson as monitor
	 */
	@Test(dependsOnMethods="createLessonWithFirstDesign")
	public void checkOnlyOneMonitorSelected() {
		
		int expectedNumberOfMonitors = 1;
		addLesson = openDialog(index);
		
		int selectedMonitorsAtStart = addLesson
				.openClasstab()
				.getNumberSelectedMonitors();
		
		// Asserts 
		Assert.assertEquals(selectedMonitorsAtStart, expectedNumberOfMonitors,
				"There was more than one monitor by default");
		
	}
	
	@Test(dependsOnMethods="checkOnlyOneMonitorSelected")
	public void checkOnlyAllLearnersSelected() {
		
		int expectedUnselectedLearners = 0;
		
		int unSelectedLearners = addLesson
				.openClasstab()
				.getNumberUnselectedLearners();
		
		// Assert
		Assert.assertEquals(unSelectedLearners, expectedUnselectedLearners, 
				"There shouldn't be any unselected learners");
		
		// addLesson.closeDialog();
	}	
	
	@Test(dependsOnMethods="checkOnlyAllLearnersSelected")
	public void createLessonWithAllMonitors() {
		
		String lessonName = "All in " + RANDOM_INT;
		
		int selectedMonitorsAtStart = addLesson
		.openClasstab()
		.getNumberSelectedMonitors();
		
		int unSelectedMonitorsAtStart = addLesson
		.openClasstab()
		.getNumberUnselectedMonitors();
		
		selectRandomDesign(addLesson, lessonName);
		
		// Asserts that there's only one monitor in the selected side
		// By default we only include one and only one monitor to a lesson
		Assert.assertTrue(selectedMonitorsAtStart == 1, 
				"There was more than one monitor by default");
		
		// Add all monitors to lesson (one by one)
		addLesson
		.openClasstab()
		.addAllMonitorsToLesson();
		
		int selectedMonitors = addLesson
		.openClasstab()
		.getNumberSelectedMonitors();
		
		// Assert that now all monitors should be in the selected list
		int allMonitors = selectedMonitorsAtStart + unSelectedMonitorsAtStart;
		Assert.assertEquals(selectedMonitors, allMonitors, "All monitors should have been selected");
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
	}
	

	private void selectRandomDesign(AddLessonPage addLesson, String lessonName) {
		
		// Gets all the nodes in the user's folder
		List<WebElement> designs = addLesson.openLessontab().getFolderNodes("user");
		
		int randomDesign = Integer.parseInt(LamsUtil.randInt(1, designs.size()-1));
		
		addLesson
		.openLessontab()
		.clickDesign(designs.get(randomDesign))
		.setLessonName(lessonName);
		
	}

	@Test(dependsOnMethods="createLessonWithAllMonitors")
	public void createLessonWithOnlyOneLearner() {
		
	}

	
	/**
	 * Advanced tab tests
	 */
	
	@Test(dependsOnMethods="createLessonWithOnlyOneLearner")
	public void createLessonWithIntro() {
		
	}
	
	@Test(dependsOnMethods="createLessonWithIntro")
	public void createLessonStartInMonitor() {
		
	}

	@Test(dependsOnMethods="createLessonStartInMonitor")
	public void createLessonStartAlwaysFirstActivity() {
		
	}

	@Test(dependsOnMethods="createLessonStartInMonitor")
	public void createLessonWithOutLiveEdit() {
		
	}

	@Test(dependsOnMethods="createLessonWithOutLiveEdit")
	public void createLessonStartWithOutLessonNotifications() {
		
	}
	
	@Test(dependsOnMethods="createLessonStartWithOutLessonNotifications")
	public void createLessonEnableExportPortfolio() {
		
	}

	@Test(dependsOnMethods="createLessonEnableExportPortfolio")
	public void createLessonEnableWhosOnline() {
		
	}

	@Test(dependsOnMethods="createLessonEnableWhosOnline")
	public void createLessonEnableIM() {
		
	}
	
	@Test(dependsOnMethods="createLessonEnableIM")
	public void createLessonSplitInMultiple() {
		
	}
	
	@Test(dependsOnMethods="createLessonEnableIM")
	public void createLessonScheduleStart() {
		
	}
	
	
	private AddLessonPage openDialog(IndexPage index) {
		addLesson = index.addLesson();
		//driver.switchTo().frame("dialogFrame");
		
		return addLesson;
	}

	
}
