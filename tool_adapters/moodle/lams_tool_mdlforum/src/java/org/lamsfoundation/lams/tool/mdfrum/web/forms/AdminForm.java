package org.lamsfoundation.lams.tool.mdfrum.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.form name="mdfrum10AdminForm"
 */
public class AdminForm extends ActionForm
{
	private static final long serialVersionUID = 8872637862875198L;
	
	String toolAdapterServlet;
	String extServerUrl;
	String serverIdMapping;
	
	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors ac = new ActionErrors();
		ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));
		return ac;
	}

	public String getToolAdapterServlet() {
		return toolAdapterServlet;
	}

	public void setToolAdapterServlet(String toolAdapterServlet) {
		this.toolAdapterServlet = toolAdapterServlet;
	}

	public String getServerIdMapping() {
		return serverIdMapping;
	}

	public void setServerIdMapping(String serverIdMapping) {
		this.serverIdMapping = serverIdMapping;
	}

	public String getExtServerUrl() {
		return extServerUrl;
	}

	public void setExtServerUrl(String extServerUrl) {
		this.extServerUrl = extServerUrl;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
