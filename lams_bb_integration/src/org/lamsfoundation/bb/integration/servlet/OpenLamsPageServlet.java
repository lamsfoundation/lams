package org.lamsfoundation.bb.integration.servlet;
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blackboard.base.InitializationException;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

/**
 * Launches LAMS pages: author, learner and monitor one.
 */
public class OpenLamsPageServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = LoggerFactory.getLogger(OpenLamsPageServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	ContextManager ctxMgr = null;
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.getContext();

	    String method = request.getParameter("method");
	    // -----------------------Assessment Author functions ---------------------------
	    if (method.equals("openAuthor")) {
		openAuthor(request, response, ctx);
		
	    } else if (method.equals("openPreview")) {
		openPreview(request, response, ctx);

	    } else if (method.equals("openLearner")) {
		openLearner(request, response, ctx);

	    } else if (method.equals("openMonitor")) {
		openMonitor(request, response, ctx);
	    }

	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	} finally {
	    // make sure context is released
	    if (ctxMgr != null) {
		ctxMgr.releaseContext();
	    }
	}

    }

    private void openAuthor(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException {
	// Authorise current user for Course Control Panel (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	// construct Login Request URL for authoring LAMS Lessons
	String authorUrl = LamsSecurityUtil.generateRequestURL(ctx, "author", null);
	response.sendRedirect(authorUrl);
    }
    
    /**
     * Starts preview lesson on LAMS server. Launches it.
     */
    private void openPreview(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException {
	// Authorize current user for Course Control Panel (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	// Get the form parameters and convert into correct data types
	String strTitle = request.getParameter("title").trim();
	String strLdId = request.getParameter("ldId").trim();
	long ldId = Long.parseLong(strLdId);

	// start lesson-preview in LAMS and get back the lesson ID
	User user = ctx.getUser();
	Long lsId = LamsSecurityUtil.startLesson(user, "Previews", ldId, strTitle, "", false, true);
	// error checking
	if (lsId == -1) {
	    response.sendRedirect("lamsServerDown.jsp");
	    System.exit(1);
	}

	// redirect to preview lesson
	String previewUrl = LamsSecurityUtil.generateRequestURL(ctx, "learnerStrictAuth", "" + lsId);
	response.sendRedirect(previewUrl);
    }

    private void openLearner(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException {
	// Authorise current user for Course Access (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourse(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	// Get Course ID and Session User ID
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	String course_idstr = request.getParameter("course_id");
	Id course_id = bbPm.generateId(Course.DATA_TYPE, course_idstr);
	User sessionUser = ctx.getUser();
	Id sessionUserId = sessionUser.getId();
	// Get the membership data to determine the User's Role
	CourseMembership courseMembership = null;
	CourseMembership.Role courseRole = null;
	CourseMembershipDbLoader sessionCourseMembershipLoader = (CourseMembershipDbLoader) bbPm
		.getLoader(CourseMembershipDbLoader.TYPE);
	try {
	    courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, sessionUserId);
	    courseRole = courseMembership.getRole();
	} catch (KeyNotFoundException e) {
	    // There is no membership record.
	    e.printStackTrace();
	} catch (PersistenceException pe) {
	    // There is no membership record.
	    pe.printStackTrace();
	}

	// if the user is not an Instructor, Teaching Assistant or Student - Access Denied 
	if (!(courseRole.equals(CourseMembership.Role.INSTRUCTOR)
		|| courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)
		|| courseRole.equals(CourseMembership.Role.COURSE_BUILDER)
		|| courseRole.equals(CourseMembership.Role.STUDENT))) {
	    response.sendRedirect("notAllowed.jsp");
	    return;
	}

	// Get the Login Request URL for authoring LAMS Lessons
	String lsid = request.getParameter("lsid");
	String learnerUrl = LamsSecurityUtil.generateRequestURL(ctx, "learnerStrictAuth", lsid);

	response.sendRedirect(learnerUrl);
    }

    private void openMonitor(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws IOException, PersistenceException {
	// Authorize current user for Course Control Panel (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	// Get Course ID and Session User ID
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	CourseMembershipDbLoader sessionCourseMembershipLoader = CourseMembershipDbLoader.Default.getInstance();
	String _course_id = request.getParameter("course_id");
	Id course_id = bbPm.generateId(Course.DATA_TYPE, _course_id);
	User sessionUser = ctx.getUser();
	Id sessionUserId = sessionUser.getId();
	// Get the membership data to determine the User's Role
	CourseMembership courseMembership = null;
	CourseMembership.Role courseRole = null;

	try {
	    courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, sessionUserId);
	    courseRole = courseMembership.getRole();
	} catch (KeyNotFoundException e) {
	    // There is no membership record.
	    e.printStackTrace();
	} catch (PersistenceException pe) {
	    // There is no membership record.
	    pe.printStackTrace();
	}

	// if the user is not an Instructor or Teaching Assistant - Access Denied 
	if (!(courseRole.equals(CourseMembership.Role.INSTRUCTOR)
		|| courseRole.equals(CourseMembership.Role.TEACHING_ASSISTANT)
		|| courseRole.equals(CourseMembership.Role.COURSE_BUILDER))) {
	    response.sendRedirect("notAllowed.jsp");
	}

	// construct Login Request URL for monitoring LAMS Lessons
	String lsid = request.getParameter("lsid");
	String monitorUrl = LamsSecurityUtil.generateRequestURL(ctx, "monitor", lsid);
	response.sendRedirect(monitorUrl);
    }

}
