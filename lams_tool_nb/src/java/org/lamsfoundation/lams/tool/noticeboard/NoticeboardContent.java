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


package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;

/**
 * <p>
 * Persistent noticeboard object/bean that defines the content for the noticeboard tool. Provides accessors and mutators
 * to get/set noticeboard attributes
 * </p>
 *
 * @hibernate.class table="tl_lanb11_content"
 * @author mtruong
 */
public class NoticeboardContent implements Serializable {

    /** identifier field */
    private Long uid;

    /** non-nullable persistent field */
    private Long nbContentId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String content;

    /** nullable persistent field */
    private boolean defineLater;

    private Boolean reflectOnActivity;

    private String reflectInstructions;

    /** nullable persistent field */
    private boolean contentInUse;

    /** nullable persistent field */
    private Long creatorUserId;

    /** nullable persistent field */
    private Date dateCreated;

    /** nullable persistent field */
    private Date dateUpdated;

    /** nullable persistent field */
    private boolean allowComments;

    /** nullable persistent field */
    private boolean commentsLikeAndDislike;

    /** persistent field */
    private Set<NoticeboardSession> nbSessions = new HashSet<NoticeboardSession>();

    /** default constructor */
    public NoticeboardContent() {
    }

    /** full constructor */
    public NoticeboardContent(Long nbContentId, String title, String content, boolean defineLater,
	    boolean reflectOnActivity, String reflectInstructions, boolean contentInUse, Long creatorUserId,
	    Date dateCreated, Date dateUpdated, boolean allowComments, boolean commentsLikeAndDislike) {
	this.nbContentId = nbContentId;
	this.title = title;
	this.content = content;
	this.defineLater = defineLater;
	this.reflectOnActivity = reflectOnActivity;
	this.reflectInstructions = reflectInstructions;
	this.contentInUse = contentInUse;
	this.creatorUserId = creatorUserId;
	this.dateCreated = dateCreated;
	this.dateUpdated = dateUpdated;
	this.allowComments = allowComments;
	this.commentsLikeAndDislike = commentsLikeAndDislike;
    }

    /**
     * Minimal Constructor used to initialise values for the NoticeboardContent object
     *
     * @return
     */

    public NoticeboardContent(Long nbContentId, String title, String content, Date dateCreated) {
	this.nbContentId = nbContentId;
	this.title = title;
	this.content = content;
	this.defineLater = false;
	this.reflectOnActivity = false;
	this.contentInUse = false;
	this.creatorUserId = null;
	this.dateCreated = dateCreated;
	this.dateUpdated = null;
	this.allowComments = false;
	this.commentsLikeAndDislike = false;
    }

    /**
     * @hibernate.property column="content" length="65535"
     */

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    /**
     *
     * @hibernate.property column="creator_user_id" length="20"
     */
    public Long getCreatorUserId() {
	return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
	this.creatorUserId = creatorUserId;
    }

    /**
     *
     * @hibernate.property column="date_created" length="19"
     */
    public Date getDateCreated() {
	return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
	this.dateCreated = dateCreated;
    }

    /**
     *
     * @hibernate.property column="date_updated" length="19"
     */
    public Date getDateUpdated() {
	return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
	this.dateUpdated = dateUpdated;
    }

    /**
     * @hibernate.property column="define_later" length="1"
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="reflect_on_activity" length="1"
     */
    public boolean getReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     * @hibernate.property column="reflect_instructions" length="65535"
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="allow_comments" length="1"
     */

    public boolean isAllowComments() {
	return allowComments;
    }

    /**
     * @param allowComments
     *            The allowComments to set.
     */
    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }

    /**
     * @hibernate.property column="comments_like_dislike" length="1"
     */

    public boolean isCommentsLikeAndDislike() {
	return commentsLikeAndDislike;
    }

    /**
     * @param commentsLikeAndDislike
     *            The commentsLikeAndDislike to set.
     */
    public void setCommentsLikeAndDislike(boolean commentsLikeAndDislike) {
	this.commentsLikeAndDislike = commentsLikeAndDislike;
    }

    /**
     * @hibernate.property column="nb_content_id" length="20" not-null="true"
     */

    public Long getNbContentId() {
	return nbContentId;
    }

    public void setNbContentId(Long nbContentId) {
	this.nbContentId = nbContentId;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
     * @hibernate.collection-key column="nb_content_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession"
     */
    public Set<NoticeboardSession> getNbSessions() {
	if (this.nbSessions == null) {
	    setNbSessions(new HashSet<NoticeboardSession>());
	}
	return nbSessions;
    }

    public void setNbSessions(Set<NoticeboardSession> nbSessions) {
	this.nbSessions = nbSessions;
    }

    /**
     * @hibernate.property column="title" length="65535"
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid" unsaved-value="0"
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="content_in_use" length="1"
     */

    public boolean isContentInUse() {
	return contentInUse;
    }

    /**
     * @param contentInUse
     *            The contentInUse to set.
     */
    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * Creates a new NoticeboardContent object from the supplied object. Assigns it the toContendId.
     *
     * @param nb
     *            NoticeboardContent object containing the content to copy from
     * @param toContentId
     *            The new Id of the new noticeboard object
     * @return newContent The new noticeboard content object
     * @throws RepositoryCheckedException
     * @throws ItemNotFoundException
     */
    public static NoticeboardContent newInstance(NoticeboardContent nb, Long toContentId)
	    throws ItemNotFoundException, RepositoryCheckedException {
	NoticeboardContent newContent = new NoticeboardContent(toContentId, nb.getTitle(), nb.getContent(),
		nb.isDefineLater(), nb.getReflectOnActivity(), nb.getReflectInstructions(), nb.isContentInUse(),
		nb.getCreatorUserId(), nb.getDateCreated(), nb.getDateUpdated(), nb.isAllowComments(),
		nb.isCommentsLikeAndDislike());

	return newContent;
    }

}