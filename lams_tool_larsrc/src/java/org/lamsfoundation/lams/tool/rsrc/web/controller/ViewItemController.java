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

package org.lamsfoundation.lams.tool.rsrc.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dto.InstructionNavDTO;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewItemController {
    @Autowired
    private IResourceService resourceService;

    /**
     * Display main frame to display instrcution and item content.
     *
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/reviewItem")
    private String reviewItem(HttpServletRequest request) throws UnsupportedEncodingException {

	SessionMap<String, Object> sessionMap = null;
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID, true);
	if (sessionMapID == null) {
	    sessionMap = new SessionMap<>();
	    sessionMapID = sessionMap.getSessionID();
	    request.getSession().setAttribute(sessionMapID, sessionMap);
	} else {
	    sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	}

	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	ResourceItem item = getResourceItem(request, sessionMap, mode);

	String idStr = request.getParameter(ResourceConstants.ATTR_TOOL_SESSION_ID);
	Long sessionId = NumberUtils.createLong(idStr);
	// mark this item access flag if it is learner
	if (ToolAccessMode.LEARNER.toString().equals(mode)) {
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    resourceService.setItemAccess(item.getUid(), new Long(user.getUserID().intValue()), sessionId);
	}

	if (item == null) {
	    return "error";
	}
	if (item.getType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT) {
	    sessionMap.put(ResourceConstants.ATT_LEARNING_OBJECT, item);
	}

	Integer itemIdx = WebUtil.readIntParam(request, ResourceConstants.PARAM_ITEM_INDEX, true);
	String reviewUrl = getReviewUrl(item, sessionMapID);
	request.setAttribute(ResourceConstants.ATTR_RESOURCE_REVIEW_URL, reviewUrl);
	if (item.getType() == ResourceConstants.RESOURCE_TYPE_URL) {
	    request.setAttribute(ResourceConstants.ATTR_ENCODED_RESOURCE_REVIEW_URL,
		    URLEncoder.encode(reviewUrl, "UTF-8"));
	}
	request.setAttribute(ResourceConstants.ATTR_IS_DOWNLOAD, item.getType() == ResourceConstants.RESOURCE_TYPE_FILE
		|| item.getType() == ResourceConstants.RESOURCE_TYPE_WEBSITE);

	request.setAttribute(ResourceConstants.ATTR_ALLOW_COMMENTS, item.isAllowComments());

	// these attribute will be use to instruction navigator page
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(ResourceConstants.PARAM_ITEM_INDEX, itemIdx);
	Long itemUid = NumberUtils.createLong(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	request.setAttribute(ResourceConstants.PARAM_RESOURCE_ITEM_UID, itemUid);
	request.setAttribute(ResourceConstants.ATTR_TOOL_SESSION_ID, sessionId);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return "pages/itemreview/mainframe";
    }

    /**
     * Return next instruction to page. It need four input parameters, mode, itemIndex or itemUid, and insIdx.
     */
    @RequestMapping("/nextInstruction")
    private String nextInstruction(HttpServletRequest request) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);

	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sesionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	ResourceItem item = getResourceItem(request, sesionMap, mode);
	if (item == null) {
	    return "error";
	}

	Integer currIns = WebUtil.readIntParam(request, ResourceConstants.PARAM_CURRENT_INSTRUCTION_INDEX, true);
	if (currIns == null) {
	    currIns = 0;
	}

	Set<ResourceItemInstruction> instructions = item.getItemInstructions();
	InstructionNavDTO navDto = new InstructionNavDTO();
	// For Learner upload item, its instruction will display description/comment fields in ReosourceItem.
	if (!item.isCreateByAuthor()) {
	    List<ResourceItemInstruction> navItems = new ArrayList<>(1);
	    // create a new instruction and put ResourceItem description into it: just for display use.
	    ResourceItemInstruction ins = new ResourceItemInstruction();
	    ins.setSequenceId(1);
	    ins.setDescription(item.getDescription());
	    navItems.add(ins);
	    navDto.setAllInstructions(navItems);
	    instructions.add(ins);
	} else {
	    navDto.setAllInstructions(new ArrayList<>(instructions));
	}
	navDto.setTitle(item.getTitle());
	navDto.setType(item.getType());
	navDto.setTotal(instructions.size());
	if (instructions.size() > 0) {
	    navDto.setInstruction(new ArrayList<>(instructions).get(currIns));
	    navDto.setCurrent(currIns + 1);
	} else {
	    navDto.setCurrent(0);
	    navDto.setInstruction(null);
	}

	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	request.setAttribute(ResourceConstants.ATTR_RESOURCE_INSTRUCTION, navDto);
	return "pages/itemreview/instructionsnav";
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    /**
     * Return resource item according to ToolAccessMode.
     *
     * @param request
     * @param sessionMap
     * @param mode
     * @return
     */
    private ResourceItem getResourceItem(HttpServletRequest request, SessionMap<String, Object> sessionMap,
	    String mode) {
	ResourceItem item = null;
	if (ResourceConstants.MODE_AUTHOR_SESSION.equals(mode)) {
	    int itemIdx = NumberUtils.stringToInt(request.getParameter(ResourceConstants.PARAM_ITEM_INDEX), 0);
	    // authoring: does not save item yet, so only has ItemList from session and identity by Index
	    List<ResourceItem> resourceList = new ArrayList<>(getResourceItemList(sessionMap));
	    item = resourceList.get(itemIdx);
	} else {
	    Long itemUid = NumberUtils.createLong(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	    // get back the resource and item list and display them on page
	    item = resourceService.getResourceItemByUid(itemUid);
	}
	return item;
    }

    private static Pattern protocolExists = Pattern.compile("http://|https://|ftp://|nntp://",
	    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final String DEFAULT_PROTOCOL = "http://";
    public static final String HTTPS_SCHEME = "https";

    private String getReviewUrl(ResourceItem item, String sessionMapID) {
	String url = null;
	switch (item.getType()) {
	    case ResourceConstants.RESOURCE_TYPE_URL:
		// protocol missing? Assume http. Must do before the popup checks otherwise LDEV-4503 case may be missed.
		url = item.getUrl();
		if (!protocolExists.matcher(url).find()) {
		    url = DEFAULT_PROTOCOL + url;
		}
		break;
	    case ResourceConstants.RESOURCE_TYPE_FILE:
	    case ResourceConstants.RESOURCE_TYPE_WEBSITE:
		url = "/download/?uuid=" + item.getFileUuid() + "&preferDownload=true";
		break;

	    case ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT:
		url = Configuration.get(ConfigurationKeys.SERVER_URL) + "pages/learningobj/mainframe.jsp?sessionMapID="
			+ sessionMapID;
		break;
	}
	return url;
    }

    /**
     * List save current resource items.
     *
     * @param request
     * @return
     */
    private SortedSet<ResourceItem> getResourceItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<ResourceItem> list = (SortedSet) sessionMap.get(ResourceConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new ResourceItemComparator());
	    sessionMap.put(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

}
