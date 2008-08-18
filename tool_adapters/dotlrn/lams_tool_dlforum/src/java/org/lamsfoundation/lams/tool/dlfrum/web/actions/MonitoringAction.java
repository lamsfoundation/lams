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

package org.lamsfoundation.lams.tool.dlfrum.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dlfrum.dto.DotLRNForumDTO;
import org.lamsfoundation.lams.tool.dlfrum.dto.DotLRNForumUserDTO;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForum;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumUser;
import org.lamsfoundation.lams.tool.dlfrum.service.IDotLRNForumService;
import org.lamsfoundation.lams.tool.dlfrum.service.DotLRNForumServiceProxy;
import org.lamsfoundation.lams.tool.dlfrum.util.DotLRNForumConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request" name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * 
 */
public class MonitoringAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(MonitoringAction.class);
	
	private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/" + DotLRNForumConstants.TOOL_SIGNATURE + "/";


	public IDotLRNForumService dotLRNForumService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		setupService();

		Long toolContentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		String contentFolderID = WebUtil.readStrParam(request,AttributeNames.PARAM_CONTENT_FOLDER_ID);
				
		DotLRNForum dotLRNForum = dotLRNForumService.getDotLRNForumByContentId(toolContentID);

		
		if (dotLRNForum == null) {
			// TODO error page.
		}

		DotLRNForumDTO dotLRNForumDT0 = new DotLRNForumDTO(dotLRNForum);

		Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB,true);
		dotLRNForumDT0.setCurrentTab(currentTab);

		request.setAttribute("dotLRNForumDTO", dotLRNForumDT0);
		request.setAttribute("contentFolderID", contentFolderID);
		return mapping.findForward("success");
	}
	
	/**
	 * set up dotLRNForumService
	 */
	private void setupService() {
		if (dotLRNForumService == null) {
			dotLRNForumService = DotLRNForumServiceProxy.getDotLRNForumService(this
					.getServlet().getServletContext());
		}
	}
}
