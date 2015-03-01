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

package org.lamsfoundation.lams.tool.forum;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.lamsfoundation.lams.author.util.AuthorConstants;
import org.lamsfoundation.lams.learner.util.LearnerConstants;
import org.lamsfoundation.lams.pages.IndexPage;
import org.lamsfoundation.lams.pages.LoginPage;
import org.lamsfoundation.lams.pages.author.FLAPage;
import org.lamsfoundation.lams.pages.learner.LearnerPage;
import org.lamsfoundation.lams.pages.monitor.addlesson.AddLessonPage;
import org.lamsfoundation.lams.pages.tool.forum.author.AdvancedTab;
import org.lamsfoundation.lams.pages.tool.forum.author.AuthorPage;
import org.lamsfoundation.lams.pages.tool.forum.author.BasicTab;
import org.lamsfoundation.lams.pages.tool.forum.learner.MessageForm;
import org.lamsfoundation.lams.pages.tool.forum.learner.ReflectionPage;
import org.lamsfoundation.lams.pages.tool.forum.learner.TopicIndexPage;
import org.lamsfoundation.lams.pages.tool.forum.learner.TopicViewPage;
import org.lamsfoundation.lams.pages.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.pages.util.LamsPageUtil;
import org.lamsfoundation.lams.util.LamsUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

/**
 * 
 * Forum Authoring tests
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

	private AddLessonPage addLesson;
	private LearnerPage learnerPage;
	private TopicIndexPage topicsPage;
	private TopicViewPage topicView;
	
	public String indexHandler;
	public String flaHandler;
	public String learnerHandler;
	
	WebDriver driver;
	
	// Tests specifics
	
	private static final String lessonName = ForumConstants.FORUM_TITLE + " tests " + RANDOM_INT;
	
	
	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		onLogin = PageFactory.initElements(driver, LoginPage.class);
		index = PageFactory.initElements(driver, IndexPage.class);
		fla = PageFactory.initElements(driver, FLAPage.class);
		forumAuthorPage = PageFactory.initElements(driver, AuthorPage.class);
		addLesson = PageFactory.initElements(driver, AddLessonPage.class);
		onLogin.navigateToLamsLogin().loginAs("test3", "test3");
		
	}

	
	/**
	 * Opens FLA interface
	 */
	@Test
	public void openFLA() {
		
		FLAPage fla = new FLAPage(driver);
		indexHandler = driver.getWindowHandle();
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
		String msgBody  = LamsUtil.randomParagraphCharLimit(1000) + RANDOM_INT;;
		
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
		

	}
	
	/**
	 * Add forum with Locked when finished
	 */
	@Test(description="Add forum with Locked when finished", dependsOnMethods={"deleteTopic"})
	public void verifyForumLockedWhenFinished() {
	
		// Assert locked when finished was saved
		
		forumAuthorPage.reEdit();
				
		boolean isLockedWhenFinshed = forumAuthorPage.openAdvancedTab().isLockWhenFinished();
		
		Assert.assertTrue(isLockedWhenFinshed, "Option locked when finished is not set");
		
		forumAuthorPage.cancel(flaHandler);
		
	}
	
	/**
	 * Add forum with no re-edits allowed
	 */
	@Test(description="Add forum with no re-edits allowed", dependsOnMethods={"verifyForumLockedWhenFinished"})
	public void addForumEditNotAllowed() {

		String forumTitle = "NoEdit " + RANDOM_INT;
		String instructionsHTML = "<b>No Edit instructions</b>";
		
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

		forumAuthorPage.openBasicTab().setTitle(forumTitle);
		
		forumAuthorPage.openBasicTab().setInstructions(instructionsHTML);
		
		forumAuthorPage.openAdvancedTab().setAllowEdit(false);
		
		forumAuthorPage.save();
		
		

	}
	
	/**
	 * Verify forum with no re-edits allowed.
	 */
	@Test(description="Verify forum with no re-edits allowed", dependsOnMethods={"addForumEditNotAllowed"})
	public void verifyForumEditNotAllowed() {
		// testing data
		String forumTitle = "NoEdit " + RANDOM_INT;
		String instructionsHTML = "<b>No Edit instructions</b>";
		
		// Open author again
		
		forumAuthorPage.reEdit();
		

		// Assert title and instructions
		
		String title = forumAuthorPage.openBasicTab().getTitle();
		// We use boolean to compare as CkEditor includes some <div> tags. 
		boolean isInstructions = forumAuthorPage.openBasicTab().getIntructions().contains(instructionsHTML);
		
		Assert.assertEquals(title, forumTitle, "Title isn't the same!");
		Assert.assertTrue(isInstructions, "Instructions don't match!");
		

		// Assert edits are not allowed
		
		boolean isAllowEdit = forumAuthorPage.openAdvancedTab().isAllowEdit();
		
		Assert.assertFalse(isAllowEdit, "Option Allow Edit is still set");

		
		forumAuthorPage.cancel(flaHandler);
	
	}
	
	/**
	 * Add forum with ratings
	 */
	@Test(description="Add forum with ratings", dependsOnMethods={"verifyForumEditNotAllowed"})
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
		forumAuthorPage.reEdit();
		

	}
	
	/**
	 * Verify forum with ratings
	 */
	@Test(description="Verify forum with ratings", dependsOnMethods={"addForumRatings"})
	public void verifyForumRatings() {
		
		// testing data
		String minimumRate = "1";
		String maximumRate = "5"; 
				
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
	@Test(description="Add forum with allow uploads on", dependsOnMethods={"verifyForumRatings"})
	public void addForumUploads() {

		String forumTitle = "Upload " + RANDOM_INT;

		// Drop activities in canvas
		driver.switchTo().window(flaHandler);
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
		forumAuthorPage.reEdit();

	}
	
	/**
	 * Verify forum with allow uploads on
	 */
	@Test(description="Verify forum with allow uploads on", dependsOnMethods={"addForumUploads"})
	public void verifyForumUploads() {
		
		// Assert that uploads was set properly
				
		boolean isAllowUpload = forumAuthorPage.openAdvancedTab().isAllowUpload();

		
		Assert.assertTrue(isAllowUpload, "Option Allow Upload is not set");

		
		forumAuthorPage.cancel(flaHandler);
	}
	
	/**
	 * Add forum with Rich Text editor
	 */
	@Test(description="Add forum with Rich Text editor", dependsOnMethods={"verifyForumUploads"})
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

	}
	
	/**
	 * Verify forum with Rich Text editor
	 */
	@Test(description="Verify forum with Rich Text editor", dependsOnMethods={"addForumRichText"})
	public void verifyForumRichText() {
		
		// Assert rich text editor is set
		
		forumAuthorPage.reEdit();
				
		boolean isAllowRichText = forumAuthorPage.openAdvancedTab().isAllowRichEditor();
		
		Assert.assertTrue(isAllowRichText, "Option Allow rich text editor is not set");
		
		forumAuthorPage.cancel(flaHandler);
		
	}
	
	
	/**
	 * Add forum with limited character restrictions
	 */
	@Test(description="Add forum with limited character restrictions", dependsOnMethods={"verifyForumRichText"})
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
	}	
	
	/**
	 * Verify forum with limited character restrictions
	 */
	@Test(description="Verify forum with limited character restrictions", dependsOnMethods={"addForumMinMaxCharacters"})
	public void verifyForumMinMaxCharacters() {
		
		// testing data
		String minChar = "10";
		String maxChar = "100";
		
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
			dependsOnMethods={"verifyForumMinMaxCharacters"})
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
		
	}	
	
	/**
	 * Verify forum with notifications on postings for learners and teachers and on mark release
	 */
	@Test(description="Verify forum with notifications on postings for learners and teachers and on mark release", 
			dependsOnMethods={"addForumNotifications"})
	public void verifyForumNotifications() {
		

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
	@Test(description="Add forum with reflection", dependsOnMethods={"verifyForumNotifications"})
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
		

	}	
	
	
	/**
	 * Verify forum with reflection
	 */
	@Test(description="Verify forum with reflection", dependsOnMethods={"verifyForumNotifications"})
	public void verifyForumReflection() {
		
		// test data
		String reflectionTxt = "Reflect on...";
		
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
			dependsOnMethods={"verifyForumReflection"})
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
		

		
		
	}	
	
	/**
	 * Verify forum with replies limitation
	 */
	@Test(description="Verify forum with replies limitation",
			dependsOnMethods={"verifyForumReflection"})
	public void verifyForumLimitReplies() {
		
		// test data
		String minReply = "2";
		String maxReply = "5";
		
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
			dependsOnMethods={"verifyForumLimitReplies"})
	public void saveForumTestsDesigns() {

		String designName = ForumConstants.FORUM_TITLE + " tests " + RANDOM_INT;
		
		fla.drawTransitionBtwActivities();
		String result = fla.saveAsDesign(designName);
		
		// Assert save
		boolean saveResult = result.contains(AuthorConstants.SAVE_SEQUENCE_SUCCESS_MSG);
		Assert.assertTrue(saveResult, "The designed was not saved or is incomplete");
		
		fla.closeWindow();
		fla.close(indexHandler);
		
	}

	
	/**
	 * Create lesson based on forum design
	 */
	@Test(description="Create lesson based on forum design",
			dependsOnMethods={"saveForumTestsDesigns"})
	public void createForumLesson() {

		String designName = ForumConstants.FORUM_TITLE + " tests " + RANDOM_INT;
		
		final String addButton= "Add now";
		
		AddLessonPage addLesson = new AddLessonPage(driver);
		addLesson.maximizeWindows();
		addLesson = openDialog(index);
		
		String assertFrameOpen = addLesson.checkAddLessonButton();
		
		Assert.assertEquals(assertFrameOpen, addButton, 
				"It doesn't seem that the add lesson UI has loaded properly");

		// Get user's learning designs
		
		addLesson.createLessonByDesignName(designName);
	
		// Assert that lesson was created
		boolean wasLessonCreated = index.isLessonPresent(designName);
		
		Assert.assertTrue(wasLessonCreated, "Lesson " + designName + " was not found!");
		
	}

	/**
	 * Jump into lesson as learner and verify number of activities
	 */
	@Test(description="Jump into lesson as learner and verify number of activities",
			dependsOnMethods={"createForumLesson"})
	public void jumpAsLearner() {

		int numberOfActivities = 10;
		
		learnerPage = index.openLessonAsLearner(lessonName);
		
		learnerHandler = driver.getWindowHandle();
		
		List<String> svg = learnerPage.openControlFrame().getActivitiesNamesFromSVG();
		
		// Assert activities in lesson (there should be ten)
		Assert.assertEquals(svg.size(), numberOfActivities, "There number of activities in lesson is incorrect!");
		
	}
	
	/**
	 * Jump into lesson as learner and verify basics
	 */
	@Test(description="Jump into lesson as learner",
			dependsOnMethods={"jumpAsLearner"})
	public void verifyLearnerUI() {

		// Assert learner window name
		
		String windowName = index.getWindowTitle();

		Assert.assertEquals(windowName, LearnerConstants.LEARNER_TITLE, "It doesn't seem that this UI is the LAMS learner interface");
		

	}
	
	/**
	 * Jump into lesson as learner and verify basics
	 */
	@Test(description="Jump into lesson as learner",
			dependsOnMethods={"verifyLearnerUI"})
	public void verifyLessonName() {
		
		// Assert lesson name
		//TODO Ensure that we add this when this is fixed. 
		// String actualLessonName = learnerPage.openControlFrame().getLessonName();
		//System.out.println(actualLessonName);
		// Assert.assertEquals(actualLessonName, lessonName, "Lesson name is not as expected");
		
		learnerPage.openControlFrame().closeLearnerPage(indexHandler);
		
	}
	
	/**
	 * Tests basic forum: Verify number of replies and new message on default subject
	 */
	@Test(description="Verify number of replies and new message on default subject",
			dependsOnMethods={"verifyLessonName"})
	public void verifyNumberOfReplies() {
		
		String title = "Topic# " + RANDOM_INT;
		
		learnerPage = index.openLessonAsLearner(lessonName);
		
		learnerHandler = driver.getWindowHandle();
		
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		
		// Assert replies
		String numberOfReplies = topicsPage.getNumberOfRepliesBySubject(title);
		
		Assert.assertEquals(numberOfReplies, "0", "There should be only zero replies yet");
		
		// Assert new messages
		String numberOfNewMsgs = topicsPage.getNumberOfNewMsgsBySubject(title);
		
		Assert.assertEquals(numberOfNewMsgs, "1", "There should be only 1 message!");

		
	}
	
	
	/**
	 * Tests basic forum: Verify subject name and number of topics
	 */
	@Test(description="Verify subject name and number of topics",
			dependsOnMethods={"verifyNumberOfReplies"})
	public void verifyNumberOfTopicsAndSubjectName() {

		int numberOfTopics = 1;
		String title = "Topic# " + RANDOM_INT;
		
		// get the topic list
		List<WebElement> topics = topicsPage.getTopics();
		
		// get the text for the first topic subject 
		String topicSubject =topics.get(0).getText();
		
		
		// Assert number of topics
		Assert.assertEquals(topics.size(), numberOfTopics, "We were expecting only 1 topic!");
		
		// Assert subject name
		Assert.assertEquals(topicSubject, title, "The message subjets don't match!");

		
	}
	
	/**
	 * Tests basic forum: Verify number of messages
	 */
	@Test(description="Verify number of messages",
			dependsOnMethods={"verifyNumberOfTopicsAndSubjectName"})
	public void verifyNumberOfMessages() {

		int numberOfMessages = 1;
		String title = "Topic# " + RANDOM_INT;
		
		// Now open topic and verify text
	
		topicView = topicsPage.openTopicBySubject(title);
		
		List<WebElement> messages = topicView.getAllMessages();
		
		int messagesSize = messages.size();
		
		// Assert messages size 
		Assert.assertEquals(messagesSize, numberOfMessages, "The number of messages don't match!");
		
		
	}
	
	/**
	 * Tests basic forum: Verify number of messages
	 */
	@Test(description="Verify number of messages",
			dependsOnMethods={"verifyNumberOfMessages"})
	public void verifyDefaultMessageDetails() {
		
		String topicSubject = "Topic# " + RANDOM_INT;
		String author = "Three Test";
		
		List<WebElement> messages = topicView.getAllMessages();
		
		// Get msg subject
		
		String msgSubject = topicView.getMsgSubject(messages.get(0));
		
		// Assert subject
		
		Assert.assertEquals(msgSubject, topicSubject, "Message subjects don't match!");
		
		
		// Get msg author
		
		String msgAuthor = topicView.getMsgAuthor(messages.get(0));
		
		// Assert author
		
		Assert.assertEquals(msgAuthor, author, "Author is not the same!");
		
	
		// Get msg body
		
		String msgBody = topicView.getMsgBody(messages.get(0));
		
		// Assert that msg body contains random int
		
		boolean containsRandomInt = msgBody.contains(RANDOM_INT);
		
		Assert.assertTrue(containsRandomInt, "Message doesn't match!");
		
	}
	
	
	/**
	 * Create new topic and verify that it shows accordingly
	 */
	@Test(description="Create new topic",
			dependsOnMethods={"verifyDefaultMessageDetails"})
	public void createNewTopic() {

		String newTopic = "Shijo topic# " + RANDOM_INT;
		String newBody  = LamsUtil.randomParagraphCharLimit(1500);
		topicView.goBackToTopics();
		
		topicsPage.createTopic()
			.createTopicMessage(newTopic, newBody);
		
		int topicCount = topicsPage.getTopics().size();

		boolean topicExists = topicsPage.topicExistBySubject(newTopic);
		
		// Assert topic count
		Assert.assertEquals(topicCount, 2, "Expected two topics!");
		
		// Assert if topic exists
		Assert.assertTrue(topicExists, "The new topic was not created!");
		
	}
	
	/**
	 * Reply to the topic we just created and verify it.
	 */
	@Test(description="Reply to the topic we just created and verify it",
			dependsOnMethods={"createNewTopic"})
	public void replyToMessage() {
		
		String title = "Shijo topic# " + RANDOM_INT;
		String newSubject = "Reply# " + RANDOM_INT;
		String newBody = LamsUtil.randomParagraphCharLimit(2000);
		
		topicView = topicsPage.openTopicBySubject(title);
		
		List<WebElement> messages = topicView.getAllMessages();
		topicView.clickReply(messages.get(0))
			.createTopicMessage(newSubject, newBody);
		
		// Assert that the reply has worked 
		
		messages = topicView.getAllMessages();
		
		// Assert that there's two messages now
		
		int numberOfMessages = messages.size();
		
		Assert.assertEquals(numberOfMessages, 2, "We expected two messages now!");
		
		// Assert that message 2 has the correct title and body
		
		String newMsgTitle = topicView.getMsgSubject(messages.get(1));
		String newMsgBody  = topicView.getMsgBody(messages.get(1));
		
		Assert.assertEquals(newMsgTitle, newSubject, "Message subjects don't match!");
		Assert.assertEquals(newMsgBody, newBody, "Body messages don't match");
		
		topicsPage = topicView.goBackToTopics();
				
		String numberOfReplies = topicsPage.getNumberOfRepliesBySubject(title);
		
		// Assert number of Replies
		Assert.assertEquals(numberOfReplies, "1", "It should have 1 reply!");
		
	}
	
	/**
	 * Reply with 5000 max char.
	 */
	@Test(description="Reply to a message",
			dependsOnMethods={"replyToMessage"})
	public void maxCharPost() {

		String title = "Topic# " + RANDOM_INT;
		
		String txt = LamsUtil.randomText(25);
		txt = txt.substring(0, 4999) + ".";
		
		topicsPage.openTopicBySubject(title);
		
		List<WebElement> messages = topicView.getAllMessages();
		topicView.clickReply(messages.get(0)).createTopicMessage(txt);
		
		// Assert that the reply has worked 
		
		messages = topicView.getAllMessages();
		
		// Assert that there's two messages now
		
		int numberOfMessages = messages.size();
		
		Assert.assertEquals(numberOfMessages, 2, "We expected two messages now!");
		
		// Assert that message 2 has the correct title and body
		
		String newMsgBody  = topicView.getMsgBody(messages.get(1));
		
		int txtLength = txt.length();
		int newMsgBodyLength = newMsgBody.length();
		
		Assert.assertEquals(newMsgBodyLength, txtLength, "Number of chars don't match");

		
	}
	
	
	/**
	 * Edit existing message
	 */
	@Test(description="Edit existing message",
			dependsOnMethods={"maxCharPost"})
	public void editMessage() {
		
		String msgTitle = "New edit reply...";
		String msgBody  = LamsUtil.randomText(6);
				
		
		List<WebElement> messages  = topicView.getAllMessages();
		
		topicView.clickEdit(messages.get(1)).editTopicMessage(msgTitle, msgBody);
		
		messages = topicView.getAllMessages();
		
		// Assert that there's two messages now
		
		int numberOfMessages = messages.size();
		
		Assert.assertEquals(numberOfMessages, 2, "We expected two messages now!");
		
		// Assert that message 2 has the correct title and body
		
		String newMsgBody  = topicView.getMsgBody(messages.get(1));
		
		Assert.assertEquals(newMsgBody, newMsgBody, "Number of chars don't match");

		topicsPage = topicView.goBackToTopics();
		
		topicsPage.nextActivity();
	}
	
	/**
	 * Verify Locked when finished (LWF): verify initial info message
	 * 
	 */
	@Test(description="Locked when finished (LWF): verify initial info message",
			dependsOnMethods={"editMessage"})
	public void verifyLwfInfoMessage() {
				
		String initialInfoWarning =  "you won't be able to continue posting";
		
		String warning = topicsPage.getInfoWarning();
		
		// Assert that when entering a locked when finished forum
		// the warning is displayed
		
		boolean isLockedWarning = warning.contains(initialInfoWarning);
		
		Assert.assertTrue(isLockedWarning, "No warning is displayed");

	}
	
	/**
	 * Verify Locked when finished (LWF): create a topic and verify it was added
	 * 
	 */
	@Test(description="LWF: create a topic and verify it was added",
			dependsOnMethods={"verifyLwfInfoMessage"})
	public void verifyLwfCreateTopic() {
		
		String subject = "LWF " + RANDOM_INT;
		String body  = LamsUtil.randomText(6);
		
		topicsPage.createTopic().createTopicMessage(subject, body);
		
		// Assert that the topic was added
		boolean topicExists = topicsPage.topicExistBySubject(subject);
		Assert.assertTrue(topicExists, "The topic was not added!");
		
		topicsPage.nextActivity();
		
	}
	
	/**
	 * Verify Locked when finished (LWF): verify activity is completed
	 * 
	 */
	@Test(description="LWF: verify activity is completed",
			dependsOnMethods={"verifyLwfCreateTopic"})
	public void verifyLwfCompletion() {
		
		// this is just to wait for the next activity to load properly
		topicsPage.getForumTitle();
		
		// Close learner page 
		learnerPage = topicsPage.returnLearnerHandler(learnerHandler);
		learnerPage.openControlFrame().closeLearnerPage(indexHandler);

		// Reopen learner
		learnerPage = index.openLessonAsLearner(lessonName);
		learnerHandler = driver.getWindowHandle();
		
		// Assert now two completed activities
		int completedActivities = 2;
		int numberOfCompletedActivities = learnerPage.openControlFrame().getCompletedActivities().size();
		
		Assert.assertEquals(numberOfCompletedActivities, completedActivities, "The number of completed activities isn't correct");

		
	}
	
	/**
	 * Verify Locked when finished (LWF): verify that a learners cannot post when 
	 * revisiting the activity
	 * 
	 */
	@Test(description="LWF: verify that a learners cannot post when revisiting the activity",
			dependsOnMethods={"verifyLwfCompletion"})
	public void verifyLwfReEntry() {
		
		String subject = "LWF " + RANDOM_INT;
		String onComeBackInfoWarning = "As you are returning to this Forum again";

		topicsPage = learnerPage.openControlFrame().openPreviouslyCompletedActivity();
		
		// Assert if the warning message is display when returning to the activity
		String warning = topicsPage.getInfoWarning();
		
		boolean isLockedWarning = warning.contains(onComeBackInfoWarning);
		
		Assert.assertTrue(isLockedWarning, "The warning message is not being displayed");
				
		// Assert whether if new topics can be added
		boolean isNewTopicEnabled = topicsPage.isNewTopicEnabled(); 
		
		Assert.assertFalse(isNewTopicEnabled, "New topics should not be allowed!");
		
		topicView = topicsPage.openTopicBySubject(subject);
		
		List<WebElement> allMessages = topicView.getAllMessages();
				
		// Assert if the reply and edit button exist
		boolean isReplyEnabled  = topicView.isReplyEnabled(allMessages.get(0));
		boolean isEditEnabled = topicView.isEditEnabled(allMessages.get(0));
		
		Assert.assertFalse(isReplyEnabled, "The reply button is showing!");
		Assert.assertFalse(isEditEnabled,  "The edit button is showing!");
		
		// Go back to topics
		topicView.goBackToTopics();
		
		// Close the pop-up window
		topicsPage.nextActivity(learnerHandler);
		
	}
	

	/**
	 * Checks that no edits activity has exact authored values.
	 * 
	 */
	@Test(description="Checked that after submit a postings cannot be re-edited",
			dependsOnMethods={"verifyLwfReEntry"})
	public void verifyNoEditsDefaults() {
		
		String forumTitle = "NoEdit " + RANDOM_INT;
		String instructionsHTML = "<b>No Edit instructions</b>";
		

		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		
		// Assert title and instructions
		String title = topicsPage.getForumTitle();
		String instructions = topicsPage.getForumInstructions();
		
		boolean isInstructions = instructionsHTML.contains(instructions);
		
		Assert.assertEquals(title, forumTitle, "Titles don't match!");
		Assert.assertTrue(isInstructions, "Instructions don't match");
		

	}


	/**
	 * Checks that after submit a postings cannot be re-edited
	 * 
	 */
	@Test(description="Checks that after submit a postings cannot be re-edited",
			dependsOnMethods={"verifyNoEditsDefaults"})
	public void verifyNoEdits() {
		
		String replyBody = LamsUtil.randomText(2);

		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		topicView.replyFirstMessage(replyBody);
		
		List<WebElement> allMessages = topicView.getAllMessages();
		
		boolean isEditEnabled  = topicView.isEditEnabled(allMessages.get(1));
		
		Assert.assertFalse(isEditEnabled, "Edit is enabled when it shouldn't!");
		
		topicsPage = topicView.goBackToTopics();;
		topicsPage.nextActivity(learnerHandler);
		
	}
	
	
	/**
	 * Ratings: 
	 * 
	 * Checks that specific info warnings are displayed to tell learners of the ratings
	 * requirements
	 * 
	 */
	 
	@Test(description="Checks that specific info warnings are displayed to tell learners of the ratings",
			dependsOnMethods={"verifyNoEdits"})
	public void verifyRatingsInfoWarnings() {
		
		
		String warningLimitTxt = "Rating limitation: minimum 1 and maximum 5";

		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		
		String warning = topicsPage.getInfoWarning();
		
		// Assert rating limitation warning 
		boolean isWarningCorrect = warning.contains(warningLimitTxt);
		Assert.assertTrue(isWarningCorrect, "No rating limitation warning is displayed!");
		
		// Assert that nextActivity button is not present
		boolean isNextActivityButtonPresent = topicsPage.isNextActivityButtonPresent();
		Assert.assertFalse(isNextActivityButtonPresent, "The next activity button should not be present!");
		
	}
	
	/**
	 * Ratings: 
	 * 
	 * Posts 5 messages as original user so they can be rated by next user.
	 * 
	 */
	 
	@Test(description="Posts 5 messages as original user so they can be rated by next user.",
			dependsOnMethods={"verifyRatingsInfoWarnings"})
	public void verifyRatingsPostMessages1() {
		
		topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		for (int i = 0; i < 5; i++) {
			topicView.replyFirstMessage(LamsUtil.randomText(i + 1));
		}
		
		// Now assert number of messages
		List<WebElement> allMessages = topicView.getAllMessages();
		
		Assert.assertEquals(allMessages.size(), 6, "There should be 6 messages at this stage!");
		
		topicView.goBackToTopics();
		
		// Assert number of replies in the forum page
		String replies = topicsPage.getNumberOfRepliesBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		Assert.assertEquals(replies, "5", "There should be 5 replies!");
		
	}
	
	/**
	 * Ratings: 
	 * 
	 * Posts 5 messages as the other user 
	 * 
	 */
	 
	@Test(description="Posts 5 messages as the other user",
			dependsOnMethods={"verifyRatingsPostMessages1"})
	public void verifyRatingsPostMessages2() {
				
		String warningLimitTxt = "Rating limitation: minimum 1 and maximum 5";
		
		// Now logout and login as a new user
		topicsPage.closeWindow();
		driver.switchTo().window(indexHandler);
		index.logOut();
		onLogin.navigateToLamsLogin().loginAs("test2", "test2");
		
		learnerPage = index.openLessonAsLearner(lessonName);
		learnerHandler = driver.getWindowHandle();
		
		topicsPage = learnerPage.openContentFrame().openLearnerForum();

		// Skip to the rating activity
		topicsPage.nextActivity();
		topicsPage.nextActivity();
		topicsPage.nextActivity();
		
		String warning = topicsPage.getInfoWarning();
		
		// Assert rating limitation warning 
		boolean isWarningCorrect = warning.contains(warningLimitTxt);
		Assert.assertTrue(isWarningCorrect, "No rating limitation warning is displayed!");		
		
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		// Post 5 messages
		for (int i = 0; i < 5; i++) {
			topicView.replyFirstMessage(LamsUtil.randomText(i+1));
		}

		List<WebElement> allMessages = topicView.getAllMessages();
		
		// Now rate all the messages we can rate
		for (WebElement message : allMessages) {
			int random = Integer.parseInt(LamsUtil.randInt(0, 5));
			topicView.rateMessage(message, random);

		}
				
		topicView.goBackToTopics();
		
		// Assert that nextActivity button is present
		boolean isNextActivityButtonPresent = topicsPage.isNextActivityButtonPresent();
		Assert.assertTrue(isNextActivityButtonPresent, "The next activity button should be present!");

		
		// Assert number of rated messages
		warning = topicsPage.getInfoWarning();
		boolean isWarning = warning.contains("You have rated 5");
		Assert.assertTrue(isWarning, "It doesn't seem that we have rated 5 messages!");
		

	}
	
	/**
	 * Ratings: 
	 * 
	 * Rate posting from other user as original user.
	 * 
	 */
	 
	@Test(description="Rate posting from other user as original user.",
			dependsOnMethods={"verifyRatingsPostMessages1"})
	public void verifyRatingsRateAsOriginalUser() {
		
		String warningLimitTxt = "Rating limitation: minimum 1 and maximum 5";
		
		topicsPage.closeWindow();
		
		// Switch to our original user
		driver.switchTo().window(indexHandler);
		index.logOut();
		onLogin.navigateToLamsLogin().loginAs("test3", "test3");
		
		learnerPage = index.openLessonAsLearner(lessonName);
		learnerHandler = driver.getWindowHandle();
		
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		
		String warning = topicsPage.getInfoWarning();
		
		// Assert rating limitation warning 
		boolean isWarningCorrect = warning.contains(warningLimitTxt);
		Assert.assertTrue(isWarningCorrect, "No rating limitation warning is displayed!");	
		
		// Assert that nextActivity button is not present
		boolean isNextActivityButtonPresent = topicsPage.isNextActivityButtonPresent();
		Assert.assertFalse(isNextActivityButtonPresent, "The next activity button should not be present!");
		
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		List<WebElement> allMessages = topicView.getAllMessages();
		
		for (WebElement message : allMessages) {
			
			int random = Integer.parseInt(LamsUtil.randInt(0, 5));
			topicView.rateMessage(message, random);
			
		}
		
		topicView.goBackToTopics();
		
		
		// Assert that nextActivity button is present
		isNextActivityButtonPresent = topicsPage.isNextActivityButtonPresent();
		Assert.assertTrue(isNextActivityButtonPresent, "The next activity button should be present!");
		
		// Assert number of rated messages
		warning = topicsPage.getInfoWarning();
		boolean isWarning = warning.contains("You have rated 5");
		Assert.assertTrue(isWarning, "It doesn't seem that we have rated 5 messages!");
		
		topicsPage.nextActivity();
		
	}
	
	/**
	 * Verify attachments
	 * 
	 * Verifies that attachments can be added
	 * 
	 */
	 
	@Test(description="Verifies that attachments can be added to postings",
			dependsOnMethods={"verifyRatingsRateAsOriginalUser"})
	public void verifyUploads() {
		
		String filename = "Example1.zip";
		
		String replyBody = LamsUtil.randomText(8);
		
		
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		List<WebElement> allMessages = topicView.getAllMessages();
		
		topicView.replyToMessageWithAttachment(allMessages.get(0), replyBody, filename);
		
		allMessages = topicView.getAllMessages();
		
		// Assert that there are two messages now
		
		int messagesSize = allMessages.size();
		Assert.assertEquals(messagesSize, 2, "There should be two messages!");
		
		// Verify that the attachment file was uploaded properly 
		
		String uploadedFilename = topicView.getMsgAttachmentFilename(allMessages.get(1));
		
		//Assert file name
		Assert.assertEquals(uploadedFilename, filename, "The file name is different");
		
		
	}
	
	/**
	 * Delete attached file
	 * 
	 *  We add a new posting with an attachment and then delete the attachment
	 * 
	 */
	 
	@Test(description="Test delete attachement feature",
			dependsOnMethods={"verifyUploads"})
	public void verifyAttachmentDeletion() {
	
		String replyBody = LamsUtil.randomText(2);
		String filename = "Example1.zip";
		
		List<WebElement> allMessages = topicView.getAllMessages();
		
		// This is for our previous addition
		WebElement message = allMessages.get(1);
		
		topicView.replyToMessageWithAttachment(message, replyBody, filename);
		
		// refresh
		allMessages = topicView.getAllMessages();
		
		// Now we should have three messages
		Assert.assertEquals(allMessages.size(), 3, "We should have three messages now!");
		
		// Get the latest posting we've just did
		message = allMessages.get(2);
		
		// Remove its attachment
		topicView.clickEdit(message).removeAttachment();
		
		// Now get it again:
		allMessages = topicView.getAllMessages();
		
		// Assert that there's three messages
		
		Assert.assertEquals(allMessages.size(), 3, "We should have three messages now!");
		
		// Now get only the latest message
		message = allMessages.get(2);
		// get the innerHTML for this node
		message.getAttribute("innerHTML");
		
		boolean isAttachementPresent = topicView.isAttachementPresent(message);
		
		// Assert that the file has been removed
		Assert.assertFalse(isAttachementPresent, "Attachment is still showing!");
		
		topicView.goBackToTopics();
		topicsPage.nextActivity(learnerHandler);
	}
	
	/**
	 * Verify CKEditor option for learners
	 * 
	 *  We test that we can reply to an existing message and create a new topic using 
	 *  the CkEditor
	 * 
	 */
	 
	@Test(description="Test CkEditor in learner",
			dependsOnMethods={"verifyAttachmentDeletion"})
	public void verifyRichTextEditor() {
		
		// Prepare testing data
		String replyBody1 = LamsUtil.randomText(1);
		String subject2 = StringUtils.capitalize(LamsUtil.randomString(15).toLowerCase());
		String replyBody2 = LamsUtil.randomParagraph(500);
		
		// Open topic
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		// Get all messages
		List<WebElement> allMessages = topicView.getAllMessages();
		
		// reply to the first message
		WebElement message = allMessages.get(0);
		topicView.replyToMessageCkEditor(message, replyBody1);
		
		// Assert text return
		allMessages = topicView.getAllMessages();
		message = allMessages.get(1);
		String returnedTxt = topicView.getMsgBody(message);
		Assert.assertEquals(replyBody1, returnedTxt, "Body text is not the same!");

		topicView.goBackToTopics();
		
		// Create a topic using CkEditor for body
		topicsPage.createTopic().createTopicMessageCkEditor(subject2, replyBody2);
		
		// Now open the new topic
		topicView = topicsPage.openTopicBySubject(subject2);
		allMessages = topicView.getAllMessages();
		
		//Assert that the body text matches
		String bodyMsg = topicView.getMsgBody(allMessages.get(0));
		Assert.assertEquals(replyBody2, bodyMsg);
		
		topicView.goBackToTopics();
		topicsPage.nextActivity(learnerHandler);
		
		
	}
	
	/**
	 * Verify minimum and max on reply
	 * 
	 *  Verify that min and max are enforced on reply topic
	 * 
	 */
	 
	@Test(description="Test CkEditor in learner",
			dependsOnMethods={"verifyRichTextEditor"})
	public void verifyReplyMinMaxChars() {
		
		// Prepare testing data
		String replyFiveCharacters = LamsUtil.randomString(5);
		String replyFinal = LamsUtil.randomParagraphCharLimit(100);
		String minChar = "10";
		String maxChar = "100";
				
		// Open topic
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		// Get all messages
		List<WebElement> allMessages = topicView.getAllMessages();
		MessageForm replyForm = topicView.clickReply(allMessages.get(0));
		
		String infoWarning = replyForm.getInfo();
		
		// Assert info warning
		boolean isInWarning = infoWarning.contains(ForumConstants.FORUM_FORM_INFO_MIN_CHAR);
		Assert.assertTrue(isInWarning, "Warning message for min char posting is incorrect!");
		
		String charLeft = replyForm.getCharactersLeft();
		String charRequired = replyForm.getCharacterRequired();
		
		// Assert character limitations
		Assert.assertEquals(charLeft, maxChar, "Minimum characters is incorrect!");
		Assert.assertEquals(charRequired, minChar, "Maximum characters is incorrect!");
		replyForm.createTopicMessage(replyFiveCharacters);
		String result = LamsPageUtil.getAlertText(driver);
		
		// Assert result popup 
		boolean isError = result.contains(ForumConstants.FORUM_LEARNER_MIN_CHAR_WARNING);
		Assert.assertTrue(isError, "No error message displayed");
		
		// Reply proper posting
		replyForm.createTopicMessage(replyFinal);
		
		// Assert that message is only 100 characters 
		allMessages = topicView.getAllMessages();
		
		int msgBody = topicView.getMsgBody(allMessages.get(1)).length();
		
		Assert.assertEquals(Integer.toString(msgBody), maxChar, "The message body length is not correct");
		
		topicView.goBackToTopics();
		
	}
	
	
	/**
	 * Verify minimum and max on create new topic
	 * 
	 *  Verify that min and max are enforced on new create topic
	 *  
	 */
	 
	@Test(description="Verify that min and max are enforced on new create topic",
			dependsOnMethods={"verifyReplyMinMaxChars"})
	public void verifyCreateMinMaxChars() {
		
		String subject = StringUtils.capitalize(LamsUtil.randomString(15).toLowerCase());
		String replyFiveCharacters = LamsUtil.randomString(5);
		String replyFinal = LamsUtil.randomParagraphCharLimit(100);
		String maxChar = "100";

		MessageForm newForm = topicsPage.createTopic();
		
		newForm.createTopicMessage(subject, replyFiveCharacters);
		String result = LamsPageUtil.getAlertText(driver);
		
		// Assert result popup 
		boolean isError = result.contains(ForumConstants.FORUM_LEARNER_MIN_CHAR_WARNING);
		Assert.assertTrue(isError, "No error message displayed");
		
		newForm.createTopicMessage(replyFinal);
		
		topicView = topicsPage.openTopicBySubject(subject);
		
		// Assert that message is only 100 characters 
		List<WebElement> allMessages = topicView.getAllMessages();
		
		int msgBody = topicView.getMsgBody(allMessages.get(0)).length();
		
		Assert.assertEquals(Integer.toString(msgBody), maxChar, "The message body length is not correct");
	
		topicView.goBackToTopics();
		topicsPage.nextActivity(learnerHandler);

	}

	
	/**
	 * Verify notifications
	 * 
	 *  As we have no way to test email notifications we'll skip this test
	 *  
	 */
	 
	@Test(description="Verify email notifications on learner events",
			dependsOnMethods={"verifyCreateMinMaxChars"})
	public void verifyNotificationsOnLearnerEvents() {
		
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		topicsPage.nextActivity(learnerHandler);
	}
	
	/**
	 * Verify reflections
	 * 
	 *  On activity completion, verify that reflection is presented
	 *  
	 */
	 
	@Test(description="On activity completion, verify that reflection is presented", 
			dependsOnMethods={"verifyNotificationsOnLearnerEvents"})
	public void verifyReflection() {
		
		// Prepare testing data
		String replyBody = LamsUtil.randomParagraph(15);
		String reflectionTxt = LamsUtil.randomText(3);
		String reflectionTxt2 = LamsUtil.randomText(2);
				
		// Open topic
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		// Add a reply
		topicView.replyFirstMessage(replyBody);
		
		// Go back and continue to enter reflection
		topicView.goBackToTopics();
		ReflectionPage reflection = topicsPage.clickContinue();
		
		// Post reflection and go to next activity
		reflection.postReflection(reflectionTxt);
		reflection.nextActivity(learnerHandler);
	
		// Heads to the next activity and gets the Forum instructions
		// this gives us just time as for some reason selenium is too fast 
		// for Firefox :-)
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		topicsPage.getForumInstructions();
		
		// Close learner page 
		learnerPage = topicsPage.returnLearnerHandler(learnerHandler);
		learnerPage.openControlFrame().closeLearnerPage(indexHandler);

		// Reopen learner
		learnerPage = index.openLessonAsLearner(lessonName);
		learnerHandler = driver.getWindowHandle();
		
		
		topicsPage = learnerPage.openControlFrame().openPreviouslyCompletedActivity();
		
		String verifyReflectionTxt = topicsPage.getReflection();
		
		// Assert submitted reflection
		
		Assert.assertEquals(verifyReflectionTxt, reflectionTxt, "Reflections don't match!");
		
		reflection = topicsPage.editReflection();
		
		reflection.postReflection(reflectionTxt2);
		reflection.nextActivity(learnerHandler);
		
	}
	
	/**
	 * Verify replies limits
	 * 
	 *  Verify that we need a minimal of two replies to continue
	 *  
	 */
	 
	@Test(description="Verify that we need a minimal of two replies to continue",
			dependsOnMethods={"verifyNotificationsOnLearnerEvents"})
	public void verifyRepliesLimits() {
		
		String infoMessage = "Posting limitations for this forum";
		String warningMinMessages = "You must contribute at least 2 posts in each topic before finish.";
		
		String body1 = LamsUtil.randomText(5);
	
		learnerPage = topicsPage.returnLearnerHandler(learnerHandler);
		topicsPage = learnerPage.openContentFrame().openLearnerForum();
		
		// Assert info message
		String infoMsg = topicsPage.getInfoWarning();
		boolean isInfoCorrect = infoMsg.contains(infoMessage);
		Assert.assertTrue(isInfoCorrect, "No info message displayed");
		
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		topicView.replyFirstMessage(body1);
		topicView.goBackToTopics();
		topicsPage.refresh();
		
		String newReplies = topicsPage.getNumberOfRepliesBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		Assert.assertEquals(newReplies.toString(), "1", "No new reply exist!");
		
		// Now try to submit
		
		topicsPage.nextActivity();
		
		String warningMsg = topicsPage.getRepliesLimitWarning();
		// Assert warning message
		Assert.assertEquals(warningMsg, warningMinMessages, "The warning message is not being shown!");
		
		topicView = topicsPage.openTopicBySubject(LearnerConstants.LEARNER_DEFAULT_TOPIC_SUBJECT);
		
		// Post 4 more messages
		for (int i = 1; i < 5; i++) {
			topicView.replyFirstMessage(LamsUtil.randomText(i));
		}
		
		// Now check that all reply buttons are gone
		//System.out.println("starts");
		int isRepliesOn = topicView.getNumberOfRepliableMessages();
		
		
		// Assert that you can't reply to messages anymore
		Assert.assertEquals(isRepliesOn, 0, "There are reply options still available!");
		
		topicView.goBackToTopics();
		topicsPage.nextActivity();
		
		// Close learner page 
		learnerPage = topicsPage.returnLearnerHandler(learnerHandler);
		learnerPage.openControlFrame().closeLearnerPage(indexHandler);

		
	}
	
	
	
	@AfterClass
	public void afterClass() {
		//driver.quit();
	}
	
	
	private AddLessonPage openDialog(IndexPage index) {
		addLesson = index.addLesson();
		
		return addLesson;
	}
	

}
