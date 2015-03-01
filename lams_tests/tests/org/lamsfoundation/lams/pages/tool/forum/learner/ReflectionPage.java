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

package org.lamsfoundation.lams.pages.tool.forum.learner;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ReflectionPage extends AbstractPage {

	@FindBy(name = "entryText")
	private WebElement entryText;
	
	@FindBy(className = "nextActivity")
	private WebElement nextActivity;
	
	public ReflectionPage(WebDriver driver) {
		super(driver);
		
	}

	public void postReflection(String reflectionTxt) {
	
		entryText.click();
		entryText.clear();
		entryText.sendKeys(reflectionTxt);
		
	}

	public void nextActivity(String learnerHandler) {

		nextActivity.click();
		driver.switchTo().window(learnerHandler);		
		
	}

}
