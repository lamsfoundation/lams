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

package org.lamsfoundation.lams.pages.learner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learner.util.LearnerConstants;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.tool.forum.learner.TopicIndexPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class ControlFrame extends AbstractPage {
	

	@FindBy(name = "exitButton")
	private WebElement exitButton;

	@FindBy(name = "exportButton")
	private WebElement exportButton;
	
	@FindBy(id = "lessonTitleRow")
	private WebElement lessonTitleRow;
	
	@FindBy(id = "progressBarDiv")
	private WebElement progressBarDiv;
	
	@FindBy(id = "supportSeparatorRow")
	private WebElement supportSeparatorRow;
	
	@FindBy(id = "notebookSeparatorRow")
	private WebElement notebookSeparatorRow;
	
	@FindBy(name = "title")
	private WebElement notebookTitle;
	
	@FindBy(name = "entry")
	private WebElement notebookEntry;
	
	@FindBy(name = "viewAllButton")
	private WebElement viewAllButton;
	
	@FindBy(name = "saveButton")
	private WebElement saveButton;
	
	@FindBy(tagName = "svg")
	private WebElement svgLD;
	
	@FindBys({
		@FindBy(tagName = "svg"),
		@FindBy(tagName = "tspan")
	})
	private List<WebElement> svgActivities;
	
	@FindBys({
		@FindBy(tagName = "svg"),
		@FindBy(tagName = "path")
	})
	
	private List<WebElement> svgPaths;
	
	
	
	public ControlFrame(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	
	public String getLessonName() {
		
		return lessonTitleRow.getText().trim();
		
	}
	
	public IndexPage closeLearnerPage(String windowHandler) {
		
		exitButton.click();
		driver.switchTo().window(windowHandler);
		return PageFactory.initElements(driver, IndexPage.class);
	}
	
	public List<String> getActivitiesNamesFromSVG() {
	
		List<String> activityNames = new ArrayList<String>();
		
		for (WebElement activity : svgActivities) {

			activityNames.add(activity.getText());
			
		}
		
		return activityNames;
		
	}
	
	
	public String getPreviousActivityName() {
		return null;
		
	}
	
	public WebElement getPreviousActivity() {
		return exitButton;
		
	}

	public List<WebElement> getAllTransitions() {
		
		List<WebElement> transitions = new ArrayList<WebElement>(); 
		
		for (WebElement path : svgPaths) {
			
			if (path.getAttribute("fill").equals("none")) {
				transitions.add(path);
			}
			
		}
		
		return transitions;
		
	}
	
	public List<WebElement> getAllActivities() {
		
		return svgActivities;
		
	}
	
	public List<WebElement> getCompletedActivities() {
		
		List<WebElement> completedActivities = new ArrayList<WebElement>();
		
		for (WebElement path : svgPaths) {
			
			if (path.getAttribute("fill").equals(LearnerConstants.LEARNER_SVG_COMPLETED_PATH_FILLCOLOR)) {
				completedActivities.add(path);
			}
			
		}
		
		return completedActivities;
	}


	public TopicIndexPage openPreviouslyCompletedActivity() {
		
		List<WebElement> completed = getCompletedActivities();
		int numberOfCompletedActivities = completed.size();
		
		WebElement previousActivity = completed.get(numberOfCompletedActivities-1);
		
		Actions openAct = new Actions(driver);
		openAct.doubleClick(previousActivity).build().perform();

		String popUpWindow = getPopUpWindowId(driver);

		driver.switchTo().window(popUpWindow);
		
		return PageFactory.initElements(driver, TopicIndexPage.class);
		
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
	

}
