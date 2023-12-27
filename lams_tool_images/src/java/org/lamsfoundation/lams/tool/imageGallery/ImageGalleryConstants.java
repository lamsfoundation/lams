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

package org.lamsfoundation.lams.tool.imageGallery;

public interface ImageGalleryConstants {
    public static final String TOOL_SIGNATURE = "laimag10";
    public static final String IMAGE_GALLERY_SERVICE = "laimagImageGalleryService";
    public static final String TOOL_CONTENT_HANDLER_NAME = "laimagImageGalleryToolContentHandler";
    public static final int COMPLETED = 1;

    // for action forward name
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    // for parameters' name
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";
    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";
    public static final String PARAM_IMAGE_INDEX = "imageIndex";
    public static final String PARAM_IMAGE_UID = "imageUid";
    public static final String PARAM_CURRENT_IMAGE = "currentImage";
    public static final String PARAM_IS_VOTED = "isVoted";
    public static final String PARAM_IS_AUTHOR = "isAuthor";

    // for request attribute name
    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";
    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";
    public static final String ATTR_IMAGE_LIST = "imageGalleryList";
    public static final String ATTR_DELETED_IMAGE_LIST = "deleteImageGalleryList";
    public static final String ATTR_DELETED_IMAGE_ATTACHMENT_LIST = "deletedItemAttachmmentList";;
    public static final String ATTR_IMAGE_GALLERY = "imageGallery";
    public static final String ATTR_IMAGE = "image";
    public static final String ATTR_SUMMARY_LIST = "summaryList";
    public static final String ATTR_IMAGE_SUMMARY = "imageSummary";
    public static final String ATTR_FINISH_LOCK = "finishedLock";
    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_IMAGE_GALLERY_FORM = "imageGalleryForm";
    public static final String ATTR_NEXT_IMAGE_TITLE = "nextImageTitle";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_INSTRUCTIONS = "instructions";
    public static final String ATTR_USER_FINISHED = "userFinished";
    public static final String ATTR_MEDIUM_IMAGE_DIMENSIONS = "mediumImageDimensions";
    public static final String ATTR_THUMBNAIL_IMAGE_DIMENSIONS = "thumbnailImageDimensions";
    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    // error message keys
    public static final String ERROR_MSG_FILE_BLANK = "error.resource.item.file.blank";
    public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";
    public static final String ERROR_MSG_REQUIRED_FIELDS_MISSING = "error.required.fields.missing";
    public static final String ERROR_MSG_ENTERED_VALUES_NOT_INTEGERS = "error.entered.values.not.integers";

    public static final String PAGE_EDITABLE = "isPageEditable";
    public static final String ATTR_USER_UID = "userUid";
    public static final String ATTR_IS_COMMENTS_ENABLED = "isCommentsEnabled";
}
