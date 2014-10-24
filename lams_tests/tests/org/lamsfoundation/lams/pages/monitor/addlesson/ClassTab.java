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

import java.util.List;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ClassTab extends AbstractPage {
	
	
	public static final String classTabTitle = "Please use drag n' drop to select or unselect monitors and learners";
	
	/**
	 * Lesson tab attributes 
	 */
	
	@FindBy(id = "tabClassTitle")
	private WebElement tabClassTitle;
	
	@FindBy(id = "unselected-monitors")
	private WebElement unselectedMonitors;
	
	@FindBy(id = "monitorMoveToRight")
	private WebElement monitorMoveToRight;

	@FindBy(id = "monitorMoveToLeft")
	private WebElement monitorMoveToLeft;

	@FindBy(id = "selected-monitors")
	private WebElement selectedMonitors;

	@FindBy(id = "unselected-learners")
	private WebElement unselectedLearners;

	@FindBy(id = "learnerMoveToRight")
	private WebElement learnerMoveToRight;

	@FindBy(id = "learnerMoveToLeft")
	private WebElement learnerMoveToLeft;

	@FindBy(id = "selected-learners")
	private WebElement selectedLearners;

	public ClassTab(WebDriver driver) {
		super(driver);
		
	}

	public ClassTab addAllMonitorsToLesson() {
		
		List<WebElement> monitorsAvailable = unselectedMonitors.findElements(By.tagName("div"));
		
		for (WebElement monitor : monitorsAvailable) {
			
			monitor.click();
			monitorMoveToRight.click();
			
		}
		
		return PageFactory.initElements(driver, ClassTab.class);
	}
	
	public int getNumberSelectedMonitors() {
		
		return selectedMonitors.findElements(By.tagName("div")).size();
	}

	public int getNumberUnselectedMonitors() {
		
		return unselectedMonitors.findElements(By.tagName("div")).size();
				
	}

	public int getNumberSelectedLearners() {
		
		return selectedLearners.findElements(By.tagName("div")).size();
	}
	
	public int getNumberUnselectedLearners() {

		return unselectedLearners.findElements(By.tagName("div")).size();
	}
	
	
}
