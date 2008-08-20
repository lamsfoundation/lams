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

package org.lamsfoundation.lams.tool.dimdim.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dimdim.dto.ContentDTO;
import org.lamsfoundation.lams.tool.dimdim.dto.UserDTO;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;
import org.lamsfoundation.lams.tool.dimdim.service.DimdimServiceProxy;
import org.lamsfoundation.lams.tool.dimdim.service.IDimdimService;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="dimdim_display"
 *                        path="tiles:/monitoring/dimdim_display"
 * 
 */
public class MonitoringAction extends DispatchAction {

	// private static final Logger logger =
	// Logger.getLogger(MonitoringAction.class);

	private IDimdimService dimdimService;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// set up dimdimService
		dimdimService = DimdimServiceProxy.getDimdimService(this.getServlet()
				.getServletContext());

		return super.execute(mapping, form, request, response);
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));

		String contentFolderID = WebUtil.readStrParam(request,
				AttributeNames.PARAM_CONTENT_FOLDER_ID);

		Dimdim dimdim = dimdimService.getDimdimByContentId(toolContentID);

		if (dimdim == null) {
			// TODO error page.
		}

		ContentDTO contentDT0 = new ContentDTO(dimdim);

		Long currentTab = WebUtil.readLongParam(request,
				AttributeNames.PARAM_CURRENT_TAB, true);
		contentDT0.setCurrentTab(currentTab);

		request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDT0);
		request.setAttribute(Constants.ATTR_CONTENT_FOLDER_ID, contentFolderID);
		return mapping.findForward("success");
	}

	public ActionForward showDimdim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long uid = new Long(WebUtil.readLongParam(request,
				Constants.PARAM_USER_UID));

		DimdimUser user = dimdimService.getUserByUID(uid);
		NotebookEntry entry = dimdimService.getEntry(user.getEntryUID());

		UserDTO userDTO = new UserDTO(user, entry);

		request.setAttribute(Constants.ATTR_USER_DTO, userDTO);

		return mapping.findForward("dimdim_display");
	}
}
