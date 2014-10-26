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

package org.lamsfoundation.lams.pages.monitor.addlesson;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdvancedTab  extends AbstractPage {

	@FindBy(id = "introEnableField")
	private WebElement introEnableField;
	
	@FindBy(id = "cke_introDescription")
	private WebElement ckEditorDescription;
	
	@FindBy(id = "introImageField")
	private WebElement introImageField;
	
	@FindBy(id = "startMonitorField")
	private WebElement startMonitorField;
	
	@FindBy(name = "learnerRestart")
	private WebElement learnerRestart;
	
	@FindBy(name = "liveEditEnable")
	private WebElement liveEditEnable;
	
	@FindBy(name = "notificationsEnable")
	private WebElement notificationsEnable;
	
	@FindBy(name = "portfolioEnable")
	private WebElement portfolioEnable;
	
	@FindBy(id = "presenceEnableField")
	private WebElement presenceEnableField;
	
	@FindBy(id = "imEnableField")
	private WebElement imEnableField;
	
	@FindBy(id = "splitLearnersField")
	private WebElement splitLearnersField;
	
	@FindBy(id = "splitLearnersCountField")
	private WebElement splitLearnersCountField;
	
	@FindBy(id = "schedulingEnableField")
	private WebElement schedulingEnableField;
	
	@FindBy(id = "schedulingDatetime")
	private WebElement schedulingDatetime;
	
	
	public AdvancedTab(WebDriver driver) {
		super(driver);
		
	}

	
	public AdvancedTab setEnableLessonIntro() {
		
		introEnableField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
		
	}
	
	public Boolean isEnableLessonIntro() {
		
		return introEnableField.isEnabled();
		
	}
	
	
	public AdvancedTab setIntroText(String txt) {
		
    	// Insert text to CKEditor via javascript 
    	((JavascriptExecutor) 
    			driver).executeScript("CKEDITOR.instances['introDescription'].setData('" + txt + "');"); 
    	
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
	
	public String getIntroText() {
	
        String getText = (String) ((JavascriptExecutor)
                driver).executeScript("return (CKEDITOR.instances['introDescription'].getData());");

        getText = getText.replaceAll("[\n\r]", "");
        return getText;
		
	}
	
	public AdvancedTab setDisplayDesign() {
		
		introImageField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isDisplayDesign() {
		
		return introImageField.isEnabled();
		
	}
	
	public AdvancedTab setStartInMonitor() {
		
		startMonitorField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isStartInMonitor() {
		
		return startMonitorField.isEnabled();
		
	}
	
	public AdvancedTab setLearnerRestart() {
		
		learnerRestart.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isLearnerRestart() {
		
		return learnerRestart.isEnabled();
		
	}
	
	public AdvancedTab setEnableLiveEdit() {
		
		liveEditEnable.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isEnableLiveEdit() {
		
		return liveEditEnable.isEnabled();
		
	}
	
	public AdvancedTab setEnableLessonNotifications() {
		
		notificationsEnable.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isEnableLessonNotifications() {
		
		return notificationsEnable.isEnabled();
		
	}
	
	public AdvancedTab setEnablePortfolio() {
		
		portfolioEnable.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isEnablePortfolio() {
		
		return portfolioEnable.isEnabled();
		
	}
	
	public AdvancedTab setEnablePresence() {
		
		presenceEnableField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isEnablePresence() {
		
		return presenceEnableField.isEnabled();
		
	}
	
	public AdvancedTab setEnableIM() {
		
		imEnableField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isEnableIM() {
		
		return imEnableField.isEnabled();
		
	}
	
	public AdvancedTab setSplitLearners() {
		
		splitLearnersField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isSplitLearners() {
		
		return splitLearnersField.isEnabled();
		
	}
	
	public AdvancedTab setSplitCounter() {
		
		splitLearnersCountField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public String getSplitCounter() {
		
		return splitLearnersCountField.getAttribute("value");
		
	}
	
	public AdvancedTab setScheduleEnable() {
		
		schedulingEnableField.click();
		
		return PageFactory.initElements(driver, AdvancedTab.class);
	}
		
	public Boolean isScheduledEnable() {
		
		return schedulingEnableField.isEnabled();
		
	}
	
}
