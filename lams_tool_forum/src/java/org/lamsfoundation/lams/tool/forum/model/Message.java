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

package org.lamsfoundation.lams.tool.forum.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * @author conradb
 */
@Entity
@Table(name = "tl_lafrum11_message")
public class Message implements Cloneable {

    private static Logger log = Logger.getLogger(Message.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String subject;

    @Column
    private String body;

    @Column(name = "sequence_id")
    private int sequenceId;

    @Column(name = "is_authored")
    private boolean isAuthored;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Column(name = "is_monitor")
    private boolean isMonitor;

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @Column(name = "last_reply_date")
    private Date lastReplyDate;

    @Column(name = "reply_number")
    private int replyNumber;

    @Column(name = "hide_flag")
    private boolean hideFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_uid")
    private Message parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_session_uid")
    private ForumToolSession toolSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_uid")
    private Forum forum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private ForumUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private ForumUser modifiedBy;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Attachment> attachments;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id")
    private ForumReport report;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "authored_parent_uid")
    private Set<Message> sessionClones;

    public Message() {
	attachments = new TreeSet<Attachment>();
	sessionClones = new HashSet<Message>();
    }

    // **********************************************************
    // Function method for Message
    // **********************************************************
    public static Message newInstance(Message fromMsg) {
	Message toMsg = new Message();
	toMsg = (Message) fromMsg.clone();
	return toMsg;
    }

    /**
     * <em>This method DOES NOT deep clone <code>Forum</code> to avoid dead loop in clone.</em>
     */
    @Override
    public Object clone() {

	Message msg = null;
	try {
	    msg = (Message) super.clone();
	    msg.setUid(null);
	    // it is not necessary to deep clone following comment fields.
	    // don't deep clone forum to avoid dead loop in clone
	    // if(parent != null){
	    // msg.parent = (Message) parent.clone();
	    // //try to keep parent uid, so avoid persistant a new instance in database for parent message
	    // msg.parent.uid = parent.uid;
	    // }
	    // if(toolSession != null){
	    // msg.toolSession = (ForumToolSession) toolSession.clone();
	    // }
	    // if(createdBy != null){
	    // msg.createdBy = (ForumUser) createdBy.clone();
	    // }
	    // if(modifiedBy != null)
	    // msg.modifiedBy = (ForumUser) modifiedBy.clone();
	    // clone attachment
	    if (attachments != null) {
		Iterator iter = attachments.iterator();
		Set<Attachment> set = new TreeSet<Attachment>();
		while (iter.hasNext()) {
		    Attachment file = (Attachment) iter.next();
		    Attachment newFile = (Attachment) file.clone();
		    newFile.setMessage(msg); // attachment 'owns' the relationship
		    // use common file node in repository
		    set.add(newFile);
		}
		msg.attachments = set;
	    }
	    // do not clone the tool session data as cloning should be creating a "fresh" copy
	    msg.sessionClones = new HashSet<Message>();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + Forum.class + " failed");
	}

	return msg;
    }

    public Object updateClone(Message clone) {

	clone.setBody(this.getBody());
	clone.setForum(this.getForum());
	clone.setSequenceId(this.getSequenceId());
	clone.setHideFlag(this.isHideFlag());
	clone.setIsAnonymous(this.isAnonymous);
	clone.setIsMonitor(this.isMonitor);
	clone.setIsAuthored(this.getIsAuthored());
	clone.setLastReplyDate(this.getLastReplyDate());
	clone.setModifiedBy(clone.getModifiedBy());
	clone.setReplyNumber(this.getReplyNumber());
	clone.setReport(this.getReport());
	clone.setSubject(this.getSubject());
	clone.setUpdated(clone.getUpdated());

	// Update the attachments. Easiest way is to recopy them - which does NOT copy them in the content repository.
	clone.getAttachments().clear();
	if (attachments != null) {
	    Iterator iter = attachments.iterator();
	    while (iter.hasNext()) {
		Attachment file = (Attachment) iter.next();
		Attachment newFile = (Attachment) file.clone();
		clone.getAttachments().add(newFile);
	    }
	}

	return clone;
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {
	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Message)) {
	    return false;
	}

	Message genericEntity = (Message) o;

	// uses same attributes to determine equality as
	// ConditionTopicComparator.compare()
	return new EqualsBuilder()
		// .append(this.uid,genericEntity.getUid())
		.append(this.subject, genericEntity.getSubject()).append(this.body, genericEntity.getBody())
		.append(this.replyNumber, genericEntity.getReplyNumber())
		// .append(this.lastReplyDate,genericEntity.lastReplyDate)
		// .append(this.created,genericEntity.created)
		// .append(this.updated,genericEntity.updated)
		.append(this.createdBy, genericEntity.getCreatedBy())
		.append(this.modifiedBy, genericEntity.getModifiedBy()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(subject).append(body).append(created).append(updated)
		.append(createdBy).append(modifiedBy).toHashCode();
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     *
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     *
     * @param created
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns this topic last reply date
     *
     * @return date
     *
     */
    public Date getLastReplyDate() {
	return lastReplyDate;
    }

    public void setLastReplyDate(Date lastPostDate) {
	this.lastReplyDate = lastPostDate;
    }

    /**
     * Returns the object's date of last update
     *
     * @return date updated
     *
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * @return Returns the userid of the user who created the Forum.
     *
     *
     *
     */
    public ForumUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Forum.
     */
    public void setCreatedBy(ForumUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     *
     *
     * @return Returns the userid of the user who modified the posting.
     */
    public ForumUser getModifiedBy() {
	return modifiedBy;
    }

    /**
     * @param modifiedBy
     *            The userid of the user who modified the posting.
     */
    public void setModifiedBy(ForumUser modifiedBy) {
	this.modifiedBy = modifiedBy;
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    /**
     * @return Returns the subject of the Message.
     *
     *
     *
     */
    public String getSubject() {
	return subject;
    }

    /**
     * @param subject
     *            The subject of the Message to be set.
     */
    public void setSubject(String subject) {
	this.subject = subject;
    }

    /**
     * @return Returns the body of the Message.
     *
     *
     *
     */
    public String getBody() {
	return body;
    }

    /**
     * @param body
     *            The body of the Message to set.
     */
    public void setBody(String body) {
	this.body = body;
    }

    /**
     * Returns Message sequence number.
     *
     * @return Message sequence number
     *
     *
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets Message sequence number.
     *
     * @param sequenceId
     *            Message sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * @return Returns true if the Message was an Authored Message.
     *
     *
     *
     */
    public boolean getIsAuthored() {
	return isAuthored;
    }

    /**
     * @param isAuthored
     *            Set isAuthored to true if Message was authored otherwise set to false.
     */
    public void setIsAuthored(boolean isAuthored) {
	this.isAuthored = isAuthored;
    }

    /**
     * @return Returns whether the Message should be shown as an Annonymous message.
     *
     *
     *
     */
    public boolean getIsAnonymous() {
	return isAnonymous;
    }

    /**
     * @param isAnonymous
     *            Indicates that the Message is to be shown as an Annonymous message when set to true.
     */
    public void setIsAnonymous(boolean isAnonymous) {
	this.isAnonymous = isAnonymous;
    }

    /**
     * @return Returns whether the Message should be shown as a teacher/tutor message created in monitoring
     *
     *
     *
     */
    public boolean getIsMonitor() {
	return isMonitor;
    }

    /**
     * @param isAnonymous
     *            Indicates whether the Message should be shown as a teacher/tutor message created in monitoring
     */
    public void setIsMonitor(boolean isMonitor) {
	this.isMonitor = isMonitor;
    }

    /**
     * Gets the toolSession
     *
     *
     *
     *
     */
    public ForumToolSession getToolSession() {
	return toolSession;
    }

    /**
     * @param toolSession
     *            The toolSession that this Message belongs to
     */
    public void setToolSession(ForumToolSession session) {
	this.toolSession = session;
    }

    /**
     * @param parent
     *            The parent of this Message
     */
    public void setParent(Message parent) {
	this.parent = parent;
    }

    /**
     *
     * @return
     */
    public Message getParent() {
	return parent;
    }

    /**
     * @return a set of Attachments to this Message.
     *
     *
     *
     *
     *
     */
    public Set<Attachment> getAttachments() {
	return attachments;
    }

    /*
     * @param attachments The attachments to set.
     */
    public void setAttachments(Set<Attachment> attachments) {
	this.attachments = attachments;
    }

    /**
     *
     * @return
     */
    public Forum getForum() {
	return forum;
    }

    public void setForum(Forum forum) {
	this.forum = forum;
    }

    /**
     *
     * @return
     */
    public int getReplyNumber() {
	return replyNumber;
    }

    public void setReplyNumber(int replyNumber) {
	this.replyNumber = replyNumber;
    }

    /**
     *
     * @return
     */
    public boolean isHideFlag() {
	return hideFlag;
    }

    public void setHideFlag(boolean hideFlag) {
	this.hideFlag = hideFlag;
    }

    /**
     *
     * @return
     */
    public ForumReport getReport() {
	return report;
    }

    public void setReport(ForumReport report) {
	this.report = report;
    }

    /**
     * @return the set of all messages cloned from this message.
     */
    public Set<Message> getSessionClones() {
	return sessionClones;
    }

    /*
     * @param sessionClones The sessionClones to set.
     */
    public void setSessionClones(Set<Message> sessionClones) {
	this.sessionClones = sessionClones;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", uid).append("subject", subject).append("body", body).toString();
    }

}
