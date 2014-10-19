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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Flashless Authoring page. 
 * All interaction with Authoring environment should be done thru this page.
 *
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class FLAPage extends AbstractPage {

	/** 
	 * Menu buttons 
	 * These are the menu buttons on the interface.
	 */

	@FindBy(id = "newButton")
	private WebElement newButton;

	@FindBy(id = "openButton")
	private WebElement openButton;
	
	@FindBy(id = "openDropButton")
	private WebElement openDropButton;
	
	@FindBy(id = "importDesignButton")
	private WebElement importDesignButton;

	@FindBy(id = "importPartDesignButton")
	private WebElement importPartDesignButton;

	@FindBy(id = "saveButton")
	private WebElement saveButton;

	@FindBy(id = "saveDropButton")
	private WebElement saveDropButton;

	@FindBy(id = "saveAsButton")
	private WebElement saveAsButton;	

	@FindBy(id = "exportButton")
	private WebElement exportButton;


	@FindBy(id = "copyButton")
	private WebElement copyButton;

	@FindBy(id = "pasteButton")
	private WebElement pasteButton;

	@FindBy(id = "transitionButton")
	private WebElement transitionButton;

	@FindBy(id = "flowButton")
	private WebElement flowButton;
	
	@FindBy(id = "flowDropButton")
	private WebElement flowDropButton;

	@FindBy(id = "gateButton")
	private WebElement gateButton;

	@FindBy(id = "branchingButton")
	private WebElement branchingButton;

	@FindBy(id = "groupButton")
	private WebElement groupButton;

	@FindBy(id = "annotateButton")
	private WebElement annotateButton;
	
	@FindBy(id = "annotateDropButton")
	private WebElement annotateDropButton;

	@FindBy(id = "annotateLabelButton")
	private WebElement annotateLabelButton;

	@FindBy(id = "annotateRegionButton")
	private WebElement annotateRegionButton;

	@FindBy(id = "arrangeButton")
	private WebElement arrangeButton;

	@FindBy(id = "previewButton")
	private WebElement previewButton;

	/** 
	 * Activities
	 */


	/** 
	 * Page elements
	 *  These are the page elements on FLA
	 */

	@FindBy(id = "canvas")
	private WebElement canvas;

	@FindBy(id = "ldDescriptionFieldTitle")
	private WebElement designTitle;

	@FindBy(id = "svg")
	private WebElement svgCanvas;

	@FindBy(id = "rubbishBin")
	private WebElement rubbishBin;


	@FindBy(id = "ldDescriptionHideTip")
	private WebElement designDescriptionLink;
	
	/**
	 * Dialogs: Save design dialog elements
	 */

	@FindBy(id = "ldStoreDialogNameField")
	private WebElement ldStoreDialogNameField;

	@FindBy(id = "saveLdStoreButton")
	private WebElement saveLdStoreButton;


	/**
	 * Dialogs: Open design dialog elements
	 */

	@FindBy(id = "openLdStoreButton")
	private WebElement openLdStoreButton;

	@FindBy(id = "ldStoreDialogTreeCell")
	private WebElement ldTree;


	@FindBy(id = "propertiesContentTool")
	private WebElement propertiesContentTool;



	public FLAPage(WebDriver driver) {
		super(driver);

	}



	/**
	 * Opens LD Dialog to open a design
	 * 
	 */
	public void openLdOpenDialog() {

		openButton.click();

	}

	/**
	 * Opens LD Dialog to save a design
	 */
	public void openLdSaveDialog() {

		saveButton.click();
	}


	/**
	 * Clears canvas to new
	 */
	public void newDesign() {

		newButton.click();
		checkAlert();
	}

	/**
	 * Drops a group activity into the canvas. 
	 *  
	 */
	public void dragGroupToCanvas() {

		groupButton.click();
		canvas.click();

	}


	/**
	 * Given the activity name, it drags it into the canvas. By default it puts it in the 
	 * center of the canvas. 
	 * @param name 
	 */
	public void dragActivityToCanvas(String name) {

		String toolName = "tool" + name;

		WebElement tool = driver.findElement(By.id(toolName));

		Actions builder = new Actions(driver);  // Configure the Action
		Action dragAndDrop = builder.clickAndHold(tool)
				.moveToElement(canvas)
				.release(canvas)
				.build();  // Get the action
		dragAndDrop.perform(); // Execute the Action

	}

	/**
	 * Given an activity, it drags it into the canvas and moves it in the x,y offset position.
	 * It uses the randomInteger value to randomize the position of the activities in the
	 * screen
	 * 
	 * @param name	activity name
	 * @param x		x offset move
	 * @param y 	y offset move
	 */
	public void dragActivityToCanvasPosition(String name, int x, int y) {

		// we include the prefix "tool" as that's what we named the ids	
		String toolName = "tool" + name;

		// Finds the activity
		WebElement tool = driver.findElement(By.id(toolName));

		// Prepare the dragAndDrop action
		Actions builder = new Actions(driver);  // Configure the Action
		Action dragAndDrop = builder.clickAndHold(tool)
				.moveToElement(canvas, x, y)
				// .moveByOffset(canvas, x, y)
				.release()
				.build();  

		// Execute the Action
		dragAndDrop.perform();

	}

	/**
	 * Saves a design with the given param desigName. 
	 *  
	 * @param designName
	 * @return the message shown in the save alert popup.
	 * 
	 */
	public String saveDesign(String designName) {

		String saveResult = null;

		openLdSaveDialog();
		ldStoreDialogNameField.click();
		ldStoreDialogNameField.clear();
		ldStoreDialogNameField.sendKeys(designName);
		saveLdStoreButton.click();
		saveResult = getAlertText();

		return saveResult;

	}

	/**
	 * Saves a design  
	 *  This takes no parameters as it's just a click on the
	 *  save menu button once the design exists. 
	 * 
	 * @return the message shown in the save alert popup.
	 * 
	 */
	public String saveDesign() {

		String saveResult = null;

		saveButton.click();
		saveResult = getAlertText();

		return saveResult;

	}

	/**
	 * Gets the Design title/name for the canvas.
	 *
	 * @return 
	 */
	public String getDesignName () {
		return designTitle.getText().trim();
	}


	/**
	 * Rearranges the design on the canvas by pressing
	 * the "Arrange" button.
	 */
	public void arrangeDesign() {
		arrangeButton.click();

	}


	/**
	 * Opens the open design dialog to select and open the design on 
	 * canvas.
	 * 
	 * @param seqName
	 * 
	 */
	public void openDesign(String seqName) {

		// opens the dialog
		openLdOpenDialog();

		// parses the available designs
		List<WebElement> elements = ldTree.findElements(
				By.xpath("//*[contains(text(), '"+ seqName +"')]"));

		// Find the design that is in seqName and also is visible 
		// -- we've got to improve this though!
		for (int i = 0; i < elements.size(); i++) {
			WebElement el = elements.get(i);
			//System.out.println(el.getText());
			if (el.isDisplayed()) {
				el.click();
			}
		}

		// click on open
		openLdStoreButton.click();

	}


	/**
	 * Draws transitions in between the activities.
	 * It follows the order in which it finds the activities.
	 *  
	 */
	public void drawTransitionBtwActivities()  {

		WebElement svg = driver.findElement(By.tagName("svg"));

		List<WebElement> listActivities = svg.findElements(By.tagName("text"));

		int size = listActivities.size();
		//System.out.println("Activity #: " + size);
		for (int i = 0; i < size; i++) {
			// System.out.println("i: " + i );
			if (i+1 < size) {
				transitionButton.click();
				listActivities.get(i).click();
				listActivities.get(i+1).click();
				//System.out.println("i + 1= " + (i+1) );

			}
			//System.out.println("closing");
		}

	}

	/**
	 * Changes the name in the activity.
	 *
	 * @param activityTitle
	 * @param newActivityTitle 
	 */
	public void changeActivityTitle(String activityTitle, String newActivityTitle)  {

		WebElement svg = driver.findElement(By.tagName("svg"));

		WebElement activity = getActivityElement(svg, activityTitle);

		// select the activity
		activity.click();

		// Now change the Title using xpath to the activity Title input 
		// -- this should change to id in the future.
		driver.findElement(
				By.xpath("/html/body/div[14]/div[2]/div/table/tbody/tr[1]/td[2]/input"))
				.clear();
		driver.findElement(
				By.xpath("/html/body/div[14]/div[2]/div/table/tbody/tr[1]/td[2]/input"))
				.sendKeys(newActivityTitle);

		// click on canvas so the change takes effect
		canvas.click();

	}

	/**
	 * Opens an the authoring window for an learning activity
	 * This should be moved eventually to another test.
	 * 
	 * 
	 * @param activityName	the name of the activity to open
	 * @return				abstractPage factory so it can be manipulated.
	 * 
	 */
	public AbstractPage openSpecificActivity(String activityName) {

		WebElement svg = driver.findElement(By.tagName("svg"));

		WebElement act = getActivityElement(svg, activityName);

		Actions openAct = new Actions(driver);
		openAct.doubleClick(act).build().perform();;

		String popUpWindow = getPopUpWindowId(driver);

		driver.switchTo().window(popUpWindow);

		return PageFactory.initElements(driver, AbstractPage.class);


	}


	/**
	 * Given a design on canvas, it saves it under a new name
	 *
	 *
	 * @param newDesignName	new name to save as
	 * @return 					msg from alert
	 */
	public String saveAsDesign(String newDesignName) {

		String saveAsResult = null;
		saveDropButton.click();
		saveAsButton.click();
		ldStoreDialogNameField.click();
		ldStoreDialogNameField.clear();
		ldStoreDialogNameField.sendKeys(newDesignName);
		saveLdStoreButton.click();
		saveAsResult = getAlertText();

		return saveAsResult;

	}

	/**
	 *	Gets the activity names into a list.
	 *
	 *
	 * @return a string list with the activity names
	 */
	public List<String> getAllActivityNames() {

		List<String> allActivities = new ArrayList<>();

		WebElement svg = driver.findElement(By.tagName("svg"));

		List<WebElement> allActivitiesElements = getActivityElements(svg);

		// System.out.println("Allactivities size: " + allActivitiesElements.size());

		for (int i = 0; i < allActivitiesElements.size(); i++) {

			WebElement element = allActivitiesElements.get(i);
			// System.out.println(element.getText());
			allActivities.add(element.getText());

		}

		return allActivities;

	}



	/**
	 * Sets a group for an activity
	 * @param groupName		the group to set to the activity
	 * @param activityName 	the activity
	 * 
	 */
	public void setGroupForActivity(String groupName, String activityName) {

		WebElement svg = driver.findElement(By.tagName("svg"));

		WebElement activity = getActivityElement(svg, activityName);

		activity.click();

		Select groupDropDown = new Select(
				driver.findElement(
						By.xpath("/html/body/div[14]/div[2]/div/table/tbody/tr[2]/td[2]/select")));
		groupDropDown.selectByVisibleText(groupName);

		canvas.click();


	}



	/**
	 * Draws a transition between two given activities
	 * @param fromActivity	from activity name
	 * @param toActivity 	to activity name
	 */
	public void drawTransitionFromTo(String fromActivity, String toActivity) {

		WebElement svg = driver.findElement(By.tagName("svg"));

		WebElement fromActivityElement = getActivityElement(svg, fromActivity);
		WebElement toActivityElement = getActivityElement(svg, toActivity);

		transitionButton.click();
		fromActivityElement.click();
		toActivityElement.click();

		canvas.click();

	}



	/**
	 * Copy/pastes a given activity
	 * @param activity	activity to copy/paste
	 */
	public void copyPasteActivity(String activity) {

		WebElement svg = driver.findElement(By.tagName("svg"));
		// Select activity
		getActivityElement(svg, activity).click();

		copyButton.click();

		pasteButton.click();



	}


	public Boolean activityExists(String activity) {

		List<String> activityNames = getAllActivityNames();

		Boolean result = activityNames.contains(activity);

		return result;
	}



	/**
	 * Throws an activity into the rubbish bin
	 * 
	 * This method doesn't seem to be working as selenium seems to 
	 * have some issues when moving things around with SVG.
	 *
	 * @param activity 
	 */
	public void deleteActivity(String activity) {

		WebElement svg = driver.findElement(By.tagName("svg"));
		
		// Select activity
		WebElement activityToDelete = getActivityElement(svg, activity);

		//activityToDelete.click();
		
		WebElement bin = svg.findElement(By.id("rubbishBin"));

		Actions builder = new Actions(driver);
		@SuppressWarnings("deprecation")
		Action binActivityAction = builder
				.moveToElement(activityToDelete)
				.clickAndHold()
				.pause(500) // for some reason we need this pause otherwise it doesn't work
				.moveToElement(bin)
				.release()
				.build();  // Get the action

		binActivityAction.perform(); // Execute the Action

	}



	/**
	 * Sets/modifies the group setting for a group activity
	 * 
	 * @param groupActivityName		group to be set/modified
	 * @param groupType				group type choice (random, monitor, learner)
	 * @param isGroupsOrLearners	true = groups , false = learners 
	 * @param numberOfGroups		number of groups/learners
	 * @param groupNames 			group names ie: "Group 01, Group 02, ..."
	 * @param groupOptions			group options 
	 *                                ie: "boolean equalGroups, boolean viewLearnersBeforeSelection"
	 */
	public void setGroups(String groupActivityName, String groupType, boolean isGroupsOrLearners, 
			int numberOfGroups, String groupNames, String groupOptions) {

		WebElement svg = driver.findElement(By.tagName("svg"));
		// Select activity
		WebElement groupActivity = getActivityElement(svg, groupActivityName);

		groupActivity.click();

		switch (groupType) {
		case "random":

			// Set grouping type to random
			setGroupType(groupType);

			// Determine if group Groups or groups Learners
			setGroupOrLearners(numberOfGroups, isGroupsOrLearners);


			break;
		case "monitor":

			// Set grouping type to random
			setGroupType(groupType);

			// set GroupGroups
			setGroupNumber(numberOfGroups, isGroupsOrLearners);
			
			break;

		case "learner":

			// Set grouping type to random
			setGroupType(groupType);

			// Determine if group Groups or groups Learners
			setGroupOrLearners(numberOfGroups, isGroupsOrLearners);

			// Now if there's extra options for learners choice grouping
			if (!(groupOptions == null || groupOptions.trim().equals(""))) {
				String[] options = groupOptions.split(",");
				
				// set equal group size
				if (options[0].equals("true")) {
					WebElement equalGroupSizes = 
							driver.findElement(
									By.xpath("/html/body/div[14]/div[2]/div/table/tbody/tr[5]/td[2]/input"));
					
					equalGroupSizes.click();
					
				}
				
				// set view learners before selection
				if (options[1].equals("true")) {
					WebElement viewLearnersBeforeSelection = 
							driver.findElement(
									By.xpath("/html/body/div[14]/div[2]/div/table/tbody/tr[6]/td[2]/input"));
					
					viewLearnersBeforeSelection.click();
					
				}
				
				
			}


			break;

		default:
			break;
		}

		// If we the group names are given then add them
		if (!(groupNames == null || groupNames.trim().equals(""))) {

			setGroupNames(groupNames);

		} 

		// Click on canvas to remove focus and set values
		canvas.click();

	}
		
	
	/**
	 * Design description UI component
	 *
	 * @return DescriptionPage object
	 */
	public DescriptionPage designDescription() {
		
	
		return PageFactory.initElements(driver, DescriptionPage.class);
		
	}

	/**
	 * Design description UI component
	 *
	 * @return DescriptionPage object
	 */
	public BranchingPropertiesPage branchingProperties(String branchingName) {
		
		branchingName = branchingName + " start";
		WebElement svg = driver.findElement(By.tagName("svg"));
		WebElement branchingActivity = getActivityElement(svg, branchingName);

		branchingActivity.click();
		
		return PageFactory.initElements(driver, BranchingPropertiesPage.class);
		
	}
	
	/**
	 * Gate properties UI component
	 *
	 * @return DescriptionPage object
	 */
	public GatePropertyPage gateProperties() {
		

		WebElement svg = driver.findElement(By.tagName("svg"));
		WebElement gateActivity = getActivityElement(svg, AuthorConstants.GATE_TITLE);
		
		gateActivity.click();
		return PageFactory.initElements(driver, GatePropertyPage.class);
		
	}
	
	public String getGroupType(String groupActivityName) {
		final String selectGroupDropDownXpath = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[2]/td[2]/select";
		
		WebElement svg = driver.findElement(By.tagName("svg"));
		// Select activity
		WebElement groupActivity = getActivityElement(svg, groupActivityName);

		groupActivity.click();


		// Set grouping type to random
		Select groupDropDown = new Select(
				driver.findElement(
						By.xpath(selectGroupDropDownXpath)));
		
		String groupType = groupDropDown.getFirstSelectedOption().getAttribute("value");
		
		return groupType;
	}
	
	/**
	 * Returns the location for a given activity  
	 * 
	 * @param activityTitle
	 * @return Point location for the activity
	 */
	public Point getActivityLocation(String activityTitle) {
		WebElement svg = driver.findElement(By.tagName("svg"));
		// Select activity
		WebElement activity = getActivityElement(svg, activityTitle);
		
		return activity.getLocation();
	}

	public void dragGateToCanvas() {

		flowDropButton.click();
		gateButton.click();
		
		// Prepare the dragAndDrop action
		Actions builder = new Actions(driver);  // Configure the Action
		Action dropGate = builder
				.moveToElement(canvas)
				.click()
				.build();  

		// Execute the Action
		dropGate.perform();
		
	}

	/**
	 * Drags a branching activity into the canvas and sets up both start
	 * and ending points.
	 */
	public void dragBranchToCanvas() {
		
		flowDropButton.click();
		branchingButton.click();
		
		// Prepare the dragAndDrop action
		Actions builder = new Actions(driver);  // Configure the Action
		Action dropBranch = builder
				.moveToElement(canvas, 200, 280)
				.click()
				.moveByOffset(600, 0)
				.click()
				.build();  

		// Execute the Action
		dropBranch.perform();
	}
	
	
	/**
	 * Returns the correct popup id
	 * 
	 * @param driver
	 * @return 
	 */
	private String getPopUpWindowId(WebDriver driver) {

		String authorToolWindow = null;
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> iterator = handles.iterator();

		while (iterator.hasNext()){
			authorToolWindow = iterator.next();
		}

		return authorToolWindow;
	}

	/**
	 *  Clicks OK on the alert javascript popup
	 */
	private void checkAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			//exception handling
		}
	}

	/**
	 *  Clicks OK on the alert javascript popup and returns text
	 *  
	 * @return text from popup
	 */
	private String getAlertText() {

		String txt = null;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			txt = alert.getText();
			//driver.switchTo().defaultContent();
			alert.accept();
		} catch (Exception e) {
			//exception handling
		}
		return txt;
	}

	/**
	 * Parses thru the SVG design to find the WebElement for the activity name. 
	 *
	 * @param svg The SVG for the current canvas
	 * @param activityName the name of the activity
	 * @return 
	 */
	private WebElement getActivityElement(WebElement svg, String activityName) {

		WebElement activityElement = null;

		List<WebElement> listActivities = svg.findElements(By.tagName("text"));

		for (WebElement webElement : listActivities) {
			
			activityElement = webElement.findElement(By.tagName("tspan"));
			String name = activityElement.getText();

			if (activityName.equals(name)) {
				break;

			}

		}

		return activityElement;
	}
	
	/**
	 * Returns all activities in the design as WebElements in a list
	 * 
	 * @param svg	svg node on page.
	 * @return		list of web elements for activities in design  
	 */
	private List<WebElement> getActivityElements(WebElement svg) {

		List<WebElement> activitiesElements = new ArrayList<WebElement>();

		List<WebElement>  activityList = svg.findElements(By.tagName("text"));

		for (WebElement webElement : activityList) {
			WebElement activityElement = webElement.findElement(By.tagName("tspan"));
			activitiesElements.add(activityElement);
		}

		return activitiesElements;

	}


	/**
	 * Add group names to group activity
	 * 
	 * @param groupNames 
	 */
	private void setGroupNames(String groupNames) {
		final String nameGroupButtonXpath = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[7]/td/div/span";
		final String okButtonXpath = 
				"/html/body/div[15]/div[11]/div/button[1]/span";
		
		// open naming groups dialog
		WebElement nameGroupButton = driver.findElement(
				By.xpath(nameGroupButtonXpath));

		nameGroupButton.click();

		// populate group names:
		String[] listGroupNames = groupNames.split(",");
		//System.out.println(listGroupNames.length);

		int i = 1;
		for (String groupName : listGroupNames) {

			WebElement inputElement = driver.findElement(
					By.xpath("/html/body/div[15]/div[2]/input[" + Integer.toString(i) + "]"));

			inputElement.clear();
			inputElement.sendKeys(groupName);
			i++;

		}

		WebElement okButton = driver.findElement(
				By.xpath(okButtonXpath));

		okButton.click();
	}

	/**
	 * Sets the group type
	 *
	 * @param groupType 
	 */
	private void setGroupType(String groupType) {
		final String selectGroupDropDownXpath = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[2]/td[2]/select";

		// Set grouping type to random
		Select groupDropDown = new Select(
				driver.findElement(
						By.xpath(selectGroupDropDownXpath)));
		groupDropDown.selectByValue(groupType);

	}

	private void setGroupOrLearners(int numberOfGroups, boolean groupsOrLearners) {
		final String radioNumberOfGroupsXpath   = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[3]/td[2]/input";
		final String radioNumberOfLearnersXpath = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[4]/td[2]/input";
		
		String elementXpath = (groupsOrLearners) ? radioNumberOfGroupsXpath : radioNumberOfLearnersXpath;

		// Set Group to group number
		WebElement radioGroups = 
				driver.findElement(
						By.xpath(elementXpath));

		radioGroups.click();

		setGroupNumber(numberOfGroups, groupsOrLearners);

	}
	
	/**
	 * Sets the group number or the number of learners per group
	 *
	 * @param numberOfGroups
	 * @param groupsOrLearners 
	 */
	private void setGroupNumber(int numberOfGroups, Boolean groupsOrLearners) {
		final String inputNumGroupXpath   = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[3]/td[2]/span/input";
		final String inputNumLearnerXpath = 
				"/html/body/div[14]/div[2]/div/table/tbody/tr[4]/td[2]/span/input";

		String elementXpath = (groupsOrLearners) ? inputNumGroupXpath : inputNumLearnerXpath;

		// Get input for number of groups
		WebElement inputNumGroups = 	driver.findElement(
				By.xpath(elementXpath));

		//System.out.println("xpath: " + elementXpath);
		// set numb groups
		inputNumGroups.clear();
		inputNumGroups.sendKeys(Integer.toString(numberOfGroups));
	}

	
}
