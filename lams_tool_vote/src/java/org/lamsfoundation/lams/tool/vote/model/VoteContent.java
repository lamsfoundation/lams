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
package org.lamsfoundation.lams.tool.vote.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * <p>
 * Persistent object/bean that defines the content for the Voting tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lavote11_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_lavote11_content")
public class VoteContent implements Serializable {

    private static final long serialVersionUID = 1986729606785509746L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long voteContentId;

    @Column
    private String title;

    @Column
    private String reflectionSubject;

    @Column
    private String instructions;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "created_by")
    private long createdBy;

    @Column(name = "use_select_leader_tool_ouput")
    private boolean useSelectLeaderToolOuput;

    @Column
    private boolean reflect;

    @Column
    private boolean allowText;

    @Column
    private String maxNominationCount;

    @Column
    private String minNominationCount;

    @Column(name = "lock_on_finish")
    private boolean lockOnFinish;

    @Column(name = "show_results")
    private boolean showResults;

    @OneToMany(mappedBy = "voteContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<VoteQueContent> voteQueContents;

    @OneToMany(mappedBy = "voteContent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<VoteSession> voteSessions;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    /**
     * This field can be calculated, but introducing it reduces number of DB calls.
     */
    @Column(name = "assigned_data_flow_object")
    private Boolean assignedDataFlowObject;

    @Column(name = "max_external_inputs")
    private Short maxExternalInputs;

    @Column(name = "external_inputs_added")
    private Short externalInputsAdded;

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

    public VoteContent() {
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

    public Set<VoteQueContent> getVoteQueContents() {
	if (voteQueContents == null) {
	    setVoteQueContents(new HashSet<VoteQueContent>());
	}
	return voteQueContents;

    }

    public void setVoteQueContents(Set<VoteQueContent> voteQueContents) {
	this.voteQueContents = voteQueContents;
    }

    public Set<VoteSession> getVoteSessions() {
	if (voteSessions == null) {
	    setVoteSessions(new HashSet<VoteSession>());
	}
	return voteSessions;
    }

    public void setVoteSessions(Set<VoteSession> voteSessions) {
	this.voteSessions = voteSessions;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    public Date getCreationDate() {
	return creationDate;
    }

    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    public boolean isLockOnFinish() {
	return lockOnFinish;
    }

    public void setLockOnFinish(boolean lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    public Long getVoteContentId() {
	return voteContentId;
    }

    public void setVoteContentId(Long voteContentId) {
	this.voteContentId = voteContentId;
    }

    public boolean isAllowText() {
	return allowText;
    }

    public void setAllowText(boolean allowText) {
	this.allowText = allowText;
    }

    public String getMaxNominationCount() {
	return maxNominationCount;
    }

    public void setMaxNominationCount(String maxNominationCount) {
	this.maxNominationCount = maxNominationCount;
    }

    public String getMinNominationCount() {
	return minNominationCount;
    }

    public void setMinNominationCount(String minNominationCount) {
	this.minNominationCount = minNominationCount;
    }

    public boolean isReflect() {
	return reflect;
    }

    public void setReflect(boolean reflect) {
	this.reflect = reflect;
    }

    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    public String getReflectionSubject() {
	return reflectionSubject;
    }

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