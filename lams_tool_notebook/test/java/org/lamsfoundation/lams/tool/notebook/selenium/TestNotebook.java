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

/* $Id$ */
package org.lamsfoundation.lams.tool.notebook.selenium;


import junit.framework.TestSuite;

import org.lamsfoundation.lams.selenium.AbstractSeleniumTestCase;
import org.lamsfoundation.lams.selenium.SeleniumTestSuite;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;

public class TestNotebook extends AbstractSeleniumTestCase {

    protected String getToolSignature() {
	return NotebookConstants.TOOL_SIGNATURE;
    }

    protected String getLearningDesignName() {
	return "bueno";
    }

    public static TestSuite suite() {
	String[] testSequence = {"testAuthoring", "testCreateNewLesson", "testLearning", "testMonitoring"};
	return new SeleniumTestSuite(TestNotebook.class, testSequence);
    }

    public void testAuthoring() throws Exception {
	loginToLams();
	setUpAuthoring();
	
	assertEquals("Notebook", selenium.getTitle());

	selenium.type("title", "leave your comment2222");
	selenium.runScript("FCKeditorAPI.GetInstance(\"instructions\").SetHTML(\"invent a new way of using Flash\")");
	selenium.click("tab-middle-link-2");
	selenium.click("lockOnFinished");
	selenium.click("tab-middle-link-3");
	selenium.type("onlineInstruction__lamstextarea", "online instructions");
	
	storeLearningDesign();	
    }
    
    public void testCreateNewLesson() throws Exception {
	createNewLesson();	
    }

    public void testLearning() throws Exception {
	setUpLearning();
	assertEquals("Notebook", selenium.getTitle());
	assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));
	selenium.type("entryText", "have fun");
	selenium.click("//a[@id='finishButton']/span");
	waitForLearning();
	
	assertTrue(selenium.isTextPresent("Congratulations"));
	assertFalse(selenium.isElementPresent("entryText"));
	tearDownLearning();

	setUpLearning();
	assertTrue(selenium.isTextPresent("Congratulations"));
	assertFalse(selenium.isElementPresent("entryText"));
    }

    public void testMonitoring() {
	setUpMonitoring();
	
	assertEquals("Notebook", selenium.getTitle());
    }

}
