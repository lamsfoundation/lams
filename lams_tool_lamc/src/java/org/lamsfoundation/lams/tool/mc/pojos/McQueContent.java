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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the question content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_que_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McQueContent implements Serializable, Comparable<McQueContent> {

    /** identifier field */
    private Long uid;

    /** Stores mcQueContent.uid, despite of what the name says */
    private Long mcQueContentId;

    /** nullable persistent field */
    private String question;
    
    /**
     * It stores sha1(question) value that allows us to search for the McQueContentc with the same question
     */
    private String questionHash;

    /** nullable persistent field */
    private Integer displayOrder;

    private Integer mark;

    private String feedback;

    /** non persistent field */
    private Long mcContentId;

    /** persistent field */
    private org.lamsfoundation.lams.tool.mc.pojos.McContent mcContent;

    /** persistent field */
    private Set mcOptionsContents;

    //DTO fields

    private String escapedQuestion;

    public McQueContent(String question, String questionHash, Integer displayOrder, Integer mark, String feedback, McContent mcContent,
	    Set mcUsrAttempts, Set mcOptionsContents) {
	this.question = question;
	this.questionHash = questionHash;
	this.displayOrder = displayOrder;
	this.mark = mark;
	this.feedback = feedback;
	this.mcContent = mcContent;
	this.mcOptionsContents = mcOptionsContents;
    }

    /** default constructor */
    public McQueContent() {
    }

    /**
     * gets called by copyToolContent
     *
     * Copy constructor
     *
     * @param queContent
     *            the original qa question content
     * @return the new qa question content object
     */
    public static McQueContent newInstance(McQueContent queContent, McContent newMcContent) {
	McQueContent newQueContent = new McQueContent(queContent.getQuestion(), queContent.getQuestionHash(), queContent.getDisplayOrder(),
		queContent.getMark(), queContent.getFeedback(), newMcContent, new TreeSet(), new TreeSet());

	newQueContent.setMcOptionsContents(queContent.deepCopyMcOptionsContent(newQueContent));
	return newQueContent;
    }

    public Set deepCopyMcOptionsContent(McQueContent newQueContent) {
	Set newMcOptionsContent = new TreeSet();
	for (Iterator i = this.getMcOptionsContents().iterator(); i.hasNext();) {
	    McOptsContent mcOptsContent = (McOptsContent) i.next();
	    McOptsContent mcNewOptsContent = McOptsContent.newInstance(mcOptsContent, newQueContent);
	    newMcOptionsContent.add(mcNewOptsContent);
	}
	return newMcOptionsContent;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getMcQueContentId() {
	return this.mcQueContentId;
    }

    public void setMcQueContentId(Long mcQueContentId) {
	this.mcQueContentId = mcQueContentId;
    }

    public String getQuestion() {
	return this.question;
    }

    public void setQuestion(String question) {
	this.question = question;
    }
    
    /**
     * Returns sha1(question) value that allows us to search for the McQueContent with the same question
     */
    public String getQuestionHash() {
	return questionHash;
    }
    public void setQuestionHash(String questionHash) {
	this.questionHash = questionHash;
    }

    public Integer getDisplayOrder() {
	return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
	this.displayOrder = displayOrder;
    }

    public org.lamsfoundation.lams.tool.mc.pojos.McContent getMcContent() {
	return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.pojos.McContent mcContent) {
	this.mcContent = mcContent;
    }

    public Set getMcOptionsContents() {
	if (this.mcOptionsContents == null) {
	    setMcOptionsContents(new HashSet());
	}
	return this.mcOptionsContents;
    }

    public void setMcOptionsContents(Set mcOptionsContents) {
	this.mcOptionsContents = mcOptionsContents;
    }

    /**
     * Get an options content record by its uid. Iterates over the set from getMcOptionsContents().
     */
    public McOptsContent getOptionsContentByUID(Long uid) {
	Iterator iter = getMcOptionsContents().iterator();
	while (iter.hasNext()) {
	    McOptsContent elem = (McOptsContent) iter.next();
	    if (elem.getUid().equals(uid)) {
		return elem;
	    }
	}
	return null;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    /**
     * @return Returns the mcContentId.
     */
    public Long getMcContentId() {
	return mcContentId;
    }

    /**
     * @param mcContentId
     *            The mcContentId to set.
     */
    public void setMcContentId(Long mcContentId) {
	this.mcContentId = mcContentId;
    }

    /**
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return feedback;
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    @Override
    public int compareTo(McQueContent queContent) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (mcQueContentId == null) {
	    return 1;
	} else {
	    return (int) (mcQueContentId.longValue() - queContent.mcQueContentId.longValue());
	}
    }

    /**
     * @return Returns the mark.
     */
    public Integer getMark() {
	return mark;
    }

    /**
     * @param mark
     *            The mark to set.
     */
    public void setMark(Integer mark) {
	this.mark = mark;
    }

    public String getEscapedQuestion() {
	return this.escapedQuestion;
    }

    public void setEscapedQuestion(String escapedQuestion) {
	this.escapedQuestion = escapedQuestion;
    }
}
