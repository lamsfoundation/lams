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

import java.util.List;

import org.lamsfoundation.lams.learner.util.LearnerConstants;
import org.lamsfoundation.lams.pages.AbstractPage;
import org.lamsfoundation.lams.pages.learner.LearnerPage;
import org.lamsfoundation.lams.pages.util.LamsPageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class TopicIndexPage extends AbstractPage {

	/**
	 * Forum learner page attributes attributes 
	 */
	
	@FindBy(name = "newtopic")
	private WebElement addTopicButton;
	
	@FindBy(id = "forumTitle")
	private WebElement forumTitle;
	
	@FindBy(id = "forumInstructions")
	private WebElement forumInstructions;
	
	@FindBy(className = "info")
	private WebElement info;
	
	@FindBy(name = "refresh")
	private WebElement refreshButton;
	
	@FindBy(id = "topicTable")
	private WebElement topicTable;
	
	@FindBys({
		@FindBy(id = "topicTable"),
		@FindBy(id = "topicTitle")
	})
	private List<WebElement> allTopicTitles;
	
	@FindBy(id = "finishButton")
	private WebElement finishButton;
	
	@FindBy(name = "continue")
	private WebElement continueButton;
	
	// This is the div that contains the 
	// finish button
	@FindBy(id = "rightButtons")
	private WebElement rightButtons;
	
	@FindBy(id = "reflection")
	private WebElement reflection;
	
	@FindBy(id = "editReflection")
	private WebElement editReflectionButton;
	
	@FindBy(className = "warning")
	private WebElement warningMessage;
	
	
	public TopicIndexPage(WebDriver driver) {
		super(driver);

	}
	
	public void refresh() {
		refreshButton.click();
	}
	
	public MessageForm createTopic() {
		
		addTopicButton.click();
		
		return PageFactory.initElements(driver, MessageForm.class);
		
	}
	
	public TopicViewPage openTopicBySubject(String subject) {
	
		List<WebElement> topics = getTopics();
		
		WebElement topicLink = null;
		
		for (WebElement topic : topics) {
			String title = topic.getText().trim();
			
			if(title.equals(subject.trim())) {
				topicLink = topic;
			}
			
		}
		
		topicLink.click();
		
		return PageFactory.initElements(driver, TopicViewPage.class);
	}
	
	public List<WebElement> getTopics() {
		
		return  allTopicTitles;
		
	}

	public boolean topicExistBySubject(String subject) {

		boolean exists = false;
		
		for (WebElement title : allTopicTitles) {
			
			String isTitle = title.getText();
			
			if (isTitle.equals(subject)) {
				exists = true;
				break;
			}
			
		}
		
		
		return exists;
	}

	public String getNumberOfRepliesBySubject(String title) {
		
		int row = getRowForSubject(title);
		row++;
		String replies = topicTable.findElement(By.xpath("//tbody/tr[" + row + "]/td[3]")).getText();
		
		return replies;
	}
	
	public String getNumberOfNewMsgsBySubject(String title) {
		
		int row = getRowForSubject(title);
		row++;
		String newMsgs = topicTable.findElement(By.xpath("//tbody/tr[" + row + "]/td[4]")).getText();
		
		return newMsgs;
		
	}
	
	/**
	 * Continues to the next activity in the lesson
	 * 
	 * Use this to continue to the next activity
	 */
	public void nextActivity() {
		
		finishButton.click();
		
	}

	/**
	 * Continues to the next activity or closes pop-up window
	 *
	 * Use this for when you have opened a previous activity in a popup
	 * 
	 * @param windowHandler 
	 */
	public void nextActivity(String windowHandler) {
		
		finishButton.click();
		driver.switchTo().window(windowHandler);
		
	}
	
	public String getForumTitle() {
		
		return forumTitle.getText();
		
	}
	
	
	public String getForumInstructions() {
		
		return forumInstructions.getText();
		
	}
	
	


	public LearnerPage returnLearnerHandler(String learnerHandler) {
		
		driver.switchTo().window(learnerHandler);
		
		return PageFactory.initElements(driver, LearnerPage.class);
	}

	public boolean isNewTopicEnabled() {
		
		return addTopicButton.isEnabled();

	}
	
	public String getInfoWarning() {
		
		return info.getText();
		
	}
	
	public boolean isNextActivityButtonPresent() {
	
		rightButtons.getAttribute("innerHTML");
		return LamsPageUtil.isElementPresentById(driver, rightButtons, LearnerConstants.LEARNER_NEXT_ACTIVITY_ID);
		
	}
	
	
	private int getRowForSubject(String subject) {
		
		int counter = 0;
		for (WebElement title : allTopicTitles) {
			
			counter++;
			String isTitle = title.getText().trim();
			
			if (isTitle.equals(subject)) {
				
				break;
			}
		}

		return counter;
		
	}

	public ReflectionPage clickContinue() {
		
		continueButton.click();		
		
		return PageFactory.initElements(driver, ReflectionPage.class);
	}

	public String getReflection() {
		
		return reflection.getText();

	}
	
	public ReflectionPage editReflection() {
		
		editReflectionButton.click();
		
		return PageFactory.initElements(driver, ReflectionPage.class);
	
	}

	public String getRepliesLimitWarning() {

		return warningMessage.getText();
	}
	
}
