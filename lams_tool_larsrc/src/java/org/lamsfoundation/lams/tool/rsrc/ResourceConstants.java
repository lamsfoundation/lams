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
package org.lamsfoundation.lams.tool.rsrc;

public class ResourceConstants {
	public static final String TOOL_SIGNNATURE = "larsrc11";
	public static final String RESOURCE_SERVICE = "resourceService";
	
	public static final int COMPLETED = 1;
	
	//resource type;
	public static final short RESOURCE_TYPE_URL = 1;
	public static final short RESOURCE_TYPE_FILE = 2;
	public static final short RESOURCE_TYPE_WEBSITE = 3;
	public static final short RESOURCE_TYPE_LEARNING_OBJECT = 4;
	
	//for action forward name
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String DEFINE_LATER = "definelater";
	
	//for parameters' name
	public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";
	public static final String PARAM_TOOL_SESSION_ID = "toolSessionID"; 
	public static final String PARAM_FILE_VERSION_ID = "fileVersionId";
	public static final String PARAM_FILE_UUID = "fileUuid";
	public static final String PARAM_ITEM_INDEX = "itemIndex";
	public static final String PARAM_RESOURCE_ITEM_UID = "itemUid";
	
	//for request attribute name
	public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";
	public static final String ATTR_TOOL_SESSION_ID = "toolSessionID"; 
	public static final String ATTR_INSTRUCTION_LIST = "instructionList";
	public static final String ATTR_RESOURCE_ITEM_LIST = "resourceList";
	public static final String ATT_ATTACHMENT_LIST = "instructionAttachmentList";
	public static final String ATTR_DELETED_RESOURCE_ITEM_LIST = "deleteResourceList";
	public static final String ATTR_DELETED_ATTACHMENT_LIST = "deletedAttachmmentList";
	public static final String ATTR_DELETED_RESOURCE_ITEM_ATTACHMENT_LIST =  "deletedItemAttachmmentList";;
	public static final String ATT_LEARNING_OBJECT = "cpPackage";
	public static final String ATTR_RESOURCE_REVIEW_URL = "resourceItemReviewUrl";
	public static final String ATTR_RESOURCE = "resource";
	public static final String ATTR_RUN_AUTO = "runAuto";
	public static final String ATTR_RESOURCE_ITEM_UID = "itemUid";
	public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";
	public static final String ATTR_SUMMARY_LIST = "summaryList";
	public static final String ATTR_USER_LIST = "userList";
	
	//error message keys
	public static final String ERROR_MSG_TITLE_BLANK = "error.resource.item.title.blank";
	public static final String ERROR_MSG_URL_BLANK = "error.resource.item.url.blank";
	public static final String ERROR_MSG_DESC_BLANK = "error.resource.item.desc.blank";
	public static final String ERROR_MSG_FILE_BLANK = "error.resource.item.file.blank";
	public static final String ERROR_MSG_INVALID_URL = "error.resource.item.invalid.url";
	public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";
	
	
	public static final String PAGE_EDITABLE = "isPageEditable";

}
