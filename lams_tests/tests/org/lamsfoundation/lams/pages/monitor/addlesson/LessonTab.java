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

package org.lamsfoundation.lams.pages.monitor.addlesson;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LessonTab extends AbstractPage {

	public static final String lessonTitle = "Select the sequence to add a lesson, and click on Add now";
	
	
	/**
	 * Lesson tab attributes 
	 */
	
	@FindBy(id = "tabLessonTitle")
	private WebElement tabLessonTitle;
	
	@FindBy(id = "lessonNameInput")
	private WebElement lessonNameInput;
	
	
	/**
	 * LD Tree attributes
	 */
	
	@FindBy(id = "ygtvc0")
	private WebElement ldTreeRoot;
	
	@FindBy(id = "ygtvt1")
	private WebElement openCloseUserFolder;
	
	@FindBy(id = "ygtvlabelel1")
	private WebElement userFolderName;
	
	@FindBy(id = "ygtvc1")
	private WebElement userFolderNodes;
	
	@FindBy(id = "ygtvt2")
	private WebElement openCloseMyCourseFolder;
	
	@FindBy(id = "ygtvlabelel2")
	private WebElement myCourseFolderName;
	
	@FindBy(id = "ygtvc2")
	private WebElement myCourseFolderNodes;
	
	@FindBy(id = "ygtvt3")
	private WebElement openClosePublicFolder;
	
	@FindBy(id = "ygtvlabelel3")
	private WebElement publicFolderName;
	
	@FindBy(id = "ygtvc3")
	private WebElement publicFolderNodes;
		
	@FindBy(id = "ldScreenshotAuthor")
	private WebElement imgDesign;
	
	
	
	public LessonTab(WebDriver driver) {
		super(driver);
		
	}

	
	/**
	 * Returns lessonTab title 
	 * 
	 * This is just static text to check that you are in 
	 * the correct tab.
	 * 
	 * @return lessonTab title
	 */
	public String getLessonTabTitle() {
		
		return tabLessonTitle.getText();
		
	}
	
	/**
	 * Sets the lesson name
	 * @param lessonName
	 * @return {@link LessonTab}
	 */
	public LessonTab setLessonName(String lessonName) {
		
		lessonNameInput.click();
		lessonNameInput.clear();
		lessonNameInput.sendKeys(lessonName);
		
		return PageFactory.initElements(driver, LessonTab.class);
	}
	
	/**
	 * Returns lesson name
	 * @return lesson name
	 */
	public String getLessonName() {
		
		return lessonNameInput.getAttribute("value");
	}

	
	/**
	 * Collapses / open folders
	 * 
	 * Gets a folder name and clicks on it to open/collapse it.
	 * 
	 * @param folder
	 * @return {@link LessonTab}
	 */
	public LessonTab clickFolder(String folder) {
		
		switch (folder) {
		case "user":
			
			openCloseUserFolder.click();
			
			break;
			
		case "courses":
			
			openCloseMyCourseFolder.click();
			
			break;
			
		case "public":
			
			openClosePublicFolder.click();
			
			break;
		}
		
		return PageFactory.initElements(driver, LessonTab.class);
	}
	
	
	/**
	 * Returns the nodes for the designs
	 * 
	 * This is probably not the best way to do this as it exposes
	 * WebElement objects to the test layer. But at the same time 
	 * for the purpose of testing it does a fine job. 
	 * 
	 * @param folder
	 * @return List of nodes as WebElements
	 */
	public List<WebElement> getFolderNodes(String folder) {
		
		List<WebElement> designs = new ArrayList<WebElement>();
		
		switch (folder) {
		case "user":
			
			designs = userFolderNodes.findElements(By.tagName("span"));
			
			break;
			
		case "courses":
			
			designs = myCourseFolderNodes.findElements(By.tagName("span"));
			
			break;
			
		case "public":
			
			designs = publicFolderNodes.findElements(By.tagName("span"));
			
			break;
		}
		return designs;
		
	}


	/**
	 * Checks if when the design is selected, the image for the design 
	 * is displayed.
	 * 
	 * @return 
	 */
	public boolean isDesignImageDisplayed() {
		
		String imageURL = ""; 
		
		imageURL = imgDesign.getAttribute("src");
		
		return (imageURL.isEmpty() ? false : true);
	}


	/**
	 * Clicks on design to create a lesson with.
	 * 
	 * @param design
	 * @return {@link LessonTab}
	 */
	public LessonTab clickDesign(WebElement design) {
		
		design.click();
		
		return PageFactory.initElements(driver, LessonTab.class);
	}
	
	
	
}
