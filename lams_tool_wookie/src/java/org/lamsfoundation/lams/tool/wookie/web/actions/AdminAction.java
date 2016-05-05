package org.lamsfoundation.lams.tool.wookie.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;
import org.lamsfoundation.lams.tool.wookie.service.IWookieService;
import org.lamsfoundation.lams.tool.wookie.service.WookieServiceProxy;
import org.lamsfoundation.lams.tool.wookie.web.forms.AdminForm;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 *
 * @struts.action path="/admin" parameter="dispatch" scope="request"
 *                name="wookieadminform" validate="false"
 *
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IWookieService wookieService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlForumService
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	WookieConfigItem wookieKey = wookieService.getConfigItem(WookieConfigItem.KEY_API);
	if (wookieKey != null) {
	    adminForm.setApiKey(wookieKey.getConfigValue());
	}

	WookieConfigItem wookieUrl = wookieService.getConfigItem(WookieConfigItem.KEY_WOOKIE_URL);
	if (wookieUrl != null) {
	    adminForm.setWookieServerUrl(wookieUrl.getConfigValue());
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	if (adminForm.getApiKey() != null && !adminForm.getApiKey().equals("") && adminForm.getWookieServerUrl() != null
		&& !adminForm.getWookieServerUrl().equals("")) {

	    if (wookieService == null) {
		wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	    }

	    WookieConfigItem wookieKey = wookieService.getConfigItem(WookieConfigItem.KEY_API);
	    wookieKey.setConfigValue(adminForm.getApiKey());
	    wookieService.saveOrUpdateWookieConfigItem(wookieKey);

	    WookieConfigItem wookieUrl = wookieService.getConfigItem(WookieConfigItem.KEY_WOOKIE_URL);
	    wookieUrl.setConfigValue(adminForm.getWookieServerUrl());
	    wookieService.saveOrUpdateWookieConfigItem(wookieUrl);

	    request.setAttribute("savedSuccess", true);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
