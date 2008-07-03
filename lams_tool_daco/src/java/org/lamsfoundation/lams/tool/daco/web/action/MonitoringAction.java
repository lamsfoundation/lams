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
package org.lamsfoundation.lams.tool.daco.web.action;

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
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.daco.dto.Summary;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
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

		if (param.equals("listuser")) {
			return listuser(mapping, form, request, response);
		}
		if (param.equals("showquestion")) {
			return showquestion(mapping, form, request, response);
		}
		if (param.equals("hidequestion")) {
			return hidequestion(mapping, form, request, response);
		}
		if (param.equals("viewReflection")) {
			return viewReflection(mapping, form, request, response);
		}
		

		return mapping.findForward(DacoConstants.ERROR);
	}


	private ActionForward hidequestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long questionUid = WebUtil.readLongParam(request, DacoConstants.PARAM_DACO_QUESTION_UID);
		IDacoService service = getDacoService();
		service.setQuestionVisible(questionUid,false);
		
		//get back SessionMap
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		
		//update session value
		List<List> groupList = (List<List>) sessionMap.get(DacoConstants.ATTR_SUMMARY_LIST);
		if(groupList != null)
			for(List<Summary> group : groupList){
				for(Summary sum: group){
					if(questionUid.equals(sum.getQuestionUid())){
						sum.setQuestionHide(true);
						break;
					}
				}
			}

		return mapping.findForward(DacoConstants.SUCCESS);
	}

	private ActionForward showquestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long questionUid = WebUtil.readLongParam(request, DacoConstants.PARAM_DACO_QUESTION_UID);
		IDacoService service = getDacoService();
		service.setQuestionVisible(questionUid,true);
		
		//get back SessionMap
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		
		//update session value
		List<List> groupList = (List<List>) sessionMap.get(DacoConstants.ATTR_SUMMARY_LIST);
		if(groupList != null)
			for(List<Summary> group : groupList){
				for(Summary sum: group){
					if(questionUid.equals(sum.getQuestionUid())){
						sum.setQuestionHide(false);
						break;
					}
				}
			}
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
//		save contentFolderID into session
		sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,WebUtil.readStrParam(request,AttributeNames.PARAM_CONTENT_FOLDER_ID));

		Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
		IDacoService service = getDacoService();
		List<List<Summary>> groupList = service.getSummary(contentId);
		
		Daco daco = service.getDacoByContentId(contentId);
		daco.toDTO();
		
		Map<Long,Set<ReflectDTO> >relectList = service.getReflectList(contentId);
		
		//cache into sessionMap
		sessionMap.put(DacoConstants.ATTR_SUMMARY_LIST, groupList);
		sessionMap.put(DacoConstants.PAGE_EDITABLE, daco.isContentInUse());
		sessionMap.put(DacoConstants.ATTR_DACO, daco);
		sessionMap.put(DacoConstants.ATTR_TOOL_CONTENT_ID, contentId);
		sessionMap.put(DacoConstants.ATTR_REFLECT_LIST, relectList);
		
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	private ActionForward listuser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		Long questionUid = WebUtil.readLongParam(request, DacoConstants.PARAM_DACO_QUESTION_UID);

		//get user list by given question uid
		IDacoService service = getDacoService();
		List list = service.getUserListBySessionQuestion(sessionId, questionUid);
		
		//set to request
		request.setAttribute(DacoConstants.ATTR_USER_LIST, list);
		return mapping.findForward(DacoConstants.SUCCESS);
	}
	
	private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long uid = WebUtil.readLongParam(request, DacoConstants.ATTR_USER_UID); 
		Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		
		IDacoService service = getDacoService();
		DacoUser user = service.getUser(uid);
		NotebookEntry notebookEntry = service.getEntry(sessionID, 
				CoreNotebookConstants.NOTEBOOK_TOOL, 
				DacoConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		
		DacoSession session = service.getDacoSessionBySessionId(sessionID);
		
		ReflectDTO refDTO = new ReflectDTO(user);
		if(notebookEntry == null){
			refDTO.setFinishReflection(false);
			refDTO.setReflect(null);
		}else{
			refDTO.setFinishReflection(true);
			refDTO.setReflect(notebookEntry.getEntry());
		}
		refDTO.setReflectInstrctions(session.getDaco().getReflectInstructions());
		
		request.setAttribute("userDTO", refDTO);
		return mapping.findForward("success");
	}	

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	private IDacoService getDacoService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
				.getServletContext());
		return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
	}	
}
