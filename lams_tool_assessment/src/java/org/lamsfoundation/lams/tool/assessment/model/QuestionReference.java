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

import org.apache.log4j.Logger;

/**
 * Reference to the Question
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_laasse10_question_reference"
 *
 */
public class QuestionReference implements Cloneable, Sequencable {
    private static final Logger log = Logger.getLogger(QuestionReference.class);

    private Long uid;

    private AssessmentQuestion question;

    private short type;

    private String title;

    private int sequenceId;

    private int defaultGrade;

    private boolean randomQuestion;

    // ***********************************************
    // Non persistant fields:

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
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @hibernate.many-to-one cascade="save-update" column="question_uid"
     *
     * @return
     */
    public AssessmentQuestion getQuestion() {
	return question;
    }

    public void setQuestion(AssessmentQuestion question) {
	this.question = question;
    }

    /**
     * @hibernate.property column="question_type"
     * @return
     */
    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    /**
     * @hibernate.property column="title" length="255"
     * @return
     */
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
     * @hibernate.property column="sequence_id"
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

    /**
     * @hibernate.property column="default_grade"
     *
     * @return
     */
    public int getDefaultGrade() {
	return defaultGrade;
    }

    public void setDefaultGrade(int defaultGrade) {
	this.defaultGrade = defaultGrade;
    }

    /**
     * @hibernate.property column="random_question"
     * @return
     */
    public boolean isRandomQuestion() {
	return randomQuestion;
    }

    public void setRandomQuestion(boolean randomQuestion) {
	this.randomQuestion = randomQuestion;
    }

}
