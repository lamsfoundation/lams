/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.tool.qa.web;

/** ActionForm for the Learning environment */
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;

/**
 * @author Ozgur Demirtas
 */
public class QaLearningForm extends ActionForm implements QaAppConstants {
	
	//controls which method is called by the Lookup map */
	protected String method;

	protected String answer;
	protected String currentQuestionIndex;
	protected String submitAnswersContent;
	protected String getNextQuestion;
	protected String getPreviousQuestion;
	protected String endLearning;
	
	protected String responseId;
	
	
	/**
     * reset user actions in learning mode
     * @param qaAuthoringForm
     * return void
     */
    
    protected void resetUserActions()
    {
    	this.getNextQuestion=null;
    	this.getPreviousQuestion=null;
    	this.endLearning=null;
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
}
