﻿/***************************************************************************
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
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;

import org.lamsfoundation.lams.learner.ls.*;

import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;

import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.ComplexActivity;
import org.lamsfoundation.lams.authoring.GateActivity;
import org.lamsfoundation.lams.authoring.SequenceActivity;

import com.polymercode.Draw;

import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*
import mx.controls.*

/**  
* LearnerActivity
* 
* This class represets and controls the display of a simple tool or system activity in the Learner's progress bar. 
* The Progress Bar is displayed in the Learner application and in the Monitor application (Learner tab). 
* 
* A simple movieclip is displayed in the progess bar using an icon to identify the status of the activity in the progress bar.
* This class can also be nested within a LCA (Learner Complex Activity) at multiple levels.
* 
*/  
class LearnerActivity extends MovieClip { 
	
	public static var GATE_ACTIVITY_HEIGHT:Number =50;
	public static var GATE_ACTIVITY_WIDTH:Number = 50;
	public static var TOOL_ACTIVITY_WIDTH:Number = 123.1;
	public static var TOOL_ACTIVITY_HEIGHT:Number = 50.5;
	
	public static var LABEL_X:Number = 0;
	public static var LABEL_Y:Number = 10;
	public static var LABEL_W:Number = 130;
	public static var LABEL_H:Number = 22;
	
	private var xPos:Number;
	private var yPos:Number;
	
	//this is set by the init object
	private var _controller:AbstractController;
	private var _view:AbstractView;
	
	private var _activity:Activity;				//TODO:This should be ToolActivity
	private var _isSelected:Boolean;
	
	private var app:ApplicationParent;
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
	
	//locals
	private var tooltipTitle:String;
	private var actStatus:String;
	private var actLabel:String;
	private var learner:Progress;
	private var clickTarget_mc:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	private var attempted_mc:MovieClip;
	private var canvasActivity_mc:MovieClip;
	private var title_lbl:MovieClip;
	private var groupIcon_mc:MovieClip;
	private var stopSign_mc:MovieClip;	
	private var sentFrom:String;
	private var canvasActivityGrouped_mc:MovieClip;
	private var _dcStartTime:Number = 0;
	private var _doubleClickInterval:Number;
	private var _doubleClicking:Boolean;
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	private var _base_mc:MovieClip;
	private var _selected_mc:MovieClip;
	private var vertHairLineMC:MovieClip;
	
	private var _line_bottom:MovieClip;
	private var _line_top:MovieClip;
	
	// Identifies if the activity is a child of a complex activity and therefore nested inside a LCA and the level it is nested at.
	private var _complex:Boolean;
	private var _level:Number;
	
	private var _isReferenceChild:Boolean;
	
	/* Constructor */
	function LearnerActivity(){
		Debugger.log("_activity:"+_activity.title,4,'Constructor','LearnerActivity');
		Debugger.log("actStatus:"+ actStatus,4,'Constructor','LearnerActivity');
		Debugger.log("learner:"+ learner,4,'Constructor','LearnerActivity');
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip(_tm);
		
		_dcStartTime = new Date().getTime();
		 
		//Get reference to application and design data model
		app = ApplicationParent.getInstance();
		
		//let it wait one frame to set up the components.
		//this has to be set b4 the do later :)
		if(_activity.isGateActivity()){
			_visibleHeight = LearnerActivity.GATE_ACTIVITY_HEIGHT;
			_visibleWidth = LearnerActivity.GATE_ACTIVITY_WIDTH;
		} else if(_activity.isGroupActivity() || _activity.isBranchingActivity()) {
			_visibleHeight = LearnerActivity.TOOL_ACTIVITY_HEIGHT;
			_visibleWidth = LearnerActivity.TOOL_ACTIVITY_WIDTH;
		}
		
		//call init if we have passed in the _activity as an initObj in the attach movie,
		//otherwise wait as the class outside will call it
		if(_activity != undefined){
			init();
		}
		
	}
	
	/** 
	* Setup and draw the component. 
	* 
	*/
	public function init(initObj):Void{
		var styleObj = _tm.getStyleObject('smallLabel');
		var _autosize:String;
		
		if(initObj){
			_view = initObj._view;
			_controller = initObj._controller;
			_activity = initObj._activity;
			learner = initObj.learner;
		}
		
		_isReferenceChild = false;
		
		if (model instanceof LessonModel) {
			var parAct = model.getLearningDesignModel().getActivityByUIID(_activity.parentUIID);
			if (parAct.activityTypeID == Activity.REFERENCE_ACTIVITY_TYPE)
				_isReferenceChild = true;
		}
		
		if (_isReferenceChild) {
			LABEL_X = 80;
			LABEL_Y = -8;
		} else if(_complex){
			_autosize = "left";
			LABEL_X = 18;
			LABEL_Y = 2;
		} else {
			LABEL_X = 0
			LABEL_Y = 10
			_autosize = "center";
		}
		if (actLabel == undefined){
			tooltipTitle =  _activity.title;
		}else {
			tooltipTitle = actLabel;
		}
		
		title_lbl = this.attachMovie("Label", "Label"+_activity.activityID, this.getNextHighestDepth(), {_x:LABEL_X , _y:LABEL_Y, _width:LABEL_W, _height:LABEL_H, autoSize:_autosize, styleName:styleObj});
		
		showAssets(false);
		
		MovieClipUtils.doLater(Proxy.create(this, draw));

	}
	
	private function showAssets(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		canvasActivity_mc._visible = isVisible;
		todo_mc._visible = isVisible;
		attempted_mc._visible = isVisible
		title_lbl._visible = true;
	}
	
	/**
	 * Updates the LearnerActivity display fields with the current data
	 * @usage   
	 * @return  
	 */
	public function refresh():Void{
		showAssets(false);
		learner = (model instanceof LessonModel) ? model.progressData : learner;
		actStatus = null;
		
		draw();
	}
	
	/**
	 * Clear this component.
	 * 
	 */
	
	public function destroy():Void {
		title_lbl.removeMovieClip();
		this._visible = false;
		this.removeMovieClip();
	}
	
	/**
	 * Does the work of laying out the screen assets, displaying the title label and setting the progress icon.
	 * 
	 */
	private function draw(){
		
		var toolTitle:String;
		if (actStatus == null || actStatus == undefined){
			actStatus = Progress.compareProgressData(learner, _activity.activityID);
		}
		
		Debugger.log("activity: " + _activity.activityID + " // activity status : " + actStatus, Debugger.CRITICAL,'draw','org.lamsfoundation.lams.LearnerActivity');
	
		clickTarget_mc._visible = true;
		
		switch (actStatus){
		    case 'completed_mc' :
				if (_isReferenceChild && app.module == 'learner') {
					attempted_mc._visible = true;
				}else{
					// normal case
					completed_mc._visible = true;
				}
		        break;
            case 'current_mc' :
				current_mc._visible = true;
				break;
            case 'attempted_mc' :
			    attempted_mc._visible = true;
                break;
			default :
				todo_mc._visible = true;
		}
			
		//write text
		if (actLabel == undefined){
			toolTitle = _activity.title;
		}else {
			toolTitle = actLabel;
		}
		
		if (_isReferenceChild) {
			vertHairLineMC._visible = false;
		}
		
		Debugger.log("parent level: " + _parent._parent.level, Debugger.CRITICAL, "draw", "LearnerActivity");
		
		// Using a dash to indicate level depth when nested inside a LCA
		
		if(_parent._parent.level > 0) {
			for(var i=0; i<(_parent._parent.level + _parent._parent.complexLevel); i++)
				toolTitle = "-" + toolTitle;
		}
		
		if(level > 0)
			for(var i=0; i<level; i++)
				toolTitle = "-" + toolTitle;
		
		// Cut the title if too long
		if (toolTitle.length > 18){
				toolTitle = toolTitle.substr(0, 16)+"...";
		}
		
		title_lbl.text = toolTitle;
		
		// Continue drawing the next activity in the design if on top level.
		if(_view instanceof LearnerTabView && !_complex)
			LearnerTabView(_view).drawNext();
		
	}
	
	/**
	 * Display the tooltip.
	 *  
	 */
	
	public function showToolTip(btnObj, ttMessage:String):Void{
		Debugger.log("showToolTip invoked", Debugger.CRITICAL, "showToolTip", "LearnerActivity");
		var appData = getAppData();
		var ttHolder = appData.ttHolder;
		
		if (ttMessage == undefined || ttMessage == null || ttMessage == "" || ttMessage == "undefined"){
			ttMessage = "<b>"+ _activity.title+"</b>";
		} else {
			ttMessage = "<b>"+ _activity.title+"</b>\n" + ttMessage;
		}
		
		var ttWidth = 140;
		
		if (isReferenceChild) {
			var ttXpos = 10;
			var ttYpos = _root._ymouse - 43;
			
		} else if(_complex){
			var ttXpos = appData.compX + xPos;
			var ttYpos = appData.compY + yPos;
			
		} else {
			if(app.module == 'learner'){
				var ttXpos = appData.compX + this._x-10;
			}else {
				var ttXpos = appData.compX + this._x;
			}
			var targetHeightOffset:Number = (btnObj._height == undefined) ? 15 : btnObj._height;
			var ttYpos = appData.compY + this._y+targetHeightOffset;
		}
		
		if (isReferenceChild)
			ttHolder.swapDepths( (this._parent.getDepth()+1) ); // tooltips were showing below depth of this._parent, the reference pain
		
		_tip.DisplayToolTip(ttHolder, ttMessage, ttXpos, ttYpos, undefined, ttWidth);
		
	}
	
	// Returns true if the parent activity is a Reference Activity
	public function get isReferenceChild():Boolean {
		return _isReferenceChild;
	}
	
	/**
	 * Hide the tooltip.
	 *
	 */
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	/**
	 * Handles the rollOver event and displays a tooltip for the activity.
	 *  
	 */
	
	private function onRollOver(){
		
		// handle reference activities
		if (isReferenceChild) {
			showToolTip(this.clickTarget_mc, Dictionary.getValue("support_act_tooltip"));
			return;
		}
		
		if (actStatus == "completed_mc"){
			showToolTip(this.clickTarget_mc, Dictionary.getValue("completed_act_tooltip"));
			
		}else if (actStatus == "current_mc"){
			showToolTip(this.clickTarget_mc, Dictionary.getValue("current_act_tooltip"));
			
		}else { // Handle tooltips for not yet attempted activities
			var attemptedActs:Array = learner.getAttemptedActivities();
			var attempted:Boolean = false;
			for (var i=0; i<attemptedActs.length; ++i) {
				if (attemptedActs[i] == _activity.activityID) {
					attempted = true;
				}
			}
			if (!attempted) { // Check if it's a gate activity
				if(app.module == 'learner') {
					if (_activity.activityTypeID == Activity.SYNCH_GATE_ACTIVITY_TYPE) {
						showToolTip(this.clickTarget_mc, Dictionary.getValue("synchronise_gate_tooltip"));
					
					} else if (_activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE) {
						
						var gateReleaseDate:Date = new Date(_activity.createDateTime); // copy Date instance
						gateReleaseDate.setMinutes(gateReleaseDate.getMinutes() + GateActivity(_activity).gateStartTimeOffset);
						showToolTip(this.clickTarget_mc, Dictionary.getValue("schedule_gate_tooltip", [gateReleaseDate.toString()]));
						
					} else if (_activity.activityTypeID == Activity.PERMISSION_GATE_ACTIVITY_TYPE) {
						showToolTip(this.clickTarget_mc, Dictionary.getValue("permission_gate_tooltip"));
					
					} else { // Otherwise show the default message for non attempted activities
						showToolTip(this.clickTarget_mc, Dictionary.getValue("not_attempted_act_tooltip"));
					}
				}
			}
		}
	}
	
	/**
	 * Handles the rollOut event and removes the tooltip.
	 * 
	 */
	
	private function onRollOut(){
		
		hideToolTip();
	}
	
	/**
	 * Handles the onPress (doubleClick) event.
	 * DoubleClick will open the activity's learner/monitor page.
	 */
	
	public function onPress():Void{
			var c = (_controller instanceof LessonController) ? LessonController(_controller) : MonitorController(_controller);
			
			// check double-click
			var now:Number = new Date().getTime();
			Debugger.log("LearnerActivity->onPress: (now - _dcStartTime)"+(now - _dcStartTime), Debugger.GEN, "onPress", "LearnerActivity");
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
					_doubleClicking = true;
					if (_doubleClickInterval != null && _doubleClickInterval != undefined) {
						clearInterval(_doubleClickInterval);
						Cursor.showCursor(ApplicationParent.C_DEFAULT);
					}
					c.activityDoubleClick(this);
					Debugger.log('DoubleClicking:+'+this,Debugger.GEN,'onPress','LearnerActivity');
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','LearnerActivity');
				_doubleClicking = false;
			}
			
			_dcStartTime = now;
			
			hideToolTip();
	
	}
	
	/**
	 * Handles the onRelease event.
	 * Assists with setting active sequence/complex activities of a LCA when the component is manually selected to be opened/closed.
	 * 
	 */
	
	public function onRelease():Void{
		var c = (_controller instanceof LessonController) ? LessonController(_controller) : MonitorController(_controller);
			
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','LearnerActivity');
			Debugger.log('Is sequence:'+this.activity.isSequenceActivity(),Debugger.GEN,'onRelease','LearnerActivity');
			
			Debugger.log('cmap:'+LearnerComplexActivity(_parent._parent).sequenceMap,Debugger.CRITICAL,'onRelease','LearnerActivity');
			Debugger.log('cmap length:'+LearnerComplexActivity(_parent._parent).sequenceMap.values().length,Debugger.CRITICAL,'onRelease','LearnerActivity');
			
			Debugger.log('activity uiid:'+this.activity.activityUIID,Debugger.CRITICAL,'onRelease','LearnerActivity');
			Debugger.log('c value :'+ LearnerComplexActivity(_parent._parent).sequenceMap.containsValue(SequenceActivity(this.activity)),Debugger.CRITICAL,'onRelease','LearnerActivity');
			Debugger.log('c title :'+ LearnerComplexActivity(_parent._parent).sequenceMap.values()[0].title,Debugger.CRITICAL,'onRelease','LearnerActivity');
			
			
			var activeComplex = LearnerComplexActivity(_parent._parent).complexMap.get(this.activity.activityUIID);
			var activeSequence = LearnerComplexActivity(_parent._parent).sequenceMap.get(this.activity.activityUIID);
					
			if(this.activity.isSequenceActivity()) {
				Debugger.log('parent :'+this._parent._parent, Debugger.CRITICAL,'onRelease','LearnerActivity');
				Debugger.log('activeSequence:'+activeSequence, Debugger.CRITICAL,'onRelease','LearnerActivity');
				
				if(LearnerComplexActivity(this._parent._parent).activity.activityUIID == this.activity.parentUIID) {
				
					_doubleClickInterval = setInterval(Proxy.create(this, selectActiveSequence, activeSequence), (Config.DOUBLE_CLICK_DELAY - 100));
					Cursor.showCursor(ApplicationParent.C_HOURGLASS); 
				}
			} else if(this.activity.isOptionsWithSequencesActivity() || this.activity.isOptionalActivity() || this.activity.isParallelActivity() || this.activity.isBranchingActivity()) {
				Debugger.log('activeComplex:'+activeComplex, Debugger.CRITICAL,'onRelease','LearnerActivity');
					
				if(model.findParent(this.activity, LearnerComplexActivity(this._parent._parent).activity) || (activeSequence.activityUIID == this.activity.parentUIID)) {
					if(activeComplex != null) {
						// close current active complex
						LearnerComplexActivity(this._parent._parent).removeAllChildrenAndInputComplex(null, null, true);
					} else {
						// open complex
						LearnerComplexActivity(this._parent._parent).removeAllChildrenAndInputComplex(this.activity, this.level, true);
					}
				}
			}
				
			c.activityRelease(this);
			
		}
		
	}
	
	private function selectActiveSequence(activeSequence):Void {
		
		if (_doubleClickInterval != null && _doubleClickInterval != undefined) {
			clearInterval(_doubleClickInterval);
			Cursor.showCursor(ApplicationParent.C_DEFAULT);
		}
		
		LearnerComplexActivity(this._parent._parent).setActiveComplex(null); //****
		
		if(activeSequence) {
			// close current active sequence
			LearnerComplexActivity(this._parent._parent).removeAllChildrenAndInputSequence(null, true);
		} else {
			// open sequence
			LearnerComplexActivity(this._parent._parent).removeAllChildrenAndInputSequence(this.activity, true);
		}
	}
	
	public function onReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','LearnerActivity');
		
		controller.activityReleaseOutside(this);
	}
	
	public function get progressData():Progress {
		return learner;
	}
	
	public function set progressData(a:Progress){
		learner = a;
	}
	
	public function get controller(){
		if(_controller instanceof LessonController){
			return LessonController(_controller);
		} else {
			return MonitorController(_controller);
		}
	}
	
	public function set controller(a:AbstractController){
		_controller = a;
	}
	
	public function get model(){
		if(app.module == 'learner')
			return LessonModel(_controller.getModel());
		else
			return MonitorModel(_controller.getModel());
	}
	
	public function getAppData():Object{
		return controller.appData;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getVisibleWidth ():Number {
		return _visibleWidth;
	}

	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getVisibleHeight ():Number {
		return _visibleHeight;
	}
	
	public function get activity():Activity{
		return getActivity();
	}
	
	public function set activity(a:Activity){
		setActivity(a);
	}
	
	public function getActivity():Activity{
		return _activity;
	}
	
	public function setActivity(a:Activity){
		_activity = a;
	}

	public function get activityStatus():String{
		return actStatus;
	}
	
	public function set activityStatus(a:String){
		actStatus = a;
	}
	
	public function get learnerID():Number{
		return learner.getLearnerId();
	}
	
	public function get learnerName():String{
		return learner.getFullName();
	}
	
	public function set lineTopVisible(a:Boolean):Void {
		_line_top._visible = a;
	}
	
	public function set lineBottomVisible(a:Boolean):Void {
		_line_bottom._visible = a;
	}
	
	public function get isCurrent():Boolean {
		return (actStatus == 'current_mc');
	}
	
	public function get isCompleted():Boolean {
		return (actStatus == 'completed_mc');
	}
	
	public function get isAttempted():Boolean {
		return (actStatus == 'attempted_mc');
	}
	
	public function get level():Number {
		return _level;
	}
}