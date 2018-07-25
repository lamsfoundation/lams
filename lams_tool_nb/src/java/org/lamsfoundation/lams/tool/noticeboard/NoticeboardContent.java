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

    /** nullable persistent field */
    private boolean allowAnonymous;
    
    /** persistent field */
    private Set<NoticeboardSession> nbSessions = new HashSet<NoticeboardSession>();

    /** default constructor */
    public NoticeboardContent() {
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

    /**
     *
     */

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    /**
     *
     *
     */
    public Long getCreatorUserId() {
	return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
	this.creatorUserId = creatorUserId;
    }

    /**
     *
     *
     */
    public Date getDateCreated() {
	return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
	this.dateCreated = dateCreated;
    }

    /**
     *
     *
     */
    public Date getDateUpdated() {
	return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
	this.dateUpdated = dateUpdated;
    }

    /**
     *
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     */
    public boolean getReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
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
     *
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
     *
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

    public boolean isAllowAnonymous() {
	return allowAnonymous;
    }

    public void setAllowAnonymous(boolean allowAnonymous) {
	this.allowAnonymous = allowAnonymous;
    }

    /**
     *
     */

    public Long getNbContentId() {
	return nbContentId;
    }

    public void setNbContentId(Long nbContentId) {
	this.nbContentId = nbContentId;
    }

    /**
     *
     *
     *
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
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     */
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