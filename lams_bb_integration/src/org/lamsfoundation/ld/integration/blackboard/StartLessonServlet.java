package org.lamsfoundation.ld.integration.blackboard;
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
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.util.BlackboardUtil;
import org.xml.sax.SAXException;

import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

/**
 * Starts LAMS lesson and stores its relevant content in Blackboard.
 */
public class StartLessonServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = Logger.getLogger(StartLessonServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	// Authorize current user for Course Control Panel (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}
	
    	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();

	ContextManager ctxMgr = null;
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.getContext();

	    //store newly created LAMS lesson
	    User user = ctx.getUser();
	    BlackboardUtil.storeBlackboardContent(request, response, user);
	    
	    // constuct strReturnUrl
	    String courseIdStr = request.getParameter("course_id");
	    String contentIdStr = request.getParameter("content_id");
	    // internal Blackboard IDs for the course and parent content item
	    Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);
	    Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE, contentIdStr);
	    String returnUrl = PlugInUtil.getEditableContentReturnURL(folderId, courseId);
	    request.setAttribute("returnUrl", returnUrl);

	} catch (PersistenceException  e) {
	    throw new ServletException(e);
	} catch (ParseException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} finally {
	    // important. make sure context is not released as otherwise <bbNG:genericPage> tag will throw an exception
//	    if (ctxMgr != null) {
//		ctxMgr.releaseContext();
//	    }
	}
	
	request.getRequestDispatcher("/modules/startLessonSuccess.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	doGet(request, response);
    }

}
