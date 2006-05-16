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
		if(mode.isAuthor()){
			session.removeAttribute(VoteAction.MAP_OPTIONS_CONTENT);
			session.removeAttribute(VoteAction.ACTIVITY_INSTRUCTIONS);
			session.removeAttribute(VoteAction.TOOL_SERVICE);
			session.removeAttribute(VoteAction.TOOL_CONTENT_ID);
			session.removeAttribute(VoteAction.MAX_OPTION_INDEX);
			session.removeAttribute(VoteAction.USER_EXCEPTION_NO_TOOL_SESSIONS);
			session.removeAttribute(VoteAction.ATTACHMENT_LIST);
			session.removeAttribute(VoteAction.DELETED_ATTACHMENT_LIST);
			session.removeAttribute(VoteAction.LIST_OFFLINEFILES_METADATA);
			session.removeAttribute(VoteAction.LIST_ONLINEFILES_METADATA);
			session.removeAttribute(VoteAction.DEFINE_LATER_IN_EDIT_MODE);
			session.removeAttribute(VoteAction.SHOW_AUTHORING_TABS);
			session.removeAttribute(VoteAction.IS_MONITORED_CONTENT_IN_USE);
			session.removeAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
			session.removeAttribute(VoteAction.DEFAULT_OPTION_CONTENT);
			session.removeAttribute(VoteAction.RICHTEXT_OFFLINEINSTRUCTIONS);
			session.removeAttribute(VoteAction.RICHTEXT_ONLINEINSTRUCTIONS);
			session.removeAttribute(VoteAction.ACTIVITY_TITLE);
			session.removeAttribute("optIndex");
			session.removeAttribute(VoteAction.LIST_UPLOADED_OFFLINE_FILENAMES);
			session.removeAttribute(VoteAction.LIST_UPLOADED_ONLINE_FILENAMES);
			session.removeAttribute(VoteAction.RICHTEXT_TITLE);
			session.removeAttribute(VoteAction.RICHTEXT_TITLE);
			session.removeAttribute(VoteAction.DEFAULT_CONTENT_ID_STR);
			session.removeAttribute(VoteAction.DEFAULT_CONTENT_ID);
			session.removeAttribute(VoteAction.ACTIVE_MODULE);
			session.removeAttribute(VoteAction.IS_DEFINE_LATER);
			
		}
	}

		
}
