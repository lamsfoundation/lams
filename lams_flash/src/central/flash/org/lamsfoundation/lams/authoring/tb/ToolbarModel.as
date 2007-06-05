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

import org.lamsfoundation.lams.authoring.tb.*;
import org.lamsfoundation.lams.common.util.*;


/*
* Model for the Toolbar
*/
class ToolbarModel extends Observable {
    
	private var _tv:Toolbar;
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	private var _btnState:Boolean = false;
	private var _mode:Number;
    
	/**
	* Constructor.
	*/
	public function ToolbarModel (tv:Toolbar, mode:Number){
		_tv = tv;
		_mode = mode;
	}
    
    /**
    * set the size on the model, this in turn will set a changed flag and notify observers (views)
    * @param width - Tookit width
    * @param height - Toolkit height
    */
    public function setSize(width:Number,height:Number) {
		__width = width;
		__height = height;
		setChanged();
		//send an update
		infoObj = {};
		infoObj.updateType = "SIZE";
		notifyObservers(infoObj);
    }
    
	
	public function toolbarButtons(){
		var buttonArr:Array = new Array();
		
		if(mode != Toolbar.EDIT_MODE) {
		
			buttonArr[0] 	= ["new_btn", "icon_newDesign"];
			buttonArr[1] 	= ["open_btn", "icon_openDesign"];
			buttonArr[2] 	= ["save_btn", "icon_saveDesign"];
			buttonArr[3] 	= ["copy_btn", "icon_copy"];
			buttonArr[4] 	= ["paste_btn", "icon_paste"];
			buttonArr[5] 	= ["trans_btn", "icon_pen"];
			buttonArr[6] 	= ["optional_btn", "icon_optional"];
			buttonArr[7] 	= ["flow_btn", "icon_flow"];
			buttonArr[8] 	= ["group_btn", "icon_group"];
			buttonArr[9] 	= ["preview_btn", "icon_preview"];
			buttonArr[10] 	= ["gate_btn", "icon_gate"];
			buttonArr[11] 	= ["branch_btn", "icon_branch"];
			
		} else {
			buttonArr[0] 	= ["apply_changes_btn", "icon_saveDesign"];
			buttonArr[1] 	= ["cancel_btn", "icon_cancel"];
			buttonArr[2] 	= ["spacer", null];
			buttonArr[3] 	= ["copy_btn", "icon_copy"];
			buttonArr[4] 	= ["paste_btn", "icon_paste"];
			buttonArr[5] 	= ["trans_btn", "icon_pen"];
			buttonArr[6] 	= ["optional_btn", "icon_optional"];
			buttonArr[7] 	= ["flow_btn", "icon_flow"];
			buttonArr[8] 	= ["group_btn", "icon_group"];
			buttonArr[9] 	= ["gate_btn", "icon_gate"];
			buttonArr[10] 	= ["branch_btn", "icon_branch"];
		
		}
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SETMENU";
		infoObj.data = buttonArr;
		notifyObservers(infoObj);
		
		
	}
	/**
	* set the button state to enabled/disabled and set a changed flag and notify view and controller.
    */
	
	public function setDefaultState(){
		Debugger.log('setDefaultState is called: ',Debugger.GEN,'setDefaultState','Toolbar');
		setButtonState("preview", false)
	}

	/**
	 * 
	 * @usage   
	 * @param   btnName  
	 * @param   btnState 
	 * @return  
	 */
	public function setButtonState(btnName:String, btnState:Boolean){
		Debugger.log('button name in setButtonState is : '+btnName, Debugger.GEN,'setButtonState','ToolbarModel');		
		
		setChanged();
		infoObj = {};
		infoObj.updateType = "BUTTON";
		infoObj.button = btnName
		infoObj.buttonstate = btnState
		notifyObservers(infoObj);
		
	}
	
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getSize():Object{
		var s:Object = {};
		s.w = __width;
		s.h = __height;
		return s;
	}  
	
	/**
	* Used by View to get the button state enabled/disabled
	* @returns Object containing true or false .  obj.newbtnstate
	*/
	
	public function getState():Object{
		var s:Object = {};
		s.newbtnstate = _btnState;
		return s;
	}  
    
	/**
    * sets the model x + y vars
	*/
	public function setPosition(x:Number,y:Number):Void{
        //Set state variables
		__x = x;
		__y = y;
        //Set flag for notify observers
		setChanged();
        
		//build and send update object
		infoObj = {};
		infoObj.updateType = "POSITION";
		notifyObservers(infoObj);
	}  

	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getPosition():Object{
		var p:Object = {};
		p.x = x;
		p.y = y;
		return p;
	}  
    
    //Acessors for x + y coordinates
    public function get x():Number{
        return __x;
    }
    
    public function get y():Number{
        return __y;
    }

    //Acessors for w + h
    public function get width():Number{
        return __width;
    }
    
    public function get mode():Number{
        return _mode;
    }    
    
    public function set mode(a:Number){
        _mode = a;
    }
    
    public function get height():Number{
        return __height;
    }    
    
	
	public function getToolbar():Toolbar{
		return _tv;
	}
}
