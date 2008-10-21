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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
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
 * @author lfoxton
 * 
 * Takes a relative path as a parameter and redirects the user there after they
 * have been authenticated. This allows direct linking to learner and monitor
 * from outside sources. It must be done through central as it is the only
 * project that offers this functionality
 * 
 * In order to prevent unauthorised access to learner and monitor pages, this 
 * action also takes parameters for access mode and tool session. It can then
 * check whether the current user is allowed to access the requested pages.
 * This is a temporary fix until the tool's actions can be improved to directly
 * handle permissions in all cases, see LDEV-1978
 * 
 * Parameters are shortened into single letters so that the entire URL is 
 * shorter, this is so email notifications do not have to produce large URLs, 
 * that may be cut off by email softwares
 * 
 * usage: 
 * 	/lams/r.do?r=<relativeUrl>&t=<toolSessionID>&a=<accessMode>
 * where:
 * 	relativeUrl = relative path to the resource
 * 	toolSessionID = tool session id valid for the lesson in question
 * 	a = access mode of the user (l = learner, t = teacher/monitor)
 * 
 * eg: 
 * 	/lams/r.do?
 *	r=%2Ftool%2Flawiki10%2Flearning.do%3Fmode%3Dlearner%26toolSessionID%3D13
 *	&t=13
 *	&a=l
 * 
 * @struts:action path="/r" validate="false"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 * 
 */
public class RedirectAction extends LamsAction {

    private static Logger log = Logger.getLogger(RedirectAction.class);

    public static final String PARAM_RELATIVE_URL = "r";
    public static final String PARAM_TOOL_SESSION_ID = "t";
    public static final String PARAM_ACCESS_MODE = "a";

    public static final String ACCESS_MODE_TEACHER = "t";
    // public static final String ACCESS_MODE_AUTHOR ="a";
    public static final String ACCESS_MODE_LEARNER = "l";

    private static ILamsToolService lamsToolService;
    private static IUserManagementService userService;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws Exception {

	try {
	    String relativePath = WebUtil.readStrParam(req, PARAM_RELATIVE_URL);
	    Long toolSessionID = WebUtil.readLongParam(req, PARAM_TOOL_SESSION_ID);
	    String accessMode = WebUtil.readStrParam(req, PARAM_ACCESS_MODE);

	    if (relativePath == null || toolSessionID == null || accessMode == null) {
		throw new Exception("Parameters missing for LAMS redirect");
	    }

	    // Get the user
	    UserDTO user = getUser();
	    if (user == null) {
		log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    }

	    // Get the tool session
	    ToolSession toolSession = getToolSession(toolSessionID);

	    // Get the lesson
	    Lesson lesson = toolSession.getLesson();

	    // Check the user's permissions, either learner or monitor
	    if (accessMode.equals(ACCESS_MODE_LEARNER)) {
		if (lesson == null || !lesson.isLessonStarted()) {
		    return displayMessage(mapping, req, "message.lesson.not.started.cannot.participate");
		}

		// Check the learner is part of the group in question
		if (toolSession.getLearners() == null || !toolSession.getLearners().contains(getRealUser(user))) {
		    log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested group. Cannot access the lesson.");
		    return displayMessage(mapping, req, "error.authorisation");
		}
		
	    } else if (accessMode.equals(ACCESS_MODE_TEACHER)) {

		// Check this is a monitor for the lesson in question
		if (lesson.getLessonClass() == null || !lesson.getLessonClass().isStaffMember(getRealUser(user))) {
		    log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested lesson. Cannot access the lesson.");
		    return displayMessage(mapping, req, "error.authorisation");
		}

	    } else {
		throw new Exception("Mode " + accessMode + " is not allowed.");
	    }

	    // If user has passed all the checks, they will get redirected
	    res.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL) + relativePath);

	    return null;
	} catch (Exception e) {
	    log.error("Failed redirect to url", e);
	    return mapping.findForward("error");
	}
    }

    public ActionForward doLearner(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res,
	    Long toolSessionID, String relativePath) throws Exception {

	return null;
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return getUserService().getUserByLogin(dto.getLogin());
    }
    
    private ToolSession getToolSession(Long toolSessionID)
    {
	return getLamsToolService().getToolSession(toolSessionID);
    }

    private IUserManagementService getUserService() {
	if (userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userService;
    }

    public static void setUserService(IUserManagementService userService) {
	RedirectAction.userService = userService;
    }
    
    private ILamsToolService getLamsToolService() {
	if (lamsToolService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lamsToolService = (ILamsToolService) ctx.getBean("lamsToolService");
	}
	return lamsToolService;
    }

    public static void setLamsToolService(ILamsToolService lamsToolService) {
        RedirectAction.lamsToolService = lamsToolService;
    }
}