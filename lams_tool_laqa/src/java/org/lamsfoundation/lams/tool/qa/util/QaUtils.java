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

	String usernameVisible = request.getParameter(QaAppConstants.USERNAME_VISIBLE);
	qaAuthoringForm.setUsernameVisible(usernameVisible);

	String allowRateAnswers = request.getParameter(QaAppConstants.ALLOW_RATE_ANSWERS);
	qaAuthoringForm.setAllowRateAnswers(allowRateAnswers);

	String notifyTeachersOnResponseSubmit = request.getParameter(QaAppConstants.NOTIFY_TEACHERS_ON_RESPONSE_SUBMIT);
	qaAuthoringForm.setNotifyTeachersOnResponseSubmit(notifyTeachersOnResponseSubmit);

	String showOtherAnswers = request.getParameter("showOtherAnswers");
	qaAuthoringForm.setShowOtherAnswers(showOtherAnswers);

	String questionsSequenced = request.getParameter(QaAppConstants.QUESTIONS_SEQUENCED);
	qaAuthoringForm.setQuestionsSequenced(questionsSequenced);

	String lockWhenFinished = request.getParameter("lockWhenFinished");
	qaAuthoringForm.setLockWhenFinished(lockWhenFinished);

	int minimumRates = WebUtil.readIntParam(request, QaAppConstants.MINIMUM_RATES, true) == null ? 0
		: WebUtil.readIntParam(request, QaAppConstants.MINIMUM_RATES);
	qaAuthoringForm.setMinimumRates(minimumRates);

	int maximumRates = WebUtil.readIntParam(request, QaAppConstants.MAXIMUM_RATES, true) == null ? 0
		: WebUtil.readIntParam(request, QaAppConstants.MAXIMUM_RATES);
	qaAuthoringForm.setMaximumRates(maximumRates);

	String reflect = request.getParameter(QaAppConstants.REFLECT);

	qaAuthoringForm.setReflect(reflect);

	String reflectionSubject = request.getParameter(QaAppConstants.REFLECTION_SUBJECT);

	qaAuthoringForm.setReflectionSubject(reflectionSubject);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
    }

    /**
     * the only attributes kept are TOOL_SESSION and TOOL_CONTENT_ID and ACTIVITY_TITLE ACTIVITY_INSTRUCTIONS
     * cleanUpSessionAbsolute(HttpServletRequest request)
     *
     * @param request
     */
    public static void cleanUpSessionAbsolute(HttpServletRequest request) {
	request.getSession().removeAttribute(QaAppConstants.MY_SIGNATURE);
	request.getSession().removeAttribute(QaAppConstants.AUTHORING_STARTER);
	request.getSession().removeAttribute(QaAppConstants.LOAD_LEARNER);
	request.getSession().removeAttribute(QaAppConstants.LEARNING_STARTER);
	request.getSession().removeAttribute(QaAppConstants.MONITORING_STARTER);
	request.getSession().removeAttribute(QaAppConstants.EDITABLE_RESPONSE_ID);
	request.getSession().removeAttribute(QaAppConstants.COPY_TOOL_CONTENT);
	request.getSession().removeAttribute(QaAppConstants.TOOL_SESSION_ID);
	request.getSession().removeAttribute(QaAppConstants.LOAD_QUESTIONS);
	request.getSession().removeAttribute(QaAppConstants.LOAD_STARTER);
	request.getSession().removeAttribute(QaAppConstants.LEARNING_MODE);
	request.getSession().removeAttribute(QaAppConstants.IS_ADD_QUESTION);
	request.getSession().removeAttribute(QaAppConstants.IS_REMOVE_QUESTION);
	request.getSession().removeAttribute(QaAppConstants.IS_REMOVE_CONTENT);
	request.getSession().removeAttribute(QaAppConstants.MAP_QUESTION_CONTENT);
	request.getSession().removeAttribute(QaAppConstants.END_LEARNING_MESSSAGE);
	request.getSession().removeAttribute(QaAppConstants.ON);
	request.getSession().removeAttribute(QaAppConstants.OFF);
	request.getSession().removeAttribute(QaAppConstants.RICHTEXT_TITLE);
	request.getSession().removeAttribute(QaAppConstants.RICHTEXT_INSTRUCTIONS);
	request.getSession().removeAttribute(QaAppConstants.RICHTEXT_BLANK);
	request.getSession().removeAttribute(QaAppConstants.USER_ID);
	request.getSession().removeAttribute(QaAppConstants.NOT_ATTEMPTED);
	request.getSession().removeAttribute(QaAppConstants.INCOMPLETE);
	request.getSession().removeAttribute(QaAppConstants.COMPLETED);
	request.getSession().removeAttribute(QaAppConstants.MAP_TOOL_SESSIONS);
	request.getSession().removeAttribute(QaAppConstants.MAX_TOOL_SESSION_COUNT.toString());
	request.getSession().removeAttribute(QaAppConstants.COUNT_SESSION_COMPLETE);
	request.getSession().removeAttribute(QaAppConstants.COUNT_ALL_USERS);
	request.getSession().removeAttribute(QaAppConstants.LIST_MONITORED_ANSWERS_CONTAINER_DTO);
	request.getSession().removeAttribute(QaAppConstants.MODE);
	request.getSession().removeAttribute(QaAppConstants.LEARNER);
	request.getSession().removeAttribute(QaAppConstants.TEACHER);
	request.getSession().removeAttribute(QaAppConstants.ADD_NEW_QUESTION);
	request.getSession().removeAttribute(QaAppConstants.REMOVE_QUESTION);
	request.getSession().removeAttribute(QaAppConstants.REMOVE_ALL_CONTENT);
	request.getSession().removeAttribute(QaAppConstants.SUBMIT_ALL_CONTENT);
	request.getSession().removeAttribute(QaAppConstants.SUBMIT_TAB_DONE);
	request.getSession().removeAttribute(QaAppConstants.OPTION_OFF);
	request.getSession().removeAttribute(QaAppConstants.MAP_QUESTION_CONTENT_LEARNER);
	request.getSession().removeAttribute(QaAppConstants.CURRENT_QUESTION_INDEX);
	request.getSession().removeAttribute(QaAppConstants.CURRENT_QUESTION_INDEX);
	request.getSession().removeAttribute(QaAppConstants.TOTAL_QUESTION_COUNT);
	request.getSession().removeAttribute(QaAppConstants.MAP_ANSWERS);
	request.getSession().removeAttribute(QaAppConstants.USER_FEEDBACK);
	request.getSession().removeAttribute(QaAppConstants.REPORT_TITLE_LEARNER);
	request.getSession().removeAttribute(QaAppConstants.END_LEARNING_MESSAGE);
	request.getSession().removeAttribute(QaAppConstants.CHECK_ALL_SESSIONS_COMPLETED);
	request.getSession().removeAttribute(QaAppConstants.FROM_TOOL_CONTENT_ID);
	request.getSession().removeAttribute(QaAppConstants.TO_TOOL_CONTENT_ID);
	request.getSession().removeAttribute(QaAppConstants.LEARNER_REPORT);
	request.getSession().removeAttribute(QaAppConstants.EDITACTIVITY_EDITMODE);
	request.getSession().removeAttribute(QaAppConstants.RENDER_MONITORING_EDITACTIVITY);
	request.getSession().removeAttribute(QaAppConstants.NO_AVAILABLE_SESSIONS);
	request.getSession().removeAttribute(QaAppConstants.NO_TOOL_SESSIONS_AVAILABLE);
	request.getSession().removeAttribute(QaAppConstants.TIMEZONE);
	request.getSession().removeAttribute(QaAppConstants.TIMEZONE_ID);
	request.getSession().removeAttribute(QaAppConstants.QUESTION_LISTING_MODE);
	request.getSession().removeAttribute(QaAppConstants.QUESTION_LISTING_MODE_SEQUENTIAL);
	request.getSession().removeAttribute(QaAppConstants.QUESTION_LISTING_MODE_COMBINED);
	request.getSession().removeAttribute(QaAppConstants.QUESTION_LISTING_MODE_COMBINED);
	request.getSession().removeAttribute(QaAppConstants.FEEDBACK_TYPE_SEQUENTIAL);
	request.getSession().removeAttribute(QaAppConstants.FEEDBACK_TYPE_COMBINED);
	request.getSession().removeAttribute(QaAppConstants.QUESTIONS);
	request.getSession().removeAttribute(QaAppConstants.SUBMIT_SUCCESS);
	request.getSession().removeAttribute(QaAppConstants.IS_USERNAME_VISIBLE);
	request.getSession().removeAttribute(QaAppConstants.CURRENT_ANSWER);
	request.getSession().removeAttribute(QaAppConstants.DEFINE_LATER);
	request.getSession().removeAttribute(QaAppConstants.SOURCE_MC_STARTER);
	request.getSession().removeAttribute(QaAppConstants.REQUEST_PREVIEW);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_WRONG_FORMAT);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_UNCOMPATIBLE_IDS);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_USER_DOESNOTEXIST);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_USERID_NOTAVAILABLE);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_USERID_NOTNUMERIC);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_USERID_EXISTING);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_NO_TOOL_SESSIONS);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_NO_STUDENT_ACTIVITY);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_CONTENT_IN_USE);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_MODE_REQUIRED);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_MODE_INVALID);
	request.getSession().removeAttribute(QaAppConstants.USER_EXCEPTION_QUESTIONS_DUPLICATE);
    }

}
