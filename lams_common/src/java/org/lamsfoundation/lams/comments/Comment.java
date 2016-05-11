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


package org.lamsfoundation.lams.comments;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Fiona Malikoff
 *
 */
public class Comment implements Cloneable {

    // message types - initially just one, the tool type.
    public static final int EXTERNAL_TYPE_TOOL = 1; // externalId will be a toolSessionId

    private Long uid;

    private String body;

    private Date created;
    private User createdBy;
    private Date updated;
    private User updatedBy;

    private Date lastReplyDate;
    private int replyNumber;
    private boolean hideFlag;
    private boolean sticky;

    private Comment rootComment;
    private Comment threadComment;
    private short commentLevel;

    private Comment parent;
    private CommentSession session;

    /** Read only fields - calculated when loaded from the database */
    private Integer likeCount;
    private Integer vote;

    public Comment() {
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData(User user) {
	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	    this.setCreatedBy(user);
	}
	this.setUpdated(new Date(now));
	this.setUpdatedBy(user);
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Comment)) {
	    return false;
	}

	Comment genericEntity = (Comment) o;

	// uses same attributes to determine equality as
	// ConditionTopicComparator.compare()
	return new EqualsBuilder().append(this.body, genericEntity.getBody())
		.append(this.replyNumber, genericEntity.getReplyNumber())
		.append(this.createdBy, genericEntity.getCreatedBy())
		.append(this.updatedBy, genericEntity.getUpdatedBy()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(body).append(created).append(updated).append(createdBy)
		.append(updatedBy).toHashCode();
    }

    // **********************************************************
    // get/set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public User getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(User createdBy) {
	this.createdBy = createdBy;
    }

    public Date getUpdated() {
	return updated;
    }

    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    public User getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
	this.updatedBy = updatedBy;
    }

    public Date getLastReplyDate() {
	return lastReplyDate;
    }

    public void setLastReplyDate(Date lastReplyDate) {
	this.lastReplyDate = lastReplyDate;
    }

    public int getReplyNumber() {
	return replyNumber;
    }

    public void setReplyNumber(int replyNumber) {
	this.replyNumber = replyNumber;
    }

    public boolean isHideFlag() {
	return hideFlag;
    }

    public void setHideFlag(boolean hideFlag) {
	this.hideFlag = hideFlag;
    }

    public boolean isSticky() {
	return sticky;
    }

    public void setSticky(boolean sticky) {
	this.sticky = sticky;
    }

    public Comment getParent() {
	return parent;
    }

    public void setParent(Comment parent) {
	this.parent = parent;
    }

    public CommentSession getSession() {
	return session;
    }

    public void setSession(CommentSession session) {
	this.session = session;
    }

    public Comment getRootComment() {
	return rootComment;
    }

    public void setRootComment(Comment rootComment) {
	this.rootComment = rootComment;
    }

    public Comment getThreadComment() {
	return threadComment;
    }

    public void setThreadComment(Comment threadComment) {
	this.threadComment = threadComment;
    }

    public short getCommentLevel() {
	return commentLevel;
    }

    public void setCommentLevel(short commentLevel) {
	this.commentLevel = commentLevel;
    }

    public Integer getLikeCount() {
	return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
	this.likeCount = likeCount;
    }

    public Integer getVote() {
	return vote;
    }

    public void setVote(Integer vote) {
	this.vote = vote;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", uid).append("body", body).toString();
    }

}
