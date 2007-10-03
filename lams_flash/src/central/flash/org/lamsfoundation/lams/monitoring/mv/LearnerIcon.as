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
import org.lamsfoundation.lams.common.util.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.common.style.*

import com.polymercode.Draw;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*

/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.monitoring.mv.LearnerIcon extends MovieClip {
  
	public static var ICON_WIDTH:Number = 8;
	public static var ICON_HEIGHT:Number = 20;
	
	//this is set by the init object
	private var _monitorController:MonitorController;
	private var _monitorView;
	private var _tm:ThemeManager;
	
	//TODO:This should be ToolActivity
	private var _activity:Activity;
	private var learner:Progress;
	
	private var _isSelected:Boolean;
	private var app:ApplicationParent;
	
	//locals
	private var toolTip:MovieClip;
	private var learnerOffset_X:Number
	private var learnerOffset_Y:Number
	private var click_mc:MovieClip;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	private var _selected_mc:MovieClip;
	
	private var smallCross:MovieClip;
	private var _hasPlus:Boolean;
	
	private var _clone_mc:MovieClip;
	private var _clone:Boolean;
	
	
	function LearnerIcon(){
		//Debugger.log("_activity:"+_activity.title,4,'Constructor','CanvasActivity');
		_tm = ThemeManager.getInstance();
		//Get reference to application and design data model
		app = ApplicationParent.getInstance();
		//let it wait one frame to set up the components.
		//this has to be set b4 the do later :)
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
			learner = initObj.learner;
			learnerOffset_X = initObj._x;
			learnerOffset_Y = initObj._y;
			_hasPlus = initObj._hasPlus;
			_clone = initObj._clone;
		}
		
		learnerOffset_X = _x;
		learnerOffset_Y = _y;
		
		Debugger.log('Learner x pos and y pos : '+learnerOffset_X+ " and "+learnerOffset_Y ,4,'draw','LearnerIcon');
		
		showAssets(false);
		
		//Click, Rollover and rollout Events for Learner Icon;
		click_mc.onRollOver = Proxy.create (this, localOnRollOver);
		click_mc.onRollOut = Proxy.create (this, localOnRollOut);
		
		if (_activity != undefined){
			click_mc.onPress = Proxy.create (this, localOnPress);
			click_mc.onRelease = Proxy.create (this, localOnRelease);
			click_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		}
		
		setStyles();
		MovieClipUtils.doLater(Proxy.create(this,draw));

	}
	
	private function showAssets(isVisible:Boolean){
		toolTip._visible = isVisible;
	}
	
	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){
		
		//Code for Drawing learner on the activty. 
				
		Debugger.log('Learner is in Activity: '+_activity.title,4,'draw','LearnerIcon');
		setStyles();
		toolTip.text = learner.getFullName();

		Debugger.log('hasPlus: '+ _hasPlus,Debugger.CRITICAL,'draw','CanvasActivity');
		smallCross._visible = _hasPlus;
		
		_visible = true;
	}
	
	
	private function localOnPress():Void{
	
		
		// check double-click
		var now:Number = new Date().getTime();
		
		if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
			if (app.controlKeyPressed != "transition"){
				_doubleClicking = true;
				Debugger.log('DoubleClicking: '+this.activity.activityID,Debugger.CRITICAL,'onPress','CanvasActivity For Monitoring');
				var _learnerID:Number = learner.getLearnerId()
				_monitorController.activityDoubleClick(_activity, "MonitorTabViewLearner", _learnerID);
						
			}
			
		} else {

			_doubleClicking = false;
			Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity for monitoring');
			_clone_mc = ApplicationParent.root.attachMovie("learnerIcon", _name + "_clone", DepthManager.kTop, {_activity:_activity, learner:learner, _monitorController:_monitorController, _x:_x + org.lamsfoundation.lams.monitoring.Application.MONITOR_X, _y:_y + org.lamsfoundation.lams.monitoring.Application.MONITOR_Y, _hasPlus:_hasPlus, _clone: true });
					
			_monitorController.activityClick(_clone_mc, "LearnerIcon");
			
		}
		
		_dcStartTime = now;
	
	}
	
	private function localOnRollOver():Void{
		toolTip._visible = true;
	}
	
	private function localOnRollOut():Void{
		toolTip._visible = false;
	}
	
	
	private function localOnRelease():Void{
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','CanvasActivity');
			_monitorController.activityRelease(_clone_mc, "LearnerIcon");
			_clone_mc.removeMovieClip();
		}
	}
	
	private function localOnReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasActivity');
		_monitorController.activityRelease(_clone_mc, "LearnerIcon");
		_clone_mc.removeMovieClip();
	}
	
	
	public function get xCoord():Number{
		return learnerOffset_X;
	}
	
	public function get yCoord():Number{
		return learnerOffset_Y;
	}
	
	public function get Learner():Progress{
		return getLearner();
	}
	
	public function set Learner(a:Progress){
		setLearner(a);
	}
	
	
	public function getLearner():Progress{
		return learner;

	}
	
	public function setLearner(a:Progress){
		learner = a;
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
    }
    

}