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

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SortComparator;

/**
 * Different type of transition - does not indicate lesson progress, but rather data flow between tools.
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@DiscriminatorValue("1")
public class DataTransition extends Transition {
    private static final long serialVersionUID = -6508807398165476228L;

    /**
     * Tool output definitions which can be later used in the target tool.
     */
    @OneToMany(mappedBy = "dataTransition", cascade = CascadeType.ALL)
    @SortComparator(DataFlowObjectComparator.class)
    private Set<DataFlowObject> dataFlowObjects = new TreeSet<DataFlowObject>(new DataFlowObjectComparator());

    public DataTransition() {
	super();
	transitionType = Transition.DATA_TRANSITION_TYPE;
    }

    public DataTransition(Long transitionId, Date createDateTime, Activity toActivity, Activity fromActivity,
	    LearningDesign learningDesign) {
	super(transitionId, createDateTime, toActivity, fromActivity, learningDesign);
	transitionType = Transition.DATA_TRANSITION_TYPE;
    }

    public DataTransition(Long transitionId, Integer id, String description, String title, Date createDateTime,
	    Activity toActivity, Activity fromActivity, LearningDesign learningDesign, Integer toUIID,
	    Integer fromUIID) {
	super(transitionId, id, description, title, createDateTime, toActivity, fromActivity, learningDesign, toUIID,
		fromUIID);
	transitionType = Transition.DATA_TRANSITION_TYPE;
    }

    public Set<DataFlowObject> getDataFlowObjects() {
	return dataFlowObjects;
    }

    public void setDataFlowObjects(Set<DataFlowObject> dataFlowObjects) {
	this.dataFlowObjects = dataFlowObjects;
    }
}