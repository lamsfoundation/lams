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
 
/**
* Util methods for string manipulation
* @class	State
* @author	Daniel Carlier
* @decription A generic state representation class
*/
class org.lamsfoundation.lams.common.util.ObjState {
	
	private var _displayText:String;
	private var _objState:Object;
	
	function ObjState(displayText:String, objState:Object){
		this.displayText = displayText;
		this.objState = objState;
	}
	
	public function get displayText():String {
		return _displayText;
	}
	
	public function set displayText(s:String):Void {
		_displayText = s;
	}
	
	public function get objState():Object {
		return _objState;
	}
	
	public function set objState(s:Object):Void {
		_objState = s;
	}
}