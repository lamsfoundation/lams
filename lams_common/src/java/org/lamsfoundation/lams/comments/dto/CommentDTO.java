package org.lamsfoundation.lams.comments.dto;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.comments.Comment;

public class CommentDTO {

    private Comment comment;
    private String authorname;
    private Integer authorUserId;
    private boolean isAuthor;
    private boolean hasAttachment;
    private short level;
    private int threadNum;
    private boolean liked;
    private boolean disliked;

    /**
     * Get a <code>CommentDTO</code> instance from a given <code>Comment</code>.
     *
     * @param msg
     * @return
     */
    public static CommentDTO getCommentDTO(Comment comment) {
	if (comment == null) {
	    return null;
	}

	CommentDTO dto = new CommentDTO();
	dto.setComment(comment);
	if (comment.getCreatedBy() != null) {
	    dto.setAuthorname(comment.getCreatedBy().getFullName());
	    dto.setAuthorUserId(comment.getCreatedBy().getUserId());
	}

	dto.liked = false;
	dto.disliked = false;

	return dto;
    }

    /**
     * Get a list of <code>CommentDTO</code> according to given list of <code>Comment</code>.
     *
     * @param msgList
     * @return
     */
    public static List<CommentDTO> getCommentDTO(List<Comment> commentList) {
	List<CommentDTO> retSet = new ArrayList<CommentDTO>();
	if (commentList == null || commentList.isEmpty()) {
	    return retSet;
	}

	Iterator<Comment> iter = commentList.iterator();
	while (iter.hasNext()) {
	    Comment msg = iter.next();
	    retSet.add(CommentDTO.getCommentDTO(msg));
	}
	return retSet;
    }

    //-------------------------------DTO get/set method------------------------------
    public String getAuthorname() {
	return authorname;
    }

    public void setAuthorname(String authorname) {
	this.authorname = authorname;
    }

    public boolean getHasAttachment() {
	return hasAttachment;
    }

    public void setHasAttachment(boolean isAttachment) {
	this.hasAttachment = isAttachment;
    }

    public Comment getComment() {
	return comment;
    }

    public void setComment(Comment comment) {
	this.comment = comment;
    }

    public short getLevel() {
	return level;
    }

    public void setLevel(short level) {
	this.level = level;
    }

    public int getThreadNum() {
	return threadNum;
    }

    public void setThreadNum(int threadNum) {
	this.threadNum = threadNum;
    }

    public boolean isAuthor() {
	return isAuthor;
    }

    public void setIsAuthor(boolean isAuthor) {
	this.isAuthor = isAuthor;
    }

    public boolean isLiked() {
	return liked;
    }

    public void setLiked(boolean liked) {
	this.liked = liked;
    }

    public boolean isDisliked() {
	return disliked;
    }

    public void setDisliked(boolean disliked) {
	this.disliked = disliked;
    }

    public Integer getAuthorUserId() {
	return authorUserId;
    }

    public void setAuthorUserId(Integer authorUserId) {
	this.authorUserId = authorUserId;
    }

}