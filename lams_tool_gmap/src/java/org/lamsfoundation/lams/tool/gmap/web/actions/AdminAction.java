package org.lamsfoundation.lams.tool.gmap.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.service.GmapServiceProxy;
import org.lamsfoundation.lams.tool.gmap.service.IGmapService;
import org.lamsfoundation.lams.tool.gmap.web.forms.AdminForm;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 */
public class AdminAction extends LamsDispatchAction {
    public IGmapService gmapService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlForumService
	if (gmapService == null) {
	    gmapService = GmapServiceProxy.getGmapService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	if (gmapKey != null) {
	    adminForm.setGmapKey(gmapKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	if (adminForm.getGmapKey() != null && !adminForm.getGmapKey().equals("")) {
	    // set up mdlForumService
	    if (gmapService == null) {
		gmapService = GmapServiceProxy.getGmapService(this.getServlet().getServletContext());
	    }

	    GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	    gmapKey.setConfigValue(adminForm.getGmapKey());
	    gmapService.saveOrUpdateGmapConfigItem(gmapKey);

	    request.setAttribute("savedSuccess", true);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
