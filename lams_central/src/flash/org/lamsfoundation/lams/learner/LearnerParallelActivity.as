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
import org.lamsfoundation.lams.authoring.cv.ICanvasActivity;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.*;
import mx.controls.*;
import mx.managers.*


/**  
* LeanrerParallelActivity
* This is the UI / view representation of a complex (parralel) activity
*/  
class LearnerParallelActivity extends MovieClip implements ICanvasActivity{   
  
	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD1_OFFSET_Y:Number = 45;
	private var CHILD2_OFFSET_Y:Number = 108;
	
	//this is set by the init object
	private var _lessonController:LessonController;
	private var _lessonView:LessonView;
	
	//Set by the init obj
	private var _activity:Activity;
	private var _children:Array;
	
	//refs to screen items:
	private var container_pnl:Panel;
	private var title_lbl:Label;
	private var childActivities_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	
	private var _ddm:DesignDataModel;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	private var child1_mc:MovieClip;
	private var child2_mc:MovieClip;
	private var _locked:Boolean;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	
	// Only for Learner Optional Container children
	private var learner:Object;
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var attempted_mc:MovieClip;
	private var todo_mc:MovieClip;
	
	
	function LearnerParallelActivity(){
		Debugger.log("_activity:"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Constructor','LearnerParallelActivity');
		_visible = false;
		_ddm = new DesignDataModel();
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		_locked = false;
		
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		
		//set up some handlers:
		clickTarget_mc.onPress = Proxy.create(this,localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this,localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this,localOnReleaseOutside);

		//_ddm.getComplexActivityChildren(_activity.activityUIID);
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
		
		Debugger.log("parallel child 1 :"+child1+'\nparallel child 2 :' + child2,Debugger.GEN,'Constructor','LearnerParallelActivity');
		
		
		//so now it is placed on in the IDE and we just call init

		child1_mc.init({activity:child1,_lessonController:_lessonController,_lessonView:_lessonView, learner:learner});
		child2_mc.init({activity:child2,_lessonController:_lessonController,_lessonView:_lessonView, learner:learner});
			
		//set the visibility to false
		child1_mc._visible = false;
		child2_mc._visible = false;
		
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,draw));
		
	}
	
	private function showStatus(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		todo_mc._visible = isVisible;
		attempted_mc._visible = isVisible;
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
			
		var actStatus:String = Progress.compareProgressData(learner, _activity.activityID);
		Debugger.log ("status : " + actStatus + 'activity id: ' + _activity.activityID, Debugger.GEN, 'Draw', 'LearnerParallelActivity');
		switch (actStatus){
			case 'completed_mc' :
				completed_mc._visible = true;
				break;
			case 'current_mc' :
				current_mc._visible = true;
				break;
			 case 'attempted_mc' :
			    attempted_mc._visible = true;
                break;
			default :
				todo_mc._visible = true;
				//Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}
		
		//write text
		title_lbl.text = _activity.title;
		container_pnl.setStyle("backgroundColor",0x4289FF);

		containerPanelHeader.title_lbl.text = 'Parallel Activities'
		
		Debugger.log ("I am in Draw :" + _activity.title + 'uiID:' + _activity.activityUIID + ' children:' + _children.length, Debugger.GEN, 'Draw', 'LearnerParallelActivity');
		
		_visible = true;
					
	}
	
	private function localOnPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'localOnPress','LearnerParallelActivity');
				_doubleClicking = true;
				draw();
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'localOnPress','LearnerParallelActivity');
				_doubleClicking = false;
			}
			
			_dcStartTime = now;
	
	}
	
	private function localOnRelease():Void{
		Debugger.log('_doubleClicking:'+_doubleClicking+', localOnRelease:'+this,Debugger.GEN,'localOnRelease','LearnerParallelActivity');
			if (_locked)
			{
				_locked = false;
				gotoAndStop('collapse')
				//set the visibility to false
				child1_mc._visible = false;
				child2_mc._visible = false;
				draw ();
				
			}else{
				_locked = true;
				//set the visibility to true
				child1_mc._visible = true;
				child2_mc._visible = true;
				gotoAndStop('expand')
				draw ();
			}
	}
	
	private function localOnReleaseOutside():Void{
		Debugger.log('localOnReleaseOutside:'+this,Debugger.GEN,'localOnReleaseOutside','CanvasParallelActivity');
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