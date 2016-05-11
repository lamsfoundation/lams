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

/**
 * Models the relationship between a group and a branch for Tool Output Based based branching.
 * The branchCondition indicates the condition on which the related group should be created.
 * During authoring, we expect branchCondition to be not null, and the group to be null.
 *
 * @author Fiona Malikoff
 * @version 2.1
 * 
 */
public class ToolOutputBranchActivityEntryDTO extends BranchActivityEntryDTO {

    private BranchConditionDTO condition;

    public BranchConditionDTO getCondition() {
	return condition;
    }

    public void setCondition(BranchConditionDTO condition) {
	this.condition = condition;
    }

}
