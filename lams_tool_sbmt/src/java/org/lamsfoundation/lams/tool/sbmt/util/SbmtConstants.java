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

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.util;

public class SbmtConstants {

	public static final String TOOLSIGNNATURE = "lasbmt11";
	public static final String DEFAULT_TITLE = "Submit Files Title";

	public static final String AUTHORING_DTO = "authoring";

//	public static final String TOOL_SESSION_ID = "toolSessionID";
//	public static final String TOOL_CONTENT_ID = "toolContentID";
	public static final String USER_ID = "userID";
	
	//TODO: hard code, need to read from config file/db
	public static final String TOOL_URL_BASE = "/lams/tool/lasbmt11/";
	public static final String ATTACHMENT_LIST = "attachmentList";
	public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";
	public static final String READ_ONLY_MODE = "readOnlyMode";
}
