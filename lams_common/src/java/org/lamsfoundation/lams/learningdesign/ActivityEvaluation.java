/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

/**
 * This is a POJO bean object for an evaluation criteria for a given activity.
 * This object exists at the learning design level. Ie it can be designed,
 * copied and exported in author.
 *
 * @author lfoxton
 */
public class ActivityEvaluation {
    private Long activityId;
    private ToolActivity activity;
    private String toolOutputDefinition;
    private Integer weight;

    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public ToolActivity getActivity() {
	return activity;
    }

    public void setActivity(ToolActivity activity) {
	this.activity = activity;
    }

    public String getToolOutputDefinition() {
	return toolOutputDefinition;
    }

    public void setToolOutputDefinition(String toolOutputDefinition) {
	this.toolOutputDefinition = toolOutputDefinition;
    }

    public Integer getWeight() {
	return weight;
    }

    public void setWeight(Integer weight) {
	this.weight = weight;
    }
}