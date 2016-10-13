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

package org.lamsfoundation.lams.tool.qa.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Common utility functions live here.
 *
 * @author Ozgur Demirtas
 */
public abstract class QaUtils implements QaAppConstants {

    public static void setFormProperties(HttpServletRequest request, QaAuthoringForm qaAuthoringForm,
	    String strToolContentID, String httpSessionID) {

	qaAuthoringForm.setHttpSessionID(httpSessionID);

	qaAuthoringForm.setToolContentID(strToolContentID);

	String usernameVisible = request.getParameter(USERNAME_VISIBLE);
	qaAuthoringForm.setUsernameVisible(usernameVisible);

	String allowRateAnswers = request.getParameter(ALLOW_RATE_ANSWERS);
	qaAuthoringForm.setAllowRateAnswers(allowRateAnswers);

	String notifyTeachersOnResponseSubmit = request.getParameter(NOTIFY_TEACHERS_ON_RESPONSE_SUBMIT);
	qaAuthoringForm.setNotifyTeachersOnResponseSubmit(notifyTeachersOnResponseSubmit);

	String showOtherAnswers = request.getParameter("showOtherAnswers");
	qaAuthoringForm.setShowOtherAnswers(showOtherAnswers);

	String questionsSequenced = request.getParameter(QUESTIONS_SEQUENCED);
	qaAuthoringForm.setQuestionsSequenced(questionsSequenced);

	String lockWhenFinished = request.getParameter("lockWhenFinished");
	qaAuthoringForm.setLockWhenFinished(lockWhenFinished);

	int minimumRates = WebUtil.readIntParam(request, MINIMUM_RATES, true) == null ? 0
		: WebUtil.readIntParam(request, MINIMUM_RATES);
	qaAuthoringForm.setMinimumRates(minimumRates);

	int maximumRates = WebUtil.readIntParam(request, MAXIMUM_RATES, true) == null ? 0
		: WebUtil.readIntParam(request, MAXIMUM_RATES);
	qaAuthoringForm.setMaximumRates(maximumRates);

	String reflect = request.getParameter(REFLECT);

	qaAuthoringForm.setReflect(reflect);

	String reflectionSubject = request.getParameter(REFLECTION_SUBJECT);

	qaAuthoringForm.setReflectionSubject(reflectionSubject);

	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
    }

    /**
     * the only attributes kept are TOOL_SESSION and TOOL_CONTENT_ID and ACTIVITY_TITLE ACTIVITY_INSTRUCTIONS
     * cleanUpSessionAbsolute(HttpServletRequest request)
     *
     * @param request
     */
    public static void cleanUpSessionAbsolute(HttpServletRequest request) {
	request.getSession().removeAttribute(MY_SIGNATURE);
	request.getSession().removeAttribute(AUTHORING_STARTER);
	request.getSession().removeAttribute(LOAD_LEARNER);
	request.getSession().removeAttribute(LEARNING_STARTER);
	request.getSession().removeAttribute(MONITORING_STARTER);
	request.getSession().removeAttribute(EDITABLE_RESPONSE_ID);
	request.getSession().removeAttribute(COPY_TOOL_CONTENT);
	request.getSession().removeAttribute(TOOL_SESSION_ID);
	request.getSession().removeAttribute(LOAD_QUESTIONS);
	request.getSession().removeAttribute(LOAD_STARTER);
	request.getSession().removeAttribute(LEARNING_MODE);
	request.getSession().removeAttribute(IS_ADD_QUESTION);
	request.getSession().removeAttribute(IS_REMOVE_QUESTION);
	request.getSession().removeAttribute(IS_REMOVE_CONTENT);
	request.getSession().removeAttribute(MAP_QUESTION_CONTENT);
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
	request.getSession().removeAttribute(COUNT_SESSION_COMPLETE);
	request.getSession().removeAttribute(COUNT_ALL_USERS);
	request.getSession().removeAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO);
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
	request.getSession().removeAttribute(DEFINE_LATER);
	request.getSession().removeAttribute(SOURCE_MC_STARTER);
	request.getSession().removeAttribute(REQUEST_LEARNING_REPORT);
	request.getSession().removeAttribute(REQUEST_LEARNING_REPORT_VIEWONLY);
	request.getSession().removeAttribute(REQUEST_PREVIEW);
	request.getSession().removeAttribute(REQUEST_LEARNING_REPORT_PROGRESS);

	request.getSession().removeAttribute(USER_EXCEPTION_WRONG_FORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_UNCOMPATIBLE_IDS);
	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_USER_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTNUMERIC);
	request.getSession().removeAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_EXISTING);
	request.getSession().removeAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_IN_USE);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_INVALID);
	request.getSession().removeAttribute(USER_EXCEPTION_QUESTIONS_DUPLICATE);
    }

}
