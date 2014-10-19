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
 * Branching tests
 * 
 *  - Set group based branching
 *  - Set learner's choice branching
 *  - Set instructor's choice branching
 *  - Set learner's output branching
 *  
 *    
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class BranchingTests {

	private static final String BRANCHING_TYPE_INSTRUCTOR = "chosen";
	private static final String BRANCHING_TYPE_GROUP = "group";
	private static final String BRANCHING_TYPE_LEARNER_OUTPUT = "tool";
	private static final String BRANCHING_TYPE_LEARNER_CHOICE = "optional";
	private static final String randomInt = LamsUtil.randInt(0, 9999);
	
	// Validation error msg
	private static final String SAVE_VALIDATION_GROUP_UNASSIGNED_MSG= 
			"A Group Based Branching Activity must have all Groups assigned to Branches";
	
	// Set number of groups for group based branching
	private static final int numberOfGroups = 3;
	
	private String randomDesignName = "Design-" + randomInt;
	private String randomBranchingName = "Branch" + randomInt;

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
		// driver.quit();
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
	 * Creates a design with three activities before we do branching
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void createDesignPreBranching() {

		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.NOTICEBOARD_TITLE, 200, 150);
		fla.dragGroupToCanvas();

		// Set grouping activity to have 3 groups - so we can match one to one to branches
		fla.setGroups(AuthorConstants.GROUP_TITLE, "random", true, numberOfGroups, "", "");

		fla.dragActivityToCanvasPosition(AuthorConstants.SHARE_RESOURCES_TITLE, 200, 250);
		fla.arrangeDesign();

		// Draw transitions
		fla.drawTransitionBtwActivities();


		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		System.out.println("All activities:" + allActivityTitles);

		// Assert that all of them are in the design
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.NOTICEBOARD_TITLE), 
				"The title " + AuthorConstants.NOTICEBOARD_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.GROUP_TITLE), 
				"The title " + AuthorConstants.GROUP_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.SHARE_RESOURCES_TITLE), 
				"The title " + AuthorConstants.SHARE_RESOURCES_TITLE + " was not found as an activity in the design");

	}



	@Test(dependsOnMethods="createDesignPreBranching")
	public void drawBranching() {	
		// Drop branch into canvas
		fla.dragBranchToCanvas();

		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		System.out.println("All activities:" + allActivityTitles);

		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCHING_START_TITLE), 
				"The title " + AuthorConstants.BRANCHING_START_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCH_END_TITLE), 
				"The title " + AuthorConstants.BRANCH_END_TITLE + " was not found as an activity in the design");

	}

	@Test(dependsOnMethods="drawBranching")
	public void drawActivitesToBranch() {	

		// Get position for branching start point (we'll use this to present 
		// the activities better on the screen)
		Point startBranchingPosition = fla.getActivityLocation(AuthorConstants.BRANCHING_START_TITLE);

		System.out.println("Branching start position:" + startBranchingPosition);

		fla.dragActivityToCanvasPosition(AuthorConstants.PIXLR_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y - 100));

		fla.dragActivityToCanvasPosition(AuthorConstants.COMMON_CARTRIDGE_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y ));

		fla.dragActivityToCanvasPosition(AuthorConstants.DATA_COLLECTION_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y + 100));

		/// Assertions
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		System.out.println("All activities:" + allActivityTitles);

		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.PIXLR_TITLE), 
				"The title " + AuthorConstants.PIXLR_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.COMMON_CARTRIDGE_TITLE), 
				"The title " + AuthorConstants.COMMON_CARTRIDGE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.DATA_COLLECTION_TITLE), 
				"The title " + AuthorConstants.DATA_COLLECTION_TITLE + " was not found as an activity in the design");

	}

	@Test(dependsOnMethods="drawActivitesToBranch")
	public void drawBranches() {	
		// Create the three branches
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.PIXLR_TITLE);

		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.COMMON_CARTRIDGE_TITLE);

		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.DATA_COLLECTION_TITLE);

		/// Assertions 
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		System.out.println("All activities:" + allActivityTitles);

		Assert.assertTrue(allActivityTitles.contains("Branch 1"), 
				"Branch 1 was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 2"), 
				"Branch 2 was not found as an activity in the design");		
		Assert.assertTrue(allActivityTitles.contains("Branch 3"), 
				"Branch 3 was not found as an activity in the design");		

	}

	@Test(dependsOnMethods="drawBranches")
	public void drawBranchClosing() {	
		// Link activity to branch ending point
		fla.drawTransitionFromTo(AuthorConstants.PIXLR_TITLE, 
				AuthorConstants.BRANCH_END_TITLE);

		fla.drawTransitionFromTo(AuthorConstants.COMMON_CARTRIDGE_TITLE, 
				AuthorConstants.BRANCH_END_TITLE);		

	}

	@Test(dependsOnMethods="drawBranches")
	public void connectBranchingToSequence() {	

		// Draw transition from last activity to branching start
		fla.drawTransitionFromTo(AuthorConstants.SHARE_RESOURCES_TITLE, 
				AuthorConstants.BRANCHING_START_TITLE);

	}

	/**
	 * Set group based branching
	 */
	@Test(dependsOnMethods="connectBranchingToSequence")
	public void setGroupBasedBranching() {

		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE).setBranchingType(BRANCHING_TYPE_GROUP);

		// Assert
		String assertBranchingGroup = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getBranchingType();

		Assert.assertEquals(assertBranchingGroup, BRANCHING_TYPE_GROUP, "Branching type is wrong");

	}

	/**
	 * As this is a group based branching, selects the grouping to be used 
	 */
	@Test(dependsOnMethods="setGroupBasedBranching")
	public void setGroupInGroupBranching() {

		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setGroupInGroupBranchingType(AuthorConstants.GROUP_TITLE + " ");

		// Assert
		String assertBranchingGroup = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getGroupInGroupBranchingType();

		Assert.assertEquals(assertBranchingGroup.trim(), AuthorConstants.GROUP_TITLE, "Branching type is wrong");


	}


	/**
	 * Set new branching title
	 */
	@Test(dependsOnMethods="setGroupInGroupBranching")
	public void setBranchingTitle() {

		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE).setBranchingTitle(randomBranchingName);

		// Assert
		String assertBranchingTitle = fla.branchingProperties(randomBranchingName).getBranchingTitle();

		Assert.assertEquals(assertBranchingTitle.trim(), randomBranchingName, "Branching title is wrong");

	}


	/**
	 * Saves the designs but it expects an error as groups and branches are not matched.
	 */
	@Test(dependsOnMethods="setBranchingTitle")
	public void nameAndSaveDesignWithError() {

		String saveResult = fla.saveAsDesign(randomDesignName);

		Assert.assertTrue(saveResult.contains(SAVE_VALIDATION_GROUP_UNASSIGNED_MSG), 
				"Save error. Returned: " + saveResult);
	}

	/**
	 *  Matches the groups to branches
	 *  
	 *  This is a tricky thing as we need to use xpath to get the groups and then match them 
	 *  to the number of groups. We start from the top so Group1 will match Branch1, Group2
	 *  will match Branch2 and so on. 
	 */
	@Test(dependsOnMethods="nameAndSaveDesignWithError")
	public void matchGroupsToBranches() {

		fla.branchingProperties(randomBranchingName)
		.clickMatchGroupsToBranches();
		
		// We need to do this as many times as groups as we have
		for (int i = 1; i <= numberOfGroups; i++) {
			fla.branchingProperties(randomBranchingName).matchGroupToBranch("1", Integer.toString(i));
		}
		
		fla.branchingProperties(randomBranchingName).clickOkGroupButton();
		
	}

	/**
	 * Saves designs and expects a normal save response.
	 */
	@Test(dependsOnMethods="matchGroupsToBranches")
	public void saveDesign() {
		
		String saveResult = fla.saveDesign();
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"Error while saving design" + saveResult);

	}
	
	
	
	
}

