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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/**
 * Authoring Gate tests:
 * 
 * - Creates a design with a gate
 * - Adds title to gate
 * - Adds description to gate
 * - Sets gate type:
 *    - permission
 *    - synchronization 
 *    - condition
 *    - schedule
 * 
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class GatesTests {
	
	// Gates constants
	
	private static final String GATE_TYPE_CONDITION = "condition";
	private static final String GATE_TYPE_SYNC = "sync";
	private static final String GATE_TYPE_SCHEDULE = "schedule";
	private static final String GATE_TYPE_PERMISSION = "permission";
	
	
	private static final String randomInt = LamsUtil.randInt(0, 9999);

	private String randomDesignName = "Design-" + randomInt;
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
	 * Creates a design with a gate
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void createDesign() {
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 200, 50);
		fla.dragGateToCanvas();
		fla.dragActivityToCanvasPosition(AuthorConstants.SHARE_RESOURCES_TITLE, 350, (10 * randomInteger));
		
		// Draw transitions in between the activities
		fla.drawTransitionFromTo(AuthorConstants.FORUM_TITLE, AuthorConstants.GATE_TITLE);
		fla.drawTransitionFromTo(AuthorConstants.GATE_TITLE, AuthorConstants.SHARE_RESOURCES_TITLE);
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
	
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.FORUM_TITLE), 
				"The title " + AuthorConstants.FORUM_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.GATE_TITLE), 
				"The title " + AuthorConstants.GATE_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(AuthorConstants.SHARE_RESOURCES_TITLE), 
				"The title " + AuthorConstants.SHARE_RESOURCES_TITLE + " was not found as an activity in the design");
		
		
	}
	/**
	 * Saves design
	 */
	@Test(dependsOnMethods={"createDesign"})
	public void namesAndSavesDesign() {
		
		String saveResult = fla.saveDesign(randomDesignName);
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), "Error while saving design");
		
	}
	
	
	/**
	 * Arranges the design
	 */
	@Test(dependsOnMethods={"namesAndSavesDesign"})
	public void arrangeDesign() {
		
		Point initialActivityLocation = fla.getActivityLocation(AuthorConstants.GATE_TITLE);
		
		fla.arrangeDesign();
		
		Point newActivityLocation = fla.getActivityLocation(AuthorConstants.GATE_TITLE);

		Assert.assertFalse(initialActivityLocation.equals(newActivityLocation), 
				"The activity " + AuthorConstants.GATE_TITLE + " is still in the same location!" );
				
	}
	
	/**
	 * Saves new re-arranged design
	 */
	@Test(dependsOnMethods={"arrangeDesign"})
	public void savesDesign() {
		
		String saveResult = fla.saveDesign();
		
		Assert.assertTrue(saveResult.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG), "Error while saving design: " + saveResult);		
	}
	
	
	/**
	 * Set Gate title
	 */
	@Test(dependsOnMethods={"savesDesign"})
	public void setGateTitle() {
		
		String gateTitleText = "Gate 01";
		
		fla.gateProperties().setGateTitle(gateTitleText);
			
		String assertGateTitle = fla.gateProperties().getGateTitle();
		
		Assert.assertEquals(assertGateTitle, gateTitleText, "Gate title is not the same");
			
		
	}
	
	/**
	 * Set Gate description
	 */
	@Test(dependsOnMethods={"setGateTitle"})
	public void setGateDescription() {
		
		String gateDescriptionText = "This is a gate";
		
		fla.gateProperties().setGateDescription(gateDescriptionText);
			
		String assertGateDescription = fla.gateProperties().getGateDescription();
		
		Assert.assertEquals(assertGateDescription, gateDescriptionText, "Gate description is not the same");
			
		
	}
	
	/**
	 *    We implement a data provider so iterate thru the gate types
	 *    
	 */
	@DataProvider(name = "gateTypes")
	public static Object[][] gateTyoes() {
		return new Object[][] {
				new Object[] { GATE_TYPE_CONDITION } , { GATE_TYPE_PERMISSION} , 
				{ GATE_TYPE_SCHEDULE }, { GATE_TYPE_SYNC } 
		};
	}
	
	
	/**
	 * Checks that all Gate types are available and can be set
	 */
	@Test(dataProvider = "gateTypes", dependsOnMethods={"setGateDescription"})
	public void setGateType(String gateType) {
		
		
		fla.gateProperties().setGateType(gateType);
			
		String assertGateType = fla.gateProperties().getGateType();
		
		Assert.assertEquals(assertGateType, gateType, "Gate type is not the same");
		
		
	}
	
	/**
	 * Set a Permission gate and save
	 */
	@Test(dependsOnMethods="setGateType")
	public void setGateTypeToPermission() {
		setGateType(GATE_TYPE_PERMISSION);
		savesDesign();
	}
	
	/**
	 * Set a Sync gate and save
	 */
	@Test(dependsOnMethods="setGateTypeToPermission")
	public void setGateTypeToSync() {
		setGateType(GATE_TYPE_SYNC);
		savesDesign();
	}
	
	/**
	 * Set a Schedule gate, its properties and save
	 */
	@Test(dependsOnMethods="setGateTypeToSync")
	public void setGateTypeToSchedule() {
		final String scheduleDays = "2";
		final String scheduleHours = "10";
		final String scheduleMinutes = "45";
		
		setGateType(GATE_TYPE_SCHEDULE);
		
		// Set days property
		fla.gateProperties().setScheduleDays(scheduleDays);
		
		// Set hours property
		fla.gateProperties().setScheduleHours(scheduleHours);

		// Set hours property
		fla.gateProperties().setScheduleMinutes(scheduleMinutes);
		
		// Set since finished previews activity option
		fla.gateProperties().setSincePreviewActivity();
		
		// Save
		savesDesign();
		
		// Now run assertions

		
		// Days
		String assertScheduleDays = fla.gateProperties()
				.getScheduleDays();
		Assert.assertEquals(scheduleDays, assertScheduleDays, 
				"The days in the schedule gate don't match");

		// Hours
		String assertScheduleHours = fla.gateProperties()
				.getScheduleHours();
		Assert.assertEquals(scheduleHours, assertScheduleHours, 
				"The days in the schedule gate don't match");
		
		// Minutes
		String assertScheduleMinutes = fla.gateProperties()
				.getScheduleMinutes();
		Assert.assertEquals(scheduleMinutes, assertScheduleMinutes, 
				"The days in the schedule gate don't match");
		
		// Since previews activity
		Boolean assertIsSincePreviews = fla.gateProperties()
				.isSincePreviewActivity();
		Assert.assertTrue(assertIsSincePreviews, 
				"Option Since finish previews activity is not on");
		
	}
	
	/**
	 * Set a Schedule gate, its properties and save
	 */
	@Test(dependsOnMethods="setGateTypeToSchedule")
	public void setGateTypeToCondition() {
		
		fla.gateProperties()
			.setGateType(GATE_TYPE_CONDITION)
			.setGateTitle("Condition Gate")
			.setGateDescription("Testing condition gate")
			.setConditionInput(AuthorConstants.FORUM_TITLE);
		
		savesDesign();
		
		// Now assertions
		
		String assertConditionInput = fla.gateProperties().getConditionInput();
		Assert.assertEquals(assertConditionInput, AuthorConstants.FORUM_TITLE,
				"Condition input activity is not the same!");
		
	}
}
