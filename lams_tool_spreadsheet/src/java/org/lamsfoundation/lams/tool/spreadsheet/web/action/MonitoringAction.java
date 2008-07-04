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
package org.lamsfoundation.lams.tool.spreadsheet.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.Summary;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
	public static Logger log = Logger.getLogger(MonitoringAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String param = mapping.getParameter();

		request.setAttribute("initialTabId",WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB,true));
		
		if (param.equals("summary")) {
			return summary(mapping, form, request, response);
		}
		if (param.equals("doStatistic")) {
			return doStatistic(mapping, form, request, response);
		}
		if (param.equals("editMark")) {
			return editMark(mapping, form, request, response);
		}
//		if (param.equals("listuser")) {
//			return listuser(mapping, form, request, response);
//		}
		if (param.equals("viewReflection")) {
			return viewReflection(mapping, form, request, response);
		}

		return mapping.findForward(SpreadsheetConstants.ERROR);
	}

	private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
//		save contentFolderID into session
		sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,WebUtil.readStrParam(request,AttributeNames.PARAM_CONTENT_FOLDER_ID));

		Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
		ISpreadsheetService service = getSpreadsheetService();
		List<Summary> summaryList = service.getSummary(contentId);
		
    	List<StatisticDTO> statisticList = getSpreadsheetService().getStatistics(contentId);
		request.setAttribute(SpreadsheetConstants.ATTR_STATISTIC_LIST, statisticList);
		
		Spreadsheet spreadsheet = service.getSpreadsheetByContentId(contentId);
		spreadsheet.toDTO();
		
		Map<Long, Set<ReflectDTO>>reflectList = service.getReflectList(contentId, false);
		
		//cache into sessionMap
		sessionMap.put(SpreadsheetConstants.ATTR_SUMMARY_LIST, summaryList);
		sessionMap.put(SpreadsheetConstants.PAGE_EDITABLE, spreadsheet.isContentInUse());
		sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE, spreadsheet);
		sessionMap.put(SpreadsheetConstants.ATTR_TOOL_CONTENT_ID, contentId);
		sessionMap.put(SpreadsheetConstants.ATTR_REFLECT_LIST, reflectList);
		
		return mapping.findForward(SpreadsheetConstants.SUCCESS);
	}
	
    /**
     * AJAX call to refresh statistic page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward doStatistic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
    	List<StatisticDTO> statisticList = getSpreadsheetService().getStatistics(contentId);
		request.setAttribute(SpreadsheetConstants.ATTR_STATISTIC_LIST, statisticList);
		
		return mapping.findForward("statistic");
		
    }
    
	public ActionForward editMark(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		Long userId = WebUtil.readLongParam(request, "userID");

		SpreadsheetUser user = getSpreadsheetService().getUser(userId);
		
//		user.getUserEditedSpreadsheet().getMark().
		
//		submitFilesService = getSubmitFilesService();
//		//return FileDetailsDTO list according to the given userID and sessionID
//		List files = submitFilesService.getFilesUploadedByUser(userID,sessionID);
//		
//		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
//		request.setAttribute("report",files);
		return mapping.findForward("success");
	}

//	private ActionForward listuser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
//		Long itemUid = WebUtil.readLongParam(request, SpreadsheetConstants.PARAM_RESOURCE_ITEM_UID);
////
////		//get user list by given item uid
//		ISpreadsheetService service = getSpreadsheetService();
//		//TODO
//		List list = null;
////		List list = service.getUserListBySessionItem(sessionId, itemUid);
//		
//		//set to request
//		request.setAttribute(SpreadsheetConstants.ATTR_USER_LIST, list);
//		return mapping.findForward(SpreadsheetConstants.SUCCESS);
//	}
	
	private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		Long uid = WebUtil.readLongParam(request, SpreadsheetConstants.ATTR_USER_UID); 
		
		ISpreadsheetService service = getSpreadsheetService();
		SpreadsheetUser user = service.getUser(uid);
		Long sessionID = user.getSession().getSessionId();		
		NotebookEntry notebookEntry = service.getEntry(sessionID, 
				CoreNotebookConstants.NOTEBOOK_TOOL, 
				SpreadsheetConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		
		SpreadsheetSession session = service.getSpreadsheetSessionBySessionId(sessionID);
		
		ReflectDTO refDTO = new ReflectDTO(user);
		if(notebookEntry == null){
			refDTO.setFinishReflection(false);
			refDTO.setReflect(null);
		}else{
			refDTO.setFinishReflection(true);
			refDTO.setReflect(notebookEntry.getEntry());
		}
		refDTO.setReflectInstrctions(session.getSpreadsheet().getReflectInstructions());
		
		request.setAttribute("userDTO", refDTO);
		return mapping.findForward("success");
	}	

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	
	/**
	 * Save statistic information into request
	 * @param request
	 * @param submitFilesSessionList
	 */
	private void statistic(HttpServletRequest request, List submitFilesSessionList) {
		Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
		List<StatisticDTO> statisticList = getSpreadsheetService().getStatistics(contentId);

	}
	
	private ISpreadsheetService getSpreadsheetService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
				.getServletContext());
		return (ISpreadsheetService) wac.getBean(SpreadsheetConstants.RESOURCE_SERVICE);
	}	
}
