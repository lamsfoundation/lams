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
import org.lamsfoundation.lams.pages.monitor.MonitorPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.AddLessonPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.LessonTab;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * 
 * Add lesson tests
 *  - Open Add lesson wizard
 *  - Check landing tab
 *  - Create a first lesson (checks default settings)
 *  - Create lesson with 
 *     + all monitors
 *     + only one learner
 *     + lesson intro
 *     + start in monitor
 *     + start always from the first activity
 *     + without live edit
 *     + without lesson notifications
 *     + without export portfolio
 *     + with presence (who's online)
 *     + with IM
 *     + multiple lesson (x number of learners in each)
 *     + scheduled lesson
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class AddLessonTests {

	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);

	private LoginPage onLogin;
	private IndexPage index;
	private MonitorPage monitor;
	private AddLessonPage addLesson;
	
	WebDriver driver;
	
	
	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		addLesson = PageFactory.initElements(driver, AddLessonPage.class);
		monitor = PageFactory.initElements(driver, MonitorPage.class);
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
		addLesson.maximizeWindows();
		addLesson = openDialog(index);
		
		String assertFrameOpen = addLesson.checkAddLessonButton();
		
		Assert.assertEquals(assertFrameOpen, addButton, 
				"It doesn't seem that the add lesson UI has loaded properly");

	}
	

	/**
	 * Checks that the opening tab is the lesson tab
	 */
	@Test(dependsOnMethods="openAddLesson")
	public void checksLandingtTab() {
		
		String lessonTabTitle = addLesson.openLessontab().getLessonTabTitle();
		
		Assert.assertEquals(lessonTabTitle, LessonTab.lessonTitle, 
				"It doesn't seem that we are in the lesson tab.");
		
	}
	
	/**
	 * Show learning design images
	 */
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
	
	/**
	 * Checks default setting: All learners should be included in the lesson
	 */
	@Test(dependsOnMethods="checkOnlyOneMonitorSelected")
	public void checkOnlyAllLearnersSelected() {
		
		Boolean emptyUnselectedLearners = addLesson.openClasstab().isUnselectedLearnersEmpty();

		// Assert
		Assert.assertTrue(emptyUnselectedLearners, 
				"There shouldn't be any unselected learners");
		
	}	
	

	/**
	 * Creates a lesson with all monitors selected
	 */
	@Test(dependsOnMethods="checkOnlyAllLearnersSelected")
	public void createLessonWithAllMonitors() {
		
		String lessonName = "All monitors included " + RANDOM_INT;
		
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
	


	/**
	 * Creates a lesson with only one learner
	 */
	@Test(dependsOnMethods="createLessonWithAllMonitors")
	public void createLessonWithOnlyOneLearner() {

		String lessonName = "Only one learner " + RANDOM_INT;
		
		addLesson = openDialog(index);

		// Remove all learners
		addLesson.openClasstab().removeAllLearnersFromLesson();

		// Now just add one
		addLesson.openClasstab().addOneLearnerToLesson();
		
		// Assert
		int numberSelectedLearners = addLesson.openClasstab().getNumberSelectedLearners();
		Assert.assertEquals(numberSelectedLearners, 1, 
				"There seems to be more than one student in this lesson");
		
		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");

		
	}

	
	/**
	 * Advanced tab tests
	 */
	
	/**
	 * Checks the default tab settings
	 * 
	 * Only Live Edit & lesson notification enabled. 
	 * 
	 */
	@Test(dependsOnMethods="createLessonWithOnlyOneLearner")
	public void checksDefaultAdvancedSettings() {
	
		addLesson = openDialog(index);
	
		Boolean isLessonIntroEnabled  = addLesson.openAdvancedTab().isEnableLessonIntro();
		Boolean isStartInMonitor = addLesson.openAdvancedTab().isStartInMonitor();
		Boolean isLearnerStartBeginning  = addLesson.openAdvancedTab().isLearnerRestart();
		Boolean isEnableLiveEdit = addLesson.openAdvancedTab().isEnableLiveEdit();
		Boolean isEnableLessonNotifications = addLesson.openAdvancedTab().isEnableLessonNotifications();
		Boolean isExportPortfolio = addLesson.openAdvancedTab().isEnablePortfolio();
		Boolean isEnablePresence = addLesson.openAdvancedTab().isEnablePresence();
		Boolean isEnableIM  = addLesson.openAdvancedTab().isEnableIM();
		Boolean isSplitLearners = addLesson.openAdvancedTab().isSplitLearners();
		Boolean isScheduledEnable = addLesson.openAdvancedTab().isScheduledEnable();
		
		// Assertions now
		
		Assert.assertFalse(isLessonIntroEnabled, "Lesson intro should be OFF");
		Assert.assertFalse(isStartInMonitor, "Start in monitor should be OFF");
		Assert.assertFalse(isLearnerStartBeginning, "Learner start from the beginning should be OFF");
		Assert.assertTrue(isEnableLiveEdit, "Live edit should be ON");
		Assert.assertTrue(isEnableLessonNotifications, "Lesson notifications should be ON");
		Assert.assertFalse(isExportPortfolio, "Export portfolio should be  OFF");
		Assert.assertFalse(isEnablePresence, "Presence should be OFF");
		Assert.assertFalse(isEnableIM, "IM should be OFF");
		Assert.assertFalse(isSplitLearners, "Split learners into lessons should be OFF");
		Assert.assertFalse(isScheduledEnable, "Scheduling should be OFF");
		
		
		//addLesson.closeDialog();
		
	}
	
	/**
	 * Creates a lesson with an intro page
	 */
	@Test(dependsOnMethods="checksDefaultAdvancedSettings")
	public void createLessonWithIntro() {
		String introHTMLTxt = "<strong>Hi!</strong> <i>This is a new lesson</i><p>Try to have a go at it</p>";
		
		String lessonName = "Lesson with Intro " + RANDOM_INT;
		selectRandomDesign(addLesson, lessonName);

		// 
		addLesson
		.openAdvancedTab()
		.setEnableLessonIntro()
		.setIntroText(introHTMLTxt)
		.setDisplayDesign();
		
		String assertIntroTxt = addLesson
				.openAdvancedTab()
				.getIntroText();
		
		// Assert lesson with intro 
		Assert.assertTrue(assertIntroTxt.contains(introHTMLTxt), "Intro text doesn't match");
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");

		
	}
	
	/**
	 * Creates a lesson set to start in monitor
	 */
	@Test(dependsOnMethods="createLessonWithIntro")
	public void createLessonStartInMonitor() {
		addLesson = openDialog(index);
		
		String lessonName = "Lesson start in monitor " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setStartInMonitor();
		
		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");

	}

	/**
	 * Creates a lesson that will always starts from the first activity
	 */
	@Test(dependsOnMethods="createLessonStartInMonitor")
	public void createLessonStartAlwaysFirstActivity() {
		
		addLesson = openDialog(index);
		
		String lessonName = "Lesson always start from 1st act " + RANDOM_INT;
		selectRandomDesign(addLesson, lessonName);

		// 
		addLesson
		.openAdvancedTab()
		.setLearnerRestart();
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");

	}

	/**
	 * Creates a lesson without live edit feature
	 */
	@Test(dependsOnMethods="createLessonStartInMonitor")
	public void createLessonWithOutLiveEdit() {
		
		addLesson = openDialog(index);
		
		String lessonName = "Without live edit " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setEnableLiveEdit();

		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");

		
	}

	/**
	 * Creates a lesson without lesson notifications
	 */
	@Test(dependsOnMethods="createLessonWithOutLiveEdit")
	public void createLessonStartWithOutLessonNotifications() {
		addLesson = openDialog(index);
		
		String lessonName = "Without notifications " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setEnableLessonNotifications();

		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");

		
	}
	
	/**
	 * Creates a lesson without lesson notifications
	 */
	@Test(dependsOnMethods="createLessonStartWithOutLessonNotifications")
	public void createLessonEnableExportPortfolio() {
		addLesson = openDialog(index);
		
		String lessonName = "Enable Export Portfolio " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setEnablePortfolio();

		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
		
	}

	/**
	 * Creates a lesson with presence enabled
	 */
	@Test(dependsOnMethods="createLessonEnableExportPortfolio")
	public void createLessonEnableWhosOnline() {
		addLesson = openDialog(index);
		
		String lessonName = "Enable presence " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setEnablePresence();

		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
		
	}

	/**
	 * Creates a lesson with IM enabled
	 */
	@Test(dependsOnMethods="createLessonEnableWhosOnline")
	public void createLessonEnableIM() {
		addLesson = openDialog(index);
		
		String lessonName = "Enable IM " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setEnablePresence()
		.setEnableIM();

		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
	}
	
	/**
	 * Creates multiple lessons putting X number of learners in each
	 */
	@Test(dependsOnMethods="createLessonEnableIM")
	public void createLessonSplitInMultiple() {
		addLesson = openDialog(index);
		
		String lessonName = "Split " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setSplitLearners()
		.setSplitCounter("2");

		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		
		for (int i = 1; i < 4; i++) {

			boolean wasLessonCreated = index.isLessonPresent(lessonName + " " + i);
			
			Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
			
		}

	
	}
	
	/**
	 * Creates an scheduled lesson
	 */
	@Test(dependsOnMethods="createLessonSplitInMultiple")
	public void createLessonScheduleStart() {
		addLesson = openDialog(index);
		
		String lessonName = "Scheduled " + RANDOM_INT;

		// 
		addLesson
		.openAdvancedTab()
		.setScheduleEnable();
		
		selectRandomDesign(addLesson, lessonName);
		
		// Add lesson
		addLesson
		.addLessonNow();
		
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(lessonName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + lessonName + " was not found!");
		
	}
	
	
	/*	
		Now let's check the monitor view for all the lessons we've created
	*/
	
	
	
	
	/**
	 * Check first lesson
	 */
	@Test(dependsOnMethods="createLessonScheduleStart")
	public void checkLessonWithFirstDesign() {
		
		String lessonName  = "First lesson " + RANDOM_INT;
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
	    // driver.findElement(By.linkText(lessonName)).click();

		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean allLearnersSelected = monitor
				.openLessonTab()
				.openEditClass()
				.areAlllearnersSelected();
		
		Assert.assertTrue(allLearnersSelected, 
				"All learners should have been selected");
		
		monitor.closeDialog();
		
	}
	
	
	/**
	 * Check All monitors included lesson
	 */
	@Test(dependsOnMethods="checkLessonWithFirstDesign")
	public void checkLessonAllMonitors() {
		
		String lessonName = "All monitors included " + RANDOM_INT;
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
	    // driver.findElement(By.linkText(lessonName)).click();

		monitor = openMonitor(index, lessonName);

		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		// Start assertions
		// Assert lesson name
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		
		boolean allMonitorsSelected = monitor
				.openLessonTab()
				.openEditClass()
				.areAllMonitorsSelected();
		
		// Assert all monitors selected
		Assert.assertTrue(allMonitorsSelected, 
				"All monitors should have been selected");
		
		boolean allLearnersSelected = monitor
				.openLessonTab()
				.openEditClass()
				.areAlllearnersSelected();
		
		Assert.assertTrue(allLearnersSelected, 
				"All learners should have been selected");
		
		monitor.closeDialog();
		
	}
	
	
	
	/**
	 * Check only one learner lesson
	 */
	@Test(dependsOnMethods="checkLessonAllMonitors")
	public void checkLessonOnlyOneLearner() {
		
		String lessonName  = "Only one learner " + RANDOM_INT;
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
	    // driver.findElement(By.linkText(lessonName)).click();

		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean allLearnersSelected = monitor
				.openLessonTab()
				.openEditClass()
				.areAlllearnersSelected();
		
		Assert.assertFalse(allLearnersSelected, 
				"Only one learner should have been selected");
		
		monitor.closeDialog();
		
	}
	
	
	/**
	 * Check lesson with intro
	 */
	@Test(dependsOnMethods="checkLessonOnlyOneLearner")
	public void checkLessonWithIntro() {
		
		String lessonName  = "Lesson with Intro " + RANDOM_INT;
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
	    // driver.findElement(By.linkText(lessonName)).click();
		
		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		String lessonDescription = monitor.openLessonTab().getLessonDescription();
		
		Assert.assertTrue(lessonDescription.contains("Hi!"), 
				"Lesson doesn't contain description");
		
		monitor.closeDialog();
		
	}
	
	/**
	 * Check only one learner lesson
	 */
	@Test(dependsOnMethods="checkLessonWithIntro")
	public void checkLessonStartInMonitor() {
		
		String lessonName = "Lesson start in monitor " + RANDOM_INT;

		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
		
		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean isLessonScheduled = monitor.openLessonTab().isLessonScheduled();
		
		Assert.assertTrue(isLessonScheduled, 
				"Lesson isn't scheduled");
		
		monitor.closeDialog();
		
	}
	
	/**
	 * Check lesson without live edit
	 */
	@Test(dependsOnMethods="checkLessonStartInMonitor")
	public void checkLessonWithOutLiveEdit() {
		
		String lessonName = "Without live edit " + RANDOM_INT;

		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
		
		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean isLiveEditPresent = monitor.openSequenceTab().isLiveEditPresent();
		
		Assert.assertFalse(isLiveEditPresent, 
				"Lesson's live edit is present, when it shouldn't");
		
		monitor.closeDialog();
		
	}
	
	
	/**
	 * Check lesson without notifications
	 */
	@Test(dependsOnMethods="checkLessonWithOutLiveEdit")
	public void checkLessonWithOutNotifications() {
		
		String lessonName = "Without notifications " + RANDOM_INT;

		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
		
		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean isNotificationsPresent = monitor.openLessonTab().isNotificationsPresent();
		
		Assert.assertFalse(isNotificationsPresent, 
				"Lesson's live edit is present, when it shouldn't");
		
		monitor.closeDialog();
		
	}
	
	
	/**
	 * Check lesson with who's online (presence)
	 */
	@Test(dependsOnMethods="checkLessonWithOutNotifications")
	public void checkLessonEnableWhosOnline() {
		
		String lessonName = "Enable presence " + RANDOM_INT;

		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
		
		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean isPresenceEnabled = monitor.openLessonTab().isPresenceEnabled();
		
		Assert.assertTrue(isPresenceEnabled, 
				"Lesson's presence is disabled");
		
		monitor.closeDialog();
		
	}
	
	
	/**
	 * Check lesson with enabled IM
	 */
	@Test(dependsOnMethods="checkLessonEnableWhosOnline")
	public void checkLessonEnableIM() {
		
		String lessonName = "Enable IM " + RANDOM_INT;

		WebDriverWait wait = new WebDriverWait(driver, 15);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(lessonName)));
		
		monitor = openMonitor(index, lessonName);
		monitor.openLessonTab();
		
		String lessonTitleInMonitor = monitor.openLessonTab().getLessonTitle();
		
		Assert.assertEquals(lessonName, lessonTitleInMonitor, 
				"Expected the name to be the same but it isn't");
		
		boolean isImEnabled = monitor.openLessonTab().isImEnabled();
		
		Assert.assertTrue(isImEnabled, 
				"Lesson's IM is disabled");
		
		monitor.closeDialog();
		
	}
	
	
	private AddLessonPage openDialog(IndexPage index) {
		addLesson = index.addLesson();
		//driver.switchTo().frame("dialogFrame");
		
		return addLesson;
	}
	
	private MonitorPage openMonitor(IndexPage index, String lessonName) {
		
		monitor = index.openMonitorLessonByLessonName(lessonName);
		
		return monitor;
	}
	
	private void selectRandomDesign(AddLessonPage addLesson, String lessonName) {
		
		// Gets all the nodes in the user's folder
		List<WebElement> designs = addLesson.openLessontab().getFolderNodes("user");
		
		// System.out.println("# of designs:" + designs.size());
		
		int randomDesign = Integer.parseInt(LamsUtil.randInt(1, designs.size()-1));
		
		//System.out.println("randomDesign : " + randomDesign );
		
		addLesson
		.openLessontab()
		.clickDesign(designs.get(randomDesign));
		
		Assert.assertTrue(addLesson.openLessontab().isDesignImageDisplayed(), 
				"Design image is not displayed");		
		
		addLesson
		.openLessontab()
		.setLessonName(lessonName);
		
		
	}

}
