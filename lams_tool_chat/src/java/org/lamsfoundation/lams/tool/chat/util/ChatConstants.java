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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.util;

public interface ChatConstants {
	public static final String TOOL_SIGNATURE = "lachat11";
	
	// Chat session status
	public static final Integer SESSION_NOT_STARTED = new Integer(0);
	public static final Integer SESSION_IN_PROGRESS = new Integer(1);
	public static final Integer SESSION_COMPLETED = new Integer(2);	
	
	public static final String AUTHORING_DEFAULT_TAB = "1";
	public static final String ATTACHMENT_LIST = "attachmentList";
	public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";
	public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
	public static final String AUTH_SESSION_ID = "authoringSessionId";
	
	public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;
	
	// TODO this is temporary for developement.  this should not remain here.
	static final String XMPPDOMAIN = "shaun.melcoe.mq.edu.au";
	static final String XMPPCONFERENCE = "conference.shaun.melcoe.mq.edu.au";
	static final String XMPP_ADMIN_USERNAME = "admin";
	static final String XMPP_ADMIN_PASSWORD = "wildfire";
}
