/****************************************************************************
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
import org.lamsfoundation.lams.authoring.br.BranchConnector;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.dict.*;
import mx.events.*;

/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.authoring.cv.CanvasModel extends Observable {
	
	public static var TRANSITION_TOOL:String = "TRANSITION";  //activie tool ID strings definition
	public static var OPTIONAL_TOOL:String = "OPTIONAL";
	public static var GATE_TOOL:String = "GATE";
	public static var GROUP_TOOL:String = "GROUP";
	public static var BRANCH_TOOL:String = "BRANCH";
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
	private var _prevSelectedItem:Object;
	private var _isDrawingTransition:Boolean;
	private var _connectionActivities:Array;
	private var _isDragging:Boolean;
	private var _importing:Boolean;
	private var _editing:Boolean;
	private var _autoSaveWait:Boolean;
	
	//these are hashtables of mc refs MOVIECLIPS (like CanvasActivity or CanvasTransition)
	//each on contains a reference to the emelment in the ddm (activity or transition)
	private var _activitiesDisplayed:Hashtable;
	private var _transitionsDisplayed:Hashtable;
	private var _branchesDisplayed:Hashtable;
	
	private var _currentBranchingActivity:Object;
	private var _activeView:Object;

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
		_branchesDisplayed = new Hashtable("_branchesDisplayed");
		
		_activeTool = "none";
		_activeView = null;
		_currentBranchingActivity = null;
		
		_autoSaveWait = false;
		_connectionActivities = new Array();
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
	
	public function set activeView(a:Object):Void{
		_activeView = a;
		
		broadcastViewUpdate("SET_ACTIVE", a);
	}
	
	public function get activeView():Object {
		return _activeView;
	}
	
	public function isActiveView(view:Object):Boolean {
		return (activeView == view);
	}
	
	public function setPIHeight(h:Number){
		_piHeight = h;
		Application.getInstance().onResize();
	}
	
	public function getPIHeight(){
		return _piHeight;
	}
	
	public function setDirty(){
		_isDirty = true;
		
		if(getCanvas().ddm.learningDesignID == undefined){
			LFMenuBar.getInstance().enableExport(false);
		} else {
			LFMenuBar.getInstance().enableExport(true);
		}

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
	}
	
	/**
	 * Stops it
	 * @usage   
	 * @return  
	 */
	 
	public function stopTransitionTool():Void{
		Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','CanvasModel');
		resetTransitionTool();
		_activeTool = "none";
	}
	
	public function findOptionalActivities():Array{
		var actOptional:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE){
				actOptional.push(k[i]);
			}
			
		}
		return actOptional;
	}
	
	public function findParallelActivities():Array{
		
		var actParallel:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				actParallel.push(k[i]);
			}
			
		}
		return actParallel;
	}
	
	public function findTopLevelActivities():Array{
		var actParent:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.parentUIID == null){
				actParent.push(k[i]);
			}
			
		}
		return actParent;
	}
	
	public function lockAllComplexActivities():Void{
		Debugger.log("Locking all Complex Activities", Debugger.GEN, "lockAllComplexActivities", "CanvasModel");
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				k[i].locked = true;
			}
		}
	}
	
	
	public function unlockAllComplexActivities():Void{
		Debugger.log("Unlocking all Complex Activities", Debugger.GEN, "unlockAllComplexActivities", "CanvasModel");
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				k[i].locked = (k[i].activity.readOnly) ? true : false;
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
	public function createGateTransition(transitionUIID,gateTypeID, parent){
		Debugger.log('transitionUIID:'+transitionUIID,Debugger.GEN,'createGateTransition','CanvasModel');
		Debugger.log('gateTypeID:'+gateTypeID,Debugger.GEN,'createGateTransition','CanvasModel');
		
		var editedTrans = _cv.ddm.getTransitionByUIID(transitionUIID);
		var editedCanvasTrans = _transitionsDisplayed.get(transitionUIID);
		var fromAct = _cv.ddm.getActivityByUIID(editedTrans.fromUIID);
		var toAct = _cv.ddm.getActivityByUIID(editedTrans.toUIID);
		
		//create a gate activity
		var gateAct = new GateActivity(_cv.ddm.newUIID(),gateTypeID);
		gateAct.learningDesignID = _cv.ddm.learningDesignID;
		gateAct.title = Dictionary.getValue('gate_btn');
		gateAct.yCoord = editedCanvasTrans.midPoint.y - (CanvasActivity.GATE_ACTIVITY_WIDTH / 2);
		gateAct.xCoord = editedCanvasTrans.midPoint.x - (CanvasActivity.GATE_ACTIVITY_HEIGHT / 2);
		
		Debugger.log('gateAct.yCoord:'+gateAct.yCoord,Debugger.GEN,'createGateTransition','CanvasModel');
		Debugger.log('gateAct.xCoord:'+gateAct.xCoord,Debugger.GEN,'createGateTransition','CanvasModel');

		if(parent != null) {
			gateAct.parentActivityID = parent.activityID;
			gateAct.parentUIID = parent.activityUIID;
		}

		_cv.ddm.addActivity(gateAct);
		_cv.ddm.removeTransition(transitionUIID);
		
		//create the from trans
		addActivityToTransition(fromAct);
		addActivityToTransition(gateAct);
		resetTransitionTool();
		
		//create the to trans
		addActivityToTransition(gateAct);
		addActivityToTransition(toAct);
		resetTransitionTool();
		
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
	public function createNewGate(gateTypeID, pos:Point, parent){
		Debugger.log('gateTypeID:'+gateTypeID,Debugger.GEN,'createNewGate','CanvasModel');
		var gateAct = new GateActivity(_cv.ddm.newUIID(), gateTypeID);
		gateAct.learningDesignID = _cv.ddm.learningDesignID;
		
		gateAct.title = Dictionary.getValue('gate_btn');
		gateAct.yCoord = pos.y;
		gateAct.xCoord = pos.x;
		
		Debugger.log('gateAct.yCoord:'+gateAct.yCoord,Debugger.GEN,'createGateTransition','CanvasModel');
		Debugger.log('gateAct.xCoord:'+gateAct.xCoord,Debugger.GEN,'createGateTransition','CanvasModel');

		if(parent != null) {
			gateAct.parentActivityID = parent.activityID;
			gateAct.parentUIID = parent.activityUIID;
		}

		_cv.ddm.addActivity(gateAct);
		
		setDirty();
		
		//select the new thing
		setSelectedItem(_activitiesDisplayed.get(gateAct.activityUIID));
		
	}
	
	/**
	 * Creates a new branch activity at the specified location
	 * @usage   
	 * @param   pos 
	 * @return  
	 */
	public function createNewBranchActivity(branchTypeID, pos:Point, parent){
		Debugger.log('Running...',Debugger.GEN,'createNewBranchActivity','CanvasModel');
		
		var branchingActivity = new BranchingActivity(_cv.ddm.newUIID(), branchTypeID);
		branchingActivity.title = Dictionary.getValue('branching_act_title');
		branchingActivity.learningDesignID = _cv.ddm.learningDesignID;
		branchingActivity.activityCategoryID = Activity.CATEGORY_SYSTEM;
		branchingActivity.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
		
		branchingActivity.yCoord = pos.y;
		branchingActivity.xCoord = pos.x;
		
		Debugger.log('branchingActivity.yCoord:'+branchingActivity.yCoord,Debugger.GEN,'createNewBranchActivity','CanvasModel');
		Debugger.log('branchingActivity.xCoord:'+branchingActivity.xCoord,Debugger.GEN,'createNewBranchActivity','CanvasModel');

		if(parent != null) {
			branchingActivity.parentActivityID = parent.activityID;
			branchingActivity.parentUIID = parent.activityUIID;
		}

		_cv.ddm.addActivity(branchingActivity);
		
		//tell the canvas to go refresh
		setDirty();
		
		//select the new thing
		setSelectedItem(_activitiesDisplayed.get(branchingActivity.activityUIID));
	}
	
	public function createNewSequenceActivity(parent){
		Debugger.log('Running...',Debugger.GEN,'createNewSequenceActivity','CanvasModel');
		
		var seqAct = new SequenceActivity(_cv.ddm.newUIID());
		seqAct.title = Dictionary.getValue('sequence_act_title');
		seqAct.learningDesignID = _cv.ddm.learningDesignID;
		seqAct.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
		seqAct.activityCategoryID = Activity.CATEGORY_SYSTEM;
		seqAct.orderID = 1;
		seqAct.stopAfterActivity = true;
		
		if(parent != null) {
			seqAct.parentActivityID = parent.activityID;
			seqAct.parentUIID = parent.activityUIID;
		}
		
		_cv.ddm.addActivity(seqAct);
		
		setDirty();
	}
	
	/**
	 * Creates a new group activity at the specified location
	 * @usage   
	 * @param   pos 
	 * @return  
	 */
	public function createNewGroupActivity(pos:Point, parent){
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

		if(parent != null) {
			groupingActivity.parentActivityID = parent.activityID;
			groupingActivity.parentUIID = parent.activityUIID;
		}

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
	public function createNewOptionalActivity(ActivityTypeID, pos:Point, parent){
		var optAct = new ComplexActivity(_cv.ddm.newUIID());
		
		optAct.learningDesignID = _cv.ddm.learningDesignID;
		optAct.activityTypeID = Activity.OPTIONAL_ACTIVITY_TYPE;
		optAct.title = Dictionary.getValue('opt_activity_title');
		optAct.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
		optAct.activityCategoryID = Activity.CATEGORY_SYSTEM;
		optAct.yCoord = pos.y;
		optAct.xCoord = pos.x;
		
		Debugger.log('Optional Activitys Y Coord is :'+optAct.yCoord,Debugger.GEN,'createNewOptionalActivity','CanvasModel');
		
		if(parent != null) {
			optAct.parentActivityID = parent.activityID;
			optAct.parentUIID = parent.activityUIID;
		}
		
		_cv.ddm.addActivity(optAct);
		
		setDirty();
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
		removeActivity(parentID);
		setDirty();
		
	}
	 
	/**
	*Called by the controller when a complex activity is dropped on bin.
	*/
	public function removeComplexActivity(ca){
		Debugger.log('Removing Complex Activity: ' + ca.activity.activityUIID,Debugger.GEN,'removeComplexActivity','CanvasModel');
		
		// recursively remove all children
		removeComplexActivityChildren(ca.actChildren);
		removeActivityOnBin(ca.activity.activityUIID);
		
		setDirty();
	}
	
	public function removeComplexActivityChildren(children){
		for (var k=0; k<children.length; k++){
			Debugger.log('Removing Child ' + children[k].activity.activityUIID+ 'from : '+ children[k].activity.parentUIID,Debugger.GEN,'removeComplexActivityChildren','CanvasModel');
		
			children[k].parentUIID = null;
			
			// TODO: use method to determine is complex by type
			if(children[k].activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || children[k].activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || children[k].activityTypeID == Activity.SEQUENCE_ACTIVITY_TYPE ) {
				this.removeComplexActivityChildren(_cv.ddm.getComplexActivityChildren(children[k].activityUIID));
			} else {
				removeActivity(children[k].activityUIID);
			}
			
			_cv.ddm.removeActivity(children[k].activityUIID);
		}
	}
	
	
	public function removeActivity(activityUIID):Object{
		//dispatch an event to show the design  has changed
				
		var r:Object = _activitiesDisplayed.remove(activityUIID);
		if(r==null){
			return new LFError("Removing activity failed:"+activityUIID,"removeActivity",this,null);
		}else{
			Debugger.log('Removed:'+r.activityUIID,Debugger.GEN,'removeActivity','DesignDataModel');
			r.removeMovieClip();
		}
	}
	
	public function removeActivityOnBin(activityUIID):Object{
		//dispatch an event to show the design  has changed
				
		var r:Object = _activitiesDisplayed.remove(activityUIID);
		if(r==null){
			return new LFError("Removing activity failed:"+activityUIID,"removeActivity",this,null);
		}else{
			Debugger.log('Removed:'+r.activityUIID,Debugger.GEN,'removeActivity','DesignDataModel');
			
			r.removeMovieClip();
			_cv.removeActivity(activityUIID);
		}
	}
	
	private function isLoopingLD(fromAct, toAct):Boolean{
		var toTransitions = _cv.ddm.getTransitionsForActivityUIID(toAct)
		if (toTransitions.out != null){
			var nextAct = toTransitions.out.toUIID;
			if (nextAct == fromAct){
				return true;
			}else {
				return isLoopingLD(fromAct, nextAct)
			}
		}else {
			return false;
		}
	}
	
	public function addNewBranch(sequence:SequenceActivity, branchingActivity:Activity):Void {
		if(sequence.firstActivityUIID != null) {
			var b:Branch = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_FROM_START, _cv.ddm.getActivityByUIID(sequence.firstActivityUIID).activityUIID, branchingActivity.activityUIID, sequence, _cv.ddm.learningDesignID);
			_cv.ddm.addBranch(b);
			
			if(!sequence.stopAfterActivity) {
				b = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_TO_END, _cv.ddm.getActivityByUIID(this.getLastActivityUIID(sequence.firstActivityUIID)).activityUIID, branchingActivity.activityUIID, sequence, _cv.ddm.learningDesignID);
			
				_cv.ddm.addBranch(b);
			}
		}
		
		setDirty();
	}
	
	private function getLastActivityUIID(activityUIID:Number):Number {
		
		// get next activity from transition
		var transObj = _cv.ddm.getTransitionsForActivityUIID(activityUIID);
		
		return (transObj.out == null) ? activityUIID : getLastActivityUIID(transObj.out.toUIID);
	}
	
	public function getDownstreamToolActivities():Array {
		var _activity;
		var _activityUIID:Number = selectedItem.activity.activityUIID;
		var tActivities:Array = new Array();
		
		while(_activityUIID != null) {
			
			var transObj:Object = getCanvas().ddm.getTransitionsForActivityUIID(_activityUIID);
		
			_activity = (transObj.into != null) ? _cv.ddm.getActivityByUIID(transObj.into.fromUIID) : null;
			
			if(_activity != null) {
				if(_activity instanceof ToolActivity) {
					tActivities.addItem({label: _activity.title, data: _activity.activityUIID});
				} else if(_activity instanceof ComplexActivity) {
					getToolActivitiesFromComplex(_activity.activityUIID, tActivities);
				}
			}
			
			_activityUIID = _activity.activityUIID;
		}

		return tActivities;
	}
	
	private function getToolActivitiesFromComplex(complexUIID, toolActs:Array):Void {
		var children:Array = getCanvas().ddm.getComplexActivityChildren(complexUIID);
		
		for(var i=0; i<children.length; i++) {
			if(children[i] instanceof ToolActivity) toolActs.addItem({label: children[i].title, data: children[i].activityUIID});
			else if(children[i] instanceof ComplexActivity) getToolActivitiesFromComplex(children[i].activityUIID, toolActs);
		}

	}
	
	public function addActivityToConnection(ca:Object):Object{
		var activity:Activity;
		//check we have not added too many
		if(ca instanceof CanvasActivity || ca instanceof CanvasParallelActivity || ca instanceof CanvasOptionalActivity){
			activity = ca.activity;
		} else if(ca instanceof Activity){
			activity = Activity(ca);
		}
		
		
		if(_connectionActivities.length > 0) {
			
			if(_connectionActivities[0].activityUIID == activeView.startHub.activity.activityUIID || 
			   activity.activityUIID == activeView.endHub.activity.activityUIID) {
				return addActivityToBranch(activity);
			} else {
				return addActivityToTransition(activity);
			}
			
		}
		
		_connectionActivities.push(activity);

		return true;
	}
	
	private function addActivityToBranch(activity:Activity):Object{
		
		if(_connectionActivities.length >= 2){
			//TODO: show an error
			return new LFError("Too many activities in the Branch","addActivityToBranch",this);
		}
		
		Debugger.log('Adding Activity.UIID:'+activity.activityUIID,Debugger.GEN,'addActivityToBranch','CanvasModel');
		_connectionActivities.push(activity);
		
		var fromAct = _connectionActivities[0].activityUIID
		var toAct = _connectionActivities[1].activityUIID
		
		//check we have 2 valid acts to create the transition.
		if(fromAct == toAct){
			return new LFError("You cannot create a Branch between the same Activities","addActivityToTransition",this);
		}
		
		if(_connectionActivities.length == 2){
			/*********************************************
			* TODO: REQUIRE NORMAL BRANCH CLIENT_SIDE VALIDATION
			*********************************************/
			Debugger.log('fromAct: ' + fromAct + " toAct:" + toAct, Debugger.GEN,'addActivityToBranch','CanvasModel');
			
			if(!_cv.ddm.activities.containsKey(toAct)){
				return new LFError(Dictionary.getValue('cv_trans_target_act_missing'),"addActivityToBranch",this);
			}
			
			//lets make the connection
			var b:Object = (activeView.fingerprint == activeView.endHub) ? createBranchEndConnector(_connectionActivities) : createBranchStartConnector(_connectionActivities);

			Debugger.log('No validation errors, creating branch.......' + b,Debugger.GEN,'addActivityToBranch','CanvasModel');
				
			//add it to the DDM
			if(b instanceof LFError) {
				b.sequenceActivity.stopAfterActivity = false;
				return b;
			} else if(b != null){
				var success:Object = _cv.ddm.addBranch(Branch(b));
			}
			
			//flag the model as dirty and trigger a refresh
			_cv.stopTransitionTool();
			
			setDirty();
		}
			
		return true;
	}
	
	/**
	 * Adds another Canvas Activity to the transition.  
	 * Only 2 may be added, adding the 2nd one triggers the creation of the transition.
	 * @usage   
	 * @param   ca (Canvas or data Activity)
	 * @return  
	 */
	private function addActivityToTransition(activity:Activity):Object{
		
		if(_connectionActivities.length >= 2){
			//TODO: show an error
			return new LFError("Too many activities in the Transition","addActivityToTransition",this);
		}
		
		Debugger.log('Adding Activity.UIID:'+activity.activityUIID,Debugger.GEN,'addActivityToTransition','CanvasModel');
		_connectionActivities.push(activity);
		
		var fromAct = _connectionActivities[0].activityUIID
		var toAct = _connectionActivities[1].activityUIID
		
		if(_connectionActivities.length == 2){
			
			var t:Transition;
			
			/*********************************************
			* BELOW: NORMAL TRANSITION CLIENT_SIDE VALIDATION
			*********************************************/
				
			//check we have 2 valid acts to create the transition.
			if(fromAct == toAct){
				return new LFError("You cannot create a Transition between the same Activities","addActivityToTransition",this);
			}
				
			if(!_cv.ddm.activities.containsKey(fromAct)){
				return new LFError("First activity of the Transition is missing, UIID:"+_connectionActivities[0].activityUIID,"addActivityToTransition",this);
			}
			
			if(!_cv.ddm.activities.containsKey(toAct)){
				return new LFError(Dictionary.getValue('cv_trans_target_act_missing'),"addActivityToTransition",this);
			}
			
			var branch = _cv.ddm.getBranchesForActivityUIID(toAct);
			if(branch.target != null) {
				return new LFError("Can't create transition between activities in different branches", "addActivityToTransition", this);
			}
				
			//check there is not already a transition to or from this activity:
			var transitionsArray:Array = _cv.ddm.transitions.values();
				
			/**/
			for(var i=0;i<transitionsArray.length;i++){
					
				if(transitionsArray[i].toUIID == toAct){
					return new LFError(Dictionary.getValue('cv_invalid_trans_target_to_activity',[_connectionActivities[1].title]));
				}
				
				if(transitionsArray[i].fromUIID == fromAct){
					return new LFError(Dictionary.getValue('cv_invalid_trans_target_from_activity',[_connectionActivities[0].title]));
				}
					
				if ((transitionsArray[i].toUIID == toAct && transitionsArray[i].fromUIID == fromAct) || (transitionsArray[i].toUIID == fromAct && transitionsArray[i].fromUIID == toAct)){
					return new LFError(Dictionary.getValue('cv_invalid_trans_circular_sequence'),"addActivityToTransition",this);
				}
				
			}
			
			var branchesArray:Array = _cv.ddm.branches.values();
			
			for(var i=0; i<branchesArray.length; i++) {
				if(branchesArray[i].targetUIID == toAct && branchesArray[i].direction == BranchConnector.DIR_TO_END) {
					return new LFError("Cannot connect a new transition a closed sequence");
				}
				
				if(branchesArray[i].targetUIID == fromAct && branchesArray[i].direction == BranchConnector.DIR_TO_END) {
					return new LFError("Cannot connect a new transition a closed sequence");
				}
			}
				
			if (isLoopingLD(fromAct, toAct)){
				return new LFError(Dictionary.getValue('cv_invalid_trans_circular_sequence'),"addActivityToTransition",this);
			}
				
			Debugger.log('No validation errors, creating transition.......',Debugger.GEN,'addActivityToTransition','CanvasModel');
			
			//lets make the transition
			t = createTransition(_connectionActivities);
				
			//add it to the DDM
			var success:Object = _cv.ddm.addTransition(t);
				
				
			//flag the model as dirty and trigger a refresh
			_cv.stopTransitionTool();
			
			setDirty();
		}
		
		return true;
	}
	
	/**
	 * Resets the transition tool to its starting state, e.g. if one chas been created or the user released the mouse over an unsuitable clip
	 * @usage   
	 */
	public function resetTransitionTool():Void{
		//clear the transitions array
		_connectionActivities = new Array();
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
	private function createTransition(transActivities:Array):Transition{
		var fromAct:Activity = transActivities[0];
		var toAct:Activity = transActivities[1];
		
		var t:Transition = new Transition(_cv.ddm.newUIID(),fromAct.activityUIID,toAct.activityUIID,_cv.ddm.learningDesignID);
		
		return t;
	}
	
	/**
	 * @usage   
	 * @param   transitionActs An array of transition activities. Must only contain 2
	 * @return  
	 */
	private function createBranchStartConnector(branchActivities:Array):Object{
		
		var fromAct:Activity = branchActivities[0];
		var toAct:Activity = branchActivities[1];
		
		var sequence:SequenceActivity = SequenceActivity(_cv.ddm.getActivityByUIID(toAct.parentUIID));
		var branchesSize:Number = _cv.ddm.getBranchesForActivityUIID(sequence.activityUIID).myBranches.length;
			
		/** Basic validation for Branch(s)/Branch Connector(s) */
		if(toAct.activityUIID == activeView.startHub.activity.activityUIID) {
			return new LFError("Branch Connector must be drawn from the Hub to an Activity");
		} else if(_cv.ddm.getTransitionsForActivityUIID(toAct.activityUIID).into != null) {
			return new LFError("Cannot create start-branch connection to Activity with inward Transition.", "createBranchStartConnector", this);
		} else if(branchesSize > 0) {
			return new LFError("Cannot create start-branch connection to Activity in a already connected Sequence.", "createBranchStartConnector", this);
		} else {
			var b = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_FROM_START, toAct.activityUIID, activeView.startHub.activity.activityUIID, activeView.defaultSequenceActivity, _cv.ddm.learningDesignID);
			
			createNewSequenceActivity(activeView.activity);
			return b;
		}
	}

	/**
	 * @usage   
	 * @param   transitionActs An array of transition activities. Must only contain 2
	 * @return  
	 */
	private function createBranchEndConnector(branchActivities:Array):Object{
		var fromAct:Activity = branchActivities[0];
		var toAct:Activity = branchActivities[1];
		
		var sequence:SequenceActivity = SequenceActivity(_cv.ddm.getActivityByUIID(fromAct.parentUIID));
		
		/** Basic validation for Branch(s)/Branch Connector(s) */
		if(fromAct.activityUIID == activeView.endHub.activity.activityUIID) {
			return new LFError("Cannot create branch from end-point.");
		} else if(_cv.ddm.getTransitionsForActivityUIID(fromAct.activityUIID).out != null) {
			return new LFError("Cannot create end-branch connection to Activity with outward Transition", "createBranchEndConnector", this);
		} else if(_cv.ddm.getBranchesForActivityUIID(sequence.activityUIID).myBranches.length <= 0) {
			return new LFError("Cannot create end-branch connection to an unconnected Sequence.", "createBranchStartConnector", this);
		} else {
			return new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_TO_END, fromAct.activityUIID, activeView.endHub.activity.activityUIID, sequence, _cv.ddm.learningDesignID);
		
		}
	}

	public function moveActivitiesToBranchSequence(activityUIID:Number, sequence:SequenceActivity):Boolean {
		// move first activity
		var ca = _activitiesDisplayed.get(activityUIID);
		
		if(sequence.activityUIID != ca.activity.parentUIID) {
			addParentToActivity(sequence.activityUIID, ca);
		} else {
			return true;
		}
		
		// get next activity from transition
		var transObj = _cv.ddm.getTransitionsForActivityUIID(activityUIID);
		
		return (transObj.out == null) ? true : moveActivitiesToBranchSequence(transObj.out.toUIID, sequence);

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
		
		//check if act has been removed from canvas
		if(ddm_activity == null || ddm_activity == undefined){
			return r = "DELETE";
		}
		
		//if they are the same (ref should point to same act) then nothing to do.
		//if the ddm does not have an act displayed then we need to remove it from the cm
		//if the ddm has an act that cm does not ref, then we need to add it.
			
		if(ddm_activity === cm_activity){
			return r = "SAME";
		}
		
		//check if the activity has a parent, if so then we dont need to check it
		Debugger.log('Checking parent activity IDs, parentUIID:'+ddm_activity.parentUIID+'parentID:'+ddm_activity.parentActivityID,Debugger.GEN,'refreshDesign','CanvasModel');
		if(ddm_activity.activityTypeID == Activity.SEQUENCE_ACTIVITY_TYPE){
			return r = "SEQ";
		}
		
		if(ddm_activity.parentActivityID > 0 || ddm_activity.parentUIID > 0){
			var parentAct;
			if((parentAct = _cv.ddm.activities.get(ddm_activity.parentUIID)) != null)
				if(parentAct.activityTypeID == Activity.SEQUENCE_ACTIVITY_TYPE)
					return r = "NEW_SEQ_CHILD";
			
			return r = "CHILD";
		}
		
		//check for a new act in the dmm
		if(cm_activity == null || cm_activity == undefined){
			return r = "NEW";
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
		Debugger.log('Comparing ddm_transition:'+ddm_transition.title + '(' +ddm_transition.transitionUIID+') WITH cm_transition:' + cm_transition.title + '(' + cm_transition.transitionUIID +')' ,Debugger.GEN,'compareTransitions','CanvasModel');
		return compareConnections(ddm_transition, cm_transition);
	}
	
	private function compareBranches(ddm_branch:Branch, cm_branch:Branch):Object{
		Debugger.log('Comparing ddm_branch:'+ddm_branch.title + '(' +ddm_branch.branchUIID+') WITH cm_branch:' + cm_branch.title + '(' + cm_branch.branchUIID +')' ,Debugger.GEN,'compareBranches','CanvasModel');
		return compareConnections(ddm_branch, cm_branch);
	}
	
	private function compareConnections(ddm_connect, cm_connect):Object{
		var r:Object = new Object();
		if(ddm_connect === cm_connect){
			return r = "SAME";
		}
		
		//check for a new connection in the dmm
		if(cm_connect == null){
			return r = "NEW";
		}
		
		//check if connection has been removed from canvas
		if(ddm_connect == null){
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
	
		Debugger.log('Running',Debugger.GEN,'refreshDesign','CanvasModel');
		
		//go through the design and see what has changed, compare DDM to canvasModel
		var ddmActivity_keys:Array = _cv.ddm.activities.keys();
		Debugger.log('ddmActivity_keys.length:'+ddmActivity_keys.length,Debugger.GEN,'refreshDesign','CanvasModel');
		
		var cmActivity_keys:Array = _activitiesDisplayed.keys();
		Debugger.log('cmActivity_keys.length:'+cmActivity_keys.length,Debugger.GEN,'refreshDesign','CanvasModel');

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
			var keyToCheck:Number = indexArray[i];
			
			var ddm_activity:Activity = _cv.ddm.activities.get(keyToCheck);
			var cm_activity:Activity = _activitiesDisplayed.get(keyToCheck).activity;
			
			/**if they are the same (ref should point to same act) then nothing to do.
			*  if the ddm does not have an act displayed then we need to remove it from the cm
			*  if the ddm has an act that cm does not ref, then we need to add it.
			**/
			
			var r_activity:Object = compareActivities(ddm_activity, cm_activity);
			
			Debugger.log('r_activity:'+r_activity,Debugger.GEN,'refreshDesign','CanvasModel');
			if(r_activity == "NEW"){
				//draw this activity
				//NOTE!: we are passing in a ref to the activity in the ddm so if we change any props of this, we are changing the ddm
				broadcastViewUpdate("DRAW_ACTIVITY",ddm_activity);

			}else if(r_activity == "NEW_SEQ_CHILD"){
				broadcastViewUpdate("DRAW_ACTIVITY_SEQ",ddm_activity);
			}else if(r_activity == "DELETE"){
				//remove this activity
				if(cm_activity.parentUIID == null){
					broadcastViewUpdate("REMOVE_ACTIVITY",cm_activity);
				}
			}else if(r_activity == "CHILD"){
				//dont ask the view to draw the activity if it is a child act				
				Debugger.log('Found a child activity, not drawing. activityID:'+ddm_activity.activityID+'parentID:'+ddm_activity.parentActivityID,Debugger.GEN,'refreshDesign','CanvasModel');
			}else if(r_activity == "SEQ"){
				broadcastViewUpdate("ADD_SEQUENCE", ddm_activity);
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
		
		//now check the transitions:
		var ddmBranch_keys:Array = _cv.ddm.branches.keys();
		var cmBranch_keys:Array = _branchesDisplayed.keys();
		var brLongest = Math.max(ddmBranch_keys.length, cmBranch_keys.length);
		
		//chose which array we are going to loop over
		var brIndexArray:Array;
		
		if(ddmBranch_keys.length == brLongest){
			brIndexArray = ddmBranch_keys;
		}else{
			brIndexArray = cmBranch_keys;
		}
		
		//loop through and do comparison
		for(var i=0;i<brIndexArray.length;i++){
			
			var branchKeyToCheck:Number = brIndexArray[i];

			var ddmBranch:Branch = _cv.ddm.branches.get(branchKeyToCheck);
			var cmBranch:Branch = _branchesDisplayed.get(branchKeyToCheck).branch;
			var r_branch:Object = compareBranches(ddmBranch, cmBranch);
			
			if(r_branch == "NEW"){
				//NOTE!: we are passing in a ref to the tns in the ddm so if we change any props of this, we are changing the ddm
				broadcastViewUpdate("DRAW_BRANCH",ddmBranch);
			}else if(r_branch == "DELETE"){
				broadcastViewUpdate("REMOVE_BRANCH",cmBranch);
			}
		}
	}
	
	public function getModTransitionsForActivityUIID(UIID:Number):Object {
		var ts:Array = _transitionsDisplayed.keys();
		var transObj = new Object();
		var modTransitions:Array = new Array();
		var into = null;
		var out = null;
		var hasTrans:Boolean = false;
		
		for(var i=0; i<ts.length;i++){
			
			var cmTransition:Transition = _transitionsDisplayed.get(ts[i]).transition;
			
			if(cmTransition.mod_toUIID == UIID || cmTransition.mod_fromUIID == UIID){
				modTransitions.push(cmTransition);
				hasTrans = true;
			}
			if(into != null && out != null){
					break;
			}else{
				if(cmTransition.mod_fromUIID == UIID){
					out = ts[i];
				}
				if(cmTransition.mod_toUIID == UIID){
					into = ts[i];
				}
			}
		}
		
		transObj.modTransitions = modTransitions;
		transObj.out = out;
		transObj.into = into;
		transObj.hasTrans = hasTrans;
		
		return transObj;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////       VALIDATE DESIGN               ////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	public function validateDesign():Array {
		var errorMap:Array = new Array();
		
		validateActivityTransitionRules(findTopLevelActivities(), _transitionsDisplayed, errorMap);
		
		return errorMap;
	}
	
	private function validateActivityTransitionRules(parentActivities:Array, transitions:Hashtable, errorMap:Array):Void {
		var noTopLevelActivities:Number = parentActivities.length;
		var noInputTransition:Array = new Array();
		var noOutputTransition:Array = new Array();
		
		validateTransitions(transitions, errorMap);
		
		for(var i=0; i<parentActivities.length; i++) {
			
			var cmActivity:Activity = Activity(parentActivities[i].activity);
			var actTransitions:Object = _cv.ddm.getTransitionsForActivityUIID(cmActivity.activityUIID);
		
			checkActivityForTransition(cmActivity, actTransitions, noTopLevelActivities, errorMap);
		
			if(actTransitions.into == null) {
				noInputTransition.push(cmActivity);
			}
			
			if(actTransitions.out == null) {
				noOutputTransition.push(cmActivity);
			}
		
		}
		
		if(noTopLevelActivities > 0) {
			if (noInputTransition.length == 0) {
				errorMap.push(new ValidationIssue(ValidationIssue.INPUT_TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.INPUT_TRANSITION_ERROR_TYPE2_KEY)));
			} else if (noInputTransition.length > 1) {
				//there is more than one activity with no input transitions
				for(var i=0; i<noInputTransition.length; i++) {
					var a:Activity = Activity(noInputTransition[i]);
					errorMap.push(new ValidationIssue(ValidationIssue.INPUT_TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.INPUT_TRANSITION_ERROR_TYPE1_KEY), a.activityUIID));
				}
			}
			
			if (noOutputTransition.length == 0) {
				errorMap.push(new ValidationIssue(ValidationIssue.OUTPUT_TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.OUTPUT_TRANSITION_ERROR_TYPE2_KEY)));
			} else if (noOutputTransition.length > 1) {
				//there is more than one activity with no output transitions
				for(var i=0; i<noOutputTransition.length; i++) {
					var a:Activity = Activity(noOutputTransition[i]);
					errorMap.push(new ValidationIssue(ValidationIssue.OUTPUT_TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.OUTPUT_TRANSITION_ERROR_TYPE1_KEY), a.activityUIID));					
				}
			}
		}
	}
	
	private function validateTransitions(transitions:Hashtable, errorMap:Array):Void {
		
		var cmTransition_keys:Array = transitions.keys();
		
		for(var i=0; i<cmTransition_keys.length; i++) {
		
			var cmTransition:Transition = Transition(transitions.get(cmTransition_keys[i]).transition);

			var fromActivity:Activity = _cv.ddm.getActivityByUIID(cmTransition.fromUIID);
			var toActivity:Activity = _cv.ddm.getActivityByUIID(cmTransition.toUIID);

			if(fromActivity == null) {
				errorMap.push(new ValidationIssue(ValidationIssue.TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.TRANSITION_ERROR_KEY), cmTransition.transitionUIID));
			} else if (toActivity == null) {
				errorMap.push(new ValidationIssue(ValidationIssue.TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.TRANSITION_ERROR_KEY), cmTransition.transitionUIID));
			}
		}
	}
	
	private function checkActivityForTransition(activity:Activity, actTransitions:Object, noOfActivities:Number, errorMap:Array):Void {
		
		if(noOfActivities > 1) {
			if(actTransitions.into == null && actTransitions.out == null)
				errorMap.push(new ValidationIssue(ValidationIssue.ACTIVITY_TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.ACTIVITY_TRANSITION_ERROR_KEY), activity.activityUIID));
			
		} else if(noOfActivities == 1) {	
			if(actTransitions.into != null || actTransitions.out != null)				
				errorMap.push(new ValidationIssue(ValidationIssue.ACTIVITY_TRANSITION_ERROR_CODE, Dictionary.getValue(ValidationIssue.ACTIVITY_TRANSITION_ERROR_KEY), activity.activityUIID));
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
					url = cfg.serverUrl+ta.authoringURL + '&toolContentID='+ta.toolContentID+'&contentFolderID='+ddm.contentFolderID;
				}else{
					url = cfg.serverUrl+ta.authoringURL + '?toolContentID='+ta.toolContentID+'&contentFolderID='+ddm.contentFolderID;
				}
			
				Debugger.log('Opening url:'+url,Debugger.GEN,'openToolActivityContent','CanvasModel');
				
				JsPopup.getInstance().launchPopupWindow(url, 'ToolActivityContent', 600, 800, true, true, false, false, false);
			
				// set modified (not-saved) flag so that potential changes cannot be lost.
				ApplicationParent.extCall('setSaved', 'false');
			
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
	
	public function openBranchActivityContent(ba):Void {
		currentBranchingActivity = ba;
		
		if(ba.branchView != null) {
			activeView = ba.branchView;
			ba.branchView.open();
		} else { _cv.openBranchView(currentBranchingActivity); }
	}
	
	public function get currentBranchingActivity():Object {
		return _currentBranchingActivity;
	}
	
	public function set currentBranchingActivity(a:Object) {
		_currentBranchingActivity = a;
	}
	
	/**
    * Notify registered listeners that a data model change has happened
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
    }
	
	/**
	 * Returns a reference to the Activity Movieclip for the UIID passed in.  Gets from _activitiesDisplayed Hashable
	 * @usage   
	 * @param   UIID 
	 * @return  Activity Movie clip
	 */
	public function getActivityMCByUIID(UIID:Number):MovieClip{
		
		var a_mc:MovieClip = _activitiesDisplayed.get(UIID);
		return a_mc;
	}

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
	
	public function get branchesDisplayed():Hashtable{
		return _branchesDisplayed;
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
		prevSelectedItem = _selectedItem;
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
	
	public function set prevSelectedItem (oldselectItem:Object):Void {
		_prevSelectedItem = oldselectItem;
	}
	
	public function get prevSelectedItem():Object {
		return _prevSelectedItem;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get selectedItem ():Object {
		return _selectedItem;
	}
	
	public function get connectionActivities():Array {
		return _connectionActivities;
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
	
	public function get editing():Boolean {
		return _editing;
	}
	
	public function set editing(editing:Boolean):Void {
		_editing = editing;
	}
	
	public function get autoSaveWait():Boolean {
		return _autoSaveWait;
	}
	
	public function set autoSaveWait(autoSaveWait:Boolean):Void {
		_autoSaveWait = autoSaveWait;
	}
	
}
