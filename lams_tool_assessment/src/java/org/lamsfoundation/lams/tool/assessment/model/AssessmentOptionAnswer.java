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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AssessmentOptionAnswer
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_option_answer")
public class AssessmentOptionAnswer implements Cloneable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "question_option_uid")
    private Long optionUid;

    @Column(name = "answer_int")
    private int answerInt;

    @Column(name = "answer_boolean")
    private boolean answerBoolean;

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * Returns question Option's Uid.
     *
     * @return question Option's Uid
     *
     *
     */
    public Long getOptionUid() {
	return optionUid;
    }

    /**
     * Sets question Option's Uid.
     *
     * @param optionUid
     *            question Option's Uid
     */
    public void setOptionUid(Long optionUid) {
	this.optionUid = optionUid;
    }

    /**
     *
     *
     * @return Returns the possible numeric answer.
     */
    public int getAnswerInt() {
	return answerInt;
    }

    public void setAnswerInt(int answerInt) {
	this.answerInt = answerInt;
    }

    /**
     *
     *
     * @return Returns the boolean answer.
     */
    public boolean getAnswerBoolean() {
	return answerBoolean;
    }

    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
    }
}
