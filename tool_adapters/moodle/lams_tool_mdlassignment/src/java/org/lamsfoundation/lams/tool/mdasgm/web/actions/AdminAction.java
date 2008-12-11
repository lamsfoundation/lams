package org.lamsfoundation.lams.tool.mdasgm.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.mdasgm.model.MdlAssignmentConfigItem;
import org.lamsfoundation.lams.tool.mdasgm.service.IMdlAssignmentService;
import org.lamsfoundation.lams.tool.mdasgm.service.MdlAssignmentServiceProxy;
import org.lamsfoundation.lams.tool.mdasgm.util.MdlAssignmentConstants;
import org.lamsfoundation.lams.tool.mdasgm.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 * 
 * @struts.action path="/mdasgm10admin" parameter="dispatch" scope="request"
 *                name="mdasgm10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IMdlAssignmentService mdlAssignmentService;
    public ILamsToolService toolService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlAssignmentService
	if (mdlAssignmentService == null) {
	    mdlAssignmentService = MdlAssignmentServiceProxy.getMdlAssignmentService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	if (toolService == null) {
	    toolService = mdlAssignmentService.getToolService();
	}
	Tool tool = toolService.getPersistToolBySignature(MdlAssignmentConstants.TOOL_SIGNATURE);

	MdlAssignmentConfigItem toolAdpServlet = mdlAssignmentService.getConfigItem(MdlAssignmentConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
	if (toolAdpServlet != null)
	    adminForm.setToolAdapterServlet(toolAdpServlet.getConfigValue());

	MdlAssignmentConfigItem extServerConfig = mdlAssignmentService.getConfigItem(MdlAssignmentConfigItem.KEY_EXTERNAL_SERVER_URL);
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
	    // set up mdlAssignmentService
	    if (mdlAssignmentService == null) {
		mdlAssignmentService = MdlAssignmentServiceProxy.getMdlAssignmentService(this.getServlet().getServletContext());
	    }

	    MdlAssignmentConfigItem servletConfig = mdlAssignmentService
		    .getConfigItem(MdlAssignmentConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
	    servletConfig.setConfigValue(adminForm.getToolAdapterServlet());
	    mdlAssignmentService.saveOrUpdateMdlAssignmentConfigItem(servletConfig);

	    MdlAssignmentConfigItem extServerConfig = mdlAssignmentService
		    .getConfigItem(MdlAssignmentConfigItem.KEY_EXTERNAL_SERVER_URL);
	    extServerConfig.setConfigValue(adminForm.getExtServerUrl());
	    mdlAssignmentService.saveOrUpdateMdlAssignmentConfigItem(extServerConfig);

	    if (toolService == null) {
		toolService = mdlAssignmentService.getToolService();
	    }
	    Tool tool = toolService.getPersistToolBySignature(MdlAssignmentConstants.TOOL_SIGNATURE);
	    tool.setExtLmsId(adminForm.getServerIdMapping());
	    toolService.saveOrUpdateTool(tool);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
