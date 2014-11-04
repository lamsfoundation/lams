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

package org.lamsfoundation.lams.pages.admin;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CreateEditCoursePage extends AbstractPage {

	@FindBy(name = "name")
	private WebElement courseName;
	
	@FindBy(name = "code")
	private WebElement courseCode;
	
	@FindBy(name = "description")
	private WebElement courseDescription;
	
	@FindBy(name = "localeId")
	private WebElement courseLocaleId;
	
	@FindBy(name = "stateId")
	private WebElement courseStatusId;
		
	@FindBy(name = "courseAdminCanAddNewUsers")
	private WebElement courseAdminCanAddNewUsers;
	
	@FindBy(name = "courseAdminCanBrowseAllUsers")
	private WebElement courseAdminCanBrowseAllUsers;
	
	@FindBy(name = "courseAdminCanChangeStatusOfCourse")
	private WebElement courseAdminCanChangeStatusOfCourse;
	
	@FindBy(name = "enableCourseNotifications")
	private WebElement enableCourseNotifications;
	
	@FindBy(name = "enableGradebookForMonitors")
	private WebElement enableGradebookForMonitors;
	
	@FindBy(name = "enableGradebookForLearners")
	private WebElement enableGradebookForLearners;
	
	@FindBy(name = "enableSingleActivityLessons")
	private WebElement enableSingleActivityLessons;
	
	@FindBy(id = "saveButton")
	private WebElement saveButton;
	
	@FindBy(id = "cancelButton")
	private WebElement cancelButton;
	
	
	public CreateEditCoursePage(WebDriver driver) {
		super(driver);
		
	}

	public CreateEditCoursePage setCourseName(String name) {
		
		courseName.click();
		courseName.clear();
		courseName.sendKeys(name);
	
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
		
	}
	
	public String getCourseName() {
		return courseName.getAttribute("value");
	}
	
	public CreateEditCoursePage setCourseCode(String code) {
		
		courseCode.click();
		courseCode.clear();
		courseCode.sendKeys(code);
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public String getCourseCode() {
		
		return courseCode.getAttribute("value");
	}
	
	public CreateEditCoursePage setDescription(String description) {
		
		courseDescription.click();
		courseDescription.clear();
		courseDescription.sendKeys(description);
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public String getDescription() {
		
		return courseDescription.getAttribute("value");
		
	}
	
	public CreateEditCoursePage setLocale(String localeName) {
		
		Select inputSelector = new Select(courseLocaleId);
		
		inputSelector.selectByVisibleText(localeName);
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}	
	
	public String getLocale() {
		
		Select inputSelector = new Select(courseLocaleId);
		
		return inputSelector.getFirstSelectedOption().getText();
		
	}
	
	public CreateEditCoursePage setStatus(String status) {
		
		Select inputSelector = new Select(courseStatusId);
		
		inputSelector.selectByVisibleText(status);
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public String getStatus() {
		
		Select inputSelector = new Select(courseStatusId);
		
		return inputSelector.getFirstSelectedOption().getText();
		
	}
	
	public CreateEditCoursePage setCourseAdminCanAddNewUsers() {
		
		courseAdminCanAddNewUsers.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getCourseAdminCanAddNewUsers() {
		
		return courseAdminCanAddNewUsers.isSelected();
	}

	public CreateEditCoursePage setCourseAdminCanBrowseAllUsers() {
		
		courseAdminCanBrowseAllUsers.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getCourseAdminCanBrowseAllUsers() {
		
		return courseAdminCanBrowseAllUsers.isSelected();
	}
	

	public CreateEditCoursePage setCourseAdminCanChangeStatusOfCourse() {
		
		courseAdminCanChangeStatusOfCourse.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getCourseAdminCanChangeStatusOfCourse() {
		
		return courseAdminCanChangeStatusOfCourse.isSelected();
	}
	

	public CreateEditCoursePage setEnableCourseNotifications() {
		
		enableCourseNotifications.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getEnableCourseNotifications() {
		
		return enableCourseNotifications.isSelected();
	}
	
	
	public CreateEditCoursePage setEnableGradebookForMonitors() {
		
		enableGradebookForMonitors.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getEnableGradebookForMonitors() {
		
		return enableGradebookForMonitors.isSelected();
	}
	
	public CreateEditCoursePage setEnableGradebookForLearners() {
		
		enableGradebookForLearners.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getEnableGradebookForLearners() {
		
		return enableGradebookForLearners.isSelected();
	}
	
	public CreateEditCoursePage setEnableSingleActivityLessons() {
		
		enableSingleActivityLessons.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}
	
	public Boolean getEnableSingleActivityLessons() {
		
		return enableSingleActivityLessons.isSelected();
	}
	
	public CourseManagementPage save() {
		saveButton.click();
		return PageFactory.initElements(driver, CourseManagementPage.class);
	}
	
}
