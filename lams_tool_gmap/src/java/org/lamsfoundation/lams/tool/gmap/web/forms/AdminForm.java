package org.lamsfoundation.lams.tool.gmap.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 */
public class AdminForm extends ActionForm {
    private static final long serialVersionUID = 414425664356226L;

    String gmapKey;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));
	return ac;
    }

    public String getGmapKey() {
	return gmapKey;
    }

    public void setGmapKey(String gmapKey) {
	this.gmapKey = gmapKey;
    }
}
