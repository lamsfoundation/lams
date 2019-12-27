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

package org.lamsfoundation.lams.tool.leaderselection.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.tool.leaderselection.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger logger = Logger.getLogger(AuthoringController.class);

    @Autowired
    private ILeaderselectionService leaderselectionService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     */
    @RequestMapping("")
    protected String unspecified(AuthoringForm authoringForm, HttpServletRequest request) {
	// Extract toolContentID from parameters.
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Leaderselection with given toolContentID
	Leaderselection leaderselection = leaderselectionService.getContentByContentId(toolContentID);
	if (leaderselection == null) {
	    leaderselection = leaderselectionService.copyDefaultContent(toolContentID);
	    leaderselection.setCreateDate(new Date());
	    leaderselectionService.saveOrUpdateLeaderselection(leaderselection);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	return readDatabaseData(authoringForm, leaderselection, request, mode);
    }

    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Leaderselection leaderselection = leaderselectionService.getContentByContentId(toolContentID);
	leaderselection.setDefineLater(true);
	leaderselectionService.saveOrUpdateLeaderselection(leaderselection);

	//audit log the teacher has started editing activity in monitor
	leaderselectionService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, leaderselection, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Leaderselection leaderselection, HttpServletRequest request, ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	// Set up the authForm.
	authoringForm.setTitle(leaderselection.getTitle());
	authoringForm.setInstructions(leaderselection.getInstructions());

	// Set up sessionMap
	SessionMap<String, Object> map = new SessionMap<>();
	map.put(AuthoringController.KEY_MODE, mode);
	map.put(AuthoringController.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringController.KEY_TOOL_CONTENT_ID, toolContentID);
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    @RequestMapping(value = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute("authoringForm") AuthoringForm authoringForm,
	    HttpServletRequest request, HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());

	// get leaderselection content.
	Leaderselection leaderselection = leaderselectionService
		.getContentByContentId((Long) map.get(AuthoringController.KEY_TOOL_CONTENT_ID));

	// update leaderselection content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringController.KEY_MODE);
	leaderselection.setTitle(authoringForm.getTitle());
	leaderselection.setInstructions(authoringForm.getInstructions());

	// set the update date
	leaderselection.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	leaderselection.setDefineLater(false);

	leaderselectionService.saveOrUpdateLeaderselection(leaderselection);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("authoringForm", authoringForm);

	return "pages/authoring/authoring";
    }
}
