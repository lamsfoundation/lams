package org.lamsfoundation.lams.tool.mdfrum.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumConfigItem;
import org.lamsfoundation.lams.tool.mdfrum.service.IMdlForumService;
import org.lamsfoundation.lams.tool.mdfrum.service.MdlForumServiceProxy;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumConstants;
import org.lamsfoundation.lams.tool.mdfrum.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
/**
 * @author
 * @version
 * 
 * @struts.action path="/mdfrum10admin" parameter="dispatch" scope="request"
 *                name="mdfrum10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction
{
	public IMdlForumService mdlForumService;
	public ILamsToolService toolService;
	

	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
	{
		// set up mdlForumService
		if (mdlForumService == null) {
			mdlForumService = MdlForumServiceProxy.getMdlForumService(this.getServlet().getServletContext());
		}
		
		AdminForm adminForm = (AdminForm)form;
		
		if (toolService == null) {toolService = mdlForumService.getToolService();}
		Tool tool = toolService.getPersistToolBySignature(MdlForumConstants.TOOL_SIGNATURE);
		
		MdlForumConfigItem toolAdpServlet = mdlForumService.getConfigItem(MdlForumConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
		if (toolAdpServlet != null)
			adminForm.setToolAdapterServlet(toolAdpServlet.getConfigValue());
		
		MdlForumConfigItem extServerConfig = mdlForumService.getConfigItem(MdlForumConfigItem.KEY_EXTERNAL_SERVER_URL);
		if (extServerConfig != null)
			adminForm.setExtServerUrl(extServerConfig.getConfigValue());
		
		if (tool != null && tool.getExtLmsId() != null)
			adminForm.setServerIdMapping(tool.getExtLmsId());
			
		
		request.setAttribute("error", false);
		return mapping.findForward("config");
	}
	
	public ActionForward saveContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
	{
		AdminForm adminForm = (AdminForm)form;
		
		if (adminForm.getToolAdapterServlet() != null && !adminForm.getToolAdapterServlet().equals("") &&
			adminForm.getServerIdMapping() != null && !adminForm.getServerIdMapping().equals("") &&
			adminForm.getExtServerUrl() != null && !adminForm.getExtServerUrl().equals("") 
			)
		{
			// set up mdlForumService
			if (mdlForumService == null) {
				mdlForumService = MdlForumServiceProxy.getMdlForumService(this.getServlet().getServletContext());
			}			
			
			MdlForumConfigItem servletConfig = mdlForumService.getConfigItem(MdlForumConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
			servletConfig.setConfigValue(adminForm.getToolAdapterServlet());
			mdlForumService.saveOrUpdateMdlForumConfigItem(servletConfig);
			
			MdlForumConfigItem extServerConfig= mdlForumService.getConfigItem(MdlForumConfigItem.KEY_EXTERNAL_SERVER_URL);
			extServerConfig.setConfigValue(adminForm.getExtServerUrl());
			mdlForumService.saveOrUpdateMdlForumConfigItem(extServerConfig);
			
			if (toolService == null) {toolService = mdlForumService.getToolService();}
			Tool tool = toolService.getPersistToolBySignature(MdlForumConstants.TOOL_SIGNATURE);
			tool.setExtLmsId(adminForm.getServerIdMapping());
			toolService.saveOrUpdateTool(tool);
			return mapping.findForward("config");
		}
		else
		{
			request.setAttribute("error", true);
			return mapping.findForward("config");
		}
	}
}
