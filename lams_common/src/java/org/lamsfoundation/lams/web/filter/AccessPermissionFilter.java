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

package org.lamsfoundation.lams.web.filter;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter checks if user has sufficient permission level to access given lesson. Initial implementation prevents
 * unauthorised cross-course traversal, but this filter can be extended for more generic use.
 *
 * @author Marcin Cieslak
 */
public class AccessPermissionFilter extends OncePerRequestFilter {

    private static Logger log = Logger.getLogger(AccessPermissionFilter.class);

    private static final char REQUEST_QUERY_SEPARATOR = '?';
    private static final String PAGE_LIST_DELIMITER = ";";

    private static ILamsToolService lamsToolService;
    private static ILamsCoreToolService lamsCoreToolService;
    private static IUserManagementService userManagementService;
    private static ILessonService lessonService;
    private static IToolContentDAO toolContentDAO;

    /*
     * Requests to specific Tool's Monitor and Learner are processed by Tool itself, so they do not go through filters
     * defined in Central.
     * This means that every Tool has its own instance of this filter for its own chain of request processing.
     * One disadvantage is that filter configuration needs to be repeated for every tool.
     * An advantage is that filter can be Tool-aware and get its exact Monitor and Learner URLs.
     * As this filter can be used in future for other purposes, "toolSignature" is not a required parameter.
     */
    private String toolSignature;
    private Pattern monitorPattern;
    private Pattern learnerPattern;

    /*
     * Pages delimited by PAGE_LIST_DELIMITER will be checked for user access persmissions
     * to the lesson identified by "lessonID" parameter.
     */
    private String lessonSecuredPages;
    private String[] lessonSecuredPagesArray;

    @Override
    protected void initFilterBean() {
	if (toolSignature == null) {
	    if (AccessPermissionFilter.log.isDebugEnabled()) {
		AccessPermissionFilter.log.debug("Init parameter \"toolSignature\" is missing.");
	    }
	} else {
	    IToolVO tool = getLamsToolService().getToolBySignature(toolSignature);
	    if (tool == null) {
		AccessPermissionFilter.log.warn("Tool missing for provided signature: " + toolSignature);
	    } else {
		monitorPattern = AccessPermissionFilter.makePattern(tool.getMonitorUrl());
		learnerPattern = AccessPermissionFilter.makePattern(tool.getLearnerUrl());
	    }
	}

	if (lessonSecuredPages != null) {
	    lessonSecuredPagesArray = lessonSecuredPages.split(AccessPermissionFilter.PAGE_LIST_DELIMITER);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	    throws IOException, ServletException {

	String requestURI = request.getRequestURI();
	boolean checkPerformed = false;

	if (lessonSecuredPagesArray != null) {
	    for (String lessonSecuredPage : lessonSecuredPagesArray) {
		if (requestURI.endsWith(lessonSecuredPage)) {
		    // it is one of pages we need to check learner lesson access for
		    checkPerformed = true;
		    String lessonIDParam = request.getParameter(AttributeNames.PARAM_LESSON_ID);
		    if (lessonIDParam != null) {
			Long lessonId = Long.valueOf(lessonIDParam);
			Lesson lesson = getLessonService().getLesson(lessonId);
			if (lesson != null) {
			    User user = getUser();
			    if ((lesson.getLessonClass() != null)
				    && lesson.getLessonClass().getLearners().contains(user)) {
				// check if preceding lessons are complete
				if (getLessonService().checkLessonReleaseConditions(lessonId, user.getUserId())) {
				    if (AccessPermissionFilter.log.isTraceEnabled()) {
					AccessPermissionFilter.log.trace("OK, user " + user.getLogin()
						+ " is a learner in the requested lesson. Lesson ID: "
						+ lesson.getLessonId() + ", name: " + lesson.getLessonName());
				    }
				} else {
				    throw new SecurityException("User " + user.getLogin()
					    + " has not finished required preceding lessons. Lesson ID: "
					    + lesson.getLessonId() + ", name: " + lesson.getLessonName());
				}
			    } else {
				throw new SecurityException("User " + user.getLogin()
					+ " is not a learner in the requested lesson. Lesson ID: "
					+ lesson.getLessonId() + ", name: " + lesson.getLessonName());
			    }
			}
		    }
		}
	    }
	}

	// some optimisation - Monitor/Learner (Tools) and lesson secured pages (Learner) check do not go in pairs
	if (!checkPerformed) {
	    // rebuild request URL from parts
	    String requestURL = requestURI + AccessPermissionFilter.REQUEST_QUERY_SEPARATOR + request.getQueryString();

	    // check if user is trying to access a lesson in which he is not a learner
	    if ((learnerPattern != null) && learnerPattern.matcher(requestURL).find()) {
		checkPerformed = true;
		String toolSessionIDParam = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
		if (toolSessionIDParam != null) {
		    Long toolSessionID = Long.valueOf(toolSessionIDParam);
		    Set<User> learners = getLamsToolService().getAllPotentialLearners(toolSessionID);
		    User user = getUser();
		    if ((learners != null) && learners.contains(user)) {
			if (AccessPermissionFilter.log.isTraceEnabled()) {
			    AccessPermissionFilter.log.trace("OK, user " + user.getLogin()
				    + " is a learner in the requested lesson. ToolSessionID: " + toolSessionIDParam);
			}
		    } else {
			throw new SecurityException("User " + user.getLogin()
				+ " is not a learner in the requested lesson. ToolSessionID: " + toolSessionIDParam);
		    }
		}

		// check if user is trying to access a lesson in which he is not a monitor
	    } else if ((monitorPattern != null) && monitorPattern.matcher(requestURL).find()) {
		checkPerformed = true;
		String toolContentIDParam = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		if (toolContentIDParam != null) {
		    Long toolContentID = Long.valueOf(toolContentIDParam);
		    // Any DAO extending IBaseDAO would do, it is nothing specific for ToolContentDAO
		    ToolContent toolContent = (ToolContent) getToolContentDAO().find(ToolContent.class, toolContentID);
		    // There can be multiple activities for single toolContentID? Proper permissions for any of them
		    // will do
		    Set<ToolActivity> activities = toolContent.getActivities();
		    if ((activities != null) && !activities.isEmpty()) {

			boolean isStaffMember = false;
			User user = getUser();
			Lesson lesson = null;

			for (ToolActivity activity : activities) {
			    // there should be just one lesson
			    lesson = (Lesson) activity.getLearningDesign().getLessons().iterator().next();
			    if ((lesson.getLessonClass() != null) && lesson.getLessonClass().isStaffMember(user)) {
				isStaffMember = true;
				break;
			    }
			}

			if (isStaffMember) {
			    if (AccessPermissionFilter.log.isTraceEnabled()) {
				AccessPermissionFilter.log
					.trace("OK, user " + user.getLogin() + " is a monitor in the requested lesson."
						+ (lesson == null ? ""
							: " Lesson ID: " + lesson.getLessonId() + ", name: "
								+ lesson.getLessonName()));
			    }
			} else {
			    throw new SecurityException(
				    "User " + user.getLogin() + " is not a monitor in the requested lesson."
					    + (lesson == null ? ""
						    : " Lesson ID: " + lesson.getLessonId() + ", name: "
							    + lesson.getLessonName()));
			}
		    }
		}
	    }
	}

	if (!checkPerformed && AccessPermissionFilter.log.isTraceEnabled()) {
	    AccessPermissionFilter.log.trace("URL does not match any patterns to check, carry on.");
	}

	if (chain != null) {
	    chain.doFilter(request, response);
	}
    }

    private User getUser() {
	HttpSession session = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) session.getAttribute(AttributeNames.USER);
	return getUserManagementService().getUserByLogin(userDTO.getLogin());
    }

    private ToolSession getToolSession(Long toolSessionID) {
	return getLamsToolService().getToolSession(toolSessionID);
    }

    /**
     * Replaces wildcard characters with literals and constructs Pattern object.
     */
    private static Pattern makePattern(String literal) {
	String escaped = literal.replaceAll("\\.", "\\\\.").replaceAll("\\?", "\\\\?");
	return Pattern.compile(escaped, Pattern.CASE_INSENSITIVE);
    }

    private IUserManagementService getUserManagementService() {
	if (AccessPermissionFilter.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getFilterConfig().getServletContext());
	    AccessPermissionFilter.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return AccessPermissionFilter.userManagementService;
    }

    private ILamsToolService getLamsToolService() {
	if (AccessPermissionFilter.lamsToolService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getFilterConfig().getServletContext());
	    AccessPermissionFilter.lamsToolService = (ILamsToolService) ctx.getBean("lamsToolService");
	}
	return AccessPermissionFilter.lamsToolService;
    }

    private ILamsCoreToolService getLamsCoreToolService() {
	if (AccessPermissionFilter.lamsCoreToolService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getFilterConfig().getServletContext());
	    AccessPermissionFilter.lamsCoreToolService = (ILamsCoreToolService) ctx.getBean("lamsCoreToolService");
	}
	return AccessPermissionFilter.lamsCoreToolService;
    }

    private ILessonService getLessonService() {
	if (AccessPermissionFilter.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getFilterConfig().getServletContext());
	    AccessPermissionFilter.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return AccessPermissionFilter.lessonService;
    }

    private IToolContentDAO getToolContentDAO() {
	if (AccessPermissionFilter.toolContentDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getFilterConfig().getServletContext());
	    AccessPermissionFilter.toolContentDAO = (IToolContentDAO) ctx.getBean("toolContentDAO");
	}
	return AccessPermissionFilter.toolContentDAO;
    }

    public void setToolSignature(String toolName) {
	this.toolSignature = toolName;
    }

    public void setLessonSecuredPages(String lessonSecuredPages) {
	this.lessonSecuredPages = lessonSecuredPages;
    }
}