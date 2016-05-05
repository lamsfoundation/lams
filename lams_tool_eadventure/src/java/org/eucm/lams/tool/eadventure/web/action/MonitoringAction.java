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

/* $Id$ */
package org.eucm.lams.tool.eadventure.web.action;

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
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.dto.ReflectDTO;
import org.eucm.lams.tool.eadventure.dto.Summary;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureSession;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.model.EadventureVars;
import org.eucm.lams.tool.eadventure.service.IEadventureService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
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
	if (param.equals("showReport")) {
	    return showReport(mapping, form, request, response);
	}

	return mapping.findForward(EadventureConstants.ERROR);
    }

    private ActionForward showReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	//SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	//request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	IEadventureService service = getEadventureService();
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	EadventureSession session = service.getEadventureSessionBySessionId(sessionId);
	Eadventure ead = session.getEadventure();
	Long uid = WebUtil.readLongParam(request, EadventureConstants.ATTR_USER_UID);
	EadventureUser user = service.getUser(uid);

	//TODO gestionar el report
	//EadventureResult result = service.getLastEadventureResult(ead.getUid(), user.getUserId());
	//
	EadventureItemVisitLog log = service.getEadventureItemLog(ead.getUid(), user.getUserId());

	EadventureVars var = service.getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_REPORT);
	request.setAttribute("html", var.getValue());
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    private ActionForward hideitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long itemUid = WebUtil.readLongParam(request, EadventureConstants.PARAM_RESOURCE_ITEM_UID);
	IEadventureService service = getEadventureService();
	service.setItemVisible(itemUid, false);

	// get back SessionMap
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// update session value
	List<List> groupList = (List<List>) sessionMap.get(EadventureConstants.ATTR_SUMMARY_LIST);
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

	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    private ActionForward showitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long itemUid = WebUtil.readLongParam(request, EadventureConstants.PARAM_RESOURCE_ITEM_UID);
	IEadventureService service = getEadventureService();
	service.setItemVisible(itemUid, true);

	// get back SessionMap
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// update session value
	List<List> groupList = (List<List>) sessionMap.get(EadventureConstants.ATTR_SUMMARY_LIST);
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
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IEadventureService service = getEadventureService();
	List<Summary> groupList = service.getSummary(contentId);

	Eadventure eadventure = service.getEadventureByContentId(contentId);

	Map<Long, Set<ReflectDTO>> relectList = service.getReflectList(contentId, false);

	// cache into sessionMap
	sessionMap.put(EadventureConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(EadventureConstants.PAGE_EDITABLE, eadventure.isContentInUse());
	sessionMap.put(EadventureConstants.ATTR_EADVENTURE, eadventure);
	sessionMap.put(EadventureConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(EadventureConstants.ATTR_REFLECT_LIST, relectList);
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    private ActionForward listuser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long itemUid = WebUtil.readLongParam(request, EadventureConstants.PARAM_RESOURCE_ITEM_UID);

	// get user list by given item uid
	IEadventureService service = getEadventureService();
	List list = service.getUserListBySessionItem(sessionId, itemUid);

	// set to request
	request.setAttribute(EadventureConstants.ATTR_USER_LIST, list);
	// check if there are associated report
	EadventureSession session = service.getEadventureSessionBySessionId(sessionId);
	Eadventure ead = session.getEadventure();
	Long uid = WebUtil.readLongParam(request, EadventureConstants.ATTR_USER_UID);
	EadventureUser user = service.getUser(uid);

	//TODO gestionar el report
	//EadventureResult result = service.getLastEadventureResult(ead.getUid(), user.getUserId());
	EadventureItemVisitLog log = service.getEadventureItemLog(ead.getUid(), user.getUserId());

	EadventureVars var = service.getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_REPORT);

	int exist;
	if (var != null) {
	    exist = 0;
	} else {
	    exist = 1;
	}
	String sessionMapID = request.getParameter(EadventureConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(EadventureConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	sessionMap.put("existFile", exist);
	//request.setAttribute("existFile",exist );
	return mapping.findForward(EadventureConstants.SUCCESS);
    }

    private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long uid = WebUtil.readLongParam(request, EadventureConstants.ATTR_USER_UID);
	Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	IEadventureService service = getEadventureService();
	EadventureUser user = service.getUser(uid);
	NotebookEntry notebookEntry = service.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		EadventureConstants.TOOL_SIGNATURE, user.getUserId().intValue());

	EadventureSession session = service.getEadventureSessionBySessionId(sessionID);

	ReflectDTO refDTO = new ReflectDTO(user);
	if (notebookEntry == null) {
	    refDTO.setFinishReflection(false);
	    refDTO.setReflect(null);
	} else {
	    refDTO.setFinishReflection(true);
	    refDTO.setReflect(notebookEntry.getEntry());
	}
	refDTO.setReflectInstrctions(session.getEadventure().getReflectInstructions());

	request.setAttribute("userDTO", refDTO);
	return mapping.findForward("success");
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IEadventureService getEadventureService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IEadventureService) wac.getBean(EadventureConstants.RESOURCE_SERVICE);
    }
}
