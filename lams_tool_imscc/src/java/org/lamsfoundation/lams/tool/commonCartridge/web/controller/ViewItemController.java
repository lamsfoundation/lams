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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeItemComparator;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeWebUtils;
import org.lamsfoundation.lams.tool.commonCartridge.util.LamsBasicLTIUtil;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewItemController {

    private static final Logger log = Logger.getLogger(ViewItemController.class);

    @Autowired
    @Qualifier("commonCartridgeMessageService")
    private MessageService messageService;

    @Autowired
    private ICommonCartridgeService commonCartridgeService;;

    /**
     * Open url in popup window page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/openUrlPopup")
    private String openUrlPopup(HttpServletRequest request) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	CommonCartridgeItem item = getCommonCartridgeItem(request, sessionMap, mode);
	int itemIdx = NumberUtils.stringToInt(request.getParameter(CommonCartridgeConstants.PARAM_ITEM_INDEX));

	String launchBasicLTIUrl;
	if (CommonCartridgeConstants.MODE_AUTHOR_SESSION.equals(mode)) {
	    launchBasicLTIUrl = "/launchBasicLTI.do?" + AttributeNames.ATTR_MODE + "=" + mode + "&"
		    + CommonCartridgeConstants.PARAM_ITEM_INDEX + "=" + itemIdx + "&"
		    + CommonCartridgeConstants.ATTR_SESSION_MAP_ID + "=" + sessionMapID;
	} else {
	    launchBasicLTIUrl = "/launchBasicLTI.do?" + CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID + "="
		    + item.getUid() + "&" + CommonCartridgeConstants.ATTR_SESSION_MAP_ID + "=" + sessionMapID;
	}
	request.setAttribute(CommonCartridgeConstants.PARAM_OPEN_URL_POPUP, launchBasicLTIUrl);
	request.setAttribute(CommonCartridgeConstants.PARAM_TITLE, item.getTitle());
	return "pages/itemreview/openurl";
    }

    /**
     * Return next instrucion to page. It need four input parameters, mode, itemIndex or itemUid, and insIdx.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/nextInstruction")
    private String nextInstruction(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/itemreview/instructionsnav";
    }

    /**
     * Display main frame to display instrcution and item content.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/reviewItem")
    private String reviewItem(HttpServletRequest request) throws IOException {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);

	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	CommonCartridgeItem item = getCommonCartridgeItem(request, sessionMap, mode);

	String idStr = request.getParameter(CommonCartridgeConstants.ATTR_TOOL_SESSION_ID);
	Long sessionId = NumberUtils.createLong(idStr);
	// mark this item access flag if it is learner
	if (ToolAccessMode.LEARNER.toString().equals(mode)) {
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    commonCartridgeService.setItemAccess(item.getUid(), new Long(user.getUserID().intValue()), sessionId);
	}

	if (item == null) {
	    return "error";
	}
	// set url to content frame

	int itemIdx = NumberUtils.stringToInt(request.getParameter(CommonCartridgeConstants.PARAM_ITEM_INDEX));

	short type = item.getType();
	String url = null;
	switch (type) {
	    case CommonCartridgeConstants.RESOURCE_TYPE_BASIC_LTI:
		if (item.isOpenUrlNewWindow()) {
		    url = "/openUrlPopup.do?";
		} else {
		    url = "/launchBasicLTI.do?";
		}
		if (CommonCartridgeConstants.MODE_AUTHOR_SESSION.equals(mode)) {
		    url += AttributeNames.ATTR_MODE + "=" + mode + "&" + CommonCartridgeConstants.PARAM_ITEM_INDEX + "="
			    + itemIdx + "&" + CommonCartridgeConstants.ATTR_SESSION_MAP_ID + "=" + sessionMapID;
		} else {
		    url += CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID + "=" + item.getUid() + "&"
			    + CommonCartridgeConstants.ATTR_SESSION_MAP_ID + "=" + sessionMapID;
		}

		break;
	    case CommonCartridgeConstants.RESOURCE_TYPE_COMMON_CARTRIDGE:
		url = "/download/?uuid=" + item.getFileDisplayUuid() + "&preferDownload=false";
		break;
	}
	request.setAttribute(CommonCartridgeConstants.ATTR_RESOURCE_REVIEW_URL, url);

	// these attribute will be use to instruction navigator page
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(CommonCartridgeConstants.PARAM_ITEM_INDEX, itemIdx);
	Long itemUid = NumberUtils.createLong(request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID));
	request.setAttribute(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID, itemUid);
	request.setAttribute(CommonCartridgeConstants.ATTR_TOOL_SESSION_ID, sessionId);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return "pages/itemreview/mainframe";

    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/launchBasicLTI")
    @ResponseBody
    private void launchBasicLTI(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	String sessionMapID = WebUtil.readStrParam(request, CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	CommonCartridgeItem item = getCommonCartridgeItem(request, sessionMap, mode);

	// Get the post data for the placement
	String returnValues = LamsBasicLTIUtil.postLaunchHTML(commonCartridgeService, messageService, item);

	try {
	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("utf-8");
	    response.addDateHeader("Expires", System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 365L));
	    response.addDateHeader("Last-Modified", System.currentTimeMillis());
	    response.addHeader("Cache-Control",
		    "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
	    response.addHeader("Pragma", "no-cache");
	    ServletOutputStream out = response.getOutputStream();

	    out.println("<!DOCTYPE html>");
	    out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
	    out.println("<html>\n<head>");
	    out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	    out.println("</head>\n<body>");
	    out.println(returnValues);
	    out.println("</body>\n</html>");

	} catch (IOException e) {
	    e.printStackTrace();
	    throw e;
	}

    }

    /**
     * Return resoruce item according to ToolAccessMode.
     *
     * @param request
     * @param sessionMap
     * @param mode
     * @return
     */
    private CommonCartridgeItem getCommonCartridgeItem(HttpServletRequest request, SessionMap sessionMap, String mode) {
	CommonCartridgeItem item = null;
	if (CommonCartridgeConstants.MODE_AUTHOR_SESSION.equals(mode)) {
	    int itemIdx = NumberUtils.stringToInt(request.getParameter(CommonCartridgeConstants.PARAM_ITEM_INDEX), 0);
	    // authoring: does not save item yet, so only has ItemList from session and identity by Index
	    List<CommonCartridgeItem> commonCartridgeList = new ArrayList<>(getCommonCartridgeItemList(sessionMap));
	    item = commonCartridgeList.get(itemIdx);
	} else {
	    Long itemUid = NumberUtils
		    .createLong(request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID));
	    // get back the commonCartridge and item list and display them on page
	    item = commonCartridgeService.getCommonCartridgeItemByUid(itemUid);
	}
	return item;
    }

    /**
     * List save current commonCartridge items.
     *
     * @param request
     * @return
     */
    private SortedSet<CommonCartridgeItem> getCommonCartridgeItemList(SessionMap sessionMap) {
	SortedSet<CommonCartridgeItem> list = (SortedSet) sessionMap
		.get(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new CommonCartridgeItemComparator());
	    sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

}
