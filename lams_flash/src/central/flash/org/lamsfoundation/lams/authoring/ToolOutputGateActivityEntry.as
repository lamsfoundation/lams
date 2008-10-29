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

import org.lamsfoundation.lams.authoring.*; // imports in super classes prob not needed
import org.lamsfoundation.lams.common.util.Debugger; // remove
import org.lamsfoundation.lams.common.dict.Dictionary;

/**
 *
 * @author Daniel Carlier
 * @version 2.1.1
 **/
class ToolOutputGateActivityEntry extends GateActivityEntry {
	
	private var _condition:ToolOutputCondition;
	private var _gateOpenWhenConditionMet:Boolean;

	function ToolOutputGateActivityEntry(entryID:Number, entryUIID:Number, gateActivity:GateActivity, condition:ToolOutputCondition, gateOpenWhenConditionMet:Boolean){
		super(entryID, entryUIID, gateActivity);
		
		this.condition = condition;
		this.condition.gateActivity = gateActivity;
		
		this.gateOpenWhenConditionMet = gateOpenWhenConditionMet;
	}

	public function toData():Object {
		var dto:Object = new Object();
		
		if(this.entryID) dto.entryID = this.entryID;
		if(this.entryUIID) dto.entryUIID = this.entryUIID;
		if(this.gateActivity) dto.gateActivityUIID = this.gateActivity.activityUIID;
		if(this.condition) dto.condition = _condition.toData(dto);
		if(this.gateOpenWhenConditionMet != null) dto.gateOpenWhenConditionMet = this.gateOpenWhenConditionMet;
		
		return dto;
	}
	
	public function get condition():ToolOutputCondition {
		return _condition;
	}
	
	public function set condition(a:ToolOutputCondition):Void {
		_condition = a;
	}
	
	/*
	* @return _gateOpenWhenConditionMet Boolean value to see if the gate should be open when this condition is met
	*/
	public function get gateOpenWhenConditionMet():Boolean {
		return _gateOpenWhenConditionMet;
	}
	
	public function set gateOpenWhenConditionMet(a:Boolean):Void {
		_gateOpenWhenConditionMet = a;
	}
	
	public function get displayName():String {
		return _condition.displayName;
	}
	
	public function get gateStateDisplayText():String {
		return (gateOpenWhenConditionMet) ? Dictionary.getValue("gate_open") : Dictionary.getValue("gate_closed");
	}
}