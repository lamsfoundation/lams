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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class ImportDesignTests {

	private static final String lamsDesignFile = "Example1.zip";
	private static final String badFile  = "xyz.zip";
	
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
	 * Imports a blank zip. 
	 * This test whether it fails when trying to import a non LAMS design file
	 */
	@Test(dependsOnMethods="openFLA")
	public void importBadZip() {
		
		List<String> importResults = fla.importDesign()
				.uploadDesign(badFile)
				.submitDesign()
				.importStatus();
		
		Assert.assertEquals(importResults.get(0), "0", "Design import successfully when it shouldn't: " + importResults.get(1));
	}
	

	/**
	 * Imports a LAMS design file
	 */
	@Test(dependsOnMethods="importBadZip")
	public void importDesign() {
		
		List<String> importResults = fla.importDesign()
		.uploadDesign(lamsDesignFile)
		.submitDesign()
		.importStatus();
		
		// Assert import correct
		Assert.assertEquals(importResults.get(0), "1", "Design import failed: " + importResults.get(1));
		
		
	}
	
	@Test(dependsOnMethods="importDesign")
	public void testDesignComplete() {
	
		List<String> allActivities = fla.getAllActivityNames();
		
		// Assert activities in design
		Assert.assertTrue(allActivities.contains(AuthorConstants.ASSESSMENT_TITLE), 
				"Design does not contain" + AuthorConstants.ASSESSMENT_TITLE);
		
		Assert.assertTrue(allActivities.contains(AuthorConstants.CHAT_TITLE), 
				"Design does not contain " + AuthorConstants.CHAT_TITLE);
		
		Assert.assertTrue(allActivities.contains(AuthorConstants.GMAP_TITLE), 
				"Design does not contain " + AuthorConstants.GMAP_TITLE);
		
		Assert.assertTrue(allActivities.contains(AuthorConstants.IMAGE_GALLERY_TITLE), 
				"Design does not contain " + AuthorConstants.IMAGE_GALLERY_TITLE);
		
		Assert.assertTrue(allActivities.contains(AuthorConstants.FORUM_TITLE), 
				"Design does not contain " + AuthorConstants.FORUM_TITLE);
		
		Assert.assertTrue(allActivities.contains(AuthorConstants.NOTEBOOK_TITLE), 
				"Design does not contain " + AuthorConstants.NOTEBOOK_TITLE);
		
		String designName = fla.getDesignName();
		
		Assert.assertTrue(lamsDesignFile.contains(designName), 
				"Imported file is different than design name");
		
	}
	

}
