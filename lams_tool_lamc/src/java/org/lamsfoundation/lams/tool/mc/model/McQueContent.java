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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;

/**
 * <p>
 * Persistent object/bean that defines the question content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_que_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_lamc11_que_content")
// in this entity's table primary key is "uid", but it references "tool_question_uid" in lams_qb_tool_question
@PrimaryKeyJoinColumn(name = "uid")
public class McQueContent extends QbToolQuestion implements Serializable, Comparable<McQueContent> {
    private static final long serialVersionUID = 4022287106119453962L;

    /**
     * It stores sha1(question) value that allows us to search for the McQueContentc with the same question
     */
    @Column(name = "question_hash")
    private String questionHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mc_content_id")
    private McContent mcContent;

    //DTO fields
    @Transient
    private String escapedQuestion;

    public McQueContent(QbQuestion qbQuestion, String questionHash, Integer displayOrder, McContent mcContent) {
	this.qbQuestion = qbQuestion;
	this.questionHash = questionHash;
	this.displayOrder = displayOrder;
	setMcContent(mcContent);
    }

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
	McQueContent newQueContent = new McQueContent(queContent.getQbQuestion(), queContent.getQuestionHash(),
		queContent.getDisplayOrder(), newMcContent);
	return newQueContent;
    }

    public String getQuestion() {
	return this.qbQuestion.getName();
    }

    public void setQuestion(String question) {
	this.qbQuestion.setName(question);
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

    public org.lamsfoundation.lams.tool.mc.model.McContent getMcContent() {
	return this.mcContent;
    }

    public void setMcContent(McContent mcContent) {
	this.mcContent = mcContent;
	this.toolContentId = mcContent.getMcContentId();
    }

    /**
     * Get an options content record by its uid. Iterates over the set from getMcOptionsContents().
     */
    public QbOption getOptionByUID(Long uid) {
	for (QbOption elem : qbQuestion.getQbOptions()) {
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
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return qbQuestion.getFeedback();
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	qbQuestion.setFeedback(feedback);
    }

    @Override
    public int compareTo(McQueContent queContent) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else if (queContent.getUid() == null) {
	    return -1;
	} else {
	    return (int) (uid.longValue() - queContent.getUid().longValue());
	}
    }

    /**
     * @return Returns the mark.
     */
    public Integer getMark() {
	return qbQuestion.getMaxMark();
    }

    /**
     * @param mark
     *            The mark to set.
     */
    public void setMark(Integer mark) {
	qbQuestion.setMaxMark(mark);
    }

    public String getEscapedQuestion() {
	return this.escapedQuestion;
    }

    public void setEscapedQuestion(String escapedQuestion) {
	this.escapedQuestion = escapedQuestion;
    }
}
