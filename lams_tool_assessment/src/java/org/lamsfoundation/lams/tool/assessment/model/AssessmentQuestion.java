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

package org.lamsfoundation.lams.tool.assessment.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;

/**
 * Assessment Question
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_assessment_question")
//in this entity's table primary key is "uid", but it references "tool_question_uid" in lams_qb_tool_question
@PrimaryKeyJoinColumn(name = "uid")
public class AssessmentQuestion extends QbToolQuestion
	implements Serializable, Cloneable, Comparable<AssessmentQuestion> {
    private static final long serialVersionUID = -7767327140430305575L;
    private static final Logger log = Logger.getLogger(AssessmentQuestion.class);
    
    @Column(name = "random_question")
    private boolean randomQuestion;

    @Column(name = "correct_answers_disclosed")
    private boolean correctAnswersDisclosed;

    @Column(name = "groups_answers_disclosed")
    private boolean groupsAnswersDisclosed;

    @Override
    public Object clone() {
	AssessmentQuestion obj = null;
	try {
	    obj = (AssessmentQuestion) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + AssessmentQuestion.class + " failed");
	}

	return obj;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof AssessmentQuestion)) {
	    return false;
	}

	final AssessmentQuestion genericEntity = (AssessmentQuestion) o;

	return new EqualsBuilder().append(this.getUid(), genericEntity.getUid())
		.append(this.getDisplayOrder(), genericEntity.getDisplayOrder()).isEquals();
    }

    @Override
    public int compareTo(AssessmentQuestion anotherQuestion) {
	return displayOrder - anotherQuestion.getDisplayOrder();
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUid()).append(getDisplayOrder()).toHashCode();
    }

    public QuestionDTO getQuestionDTO() {
	QuestionDTO questionDTO = new QuestionDTO(this);

	return questionDTO;
    }

    public Integer getType() {
	return getQbQuestion().getType();
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
    
    public boolean isRandomQuestion() {
	return randomQuestion;
    }

    public void setRandomQuestion(boolean randomQuestion) {
	this.randomQuestion = randomQuestion;
    }

    public boolean isCorrectAnswersDisclosed() {
	return correctAnswersDisclosed;
    }

    public void setCorrectAnswersDisclosed(boolean correctAnswersDisclosed) {
	this.correctAnswersDisclosed = correctAnswersDisclosed;
    }

    public boolean isGroupsAnswersDisclosed() {
	return groupsAnswersDisclosed;
    }

    public void setGroupsAnswersDisclosed(boolean groupsAnswersDisclosed) {
	this.groupsAnswersDisclosed = groupsAnswersDisclosed;
    }
}
