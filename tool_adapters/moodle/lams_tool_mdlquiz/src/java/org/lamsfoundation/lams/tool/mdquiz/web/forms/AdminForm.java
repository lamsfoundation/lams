package org.lamsfoundation.lams.tool.mdquiz.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @struts.form name="mdquiz10AdminForm"
 */
public class AdminForm extends ActionForm {
    private static final long serialVersionUID = 8872637862875198L;
    
    String[] mappableServers;
    String[] mappedServers;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));
	return ac;
    }

    public String[] getMappableServers() {
        return mappableServers;
    }

    public void setMappableServers(String[] mappableServers) {
        this.mappableServers = mappableServers;
    }

    public String[] getMappedServers() {
        return mappedServers;
    }

    public void setMappedServers(String[] mappedServers) {
        this.mappedServers = mappedServers;
    }

}
