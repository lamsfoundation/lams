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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeSession;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.tool.commonCartridge.service.CommonCartridgeApplicationException;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.tool.commonCartridge.util.CommonCartridgeItemComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    private ICommonCartridgeService commonCartridgeService;

    @Autowired
    @Qualifier("commonCartridgeMessageService")
    private MessageService messageService;

    /**
     * Read commonCartridge data from database and put them into HttpSession. It will redirect to init.do directly after
     * this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     */

    @RequestMapping("/start")
    private String start(HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(CommonCartridgeConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the commonCartridge and item list and display them on page
	CommonCartridgeUser commonCartridgeUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // commonCartridgeUser may be null if the user was force completed.
	    commonCartridgeUser = getSpecifiedUser(commonCartridgeService, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    commonCartridgeUser = getCurrentUser(commonCartridgeService, sessionId);
	}

	CommonCartridge commonCartridge = commonCartridgeService.getCommonCartridgeBySessionId(sessionId);
	List<CommonCartridgeItem> items = new ArrayList<>();
	items.addAll(commonCartridge.getCommonCartridgeItems());

	// check whether there is only one commonCartridge item and run auto flag is true or not.
	boolean runAuto = false;
	Long runAutoItemUid = null;
	int itemsNumber = 0;
	if (commonCartridge.getCommonCartridgeItems() != null) {
	    itemsNumber = commonCartridge.getCommonCartridgeItems().size();
	    if (commonCartridge.isRunAuto() && itemsNumber == 1) {
		CommonCartridgeItem item = (CommonCartridgeItem) commonCartridge.getCommonCartridgeItems().iterator()
			.next();
		// only visible item can be run auto.
		if (!item.isHide()) {
		    runAuto = true;
		    runAutoItemUid = item.getUid();
		}
	    }
	}

	// basic information
	sessionMap.put(CommonCartridgeConstants.ATTR_TITLE, commonCartridge.getTitle());
	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_INSTRUCTION, commonCartridge.getInstructions());
	sessionMap.put(CommonCartridgeConstants.ATTR_USER_FINISHED,
		commonCartridgeUser != null && commonCartridgeUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(CommonCartridgeConstants.ATTR_RUN_AUTO, new Boolean(runAuto));

	// add define later support
	if (commonCartridge.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	commonCartridge.setContentInUse(true);
	commonCartridge.setDefineLater(false);
	commonCartridgeService.saveOrUpdateCommonCartridge(commonCartridge);

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, commonCartridgeService.isLastActivity(sessionId));

	// init commonCartridge item list
	SortedSet<CommonCartridgeItem> commonCartridgeItemList = getCommonCartridgeItemList(sessionMap);
	commonCartridgeItemList.clear();
	if (items != null) {
	    // remove hidden items.
	    for (CommonCartridgeItem item : items) {
		// becuase in webpage will use this login name. Here is just
		// initial it to avoid session close error in proxy object.
		if (item.getCreateBy() != null) {
		    item.getCreateBy().getLoginName();
		}
		if (!item.isHide()) {
		    commonCartridgeItemList.add(item);
		}
	    }
	}

	// set complete flag for display purpose
	if (commonCartridgeUser != null) {
	    commonCartridgeService.retrieveComplete(commonCartridgeItemList, commonCartridgeUser);
	}
	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE, commonCartridge);

	if (runAuto) {
	    String redirectURL = "redirect:/reviewItem.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, CommonCartridgeConstants.ATTR_SESSION_MAP_ID,
		    sessionMap.getSessionID());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, CommonCartridgeConstants.ATTR_TOOL_SESSION_ID,
		    sessionId.toString());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, CommonCartridgeConstants.ATTR_RESOURCE_ITEM_UID,
		    runAutoItemUid.toString());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, CommonConstants.PARAM_MODE, mode.toString());
	    return redirectURL;

	} else {
	    return "pages/learning/learning";
	}
    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/finish")
    private String finish(HttpServletRequest request, HttpServletResponse response)
	    throws CommonCartridgeApplicationException, IOException {

	// get back SessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// auto run mode, when use finish the only one commonCartridge item, mark it as complete then finish this activity as
	// well.
	String commonCartridgeItemUid = request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);
	if (commonCartridgeItemUid != null) {
	    doComplete(request);
	    // NOTE:So far this flag is useless(31/08/2006).
	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	    request.setAttribute(CommonCartridgeConstants.ATTR_RUN_AUTO, true);
	} else {
	    request.setAttribute(CommonCartridgeConstants.ATTR_RUN_AUTO, false);
	}

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return "pages/learning/learning";
	}

	// get sessionId from HttpServletRequest
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	String nextActivityUrl = commonCartridgeService.finishToolSession(sessionId, userID);
	response.sendRedirect(nextActivityUrl);
	return null;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	int miniViewFlag = commonCartridgeService.checkMiniView(sessionId, userID);
	// if current user view less than reqired view count number, then just return error message.
	if (miniViewFlag > 0) {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    errorMap.add("GLOBAL", messageService.getMessage("lable.learning.minimum.view.number.less",
		    new Object[] { miniViewFlag }));
	    request.setAttribute("errorMap", errorMap);
	    return false;
	}

	return true;
    }

    /**
     * List save current commonCartridge items.
     *
     * @param request
     * @return
     */
    private SortedSet<CommonCartridgeItem> getCommonCartridgeItemList(SessionMap sessionMap) {
	SortedSet<CommonCartridgeItem> list = (SortedSet<CommonCartridgeItem>) sessionMap
		.get(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new CommonCartridgeItemComparator());
	    sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    private CommonCartridgeUser getCurrentUser(ICommonCartridgeService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	CommonCartridgeUser commonCartridgeUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),
		sessionId);

	if (commonCartridgeUser == null) {
	    CommonCartridgeSession session = service.getCommonCartridgeSessionBySessionId(sessionId);
	    commonCartridgeUser = new CommonCartridgeUser(user, session);
	    service.createUser(commonCartridgeUser);
	}
	return commonCartridgeUser;
    }

    private CommonCartridgeUser getSpecifiedUser(ICommonCartridgeService service, Long sessionId, Integer userId) {
	CommonCartridgeUser commonCartridgeUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (commonCartridgeUser == null) {
	    LearningController.log.error(
		    "Unable to find specified user for commonCartridge activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return commonCartridgeUser;
    }

    /**
     * Set complete flag for given commonCartridge item.
     *
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request) {
	// get back sessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	Long commonCartridgeItemUid = new Long(request.getParameter(CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID));
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(CommonCartridgeConstants.ATTR_TOOL_SESSION_ID);
	commonCartridgeService.setItemComplete(commonCartridgeItemUid, new Long(user.getUserID().intValue()),
		sessionId);

	// set commonCartridge item complete tag
	SortedSet<CommonCartridgeItem> commonCartridgeItemList = getCommonCartridgeItemList(sessionMap);
	for (CommonCartridgeItem item : commonCartridgeItemList) {
	    if (item.getUid().equals(commonCartridgeItemUid)) {
		item.setComplete(true);
		break;
	    }
	}
    }

}
