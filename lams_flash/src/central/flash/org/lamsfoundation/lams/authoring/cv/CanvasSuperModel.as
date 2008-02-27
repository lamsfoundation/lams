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
import org.lamsfoundation.lams.authoring.br.CanvasBranchView;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.dict.*;
import mx.events.*;

/**
 *
 * @author
 * @version
 **/
class org.lamsfoundation.lams.authoring.cv.CanvasSuperModel extends Observable {
	
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
	
	private var _lastBranchActionType:Number;
	
	private var _doRefresh:Boolean;
	private var _activeRefresh:Boolean;
	private var _refreshQueueCount:Number;
	private var _branchingQueue:Array;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	public function CanvasSuperModel(cv:Canvas) {
		
		_cv = cv;
		_ddm = new DesignDataModel();
		_activitiesDisplayed = new Hashtable("_activitiesDisplayed");
		_transitionsDisplayed = new Hashtable("_transitionsDisplayed");
		_branchesDisplayed = new Hashtable("_branchesDisplayed");
		
		_activeTool = "none";
		_activeView = null;
		_currentBranchingActivity = null;
		_lastBranchActionType = null;
		
		_autoSaveWait = false;
		_connectionActivities = new Array();
		_defaultGroupingTypeID = Grouping.RANDOM_GROUPING;
		
		_doRefresh = true;
		
		_branchingQueue = new Array();
		
		 //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
	}
		
	public function clearGroupedActivities(activityUIID:Number):Void {
		
		var act:Activity = _cv.ddm.getActivityByUIID(activityUIID);
		var createGroupingUIID:Number;
		
		Debugger.log('act isGroupingAct?: '+act.isGroupActivity(), Debugger.CRITICAL, 'clearGroupedActivities', 'CanvasSuperModel');
			
		if(!act.isGroupActivity())
			return;
		else
			createGroupingUIID = GroupingActivity(act).createGroupingUIID;
		
		if(createGroupingUIID != null) {
		
			var keyArray:Array = _activitiesDisplayed.keys();
			
			for(var i=0; i<keyArray.length; i++){
				var a = _activitiesDisplayed.get(keyArray[i]);
				var currentGroupingUIID:Number = a.activity.groupingUIID;
				
				Debugger.log('currentGroupingUIID:'+currentGroupingUIID, Debugger.CRITICAL,'clearGroupedActivities','CanvasSuperModel');
				
				if(currentGroupingUIID == createGroupingUIID) {
					
					Debugger.log('Found grouping match:'+ a.activity.groupingUIID, Debugger.CRITICAL,'clearGroupedActivities','CanvasSuperModel');
					
					a.activity.groupingUIID = null;
					
					if (a.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
						for (var k=0; k<a.actChildren.length; k++){
							a.actChildren[k].groupingUIID = null;
						}
						
						a.init();
					}
			
					Debugger.log('Set grouping UIID to null: '+a.activity.groupingUIID ,Debugger.GEN,'clearGroupedActivities','CanvasSuperModel');
					
					a.isSetSelected = false;
					a.refresh()
				}
			}
		}
		
		selectedItem = null;
	}
	
	public function getDownstreamActivities(_class, isBranching:Boolean):Array {
		var _activity;
		var _activityUIID:Number = selectedItem.activity.activityUIID;
		var tActivities:Array = new Array();
		
		if(isBranching)
			tActivities.addItem({label: "--Selection--", data: 0});	 // TODO: Label required
		
		while(_activityUIID != null) {
			
			var transObj:Object = getCanvas().ddm.getTransitionsForActivityUIID(_activityUIID);
		
			_activity = (transObj.into != null) ? _cv.ddm.getActivityByUIID(transObj.into.fromUIID) : getParentActivity(_activity);
			
			if(_activity != null) {
				if(_activity instanceof _class) {
					if(isBranching) tActivities.addItem({label: _activity.title, data: _activity.activityUIID});
					else tActivities.addItem({label: _activity.title, data: _activity});
				} else if(_activity instanceof ComplexActivity) {
					if(!isBranching && !_activity.isOptionalActivity() && !_activity.isSequenceActivity())
						getActivitiesFromComplexByClass(_activity.activityUIID, tActivities, _class, isBranching);
					else if(isBranching && !_activity.isOptionalActivity() && !_activity.isOptionsWithSequencesActivity())
						getActivitiesFromComplexByClass(_activity.activityUIID, tActivities, _class, isBranching);
						
				}
			}
			
			_activityUIID = _activity.activityUIID;
		}

		return tActivities;
	}
	
	private function getActivitiesFromComplexByClass(complexUIID, tActs:Array, _class, isBranching:Boolean):Void {
		var children:Array = getCanvas().ddm.getComplexActivityChildren(complexUIID);
		
		for(var i=0; i<children.length; i++) {
			if(children[i] instanceof _class) {
				if(isBranching) tActs.addItem({label: children[i].title, data: children[i].activityUIID});
				else tActs.addItem({label: children[i].title, data: children[i]});
			} else if(children[i] instanceof ComplexActivity) {
				if(!isBranching && !children[i].isOptionalActivity() && !children[i].isSequenceActivity()) {
					getActivitiesFromComplexByClass(children[i].activityUIID, tActs, _class);
				} else if(isBranching && !children[i].isOptionalActivity() && !children[i].isOptionsWithSequencesActivity()) {
					getActivitiesFromComplexByClass(children[i].activityUIID, tActs, _class);
				}
			}
		}

	}
	
	public function addToBranchingQueue(a:CanvasBranchView):Void {
		Debugger.log("adding to branching queue: " + a, Debugger.CRITICAL, "addToBranchingQueue", "CanvasSuperModel");
		
		_branchingQueue.push(a);
		
		Debugger.log("branching queue length: " + _branchingQueue.length, Debugger.CRITICAL, "addToBranchingQueue", "CanvasSuperModel");
		
		if(_branchingQueue.length == 1) {
			a.init(this, undefined);
			setupBranchingObserver(a);
		}
	}
	
	public function releaseNextFromBranchingQueue():Void {
		if(_branchingQueue.length > 0) {
			_branchingQueue.shift();
		
		if(_branchingQueue[0] instanceof CanvasBranchView) {
				CanvasBranchView(_branchingQueue[0]).init(this, undefined); 
				setupBranchingObserver(CanvasBranchView(_branchingQueue[0]));
			} else {
				CanvasOptionalActivity(_branchingQueue[0]).init();
			}
		}
	}
	
	private function setupBranchingObserver(a:CanvasBranchView):Void {
		//Add listener to view so that we know when it's loaded
        a.addEventListener('load', Proxy.create(_cv, _cv.viewLoaded));
		
		this.addObserver(a);
	}
	
	public function lockAllComplexActivities():Void{
		Debugger.log("Locking all Complex Activities", Debugger.GEN, "lockAllComplexActivities", "CanvasModel");
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.OPTIONS_WITH_SEQUENCES_TYPE){
				k[i].locked = true;
			}
		}
	}
	
	public function unlockAllComplexActivities():Void{
		Debugger.log("Unlocking all Complex Activities", Debugger.GEN, "unlockAllComplexActivities", "CanvasModel");
		var k:Array = _activitiesDisplayed.values();
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.OPTIONS_WITH_SEQUENCES_TYPE){
				k[i].locked = (k[i].activity.readOnly) ? true : false;
			}
		}
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
		_activeTool = CanvasModel.TRANSITION_TOOL;
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
	
	/**
	 * Resets the transition tool to its starting state, e.g. if one chas been created or the user released the mouse over an unsuitable clip
	 * @usage   
	 */
	public function resetTransitionTool():Void{
		//clear the transitions array
		_connectionActivities = new Array();
	}
	
	public function isTransitionToolActive():Boolean{
	   if(_activeTool == CanvasModel.TRANSITION_TOOL){
		   return true;
		}else{
			return false;
		}
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
				if(parentAct.activityTypeID == Activity.SEQUENCE_ACTIVITY_TYPE && !parentAct.isOptionalSequenceActivity(_cv.ddm.activities.get(parentAct.parentUIID))) {
					SequenceActivity(parentAct).empty = false;
					return r = "NEW_SEQ_CHILD";
				}
			
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
		var evtsTable:Hashtable = new Hashtable("evtsTable");
		
		if(activeRefresh) {
			_refreshQueueCount++;
			return;
		}
		
		startRefresh();
		
		//go through the design and see what has changed, compare DDM to canvasModel
		var ddmActivity_keys:Array = _cv.ddm.activities.keys();
		Debugger.log('ddmActivity_keys.length:'+ddmActivity_keys.length,Debugger.GEN,'refreshDesign','CanvasModel');
		
		var cmActivity_keys:Array = _activitiesDisplayed.keys();
		Debugger.log('cmActivity_keys.length:'+cmActivity_keys.length,Debugger.GEN,'refreshDesign','CanvasModel');

		var longest = Math.max(ddmActivity_keys.length, cmActivity_keys.length);
		
		//chose which array we are going to loop over
		var indexArray:Array;
		var compArray:Array;
		
		if(ddmActivity_keys.length == longest){
			indexArray = ddmActivity_keys;
			compArray = cmActivity_keys;
		}else{
			indexArray = cmActivity_keys;
			compArray = ddmActivity_keys;
		}
		
		refreshActivities(evtsTable, indexArray);
		refreshActivities(evtsTable, compArray);
		
		//now check the transitions:
		var ddmTransition_keys:Array = _cv.ddm.getValidTransitionsKeys();
		var cmTransition_keys:Array = _transitionsDisplayed.keys();
		
		var trLongest = Math.max(ddmTransition_keys.length, cmTransition_keys.length);
		
		//chose which array we are going to loop over
		var trIndexArray:Array;
		var trCompArray:Array;
		
		if(ddmTransition_keys.length == trLongest){
			trIndexArray = ddmTransition_keys;
			trCompArray = cmTransition_keys;
		}else{
			trIndexArray = cmTransition_keys;
			trCompArray = ddmTransition_keys;
		}
		
		refreshTransitions(evtsTable, trIndexArray);
		refreshTransitions(evtsTable, trCompArray);
		
		//now check the transitions:
		var ddmBranch_keys:Array = _cv.ddm.branches.keys();
		var cmBranch_keys:Array = _branchesDisplayed.keys();
		
		var brLongest = Math.max(ddmBranch_keys.length, cmBranch_keys.length);
		
		//chose which array we are going to loop over
		var brIndexArray:Array;
		var brCompArray:Array;
		
		if(ddmBranch_keys.length == brLongest){
			brIndexArray = ddmBranch_keys;
			brCompArray = cmBranch_keys;
		}else{
			brIndexArray = cmBranch_keys;
			brCompArray = ddmBranch_keys;
		}
		
		refreshBranches(evtsTable, brIndexArray);
		refreshBranches(evtsTable, brCompArray);
		
		broadcastViewUpdate("DRAW_ALL", evtsTable.values());
		stopRefresh();
		
		if(_refreshQueueCount > 0) {
			_refreshQueueCount = 0;
			refreshDesign();
		}
		
	}
	
	private function refreshActivities(evtsTable:Hashtable, keys:Array):Void {
		//loop through and do comparison
		for(var i=0;i<keys.length;i++){
			//check DDM against CM, DDM is king.
			var keyToCheck:Number = keys[i];
			
			var ddm_activity:Activity = _cv.ddm.activities.get(keyToCheck);
			var cm_activity:Activity = _activitiesDisplayed.get(keyToCheck).activity;
			
			/**if they are the same (ref should point to same act) then nothing to do.
			*  if the ddm does not have an act displayed then we need to remove it from the cm
			*  if the ddm has an act that cm does not ref, then we need to add it.
			**/
			
			var r_activity:Object = compareActivities(ddm_activity, cm_activity);
			
			Debugger.log('r_activity:'+r_activity,Debugger.GEN,'refreshActivities','CanvasModel');
			
			if(r_activity == "NEW"){
				//draw this activity
				//NOTE!: we are passing in a ref to the activity in the ddm so if we change any props of this, we are changing the ddm
				
				addModelEvent(evtsTable, ddm_activity.activityUIID, createViewUpdate("DRAW_ACTIVITY", ddm_activity));

			}else if(r_activity == "NEW_SEQ_CHILD"){
				addModelEvent(evtsTable, ddm_activity.activityUIID, createViewUpdate("DRAW_ACTIVITY_SEQ",ddm_activity));
			}else if(r_activity == "DELETE"){
				//remove this activity
				if(cm_activity.parentUIID == null){
					addModelEvent(evtsTable, cm_activity.activityUIID, createViewUpdate("REMOVE_ACTIVITY", cm_activity));
				}
			}else if(r_activity == "CHILD"){
				//dont ask the view to draw the activity if it is a child act				
				Debugger.log('Found a child activity, not drawing. activityID:'+ddm_activity.activityID+'parentID:'+ddm_activity.parentActivityID,Debugger.GEN,'refreshActivities','CanvasModel');
			}else if(r_activity == "SEQ"){
				addModelEvent(evtsTable, ddm_activity.activityUIID, createViewUpdate("ADD_SEQUENCE", ddm_activity));
			}
		}
	}
	
	private function refreshTransitions(evtsTable:Hashtable, keys:Array):Void {
		//loop through and do comparison
		for(var i=0;i<keys.length;i++){
			
			var transitionKeyToCheck:Number = keys[i];

			var ddmTransition:Transition = _cv.ddm.transitions.get(transitionKeyToCheck);
			var cmTransition:Transition = _transitionsDisplayed.get(transitionKeyToCheck).transition;
			var r_transition:Object = compareTransitions(ddmTransition, cmTransition);
			
			if(r_transition == "NEW"){
				//NOTE!: we are passing in a ref to the tns in the ddm so if we change any props of this, we are changing the ddm
				addModelEvent(evtsTable, ddmTransition.transitionUIID, createViewUpdate("DRAW_TRANSITION", ddmTransition));
			}else if(r_transition == "DELETE"){
				addModelEvent(evtsTable, cmTransition.transitionUIID, createViewUpdate("REMOVE_TRANSITION", cmTransition));
			}
		}
	}
	
	private function refreshBranches(evtsTable:Hashtable, keys:Array):Void {
		//loop through and do comparison
		for(var i=0;i<keys.length;i++){
			
			var branchKeyToCheck:Number = keys[i];

			var ddmBranch:Branch = _cv.ddm.branches.get(branchKeyToCheck);
			var cmBranch:Branch = _branchesDisplayed.get(branchKeyToCheck).branch;
			var r_branch:Object = compareBranches(ddmBranch, cmBranch);
			
			if(r_branch == "NEW"){
				//NOTE!: we are passing in a ref to the tns in the ddm so if we change any props of this, we are changing the ddm
				addModelEvent(evtsTable, ddmBranch.branchUIID, createViewUpdate("DRAW_BRANCH", ddmBranch));
			}else if(r_branch == "DELETE"){
				addModelEvent(evtsTable, cmBranch.branchUIID, createViewUpdate("REMOVE_BRANCH", cmBranch));
			}
		}
	}
	
	private function addModelEvent(table:Hashtable, UIID:Number, infoObj:Object):Boolean {
		if(!table.containsKey(UIID)) {
			table.put(UIID, infoObj);
			return true;
		}
		
		return false;
	}
	
	public function createViewUpdate(updateType, data):Object {
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.target = this;
		infoObj.updateType = updateType;
		infoObj.data = data;
		
		return infoObj;
	}
	
	/**
    * Notify registered listeners that a data model change has happened
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
    }
	
	public function startRefresh():Void {
		_activeRefresh = true;
		_refreshQueueCount = 0;
	}
	
	public function stopRefresh():Void {
		_activeRefresh = false;
	}
	
	public function get activeRefresh():Boolean {
		return _activeRefresh;
	}
	
	public function haltRefresh(a:Boolean):Void {
		_doRefresh = !a;
	}
	
	public function get isDirty() {
		return _isDirty;
	}
	
	public function setDirty(){
		_isDirty = true;
		
		if(getCanvas().ddm.learningDesignID == undefined){
			LFMenuBar.getInstance().enableExport(false);
		} else {
			LFMenuBar.getInstance().enableExport(true);
		}

		if(_doRefresh) refreshDesign();
	}
	
	public function clearAllElements():Void {
		var branch_keys:Array = _branchesDisplayed.keys();
		var act_keys:Array = _activitiesDisplayed.keys();
		var trans_keys:Array = _transitionsDisplayed.keys();
		
		for(var i=0; i<branch_keys.length; i++)
			_branchesDisplayed.get(branch_keys[i]).removeMovieClip();
		for(var i=0; i<trans_keys.length; i++)
			_transitionsDisplayed.get(trans_keys[i]).removeMovieClip();
		for(var i=0; i<act_keys.length; i++) {
			if(_activitiesDisplayed.get(act_keys[i]).activity.isBranchingActivity())
				_activitiesDisplayed.get(act_keys[i]).branchView.removeMovieClip();
			_activitiesDisplayed.get(act_keys[i]).removeMovieClip();
			
		}
		
		_branchesDisplayed.clear();
		_transitionsDisplayed.clear();
		_activitiesDisplayed.clear();
		
	}
	
	public function clearAllBranches(a:Activity):Void {
		var branch_keys:Array = _branchesDisplayed.keys();
		var act_keys:Array = _activitiesDisplayed.keys();
		Debugger.log("clearing branches: " + branch_keys.length, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
		
			
		for(var i=0; i<branch_keys.length; i++) {
			var branch:Branch = _branchesDisplayed.get(branch_keys[i]).branch;
			
			Debugger.log("branch: " + branch.branchUIID, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
		
			if(branch.sequenceActivity.parentUIID == a.activityUIID) {
				for(var j=0; j<act_keys.length; j++) {
					
					Debugger.log("activity parent: " + _activitiesDisplayed.get(act_keys[i]).activity.parentUIID, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
					Debugger.log("seq uiid: " + branch.sequenceActivity.activityUIID, Debugger.CRITICAL, "clearAllBranches", "CanvasModel");
					
					if(_activitiesDisplayed.get(act_keys[i]).activity.parentUIID == branch.sequenceActivity.activityUIID) {
						_activitiesDisplayed.remove(act_keys[i]);
					}
		
				}
			}
		}
		
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
	
	public function getParentActivity(act):Activity {
		 
		var selectedAct:Activity = (act == null) ? _cv.ddm.getActivityByUIID(selectedItem.activity.activityUIID) : act;
		var parentAct:Activity = _cv.ddm.getActivityByUIID(selectedAct.parentUIID);
		
		if(parentAct != null) {
			if(parentAct.isSequenceActivity() && SequenceActivity(parentAct).firstActivityUIID != selectedAct.activityUIID)
				return null;
			else if(parentAct.isSequenceActivity())
				return getParentActivity(parentAct);
			
			var transObj:Object = getCanvas().ddm.getTransitionsForActivityUIID(parentAct.activityUIID);
		
			return (transObj.into != null) ? _cv.ddm.getActivityByUIID(transObj.into.fromUIID) : getParentActivity(parentAct);
			
			
			return parentAct;
		}
		
		return null;
	}
	
	public function findParent(a:Activity, b:Activity):Boolean {
		if(a.activityUIID == b.activityUIID)
			return true;
		else if(a.isBranchingActivity())
			return false;
		else if(a.parentUIID == null)
			return false;
		else
			return findParent(_cv.ddm.getActivityByUIID(a.parentUIID), b);
    }
	
	public function setPIHeight(h:Number){
		_piHeight = h;
		Application.getInstance().onResize();
	}
	
	public function getPIHeight(){
		return _piHeight;
	}
	
	public function findOptionalActivities():Array{
		var actOptional:Array = new Array();
		var k:Array = _activitiesDisplayed.values();
		
		for (var i=0; i<k.length; i++){
			if (k[i].activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || k[i].activity.activityTypeID == Activity.OPTIONS_WITH_SEQUENCES_TYPE) {
				
				if(k[i].activity.parentUIID == null && (_activeView instanceof CanvasView))
					actOptional.push(k[i]);
				else if((_activeView instanceof CanvasBranchView) && findParent(k[i].activity, _activeView.activity))
					actOptional.push(k[i]);
			}
			
		}
		
		Debugger.log("returning length: " + actOptional.length, Debugger.CRITICAL, "findOptionalActivites", "CanvasSuperModel");
		
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
	
	public function get currentBranchingActivity():Object {
		return _currentBranchingActivity;
	}
	
	public function set currentBranchingActivity(a:Object) {
		_currentBranchingActivity = a;
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
	public function set activeTool(newactivetool:String):Void {
		_activeTool = newactivetool;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newactivetool 
	 * @return  
	 */
	public function setActiveTool(newactivetool):Void {
		_activeTool = newactivetool;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activeTool():String {
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
		
	/**
	 * Assign activityID of Optional activity as a parentID to the ca (canvas activity) 
	 * Which will draw child activities in Parent Optional Activity.
	 * @usage   
	 * @param   parentID (ActivityID of Optional Activity where canavas activity has been dropped.)
	 * @param   ca       (reference of the canvas activity to which parentID is assigned)
	 * @return  
	 */
	public function addParentToActivity(parentID, ca:Object, doRemoveParent:Boolean){
		ca.activity.parentUIID = parentID;
		
		Debugger.log('ParentId of '+ca.activity.activityUIID+ ' ==> '+ca.activity.parentUIID,Debugger.GEN,'addParentToActivity','CanvasModel');
		
		removeActivity(ca.activity.activityUIID);
		if(doRemoveParent) removeActivity(parentID);
		
		setDirty();
	}

	public function closeAllComplexViews():Void {
		while(activeView instanceof CanvasComplexView) {
			CanvasComplexView(activeView).close();
		}
	}

	public function getCoords(ca:Activity, o:Object):Object {
		var obj:Object = (o != null) ? o : new Object();
		
		if(obj.x != null && obj.y != null) {
			obj.x += ca.xCoord;
			obj.y += ca.yCoord;
		} else {
			obj.x = ca.xCoord;
			obj.y = ca.yCoord;
		}
			
		if(ca.parentUIID != null) {
			return getCoords(_cv.ddm.getActivityByUIID(ca.parentUIID), obj);
		} else {
			return obj;
		}
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
	
	public function set lastBranchActionType(a:Number):Void {
		_lastBranchActionType = a;
	}
	
	public function get lastBranchActionType():Number {
		return _lastBranchActionType;
	}
	
	public function get ddm():DesignDataModel {
		return _cv.ddm;
	}

}