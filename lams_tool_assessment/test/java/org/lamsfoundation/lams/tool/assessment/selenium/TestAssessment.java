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
package org.lamsfoundation.lams.tool.assessment.selenium;

import junit.framework.TestSuite;

import org.lamsfoundation.lams.selenium.AbstractSeleniumTestCase;
import org.lamsfoundation.lams.selenium.SeleniumTestSuite;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;

public class TestAssessment extends AbstractSeleniumTestCase {

    protected String getToolSignature() {
	return AssessmentConstants.TOOL_SIGNATURE;
    }

    protected String getLearningDesignName() {
	return "assessmentx";
    }
    
    public static TestSuite suite() {
	String[] testSequence = {"testAuthoring", "testCreateNewLesson", "testLearning", "testMonitoring"};
	return new SeleniumTestSuite(TestAssessment.class, testSequence);
    }

    public void testAuthoring() throws Exception {
	loginToLams();
	setUpAuthoring();
	
	assertEquals("Assessment Tool Authoring", selenium.getTitle());
	verifyTrue(selenium.isTextPresent("Assessment Tool"));
	verifyEquals("Basic", selenium.getText("tab-middle-link-1"));

	assertEquals("Title", selenium.getText("//div[@id='tabbody1']/table/tbody/tr[1]/td/div"));
	verifyEquals("Assessment", selenium.getValue("assessment.title"));
	verifyTrue(selenium.isElementPresent("//span[@class='okIcon']"));
	verifyTrue(selenium.isElementPresent("//span[@class='cancelIcon']"));
	verifyEquals("Question List", selenium.getText("//div[@id='questionList']/h2"));
	selenium.type("assessment.title", "Only for clever");
	selenium.runScript("FCKeditorAPI.GetInstance(\"assessment.instructions\").SetHTML(\"Show your best in mathematics\")");

	selenium.click("tab-middle-link-2");
	assertTrue(selenium.isElementPresent("timeLimit"));
	selenium.click("allowOverallFeedbackAfterQuestion");
	selenium.click("allowQuestionFeedback");
	selenium.click("allowRightWrongAnswersAfterQuestion");
	selenium.click("allowGradesAfterAttempt");
	selenium.click("allowHistoryResponsesAfterAttempt");

	selenium.click("tab-middle-link-3");
	verifyTrue(selenium.isTextPresent("Save"));
	selenium.type("assessment.onlineInstructions__textarea", "online instructions");

	selenium.click("tab-middle-link-1");
	createMultipleChoice();
	verifyEquals("Easy one", selenium.getText("//table[@id='questionTable']/tbody/tr[2]/td[2]"));

	createTrueFalse();
	verifyEquals("Tasty question", selenium.getText("//table[@id='questionTable']/tbody/tr[3]/td[2]"));
	
	storeLearningDesign();
    }

    private void createMultipleChoice() throws InterruptedException {
	selenium.click("newQuestionInitHref");
	selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("title");

	assertTrue(selenium.isTextPresent("Multiple choice"));
	selenium.click("link=Add Multiple Choice");
	verifyTrue(selenium.isTextPresent("You have 3 errors in a form. They have been highlighted."));
	verifyTrue(selenium.isTextPresent("This field is required."));
	verifyTrue(selenium.isTextPresent("You should provide at least one possible answer."));
	verifyTrue(selenium.isTextPresent("One of the answers should have a grade of 100% so it is possible to get full marks for this question."));

	selenium.type("title", "Easy one");
	selenium.runScript("FCKeditorAPI.GetInstance(\"question\").SetHTML(\"How much is 2+2?\")");
	selenium.runScript("FCKeditorAPI.GetInstance(\"generalFeedback\").SetHTML(\"You should better know this\")");
	verifyEquals("1", selenium.getValue("defaultGrade"));
	verifyEquals("0.1", selenium.getValue("penaltyFactor"));
	selenium.click("link=Add Multiple Choice");
	verifyTrue(selenium.isTextPresent("You have 2 errors in a form. They have been highlighted."));
	// verifyFalse(selenium.isTextPresent("This field is required."));

	selenium.runScript("FCKeditorAPI.GetInstance(\"optionString0\").SetHTML(\"4\")");
	selenium.select("optionGrade0", "label=100 %");
	selenium.type("optionFeedback0__lamstextarea", "good boy");
	selenium.runScript("FCKeditorAPI.GetInstance(\"optionString1\").SetHTML(\"2\")");
	selenium.select("optionGrade1", "label=None");
	selenium.type("optionFeedback1__lamstextarea", "bad girl");
	selenium.select("optionGrade2", "label=20 %");
	selenium.runScript("FCKeditorAPI.GetInstance(\"optionString2\").SetHTML(\"5\")");
	selenium.type("optionFeedback2__lamstextarea", "Tt could be so, on Mars");
	selenium.click("link=Add Multiple Choice");

	// TODO may be get rid of this thing
	selenium.selectWindow("openToolId");
	waitForElementPresent("link=Save");
    }

    private void createTrueFalse() throws InterruptedException {
	selenium.select("questionType", "label=True/False");
	selenium.click("newQuestionInitHref");
	selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("title");

	assertTrue(selenium.isTextPresent("True/False Question"));
	selenium.click("link=Add True/False");
	verifyTrue(selenium.isTextPresent("You have 1 error in a form. It has been highlighted."));

	selenium.type("title", "Tasty question");
	selenium.runScript("FCKeditorAPI.GetInstance(\"question\").SetHTML(\"<div>apple + apple = apple pie.  Is this correct?</div>\")");
	selenium.runScript("FCKeditorAPI.GetInstance(\"generalFeedback\").SetHTML(\"You're ready to become a cook if you know this\")");
	verifyEquals("1", selenium.getValue("defaultGrade"));
	verifyEquals("0.1", selenium.getValue("penaltyFactor"));

	selenium.runScript("FCKeditorAPI.GetInstance(\"feedbackOnCorrect\").SetHTML(\"Unfortunately, this is not true\")");
	selenium.runScript("FCKeditorAPI.GetInstance(\"feedbackOnIncorrect\").SetHTML(\"Correct!!\")");
	selenium.click("link=Add True/False");

	selenium.selectWindow("openToolId");
	waitForElementPresent("link=Save");
    }
    
    public void testCreateNewLesson() throws Exception {
	createNewLesson();	
    }

    public void testLearning() throws InterruptedException {
	setUpLearning();
	// assertEquals("Assessment Learning", selenium.getTitle());
	assertTrue(selenium.isTextPresent("Only for clever"));
	verifyFalse(selenium.isTextPresent("Started on"));
	verifyTrue(selenium.isElementPresent("question0"));
	selenium.click("//input[@name='question0' and @value='1']");
	selenium.click("question1");

	selenium.click("submitAll");
	selenium.waitForPageToLoad("30000");
	assertEquals("Started on", selenium.getText("//div[@id='content']/table/tbody/tr[1]/th"));
	verifyTrue(selenium.isTextPresent("0 out of a maximum of 2"));
	verifyTrue(selenium.isTextPresent("bad girl"));
	verifyTrue(selenium.isTextPresent("You should better know this"));
	verifyTrue(selenium.isElementPresent("//form[@id='answers']/table/tbody/tr[2]/td[2]/table/tbody/tr[1]/td[1]/img"));
	verifyTrue(selenium.isElementPresent("//form[@id='answers']/table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[1]/img"));
	verifyTrue(selenium.isTextPresent("Unfortunately, this is not true"));
	verifyTrue(selenium.isTextPresent("You're ready to become a cook if you know this"));

	selenium.click("link=Next Activity");
	selenium.waitForPageToLoad("30000");
	assertTrue(selenium.isTextPresent("Congratulations, you have finished."));
	verifyFalse(selenium.isElementPresent("Only for clever"));
	tearDownLearning();

	setUpLearning();
	assertTrue(selenium.isTextPresent("Congratulations, you have finished."));
	assertFalse(selenium.isTextPresent("Only for clever"));
	tearDownLearning();
    }

    public void testMonitoring() throws InterruptedException {
	setUpMonitoring();
	
	assertTrue(selenium.isTextPresent("Assessment Tool"));
	verifyTrue(selenium.isTextPresent("Summary"));
	verifyEquals("1", selenium.getText("//tr[@id='1']/td[1]"));
	verifyEquals("Morgan, Mary", selenium.getText("//tr[@id='1']/td[4]"));
	verifyEquals("0", selenium.getText("//tr[@id='1']/td[5]"));
	verifyEquals("0", selenium.getText("//tr[@id='1']/td[6]"));
	verifyEquals("0.00", selenium.getText("//tr[@id='1']/td[7]"));
	// verifyFalse(selenium.isElementPresent("//div[4]/table/tbody/tr[2]/td[1]"));

	selenium.click("//tr[@id='1']/td[4]");
	waitForElementPresent("//div[4]/table/tbody/tr[2]/td[1]");
	// verifyEquals("1", selenium.getText("//div[4]/table/tbody/tr[2]/td[1]"));
	verifyEquals("Easy one", selenium.getText("//div[4]/table/tbody/tr[2]/td[3]"));
	verifyEquals("2", selenium.getText("//tr[@id='1']/td[4]/div"));
	verifyEquals("0", selenium.getText("//div[4]/table/tbody/tr[2]/td[5]"));
	verifyEquals("2", selenium.getText("//tr[@id='2']/td[1]"));
	verifyEquals("Tasty question", selenium.getText("//tr[@id='2']/td[3]"));
	verifyEquals("true", selenium.getText("//tr[@id='2']/td[4]"));
	verifyEquals("0", selenium.getText("//tr[@id='2']/td[5]"));

	selenium.doubleClick("//tr[@id='1']/td[4]");
	selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("link=Ok");
	assertTrue(selenium.isTextPresent("User's history of responses"));
	verifyEquals("1", selenium.getText("//div[@id='content']/table/tbody/tr[2]/td"));
	assertTrue(selenium.isTextPresent("How much is 2+2?"));
	selenium.click("link=Ok");
	selenium.selectWindow("monitorId");
	waitForElementPresent("link=Export summary");

	selenium.select("questionUid", "label=Easy one");
	selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("link=Ok");
	assertEquals("1", selenium.getText("//div[@id='content']/table/tbody/tr[3]/td"));
	assertEquals("0.1", selenium.getText("//div[@id='content']/table/tbody/tr[4]/td"));
	assertEquals("Morgan, Mary", selenium.getText("//tr[@id='1']/td[2]"));
	assertEquals("2", selenium.getText("//tr[@id='1']/td[3]/div"));
	assertEquals("0", selenium.getText("//tr[@id='1']/td[4]"));
	selenium.click("//tr[@id='1']/td[4]");
	selenium.type("1_grade", "12");
	selenium.click("link=Ok");
	selenium.selectWindow("monitorId");
	waitForElementPresent("link=Export summary");

	tearDownMonitoring();
    }

}
