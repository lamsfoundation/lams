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

package org.lamsfoundation.lams.pages.tool.forum;

import java.util.Iterator;
import java.util.Set;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class AuthorPage extends AbstractPage {

	/**
	 * Forum author page properties
	 */
	
	@FindBy(id = "tab-middle-link-1")
	private WebElement basicTab;
	
	@FindBy(id = "tab-middle-link-2")
	private WebElement advancedTab;
	
	@FindBy(id = "tab-middle-link-3")
	private WebElement conditionsTab;
	
	@FindBy(id = "cancelButton")
	private WebElement cancelButton;
	
	@FindBy(id = "saveButton")
	private WebElement saveButton;
	
	@FindBy(className="close")
	private WebElement closeButton;
	
	@FindBy(className="editForm")
	private WebElement reEdit;
	
	@FindBy(className="warning")
	private WebElement warning;
	
	public AuthorPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 *  Open basic tab
	 *
	 * @return 
	 */
	public BasicTab openBasicTab() { 
		basicTab.click();
		
		return PageFactory.initElements(driver, BasicTab.class);		
	}
	
	/**
	 *  Open advanced tab
	 *
	 * @return 
	 */
	public AdvancedTab openAdvancedTab() { 
		advancedTab.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);		
	}
	
	
	public void cancel(String windowHandler) {
		
		cancelButton.click();
		getAlertText();
		driver.switchTo().window(windowHandler);
		
	}

	public void save() {
		saveButton.click();
	}
	
	public void close(String windowHandler) {
		closeButton.click();
		driver.switchTo().window(windowHandler);
	}
	
	public void reEdit() {
		reEdit.click();
	}

	/**
	 *  Clicks OK on the alert javascript popup and returns text
	 *  
	 * @return text from popup
	 */
	private String getAlertText() {

		String txt = null;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			txt = alert.getText();
			alert.accept();

		} catch (Exception e) {
		}
		return txt;
	}
	
    public String getPopUpWindowId(WebDriver driver) {

        String authorToolWindow = null;
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> iterator = handles.iterator();

        while (iterator.hasNext()){
                authorToolWindow = iterator.next();
                System.out.println("handle: " + authorToolWindow);
        }

        return authorToolWindow;
    }

    public String getWarningMsg(){
    	
    	return warning.getText();
    }
    
    
}
