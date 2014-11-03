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

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.author.ConditionsPropertiesPage;
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *  LAMS Authoring common errors
 *  
 *  - attempt clear when unsaved design
 *  - Two transition going to the same activity
 *  - recursive transition act1 --> act2 --> act1  (LDEV-3354)
 *  - circular designs (LDEV-3355)
 *  - Overwrite warning
 *  - don't allow system activities in support and optional activities (LDEV-3356)
 *  - saving invalid designs:
 *  	- no transition
 *  	- no grouping assigned
 *  	- no branches set
 *  	- no set grouping branches
 *  	- no set branching conditions
 *  	- optional activities must have at least one act inside
 *  	- support activities must have at least on act inside
 *  	- no activity in design
 * 
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */

public class AuthorErrorsTests {
 
	
	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);


	private String randomDesignName = "Design-" + RANDOM_INT;
	
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
		Assert.assertEquals(AuthorConstants.FLA_TITLE, fla.getTitle(), 
				"The expected title is not present");


		
	}
	
	/**
	 * Clears the canvas
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void cleanCanvas() {
		fla.newDesign().getAlertText();
		
		// Check that the design titled is back to untitled
		String newTitle = fla.getDesignName();
		Assert.assertTrue(newTitle.equals("Untitled"), 
				"The canvas wasn't clean. Still shows: " + newTitle);
		
	}
	
	/**
	 * Attempt to clear the canvas when there's unsaved design.
	 */
	@Test(dependsOnMethods={"cleanCanvas"})
	public void attemptClearWhenUnsavedDesign() {

		fla.dragActivityToCanvasPosition(AuthorConstants.ASSESSMENT_TITLE, 200, 250);
		fla.dragActivityToCanvas(AuthorConstants.WIKI_TITLE);
		
		String assertOutput = fla.newDesign().getAlertText();
		
		// Assert error message
		Assert.assertTrue(assertOutput.contains(AuthorConstants.CLEAR_ON_UNSAVED_DESIGN), 
				"No alert message before clearing the design without saving was displayed");
		
		// Assert 
		String newTitle = fla.getDesignName();
		Assert.assertTrue(newTitle.equals("Untitled"), 
				"There's an unexpected design title");
		
	}
	
	/**
	 * Attempt to clear the canvas when there's unsaved design.
	 */
	@Test(dependsOnMethods={"attemptClearWhenUnsavedDesign"})
	public void attemptActivitiesTransitionToEachOther() {
		
		cleanCanvas();

		fla.dragActivityToCanvasPosition(AuthorConstants.MINDMAP_TITLE, 200, 450);
		fla.dragActivityToCanvas(AuthorConstants.MULTIPLE_CHOICE_TITLE);
		
		fla.drawTransitionBtwActivities();
		
		String alertTxt = fla.drawTransitionFromTo(AuthorConstants.MINDMAP_TITLE, 
				AuthorConstants.MULTIPLE_CHOICE_TITLE)
				.getAlertText();
	
		// Assert error message
		Assert.assertTrue(alertTxt.contains(AuthorConstants.RECURSIVE_TRANSITION_MSG), 
				"Unexpected message when attempting to bow transitions");
	
	}
	
	
	/**
	 * Attempt to clear the canvas when there's unsaved design.
	 */
	@Test(dependsOnMethods={"attemptActivitiesTransitionToEachOther"})
	public void attemptCircularDesign() {
		

		fla.dragActivityToCanvasPosition(AuthorConstants.ASSESSMENT_TITLE, 400, 450);
		
		fla.drawTransitionFromTo(AuthorConstants.MULTIPLE_CHOICE_TITLE, AuthorConstants.ASSESSMENT_TITLE);
		
		String alertTxt = fla.drawTransitionFromTo(AuthorConstants.ASSESSMENT_TITLE, 
				AuthorConstants.MINDMAP_TITLE)
				.getAlertText();
	
		// Assert error message
		Assert.assertTrue(alertTxt.contains(AuthorConstants.CIRCULAR_DESIGN_MSG), 
				"Unexpected message when attempting to create a circular design");
	
	}
	
	/**
	 * Attempt to clear the canvas when there's unsaved design.
	 */
	@Test(dependsOnMethods={"attemptCircularDesign"})
	public void attemptOverwriteDesign() {
		
		String alertTxt = fla.saveAsDesign(randomDesignName + "-overwrite");
		
		// Assert initial save
		Assert.assertTrue(alertTxt.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"The saving result should have been valid. We've got: " + alertTxt);

		alertTxt = fla.overWriteDesign(randomDesignName + "-overwrite");
		
		// Assert error message
		Assert.assertTrue(alertTxt.contains(AuthorConstants.SAVE_OVERWRITE_MSG), 
				"Unexpected message when attempting to overwrite a design");
	
	}
	
	/**
	 *  Attempts to put a system gate into a support activity
	 *  
	 *  @see LDEV-3356
	 */
	@Test(dependsOnMethods={"attemptOverwriteDesign"})
	public void attemptSystemActivityInSupportActivities() {
		

		cleanCanvas();
		fla.dragOptionalActivityToCanvas();
		
		// Try with Gate
		fla.dragGateToCanvas();
		
		String alertTxt = fla.dragActivityIntoOptionalActivity(AuthorConstants.OPTIONAL_ACTIVITY_TITLE, 
				AuthorConstants.GATE_TITLE).getAlertText();

		Assert.assertTrue(alertTxt.contains(AuthorConstants.ADD_SYSTEM_ACT_TO_OPTIONAL_MSG), 
				"No error message thrown!");

		// Try with Group
		fla.dragGroupToCanvas();

		alertTxt = fla.dragActivityIntoOptionalActivity(AuthorConstants.OPTIONAL_ACTIVITY_TITLE, 
				AuthorConstants.GROUP_TITLE).getAlertText();
		
		Assert.assertTrue(alertTxt.contains(AuthorConstants.ADD_SYSTEM_ACT_TO_OPTIONAL_MSG), 
				"No error message thrown!");
		
		// Try with branching
		fla.dragBranchToCanvas();
		
		alertTxt = fla.dragActivityIntoOptionalActivity(AuthorConstants.OPTIONAL_ACTIVITY_TITLE, 
				AuthorConstants.BRANCHING_START_TITLE).getAlertText();

		Assert.assertTrue(alertTxt.contains(AuthorConstants.ADD_SYSTEM_ACT_TO_OPTIONAL_MSG), 
				"No error message thrown!");
		
	
	}
		
	/**
	 * Save invalid design: no transitions
	 * 
	 *  Attempts to save an design that has no transitions. 
	 *  
	 */
	@Test(dependsOnMethods={"attemptSystemActivityInSupportActivities"})
	public void attemptSaveNoTransitionsDesign() {
		
		cleanCanvas();
		
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 200, 300);
		fla.dragActivityToCanvas(AuthorConstants.IMAGE_GALLERY_TITLE);

		String saveResult = fla.saveAsDesign(randomDesignName + RANDOM_INT + "-incompleteSave");

		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_INVALID_MSG),
				"The design should not be valid as it's missing transitions");
		
		// Make the design valid and save
		fla.arrangeDesign();
		fla.drawTransitionBtwActivities();
		
		// save
		String saveOutput = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveOutput.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"The design still shows as no valid");
		
	}
	
	/**
	 * Save invalid design: no assigned groups
	 * 
	 *  Attempts to save a design that has no groups assigned.
	 *  
	 */
	@Test(dependsOnMethods={"attemptSaveNoTransitionsDesign"})
	public void attemptSaveNoGroupingAssigned() {
		
		fla.dragGroupToCanvas();
		
		fla.drawTransitionFromTo(AuthorConstants.GROUP_TITLE, AuthorConstants.FORUM_TITLE);

		String saveResult = fla.saveDesign();

		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_NO_GROUPS_ASSIGNED),
				"The design should not be valid as it has unassigned groups");
		
		// fix design
		fla.setGroupForActivity(AuthorConstants.GROUP_TITLE, AuthorConstants.FORUM_TITLE);
		
		// save
		String saveOutput = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveOutput.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"The design still shows as no valid");
		
	}
  
	/**
	 * Save invalid design: no branches set
	 * 
	 *  Attempts to save a design that has no branches created.
	 *  
	 */
	@Test(dependsOnMethods={"attemptSaveNoGroupingAssigned"})
	public void attemptSaveNoBranchesSet() {
		
		cleanCanvas();
		
		fla.dragActivityToCanvasPosition(AuthorConstants.COMMON_CARTRIDGE_TITLE, 300, 150);
		fla.dragBranchToCanvas();
		
		fla.drawTransitionFromTo(AuthorConstants.COMMON_CARTRIDGE_TITLE, AuthorConstants.BRANCHING_START_TITLE);
		
		String saveResult = fla.saveAsDesign(randomDesignName + "-noBranches" );

		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.BRANCHING_MUST_HAVE_AT_LEAST_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
		// fix design
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, AuthorConstants.BRANCH_END_TITLE);
		
		// save
		String saveOutput = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveOutput.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"The design still shows as no valid");
		
	}
	
	/**
	 * Save invalid design: no branches set
	 * 
	 *  Attempts to save a design that has no branches created.
	 *  
	 */
	@Test(dependsOnMethods={"attemptSaveNoBranchesSet"})
	public void attemptSaveNoGroupBranchesSet() {
		
		cleanCanvas();
		
		fla.dragGroupToCanvas();
		fla.dragBranchToCanvas();
		
		fla.drawTransitionFromTo(AuthorConstants.GROUP_TITLE, AuthorConstants.BRANCHING_START_TITLE);
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, AuthorConstants.BRANCH_END_TITLE);
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
			.setBranchingType(BranchingTests.BRANCHING_TYPE_GROUP)
			.setGroupInGroupBranchingType(AuthorConstants.GROUP_TITLE);
		
		String saveResult = fla.saveAsDesign(randomDesignName + "-noGroupBranches" );
		
		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.BRANCHING_MUST_HAVE_GROUPS_TO_BRANCHES_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
		// fix design.
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.clickMatchGroupsToBranches();
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
		.matchGroupToBranch("1", "1")
		.matchGroupToBranch("1", "1");
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE).clickOkGroupButton();
		
		// save
		String saveOutput = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveOutput.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), 
				"The design still shows as no valid");
		
	}
	
	
	/**
	 * Save invalid design: no conditions set
	 * 
	 *  Attempts to save a design that has no conditions created.
	 *  
	 */
	@Test(dependsOnMethods={"attemptSaveNoGroupBranchesSet"})
	public void attemptSaveNoConditionsBranchesSet() {
		
		// Test data
		String rangeValueZero = "0";
		String rangeValueOne = "1";
		
		String conditionZeroRange = "Zero condition";
		String conditionOneRange  = "One condition";
		
		String branchOne = "Branch 1";
		String branchTwo = "Branch 2";
		
		cleanCanvas();
		
		fla.dragActivityToCanvasPosition(AuthorConstants.MULTIPLE_CHOICE_TITLE, 250, -10);
		fla.dragBranchToCanvas();
		
		fla.drawTransitionFromTo(AuthorConstants.MULTIPLE_CHOICE_TITLE, AuthorConstants.BRANCHING_START_TITLE);
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, AuthorConstants.BRANCH_END_TITLE);
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
			.setBranchingType(BranchingTests.BRANCHING_TYPE_LEARNER_OUTPUT)
			.setInputTool(AuthorConstants.MULTIPLE_CHOICE_TITLE);;
		
		String saveResult = fla.saveAsDesign(randomDesignName + "-noConditionsBranches" );
		
		
		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.BRANCHING_MUST_HAVE_ONE_CONDITION_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
		// fix design
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
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

		saveResult = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG),
				"The error message recieved was not what we expected." + saveResult);
	
		
	}
	
	
	/**
	 * Attempts to create a cross branching transition
	 * 
	 */
	@Test(dependsOnMethods="attemptSaveNoConditionsBranchesSet")
	public void attempCrossBranchingTransition() {
		
		String branchingOne = "Branching1";
		
		cleanCanvas();
		
		fla.dragActivityToCanvasPosition(AuthorConstants.MULTIPLE_CHOICE_TITLE, 250, -10);
		fla.dragBranchToCanvas();
		
		fla.drawTransitionFromTo(AuthorConstants.MULTIPLE_CHOICE_TITLE, 
				AuthorConstants.BRANCHING_START_TITLE);
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.BRANCH_END_TITLE);
		
		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE)
			.setBranchingType(BranchingTests.BRANCHING_TYPE_INSTRUCTOR);
		
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 250, 200)
		.dragActivityToCanvasPosition(AuthorConstants.COMMON_CARTRIDGE_TITLE, 450, 400);
		
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE,
				AuthorConstants.FORUM_TITLE);
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, 
				AuthorConstants.COMMON_CARTRIDGE_TITLE);
		
		fla.drawTransitionFromTo(AuthorConstants.FORUM_TITLE,
				AuthorConstants.BRANCH_END_TITLE);

		fla.branchingProperties(AuthorConstants.BRANCHING_TITLE).setBranchingTitle(branchingOne);

		fla.dragBranchesToCanvasPosition(150, 350);
		
		// fla.branchingProperties(AuthorConstants.BRANCHING_TITLE).setBranchingTitle(branchingTwo);
		
		fla.drawTransitionFromTo(AuthorConstants.COMMON_CARTRIDGE_TITLE, AuthorConstants.BRANCHING_START_TITLE);
		
		fla.dragActivityToCanvasPosition(AuthorConstants.NOTEBOOK_TITLE, 300, 500);
		fla.dragActivityToCanvasPosition(AuthorConstants.IMAGE_GALLERY_TITLE, 350, 600);
		
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, AuthorConstants.NOTEBOOK_TITLE);
		fla.drawTransitionFromTo(AuthorConstants.BRANCHING_START_TITLE, AuthorConstants.IMAGE_GALLERY_TITLE);
		
		fla.drawTransitionFromTo(AuthorConstants.IMAGE_GALLERY_TITLE, AuthorConstants.BRANCH_END_TITLE);
		fla.arrangeDesign();
		
		String txt = fla.drawTransitionFromTo(AuthorConstants.NOTEBOOK_TITLE, branchingOne + " end").getAlertText();
		
		Assert.assertEquals(txt, AuthorConstants.BRANCHING_CROSSBRANCHING_NOT_ALLOWED, 
				"It should have been given an error when attempting to do a cross branching transtion!");
		
	}
	
	
	/**
	 * Save invalid design: no activities inside the optional
	 * 
	 *  Attempts to save a design that has no activities within the optional activity.
	 *  
	 */
	@Test(dependsOnMethods={"attempCrossBranchingTransition"})
	public void attemptSaveNoActivitiesInsideOptional() {
		
		cleanCanvas();

		fla.dragOptionalActivityToCanvas();
		fla.dragActivityToCanvas(AuthorConstants.MINDMAP_TITLE);
		fla.drawTransitionBtwActivities();

		String saveResult = fla.saveAsDesign(randomDesignName + "-nothingInsideOptional" );
		
		
		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.OPTIONAL_MUST_HAVE_ONE_ACTIVITY_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
		// fix design
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 300, 185);
		fla.dragActivityIntoOptionalActivity(AuthorConstants.OPTIONAL_ACTIVITY_TITLE, 
				AuthorConstants.FORUM_TITLE);
		
		saveResult = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
	}
	
	
	/**
	 * Save invalid design: no activities inside a support container
	 * 
	 *  Attempts to save a design that has no activities within the support activity.
	 *  
	 */
	@Test(dependsOnMethods={"attemptSaveNoActivitiesInsideOptional"})
	public void attemptSaveNoActivitiesInsideSupport() {
		
		cleanCanvas();

		fla.dragSupportActivityToCanvas();
		fla.dragActivityToCanvas(AuthorConstants.WIKI_TITLE);
		fla.drawTransitionBtwActivities();

		String saveResult = fla.saveAsDesign(randomDesignName + "-nothingInsideSupport" );
		
		
		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.SUPPORT_MUST_HAVE_ONE_ACTIVITY_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
		// fix design
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 300, 185);
		fla.dragActivityIntoSupportActivity(AuthorConstants.SUPPORT_ACTIVITY_TITLE, 
				AuthorConstants.FORUM_TITLE);
		
		saveResult = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
	}
	
	/**
	 * Save invalid design: no activities inside a support container
	 * 
	 *  Attempts to save a design that has no activities within the support activity.
	 *  
	 */
	@Test(dependsOnMethods={"attemptSaveNoActivitiesInsideSupport"})
	public void attemptSaveNoActivities() {
		
		cleanCanvas();

		String saveResult = fla.saveAsDesign(randomDesignName + "-blankDesign" );
		
		
		// Assert invalid design
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_MUST_HAVE_ONE_ACTIVITY_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
		// fix design
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 300, 185);

		saveResult = saveDesign();
		
		// Assert design is valid
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG),
				"The error message recieved was not what we expected." + saveResult);
		
	}
	
	
	private String saveDesign() {
		
		return fla.saveDesign();
	}
	
	

	
	
}
