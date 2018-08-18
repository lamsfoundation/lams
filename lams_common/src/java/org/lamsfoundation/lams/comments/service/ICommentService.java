/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.comments.service;

import java.util.List;

import org.lamsfoundation.lams.comments.Comment;
import org.lamsfoundation.lams.comments.dto.CommentDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;

public interface ICommentService {

    public static final Integer SORT_BY_DATE = 0;
    public static final Integer SORT_BY_LIKE = 1;

    /**
     * Gets the comments for a tool, based on the tool session and the tool's id. Allows for paging.
     * Will not include the sticky - get them using getTopicStickyThread()
     */
    List<CommentDTO> getTopicThread(Long externalId, Long externalSecondaryId, Integer externalType,
	    String externalSignature, Long lastMsgSeqId, Integer pageSize, Integer sortBy, String extraSortParam,
	    Integer userId);

    /** Gets all the sticky comments for a tool, based on the tool session and the tool's id */
    List<CommentDTO> getTopicStickyThread(Long externalId, Long externalSecondaryId, Integer externalType,
	    String externalSignature, Integer sortBy, String extraSortParam, Integer userId);

    /** Saves a comment - either creating a whole tree one if there is no parent or saving under the given parent. */
    Comment createReply(Long parentId, String replyText, User user, boolean isMonitor, boolean isAnonymous);

    Comment createReply(Comment parent, String replyText, User user, boolean isMonitor, boolean isAnonymous);

    /** Gets the dummy root for the comment system and if one doesn't exist for this session then set it up! */
    Comment createOrGetRoot(Long externalId, Long externalSecondaryId, Integer externalIdType, String externalSignature,
	    User user);

    /** Gets the dummy root. If it doesn't exist, returns null. */
    Comment getRoot(Long externalId, Long externalSecondaryId, Integer externalIdType, String externalSignature);

    /**
     * Get one complete thread within a topic Note that the return type is DTO.
     */
    List<CommentDTO> getThread(Long threadId, Integer sortBy, Integer userId);

    /** Get a single comment. Note that the return type is DTO */
    CommentDTO getComment(Long commentUid);

    /** Update the body in a comment, and update the modified time, who by, etc, etc.
     * If isAnonymous is null, do not update. Used to ensure monitoring changes do not accidently change the value. */
    Comment updateComment(Long commentUid, String newBody, User user, Boolean isAnonymous, boolean makeAuditEntry);

    /** Update the sticky flag for a comment */
    Comment updateSticky(Long commentUid, Boolean newSticky);

    /** Update the like list for Comment commentUid by likeVote. Returns true if like/dislike added. */
    boolean addLike(Long commentUid, User user, Integer likeVote);

    /** Update the hidden / not hidden flag */
    Comment hideComment(Long commentUid, User user, boolean status);

    /* Utility calls for the Action */
    MessageService getMessageService();
}