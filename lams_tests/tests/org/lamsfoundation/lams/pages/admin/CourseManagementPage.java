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

import java.util.List;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.admin.CreateEditCoursePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * 
 * Course management page
 * 
 * This page is for sysadmins and global course managers to create/edit courses and users. 
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class CourseManagementPage extends AbstractPage {

	/** 
	 * Page buttons 
	 * These are the buttons on the interface.
	 */
	
	@FindBy(id = "userCreate")
	private WebElement createUser;

	@FindBy(id = "findUsers")
	private WebElement findUsers;
	
	@FindBy(id = "createCourse")
	private WebElement createCourse;

	@FindBy(id = "manageGlobalRoles")
	private WebElement manageGlobalRoles;
	
	@FindBy(id = "manageUsers")
	private WebElement manageUsers;
	
	@FindBy(id = "closeLessons")
	private WebElement closeLessons;
	
	@FindBy(id = "createNewSubcourse")
	private WebElement createNewSubcourse;
	
	@FindBy(id = "courseStatus")
	private WebElement courseStatus;

	@FindBy(id = "courseName")
	private WebElement courseName;
	
	@FindBy(id = "idsorter")
	private WebElement idSorter;
	
		
	public CourseManagementPage(WebDriver driver) {
		super(driver);
		
	}
	
	/**
	 * Form page to Create/Edit a course
	 *	 *
	 * @return {@link CreateEditCoursePage}
	 */
	public CreateEditCoursePage createEditCourse() {

		createCourse.click();
		
		return PageFactory.initElements(driver, CreateEditCoursePage.class);
	}

	/**
	 * Returns true if the course exists
	 *
	 * @param courseName
	 * @return true/false
	 */
	public boolean courseExists(String courseName) {
		
		List<WebElement> courses = driver.findElements(By.linkText(courseName));
		
		return ((courses.size() > 0) ? true: false);
		
	}
	
	
	/**
	 * Form page to Create/Edit a user
	 *	
	 * @return {@link CreateEditUserPage}
	 */
	public CreateEditUserPage createEditUser() {

		createUser.click();
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}

	public CourseManagementPage getCourse(String courseName) {
		
		driver.findElement(By.linkText(courseName)).click();
		
		return PageFactory.initElements(driver, CourseManagementPage.class);
		
	}

	public UserManagementPage manageUsers() {
		
		manageUsers.click();
		
		return PageFactory.initElements(driver, UserManagementPage.class);
		
	}
	
	public CourseManagementPage clickIdSorter() {
		
		idSorter.click();
		
		return PageFactory.initElements(driver, CourseManagementPage.class);
	}
	

}
