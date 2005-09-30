/*
 * ozgurd
 * Created on 26/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.web;

/**
 * ActionForm for the Learning environment
 */
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QaLearningForm extends ActionForm implements QaAppConstants {
	protected String answer;
	protected String currentQuestionIndex;
	/** form controllers */
	protected String submitAnswersContent;
	protected String getNextQuestion;
	protected String getPreviousQuestion;
	protected String endLearning;
	
	
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
	}
