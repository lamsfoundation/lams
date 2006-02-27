package org.lamsfoundation.lams.sysadmin.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author fmalikoff
 * 
 * @struts:form name="CacheActionForm"
 */
public class CacheActionForm extends ActionForm {

	public static final String formName = "CacheActionForm"; // must match name in @struts:action section above

	private String node;

	public CacheActionForm() {
	}


	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setNode(null);
	}


	public String getNode() {
		return node;
	}


	public void setNode(String node) {
		this.node = node;
	}

}