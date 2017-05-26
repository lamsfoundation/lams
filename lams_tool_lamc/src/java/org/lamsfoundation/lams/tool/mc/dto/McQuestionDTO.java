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

package org.lamsfoundation.lams.tool.mc.dto;

import java.util.List;

/**
 * DTO that holds users attempt history data for jsp purposes
 *
 * @author Ozgur Demirtas
 */
public class McQuestionDTO implements Comparable<McQuestionDTO> {
    private Long uid;
    private String question;
    private Integer displayOrder;
    private String feedback;
    private String mark;

    private List<McOptionDTO> optionDtos;

    @Override
    public int compareTo(McQuestionDTO mcQuestionDTO) {

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
    public Integer getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(Integer displayOrder) {
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
     * @return Returns the optionDtos.
     */
    public List<McOptionDTO> getOptionDtos() {
	return optionDtos;
    }

    /**
     * @param optionDtos
     *            The optionDtos to set.
     */
    public void setOptionDtos(List<McOptionDTO> optionDtos) {
	this.optionDtos = optionDtos;
    }
}
