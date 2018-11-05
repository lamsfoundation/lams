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

package org.lamsfoundation.lams.tool.mc.dto;

import org.lamsfoundation.lams.tool.mc.model.McOptsContent;

/**
 * DTO that holds candiate answers data for summary page
 *
 * @author Ozgur Demirtas
 */
public class McOptionDTO implements Comparable {

    private Long uid;
    private String candidateAnswer;
    private String correct;
    
    /**
     * used for TBL monitoring
     */
    private float percentage;

    public McOptionDTO() {

    }

    public McOptionDTO(McOptsContent option) {
	this.uid = option.getUid();
	this.candidateAnswer = option.getMcQueOptionText();
	//this.correct = new Boolean(option.isCorrectOption()).toString();

	if (option.isCorrectOption()) {
	    this.correct = "Correct";
	} else {
	    this.correct = "Incorrect";
	}
    }

    /**
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the candidateAnswer.
     */
    public String getCandidateAnswer() {
	return candidateAnswer;
    }

    /**
     * @param candidateAnswer
     *            The candidateAnswer to set.
     */
    public void setCandidateAnswer(String candidateAnswer) {
	this.candidateAnswer = candidateAnswer;
    }

    /**
     * @return Returns the correct.
     */
    public String getCorrect() {
	return correct;
    }

    /**
     * @param correct
     *            The correct to set.
     */
    public void setCorrect(String correct) {
	this.correct = correct;
    }
    
    public float getPercentage() {
	return percentage;
    }
    
    public void setPercentage(float percentage) {
	this.percentage = percentage;
    }

    @Override
    public int compareTo(Object o) {
	McOptionDTO mcOptionDTO = (McOptionDTO) o;

	if (mcOptionDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

}
