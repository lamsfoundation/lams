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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.Authoring.*
import mx.managers.DepthManager;



/**  
* Cursor  
* Used to set alternitive cursors to the UI
*/  
class Cursor {  
	private static var _cursors:Array = new Array();;
	private static var _current:String = new String();;
    //Declarations  
    //Constructor  
  function Cursor() {  
		
  }
  
  
    
	 
	public static function addCursor(id:String,aCursor_mc:MovieClip):Void{
		//Application.cursor.

		var cursor_mc:MovieClip = DepthManager.createObjectAtDepth(id, DepthManager.kCursor);
		cursor_mc._visible = false;
		_cursors[id]=cursor_mc;
		Debugger.log('Adding cursor ID:'+id+'('+cursor_mc+')',Debugger.GEN,'addCursor','Cursor');
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
	public static function showCursor(id:String):Void{
		Debugger.log('ID:'+id,Debugger.GEN,'showCursor','Cursor');
		//TODO: Disable the UI when wait cursor is shown
		
		if(id=='default'){
			stopDrag();
			Mouse.show();
			_cursors[_current]._visible = false;
		}else{
			Mouse.hide();
			_cursors[_current]._visible = false;
			_cursors[id]._visible = true;
			startDrag(_cursors[id], true);
		}
		_current = id;
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