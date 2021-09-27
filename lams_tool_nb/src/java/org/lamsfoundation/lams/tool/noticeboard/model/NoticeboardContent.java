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

package org.lamsfoundation.lams.tool.noticeboard.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;

/**
 * <p>
 * Persistent noticeboard object/bean that defines the content for the noticeboard tool. Provides accessors and mutators
 * to get/set noticeboard attributes
 * </p>
 *
 *
 * @author mtruong
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tl_lanb11_content")
public class NoticeboardContent implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "nb_content_id")
    private Long nbContentId;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "reflect_on_activity")
    private Boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "creator_user_id")
    private Long creatorUserId;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "allow_comments")
    private boolean allowComments;

    @Column(name = "comments_like_dislike")
    private boolean commentsLikeAndDislike;

    @Column(name = "allow_anonymous")
    private boolean allowAnonymous;

    @OneToMany(mappedBy = "nbContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<NoticeboardSession> nbSessions;

    /** default constructor */
    public NoticeboardContent() {
	this.nbSessions = new HashSet<>();
    }

    /** full constructor */
    public NoticeboardContent(Long nbContentId, String title, String content, boolean defineLater,
	    boolean reflectOnActivity, String reflectInstructions, boolean contentInUse, Long creatorUserId,
	    Date dateCreated, Date dateUpdated, boolean allowComments, boolean commentsLikeAndDislike,
	    boolean allowAnonymous) {
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
	this.allowAnonymous = allowAnonymous;
	this.nbSessions = new HashSet<>();
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
	this.allowAnonymous = false;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public Long getCreatorUserId() {
	return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
	this.creatorUserId = creatorUserId;
    }

    public Date getDateCreated() {
	return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
	this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
	return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
	this.dateUpdated = dateUpdated;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public boolean getReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }

    public boolean isCommentsLikeAndDislike() {
	return commentsLikeAndDislike;
    }

    public void setCommentsLikeAndDislike(boolean commentsLikeAndDislike) {
	this.commentsLikeAndDislike = commentsLikeAndDislike;
    }

    public boolean isAllowAnonymous() {
	return allowAnonymous;
    }

    public void setAllowAnonymous(boolean allowAnonymous) {
	this.allowAnonymous = allowAnonymous;
    }

    public Long getNbContentId() {
	return nbContentId;
    }

    public void setNbContentId(Long nbContentId) {
	this.nbContentId = nbContentId;
    }

    public Set<NoticeboardSession> getNbSessions() {
	return nbSessions;
    }

    public void setNbSessions(Set<NoticeboardSession> nbSessions) {
	this.nbSessions = nbSessions;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
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
		nb.isCommentsLikeAndDislike(), nb.isAllowAnonymous());

	return newContent;
    }

}