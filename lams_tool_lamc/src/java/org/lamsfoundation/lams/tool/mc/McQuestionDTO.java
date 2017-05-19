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

package org.lamsfoundation.lams.tool.mc;

import java.util.List;

/**
 * DTO that holds users attempt history data for jsp purposes
 *
 * @author Ozgur Demirtas
 */
public class McQuestionDTO implements Comparable {
    private Long uid;
    private String question;
    private String displayOrder;
    private String feedback;
    private String weight;
    private String mark;

    private List<McOptionDTO> listCandidateAnswersDTO;

    @Override
    public int compareTo(Object o) {
	McQuestionDTO mcQuestionDTO = (McQuestionDTO) o;

	if (mcQuestionDTO == null) {
	    return 1;
	} else {
	    return 0;
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
     * @return Returns the displayOrder.
     */
    public String getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(String displayOrder) {
	this.displayOrder = displayOrder;
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

    /**
     * @return Returns the question.
     */
    public String getQuestion() {
	return question;
    }

    /**
     * @param question
     *            The question to set.
     */
    public void setQuestion(String question) {
	this.question = question;
    }

    /**
     * @return Returns the weight.
     */
    public String getWeight() {
	return weight;
    }

    /**
     * @param weight
     *            The weight to set.
     */
    public void setWeight(String weight) {
	this.weight = weight;
    }

    /**
     * @return Returns the mark.
     */
    public String getMark() {
	return mark;
    }

    /**
     * @param mark
     *            The mark to set.
     */
    public void setMark(String mark) {
	this.mark = mark;
    }

    /**
     * @return Returns the listCandidateAnswersDTO.
     */
    public List<McOptionDTO> getListCandidateAnswersDTO() {
	return listCandidateAnswersDTO;
    }

    /**
     * @param listCandidateAnswersDTO
     *            The listCandidateAnswersDTO to set.
     */
    public void setListCandidateAnswersDTO(List<McOptionDTO> listCandidateAnswersDTO) {
	this.listCandidateAnswersDTO = listCandidateAnswersDTO;
    }
}
