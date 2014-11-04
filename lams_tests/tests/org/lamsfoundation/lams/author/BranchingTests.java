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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.author.ConditionsPropertiesPage;
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

	// Branching constants 
	public static final String BRANCHING_TYPE_INSTRUCTOR = "chosen";
	public static final String BRANCHING_TYPE_GROUP = "group";
	public static final String BRANCHING_TYPE_LEARNER_OUTPUT = "tool";
	public static final String BRANCHING_TYPE_LEARNER_CHOICE = "optional";
	
	
	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);
	
	// Learner choice constants
	private static final String MAXNUMBERSEQUENCES = "2";
	private static final String MINNUMBERSEQUENCES = "1";

	// Set number of groups for group based branching
	private static final int numberOfGroups = 3;
	
	// Validation error msg
	private static final String SAVE_VALIDATION_GROUP_UNASSIGNED_MSG= 
			"A Group Based Branching Activity must have all Groups assigned to Branches";
	

	
	private String randomDesignName = "Design-" + RANDOM_INT;
	private String randomBranchingName = "Branch" + RANDOM_INT;

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
	 * Creates a design with three activities before we do branching
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void createDesignPreBranching() {

		// Drop activities in canvas
		fla
		 .dragActivityToCanvasPosition(AuthorConstants.NOTICEBOARD_TITLE, 200, 150)
		 .dragGroupToCanvas();
		
		// Set grouping activity to have 3 groups - so we can match one to one to branches
		fla.setGroups(AuthorConstants.GROUP_TITLE, "random", true, numberOfGroups, "", "");

		fla
		 .dragActivityToCanvasPosition(AuthorConstants.SHARE_RESOURCES_TITLE, 200, 250)
		 .arrangeDesign();
		
		// Draw transitions
		fla.drawTransitionBtwActivities();


		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
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

		fla.dragActivityToCanvasPosition(AuthorConstants.PIXLR_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y - 100));

		fla.dragActivityToCanvasPosition(AuthorConstants.COMMON_CARTRIDGE_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y ));

		fla.dragActivityToCanvasPosition(AuthorConstants.DATA_COLLECTION_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y + 100));

		/// Assertions
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
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

		Assert.assertEquals(assertBranchingGroup.trim(), AuthorConstants.GROUP_TITLE, "Grouping selection is wrong");


	}


	/**
	 * Set new branching title
	 */
	@Test(dependsOnMethods="setGroupInGroupBranching")
	public void setBranchingTitle() {

		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE).setBranchingTitle(randomBranchingName);

		// Assert
		String assertBranchingTitle = fla.branchingProperties(randomBranchingName).getBranchingTitle();

		//System.out.println("assertBranchingTitle: " + assertBranchingTitle + " " + randomBranchingName);
		
		Assert.assertEquals(assertBranchingTitle.trim(), randomBranchingName, "Branching title is wrong");

	}


	/**
	 * Saves the designs but it expects an error as groups and branches are not matched.
	 */
	@Test(dependsOnMethods="setBranchingTitle")
	public void nameAndSaveDesignWithError() {

		String saveResult = fla.saveAsDesign(randomDesignName + "-" + BRANCHING_TYPE_GROUP);

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
	
	
	/**
	 * Prepares for a new design for
	 */
	@Test(dependsOnMethods="saveDesign")
	public void clearCanvas() {
		
		fla.newDesign().getAlertText();

	}

	/**
	 * Verifies the details for the group based branching
	 */
	@Test(dependsOnMethods="clearCanvas")
	public void reOpenAndVerifyGroupBranching() {
		
		fla.openDesign(randomDesignName + "-" + BRANCHING_TYPE_GROUP);
		
		
		// Assertions
		// Assert branching settings
		
		String assertBranchingType = fla.branchingProperties(randomBranchingName)
		.getBranchingType();
		
		// Assert branching type
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_GROUP, "Branching type error");
		
		// assert branching grouping
		String assertBranchingGroup = fla.branchingProperties(randomBranchingName)
				.getGroupInGroupBranchingType();

		Assert.assertEquals(assertBranchingGroup.trim(), AuthorConstants.GROUP_TITLE, "Grouping selection is wrong");
	}
	
	
	/**
	 * Creates a design with three activities before we do branching
	 */
	@Test(dependsOnMethods={"reOpenAndVerifyGroupBranching"})
	public void createDesignLearnerChoiceBranching() {

		clearCanvas();
		// Drop activities in canvas
		fla
		.dragActivityToCanvasPosition(AuthorConstants.NOTICEBOARD_TITLE, 200, 150)
		.dragActivityToCanvasPosition(AuthorConstants.GMAP_TITLE, 200, 250)
		.arrangeDesign();

		// Draw transitions
		fla.drawTransitionBtwActivities();

		// Drop branch into canvas
		fla.dragBranchToCanvas();

		// Get position for branching start point (we'll use this to present 
		// the activities better on the screen)
		Point startBranchingPosition = fla.getActivityLocation(AuthorConstants.BRANCHING_START_TITLE);
		
		fla
		.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y - 200))
			.dragActivityToCanvasPosition(AuthorConstants.IMAGE_GALLERY_TITLE, 
				(startBranchingPosition.x + 230), (startBranchingPosition.y - 200))
			.dragActivityToCanvasPosition(AuthorConstants.MINDMAP_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y -80))
			.dragActivityToCanvasPosition(AuthorConstants.MULTIPLE_CHOICE_TITLE, 
				(startBranchingPosition.x + 50), (startBranchingPosition.y + 50))
			.dragActivityToCanvasPosition(AuthorConstants.DATA_COLLECTION_TITLE, 
				(startBranchingPosition.x + 230), (startBranchingPosition.y + 50))
			.dragActivityToCanvasPosition(AuthorConstants.EADVENTURE_TITLE, 
				(startBranchingPosition.x + 400), (startBranchingPosition.y + 50));

		// Draw branches
		// Branch 1
		fla
		.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.FORUM_TITLE)
		.drawTransitionFromTo(AuthorConstants.FORUM_TITLE, 
				AuthorConstants.IMAGE_GALLERY_TITLE);

		// Branch 2
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.MINDMAP_TITLE)
			.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.MULTIPLE_CHOICE_TITLE)
			.drawTransitionFromTo(AuthorConstants.MULTIPLE_CHOICE_TITLE, 
				AuthorConstants.DATA_COLLECTION_TITLE)
			.drawTransitionFromTo(AuthorConstants.DATA_COLLECTION_TITLE, 
				AuthorConstants.EADVENTURE_TITLE);			
		
		
		// Draw transition from last activity to branching start
		fla.drawTransitionFromTo(AuthorConstants.GMAP_TITLE, 
				AuthorConstants.BRANCHING_START_TITLE);
		
		// Link activity to branch ending point
		fla.drawTransitionFromTo(AuthorConstants.EADVENTURE_TITLE, 
				AuthorConstants.BRANCH_END_TITLE);

		fla.drawTransitionFromTo(AuthorConstants.MINDMAP_TITLE, 
				AuthorConstants.BRANCH_END_TITLE);	
		
		fla.drawTransitionFromTo(AuthorConstants.IMAGE_GALLERY_TITLE, 
				AuthorConstants.BRANCH_END_TITLE);			
		
		
		// Set branching as learner choice
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setBranchingType(BRANCHING_TYPE_LEARNER_CHOICE);
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setLearnerChoiceMinSequences(MINNUMBERSEQUENCES)
		.setLearnerChoiceMaxSequences(MAXNUMBERSEQUENCES);
		
		
		
		
		/// Assertions now
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCHING_START_TITLE), 
				"The title " + AuthorConstants.BRANCHING_START_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCH_END_TITLE), 
				"The title " + AuthorConstants.BRANCH_END_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.NOTICEBOARD_TITLE), 
				"The title " + AuthorConstants.NOTICEBOARD_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.GMAP_TITLE), 
				"The title " + AuthorConstants.GMAP_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.FORUM_TITLE), 
				"The title " + AuthorConstants.FORUM_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.IMAGE_GALLERY_TITLE), 
				"The title " + AuthorConstants.IMAGE_GALLERY_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.MINDMAP_TITLE), 
				"The title " + AuthorConstants.MINDMAP_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.MULTIPLE_CHOICE_TITLE), 
				"The title " + AuthorConstants.MULTIPLE_CHOICE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.DATA_COLLECTION_TITLE), 
				"The title " + AuthorConstants.DATA_COLLECTION_TITLE + " was not found as an activity in the design");	
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.EADVENTURE_TITLE), 
				"The title " + AuthorConstants.EADVENTURE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 1"), 
				"Branch 1 was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 2"), 
				"Branch 2 was not found as an activity in the design");		
		Assert.assertTrue(allActivityTitles.contains("Branch 3"), 
				"Branch 3 was not found as an activity in the design");	
		
		
		// Assert branching settings
		
		String assertBranchingType = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.getBranchingType();
		
		String assertMinSequences = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getLearnerChoiceMinSequences();
		
		String assertMaxSequences = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getLearnerChoiceMaxSequences();
		
		// Assert branching type
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_LEARNER_CHOICE, "Branching type error");
		
		// Asserts min/max sequences
		
		Assert.assertEquals(assertMinSequences, MINNUMBERSEQUENCES, "Minimal number of sequences incorrect");
		Assert.assertEquals(assertMaxSequences, MAXNUMBERSEQUENCES, "Maximal number of sequences incorrect");
		
	}

	/**
	 * Gives a name and saves the designs 
	 */
	@Test(dependsOnMethods="createDesignLearnerChoiceBranching")
	public void nameAndSaveDesignLearnerChoiceDesign() {

		String saveResult = fla.saveAsDesign(randomDesignName + "-" + BRANCHING_TYPE_LEARNER_CHOICE);

		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"Save error. Returned: " + saveResult);
	}
	
	/**
	 * Reopen previously saved design to confirm settings were saved correctly 
	 */
	@Test(dependsOnMethods="nameAndSaveDesignLearnerChoiceDesign")
	public void reOpenAndVerifyLearnerChoiceDesign() {

		clearCanvas();
		
		fla.openDesign(randomDesignName + "-" + BRANCHING_TYPE_LEARNER_CHOICE);
		
		
		// Assertions
		// Assert branching settings
		
		String assertBranchingType = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.getBranchingType();
		
		String assertMinSequences = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getLearnerChoiceMinSequences();
		
		String assertMaxSequences = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getLearnerChoiceMaxSequences();
		
		// Assert branching type
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_LEARNER_CHOICE, "Branching type error");
		
		// Asserts min/max sequences
		
		Assert.assertEquals(assertMinSequences, MINNUMBERSEQUENCES, "Minimal number of sequences incorrect");
		Assert.assertEquals(assertMaxSequences, MAXNUMBERSEQUENCES, "Maximal number of sequences incorrect");
		
	}
	
	/**
	 * Creates a teacher choice branching by dragging transitions from the same activity
	 */
	@Test(dependsOnMethods="reOpenAndVerifyLearnerChoiceDesign")
	public void createTeacherChoiceBranching() {
		
		clearCanvas();
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.CHAT_AND_SCRIBE_TITLE, 200, 150)
		.dragActivityToCanvasPosition(AuthorConstants.NOTEBOOK_TITLE, 400, 100)
		.dragActivityToCanvasPosition(AuthorConstants.WIKI_TITLE, 400, 250)
		.dragActivityToCanvasPosition(AuthorConstants.WOOKIE_TITLE, 400, 400);
		// fla.arrangeDesign();

		// Draw transitions
		List<String> toActivities = new ArrayList<>();
		toActivities.add(AuthorConstants.NOTEBOOK_TITLE);
		toActivities.add(AuthorConstants.WIKI_TITLE);
		toActivities.add(AuthorConstants.WOOKIE_TITLE);
		
		fla.drawBranchingFromActivity(AuthorConstants.CHAT_AND_SCRIBE_TITLE, toActivities)
		.arrangeDesign();
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setBranchingType(BRANCHING_TYPE_INSTRUCTOR);
		
		/// Assertions now
		// Branching type
		
		String assertBranchingType = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getBranchingType();
		
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_INSTRUCTOR, "Branching type is incorrect");
		
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCHING_START_TITLE), 
				"The title " + AuthorConstants.BRANCHING_START_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCH_END_TITLE), 
				"The title " + AuthorConstants.BRANCH_END_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.NOTEBOOK_TITLE), 
				"The title " + AuthorConstants.NOTEBOOK_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.CHAT_AND_SCRIBE_TITLE), 
				"The title " + AuthorConstants.CHAT_AND_SCRIBE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.WIKI_TITLE), 
				"The title " + AuthorConstants.WIKI_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.WOOKIE_TITLE), 
				"The title " + AuthorConstants.WOOKIE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 1"), 
				"Branch 1 was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 2"), 
				"Branch 2 was not found as an activity in the design");		
		Assert.assertTrue(allActivityTitles.contains("Branch 3"), 
				"Branch 3 was not found as an activity in the design");	

		
		
	}
	
	/**
	 * Gives a name and saves the designs 
	 */
	@Test(dependsOnMethods="createTeacherChoiceBranching")
	public void nameAndSaveDesignTeacherChoiceDesign() {

		String saveResult = fla.saveAsDesign(randomDesignName + "-" + BRANCHING_TYPE_INSTRUCTOR);

		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"Save error. Returned: " + saveResult);
	}
	
	/**
	 * Reopen previously saved design to confirm settings were saved correctly 
	 */
	@Test(dependsOnMethods="nameAndSaveDesignTeacherChoiceDesign")
	public void reOpenAndVerifyTeacherChoiceDesign() {

		clearCanvas();
		
		fla.openDesign(randomDesignName + "-" + BRANCHING_TYPE_INSTRUCTOR);
		
		
		// Assertions
		// Assert branching settings
		
		String assertBranchingType = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.getBranchingType();
		
		// Assert branching type
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_INSTRUCTOR, "Branching type error");
		
		
	}

	
	
	
	
	/**
	 * Creates a tool output branching by dragging transitions from the same activity
	 * 
	 * Here we create a small sequence with an tool input from an MCQ. 
	 * MCQ by default has 1 question with a 1 mark output. So we create two conditions for total 
	 * score:
	 * 
	 *  1) Zero condition, if the user gets just zero (wrong answer)
	 *  2) One condition, if the user gets one mark (answers correctly)
	 *  
	 *  Then, we match the Zero condition to Branch 2 and One condition to Branch 1(default)
	 * 
	 * 
	 */
	@Test(dependsOnMethods="reOpenAndVerifyTeacherChoiceDesign")
	public void createToolOutputBranching() {
		
		// Test data
		String rangeValueZero = "0";
		String rangeValueOne = "1";
		
		String conditionZeroRange = "Zero condition";
		String conditionOneRange  = "One condition";
		
		String branchOne = "Branch 1";
		String branchTwo = "Branch 2";
		
				
		clearCanvas();
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.MULTIPLE_CHOICE_TITLE, 200, 150)
		.dragActivityToCanvasPosition(AuthorConstants.NOTEBOOK_TITLE, 400, 100)
		.dragActivityToCanvasPosition(AuthorConstants.WIKI_TITLE, 400, 250);


		// Draw transitions
		List<String> toActivities = new ArrayList<>();
		toActivities.add(AuthorConstants.NOTEBOOK_TITLE);
		toActivities.add(AuthorConstants.WIKI_TITLE);
		
		fla.drawBranchingFromActivity(AuthorConstants.MULTIPLE_CHOICE_TITLE, toActivities)
		.arrangeDesign();
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setBranchingType(BRANCHING_TYPE_LEARNER_OUTPUT);
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setInputTool(AuthorConstants.MULTIPLE_CHOICE_TITLE)
		.clickCreateConditions()
		.setConditionOutput(ConditionsPropertiesPage.OUTPUT_MCQ_TOTAL_MARK)
		.setOptionType(ConditionsPropertiesPage.OPTION_RANGE)
		.setFromRangeValue(rangeValueZero)
		.setToRangeValue(rangeValueZero)
		.clickAddOptionRange()
		.setConditionName(conditionZeroRange, "2")
		.setFromRangeValue(rangeValueOne)
		.setToRangeValue(rangeValueOne)
		.clickAddOptionRange()
		.setConditionName(conditionOneRange, "3")
		.clickOkConditionsButton()
		.matchConditionToBranch(conditionOneRange, branchOne)
		.matchConditionToBranch(conditionZeroRange, branchTwo)
		.clickOkMatchingConditionsToBranchesButton();
		
		/// Assertions now
		// Branching type
		
		String assertBranchingType = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getBranchingType();
		
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_LEARNER_OUTPUT, "Branching type is incorrect");
	
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCHING_START_TITLE), 
				"The title " + AuthorConstants.BRANCHING_START_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.BRANCH_END_TITLE), 
				"The title " + AuthorConstants.BRANCH_END_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.MULTIPLE_CHOICE_TITLE), 
				"The title " + AuthorConstants.MULTIPLE_CHOICE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.NOTEBOOK_TITLE), 
				"The title " + AuthorConstants.NOTEBOOK_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.WIKI_TITLE), 
				"The title " + AuthorConstants.WIKI_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 1"), 
				"Branch 1 was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains("Branch 2"), 
				"Branch 2 was not found as an activity in the design");		
		
	}
	
	/**
	 * Gives a name and saves the designs 
	 */
	@Test(dependsOnMethods="createToolOutputBranching")
	public void nameAndSaveDesignToolOutputDesign() {

		String saveResult = fla.saveAsDesign(randomDesignName + "-" + BRANCHING_TYPE_LEARNER_OUTPUT);

		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"Save error. Returned: " + saveResult);
	}
	
	
	
	/**
	 * Reopen previously saved design to confirm settings were saved correctly 
	 */
	@Test(dependsOnMethods="nameAndSaveDesignToolOutputDesign")
	public void reOpenAndVerifyToolOutputDesign() {
		
		// Test data
		final String conditionZeroRange = "Zero condition";
		final String conditionOneRange  = "One condition";
		
		final String branchOne = "Branch 1";
		final String branchTwo = "Branch 2";
		
		clearCanvas();
		
		fla.openDesign(randomDesignName + "-" + BRANCHING_TYPE_LEARNER_OUTPUT);
		
		
		// Assertions
		// Assert branching settings
		
		String assertBranchingType = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.getBranchingType();
		
		String assertInputTool = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.getInputTool();

		String assertToolOutput = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.clickCreateConditions().getConditionOutput();
		
		
		List<String> assertAllConditions = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.clickCreateConditions().getConditionNames();
		
		List<String> assertAllMappings = fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
				.clickMatchConditionsToBranchesButton().getAllMappings();

		// Assert branching type
		Assert.assertEquals(assertBranchingType, BRANCHING_TYPE_LEARNER_OUTPUT, "Branching type error");
		
		// AssertInputtool
		Assert.assertEquals(assertInputTool, AuthorConstants.MULTIPLE_CHOICE_TITLE, 
				"The input tool is incorrect");
		
		// Assert Tool output
		Assert.assertEquals(assertToolOutput, ConditionsPropertiesPage.OUTPUT_MCQ_TOTAL_MARK,
				"The tool output is incorrect");
		
		// Assert condition lists
		Assert.assertTrue(assertAllConditions.contains(conditionZeroRange), 
				conditionZeroRange + "is not conditions list");
		Assert.assertTrue(assertAllConditions.contains(conditionOneRange), 
				conditionOneRange + "is not conditions list");
		
		// Assert conditions to branches mapping
		Assert.assertTrue(assertAllMappings.contains(conditionOneRange + " matches " + branchOne), 
				"Error " + conditionOneRange + " doesn't match with " + branchOne);
		Assert.assertTrue(assertAllMappings.contains(conditionZeroRange + " matches " + branchTwo), 
				"Error " + conditionZeroRange + " doesn't match with " + branchTwo);
		
		clearCanvas();
		
	}
	
	@Test(dependsOnMethods="reOpenAndVerifyToolOutputDesign")
	public void createBranchingWithinBranching() {
		
		String firstBranching = "Branching1";
		String secondBranching = "Branching2";
		
		// Drop activities in canvas
		fla
		 .dragActivityToCanvasPosition(AuthorConstants.NOTICEBOARD_TITLE, 200, 150);

		// draw branching
		fla.dragBranchToCanvas();

		// draw transition
		fla.drawTransitionFromTo(AuthorConstants.NOTICEBOARD_TITLE, AuthorConstants.BRANCHING_START_TITLE);
		
		// rename branching
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setBranchingType(BRANCHING_TYPE_INSTRUCTOR)
		.setBranchingTitle(firstBranching);
		
		fla.dragActivityToCanvasPosition(AuthorConstants.CHAT_TITLE, 300, 250)
		.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 450, 450);
		
		fla.drawTransitionFromTo(firstBranching + " start", AuthorConstants.CHAT_TITLE)
		.drawTransitionFromTo(firstBranching + " start", AuthorConstants.FORUM_TITLE);
		
		fla.dragBranchesToCanvasPosition(400, 580)
		.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.setBranchingTitle(secondBranching)
		.setBranchingType(BRANCHING_TYPE_INSTRUCTOR);
		
		fla.dragActivityToCanvasPosition(AuthorConstants.GMAP_TITLE, 300, 100)
		.dragActivityToCanvasPosition(AuthorConstants.IMAGE_GALLERY_TITLE, 400, 350);
		
		fla.drawTransitionFromTo(secondBranching + " start", AuthorConstants.GMAP_TITLE)
		.drawTransitionFromTo(secondBranching + " start", AuthorConstants.IMAGE_GALLERY_TITLE);
		
		fla.drawTransitionFromTo(AuthorConstants.FORUM_TITLE, secondBranching + " start")
		.drawTransitionFromTo(AuthorConstants.CHAT_TITLE, firstBranching + " end")
		.drawTransitionFromTo(AuthorConstants.IMAGE_GALLERY_TITLE, secondBranching + " end")
		.drawTransitionFromTo(AuthorConstants.GMAP_TITLE, secondBranching + " end"); 
		
		fla.arrangeDesign();
		
	}
	
	/**
	 * Gives a name and saves the designs 
	 */
	@Test(dependsOnMethods="createBranchingWithinBranching")
	public void nameAndSaveDesignBranchingWithinBranching() {

		String saveResult = fla.saveAsDesign(randomDesignName + "- Branching within Branching");

		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"Save error. Returned: " + saveResult);
	}
	
}

