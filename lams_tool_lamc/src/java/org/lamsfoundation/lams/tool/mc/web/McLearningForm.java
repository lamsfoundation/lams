package org.lamsfoundation.lams.tool.mc.web;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Learning environment
 */
public class McLearningForm extends ActionForm implements McAppConstants {
	protected String optionCheckBoxSelected;
	protected String questionIndex;
	protected String optionIndex;
	protected String optionValue;
	protected String checked;
	
	protected String continueOptions;
	protected String nextOptions;
	protected String continueOptionsCombined;
	protected String redoQuestions;
	protected String viewSummary;
	protected String viewAnswers;
	protected String learnerFinished;
	protected String redoQuestionsOk;
	
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
}
