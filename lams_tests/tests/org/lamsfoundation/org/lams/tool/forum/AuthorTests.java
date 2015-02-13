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

package org.lamsfoundation.org.lams.tool.forum;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.pages.tool.forum.AuthorPage;
import org.lamsfoundation.lams.pages.tool.forum.BasicTab;
import org.lamsfoundation.lams.pages.tool.forum.AdvancedTab;
import org.lamsfoundation.lams.pages.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;


/**
 * 
 * Authoring Forum tests
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */

public class AuthorTests {
	
	
	private static final String RANDOM_INT = LamsUtil.randInt(0, 9999);
	
	private LoginPage onLogin;
	private IndexPage index;
	private FLAPage fla;
	private AuthorPage forumAuthorPage;
	
	public String flaHandler;
	
	WebDriver driver;
	
	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		fla = PageFactory.initElements(driver, FLAPage.class);
		forumAuthorPage = PageFactory.initElements(driver, AuthorPage.class);
		onLogin.navigateToLamsLogin().loginAs("test3", "test3");
		
	}

	
	/**
	 * Opens FLA interface
	 */
	@Test
	public void openFLA() {
		
		FLAPage fla = new FLAPage(driver);
		fla = index.openFla();
		flaHandler = driver.getWindowHandle();
		fla.maximizeWindows();
		Assert.assertEquals(AuthorConstants.FLA_TITLE, fla.getTitle(), "The expected title is not present");
		
	}
	
	/**
	 * Drop a forum in the canvas
	 */
	@Test(dependsOnMethods={"openFLA"})
	public void dragForumToCanvas() {
		
		String forumTitle  = "Basic " + RANDOM_INT;
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 100, 100)
		.changeActivityTitle(AuthorConstants.FORUM_TITLE, forumTitle);
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
	}
	
	/**
	 * Open forum authoring
	 */
	@Test(dependsOnMethods={"dragForumToCanvas"})
	public void openForumAuthoring() {
		
		// open forum author
		AuthorPage forumAuthorPage = fla.openForumAuthoring("Basic " + RANDOM_INT);

		String forumWindowTitle = forumAuthorPage.getWindowTitle();
		
		// Assert forum window title
		Assert.assertEquals(forumWindowTitle, ForumConstants.FORUM_TITLE);

	}
	
	/**
	 * Check forum default values basic tab
	 */
	@Test(description="Check default values basic tab", dependsOnMethods={"openForumAuthoring"})
	public void checkBasicTabDefaultValues() {
		
		BasicTab basicTab = forumAuthorPage.openBasicTab();
		
		// Get default title and instructions
		String forumTitle = basicTab.getTitle();
		String forumInstructions = basicTab.getIntructions();
				
		// Assert forum defaults (basic tab)
		
		Assert.assertEquals(forumTitle, ForumConstants.FORUM_TITLE);
		Assert.assertEquals(forumInstructions, ForumConstants.FORUM_INSTRUCTIONS);
	
	}
	
	/**
	 * Check forum default values advanced tab
	 */
	@Test(description="Check default values for advanced tab",
			dependsOnMethods={"checkBasicTabDefaultValues"})
	public void checkAdvancedTabDefaultValues() {
		
		AdvancedTab advancedTab = forumAuthorPage.openAdvancedTab();
		
		// Get default values
		Boolean isLockWhenFinished = advancedTab.isLockWhenFinished();
		Boolean isAllowEdit = advancedTab.isAllowEdit();
		Boolean isAllowRateMessages = advancedTab.isAllowRateMessages();
		Boolean isAllowUploads = advancedTab.isAllowUpload();
		Boolean isAllowRichEditor = advancedTab.isAllowRichEditor();
		Boolean isLimitedMinCharacters = advancedTab.isLimitedMinCharacters();
		Boolean isLimitedMaxCharacters = advancedTab.isLimitedMaxCharacters();
		Boolean isNotifyLearnersOnForumPosting = advancedTab.isNotifyLearnersOnForumPosting();
		Boolean isNotifyTeachersOnForumPosting = advancedTab.isNotifyTeachersOnForumPosting();
		Boolean isNotifyLearnersOnMarkRelease = advancedTab.isNotifyLearnersOnMarkRelease();
		Boolean isReflectOnActivity = advancedTab.isReflectOnActivity();
		Boolean isLimitedReplies = advancedTab.isLimitReplies();
		
		Assert.assertFalse(isLockWhenFinished, "Locked when finish should be false by default");
		Assert.assertTrue(isAllowEdit, "Allow learners to change their posting should be true by default");
		Assert.assertFalse(isAllowRateMessages, "Rate messages should be false by default");
		Assert.assertFalse(isAllowUploads, "Learners attacchments should be false by default");
		Assert.assertFalse(isAllowRichEditor, "CKEditor should be false by default");
		Assert.assertFalse(isLimitedMinCharacters, "There's no minimum number of characters by default");
		Assert.assertTrue(isLimitedMaxCharacters, "There should be a limit in the max number of characters by default");
		Assert.assertFalse(isNotifyLearnersOnForumPosting, "Option to notify learners should be off by default ");
		Assert.assertFalse(isNotifyTeachersOnForumPosting, "Option to notify teacher should be false by default");
		Assert.assertFalse(isNotifyLearnersOnMarkRelease, "Notify learners on mark release should be false by default");
		Assert.assertFalse(isReflectOnActivity, "Reflection should false by default");
		Assert.assertFalse(isLimitedReplies, "Limited replies should be false by default");
		
	}

	/**
	 * Add message
	 */
	@Test(description="Check forum author validation when no topic is listed and no new threads are allowed",
			dependsOnMethods={"checkAdvancedTabDefaultValues"})
	public void checkValidationErrors() {
		
		// Check validation when
		forumAuthorPage.openBasicTab().deleteMesage(0);
		forumAuthorPage.openAdvancedTab().setLimitReplies("1", "5");
		forumAuthorPage.save();
		
		// Assert error message
		
		String warningMsg = forumAuthorPage.getWarningMsg();
		
		System.out.println("Warning: " + warningMsg);
		Assert.assertTrue(warningMsg.contains(ForumConstants.FORUM_WARNING_REPLY_LIMITS_TXT));
		
		// Cancel the changes so we don't need to rebuild the original content
		forumAuthorPage.cancel(flaHandler);
		
		// As cancel closes the pop-up, reopen the same activity again and set
		// it ready for the next test
		fla.openForumAuthoring("Basic " + RANDOM_INT);
	}
	
	/**
	 * Add message
	 */
	@Test(description="Add message/topic", dependsOnMethods={"checkValidationErrors"})
	public void addTopic() {
		
		String msgTitle = "Topic# " + RANDOM_INT;
		String msgBody  = "<strong>" + RANDOM_INT + "</strong>" + "<p>" + ForumConstants.FORUM_BODY_TXT + "</p><p>" + ForumConstants.FORUM_BODY_TXT + "</p>";
		
		BasicTab basicTab = forumAuthorPage.openBasicTab();
		
		basicTab.addTopic();
		basicTab.setMessageSubject(msgTitle);
		basicTab.setMessageBody(msgBody);
		basicTab.addMessage();
		
		forumAuthorPage.save();
		
		// Check that the message was saved appropriately
		
		forumAuthorPage.reEdit();
		
		boolean topicExists = basicTab.messageExists(msgTitle);
		Assert.assertTrue(topicExists, "For some reason message title " + msgTitle + " didn't save");;
		
	}
	
	/**
	 * Delete message
	 */
	@Test(description="Delete message/topic", dependsOnMethods={"addTopic"})
	public void deleteTopic() {
		
		// Delete the first default message
		BasicTab basicTab = forumAuthorPage.openBasicTab();
		basicTab.deleteMesage(0);
		
		forumAuthorPage.save();
		forumAuthorPage.reEdit();
		
		// Assert that default message was deleted
		boolean msgExists = basicTab.messageExists(ForumConstants.FORUM_DEFAULT_TOPIC);
		
		Assert.assertFalse(msgExists, "Default message still exists");
		
		forumAuthorPage.cancel(flaHandler);
	
	}
	
	
	
	/**
	 * Add forum with Locked when finished
	 */
	@Test(description="Add forum with Locked when finished", dependsOnMethods={"deleteTopic"})
	public void addForumLockedWhenFinished() {

		String forumTitle = "Locked " + RANDOM_INT;
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 300, 100)
		.changeActivityTitle("Forum", forumTitle);
		
		// Assert new forum activity title
		
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		forumAuthorPage.openAdvancedTab().setLockedWhenFinished(true);
		
		forumAuthorPage.save();
		
		// Assert locked when finished was saved
		
		forumAuthorPage.reEdit();
				
		boolean isLockedWhenFinshed = forumAuthorPage.openAdvancedTab().isLockWhenFinished();
		
		Assert.assertTrue(isLockedWhenFinshed, "Option locked when finished is not set");
		
		forumAuthorPage.cancel(flaHandler);
	}
	
	/**
	 * Add forum with no re-edits allowed
	 */
	@Test(description="Add forum with no re-edits allowed", dependsOnMethods={"addForumLockedWhenFinished"})
	public void addForumEditNotAllowed() {

		String forumTitle = "NoEdit " + RANDOM_INT;
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 500, 100)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		forumAuthorPage.openAdvancedTab().setAllowEdit(false);
		
		forumAuthorPage.save();
		
		// Assert edits are not allowed
		
		forumAuthorPage.reEdit();
				
		boolean isAllowEdit = forumAuthorPage.openAdvancedTab().isAllowEdit();
		
		Assert.assertFalse(isAllowEdit, "Option Allow Edit is still set");
		
		forumAuthorPage.cancel(flaHandler);
	}
	
	/**
	 * Add forum with ratings
	 */
	@Test(description="Add forum with ratings", dependsOnMethods={"addForumEditNotAllowed"})
	public void addForumRatings() {

		String forumTitle = "Ratings " + RANDOM_INT;
		String minimumRate = "1";
		String maximumRate = "5"; 
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE, 700, 100)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		forumAuthorPage.openAdvancedTab().setAllowRateMessages(true);
		forumAuthorPage.openAdvancedTab().setMinimumRate(minimumRate);
		forumAuthorPage.openAdvancedTab().setMaximumRate(maximumRate);

		forumAuthorPage.save();
		
		// Assert rating is on and the min and max are set 
		
		forumAuthorPage.reEdit();
				
		boolean isAllowEdit = forumAuthorPage.openAdvancedTab().isAllowRateMessages();
		String minRate = forumAuthorPage.openAdvancedTab().getMinimumRate();
		String maxRate = forumAuthorPage.openAdvancedTab().getMaximumRate();
		
		Assert.assertTrue(isAllowEdit, "Option Allow Edit is still set");
		Assert.assertEquals(minRate, minimumRate, "Minimum rate is not the same");
		Assert.assertEquals(maxRate, maximumRate, "Maximum rate is not the same");
		
		forumAuthorPage.cancel(flaHandler);
	}
	
	/**
	 * Add forum with allow uploads on
	 */
	@Test(description="Add forum with allow uploads on", dependsOnMethods={"addForumRatings"})
	public void addForumUploads() {

		String forumTitle = "Upload " + RANDOM_INT;

		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE,900, 100)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		forumAuthorPage.openAdvancedTab().setAllowUpload(true);

		forumAuthorPage.save();
		
		// Assert that uploads was set properly
		
		forumAuthorPage.reEdit();
				
		boolean isAllowUpload = forumAuthorPage.openAdvancedTab().isAllowUpload();

		
		Assert.assertTrue(isAllowUpload, "Option Allow Upload is not set");

		
		forumAuthorPage.cancel(flaHandler);
	}
	
	/**
	 * Add forum with Rich Text editor
	 */
	@Test(description="Add forum with Rich Text editor", dependsOnMethods={"addForumUploads"})
	public void addForumRichText() {

		String forumTitle = "RichText " + RANDOM_INT;

		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE,900,200)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		forumAuthorPage.openAdvancedTab().setAllowRichText(true);

		forumAuthorPage.save();

		
		// Assert rich text editor is set
		
		forumAuthorPage.reEdit();
				
		boolean isAllowRichText = forumAuthorPage.openAdvancedTab().isAllowRichEditor();
		
		Assert.assertTrue(isAllowRichText, "Option Allow rich text editor is not set");
		
		forumAuthorPage.cancel(flaHandler);
	}
	
	/**
	 * Add forum with limited character restrictions
	 */
	@Test(description="Add forum with limited character restrictions", dependsOnMethods={"addForumRichText"})
	public void addForumMinMaxCharacters() {

		String forumTitle = "MinMax " + RANDOM_INT;
		String minChar = "10";
		String maxChar = "100";
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE,700,200)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		// Set minimum characters
		forumAuthorPage.openAdvancedTab().setLimitedMinCharacters(true);
		forumAuthorPage.openAdvancedTab().setMinCharacters(minChar);
		
		// set maximum characters
		forumAuthorPage.openAdvancedTab().setLimitedMaxCharacters(true);
		forumAuthorPage.openAdvancedTab().setMaxCharacters(maxChar);

		forumAuthorPage.save();
		
		// Assert Min and max characters are set 
		
		forumAuthorPage.reEdit();
		
		// Assert min char limits
		boolean isMinLimit = forumAuthorPage.openAdvancedTab().isLimitedMinCharacters();
		String minCharacters = forumAuthorPage.openAdvancedTab().getMinCharacters();
		
		Assert.assertTrue(isMinLimit, "Option for minimum characters is not set");
		Assert.assertEquals(minCharacters, minChar, "Minimal characters are not set properly");
		
		
		// Assert max char limits
		boolean isMaxLimit = forumAuthorPage.openAdvancedTab().isLimitedMaxCharacters();
		String maxCharacters = forumAuthorPage.openAdvancedTab().getMaxCharacters();
		
		Assert.assertTrue(isMaxLimit, "Option for minimum characters is not set");
		Assert.assertEquals(maxCharacters, maxChar, "Minimal characters are not set properly");
		
		forumAuthorPage.cancel(flaHandler);
	}	
	
	
	/**
	 * Add forum with notifications on postings for learners and teachers and on mark release
	 */
	@Test(description="Add forum with notifications on postings for learners and teachers and on mark release", 
			dependsOnMethods={"addForumMinMaxCharacters"})
	public void addForumNotifications() {

		String forumTitle = "Notifications " + RANDOM_INT;
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE,500,200)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		// Set notifications
		forumAuthorPage.openAdvancedTab().setNotifyLearnersOnForumPosting(true);
		forumAuthorPage.openAdvancedTab().setNotifyTeachersOnForumPosting(true);
		forumAuthorPage.openAdvancedTab().setNotifyLearnersOnMarkRelease(true);

		forumAuthorPage.save();
		
		// Assert Notifications
		
		forumAuthorPage.reEdit();
		
		// Assert notifications
		boolean learnerOnPosting = forumAuthorPage.openAdvancedTab().isNotifyLearnersOnForumPosting();
		boolean teacherOnPosting = forumAuthorPage.openAdvancedTab().isNotifyTeachersOnForumPosting();
		boolean learneronMark    = forumAuthorPage.openAdvancedTab().isNotifyLearnersOnMarkRelease();
				
		Assert.assertTrue(learnerOnPosting, "Option for learners notification on posting is not set");
		Assert.assertTrue(teacherOnPosting, "Option for teachers notification on posting is not set");
		Assert.assertTrue(learneronMark, "Option for learners notification on mark release is not set");
		
		forumAuthorPage.cancel(flaHandler);
	}	
	
	
	
	/**
	 * Add forum with reflection
	 */
	@Test(description="Add forum with reflection", dependsOnMethods={"addForumNotifications"})
	public void addForumReflection() {

		String forumTitle = "Reflection " + RANDOM_INT;
		String reflectionTxt = "Reflect on...";
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE,300,200)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		// Set reflection
		forumAuthorPage.openAdvancedTab().setRelectOnActivity(true);
		forumAuthorPage.openAdvancedTab().setRelectionInstructions(reflectionTxt);

		forumAuthorPage.save();
		
		// Assert Reflection
		
		forumAuthorPage.reEdit();
		
		// Assert reflection txt
		boolean reflectionOption = forumAuthorPage.openAdvancedTab().isReflectOnActivity();
		String reflectionInstructions = forumAuthorPage.openAdvancedTab().getReflectionInstructions();
				
		Assert.assertTrue(reflectionOption, "Option for reflection is not set");
		Assert.assertEquals(reflectionInstructions, reflectionTxt, "Reflection instructions don't match");

		
		forumAuthorPage.cancel(flaHandler);
	}	
	
	
	/**
	 * Add forum with replies limitation
	 */
	@Test(description="Add forum with replies limitation",
			dependsOnMethods={"addForumReflection"})
	public void addForumLimitReplies() {

		String forumTitle = "LimReply " + RANDOM_INT;
		String minReply = "2";
		String maxReply = "5";
		
		// Drop activities in canvas
		fla.dragActivityToCanvasPosition(AuthorConstants.FORUM_TITLE,100,200)
		.changeActivityTitle("Forum", forumTitle);

		// Assert new forum activity title
		// Now get all the activity titles
		List<String> allActivityTitles = fla.getAllActivityNames();
		
		// Assert that all of them are in the design
		
		Assert.assertTrue(allActivityTitles.contains(forumTitle), 
				"The title " + forumTitle + " was not found as an activity in the design");
	
		forumAuthorPage = fla.openForumAuthoring(forumTitle);
		
		// Set limitation replies
		forumAuthorPage.openAdvancedTab().setLimitReplies(minReply, maxReply);

		forumAuthorPage.save();
		
		// Assert limit replies
		
		forumAuthorPage.reEdit();
		
		// Assert limit replies
		boolean limitReplies = forumAuthorPage.openAdvancedTab().isLimitReplies();
		String minimumReplies = forumAuthorPage.openAdvancedTab().getMinReply();
		String maximumReplies = forumAuthorPage.openAdvancedTab().getMaxReply();
				
		Assert.assertTrue(limitReplies, "Option for limit replies is not set");
		Assert.assertEquals(minimumReplies, minReply, "Minimum replies don't match");
		Assert.assertEquals(maximumReplies, maxReply, "Maximum replies don't match");
		
		forumAuthorPage.cancel(flaHandler);
		
		
	}	
	
	
	/**
	 * Save design
	 */
	@Test(description="saves forum design",
			dependsOnMethods={"addForumLimitReplies"})
	public void saveForumTestsDesigns() {

		String designName = ForumConstants.FORUM_TITLE + " tests " + RANDOM_INT;
		
		fla.drawTransitionBtwActivities();
		String result = fla.saveAsDesign(designName);
		
		// Assert save
		boolean saveResult = result.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG);
		Assert.assertTrue(saveResult, "The designed was not saved or is incomplete");
		
	}

	
	
	@AfterClass
	public void afterClass() {
		//driver.quit();
	}
	
}
