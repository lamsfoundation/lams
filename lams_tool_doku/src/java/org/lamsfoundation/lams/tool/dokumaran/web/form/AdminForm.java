package org.lamsfoundation.lams.tool.dokumaran.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 */
public class AdminForm{
    private static final long serialVersionUID = 414425664356226L;

    private String etherpadUrl;
    private String apiKey;

    
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
