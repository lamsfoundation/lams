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
import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import mx.managers.DepthManager;



/**  
* Cursor  
* Used to set alternitive cursors to the UI
*/  
class Cursor {  
	private static var _cursors:Array = new Array();
	private static var _bk_cursors:Array = new Array();
	private static var _current:String = new String();
    //Declarations  
    //Constructor  
  function Cursor() {  
		
  }
  
  
    
	 
	public static function addCursor(id:String,aCursor_mc:MovieClip):Void{
		//Application.cursor.
		//var _cursor = _root.attachMovie(id,id+'_mc',DepthManager.kCursor);
		//Debugger.log('Test cursor id:' + id + '(' + _cursor + ')',Debugger.GEN,'addCursor','Cursor');
		
		//var cursor_mc:MovieClip = ApplicationParent.ccursor.createChildAtDepth(id, DepthManager.kCursor);
		
		//var cursor_mc:MovieClip = DepthManager.createObjectAtDepth(id, DepthManager.kCursor);
		//cursor_mc._visible = false;
		//_cursors[id]=cursor_mc;
		
		updateOrCreateCursorRef(id, ApplicationParent.ccursor, DepthManager.kCursor);
		
		Debugger.log('Adding cursor ID:'+id+'('+_cursors[id]+')',Debugger.GEN,'addCursor','Cursor');
	}
	
	public static function removeCursor(id:String):Void{
		 
		 
		
	}
	
	/**
	 * Switches active coursor to the string specified
	 * @usage   
	 * @param   id String Identifier - movieclip in the library linkage name. 
	 * 			Stored as static constants E.g: Aplication.C_HOURGLASS
	 * @return  
	 */
	public static function showCursor(id:String, target:MovieClip):Void{
		Debugger.log('ID:'+id,Debugger.GEN,'showCursor','Cursor');
		//TODO: Disable the UI when wait cursor is shown
		
		if(id=='default'){
			stopDrag();
			Mouse.show();
			_cursors[_current]._visible = false;
		}else{
			Mouse.hide();
			_cursors[_current]._visible = false;
			
			if(_current != id) {
			
				if(target != null || target != undefined) {
					updateOrCreateCursorRef(id, target, target._parent.getNextHighestDepth());
				} else {
					updateOrCreateCursorRef(id, ApplicationParent.ccursor, DepthManager.kCursor);
				}
			}
			
			startDrag(_cursors[id], true);
			_cursors[id]._visible = true;
		}
		_current = id;
	}
	
	public static function updateOrCreateCursorRef(cursor_id:String, target:MovieClip, depth:Number) {
		Debugger.log('cursor_id:'+cursor_id + " target: " + target + " depth: " + depth,Debugger.GEN,'updateOrCreateCursorRef','Cursor');
		if(target != ApplicationParent.ccursor) {
			_bk_cursors[cursor_id] = _cursors[cursor_id];
		}
		
		var cursor_mc = target.createChildAtDepth(cursor_id, depth);
		
		cursor_mc._visible = false;
		_cursors[cursor_id] = cursor_mc;
		
		Debugger.log('cursor_mc: '+ cursor_mc,Debugger.GEN,'updateOrCreateCursorRef','Cursor');
		
	
	}
		
	/**
	 * Returns current cursor
	 * @usage   
	 * @return  String name of current cursor
	 */
	public static function getCurrentCursor():String{
		return _current;
	}
	
	/**
	 * Returns current cursor, if cursor is the mouse, then null is returned
	 * @usage   
	 * @return  Reference of current cursor
	 */
	public static function getCurrentCursorRef():MovieClip{
		if(_current == "default"){
			return null;
		}else{
			return _cursors[_current];
		}
	}
	
	
	
	
	
	
	
	
	
}