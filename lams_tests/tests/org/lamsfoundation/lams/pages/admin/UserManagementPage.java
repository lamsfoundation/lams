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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * User management page
 * 
 * This also includes the user/course enrollment page
 * 
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class UserManagementPage extends AbstractPage {

	@FindBy(id = "addRemoveUsers")
	private WebElement addRemoveUsers;
	
	@FindBy(id = "term")
	private WebElement searchField;

	@FindBy(id = "potential")
	private WebElement potentialUsers;

	@FindBy(id = "nextButton")
	private WebElement nextButton;
	
	
	public UserManagementPage(WebDriver driver) {
		super(driver);
		
	}

	/**
	 * Enrolls a user into a course based on the roles given. 
	 * 
	 * @param login username
	 * @param roles roles' list
	 */
	public void addRemoveUser(String login, List<String> roles) {
		
		addRemoveUsers.click();
		searchField.click();
		searchField.clear();
		searchField.sendKeys(login);
		searchField.sendKeys(Keys.ENTER);
		
		List<WebElement> users = potentialUsers.findElements(By.tagName("a"));
		
		
		for (WebElement user : users) {
		
			user.click();
			
		}
		
		nextButton.click();
		
		
		for (String role : roles) {
			
			if (role.contains("Author")) {
				driver.findElement(By.id(login + "Role3")).click();
				
			} else if (role.contains("Monitor")) {
				driver.findElement(By.id(login + "Role4")).click();
				
			} else if (role.contains("Learner")) {
				driver.findElement(By.id(login + "Role5")).click();
				
			} else if (role.contains("Course Admin")) {
				driver.findElement(By.id(login + "Role6")).click();
				
			} else if (role.contains("Course Manager")) {
				driver.findElement(By.id(login + "Role2")).click();
			}
			
		}
		
		driver.findElement(By.id("saveButton")).click();
		
		
	}

}
