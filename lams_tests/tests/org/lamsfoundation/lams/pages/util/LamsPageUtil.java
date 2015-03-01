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

package org.lamsfoundation.lams.pages.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LamsPageUtil {

	
	
	public static boolean isElementPresentById(WebDriver driver,WebElement webElement, String elementId) {
		
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	    List<WebElement> isEnabled = webElement.findElements(By.id(elementId));
	    driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		
	    return ((isEnabled.size() > 0) ? true : false);
		
	}
	
	public static boolean isElementPresentByClassname(WebDriver driver, WebElement webElement, String classname) {
		
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	    List<WebElement> isEnabled = webElement.findElements(By.className(classname));
	    driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		
	    return ((isEnabled.size() > 0) ? true : false);
		
	}
	
	public static String getCkEditorContent(WebDriver driver, String ckEditorId) {
		
        String getText = (String) ((JavascriptExecutor)
                driver).executeScript("return (CKEDITOR.instances['"+ ckEditorId + "'].getData());");

        getText = getText.replaceAll("[\n\r]", "");
        return getText;
		
	}
	
	public static void setCkEditorContent(WebDriver driver, String ckEditorId, String content) {
		
		content = content.replace("'", "\\'");
		
        // Insert text to CKEditor via javascript 
        ((JavascriptExecutor)driver).executeScript("CKEDITOR.instances['" + ckEditorId + "'].setData('" + content +"');");

		
	}
	
	/**
	 *  Clicks OK on the alert javascript popup and returns text
	 *  
	 * @return text from popup
	 */
	public static String getAlertText(WebDriver driver) {

		String txt = null;

		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			txt = alert.getText();
			alert.accept();
		} catch (Exception e) {
			//exception handling
		}
		return txt;
	}
	
}
