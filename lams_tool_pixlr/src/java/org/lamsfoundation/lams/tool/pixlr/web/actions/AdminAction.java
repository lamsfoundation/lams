package org.lamsfoundation.lams.tool.pixlr.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.service.PixlrServiceProxy;
import org.lamsfoundation.lams.tool.pixlr.web.forms.AdminForm;
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
    public IPixlrService pixlrService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlForumService
	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	PixlrConfigItem pixlrKey = pixlrService.getConfigItem(PixlrConfigItem.KEY_LANGUAGE_CSV);
	if (pixlrKey != null) {
	    adminForm.setLanguagesCSV(pixlrKey.getConfigValue());
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	if (adminForm.getLanguagesCSV() != null && !adminForm.getLanguagesCSV().equals("")) {
	    // set up mdlForumService
	    if (pixlrService == null) {
		pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	    }

	    PixlrConfigItem pixlrKey = pixlrService.getConfigItem(PixlrConfigItem.KEY_LANGUAGE_CSV);
	    pixlrKey.setConfigValue(adminForm.getLanguagesCSV());
	    pixlrService.saveOrUpdatePixlrConfigItem(pixlrKey);

	    request.setAttribute("savedSuccess", true);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
