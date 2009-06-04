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

package org.lamsfoundation.lams.tool.mdfrum.web.actions;

import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForum;
import org.lamsfoundation.lams.tool.mdfrum.service.IMdlForumService;
import org.lamsfoundation.lams.tool.mdfrum.service.MdlForumServiceProxy;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 * @struts.action-forward name="message_page" path="tiles:/generic/message"
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IMdlForumService mdlForumService;

    //public static final String RELATIVE_MOODLE_AUTHOR_URL = "/course/modedit-lams.php?";
    public static final String RELATIVE_MOODLE_AUTHOR_URL = "course/lamsframes.php?";
    public static final String MOODLE_EDIT_URL = "course/modedit-lams.php";

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_EXTERNAL_TOOL_CONTENT_ID = "extToolContentID";

    private static final String KEY_MODE = "mode";

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlForumConstants.TOOL_SIGNATURE + "/";

    public IIntegrationService integrationService;

    /**
     * Default method when no dispatch parameter is specified. It is expected
     * that the parameter <code>toolContentID</code> will be passed in. This
     * will be used to retrieve content for this tool.
     * 
     */
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID, contentFolderID and ToolAccessMode from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, KEY_MODE, true);

	// set up mdlForumService
	if (mdlForumService == null) {
	    mdlForumService = MdlForumServiceProxy.getMdlForumService(this.getServlet().getServletContext());
	}

	// retrieving MdlForum with given toolContentID
	// if is a new instance, customCSV should be passed, and used for the course url, otherwise the saved value should be used
	MdlForum mdlForum = mdlForumService.getMdlForumByContentId(toolContentID);

	// Getting the custom csv from the request
	String customCSV = WebUtil.readStrParam(request, "customCSV", true);
	String userFromCSV = null;
	String courseFromCSV = null;
	String sectionFromCSV = null;
	String extLmsIdFromCSV = null;
	if (customCSV == null && mdlForum == null) {
	    logger.error("CustomCSV required if mdlForum is null");
	    throw new ToolException("CustomCSV required if mdlForum is null");
	} else if (customCSV != null) {
	    String splitCSV[] = customCSV.split(",");
	    if (splitCSV.length != 4) {
		logger.error("mdlForum tool customCSV not in required (user,course,courseURL) form: " + customCSV);
		throw new ToolException("mdlForum tool cusomCSV not in required (user,course,courseURL) form: "
			+ customCSV);
	    } else {
		userFromCSV = splitCSV[0];
		courseFromCSV = splitCSV[1];
		sectionFromCSV = splitCSV[2];
		extLmsIdFromCSV = splitCSV[3];
	    }
	}

	if (mdlForum == null) {
	    mdlForum = mdlForumService.copyDefaultContent(toolContentID);
	    mdlForum.setExtUsername(userFromCSV);
	    mdlForum.setExtCourseId(courseFromCSV);
	    mdlForum.setExtSection(sectionFromCSV);
	    mdlForum.setExtLmsId(extLmsIdFromCSV);
	    mdlForum.setCreateDate(new Date());
	}
	
	if (mdlForum.getExtLmsId() == null) {
	    mdlForum.setExtUsername(userFromCSV);
	    mdlForum.setExtCourseId(courseFromCSV);
	    mdlForum.setExtSection(sectionFromCSV);
	    mdlForum.setExtLmsId(extLmsIdFromCSV);
	    mdlForum.setCreateDate(new Date());
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    mdlForum.setDefineLater(true);
	    mdlForumService.saveOrUpdateMdlForum(mdlForum);
	}

	// if no external content id, open the mdl author page, otherwise, open the edit page
	try {

	    // If the mdlForum has a saved course url, use it, otherwise use the one giving in the request in customCSV	    
	    String responseUrl = mdlForumService.getExtServerUrl(mdlForum.getExtLmsId());
	    
	    responseUrl += RELATIVE_MOODLE_AUTHOR_URL;
	    String returnUpdateUrl = URLEncoder.encode(TOOL_APP_URL + "/authoring.do?dispatch=updateContent" + "&"
		    + AttributeNames.PARAM_TOOL_CONTENT_ID + "=" + toolContentID.toString(), "UTF8");
  
	    responseUrl += "&lamsUpdateURL=" + returnUpdateUrl;
	    
	    String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_EDIT_URL, "UTF8");
	    
	    responseUrl += "&dest=" + encodedMoodleRelativePath;

	    if (mdlForum.getExtSection() != null) {
		responseUrl += "&section=" + mdlForum.getExtSection();
	    } else {
		responseUrl += "&section=" + sectionFromCSV;
	    }

	    if (mdlForum.getExtToolContentId() != null) {
		responseUrl += "&id=" + mdlForum.getExtToolContentId().toString();
	    } else {
		responseUrl += "&add=Forum";
	    }

	    if (mdlForum.getExtCourseId() != null) {
		responseUrl += "&course=" + mdlForum.getExtCourseId();
	    } else {
		responseUrl += "&course=" + courseFromCSV;
	    }

	    log.debug("Sending to moodle Forum edit page: " + responseUrl);

	    response.sendRedirect(responseUrl);
	} catch (Exception e) {
	    log.error("Could not redirect to mdl Forum authoring", e);
	}
	return null;
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	Long toolContentID = new Long(WebUtil.readLongParam(request, KEY_TOOL_CONTENT_ID));
	Long externalToolContentID = new Long(WebUtil.readLongParam(request, KEY_EXTERNAL_TOOL_CONTENT_ID));
	//String sessionMapID = WebUtil.readStrParam(request,"sessionMapID");

	// get mdlForum content.
	MdlForum mdlForum = mdlForumService.getMdlForumByContentId(toolContentID);
	mdlForum.setExtToolContentId(externalToolContentID);
	mdlForum.setUpdateDate(new Date());
	mdlForum.setDefineLater(false);
	mdlForumService.saveOrUpdateMdlForum(mdlForum);

	String redirectString = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
		+ MdlForumConstants.TOOL_SIGNATURE + "/clearsession.do" + "?action=confirm&mode=author" + "&signature="
		+ MdlForumConstants.TOOL_SIGNATURE + "&toolContentID=" + toolContentID.toString() + "&defineLater=no"
		+ "&customiseSessionID=" + "&contentFolderID=0";

	log.debug("Manual redirect for MdlForum to: " + redirectString);

	try {
	    response.sendRedirect(redirectString);
	} catch (Exception e) {
	    log.error("Could not redirect to clear session action for MdlForum", e);
	}

	return null;
    }
}
