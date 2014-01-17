/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dto.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Ozgur Demirtas
 * 
 * Common utility functions live here.
 */
public abstract class QaUtils implements QaAppConstants {

    /**
     * 
     * @param request
     * @param defaultQaContent
     * @param qaAuthoringForm
     */
    public static void populateAuthoringDTO(HttpServletRequest request, QaContent defaultQaContent,
	    QaGeneralAuthoringDTO qaGeneralAuthoringDTO) {
	qaGeneralAuthoringDTO.setActivityTitle(defaultQaContent.getTitle());
	qaGeneralAuthoringDTO.setActivityInstructions(defaultQaContent.getInstructions());
    }

    public static String replaceNewLines(String text) {
	
	String newText = "";
	if (text != null) {
	    newText = text.replaceAll("\n", "<br>");
	    
	}

	return newText;
    }

    public static void setFormProperties(HttpServletRequest request, IQaService qaService,
	    QaAuthoringForm qaAuthoringForm, QaGeneralAuthoringDTO qaGeneralAuthoringDTO, String strToolContentID,
	    String defaultContentIdStr, String activeModule, SessionMap sessionMap, String httpSessionID) {

	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	qaAuthoringForm.setToolContentID(strToolContentID);

	if ((defaultContentIdStr != null) && (defaultContentIdStr.length() > 0))
	    qaAuthoringForm.setDefaultContentIdStr(new Long(defaultContentIdStr).toString());

	qaAuthoringForm.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);

	String synchInMonitor = request.getParameter(SYNC_IN_MONITOR);
	qaAuthoringForm.setSynchInMonitor(synchInMonitor);
	qaGeneralAuthoringDTO.setSynchInMonitor(synchInMonitor);

	String usernameVisible = request.getParameter(USERNAME_VISIBLE);
	qaAuthoringForm.setUsernameVisible(usernameVisible);
	qaGeneralAuthoringDTO.setUsernameVisible(usernameVisible);
	
	String allowRateAnswers = request.getParameter(ALLOW_RATE_ANSWERS);
	qaAuthoringForm.setAllowRateAnswers(allowRateAnswers);
	qaGeneralAuthoringDTO.setAllowRateAnswers(allowRateAnswers);

	String showOtherAnswers = request.getParameter("showOtherAnswers");
	qaAuthoringForm.setShowOtherAnswers(showOtherAnswers);
	qaGeneralAuthoringDTO.setShowOtherAnswers(showOtherAnswers);

	String questionsSequenced = request.getParameter(QUESTIONS_SEQUENCED);
	qaAuthoringForm.setQuestionsSequenced(questionsSequenced);
	qaGeneralAuthoringDTO.setQuestionsSequenced(questionsSequenced);

	String lockWhenFinished = request.getParameter("lockWhenFinished");
	qaAuthoringForm.setLockWhenFinished(lockWhenFinished);
	qaGeneralAuthoringDTO.setLockWhenFinished(lockWhenFinished);

	String reflect = request.getParameter(REFLECT);
	
	qaAuthoringForm.setReflect(reflect);
	qaGeneralAuthoringDTO.setReflect(reflect);

	String reflectionSubject = request.getParameter(REFLECTION_SUBJECT);
	
	qaAuthoringForm.setReflectionSubject(reflectionSubject);
	qaGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);

	
	
    }

    public static int getCurrentUserId(HttpServletRequest request) throws QaApplicationException {
	HttpSession ss = SessionManager.getSession();
	/* get back login user DTO */
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user.getUserID().intValue();
    }

    /**
     * 
     * @param long
     *                toolContentID
     * @return boolean determine whether a specific toolContentID exists in the
     *         db
     */
    public static boolean existsContent(long toolContentID, IQaService qaService) {
	QaContent qaContent = qaService.getQa(toolContentID);
	if (qaContent == null)
	    return false;

	return true;
    }

    /**
     * it is expected that the tool session id already exists in the tool
     * sessions table existsSession(long toolSessionId)
     * 
     * @param toolSessionId
     * @return boolean
     */
    public static boolean existsSession(long toolContentID, IQaService qaService) {
	QaSession qaSession = qaService.getSessionById(toolContentID);
	

	if (qaSession == null)
	    return false;

	return true;
    }

    public static String getFormattedDateString(Date date) {
	return (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date));
    }

    /**
     * builds a String based map from a list convertToMap(List sessionsList)
     * 
     * @param sessionsList
     * @return Map
     */
    public static Map convertToStringMap(List sessionsList, String listType) {
	Map map = new TreeMap(new QaComparator());

	Iterator listIterator = sessionsList.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    if (listType.equals("String")) {
		String text = (String) listIterator.next();
		map.put(mapIndex.toString(), text);
	    } else if (listType.equals("Long")) {
		Long LongValue = (Long) listIterator.next();
		map.put(mapIndex.toString(), LongValue.toString());
	    }
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    /**
     * the only attributes kept are TOOL_SESSION and TOOL_CONTENT_ID and
     * ACTIVITY_TITLE ACTIVITY_INSTRUCTIONS
     * cleanUpSessionAbsolute(HttpServletRequest request)
     * 
     * @param request
     */
    public static void cleanUpSessionAbsolute(HttpServletRequest request) {
	request.getSession().removeAttribute(MY_SIGNATURE);
	request.getSession().removeAttribute(ERROR_QAAPPLICATION);
	request.getSession().removeAttribute(TARGET_MODE);
	request.getSession().removeAttribute(TARGET_MODE_AUTHORING);
	request.getSession().removeAttribute(TARGET_MODE_LEARNING);
	request.getSession().removeAttribute(TARGET_MODE_MONITORING);
	request.getSession().removeAttribute(TARGET_MODE_EXPORT_PORTFOLIO);
	request.getSession().removeAttribute(AUTHORING_STARTER);
	request.getSession().removeAttribute(LOAD_LEARNER);
	request.getSession().removeAttribute(LEARNING_STARTER);
	request.getSession().removeAttribute(MONITORING_STARTER);
	request.getSession().removeAttribute(LOAD_MONITORING);
	request.getSession().removeAttribute(EDIT_RESPONSE);
	request.getSession().removeAttribute(EDITABLE_RESPONSE_ID);
	request.getSession().removeAttribute(COPY_TOOL_CONTENT);
	request.getSession().removeAttribute(ERROR_LIST);
	request.getSession().removeAttribute(ERROR_LIST_LEARNER);
	request.getSession().removeAttribute(DEFAULT_CONTENT_ID_STR);
	request.getSession().removeAttribute(TOOL_SESSION_ID);
	request.getSession().removeAttribute(LOAD);
	request.getSession().removeAttribute(LOAD_QUESTIONS);
	request.getSession().removeAttribute(LOAD_STARTER);
	request.getSession().removeAttribute(IS_DEFINE_LATER);
	request.getSession().removeAttribute(LEARNING_MODE);
	request.getSession().removeAttribute(IS_ADD_QUESTION);
	request.getSession().removeAttribute(IS_REMOVE_QUESTION);
	request.getSession().removeAttribute(IS_REMOVE_CONTENT);
	request.getSession().removeAttribute(SELECTION_CASE);
	request.getSession().removeAttribute(MAP_QUESTION_CONTENT);
	request.getSession().removeAttribute(DEFAULT_QUESTION_CONTENT);
	request.getSession().removeAttribute(END_LEARNING_MESSSAGE);
	request.getSession().removeAttribute(ON);
	request.getSession().removeAttribute(OFF);
	request.getSession().removeAttribute(RICHTEXT_TITLE);
	request.getSession().removeAttribute(RICHTEXT_INSTRUCTIONS);
	request.getSession().removeAttribute(RICHTEXT_BLANK);
	request.getSession().removeAttribute(USER_ID);
	request.getSession().removeAttribute(NOT_ATTEMPTED);
	request.getSession().removeAttribute(INCOMPLETE);
	request.getSession().removeAttribute(COMPLETED);
	request.getSession().removeAttribute(MAP_TOOL_SESSIONS);
	request.getSession().removeAttribute(MAX_TOOL_SESSION_COUNT.toString());
	request.getSession().removeAttribute(IS_TOOL_SESSION_CHANGED);
	request.getSession().removeAttribute(COUNT_SESSION_COMPLETE);
	request.getSession().removeAttribute(CURRENT_MONITORED_TOOL_SESSION);
	request.getSession().removeAttribute(COUNT_ALL_USERS);
	request.getSession().removeAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO);
	request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS);
	request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS_ID);
	request.getSession().removeAttribute(MODE);
	request.getSession().removeAttribute(LEARNER);
	request.getSession().removeAttribute(TEACHER);
	request.getSession().removeAttribute(PORTFOLIO_REPORT);
	request.getSession().removeAttribute(PORTFOLIO_REQUEST);
	request.getSession().removeAttribute(ADD_NEW_QUESTION);
	request.getSession().removeAttribute(REMOVE_QUESTION);
	request.getSession().removeAttribute(REMOVE_ALL_CONTENT);
	request.getSession().removeAttribute(SUBMIT_ALL_CONTENT);
	request.getSession().removeAttribute(SUBMIT_TAB_DONE);
	request.getSession().removeAttribute(OPTION_OFF);
	request.getSession().removeAttribute(MAP_QUESTION_CONTENT_LEARNER);
	request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
	request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
	request.getSession().removeAttribute(TOTAL_QUESTION_COUNT);
	request.getSession().removeAttribute(MAP_ANSWERS);
	request.getSession().removeAttribute(USER_FEEDBACK);
	request.getSession().removeAttribute(REPORT_TITLE);
	request.getSession().removeAttribute(MONITORING_REPORT_TITLE);
	request.getSession().removeAttribute(REPORT_TITLE_LEARNER);
	request.getSession().removeAttribute(END_LEARNING_MESSAGE);
	request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
	request.getSession().removeAttribute(FROM_TOOL_CONTENT_ID);
	request.getSession().removeAttribute(TO_TOOL_CONTENT_ID);
	request.getSession().removeAttribute(LEARNER_REPORT);
	request.getSession().removeAttribute(EDITACTIVITY_EDITMODE);
	request.getSession().removeAttribute(RENDER_MONITORING_EDITACTIVITY);
	request.getSession().removeAttribute(NO_AVAILABLE_SESSIONS);
	request.getSession().removeAttribute(NO_TOOL_SESSIONS_AVAILABLE);
	request.getSession().removeAttribute(TIMEZONE);
	request.getSession().removeAttribute(TIMEZONE_ID);
	request.getSession().removeAttribute(QUESTION_LISTING_MODE);
	request.getSession().removeAttribute(QUESTION_LISTING_MODE_SEQUENTIAL);
	request.getSession().removeAttribute(QUESTION_LISTING_MODE_COMBINED);
	request.getSession().removeAttribute(QUESTION_LISTING_MODE_COMBINED);
	request.getSession().removeAttribute(FEEDBACK_TYPE_SEQUENTIAL);
	request.getSession().removeAttribute(FEEDBACK_TYPE_COMBINED);
	request.getSession().removeAttribute(QUESTIONS);
	request.getSession().removeAttribute(SUBMIT_SUCCESS);
	request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
	request.getSession().removeAttribute(CURRENT_ANSWER);
	request.getSession().removeAttribute(ACTIVE_MODULE);
	request.getSession().removeAttribute(AUTHORING);
	request.getSession().removeAttribute(DEFINE_LATER_IN_EDIT_MODE);
	request.getSession().removeAttribute(SHOW_AUTHORING_TABS);
	request.getSession().removeAttribute(DEFINE_LATER);
	request.getSession().removeAttribute(SOURCE_MC_STARTER);
	request.getSession().removeAttribute(IS_MONITORED_CONTENT_IN_USE);
	request.getSession().removeAttribute(LOAD_MONITORING_CONTENT_EDITACTIVITY);
	request.getSession().removeAttribute(MONITORING_ORIGINATED_DEFINELATER);
	request.getSession().removeAttribute(REQUEST_LEARNING_REPORT);
	request.getSession().removeAttribute(REQUEST_LEARNING_REPORT_VIEWONLY);
	request.getSession().removeAttribute(REQUEST_PREVIEW);
	request.getSession().removeAttribute(REQUEST_LEARNING_REPORT_PROGRESS);

	request.getSession().removeAttribute(USER_EXCEPTION_WRONG_FORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_UNCOMPATIBLE_IDS);
	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_USER_DOESNOTEXIST);
	//request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOT_AVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTNUMERIC);
	request.getSession().removeAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_EXISTING);
	request.getSession().removeAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP);
	request.getSession().removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_IN_USE);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_INVALID);
	request.getSession().removeAttribute(USER_EXCEPTION_QUESTIONS_DUPLICATE);
    }

    /**
     * 
     * @param request
     * @param value
     * @param toolContentID
     */
    public static void setDefineLater(HttpServletRequest request, boolean value, String strToolContentID,
	    IQaService qaService) {

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	
	if (qaContent != null) {
	    qaContent.setDefineLater(value);
	    qaService.updateQa(qaContent);
	}
    }

    /**
     * determines the struts level location to return
     * 
     * @param sourceMcStarter
     * @param requestedModule
     * @return
     */
    public static String getDestination(String sourceMcStarter, String requestedModule) {
	if (requestedModule.equals(DEFINE_LATER)) {
	    //request is from define Later url. return to  LOAD_VIEW_ONLY
	    return LOAD_VIEW_ONLY;
	} else if (requestedModule.equals(AUTHORING)) {
	    //request is from authoring url. return to  LOAD_QUESTIONS
	    return LOAD_QUESTIONS;
	} else {
	    //request is from an unknown source. return null
	    return null;
	}
    }

    public static void setDefineLater(HttpServletRequest request, boolean value, IQaService qaService,
	    String toolContentID) {

	QaContent qaContent = qaService.getQa(new Long(toolContentID).longValue());
	
	if (qaContent != null) {
	    qaContent.setDefineLater(value);
	    qaService.updateQa(qaContent);
	}
    }

    public static String getCurrentLearnerID() {
	String userID = "";
	HttpSession ss = SessionManager.getSession();
	

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user != null) && (user.getUserID() != null)) {
		userID = user.getUserID().toString();
	    }
	}
	return userID;
    }

}
