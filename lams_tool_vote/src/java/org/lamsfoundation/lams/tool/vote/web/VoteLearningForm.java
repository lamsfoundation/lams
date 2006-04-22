/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Learning environment
 */
public class VoteLearningForm extends ActionForm implements VoteAppConstants {
	protected String optionCheckBoxSelected;
	protected String questionIndex;
	protected String optionIndex;
	protected String optionValue;
	protected String checked;
	
	protected String userEntry;
	protected String dispatch;
	protected String toolContentID;
	
	protected String continueOptions;
	protected String nextOptions;
	protected String continueOptionsCombined;
	protected String redoQuestions;
	protected String viewSummary;
	protected String viewAnswers;
	protected String learnerFinished;
	protected String redoQuestionsOk;
	protected String donePreview;
	protected String doneLearnerProgress;
	protected String viewAllResults;
	
	protected String method;
	protected String answer;
	protected String currentQuestionIndex;
	protected String submitAnswersContent;
	protected String getNextQuestion;
	protected String getPreviousQuestion;
	protected String endLearning;
	
	
    protected void resetUserActions()
    {
    	this.getNextQuestion=null;
    	this.getPreviousQuestion=null;
    	this.endLearning=null;
    	this.viewAllResults=null;
    }

	
    /**
     * @return Returns the answer.
     */
    public String getAnswer() {
        return answer;
    }
    /**
     * @param answer The answer to set.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    /**
     * @return Returns the currentQuestionIndex.
     */
    public String getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    /**
     * @param currentQuestionIndex The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(String currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
    /**
     * @return Returns the endLearning.
     */
    public String getEndLearning() {
        return endLearning;
    }
    /**
     * @param endLearning The endLearning to set.
     */
    public void setEndLearning(String endLearning) {
        this.endLearning = endLearning;
    }
    /**
     * @return Returns the getNextQuestion.
     */
    public String getGetNextQuestion() {
        return getNextQuestion;
    }
    /**
     * @param getNextQuestion The getNextQuestion to set.
     */
    public void setGetNextQuestion(String getNextQuestion) {
        this.getNextQuestion = getNextQuestion;
    }
    /**
     * @return Returns the getPreviousQuestion.
     */
    public String getGetPreviousQuestion() {
        return getPreviousQuestion;
    }
    /**
     * @param getPreviousQuestion The getPreviousQuestion to set.
     */
    public void setGetPreviousQuestion(String getPreviousQuestion) {
        this.getPreviousQuestion = getPreviousQuestion;
    }
    /**
     * @return Returns the method.
     */
    public String getMethod() {
        return method;
    }
    /**
     * @param method The method to set.
     */
    public void setMethod(String method) {
        this.method = method;
    }
    /**
     * @return Returns the responseId.
     */
    public String getResponseId() {
        return responseId;
    }
    /**
     * @param responseId The responseId to set.
     */
    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
    /**
     * @return Returns the submitAnswersContent.
     */
    public String getSubmitAnswersContent() {
        return submitAnswersContent;
    }
    /**
     * @param submitAnswersContent The submitAnswersContent to set.
     */
    public void setSubmitAnswersContent(String submitAnswersContent) {
        this.submitAnswersContent = submitAnswersContent;
    }
	protected String responseId;

	
	
	
	
	public void resetCommands()
	{
		this.setContinueOptions(null);
		this.setNextOptions(null);
		this.setContinueOptionsCombined(null);
		this.setRedoQuestions( null); 
		this.setViewSummary(null);
		this.setViewAnswers(null);
		this.setRedoQuestionsOk(null);
		this.setLearnerFinished(null);
		this.setDonePreview(null);
		this.setDoneLearnerProgress(null);
	}

	public void resetParameters()
	{
		this.setOptionCheckBoxSelected(null);
		this.setQuestionIndex(null);
		this.setOptionIndex(null);
		this.setChecked(null);
		this.setOptionValue(null);
	}


	/**
	 * @return Returns the continueOptions.
	 */
	public String getContinueOptions() {
		return continueOptions;
	}
	/**
	 * @param continueOptions The continueOptions to set.
	 */
	public void setContinueOptions(String continueOptions) {
		this.continueOptions = continueOptions;
	}
	/**
	 * @return Returns the checked.
	 */
	public String getChecked() {
		return checked;
	}
	/**
	 * @param checked The checked to set.
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}
	/**
	 * @return Returns the optionCheckBoxSelected.
	 */
	public String getOptionCheckBoxSelected() {
		return optionCheckBoxSelected;
	}
	/**
	 * @param optionCheckBoxSelected The optionCheckBoxSelected to set.
	 */
	public void setOptionCheckBoxSelected(String optionCheckBoxSelected) {
		this.optionCheckBoxSelected = optionCheckBoxSelected;
	}
	/**
	 * @return Returns the optionIndex.
	 */
	public String getOptionIndex() {
		return optionIndex;
	}
	/**
	 * @param optionIndex The optionIndex to set.
	 */
	public void setOptionIndex(String optionIndex) {
		this.optionIndex = optionIndex;
	}
	/**
	 * @return Returns the questionIndex.
	 */
	public String getQuestionIndex() {
		return questionIndex;
	}
	/**
	 * @param questionIndex The questionIndex to set.
	 */
	public void setQuestionIndex(String questionIndex) {
		this.questionIndex = questionIndex;
	}
	
	/**
	 * @return Returns the viewSummary.
	 */
	public String getViewSummary() {
		return viewSummary;
	}
	/**
	 * @param viewSummary The viewSummary to set.
	 */
	public void setViewSummary(String viewSummary) {
		this.viewSummary = viewSummary;
	}
	/**
	 * @return Returns the continueOptionsCombined.
	 */
	public String getContinueOptionsCombined() {
		return continueOptionsCombined;
	}
	/**
	 * @param continueOptionsCombined The continueOptionsCombined to set.
	 */
	public void setContinueOptionsCombined(String continueOptionsCombined) {
		this.continueOptionsCombined = continueOptionsCombined;
	}
	/**
	 * @return Returns the redoQuestions.
	 */
	public String getRedoQuestions() {
		return redoQuestions;
	}
	/**
	 * @param redoQuestions The redoQuestions to set.
	 */
	public void setRedoQuestions(String redoQuestions) {
		this.redoQuestions = redoQuestions;
	}
	/**
	 * @return Returns the optionValue.
	 */
	public String getOptionValue() {
		return optionValue;
	}
	/**
	 * @param optionValue The optionValue to set.
	 */
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	/**
	 * @return Returns the viewAnswers.
	 */
	public String getViewAnswers() {
		return viewAnswers;
	}
	/**
	 * @param viewAnswers The viewAnswers to set.
	 */
	public void setViewAnswers(String viewAnswers) {
		this.viewAnswers = viewAnswers;
	}
	/**
	 * @return Returns the redoQuestionsOk.
	 */
	public String getRedoQuestionsOk() {
		return redoQuestionsOk;
	}
	/**
	 * @param redoQuestionsOk The redoQuestionsOk to set.
	 */
	public void setRedoQuestionsOk(String redoQuestionsOk) {
		this.redoQuestionsOk = redoQuestionsOk;
	}
	/**
	 * @return Returns the nextOptions.
	 */
	public String getNextOptions() {
		return nextOptions;
	}
	/**
	 * @param nextOptions The nextOptions to set.
	 */
	public void setNextOptions(String nextOptions) {
		this.nextOptions = nextOptions;
	}
	/**
	 * @return Returns the learnerFinished.
	 */
	public String getLearnerFinished() {
		return learnerFinished;
	}
	/**
	 * @param learnerFinished The learnerFinished to set.
	 */
	public void setLearnerFinished(String learnerFinished) {
		this.learnerFinished = learnerFinished;
	}
	/**
	 * @return Returns the donePreview.
	 */
	public String getDonePreview() {
		return donePreview;
	}
	/**
	 * @param donePreview The donePreview to set.
	 */
	public void setDonePreview(String donePreview) {
		this.donePreview = donePreview;
	}
	/**
	 * @return Returns the doneLearnerProgress.
	 */
	public String getDoneLearnerProgress() {
		return doneLearnerProgress;
	}
	/**
	 * @param doneLearnerProgress The doneLearnerProgress to set.
	 */
	public void setDoneLearnerProgress(String doneLearnerProgress) {
		this.doneLearnerProgress = doneLearnerProgress;
	}
	
    /**
     * @return Returns the userEntry.
     */
    public String getUserEntry() {
        return userEntry;
    }
    /**
     * @param userEntry The userEntry to set.
     */
    public void setUserEntry(String userEntry) {
        this.userEntry = userEntry;
    }
    
    /**
     * @return Returns the dispatch.
     */
    public String getDispatch() {
        return dispatch;
    }
    /**
     * @param dispatch The dispatch to set.
     */
    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * @return Returns the toolContentID.
     */
    public String getToolContentID() {
        return toolContentID;
    }
    /**
     * @param toolContentID The toolContentID to set.
     */
    public void setToolContentID(String toolContentID) {
        this.toolContentID = toolContentID;
    }

	
    /**
     * @return Returns the viewAllResults.
     */
    public String getViewAllResults() {
        return viewAllResults;
    }
    /**
     * @param viewAllResults The viewAllResults to set.
     */
    public void setViewAllResults(String viewAllResults) {
        this.viewAllResults = viewAllResults;
    }
}
