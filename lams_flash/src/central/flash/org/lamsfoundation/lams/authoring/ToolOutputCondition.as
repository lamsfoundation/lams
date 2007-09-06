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
import org.lamsfoundation.lams.common.Config;

/**
 *
 * @author Mitchell Seaton
 * @version 2.1
 **/
class ToolOutputCondition   {
	
	private var _conditionID:Number;
	private var _conditionUIID:Number;
	
	private var _orderID:Number;
	private var _name:String;
	private var _display_name:String;
	private var _type:String;
	private var _startValue:Object;
	private var _endValue:Object;
	private var _exactMatchValue:Object;
	
	private var _branchingActivity:Activity;
	
	function ToolOutputCondition(conditionID:Number, conditionUIID:Number, name:String, type:String, startValue:Object, endValue:Object, exactMatchValue:Object, displayName:String, toolContentID:Number){
		_conditionID = conditionID;
		_conditionUIID = conditionUIID;
		_orderID = 1;
		_type = type;
		_name = name;
		_startValue = startValue;
		_endValue = endValue;
		_exactMatchValue = exactMatchValue;
		
		_display_name = displayName;
	}
	
	public static function createBoolCondition(UIID:Number, definition:ToolOutputDefinition, value:Boolean):ToolOutputCondition {
		var condition:ToolOutputCondition = new ToolOutputCondition();
		condition.conditionUIID = UIID;
		
		if(definition.type == ToolOutputDefinition.BOOL) {
			condition.type = definition.type;
			condition.name = definition.name;
			condition.displayName = definition.type + " (" + String(value) + ") ";
			condition.exactMatchValue = value;
			
		}
		
		return condition;
	}
	
	public static function createLongCondition(UIID:Number, displayName:String, definition:ToolOutputDefinition, startValue:Number, endValue:Number):ToolOutputCondition {
		var condition:ToolOutputCondition = new ToolOutputCondition();
		condition.conditionUIID = UIID;
		
		if(definition.type == ToolOutputDefinition.LONG) {
			condition.name = definition.name;
			condition.type = definition.type;
			condition.displayName = displayName;
			
			if(startValue == endValue) {
				condition.exactMatchValue = startValue;
			} else { 
				condition.startValue = startValue;
				condition.endValue = endValue;
			}
		}
		
		return condition;
	}
	
	public function populateFromDTO(dto:Object):Void {
		_conditionID = dto.conditionID;
		_conditionUIID = dto.conditionUIID;
		
		_orderID = dto.orderID;
		_name = dto.name;
		_display_name = dto.displayName;
		_type = dto.type;
		_startValue = dto.startValue;
		_endValue = dto.endValue;
		_exactMatchValue = dto.exactMatchValue;
	}
	
	public function toData():Object {
		var dto:Object = new Object();
		
		if(_conditionID) dto.conditionID = conditionID;
		if(_conditionUIID) dto.conditionUIID = _conditionUIID;
		
		if(_orderID) dto.orderID = _orderID;
		if(_name) dto.name = _name;
		if(_display_name) dto.displayName = _display_name;
		if(_type) dto.type = _type;
		
		dto.startValue = (_startValue) ? _startValue : Config.STRING_NULL_VALUE;
		dto.endValue = (_endValue)  ? _endValue : Config.STRING_NULL_VALUE;
		dto.exactMatchValue = (_exactMatchValue) ? _exactMatchValue : Config.STRING_NULL_VALUE;
		
		return dto;
	}
	
	public function set conditionID(a:Number) {
		_conditionID = a;
	}
	
	public function get conditionID():Number {
		return _conditionID;
	}
	
	public function set conditionUIID(a:Number) {
		_conditionUIID = a;
	}
	
	public function get conditionUIID():Number {
		return _conditionUIID;
	}
	
	public function set name(a:String) {
		_name = a;
	}
	
	public function get name():String {
		return _name;
	}
	
	public function set displayName(a:String) {
		_display_name = a;
	}
	
	public function get displayName():String {
		return _display_name;
	}
	
	public function set type(a:String) {
		_type = a;
	}
	
	public function get type():String {
		return _type;
	}
	
	public function set startValue(a:Object) {
		_startValue = a;
	}
	
	public function get startValue():Object {
		return _startValue;
	}
	
	public function set endValue(a:Object) {
		_endValue = a;
	}
	
	public function get endValue():Object {
		return _endValue;
	}
	
	public function set exactMatchValue(a:Object) {
		_exactMatchValue = a;
	}
	
	public function get exactMatchValue():Object {
		return _exactMatchValue;
	}
	
	public function set orderID(a:Number) {
		_orderID = a;
	}
	
	public function get orderID():Number {
		return _orderID;
	}


}