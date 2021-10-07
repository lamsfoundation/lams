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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;

/**
 * Forum
 *
 * @author conradb
 */
@Entity
@Table(name = "tl_lafrum11_forum")
public class Forum implements Cloneable {

    private static final Logger log = Logger.getLogger(Forum.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    // tool contentID
    @Column(name = "content_id")
    private Long contentId;

    @Column
    private String title;

    @Column(name = "lock_on_finished")
    private boolean lockWhenFinished;

    @Column(name = "allow_anonym")
    private boolean allowAnonym;

    @Column(name = "allow_edit")
    private boolean allowEdit;

    @Column(name = "allow_new_topic")
    private boolean allowNewTopic;

    @Column(name = "allow_upload")
    private boolean allowUpload;

    @Column(name = "allow_rate_messages")
    private boolean allowRateMessages;

    @Column(name = "maximum_reply")
    private int maximumReply;

    @Column(name = "minimum_reply")
    private int minimumReply;

    @Column(name = "maximum_rate")
    private int maximumRate;

    @Column(name = "minimum_rate")
    private int minimumRate;

    @Column(name = "allow_rich_editor")
    private boolean allowRichEditor;

    @Column
    private String instructions;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    /*
     * TODO: I don't think this is going to work as the create_by column is in this table and hibernate will expect it
     * in the user table.
     * Probably need to revert to a hacked many - to - one
     *
     * @ManyToOne(fetch = FetchType.LAZY)
     *
     * @JoinColumn(name = "create_by")
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_by")
    private ForumUser createdBy;

    // TODO why doesn't this have cascades. Why do we remove the messages manually?
    @OneToMany(mappedBy = "forum")
    @OrderBy("created DESC")
    private Set<Message> messages;

    @Column(name = "min_characters")
    private int minCharacters;

    @Column(name = "limited_input_flag")
    private boolean limitedMinCharacters;

    @Column(name = "limited_of_chars")
    private int maxCharacters;

    @Column(name = "limited_min_characters")
    private boolean limitedMaxCharacters;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "mark_release_notify")
    private boolean notifyLearnersOnMarkRelease;

    @Column(name = "notify_learners_on_forum_posting")
    private boolean notifyLearnersOnForumPosting;

    @Column(name = "notify_teachers_on_forum_posting")
    private boolean notifyTeachersOnForumPosting;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_uid")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ForumCondition> conditions;

    /**
     * Default construction method.
     *
     */
    public Forum() {
	messages = new HashSet();
	conditions = new TreeSet<>(new TextSearchConditionComparator());
    }

    // **********************************************************
    // Function method for Forum
    // **********************************************************
    @Override
    public Object clone() {

	Forum forum = null;
	try {
	    forum = (Forum) super.clone();
	    forum.setUid(null);
	    // clone message
	    if (messages != null) {
		Iterator iter = messages.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    set.add(Message.newInstance((Message) iter.next()));
		}
		forum.messages = set;
	    }
	    if (getConditions() != null) {
		Set<ForumCondition> conditionsCopy = new TreeSet<>(new TextSearchConditionComparator());
		for (ForumCondition condition : getConditions()) {
		    conditionsCopy.add(condition.clone(forum));
		}
		forum.setConditions(conditionsCopy);
	    }

	} catch (CloneNotSupportedException e) {
	    Forum.log.error("When clone " + Forum.class + " failed");
	}

	return forum;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Forum)) {
	    return false;
	}

	final Forum genericEntity = (Forum) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
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
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the title.
     *
     *
     *
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the allowAnonym.
     *
     *
     *
     */
    public boolean getAllowAnonym() {
	return allowAnonym;
    }

    /**
     * @param allowAnonym
     *            The allowAnonym to set.
     *
     */
    public void setAllowAnonym(boolean allowAnnomity) {
	allowAnonym = allowAnnomity;
    }

    /**
     * @return Returns the lockWhenFinish.
     *
     *
     *
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the forum for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     *
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * NOTE: The reason that relation don't use save-update to persist message is MessageSeq table need save a record as
     * well.
     *
     *
     *
     *
     *
     * @return
     */
    public Set<Message> getMessages() {
	return messages;
    }

    public void setMessages(Set messages) {
	this.messages = messages;
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

    /**
     *
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     *
     * @return
     */
    public boolean isAllowEdit() {
	return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
	this.allowEdit = allowEdit;
    }

    /**
     *
     * @return
     */
    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public static Forum newInstance(Forum fromContent, Long contentId) {
	Forum toContent = new Forum();
	toContent = (Forum) fromContent.clone();
	toContent.setContentId(contentId);

	Set topics = toContent.getMessages();
	if (topics != null) {
	    Iterator iter = topics.iterator();
	    while (iter.hasNext()) {
		Message msg = (Message) iter.next();
		// clear message forum so that they can be saved when persistent happens
		msg.setForum(null);
	    }
	}

	return toContent;
    }

    /**
     *
     * @return
     */
    public int getMinCharacters() {
	return minCharacters;
    }

    public void setMinCharacters(int minCharacters) {
	this.minCharacters = minCharacters;
    }

    /**
     *
     * @return
     */
    public boolean isLimitedMinCharacters() {
	return limitedMinCharacters;
    }

    public void setLimitedMinCharacters(boolean limitedMinCharacters) {
	this.limitedMinCharacters = limitedMinCharacters;
    }

    /**
     *
     * @return
     */
    public int getMaxCharacters() {
	return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
	this.maxCharacters = maxCharacters;
    }

    /**
     *
     * @return
     */
    public boolean isLimitedMaxCharacters() {
	return limitedMaxCharacters;
    }

    public void setLimitedMaxCharacters(boolean limitedMaxCharacters) {
	this.limitedMaxCharacters = limitedMaxCharacters;
    }

    /**
     *
     * @return
     */
    public boolean isAllowNewTopic() {
	return allowNewTopic;
    }

    public void setAllowNewTopic(boolean allowNewTopic) {
	this.allowNewTopic = allowNewTopic;
    }

    /**
     *
     * @return
     */
    public boolean isAllowUpload() {
	return allowUpload;
    }

    public void setAllowUpload(boolean allowUpload) {
	this.allowUpload = allowUpload;
    }

    /**
     *
     * @return
     */
    public boolean isAllowRateMessages() {
	return allowRateMessages;
    }

    public void setAllowRateMessages(boolean allowRateMessages) {
	this.allowRateMessages = allowRateMessages;
    }

    /**
     *
     * @return
     */
    public int getMaximumReply() {
	return maximumReply;
    }

    public void setMaximumReply(int maximumReply) {
	this.maximumReply = maximumReply;
    }

    /**
     *
     * @return
     */
    public int getMinimumReply() {
	return minimumReply;
    }

    public void setMinimumReply(int minimumReply) {
	this.minimumReply = minimumReply;
    }

    /**
     *
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     * @return
     */
    public boolean isNotifyLearnersOnForumPosting() {
	return notifyLearnersOnForumPosting;
    }

    public void setNotifyLearnersOnForumPosting(boolean notifyLearnersOnForumPosting) {
	this.notifyLearnersOnForumPosting = notifyLearnersOnForumPosting;
    }

    /**
     *
     * @return
     */
    public boolean isNotifyTeachersOnForumPosting() {
	return notifyTeachersOnForumPosting;
    }

    public void setNotifyTeachersOnForumPosting(boolean notifyTeachersOnForumPosting) {
	this.notifyTeachersOnForumPosting = notifyTeachersOnForumPosting;
    }

    /**
     *
     * @return
     */
    public boolean isNotifyLearnersOnMarkRelease() {
	return notifyLearnersOnMarkRelease;
    }

    public void setNotifyLearnersOnMarkRelease(boolean notifyLearnersOnMarkRelease) {
	this.notifyLearnersOnMarkRelease = notifyLearnersOnMarkRelease;
    }

    /**
     *
     * sort="org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator"
     *
     *
     *
     */
    public Set<ForumCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<ForumCondition> conditions) {
	this.conditions = conditions;
    }

    /**
     *
     * @return date submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     *
     * @return
     */
    public int getMaximumRate() {
	return maximumRate;
    }

    public void setMaximumRate(int maximumRate) {
	this.maximumRate = maximumRate;
    }

    /**
     *
     * @return
     */
    public int getMinimumRate() {
	return minimumRate;
    }

    public void setMinimumRate(int minimumRate) {
	this.minimumRate = minimumRate;
    }
}
