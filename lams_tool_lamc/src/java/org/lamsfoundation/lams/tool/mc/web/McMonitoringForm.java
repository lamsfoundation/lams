package org.lamsfoundation.lams.tool.mc.web;


import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Monitoring environment
 */
public class McMonitoringForm extends ActionForm implements McAppConstants {
	// dispatch controls which method is called by the Lookup map */
	protected String dispatch;
	
	protected String isToolSessionChanged;
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
}
