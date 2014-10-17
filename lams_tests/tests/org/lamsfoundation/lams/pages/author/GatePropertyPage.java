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

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class GatePropertyPage extends AbstractPage {

	/**	
     * 	Gate properties
     * 
     */	
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[1]/td[2]/input")
	private WebElement gateTitle;
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[2]/td[2]/textarea")
	private WebElement gateDescription;
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[3]/td[2]/select")
	private WebElement gateTypeSelect;
	
	
	/**	
     * 	Schedule gate properties
     * 
     */	
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[5]/td[2]/span/input")
	private WebElement scheduleDays;
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[5]/td[3]/span/input")
	private WebElement scheduleHours;
	
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[5]/td[4]/span/input")
	private WebElement scheduleMinutes;

	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[6]/td[2]/input")
	private WebElement sincePreviewActivity;
	
	
	/**	
     * 	Schedule gate properties
     * 
     */	
		
	@FindBy(xpath = "/html/body/div[14]/div[2]/div/table/tbody/tr[4]/td[2]/select")
	private WebElement conditionInputSelect;
	
	@FindBy (xpath ="/html/body/div[14]/div[2]/div/table/tbody/tr[7]/td/div/span")
	private WebElement createConditionsButton;
	
	
	public GatePropertyPage(WebDriver driver) {
		super(driver);

	}

	
	/**
	 * Set gate title 
	 * 
	 * @param designDescription 
	 * @return 
	 */
	public GatePropertyPage setGateTitle(String gateTitleText) {
		
		gateTitle.click();
		gateTitle.clear();
		gateTitle.sendKeys(gateTitleText);

		return PageFactory.initElements(driver, GatePropertyPage.class);
	}


	/**
	 * Returns gate title
	 * @return gate title
	 */
	public String getGateTitle() {
		
		return gateTitle.getAttribute("value");

	}


	/**
	 * Sets gate description
	 * @param gateDescriptionText 
	 * @return 
	 */
	public GatePropertyPage setGateDescription(String gateDescriptionText) {

		
		gateDescription.click();
		gateDescription.clear();
		gateDescription.sendKeys(gateDescriptionText);
		
		return PageFactory.initElements(driver, GatePropertyPage.class);
	}
	
	/**
	 * Returns gate description
	 * @return gate description
	 */
	public String getGateDescription() {
		
		return gateDescription.getAttribute("value");

	}


	/**
	 * Set gate type in select 
	 * @param gateType 
	 * @return 
	 */
	public GatePropertyPage setGateType(String gateType) {
		
		Select gateSelect = new Select(gateTypeSelect);

		gateSelect.selectByValue(gateType);
		
		return PageFactory.initElements(driver, GatePropertyPage.class);
	}


	/**
	 * Returns gate type value
	 * @return gate type value
	 */
	public String getGateType() {
		
		Select gateSelect = new Select(gateTypeSelect);

		return gateSelect.getFirstSelectedOption().getAttribute("value");
		
	}
	
	public GatePropertyPage setScheduleDays(String days) {
		
		scheduleDays.click();
		scheduleDays.clear();
		scheduleDays.sendKeys(days);
		
		return PageFactory.initElements(driver, GatePropertyPage.class);
	}
	
	public String getScheduleDays() {
		
		return scheduleDays.getAttribute("value");
		
	}


	public GatePropertyPage setScheduleHours(String hours) {
		
		scheduleHours.click();
		scheduleHours.clear();
		scheduleHours.sendKeys(hours);
		
		return PageFactory.initElements(driver, GatePropertyPage.class);
	}

	public String getScheduleHours() {
		
		return scheduleHours.getAttribute("value");
		
	}

	public GatePropertyPage setScheduleMinutes(String minutes) {
		
		scheduleMinutes.click();
		scheduleMinutes.clear();
		scheduleMinutes.sendKeys(minutes);
		
		return PageFactory.initElements(driver, GatePropertyPage.class);
		
	}
	
	public String getScheduleMinutes() {
		
		return scheduleMinutes.getAttribute("value");
		
	}
	
	public GatePropertyPage setSincePreviewActivity() {
		
		sincePreviewActivity.click();
		
		return PageFactory.initElements(driver, GatePropertyPage.class);
		
	}
	
	public Boolean isSincePreviewActivity() {
		
		return sincePreviewActivity.isSelected();
		
	}


	public GatePropertyPage setConditionInput(String activity) {
		
		Select inputDropDown = new Select(conditionInputSelect);
		
		inputDropDown.selectByVisibleText(activity.trim());
	
		return PageFactory.initElements(driver, GatePropertyPage.class);
	}
	
	public String getConditionInput() {
		
		Select inputDropDown = new Select(conditionInputSelect);
		
		return inputDropDown.getFirstSelectedOption().getText();

	}	
}
