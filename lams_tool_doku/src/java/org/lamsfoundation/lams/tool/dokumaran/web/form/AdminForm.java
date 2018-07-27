package org.lamsfoundation.lams.tool.dokumaran.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 */
public class AdminForm extends ActionForm {
    private static final long serialVersionUID = 414425664356226L;

    private String etherpadUrl;
    private String apiKey;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
//	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));
	return ac;
    }
    
    public String getEtherpadUrl() {
	return etherpadUrl;
    }

    public void setEtherpadUrl(String etherpadUrl) {
	this.etherpadUrl = etherpadUrl;
    }

    public String getApiKey() {
	return apiKey;
    }

    public void setApiKey(String apiKey) {
	this.apiKey = apiKey;
    }
}
