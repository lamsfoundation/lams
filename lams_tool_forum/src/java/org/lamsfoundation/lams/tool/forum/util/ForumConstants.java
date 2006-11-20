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

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.forum.util;

/**
 * User: conradb
 * Date: 14/06/2005
 * Time: 10:33:00
 */
public interface ForumConstants {
	public static final String TOOL_SIGNATURE = "lafrum11";
	public static final String TOOL_CONTENT_HANDLER_NAME = "forumToolContentHandler";

	
	//See LDEV652 
	// For old style (Fiona's description): The oldest topic is at the top, does not depends the reply date etc.
	// For new style (Ernie's early desc): Most current popular forum used style: latest topics 
	// should be at the top, reply date will descide the order etc.
	public static final boolean OLD_FORUM_STYLE  = true;
	
	public static final int STATUS_CONTENT_COPYED = 1;
	public static final int SESSION_STATUS_FINISHED = 1;
	
    public final static int MAX_FILE_SIZE = 250 * 1000;
    public final static String FORUM_SERVICE = "forumService";
    
    public final static String CONTENT_HANDLER = "toolContentHandler";

	public static final String AUTHORING_DTO = "authoring";
	public static final String AUTHORING_TOPICS_LIST = "topicList";
	public static final String AUTHORING_TOPICS_INDEX = "topicIndex";
	public static final String AUTHORING_TOPIC_THREAD = "topicThread";
	public static final String AUTHORING_TOPIC = "topic";
	
	public static final String DEFAULT_TITLE = "Forum";
	//TODO:hard code!!! need to read from config
	public static final String TOOL_URL_BASE = "/lams/tool/lafrum11/";
	public static final String FORUM_ID = "forum_id";

	public static final String ATTR_ALLOW_EDIT = "allowEdit";
	public static final String ATTR_ALLOW_UPLOAD = "allowUpload";
	public static final String ATTR_ALLOW_NEW_TOPICS = "allowNewTopics";
	public static final String ATTR_ALLOW_RICH_EDITOR = "allowRichEditor";
	public static final String ATTR_LIMITED_CHARS = "limitedChars";

	public static final String ONLINE_ATTACHMENT = "online_att";
	public static final String OFFLINE_ATTACHMENT = "offline_att";

	public static final String ATTACHMENT_LIST = "attachmentList";
	public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";

	public static final String TOPIC_DELETED_ATTACHMENT_LIST = "topicDeletedAttachmentList";

	public static final String DELETED_AUTHORING_TOPICS_LIST = "deletedAuthoringTopicList";

	public static final String USER_UID = "userID";

	public static final String ATTR_FINISHED_LOCK = "finishedLock";
	
	public static final String ATTR_USER_FINISHED = "userFinished";
	
	// used in monitoring 
	public static final String TITLE = "title";
	public static final String INSTRUCTIONS = "instructions";
	public static final String PAGE_EDITABLE = "isPageEditable";

	public static final String ATTR_ROOT_TOPIC_UID = "rootUid";

	public static final String ATTR_FORUM_TITLE = "title";
	public static final String ATTR_FORUM_INSTRCUTION = "instruction";

	public static final String ATTR_TOOL_CONTENT_TOPICS = "ToolContentTopicList";
	public static final String ATTR_TOPIC = "topic";
	public static final String ATTR_USER= "user";
	public static final String ATTR_USER_UID= "userUid";
	public static final String ATTR_REPORT = "report";
	public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";
	
	public static final String PARAM_UPDATE_MODE = "updateMode";

	public static final String ATTR_NO_MORE_POSTS = "noMorePosts";

	public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

	public static final String ATTR_PARENT_TOPIC_ID = "parentID";

	public static final String ATTR_TOPIC_ID = "topicID";
	public static final String ATTR_REFLECTION_ON = "reflectOn";
	public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";
	public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";
	
	public static final String MARK_UPDATE_FROM_SESSION = "listAllMarks";
	public static final String MARK_UPDATE_FROM_USER = "listMarks";
	public static final String MARK_UPDATE_FROM_FORUM = "viewForum";



	
	
}
