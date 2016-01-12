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

/* $Id$ */

package org.lamsfoundation.lams.comments;

/**
 * @author Fiona.Malikoff
 * 
 */
public class CommentLike {

    public static int LIKE = 1;
    public static int DISLIKE = -1;

    private Long uid;
    private Comment comment;
    private Integer userId;
    private int vote;

    public Long getUid() {
	return uid;
    }
	
    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Comment getComment() {
	return comment;
    }

    public void setComment(Comment comment) {
	this.comment = comment;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public int getVote() {
	return vote;
    }

    public void setVote(int vote) {
	this.vote = vote;
    }

}
