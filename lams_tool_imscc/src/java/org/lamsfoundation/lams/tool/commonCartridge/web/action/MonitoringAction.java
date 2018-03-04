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


package org.lamsfoundation.lams.tool.commonCartridge.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.commonCartridge.dto.Summary;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeSession;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String param = mapping.getParameter();

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("listuser")) {
	    return listuser(mapping, form, request, response);
	}
	if (param.equals("showitem")) {
	    return showitem(mapping, form, request, response);
	}
	if (param.equals("hideitem")) {
	    return hideitem(mapping, form, request, response);
	}
	if (param.equals("viewReflection")) {
	    return viewReflection(mapping, form, request, response);
	}

	return mapping.findForward(CommonCartridgeConstants.ERROR);
    }

    private ActionForward hideitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	Long toolContentId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);

	Long itemUid = WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);
	ICommonCartridgeService service = getCommonCartridgeService();
	service.setItemVisible(itemUid, toolContentId, false);

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

	return mapping.findForward(CommonCartridgeConstants.SUCCESS);
    }

    private ActionForward showitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(CommonCartridgeConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	Long toolContentId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	
	Long itemUid = WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);
	ICommonCartridgeService service = getCommonCartridgeService();
	service.setItemVisible(itemUid, toolContentId, true);

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
	return mapping.findForward(CommonCartridgeConstants.SUCCESS);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(CommonCartridgeConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ICommonCartridgeService service = getCommonCartridgeService();
	List<List<Summary>> summaryList = service.getSummary(contentId);

	CommonCartridge commonCartridge = service.getCommonCartridgeByContentId(contentId);

	Map<Long, Set<ReflectDTO>> relectList = service.getReflectList(contentId, false);

	// cache into sessionMap
	sessionMap.put(CommonCartridgeConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(CommonCartridgeConstants.PAGE_EDITABLE, commonCartridge.isContentInUse());
	sessionMap.put(CommonCartridgeConstants.ATTR_RESOURCE, commonCartridge);
	sessionMap.put(CommonCartridgeConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(CommonCartridgeConstants.ATTR_REFLECT_LIST, relectList);
	return mapping.findForward(CommonCartridgeConstants.SUCCESS);
    }

    private ActionForward listuser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long itemUid = WebUtil.readLongParam(request, CommonCartridgeConstants.PARAM_RESOURCE_ITEM_UID);

	// get user list by given item uid
	ICommonCartridgeService service = getCommonCartridgeService();
	List list = service.getUserListBySessionItem(sessionId, itemUid);

	// set to request
	request.setAttribute(CommonCartridgeConstants.ATTR_USER_LIST, list);
	return mapping.findForward(CommonCartridgeConstants.SUCCESS);
    }

    private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long uid = WebUtil.readLongParam(request, CommonCartridgeConstants.ATTR_USER_UID);
	Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	ICommonCartridgeService service = getCommonCartridgeService();
	CommonCartridgeUser user = service.getUser(uid);
	NotebookEntry notebookEntry = service.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		CommonCartridgeConstants.TOOL_SIGNATURE, user.getUserId().intValue());

	CommonCartridgeSession session = service.getCommonCartridgeSessionBySessionId(sessionID);

	ReflectDTO refDTO = new ReflectDTO(user);
	if (notebookEntry == null) {
	    refDTO.setFinishReflection(false);
	    refDTO.setReflect(null);
	} else {
	    refDTO.setFinishReflection(true);
	    refDTO.setReflect(notebookEntry.getEntry());
	}
	refDTO.setReflectInstrctions(session.getCommonCartridge().getReflectInstructions());

	request.setAttribute("userDTO", refDTO);
	return mapping.findForward("success");
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private ICommonCartridgeService getCommonCartridgeService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ICommonCartridgeService) wac.getBean(CommonCartridgeConstants.RESOURCE_SERVICE);
    }
}
