package org.lamsfoundation.lams.tool.mdscrm.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormConfigItem;
import org.lamsfoundation.lams.tool.mdscrm.service.IMdlScormService;
import org.lamsfoundation.lams.tool.mdscrm.service.MdlScormServiceProxy;
import org.lamsfoundation.lams.tool.mdscrm.util.MdlScormConstants;
import org.lamsfoundation.lams.tool.mdscrm.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 * 
 * @struts.action path="/mdscrm10admin" parameter="dispatch" scope="request"
 *                name="mdscrm10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IMdlScormService mdlScormService;
    public ILamsToolService toolService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlScormService
	if (mdlScormService == null) {
	    mdlScormService = MdlScormServiceProxy.getMdlScormService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	if (toolService == null) {
	    toolService = mdlScormService.getToolService();
	}
	Tool tool = toolService.getPersistToolBySignature(MdlScormConstants.TOOL_SIGNATURE);

	MdlScormConfigItem toolAdpServlet = mdlScormService.getConfigItem(MdlScormConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
	if (toolAdpServlet != null)
	    adminForm.setToolAdapterServlet(toolAdpServlet.getConfigValue());

	MdlScormConfigItem extServerConfig = mdlScormService.getConfigItem(MdlScormConfigItem.KEY_EXTERNAL_SERVER_URL);
	if (extServerConfig != null)
	    adminForm.setExtServerUrl(extServerConfig.getConfigValue());

	if (tool != null && tool.getExtLmsId() != null)
	    adminForm.setServerIdMapping(tool.getExtLmsId());

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	if (adminForm.getToolAdapterServlet() != null && !adminForm.getToolAdapterServlet().equals("")
		&& adminForm.getServerIdMapping() != null && !adminForm.getServerIdMapping().equals("")
		&& adminForm.getExtServerUrl() != null && !adminForm.getExtServerUrl().equals("")) {
	    // set up mdlScormService
	    if (mdlScormService == null) {
		mdlScormService = MdlScormServiceProxy.getMdlScormService(this.getServlet().getServletContext());
	    }

	    MdlScormConfigItem servletConfig = mdlScormService
		    .getConfigItem(MdlScormConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
	    servletConfig.setConfigValue(adminForm.getToolAdapterServlet());
	    mdlScormService.saveOrUpdateMdlScormConfigItem(servletConfig);

	    MdlScormConfigItem extServerConfig = mdlScormService
		    .getConfigItem(MdlScormConfigItem.KEY_EXTERNAL_SERVER_URL);
	    extServerConfig.setConfigValue(adminForm.getExtServerUrl());
	    mdlScormService.saveOrUpdateMdlScormConfigItem(extServerConfig);

	    if (toolService == null) {
		toolService = mdlScormService.getToolService();
	    }
	    Tool tool = toolService.getPersistToolBySignature(MdlScormConstants.TOOL_SIGNATURE);
	    tool.setExtLmsId(adminForm.getServerIdMapping());
	    toolService.saveOrUpdateTool(tool);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
