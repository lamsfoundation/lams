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

public class TestWiki extends AbstractSeleniumTestCase {

    private static final String LEARNING_DESIGN_TITLE  ="wikiTest";
    private static final String WIKI_TITLE1 = "Dog Wiki";
    private static final String WIKI_TITLE2 = "Blue Healers";
    private static final String WIKI_BODY1 = "Dogs come in many breeds.";
    private static final String WIKI_BODY2 = "Blue Healers are small dogs origninally bred for farming and hunting";
    private static final String WIKI_BODY3 = "Dogs come in many breeds, please attach a wiki page about a breed.";

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

    // *************************************************************************
    // Wiki author tests
    // *************************************************************************
    
    /**
     * Testing the wiki author module
     */
    public void testAuthor() {
	try {

	    // Logging in and opening authoring
	    loginToLams();
	    contentDetails = setUpAuthoring();

	    // Testing wiki edit page
	    testAuthorEdit(WIKI_TITLE1, WIKI_BODY1);

	    // Testing wiki add page
	    testAuthorAdd(WIKI_TITLE2, WIKI_BODY2);

	    // Testing author edit again
	    selenium.runScript("changeWikiPage('" + WIKI_TITLE1 + "')");
	    selenium.waitForPageToLoad("30000");
	    testAuthorEdit(WIKI_TITLE1, WIKI_BODY3);

	    // Test history
	    testAuthorHistory(WIKI_TITLE1);

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
     * Tests editing the wiki page in author
     */
    private void testAuthorEdit(String title, String body) {

	// Go to the edit tab
	selenium.runScript("changeDiv('edit')");

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
    private void testAuthorAdd(String title, String body) {
	// Go to the edit tab
	selenium.runScript("changeDiv('add')");

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

    private void testAuthorHistory(String title) {
	selenium.runScript("changeWikiPage('" + title + "')");
	selenium.waitForPageToLoad("30000");

	// Go to the history tab
	selenium.runScript("changeDiv('history'" + "" + ")");
	assertTrue("Revision is not present on edited page: " + title, selenium.isTextPresent("Revert"));
	
	// TODO: Test the compare feature
    }
    
    // *************************************************************************
    // Wiki learner tests
    // *************************************************************************
    
    public void testLearner() {
	try {
	    loginToLams();
	    createNewLesson();
	    setUpLearning();
	    assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));  
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
    
    // *************************************************************************
    // Wiki monitor tests
    // *************************************************************************

    public void testMonitor() {
	try {
	    loginToLams(); 
	} catch (Exception e) {
	    fail(e.getMessage());
	}
    }
}
