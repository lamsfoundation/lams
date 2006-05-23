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
package org.lamsfoundation.lams.tool.vote.web;

import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.authoring.web.LamsAuthoringFinishAction;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * This class give a chance to clear HttpSession when user save/close authoring page.
 * @author Steve.Ni
 * 
 * @version $Revision$
 * @author Ozgur Demirtas
 */
public class ClearSessionAction extends LamsAuthoringFinishAction implements VoteAppConstants {


	public void clearSession(HttpSession session, ToolAccessMode mode) {
	    session.removeAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG);
		if(mode.isAuthor()){
		    session.removeAttribute(TOOL_SERVICE);
		    session.removeAttribute(ACTIVE_MODULE);
		    session.removeAttribute(DEFINE_LATER_IN_EDIT_MODE);
		    session.removeAttribute(TOOL_CONTENT_ID);
		    session.removeAttribute(DEFAULT_CONTENT_ID);
		    session.removeAttribute(DEFAULT_CONTENT_ID_STR);
		    session.removeAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
		    session.removeAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
		    session.removeAttribute(LIST_OFFLINEFILES_METADATA);
		    session.removeAttribute(LIST_ONLINEFILES_METADATA);
		    session.removeAttribute(IS_DEFINE_LATER);
		    session.removeAttribute(REMOVABLE_QUESTION_INDEX);
		    session.removeAttribute(ACTIVITY_TITLE);
		    session.removeAttribute(ACTIVITY_INSTRUCTIONS);
		    session.removeAttribute(DEFAULT_OPTION_CONTENT);
		    session.removeAttribute(MAP_OPTIONS_CONTENT);
		    session.removeAttribute(MAX_OPTION_INDEX);
		    session.removeAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
		    session.removeAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
		    session.removeAttribute(LIST_OFFLINEFILES_METADATA);
		    session.removeAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
		    session.removeAttribute(LIST_ONLINEFILES_METADATA);
		    session.removeAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
		    session.removeAttribute(EDITACTIVITY_EDITMODE);
		    session.removeAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
		    session.removeAttribute(ATTACHMENT_LIST);
		    session.removeAttribute(DELETED_ATTACHMENT_LIST);
		    session.removeAttribute(IS_MONITORED_CONTENT_IN_USE);
		    session.removeAttribute(OPT_INDEX);
		    session.removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP);
		    session.removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);
		    session.removeAttribute(USER_EXCEPTION_CONTENT_IN_USE);
		    session.removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
		    session.removeAttribute(USER_EXCEPTION_FILENAME_EMPTY);
		    session.removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
		}
	}

		
}
