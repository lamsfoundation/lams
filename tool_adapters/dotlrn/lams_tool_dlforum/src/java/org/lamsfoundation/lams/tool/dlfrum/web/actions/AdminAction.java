package org.lamsfoundation.lams.tool.dlfrum.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumConfigItem;
import org.lamsfoundation.lams.tool.dlfrum.service.DotLRNForumServiceProxy;
import org.lamsfoundation.lams.tool.dlfrum.service.IDotLRNForumService;
import org.lamsfoundation.lams.tool.dlfrum.util.DotLRNForumConstants;
import org.lamsfoundation.lams.tool.dlfrum.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
/**
 * @author
 * @version
 * 
 * @struts.action path="/dlfrum10admin" parameter="dispatch" scope="request"
 *                name="dlfrum10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction
{
	public IDotLRNForumService dotLRNForumService;
	public ILamsToolService toolService;
	
	private static String KEY_TOOL_ADAPTER_SERVLET = "toolAdapterServlet";
	//private static String KEY_EXT_SERVERID_MAPPING = "extServerIdMapping";
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
	{
		// set up dotLRNForumService
		if (dotLRNForumService == null) {
			dotLRNForumService = DotLRNForumServiceProxy.getDotLRNForumService(this.getServlet().getServletContext());
		}
		
		AdminForm adminForm = (AdminForm)form;
		
		if (toolService == null) {toolService = dotLRNForumService.getToolService();}
		Tool tool = toolService.getPersistToolBySignature(DotLRNForumConstants.TOOL_SIGNATURE);
		
		DotLRNForumConfigItem toolAdpServlet = dotLRNForumService.getConfigItem(KEY_TOOL_ADAPTER_SERVLET);
		
		if (toolAdpServlet != null)
			adminForm.setToolAdapterServlet(toolAdpServlet.getConfigValue());
		
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
			adminForm.getServerIdMapping() != null && !adminForm.getServerIdMapping().equals("") )
		{
			// set up dotLRNForumService
			if (dotLRNForumService == null) {
				dotLRNForumService = DotLRNForumServiceProxy.getDotLRNForumService(this.getServlet().getServletContext());
			}			
			
			DotLRNForumConfigItem servletConfig = dotLRNForumService.getConfigItem(KEY_TOOL_ADAPTER_SERVLET);
			servletConfig.setConfigValue(adminForm.getToolAdapterServlet());
			dotLRNForumService.saveOrUpdateDotLRNForumConfigItem(servletConfig);
			
			if (toolService == null) {toolService = dotLRNForumService.getToolService();}
			Tool tool = toolService.getPersistToolBySignature(DotLRNForumConstants.TOOL_SIGNATURE);
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
