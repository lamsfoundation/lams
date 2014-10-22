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

package org.lamsfoundation.lams.author;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Authoring tests for:
 * 
 *  - opening FLA
 *  - create a design with 4 activities
 *  - name and save design
 *  - clear canvas
 *  - reopen design
 *  - arrange activities
 *  - re-save design
 *  - saveAs design
 *  - change activity titles
 *  - save (one click)
 *  - Create group
 *  - Assign group to activity
 *  - Modify group settings
 *  - Add design description
 *  - Add design license
 *  - Copy/paste activities
 *  - Arrange design
 *  - Save invalid design
 *  - Delete activity
 *  
 *  
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class AuthorTests {
	
	
	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);
	private static final String GROUP_ACTIVITY_NAME = "New Group";

	private String randomDesignName = "Design-" + RANDOM_INT;
	private int randomInteger = Integer.parseInt(LamsUtil.randInt(0, 5));
	
	private LoginPage onLogin;
	private IndexPage index;
	private FLAPage fla;
	
	WebDriver driver;
	
	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		fla = PageFactory.initElements(driver, FLAPage.class);
		onLogin.navigateToLamsLogin().loginAs("test3", "test3");
		
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	/**
	 * Opens FLA interface
	 */
	@Test
	public void openFLA() {
		
		FLAPage fla = new FLAPage(driver);
		fla = index.openFla();
		fla.maximizeWindows();
		Assert.assertEquals(AuthorConstants.FLA_TITLE, fla.getTitle(), "The expected title is not present");


		
	}
	
	/**
	 * Creates a 4 activity design
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void createDesign() {
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 200, 250);
		fla.dragActivityToCanvasPosition(AuthorConstants.SHARE_RESOURCES_TITLE, 350, (10 * randomInteger));
		fla.dragActivityToCanvasPosition(AuthorConstants.KALTURA_TITLE, 550, (80 * randomInteger));
		fla.dragActivityToCanvas(AuthorConstants.Q_AND_A_TITLE);
		fla.drawTransitionBtwActivities();
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.FORUM_TITLE), 
				"The title " + AuthorConstants.FORUM_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.SHARE_RESOURCES_TITLE), 
				"The title " + AuthorConstants.SHARE_RESOURCES_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.KALTURA_TITLE), 
				"The title " + AuthorConstants.KALTURA_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.Q_AND_A_TITLE), 
				"The title " + AuthorConstants.Q_AND_A_TITLE + " was not found as an activity in the design");		
	}

	/**
	 * Gives the design a new name and saves it
	 */
	@Test(dependsOnMethods={"createDesign"})
	public void nameAndSaveDesign() {
		String saveResult = fla.saveDesign(randomDesignName);
		// System.out.println(saveResult);
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"Saving a design returned an unexpected message: "+ saveResult);

	}
	
	/**
	 * Clears the canvas
	 */
	@Test(dependsOnMethods={"nameAndSaveDesign"})
	public void cleanCanvas() {
		fla.newDesign().getAlertText();
		
		// Check that the design titled is back to untitled
		String newTitle = fla.getDesignName();
		Assert.assertTrue(newTitle.equals("Untitled"), "The canvas wasn't clean. Still shows: " + newTitle);
		
	}
	
	/**
	 * Opens a design
	 */
	@Test(dependsOnMethods={"cleanCanvas"})
	public void reOpenDesign() {
		
		fla.openDesign(randomDesignName);
		Assert.assertEquals(fla.getDesignName(), randomDesignName);
	
		
	}
	
	
	/**
	 * Arranges design activities using "Arrange" menu button
	 */
	@Test(dependsOnMethods={"reOpenDesign"})
	public void reArrangeDesign() {
		
		fla.arrangeDesign();
		
	}
	
	/**
	 * SaveAs a design
	 */
	@Test(dependsOnMethods={"reOpenDesign"})
	public void saveAsDesign() {
		
		String newDesignName = "Re" + randomDesignName;
		String saveResult = fla.saveAsDesign(newDesignName);
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG));
		Assert.assertEquals(fla.getDesignName(), newDesignName, "The design name saved is incorrect");
	}
	
	
	/**
	 * Change activity title
	 */
	@Test(dependsOnMethods={"saveAsDesign"})
	public void changeActivityTitle() {
		
		String forumNewTitle =  "New " + AuthorConstants.FORUM_TITLE;
		String kalturaNewTitle = "New " + AuthorConstants.KALTURA_TITLE;
		
		fla.changeActivityTitle(AuthorConstants.FORUM_TITLE, forumNewTitle);
		fla.changeActivityTitle(AuthorConstants.KALTURA_TITLE, kalturaNewTitle);
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		Assert.assertTrue(allActivityTitles.contains(forumNewTitle), 
				"The title " + forumNewTitle + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(kalturaNewTitle), 
				"The title " + kalturaNewTitle + " was not found as an activity in the design");
		
				
	}
	

	/**
	 * Creates a design using a grouping activity
	 */
	@Test(dependsOnMethods={"changeActivityTitle"})
	public void createGroupDesign () {
		
		cleanCanvas();
		fla.dragGroupToCanvas();
		fla.arrangeDesign();
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 150, 100);
		fla.arrangeDesign();
		
		fla.drawTransitionBtwActivities();
		
		String newGroupTitle = GROUP_ACTIVITY_NAME; // + GROUP_TITLE; 
		fla.changeActivityTitle(AuthorConstants.GROUP_TITLE, newGroupTitle);
		
		fla.setGroupForActivity(GROUP_ACTIVITY_NAME, AuthorConstants.FORUM_TITLE);
		
		String designName = randomDesignName + "-" + AuthorConstants.GROUP_TITLE;
		
		fla.saveAsDesign(designName);
		
		Assert.assertEquals(fla.getDesignName(), designName, 
				"Design name is wrong. It should be: " + designName);
		
	}

	
	/**
	 * Draws a new Q&A Activity
	 * 
	 */
	@Test(dependsOnMethods={"createGroupDesign"})
	public void drawQaActivity() {
		
		fla.dragActivityToCanvas(AuthorConstants.Q_AND_A_TITLE);
		
		boolean activityExists = fla.activityExists(AuthorConstants.Q_AND_A_TITLE);
		
		Assert.assertTrue(activityExists, 
				"The activity " + AuthorConstants.Q_AND_A_TITLE + " is not present in design");
		
	}
	
	/**
	 * Saves an invalid design
	 * 
	 */
	@Test(dependsOnMethods={"drawQaActivity"})
	public void saveInvalidDesign() {
		
		String saveResult = fla.saveDesign();
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_INVALID_MSG), 
				"The save design returns an unexpected message: " + saveResult );
		
	}
	
	/**
	 * Fix validation and saves a valid design
	 * 
	 */
	@Test(dependsOnMethods={"saveInvalidDesign"})
	public void fixValidation() {
		
		fla.drawTransitionFromTo(AuthorConstants.FORUM_TITLE, AuthorConstants.Q_AND_A_TITLE);
		
	}
	
	/**
	 * Saves design
	 * 
	 */
	@Test(dependsOnMethods={"fixValidation"})
	public void saveDesign() {
		
		String saveResult = fla.saveDesign();
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"The save design returns an invalid message: " + saveResult );
		
	}
	
	/**
	 * Set group settings to random groups
	 * 
	 * Sets up some groups and modifications
	 * 
	 */
	@Test(dependsOnMethods={"saveDesign"})
	public void changeGroupSettingsToRandom() {
		
		
		final String groupTypeRandom = "random";
		
		// set random grouping and number of learners to 3
		fla.setGroups(GROUP_ACTIVITY_NAME, groupTypeRandom, true, 3, "", "");
		
		/// Get assertions
		String testGroupType = fla.getGroupType(GROUP_ACTIVITY_NAME);
		Assert.assertEquals(testGroupType, groupTypeRandom, "The group type returned is incorrect");
		
		saveDesign();
		
	}
	
	/**
	 * Change Group settings to select in monitor
	 * 
	 * Sets up some groups and modifications
	 * 
	 */
	@Test(dependsOnMethods={"changeGroupSettingsToRandom"})
	public void changeGroupSettingsToMonitor() {
		
		final String groupTypeMonitor = "monitor";
		
		// set teacher selection (monitor) 4 groups and pass names
		fla.setGroups(GROUP_ACTIVITY_NAME, groupTypeMonitor, true, 4, "Group Blue, Group Yellow, Group Red, Group Orange", "");
				
		/// Get assertions
		String testGroupType = fla.getGroupType(GROUP_ACTIVITY_NAME);
		Assert.assertEquals(testGroupType, groupTypeMonitor, "The group type returned is incorrect");
		
		saveDesign();
		
	}
	
	/**
	 * Change Group settings to learner selection
	 * 
	 * Sets up some groups and modifications
	 * 
	 */
	@Test(dependsOnMethods={"changeGroupSettingsToMonitor"})
	public void changeGroupSettingsToLearner() {

		final String groupTypeLearner = "learner";
		
		// set learner choice, 5 groups, equal group sizes, view learners before select
		fla.setGroups(GROUP_ACTIVITY_NAME, groupTypeLearner, false, 5, "", "true,true");
				
		/// Get assertions
		String testGroupType = fla.getGroupType(GROUP_ACTIVITY_NAME);
		Assert.assertEquals(testGroupType, groupTypeLearner, "The group type returned is incorrect");
		
		saveDesign();
	
	}
	
	
	/**
	 * Adds description to design
	 * 
	 */
	@Test(dependsOnMethods={"changeGroupSettingsToLearner"})
	public void addDesignDescription() {
		
		
		String designDescription = "This is the <strong>description</strong> for this design";
		
		// opens dialog
		fla.designDescription()
		.openDesignDescriptionDialog();
		
		// Adds description
		fla.designDescription()
		.addDesignDescription(designDescription);
		
		
		saveDesign();

		String designName = fla.getDesignName();
		
		cleanCanvas();
		
		fla.openDesign(designName);
		
		String assertDesignDescription  = fla.designDescription().getDesignDescription();
				
		Assert.assertTrue((assertDesignDescription.contains(designDescription)),
				"The description we've got is not accurate");
		

		
	}
	
	/**
	 * Adds license to design
	 * 
	 */
	@Test(dependsOnMethods={"addDesignDescription"})
	public void addDesignLicense() {
		
		final String licenseText = "LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 2.5";
		
		// opens dialog
		fla.designDescription()
		.openDesignDescriptionDialog();
		
		int licenseId = 1;
		// sets license
		
		String assertLicense = fla.designDescription().addDesignLicense(licenseId);

		// close description UI component
		fla.designDescription().closeDesignDescriptionDialog();
		
		Assert.assertEquals(assertLicense, licenseText, "The license text does not match the expected one");
		
		saveDesign();
		
	}
	
		
	/**
	 * Copy/Pastes an activity
	 * 
	 */
	@Test(dependsOnMethods={"addDesignLicense"})
	public void copyPasteActivity() {
		
		String copyOfActivity = "Copy of " + AuthorConstants.Q_AND_A_TITLE;
		fla.copyPasteActivity(AuthorConstants.Q_AND_A_TITLE);
		
		Boolean activityExists = fla.activityExists(copyOfActivity);
	
		Assert.assertTrue(activityExists, 
				"Sorry, activity " + copyOfActivity + " doesn't exist in design.");
		
		saveInvalidDesign();
		
	}
	
	/**
	 * Arranges design
	 * 
	 */
	@Test(dependsOnMethods={"copyPasteActivity"})
	public void arrangeDesign() {
		
		Point initialActivityLocation = fla.getActivityLocation(AuthorConstants.Q_AND_A_TITLE);
		
		reArrangeDesign();
		
		Point newActivityLocation = fla.getActivityLocation(AuthorConstants.Q_AND_A_TITLE);

		Assert.assertFalse(initialActivityLocation.equals(newActivityLocation), 
				"The activity " + AuthorConstants.Q_AND_A_TITLE + " is still in the same location!" );
		
		saveInvalidDesign();
		
	}	
	
	
	/**
	 * Delete an activity
	 * 
	 *  Deletes a given activity by throwing it in the trash bin. 
	 *  
	 */
	@Test(dependsOnMethods={"arrangeDesign"})
	public void deleteActivity() {
		
		String copyOfActivity = "Copy of " + AuthorConstants.Q_AND_A_TITLE;
		fla.deleteActivity(copyOfActivity);
		
		Boolean activityExists = fla.activityExists(copyOfActivity);
	
		Assert.assertFalse(activityExists, 
				"Sorry, activity " + copyOfActivity + " still exists in design.");
		
		saveDesign();
		
	}
	
	

	
	
}
