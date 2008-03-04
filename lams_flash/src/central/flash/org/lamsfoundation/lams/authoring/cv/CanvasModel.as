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
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.br.BranchConnector;
import org.lamsfoundation.lams.authoring.br.CanvasBranchView;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.dict.*;
import mx.events.*;

/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.authoring.cv.CanvasModel extends org.lamsfoundation.lams.authoring.cv.CanvasSuperModel {

	public static var TRANSITION_TOOL:String = "TRANSITION";  //activie tool ID strings definition
	public static var OPTIONAL_TOOL:String = "OPTIONAL";
	public static var OPTIONAL_SEQ_TOOL:String = "OPTIONAL_SEQ";
	public static var GATE_TOOL:String = "GATE";
	public static var GROUP_TOOL:String = "GROUP";
	public static var BRANCH_TOOL:String = "BRANCH";
	
	public static var OPEN_FROM_FILE:Number = 0;
	public static var ADD_FROM_TEMPLATE:Number = 1;
	

	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	/**
	* Constructor.
	*/
	public function CanvasModel(cv:Canvas){
		super(cv);
		
		 //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
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
	 * Create a transition between two activities in a Sequence Activity (in Optional).
	 * 
	 * @usage   
	 * @param   sequence   
	 * @param   toActivity 
	 * @return  
	 */
	
	public function createSequenceTransition(fromActivity:Activity, toActivity:Activity):Void {
	/**	var fromActivity:Activity;
		var _children:Array = _cv.ddm.getComplexActivityChildren(sequence.activityUIID);
		_children.sortOn('orderID', Array.NUMERIC);
		
		Debugger.log("toActivity orderID " + toActivity.orderID, Debugger.CRITICAL, "createSequenceTransition", "CanvasModel");
		var _index:Number = toActivity.orderID - 2;
		if(_index >= 0) {
			fromActivity = _children[_index];
			Debugger.log("fromActivity " + fromActivity.activityUIID, Debugger.CRITICAL, "createSequenceTransition", "CanvasModel");
		*/
			addActivityToTransition(fromActivity);
			addActivityToTransition(toActivity);
			resetTransitionTool();
		//}
		
		 setDirty();
	 }
	
	/**
	 * Creates a new gate activity at the specified location
	 * @usage   
	 * @param   gateTypeID 
	 * @param   pos        
	 * @return  
	 */
	public function createNewGate(gateTypeID, pos:Point, parent){
		var gateAct = new GateActivity(_cv.ddm.newUIID(), gateTypeID);
		gateAct.learningDesignID = _cv.ddm.learningDesignID;
		
		gateAct.title = Dictionary.getValue('gate_btn');
		gateAct.yCoord = pos.y;
		gateAct.xCoord = pos.x;

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
	
	public function createNewSequenceActivity(parent, orderID, stopAfterActivity:Boolean, isBranch:Boolean){
		Debugger.log('Running...',Debugger.GEN,'createNewSequenceActivity','CanvasModel');
		
		var seqAct = new SequenceActivity(_cv.ddm.newUIID());
		var title = (isBranch) ? Dictionary.getValue('branch_mapping_dlg_branch_col_lbl') : Dictionary.getValue('sequence_act_title');
		seqAct.title =  Dictionary.getValue('sequence_act_title_new', [title, orderID]);
		seqAct.learningDesignID = _cv.ddm.learningDesignID;
		seqAct.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
		seqAct.activityCategoryID = Activity.CATEGORY_SYSTEM;
		seqAct.orderID = (orderID != null || orderID != undefined) ? orderID : 1;
		seqAct.stopAfterActivity = (stopAfterActivity != null) ? stopAfterActivity : true;
		
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
	public function createNewOptionalActivity(ActivityTypeID, pos:Point, parent, isSequence:Boolean){
		var optAct = new ComplexActivity(_cv.ddm.newUIID());
		
		optAct.learningDesignID = _cv.ddm.learningDesignID;
		optAct.activityTypeID = (!isSequence) ? Activity.OPTIONAL_ACTIVITY_TYPE : Activity.OPTIONS_WITH_SEQUENCES_TYPE;
		optAct.title = (!isSequence) ? Dictionary.getValue('opt_activity_title') : Dictionary.getValue('opt_activity_seq_title');
		optAct.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
		optAct.activityCategoryID = Activity.CATEGORY_SYSTEM;
		optAct.yCoord = pos.y;
		optAct.xCoord = pos.x;
		
		Debugger.log('Optional Activitys Y Coord is :'+optAct.yCoord,Debugger.GEN,'createNewOptionalActivity','CanvasModel');
		
		if(parent != null) {
			optAct.parentActivityID = parent.activityID;
			optAct.parentUIID = parent.activityUIID;
		}
		
		if(isSequence) {
			createNewSequenceActivity(optAct, 1, false);
			createNewSequenceActivity(optAct, 2, false);
			optAct.noSequences = 2;
		}
		
		_cv.ddm.addActivity(optAct);
		
		setDirty();
		setSelectedItem(_activitiesDisplayed.get(optAct.activityUIID));
		
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
		haltRefresh(true);
		
		ca.activity.parentUIID = (activeView instanceof CanvasBranchView) ? activeView.defaultSequenceActivity.activityUIID : null;
		ca.activity.orderID = null;
		ca.activity.parentActivityID = (activeView instanceof CanvasBranchView) ? activeView.defaultSequenceActivity.activityID : null;
		
		if(ca.activity.isBranchingActivity())
			ca.activity.clear = true;
		
		removeActivity(ca.activity.activityUIID);
		removeActivity(parentID);
		
		haltRefresh(false);
		setDirty();
		
	}
	
	public function removeOptionalSequenceCA(ca:Object, parentID){
		haltRefresh(true);

		unhookOptionalSequenceCA(ca);
		
		ca.activity.parentUIID = (activeView instanceof CanvasBranchView) ? activeView.defaultSequenceActivity.activityUIID : null;
		ca.activity.orderID = null;
		ca.activity.parentActivityID = (activeView instanceof CanvasBranchView) ? activeView.defaultSequenceActivity.activityID : null;
		
		if(!(activeView instanceof CanvasComplexView)) removeActivity(parentID);
		
		haltRefresh(false);
		setDirty();
	}
	
	private function unhookOptionalSequenceCA(ca:Object) {
		var transitionObj:Object = _cv.ddm.getTransitionsForActivityUIID(ca.activity.activityUIID);
		var sequence:Activity = _cv.ddm.getActivityByUIID(ca.activity.parentUIID);
		
		var toActivity:Activity = null;
		var fromActivity:Activity = null;
		var parentAct:SequenceActivity = SequenceActivity(sequence);
		
		if(transitionObj.into != null && transitionObj.out != null) {
			toActivity = _cv.ddm.getActivityByUIID(transitionObj.out.toUIID);
			fromActivity = _cv.ddm.getActivityByUIID(transitionObj.into.fromUIID);
		} else if(transitionObj.out != null) {
			parentAct.firstActivityUIID = transitionObj.out.toUIID;
		} else if(transitionObj.out == null && transitionObj.into == null){
			parentAct.firstActivityUIID = null;
		}
		
		_cv.ddm.removeTransitionByConnection(ca.activity.activityUIID);
		if(toActivity != null && fromActivity != null) createSequenceTransition(fromActivity, toActivity);
		
	}
	
	public function moveOptionalSequenceCA(ca:Object, parent:Activity):Boolean {
		var minDiff:Number = null;
		var selectedIndex:Number = null;
		
		Debugger.log("ca y: " + ca._y, Debugger.CRITICAL, "moveOptionalSequenceCA", "CanvasModel");
		Debugger.log("parent y: " + parent.yCoord, Debugger.CRITICAL, "moveOptionalSequenceCA", "CanvasModel");
		
		if(ca._y > ca._parent._parent.getVisibleHeight() || ca._y < -ca.getVisibleHeight())
			return false;
		
		var oChildren:Array = _cv.ddm.getComplexActivityChildren(parent.activityUIID);
		oChildren.sortOn('orderID', Array.NUMERIC);
		
		for(var i=0; i<oChildren.length; i++) {
			var diff:Number = oChildren[i].xCoord - ca._x;
			
			if((minDiff == null || diff < minDiff) && diff > 0 && ca.activity.xCoord > ca._x) {
				minDiff = diff;
				selectedIndex = i;
			} else if((minDiff == null || diff > minDiff) && diff < 0 && ca.activity.xCoord < ca._x) {
				minDiff = diff;
				selectedIndex = i;
			}
		}
		
		Debugger.log("selectedIndex: " + selectedIndex, Debugger.CRITICAL, "moveOptionalSequenceCA", "CanvasModel");
		if(selectedIndex != null) {
			if(oChildren[selectedIndex] != ca.activity) {
				// remove ca from sequence
				Debugger.log("selectedIndex order: " + Activity(oChildren[selectedIndex]).orderID, Debugger.CRITICAL, "moveOptionalSequenceCA", "CanvasModel");
				Debugger.log("ca order: " + ca.activity.orderID, Debugger.CRITICAL, "moveOptionalSequenceCA", "CanvasModel");
			
				//var _dir:Number = (Activity(oChildren[selectedIndex]).orderID < ca.activity.orderID) ? 0 : 1;
				var _dir:Number = (ca.activity.xCoord > ca._x) ? 0 : 1;
				
				unhookOptionalSequenceCA(ca);
				addOptionalSequenceCA(ca, oChildren[selectedIndex], _dir);
				
				return true;
			} 
		}
		
		return false;
	}
	 
	private function addOptionalSequenceCA(ca:Object, nextOrPrevActivity:Activity, _dir:Number):Void {
		haltRefresh(true);
		
		Debugger.log("ca: " + ca.activity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		
		var sequence:Activity = _cv.ddm.getActivityByUIID(nextOrPrevActivity.parentUIID);
		var transitionObj:Object = _cv.ddm.getTransitionsForActivityUIID(nextOrPrevActivity.activityUIID);
		
		var targetActivity:Activity = null;
		
		var transition:Transition = (_dir == 0) ? transitionObj.into : transitionObj.out;
		Debugger.log("transition length: " + transitionObj.myTransitions.length, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		
		if(transition != null) {
			targetActivity = (_dir == 0) ? _cv.ddm.getActivityByUIID(transition.fromUIID) :  _cv.ddm.getActivityByUIID(transition.toUIID);
		} else {
			if(_dir == 0) ComplexActivity(sequence).firstActivityUIID = ca.activity.activityUIID;
		}
		
		Debugger.log("targetActivity order: " + targetActivity.orderID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		Debugger.log("transition toUIID: " + transition.toUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		Debugger.log("transition fromUIID: " + transition.fromUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		
		Debugger.log("target UIID: " +targetActivity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		Debugger.log("nextOrPrevActivity: " + nextOrPrevActivity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		Debugger.log("direction: " + _dir, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		
		//_cv.ddm.removeTransitionByConnection(ca.activity.activityUIID);
		
		if(targetActivity != null) {
			var fromActivity:Activity = (_dir == 0) ? targetActivity : nextOrPrevActivity;
			var toActivity:Activity = (_dir == 0) ? nextOrPrevActivity : targetActivity;
			
			Debugger.log("fromActivity: " + fromActivity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
			Debugger.log("toActivity: " + toActivity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
			
			_cv.ddm.removeTransition(transition.transitionUIID);
			
			createSequenceTransition(fromActivity, ca.activity);
			createSequenceTransition(ca.activity, toActivity);
			
		} else {
			var fromActivity:Activity = (_dir == 0) ? ca.activity : nextOrPrevActivity;
			var toActivity:Activity = (_dir == 0) ? nextOrPrevActivity : ca.activity;
			
			Debugger.log("fromActivity: " + fromActivity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
			Debugger.log("toActivity: " + toActivity.activityUIID, Debugger.CRITICAL, "addOptionalSequenceCA", "CanvasModel");
		
			createSequenceTransition(fromActivity, toActivity);
		}

		if(activeView instanceof CanvasComplexView && activeView.openActivity.activity.activityUIID == sequence.parentUIID) {
			activeView.updateActivity();
		} else {
			removeActivity(sequence.parentUIID);
		}
		
		haltRefresh(false);

		setDirty();
		
	}
	 
	/**
	*Called by the controller when a complex activity is dropped on bin.
	*/
	public function removeComplexActivity(ca){
		Debugger.log('Removing Complex Activity: ' + ca.activity.activityUIID,Debugger.GEN,'removeComplexActivity','CanvasModel');
		haltRefresh(true);
		
		// recursively remove all children
		removeComplexActivityChildren(ca.actChildren);
		removeActivityOnBin(ca.activity.activityUIID);
		
		haltRefresh(false);
		setDirty();
	}
	
	public function removeComplexActivityChildren(children){
		for (var k=0; k<children.length; k++){
			Debugger.log('Removing Child ' + children[k].activityUIID+ 'from : '+ children[k].parentUIID,Debugger.GEN,'removeComplexActivityChildren','CanvasModel');
		
			children[k].parentUIID = null;
			
			// TODO: use method to determine is complex by type
			if(children[k].activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || children[k].activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || children[k].activityTypeID == Activity.SEQUENCE_ACTIVITY_TYPE ) {
				this.removeComplexActivityChildren(_cv.ddm.getComplexActivityChildren(children[k].activityUIID));
			} else {
				removeActivity(children[k].activityUIID);
			}
			
			_cv.removeActivity(children[k].activityUIID);
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
	
	public function addNewBranch(sequence:SequenceActivity, branchingActivity:BranchingActivity, isDefault:Boolean):Void {
		
		if(sequence.firstActivityUIID == null && _cv.ddm.getComplexActivityChildren(sequence.activityUIID).length <= 0) {
		
			var b:Branch = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_SINGLE, branchingActivity.activityUIID, null, sequence, _cv.ddm.learningDesignID);
			_cv.ddm.addBranch(b);
		
		} else if(sequence.firstActivityUIID != null) {
			var b:Branch = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_FROM_START, _cv.ddm.getActivityByUIID(sequence.firstActivityUIID).activityUIID, branchingActivity.activityUIID, sequence, _cv.ddm.learningDesignID);
			_cv.ddm.addBranch(b);
		
			// TODO: review
			if(!sequence.stopAfterActivity) {
				b = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_TO_END, _cv.ddm.getActivityByUIID(this.getLastActivityUIID(sequence.firstActivityUIID)).activityUIID, branchingActivity.activityUIID, sequence, _cv.ddm.learningDesignID);
			
				_cv.ddm.addBranch(b);
			}
			
			if(isDefault)
				branchingActivity.defaultBranch = b;
		}
		
		setDirty();
	}
	
	private function getLastActivityUIID(activityUIID:Number):Number {
		
		// get next activity from transition
		var transObj = _cv.ddm.getTransitionsForActivityUIID(activityUIID);
		
		return (transObj.out == null) ? activityUIID : getLastActivityUIID(transObj.out.toUIID);
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
		
		var fromAct = _connectionActivities[0].activityUIID;
		var toAct = _connectionActivities[1].activityUIID;
		
		//check we have 2 valid acts to create the transition.
		if(fromAct == toAct){
			// create activityless branch
			var b:Object = (activeView.fingerprint == activeView.endHub) ? createActivitylessBranch() : new LFError("Trying to create branch in wrong direction.");
			
			if(b instanceof LFError) {
				return b;
			} else if(b != null){
				var success:Object = _cv.ddm.addBranch(Branch(b));
			}
			
			//flag the model as dirty and trigger a refresh
			_cv.stopTransitionTool();
			
			setDirty();
			
			return true;
			
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
				//b.sequenceActivity.stopAfterActivity = false;
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
			
			var branch = _cv.ddm.getBranchesForActivityUIID(toAct);
			if(branch.target != null && branch.target.direction == BranchConnector.DIR_FROM_START) {
				
				return new LFError("Can't create transition between activities in different branches", "addActivityToTransition", this);
			}
			
			var branchesArray:Array = _cv.ddm.branches.values();
			for(var i=0; i<branchesArray.length; i++) {
				if(branchesArray[i].targetUIID == toAct && branchesArray[i].direction == BranchConnector.DIR_TO_END) {
					//return new LFError("Cannot connect a new transition a closed sequence");
					
				}
				
				if(branchesArray[i].targetUIID == fromAct && branchesArray[i].direction == BranchConnector.DIR_TO_END ) {
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
		} else if(branchesSize > 0 && isLoopingLD(toAct.activityUIID, sequence.firstActivityUIID)) {
			return new LFError("Cannot create start-branch connection to Activity in a already connected Sequence.", "createBranchStartConnector", this);
		} else {
			
			var b = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_FROM_START, toAct.activityUIID, activeView.startHub.activity.activityUIID, activeView.defaultSequenceActivity, _cv.ddm.learningDesignID);
			b.sequenceActivity.isDefault = false;
			
			var sequences:Array = _cv.ddm.getComplexActivityChildren(sequence.parentUIID);
			//b.sequenceActivity.orderID = sequences.length;
			//b.setDefaultSequenceName();
			
			sequences.sortOn("orderID", Array.NUMERIC);
			var orderID:Number = (sequences.length > 0) ? sequences[sequences.length-1].orderID : 0;
			
			Debugger.log("sequences length (order id): " + orderID, Debugger.CRITICAL, "createBranchStartConnector", "CanvasModel");
			
			createNewSequenceActivity(activeView.activity, orderID+1, null, true);
			
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
			Debugger.log("looping: " + isLoopingLD(fromAct, _cv.ddm.getActivityByUIID(sequence.firstActivityUIID)), Debugger.CRITICAL, "createBranchStartConnector", "CanvasModel");
			
			var condition:Boolean = (sequence.firstActivityUIID != null && isLoopingLD(fromAct.activityUIID, sequence.firstActivityUIID));
			Debugger.log("condition: " + condition, Debugger.CRITICAL, "createBranchStartConnector", "CanvasModel");
			
			if(condition || sequence.firstActivityUIID == fromAct.activityUIID)
				return new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_TO_END, fromAct.activityUIID, activeView.endHub.activity.activityUIID, sequence, _cv.ddm.learningDesignID);
			else
				return new LFError("Cannot create end-branch connection to an unconnected Sequence.", "createBranchStartConnector", this);
		}
	}
	
	private function createActivitylessBranch():Object{
		if(_cv.ddm.getBranchesForActivityUIID(activeView.startHub.activity.activityUIID).activityless != null)
			return new LFError("Cannot add more than one Activityless branch.", null);
		
		var b:Branch = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_SINGLE, activeView.startHub.activity.activityUIID,  null, activeView.defaultSequenceActivity, _cv.ddm.learningDesignID);
		var sequences:Array = _cv.ddm.getComplexActivityChildren(b.sequenceActivity.parentUIID);
		
		SequenceActivity(b.sequenceActivity).isDefault = false;
		//b.sequenceActivity.orderID = sequences.length;
		//b.setDefaultSequenceName();
			
		sequences.sortOn("orderID", Array.NUMERIC);
		var orderID:Number = (sequences.length > 0) ? sequences[sequences.length-1].orderID : 0;
		
		Debugger.log("orderID: " + orderID, Debugger.CRITICAL, "createActivitylessBranch", "CanvasModel");
			
		createNewSequenceActivity(activeView.activity, orderID+1, null, true);
			
		return b;
	}

	public function migrateActivitiesToSequence(sequenceToMigrate:SequenceActivity, targetSequence:SequenceActivity):Boolean {
		var activitiesToMigrate:Array = _cv.ddm.getComplexActivityChildren(sequenceToMigrate.activityUIID);
		
		for(var i=0; i<activitiesToMigrate.length; i++) {
			addParentToActivity(targetSequence.activityUIID, _activitiesDisplayed.get(activitiesToMigrate[i].activityUIID), false); 
		}
		
		return true;
	}

	public function moveActivitiesToBranchSequence(activityUIID:Number, sequence:SequenceActivity):Boolean {
		// move first activity
		var ca = _activitiesDisplayed.get(activityUIID);
		
		Debugger.log("sequence uiid: " + sequence.activityUIID, Debugger.CRITICAL, "moveActivitiesToBranchSequence", "CanvasModel");
		Debugger.log("ca.activity.parentUIID: " + ca.activity.parentUIID, Debugger.CRITICAL, "moveActivitiesToBranchSequence", "CanvasModel");
		
		if(sequence.activityUIID != ca.activity.parentUIID) {
			addParentToActivity(sequence.activityUIID, ca, false);
		} else {
			return true;
		}
		
		// get next activity from transition
		var transObj = _cv.ddm.getTransitionsForActivityUIID(activityUIID);
		var branches = _cv.ddm.getBranchesForActivityUIID(activityUIID);
		
		if(transObj.out == null && branches.target != null) {
			if(branches.target.sequenceActivity.activityUIID != sequence.activityUIID && branches.target.direction == BranchConnector.DIR_TO_END) {
				
				Debugger.log("end branch found: " + branches.target.branchUIID, Debugger.CRITICAL, "moveActivitiesToBranchSequence", "CanvasModel");
				Debugger.log("tar seq: " + branches.target.sequenceActivity.activityUIID + " = " + sequence.activityUIID, Debugger.CRITICAL, "moveActivitiesToBranchSequence", "CanvasModel");
				
				if(!branches.target.sequenceActivity.stopAfterActivity) {
					
					_cv.removeBranch(branches.target.branchUIID);
					MovieClipUtils.doLater(Proxy.create(this, moveBranchToSequence, activityUIID, sequence));
					
					Debugger.log("stopAfterActivity: " + sequence.stopAfterActivity, Debugger.CRITICAL, "moveActivitiesToBranchSequence", "CanvasModel");
				}
				
			}
		}
		
		return (transObj.out == null) ? true : moveActivitiesToBranchSequence(transObj.out.toUIID, sequence);

	}
	
	private function moveBranchToSequence(activityUIID:Number, sequence:SequenceActivity):Void {
		var b:Branch = new Branch(_cv.ddm.newUIID(), BranchConnector.DIR_TO_END, activityUIID, sequence.parentUIID, sequence, _cv.ddm.learningDesignID);
		_cv.ddm.addBranch(b);
		setDirty();
	}
	
	
	public function setDesignTitle(){
		broadcastViewUpdate("POSITION_TITLEBAR", null);
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
				
				// TODO: Maybe add learningDesignID and serverURL to window title to handle multiple LAMS(s) running in same browser session.
				JsPopup.getInstance().launchPopupWindow(url, 'ToolActivityContent_' + ta.toolContentID, 600, 800, true, true, false, false, false);
			
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
	
	public function openBranchActivityContent(ba, visible:Boolean):Void {
		currentBranchingActivity = ba;
		
		if(visible == null) visible = true;
		
		Debugger.log("visible: " + visible, Debugger.CRITICAL, "openBranchActivityContent", "CanvasModel");
		Debugger.log("currentBranchingActivity UIID: " + currentBranchingActivity.activity.activityUIID, Debugger.CRITICAL, "openBranchActivityContent", "CanvasModel");
		Debugger.log("branchView: " + ba.activity.branchView, Debugger.CRITICAL, "openBranchActivityContent", "CanvasModel");
		
		if(BranchingActivity(ba.activity).clear) {
			clearBranchingActivity(ba);
		}
		
		if(ba.activity.branchView != null) {
			activeView = (visible) ? ba.activity.branchView : activeView;
			ba.activity.branchView.setOpen(visible);
			ba.activity.branchView.open();
			
			openBranchingActivities.push(ba);
		} else { _cv.openBranchView(currentBranchingActivity, visible); }
		
		_lastBranchActionType = null;
	}

}
