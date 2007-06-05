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

import mx.containers.*
import mx.managers.*
import mx.utils.*

import org.lamsfoundation.lams.common.layout.*

/**
* LFLayoutItem - Layout item (MC component) used in a LFLayout
* @author   Mitchell Seaton
*  
*/
class LFLayoutItem{

	private var _component:Object;
	private var _componentName:String;
   
    //Constructor
    function LFLayoutItem(name:String, component:Object) {
		
		if(component instanceof MovieClip) {
			_component = component;
		} else {
			_component = component.view;
		}
		
		componentName = name;
    }
	
	/**  
	 * @return  Return the MC or Class component.
	 */
	
	public function get component():Object {
		return _component;
	}
	
	/**
	 * @param   a	Set the MC component. 
	 */
	
	public function set component(a:Object) {
		_component = a;
	}
	
	public function get componentName():String {
		return _componentName;
	}
	
	public function set componentName(a:String) {
		_componentName = a;
	}
	
	public function get x():Number {
		return _component._x;
	}
	
	public function get y():Number {
		return _component._y;
	}
	
	public function get depth():Number {
		return _component.getDepth();
	}
	
	public function move(x:Number, y:Number) {
		_component._x = x;
		_component._y = y;
	}
}