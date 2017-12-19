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

package org.lamsfoundation.lams.tool.mc.pojos;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McContent implements Serializable {

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long mcContentId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String instructions;

    private boolean defineLater;

    /** nullable persistent field */
    private boolean reflect;

    /** nullable persistent field */
    private Date creationDate;

    /** nullable persistent field */
    private Date updateDate;

    /** nullable persistent field */
    private boolean questionsSequenced;

    /** nullable persistent field */
    private long createdBy;

    /** nullable persistent field */
    private boolean retries;

    private boolean showReport;

    private boolean randomize;

    private boolean displayAnswers;

    private boolean showMarks;

    private boolean useSelectLeaderToolOuput;

    private boolean prefixAnswersWithLetters;

    /** nullable persistent field */
    private Date submissionDeadline;

    /** nullable persistent field */
    private Integer passMark;
    
    private boolean enableConfidenceLevels;

    private String reflectionSubject;

    /** persistent field */
    private Set mcQueContents;

    /** persistent field */
    private Set mcSessions;

    /** full constructor */
    public McContent(Long mcContentId, String title, String instructions, boolean defineLater, Date creationDate,
	    Date updateDate, boolean questionsSequenced, long createdBy, Integer passMark,
	    boolean enableConfidenceLevels, boolean showReport, boolean randomize, boolean displayAnswers,
	    boolean showMarks, boolean useSelectLeaderToolOuput, boolean prefixAnswersWithLetters, boolean retries,
	    boolean reflect, String reflectionSubject, Set mcQueContents, Set mcSessions) {

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
	this.showMarks = showMarks;
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
	this.prefixAnswersWithLetters = prefixAnswersWithLetters;
	this.mcQueContents = mcQueContents;
	this.mcSessions = mcSessions;
    }

    /** default constructor */
    public McContent() {
    }

    /** minimal constructor */
    public McContent(Long mcContentId, Set mcQueContents, Set mcSessions) {
	this.mcContentId = mcContentId;
	this.mcQueContents = mcQueContents;
	this.mcSessions = mcSessions;
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
		mc.isDisplayAnswers(), mc.isShowMarks(), mc.isUseSelectLeaderToolOuput(),
		mc.isPrefixAnswersWithLetters(), mc.isRetries(), mc.isReflect(), mc.getReflectionSubject(),
		new TreeSet(), new TreeSet());
	newContent.setMcQueContents(mc.deepCopyMcQueContent(newContent));

	return newContent;
    }

    /**
     * gets called as part of the copyToolContent
     *
     * @param newQaContent
     * @return Set
     */
    public Set deepCopyMcQueContent(McContent newMcContent) {

	Set newMcQueContent = new TreeSet();
	for (Iterator i = this.getMcQueContents().iterator(); i.hasNext();) {
	    McQueContent queContent = (McQueContent) i.next();
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

    public Set getMcQueContents() {
	if (this.mcQueContents == null) {
	    setMcQueContents(new HashSet());
	}
	return this.mcQueContents;
    }

    public void setMcQueContents(Set mcQueContents) {
	this.mcQueContents = mcQueContents;
    }

    public Set getMcSessions() {
	if (this.mcSessions == null) {
	    setMcSessions(new HashSet());
	}
	return this.mcSessions;
    }

    public void setMcSessions(Set mcSessions) {
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
	Iterator itQuestions = getMcQueContents().iterator();
	while (itQuestions.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) itQuestions.next();
	    Integer mark = mcQueContent.getMark();
	    totalMarksPossible += (mark != null ? mark.intValue() : 0);
	}

	return new Integer(totalMarksPossible);
    }

    public boolean isPassMarkApplicable() {
	return passMark != null;
    }
}
