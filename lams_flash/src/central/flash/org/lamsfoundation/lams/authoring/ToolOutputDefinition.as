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
class ToolOutputDefinition  {
	
	public static var LONG:String = "OUTPUT_LONG";
	public static var BOOL:String = "OUTPUT_BOOLEAN";
	public static var USER_DEFINED:String = "OUTPUT_SET_BOOLEAN";
	public static var COMPLEX:String = "OUTPUT_COMPLEX";
	
	private var _name:String;
	private var _description:String;
	private var _type:String;
	private var _startValue:Object;
	private var _endValue:Object;
	private var _complexDefinition:Object;
	private var _defaultConditions:Array;
	private var _showConditionNameOnly:Boolean;
	
	function ToolOutputDefinition(){
		_defaultConditions = new Array();
	}
	
	public function populateFromDTO(dto:Object):Void {
		_name = dto.name;
		_description = dto.description;
		_type = dto.type;
		_startValue = dto.startValue;
		_endValue = dto.endValue;
		if(dto.defaultConditions) {
			for(var i=0; i<dto.defaultConditions.length; i++) {
				var bdto:ToolOutputCondition = new ToolOutputCondition();
				bdto.populateFromDTO(dto.defaultConditions[i]);
				
				_defaultConditions.push(bdto);
			}
		}
		
		_showConditionNameOnly = dto.showConditionNameOnly;
			
	}
	
	public function toData():Object {
		var dto = new Object();
		
		if(_name) dto.name = _name;
		if(_description) dto.description = _description;
		if(_type) dto.type = _type;
		if(_startValue) dto.startValue = _startValue;
		if(_endValue) dto.endValue = _endValue;
		
		return dto;
	}
	
	public function set name(a:String) {
		_name = a;
	}
	
	public function get name():String {
		return _name;
	}
	
	public function set description(a:String) {
		_description = a;
	}
	
	public function get description():String {
		return _description;
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
	
	public function get complexDefinition():Object {
		return _complexDefinition;
	}
	
	public function get defaultConditions():Array {
		return _defaultConditions;
	}
	
	public function get showConditionNameOnly():Boolean {
		return _showConditionNameOnly;
	}

}