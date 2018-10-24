package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.bb.integration.dto.LearnerProgressDTO;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import blackboard.base.InitializationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

/**
 * 
 */
public class LearnerMonitorServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = LoggerFactory.getLogger(LearnerMonitorServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	// Authorise current user for Course Access (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourse(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	try {
	    BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	    CourseMembershipDbLoader sessionCourseMembershipLoader = CourseMembershipDbLoader.Default.getInstance();

	    String strLessonId = request.getParameter("lsid").trim();
	    long lessonId = Long.parseLong(strLessonId);
	    String courseIdParam = request.getParameter("course_id");

	    // Get Course ID and User ID
	    Id course_id = bbPm.generateId(Course.DATA_TYPE, courseIdParam);

	    // get Blackboard context
	    ContextManager ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.getContext();

	    Id userId = ctx.getUser().getId();

	    // Get the membership data to determine the User's Role
	    CourseMembership courseMembership = null;
	    CourseMembership.Role courseRole = null;
	    boolean isActive = false;

	    try {
		courseMembership = sessionCourseMembershipLoader.loadByCourseAndUserId(course_id, userId);
		courseRole = courseMembership.getRole();
		isActive = courseMembership.getIsAvailable();
	    } catch (KeyNotFoundException e) {
		// There is no membership record.
		e.printStackTrace();
	    } catch (PersistenceException pe) {
		// There is no membership record.
		pe.printStackTrace();
	    }

	    // Is the User an Instructor of Teaching Assistant
	    boolean isInstructor = false;
	    if (CourseMembership.Role.INSTRUCTOR.equals(courseRole)
		    || CourseMembership.Role.TEACHING_ASSISTANT.equals(courseRole)
		    || CourseMembership.Role.COURSE_BUILDER.equals(courseRole)) {
		isInstructor = true;

	    } else if (!CourseMembership.Role.STUDENT.equals(courseRole)) {
		// The user is not an Instructor, Teaching Assistant or Student - Access Denied 
		response.sendRedirect("notAllowed.jsp");
	    }

	    // Are they active in the course?  If not let Blackboard handle the redirect
	    if (!isActive) {
		PlugInUtil.sendAccessDeniedRedirect(request, response);
	    }

	    //Get lessson's title and description
	    String title = "";
	    String description = "";
	    //contentId is available in versions after 1.2.3
	    String contentIdParam = request.getParameter("content_id");
	    if (contentIdParam != null) {

		Container bbContainer = bbPm.getContainer();
		Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, contentIdParam);
		ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader(ContentDbLoader.TYPE);
		Content courseDoc = (Content) courseDocumentLoader.loadById(contentId);
		title = courseDoc.getTitle();
		description = courseDoc.getBody().getFormattedText();

	    } else {

		title = (request.getParameter("title") != null) ? request.getParameter("title") : "LAMS Options";
		description = request.getParameter("description");
	    }

	    //display learning design image
	    String strIsDisplayDesignImage = request.getParameter("isDisplayDesignImage");
	    boolean isDisplayDesignImage = "true".equals(strIsDisplayDesignImage) ? true : false;
	    String learningDesignImageUrl = "";
	    if (isDisplayDesignImage) {
		String username = ctx.getUser().getUserName();
		learningDesignImageUrl = LamsSecurityUtil.generateRequestLearningDesignImage(username) + "&lsId="
			+ lessonId;
	    }

	    //prepare learnerProgressDto for displaying on jsp
	    LearnerProgressDTO learnerProgressDto = LamsSecurityUtil.getLearnerProgress(ctx, lessonId);
	    
	    request.setAttribute("title", title);
	    request.setAttribute("isInstructor", isInstructor);
	    request.setAttribute("description", description);
	    request.setAttribute("isDisplayDesignImage", isDisplayDesignImage);
	    request.setAttribute("learningDesignImageUrl", learningDesignImageUrl);
	    request.setAttribute("isLessonCompleted", learnerProgressDto.getLessonComplete());
	    request.setAttribute("attemptedActivitiesCount", learnerProgressDto.getAttemptedActivities());
	    request.setAttribute("activitiesCompletedCount", learnerProgressDto.getActivitiesCompleted());
	    request.setAttribute("activitiesCount", learnerProgressDto.getActivityCount());

	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	doGet(request, response);
    }

}
