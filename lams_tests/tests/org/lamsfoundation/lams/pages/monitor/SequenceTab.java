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

package org.lamsfoundation.lams.pages.monitor;

import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SequenceTab extends AbstractPage {

	
	/**
	 * Sequence tab attributes 
	 */
	
	@FindBy(id = "refreshButton")
	private WebElement refreshButton;
	
	@FindBy(id = "exportPortfolioButton")
	private WebElement exportPortfolioButton;

	@FindBy(id = "liveEditButton")
	private WebElement liveEditButton;
	
	@FindBy(id = "closeBranchingButton")
	private WebElement closeBranchingButton;

	@FindBy(id = "helpButton")
	private WebElement helpButton;
	
	
	public SequenceTab(WebDriver driver) {
		super(driver);

	}
	
	public boolean isLiveEditPresent() {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Boolean isLiveEditPresent = driver.findElements(By.id("liveEditButton")).size() > 0 ? true: false;
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return isLiveEditPresent;
		


	}


}
