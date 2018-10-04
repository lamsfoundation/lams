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

package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This action is used for notification emails. It is designed to enable direct linking to either monitor or
 * learner so that links can be sent in the email. This must be done through LAMS central so it correctly
 * redirects the user to the login page (if they have no session) before returning them to the correct location.
 *
 * This action takes one parameter "h" which is a Base64 hash of a comma-separated value (relativeUrlPath,
 * toolSessionID, accessMode) where: relativeUrlPath = Relative path to resource eg /tool/lawiki10/learner.do
 * toolSessionID = A valid tool session ID for the lesson accessMode = l or t (learner or teacher)
 *
 * The parameters are hashed to prevent people from identifying the url, and attempting to access content to
 * which they are unauthorised, see LDEV-1978
 *
 * The toolSessionID and accessMode are used to determine the permissions of this user so it someone forwards
 * the email to an unauthorised user, they still cannot access the link unless they are part of the correct
 * group. These checks may become unneccessary on the completion of LDEV-1978
 *
 * Note that parameter names have been made as short as possible here to attempt to shorten the entire link
 * required, and hopefully prevent email clients cutting them off and making a newline which sometimes breaks
 * links.
 * 
 * @author lfoxton
 */
@Controller
public class RedirectController {
    private static Logger log = Logger.getLogger(RedirectController.class);

    public static final String PARAM_HASH = "h";

    public static final String ACCESS_MODE_TEACHER = "t";
    public static final String ACCESS_MODE_LEARNER = "l";

    @Autowired
    private ILamsToolService lamsToolService;
    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("/r")
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

	try {
	    String hash = WebUtil.readStrParam(req, RedirectController.PARAM_HASH);

	    // Un-hash the string to gain all the paramters
	    String fullParams = new String(Base64.decodeBase64(hash.getBytes()));

	    // Split the CSV parameters
	    String[] split = fullParams.split(",");

	    if (split.length != 3) {
		throw new Exception("Hash did not contain correct format (relative path, toolSessionID, toolaccess )");
	    }

	    // Getting the parameters from the hash
	    String relativePath = split[0];
	    Long toolSessionID = Long.parseLong(split[1]);
	    String accessMode = split[2];

	    // Get the user
	    UserDTO user = getUser();
	    if (user == null) {
		RedirectController.log.error("admin: User missing from session. ");
		return "errorContent";
	    }

	    // Get the tool session
	    ToolSession toolSession = getToolSession(toolSessionID);

	    if (toolSession == null) {
		RedirectController.log.error("No ToolSession with ID " + toolSessionID + " found.");
		return "errorContent";
	    }

	    // Get the lesson
	    Lesson lesson = toolSession.getLesson();

	    // Check the user's permissions, either learner or monitor
	    if (accessMode.equals(RedirectController.ACCESS_MODE_LEARNER)) {
		if ((lesson == null) || !lesson.isLessonStarted()) {
		    return displayMessage(req, "message.lesson.not.started.cannot.participate");
		}

		// Check the learner is part of the group in question
		if (!toolSession.getLearners().contains(getRealUser(user))) {
		    RedirectController.log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested group. Cannot access the lesson.");
		    return displayMessage(req, "error.authorisation");
		}

	    } else if (accessMode.equals(RedirectController.ACCESS_MODE_TEACHER)) {

		// Check this is a monitor for the lesson in question
		if ((lesson.getLessonClass() == null) || !lesson.getLessonClass().isStaffMember(getRealUser(user))) {
		    RedirectController.log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested lesson. Cannot access the lesson.");
		    return displayMessage(req, "error.authorisation");
		}

	    } else {
		throw new Exception("Mode " + accessMode + " is not allowed.");
	    }

	    // If user has passed all the checks, they will get redirected
	    res.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL) + relativePath);

	    return null;
	} catch (Exception e) {
	    RedirectController.log.error("Failed redirect to url", e);
	    return "errorContent";
	}
    }

    private String displayMessage(HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return "msgContent";
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return userManagementService.getUserByLogin(dto.getLogin());
    }

    private ToolSession getToolSession(Long toolSessionID) {
	return lamsToolService.getToolSession(toolSessionID);
    }

}