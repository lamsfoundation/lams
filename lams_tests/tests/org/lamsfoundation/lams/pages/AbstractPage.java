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

package org.lamsfoundation.lams.pages;

import org.lamsfoundation.lams.LamsConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * AbstractPage for PageModel and PageFactory patterns
 * All other pages will inherit from this. 
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class AbstractPage {
	
	protected WebDriver driver;
		
	public AbstractPage (WebDriver driver) {
		this.driver = driver;
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public LoginPage navigateToLamsLogin() {
		driver.navigate().to(LamsConstants.TEST_SERVER_URL);
		return PageFactory.initElements(driver, LoginPage.class);
	}
	
	public String getTitle() {
		return driver.getTitle();
	}

	public void maximizeWindows() {
		driver.manage().window().maximize();
	}
	
	public void closeWindow() {
		driver.close();
	}
	
	public String getWindowTitle() {
		return driver.getTitle();
	}

}
