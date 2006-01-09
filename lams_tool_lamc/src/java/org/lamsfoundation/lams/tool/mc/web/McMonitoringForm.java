package org.lamsfoundation.lams.tool.mc.web;


import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Monitoring environment
 */
public class McMonitoringForm extends ActionForm implements McAppConstants {
	// dsp controls which method is called by the Lookup map */
	protected String dsp;
	
	protected String selectedToolSessionId;
	
	protected String isToolSessionChanged;
	
	/**
	 * @return Returns the isToolSessionChanged.
	 */
	public String getIsToolSessionChanged() {
		return isToolSessionChanged;
	}
	/**
	 * @param isToolSessionChanged The isToolSessionChanged to set.
	 */
	public void setIsToolSessionChanged(String isToolSessionChanged) {
		this.isToolSessionChanged = isToolSessionChanged;
	}
	/**
	 * @return Returns the selectedToolSessionId.
	 */
	public String getSelectedToolSessionId() {
		return selectedToolSessionId;
	}
	/**
	 * @param selectedToolSessionId The selectedToolSessionId to set.
	 */
	public void setSelectedToolSessionId(String selectedToolSessionId) {
		this.selectedToolSessionId = selectedToolSessionId;
	}
	
	/**
	 * @return Returns the dsp.
	 */
	public String getDsp() {
		return dsp;
	}
	/**
	 * @param dsp The dsp to set.
	 */
	public void setDsp(String dsp) {
		this.dsp = dsp;
	}
}
