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

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LearnerPage  extends AbstractPage {
	
	/** 
	 * Menu buttons 
	 * These are the menu buttons on the interface.
	 */

	@FindBy(id = "controlFrame")
	private WebElement controlFrame;
	
	@FindBy(id = "contentFrame")
	private WebElement contentFrame;
	
		
	public LearnerPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public LearnerPage name() {
		
		return PageFactory.initElements(driver, LearnerPage.class);	
		
	}
	
	public ControlFrame openControlFrame() {


		
		return PageFactory.initElements(driver, ControlFrame.class);
	}
	
	
	public ContentFrame openContentFrame() {

		driver.switchTo().frame("contentFrame");
		
		return PageFactory.initElements(driver, ContentFrame.class);
	}
	
	
}
