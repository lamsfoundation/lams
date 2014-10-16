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

import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

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
 *  - open activity authoring
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class AuthorTests {
	
	
	// Constants
	
	private static final String FLA_TITLE = "Flashless Authoring";
	private static final String SAVE_SEQUENCE_SUCCESS_MSG = "Congratulations";
	
	private static final String FORUM_TITLE = "Forum";
	private static final String KALTURA_TITLE = "Kaltura";
	private static final String SHARE_RESOURCES_TITLE = "Share Resources";
	private static final String Q_AND_A_TITLE = "Q & A";
	
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

		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		fla = PageFactory.initElements(driver, FLAPage.class);
		onLogin.navigateToLamsLogin().loginAs("test2", "test2");
		
	}

	@AfterClass
	public void afterClass() {
		//driver.quit();
	}

	/**
	 * Opens FLA interface
	 */
	@Test
	public void openFLA() {
		
		FLAPage fla = new FLAPage(driver);
		fla = index.openFla();
		fla.maximizeWindows();
		Assert.assertEquals(FLA_TITLE, fla.getTitle(), "The expected title is not present");


		
	}
	
	/**
	 * Creates a 4 activity sequence
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void createDesign() {
		
		// Drop activites in canvas
		fla.dragActivityToCanvas(FORUM_TITLE);
		fla.dragActivityToCanvasPosition(SHARE_RESOURCES_TITLE, 250, (20 * randomInteger));
		fla.dragActivityToCanvasPosition(KALTURA_TITLE, (350 * randomInteger), 120);
		fla.dragActivityToCanvasPosition(Q_AND_A_TITLE, 600, (14 * randomInteger));
		fla.drawTransitionBtwActivities();
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(FORUM_TITLE), 
				"The title " + FORUM_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(SHARE_RESOURCES_TITLE), 
				"The title " + SHARE_RESOURCES_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(KALTURA_TITLE), 
				"The title " + KALTURA_TITLE + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(Q_AND_A_TITLE), 
				"The title " + Q_AND_A_TITLE + " was not found as an activity in the design");		
	}

	@Test(dependsOnMethods={"createDesign"})
	public void nameAndSaveDesign() {
		
		String saveResult = fla.saveDesign(randomDesignName);
		// System.out.println(saveResult);
		Assert.assertTrue(saveResult.contains(SAVE_SEQUENCE_SUCCESS_MSG), 
				"Saving a sequence returned an unexpected message: "+ saveResult);

	}
	
	@Test(dependsOnMethods={"nameAndSaveDesign"})
	public void cleanCanvas() {
		fla.newDesign();
		
		// Check that the design titled is back to untitled
		String newTitle = fla.getSequenceName();
		Assert.assertTrue(newTitle.equals("Untitled"), "The canvas wasn't clean. Still shows: " + newTitle);
		
	}
	
	@Test(dependsOnMethods={"cleanCanvas"})
	public void reOpenDesign() {
		
		fla.openDesign(randomDesignName);
		Assert.assertEquals(fla.getSequenceName(), randomDesignName);
	
		
	}
	
	
	@Test(dependsOnMethods={"reOpenDesign"})
	public void reArrangeDesign() {
		
		fla.arrangeDesign();
		
	}
	
	@Test(dependsOnMethods={"reOpenDesign"})
	public void saveAsDesign() {
		
		String newSequenceName = "Re" + randomDesignName;
		fla.saveAsDesign(newSequenceName);
		Assert.assertEquals(fla.getSequenceName(), newSequenceName);
	}
	
	
	/**
	 * 
	 */
	@Test(dependsOnMethods={"saveAsDesign"})
	public void changeActivityTitle() {
		
		String forumNewTitle =  "New " + FORUM_TITLE;
		String kalturaNewTitle = "New " + KALTURA_TITLE;
		
		fla.changeActivityTitle(FORUM_TITLE, forumNewTitle);
		fla.changeActivityTitle(KALTURA_TITLE, kalturaNewTitle);
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		System.out.println("All Activites: "+ allActivityTitles);

		Assert.assertTrue(allActivityTitles.contains(forumNewTitle), 
				"The title " + forumNewTitle + " was not found as an activity in the design");
		Assert.assertTrue(allActivityTitles.contains(kalturaNewTitle), 
				"The title " + kalturaNewTitle + " was not found as an activity in the design");
		
				
	}
	

	@Test(dependsOnMethods={"changeActivityTitle"})
	public void openActivity() {
		
		AbstractPage forumPage = fla.openSpecificActivity("New " + FORUM_TITLE);

		String forumPageTitle = forumPage.getTitle();
		forumPage.closeWindow();
		
		Assert.assertEquals(forumPageTitle, FORUM_TITLE, 
				"The title seems to be different from what we expected.");

				
	}
	
	
}
