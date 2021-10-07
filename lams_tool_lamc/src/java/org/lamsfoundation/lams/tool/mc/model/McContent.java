/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.model;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortComparator;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;

/**
 * <p>
 * Persistent object/bean that defines the content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tl_lamc11_content")
public class McContent implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long mcContentId;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column
    private boolean reflect;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "questions_sequenced")
    private boolean questionsSequenced;

    @Column(name = "created_by")
    private long createdBy;

    @Column
    private boolean retries;

    @Column(name = "show_report")
    private boolean showReport;

    @Column
    private boolean randomize;

    @Column
    private boolean displayAnswers;

    @Column(name = "display_feedback_only")
    private boolean displayFeedbackOnly;

    @Column
    private boolean showMarks;

    @Column(name = "use_select_leader_tool_ouput")
    private boolean useSelectLeaderToolOuput;

    @Column(name = "prefix_answers_with_letters")
    private boolean prefixAnswersWithLetters;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @Column(name = "pass_mark")
    private Integer passMark;

    @Column(name = "enable_confidence_levels")
    private boolean enableConfidenceLevels;

    @Column
    private String reflectionSubject;

    @OneToMany(mappedBy = "mcContent", cascade = CascadeType.ALL)
    @SortComparator(QbToolQuestion.QbToolQuestionComparator.class)
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<McQueContent> mcQueContents;

    @OneToMany(mappedBy = "mcContent", cascade = CascadeType.ALL, orphanRemoval = true)
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<McSession> mcSessions;

    /** full constructor */
    public McContent(Long mcContentId, String title, String instructions, boolean defineLater, Date creationDate,
	    Date updateDate, boolean questionsSequenced, long createdBy, Integer passMark,
	    boolean enableConfidenceLevels, boolean showReport, boolean randomize, boolean displayAnswers,
	    boolean displayFeedbackOnly, boolean showMarks, boolean useSelectLeaderToolOuput,
	    boolean prefixAnswersWithLetters, boolean retries, boolean reflect, String reflectionSubject,
	    Set<McQueContent> mcQueContents, Set<McSession> mcSessions) {

	this.mcContentId = mcContentId;
	this.title = title;
	this.instructions = instructions;
	this.defineLater = defineLater;
	this.creationDate = creationDate;
	this.updateDate = updateDate;
	this.questionsSequenced = questionsSequenced;
	this.createdBy = createdBy;
	this.retries = retries;
	this.reflectionSubject = reflectionSubject;
	this.reflect = reflect;
	this.passMark = passMark;
	this.enableConfidenceLevels = enableConfidenceLevels;
	this.showReport = showReport;
	this.randomize = randomize;
	this.displayAnswers = displayAnswers;
	this.displayFeedbackOnly = displayFeedbackOnly;
	this.showMarks = showMarks;
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
	this.prefixAnswersWithLetters = prefixAnswersWithLetters;
	this.mcQueContents = mcQueContents != null ? mcQueContents : new HashSet<>();
	this.mcSessions = mcSessions != null ? mcSessions : new HashSet<>();
    }

    /** default constructor */
    public McContent() {
	this.mcQueContents = new TreeSet<>();
	this.mcSessions = new HashSet<>();
    }

    /** minimal constructor */
    public McContent(Long mcContentId, Set<McQueContent> mcQueContents, Set<McSession> mcSessions) {
	this.mcContentId = mcContentId;
	this.mcQueContents = mcQueContents != null ? mcQueContents : new HashSet<>();
	this.mcSessions = mcSessions != null ? mcSessions : new HashSet<>();
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
    public static McContent newInstance(McContent mc, Long newContentId) {
	McContent newContent = new McContent(newContentId, mc.getTitle(), mc.getInstructions(), mc.isDefineLater(),
		mc.getCreationDate(), mc.getUpdateDate(), mc.isQuestionsSequenced(), mc.getCreatedBy(),
		mc.getPassMark(), mc.isEnableConfidenceLevels(), mc.isShowReport(), mc.isRandomize(),
		mc.isDisplayAnswers(), mc.displayFeedbackOnly, mc.isShowMarks(), mc.isUseSelectLeaderToolOuput(),
		mc.isPrefixAnswersWithLetters(), mc.isRetries(), mc.isReflect(), mc.getReflectionSubject(),
		new TreeSet<McQueContent>(), new TreeSet<McSession>());
	newContent.setMcQueContents(mc.deepCopyMcQueContent(newContent));

	return newContent;
    }

    /**
     * gets called as part of the copyToolContent
     *
     * @param newQaContent
     * @return Set
     */
    public Set<McQueContent> deepCopyMcQueContent(McContent newMcContent) {

	Set<McQueContent> newMcQueContent = new TreeSet<>();
	for (McQueContent queContent : this.getMcQueContents()) {
	    if (queContent.getMcContent() != null) {
		McQueContent mcQueContent = McQueContent.newInstance(queContent, newMcContent);
		newMcQueContent.add(mcQueContent);
	    }
	}
	return newMcQueContent;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getMcContentId() {
	return this.mcContentId;
    }

    public void setMcContentId(Long mcContentId) {
	this.mcContentId = mcContentId;
	for (McQueContent mcQuestion : mcQueContents) {
	    mcQuestion.setToolContentId(mcContentId);
	}
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public boolean isQuestionsSequenced() {
	return this.questionsSequenced;
    }

    public void setQuestionsSequenced(boolean questionsSequenced) {
	this.questionsSequenced = questionsSequenced;
    }

    public long getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(long createdBy) {
	this.createdBy = createdBy;
    }

    public Integer getPassMark() {
	return this.passMark;
    }

    public void setPassMark(Integer passMark) {
	this.passMark = passMark;
    }

    /**
     *
     * @return
     */
    public boolean isEnableConfidenceLevels() {
	return enableConfidenceLevels;
    }

    public void setEnableConfidenceLevels(boolean enableConfidenceLevels) {
	this.enableConfidenceLevels = enableConfidenceLevels;
    }

    public Set<McQueContent> getMcQueContents() {
	return this.mcQueContents;
    }

    public void setMcQueContents(Set<McQueContent> mcQueContents) {
	this.mcQueContents = mcQueContents;
    }

    public Set<McSession> getMcSessions() {
	return this.mcSessions;
    }

    public void setMcSessions(Set<McSession> mcSessions) {
	this.mcSessions = mcSessions;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    /**
     * @return Returns the retries.
     */
    public boolean isRetries() {
	return retries;
    }

    /**
     * @param retries
     *            The retries to set.
     */
    public void setRetries(boolean retries) {
	this.retries = retries;
    }

    /**
     * @return Returns the showReport.
     */
    public boolean isShowReport() {
	return showReport;
    }

    /**
     * @param showReport
     *            The showReport to set.
     */
    public void setShowReport(boolean showReport) {
	this.showReport = showReport;
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

    /**
     * @return Returns the showMarks.
     */
    public boolean isShowMarks() {
	return showMarks;
    }

    /**
     * @param showMarks
     *            The showMarks to set.
     */
    public void setShowMarks(boolean showMarks) {
	this.showMarks = showMarks;
    }

    /**
     * @return
     */
    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    /**
     * @param useSelectLeaderToolOuput
     */
    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return
     */
    public boolean isPrefixAnswersWithLetters() {
	return prefixAnswersWithLetters;
    }

    /**
     * @param prefixAnswersWithLetters
     */
    public void setPrefixAnswersWithLetters(boolean prefixAnswersWithLetters) {
	this.prefixAnswersWithLetters = prefixAnswersWithLetters;
    }

    /**
     * @return Returns the randomize.
     */
    public boolean isRandomize() {
	return randomize;
    }

    /**
     * @param randomize
     *            The randomize to set.
     */
    public void setRandomize(boolean randomize) {
	this.randomize = randomize;
    }

    /**
     * @return Returns the displayAnswers.
     */
    public boolean isDisplayAnswers() {
	return displayAnswers;
    }

    /**
     * @param displayAnswers
     *            The displayAnswers to set.
     */
    public void setDisplayAnswers(boolean displayAnswers) {
	this.displayAnswers = displayAnswers;
    }

    public boolean isDisplayFeedbackOnly() {
	return displayFeedbackOnly;
    }

    public void setDisplayFeedbackOnly(boolean displayFeedbackOnly) {
	this.displayFeedbackOnly = displayFeedbackOnly;
    }

    /**
     * @return date submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     * Get total possible marks for this content. Iterates over the McQueContents set
     */
    public Integer getTotalMarksPossible() {

	int totalMarksPossible = 0;
	for (McQueContent mcQueContent : this.getMcQueContents()) {
	    Integer mark = mcQueContent.getMark();
	    totalMarksPossible += (mark != null ? mark.intValue() : 0);
	}

	return Integer.valueOf(totalMarksPossible);
    }

    public boolean isPassMarkApplicable() {
	return passMark != null;
    }
}
