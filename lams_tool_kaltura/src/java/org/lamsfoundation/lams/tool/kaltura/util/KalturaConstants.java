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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.kaltura.util;

public interface KalturaConstants {
    public static final String TOOL_SIGNATURE = "lakalt11";

    // Kaltura session status
    public static final Integer SESSION_NOT_STARTED = new Integer(0);
    public static final Integer SESSION_IN_PROGRESS = new Integer(1);
    public static final Integer SESSION_COMPLETED = new Integer(2);

    public static final String AUTHORING_DEFAULT_TAB = "1";
    public static final String ATTACHMENT_LIST = "attachmentList";
    public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";
    public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
    public static final String AUTH_SESSION_ID = "authoringSessionId";

    public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;

    // Attribute names
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_KALTURA = "kaltura";
    public static final String ATTR_ITEM = "item";
    public static final String ATTR_ITEMS = "items";
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_ITEM_LIST = "itemList";
    public static final String ATTR_DELETED_ITEM_LIST = "deletedItemList";
    public static final String ATTR_REFLECTION_ON = "reflectOn";
    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";
    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";
    public static final String ATTR_REFLECT_LIST = "reflectList";
    public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";
    public static final String ATTR_FINISHED_LOCK = "finishedLock";
    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";
    public static final String ATTR_USER_FINISHED = "userFinished";
    public static final String ATTR_COMMENT = "comment";
    public static final String ATTR_IS_ALLOW_UPLOADS = "isAllowUpload";
    public static final String ATTR_IS_USER_ITEM_AUTHOR = "isUserItemAuthor";
    public static final String ATTR_IS_GROUP_MONITORING = "isGroupMonitoring";
    
    // Parameter names
    public static final String PARAM_PARENT_PAGE = "parentPage";
    public static final String PARAM_ORDER_ID = "orderId";
    public static final String PARAM_ITEM_INDEX = "itemIndex";
    public static final String PARAM_ITEM_UID = "itemUid";
    public static final String PARAM_ITEM_TITLE = "itemTitle";
    public static final String PARAM_ITEM_DURATION = "itemDuration";
    public static final String PARAM_ITEM_ENTRY_ID = "itemEntryId";
    public static final String PARAM_COMMENT_UID = "commentUid";
    public static final String PARAM_IS_HIDING = "isHiding";
    public static final String USER_ENTRY_DEFINITION_NAME = "user.entry.output.definition.kaltura";
    public static final String USER_ENTRY_DEFAULT_CONDITION_DISPLAY_NAME_KEY = "user.entry.output.definition.kaltura.default.condition";
    public static final String ALL_USERS_ENTRIES_DEFINITION_NAME = "all.users.entries.definition.kaltura";
    
    //forward names
    public static final String SUCCESS = "success";
    public static final String VIEW_ITEM = "viewitem";
    public static final String COMMENT_LIST = "commentlist";
    public static final String ITEM_LIST = "itemlist";
    public static final String PREVIEW = "preview";
    public static final String NOTEBOOK = "notebook";
    public static final String FINISH = "finish";    
    public static final String GROUP_LEARNING = "groupLearning";
    
    public static final String ERROR_MSG_CONDITION = "error.condition";
    public static final String ERROR_MSG_NAME_BLANK = "error.condition.name.blank";
    public static final String ERROR_MSG_NAME_DUPLICATED = "error.condition.duplicated.name";
    public static final String ERROR_MSG_COMMENT_BLANK = "error.resource.image.comment.blank";

}
