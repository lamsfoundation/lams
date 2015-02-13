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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class AdvancedTab  extends AbstractPage {
	
	/**
	 * Forum author advanced tab page properties
	 */

	@FindBy(name="forum.lockWhenFinished")
	private WebElement lockWhenFinished;
	
	@FindBy(name="forum.allowEdit")
	private WebElement allowEdit;
	
	@FindBy(name="forum.allowRateMessages")
	private WebElement allowRateMessages;	
	
	@FindBy(id="minimumRate")
	private WebElement minimumRate;
	
	@FindBy(id="maximumRate")
	private WebElement maximumRate;	

	@FindBy(name="forum.allowUpload")
	private WebElement allowUpload;
	
	@FindBy(name="forum.allowRichEditor")
	private WebElement allowRichEditor;	
	
	@FindBy(name="forum.limitedMinCharacters")
	private WebElement limitedMinCharacters;
	
	@FindBy(name="forum.minCharacters")
	private WebElement minCharacters;	
	
	@FindBy(name="forum.limitedMaxCharacters")
	private WebElement limitedMaxCharacters;
	
	@FindBy(name="forum.maxCharacters")
	private WebElement maxCharacters;
	
	@FindBy(name="forum.notifyLearnersOnForumPosting")
	private WebElement notifyLearnersOnForumPosting;
	
	@FindBy(name="forum.notifyTeachersOnForumPosting")
	private WebElement notifyTeachersOnForumPosting;	
	
	@FindBy(name="forum.notifyLearnersOnMarkRelease")
	private WebElement notifyLearnersOnMarkRelease;
	
	@FindBy(name="forum.reflectOnActivity")
	private WebElement reflectOnActivity;
	
	@FindBy(name="forum.reflectInstructions")
	private WebElement reflectInstructions;	
	
	@FindBy(name="forum.allowNewTopic")
	private WebElement allowNewTopic;
	
	@FindBy(name="forum.minimumReply")
	private WebElement minimumReply;	
	
	@FindBy(name="forum.maximumReply")
	private WebElement maximumReply;
	
	
	public AdvancedTab(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public void setLockedWhenFinished(boolean value) {
		
		if(value && !lockWhenFinished.isSelected()){

			lockWhenFinished.click();
			
		} else if (lockWhenFinished.isSelected() && !value) {
			
			lockWhenFinished.click();
		}

	}
	
	public boolean isLockWhenFinished() {
		
		return lockWhenFinished.isSelected();
		
	}
	
	public void setAllowEdit(boolean value) {
		
		if(value && !allowEdit.isSelected()) {

			allowEdit.click();
			
		} else if(allowEdit.isSelected() && !value) {
		
			allowEdit.click();
			
		}
	}
	
	public boolean isAllowEdit() {
		
		return allowEdit.isSelected();
		
	}
	
	public void setAllowRateMessages(boolean value) {
		
		if(value && !allowRateMessages.isSelected()) {

			allowRateMessages.click();
			
		} else if(allowRateMessages.isSelected() && !value) {
		
			allowRateMessages.click();
			
		}
	}
	
	
	public boolean isAllowRateMessages() {
		
		return allowRateMessages.isSelected();
		
	}
	
	public void setMinimumRate(String value) {
		
		if(allowRateMessages.isEnabled()) {
			minimumRate.sendKeys(value);
		}

	}
	
	public String getMinimumRate() {
		WebElement selectMinRate = minimumRate;
		Select select = new Select(selectMinRate);
		
		return select.getFirstSelectedOption().getText();
	}
	
	
	public void setMaximumRate(String value) {
		
		if(allowRateMessages.isEnabled()) {
			maximumRate.sendKeys(value);
		}	
		
	}
	
	public String getMaximumRate() {
		WebElement selectMaxRate = maximumRate;
		Select select = new Select(selectMaxRate);
		
		return select.getFirstSelectedOption().getText();
	}
	
	public void setAllowUpload(Boolean value) {
		
		if(value && !allowUpload.isSelected()) {

			allowUpload.click();
			
		} else if(allowUpload.isSelected() && !value) {
		
			allowUpload.click();
			
		}
	}
	
	public boolean isAllowUpload() {
		
		return allowUpload.isSelected();
	}
	
	public void setAllowRichText(Boolean value) {
		
		if(value && !allowRichEditor.isSelected()) {

			allowRichEditor.click();
			
		} else if(allowRichEditor.isSelected() && !value) {
		
			allowRichEditor.click();
			
		}
	}
	
	
	public boolean isAllowRichEditor() {
		
		return allowRichEditor.isSelected();
				
	}
	
	public void setLimitedMinCharacters(Boolean value) {
		
		if(value && !limitedMinCharacters.isSelected()) {

			limitedMinCharacters.click();
			
		} else if(limitedMinCharacters.isSelected() && !value) {
		
			limitedMinCharacters.click();
			
		}
	}
	
	public boolean isLimitedMinCharacters() {
		
		return limitedMinCharacters.isSelected();
	}
	
	
	public void setMinCharacters(String value) {
	
		if(isLimitedMinCharacters()) {
			minCharacters.clear();
			minCharacters.sendKeys(value);
			
		}
		
	}
	
	public String getMinCharacters() {
		
		return minCharacters.getAttribute("value").trim();
		
	}
	
	
	
	public void setLimitedMaxCharacters(Boolean value) {
		
		if(value && !limitedMaxCharacters.isSelected()) {

			limitedMaxCharacters.click();
			
		} else if(limitedMaxCharacters.isSelected() && !value) {
		
			limitedMaxCharacters.click();
			
		}
	}
	
	public boolean isLimitedMaxCharacters() {
		
		return limitedMaxCharacters.isSelected();
	}
	
	public void setMaxCharacters(String value) {
		
		if(isLimitedMaxCharacters()) {
			
			maxCharacters.clear();
			maxCharacters.sendKeys(value);
			
		}
		
	}
	
	public String getMaxCharacters() {
		
		return maxCharacters.getAttribute("value").trim();
		
	}	
	
	public void setNotifyLearnersOnForumPosting(Boolean value) {

		if(value && !notifyLearnersOnForumPosting.isSelected()) {

			notifyLearnersOnForumPosting.click();
			
		} else if(notifyLearnersOnForumPosting.isSelected() && !value) {
		
			notifyLearnersOnForumPosting.click();
			
		}
		
	}
	
	
	public boolean isNotifyLearnersOnForumPosting() {
		
		return notifyLearnersOnForumPosting.isSelected();
				
	}
	
	public void setNotifyTeachersOnForumPosting(Boolean value) {
		
		if(value && !notifyTeachersOnForumPosting.isSelected()) {

			notifyTeachersOnForumPosting.click();
			
		} else if(notifyTeachersOnForumPosting.isSelected() && !value) {
		
			notifyTeachersOnForumPosting.click();
			
		}
		
	}
	
	public boolean isNotifyTeachersOnForumPosting() {
		
		return notifyTeachersOnForumPosting.isSelected();
				
	}
	
	public void setNotifyLearnersOnMarkRelease(Boolean value) {
		
		if(value && !notifyLearnersOnMarkRelease.isSelected()) {

			notifyLearnersOnMarkRelease.click();
			
		} else if(notifyLearnersOnMarkRelease.isSelected() && !value) {
		
			notifyLearnersOnMarkRelease.click();
			
		}
		
	}
	
	public boolean isNotifyLearnersOnMarkRelease() {
		
		return notifyLearnersOnMarkRelease.isSelected();
	}
	
	public boolean isReflectOnActivity() {
		
		return reflectOnActivity.isSelected();
	}
	
	
	public boolean isAllowNewTopic() {
		
		return allowNewTopic.isSelected();
	}
}
