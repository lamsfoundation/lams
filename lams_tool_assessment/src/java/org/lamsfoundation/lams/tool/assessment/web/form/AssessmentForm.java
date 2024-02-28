/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.assessment.web.form;

import org.lamsfoundation.lams.tool.assessment.model.Assessment;

import javax.servlet.http.HttpServletRequest;

/**
 * Assessment Form.
 *
 * @author Andrey Balan
 */
public class AssessmentForm {
    private String sessionMapID;
    private String contentFolderID;
    private int currentTab;

    private Assessment assessment;

    private String questionDistributionType;

    public AssessmentForm() {
	assessment = new Assessment();
	assessment.setTitle("Shared Assessment");
	currentTab = 1;
	questionDistributionType = "all";
    }

    public void setAssessment(Assessment assessment) {
	this.assessment = assessment;
    }

    public void reset(HttpServletRequest request) {
	assessment.setAllowGradesAfterAttempt(false);
	assessment.setAllowOverallFeedbackAfterQuestion(false);
	assessment.setAllowQuestionFeedback(false);
	assessment.setAllowDiscloseAnswers(false);
	assessment.setAllowDiscloseAnswers(true);
	assessment.setAllowRightAnswersAfterQuestion(false);
	assessment.setAllowWrongAnswersAfterQuestion(false);
	assessment.setDefineLater(false);
	assessment.setShuffled(false);
	assessment.setShuffledAnswers(false);
	assessment.setNumbered(false);
	assessment.setDisplaySummary(false);
    }

    public int getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(int currentTab) {
	this.currentTab = currentTab;
    }

    public Assessment getAssessment() {
	return assessment;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public String getQuestionDistributionType() {
	return questionDistributionType;
    }

    public void setQuestionDistributionType(String questionDistributionType) {
	this.questionDistributionType = questionDistributionType;
    }
}