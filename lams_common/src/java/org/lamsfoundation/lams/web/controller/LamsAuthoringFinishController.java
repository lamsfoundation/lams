/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.web.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * This controller class does some process when author try to save/cancel/close authoring tool pages. If author try to
 * save
 * tool page, this action will redirects tool page to confirm page and execute clearSession() method. If author try to
 * cancel/close window, this action will execute clearSession().
 *
 * @author Steve.Ni
 */
public abstract class LamsAuthoringFinishController {
    private static Logger log = Logger.getLogger(LamsAuthoringFinishController.class);

    private static final String ACTION_NAME = "action";
    private static final String ACTION_MODE = "mode";
    private static final String CUSTOMISE_SESSION_ID = "customiseSessionID";
    private static final String TOOL_SIGNATURE = "signature";

    private static final String CONFIRM_ACTION = "confirm";
    private static final String CANCEL_ACTION = "cancel";
    private static final String DEFINE_LATER_ACTION = "defineLater";

    private static final String RE_EDIT_URL = "reEditUrl";

    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private ILamsToolService lamsToolService;

    /**
     * Action method, will handle save/cancel action.
     */
    public void execute(HttpServletRequest request, HttpServletResponse response, ApplicationContext applicationContext)
	    throws IOException {
	String action = request.getParameter(ACTION_NAME);
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, ACTION_MODE, false);
	String cSessionID = request.getParameter(CUSTOMISE_SESSION_ID);
	String notifyCloseURL = (String) request.getSession().getAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL);
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	// clear session according to the ToolAccessMode.
	clearSession(cSessionID, request.getSession(), mode);

	//CONFIRM_ACTION got fired only for general authoring and not for define later one
	if (StringUtils.equals(action, CONFIRM_ACTION)) {
	    String nextUrl = getLamsUrl() + "authoringConfirm.jsp";
	    String signature = request.getParameter(TOOL_SIGNATURE);

	    String contentFolderID = "TODO_remove-change_optional_to_false";
	    contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID, true);

	    // check whether it use on define it later page
	    Tool tool = lamsToolService.getToolBySignature(signature);

	    //add reeditUrl parameter
	    String reeditUrl = WebUtil.appendParameterToURL(getLamsUrl() + tool.getAuthorUrl(),
		    AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId.toString());
	    reeditUrl = WebUtil.appendParameterToURL(reeditUrl, AttributeNames.PARAM_CONTENT_FOLDER_ID,
		    contentFolderID);
	    if (notifyCloseURL != null && notifyCloseURL.length() > 0) {
		reeditUrl = WebUtil.appendParameterToURL(reeditUrl, AttributeNames.PARAM_NOTIFY_CLOSE_URL,
			notifyCloseURL);
	    }
	    nextUrl = WebUtil.appendParameterToURL(nextUrl, RE_EDIT_URL, URLEncoder.encode(reeditUrl, "UTF-8"));

	    if (!StringUtils.isBlank(notifyCloseURL)) {
		nextUrl = WebUtil.appendParameterToURL(nextUrl, AttributeNames.PARAM_NOTIFY_CLOSE_URL, notifyCloseURL);
	    }
	    response.sendRedirect(nextUrl);
	}

	//audit log content has been finished being edited
	if (StringUtils.equals(action, DEFINE_LATER_ACTION)) {
	    logEventService.logFinishEditingActivityInMonitor(toolContentId);
	}

	//reset defineLater task
	if (StringUtils.equals(action, CANCEL_ACTION) && mode.isTeacher()) {
	    String signature = request.getParameter(TOOL_SIGNATURE);

	    ToolContentManager contentManager = (ToolContentManager) findToolService(applicationContext, signature);
	    contentManager.resetDefineLater(toolContentId);

	    logEventService.logCancelEditingActivityInMonitor(toolContentId);
	}
    }

    /**
     * All subclass will implements this method and execute clear <code>HttpSession</code> action to remove obsolete
     * session values.
     *
     * @param customiseSessionID
     *            customised session ID.
     * @param session
     * @param mode
     *            ToolAccessMode to decide which role's session will be clear.
     */
    abstract protected void clearSession(String customiseSessionID, HttpSession session, ToolAccessMode mode);

    // ---------------------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------------------

    private String getLamsUrl() {
	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);

	if (StringUtils.isBlank(serverURL)) {
	    log.warn("ServerURLTag unable to write out server URL as it is missing from the configuration file.");
	}

	return serverURL;
    }

    /**
     * Find a tool's service registered inside lams.
     *
     * @param signature
     *            the tool signature.
     * @return the service object from tool.
     * @throws NoSuchBeanDefinitionException
     *             if the tool is not the classpath or the supplied service name is wrong.
     */
    private Object findToolService(ApplicationContext applicationContext, String signature)
	    throws NoSuchBeanDefinitionException {
	Tool tool = lamsToolService.getToolBySignature(signature);
	return applicationContext.getBean(tool.getServiceName());
    }
}
