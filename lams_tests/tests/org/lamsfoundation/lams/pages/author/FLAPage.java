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

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
	
	@FindBy(id = "importSequenceButton")
	private WebElement importSequenceButton;
	
	@FindBy(id = "importPartSequenceButton")
	private WebElement importPartSequenceButton;

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
	
	@FindBy(id = "gateButton")
	private WebElement gateButton;
		
	@FindBy(id = "branchingButton")
	private WebElement branchingButton;
	
	@FindBy(id = "groupButton")
	private WebElement groupButton;
	
	@FindBy(id = "annotateButton")
	private WebElement annotateButton;
		
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
	private WebElement sequenceTitle;
	
	@FindBy(id = "svg")
	private WebElement svgCanvas;
	
	
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
        		.moveByOffset(x, y)
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
		ldStoreDialogNameField.sendKeys(designName);
		saveLdStoreButton.click();
		saveResult = getAlertText();
	
		return saveResult;
		
	}
	
	/**
	 * Saves a design  
	 *  This takes no parameters as it's just a click on the
	 *  save menu button once the sequence/design exists. 
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
	 * Gets the Sequence title/name for the canvas.
	 *
	 * @return 
	 */
	public String getSequenceName () {
		return sequenceTitle.getText().trim();
	}
	

	/**
	 * Rearranges the design on the canvas by pressing
	 * the "Arrange" button.
	 */
	public void arrangeDesign() {
		arrangeButton.click();
		
	}


	/**
	 * Opens the open sequence dialog to select and open the design on 
	 * canvas.
	 * 
	 * @param seqName
	 * 
	 */
	public void openDesign(String seqName) {
		
		// opens the dialog
		openLdOpenDialog();
		
		// parses the available sequences
		List<WebElement> elements = ldTree.findElements(
				By.xpath("//*[contains(text(), '"+ seqName +"')]"));
		
		// Find the sequence that is in seqName and also is visible 
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
			System.out.println("i: " + i );
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
	 * @param newSequenceName	new name to save as
	 * @return 					msg from alert
	 */
	public String saveAsDesign(String newSequenceName) {
		
		String saveAsResult = null;
		saveDropButton.click();
		saveAsButton.click();
		ldStoreDialogNameField.clear();
		ldStoreDialogNameField.sendKeys(newSequenceName);
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
		
		System.out.println("Allactivities size: " + allActivitiesElements.size());
		
		for (int i = 0; i < allActivitiesElements.size(); i++) {
			
			WebElement element = allActivitiesElements.get(i);
			System.out.println(element.getText());
			allActivities.add(element.getText());
			
		}
		
		return allActivities;
		
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
	
	
}
