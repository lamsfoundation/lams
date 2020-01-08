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

package org.lamsfoundation.lams.tool.pixlr.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrDTO;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrSessionDTO;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrUserDTO;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.tool.pixlr.web.forms.MonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    @Autowired
    private IPixlrService pixlrService;

    @RequestMapping("")
    public String unspecified(@ModelAttribute("monitoringForm")MonitoringForm monitoringForm,HttpServletRequest request) {

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Pixlr pixlr = pixlrService.getPixlrByContentId(toolContentID);

	if (pixlr == null) {
	    // TODO error page.
	}

	PixlrDTO pixlrDT0 = new PixlrDTO(pixlr);

	for (PixlrSessionDTO sessionDTO : pixlrDT0.getSessionDTOs()) {
	    Long toolSessionID = sessionDTO.getSessionID();

	    for (PixlrUserDTO userDTO : sessionDTO.getUserDTOs()) {
		// get the notebook entry.
		NotebookEntry notebookEntry = pixlrService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			PixlrConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());
		if (notebookEntry != null) {
		    userDTO.notebookEntry = notebookEntry.getEntry();
		    userDTO.setFinishedReflection(true);
		}

	    }
	}

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	pixlrDT0.setCurrentTab(currentTab);

	request.setAttribute("pixlrDTO", pixlrDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("toolContentID", toolContentID);
	request.setAttribute("pixlrImageFolderURL", PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL);
	request.setAttribute("isGroupedActivity", pixlrService.isGroupedActivity(toolContentID));
	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/showPixlr")
    public String showPixlr(@ModelAttribute("monitoringForm")MonitoringForm monitoringForm,HttpServletRequest request) {

	Long uid = new Long(WebUtil.readLongParam(request, "userUID"));

	PixlrUser user = pixlrService.getUserByUID(uid);

	PixlrUserDTO userDTO = new PixlrUserDTO(user);

	request.setAttribute("userDTO", userDTO);

	return "pages/monitoring/pixlrDisplay";
    }

    /**
     * set up pixlrService
     */

    /**
     * Opens a user's reflection
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/openNotebook")
    public String openNotebook(HttpServletRequest request) {

	//MonitoringForm monitorForm = (MonitoringForm) form;
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionID", false);
	Long userID = WebUtil.readLongParam(request, "userID", false);

	PixlrUser pixlrUser = pixlrService.getUserByUserIdAndSessionId(userID, toolSessionId);

	NotebookEntry notebookEntry = pixlrService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		PixlrConstants.TOOL_SIGNATURE, userID.intValue());

	PixlrUserDTO pixlrUserDTO = new PixlrUserDTO(pixlrUser);
	pixlrUserDTO.setNotebookEntry(notebookEntry.getEntry());

	request.setAttribute("pixlrUserDTO", pixlrUserDTO);

	return "/pages/learning/notebook";
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
    @RequestMapping("/toggleHideImage")
    public String toggleHideImage(@ModelAttribute("monitoringForm")MonitoringForm monitoringForm, HttpServletRequest request,
	    HttpServletResponse response) {

	PixlrUser pixlrUser = pixlrService.getUserByUID(monitoringForm.getHideUserImageUid());

	if (pixlrUser.isImageHidden()) {
	    pixlrUser.setImageHidden(false);
	} else {
	    pixlrUser.setImageHidden(true);
	}
	pixlrService.saveOrUpdatePixlrUser(pixlrUser);
	return unspecified(monitoringForm,request);
    }
}
