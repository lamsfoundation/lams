/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.chat.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.chat.service.ChatService;

@Entity
@Table(name = "tl_lachat11_chat")
public class Chat implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(ChatService.class.getName());

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "create_by")
    private Long createBy;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "filtering_enabled")
    private boolean filteringEnabled;

    @Column(name = "filter_keywords")
    private String filterKeywords;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ChatSession> chatSessions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_uid")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ChatCondition> conditions = new TreeSet<>(new TextSearchConditionComparator());

    public Chat() {
    }

    public Chat(Date createDate, Date updateDate, Long createBy, String title, String instructions,
	    boolean lockOnFinished, boolean filteringEnabled, String filterKeywords, boolean contentInUse,
	    boolean defineLater, Long toolContentId, Set<ChatSession> chatSessions) {
	this.createDate = createDate;
	this.updateDate = updateDate;
	this.createBy = createBy;
	this.title = title;
	this.instructions = instructions;
	this.lockOnFinished = lockOnFinished;
	this.filteringEnabled = filteringEnabled;
	this.filterKeywords = filterKeywords;
	this.contentInUse = contentInUse;
	this.defineLater = defineLater;
	this.toolContentId = toolContentId;
	this.chatSessions = chatSessions;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public Long getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public boolean isReflectOnActivity() {
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

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public Set<ChatSession> getChatSessions() {
	return chatSessions;
    }

    public void setChatSessions(Set<ChatSession> chatSessions) {
	this.chatSessions = chatSessions;
    }

    public boolean isFilteringEnabled() {
	return filteringEnabled;
    }

    public void setFilteringEnabled(boolean filteringEnabled) {
	this.filteringEnabled = filteringEnabled;
    }

    public String getFilterKeywords() {
	return filterKeywords;
    }

    public void setFilterKeywords(String filterKeywords) {
	this.filterKeywords = filterKeywords;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if ((other == null) || !(other instanceof Chat)) {
	    return false;
	}
	Chat castOther = (Chat) other;

	return this.getUid() == castOther.getUid()
		|| this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Chat newInstance(Chat fromContent, Long toContentId) {
	Chat toContent = new Chat();
	toContent = (Chat) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Chat chat = null;
	try {
	    chat = (Chat) super.clone();
	    chat.setUid(null);

	    // create an empty set for the chatSession
	    chat.chatSessions = new HashSet<>();

	    if (conditions != null) {
		Set<ChatCondition> set = new TreeSet<>(new TextSearchConditionComparator());
		for (ChatCondition condition : conditions) {
		    set.add((ChatCondition) condition.clone());
		}
		chat.setConditions(set);
	    }

	} catch (CloneNotSupportedException cnse) {
	    Chat.log.error("Error cloning " + Chat.class);
	}
	return chat;
    }

    public Set<ChatCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<ChatCondition> conditions) {
	this.conditions = conditions;
    }
}