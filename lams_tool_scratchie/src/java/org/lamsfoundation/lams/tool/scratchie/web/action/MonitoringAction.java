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
package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.FileUtil;
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
	if (param.equals("itemSummary")) {
	    return itemSummary(mapping, form, request, response);
	}
	if (param.equals("manageLeaders")) {
	    return manageLeaders(mapping, form, request, response);
	}
	if (param.equals("saveLeaders")) {
	    return saveLeaders(mapping, form, request, response);
	}
	if (param.equals("exportExcel")) {
	    return exportExcel(mapping, form, request, response);
	}

	return mapping.findForward(ScratchieConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IScratchieService service = getScratchieService();
	List<GroupSummary> summaryList = service.getMonitoringSummary(contentId);

	Scratchie scratchie = service.getScratchieByContentId(contentId);
	scratchie.toDTO();
	Set<ScratchieUser> learners = service.getAllLearners(contentId);

	// cache into sessionMap
	boolean isGroupedActivity = service.isGroupedActivity(contentId);
	sessionMap.put(ScratchieConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put(ScratchieConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(ScratchieConstants.PAGE_EDITABLE, scratchie.isContentInUse());
	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	sessionMap.put(ScratchieConstants.ATTR_LEARNERS, learners);
	sessionMap.put(ScratchieConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, scratchie.isReflectOnActivity());

	// Create reflectList if reflection is enabled.
	if (scratchie.isReflectOnActivity()) {
	    
	    List<ReflectDTO> reflections = service.getReflectionList(contentId);
	    sessionMap.put(ScratchieConstants.ATTR_REFLECTIONS, reflections);
	}

	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    private ActionForward itemSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long itemUid = WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID);
	if (itemUid.equals(-1)) {
	    return null;
	}
	ScratchieItem item = getScratchieService().getScratchieItemByUid(itemUid);
	request.setAttribute(ScratchieConstants.ATTR_ITEM, item);

	Long contentId = (Long) sessionMap.get(ScratchieConstants.ATTR_TOOL_CONTENT_ID);
	List<GroupSummary> summaryList = getScratchieService().getQuestionSummary(contentId, itemUid);

	request.setAttribute(ScratchieConstants.ATTR_SUMMARY_LIST, summaryList);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Show leaders manage page
     */
    private ActionForward manageLeaders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Save selected users as a leaders
     */
    private ActionForward saveLeaders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	IScratchieService service = getScratchieService();

	List<GroupSummary> summaryList = (List<GroupSummary>) sessionMap.get(ScratchieConstants.ATTR_SUMMARY_LIST);
	for (GroupSummary summary : summaryList) {
	    Long toolSessionId = summary.getSessionId();
	    Long leaderUserId = WebUtil.readLongParam(request, "sessionId" + toolSessionId, true);

	    // save selected users as a leaders
	    if (leaderUserId != null) {
		service.setGroupLeader(leaderUserId, toolSessionId);
	    }
	}

	// refresh users leadership status in summaryList
	Long contentId = (Long) sessionMap.get(ScratchieConstants.ATTR_TOOL_CONTENT_ID);
	summaryList = service.getMonitoringSummary(contentId);
	sessionMap.put(ScratchieConstants.ATTR_SUMMARY_LIST, summaryList);

	return null;
    }

    private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long uid = WebUtil.readLongParam(request, ScratchieConstants.ATTR_USER_UID);
	Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	IScratchieService service = getScratchieService();
	ScratchieUser user = service.getUser(uid);
	NotebookEntry notebookEntry = service.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, user.getUserId().intValue());

	ScratchieSession session = service.getScratchieSessionBySessionId(sessionID);

	return mapping.findForward("success");
    }

    /**
     * Exports tool results into excel.
     * @throws IOException 
     */
    public ActionForward exportExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException  {

	IScratchieService scratchieService = getScratchieService();
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Scratchie scratchie = (Scratchie) sessionMap.get(ScratchieConstants.ATTR_SCRATCHIE);
	
	LinkedHashMap<String, ExcelCell[][]> dataToExport = scratchieService.exportExcel(scratchie.getContentId());

	String fileName = "scratchie_export.xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, dataToExport, null, false);

	return null;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IScratchieService getScratchieService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IScratchieService) wac.getBean(ScratchieConstants.RESOURCE_SERVICE);
    }
}
