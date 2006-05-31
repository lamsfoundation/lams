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

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.common.style.*

import com.polymercode.Draw;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*

/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.monitoring.mv.MonitorLearnerActivity extends MovieClip implements ICanvasActivity{  
//class org.lamsfoundation.lams.authoring.cv.CanvasActivity extends MovieClip{  
	public static var GATE_ACTIVITY_HEIGHT:Number =50;
	public static var GATE_ACTIVITY_WIDTH:Number = 50;
	public static var TOOL_ACTIVITY_WIDTH:Number = 123.1;
	public static var TOOL_ACTIVITY_HEIGHT:Number = 50.5;
	//this is set by the init object
	private var _monitorController:MonitorController;
	private var _monitorView:MonitorView;
	private var _learnerTabView:LearnerTabView;
	private var _tm:ThemeManager;
	//TODO:This should be ToolActivity
	private var _activity:Activity;
	private var actStatus:String;
	private var _isSelected:Boolean;
	private var app:Application;
	//locals
	private var learner:Object = new Object();
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	private var canvasActivity_mc:MovieClip;
	private var title_lbl:MovieClip;
	private var groupIcon_mc:MovieClip;
	private var stopSign_mc:MovieClip;	
	private var clickTarget_mc:MovieClip;
	private var canvasActivityGrouped_mc:MovieClip;	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	private var _base_mc:MovieClip;
	private var _selected_mc:MovieClip;
	
	
	
	function MonitorLearnerActivity(){
		//Debugger.log("_activity:"+_activity.title,4,'Constructor','CanvasActivity');
		_tm = ThemeManager.getInstance();
		//Get reference to application and design data model
		app = Application.getInstance();
		//let it wait one frame to set up the components.
		//this has to be set b4 the do later :)
		if(_activity.isGateActivity()){
			_visibleHeight = MonitorLearnerActivity.GATE_ACTIVITY_HEIGHT;
			_visibleWidth = MonitorLearnerActivity.GATE_ACTIVITY_WIDTH;
		}else if(_activity.isGroupActivity()){
			_visibleHeight = MonitorLearnerActivity.TOOL_ACTIVITY_HEIGHT;
			_visibleWidth = MonitorLearnerActivity.TOOL_ACTIVITY_WIDTH;
		}
		//_base_mc = this;
		//call init if we have passed in the _activity as an initObj in the attach movie,
		//otherwise wait as the class outside will call it
		if(_activity != undefined){
			init();
		}
		
	}
	
	public function init(initObj):Void{
		if(initObj){
			
				_monitorView = initObj._monitorView;
				_monitorController = initObj._monitorController;
				_activity = initObj.activity;
		}
		
		showAssets(false);
		
		

		
		if(!_activity.isGateActivity() && !_activity.isGroupActivity()){
			//loadIcon();
		}
		setStyles();
		MovieClipUtils.doLater(Proxy.create(this,draw));

	}
	
	private function showAssets(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		canvasActivity_mc._visible = isVisible;
		clickTarget_mc._visible = isVisible;
		todo_mc._visible = isVisible;
		title_lbl._visible = true;
	}
	
	/**
	 * Updates the CanvasActivity display fields with the current data
	 * @usage   
	 * @return  
	 */
	public function refresh():Void{
		draw();
		//setSelected(_isSelected);
	}
	
	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){		Debugger.log(_activity.title+',_activity.isGateActivity():'+_activity.isGateActivity(),4,'draw','CanvasActivity');
		actStatus = _learnerTabView.compareProgressData(learner, _activity.activityID);
		title_lbl._visible = true;
		
		clickTarget_mc._visible = true;
		
		switch (actStatus){
		    case 'completed_mc' :
				//trace("TabID for Selected tab is: "+infoObj.tabID)
				completed_mc._visible = true;
		
                break;
            case 'current_mc' :
				current_mc._visible = true;
                break;
            //case 'toto_mc' :
			    //todo_mc._visible = true;
                //break;
			default :
				todo_mc._visible = true;
                //Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}
			
		//write text
		title_lbl.text = _activity.title;
									//Debugger.log('canvasActivity_mc._visible'+canvasActivity_mc._visible,4,'draw','CanvasActivity');
		//_visible = true;
	}
	
		
	private function onPress():Void{
		
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				if (app.controlKeyPressed != "transition"){
					_doubleClicking = true;
					
						_monitorController.activityDoubleClick(this);
					
				}
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','MonitorLearnerActivity');
				_doubleClicking = false;
				
					_monitorController.activityClick(this);
			}
			
			_dcStartTime = now;
	
	}
	
	private function onRelease():Void{
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','MonitorLearnerActivity');
				trace("Activity ID is: "+this.activity.activityUIID)	
				_monitorController.activityRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','MonitorLearnerActivity');
		_monitorController.activityReleaseOutside(this);
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


	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
		var styleObj = _tm.getStyleObject('label');
		
		title_lbl.setStyle('styleName',styleObj);
		title_lbl.setStyle('textAlign', 'center');
		
		//styleObj = _tm.getStyleObject('ACTPanel')
		//act_pnl.setStyle('styleName',styleObj);
			
    }
    

}