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

package org.lamsfoundation.lams.tool.mdscrm.web.actions;

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
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScorm;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScormConfigItem;
import org.lamsfoundation.lams.tool.mdscrm.service.IMdlScormService;
import org.lamsfoundation.lams.tool.mdscrm.service.MdlScormServiceProxy;
import org.lamsfoundation.lams.tool.mdscrm.util.MdlScormConstants;
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

    public IMdlScormService mdlScormService;

    //public static final String RELATIVE_MOODLE_AUTHOR_URL = "/course/modedit-lams.php?";
    public static final String RELATIVE_MOODLE_AUTHOR_URL = "course/lamsframes.php?";
    public static final String MOODLE_EDIT_URL = "course/modedit-lams.php";

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";

    private static final String KEY_EXTERNAL_TOOL_CONTENT_ID = "extToolContentID";

    private static final String KEY_MODE = "mode";

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlScormConstants.TOOL_SIGNATURE + "/";

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

	// set up mdlScormService
	if (mdlScormService == null) {
	    mdlScormService = MdlScormServiceProxy.getMdlScormService(this.getServlet().getServletContext());
	}

	// retrieving MdlScorm with given toolContentID
	// if is a new instance, customCSV should be passed, and used for the course url, otherwise the saved value should be used
	MdlScorm mdlScorm = mdlScormService.getMdlScormByContentId(toolContentID);

	// Getting the custom csv from the request
	String customCSV = WebUtil.readStrParam(request, "customCSV", true);
	String userFromCSV = null;
	String courseFromCSV = null;
	String sectionFromCSV = null;
	if (customCSV == null && mdlScorm == null) {
	    logger.error("CustomCSV required if mdlScorm is null");
	    throw new ToolException("CustomCSV required if mdlScorm is null");
	} else if (customCSV != null) {
	    String splitCSV[] = customCSV.split(",");
	    if (splitCSV.length != 3) {
		logger.error("mdlScorm tool customCSV not in required (user,course,courseURL) form: " + customCSV);
		throw new ToolException("mdlScorm tool cusomCSV not in required (user,course,courseURL) form: "
			+ customCSV);
	    } else {
		userFromCSV = splitCSV[0];
		courseFromCSV = splitCSV[1];
		sectionFromCSV = splitCSV[2];
	    }
	}

	if (mdlScorm == null) {
	    mdlScorm = mdlScormService.copyDefaultContent(toolContentID);
	    mdlScorm.setExtUsername(userFromCSV);
	    mdlScorm.setExtCourseId(courseFromCSV);
	    mdlScorm.setExtSection(sectionFromCSV);
	    mdlScorm.setCreateDate(new Date());
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    mdlScorm.setDefineLater(true);
	    mdlScormService.saveOrUpdateMdlScorm(mdlScorm);
	}

	// if no external content id, open the mdl author page, otherwise, open the edit page
	try {

	    // If the mdlScorm has a saved course url, use it, otherwise use the one giving in the request in customCSV
	    //String courseUrlToBeUsed = (mdlScorm.getExtCourseUrl() != null) ? mdlScorm.getExtCourseUrl() : courseUrlFromCSV;

	    String responseUrl = mdlScormService.getConfigItem(MdlScormConfigItem.KEY_EXTERNAL_SERVER_URL)
		    .getConfigValue();
	    responseUrl += RELATIVE_MOODLE_AUTHOR_URL;
	    String returnUpdateUrl = URLEncoder.encode(TOOL_APP_URL + "/authoring.do?dispatch=updateContent" + "&"
		    + AttributeNames.PARAM_TOOL_CONTENT_ID + "=" + toolContentID.toString(), "UTF8");
  
	    responseUrl += "&lamsUpdateURL=" + returnUpdateUrl;
	    
	    String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_EDIT_URL, "UTF8");
	    
	    responseUrl += "&dest=" + encodedMoodleRelativePath ;

	    if (mdlScorm.getExtSection() != null) {
		responseUrl += "&section=" + mdlScorm.getExtSection();
	    } else {
		responseUrl += "&section=" + sectionFromCSV;
	    }

	    if (mdlScorm.getExtToolContentId() != null) {
		responseUrl += "&id=" + mdlScorm.getExtToolContentId().toString();
	    } else {
		responseUrl += "&add=scorm";
	    }

	    if (mdlScorm.getExtCourseId() != null) {
		responseUrl += "&course=" + mdlScorm.getExtCourseId();
	    } else {
		responseUrl += "&course=" + courseFromCSV;
	    }

	    log.debug("Sending to moodle scorm edit page: " + responseUrl);

	    response.sendRedirect(responseUrl);
	} catch (Exception e) {
	    log.error("Could not redirect to mdl scorm authoring", e);
	}
	return null;
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	Long toolContentID = new Long(WebUtil.readLongParam(request, KEY_TOOL_CONTENT_ID));
	Long externalToolContentID = new Long(WebUtil.readLongParam(request, KEY_EXTERNAL_TOOL_CONTENT_ID));
	//String sessionMapID = WebUtil.readStrParam(request,"sessionMapID");

	// get mdlScorm content.
	MdlScorm mdlScorm = mdlScormService.getMdlScormByContentId(toolContentID);
	mdlScorm.setExtToolContentId(externalToolContentID);
	mdlScorm.setUpdateDate(new Date());
	mdlScorm.setDefineLater(false);
	mdlScormService.saveOrUpdateMdlScorm(mdlScorm);

	String redirectString = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
		+ MdlScormConstants.TOOL_SIGNATURE + "/clearsession.do" + "?action=confirm&mode=author" + "&signature="
		+ MdlScormConstants.TOOL_SIGNATURE + "&toolContentID=" + toolContentID.toString() + "&defineLater=no"
		+ "&customiseSessionID=" + "&contentFolderID=0";

	log.debug("Manual redirect for MdlScorm to: " + redirectString);

	try {
	    response.sendRedirect(redirectString);
	} catch (Exception e) {
	    log.error("Could not redirect to clear session action for MdlScorm", e);
	}

	return null;
    }
}
