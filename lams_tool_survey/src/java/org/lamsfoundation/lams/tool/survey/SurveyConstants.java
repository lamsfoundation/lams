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
/* $Id$ */
package org.lamsfoundation.lams.tool.survey;

public class SurveyConstants {
	public static final String TOOL_SIGNATURE = "lasurv11";
	public static final String RESOURCE_SERVICE = "lasurvSurveyService";
	
	public static final int COMPLETED = 1;
	
	//survey type;
	public static final short QUESTION_TYPE_SINGLE_CHOICE = 1;
	public static final short QUESTION_TYPE_MUTLIPLE_CHOICE = 2;
	public static final short QUESTION_TYPE_TEXT_ENTRY = 3;
	
	//for action forward name
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String DEFINE_LATER = "definelater";
	
	//for parameters' name
	public static final String PARAM_FILE_VERSION_ID = "fileVersionId";
	public static final String PARAM_FILE_UUID = "fileUuid";
	public static final String PARAM_ITEM_INDEX = "itemIndex";
	public static final String PARAM_RESOURCE_ITEM_UID = "itemUid";
	public static final String PARAM_RUN_OFFLINE = "runOffline";
	public static final String PARAM_TITLE = "title";
	public static final String ATTR_USER_UID = "userUid";
	
	
	//error message keys
	public static final String ERROR_MSG_TITLE_BLANK = "error.survey.item.title.blank";
	public static final String ERROR_MSG_URL_BLANK = "error.survey.item.url.blank";
	public static final String ERROR_MSG_DESC_BLANK = "error.survey.item.desc.blank";
	public static final String ERROR_MSG_FILE_BLANK = "error.survey.item.file.blank";
	public static final String ERROR_MSG_INVALID_URL = "error.survey.item.invalid.url";
	public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";
	
	
	public static final String ATTR_REFLECTION_ON = "reflectOn";
	public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";
	public static final String ATTR_REFLECT_LIST = "reflectList";
	public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
	public static final String PAGE_EDITABLE = "isPageEditable";
	public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";

}
