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
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.common.style.*;
import mx.controls.*;
import mx.managers.*;


/**  
* CanvasParallelActivity
* This is the UI / view representation of a complex (parralel) activity
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip implements ICanvasActivity{  
	
	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD1_OFFSET_Y:Number = 45 //67.5;
	private var CHILD2_OFFSET_Y:Number = 108 //130.5;
	private var newContainerXCoord:Number; 
	private var newContainerYCoord:Number;
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _monitorController:MonitorController;
	private var _monitorTabView:MonitorTabView;
	private var _tm:ThemeManager;
	
	//Set by the init obj
	private var _activity:Activity;
	private var _children:Array;
	
	//refs to screen items:
	private var container_pnl:Panel;
	private var header_pnl:Panel;
	private var title_lbl:Label;
	private var actCount_lbl:Label;
	private var childActivities_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var padlockClosed_mc:MovieClip;
	private var padlockOpen_mc:MovieClip;
	
	private var learnerOffset_X:Number = 4;
	private var learnerOffset_Y:Number = 3;
	private var learnerContainer:MovieClip;
	private var _ddm:DesignDataModel;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var child1_mc:MovieClip;
	private var child2_mc:MovieClip;
	private var _locked:Boolean = false;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	
	// Only for Monitor Optional Container children
	private var fromModuleTab:String;
	private var learner:Object = new Object();
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	
	
	function CanvasParallelActivity(){
		Debugger.log("_activity:"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Constructor','CanvasParallelActivity');
		_visible = false;
		_tm = ThemeManager.getInstance();
		_ddm = new DesignDataModel()
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		
		
		
		//set up some handlers:
		clickTarget_mc.onPress = Proxy.create(this,localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this,localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this,localOnReleaseOutside);

		_ddm.getComplexActivityChildren(_activity.activityUIID);
		
		showStatus(false);
		
		var child1:Activity;
		var child2:Activity;
		if(_children[0].orderID < _children[1].orderID){
			child1 = _children[0];
			child2 = _children[1];
			
		}else{
			child1 = _children[1];
			child2 = _children[0];
		
		}
		//set the positioning co-ords
			newContainerXCoord = container_pnl._width/2
			newContainerYCoord = container_pnl._height/2
			child1.xCoord = CHILD_OFFSET_X //+ (newContainerXCoord-CHILD_OFFSET_X);
			child1.yCoord = CHILD1_OFFSET_Y;
			child2.xCoord = CHILD_OFFSET_X //+ (newContainerXCoord-CHILD_OFFSET_X);
			child2.yCoord = CHILD2_OFFSET_Y //+ newContainerYCoord;
		//so now it is placed on in the IDE and we just call init
		if (fromModuleTab == "monitorMonitorTab"){
			child1_mc.init({activity:child1,_monitorController:_monitorController,_monitorView:_monitorTabView, _module:"monitoring", learnerContainer:learnerContainer});
			child2_mc.init({activity:child2,_monitorController:_monitorController,_monitorView:_monitorTabView, _module:"monitoring", learnerContainer:learnerContainer});
				
		}else {
			trace("called when seleting act")
			child1_mc.init({activity:child1,_canvasController:_canvasController,_canvasView:_canvasView});
			child2_mc.init({activity:child2,_canvasController:_canvasController,_canvasView:_canvasView});
			
		}
		
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,draw));
		
	}
	
	public function refreshChildren():Void {
		child1_mc.setSelected(false);
		child2_mc.setSelected(false);
	}
	
	public function setSelected(isSelected){}
	
	private function showStatus(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		todo_mc._visible = isVisible;
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
	
	
	private function getAssociatedStyle():Object{
		trace("Category ID for Activity "+_activity.title +": "+_activity.activityCategoryID)
		var styleObj:Object = new Object();
		switch (String(_activity.activityCategoryID)){
			case '0' :
				styleObj = _tm.getStyleObject('ACTPanel0')
                break;
            case '1' :
			    styleObj = _tm.getStyleObject('ACTPanel1')
                break;
			case '2' :
				styleObj = _tm.getStyleObject('ACTPanel2')
				break;
			case '3' :
				styleObj = _tm.getStyleObject('ACTPanel5')
				break;
			case '4' :
				styleObj = _tm.getStyleObject('ACTPanel4')
				break;
			case '5' :
				styleObj = _tm.getStyleObject('ACTPanel1')
				break;
			case '6' :
				styleObj = _tm.getStyleObject('ACTPanel3')
				break;
            default :
                styleObj = _tm.getStyleObject('ACTPanel0')
		}
		return styleObj;
	}
	
	private function drawLearners():Void {
		var mm:MonitorModel = MonitorModel(_monitorController.getModel());
		var learner_X = _activity.xCoord + learnerOffset_X;
		var learner_Y = _activity.yCoord + learnerOffset_Y;
			
		// get the length of learners from the Monitor Model and run a for loop.
		for (var j=0; j<mm.allLearnersProgress.length; j++){
			var learner:Object = new Object();
			learner = mm.allLearnersProgress[j]
				
			//Gets a true if learner's currect activityID matches this activityID else false.
			var isLearnerCurrentAct:Boolean = Progress.isLearnerCurrentActivity(learner, _activity.activityID);
			var hasPlus:Boolean = false;
			
			if (isLearnerCurrentAct){
					
				if (learner_X > (_activity.xCoord + 112)){
					learner_X = _activity.xCoord + learnerOffset_X 
					learner_Y = 27
					hasPlus = true;
					learnerContainer.attachMovie("learnerIcon", "learnerIcon"+learner.getUserName(), this._parent.getNextHighestDepth(),{_activity:_activity, learner:learner, _monitorController:_monitorController, _x:learner_X, _y:learner_Y, _hasPlus:hasPlus });
					return;
				}
					
				learnerContainer.attachMovie("learnerIcon", "learnerIcon"+learner.getUserName(), this._parent.getNextHighestDepth(),{_activity:_activity, learner:learner, _monitorController:_monitorController, _x:learner_X, _y:learner_Y, _hasPlus:hasPlus});
				learner_X = learner_X+10
			}
		}
	}
	
	private function draw(){			
		//write text
		title_lbl.text = _activity.title;
			
		if(fromModuleTab == "monitorMonitorTab")
			drawLearners()
			
		//position the container (this)
		_x = _activity.xCoord //- newContainerXCoord;
		_y = _activity.yCoord
	
		setLocking()
		setStyles()
		_visible = true;
					
	}
	
	private function setStyles():Void {
		var styleObj = _tm.getStyleObject ('label');
		title_lbl.setStyle (styleObj);
		styleObj = getAssociatedStyle();
		container_pnl.setStyle('styleName',styleObj);
		styleObj = _tm.getStyleObject ('parallelHeadPanel');
		header_pnl.setStyle('styleName',styleObj);
		//container_pnl.setStyle("backgroundColor",0x4289FF);
	}
	
	private function setLocking():Void{
		if(_locked){
			padlockClosed_mc._visible = true;
			padlockOpen_mc._visible = false;
			clickTarget_mc._height = 173;
		}else{
			padlockOpen_mc._visible = true;
			padlockClosed_mc._visible = false;
			clickTarget_mc._height = 30;
		}
	}
	
	public function set locked(setLock:Boolean):Void {
		_locked = setLock;
		setLocking();
		
	}
	
	public function get locked():Boolean {
		return _locked;
	}
	
	private function localOnPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'localOnPress','CanvasParallelActivity');
				_doubleClicking = true;
				
				//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
				if(_locked && !(_activity.isReadOnly() && (fromModuleTab == null || fromModuleTab == undefined))){
					_locked = false;
				}else {
					if(_activity.isReadOnly() && (fromModuleTab == null || fromModuleTab == undefined)) { 
						/** TODO: Change label warning */ 
						LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_dbclick_readonly')); 
					}
					
					_locked = true;
				}
				draw();
				
				
				
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'localOnPress','CanvasParallelActivity');
				_doubleClicking = false;
				_canvasController.activityClick(this);
				
			}
			
			_dcStartTime = now;
	
	}
	
	private function localOnRelease():Void{
		Debugger.log('_doubleClicking:'+_doubleClicking+', localOnRelease:'+this,Debugger.GEN,'localOnRelease','CanvasParallelActivity');
		if ( ! _doubleClicking)	{
				_canvasController.activityRelease(this);
		}
		
	}
	
	private function localOnReleaseOutside():Void{
		Debugger.log('localOnReleaseOutside:'+this,Debugger.GEN,'localOnReleaseOutside','CanvasParallelActivity');
		_canvasController.activityReleaseOutside(this);
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
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get actChildren():Array {
		return _children;
	} 
	
	public function get children():Array {
		return new Array(child1_mc, child2_mc);
	}


}