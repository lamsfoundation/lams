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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.ld.util.LineitemUtil;

import blackboard.base.FormattedText;
import blackboard.base.InitializationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbPersister;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

/**
 * Starts a lesson, returning the BB Content Id in JSON. Does exactly the same things (code is copied!) as start_lesson_proc.
 * Had tried wrapping the jsp but encountered too many problems.
 * Return a server error rather than throw an exception as this will be consumed by AJAX call or the like. 
 */
public class StartLessonServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = Logger.getLogger(StartLessonServlet.class);
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {

	ContextManager ctxMgr = null;
	Context ctx = null;
	try {
	    // In some instances of calling this servlet, the user is missing from the context. Try a few different ways to ensure we have the user!
	    User user = null;

	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    ctx = ctxMgr.setContext(request);
	    if ( ctx != null ) {
		user = ctx.getUser();
	    }
	    if ( user == null ) {
		ctxMgr = ContextManagerFactory.getInstance();
		ctx=ctxMgr.setContext(request);
		if ( ctx != null ) {
		    user = ctx.getUser();
		}
	    }
	    if ( user == null ) {
		ctx = ContextManagerFactory.getInstance().getContext();
		if ( ctx !=null )
		    user = ctx.getUser();
	    }
	    if ( user == null )
		throw new RuntimeException("Unable to get user from context: cannot proceed to get Learning Designs");
	    
	    // we have a good context, now get on with the task.

	    // Set the new LAMS Lesson Content Object
	    CourseDocument bbContent = new blackboard.data.content.CourseDocument();

	    // Authorise current user for Course Control Panel (automatic redirect)
	    try {
		if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		    return;
	    } catch (PlugInException e) {
		throw new RuntimeException(e);
	    }

	    // Retrieve the Db persistence manager from the persistence service
	    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();

	    String courseIdStr = request.getParameter("course_id");
	    String contentIdStr = request.getParameter("content_id");
	    String strTitle = getTrimmedString(request,"title");
	    String strSequenceID = getTrimmedString(request,"sequence_id");
	    
	    if ( courseIdStr == null || contentIdStr == null || strSequenceID.length()==0 || strTitle.length() == 0) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
			"Unable to create error - parameter missing. course_id, content_id, sequence_id and title required");

	    } else {
	    
        	    // Internal Blackboard IDs for the course and parent content item
        	    Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdStr);
        	    Id folderId = bbPm.generateId(CourseDocument.DATA_TYPE, contentIdStr);
                
        	    // Get the form parameters and convert into correct data types
        	    // TODO: Use bb text area instead
        	    String strDescription = getTrimmedString(request,"descriptiontext");
        	    FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
        
        	    long ldId = Long.parseLong(strSequenceID);
        
        	    String strIsAvailable = request.getParameter("isAvailable");
        	    String strIsGradecenter = request.getParameter("isGradecenter");
        	    String strIsTracked = request.getParameter("isTracked");
        	    boolean isAvailable = (strIsAvailable == null || strIsAvailable.equals("true")) ? true : false; // default true
        	    boolean isGradecenter = (strIsGradecenter != null && strIsGradecenter.equals("true")) ? true : false; // default
        	    // false
        	    boolean isTracked = (strIsTracked != null && strIsTracked.equals("true")) ? true : false; // default false
        
        	    String isDisplayDesignImage = request.getParameter("isDisplayDesignImage");
        
        	    // Set Availability Dates
        	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        	    // Start Date
        	    String strStartDate = request.getParameter("lessonAvailability_start_datetime");
        	    if (strStartDate != null) {
        		Calendar startDate = Calendar.getInstance();
        		startDate.setTime(formatter.parse(strStartDate));
        		String strStartDateCheckbox = request.getParameter("lessonAvailability_start_checkbox");
        		if (strStartDateCheckbox != null) {
        		    if (strStartDateCheckbox.equals("1")) {
        			bbContent.setStartDate(startDate);
        		    }
        		}
        	    }
        
        	    // End Date
        	    String strEndDate = request.getParameter("lessonAvailability_end_datetime");
        	    if (strEndDate != null) {
        		Calendar endDate = Calendar.getInstance();
        		endDate.setTime(formatter.parse(strEndDate));
        		String strEndDateCheckbox = request.getParameter("lessonAvailability_end_checkbox");
        		if (strEndDateCheckbox != null) {
        		    if (strEndDateCheckbox.equals("1")) {
        			bbContent.setEndDate(endDate);
        		    }
        		}
        	    }
        
        	    // Set the New LAMS Lesson content data (in Blackboard)
        	    bbContent.setTitle(strTitle);
        	    bbContent.setIsAvailable(isAvailable);
        	    bbContent.setIsDescribed(isGradecenter);// isDescribed field is used for storing isGradecenter parameter
        	    bbContent.setIsTracked(isTracked);
        	    bbContent.setAllowGuests(false);
        	    bbContent.setContentHandler(LamsPluginUtil.CONTENT_HANDLE);
        
        	    bbContent.setCourseId(courseId);
        	    bbContent.setParentId(folderId);
        
        	    bbContent.setRenderType(Content.RenderType.URL);
        	    bbContent.setBody(description);
        
        	    bbContent.setLaunchInNewWindow(true);
        
        	    // LDEV-3510 LAMS Lessons were always at the top and could not be moved.
        	    bbContent.setPosition(0);
        
        	    // Start the Lesson in LAMS (via Webservices) and capture the lesson ID
        	    final long LamsLessonIdLong = LamsSecurityUtil.startLesson(ctx, ldId, strTitle, strDescription, false);
        	    // error checking
        	    if (LamsLessonIdLong == -1) {
        		response.sendRedirect("lamsServerDown.jsp");
        		System.exit(1);
        	    }
        	    String lamsLessonId = Long.toString(LamsLessonIdLong);
        	    bbContent.setLinkRef(lamsLessonId);
        
        	    // Persist the New Lesson Object in Blackboard
        	    ContentDbPersister persister = (ContentDbPersister) bbPm.getPersister(ContentDbPersister.TYPE);
        	    persister.persist(bbContent);
        	    PkId bbContentPkId = (PkId) bbContent.getId();
        	    String bbContentId = "_" + bbContentPkId.getPk1() + "_" + bbContentPkId.getPk2();
        
        	    // Build and set the content URL. Include new lesson id parameter
        	    int bbport = request.getServerPort();// Add port to the url if the port is in the blackboard url.
        	    String bbportstr = bbport != 0 ? ":" + bbport : "";
        	    String contentUrl = request.getScheme() + "://" + request.getServerName() + bbportstr
        		    + request.getContextPath() + "/modules/learnermonitor.jsp?lsid=" + lamsLessonId + "&course_id="
        		    + request.getParameter("course_id") + "&content_id=" + bbContentId + "&ldid=" + ldId
        		    + "&isDisplayDesignImage=" + isDisplayDesignImage;
        	    bbContent.setUrl(contentUrl);
        	    persister.persist(bbContent);
        
        	    // store internalContentId -> externalContentId. It's used for lineitem removal (delete.jsp)
        	    PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
        	    ExtraInfo ei = pei.getExtraInfo();
        	    ei.setValue(bbContentId, lamsLessonId);
        	    PortalUtil.savePortalExtraInfo(pei);
        
        	    // Create new Gradebook column for current lesson
        	    if (isGradecenter) {
        		LineitemUtil.createLineitem(ctx, bbContent);
        	    }
        
        	    String strReturnUrl = PlugInUtil.getEditableContentReturnURL(bbContent.getParentId(), courseId);
        
        	    // create a new thread to pre-add students and monitors to a lesson (in order to do this task in parallel not to
        	    // slow down later work)
        	    final Context ctxFinal = ctx;
        	    Thread preaddLearnersMonitorsThread = new Thread(new Runnable() {
        		@Override
        		public void run() {
        		    LamsSecurityUtil.preaddLearnersMonitorsToLesson(ctxFinal, LamsLessonIdLong);
        		}
        	    }, "LAMS_preaddLearnersMonitors_thread");
        	    preaddLearnersMonitorsThread.start();
        
        	    response.setContentType("application/json;charset=UTF-8");
        	    response.getWriter().print("{\"content_id\":"+bbContentId+"}");
	    }
	    
	} catch (Exception e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to start lesson "+e.getMessage());
	} finally {
	    // make sure context is released
	    if (ctxMgr != null) {
		ctxMgr.releaseContext();
	    }
	}

    }

    private String getTrimmedString(HttpServletRequest request, String paramName) {
	String value = request.getParameter(paramName);
	return value != null ? value.trim() : "";
    }

    // In some instances of calling this servlet, the user is missing from the context. Try a few different ways to ensure we have the user!
    private Context getContext(HttpServletRequest request) throws InitializationException, BbServiceException {

	User user = null;

	ContextManager ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	Context ctx = ctxMgr.setContext(request);
	if ( ctx != null ) {
	    user = ctx.getUser();
	}
	if ( user == null ) {
	    final ContextManager contextManagerViaFactory = ContextManagerFactory.getInstance();
	    ctx=contextManagerViaFactory.setContext(request);
	    if ( ctx != null ) {
		user = ctx.getUser();
	    }
	}
	if ( user == null ) {
	    ctx = ContextManagerFactory.getInstance().getContext();
	    if ( ctx !=null )
		user = ctx.getUser();
	}
	if ( user != null )
	    return ctx;
	else 
	    throw new RuntimeException("Unable to get user from context: cannot proceed to get Learning Designs");
    }
}

