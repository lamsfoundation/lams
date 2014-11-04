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

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConditionsPropertiesPage extends AbstractPage {

	
	public static final String OUTPUT_MCQ_ALL_CORRECT = "Are learner's answers all correct?";
	public static final String OUTPUT_MCQ_TOTAL_MARK = "Learner's total mark(range)";
	
	public static final String OPTION_GREATER_THAN = "greater";
	public static final String OPTION_LESS_THAN = "less";
	public static final String OPTION_RANGE = "range";	
	
	/**	
     * 	Conditions properties
     * 
     */	
	
	@FindBy(xpath = "//*[@id=\"outputSelect\"]")
	private WebElement outputSelect;	
	
	@FindBy (xpath = "//*[@id=\"rangeOptionSelect\"]")
	private WebElement optionsSelect;	

	// Range options
	
	@FindBy(xpath = "//*[@id=\"multiRangeFromSpinner\"]")
	private WebElement rangeFrom;
	
	@FindBy(xpath = "//*[@id=\"multiRangeToSpinner\"]")
	private WebElement rangeTo;
	
	@FindBy(xpath = "//*[@id=\"singleRangeSpinner\"]")
	private WebElement singleRange;
	
	@FindBy(xpath = "//*[@id=\"rangeAddButton\"]/span")
	private WebElement addRangeButton;
	
	
	@FindBy(xpath = "/html/body/div[17]/div[11]/div/button[5]/span")
	private WebElement okConditionsButton;
	
	@FindBy(xpath = "/html/body/div[17]/div[11]/div/button[4]/span")
	private WebElement cancelConditionsButton;
	
	@FindBy(xpath = "/html/body/div[17]/div[11]/div/button[2]/span")
	private WebElement removeConditionButton;
	
	@FindBy(xpath = "/html/body/div[17]/div[11]/div/button[1]/span")
	private WebElement clearAllConditionsButton;
	
	
	
	/**
	 * Note that we need to use this xpath and iterate thru the elements on a
	 * table, so we use the " " where we'd need to change the numbering according to the
	 * number of labels we find in the table.
	 * 
	 */
	private static final String RANGE_CONDITIONS_OPTION_LABEL = 
			"//*[@id=\"rangeConditions\"]/tbody/tr[ ]/td[1]/input";
	
	// private static final String RANGE_CONDITIONS_CONDITION_LABEL = 
	//		"//*[@id=\"rangeConditions\"]/tbody/tr[ ]/td[2]";
	
	/**	
     * 	Match conditions to branches elements
     * 
     */	
	
	@FindBy(xpath = "//*[@id=\"ctbDialog\"]/table/tbody/tr[3]/td[1]")
	private WebElement conditionsColumn;
	
	@FindBy(xpath = "//*[@id=\"ctbDialog\"]/table/tbody/tr[3]/td[2]")
	private WebElement branchesColumn;
	
	
	// These buttons are to move the conditions to mapping
	@FindBy(xpath = "//*[@id=\"ctbDialog\"]/table/tbody/tr[3]/td[3]/div[1]/span[1]")
	private WebElement toRightButton;
	
	@FindBy(xpath = "//*[@id=\"ctbDialog\"]/table/tbody/tr[3]/td[3]/div[2]/span[1]")
	private WebElement toLeftButton;
	
	// final mappings
	
	@FindBy(xpath = "//*[@id=\"ctbDialog\"]/table/tbody/tr[3]/td[4]")
	private WebElement mappingsConditionsColumn;
	
	@FindBy(xpath = "//*[@id=\"ctbDialog\"]/table/tbody/tr[3]/td[5]")
	private WebElement mappingsBranchColumn;
	
	
	@FindBy(xpath = "/html/body/div[18]/div[11]/div/button/span")
	private WebElement okMatchConditionsBranchesButton;
	
	
	public ConditionsPropertiesPage(WebDriver driver) {
		super(driver);
		
	}

	
	
	/**
	 * Select condition output 
	 * 
	 * @param Condition output to select
	 * @return ConditionsPropertiesPage
	 */
	public ConditionsPropertiesPage setConditionOutput(String output) {
		
		Select outputSelection = new Select(outputSelect);
		outputSelection.selectByVisibleText(output);

		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}
	
	/**
	 * Gets the selection for condition output
	 * 
	 * @return output text (String)
	 */
	public String getConditionOutput() {
		
		Select outputSelection = new Select(outputSelect);
		
		return outputSelection.getFirstSelectedOption().getText();
		
	}

	
	/**
	 * Sets the option for the condition (ranges)
	 * @param option
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage setOptionType(String option) {
		
		Select optionSelector = new Select(optionsSelect);
		
		optionSelector.selectByValue(option);
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}

	/**
	 * Gets the selected option (range)
	 * @return text from value of the condition
	 */
	public String getOptionType() {
		
		Select optionSelector = new Select(optionsSelect);
		
		return optionSelector.getFirstSelectedOption().getAttribute("value");		
	}
	
	
	/**
	 * Sets the value for the option when the greater or less than option was selected
	 * @param value the option mark/score 
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage setSingleRangeValue(String value) {
		
		singleRange.click();
		singleRange.clear();
		singleRange.sendKeys(value);
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
		
	}
	
	/**
	 * Returns the value set for the single range option
	 * @return value within input
	 */
	public String getSingleRangeValue() {
		
		singleRange.click();
		return singleRange.getAttribute("value");
		
	}
	
	/**
	 * Sets the from range for an option
	 * @param value
	 * @return {@link ConditionsPropertiesPage} 
	 */
	public ConditionsPropertiesPage setFromRangeValue(String value) {
		
		rangeFrom.click();
		rangeFrom.clear();
		rangeFrom.sendKeys(value);

		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
		
	}

	/**
	 * Returns the value set in the from option
	 * @param value
	 * @return String value of from option
	 */
	public String getFromRangeValue() {
		
		rangeFrom.click();
		return rangeFrom.getAttribute("value");		
	}
	
	/**
	 * Sets the to range for an option
	 * @param value
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage setToRangeValue(String value) {
		
		rangeTo.click();
		rangeTo.clear();
		rangeTo.sendKeys(value);
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}
	
	/**
	 * Returns the value set in in the To range
	 * @return value set in to range
	 */
	public String getToRangeValue() {
		
		return rangeTo.getAttribute("value");
		
	}
	
	
	/**
	 * Clicks on the Add option for range
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage clickAddOptionRange() {
		
		addRangeButton.click();
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}
	
	/**
	 * Sets the name for the condition
	 * @param conditionName
	 * @param order this is for the xpath order (starts always in 2).
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage setConditionName(String conditionName, String order) {
		
		String inputXpath = RANGE_CONDITIONS_OPTION_LABEL.replace(" ", order);
		//System.out.println("inputXpath: " + inputXpath);
		
		WebElement input = driver.findElement(By.xpath(inputXpath));
		
		input.click();
		input.clear();
		input.sendKeys(conditionName);
		
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}
	
	/**
	 * Returns all the available conditions in a List
	 * @return 
	 */
	public List<String> getConditionNames() {
		
		List<WebElement> elements = driver.findElements(By.xpath("id('rangeConditions')/tbody//tr[*]/td[1]/input"));
		List<String> conditionNames = new ArrayList<String>();
		
		for (int i = 0; i < elements.size(); i++) {
			WebElement element = elements.get(i);
			
			conditionNames.add(element.getAttribute("value"));
			
			
		}
		
		cancelConditionsButton.click();
		
		return conditionNames;
	}
	
	
	/**
	 * Click on OK for conditions. 
	 * 
	 * This should open the conditions to branches matching page.
	 * 
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage clickOkConditionsButton() {
		
		okConditionsButton.click();
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}
	
	
	/**
	 * Matches conditions to branches
	 * 
	 * Given a condition name, it matches it to a branch
	 * 
	 * @param conditionName
	 * @param branchName
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage matchConditionToBranch(String conditionName, String branchName) {
		
		// Let's iterate thru the conditions and click on the one sent
		
		List<WebElement> allConditions = conditionsColumn.findElements(By.tagName("div"));
		
		// We've got to use this wait as otherwise the javascript isn't as fast to return the txt 
		// within the div
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		
		for (WebElement condition : allConditions) {
			wait.until(ExpectedConditions.elementToBeClickable(condition));
			String name = condition.getText();
		
			if (conditionName.equals(name.trim())) {
				//System.out.print("condition name: " + name);
				condition.click();
				break;
			}
		}
		
		
		// Now iterate thru the branches and choose the one sent
		
		List<WebElement> allBranches = branchesColumn.findElements(By.tagName("div"));
				
		for (WebElement branch : allBranches) {
			wait.until(ExpectedConditions.elementToBeClickable(branch));
			String name = branch.getText();
			
			// when doing the comparison we remove the "(default)" string just to make
			// that it matches even when we don't know which one is the default branch
			
			if (branchName.equals(name.replace("(default)", "").trim())) {
				//System.out.println(" matching with  branch name: " + name);
				branch.click();
				break;
			}
		}
		
		toRightButton.click();
		

		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}

	
	/**
	 * Click OK Matching conditions. 
	 * 
	 * Note that all of the conditions must be matched to branches. Otherwise on clicking this button
	 * an alert will show stating that all conditions must be matched before finishing and that by
	 * default all matchless conditions will be matched to the default branch.
	 * 
	 * @return {@link ConditionsPropertiesPage}
	 */
	public ConditionsPropertiesPage clickOkMatchingConditionsToBranchesButton() {
		
		okMatchConditionsBranchesButton.click();
		
		return PageFactory.initElements(driver, ConditionsPropertiesPage.class);
	}
	
	
	/**
	 * Returns all the condition to branches mappings
	 * 
	 * @return all conditions available in the format "{condition} + ' matches ' + {branch}"
	 */
	public List<String> getAllMappings() {
		
		List<String> allMappings = new ArrayList<String>();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		List<WebElement> allConditions = mappingsConditionsColumn.findElements(By.tagName("div"));
		
		List<String> conditions = new ArrayList<String>();
		for (WebElement condition : allConditions) {
			wait.until(ExpectedConditions.elementToBeClickable(condition));
			conditions.add(condition.getText());
		}
		
		
		List<WebElement> allBranches = mappingsBranchColumn.findElements(By.tagName("div"));
		
		List<String> branches = new ArrayList<String>();
		for (WebElement branch : allBranches) {
			wait.until(ExpectedConditions.elementToBeClickable(branch));
			branches.add(branch.getText());
		}
		
		// Create final mapping list
		for (int i = 0; i < conditions.size(); i++) {
			allMappings.add(conditions.get(i) +  " matches " + branches.get(i));
		}
		
		
		return allMappings;
		
	}
	
	
}
