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

package org.lamsfoundation.lams.pages.author;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ImportDesignPage extends AbstractPage {
	
	/**	
     * 	Import Design properties
     * 
     */	
	
	public static final String IMPORT_SUCCESS_MSG = 
			"Learning design and activities imported successfully.";
	
	public static final String IMPORT_FAIL_MSG = 
			"Learning design import failed";
	
	@FindBy(name = "UPLOAD_FILE")
	private WebElement uploadFile;
	
	@FindBy(xpath = "//*[@id=\"importForm\"]/p/a/span")
	private WebElement submitButton;
	
	@FindBy(xpath = "//*[@id=\"content\"]/h2")
	private WebElement importResult;
	
	@FindBy(xpath = "//*[@id=\"content\"]/div[1]/p")
	private WebElement errorMsg;
	
	
	@FindBy(xpath = "//*[@id=\"content\"]/div/a[1]/span")
	private WebElement anotherImportButton;
	
	@FindBy(xpath = "//*[@id=\"content\"]/div/a[2]/span")
	private WebElement closeButton;
	
	
	public ImportDesignPage(WebDriver driver) {
		super(driver);
		
	}

	/**
	 * Uploads a learning design
	 *
	 * @param fileName name of the file to import
	 * @return {@link ImportDesignPage}
	 */
	public ImportDesignPage uploadDesign(String fileName) {
		
		//uploadFile.clear();
		uploadFile.sendKeys(LamsConstants.RESOURCES_PATH + fileName);
		
		return PageFactory.initElements(driver, ImportDesignPage.class);
	}
	
	/**
	 * Submit design form
	 * @return {@link ImportDesignPage}
	 */
	public ImportDesignPage submitDesign() {

		submitButton.click();
		
		return PageFactory.initElements(driver, ImportDesignPage.class);
	}
	
	/**
	 * Returns the import result
	 *
	 * @return List<Strings>. First element result (0 = failed, 1 = success)
	 *         Second element is the fail/success message.
	 */
	public List<String> importStatus() {
		
		List<String> importOutput = new ArrayList<String>();
		
		String importMsg = importResult.getText();
		
		if (importMsg.contains(IMPORT_FAIL_MSG)) {
			importOutput.add("0");
			importOutput.add(IMPORT_FAIL_MSG + " " + errorMsg.getText());
		} else {
			importOutput.add("1");
			importOutput.add(importMsg);
			
		}
		
		closeDialog();
		return importOutput;
		
	}

	public FLAPage closeDialog() {
		
		closeButton.click();
		driver.switchTo().window("FlashlessAuthoring");
		
		return PageFactory.initElements(driver, FLAPage.class);
	}
	
	
	


}
