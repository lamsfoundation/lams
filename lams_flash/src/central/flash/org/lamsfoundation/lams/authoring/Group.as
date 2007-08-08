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
class Group {
	
	private var _parentID:Number;
	private var _groupID:Number;
	private var _groupUIID:Number;
	private var _groupName:String;
	private var _orderID:Number;
	
	function Group(parentID, groupID, groupUIID, groupName, orderID){
		_parentID = parentID;
		_groupID = groupID;
		_groupUIID = groupUIID;
		_groupName = groupName;
		_orderID = orderID;
	}
	
	public function toData():Object {
		var dto:Object = new Object();
		
		if(_groupID) dto.groupID = _groupID;
		if(_groupUIID) dto.groupUIID = _groupUIID;
		if(_groupName) dto.groupName = _groupName;
		if(_orderID) dto.orderID = _orderID;
		
		return dto;
	}
	
	public function get parentID():Number {
		return _parentID;
	}
	
	public function set parentID(a:Number) {
		_parentID = a;
	}
	
	public function get groupID():Number {
		return _groupID;
	}
	
	public function set groupID(a:Number) {
		_groupID = a;
	}
	
	public function get groupUIID():Number {
		return _groupUIID;
	}
	
	public function set groupUIID(a:Number) {
		_groupUIID = a;
	}
	
	public function get groupName():String {
		return _groupName;
	}
	
	public function set groupName(a:String) {
		_groupName = a;
	}
	
	public function get orderID():Number {
		return _orderID;
	}

	public function set orderID(a:Number) {
		_orderID = a;
	}

}