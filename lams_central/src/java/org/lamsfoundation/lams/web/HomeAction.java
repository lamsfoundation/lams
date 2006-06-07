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
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * this is an action where all lams client environments launch.
 * initial configuration of the individual environment setting is done here.
 * 
 * @struts:action path="/home"
 * 				  validate="false"
 * 				  parameter="method"
 * @struts:action-forward name="sysadmin" path="/sysadmin.jsp"
 * @struts:action-forward name="learner" path="/learner.jsp"
 * @struts:action-forward name="author" path="/author.jsp"
 * @struts:action-forward name="monitorLesson" path="/monitorLesson.jsp"
 * @struts:action-forward name="addLesson" path="/addLesson.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="passwordChange" path=".passwordChange"
 *
 */
public class HomeAction extends DispatchAction {
	
	private static Logger log = Logger.getLogger(HomeAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
	
	
	private boolean isUserInRole(Integer userId,int orgId, String roleName)
	{
		if (service.getUserOrganisationRole(userId, new Integer(orgId),roleName)==null)
			return false;
		return true;
	}
	
	private UserDTO getUser() {
		HttpSession ss = SessionManager.getSession();
		return (UserDTO) ss.getAttribute(AttributeNames.USER);
	}
	


	/**
	 * request for sysadmin environment
	 */
	public ActionForward sysadmin(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
			log.debug("request sysadmin");
			int orgId = new Integer(req.getParameter("orgId")).intValue();
			UserDTO user = getUser();
			if ( user == null ) {
				log.error("admin: User missing from session. ");
				return mapping.findForward("error");
			} else if ( isUserInRole(user.getUserID(),orgId,Role.SYSADMIN)) {
				log.debug("user is sysadmin");
				return mapping.findForward("sysadmin");
			} else {
				log.error("User "+user.getLogin()+" tried to get sysadmin screen but isn't sysadmin in organisation: "+orgId);
				return mapping.findForward("error");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
	
	/**
	 * request for learner environment
	 */
	public ActionForward learner(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
			log.debug("request learner");
			
			Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
			UserDTO user = getUser();
			if ( user == null ) {
				log.error("admin: User missing from session. ");
				return mapping.findForward("error");
			} else { 
				// TODO check that the user is in the learner group for the lesson
				String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
				req.setAttribute("serverUrl", serverUrl);
				req.setAttribute(AttributeNames.PARAM_LESSON_ID,lessonId);
				return mapping.findForward("learner");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
	
	
	/**
	 * request for author environment
	 */
	public ActionForward author(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		try {
			log.debug("request author");
			UserDTO user = getUser();
			if ( user == null ) {
				log.error("admin: User missing from session. ");
				return mapping.findForward("error");
			} else {
				// todo check user really does have authoring permission in some organisation.
				String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
				req.setAttribute("serverUrl", serverUrl);
				return mapping.findForward("author");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
	
	
	/**
	 * request for monitor environment
	 */
	public ActionForward monitorLesson(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

			try {
			log.debug("request monitorLesson");
			int lessonId = WebUtil.readIntParam(req, AttributeNames.PARAM_LESSON_ID);
			UserDTO user = getUser();
			if ( user == null ) {
				log.error("admin: User missing from session. ");
				return mapping.findForward("error");
			} else { 
				// TODO check that the user is in the staff group for the lesson
				log.debug("user is staff");
				String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
				req.setAttribute("serverUrl", serverUrl);
				req.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
				return mapping.findForward("monitorLesson");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}

	/**
	 * request for add lesson wizard
	 */
	public ActionForward addLesson(ActionMapping mapping, ActionForm form, 
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

			try {
			log.debug("request addLesson");
			Integer courseId = WebUtil.readIntParam(req, AttributeNames.PARAM_COURSE_ID, false);
			Integer classId = WebUtil.readIntParam(req, AttributeNames.PARAM_CLASS_ID, true);
			UserDTO user = getUser();
			if ( user == null ) {
				log.error("admin: User missing from session. ");
				return mapping.findForward("error");
			} else { 
				Integer orgId = classId != null ? classId : courseId;
				if (isUserInRole(user.getUserID(), orgId, Role.STAFF)) {
					log.debug("user is staff");
					String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
					req.setAttribute("serverUrl", serverUrl);
					req.setAttribute(AttributeNames.PARAM_COURSE_ID, courseId);
					req.setAttribute(AttributeNames.PARAM_CLASS_ID, classId);
					return mapping.findForward("addLesson");
				} else {
					log.error("User "+ user.getLogin() + " tried to get staff screen but isn't staff in organisation: " + orgId);
					return mapping.findForward("error");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return mapping.findForward("error");
		}
	}
	
}