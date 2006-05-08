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
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.Transition;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.util.*;

/*
* Model for the Monitoring Tabs 
*/
class MonitorModel extends Observable{
	private var _className:String = "MonitorModel";
   
   public var RT_FOLDER:String = "Folder";
   public var RT_ORG:String = "Organisation";
   
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
	
	//these are hashtables of mc refs MOVIECLIPS (like CanvasActivity or CanvasTransition)
	//each on contains a reference to the emelment in the ddm (activity or transition)
	private var _activitiesDisplayed:Hashtable;
	private var _transitionsDisplayed:Hashtable;
	
	//this is the dataprovider for the org tree
	private var _treeDP:XML;
	private var _orgResources:Array;	
	private var _orgs:Array;
	private var _selectedTreeNode:XMLNode;
	/**
	* Constructor.
	*/
	public function MonitorModel (monitor:Monitor){
		_monitor = monitor;
		_showLearners = true;
		_activitiesDisplayed = new Hashtable("_activitiesDisplayed");
		_transitionsDisplayed = new Hashtable("_transitionsDisplayed");

		_orgResources = new Array();
	}
	
	// add get/set methods
	
	public function setSequence(activeSeq:Sequence){
		_activeSeq = activeSeq;
		_monitor.openLearningDesign(_activeSeq)
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
	
	public function saveOrgs(orgs:Array){
		_orgs = orgs;
	}
	
	public function getOrgs():Array{
		return _orgs;
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
		infoObj.updateType = "HIDE_LEARNERS";
		notifyObservers(infoObj);
	}
	
	public function isShowLearners():Boolean{
		return _showLearners;
	}
	
	/**
	 * get the design in the DesignDataModel and update the Monitor Model accordingly.
	 * NOTE: Design elements are added to the DDM here.
	 * 
	 * @usage   
	 * @return  
	 */
	public function drawDesign(tabID:Number){
		
		//porobbably need to get a bit more granular
		//go through the design and get the activities and transitions 
		var indexArray:Array;
		var dataObj:Object;
		var ddmActivity_keys:Array = _monitor.ddm.activities.keys();
			
		
		
		indexArray = ddmActivity_keys;
		trace("Length of Activities in DDM: "+indexArray.length)
		
		//loop through and do comparison
		for(var i=0;i<indexArray.length;i++){
					
			var keyToCheck:Number = indexArray[i];
			
			
			var ddm_activity:Activity = _monitor.ddm.activities.get(keyToCheck);
			
			broadcastViewUpdate("DRAW_ACTIVITY",ddm_activity, tabID);
			//dataObj.activity = ddm_activity;
		}
		
		//now check the transitions:
		var ddmTransition_keys:Array = _monitor.ddm.transitions.keys();
				
		//chose which array we are going to loop over
		var trIndexArray:Array;
		trIndexArray = ddmTransition_keys;
		
		//loop through and do comparison
		for(var i=0;i<trIndexArray.length;i++){
			
			var transitionKeyToCheck:Number = trIndexArray[i];

			var ddmTransition:Transition = _monitor.ddm.transitions.get(transitionKeyToCheck);
			
			//NOTE!: we are passing in a ref to the tns in the ddm so if we change any props of this, we are changing the ddm
			broadcastViewUpdate("DRAW_TRANSITION",ddmTransition, tabID);	
			//dataObj.trans = ddmTransition;
		}
		
	}
	
	public function broadcastViewUpdate(updateType, data, tabID){
		//getMonitor().getMV().clearView();
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = updateType;
		infoObj.drawData = data;
		infoObj.tabID = tabID;
		notifyObservers(infoObj);
		
	}
	
	
	public function changeTab(tabID:Number){
		//getMonitor().getMV().clearView();
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "TABCHANGE";
		infoObj.tabID = tabID;
		notifyObservers(infoObj);
		
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
	
	/**
	 * Sets up the tree for the 1st time
	 * 
	 * @usage   
	 * @return  
	 */
	public function initOrganisationTree(){
		_treeDP = new XML();
		_orgResources = new Array();
		//add top level
		//create the data obj:
		var mdto= {};
		mdto.organisationID = null;
		mdto.description = "";
		// use Dictionary.getValue
		mdto.name = "Classes";
		mdto.parentID = null;
		mdto.resourceID="-1";
		
		
		var rootNode = _treeDP.addTreeNode(mdto.name,mdto);
		rootNode.attributes.isBranch = true;
		setOrganisationResource(RT_ORG+'_'+mdto.resourceID,rootNode);
		
	}
	
	/**
	 * 
	 * @usage   
	 * @param   neworgResources 
	 * @return  
	 */
	public function setOrganisationResource(key:String,neworgResources:XMLNode):Void {
		Debugger.log(key+'='+neworgResources,Debugger.GEN,'setOrganisationResource','org.lamsfoundation.lams.monitoring.mv.MonitorModel');
		_orgResources[key] = neworgResources;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getOrganisationResource(key:String):XMLNode{
		Debugger.log(key+' is returning '+_orgResources[key],Debugger.GEN,'getOrganisationResource','org.lamsfoundation.lams.monitoring.mv.MonitorModel');
		return _orgResources[key];
		
	}

	
	public function get treeDP():XML{
		return _treeDP;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newselectedTreeNode 
	 * @return  
	 */
	public function setSelectedTreeNode (newselectedTreeNode:XMLNode):Void {
		_selectedTreeNode = newselectedTreeNode;
		//dispatch an update to the view
		broadcastViewUpdate('ITEM_SELECTED',_selectedTreeNode);
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getSelectedTreeNode ():XMLNode {
		return _selectedTreeNode;
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

	/**
	 * Returns a reference to the Activity Movieclip for the UIID passed in.  Gets from _activitiesDisplayed Hashable
	 * @usage   
	 * @param   UIID 
	 * @return  Activity Movie clip
	 */
	public function getActivityMCByUIID(UIID:Number):MovieClip{
		
		var a_mc:MovieClip = _activitiesDisplayed.get(UIID);
		//Debugger.log('UIID:'+UIID+'='+a_mc,Debugger.GEN,'getActivityMCByUIID','CanvasModel');
		return a_mc;
	}
	
	public function get activitiesDisplayed():Hashtable{
		return _activitiesDisplayed;
	}
	
	public function get transitionsDisplayed():Hashtable{
		return _transitionsDisplayed;
	}	

	public function getMonitor():Monitor{
		return _monitor;
	}
	
}
