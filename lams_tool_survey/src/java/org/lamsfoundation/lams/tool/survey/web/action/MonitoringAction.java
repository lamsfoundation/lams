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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.IOException;
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
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
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

		if (param.equals("summary")) {
			return summary(mapping, form, request, response);
		}

		if (param.equals("listuser")) {
			return listuser(mapping, form, request, response);
		}

		if (param.equals("viewReflection")) {
			return viewReflection(mapping, form, request, response);
		}
		

		return mapping.findForward(SurveyConstants.ERROR);
	}


	private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		
		Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
		ISurveyService service = getSurveyService();
//		List<List<Summary>> groupList = service.getSummary(contentId);
//		
//		Survey survey = service.getSurveyByContentId(contentId);
//		survey.toDTO();
//		
		Map<Long,Set<ReflectDTO> >relectList = service.getReflectList(contentId);
//		
//		//cache into sessionMap
//		sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, groupList);
//		sessionMap.put(SurveyConstants.PAGE_EDITABLE, new Boolean(SurveyWebUtils.isSurveyEditable(survey)));
//		sessionMap.put(SurveyConstants.ATTR_RESOURCE, survey);
//		sessionMap.put(SurveyConstants.ATTR_TOOL_CONTENT_ID, contentId);
		sessionMap.put(SurveyConstants.ATTR_REFLECT_LIST, relectList);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}

	private ActionForward listuser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		Long itemUid = WebUtil.readLongParam(request, SurveyConstants.PARAM_RESOURCE_ITEM_UID);

		//get user list by given item uid
		ISurveyService service = getSurveyService();
//		List list = service.getUserListBySessionItem(sessionId, itemUid);
		
		//set to request
//		request.setAttribute(SurveyConstants.ATTR_USER_LIST, list);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	
	private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long uid = WebUtil.readLongParam(request, SurveyConstants.ATTR_USER_UID); 
		Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		
		ISurveyService service = getSurveyService();
		SurveyUser user = service.getUser(uid);
		NotebookEntry notebookEntry = service.getEntry(sessionID, 
				CoreNotebookConstants.NOTEBOOK_TOOL, 
				SurveyConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		
		SurveySession session = service.getSurveySessionBySessionId(sessionID);
		
		ReflectDTO refDTO = new ReflectDTO(user);
		if(notebookEntry == null){
			refDTO.setFinishReflection(false);
			refDTO.setReflect(null);
		}else{
			refDTO.setFinishReflection(true);
			refDTO.setReflect(notebookEntry.getEntry());
		}
		refDTO.setReflectInstrctions(session.getSurvey().getReflectInstructions());
		
		request.setAttribute("userDTO", refDTO);
		return mapping.findForward("success");
	}	

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	private ISurveyService getSurveyService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
				.getServletContext());
		return (ISurveyService) wac.getBean(SurveyConstants.RESOURCE_SERVICE);
	}	
}
