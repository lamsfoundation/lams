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
class ToolOutputCondition   {
	
	private var _conditionID:Number;
	private var _conditionUIID:Number;
	
	private var _orderId:Number;
	private var _name:String;
	private var _type:String;
	private var _startValue:Object;
	private var _endValue:Object;
	private var _exactMatchValue:Object;
	
	function ToolOutputCondition(conditionID:Number, conditionUIID:Number, name:String, type:String, startValue:Object, endValue:Object, exactMatchValue:Object){
		_conditionID = conditionID;
		_conditionUIID = conditionUIID;
		_orderId = 1;
		_name = name;
		_startValue = startValue;
		_endValue = endValue;
		_exactMatchValue = exactMatchValue;
	}
	
	public function addConditionData(dto:Object):Object {
		
		if(_orderId) dto.orderId = _orderId;
		if(_name) dto.name = _name;
		if(_type) dto.type = _type;
		if(_startValue) dto.startValue = _startValue;
		if(_endValue) dto.endValue = _endValue;
		if(_exactMatchValue) dto.exactMatchValue = _exactMatchValue;
		
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
	
	public function set orderId(a:Number) {
		_orderId = a;
	}
	
	public function get orderId():Number {
		return _orderId;
	}


}