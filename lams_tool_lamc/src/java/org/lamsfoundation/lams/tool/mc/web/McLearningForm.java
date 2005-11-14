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
}
