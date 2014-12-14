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
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * Monitor Lessontab
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class LessonTab extends AbstractPage {

	
	/**
	 * Lesson tab attributes 
	 */
	
	@FindBy(id = "tabLessonLessonName")
	private WebElement tabLessonLessonName;
	
	@FindBy(id = "tabLessonLessonDescription")
	private WebElement tabLessonLessonDescription;

	@FindBy(id = "lessonStateLabel")
	private WebElement lessonStateLabel;
	
	@FindBy(id = "learnersStartedPossibleCell")
	private WebElement learnersStartedPossibleCell;
	
	@FindBy(id = "viewLearnersButton")
	private WebElement viewLearnersButton;

	@FindBy(id = "editClassButton")
	private WebElement editClassButton;
	
	@FindBy(id = "notificationButton")
	private WebElement notificationButton;
	
	@FindBy(id = "lessonManageField")
	private WebElement lessonManageField;
	
	@FindBy(id = "exportAvailableField")
	private WebElement exportAvailableField;
	
	@FindBy(id = "presenceAvailableField")
	private WebElement presenceAvailableField;

	@FindBy(id = "imAvailableField")
	private WebElement imAvailableField;

	@FindBy(id = "lessonStartDateSpan")
	private WebElement lessonStartDateSpan;
	
	@FindBy(id = "scheduleDatetimeField")
	private WebElement scheduleDatetimeField;
	
	@FindBy(id = "startLessonButton")
	private WebElement startLessonButton;

	
	public LessonTab(WebDriver driver) {
		super(driver);
		
	}

	public String getLessonTitle() {
		
		return tabLessonLessonName.getText();
	}
	
	public EditClassPage openEditClass() {
		
		editClassButton.click();
		
		return PageFactory.initElements(driver, EditClassPage.class);	
		
	}
	
	public String getLessonDescription() {
		return tabLessonLessonDescription.getText();
	}
	
	public boolean isLessonScheduled() {
		
		return ((scheduleDatetimeField.getCssValue("display").contains("none")) ? false : true);
		
		
	}
	
	public boolean isNotificationsPresent() {

		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		
		Boolean isNotificationsPresent = driver.findElements(By.id("notificationButton")).size() > 0 ? true: false;
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return isNotificationsPresent;
	}
	
	public Boolean isPresenceEnabled() {
		
		return presenceAvailableField.isSelected();
		
		
	}

	public boolean isImEnabled() {
		
		return imAvailableField.isSelected();
	}
	
}
