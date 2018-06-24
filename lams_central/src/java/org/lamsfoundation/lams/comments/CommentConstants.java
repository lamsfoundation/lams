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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.comments;

public class CommentConstants {

    public static final String ATTR_EXTERNAL_ID = "externalID";
    public static final String ATTR_EXTERNAL_SECONDARY_ID = "externalSecondaryID";
    public static final String ATTR_EXTERNAL_SIG = "externalSig";
    public static final String ATTR_EXTERNAL_TYPE = "externalType";
    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    public static final String ATTR_COMMENT_THREAD = "commentThread";
    public static final String ATTR_COMMENT = "comment";
    public static final String ATTR_PARENT_COMMENT_ID = "parentUid";
    public static final String ATTR_ROOT_COMMENT_UID = "rootUid";
    public static final String ATTR_BODY = "body";
    public static final String ATTR_COMMENT_ID = "commentUid";
    public static final String ATTR_LIKE_COUNT = "likeCount";
    public static final String ATTR_THREAD_ID = "threadUid";
    public static final String ATTR_ERR_MESSAGE = "errMessage";
    public static final String ATTR_HIDE_FLAG = "hideFlag";
    public static final String ATTR_STATUS = "status";
    public static final String ATTR_LIKE_AND_DISLIKE = "likeAndDislike";
    public static final String ATTR_ANONYMOUS = "anonymous"; // do we allow anonymous comments?
    public static final String ATTR_COMMENT_ANONYMOUS_NEW = "commentAnonymousNew"; // is this new comment anonymous?
    public static final String ATTR_COMMENT_ANONYMOUS_EDIT_ALLOW_UPDATE = "commentAnonymousAllowUpdate"; // is this new comment anonymous?
    public static final String ATTR_COMMENT_ANONYMOUS_EDIT = "commentAnonymousEdit"; // is this edited comment anonymous?
    public static final String ATTR_COMMENT_ANONYMOUS_REPLY = "commentAnonymousReply"; // is this edited comment anonymous?
    public static final String ATTR_READ_ONLY = "readOnly";
    public static final String ATTR_SORT_BY = "sortBy"; // 0 date, 1 likes
    public static final String ATTR_STICKY = "sticky"; // 0 date, 1 likes

    // for paging long topics & inlining reply
    public static final String PAGE_LAST_ID = "pageLastId";
    public static final String PAGE_SIZE = "pageSize";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String ATTR_NO_MORE_PAGES = "noMorePages";

    public static final int MAX_BODY_LENGTH = 5000;

    // message keys
    public static final String KEY_BODY_VALIDATION = "label.comment.body.validation";

}
