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
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.common.style.*;

import com.polymercode.Draw;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*
import mx.controls.*

/**  
* LearnerActivity - 
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
	private var _tm:ThemeManager;
	
	//TODO:This should be ToolActivity
	private var _activity:Activity;
	private var _isSelected:Boolean;
	private var app:ApplicationParent;
	private var _tip:ToolTip;
	
	//locals
	private var tooltipTitle:String;
	private var actStatus:String;
	private var actLabel:String;
	private var learner:Progress;
	private var clickTarget_mc:MovieClip;
	//private var completed_mc:MovieClip;
	//private var current_mc:MovieClip;
	//private var todo_mc:MovieClip;
	//private var attempted_mc:MovieClip;
	private var canvasActivity_mc:MovieClip;
	private var title_lbl:MovieClip;
	private var groupIcon_mc:MovieClip;
	private var stopSign_mc:MovieClip;	
	private var sentFrom:String;
	private var canvasActivityGrouped_mc:MovieClip;	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	private var _base_mc:MovieClip;
	private var _selected_mc:MovieClip;
	
	private var _line_bottom:MovieClip;
	private var _line_top:MovieClip;
	private var icon_mc:MovieClip;
	
	private var _complex:Boolean;
	
	function LearnerActivity(){
		Debugger.log("_activity:"+_activity.title,4,'Constructor','Activity');
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
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
		
		//_base_mc = this;
		//call init if we have passed in the _activity as an initObj in the attach movie,
		//otherwise wait as the class outside will call it

		if(_activity != undefined){
			init();
		}
		
	}
	
	public function init(initObj):Void{
		var styleObj = _tm.getStyleObject('smallLabel');
		var _autosize:String;
		
		if(initObj){
			
				_view = initObj._view;
				_controller = initObj._controller;
				_activity = initObj._activity;
				learner = initObj.learner;
		}
		
		if(_complex){
			_autosize = "left";
			LABEL_X = 18;
			LABEL_Y = 2;
		} else {
			LABEL_X = 0
			LABEL_Y = 10
			_autosize = "center";
		}
		if (actLabel == undefined){
			tooltipTitle =  _activity.title
		}else {
			tooltipTitle = actLabel
		}
		
		title_lbl = this.attachMovie("Label", "Label"+_activity.activityID, this.getNextHighestDepth(), {_x:LABEL_X , _y:LABEL_Y, _width:LABEL_W, _height:LABEL_H, autoSize:_autosize, styleName:styleObj});
		
		showAssets(false);
		
		if(!_activity.isGateActivity() && !_activity.isGroupActivity()){
			//loadIcon();
		}
		
		Debugger.log('initialising activity : ' + _activity.activityID ,Debugger.CRITICAL,'init','org.lamsfoundation.lams.LearnerActivity');
		
		MovieClipUtils.doLater(Proxy.create(this,draw));

	}
	
	private function showAssets(isVisible:Boolean){
		canvasActivity_mc._visible = isVisible;
		title_lbl._visible = true;
	}
	
	/**
	 * Updates the LearnerActivity display fields with the current data
	 * @usage   
	 * @return  
	 */
	public function refresh():Void{
		showAssets(false);
		learner = controller.getModel().progressData;
		actStatus = null;
		this.removeMovieClip(icon_mc);
		draw();
	}
	
	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){
		
		var toolTitle:String
		if (actStatus == null || actStatus == undefined){
			actStatus = Progress.compareProgressData(learner, _activity.activityID);
		}
		
		//title_lbl._visible = true;
		Debugger.log('activity status : ' + actStatus ,Debugger.CRITICAL,'draw','org.lamsfoundation.lams.LearnerActivity');
	
		clickTarget_mc._visible = true;
		
		if (!stopSign_mc) {
			switch (actStatus){
				case 'completed_mc' :
					icon_mc = this.attachMovie("completed_mc", "completed_mc" + _activity.activityID, this.getNextHighestDepth(), {_x: 58 , _y: -7});
					//completed_mc._visible = true;
					break;
				case 'current_mc' :
					icon_mc = this.attachMovie("current_mc", "current_mc" + _activity.activityID, this.getNextHighestDepth(), {_x: 58 , _y: -7});
					//current_mc._visible = true;
					break;
				case 'attempted_mc' :
					icon_mc = this.attachMovie("attempted_mc", "attempted_mc" + _activity.activityID, this.getNextHighestDepth(), {_x: 58 , _y: -7});
					//attempted_mc._visible = true;
					break;
				//case 'attempted_mc' 
				default :
					icon_mc = this.attachMovie("todo_mc", "todo_mc" + _activity.activityID, this.getNextHighestDepth(), {_x: 58 , _y: -7});
					//todo_mc._visible = true;
			}
		}
		
		//write text
		trace("Title passed for Gate Activity: "+actLabel)
		if (actLabel == undefined){
			toolTitle = _activity.title
			if (toolTitle.length > 19){
				toolTitle = toolTitle.substr(0, 17)+"..."
			}
			//title_lbl.text = toolTitle;
		}else {
			toolTitle = actLabel
			if (toolTitle.length > 19){
				toolTitle = toolTitle.substr(0, 17)+"..."
			}
			
		}
		title_lbl.text = toolTitle;
		
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var appData = getAppData();
		if(_complex){
			var ttXpos = appData.compX + xPos;
			var ttYpos = appData.compY + yPos;
		} else {
			if(app.module == 'learner'){
				var ttXpos = appData.compX + this._x-10;
			}else {
				var ttXpos = appData.compX + this._x;
			}
			var ttYpos = appData.compY + this._y+btnObj._height;
		}
		
		var ttHolder = appData.ttHolder;
		trace("x pos: "+ttXpos+" and y pos: "+ttYpos+" and tt holder is: "+ttHolder)
		if (btnTT == undefined || btnTT == null || btnTT == "" || btnTT == "undefined"){
			var ttMessage = "<b>"+ _activity.title+"</b>"
		}else {
			var ttMessage = "<b>"+ _activity.title +"</b> \n"+Dictionary.getValue(btnTT);
		}
		var ttWidth = 140;
		_tip.DisplayToolTip(ttHolder, ttMessage, ttXpos, ttYpos, undefined, ttWidth);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function onRollOver(){
		if (actStatus == "completed_mc"){
			showToolTip(this.clickTarget_mc, "completed_act_tooltip");
		}else if (actStatus == "current_mc"){
			showToolTip(this.clickTarget_mc, "current_act_tooltip");
		}else {
			if (String(_activity.title).length > 19){
				showToolTip(this.clickTarget_mc, "undefined");
			}
		}
		
	}
	
	private function onRollOut(){
		
		hideToolTip();
	}
	
	private function onPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				//if (app.controlKeyPressed != "transition"){
					_doubleClicking = true;
					controller.activityDoubleClick(this);
					Debugger.log('DoubleClicking:+'+this,Debugger.GEN,'onPress','LearnerActivity');
				//}
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','LearnerActivity');
				_doubleClicking = false;
			}
			
			_dcStartTime = now;
			hideToolTip();
	
	}
	
	
	private function onRelease():Void{
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','LearnerActivity');
				trace("Activity ID is: "+this.activity.activityID)	
				controller.activityRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','LearnerActivity');
		controller.activityReleaseOutside(this);
	}
	
	public function set progressData(a:Progress){
		learner = a;
	}
	
	public function get controller(){
		if(app.module == 'learner'){
			return LessonController(_controller);
		} else {
			return MonitorController(_controller);
		}
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
	
}