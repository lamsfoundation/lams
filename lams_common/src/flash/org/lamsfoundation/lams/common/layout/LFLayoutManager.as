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

import org.lamsfoundation.lams.common.ApplicationParent
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.layout.*

/**
* LFLayoutManager - Layout manager for an Application
* @author   Mitchell Seaton
*  
*/
class LFLayoutManager implements ILayoutManager{

	private var _target:ApplicationParent;
	private var _layoutName:String;
	private var _layoutItems:Hashtable;
	
    //Constructor
    function LFLayoutManager(name:String) {
		_target = null;
		if(name)
			_layoutName = name;
			
		_layoutItems = new Hashtable("_layoutItems");
    }
	
	public function setup(target:ApplicationParent):Void {
		_target = target;
	}
	
	/**  
	 * @return  Return the target Application instance.
	 */
	
	public function get target():ApplicationParent {
		return _target;
	}
	
	/**
	 * @param   a	Set the target Application instance. 
	 */
	
	public function set target(a:ApplicationParent) {
		_target = a;
	}
	
	/**  
	 * @return  Return the layoutItems collection.
	 */
	
	public function get layoutItems():Hashtable {
		return _layoutItems;
	}
	
	/**
	 * @param   a 	Set the layoutItems.
	 */
	
	public function set layoutItems(a:Hashtable) {
		_layoutItems = a;
	}
	
	public function addLayoutItem(a:LFLayoutItem) {
		var item:LFLayoutItem = _layoutItems.get(a.componentName);
		if(item != null) {
			if(item.component == null) _layoutItems.put(a);
		}
	}
	
	public function completedLayout():Boolean {
		var items = _layoutItems.values();
		for(var i=0; i< items.length; i++) {
			if(items[i] == null) return false;
		}
		return true;
	}
	
	public function setEmptyLayoutItem(a:LFLayoutItem) {
		Debugger.log("Adding Layout Item: " + a.componentName, Debugger.GEN, "addLayoutItem", "LFLayoutManager");
		_layoutItems.put(a.componentName, null);
	}
	
	public function removeLayoutItem(a:LFLayoutItem) {
		_layoutItems.remove(a);
	}
	
	public function moveLayoutItem(a:Object, x:Number, y:Number) {
		var _item = _layoutItems.get(a);
		_item.x = x;
		_item.y = y;
	}
	
	public function getLayoutItem(a:Object) {
		_layoutItems.get(a);
	}
	
	public function getLayoutName():String {
		return _layoutName;
	}
	
	public function resize(w:Number, h:Number) {	}
}