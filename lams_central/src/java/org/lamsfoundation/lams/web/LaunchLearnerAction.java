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
/* $$Id$$ */
package org.lamsfoundation.lams.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Launch a LAMS learner window which contains the details of a lesson and a link
 * to start learner. Format: launchlearner.do?lessonID=<lessonid>
 * 
 * @struts:action path="/launchlearner"
 * 				  validate="false"
 * 				  parameter="mod"
 * @struts:action-forward name="learner" path="/launchlearner.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 *
 */
public class LaunchLearnerAction extends LamsAction {
	
	private static Logger log = Logger.getLogger(LaunchLearnerAction.class);
	
	private static ILessonService lessonService;
	
	private ILessonService getLessonService(){
		if(lessonService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			lessonService = (ILessonService) ctx.getBean("lessonService");
		}
		return lessonService;
	}

	/**
	 * request for learner environment
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
			Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
			Lesson lesson = lessonId != null ? getLessonService().getLesson(lessonId) : null;
			if ( lesson != null ) {
//				return displayMessage(mapping, req, "message.lesson.not.started.cannot.participate");
			}
				
			req.setAttribute("name", lesson.getLessonName());
			req.setAttribute("description", lesson.getLessonDescription());
			req.setAttribute("status", lesson.getLessonStateId());
			req.setAttribute(AttributeNames.PARAM_LESSON_ID,lessonId);
			return mapping.findForward("learner");
			
		} catch (Exception e) {
			log.error("Failed to load learner",e);
			return mapping.findForward("error");
		}
	}

	private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
		req.setAttribute("messageKey", messageKey);
		return mapping.findForward("message");
	}
}