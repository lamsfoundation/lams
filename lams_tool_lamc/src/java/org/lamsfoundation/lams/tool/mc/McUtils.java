/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.mc.web.McAuthoringForm;

/**
 * Common MCQ utility functions live here.
 *
 * @author Ozgur Demirtas
 */
public abstract class McUtils implements McAppConstants {

    /**
     * the only attributes kept are TOOL_SESSION and TOOL_CONTENT_ID and CURRENT_MONITORED_TOOL_SESSION
     *
     * @param request
     */
    public static void cleanUpSessionAbsolute(HttpServletRequest request) {
	request.getSession().removeAttribute(MY_SIGNATURE);
	request.getSession().removeAttribute(DEFAULT_CONTENT_ID);
	request.getSession().removeAttribute(ERROR_MCAPPLICATION);
	request.getSession().removeAttribute(LOAD_AUTHORING);
	request.getSession().removeAttribute("starter");
	request.getSession().removeAttribute(LEARNING_STARTER);
	request.getSession().removeAttribute("monitoringStarter");
	request.getSession().removeAttribute(LOAD_LEARNER);
	request.getSession().removeAttribute(LOAD_MONITORING_CONTENT);
	request.getSession().removeAttribute(REDO_QUESTIONS);
	request.getSession().removeAttribute(SINGLE_QUESTION_ANSWERS);
	request.getSession().removeAttribute(ERROR_LIST);
	request.getSession().removeAttribute(PREVIEW);
	request.getSession().removeAttribute(AUTHORING_CANCELLED);
	request.getSession().removeAttribute(DEFINE_LATER_EDIT_ACTIVITY);
	request.getSession().removeAttribute(EDIT_OPTIONS_MODE);
	request.getSession().removeAttribute(IS_ADD_QUESTION);
	request.getSession().removeAttribute(IS_REMOVE_QUESTION);
	request.getSession().removeAttribute(SUBMIT_SUCCESS);
	request.getSession().removeAttribute(MAP_QUESTIONS_CONTENT);
	request.getSession().removeAttribute(IS_REMOVE_CONTENT);
	request.getSession().removeAttribute(IS_REVISITING_USER);
	request.getSession().removeAttribute(USER);
	request.getSession().removeAttribute(TOOL_CONTENT_UID);
	request.getSession().removeAttribute(TOOL_SESSION_ID);
	request.getSession().removeAttribute(USER_ID);
	request.getSession().removeAttribute(MAX_QUESTION_INDEX);
	request.getSession().removeAttribute(COPY_TOOL_CONTENT);
	request.getSession().removeAttribute(REMOVE_TOOL_CONTENT);
	request.getSession().removeAttribute(MAP_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_DEFAULTOPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_DISABLED_QUESTIONS);
	request.getSession().removeAttribute(MAP_GENERAL_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_STARTUP_GENERAL_OPTIONS_QUEID);
	request.getSession().removeAttribute(QUESTIONS_WITHNO_OPTIONS);
	request.getSession().removeAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_LEARNER_QUESTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_LEARNER_CHECKED_OPTIONS_CONTENT);
	request.getSession().removeAttribute(MAP_LEARNER_ASSESSMENT_RESULTS);
	request.getSession().removeAttribute(MAP_LEARNER_FEEDBACK_INCORRECT);
	request.getSession().removeAttribute(MAP_LEARNER_FEEDBACK_CORRECT);
	request.getSession().removeAttribute(MAP_QUESTION_WEIGHTS);
	request.getSession().removeAttribute(MAP_WEIGHTS);
	request.getSession().removeAttribute(MAP_CHECKBOX_STATES);
	request.getSession().removeAttribute(MAP_SELECTED_OPTIONS);
	request.getSession().removeAttribute(MAP_FEEDBACK_INCORRECT);
	request.getSession().removeAttribute(MAP_FEEDBACK_CORRECT);
	request.getSession().removeAttribute(SELECTED_QUESTION);
	request.getSession().removeAttribute(SELECTED_QUESTION_INDEX);
	request.getSession().removeAttribute(DEFAULT_QUESTION_UID);
	request.getSession().removeAttribute(TITLE);
	request.getSession().removeAttribute(INSTRUCTIONS);
	request.getSession().removeAttribute(CREATION_DATE);
	request.getSession().removeAttribute(RICHTEXT_FEEDBACK_CORRECT);
	request.getSession().removeAttribute(RETRIES);
	request.getSession().removeAttribute(ON);
	request.getSession().removeAttribute(OFF);
	request.getSession().removeAttribute(RICHTEXT_FEEDBACK_INCORRECT);
	request.getSession().removeAttribute(PASSMARK);
	request.getSession().removeAttribute(SHOW_AUTHORING_TABS);
	request.getSession().removeAttribute(RICHTEXT_REPORT_TITLE);
	// request.getSession().removeAttribute(RICHTEXT_END_LEARNING_MSG);
	request.getSession().removeAttribute(RICHTEXT_TITLE);
	request.getSession().removeAttribute(RICHTEXT_INSTRUCTIONS);
	request.getSession().removeAttribute(RICHTEXT_BLANK);
	request.getSession().removeAttribute(COUNT_SESSION_COMPLETE);
	request.getSession().removeAttribute(COUNT_ALL_USERS);
	request.getSession().removeAttribute(COUNT_MAX_ATTEMPT);
	request.getSession().removeAttribute(TOP_MARK);
	request.getSession().removeAttribute(LOWEST_MARK);
	request.getSession().removeAttribute(AVERAGE_MARK);
	request.getSession().removeAttribute(NOT_ATTEMPTED);
	request.getSession().removeAttribute(INCOMPLETE);
	request.getSession().removeAttribute(COMPLETED);
	request.getSession().removeAttribute(MAP_TOOL_SESSIONS);
	request.getSession().removeAttribute(MAX_TOOL_SESSION_COUNT.toString());
	request.getSession().removeAttribute(ADD_NEW_QUESTION);
	request.getSession().removeAttribute(OPTION_OFF);
	request.getSession().removeAttribute(REMOVE_QUESTION);
	request.getSession().removeAttribute(REMOVE_ALL_CONTENT);
	request.getSession().removeAttribute(SUBMIT_ALL_CONTENT);
	request.getSession().removeAttribute(SUBMIT_TAB_DONE);
	request.getSession().removeAttribute(MAP_QUESTION_CONTENT_LEARNER);
	request.getSession().removeAttribute(CURRENT_QUESTION_INDEX);
	request.getSession().removeAttribute(TOTAL_QUESTION_COUNT);
	request.getSession().removeAttribute(LEARNER_MARK);
	request.getSession().removeAttribute(MAP_ANSWERS);
	request.getSession().removeAttribute(CURRENT_ANSWER);
	request.getSession().removeAttribute(USER_FEEDBACK);
	request.getSession().removeAttribute(REPORT_TITLE_LEARNER);
	request.getSession().removeAttribute(TOTAL_COUNT_REACHED);
	request.getSession().removeAttribute(IS_USERNAME_VISIBLE);
	request.getSession().removeAttribute(IS_CONTENT_IN_USE);
	request.getSession().removeAttribute(IS_RETRIES);
	request.getSession().removeAttribute(IS_SHOW_FEEDBACK);
	request.getSession().removeAttribute(IS_SHOW_LEARNERS_REPORT);
	request.getSession().removeAttribute(IS_ALL_SESSIONS_COMPLETED);
	request.getSession().removeAttribute(CHECK_ALL_SESSIONS_COMPLETED);
	request.getSession().removeAttribute(FROM_TOOL_CONTENT_ID);
	request.getSession().removeAttribute(TO_TOOL_CONTENT_ID);
	request.getSession().removeAttribute(LEARNER_REPORT);
	request.getSession().removeAttribute(MAP_USER_RESPONSES);
	request.getSession().removeAttribute(MAP_MAIN_REPORT);
	request.getSession().removeAttribute(MAP_STATS);
	request.getSession().removeAttribute(REPORT_TITLE_MONITOR);
	request.getSession().removeAttribute(MONITOR_USER_ID);
	request.getSession().removeAttribute(MONITORING_REPORT);
	request.getSession().removeAttribute(MONITORING_ERROR);
	request.getSession().removeAttribute(MAP_MONITORING_QUESTIONS);
	request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS);
	request.getSession().removeAttribute(MONITORED_CONTENT_ID);
	request.getSession().removeAttribute(EDITACTIVITY_EDITMODE);
	request.getSession().removeAttribute(FORM_INDEX);
	request.getSession().removeAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO);
	request.getSession().removeAttribute(PREVIEW_ONLY);
	request.getSession().removeAttribute(TIMEZONE);
	request.getSession().removeAttribute(MODE);
	request.getSession().removeAttribute(LEARNING_MODE);
	request.getSession().removeAttribute(EXPORT_USER_ID);
	request.getSession().removeAttribute(MAP_INCORRECT_FEEDBACK);
	request.getSession().removeAttribute(MAP_CORRECT_FEEDBACK);
	request.getSession().removeAttribute(MAP_INCORRECT_FEEDBACK_LEARNER);
	request.getSession().removeAttribute(MAP_CORRECT_FEEDBACK_LEARNER);
	request.getSession().removeAttribute(ACTIVITY_TITLE);
	request.getSession().removeAttribute(ACTIVITY_INSTRUCTIONS);
	request.getSession().removeAttribute(SUMMARY_TOOL_SESSIONS_ID);
    }

    /**
     *
     */
    public static void setFormProperties(HttpServletRequest request, McAuthoringForm mcAuthoringForm,
	    McGeneralAuthoringDTO mcGeneralAuthoringDTO, String strToolContentID, String httpSessionID) {

	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	mcAuthoringForm.setToolContentID(strToolContentID);

	String sln = request.getParameter("sln");
	mcAuthoringForm.setSln(sln);
	mcGeneralAuthoringDTO.setSln(sln);

	String questionsSequenced = request.getParameter("questionsSequenced");
	mcAuthoringForm.setQuestionsSequenced(questionsSequenced);
	mcGeneralAuthoringDTO.setQuestionsSequenced(questionsSequenced);

	String randomize = request.getParameter("randomize");
	mcAuthoringForm.setRandomize(randomize);
	mcGeneralAuthoringDTO.setRandomize(randomize);

	String showMarks = request.getParameter("showMarks");
	mcAuthoringForm.setShowMarks(showMarks);
	mcGeneralAuthoringDTO.setShowMarks(showMarks);

	String retries = request.getParameter("retries");
	mcAuthoringForm.setRetries(retries);
	mcGeneralAuthoringDTO.setRetries(retries);

	String reflect = request.getParameter(REFLECT);
	mcAuthoringForm.setReflect(reflect);
	mcGeneralAuthoringDTO.setReflect(reflect);

	String reflectionSubject = request.getParameter(REFLECTION_SUBJECT);
	mcAuthoringForm.setReflectionSubject(reflectionSubject);
	mcGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);

	String passmark = request.getParameter("passmark");
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);
    }

}
