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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.assessment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Reference to the Question
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_question_reference")
public class QuestionReference implements Cloneable, Sequencable {
    private static final Logger log = Logger.getLogger(QuestionReference.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "question_type")
    private short type;

    @Column
    private String title;

    @Column(name = "sequence_id")
    private int sequenceId;

    @Column(name = "default_grade")
    private int maxMark;

    @Column(name = "random_question")
    private boolean randomQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_uid")
    private AssessmentQuestion question;

    @Override
    public Object clone() {
	QuestionReference obj = null;
	try {
	    obj = (QuestionReference) super.clone();
	    obj.setUid(null);

//	    // clone AssessmentQuestion as well
//	    if (this.question != null) {
//		obj.setQuestion((AssessmentQuestion) this.question.clone());
//	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + QuestionReference.class + " failed");
	}

	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public AssessmentQuestion getQuestion() {
	return question;
    }

    public void setQuestion(AssessmentQuestion question) {
	this.question = question;
    }

    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Returns image sequence number.
     *
     * @return image sequence number
     *
     *
     */
    @Override
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets image sequence number.
     *
     * @param sequenceId
     *            image sequence number
     */
    @Override
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    public int getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(int maxMark) {
	this.maxMark = maxMark;
    }

    public boolean isRandomQuestion() {
	return randomQuestion;
    }

    public void setRandomQuestion(boolean randomQuestion) {
	this.randomQuestion = randomQuestion;
    }

}
