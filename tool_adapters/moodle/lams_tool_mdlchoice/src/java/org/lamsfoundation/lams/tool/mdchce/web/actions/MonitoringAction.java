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

package org.lamsfoundation.lams.tool.mdchce.web.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.mdchce.dto.MdlChoiceDTO;
import org.lamsfoundation.lams.tool.mdchce.dto.MdlChoiceSessionDTO;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoice;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceConfigItem;
import org.lamsfoundation.lams.tool.mdchce.service.IMdlChoiceService;
import org.lamsfoundation.lams.tool.mdchce.service.MdlChoiceServiceProxy;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceConstants;
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
	    + MdlChoiceConstants.TOOL_SIGNATURE + "/";

    public static final String RELATIVE_MONITOR_URL = "course/modedit-lams.php?";

    public IMdlChoiceService mdlChoiceService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	MdlChoice mdlChoice = mdlChoiceService.getMdlChoiceByContentId(toolContentID);

	if (mdlChoice == null) {
	    // TODO error page.
	}

	MdlChoiceDTO mdlChoiceDT0 = new MdlChoiceDTO(mdlChoice);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	mdlChoiceDT0.setCurrentTab(currentTab);

	for (MdlChoiceSessionDTO sessionDTO : mdlChoiceDT0.getSessionDTOs()) {
	    try {
		String responseUrl = mdlChoiceService.getConfigItem(MdlChoiceConfigItem.KEY_EXTERNAL_SERVER_URL)
			.getConfigValue();
		responseUrl += RELATIVE_MONITOR_URL;

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ sessionDTO.getSessionID().toString() + "&dispatch=finishActivity";
		returnUrl = URLEncoder.encode(returnUrl, "UTF8");

		responseUrl += "&update=" + sessionDTO.getExtSessionID()+ "&id=" + sessionDTO.getExtSessionID() + "&returnUrl=" + returnUrl;

		sessionDTO.setRunTimeUrl(responseUrl);
	    } catch (UnsupportedEncodingException e) {
		log.error(e);
	    }
	}

	request.setAttribute("mdlChoiceDTO", mdlChoiceDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	return mapping.findForward("success");
    }

    /**
     * set up mdlChoiceService
     */
    private void setupService() {
	if (mdlChoiceService == null) {
	    mdlChoiceService = MdlChoiceServiceProxy.getMdlChoiceService(this.getServlet().getServletContext());
	}
    }
}
