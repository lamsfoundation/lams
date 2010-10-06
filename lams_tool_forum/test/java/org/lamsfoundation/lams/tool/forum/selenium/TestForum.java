/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
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
package org.lamsfoundation.lams.tool.forum.selenium;

import junit.framework.TestSuite;

import org.jfree.util.Log;
import org.lamsfoundation.lams.selenium.AbstractSeleniumTestCase;
import org.lamsfoundation.lams.selenium.SeleniumTestSuite;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;

/**
 * Tests for forum tool
 * 
 * NOTE: For the learner and monitor tests, you must have already run the testAuthor and startLesson tests at least once
 * 
 * @author lfoxton
 * 
 */
public class TestForum extends AbstractSeleniumTestCase {

    private static final String LEARNING_DESIGN_TITLE = "forumTest";

    private static final String DEFAULT_TOPIC = "Topic Heading";

    private static final String TITLE = "Car Forum";
    private static final String DESCRIPTION = "Make comments about your favourite car types";
    private static final String TOPIC1_TITLE = "Ferrari";
    private static final String TOPIC1_MESSAGE1 = "Testarossa is the best";
    private static final String TOPIC2_TITLE = "Porche";
    private static final String TOPIC2_MESSAGE1 = "Porche 911 is the best";
    private static final String TOPIC2_MESSAGE2 = "Porche 911 is the best, although everone has them.";
    private static final String TOPIC3_TITLE = "Dummy Title";
    private static final String TOPIC3_MESSAGE1 = "Dummy Message";
    private static final String REPLY_TITLE1 = "Reply title";
    private static final String REPLY_MESSAGE1 = "This is a reply";
    private static final String EDIT_MESSAGE1 = "This is an edited message";
    private static final String TOPIC4_TITLE = "Learner Topic";
    private static final String TOPIC4_MESSAGE = "Created in learner";

    @Override
    protected String getToolSignature() {
	return ForumConstants.TOOL_SIGNATURE;
    }

    @Override
    protected String getLearningDesignName() {
	return TestForum.LEARNING_DESIGN_TITLE;
    }

    public static TestSuite suite() {
	String[] testSequence = { "testAuthor", "testStartLesson", "testLearner", "testMonitor" };
	return new SeleniumTestSuite(TestForum.class, testSequence);
    }

    /**
     * Testing the wiki author module
     */
    public void testAuthor() {
	try {

	    // Logging in and opening authoring
	    loginToLams();
	    setUpAuthoring();

	    // check the author page has loaded the default content
	    assertTrue("Default content is not present",
		    AbstractSeleniumTestCase.selenium.isTextPresent(TestForum.DEFAULT_TOPIC));

	    AbstractSeleniumTestCase.selenium.type("forum.title", TestForum.TITLE);
	    AbstractSeleniumTestCase.selenium.runScript("CKEDITOR.instances[\"forum.instructions\").setData(\""
		    + TestForum.DESCRIPTION + "\")");

	    // Edit the topic of the default thread
	    testEditTopic(TestForum.DEFAULT_TOPIC, TestForum.TOPIC1_TITLE, TestForum.TOPIC1_MESSAGE1);

	    // Test adding a new topic
	    testCreateTopic(TestForum.TOPIC2_TITLE, TestForum.TOPIC2_MESSAGE1);

	    // Test deleting a topic
	    testCreateTopic(TestForum.TOPIC3_TITLE, TestForum.TOPIC3_MESSAGE1);
	    testRemoveTopic(TestForum.TOPIC3_TITLE);

	    // Testing other tabs
	    AbstractSeleniumTestCase.selenium.click("tab-middle-link-2");
	    AbstractSeleniumTestCase.selenium.click("lockWhenFinished");
	    AbstractSeleniumTestCase.selenium.click("allowUpload");
	    AbstractSeleniumTestCase.selenium.click("limitedInput");

	    // TODO: Test conditions tab

	    storeLearningDesign();
	} catch (Exception e) {
	    Log.error(e);
	    fail(e.getMessage());
	}

    }

    /**
     * Test starting the lesson
     */
    public void testStartLesson() {
	try {
	    createNewLesson();
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Learner Tests
     */
    public void testLearner() {
	try {
	    setUpLearning();
	    assertEquals("LAMS Learner", AbstractSeleniumTestCase.selenium.isElementPresent("//a[@id='finishButton']"));

	    // Testing a simple reply to a post
	    testReplyToFirstPost(TestForum.TOPIC2_TITLE, TestForum.REPLY_TITLE1, TestForum.REPLY_MESSAGE1);

	    // Testing an edit of a post
	    testEditFirstPost(TestForum.TOPIC2_TITLE, TestForum.TOPIC2_TITLE, TestForum.EDIT_MESSAGE1);

	    // Testing creating a topic
	    testCreateTopicLearner(TestForum.TOPIC4_TITLE, TestForum.TOPIC4_MESSAGE);
	} catch (Exception e) {
	    Log.error(e);
	    fail(e.getMessage());
	}
    }

    /**
     * Monitor Tests
     */
    public void testMonitor() {
	try {
	    setUpMonitoring();

	    // TODO: Work out how to access the forum page, which is opened in a _blank page

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    // *************************************************************************
    // Helper test methods
    // *************************************************************************

    /**
     * Test for editing a thread, it must already exist
     * 
     * @param title
     * @patam newTitle
     * @param message
     */
    private void testEditTopic(String title, String newTitle, String message) {

	// Check the topic exists
	assertTrue("Topic does not exist", AbstractSeleniumTestCase.selenium.isTextPresent(title));

	// Open the iframe for editing the topic
	AbstractSeleniumTestCase.selenium.click("//a[text()='" + title + "']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");
	AbstractSeleniumTestCase.selenium.selectFrame("messageArea");

	// Edit the topic
	AbstractSeleniumTestCase.selenium.click("//a[text()='Edit']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");
	AbstractSeleniumTestCase.selenium.type("message.subject", newTitle);
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"message.body\").setData(\"" + message + "\")");

	// TODO: Add tests for attaching files

	// Save the changes
	AbstractSeleniumTestCase.selenium.click("//a[@class='button-add-item']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");

	// Return to the top frame
	AbstractSeleniumTestCase.selenium.selectFrame("relative=top");

	// Testing that the topic was successfully edited
	assertTrue("Topic not successfully edited: " + newTitle,
		AbstractSeleniumTestCase.selenium.isTextPresent(newTitle));
    }

    /**
     * Tests creating a new topic, requires topic not already created
     * 
     * @param title
     * @param message
     */
    private void testCreateTopic(String title, String message) {

	// Open the iframe for editing the topic
	AbstractSeleniumTestCase.selenium.click("//a[text()=' Create a new topic ']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");
	AbstractSeleniumTestCase.selenium.selectFrame("messageArea");

	// Add the topic
	AbstractSeleniumTestCase.selenium.type("message.subject", title);
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"message.body\").setData(\"" + message + "\")");

	// TODO: Add tests for attaching files

	// Save the changes
	AbstractSeleniumTestCase.selenium.click("//a[@class='button-add-item']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");

	// Return to the top frame
	AbstractSeleniumTestCase.selenium.selectFrame("relative=top");

	// Testing that the topic was successfully edited
	assertTrue("Topic not successfully added: " + title, AbstractSeleniumTestCase.selenium.isTextPresent(title));
    }

    /**
     * Tests removing a title
     * 
     * @param title
     */
    private void testRemoveTopic(String title) {
	// Check the topic exists
	assertTrue("Topic does not exist", AbstractSeleniumTestCase.selenium.isTextPresent(title));

	// Open the iframe for editing the topic
	AbstractSeleniumTestCase.selenium.click("//a[text()='" + title + "']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");
	AbstractSeleniumTestCase.selenium.selectFrame("messageArea");

	// Click the delete button
	AbstractSeleniumTestCase.selenium.click("//a[text()='Delete']");
	AbstractSeleniumTestCase.selenium.waitForFrameToLoad("messageArea", "30000");

	// Return to the top frame
	AbstractSeleniumTestCase.selenium.selectFrame("relative=top");

	// Testing that the topic was successfully edited
	assertFalse("Topic not successfully removed: " + title, AbstractSeleniumTestCase.selenium.isTextPresent(title));
    }

    private void returnToTopicList() {
	AbstractSeleniumTestCase.selenium.click("//input[@name='backToForum']");
	AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");
    }

    /**
     * Tests replying to a post
     * 
     * @param topic
     * @param title
     * @param message
     */
    private void testReplyToFirstPost(String topic, String title, String message) {
	// Check the topic exists
	assertTrue("Topic does not exist: " + topic, AbstractSeleniumTestCase.selenium.isTextPresent(topic));

	// Open the iframe for editing the topic
	AbstractSeleniumTestCase.selenium.click("//a[text()='<b>" + topic + "</b>' or text()='" + topic + "']");
	AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");

	// Select the last post to reply to
	AbstractSeleniumTestCase.selenium.click("//a[text()='Reply']");

	// Check the reply has opened
	assertTrue("Reply form did not open.", AbstractSeleniumTestCase.selenium.isTextPresent("Reply Message"));

	// Do the reply
	AbstractSeleniumTestCase.selenium.type("message.subject", title);
	AbstractSeleniumTestCase.selenium.type("message.body__textarea", message);
	AbstractSeleniumTestCase.selenium.click("//input[@type='submit']");
	AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");

	// Test the reply has been posted
	assertTrue("Reply did not appear: " + title, AbstractSeleniumTestCase.selenium.isTextPresent(title));
	assertTrue("Reply did not appear: " + message, AbstractSeleniumTestCase.selenium.isTextPresent(message));

	// Return to the topic list
	returnToTopicList();
    }

    /**
     * Tests editing a post, must have a post available to edit
     * 
     * @param topic
     * @param title
     * @param message
     */
    private void testEditFirstPost(String topic, String title, String message) {
	// Check the topic exists
	assertTrue("Topic does not exist: " + topic, AbstractSeleniumTestCase.selenium.isTextPresent(topic));

	// Open the iframe for editing the topic
	AbstractSeleniumTestCase.selenium.click("//a[text()='<b>" + topic + "</b>' or text()='" + topic + "']");
	AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");

	// Select the last post to reply to
	AbstractSeleniumTestCase.selenium.click("//a[text()='Edit']");

	// Check the edit has opened
	assertTrue("Edit form did not open.", AbstractSeleniumTestCase.selenium.isTextPresent("Edit Message"));

	// Do the reply
	AbstractSeleniumTestCase.selenium.type("message.subject", title);
	AbstractSeleniumTestCase.selenium.type("message.body__textarea", message);
	AbstractSeleniumTestCase.selenium.click("//input[@type='submit']");
	AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");

	// Test the reply has been posted
	assertTrue("Reply did not appear: " + title, AbstractSeleniumTestCase.selenium.isTextPresent(title));
	assertTrue("Reply did not appear: " + message, AbstractSeleniumTestCase.selenium.isTextPresent(message));

	// Return to the topic list
	returnToTopicList();
    }

    /**
     * Tests creating a new topic for learner, requires topic not already created
     * 
     * @param title
     * @param message
     */
    private void testCreateTopicLearner(String title, String message) {

	// Skip the test if the topic has already been created - ie a previously passed test
	if (!AbstractSeleniumTestCase.selenium.isTextPresent(title)) {
	    // Click the new topic link
	    AbstractSeleniumTestCase.selenium.click("//input[@name='newtopic']");
	    AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");

	    // Add the topic
	    AbstractSeleniumTestCase.selenium.type("message.subject", title);
	    AbstractSeleniumTestCase.selenium.type("message.body__textarea", message);

	    // Save the changes
	    AbstractSeleniumTestCase.selenium.click("//input[@type='submit']");
	    AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");

	    // Testing that the topic was successfully edited
	    assertTrue("Topic not successfully added: " + title, AbstractSeleniumTestCase.selenium.isTextPresent(title));
	}

    }

}
