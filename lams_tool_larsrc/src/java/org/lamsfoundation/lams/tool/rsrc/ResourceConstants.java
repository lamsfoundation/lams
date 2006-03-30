/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.rsrc;

public class ResourceConstants {
	public static final String TOOL_SIGNNATURE = "larsrc11";
	public static final String RESOURCE_SERVICE = "resourceService";
	
	//resource type;
	public static final short RESOURCE_TYPE_URL = 1;
	public static final short RESOURCE_TYPE_FILE = 2;
	public static final short RESOURCE_TYPE_WEBSITE = 3;
	public static final short RESOURCE_TYPE_LEARNING_OBJECT = 4;
	
	//for action forward name
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	
	//for parameters' name
	public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";
	public static final String PARAM_ITEM_INDEX = "itemIndex";
	
	//for request attribute name
	public static final String ATTR_INSTRUCTION_LIST = "instructionList";
	public static final String ATTR_RESOURCE_LIST = "resourceList";
	public static final String ATT_ATTACHMENT_LIST = "instructionAttachmentList";
	public static final String ATTR_DELETED_RESOURCE_LIST = "deleteResourceList";
	public static final String ATTR_DELETED_ATTACHMENT_LIST = "deletedAttachmmentList";
	
	//error message keys
	public static final String ERROR_MSG_TITLE_BLANK = "error.resource.item.title.blank";
	public static final String ERROR_MSG_URL_BLANK = "error.resource.item.url.blank";
	public static final String ERROR_MSG_DESC_BLANK = "error.resource.item.desc.blank";
	public static final String ERROR_MSG_FILE_BLANK = "error.resource.item.file.blank";
	public static final String ERROR_MSG_INVALID_URL = "error.resource.item.invalid.url";

}
