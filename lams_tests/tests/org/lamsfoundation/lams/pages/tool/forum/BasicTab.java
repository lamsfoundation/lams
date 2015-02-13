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

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicTab extends AbstractPage {
	
	/**
	 * Forum author basic tab page properties
	 */
	
	@FindBy(name="forum.title")
	private WebElement forumTitle;
	
	@FindBy(id = "messageListArea")
	private WebElement messageListArea;
	
	@FindBy(id = "addTopic")
	private WebElement addTopic;
	
	
	@FindBy(name="message.subject")
	private WebElement messsageSubject;

	@FindBy(id="cancelMsg")
	private WebElement cancelMessage;
	
	@FindBy(id="addMsg")
	private WebElement addMessage;
		
	
	
	public BasicTab(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public String getTitle() {
		
		return forumTitle.getAttribute("value").trim();
	
	}
	
	public String getIntructions() {
		
	       String getText = (String) ((JavascriptExecutor)
	                driver).executeScript("return (CKEDITOR.instances['forum.instructions'].getData());");

	        getText = getText.replaceAll("[\n\r]", "");
	        return getText;
	}
	
	public void addTopic() {
		
		addTopic.click();
		driver.switchTo().frame("messageArea");
		
	}
		
	public void setMessageSubject(String subject) {
		
		messsageSubject.sendKeys(subject);
		
	}
	
	public void setMessageBody(String body) {
		
    	// Insert text to CKEditor via javascript 
    	((JavascriptExecutor) 
    			driver).executeScript("CKEDITOR.instances['message.body'].setData('" + body +"');");
    	

		
	}
	
	public void addMessage() {
		
		addMessage.click();
		driver.switchTo().defaultContent();
	}
	
	public boolean messageExists(String topicTitle) {

		return messageListArea.getText().contains(topicTitle);
		
	}
	
	public void deleteMesage(Integer number) {
		
		driver.findElement(By.id("delete"+number)).click();
		getAlertText();
		
	}
	
	
	/**
	 *  Clicks OK on the alert javascript popup and returns text
	 *  
	 * @return text from popup
	 */
	public String getAlertText() {

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
	
	
}
