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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.lamsfoundation.bb.integration.util.BlackboardUtil;
import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.lamsfoundation.bb.integration.util.LineitemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import blackboard.base.FormattedText;
import blackboard.base.InitializationException;
import blackboard.data.ValidationException;
import blackboard.data.content.Content;
import blackboard.data.content.CourseDocument;
import blackboard.data.course.Course;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Container;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.PkId;
import blackboard.persist.content.ContentDbLoader;
import blackboard.persist.content.ContentDbPersister;
import blackboard.platform.BbServiceException;
import blackboard.platform.BbServiceManager;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManager;
import blackboard.platform.persistence.PersistenceServiceFactory;
import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;
import blackboard.portal.data.ExtraInfo;
import blackboard.portal.data.PortalExtraInfo;
import blackboard.portal.servlet.PortalUtil;

/**
 * Shows startLesson page and modifyLesson pages, also handles subsequent start and modification of LAMS lessons.
 */
public class LessonManagerServlet extends HttpServlet {

    private static final long serialVersionUID = -351131323404991332L;
    private static Logger logger = LoggerFactory.getLogger(LessonManagerServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	// Authorise current user for Course Control Panel (automatic redirect)
	try {
	    if (!PlugInUtil.authorizeForCourseControlPanel(request, response))
		return;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	}

	ContextManager ctxMgr = null;
	try {
	    // get Blackboard context
	    ctxMgr = (ContextManager) BbServiceManager.lookupService(ContextManager.class);
	    Context ctx = ctxMgr.getContext();

	    String method = request.getParameter("method");
	    // -----------------------Start lesson functions ---------------------------
	    if (method.equals("showStartLessonPage")) {
		showStartLessonPage(request, response, ctx);
		
	    } else if (method.equals("start")) {
		start(request, response, ctx);
		
	    // -----------------------Modify lesson functions ---------------------------
	    } else if (method.equals("showModifyLessonPage")) {
		showModifyLessonPage(request, response, ctx);
		
	    } else if (method.equals("modify")) {
		modify(request, response, ctx);

	    // -----------------------Delete lesson functions ---------------------------
	    } else if (method.equals("delete")) {
		delete(request, response, ctx);
	    }

	} catch (InitializationException e) {
	    throw new ServletException(e);
	} catch (BbServiceException e) {
	    throw new ServletException(e);
	} catch (PersistenceException e) {
	    throw new ServletException(e);
	}  catch (ParseException e) {
	    throw new ServletException(e);
	} catch (ValidationException e) {
	    throw new ServletException(e);
	} catch (ParserConfigurationException e) {
	    throw new ServletException(e);
	} catch (SAXException e) {
	    throw new ServletException(e);
	}
	
//	// important. make sure context is not released as otherwise <bbNG:genericPage> tag will throw an exception
//	finally {
// 		if (ctxMgr != null) {
//			ctxMgr.releaseContext();
//		}
//    	}
    }
    
    /**
     * Shows preparation page with available learning designs. Preview is available.
     */
    private void showStartLessonPage(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException, ServletException {

	String lamsServerUrl = LamsPluginUtil.getServerUrl();
	request.setAttribute("lamsServerUrl", lamsServerUrl);

	// get all user accessible folders and LD descriptions as JSON
	String learningDesigns = LamsSecurityUtil.getLearningDesigns(ctx, ctx.getCourse().getCourseId(), null);
	request.setAttribute("learningDesigns", learningDesigns);

	request.getRequestDispatcher("/modules/create.jsp").forward(request, response);
    }
    
    /**
     * Starts preview lesson on LAMS server. Launches it.
     */
    private void start(HttpServletRequest request, HttpServletResponse response, Context ctx) throws IOException, ServletException, PersistenceException, ParseException, ValidationException, ParserConfigurationException, SAXException {
	
    	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();

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
	
	request.getRequestDispatcher("/modules/startLessonSuccess.jsp").forward(request, response);
    }

    private void showModifyLessonPage(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException, ServletException {

	// retrive the LAMS lesson
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	Container bbContainer = bbPm.getContainer();
	Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, request.getParameter("content_id"));
	ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader(ContentDbLoader.TYPE);
	Content bbContent = (Content) courseDocumentLoader.loadById(contentId);

	// get LAMS lessons's properties
	Calendar startDate = bbContent.getStartDate();
	Calendar endDate = bbContent.getEndDate();
	FormattedText description = bbContent.getBody();
	
	request.setAttribute("bbContent", bbContent);
	request.setAttribute("startDate", startDate);
	request.setAttribute("endDate", endDate);
	request.setAttribute("description", description);
	
	request.getRequestDispatcher("/modules/modify.jsp").forward(request, response);
    }
    
    private void modify(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException, ValidationException,
	    ServletException, ParserConfigurationException, SAXException, ParseException {

	String _course_id = request.getParameter("course_id");
	String _content_id = request.getParameter("content_id");

        // Retrieve the Db persistence manager from the persistence service
        BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
        Container bbContainer = bbPm.getContainer();

        // Internal Blackboard IDs for the course and parent content item
        Id courseId = bbPm.generateId(Course.DATA_TYPE, _course_id);
        Id contentId = new PkId( bbContainer, CourseDocument.DATA_TYPE, _content_id);

        // Load the content item
        ContentDbLoader courseDocumentLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );
        Content bbContent = (Content)courseDocumentLoader.loadById( contentId );

        // Get the form parameters and convert into correct data types
        // TODO: Use bb text area instead
        String strTitle = request.getParameter("title").trim();
        String strDescription = request.getParameter("descriptiontext").trim();
        FormattedText description = new FormattedText(strDescription, FormattedText.Type.HTML);
        
        String strIsAvailable = request.getParameter("isAvailable");
        String strIsGradecenter = request.getParameter("isGradecenter");
        String strIsTracked = request.getParameter("isTracked");
        boolean isAvailable = strIsAvailable.equals("true")?true:false;
        boolean isGradecenter = strIsGradecenter.equals("true")?true:false;
        boolean isTracked = strIsTracked.equals("true")?true:false;
        
        String strStartDate = request.getParameter("lessonAvailability_start_datetime");
        String strEndDate = request.getParameter("lessonAvailability_end_datetime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strStartDateCheckbox = request.getParameter("lessonAvailability_start_checkbox");
        String strEndDateCheckbox = request.getParameter("lessonAvailability_end_checkbox");
        
        //if teacher turned Gradecenter option ON (and it was OFF previously) - create lineitem
        if (!bbContent.getIsDescribed() && isGradecenter) {
	    String username = ctx.getUser().getUserName();
	    LineitemUtil.createLineitem(bbContent, username);
            
        //if teacher turned Gradecenter option OFF (and it was ON previously) - remove lineitem
        } else if (bbContent.getIsDescribed() && !isGradecenter) {
            LineitemUtil.removeLineitem(_content_id, _course_id);
            
        //change existing lineitem's name if lesson name has been changed
        } else if (isGradecenter && !strTitle.equals(bbContent.getTitle())) {
            LineitemUtil.changeLineitemName(_content_id, _course_id, strTitle);
        }
    
        // Set LAMS content data in Blackboard
        bbContent.setTitle(strTitle);
        bbContent.setIsAvailable(isAvailable);
        bbContent.setIsDescribed(isGradecenter);//isDescribed field is used for storing isGradecenter parameter
        bbContent.setIsTracked(isTracked);
        bbContent.setBody(description);
    
        // Set Availability Dates
	// Clear the date (set to null) if the checkbox is unchecked
	// Start Date
	Calendar startDate = null;
	if (strStartDateCheckbox != null && strStartDateCheckbox.equals("1")) {
	    startDate = Calendar.getInstance();
	    startDate.setTime(formatter.parse(strStartDate));
	}
        bbContent.setStartDate(startDate);
        // End Date
	Calendar endDate = null;
	if (strEndDateCheckbox != null && (strEndDateCheckbox.equals("1"))) {
	    endDate = Calendar.getInstance();
	    endDate.setTime(formatter.parse(strEndDate));
	}
	bbContent.setEndDate(endDate);

        //Persist the Modified Lesson Object in Blackboard    
        ContentDbPersister persister= (ContentDbPersister) bbPm.getPersister( ContentDbPersister.TYPE );
        persister.persist( bbContent );
    
        String strReturnUrl = PlugInUtil.getEditableContentReturnURL(bbContent.getParentId(), courseId);
        request.setAttribute("strReturnUrl", strReturnUrl);
        
        request.getRequestDispatcher("/modules/modifyLessonSuccess.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response, Context ctx)
	    throws InitializationException, BbServiceException, PersistenceException, IOException, ServletException, ParserConfigurationException, SAXException {

	//remove Lineitem object from Blackboard DB
	String _content_id = request.getParameter("content_id");
	String _course_id = request.getParameter("course_id");
	LineitemUtil.removeLineitem(_content_id, _course_id);
	
	// remove internalContentId -> externalContentId key->value pair (it's used for GradebookServlet)
	PortalExtraInfo pei = PortalUtil.loadPortalExtraInfo(null, null, "LamsStorage");
	ExtraInfo ei = pei.getExtraInfo();
	ei.clearEntry(_content_id);
	PortalUtil.savePortalExtraInfo(pei);
	
	//remove lesson from LAMS server
	BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	Container bbContainer = bbPm.getContainer();
	ContentDbLoader courseDocumentLoader = ContentDbLoader.Default.getInstance();
	Id contentId = new PkId(bbContainer, CourseDocument.DATA_TYPE, _content_id);
	Content bbContent = courseDocumentLoader.loadById(contentId);
	String lsId = bbContent.getLinkRef();
	String userName = ctx.getUser().getUserName();
	Boolean isDeletedSuccessfully = LamsSecurityUtil.deleteLesson(userName, lsId);
	
	System.out.println("Lesson (bbContentId:" + _content_id + ") successfully deleted by userName:" + userName);
    }
    


}
