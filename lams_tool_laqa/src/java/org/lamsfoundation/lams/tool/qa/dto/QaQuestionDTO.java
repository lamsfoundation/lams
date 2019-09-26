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

package org.lamsfoundation.lams.tool.qa.dto;

import org.lamsfoundation.lams.tool.qa.model.QaQueContent;

/**
 * DTO that holds users attempt history data for jsp purposes
 *
 * @author Ozgur Demirtas
 */
public class QaQuestionDTO implements Comparable<QaMonitoredUserDTO> {
    private Long uid;
    private String name;
    private String description;
    private String displayOrder;
    private String feedback;
    private boolean required;
    private int minWordsLimit;

    public QaQuestionDTO(QaQueContent qaQuestion) {
	this.name = qaQuestion.getQbQuestion().getName();
	this.description = qaQuestion.getQbQuestion().getDescription();
	this.displayOrder = String.valueOf(qaQuestion.getDisplayOrder());
	this.feedback = qaQuestion.getQbQuestion().getFeedback() != null ? qaQuestion.getQbQuestion().getFeedback() : "";
	this.required = qaQuestion.getQbQuestion().isAnswerRequired();
	this.minWordsLimit = qaQuestion.getQbQuestion().getMinWordsLimit();
	this.uid = qaQuestion.getUid();
    }

    @Override
    public int compareTo(QaMonitoredUserDTO o) {
	if (o == null) {
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
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String question) {
	this.name = question;
    }
    
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return Is this name required?
     */
    public boolean isRequired() {
	return required;
    }

    /**
     * @param required
     *            Is this name required
     */
    public void setRequired(boolean required) {
	this.required = required;
    }

    /**
     * @return minWordsLimit
     */
    public int getMinWordsLimit() {
	return minWordsLimit;
    }

    /**
     * @param minWordsLimit
     *            minWordsLimit
     */
    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof QaQuestionDTO) {
	    QaQuestionDTO compare = (QaQuestionDTO) o;
	    return compare.getDisplayOrder().equals(getDisplayOrder());
	}
	return false;
    }

    @Override
    public int hashCode() {
	return Integer.valueOf(displayOrder);
    }
}
