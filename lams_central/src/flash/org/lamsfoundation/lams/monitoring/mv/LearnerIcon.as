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
//class org.lamsfoundation.lams.authoring.cv.CanvasActivity extends MovieClip{  
  
	public static var ICON_WIDTH:Number = 7;
	public static var ICON_HEIGHT:Number = 19;
	
	//this is set by the init object
	private var _monitorController:MonitorController;
	private var _monitorView;
	private var _tm:ThemeManager;
	
	//TODO:This should be ToolActivity
	private var _activity:Activity;
	private var learner:Progress;
	
	private var _isSelected:Boolean;
	private var app:Application;
	//locals
	private var toolTip:MovieClip;
	private var learnerOffset_X:Number = 4
	private var learnerOffset_Y:Number = 3
	private var click_mc:MovieClip;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	private var _selected_mc:MovieClip;
	
	
	
	function LearnerIcon(){
		//Debugger.log("_activity:"+_activity.title,4,'Constructor','CanvasActivity');
		_tm = ThemeManager.getInstance();
		//Get reference to application and design data model
		app = Application.getInstance();
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
		click_mc.onRollOver = Proxy.create (this, localOnRollOver);
		click_mc.onRollOut = Proxy.create (this, localOnRollOut);
		click_mc.onPress = Proxy.create (this, localOnPress);
		click_mc.onRelease = Proxy.create (this, localOnRelease);
		click_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		
		if(initObj){
			_monitorView = initObj._monitorView;
			_monitorController = initObj._monitorController;
			_activity = initObj.activity;
			learner = initObj.learner;
		}
		
		showAssets(false);
		
		setStyles();
		MovieClipUtils.doLater(Proxy.create(this,draw));

	}
	
	private function showAssets(isVisible:Boolean){
		
		//canvasActivity_mc._visible = isVisible;
		toolTip._visible = isVisible;
		//click_mc._visible = isVisible;
		
	}
	
	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){
		
		//Code for Drawing learner on the activty. 
		
		//trace("value of _Module in Canvas Activity: "+_module)
		//trace("Monitor Model is: "+_monitorController.getModel())
		
		Debugger.log('Learner is in Activity: '+_activity.title,4,'draw','LearnerIcon');
		setStyles();
		//position
		//_x = xCoord;
		//_y = yCoord;
		toolTip.text = learner.getUserName();

		
		//Debugger.log('canvasActivity_mc._visible'+canvasActivity_mc._visible,4,'draw','CanvasActivity');
		_visible = true;
	}
	
	
	private function localOnPress():Void{
	
		
		// check double-click
		var now:Number = new Date().getTime();
		
		if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
			//trace("Module passed is: "+_module)
			if (app.controlKeyPressed != "transition"){
				_doubleClicking = true;
				//Debugger.log('DoubleClicking: '+this.activity.activityID,Debugger.GEN,'onPress','CanvasActivity For Monitoring');
				var _learnerID:Number = learner.getLearnerId()
				_monitorController.activityDoubleClick(_activity, "MonitorTabViewLearner", _learnerID);
				
			}
			
			
		}else{
			
			_doubleClicking = false;
			Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasActivity for monitoring');
			//_monitorController.activityClick(this);
		}
		
		_dcStartTime = now;
	
	}
	
	private function localOnRollOver():Void{
		trace("I am: "+learner.getUserName()+" with learner ID: "+learner.getLearnerId())
		toolTip._visible = true;
	}
	
	private function localOnRollOut():Void{
		trace("I am: "+learner.getUserName())
		toolTip._visible = false;
	}
	
	
	private function localOnRelease():Void{
		if(!_doubleClicking){
			Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','CanvasActivity');
			//	trace("Activity ID is: "+this.activity.activityUIID)	
				_monitorController.activityRelease(this);
		}
	}
	
	private function localOnReleaseOutside():Void{
		Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasActivity');
		_monitorController.activityReleaseOutside(this);
	}
	
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
		//var styleObj = _tm.getStyleObject('label');
		
		//title_lbl.setStyle('styleName',styleObj);
		//title_lbl.setStyle('textAlign', 'center');
		
		//styleObj = _tm.getStyleObject('ACTPanel')
		//act_pnl.setStyle('styleName',styleObj);
			
    }
    

}