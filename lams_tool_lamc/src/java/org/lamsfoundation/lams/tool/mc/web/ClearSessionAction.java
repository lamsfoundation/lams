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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * This class give a chance to clear HttpSession when user save/close authoring page.
 * @author Steve.Ni
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/clearsession"   validate="false"
 *                
 * @version $Revision$
 */
public class ClearSessionAction extends LamsAuthoringFinishAction {

	@Override
	public void clearSession(HttpSession session, ToolAccessMode mode) {
		//only this tool save LAMS_AUTHORING_SUCCESS_FLAG into session, remove it!!!
		session.removeAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG);
		if(mode.isAuthor()){
			session.removeAttribute(McAction.SUBMIT_SUCCESS);
			session.removeAttribute(McAction.USER_EXCEPTION_QUESTION_EMPTY);
			session.removeAttribute(McAction.MAX_QUESTION_INDEX);
			session.removeAttribute(McAction.USER_EXCEPTION_WEIGHT_TOTAL);
			session.removeAttribute(McAction.MAP_WEIGHTS);
			session.removeAttribute(McAction.EDIT_OPTIONS_MODE);
			session.removeAttribute(McAction.MAP_GENERAL_OPTIONS_CONTENT);
			session.removeAttribute(McAction.MAP_GENERAL_SELECTED_OPTIONS_CONTENT);
			session.removeAttribute(McAction.MAP_INCORRECT_FEEDBACK);
			session.removeAttribute(McAction.MAP_CORRECT_FEEDBACK);
			session.removeAttribute(McAction.TOOL_CONTENT_ID);
			session.removeAttribute(McAction.SELECTED_QUESTION_INDEX);
			session.removeAttribute(McAction.DEFAULT_QUESTION_UID);
			session.removeAttribute(McAction.USER_EXCEPTION_ANSWERS_DUPLICATE);
			session.removeAttribute(McAction.USER_EXCEPTION_ANSWER_EMPTY);
			session.removeAttribute(McAction.LIST_UPLOADED_OFFLINE_FILENAMES);
			session.removeAttribute(McAction.LIST_UPLOADED_ONLINE_FILENAMES);
			session.removeAttribute(McAction.CREATION_DATE);
			session.removeAttribute(McAction.LIST_OFFLINEFILES_METADATA);
			session.removeAttribute(McAction.LIST_ONLINEFILES_METADATA);
			session.removeAttribute(McAction.QUESTIONS_WITHNO_OPTIONS);
			session.removeAttribute(McAction.RICHTEXT_CORRECT_FEEDBACK);
			session.removeAttribute(McAction.RICHTEXT_END_LEARNING_MSG);
			session.removeAttribute(McAction.RICHTEXT_OFFLINEINSTRUCTIONS);
			session.removeAttribute(McAction.RICHTEXT_ONLINEINSTRUCTIONS);
			session.removeAttribute(McAction.RICHTEXT_INSTRUCTIONS);
			session.removeAttribute(McAction.MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
			session.removeAttribute(McAction.MAP_STARTUP_GENERAL_OPTIONS_QUEID);
			session.removeAttribute(McAction.MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
			session.removeAttribute(McAction.SHOW_AUTHORING_TABS);
			session.removeAttribute(McAction.MAP_SELECTED_OPTIONS);
			session.removeAttribute(McAction.SELECTED_QUESTION);
			session.removeAttribute(McAction.DEFINE_LATER_IN_EDIT_MODE);
			session.removeAttribute(McAction.RICHTEXT_TITLE);
			session.removeAttribute(McAction.RICHTEXT_REPORT_TITLE);
			session.removeAttribute(McAction.DEFAULT_CONTENT_ID);
			session.removeAttribute(McAction.DEFINE_LATER);
			session.removeAttribute(McAction.MAP_DISABLED_QUESTIONS);
			session.removeAttribute(McAction.MAP_QUESTIONS_CONTENT);
			session.removeAttribute(McAction.IS_REVISITING_USER);
			session.removeAttribute(McAction.MAP_CHECKBOX_STATES);
			session.removeAttribute(McAction.ACTIVE_MODULE);
			session.removeAttribute("queIndex");
			session.removeAttribute(McAction.MAP_OPTIONS_CONTENT);
			session.removeAttribute(McAction.MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
			session.removeAttribute(McAction.PASSMARK);
			session.removeAttribute(McAction.TOOL_SERVICE);
			session.removeAttribute(McAction.MAP_DEFAULTOPTIONS_CONTENT);
			session.removeAttribute(McAction.RETRIES);
			session.removeAttribute(McAction.DEFINE_LATER_EDIT_ACTIVITY);
			session.removeAttribute("optionIndex");
		}
	}

		
}
