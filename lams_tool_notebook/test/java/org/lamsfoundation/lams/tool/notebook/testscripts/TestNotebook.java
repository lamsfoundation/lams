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

import org.lamsfoundation.lams.tool.notebook.core.SeleniumBaseTestCase;

public class TestNotebook extends SeleniumBaseTestCase{
    
    public static final String TOOL_SIGNATURE = "lantbk11";
    public static final String LEARNING_DESIGN_NAME = "bueno";
    
    public void testAuthoring() throws Exception {
	openToolAuthoringWindow(TOOL_SIGNATURE);
	
	assertEquals("Notebook", selenium.getTitle());
	
	selenium.type("title", "leave your comment");
	selenium.runScript("FCKeditorAPI.GetInstance(\"instructions\").SetHTML(\"invent a new way of using Flash\")");
	selenium.click("tab-middle-link-2");
	selenium.click("lockOnFinished");
	selenium.click("tab-middle-link-3");
	selenium.type("onlineInstruction__lamstextarea", "online instructions");
	
	storeLearningDesign(TOOL_SIGNATURE, LEARNING_DESIGN_NAME);
    }

    
//    public void testFlex() throws Exception {
//	loginToLams();
//
//	selenium.click("link=Add Lesson");
//	Thread.sleep(6000);
//    
//    DefaultSeleniumFlex flexSelenium = (DefaultSeleniumFlex)selenium;
//    Thread.sleep(2000);
//    flexSelenium.click("link=Add Lesson");
//    Thread.sleep(6000);
////	waitForFlexExists("resourceName_txi", 20, flexSelenium);
////    flexSelenium.wait();
////    flexSelenium.flexSetFlexObjID("CloudWizard");
//
//	 flexSelenium.flexType("resourceName_txi", "bueno");
//	 flexSelenium.flexClick("startButton");
//        
////	 flexSelenium.flexSelect(target)
//  }
    
//
//    public void testLearning() throws Exception {
//	loginToLams();
//	
//	selenium.click("link=" + LEARNING_DESIGN_NAME);
//	selenium.waitForPopUp("lWindow", "30000");
//	selenium.setSpeed("3000");
//	selenium.selectWindow("lWindow");
//	selenium.setSpeed("1000");
//
//	assertEquals("LAMS Learner", selenium.getTitle());
//	assertEquals("LAMS Learner", selenium.isElementPresent("//a[@id='finishButton']"));
//	
//	selenium.type("entryText", "have fun");
//	selenium.click("//a[@id='finishButton']/span");
//	selenium.waitForPageToLoad("30000");
////	assertTrue(selenium.isTextPresent("Congratulations"));
//	assertFalse(selenium.isElementPresent("entryText"));
//	selenium.close();
//	selenium.selectWindow(null);
//	
//	selenium.click("link=" + LEARNING_DESIGN_NAME);
//	selenium.waitForPopUp("lWindow", "30000");
//	selenium.setSpeed("3000");
//	selenium.selectWindow("lWindow");
//	selenium.setSpeed("1000");
////	assertTrue(selenium.isTextPresent("Congratulations"));
//	assertFalse(selenium.isElementPresent("entryText"));
//	selenium.close();
//	selenium.selectWindow(null);
//    }
    
//    public void testMonitoring() throws Exception {
//
//	openToolMonitor(TOOL_SIGNATURE, LEARNING_DESIGN_NAME);
//	
//	assertEquals("Notebook", selenium.getTitle());
//	
//	closeToolMonitor();
//    }
    
    

//    public void verifyFlexAppSumIsCorrect() {
//            flexUITester.type("2").at("arg1");
//            flexUITester.type("3").at("arg2");
//            flexUITester.click("submit");
//            assertEquals("5", flexUITester.readFrom("result"));
//            
//            assertEquals("Clicking Colors", selenium.getTitle());
//
//            assertEquals("(Click here)", flashApp.call("getSquareLabel"));
//            flashApp.call("click");
//    }

}
