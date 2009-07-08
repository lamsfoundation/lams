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
package org.lamsfoundation.lams.tool.notebook.testscripts;

import org.lamsfoundation.lams.tool.notebook.core.AbstractSeleniumTestCase;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;

public class TestNotebook extends AbstractSeleniumTestCase {


	protected String getToolSignature() {
		return NotebookConstants.TOOL_SIGNATURE;
	}
	
	protected String getLearningDesignName() {
		return "bueno";
	}

	protected void authoringTest() {
		assertEquals("Notebook", selenium.getTitle());

		selenium.type("title", "leave your comment2222");
		selenium.runScript("FCKeditorAPI.GetInstance(\"instructions\").SetHTML(\"invent a new way of using Flash\")");
		selenium.click("tab-middle-link-2");
		selenium.click("lockOnFinished");
		selenium.click("tab-middle-link-3");
		selenium.type("onlineInstruction__lamstextarea", "online instructions");
	}

	protected void learningTest() throws InterruptedException {
		setUpLearning();
		assertEquals("Notebook", selenium.getTitle());
		assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));
		selenium.type("entryText", "have fun");
		selenium.click("//a[@id='finishButton']/span");
		selenium.waitForPageToLoad("30000");
		// assertTrue(selenium.isTextPresent("Congratulations"));
		assertFalse(selenium.isElementPresent("entryText"));
		tearDownLearning();

		setUpLearning();
		// assertTrue(selenium.isTextPresent("Congratulations"));
		assertFalse(selenium.isElementPresent("entryText"));
		tearDownLearning();
	 }

	protected void monitoringTest() {
		assertEquals("Notebook", selenium.getTitle());
	}

}
