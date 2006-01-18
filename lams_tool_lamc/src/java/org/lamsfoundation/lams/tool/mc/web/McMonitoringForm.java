package org.lamsfoundation.lams.tool.mc.web;


import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Monitoring environment
 */
public class McMonitoringForm extends McAuthoringForm implements McAppConstants {
	// controls which method is called by the Lookup map */
	protected String method;
	
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
