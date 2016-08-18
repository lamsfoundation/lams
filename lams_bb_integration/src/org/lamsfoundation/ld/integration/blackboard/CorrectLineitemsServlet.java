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


package org.lamsfoundation.ld.integration.blackboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.integration.util.BlackboardUtil;
import org.lamsfoundation.ld.integration.util.LineitemUtil;

import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.data.user.User;
import blackboard.persist.PkId;
import blackboard.persist.course.CourseDbLoader;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.util.StringUtil;

/**
 * Admin on BB side calls this servlet to correct lineitems that have been screwed up while copying/importing courses.
 */
public class CorrectLineitemsServlet extends HttpServlet {

    private static final long serialVersionUID = -3284220455069633836L;
    private static Logger logger = Logger.getLogger(CorrectLineitemsServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	String courseIdParam = request.getParameter("courseId");	
	if (StringUtil.isEmpty(courseIdParam)) {
	    throw new RuntimeException("Required parameters are missing. courseId: " + courseIdParam);
	}

	try {
	    // get Blackboard context
	    ContextManager ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.setContext(request);
	    
	    CourseDbLoader courseLoader = CourseDbLoader.Default.getInstance();
	    Course course = courseLoader.loadByCourseId(courseIdParam);
	    PkId courseId = (PkId) course.getId();
	    String _course_id = "_" + courseId.getPk1() + "_" + courseId.getPk2();
	    logger.debug("Starting clonning course lessons (courseId=" + courseId + ").");

	    // find a teacher that will be assigned as lesson's author on LAMS side
	    User teacher = BlackboardUtil.getCourseTeacher(courseId);

	    //find all lessons that should be updated
	    List<Content> lamsContents = BlackboardUtil.getLamsLessonsByCourse(courseId);
	    for (Content content : lamsContents) {

		// update lesson id
		String lessonId = content.getLinkRef();

		//update lineitem details
		LineitemUtil.updateLineitemLessonId(content, _course_id, Long.parseLong(lessonId), ctx,
			teacher.getUserName());
	    }

	} catch (IllegalStateException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (Exception e) {
	    throw new ServletException(e);
	}
	
	//prepare string to write out
	String resultStr = "Complete! All lineiems have been corrected.";
	logger.debug(resultStr);
	
	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
        out.write(resultStr);
        out.flush();
        out.close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	doGet(req, resp);
    }

}