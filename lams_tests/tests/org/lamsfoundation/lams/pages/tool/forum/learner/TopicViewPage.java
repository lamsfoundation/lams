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

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.pages.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.pages.util.LamsPageUtil;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class TopicViewPage extends AbstractPage {
	
	@FindBy(name = "backToForum")
	private WebElement backToForumButton;
	
	@FindBys({
		@FindBy(id = "message")
	})
	private List<WebElement> allMessages;
	
	@FindBys({
		@FindBy(id = "message"),
		@FindBy(id = "msgSubject")
	})
	private List<WebElement> allMessageSubjects;
	
	@FindBys({
			@FindBy(id = "message"),
			@FindBy(id = "person")
	})
	private List<WebElement> allMessagePersons;
	
	@FindBys({
		@FindBy(id = "message"),
		@FindBy(id = "msgDate")
	})
	private List<WebElement> allMessageDate;
	
	@FindBys({
		@FindBy(id = "message"),
		@FindBy(className = "rating-starts-caption")
	})
	private List<WebElement> allRatingCaptions;
	
	@FindAll({
		@FindBy(id = "replyButton")
	})
	private List<WebElement> allRepliableMessages;
	
	
	public TopicViewPage(WebDriver driver) {
		super(driver);
		
	}

	
	public TopicIndexPage goBackToTopics() {
		
		backToForumButton.click();
		
		return PageFactory.initElements(driver, TopicIndexPage.class);
	}
	
	
	public List<WebElement> getAllMessages() {
		
		return allMessages;
		
	}
	
	public String getMsgBody(WebElement message) {
		
		return message.findElement(By.id("msgBody")).getText();
		
	}
	
	public String getMsgBodyCkEditor(WebElement message) {
		
		return LamsPageUtil.getCkEditorContent(driver, ForumConstants.FORUM_LEARNER_CKEDITOR_ID);
		
	}
	
	public String getMsgAuthor(WebElement message) {
		
		return message.findElement(By.id("author")).getText().trim();
		
	}
	
	public String getMsgDate(WebElement message) {
		
		return message.findElement(By.id("date")).getText().trim();
	}
	
	public String getMsgSubject(WebElement message) {
		
		return message.findElement(By.id("subject")).getText().trim();
	}
	
	public String getMsgAttachmentFilename(WebElement message) {
		
		return message.findElement(By.id("attachments")).getText().trim();
	}
	
	
	public MessageForm clickReply(WebElement message) {
		
		message.findElement(By.id("replyButton")).click();
		
		return PageFactory.initElements(driver, MessageForm.class);
	}

	public void replyFirstMessage(String body) {
		
		List<WebElement> messages = getAllMessages();
		
		clickReply(messages.get(0)).createTopicMessage(body);
		
	}
	
	public void replyToMessage(WebElement message, String body) {
				
		clickReply(message).createTopicMessage(body);
		
	}
	
	public void replyToMessageWithAttachment(WebElement message, String body, String attachment) {
		
		clickReply(message).createTopicWithAttachment(body, attachment);
		//clickReply(message).submitNewTopicMessage();
		
	}

	public MessageForm clickEdit(WebElement message) {
		
		message.findElement(By.id("editButton")).click();
		
		return PageFactory.initElements(driver, MessageForm.class);
	}


	public boolean isReplyEnabled(WebElement message) {
			    
        return LamsPageUtil.isElementPresentById(driver, message, "replyButton");
	
	}
	
	public boolean isEditEnabled(WebElement message) {

        return LamsPageUtil.isElementPresentById(driver, message, "editButton");
		
	}
	
	public boolean isRateable(WebElement message) {
		
		// if the message is rateable, then give it a rate
		
		return LamsPageUtil.isElementPresentByClassname(driver, message, "rating-stars-div");
	    
	}
	


	public void rateMessage(WebElement message, int rating) {
		
		if (isRateable(message)) {
			
			WebElement hoverItem = message.findElement(By.className("jStar"));
			Actions clicker = new Actions(driver);

			int xOffset = 0;
			switch(rating) {
				case 0:	xOffset = -57;
						break;
				case 1: xOffset = -28;
						break;
				case 2: xOffset = 0;
						break;
				case 3: xOffset = 28;
						break;
				case 4: xOffset = 57;
						break;
			}
			
			clicker.moveToElement(hoverItem).moveByOffset(xOffset, 0).click().perform();

			
		}
		
	}
	
	public List<String> getRatedMessages() {
	
		String ratingText;
		
		List<String> allRated = new ArrayList<String>();
		for (WebElement webElement: allRatingCaptions) {
			
			ratingText = webElement.getText();
			
			if (ratingText.contains("1 vote")){
			
				allRated.add(webElement.getText());
			}
			
		}
		
		return allRated;
		
	}


	public boolean isAttachementPresent(WebElement message) {
		
		return LamsPageUtil.isElementPresentById(driver, message, "attachments");

	}


	public void replyToMessageCkEditor(WebElement message, String content) {
		
		clickReply(message).createTopicMessageCkEditor("", content);
		
	}


	public int getNumberOfRepliableMessages() {
		
		return allRepliableMessages.size();
		
	}

}
