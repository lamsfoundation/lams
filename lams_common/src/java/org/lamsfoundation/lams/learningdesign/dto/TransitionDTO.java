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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.dto;

import java.util.ArrayList;
import java.util.Date;

import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.learningdesign.DataTransition;
import org.lamsfoundation.lams.learningdesign.Transition;

/**
 * @author Manpreet Minhas
 */
public class TransitionDTO extends BaseDTO {

    private Long transitionID;
    private Integer transitionUIID;
    private Integer toUIID;
    private Integer fromUIID;
    private String description;
    private String title;
    private Date createDateTime;
    private Long toActivityID;
    private Long fromActivityID;
    private Long learningDesignID;
    private Integer transitionType;
    private ArrayList<DataFlowObjectDTO> dataFlowObjects;

    public TransitionDTO() {

    }

    public TransitionDTO(Long transitionId, Integer transitionUIID, Integer toUIID, Integer fromUIID,
	    String description, String title, Date createDateTime, Long toActivityID, Long fromActivityID,
	    Long learningDesignID) {
	super();
	transitionID = transitionId;
	this.transitionUIID = transitionUIID;
	this.toUIID = toUIID;
	this.fromUIID = fromUIID;
	this.description = description;
	this.title = title;
	this.createDateTime = createDateTime;
	this.toActivityID = toActivityID;
	this.fromActivityID = fromActivityID;
	this.learningDesignID = learningDesignID;
	dataFlowObjects = new ArrayList<DataFlowObjectDTO>();
    }

    public TransitionDTO(Transition transition) {
	transitionID = transition.getTransitionId();
	transitionUIID = transition.getTransitionUIID();
	toUIID = transition.getToUIID();
	fromUIID = transition.getFromUIID();
	description = transition.getDescription();
	title = transition.getTitle();
	createDateTime = transition.getCreateDateTime();
	toActivityID = transition.getToActivity().getActivityId();
	fromActivityID = transition.getFromActivity().getActivityId();
	learningDesignID = transition.getLearningDesign().getLearningDesignId();
	transitionType = transition.getTransitionType();
	dataFlowObjects = new ArrayList<DataFlowObjectDTO>();
	if (transition.isDataTransition()) {
	    DataTransition dataTransition = (DataTransition) transition;
	    for (DataFlowObject dataFlowObject : dataTransition.getDataFlowObjects()) {
		DataFlowObjectDTO dataFlowObjectDto = new DataFlowObjectDTO(dataFlowObject);
		dataFlowObjects.add(dataFlowObjectDto);
	    }
	}
    }

    /**
     * @return Returns the createDateTime.
     */
    public Date getCreateDateTime() {
	return createDateTime;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the fromActivityID.
     */
    public Long getFromActivityID() {
	return fromActivityID;
    }

    /**
     * @return Returns the fromUIID.
     */
    public Integer getFromUIID() {
	return fromUIID;
    }

    /**
     * @return Returns the learningDesignID.
     */
    public Long getLearningDesignID() {
	return learningDesignID;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @return Returns the toActivityID.
     */
    public Long getToActivityID() {
	return toActivityID;
    }

    /**
     * @return Returns the toUIID.
     */
    public Integer getToUIID() {
	return toUIID;
    }

    /**
     * @return Returns the transitionID.
     */
    public Long getTransitionID() {
	return transitionID;
    }

    /**
     * @return Returns the transitionUIID.
     */
    public Integer getTransitionUIID() {
	return transitionUIID;
    }

    /**
     * @param createDateTime
     *            The createDateTime to set.
     */
    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @param fromActivityID
     *            The fromActivityID to set.
     */
    public void setFromActivityID(Long fromActivityID) {
	this.fromActivityID = fromActivityID;
    }

    /**
     * @param fromUIID
     *            The fromUIID to set.
     */
    public void setFromUIID(Integer fromUIID) {
	this.fromUIID = fromUIID;
    }

    /**
     * @param learningDesignID
     *            The learningDesignID to set.
     */
    public void setLearningDesignID(Long learningDesignID) {
	this.learningDesignID = learningDesignID;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @param toActivityID
     *            The toActivityID to set.
     */
    public void setToActivityID(Long toActivityID) {
	this.toActivityID = toActivityID;
    }

    /**
     * @param toUIID
     *            The toUIID to set.
     */
    public void setToUIID(Integer toUIID) {
	this.toUIID = toUIID;
    }

    /**
     * @param transitionID
     *            The transitionID to set.
     */
    public void setTransitionID(Long transitionId) {
	transitionID = transitionId;
    }

    /**
     * @param transitionUIID
     *            The transitionUIID to set.
     */
    public void setTransitionUIID(Integer transitionUIID) {
	this.transitionUIID = transitionUIID;
    }

    public Integer getTransitionType() {
	return transitionType;
    }

    public void setTransitionType(Integer transitionType) {
	this.transitionType = transitionType;
    }

    public ArrayList<DataFlowObjectDTO> getDataFlowObjects() {
	return dataFlowObjects;
    }

    public void setDataFlowObjects(ArrayList<DataFlowObjectDTO> dataFlowObjects) {
	this.dataFlowObjects = dataFlowObjects;
    }
}
