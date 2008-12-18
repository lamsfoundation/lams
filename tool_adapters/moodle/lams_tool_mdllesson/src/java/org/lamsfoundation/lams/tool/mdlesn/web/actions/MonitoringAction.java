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

package org.lamsfoundation.lams.tool.mdlesn.web.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.mdlesn.dto.MdlLessonDTO;
import org.lamsfoundation.lams.tool.mdlesn.dto.MdlLessonSessionDTO;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLesson;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonConfigItem;
import org.lamsfoundation.lams.tool.mdlesn.service.IMdlLessonService;
import org.lamsfoundation.lams.tool.mdlesn.service.MdlLessonServiceProxy;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
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
 * 
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlLessonConstants.TOOL_SIGNATURE + "/";

    public static final String RELATIVE_MONITOR_URL = "mod/lesson/view.php?";

    public IMdlLessonService mdlLessonService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	MdlLesson mdlLesson = mdlLessonService.getMdlLessonByContentId(toolContentID);

	if (mdlLesson == null) {
	    // TODO error page.
	}

	MdlLessonDTO mdlLessonDT0 = new MdlLessonDTO(mdlLesson);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	mdlLessonDT0.setCurrentTab(currentTab);

	for (MdlLessonSessionDTO sessionDTO : mdlLessonDT0.getSessionDTOs()) {
	    try {
		String responseUrl = mdlLessonService.getConfigItem(MdlLessonConfigItem.KEY_EXTERNAL_SERVER_URL)
			.getConfigValue();
		responseUrl += RELATIVE_MONITOR_URL;

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ sessionDTO.getSessionID().toString() + "&dispatch=finishActivity";
		returnUrl = URLEncoder.encode(returnUrl, "UTF8");

		responseUrl += "&id=" + sessionDTO.getExtSessionID() + "&returnUrl=" + returnUrl;

		sessionDTO.setRunTimeUrl(responseUrl);
	    } catch (UnsupportedEncodingException e) {
		log.error(e);
	    }
	}

	request.setAttribute("mdlLessonDTO", mdlLessonDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	return mapping.findForward("success");
    }

    /**
     * set up mdlLessonService
     */
    private void setupService() {
	if (mdlLessonService == null) {
	    mdlLessonService = MdlLessonServiceProxy.getMdlLessonService(this.getServlet().getServletContext());
	}
    }
}
