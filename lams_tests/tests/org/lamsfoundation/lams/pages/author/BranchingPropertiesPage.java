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

package org.lamsfoundation.lams.pages.author;

import java.util.List;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class BranchingPropertiesPage extends AbstractPage {
	
	public BranchingPropertiesPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	/**	
     * 	Branching properties
     * 
     */	
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[1]/td[2]/input")
	private WebElement branchingTitle;
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[2]/td[2]/select")
	private WebElement branchingTypeSelect;
	

	/**	
     * 	Group branching properties
     * 
     */	
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[3]/td[2]/select")
	private WebElement groupSelect;
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[7]/td/div/span")
	private WebElement matchGroupsToBranchesButton;
	
	// We need this so we can iterate thru the groups
	private final String matchingGroupsItemPrefix = "//*[@id=\"gtbDialog\"]/table/tbody/tr[3]/td[1]/div[";

	// Branch xpath
	private final String matchingGroupsBranchPrefix = "//*[@id=\"gtbDialog\"]/table/tbody/tr[3]/td[2]/div[";
	
	@FindBy(xpath = "//*[@id=\"gtbDialog\"]/table/tbody/tr[3]/td[3]/div[1]/span[1]")
	private WebElement toRightButton;
	
	@FindBy(xpath = "//*[@id=\"gtbDialog\"]/table/tbody/tr[3]/td[3]/div[2]")
	private WebElement toLeftButton;
	
	@FindBy(xpath = "/html/body/div[16]/div[11]/div/button/span")
	private WebElement okGroupButton;
	
	
	/**
	 * Set branching title
	 * @param title name for the branching
	 * @return BranchingPropertiesPage
	 */
	public BranchingPropertiesPage setBranchingTitle(String title) {
		
		branchingTitle.click();
		branchingTitle.clear();
		branchingTitle.sendKeys(title);
	
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
	}

	/**
	 * Returns branching title
	 * @return branching title
	 */
	public String getBranchingTitle() {
			
			return branchingTitle.getAttribute("value");
		
	}
	
	/**
	 * Set branching type in select 
	 * @param branchingType 
	 * @return 
	 */
	public BranchingPropertiesPage setBranchingType(String branchingType) {
		
		Select branchingSelect = new Select(branchingTypeSelect);

		branchingSelect.selectByValue(branchingType);
		
		branchingTitle.click();
		
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
	}
	
	/**
	 * Returns branching type value
	 * @return branching type value
	 */
	public String getBranchingType() {
		
		Select branchingSelect = new Select(branchingTypeSelect);

		return branchingSelect.getFirstSelectedOption().getAttribute("value");
		
	}
	
	/**
	 * Select grouping for group based branching 
	 * @param branchingType 
	 * @return 
	 */
	public BranchingPropertiesPage setGroupInGroupBranchingType(String grouping) {
		
		Select branchingSelect = new Select(groupSelect);

		branchingSelect.selectByIndex(1);
		
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
	}
	
	/**
	 * Returns branching type value
	 * @return branching type value
	 */
	public String getGroupInGroupBranchingType() {
		
		Select branchingSelect = new Select(groupSelect);

		return branchingSelect.getFirstSelectedOption().getAttribute("value");
		
	}
	
	/**
	 * Presses the button to open the group to branching mapping UI.
	 * @return BranchingPropertiesPage
	 */
	public BranchingPropertiesPage clickMatchGroupsToBranches() {
		
		matchGroupsToBranchesButton.click();
		
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
	}
	
	/**
	 * Gets a group number and matches it to a branch 
	 * @param groupNumber
	 * @param branchNumber
	 * @return BranchingPropertiesPage
	 */
	public BranchingPropertiesPage matchGroupToBranch(String groupNumber, String branchNumber) {
		
		// Clicks on the group number
		driver.findElement(By.xpath(matchingGroupsItemPrefix + groupNumber + "]")).click();
		
		// clicks on the branch number 
		driver.findElement(By.xpath(matchingGroupsBranchPrefix + branchNumber + "]")).click();
		
		toRightButton.click();
		
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
		
	}
	
	/**
	 * Click on OK for the groups <=> branching matching UI
	 * @return BranchingPropertiesPage
	 */
	public BranchingPropertiesPage clickOkGroupButton() {
		
		okGroupButton.click();
		
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
	}
	
}
