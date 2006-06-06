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
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;
import mx.controls.*;
import mx.managers.*


/**  
* CanvasParallelActivity
* This is the UI / view representation of a complex (parralel) activity
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip implements ICanvasActivity{   
//class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip {   
  
	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD1_OFFSET_Y:Number = 45;
	private var CHILD2_OFFSET_Y:Number = 108;
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _monitorController:MonitorController;
	private var _monitorView:MonitorView;
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
	
	
	private var _ddm:DesignDataModel;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var child1_mc:MovieClip;
	private var child2_mc:MovieClip;
	private var _locked:Boolean;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	
	// Only for Monitor Optional Container children
	private var fromModuleTab:String;
	private var _learnerTabView:LearnerTabView;
	private var learner:Object = new Object();
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	
	
	function CanvasParallelActivity(){
		Debugger.log("_activity:"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Constructor','CanvasParallelActivity');
		_visible = false;
		_ddm = new DesignDataModel()
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		//init();
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		
		
		
		//set up some handlers:
		clickTarget_mc.onPress = Proxy.create(this,localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this,localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this,localOnReleaseOutside);

		_ddm.getComplexActivityChildren(_activity.activityUIID);
		_locked = true;
		
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
		
		//so now it is placed on in the IDE and we just call init
		if (fromModuleTab == "monitorMonitorTab"|| fromModuleTab == "monitorLearnerTab"){
			
			child1_mc.init({activity:child1,_monitorController:_monitorController,_monitorView:_monitorView, learner:learner, _module:"monitoring"});
			child2_mc.init({activity:child2,_monitorController:_monitorController,_monitorView:_monitorView, learner:learner, _module:"monitoring"});
			
			//set the visibility to false
			child1_mc._visible = false;
			child2_mc._visible = false;
			
		} else {
			//set the positioning co-ords
			child1.xCoord = CHILD_OFFSET_X;
			child1.yCoord = CHILD1_OFFSET_Y;
			child2.xCoord = CHILD_OFFSET_X;
			child2.yCoord = CHILD2_OFFSET_Y;
			child1_mc.init({activity:child1,_canvasController:_canvasController,_canvasView:_canvasView});
			child2_mc.init({activity:child2,_canvasController:_canvasController,_canvasView:_canvasView});
			
		}
		
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,draw));
		
	}
	
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
	
	private function draw(){			
		var actStatus:String = _learnerTabView.compareProgressData(learner, _activity.activityID);
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
		actCount_lbl.text = _children.length+" activities";
		
//			_global.breakpoint();
		
		header_pnl.borderType='outset';
		container_pnl.setStyle("backgroundColor",0x4289FF);
		
		if (fromModuleTab != "monitorLearnerTab"){
			//position the container (this)
			_x = _activity.xCoord;
			_y = _activity.yCoord;
		
			if(_locked){
				padlockClosed_mc._visible = true;
				padlockOpen_mc._visible = false;
				clickTarget_mc._height = 173;
			}else{
				padlockOpen_mc._visible = true;
				padlockClosed_mc._visible = false;
				clickTarget_mc._height = 38;
			}
		}else {
			containerPanelHeader.title_lbl.text = 'Parallel Activities'
		}
	
		_visible = true;
		//child1_mc._visible = true;
					
	}
	
	
	private function localOnPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'localOnPress','CanvasParallelActivity');
				_doubleClicking = true;
				
				//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
				if(_locked){
					_locked = false;
				}else{
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
		if (fromModuleTab != "monitorLearnerTab"){
			if ( ! _doubleClicking)
			{
				_canvasController.activityRelease (this);
			}
		}else {
			if (_locked)
			{
				_locked = false;
				gotoAndStop('collapse')
				//set the visibility to false
				child1_mc._visible = false;
				child2_mc._visible = false;
				draw ();
				
			}else
			{
				_locked = true;
				//set the visibility to true
				child1_mc._visible = true;
				child2_mc._visible = true;
				gotoAndStop('expand')
				draw ();
				
			}
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
	
	
	
	



}