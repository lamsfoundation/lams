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

import java.util.Map;

import junit.framework.TestSuite;

import org.jfree.util.Log;
import org.lamsfoundation.lams.selenium.AbstractSeleniumTestCase;
import org.lamsfoundation.lams.selenium.SeleniumTestSuite;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;

/**
 * Tests for forum tool
 * 
 * NOTE: For the learner and monitor tests, you must have already run the
 * testAuthor and startLesson tests at least once
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

    protected String getToolSignature() {
	return ForumConstants.TOOL_SIGNATURE;
    }

    protected String getLearningDesignName() {
	return LEARNING_DESIGN_TITLE;
    }
    
    public static TestSuite suite() {
	String[] testSequence = {"testAuthor", "testStartLesson", "testLearner", "testMonitor"};
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
	    assertTrue("Default content is not present", selenium.isTextPresent(DEFAULT_TOPIC));

	    selenium.type("forum.title", TITLE);
	    selenium.runScript("FCKeditorAPI.GetInstance(\"forum.instructions\").SetHTML(\"" + DESCRIPTION + "\")");

	    // Edit the topic of the default thread
	    testEditTopic(DEFAULT_TOPIC, TOPIC1_TITLE, TOPIC1_MESSAGE1);

	    // Test adding a new topic
	    testCreateTopic(TOPIC2_TITLE, TOPIC2_MESSAGE1);

	    // Test deleting a topic
	    testCreateTopic(TOPIC3_TITLE, TOPIC3_MESSAGE1);
	    testRemoveTopic(TOPIC3_TITLE);

	    // Testing other tabs
	    selenium.click("tab-middle-link-2");
	    selenium.click("lockWhenFinished");
	    selenium.click("allowUpload");
	    selenium.click("limitedInput");

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
	    assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));

	    // Testing a simple reply to a post
	    testReplyToFirstPost(TOPIC2_TITLE, REPLY_TITLE1, REPLY_MESSAGE1);

	    // Testing an edit of a post
	    testEditFirstPost(TOPIC2_TITLE, TOPIC2_TITLE, EDIT_MESSAGE1);

	    // Testing creating a topic
	    testCreateTopicLearner(TOPIC4_TITLE, TOPIC4_MESSAGE);
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
	assertTrue("Topic does not exist", selenium.isTextPresent(title));

	// Open the iframe for editing the topic
	selenium.click("//a[text()='" + title + "']");
	selenium.waitForFrameToLoad("messageArea", "30000");
	selenium.selectFrame("messageArea");

	// Edit the topic
	selenium.click("//a[text()='Edit']");
	selenium.waitForFrameToLoad("messageArea", "30000");
	selenium.type("message.subject", newTitle);
	selenium.runScript("FCKeditorAPI.GetInstance(\"message.body\").SetHTML(\"" + message + "\")");

	// TODO: Add tests for attaching files

	// Save the changes
	selenium.click("//a[@class='button-add-item']");
	selenium.waitForFrameToLoad("messageArea", "30000");

	// Return to the top frame
	selenium.selectFrame("relative=top");

	// Testing that the topic was successfully edited
	assertTrue("Topic not successfully edited: " + newTitle, selenium.isTextPresent(newTitle));
    }

    /**
     * Tests creating a new topic, requires topic not already created
     * 
     * @param title
     * @param message
     */
    private void testCreateTopic(String title, String message) {

	// Open the iframe for editing the topic
	selenium.click("//a[text()=' Create a new topic ']");
	selenium.waitForFrameToLoad("messageArea", "30000");
	selenium.selectFrame("messageArea");

	// Add the topic 
	selenium.type("message.subject", title);
	selenium.runScript("FCKeditorAPI.GetInstance(\"message.body\").SetHTML(\"" + message + "\")");

	// TODO: Add tests for attaching files

	// Save the changes
	selenium.click("//a[@class='button-add-item']");
	selenium.waitForFrameToLoad("messageArea", "30000");

	// Return to the top frame
	selenium.selectFrame("relative=top");

	// Testing that the topic was successfully edited
	assertTrue("Topic not successfully added: " + title, selenium.isTextPresent(title));
    }

    /**
     * Tests removing a title
     * 
     * @param title
     */
    private void testRemoveTopic(String title) {
	// Check the topic exists
	assertTrue("Topic does not exist", selenium.isTextPresent(title));

	// Open the iframe for editing the topic
	selenium.click("//a[text()='" + title + "']");
	selenium.waitForFrameToLoad("messageArea", "30000");
	selenium.selectFrame("messageArea");

	// Click the delete button
	selenium.click("//a[text()='Delete']");
	selenium.waitForFrameToLoad("messageArea", "30000");

	// Return to the top frame
	selenium.selectFrame("relative=top");

	// Testing that the topic was successfully edited
	assertFalse("Topic not successfully removed: " + title, selenium.isTextPresent(title));
    }

    private void returnToTopicList() {
	selenium.click("//input[@name='backToForum']");
	selenium.waitForPageToLoad("30000");
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
	assertTrue("Topic does not exist: " + topic, selenium.isTextPresent(topic));

	// Open the iframe for editing the topic
	selenium.click("//a[text()='<b>" + topic + "</b>' or text()='" + topic + "']");
	selenium.waitForPageToLoad("30000");

	// Select the last post to reply to
	selenium.click("//a[text()='Reply']");

	// Check the reply has opened
	assertTrue("Reply form did not open.", selenium.isTextPresent("Reply Message"));

	// Do the reply
	selenium.type("message.subject", title);
	selenium.type("message.body__textarea", message);
	selenium.click("//input[@type='submit']");
	selenium.waitForPageToLoad("30000");

	// Test the reply has been posted
	assertTrue("Reply did not appear: " + title, selenium.isTextPresent(title));
	assertTrue("Reply did not appear: " + message, selenium.isTextPresent(message));

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
	assertTrue("Topic does not exist: " + topic, selenium.isTextPresent(topic));

	// Open the iframe for editing the topic
	selenium.click("//a[text()='<b>" + topic + "</b>' or text()='" + topic + "']");
	selenium.waitForPageToLoad("30000");

	// Select the last post to reply to
	selenium.click("//a[text()='Edit']");

	// Check the edit has opened
	assertTrue("Edit form did not open.", selenium.isTextPresent("Edit Message"));

	// Do the reply
	selenium.type("message.subject", title);
	selenium.type("message.body__textarea", message);
	selenium.click("//input[@type='submit']");
	selenium.waitForPageToLoad("30000");

	// Test the reply has been posted
	assertTrue("Reply did not appear: " + title, selenium.isTextPresent(title));
	assertTrue("Reply did not appear: " + message, selenium.isTextPresent(message));

	// Return to the topic list
	returnToTopicList();
    }

    /**
     * Tests creating a new topic for learner, requires topic not already
     * created
     * 
     * @param title
     * @param message
     */
    private void testCreateTopicLearner(String title, String message) {

	// Skip the test if the topic has already been created - ie a previously passed test
	if (!selenium.isTextPresent(title)) {
	    // Click the new topic link
	    selenium.click("//input[@name='newtopic']");
	    selenium.waitForPageToLoad("30000");

	    // Add the topic 
	    selenium.type("message.subject", title);
	    selenium.type("message.body__textarea", message);

	    // Save the changes
	    selenium.click("//input[@type='submit']");
	    selenium.waitForPageToLoad("30000");

	    // Testing that the topic was successfully edited
	    assertTrue("Topic not successfully added: " + title, selenium.isTextPresent(title));
	}

    }

}
