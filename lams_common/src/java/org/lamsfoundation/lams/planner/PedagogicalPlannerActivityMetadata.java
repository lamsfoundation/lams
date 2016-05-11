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


package org.lamsfoundation.lams.planner;

import java.io.Serializable;

import org.lamsfoundation.lams.learningdesign.ToolActivity;

/**
 * Holds additional information about activities used in Pedagogical Planner only.
 *
 * @author Marcin Cieslak
 * @hibernate.class table="lams_planner_activity_metadata"
 */
public class PedagogicalPlannerActivityMetadata implements Serializable, Cloneable {

    private Long id;

    private ToolActivity activity;

    /**
     * Tells whether the activity should be collapsed in Pedagogical Planner
     */
    private Boolean collapsed;

    /**
     * Tells whether the activity should be expanded in Pedagogical Planner
     */
    private Boolean expanded;

    /**
     * HoTells whether the activity should be hidden in Pedagogical Planner
     */
    private Boolean hidden;

    /**
     * Hold editing advice for the activity.
     */
    private String editingAdvice;

    @Override
    public PedagogicalPlannerActivityMetadata clone() {
	PedagogicalPlannerActivityMetadata plannerMetadata = new PedagogicalPlannerActivityMetadata();
	plannerMetadata.setCollapsed(this.collapsed);
	plannerMetadata.setExpanded(this.expanded);
	plannerMetadata.setHidden(this.getHidden());
	plannerMetadata.setEditingAdvice(this.editingAdvice);
	return plannerMetadata;
    }

    public void copyProperties(PedagogicalPlannerActivityMetadata source) {
	this.collapsed = source.collapsed;
	this.expanded = source.expanded;
	this.hidden = source.hidden;
	this.editingAdvice = source.editingAdvice;
    }

    /**
     * @hibernate.id column="activity_id" generator-class="foreign" type="java.lang.Long"
     * @hibernate.generator-param name="property" value="activity"
     */
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @hibernate.property column="collapsed" type="java.lang.Boolean" length="1"
     */
    public Boolean getCollapsed() {
	return collapsed;
    }

    public void setCollapsed(Boolean plannerCollapsed) {
	this.collapsed = plannerCollapsed;
    }

    /**
     * @hibernate.property column="expanded" type="java.lang.Boolean" length="1"
     */
    public Boolean getExpanded() {
	return expanded;
    }

    public void setExpanded(Boolean plannerExpanded) {
	this.expanded = plannerExpanded;
    }

    /**
     * @hibernate.property column="hidden" type="java.lang.Boolean" length="1"
     */
    public Boolean getHidden() {
	return hidden;
    }

    public void setHidden(Boolean plannerHidden) {
	this.hidden = plannerHidden;
    }

    /**
     * @hibernate.property column="editing_advice" type="java.lang.String" length="255"
     */
    public String getEditingAdvice() {
	return editingAdvice;
    }

    public void setEditingAdvice(String editingAdvice) {
	this.editingAdvice = editingAdvice;
    }

    /**
     * @hibernate.one-to-one constrained="true" foreign-key="FK_lams_planner_metadata_primary"
     * @return
     */
    public ToolActivity getActivity() {
	return activity;
    }

    public void setActivity(ToolActivity activity) {
	this.activity = activity;
    }
}