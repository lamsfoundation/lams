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
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.util.LamsServerException;

import blackboard.base.BbList;
import blackboard.base.FormattedText;
import blackboard.base.InitializationException;
import blackboard.data.content.Content;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.navigation.CourseToc;
import blackboard.data.user.User;
import blackboard.persist.Id;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.navigation.CourseTocDbLoader;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;
import blackboard.util.StringUtil;

/**
 * Admin on BB side calls this servlet to import old lesson that were copied to the new course.
 */
public class ImportLessonsServlet extends HttpServlet {

    private static final long serialVersionUID = -3587062723412672184L;
    private static Logger logger = Logger.getLogger(ImportLessonsServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	String courseIdParam = request.getParameter("courseId");	
	if (StringUtil.isEmpty(courseIdParam)) {
	    throw new RuntimeException("Required parameters are missing. courseId: " + courseIdParam);
	}

	String newLessonIds = "";
	try {
	    // get Blackboard context
	    ContextManager ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.setContext(request);
	    
	    CourseDbLoader courseLoader = CourseDbLoader.Default.getInstance();
	    Course course = courseLoader.loadByCourseId(courseIdParam);
	    PkId courseId = (PkId) course.getId();
	    String _course_id = "_" + courseId.getPk1() + "_" + courseId.getPk2();

	    // find the main teacher
	    CourseMembershipDbLoader courseMemLoader = CourseMembershipDbLoader.Default.getInstance();
	    BbList<CourseMembership> monitorCourseMemberships = courseMemLoader.loadByCourseIdAndRole(courseId,
		    CourseMembership.Role.INSTRUCTOR, null, true);
	    if (monitorCourseMemberships.isEmpty()) {
		BbList<CourseMembership> teachingAssistantCourseMemberships = courseMemLoader
			.loadByCourseIdAndRole(courseId, CourseMembership.Role.TEACHING_ASSISTANT, null, true);
		monitorCourseMemberships.addAll(teachingAssistantCourseMemberships);
		if (monitorCourseMemberships.isEmpty()) {
		    BbList<CourseMembership> courseBuilderCourseMemberships = courseMemLoader
			    .loadByCourseIdAndRole(courseId, CourseMembership.Role.COURSE_BUILDER, null, true);
		    monitorCourseMemberships.addAll(courseBuilderCourseMemberships);
		}
	    }
	    // validate teacher existence
	    if (monitorCourseMemberships.isEmpty()) {
		throw new RuntimeException("There are no monitors in the course courseId=" + courseId);
	    }
	    User teacher = monitorCourseMemberships.get(0).getUser();

	    logger.debug("Starting importing course lessons (courseId=" + courseId + ").");

	    ContentDbLoader contentLoader = ContentDbLoader.Default.getInstance();
	    CourseTocDbLoader cTocDbLoader = CourseTocDbLoader.Default.getInstance();
	    ContentDbPersister persister =ContentDbPersister.Default.getInstance();

	    //find all lessons that should be updated

	    // get a CourseTOC (Table of Contents) loader. We will need this to iterate through all of the "areas"
	    // within the course
	    BbList<CourseToc> courseTocs = cTocDbLoader.loadByCourseId(courseId);

	    // iterate through the course TOC items
	    for (CourseToc courseToc : courseTocs) {

		// determine if the TOC item is of type "CONTENT" rather than applicaton, or something else
		if ((courseToc.getTargetType() == CourseToc.Target.CONTENT)
			&& (courseToc.getContentId() != Id.UNSET_ID)) {
		    // we have determined that the TOC item is content, next we need to load the content object and
		    // iterate through it
		    // load the content tree into an object "content" and iterate through it
		    BbList<Content> contents = contentLoader.loadListById(courseToc.getContentId());
		    // iterate through the content items in this content object
		    for (Content content : contents) {
			// only LAMS content
			if ("resource/x-lams-lamscontent".equals(content.getContentHandler())) {
			    
			    PkId contentId =  (PkId) content.getId();
			    String _content_id = "_" + contentId.getPk1() + "_" + contentId.getPk2();
			    
			    String url = content.getUrl();
			    String urlLessonId = getParameterValue(url, "lsid");
			    String urlCourseId = getParameterValue(url, "course_id");
			    String urlContentId = getParameterValue(url, "content_id");
			    String urlLdId = getParameterValue(url, "ldid");

			    //in case when both courseId and contentId don't coincide with the ones from URL - means lesson needs to be imported
			    if (!urlCourseId.equals(_course_id) && !urlContentId.equals(_content_id)) {
				
				final Long newLdId = LamsSecurityUtil.importLearningDesign(teacher, courseIdParam, urlLessonId,
					urlLdId);
				
				logger.debug("Lesson (lessonId=" + urlLessonId
					+ ") was successfully imported to the one (learningDesignId=" + newLdId + ").");

				// Start the Lesson in LAMS (via Webservices) and capture the lesson ID
				String teacherUsername = teacher.getUserName();
				String title = content.getTitle(); 
				FormattedText descriptionFormatted = content.getBody();
				String description = URLEncoder.encode(descriptionFormatted.getText(), "UTF-8");
				final long newLessonId = LamsSecurityUtil.startLesson(ctx, teacherUsername,
					courseIdParam, newLdId, title, description, false);
				
				// update lesson id
				content.setLinkRef(Long.toString(newLessonId));

				// update URL
				url = replaceParameterValue(url, "lsid", Long.toString(newLessonId));
				url = replaceParameterValue(url, "course_id", _course_id);
				url = replaceParameterValue(url, "content_id", _content_id);
				content.setUrl(url);

				// persist updated content
				persister.persist(content);

				// store internalContentId -> externalContentId. It's used for lineitem removal (delete.jsp)
				PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
				ExtraInfo ei = pei.getExtraInfo();
				ei.setValue(_content_id, Long.toString(newLessonId));
				PortalUtil.savePortalExtraInfo(pei);

				// Gradebook column will be copied automatically if appropriate option is selected on
				// importing lesson page

				logger.debug("Lesson (lessonId=" + urlLessonId
					+ ") was successfully imported to the one (lessonId=" + newLessonId + ").");
				
				newLessonIds += newLessonId + ", ";
			    }
			}

		    }
		}
	    }

	} catch (LamsServerException e) {
	    //printing out error cause
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("Failed! " + e.getMessage());
	    out.flush();
	    out.close();
	    return;
	} catch (IllegalStateException e) {
	    throw new ServletException(
		    "LAMS Server timeout, did not get a response from the LAMS server. Please contact your systems administrator",
		    e);
	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (Exception e) {
	    throw new ServletException(e);
	}
	
	//prepare string to write out
	int newLessonsCounts = newLessonIds.length() - newLessonIds.replace(",", "").length();
	String resultStr = "Complete! " + newLessonsCounts + " lessons have been imported.";
	//add all lessonIds (without the last comma)
	if (newLessonsCounts > 0) {
	    resultStr += " Their updated lessonIds: " + newLessonIds.substring(0, newLessonIds.length()-2);
	}
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
    
    /*
     * Returns param value, and empty string in case of there is no such param available
     * 
     * @param url
     * @param paramName
     * @return
     */
    private static String getParameterValue(String url, String paramName) {
	String paramValue = "";

	int quotationMarkIndex = url.indexOf("?");
	String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	String[] paramEntries = queryPart.split("&");
	for (String paramEntry : paramEntries) {
	    String[] paramEntrySplitted = paramEntry.split("=");
	    if ((paramEntrySplitted.length > 1) && paramName.equalsIgnoreCase(paramEntrySplitted[0])) {
		paramValue = paramEntrySplitted[1];
		break;
	    }
	}

	return paramValue;
    }
    
    private static String replaceParameterValue(String url, String paramName, String newParamValue) {
	String oldParamValue = "";

	int quotationMarkIndex = url.indexOf("?");
	String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	String[] paramEntries = queryPart.split("&");
	for (String paramEntry : paramEntries) {
	    String[] paramEntrySplitted = paramEntry.split("=");
	    if ((paramEntrySplitted.length > 1) && paramName.equalsIgnoreCase(paramEntrySplitted[0])) {
		oldParamValue = paramEntrySplitted[1];

		return url.replaceFirst(paramName + "=" + oldParamValue, paramName + "=" + newParamValue);
	    }
	}

	return url;
    }

}