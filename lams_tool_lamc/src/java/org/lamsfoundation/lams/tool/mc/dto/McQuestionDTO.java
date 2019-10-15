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

import org.lamsfoundation.lams.qb.service.IQbService;

/**
 * DTO that holds users attempt history data for jsp purposes
 *
 * @author Ozgur Demirtas
 */
public class McQuestionDTO implements Comparable<McQuestionDTO> {
    private Long uid;
    private Long qbQuestionUid;
    private int qbQuestionModified = IQbService.QUESTION_MODIFIED_NONE;
    private String name;
    private String description;
    private Integer displayOrder;
    private String feedback;
    private String mark;
    private String contentFolderId;

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

    public Long getQbQuestionUid() {
	return qbQuestionUid;
    }

    public void setQbQuestionUid(Long qaQuestionId) {
	this.qbQuestionUid = qaQuestionId;
    }

    public int getQbQuestionModified() {
	return qbQuestionModified;
    }

    public void setQbQuestionModified(int qbQuestionModified) {
	this.qbQuestionModified = qbQuestionModified;
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
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
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

    
    
    public String getContentFolderId() {
        return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
        this.contentFolderId = contentFolderId;
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
