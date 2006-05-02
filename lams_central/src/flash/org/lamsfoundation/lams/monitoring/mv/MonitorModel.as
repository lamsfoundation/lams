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

import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.util.*;

/*
* Model for the Monitoring Tabs 
*/
class MonitorModel extends Observable{
	private var _className:String = "MonitorModel";
   
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	
	private var _monitor:Monitor;
	
	// add model data
	private var _activeSeq:Sequence;
	private var _org:Organisation;
	private var _todos:Array;  // Array of ToDo ContributeActivity(s)
	// state data
	private var _showLearners:Boolean;
	
	/**
	* Constructor.
	*/
	public function MonitorModel (monitor:Monitor){
		_monitor = monitor;
		_showLearners = true;
	}
	
	// add get/set methods
	
	public function setSequence(activeSeq:Sequence){
		_activeSeq = activeSeq;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SEQUENCE";
		infoObj.tabID = getMonitor().getMV().getMonitorTab().selectedIndex;
		notifyObservers(infoObj);
	}
	
	public function getSequence():Sequence{
		return _activeSeq;
	}
	
	public function setOrganisation(org:Organisation){
		_org = org;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "ORG";
		notifyObservers(infoObj);
	}
	
	public function getOrganisation():Organisation{
		return _org;
	}
	
	
	
	public function setToDos(todos:Array){
		
		_todos = new Array();
		
		for(var i=0; i< todos.length; i++){
			var t:Object = todos[i];
			var todo:ContributeActivity = new ContributeActivity();
			todo.populateFromDTO(t);
			_todos.push(todo);
		}
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "TODOS";
		notifyObservers(infoObj);
	}
	
	public function getToDos():Array{
		return _todos;
	}
	
	public function showLearners(){
		_showLearners = true;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SHOW_LEARNERS";
		notifyObservers(infoObj);
	}
	
	public function hideLearners(){
		_showLearners = false;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SHOW_LEARNERS";
		notifyObservers(infoObj);
	}
	
	public function isShowLearners():Boolean{
		return _showLearners;
	}
	
	public function setSize(width:Number,height:Number) {
		__width = width;
		__height = height;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SIZE";
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
		p.x = __x;
		p.y = __y;
		return p;
	}  
	
	//Accessors for x + y coordinates
    public function get x():Number{
        return __x;
    }
    
    public function get y():Number{
        return __y;
    }

    //Accessors for x + y coordinates
    public function get width():Number{
        return __width;
    }
    
    public function get height():Number{
        return __height;
    }
	
	public function get className():String{
        return 'MonitorModel';
    }
	
	public function getMonitor():Monitor{
		return _monitor;
	}
	
}
