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


package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.DataFlowObject;

public class DataFlowObjectDTO {

    private Long dataFlowObjectId;

    private String name;

    private String displayName;

    private Integer orderId;

    private String toolAssigmentId;

    public DataFlowObjectDTO() {

    }

    public DataFlowObjectDTO(Long dataFlowObjectId, String name, String displayName, Integer orderId,
	    Integer toolAssigmentId) {
	this.dataFlowObjectId = dataFlowObjectId;
	this.name = name;
	this.displayName = displayName;
	this.orderId = orderId;
	this.toolAssigmentId = toolAssigmentId == null ? null : toolAssigmentId.toString();
    }

    public DataFlowObjectDTO(DataFlowObject dataFlowObject) {
	dataFlowObjectId = dataFlowObject.getDataFlowObjectId();
	name = dataFlowObject.getName();
	displayName = dataFlowObject.getDisplayName();
	orderId = dataFlowObject.getOrderId();
	toolAssigmentId = dataFlowObject.getToolAssigmentId() == null ? null
		: dataFlowObject.getToolAssigmentId().toString();
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

    public Long getDataFlowObjectId() {
	return dataFlowObjectId;
    }

    public void setDataFlowObjectId(Long dataFlowObjectId) {
	this.dataFlowObjectId = dataFlowObjectId;
    }

    public String getToolAssigmentId() {
	return toolAssigmentId;
    }

    public void setToolAssigmentId(String toolAssigmentId) {
	this.toolAssigmentId = toolAssigmentId;
    }
}