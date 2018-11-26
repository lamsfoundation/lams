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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;

/**
 * @author Manpreet Minhas
 */
@Entity
@Table(name = "lams_learning_transition")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transition_type", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Transition implements Serializable {
    // LAMS 2.4 introduced different transition types; "classical" one is progress type; now we also have data flow; see
    // DataTransition
    private static final long serialVersionUID = -1508015273745811405L;

    public static final int PROGRESS_TRANSITION_TYPE = 0;

    public static final int DATA_TRANSITION_TYPE = 1;

    @Id
    @Column(name = "transition_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitionId;

    @Column(name = "transition_ui_id")
    private Integer transitionUIID;

    @Column(name = "to_ui_id")
    private Integer toUIID;

    @Column(name = "from_ui_id")
    private Integer fromUIID;

    @Column
    private String description;

    @Column
    private String title;

    @Column(name = "create_date_time")
    private Date createDateTime;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_activity_id")
    @Cascade(CascadeType.SAVE_UPDATE)
    Activity toActivity;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_activity_id")
    @Cascade(CascadeType.SAVE_UPDATE)
    Activity fromActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_design_id")
    @Cascade(CascadeType.SAVE_UPDATE)
    LearningDesign learningDesign;

    @Column(name = "transition_type", insertable = false, updatable = false)
    protected Integer transitionType = Transition.PROGRESS_TRANSITION_TYPE;

    /*
     * If the value for createDateTime is null then it will be assigned the default value equal to the current datetime
     */
    /** full constructor */
    public Transition(Long transitionId, Integer id, String description, String title, Date createDateTime,
	    Activity toActivity, Activity fromActivity, LearningDesign learningDesign, Integer toUIID,
	    Integer fromUIID) {
	this.transitionId = transitionId;
	transitionUIID = id;
	this.description = description;
	this.title = title;
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
	this.toActivity = toActivity;
	this.fromActivity = fromActivity;
	this.learningDesign = learningDesign;
	this.toUIID = toUIID;
	this.fromUIID = fromUIID;
    }

    /** default constructor */
    public Transition() {
	createDateTime = new Date(); // default value is set to when the Transition object is created
    }

    /** minimal constructor */
    public Transition(Long transitionId, Date createDateTime, Activity toActivity, Activity fromActivity,
	    LearningDesign learningDesign) {
	this.transitionId = transitionId;
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
	this.toActivity = toActivity;
	this.fromActivity = fromActivity;
	this.learningDesign = learningDesign;
    }

    /**
     * Makes a copy of the Transition for authoring, preview and monitoring environment
     *
     * @param originalTransition
     *            The transition to be deep-copied
     * @return Transition Returns a deep-copy o fthe originalTransition
     */
    public static Transition createCopy(Transition originalTransition, int uiidOffset) {

	Transition newTransition = null;
	if (originalTransition.isDataTransition()) {
	    newTransition = new DataTransition();
	    for (DataFlowObject dataFlowObject : ((DataTransition) originalTransition).getDataFlowObjects()) {
		DataFlowObject newDataFlowObject = DataFlowObject.createCopy(dataFlowObject,
			((DataTransition) newTransition));
		((DataTransition) newTransition).getDataFlowObjects().add(newDataFlowObject);
	    }
	} else {
	    newTransition = new Transition();
	}

	newTransition.setTransitionUIID(LearningDesign.addOffset(originalTransition.getTransitionUIID(), uiidOffset));
	newTransition.setDescription(originalTransition.getDescription());
	newTransition.setTitle(originalTransition.getTitle());
	newTransition.setCreateDateTime(new Date());
	newTransition.setToUIID(LearningDesign.addOffset(originalTransition.getToUIID(), uiidOffset));
	newTransition.setFromUIID(LearningDesign.addOffset(originalTransition.getFromUIID(), uiidOffset));
	return newTransition;
    }

    public Long getTransitionId() {
	return transitionId;
    }

    public void setTransitionId(Long transitionId) {
	this.transitionId = transitionId;
    }

    public Integer getTransitionUIID() {
	return transitionUIID;
    }

    public void setTransitionUIID(Integer id) {
	transitionUIID = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Date getCreateDateTime() {
	return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
    }

    public org.lamsfoundation.lams.learningdesign.Activity getToActivity() {
	return toActivity;
    }

    public void setToActivity(org.lamsfoundation.lams.learningdesign.Activity toActivity) {
	this.toActivity = toActivity;
    }

    public org.lamsfoundation.lams.learningdesign.Activity getFromActivity() {
	return fromActivity;
    }

    public void setFromActivity(org.lamsfoundation.lams.learningdesign.Activity fromActivity) {
	this.fromActivity = fromActivity;
    }

    public org.lamsfoundation.lams.learningdesign.LearningDesign getLearningDesign() {
	return learningDesign;
    }

    public void setLearningDesign(org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign) {
	this.learningDesign = learningDesign;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("transitionId", getTransitionId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof Transition)) {
	    return false;
	}
	Transition castOther = (Transition) other;
	return new EqualsBuilder().append(this.getTransitionId(), castOther.getTransitionId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getTransitionId()).toHashCode();
    }

    public Integer getFromUIID() {
	return fromUIID;
    }

    public void setFromUIID(Integer fromUIID) {
	this.fromUIID = fromUIID;
    }

    public Integer getToUIID() {
	return toUIID;
    }

    public void setToUIID(Integer toUIID) {
	this.toUIID = toUIID;
    }

    public TransitionDTO getTransitionDTO() {
	return new TransitionDTO(this);
    }

    public boolean isDataTransition() {
	return transitionType.equals(Transition.DATA_TRANSITION_TYPE);
    }

    public boolean isProgressTransition() {
	return transitionType.equals(Transition.PROGRESS_TRANSITION_TYPE);
    }

    public Integer getTransitionType() {
	return transitionType;
    }
}