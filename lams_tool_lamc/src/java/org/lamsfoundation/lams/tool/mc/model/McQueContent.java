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
import java.util.HashSet;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the question content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_que_content
 * </p>
 *
 * @author Ozgur Demirtas
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tl_lamc11_que_content")
public class McQueContent implements Serializable, Comparable<McQueContent> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String question;

    /**
     * It stores sha1(question) value that allows us to search for the McQueContentc with the same question
     */
    @Column(name = "question_hash")
    private String questionHash;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column
    private Integer mark;

    @Column
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mc_content_id")
    private org.lamsfoundation.lams.tool.mc.model.McContent mcContent;

    // TODO Make this orphanRemoval = true, but first fix McService.createQuestions() to stop mucking around with the collections.
    // Currently options that are deleted in authoring leave junk records in the database with the mc_que_content_id set to null
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("displayOrder")
    @JoinColumn(name = "mc_que_content_id")
    private Set<McOptsContent> mcOptionsContents;

    //DTO fields
    @Transient
    private String escapedQuestion;

    public McQueContent(String question, String questionHash, Integer displayOrder, Integer mark, String feedback,
	    McContent mcContent, Set<McOptsContent> mcOptionsContents) {
	this.question = question;
	this.questionHash = questionHash;
	this.displayOrder = displayOrder;
	this.mark = mark;
	this.feedback = feedback;
	this.mcContent = mcContent;
	this.mcOptionsContents = mcOptionsContents != null ? mcOptionsContents : new HashSet<McOptsContent>();
    }

    /** default constructor */
    public McQueContent() {
	this.mcOptionsContents = new HashSet<McOptsContent>();
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
	McQueContent newQueContent = new McQueContent(queContent.getQuestion(), queContent.getQuestionHash(),
		queContent.getDisplayOrder(), queContent.getMark(), queContent.getFeedback(), newMcContent,
		new TreeSet<McOptsContent>());
	newQueContent.setMcOptionsContents(queContent.deepCopyMcOptionsContent(newQueContent));
	return newQueContent;
    }

    public Set<McOptsContent> deepCopyMcOptionsContent(McQueContent newQueContent) {
	Set<McOptsContent> newMcOptionsContent = new TreeSet<McOptsContent>();
	for (McOptsContent mcOptsContent : this.getMcOptionsContents()) {
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

    public org.lamsfoundation.lams.tool.mc.model.McContent getMcContent() {
	return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.model.McContent mcContent) {
	this.mcContent = mcContent;
    }

    public Set<McOptsContent> getMcOptionsContents() {
	return this.mcOptionsContents;
    }

    public void setMcOptionsContents(Set<McOptsContent> mcOptionsContents) {
	this.mcOptionsContents = mcOptionsContents;
    }

    /**
     * Get an options content record by its uid. Iterates over the set from getMcOptionsContents().
     */
    public McOptsContent getOptionsContentByUID(Long uid) {
	for (McOptsContent elem : getMcOptionsContents()) {
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
