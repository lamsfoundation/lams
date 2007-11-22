/***************************************************************************
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
 * ************************************************************************
 */

import org.lamsfoundation.lams.authoring.*;

/**
 *
 * @author Mitchell Seaton
 * @version 2.1
 **/
class ToolOutputBranchActivityEntry extends BranchActivityEntry {
	
	private var _condition:ToolOutputCondition;
	
	function ToolOutputBranchActivityEntry(entryID:Number, entryUIID:Number, condition:ToolOutputCondition, sequenceActivity:SequenceActivity, branchingActivity:BranchingActivity){
		this.entryID = entryID;
		this.entryUIID = entryUIID;
		this.sequenceActivity = sequenceActivity;
		this.branchingActivity = branchingActivity;
		
		this.condition = condition;
		this.condition.branchingActivity = branchingActivity;
	}

	public function toData():Object {
		var dto:Object = new Object();
		
		if(this.entryID) dto.entryID = this.entryID;
		if(this.entryUIID) dto.entryUIID = this.entryUIID;
		if(this.sequenceActivity) dto.sequenceActivityUIID = this.sequenceActivity.activityUIID;
		if(this.branchingActivity) dto.branchingActivityUIID = this.branchingActivity.activityUIID;
		
		if(this.condition) dto.condition = _condition.toData(dto);
		
		return dto;
	}
	
	public function get condition():ToolOutputCondition {
		return _condition;
	}
	
	public function set condition(a:ToolOutputCondition):Void {
		_condition = a;
	}
	
	public function get displayName():String {
		return _condition.displayName;
	}
	
}