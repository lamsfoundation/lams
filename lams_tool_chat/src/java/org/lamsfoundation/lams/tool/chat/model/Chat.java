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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.chat.service.ChatService;

/**
 *
 */

public class Chat implements java.io.Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(ChatService.class.getName());

    // Fields
    /**
     *
     */
    private Long uid;

    private Date createDate;

    private Date updateDate;

    private Long createBy;

    private String title;

    private String instructions;

    private boolean lockOnFinished;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean filteringEnabled;

    private String filterKeywords;

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;

    private Date submissionDeadline;

    private Set chatSessions;
    private Set<ChatCondition> conditions = new TreeSet<ChatCondition>(new TextSearchConditionComparator());

    // Constructors

    /** default constructor */
    public Chat() {
    }

    /** full constructor */
    public Chat(Date createDate, Date updateDate, Long createBy, String title, String instructions,
	    boolean lockOnFinished, boolean filteringEnabled, String filterKeywords, boolean contentInUse,
	    boolean defineLater, Long toolContentId, Set chatSessions) {
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

    // Property accessors
    /**
     *
     *
     */

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     *
     */

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     *
     *
     */

    public Long getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     *
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
     *
     */

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     *
     */

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     *
     */
    public boolean isReflectOnActivity() {
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
     *
     */

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
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
     *
     */

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     *
     *
     *
     */

    public Set getChatSessions() {
	return chatSessions;
    }

    public void setChatSessions(Set chatSessions) {
	this.chatSessions = chatSessions;
    }

    /**
     *
     */
    public boolean isFilteringEnabled() {
	return filteringEnabled;
    }

    public void setFilteringEnabled(boolean filteringEnabled) {
	this.filteringEnabled = filteringEnabled;
    }

    /**
     *
     */
    public String getFilterKeywords() {
	return filterKeywords;
    }

    public void setFilterKeywords(String filterKeywords) {
	this.filterKeywords = filterKeywords;
    }

    /**
     *
     * @return
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     * toString
     *
     * @return String
     */
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
	if (other == null) {
	    return false;
	}
	if (!(other instanceof Chat)) {
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
	    chat.chatSessions = new HashSet();

	    if (conditions != null) {
		Set<ChatCondition> set = new TreeSet<ChatCondition>(new TextSearchConditionComparator());
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

    /**
     *
     * sort="org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator"
     *
     *
     *
     */
    public Set<ChatCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<ChatCondition> conditions) {
	this.conditions = conditions;
    }
}
