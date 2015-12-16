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
 
/* $Id$ */ 
package org.lamsfoundation.lams.comments.util;

public class CommentConstants {

    public static final String MODULE_NAME = "Comments";

    // type of comments - initially only 1 - tool comments! Found in ATTR_EXTERNAL_TYPE
    public static final int TYPE_TOOL = 1;
    
    public static final String ATTR_EXTERNAL_ID = "externalID";
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

    // for paging long topics & inlining reply
    public static final String PAGE_LAST_ID = "pageLastId";
    public static final String PAGE_SIZE = "size";
    public static final int DEFAULT_PAGE_SIZE = 2;
    public static final String ATTR_NO_MORE_PAGES = "noMorePages";

    public static final int MAX_BODY_LENGTH = 5000;
    
    // message keys
    public static final String KEY_BODY_VALIDATION = "label.body.validation";

    
}

