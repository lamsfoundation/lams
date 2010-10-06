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

    @Override
    protected String getToolSignature() {
	return AssessmentConstants.TOOL_SIGNATURE;
    }

    @Override
    protected String getLearningDesignName() {
	return "assessmentx";
    }

    public static TestSuite suite() {
	String[] testSequence = { "testAuthoring", "testCreateNewLesson", "testLearning", "testMonitoring" };
	return new SeleniumTestSuite(TestAssessment.class, testSequence);
    }

    public void testAuthoring() throws Exception {
	loginToLams();
	setUpAuthoring();

	assertEquals("Assessment Tool Authoring", AbstractSeleniumTestCase.selenium.getTitle());
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Assessment Tool"));
	verifyEquals("Basic", AbstractSeleniumTestCase.selenium.getText("tab-middle-link-1"));

	assertEquals("Title",
		AbstractSeleniumTestCase.selenium.getText("//div[@id='tabbody1']/table/tbody/tr[1]/td/div"));
	verifyEquals("Assessment", AbstractSeleniumTestCase.selenium.getValue("assessment.title"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isElementPresent("//span[@class='okIcon']"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isElementPresent("//span[@class='cancelIcon']"));
	verifyEquals("Question List", AbstractSeleniumTestCase.selenium.getText("//div[@id='questionList']/h2"));
	AbstractSeleniumTestCase.selenium.type("assessment.title", "Only for clever");
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"assessment.instructions\").setData(\"Show your best in mathematics\")");

	AbstractSeleniumTestCase.selenium.click("tab-middle-link-2");
	assertTrue(AbstractSeleniumTestCase.selenium.isElementPresent("timeLimit"));
	AbstractSeleniumTestCase.selenium.click("allowOverallFeedbackAfterQuestion");
	AbstractSeleniumTestCase.selenium.click("allowQuestionFeedback");
	AbstractSeleniumTestCase.selenium.click("allowRightWrongAnswersAfterQuestion");
	AbstractSeleniumTestCase.selenium.click("allowGradesAfterAttempt");
	AbstractSeleniumTestCase.selenium.click("allowHistoryResponsesAfterAttempt");

	AbstractSeleniumTestCase.selenium.click("tab-middle-link-3");
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Save"));
	AbstractSeleniumTestCase.selenium.type("assessment.onlineInstructions__textarea", "online instructions");

	AbstractSeleniumTestCase.selenium.click("tab-middle-link-1");
	createMultipleChoice();
	verifyEquals("Easy one",
		AbstractSeleniumTestCase.selenium.getText("//table[@id='questionTable']/tbody/tr[2]/td[2]"));

	createTrueFalse();
	verifyEquals("Tasty question",
		AbstractSeleniumTestCase.selenium.getText("//table[@id='questionTable']/tbody/tr[3]/td[2]"));

	storeLearningDesign();
    }

    private void createMultipleChoice() throws InterruptedException {
	AbstractSeleniumTestCase.selenium.click("newQuestionInitHref");
	AbstractSeleniumTestCase.selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("title");

	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Multiple choice"));
	AbstractSeleniumTestCase.selenium.click("link=Add Multiple Choice");
	verifyTrue(AbstractSeleniumTestCase.selenium
		.isTextPresent("You have 3 errors in a form. They have been highlighted."));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("This field is required."));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("You should provide at least one possible answer."));
	verifyTrue(AbstractSeleniumTestCase.selenium
		.isTextPresent("One of the answers should have a grade of 100% so it is possible to get full marks for this question."));

	AbstractSeleniumTestCase.selenium.type("title", "Easy one");
	AbstractSeleniumTestCase.selenium.runScript("CKEDITOR.instances[\"question\").setData(\"How much is 2+2?\")");
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"generalFeedback\").setData(\"You should better know this\")");
	verifyEquals("1", AbstractSeleniumTestCase.selenium.getValue("defaultGrade"));
	verifyEquals("0.1", AbstractSeleniumTestCase.selenium.getValue("penaltyFactor"));
	AbstractSeleniumTestCase.selenium.click("link=Add Multiple Choice");
	verifyTrue(AbstractSeleniumTestCase.selenium
		.isTextPresent("You have 2 errors in a form. They have been highlighted."));
	// verifyFalse(selenium.isTextPresent("This field is required."));

	AbstractSeleniumTestCase.selenium.runScript("CKEDITOR.instances[\"optionString0\").setData(\"4\")");
	AbstractSeleniumTestCase.selenium.select("optionGrade0", "label=100 %");
	AbstractSeleniumTestCase.selenium.type("optionFeedback0__lamstextarea", "good boy");
	AbstractSeleniumTestCase.selenium.runScript("CKEDITOR.instances[\"optionString1\").setData(\"2\")");
	AbstractSeleniumTestCase.selenium.select("optionGrade1", "label=None");
	AbstractSeleniumTestCase.selenium.type("optionFeedback1__lamstextarea", "bad girl");
	AbstractSeleniumTestCase.selenium.select("optionGrade2", "label=20 %");
	AbstractSeleniumTestCase.selenium.runScript("CKEDITOR.instances[\"optionString2\").setData(\"5\")");
	AbstractSeleniumTestCase.selenium.type("optionFeedback2__lamstextarea", "Tt could be so, on Mars");
	AbstractSeleniumTestCase.selenium.click("link=Add Multiple Choice");

	AbstractSeleniumTestCase.selenium.selectWindow("openToolId");
	waitForElementPresent("link=Save");
    }

    private void createTrueFalse() throws InterruptedException {
	AbstractSeleniumTestCase.selenium.select("questionType", "label=True/False");
	AbstractSeleniumTestCase.selenium.click("newQuestionInitHref");
	AbstractSeleniumTestCase.selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("title");

	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("True/False Question"));
	AbstractSeleniumTestCase.selenium.click("link=Add True/False");
	verifyTrue(AbstractSeleniumTestCase.selenium
		.isTextPresent("You have 1 error in a form. It has been highlighted."));

	AbstractSeleniumTestCase.selenium.type("title", "Tasty question");
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"question\").setData(\"<div>apple + apple = apple pie.  Is this correct?</div>\")");
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"generalFeedback\").setData(\"You're ready to become a cook if you know this\")");
	verifyEquals("1", AbstractSeleniumTestCase.selenium.getValue("defaultGrade"));
	verifyEquals("0.1", AbstractSeleniumTestCase.selenium.getValue("penaltyFactor"));

	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"feedbackOnCorrect\").setData(\"Unfortunately, this is not true\")");
	AbstractSeleniumTestCase.selenium
		.runScript("CKEDITOR.instances[\"feedbackOnIncorrect\").setData(\"Correct!!\")");
	AbstractSeleniumTestCase.selenium.click("link=Add True/False");

	AbstractSeleniumTestCase.selenium.selectWindow("openToolId");
	waitForElementPresent("link=Save");
    }

    public void testCreateNewLesson() throws Exception {
	createNewLesson();
    }

    public void testLearning() throws InterruptedException {
	setUpLearning();
	assertEquals("Assessment Learning", AbstractSeleniumTestCase.selenium.getTitle());
	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Only for clever"));
	verifyFalse(AbstractSeleniumTestCase.selenium.isTextPresent("Started on"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isElementPresent("question0"));
	AbstractSeleniumTestCase.selenium.click("//input[@name='question0' and @value='1']");
	AbstractSeleniumTestCase.selenium.click("question1");

	AbstractSeleniumTestCase.selenium.click("submitAll");
	AbstractSeleniumTestCase.selenium.waitForPageToLoad("30000");
	assertEquals("Started on",
		AbstractSeleniumTestCase.selenium.getText("//div[@id='content']/table/tbody/tr[1]/th"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("0 out of a maximum of 2"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("bad girl"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("You should better know this"));
	verifyTrue(AbstractSeleniumTestCase.selenium
		.isElementPresent("//form[@id='answers']/table/tbody/tr[2]/td[2]/table/tbody/tr[1]/td[1]/img"));
	verifyTrue(AbstractSeleniumTestCase.selenium
		.isElementPresent("//form[@id='answers']/table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td[1]/img"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Unfortunately, this is not true"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("You're ready to become a cook if you know this"));

	AbstractSeleniumTestCase.selenium.click("link=Next Activity");
	waitForLearning();
	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Congratulations, you have finished."));
	verifyFalse(AbstractSeleniumTestCase.selenium.isElementPresent("Only for clever"));
	tearDownLearning();

	setUpLearning();
	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Congratulations, you have finished."));
	assertFalse(AbstractSeleniumTestCase.selenium.isTextPresent("Only for clever"));
    }

    public void testMonitoring() throws InterruptedException {
	setUpMonitoring();
	AbstractSeleniumTestCase.selenium.setSpeed("600");

	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Assessment Tool"));
	verifyTrue(AbstractSeleniumTestCase.selenium.isTextPresent("Summary"));
	verifyEquals("1", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[1]"));
	verifyEquals("Morgan, Mary", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[4]"));
	verifyEquals("0", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[5]"));
	verifyEquals("0", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[6]"));
	verifyEquals("0.00", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[7]"));
	// verifyFalse(selenium.isElementPresent("//div[4]/table/tbody/tr[2]/td[1]"));

	AbstractSeleniumTestCase.selenium.click("//tr[@id='1']/td[4]");
	waitForElementPresent("//div[4]/table/tbody/tr[2]/td[1]");
	// verifyEquals("1", selenium.getText("//div[4]/table/tbody/tr[2]/td[1]"));
	verifyEquals("Easy one", AbstractSeleniumTestCase.selenium.getText("//div[4]/table/tbody/tr[2]/td[3]"));
	verifyEquals("2", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[4]/div"));
	verifyEquals("0", AbstractSeleniumTestCase.selenium.getText("//div[4]/table/tbody/tr[2]/td[5]"));
	verifyEquals("2", AbstractSeleniumTestCase.selenium.getText("//tr[@id='2']/td[1]"));
	verifyEquals("Tasty question", AbstractSeleniumTestCase.selenium.getText("//tr[@id='2']/td[3]"));
	verifyEquals("true", AbstractSeleniumTestCase.selenium.getText("//tr[@id='2']/td[4]"));
	verifyEquals("0", AbstractSeleniumTestCase.selenium.getText("//tr[@id='2']/td[5]"));

	AbstractSeleniumTestCase.selenium.doubleClick("//tr[@id='1']/td[4]");
	AbstractSeleniumTestCase.selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("link=Ok");
	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("User's history of responses"));
	verifyEquals("1", AbstractSeleniumTestCase.selenium.getText("//div[@id='content']/table/tbody/tr[2]/td"));
	assertTrue(AbstractSeleniumTestCase.selenium.isTextPresent("How much is 2+2?"));
	AbstractSeleniumTestCase.selenium.click("link=Ok");
	AbstractSeleniumTestCase.selenium.selectWindow("monitorId");
	waitForElementPresent("link=Export summary");

	AbstractSeleniumTestCase.selenium.select("questionUid", "label=Easy one");
	AbstractSeleniumTestCase.selenium.selectFrame("TB_iframeContent");
	waitForElementPresent("link=Ok");
	assertEquals("1", AbstractSeleniumTestCase.selenium.getText("//div[@id='content']/table/tbody/tr[3]/td"));
	assertEquals("0.1", AbstractSeleniumTestCase.selenium.getText("//div[@id='content']/table/tbody/tr[4]/td"));
	assertEquals("Morgan, Mary", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[2]"));
	assertEquals("2", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[3]/div"));
	assertEquals("0", AbstractSeleniumTestCase.selenium.getText("//tr[@id='1']/td[4]"));
	AbstractSeleniumTestCase.selenium.click("//tr[@id='1']/td[4]");
	AbstractSeleniumTestCase.selenium.type("1_grade", "12");
	AbstractSeleniumTestCase.selenium.click("link=Ok");
	AbstractSeleniumTestCase.selenium.selectWindow("monitorId");
	waitForElementPresent("link=Export summary");
    }

}
