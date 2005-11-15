package org.lamsfoundation.lams.tool.mc.web;


import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Learning environment
 */
public class McLearningForm extends ActionForm implements McAppConstants {
	protected String continueOptions;
	protected String optionCheckBoxSelected;
	protected String questionIndex;
	protected String optionIndex;
	protected String checked;
	
	

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
}
