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

package org.lamsfoundation.lams.tool.wookie.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.wookie.dto.WookieDTO;
import org.lamsfoundation.lams.tool.wookie.dto.WookieSessionDTO;
import org.lamsfoundation.lams.tool.wookie.dto.WookieUserDTO;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieUser;
import org.lamsfoundation.lams.tool.wookie.service.IWookieService;
import org.lamsfoundation.lams.tool.wookie.service.WookieServiceProxy;
import org.lamsfoundation.lams.tool.wookie.util.WookieConstants;
import org.lamsfoundation.lams.tool.wookie.web.forms.MonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="wookie_display"
 *                        path="tiles:/monitoring/wookie_display"
 * 
 */
public class MonitoringAction extends LamsDispatchAction {

    public IWookieService wookieService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Wookie wookie = wookieService.getWookieByContentId(toolContentID);

	if (wookie == null) {
	    // TODO error page.
	}

	WookieDTO wookieDT0 = new WookieDTO(wookie);
	
	for (WookieSessionDTO sessionDTO : wookieDT0.getSessionDTOs()) {
	    Long toolSessionID = sessionDTO.getSessionID();
	   
	    for (WookieUserDTO userDTO : sessionDTO.getUserDTOs()) {
		// get the notebook entry.
		NotebookEntry notebookEntry = wookieService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			WookieConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());
		if (notebookEntry != null) {
		    userDTO.notebookEntry = notebookEntry.getEntry();
		    userDTO.setFinishedReflection(true);
		}
		
	    }
	}

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	wookieDT0.setCurrentTab(currentTab);

	request.setAttribute("wookieDTO", wookieDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("toolContentID", toolContentID);
	request.setAttribute("wookieImageFolderURL", WookieConstants.LAMS_WWW_PIXLR_FOLDER_URL);
	return mapping.findForward("success");
    }

    public ActionForward showWookie(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long uid = new Long(WebUtil.readLongParam(request, "userUID"));

	WookieUser user = wookieService.getUserByUID(uid);

	WookieUserDTO userDTO = new WookieUserDTO(user);

	request.setAttribute("userDTO", userDTO);

	return mapping.findForward("wookie_display");
    }

    /**
     * set up wookieService
     */
    private void setupService() {
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}
    }
    
    /**
     * Opens a user's reflection
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	//MonitoringForm monitorForm = (MonitoringForm) form;
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionID", false);
	Long userID = WebUtil.readLongParam(request, "userID", false);

	WookieUser wookieUser = wookieService.getUserByUserIdAndSessionId(userID, toolSessionId);

	NotebookEntry notebookEntry = wookieService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		WookieConstants.TOOL_SIGNATURE, userID.intValue());

	WookieUserDTO wookieUserDTO = new WookieUserDTO(wookieUser);
	wookieUserDTO.setNotebookEntry(notebookEntry.getEntry());

	request.setAttribute("wookieUserDTO", wookieUserDTO);

	return mapping.findForward("notebook");
    }
    
    /**
     * Opens a user's reflection
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward toggleHideImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	MonitoringForm monitorForm = (MonitoringForm) form;
	WookieUser wookieUser = wookieService.getUserByUID(monitorForm.getHideUserImageUid());
	
	if (wookieUser.isImageHidden())
	{
	    wookieUser.setImageHidden(false);
	}
	else
	{
	    wookieUser.setImageHidden(true);
	}
	wookieService.saveOrUpdateWookieUser(wookieUser);
	return unspecified(mapping, monitorForm, request, response);
    }
}
