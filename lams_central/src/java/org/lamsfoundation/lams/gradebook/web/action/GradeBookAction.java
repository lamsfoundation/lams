/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.gradebook.web.action;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRow;
import org.lamsfoundation.lams.gradebook.service.IGradeBookService;
import org.lamsfoundation.lams.gradebook.util.GradeBookConstants;
import org.lamsfoundation.lams.gradebook.util.GradeBookUtil;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author lfoxton
 * 
 * Handles the general requests for content in gradebook
 * 
 * 
 * @struts.action path="/gradebook/gradebook" parameter="dispatch"
 *                scope="request" name="gradeBookForm" validate="false"
 * 
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class GradeBookAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(GradeBookAction.class);

    private static IGradeBookService gradeBookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return null;
    }

    /**
     * Returns an xml representation of a user's lesson gradebook data. It is
     * essential a list of the activities and the user's output for those
     * activities.
     * 
     * This is in the sub-grid area of gradebook, and is called when the teacher
     * clicks to expand the gradebook info for a specified user
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getLessonGradeBookDataForUser(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	initServices();
	int page = WebUtil.readIntParam(request, GradeBookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradeBookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SIDX, true);
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String login = WebUtil.readStrParam(request, GradeBookConstants.PARAM_LOGIN);

	Lesson lesson = lessonService.getLesson(lessonID);
	User learner = userService.getUserByLogin(login);
	
	

	if (lesson != null && learner != null) {
	    
	    Collection<GradeBookGridRow> gradeBookActivityDTOs = gradeBookService.getUserGradeBookActivityDTOs(lesson, learner);
	    String ret = GradeBookUtil.toGridXML(gradeBookActivityDTOs, page, rowLimit);

	    response.setContentType("text/xml");
	    PrintWriter out = response.getWriter();
	    out.print(ret);

	} else {
	    // Handle error
	}

	return null;
    }
    
    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    public static IGradeBookService getGradeBookService() {
	return gradeBookService;
    }

    public static IUserManagementService getUserService() {
	return userService;
    }

    public static ILessonService getLessonService() {
	return lessonService;
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private void initServices() {
	ServletContext context = this.getServlet().getServletContext();

	if (gradeBookService == null)
	    gradeBookService = (IGradeBookService) WebApplicationContextUtils.getRequiredWebApplicationContext(context)
		    .getBean("gradeBookService");

	if (userService == null)
	    userService = (IUserManagementService) WebApplicationContextUtils.getRequiredWebApplicationContext(context)
		    .getBean("userManagementService");

	if (lessonService == null)
	    lessonService = (ILessonService) WebApplicationContextUtils.getRequiredWebApplicationContext(context)
		    .getBean("lessonService");
    }
}
