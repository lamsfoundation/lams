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
package org.lamsfoundation.lams.tool.wiki.selenium;

import java.util.Map;

import org.lamsfoundation.lams.selenium.AbstractSeleniumTestCase;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;

/**
 * Tests for wiki tool
 * 
 * NOTE: For the learner and monitor tests, you must have already run the
 * testAuthor and startLesson tests at least once
 * 
 * @author lfoxton
 * 
 */
public class TestWiki extends AbstractSeleniumTestCase {

    private static final String LEARNING_DESIGN_TITLE = "wikiTest";
    private static final String WIKI_TITLE1 = "Dog Wiki";
    private static final String WIKI_TITLE2 = "Blue Healers";
    private static final String WIKI_TITLE3 = "Alsatian";
    private static final String WIKI_TITLE4 = "Dummy Title";
    private static final String WIKI_BODY1 = "Dogs come in many breeds.";
    private static final String WIKI_BODY2 = "Blue Healers are small dogs origninally bred for farming and hunting";
    private static final String WIKI_BODY3 = "Dogs come in many breeds, please attach a wiki page about a breed.";
    private static final String WIKI_BODY4 = "Alsatian are large dogs used by police.";
    private static final String WIKI_BODY5 = "Alsatian are large dogs used by police. originally bred to herd sheep";
    private static final String WIKI_BODY6 = "Dummy Body";
    private static final String LOCK_ON_FINISH_MESSAGE = "Note: After you click on \"Next Activity\" and you come back to this Wiki, you won't be able to continue editing.";

    private Map<String, String> contentDetails;

    protected String getToolSignature() {
	return WikiConstants.TOOL_SIGNATURE;
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

	    // Testing wiki edit page
	    testEdit(WIKI_TITLE1, WIKI_BODY1);

	    // Test that the main page cannot be removed
	    testRemove(WIKI_TITLE1, false);

	    // Testing wiki add page
	    testAdd(WIKI_TITLE2, WIKI_BODY2);

	    // Testing author edit again
	    selenium.runScript("changeWikiPage('" + WIKI_TITLE1 + "')");
	    selenium.waitForPageToLoad("30000");
	    testEdit(WIKI_TITLE1, WIKI_BODY3);

	    // Test history
	    testHistory(WIKI_TITLE1, true);

	    // Testing other tabs
	    selenium.click("tab-middle-link-2");
	    selenium.click("lockOnFinished");
	    selenium.click("tab-middle-link-3");
	    selenium.type("onlineInstruction__lamstextarea", "online instructions");

	    storeLearningDesign(contentDetails);
	} catch (Exception e) {
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
     * Wiki Learner Tests
     */
    public void testLearner() {
	try {
	    loginToLams();
	    setUpLearning();
	    assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));

	    // Test that the lock on finish message is present
	    assertTrue("Lock on finish message not present", selenium.isTextPresent(LOCK_ON_FINISH_MESSAGE));
	    
	    // Test history has been emptied
	    testHistory(WIKI_TITLE1, false);

	    // Test the main page cannot be removed
	    testRemove(WIKI_TITLE1, false);

	    // Adding a dummy page and removing it
	    testAdd(WIKI_TITLE4, WIKI_BODY6);
	    testRemove(WIKI_TITLE4, true);

	    // Testing wiki add page in learner
	    testAdd(WIKI_TITLE3, WIKI_BODY4);

	    // Testing author edit again
	    selenium.runScript("changeWikiPage('" + WIKI_TITLE3 + "')");
	    selenium.waitForPageToLoad("30000");
	    testEdit(WIKI_TITLE3, WIKI_BODY5);

	    // Test history has been added
	    testHistory(WIKI_TITLE3, true);

	    // Removing the wiki page so it can be made again in the next test
	    testRemove(WIKI_TITLE3, true);

	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }

    /**
     * Wiki Monitor Tests
     */
    public void testMonitor() {
	try {
	    loginToLams();
	    openToolMonitor();
	    
	    assertTrue("Montor page did not load properly", selenium.isTextPresent("Wiki Sessions"));
	    
	    // Clicking on the first link which pops up the monitor page
	    selenium.click("//td//a");
	    selenium.waitForPopUp("viewWindow", "10000");
	    selenium.selectWindow("viewWindow");
	    

	    // Test history has been emptied
	    testHistory(WIKI_TITLE1, false);

	    // Test the main page cannot be removed
	    testRemove(WIKI_TITLE1, false);
	    
	    // Adding a dummy page and removing it
	    testAdd(WIKI_TITLE4, WIKI_BODY6);
	    
	    // Editing the page
	    selenium.runScript("changeWikiPage('" + WIKI_TITLE4 + "')");
	    selenium.waitForPageToLoad("30000");
	    testEdit(WIKI_TITLE4, WIKI_BODY5);
	    
	    // Removing a page
	    testRemove(WIKI_TITLE4, true);
	    
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
     * Tests editing the wiki page in author
     */
    private void testEdit(String title, String body) {

	// Go to the edit tab
	selenium.click("//div[@id='buttons']//a[@title='Edit the current Wiki page']");

	// Set the  wiki title and body, then save
	selenium.type("title", title);
	selenium.runScript("FCKeditorAPI.GetInstance(\"wikiBody\").SetHTML(\"" + body + "\")");
	selenium.runScript("doEditOrAdd('editPage')");
	selenium.waitForPageToLoad("30000");

	assertTrue("Title is not present on page: " + title, selenium.isTextPresent(title));
	assertTrue("Wiki body is not present on page: " + body, selenium.isTextPresent(body));
    }

    /**
     * Tests editing the wiki page in author
     */
    private void testAdd(String title, String body) {
	// Go to the edit tab
	selenium.click("//div[@id='buttons']//a[@title='Add a new Wiki page to this Wiki']");

	// Set the  wiki title and body, then save
	selenium.type("newPageTitle", title);
	selenium.runScript("FCKeditorAPI.GetInstance(\"newPageWikiBody\").SetHTML(\"" + body + "\")");
	selenium.runScript("doEditOrAdd('addPage')");
	selenium.waitForPageToLoad("30000");

	selenium.runScript("changeWikiPage('" + title + "')");
	selenium.waitForPageToLoad("30000");

	assertTrue("Title is not present on page: " + title, selenium.isTextPresent(title));
	assertTrue("Wiki body is not present on page: " + body, selenium.isTextPresent(body));
    }

    private void testHistory(String title, boolean historyExpected) {
	selenium.runScript("changeWikiPage('" + title + "')");
	selenium.waitForPageToLoad("30000");

	// Go to the history tab
	selenium.click("//div[@id='buttons']//a[@title='View previous versions of this Wiki page']");

	if (historyExpected) {
	    assertTrue("Revision is not present on edited page: " + title, selenium.isTextPresent("Revert"));
	    // TODO: Test the compare and revert feature
	} else {
	    assertFalse("Revision is not present on edited page: " + title, selenium.isTextPresent("Revert"));
	}
    }

    private void testRemove(String title, boolean removeAllowedExpected) {
	selenium.runScript("changeWikiPage('" + title + "')");
	selenium.waitForPageToLoad("30000");

	if (removeAllowedExpected) {
	    assertTrue("Remove should be allowed for wiki page: " + title, selenium.isTextPresent("Remove"));

	    // Remove the page
	    selenium.runScript("submitWiki('removePage');");
	    selenium.waitForPageToLoad("30000");

	    selenium.runScript("toggleWikiList('removePage');");
	    assertFalse("Wiki page should have been removed: " + title, selenium.isTextPresent(title));
	} else {
	    assertFalse("Remove should not be allowed for wiki page: " + title, selenium.isTextPresent("Remove"));
	}

    }

}
