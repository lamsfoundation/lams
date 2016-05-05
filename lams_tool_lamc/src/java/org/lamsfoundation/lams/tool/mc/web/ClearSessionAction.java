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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.mc.web;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.authoring.web.LamsAuthoringFinishAction;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * This class give a chance to clear HttpSession when user save/close authoring page.
 *
 * @author Steve.Ni ----------------XDoclet Tags--------------------
 *
 * @struts:action path="/clearsession" validate="false"
 *
 */
public class ClearSessionAction extends LamsAuthoringFinishAction {

    @Override
    public void clearSession(String customiseSessionID, HttpSession session, ToolAccessMode mode) {
	// only this tool save LAMS_AUTHORING_SUCCESS_FLAG into session, remove it!!!
	session.removeAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG);
	if (mode.isAuthor()) {
	    session.removeAttribute(McAppConstants.SUBMIT_SUCCESS);
	    session.removeAttribute(McAppConstants.USER_EXCEPTION_QUESTION_EMPTY);
	    session.removeAttribute(McAppConstants.MAX_QUESTION_INDEX);
	    session.removeAttribute(McAppConstants.USER_EXCEPTION_WEIGHT_TOTAL);
	    session.removeAttribute(McAppConstants.MAP_WEIGHTS);
	    session.removeAttribute(McAppConstants.EDIT_OPTIONS_MODE);
	    session.removeAttribute(McAppConstants.MAP_GENERAL_OPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.MAP_INCORRECT_FEEDBACK);
	    session.removeAttribute(McAppConstants.MAP_CORRECT_FEEDBACK);
	    session.removeAttribute(McAppConstants.TOOL_CONTENT_ID);
	    session.removeAttribute(McAppConstants.SELECTED_QUESTION_INDEX);
	    session.removeAttribute(McAppConstants.DEFAULT_QUESTION_UID);
	    session.removeAttribute(McAppConstants.USER_EXCEPTION_ANSWERS_DUPLICATE);
	    session.removeAttribute(McAppConstants.USER_EXCEPTION_ANSWER_EMPTY);
	    session.removeAttribute(McAppConstants.USER_EXCEPTION_NO_TOOL_SESSIONS);
	    session.removeAttribute(McAppConstants.CREATION_DATE);
	    session.removeAttribute(McAppConstants.QUESTIONS_WITHNO_OPTIONS);
	    session.removeAttribute(McAppConstants.RICHTEXT_CORRECT_FEEDBACK);
	    session.removeAttribute(McAppConstants.RICHTEXT_INSTRUCTIONS);
	    session.removeAttribute(McAppConstants.MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.MAP_STARTUP_GENERAL_OPTIONS_QUEID);
	    session.removeAttribute(McAppConstants.MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.SHOW_AUTHORING_TABS);
	    session.removeAttribute(McAppConstants.MAP_SELECTED_OPTIONS);
	    session.removeAttribute(McAppConstants.SELECTED_QUESTION);
	    session.removeAttribute(McAppConstants.RICHTEXT_TITLE);
	    session.removeAttribute(McAppConstants.RICHTEXT_REPORT_TITLE);
	    session.removeAttribute(McAppConstants.DEFAULT_CONTENT_ID);
	    session.removeAttribute(McAppConstants.MAP_DISABLED_QUESTIONS);
	    session.removeAttribute(McAppConstants.MAP_QUESTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.IS_REVISITING_USER);
	    session.removeAttribute(McAppConstants.MAP_CHECKBOX_STATES);
	    session.removeAttribute("queIndex");
	    session.removeAttribute(McAppConstants.MAP_OPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.PASSMARK);
	    session.removeAttribute(McAppConstants.TOOL_SERVICE);
	    session.removeAttribute(McAppConstants.MAP_DEFAULTOPTIONS_CONTENT);
	    session.removeAttribute(McAppConstants.RETRIES);
	    session.removeAttribute(McAppConstants.DEFINE_LATER_EDIT_ACTIVITY);
	    session.removeAttribute("optionIndex");
	}
    }

}
