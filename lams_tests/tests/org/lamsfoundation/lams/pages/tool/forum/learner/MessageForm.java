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

import org.lamsfoundation.lams.LamsConstants;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.pages.util.LamsPageUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MessageForm extends AbstractPage {
	
	@FindBy(name = "message.subject")
	private WebElement messageSubject;
	
	@FindBy(id = "message.body__lamstextarea")
	private WebElement messageBody;
	
	@FindBy(id = "cancelButton")
	private WebElement cancelButton;
	
	@FindBy(id = "submitButton")
	private WebElement submitButton;
	
	@FindBy(id = "char-left-div")
	private WebElement charLeft;
	
	@FindBy(name = "attachmentFile")
	private WebElement attachmentFile;
	
	@FindBy(id = "removeAttachmentButton")
	private WebElement removeAttachmentButton;
	
	@FindBy(className = "info")
	private WebElement infoWarning;
	
	@FindBy(id = "char-left-div")
	private WebElement charactersLeft;
	
	@FindBy(id = "char-required-div")
	private WebElement charactersRequired;
	
	public MessageForm(WebDriver driver) {
		super(driver);
		
	}

	public void createTopicMessage(String subject, String body) {
		
		addSubject(subject);
		
		messageBody.click();
		messageBody.sendKeys(body);
		
		submitNewTopicMessage();
		
	}

	public void submitNewTopicMessage() {
		
		submitButton.click();
		
	}
	
	public void createTopicMessage(String body) {
		
		addBody(body);
		
		submitNewTopicMessage();
	}
	
	public void createTopicMessageCkEditor(String subject, String content) {
		
		if (subject != "") {
			addSubject(subject);
		}
		
		messageSubject.click();
		
		LamsPageUtil.setCkEditorContent(driver, ForumConstants.FORUM_LEARNER_CKEDITOR_ID, content);
		
		submitButton.click();
	}
	
	
	public void editTopicMessage(String subject, String body) {
		
		addSubject(subject);
		addBody(body);		
		submitButton.click();
		
	}
	
	private void addSubject(String subject) {
		
		messageSubject.click();
		messageSubject.clear();
		messageSubject.sendKeys(subject);
		
	}
	
	private void addBody(String body) {
		
		messageBody.click();
		messageBody.clear();
		messageBody.sendKeys(body);
		
	}
	
	
	public void removeAttachment() {
		
		removeAttachmentButton.getAttribute("innerHTML");
		removeAttachmentButton.click();
		submitButton.click();
		
	}
	
	
	public String getTopicSubject() {
		
		return messageSubject.getAttribute("value").trim();
	}
	
	public String getTopicBody() {
		
		return messageBody.getAttribute("value").trim();
	}

	public String getCharLeft() {
		
		return charLeft.getText().trim();
		
	}

	public void createTopicWithAttachment( String body, String attachment) {
		
		addBody(body);

		attachmentFile.sendKeys(LamsConstants.RESOURCES_PATH + attachment);
		
		submitNewTopicMessage();
	}

	public String getInfo() {
		
		return infoWarning.getText();
		
	}

	public String getCharactersLeft() {
		
		return charactersLeft.getText();
				
	}

	public String getCharacterRequired() {
		
		return charactersRequired.getText();
		
	}
	
	
}
