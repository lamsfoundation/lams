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

import org.lamsfoundation.lams.selenium.AbstractSeleniumTestCase;
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
    
    private Map<String, String> contentDetails;

    protected String getToolSignature() {
	return ForumConstants.TOOL_SIGNATURE;
    }

    protected String getLearningDesignName() {
	return LEARNING_DESIGN_TITLE;
    }

    @Override
    public void testEntireTool() throws Exception {
    }

    @Override
    protected void learningTest() throws InterruptedException {
    }

    @Override
    protected void monitoringTest() {
    }

    @Override
    protected void authoringTest() throws Exception {
    }

    /**
     * Testing the wiki author module
     */
    public void testAuthor() {
	try {

	    // Logging in and opening authoring
	    loginToLams();
	    contentDetails = setUpAuthoring();

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

	    storeLearningDesign(contentDetails);
	} catch (Exception e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	}

    }

    /**
     * Test starting the lesson
     */
    public void testStartLesson() {
	try {
	    loginToLams();
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
	    loginToLams();
	    setUpLearning();
	    assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));

	    //	    // Test history has been emptied
	    //	    testHistory(WIKI_TITLE1, false);
	    //
	    //	    // Test the main page cannot be removed
	    //	    testRemove(WIKI_TITLE1, false);
	    //
	    //	    // Adding a dummy page and removing it
	    //	    testAdd(WIKI_TITLE4, WIKI_BODY6);
	    //	    testRemove(WIKI_TITLE4, true);
	    //
	    //	    // Testing wiki add page in learner
	    //	    testAdd(WIKI_TITLE3, WIKI_BODY4);
	    //
	    //	    // Testing author edit again
	    //	    selenium.runScript("changeWikiPage('" + WIKI_TITLE3 + "')");
	    //	    selenium.waitForPageToLoad("30000");
	    //	    testEdit(WIKI_TITLE3, WIKI_BODY5);
	    //
	    //	    // Test history has been added
	    //	    testHistory(WIKI_TITLE3, true);
	    //
	    //	    // Removing the wiki page so it can be made again in the next test
	    //	    testRemove(WIKI_TITLE3, true);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Monitor Tests
     */
    public void testMonitor() {
	try {
	    loginToLams();
	    openToolMonitor();

	    // Should be able to edit this topic 
	    testEditTopic(TOPIC2_TITLE, TOPIC2_TITLE, TOPIC2_MESSAGE2);
	    returnToTopicList();
	    
	    // Test creating new topics
	    
	   
	    
	    //	    assertTrue("Montor page did not load properly", selenium.isTextPresent("Wiki Sessions"));
	    //	    
	    //	    // Clicking on the first link which pops up the monitor page
	    //	    selenium.click("//td//a");
	    //	    selenium.waitForPopUp("viewWindow", "10000");
	    //	    selenium.selectWindow("viewWindow");
	    //	    
	    //
	    //	    // Test history has been emptied
	    //	    testHistory(WIKI_TITLE1, false);
	    //
	    //	    // Test the main page cannot be removed
	    //	    testRemove(WIKI_TITLE1, false);
	    //	    
	    //	    // Adding a dummy page and removing it
	    //	    testAdd(WIKI_TITLE4, WIKI_BODY6);
	    //	    
	    //	    // Editing the page
	    //	    selenium.runScript("changeWikiPage('" + WIKI_TITLE4 + "')");
	    //	    selenium.waitForPageToLoad("30000");
	    //	    testEdit(WIKI_TITLE4, WIKI_BODY5);
	    //	    
	    //	    // Removing a page
	    //	    testRemove(WIKI_TITLE4, true);

	    selenium.close();
	    selenium.selectWindow(null);
	    closeToolMonitor();
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
	selenium.click("//a[@name=backToForum");
	selenium.waitForPageToLoad("30000");
    }
    
    

    //    /**
    //     * Tests editing the wiki page in author
    //     */
    //    private void testEdit(String title, String body) {
    //
    //	// Go to the edit tab
    //	selenium.click("//div[@id='buttons']//a[@title='Edit the current Wiki page']");
    //
    //	// Set the  wiki title and body, then save
    //	selenium.type("title", title);
    //	selenium.runScript("FCKeditorAPI.GetInstance(\"wikiBody\").SetHTML(\"" + body + "\")");
    //	selenium.runScript("doEditOrAdd('editPage')");
    //	selenium.waitForPageToLoad("30000");
    //
    //	assertTrue("Title is not present on page: " + title, selenium.isTextPresent(title));
    //	assertTrue("Wiki body is not present on page: " + body, selenium.isTextPresent(body));
    //    }
    //
    //    /**
    //     * Tests editing the wiki page in author
    //     */
    //    private void testAdd(String title, String body) {
    //	// Go to the edit tab
    //	selenium.click("//div[@id='buttons']//a[@title='Add a new Wiki page to this Wiki']");
    //
    //	// Set the  wiki title and body, then save
    //	selenium.type("newPageTitle", title);
    //	selenium.runScript("FCKeditorAPI.GetInstance(\"newPageWikiBody\").SetHTML(\"" + body + "\")");
    //	selenium.runScript("doEditOrAdd('addPage')");
    //	selenium.waitForPageToLoad("30000");
    //
    //	selenium.runScript("changeWikiPage('" + title + "')");
    //	selenium.waitForPageToLoad("30000");
    //
    //	assertTrue("Title is not present on page: " + title, selenium.isTextPresent(title));
    //	assertTrue("Wiki body is not present on page: " + body, selenium.isTextPresent(body));
    //    }
    //
    //    private void testHistory(String title, boolean historyExpected) {
    //	selenium.runScript("changeWikiPage('" + title + "')");
    //	selenium.waitForPageToLoad("30000");
    //
    //	// Go to the history tab
    //	selenium.click("//div[@id='buttons']//a[@title='View previous versions of this Wiki page']");
    //
    //	if (historyExpected) {
    //	    assertTrue("Revision is not present on edited page: " + title, selenium.isTextPresent("Revert"));
    //	    // TODO: Test the compare and revert feature
    //	} else {
    //	    assertFalse("Revision is not present on edited page: " + title, selenium.isTextPresent("Revert"));
    //	}
    //    }
    //
    //    private void testRemove(String title, boolean removeAllowedExpected) {
    //	selenium.runScript("changeWikiPage('" + title + "')");
    //	selenium.waitForPageToLoad("30000");
    //
    //	if (removeAllowedExpected) {
    //	    assertTrue("Remove should be allowed for wiki page: " + title, selenium.isTextPresent("Remove"));
    //
    //	    // Remove the page
    //	    selenium.runScript("submitWiki('removePage');");
    //	    selenium.waitForPageToLoad("30000");
    //
    //	    selenium.runScript("toggleWikiList('removePage');");
    //	    assertFalse("Wiki page should have been removed: " + title, selenium.isTextPresent(title));
    //	} else {
    //	    assertFalse("Remove should not be allowed for wiki page: " + title, selenium.isTextPresent("Remove"));
    //	}
    //
    //    }

}
