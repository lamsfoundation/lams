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

package org.lamsfoundation.lams.tool.commonCartridge.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.dto.Summary;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    public static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    private ICommonCartridgeService commonCartridgeService;

    @RequestMapping("/hideitem")
    private String hideitem(HttpServletRequest request) {

	// get back SessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	Long toolContentId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);

	Long itemUid = WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);
	commonCartridgeService.setItemVisible(itemUid, toolContentId, false);

	// update session value
	List<List> groupList = (List<List>) sessionMap.get(CommonCartridgeConstants.ATTR_SUMMARY_LIST);
	if (groupList != null) {
	    for (List<Summary> group : groupList) {
		for (Summary sum : group) {
		    if (itemUid.equals(sum.getItemUid())) {
			sum.setItemHide(true);
			break;
		    }
		}
	    }
	}

	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/showitem")
    private String showitem(HttpServletRequest request) {

	// get back SessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	Long toolContentId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);

	Long itemUid = WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);
	commonCartridgeService.setItemVisible(itemUid, toolContentId, true);

	// update session value
	List<List> groupList = (List<List>) sessionMap.get(CommonCartridgeConstants.ATTR_SUMMARY_LIST);
	if (groupList != null) {
	    for (List<Summary> group : groupList) {
		for (Summary sum : group) {
		    if (itemUid.equals(sum.getItemUid())) {
			sum.setItemHide(false);
			break;
		    }
		}
	    }
	}
	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/summary")
    private String summary(HttpServletRequest request) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<List<Summary>> summaryList = commonCartridgeService.getSummary(contentId);

	CommonCartridge commonCartridge = commonCartridgeService.getCommonCartridgeByContentId(contentId);

	// cache into sessionMap
	sessionMap.put(CommonCartridgeConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(CommonCartridgeConstants.PAGE_EDITABLE, commonCartridge.isContentInUse());
	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE, commonCartridge);
	sessionMap.put(CommonCartridgeConstants.ATTR_TOOL_CONTENT_ID, contentId);
	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/listuser")
    private String listuser(HttpServletRequest request) {
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long itemUid = WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);

	// get user list by given item uid
	List list = commonCartridgeService.getUserListBySessionItem(sessionId, itemUid);

	// set to request
	request.setAttribute(CommonCartridgeConstants.ATTR_USER_LIST, list);
	return "pages/monitoring/userlist";
    }
}
