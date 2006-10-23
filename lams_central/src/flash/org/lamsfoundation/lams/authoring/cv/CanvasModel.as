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

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.dict.*
import mx.events.*
/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.authoring.cv.CanvasModel extends Observable {
	
	public static var TRANSITION_TOOL:String = "TRANSITION";  //activie tool ID strings definition
	public static var OPTIONAL_TOOL:String = "OPTIONAL";
	public static var GATE_TOOL:String = "GATE";
	public static var GROUP_TOOL:String = "GROUP";
	private var _defaultGroupingTypeID;
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _piHeight:Number;
	private var infoObj:Object;
	
	
	private var _cv:Canvas;
	private var _ddm:DesignDataModel;
	private var optionalCA:CanvasOptionalActivity;
	//UI State variabls
	private var _isDirty:Boolean;
	private var _activeTool:String;
	private var _selectedItem:Object;  // the currently selected thing - could be activity, transition etc.
	private var _isDrawingTransition:Boolean;
	private var _transitionActivities:Array;
	private var _isDragging:Boolean;
	private var _importing:Boolean;
	private var _autoSaveWait:Boolean;

	
	//these are hashtables of mc refs MOVIECLIPS (like CanvasActivity or CanvasTransition)
	//each on contains a reference to the emelment in the ddm (activity or transition)
	private var _activitiesDisplayed:Hashtable;
	private var _transitionsDisplayed:Hashtable;

	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	/**
	* Constructor.
	*/
	public function CanvasModel (cv:Canvas){
		
		 //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		_cv = cv;
		_ddm = new DesignDataModel();
		_activitiesDisplayed = new Hashtable("_activitiesDisplayed");
		_transitionsDisplayed = new Hashtable("_transitionsDisplayed");
		
		
		
				
	
		_activeTool = null;
		_autoSaveWait = false;
		_transitionActivities = new Array();
		_defaultGroupingTypeID = Grouping.RANDOM_GROUPING;
	}

	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		__width = width;
		__height = height;
		
		/*
		//send an update
		setChanged();
		infoObj = {};
		infoObj.updateType = "SIZE";
		notifyObservers(infoObj);
		*/
		broadcastViewUpdate("SIZE");
		
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
	* Used by application to set the Position
	* @param x
	* @param y
	*/
	public function setPosition(x:Number, y:Number):Void{
		__x=x;
		__y=y;
		/*
		//send an update
		setChanged();
		infoObj = {};
		infoObj.updateType = "POSITION";
		notifyObservers(infoObj);
		*/
		broadcastViewUpdate("POSITION");
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
	
	public function setPIHeight(h:Number){
		trace ("height is set to: "+h)
		_piHeight = h
		Application.getInstance().onResize();
	}
	
	public function getPIHeight(){
		trace ("returning pi height: "+_piHeight)
		return _piHeight;
	}
	public function setDirty(){
		_isDirty = true;
		
		if(getCanvas().ddm.learningDesignID == undefined){
			LFMenuBar.getInstance().enableExport(false);
		} else {
			LFMenuBar.getInstance().enableExport(true);
		}
		
		/*
		//work out what we need to redraw.
		//for now lets just do a full re-draw
		//send an update
		setChanged();
		infoObj = {};
		infoObj.updateType = "DRAW_DESIGN";
		notifyObservers(infoObj);
		*/
		refreshDesign();
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////       TRANSITIONS         //////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts the transition tool
	 * @usage   
	 * @return  
	 */
	public function startTransitionTool():Void{
		Debugger.log('Starting transition tool',Debugger.GEN,'startTransitionTool','CanvasModel');			
		resetTransitionTool();
		_activeTool = TRANSITION_TOOL;
		//broadcastViewUpdate("START_TRANSITION_TOOL");
	}
	
	/**
	 * Stops it
	 * @usage   
	 * @return  
	 */
	 
	public function stopTransitionTool():Void{
		Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','CanvasModel');
		resetTransitionTool();
		_activeTool = null;
		//broadcastViewUpdate("STOP_TRANSITION_TOOL");
	}
	
	
	
	public function findOptionalActivities():Array{
		//_activitiesDisplayed
		//var _ddm.getActivityByUIID(Activity.OPTIONAL_ACTIVITY_TYPE)
		var actOptional:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		//trace("findOptionalActivities Called "+k.length )
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE){
				actOptional.push(k[i]);
				trace("find the Optional with id:"+k[i].activity.activityUIID )
			}
			
		}
		return actOptional
	}
	
	public function findParallelActivities():Array{
		
		var actParallel:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		//trace("findOptionalActivities Called "+k.length )
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				actParallel.push(k[i]);
				trace("find the Parallel with id:"+k[i].activity.activityUIID )
			}
			
		}
		return actParallel
	}
	
	public function lockAllComplexActivities():Void{
		Debugger.log("Locking all Complex Activities", Debugger.GEN, "lockAllComplexActivities", "CanvasModel");
		var k:Array = _activitiesDisplayed.values();
		//trace("findOptionalActivities Called "+k.length )
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				k[i].locked = true;
				trace("complex activity with id:"+k[i].activity.activityUIID )
			}
		}
	}
	
	
	public function unlockAllComplexActivities():Void{
		Debugger.log("Unlocking all Complex Activities", Debugger.GEN, "unlockAllComplexActivities", "CanvasModel");
		var k:Array = _activitiesDisplayed.values();
		//trace("findOptionalActivities Called "+k.length )
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				k[i].locked = false;
				trace("complex activity with id:"+k[i].activity.activityUIID )
			}
		}
	}
	/**
	 * Craetes a [trans][gate act][trans] combo from an existing transition.
	 * NOT used anymore as we dont want to allow users to think they can click transitions
	 * BUT IF/When the request this - just use this function
	 * @usage   
	 * @param   transitionUIID 
	 * @param   gateTypeID     
	 * @return  
	 */
	public function createGateTransition(transitionUIID,gateTypeID){
		Debugger.log('transitionUIID:'+transitionUIID,Debugger.GEN,'createGateTransition','CanvasModel');
		Debugger.log('gateTypeID:'+gateTypeID,Debugger.GEN,'createGateTransition','CanvasModel');
		var editedTrans = _cv.ddm.getTransitionByUIID(transitionUIID);
		var editedCanvasTrans = _transitionsDisplayed.get(transitionUIID);
		var fromAct = _cv.ddm.getActivityByUIID(editedTrans.fromUIID);
		var toAct = _cv.ddm.getActivityByUIID(editedTrans.toUIID);
		//create a gate activity
		var gateAct = new GateActivity(_cv.ddm.newUIID(),gateTypeID);
		gateAct.learningDesignID = _cv.ddm.learningDesignID;
		
		//gateAct.yCoord = (toAct.yCoord + fromAct.yCoord) / 2;
		//gateAct.xCoord = (toAct.xCoord + fromAct.xCoord) / 2;
		gateAct.title = Dictionary.getValue('gate_btn');
		gateAct.yCoord = editedCanvasTrans.midPoint.y - (CanvasActivity.GATE_ACTIVITY_WIDTH / 2);
		gateAct.xCoord = editedCanvasTrans.midPoint.x - (CanvasActivity.GATE_ACTIVITY_HEIGHT / 2);
		
		Debugger.log('gateAct.yCoord:'+gateAct.yCoord,Debugger.GEN,'createGateTransition','CanvasModel');
		Debugger.log('gateAct.xCoord:'+gateAct.xCoord,Debugger.GEN,'createGateTransition','CanvasModel');

		_cv.ddm.addActivity(gateAct);
		_cv.ddm.removeTransition(transitionUIID);
		//create the from trans
		addActivityToTransition(fromAct);
		addActivityToTransition(gateAct);
		resetTransitionTool()
		//create the to trans
		addActivityToTransition(gateAct);
		addActivityToTransition(toAct);
		resetTransitionTool()
		
		
		//flag the model as dirty and trigger a refresh
		setDirty();	
		//select the new thing
		setSelectedItem(_activitiesDisplayed.get(gateAct.activityUIID));
	}
	
	/**
	 * Creates a new gate activity at the specified location
	 * @usage   
	 * @param   gateTypeID 
	 * @param   pos        
	 * @return  
	 */
	public function createNewGate(gateTypeID, pos:Point){
		Debugger.log('gateTypeID:'+gateTypeID,Debugger.GEN,'createNewGate','CanvasModel');
		var gateAct = new GateActivity(_cv.ddm.newUIID(),gateTypeID);
		gateAct.learningDesignID = _cv.ddm.learningDesignID;
		
		gateAct.title = Dictionary.getValue('gate_btn');
		gateAct.yCoord = pos.y;
		gateAct.xCoord = pos.x;
		
		
		Debugger.log('gateAct.yCoord:'+gateAct.yCoord,Debugger.GEN,'createGateTransition','CanvasModel');
		Debugger.log('gateAct.xCoord:'+gateAct.xCoord,Debugger.GEN,'createGateTransition','CanvasModel');

		_cv.ddm.addActivity(gateAct);
		
		setDirty();
		//select the new thing
		setSelectedItem(_activitiesDisplayed.get(gateAct.activityUIID));
		
	}
	/**
	 * Creates a new group activity at the specified location
	 * @usage   
	 * @param   pos 
	 * @return  
	 */
	public function createNewGroupActivity(pos){
		Debugger.log('Running...',Debugger.GEN,'createNewGroupActivity','CanvasModel');
		
		//first create the grouping object
		var newGrouping = new Grouping(_cv.ddm.newUIID());
		newGrouping.groupingTypeID = _defaultGroupingTypeID;
		
		_cv.ddm.addGrouping(newGrouping);
		
		var groupingActivity = new GroupingActivity(_cv.ddm.newUIID());
		groupingActivity.title = Dictionary.getValue('grouping_act_title');
		groupingActivity.learningDesignID = _cv.ddm.learningDesignID;
		groupingActivity.createGroupingUIID = newGrouping.groupingUIID;
		
		groupingActivity.yCoord = pos.y;
		groupingActivity.xCoord = pos.x;
		
		
		Debugger.log('groupingActivity.createGroupingUIID :'+groupingActivity.createGroupingUIID ,Debugger.GEN,'createNewGroupActivity','CanvasModel');
		Debugger.log('groupingActivity.yCoord:'+groupingActivity.yCoord,Debugger.GEN,'createNewGroupActivity','CanvasModel');
		Debugger.log('groupingActivity.xCoord:'+groupingActivity.xCoord,Debugger.GEN,'createNewGroupActivity','CanvasModel');


		_cv.ddm.addActivity(groupingActivity);
		
		//tell the canvas to go refresh
		setDirty();
		//select the new thing
		setSelectedItem(_activitiesDisplayed.get(groupingActivity.activityUIID));
	}
	/**
	 * Creates a new gate activity at the specified location
	 * @usage   
	 * @param   gateTypeID 
	 * @param   pos        
	 * @return  
	 */
	public function createNewOptionalActivity(ActivityTypeID, pos:Point){
		//Debugger.log('gateTypeID:'+gateTypeID,Debugger.GEN,'createNewGate','CanvasModel');
		var optAct = new ComplexActivity(_cv.ddm.newUIID());
		
		optAct.learningDesignID = _cv.ddm.learningDesignID;
		optAct.activityTypeID = Activity.OPTIONAL_ACTIVITY_TYPE;
		optAct.title = Dictionary.getValue('opt_activity_title');
		optAct.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
		optAct.activityCategoryID = Activity.CATEGORY_SYSTEM;
		optAct.yCoord = pos.y;
		optAct.xCoord = pos.x;
		
		
		Debugger.log('Optional Activitys Y Coord is :'+optAct.yCoord,Debugger.GEN,'createNewOptionalActivity','CanvasModel');
		//Debugger.log('gateAct.xCoord:'+gateAct.xCoord,Debugger.GEN,'createGateTransition','CanvasModel');

		_cv.ddm.addActivity(optAct);
		
		setDirty();
		//select the new thing
		setSelectedItem(_activitiesDisplayed.get(optAct.activityUIID));
		
	}
	
	/**
	 * Assign activityID of Optional activity as a parentID to the ca (canvas activity) 
	 * Which will draw child activities in Parent Optional Activity.
	 * @usage   
	 * @param   parentID (ActivityID of Optional Activity where canavas activity has been dropped.)
	 * @param   ca       (reference of the canvas activity to which parentID is assigned)
	 * @return  
	 */
	public function addParentToActivity(parentID, ca:Object){
		ca.activity.parentUIID = parentID;
		Debugger.log('ParentId of '+ca.activity.activityUIID+ 'Is : '+ca.activity.parentUIID,Debugger.GEN,'addActivityToTransition','CanvasModel');
		removeActivity(ca.activity.activityUIID);
		removeActivity(parentID);
		setDirty();
		//var lengthOfChildren:Array = _cv.ddm.getComplexActivityChildren(parentID);
		//trace("No. of Chlidren in Optional Activity "+parentID+" is: "+lengthOfChildren.length)
	}
	
	/**
	 * Removes the activity from the Canvas Model
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	 
	 /**
	*Called by the view when a template activity icon is dropped
	*/
	public function removeOptionalCA(ca:Object, parentID){
		//lets do a test to see if we got the canvas
		
		Debugger.log('Removed Child '+ca.activity.activityUIID+ 'from : '+ca.activity.parentUIID,Debugger.GEN,'removeOptionalCA','CanvasModel');
		ca.activity.parentUIID = null;
		ca.activity.orderID = null;
		ca.activity.parentActivityID = null;
		removeActivity(ca.activity.activityUIID);
		//_cv.ddm.removeActivity(ca.activity.activityUIID);
		removeActivity(parentID);
		//_cv.ddm.addActivity(ToolActivity(ca));
		setDirty();
		
	}
	
	/**
	 * Removes the activity from the Canvas Model
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	 
	 /**
	*Called by the controller when a complex activity is dropped on bin.
	*/
	public function removeComplexActivity(ca){
		
		Debugger.log('Removed Child '+ca.activity.activityUIID+ 'from : '+ca.activity.parentUIID,Debugger.GEN,'removeOptionalCA','CanvasModel');
		trace("Number of Children are: "+ca.actChildren.length)
		for (var k=0; k<ca.actChildren.length; k++){
			trace("ca.actChildren[k].activityUIID - Child has UIID : "+ca.actChildren[k].activityUIID)
			ca.actChildren[k].parentUIID = null;
						
			removeActivity(ca.actChildren[k].activityUIID);
			_cv.ddm.removeActivity(ca.actChildren[k].activityUIID);
		
			
		}
		removeActivityOnBin(ca.activity.activityUIID);
		setDirty();
	}
	
	
	public function removeActivity(activityUIID):Object{
		//dispatch an event to show the design  has changed
				
		var r:Object = _activitiesDisplayed.remove(activityUIID);
		if(r==null){
			return new LFError("Removing activity failed:"+activityUIID,"removeActivity",this,null);
		}else{
			Debugger.log('Removed:'+r.activityUIID,Debugger.GEN,'removeActivity','DesignDataModel');
				//dispatchEvent({type:'ddmUpdate',target:this});
			
			r.removeMovieClip();
			//_cv.ddm.removeActivity(activityUIID);
		}
	}
	
	public function removeActivityOnBin(activityUIID):Object{
		//dispatch an event to show the design  has changed
				
		var r:Object = _activitiesDisplayed.remove(activityUIID);
		if(r==null){
			return new LFError("Removing activity failed:"+activityUIID,"removeActivity",this,null);
		}else{
			Debugger.log('Removed:'+r.activityUIID,Debugger.GEN,'removeActivity','DesignDataModel');
				//dispatchEvent({type:'ddmUpdate',target:this});
			
			r.removeMovieClip();
			_cv.removeActivity(activityUIID);
		}
	}
	
	
	private function isLoopingLD(fromAct, toAct):Boolean{
		trace("fromAct is: "+fromAct+" and toAct is: "+toAct)
		var toTransitions = _cv.ddm.getTransitionsForActivityUIID(toAct)
		trace("toTransitions.out"+toTransitions.out)
		if (toTransitions.out != null){
			var nextAct = toTransitions.out.toUIID;
			trace("next activity is: "+nextAct)
			if (nextAct == fromAct){
				return true;
			}else {
				return isLoopingLD(fromAct, nextAct)
			}
		}else {
			return false;
		}
		
	}
	
	
	/**
	 * Adds another Canvas Activity to the transition.  
	 * Only 2 may be added, adding the 2nd one triggers the creation of the transition.
	 * @usage   
	 * @param   ca (Canvas or data Activity)
	 * @return  
	 */
	public function addActivityToTransition(ca:Object):Object{
		var activity:Activity;
		//check we have not added too many
		if(ca instanceof CanvasActivity || ca instanceof CanvasParallelActivity || ca instanceof CanvasOptionalActivity){
			activity = ca.activity;
		}else if(ca instanceof Activity){
			activity = Activity(ca);
		}
		
		if(_transitionActivities.length >= 2){
			//TODO: show an error
			return new LFError("Too many activities in the Transition","addActivityToTransition",this);
		}
		
		Debugger.log('Adding Activity.UIID:'+activity.activityUIID,Debugger.GEN,'addActivityToTransition','CanvasModel');
		_transitionActivities.push(activity);
		var fromAct = _transitionActivities[0].activityUIID
		var toAct = _transitionActivities[1].activityUIID
		if(_transitionActivities.length == 2){
			//check we have 2 valid acts to create the transition.
			if(fromAct == toAct){
				return new LFError("You cannot create a Transition between the same Activities","addActivityToTransition",this);
			}
			if(!_cv.ddm.activities.containsKey(fromAct)){
				return new LFError("First activity of the Transition is missing, UIID:"+_transitionActivities[0].activityUIID,"addActivityToTransition",this);
			}
			if(!_cv.ddm.activities.containsKey(toAct)){
				return new LFError(Dictionary.getValue('cv_trans_target_act_missing'),"addActivityToTransition",this);
			}
			//check there is not already a transition to or from this activity:
			var transitionsArray:Array = _cv.ddm.transitions.values();
			
			/**/
			for(var i=0;i<transitionsArray.length;i++){
				
				if(transitionsArray[i].toUIID == toAct){
					return new LFError(Dictionary.getValue('cv_invalid_trans_target_to_activity',[_transitionActivities[1].title]));
				}
				if(transitionsArray[i].fromUIID == fromAct){
					return new LFError(Dictionary.getValue('cv_invalid_trans_target_from_activity',[_transitionActivities[0].title]));
				}
				
				if ((transitionsArray[i].toUIID == toAct && transitionsArray[i].fromUIID == fromAct) || (transitionsArray[i].toUIID == fromAct && transitionsArray[i].fromUIID == toAct)){
					return new LFError(Dictionary.getValue('cv_invalid_trans_circular_sequence'),"addActivityToTransition",this);
				}
				
				
			}
			
			if (isLoopingLD(fromAct, toAct)){
				return new LFError(Dictionary.getValue('cv_invalid_trans_circular_sequence'),"addActivityToTransition",this);
			}
			
			Debugger.log('No validation errors, creating transition.......',Debugger.GEN,'addActivityToTransition','CanvasModel');
			//lets make the transition
			var t:Transition = createTransition(_transitionActivities);
			//add it to the DDM
			
			var success:Object = _cv.ddm.addTransition(t);
			//flag the model as dirty and trigger a refresh
			
			setDirty();
			
			setSelectedItem(_transitionsDisplayed.get(t.transitionUIID));
			_cv.stopTransitionTool();
			
		}
		return true;
	}
	
	/**
	 * Resets the transition tool to its starting state, e.g. if one chas been created or the user released the mouse over an unsuitable clip
	 * @usage   
	 */
	public function resetTransitionTool():Void{
		//clear the transitions array
			_transitionActivities = new Array();
	}
	
	public function isTransitionToolActive():Boolean{
	   if(_activeTool == TRANSITION_TOOL){
		   return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Forms a transition	
	 * @usage   
	 * @param   transitionActs An array of transition activities. Must only contain 2
	 * @return  
	 */
	private function createTransition(transitionActs:Array):Transition{
		var fromAct:Activity = transitionActs[0];
		var toAct:Activity = transitionActs[1];
		
		var t:Transition = new Transition(_cv.ddm.newUIID(),fromAct.activityUIID,toAct.activityUIID,_cv.ddm.learningDesignID);
		
		return t;
	}
	
	
	public function setDesignTitle(){
		broadcastViewUpdate("POSITION_TITLEBAR", null);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////       REFRESHING DESIGNS       /////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * Compares 2 activities, decides if they are new, the same or to be deleted
	 * @usage   
	 * @param   ddm_activity Design Data Model activity
	 * @param   cm_activity  Canvas Model activity
	 * @return  
	 */
	private function compareActivities(ddm_activity:Activity,cm_activity:Activity):Object{
		Debugger.log('Comparing ddm_activity:'+ddm_activity.title+'('+ddm_activity.activityUIID+') WITH cm_activity:'+cm_activity.title+'('+cm_activity.activityUIID+')',Debugger.GEN,'compareActivities','CanvasModel');
		var r:Object = new Object();
		
		//check if the activity has a parent, if so then we dont need to check it
		Debugger.log('Checking parent activity IDs, parentUIID:'+ddm_activity.parentUIID+'parentID:'+ddm_activity.parentActivityID,Debugger.GEN,'refreshDesign','CanvasModel');
		if(ddm_activity.parentActivityID > 0 || ddm_activity.parentUIID > 0){
			return r = "CHILD";
		}
		
		//if they are the same (ref should point to same act) then nothing to do.
		//if the ddm does not have an act displayed then we need to remove it from the cm
		//if the ddm has an act that cm does not ref, then we need to add it.
			
		if(ddm_activity === cm_activity){
			return r = "SAME";
		}
		
		//check for a new act in the dmm
		if(cm_activity == null || cm_activity == undefined){
			return r = "NEW";
		}
		
		//check if act has been removed from canvas
		if(ddm_activity == null || ddm_activity == undefined){
			return r = "DELETE";
		}
		
			
	}
	
	/**
	 * Compares 2 transitions, decides if they are new, the same or to be deleted
	 * @usage   
	 * @param   ddm_transition 
	 * @param   cm_transition  
	 * @return  
	 */
	private function compareTransitions(ddm_transition:Transition, cm_transition:Transition):Object{
		Debugger.log('Comparing ddm_activity:'+ddm_transition.title+'('+ddm_transition.transitionUIID+') WITH cm_transition:'+cm_transition.title+'('+cm_transition.transitionUIID+')',Debugger.GEN,'compareTransitions','CanvasModel');
		var r:Object = new Object();
		if(ddm_transition === cm_transition){
			return r = "SAME";
		}
		
		//check for a new act in the dmm
		if(cm_transition == null){
			return r = "NEW";
		}
		
		//check if act has been removed from canvas
		if(ddm_transition == null){
			return r = "DELETE";
		}
		
		
	}
	
	/**
	 * Compares the design in the CanvasModel (what is displayed on the screen) 
	 * against the design in the DesignDataModel and updates the Canvas Model accordingly.
	 * NOTE: Design elements are added to the DDM here, but removed in the View
	 * 
	 * @usage   
	 * @return  
	 */
	private function refreshDesign(){
	
		//porobbably need to get a bit more granular
		Debugger.log('Running',Debugger.GEN,'refreshDesign','CanvasModel');
		//go through the design and see what has changed, compare DDM to canvasModel
		var ddmActivity_keys:Array = _cv.ddm.activities.keys();
		Debugger.log('ddmActivity_keys.length:'+ddmActivity_keys.length,Debugger.GEN,'refreshDesign','CanvasModel');
		//Debugger.log('ddmActivity_keys::'+ddmActivity_keys.toString(),Debugger.GEN,'refreshDesign','CanvasModel');
		var cmActivity_keys:Array = _activitiesDisplayed.keys();
		Debugger.log('cmActivity_keys.length:'+cmActivity_keys.length,Debugger.GEN,'refreshDesign','CanvasModel');
		//Debugger.log('cmActivity_keys:'+cmActivity_keys.toString(),Debugger.GEN,'refreshDesign','CanvasModel');
		
		
		
		var longest = Math.max(ddmActivity_keys.length, cmActivity_keys.length);
		
		//chose which array we are going to loop over
		var indexArray:Array;
		
		if(ddmActivity_keys.length == longest){
			indexArray = ddmActivity_keys;
		}else{
			indexArray = cmActivity_keys;
		}
		
		
		//loop through and do comparison
		for(var i=0;i<longest;i++){
			//check DDM against CM, DDM is king.
			/*
			var keyToCheck:Number = ddmActivity_keys[i];
			// if its nan then we have to use the cm version
			if(isNaN(keyToCheck)){
				keyToCheck = cmActivity_keys[i];
			}
			*/
			
			var keyToCheck:Number = indexArray[i];
			
			
			var ddm_activity:Activity = _cv.ddm.activities.get(keyToCheck);
			var cm_activity:Activity = _activitiesDisplayed.get(keyToCheck).activity;
			//if they are the same (ref should point to same act) then nothing to do.
			//if the ddm does not have an act displayed then we need to remove it from the cm
			//if the ddm has an act that cm does not ref, then we need to add it.
			
			
			var r_activity:Object = compareActivities(ddm_activity, cm_activity);
			
			Debugger.log('r_activity:'+r_activity,Debugger.GEN,'refreshDesign','CanvasModel');
			if(r_activity == "NEW"){
				//draw this activity
				//NOTE!: we are passing in a ref to the activity in the ddm so if we change any props of this, we are changing the ddm
				
				broadcastViewUpdate("DRAW_ACTIVITY",ddm_activity);
				
			}else if(r_activity == "DELETE"){
				//remove this activity
				if(cm_activity.parentUIID == null){
					broadcastViewUpdate("REMOVE_ACTIVITY",cm_activity);
				}
			}else if(r_activity == "CHILD"){
				//dont ask the view to draw the activity if it is a child act				
				Debugger.log('Found a child activity, not drawing. activityID:'+ddm_activity.activityID+'parentID:'+ddm_activity.parentActivityID,Debugger.GEN,'refreshDesign','CanvasModel');
				
				
			}else{
	
			}
		
			
		}
		
		//now check the transitions:
		var ddmTransition_keys:Array = _cv.ddm.transitions.keys();
		var cmTransition_keys:Array = _transitionsDisplayed.keys();
		var trLongest = Math.max(ddmTransition_keys.length, cmTransition_keys.length);
		//chose which array we are going to loop over
		var trIndexArray:Array;
		
		if(ddmTransition_keys.length == trLongest){
			trIndexArray = ddmTransition_keys;
		}else{
			trIndexArray = cmTransition_keys;
		}
		
		
		//loop through and do comparison
		for(var i=0;i<trIndexArray.length;i++){
			
			var transitionKeyToCheck:Number = trIndexArray[i];

			var ddmTransition:Transition = _cv.ddm.transitions.get(transitionKeyToCheck);
			var cmTransition:Transition = _transitionsDisplayed.get(transitionKeyToCheck).transition;
			
			var r_transition:Object = compareTransitions(ddmTransition, cmTransition);
			
			if(r_transition == "NEW"){
				//NOTE!: we are passing in a ref to the tns in the ddm so if we change any props of this, we are changing the ddm
				broadcastViewUpdate("DRAW_TRANSITION",ddmTransition);
			}else if(r_transition == "DELETE"){
				broadcastViewUpdate("REMOVE_TRANSITION",cmTransition);
			}
			
		}
		
		
		
		
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////       EDITING ACTIVITIES               /////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Called on double clicking an activity
	 * @usage   
	 * @return  
	 */
	public function openToolActivityContent(ta:ToolActivity):Void{
		trace("tool content Id for "+ta.title+" is: "+ta.toolContentID)
		Debugger.log('ta:'+ta.title+'toolContentID:'+ta.toolContentID+" and learningLibraryID: "+ta.learningLibraryID,Debugger.GEN,'openToolActivityContent','CanvasModel');
		//check if we have a toolContentID
		
		var defaultContentID:Number = Application.getInstance().getToolkit().getDefaultContentID(ta.toolContentID,ta.toolID);
		Debugger.log('ta:'+ta.title+'toolContentID:'+ta.toolContentID+', default content ID:'+defaultContentID,Debugger.GEN,'openToolActivityContent','CanvasModel');
		if(ta.toolContentID == defaultContentID){
			getNewToolContentID(ta);
		}else{
		
			//if we have a good toolID lets open it
			if(ta.toolContentID > 0){
				var url:String;
				var cfg = Config.getInstance();
				var ddm = _cv.ddm;
				if(ta.authoringURL.indexOf("?") != -1){
					//09-11-05 Change to toolContentID and remove userID.
					//url = cfg.serverUrl+ta.authoringURL + '&toolContentId='+ta.toolContentID+'&userID='+cfg.userID;
					url = cfg.serverUrl+ta.authoringURL + '&toolContentID='+ta.toolContentID+'&contentFolderID='+ddm.contentFolderID;
				}else{
					//url = cfg.serverUrl+ta.authoringURL + '?toolContentId='+ta.toolContentID+'&userID='+cfg.userID;
					url = cfg.serverUrl+ta.authoringURL + '?toolContentID='+ta.toolContentID+'&contentFolderID='+ddm.contentFolderID;
				}
			
				Debugger.log('Opening url:'+url,Debugger.GEN,'openToolActivityContent','CanvasModel');
				
				JsPopup.getInstance().launchPopupWindow(url, 'ToolActivityContent', 600, 800, true, true, false, false, false);
			
			}else{
				new LFError("We dont have a valid toolContentID","openToolActivityContent",this);
			}
		
		}
		
		
		
		
	}
	
	public function getNewToolContentID(ta:ToolActivity):Void{
		Debugger.log('ta:'+ta.title+', activityUIID:'+ta.activityUIID,Debugger.GEN,'getNewToolContentID','CanvasModel');
		var callback:Function = Proxy.create(this,setNewToolContentID,ta);
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getToolContentID&toolID='+ta.toolID,callback, false);
	}
	
	public function setNewToolContentID(toolContentID:Number,ta:ToolActivity):Void{
		Debugger.log('new content ID from server:'+toolContentID,Debugger.GEN,'setNewToolContentID','CanvasModel');
		ta.toolContentID = toolContentID;
		Debugger.log('ta:'+ta.title+',toolContentID:'+ta.toolContentID+', activityUIID:'+ta.activityUIID,Debugger.GEN,'setNewToolContentID','CanvasModel');
		openToolActivityContent(ta);
	}
	
	public function setDefaultToolContentID(ta:ToolActivity):Void{
		ta.toolContentID = Application.getInstance().getToolkit().getDefaultContentID(ta.toolContentID,ta.toolID);
		Debugger.log('ta:'+ta.title+',toolContentID:'+ta.toolContentID+', activityUIID:'+ta.activityUIID,Debugger.GEN,'setDefaultToolContentID','CanvasModel');
	}
	
	
	
	/**
    * Notify registered listeners that a data model change has happened
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
        trace('broadcast');
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
	/*
	public function setContainerRef(c:Canvas):Void{
		_cv = c;
	}
	
	*/
	//Getters n setters
	
	public function getCanvas():Canvas{
		return _cv;
	}
	
	public function get activitiesDisplayed():Hashtable{
		return _activitiesDisplayed;
	}
	
	public function get transitionsDisplayed():Hashtable{
		return _transitionsDisplayed;
	}
	
	public function get isDrawingTransition():Boolean{
		return _isDrawingTransition;
	}
	/**
	 * 
	 * @usage   
	 * @param   newactivetool 
	 * @return  
	 */
	public function set activeTool (newactivetool:String):Void {
		_activeTool = newactivetool;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newactivetool 
	 * @return  
	 */
	public function setActiveTool (newactivetool):Void {
		_activeTool = newactivetool;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activeTool ():String {
		return _activeTool;
	}
	
	private function setSelectedItem(newselectItem:Object){
		_selectedItem = newselectItem;
		broadcastViewUpdate("SELECTED_ITEM");
	}

	public function setSelectedItemByUIID(uiid:Number){
		var selectedCanvasElement;
		if(_activitiesDisplayed.get(uiid) != null){
			selectedCanvasElement = _activitiesDisplayed.get(uiid);
		}else{
			selectedCanvasElement = _transitionsDisplayed.get(uiid);
		}
		setSelectedItem(selectedCanvasElement);
		
	}
	/**
	 * 
	 * @usage   
	 * @param   newselectItem 
	 * @return  
	 */
	public function set selectedItem (newselectItem:Object):Void {
		setSelectedItem(newselectItem);
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get selectedItem ():Object {
		return _selectedItem;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get isDragging ():Boolean {
		return _isDragging;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function set isDragging (newisDragging:Boolean):Void{
		_isDragging = newisDragging;
	}
	
	public function get importing():Boolean {
		return _importing;
	}
	
	public function set importing(importing:Boolean):Void {
		_importing = importing;
	}
	
	public function get autoSaveWait():Boolean {
		return _autoSaveWait;
	}
	
	public function set autoSaveWait(autoSaveWait:Boolean):Void {
		_autoSaveWait = autoSaveWait;
	}
	
}
