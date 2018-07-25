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


package org.lamsfoundation.lams.tool.kaltura.util;

public interface KalturaConstants {
    public static final String TOOL_SIGNATURE = "lakalt11";

    // Attribute names
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_KALTURA = "kaltura";
    public static final String ATTR_ITEM = "item";
    public static final String ATTR_ITEMS = "items";
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_SUBMISSION_DEADLINE = "submissionDeadline";
    public static final String ATTR_SUBMISSION_DEADLINE_DATESTRING = "submissionDateString";
    public static final String ATTR_IS_SUBMISSION_DEADLINE_PASSED = "isSubmissionDeadlinePassed";
    public static final String ATTR_ITEM_LIST = "itemList";
    public static final String ATTR_DELETED_ITEM_LIST = "deletedItemList";
    public static final String ATTR_REFLECTION_ON = "reflectOn";
    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";
    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";
    public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";
    public static final String ATTR_FINISHED_LOCK = "finishedLock";
    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";
    public static final String ATTR_USER_FINISHED = "userFinished";
    public static final String ATTR_COMMENT = "comment";
    public static final String ATTR_IS_ALLOW_UPLOADS = "isAllowUpload";
    public static final String ATTR_IS_USER_ITEM_AUTHOR = "isUserItemAuthor";
    public static final String ATTR_IS_GROUP_MONITORING = "isGroupMonitoring";
    public static final String ATTR_REFLECT_LIST = "reflectList";

    // Parameter names
    public static final String PARAM_ITEM_INDEX = "itemIndex";
    public static final String PARAM_ITEM_UID = "itemUid";
    public static final String PARAM_ITEM_TITLE = "itemTitle";
    public static final String PARAM_ITEM_DURATION = "itemDuration";
    public static final String PARAM_ITEM_ENTRY_ID = "itemEntryId";
    public static final String PARAM_COMMENT_UID = "commentUid";
    public static final String PARAM_IS_HIDING = "isHiding";

    //tool outputs
    public static final String LEARNER_NUMBER_VIEWED_VIDEOS = "learner.number.viewed.videos";
    public static final String LEARNER_NUMBER_UPLOADED_VIDEOS = "learner.number.uploaded.videos";

    //forward names
    public static final String SUCCESS = "success";
    public static final String VIEW_ITEM = "viewitem";
    public static final String COMMENT_LIST = "commentlist";
    public static final String ITEM_LIST = "itemlist";
    public static final String PREVIEW = "preview";
    public static final String NOTEBOOK = "notebook";
    public static final String FINISH = "finish";
    public static final String GROUP_LEARNING = "groupLearning";

    public static final String ERROR_MSG_COMMENT_BLANK = "error.resource.image.comment.blank";

}
