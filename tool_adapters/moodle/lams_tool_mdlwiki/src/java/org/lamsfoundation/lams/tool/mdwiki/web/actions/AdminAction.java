/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mdwiki.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWikiConfigItem;
import org.lamsfoundation.lams.tool.mdwiki.service.IMdlWikiService;
import org.lamsfoundation.lams.tool.mdwiki.service.MdlWikiServiceProxy;
import org.lamsfoundation.lams.tool.mdwiki.util.MdlWikiConstants;
import org.lamsfoundation.lams.tool.mdwiki.web.forms.AdminForm;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author
 * @version
 * 
 * @struts.action path="/mdwiki10admin" parameter="dispatch" scope="request"
 *                name="mdwiki10AdminForm" validate="false"
 * 
 * @struts.action-forward name="config" path="/pages/admin/config.jsp"
 */
public class AdminAction extends LamsDispatchAction {
    public IMdlWikiService mdlWikiService;
    public ILamsToolService toolService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// set up mdlWikiService
	if (mdlWikiService == null) {
	    mdlWikiService = MdlWikiServiceProxy.getMdlWikiService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	if (toolService == null) {
	    toolService = mdlWikiService.getToolService();
	}
	Tool tool = toolService.getPersistToolBySignature(MdlWikiConstants.TOOL_SIGNATURE);

	MdlWikiConfigItem toolAdpServlet = mdlWikiService.getConfigItem(MdlWikiConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
	if (toolAdpServlet != null)
	    adminForm.setToolAdapterServlet(toolAdpServlet.getConfigValue());

	MdlWikiConfigItem extServerConfig = mdlWikiService.getConfigItem(MdlWikiConfigItem.KEY_EXTERNAL_SERVER_URL);
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
	    // set up mdlWikiService
	    if (mdlWikiService == null) {
		mdlWikiService = MdlWikiServiceProxy.getMdlWikiService(this.getServlet().getServletContext());
	    }

	    MdlWikiConfigItem servletConfig = mdlWikiService.getConfigItem(MdlWikiConfigItem.KEY_EXTERNAL_TOOL_SERVLET);
	    servletConfig.setConfigValue(adminForm.getToolAdapterServlet());
	    mdlWikiService.saveOrUpdateMdlWikiConfigItem(servletConfig);

	    MdlWikiConfigItem extServerConfig = mdlWikiService.getConfigItem(MdlWikiConfigItem.KEY_EXTERNAL_SERVER_URL);
	    extServerConfig.setConfigValue(adminForm.getExtServerUrl());
	    mdlWikiService.saveOrUpdateMdlWikiConfigItem(extServerConfig);

	    if (toolService == null) {
		toolService = mdlWikiService.getToolService();
	    }
	    Tool tool = toolService.getPersistToolBySignature(MdlWikiConstants.TOOL_SIGNATURE);
	    tool.setExtLmsId(adminForm.getServerIdMapping());
	    toolService.saveOrUpdateTool(tool);
	    return mapping.findForward("config");
	} else {
	    request.setAttribute("error", true);
	    return mapping.findForward("config");
	}
    }
}
