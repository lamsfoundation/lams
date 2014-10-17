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

/**
 * 
 */
package org.lamsfoundation.lams.pages.author;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Description Page within FLA for setting a general design description and license
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class DescriptionPage extends AbstractPage {
	
	@FindBy(id = "ldDescriptionHideTip")
	private WebElement ldDescriptionHideTip;

	
	@FindBy(id = "ldDescriptionLicenseSelect")
	private WebElement ldDescriptionLicenseSelect;
	
	public DescriptionPage(WebDriver driver) {
		super(driver);

	}

	
	public FLAPage navigateToFLAPage() {
		closeDesignDescriptionDialog();
		return new FLAPage(driver);
	}
	
	/**
	 * Opens Description Dialog 
	 * 
	 */
	public void openDesignDescriptionDialog() {

		ldDescriptionHideTip.click();

	}
	
	/**
	 * Close Description Dialog 
	 * 
	 */
	public void closeDesignDescriptionDialog() {

		// toggles to open/close
		openDesignDescriptionDialog();
		
	}	
	
	/**
	 * Add description to CkEditor 
	 * 
	 * @param designDescription 
	 */
	public void addDesignDescription(String designDescription) {
		
    	// Insert text to CKEditor via javascript 
    	((JavascriptExecutor) 
    			driver).executeScript("CKEDITOR.instances['ldDescriptionFieldDescription'].setData('" + designDescription +"');");
    	


	}
	
	/**
	 * Selects a license from the dropdown menu
	 *
	 * @param licenseId
	 * @return text of the license selected
	 */
	public String addDesignLicense(int licenseId) {
		
		Select licenseDropDown = new Select(ldDescriptionLicenseSelect);
		licenseDropDown.selectByIndex(licenseId);
    
		return licenseDropDown.getFirstSelectedOption().getText();
	}
	
	/**
	 * Returns the design description entered
	 *
	 * @return string with description text
	 */
	public String getDesignDescription() {
		
    	String getSetText = (String) ((JavascriptExecutor) 
    			driver).executeScript("return (CKEDITOR.instances['ldDescriptionFieldDescription'].getData());");
    	
    	return getSetText;
		
		
	}
	
	
}
