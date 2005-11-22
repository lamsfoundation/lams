/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.web.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {

  	private static Logger log = Logger.getLogger(MonitoringAction.class);
	private IForumService forumService;
	
	 public final ActionForward execute(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response) throws Exception {
		 String param = mapping.getParameter();
		 
		 if (param.equals("listContentUsers")) {
			 return listContentUsers(mapping,form, request, response);
		 }
		 //***************** Marks Functions ********************
		 if (param.equals("viewAllMarks")) {
			 return viewAllMarks(mapping,form, request, response);
		 }
		 if (param.equals("releaseMarks")) {
			 return releaseMarks(mapping,form, request, response);
		 }
		 if (param.equals("downloadMarks")) {
			 return downloadMarks(mapping,form, request, response);
		 }
		 if (param.equals("viewUserMark")) {
			 return viewUserMark(mapping,form, request, response);
		 }
		 if (param.equals("editMark")) {
			 return editMark(mapping,form, request, response);
		 }
		 if (param.equals("updateMark")) {
			 return updateMark(mapping,form, request, response);
		 }
		 
		 //***************** Activity and Instructions ********************
		 if (param.equals("editActivity")) {
			 return editActivity(mapping,form, request, response);
		 }
		 if (param.equals("updateActivity")) {
			 return updateActivity(mapping,form, request, response);
		 }
		 if (param.equals("viewInstructions")) {
			 return viewInstructions(mapping,form, request, response);
		 }
		 //***************** Statistic ********************		 
		 if (param.equals("statistic")) {
			 return statistic(mapping,form, request, response);
		 }
		 
		 return mapping.findForward("error");
	 }

	private ActionForward listContentUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//get content ID from URL	
        Long contentID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
        
		forumService = getForumService();
        List sessionsList = forumService.getSessionsByContentId(contentID);

        Map sessionUserMap = new HashMap();
        //build a map with all users in the submitFilesSessionList
        Iterator it = sessionsList.iterator();
        while(it.hasNext()){
            Long sessionID = ((ForumToolSession)it.next()).getUid();
            List userList = forumService.getUsersBySessionId(sessionID);
            sessionUserMap.put(sessionID, userList);
        }
        
		//request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("sessionUserMap",sessionUserMap);
		return mapping.findForward("success");
	}

	private ActionForward viewAllMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward releaseMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward viewUserMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward editMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward updateMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward updateActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward viewInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	private ActionForward statistic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}


	//==========================================================================================
	// Utility methods
	//==========================================================================================
	/**
	 * Get Forum Service.
	 * 
	 * @return
	 */
  	private IForumService getForumService() {
  	    if ( forumService == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
  	    }
  	    return forumService;
  	}
}
