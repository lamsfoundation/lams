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
 
/* $Id$ */  
package org.lamsfoundation.lams.cloud.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.cloud.CloudConstants;
import org.lamsfoundation.lams.cloud.service.ICloudService;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * Handles the general requests for content in gradebook
 * 
 * @author Andrey Balan
 * 
 * ----------------XDoclet Tags-------------------- 
 * @struts.action path="/index" parameter="dispatch"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name = "index" path = "/index.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class IndexAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(IndexAction.class);

    private static ICloudService gradebookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;
    
    private static final String INDEX = "index";
    private static final String ERROR = "error";    

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
//	ActionErrors errors = validateIncomingUrl(request);
//	if (!errors.isEmpty()) {
//	    this.addErrors(request, errors);
//	    return mapping.findForward(ERROR);
//	}
	
    	//http://lamscloud.com/?sequence_name=Andrey_Sequence&sequence_location=http://lamscommunity.org/download?seq_id=12312
	
//    	initServices();
    	String sequenceName = request.getParameter(CloudConstants.PARAM_SEQUENCE_NAME);
    	String sequenceLocation = request.getParameter(CloudConstants.PARAM_SEQUENCE_LOCATION);
    	
    	
	request.getSession().setAttribute(CloudConstants.ATTR_SEQUENCE_LOCATION, sequenceLocation);
	request.setAttribute(CloudConstants.ATTR_SEQUENCE_NAME, sequenceName);

	return mapping.findForward(INDEX);
    }
    
    /**
     * Gets the total mark for a user's lesson and writes the result in the 
     * response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward index(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    	    HttpServletResponse response) throws Exception {
	return null;
    }

    
    /**
     * Gets the total mark for a user's lesson and writes the result in the 
     * response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward getLessonMarkAggregate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    	    HttpServletResponse response) throws Exception {
    	initServices();

    	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
    	Integer userID = WebUtil.readIntParam(request, CloudConstants.PARAM_USERID);

    	Lesson lesson = lessonService.getLesson(lessonID);
    	User learner = (User) userService.findById(User.class, userID);


    	if (lesson != null && learner != null) {
    	    GradebookUserLesson lessonMark = gradebookService.getGradebookUserLesson(lessonID, userID);
    	    writeResponse(response, CONTENT_TYPE_TEXT_PLAIN, ENCODING_UTF8, lessonMark.getMark().toString());
    	} else {
    	    // Grid will handle error, just log and return null
    	    logger.error("Error: request for course gradebook data with null user or lesson. lessonID: " + lessonID);
    	}
    	return null;
    }
    
    /**
     * Gets the average mark for an activity and writes the result in the 
     * response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward getActivityMarkAverage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    	    HttpServletResponse response) throws Exception {
	initServices();

    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
    	Activity activity = gradebookService.getActivityById(activityID);
    	
    	if (activity != null) {
    	    Double averageMark = gradebookService.getAverageMarkForActivity(activityID);

    	    
    	    if (averageMark != null) {
    		writeResponse(response, CONTENT_TYPE_TEXT_PLAIN, ENCODING_UTF8, averageMark.toString());
    	    } else {
    		writeResponse(response, CONTENT_TYPE_TEXT_PLAIN, ENCODING_UTF8, CloudConstants.CELL_EMPTY);
    	    }
    	} else {
    	    // Grid will handle error, just log and return null
    	    logger.error("Error: request for course gradebook data with null activity. actvity: " + activityID);
    	}
    	return null;
    }
    
    /**
     * Gets the average mark for lesson and writes the result in the response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward getLessonMarkAverage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
    	    HttpServletResponse response) throws Exception {
	initServices();

    	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
    	Lesson lesson = lessonService.getLesson(lessonID);
    	
    	if (lesson != null) {
    	    Double averageMark = gradebookService.getAverageMarkForLesson(lessonID);
    	    
    	    if (averageMark != null) {
    		writeResponse(response, CONTENT_TYPE_TEXT_PLAIN, ENCODING_UTF8, averageMark.toString());
    	    } else {
    		writeResponse(response, CONTENT_TYPE_TEXT_PLAIN, ENCODING_UTF8, CloudConstants.CELL_EMPTY);
    	    }
    	} else {
    	    // Grid will handle error, just log and return null
    	    logger.error("Error: request for course gradebook data with null lesson. lesson: " + lessonID);
    	}
    	return null;
    }
    


    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser() {
	UserDTO userDTO = getUser();
	if (userDTO != null) {
	    return getUserService().getUserByLogin(userDTO.getLogin());
	} else {
	    return null;
	}
    }

    private void initServices() {
	getUserService();
	getLessonService();
	getGradebookService();
    }

    private IUserManagementService getUserService() {
	if (userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userService;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }

    private ICloudService getGradebookService() {
	if (gradebookService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    gradebookService = (ICloudService) ctx.getBean("cloudService");
	}
	return gradebookService;
    }
    
    /**
     * Validate imageGallery item.
     * 
     * @param itemForm
     * @return
     */
    private ActionErrors validateIncomingUrl(HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();

	// for edit validate: file already exist
	String sequenceName = request.getParameter(CloudConstants.PARAM_SEQUENCE_NAME);
	if (sequenceName == null || StringUtils.isEmpty(sequenceName)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(CloudConstants.ERROR_MSG_WRONG_URL_SEQUENCE_NAME));
	}

	// for edit validate: file already exist
	String sequenceLocation = request.getParameter(CloudConstants.PARAM_SEQUENCE_LOCATION);
	if (sequenceLocation == null || StringUtils.isEmpty(sequenceLocation)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(CloudConstants.ERROR_MSG_WRONG_URL_SEQUENCE_LOCATION));
	}

	return errors;
    }

}

