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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Similar to DataOutputDefinition. It's bound with DataTransitions. It defines what outputs the source tool can
 * provide. Teacher can rename the data flow object so it's more meaningful to him.
 *
 */
@Entity
@Table(name = "lams_data_flow")
public class DataFlowObject implements Serializable {
    private static final long serialVersionUID = 4460757845007369769L;

    @Id
    @Column(name = "data_flow_object_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataFlowObjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transition_id")
    private DataTransition dataTransition;

    @Column
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "order_id")
    private Integer orderId;

    /**
     * persistent field It's a tool's internal parameter. It can be anything - the tool itself should know what to do
     * with it. It can be for example an ID of question in Q&A which this data flow object provides input for.
     */
    @Column(name = "tool_assigment_id")
    private Integer toolAssigmentId;

    public DataFlowObject() {

    }

    public DataFlowObject(Long dataFlowObjectId, DataTransition dataTransition, String name, String displayName,
	    Integer orderId, Integer toolAssigmentId) {
	this.dataFlowObjectId = dataFlowObjectId;
	this.dataTransition = dataTransition;
	this.name = name;
	this.displayName = displayName;
	this.orderId = orderId;
	this.toolAssigmentId = toolAssigmentId;
    }

    public Long getDataFlowObjectId() {
	return dataFlowObjectId;
    }

    public void setDataFlowObjectId(Long dataOutputId) {
	dataFlowObjectId = dataOutputId;
    }

    public DataTransition getDataTransition() {
	return dataTransition;
    }

    public void setDataTransition(DataTransition dataTransition) {
	this.dataTransition = dataTransition;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public Integer getToolAssigmentId() {
	return toolAssigmentId;
    }

    public void setToolAssigmentId(Integer toolAssigmentId) {
	this.toolAssigmentId = toolAssigmentId;
    }

    public static DataFlowObject createCopy(DataFlowObject originalDataFlowObject, DataTransition newDataTransition) {
	return new DataFlowObject(null, newDataTransition, originalDataFlowObject.getName(),
		originalDataFlowObject.getDisplayName(), originalDataFlowObject.getOrderId(),
		originalDataFlowObject.getToolAssigmentId());
    }
}