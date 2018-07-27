package org.lamsfoundation.lams.tool.dokumaran.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranServiceProxy;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.web.form.AdminForm;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author Andrey Balan
 */
public class AdminAction extends LamsDispatchAction {
    public IDokumaranService dokumaranService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlForumService
	if (dokumaranService == null) {
	    dokumaranService = DokumaranServiceProxy.getDokumaranService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;
	
	DokumaranConfigItem etherpadUrl = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	if (etherpadUrl != null) {
	    adminForm.setEtherpadUrl(etherpadUrl.getConfigValue());
	}
	
	DokumaranConfigItem apiKey = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_API_KEY);
	if (apiKey != null) {
	    adminForm.setApiKey(apiKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	if (adminForm.getApiKey() != null && !adminForm.getApiKey().equals("")) {
	    // set up mdlForumService
	    if (dokumaranService == null) {
		dokumaranService = DokumaranServiceProxy.getDokumaranService(this.getServlet().getServletContext());
	    }

	    DokumaranConfigItem etherpadUrl = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	    etherpadUrl.setConfigValue(adminForm.getEtherpadUrl());
	    dokumaranService.saveOrUpdateDokumaranConfigItem(etherpadUrl);
	    
	    DokumaranConfigItem apiKey = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_API_KEY);
	    apiKey.setConfigValue(adminForm.getApiKey());
	    dokumaranService.saveOrUpdateDokumaranConfigItem(apiKey);

	    request.setAttribute("savedSuccess", true);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
