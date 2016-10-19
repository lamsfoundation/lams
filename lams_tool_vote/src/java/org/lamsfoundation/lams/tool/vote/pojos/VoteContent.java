/***************************************************************************
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
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.vote.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the content for the Voting tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lavote11_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1986729606785509746L;

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long voteContentId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String reflectionSubject;

    private String instructions;

    /** nullable persistent field */
    private boolean defineLater;

    /** nullable persistent field */
    private Date creationDate;

    /** nullable persistent field */
    private Date updateDate;

    /** nullable persistent field */
    private long createdBy;

    private boolean useSelectLeaderToolOuput;

    private boolean reflect;

    private boolean allowText;

    private String maxNominationCount;

    private String minNominationCount;

    /** nullable persistent field */
    private boolean lockOnFinish;

    private boolean showResults;

    /** persistent field */
    private Set<VoteQueContent> voteQueContents;

    /** persistent field */
    private Set<VoteSession> voteSessions;

    private Date submissionDeadline;

    /**
     * persistent field This field can be calculated, but introducing it reduces number of DB calls.
     */
    private Boolean assignedDataFlowObject;

    /** persistent field */
    private Short maxExternalInputs;

    /** persistent field */
    private Short externalInputsAdded;

    /** full constructor */
    public VoteContent(Long voteContentId, String title, String instructions, boolean defineLater, Date creationDate,
	    Date updateDate, boolean allowText, boolean useSelectLeaderToolOuput, boolean reflect,
	    String reflectionSubject, String maxNominationCount, String minNominationCount, long createdBy,
	    boolean lockOnFinish, boolean showResults, Short maxExternalInputs, Short externalInputsAdded,
	    Set<VoteQueContent> voteQueContents, Set<VoteSession> voteSessions) {
	this.voteContentId = voteContentId;
	this.title = title;
	this.instructions = instructions;
	this.defineLater = defineLater;
	this.creationDate = creationDate;
	this.updateDate = updateDate;
	this.maxNominationCount = maxNominationCount;
	this.minNominationCount = minNominationCount;
	this.allowText = allowText;
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
	this.reflect = reflect;
	this.reflectionSubject = reflectionSubject;
	this.createdBy = createdBy;
	this.lockOnFinish = lockOnFinish;
	this.showResults = showResults;
	this.voteQueContents = voteQueContents;
	this.voteSessions = voteSessions;
	this.maxExternalInputs = maxExternalInputs;
	this.externalInputsAdded = externalInputsAdded;
    }

    /** default constructor */
    public VoteContent() {
    }

    /** minimal constructor */
    public VoteContent(Long voteContentId, Set<VoteQueContent> voteQueContents, Set<VoteSession> voteSessions) {
	this.voteContentId = voteContentId;
	this.voteQueContents = voteQueContents;
	this.voteSessions = voteSessions;
    }

    /**
     * gets called as part of the copyToolContent
     *
     * Copy Construtor to create a new mc content instance. Note that we don't copy the mc session data here because the
     * mc session will be created after we copied tool content.
     *
     * @param mc
     *            the original mc content.
     * @param newContentId
     *            the new mc content id.
     * @return the new mc content object.
     */
    public static VoteContent newInstance(VoteContent vote, Long newContentId) {
	VoteContent newContent = new VoteContent(newContentId, vote.getTitle(), vote.getInstructions(),
		vote.isDefineLater(), vote.getCreationDate(), vote.getUpdateDate(), vote.isAllowText(),
		vote.isUseSelectLeaderToolOuput(), vote.isReflect(), vote.getReflectionSubject(),
		vote.getMaxNominationCount(), vote.getMinNominationCount(), vote.getCreatedBy(), vote.isLockOnFinish(),
		vote.isShowResults(), vote.getMaxExternalInputs(), vote.getExternalInputsAdded(),
		new TreeSet<VoteQueContent>(), new TreeSet<VoteSession>());
	newContent.setVoteQueContents(vote.deepCopyMcQueContent(newContent));
	newContent.setAssignedDataFlowObject(vote.getAssignedDataFlowObject());

	return newContent;
    }

    /**
     * gets called as part of the copyToolContent
     *
     * @param newQaContent
     * @return Set
     */
    public Set<VoteQueContent> deepCopyMcQueContent(VoteContent newMcContent) {

	Set<VoteQueContent> newMcQueContent = new TreeSet<>();
	for (Iterator<VoteQueContent> i = this.getVoteQueContents().iterator(); i.hasNext();) {
	    VoteQueContent queContent = i.next();
	    if (queContent.getMcContent() != null) {
		int displayOrder = queContent.getDisplayOrder();
		VoteQueContent mcQueContent = VoteQueContent.newInstance(queContent, displayOrder, newMcContent);
		newMcQueContent.add(mcQueContent);
	    }
	}
	return newMcQueContent;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
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

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public long getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(long createdBy) {
	this.createdBy = createdBy;
    }

    /**
     * @return Returns the voteQueContents.
     */
    public Set<VoteQueContent> getVoteQueContents() {
	if (voteQueContents == null) {
	    setVoteQueContents(new HashSet<VoteQueContent>());
	}
	return voteQueContents;

    }

    /**
     * @param voteQueContents
     *            The voteQueContents to set.
     */
    public void setVoteQueContents(Set<VoteQueContent> voteQueContents) {
	this.voteQueContents = voteQueContents;
    }

    /**
     * @return Returns the voteSessions.
     */
    public Set<VoteSession> getVoteSessions() {
	if (voteSessions == null) {
	    setVoteSessions(new HashSet<VoteSession>());
	}
	return voteSessions;
    }

    /**
     * @param voteSessions
     *            The voteSessions to set.
     */
    public void setVoteSessions(Set<VoteSession> voteSessions) {
	this.voteSessions = voteSessions;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    /**
     * @return Returns the lockOnFinish.
     */
    public boolean isLockOnFinish() {
	return lockOnFinish;
    }

    /**
     * @param lockOnFinish
     *            The lockOnFinish to set.
     */
    public void setLockOnFinish(boolean lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    /**
     * @return Returns the voteContentId.
     */
    public Long getVoteContentId() {
	return voteContentId;
    }

    /**
     * @param voteContentId
     *            The voteContentId to set.
     */
    public void setVoteContentId(Long voteContentId) {
	this.voteContentId = voteContentId;
    }

    /**
     * @return Returns the allowText.
     */
    public boolean isAllowText() {
	return allowText;
    }

    /**
     * @param allowText
     *            The allowText to set.
     */
    public void setAllowText(boolean allowText) {
	this.allowText = allowText;
    }

    /**
     * @return Returns the maxNominationCount.
     */
    public String getMaxNominationCount() {
	return maxNominationCount;
    }

    /**
     * @param maxNominationCount
     *            The maxNominationCount to set.
     */
    public void setMaxNominationCount(String maxNominationCount) {
	this.maxNominationCount = maxNominationCount;
    }

    /**
     * @return Returns the minNominationCount.
     */
    public String getMinNominationCount() {
	return minNominationCount;
    }

    /**
     * @param minNominationCount
     *            The minNominationCount to set.
     */
    public void setMinNominationCount(String minNominationCount) {
	this.minNominationCount = minNominationCount;
    }

    /**
     * @return Returns the reflect.
     */
    public boolean isReflect() {
	return reflect;
    }

    /**
     * @param reflect
     *            The reflect to set.
     */
    public void setReflect(boolean reflect) {
	this.reflect = reflect;
    }

    /**
     * @param useSelectLeaderToolOuput
     *            The useSelectLeaderToolOuput to set.
     */
    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the useSelectLeaderToolOuput.
     */
    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the reflectionSubject.
     */
    public String getReflectionSubject() {
	return reflectionSubject;
    }

    /**
     * @param reflectionSubject
     *            The reflectionSubject to set.
     */
    public void setReflectionSubject(String reflectionSubject) {
	this.reflectionSubject = reflectionSubject;
    }

    public boolean isShowResults() {
	return showResults;
    }

    public void setShowResults(boolean showResults) {
	this.showResults = showResults;
    }

    public Boolean getAssignedDataFlowObject() {
	return assignedDataFlowObject;
    }

    public void setAssignedDataFlowObject(Boolean assignedDataFlowObject) {
	this.assignedDataFlowObject = assignedDataFlowObject;
    }

    public Short getMaxExternalInputs() {
	return maxExternalInputs;
    }

    public void setMaxExternalInputs(Short maxInputs) {
	maxExternalInputs = maxInputs;
    }

    public Short getExternalInputsAdded() {
	return externalInputsAdded;
    }

    public void setExternalInputsAdded(Short externalInputsAdded) {
	this.externalInputsAdded = externalInputsAdded;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }
}