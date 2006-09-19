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
import org.lamsfoundation.lams.common. *;
import org.lamsfoundation.lams.common.util. *;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.ui. *;
import org.lamsfoundation.lams.authoring. *;
import org.lamsfoundation.lams.authoring.cv. *;
import org.lamsfoundation.lams.monitoring.mv. *;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
//import org.lamsfoundation.lams.authoring.cv.DesignDataModel;
import org.lamsfoundation.lams.common.style. *;
import mx.controls. *;
import mx.managers. *
/**
* CanvasOptionalActivity
* This is the UI / view representation of a complex (Optional) activity
*/
class org.lamsfoundation.lams.authoring.cv.CanvasOptionalActivity extends MovieClip implements ICanvasActivity{
	//class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip {
	private var CHILD_OFFSET_X : Number = 8;
	private var CHILD_OFFSET_Y : Number = 57;
	private var newContainerXCoord:Number; 
	private var newContainerYCoord:Number;
	private var CHILD_INCRE : Number = 60;
	private var learnerOffset_X:Number = 4
	private var learnerOffset_Y:Number = 3
	//this is set by the init object
	private var _canvasController : CanvasController;
	private var _canvasView : CanvasView;
	private var _monitorController : MonitorController;
	private var _monitorTabView : MonitorTabView;
	private var _ca = ComplexActivity;
	//Set by the init obj
	private var _activity : Activity;
	private var _children : Array;
	private var children_mc : Array 
	private var panelHeight : Number;
	private var actMinOptions: Number;
	private var actMaxOptions: Number;
	//refs to screen items:
	private var container_pnl : Panel;
	private var header_pnl : Panel;
	private var act_pnl : Panel;
	private var title_lbl : Label;
	private var actCount_lbl : Label;
	//locals
	private var childActivities_mc : MovieClip;
	private var optionalActivity_mc : MovieClip;
	private var clickTarget_mc : MovieClip;
	private var padlockClosed_mc : MovieClip;
	private var padlockOpen_mc : MovieClip;
	private var _dcStartTime : Number = 0;
	private var _doubleClicking : Boolean;
	// Only for Monitor Optional Container children
	private var fromModuleTab:String;
	private var learner:Object = new Object();
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	//---------------------------//
	private var child_mc : MovieClip;
	private var _locked : Boolean;
	private var _visibleHeight : Number;
	private var _visibleWidth : Number;
	private var _tm : ThemeManager;
	private var _ddm : DesignDataModel;
	private var _dictionary:Dictionary;
	
	function CanvasOptionalActivity ()	{
		optionalActivity_mc = this
		_ddm = new DesignDataModel ();
		
		_visible = false;
		_tm = ThemeManager.getInstance ();
		_dictionary = Dictionary.getInstance();
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		_ca = new ComplexActivity(_activity.activityUIID)
		_activity.activityCategoryID = Activity.CATEGORY_SYSTEM
		//_activity.title = Dictionary.getValue('opt_activity_title')
		//init();
		MovieClipUtils.doLater (Proxy.create (this, init));
	}
	
	public function init () : Void	{
		
		clickTarget_mc.onPress = Proxy.create (this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		
		trace("complex MinOptions :"+_ca.minOptions)
		actMinOptions = _ca.minOptions;
		actMaxOptions = _ca.maxOptions;
		
		trace("MinOptions :"+actMinOptions)
		trace("MaxOptions :"+actMaxOptions)
		_ddm.getComplexActivityChildren(_activity.activityUIID);
		_locked = false;
		showStatus(false);
		
		CHILD_OFFSET_X = 8;
		CHILD_OFFSET_Y = 57;
		//childActivities_mc = this;
		for (var j=0; j<children_mc.length; j++){
			children_mc[j].removeMovieClip();
		}
		children_mc = new Array();
		
		//childActivities_mc.removeMovieClip();
		for (var i = 0; i < _children.length; i ++)		{
			if (fromModuleTab == "monitorMonitorTab"){
				children_mc [i] = childActivities_mc.attachMovie ("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children [i] , _monitorController:_monitorController, _monitorView:_monitorTabView, _module:"monitoring"});
			}else {
				children_mc [i] = childActivities_mc.attachMovie ("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children [i] , _canvasController:_canvasController, _canvasView:_canvasView});
				
			}
			//set the positioning co-ords
			children_mc [i].activity.xCoord = CHILD_OFFSET_X;
			children_mc [i].activity.yCoord = CHILD_OFFSET_Y + (i * CHILD_INCRE);
			
			children_mc [i]._visible = true;

		}
		
		MovieClipUtils.doLater (Proxy.create (this, draw));
	}
	
	private function showStatus(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		todo_mc._visible = isVisible;
		
	}
	
	public function get activity () : Activity	{
		return getActivity ();
	}
	
	public function set activity (a : Activity)	{
		setActivity (a);
	}
	
	public function getActivity () : Activity	{
		return _activity;
	}
	
	public function setActivity (a : Activity)	{
		_activity = a;
	}
	
	private function draw (){
		
		//clickTarget_mc.swapDepths(childActivities_mc.getNextHighestDepth());
		var numOfChildren = _children.length
		panelHeight = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
		setStyles ()
		//write text
		title_lbl.text = _activity.title 		//Dictionary.getValue('opt_activity_title'); //'Optional Activities'
		//_activity.title = 'Optional Activities';
		actCount_lbl.text = _children.length +" - "+ Dictionary.getValue('lbl_num_activities'); //" activities";
		
		//position the container (this)
		if(numOfChildren > 1){
			container_pnl._height = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
		}
		
		_x = _activity.xCoord;
		_y = _activity.yCoord;

		
		
		
		//dimentions of container (this)
		setLocking();
			

		if(fromModuleTab == "monitorMonitorTab"){
			var mm:MonitorModel = MonitorModel(_monitorController.getModel());
		trace("all learner progress length in Canvas activity: "+mm.allLearnersProgress.length);
			var learner_X = _activity.xCoord + learnerOffset_X;
			var learner_Y = _activity.yCoord + learnerOffset_Y;
		// get the length of learners from the Monitor Model and run a for loop.
			for (var j=0; j<mm.allLearnersProgress.length; j++){
				var learner:Object = new Object();
				learner = mm.allLearnersProgress[j]
				
				//Gets a true if learner's currect activityID matches this activityID else false.
				
				var isLearnerCurrentAct:Boolean = Progress.isLearnerCurrentActivity(learner, _activity.activityID);
				if (isLearnerCurrentAct){
					if (learner_X > (_activity.xCoord + 112)){
						learner_X = _activity.xCoord + learnerOffset_X 
						learner_Y = 27
					}
					
					trace("this._parent is "+this._parent)
					trace(_activity.title+": is the learner's current Activity.")
					this._parent.attachMovie("learnerIcon", "learnerIcon"+learner.getUserName(), this._parent.getNextHighestDepth(),{_activity:_activity, learner:learner, _monitorController:_monitorController, _x:learner_X, _y:learner_Y});
					learner_X = learner_X+10
				}else {
					trace(_activity.title+": is not the learner's current Activity.")
				}
			}
		}
		
		Debugger.log ("I am in Draw :" + _activity.title + 'uiID:' + _activity.activityUIID + ' children:' + _children.length, Debugger.GEN, 'Draw', 'CanvasOptionalActivity');
		_visible = true;
		//child1_mc._visible = true;
	}
	
	private function setLocking():Void{
		if (_locked){
			padlockClosed_mc._visible = true;
			padlockOpen_mc._visible = false;
			clickTarget_mc._height = container_pnl._height;
		}else{
			padlockOpen_mc._visible = true;
			padlockClosed_mc._visible = false;
			clickTarget_mc._height = 45;
		}
	}
	
	public function set locked(setLock:Boolean):Void {
		_locked = setLock;
		setLocking();
		
	}
	
	public function get locked():Boolean {
		return _locked;
	}
	
	private function localOnPress ():Void{
		
		// check double-click
		var now : Number = new Date ().getTime ();
		if ((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY)	{
			Debugger.log ('DoubleClicking:' + this, Debugger.GEN, 'localOnPress', 'CanvasOptionalActivity');
			_doubleClicking = true;
			//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
			if (_locked) {
				_locked = false;
			}else {
				_locked = true;
			}
			draw();
		}else {
			Debugger.log ('SingleClicking:+' + this, Debugger.GEN, 'localOnPress', 'CanvasOptionalActivity');
			_doubleClicking = false;
			_canvasController.activityClick (this);
		}
		_dcStartTime = now;
	}
	
	
	private function localOnRelease ():Void{
		Debugger.log ('_doubleClicking:' + _doubleClicking + ', localOnRelease:' + this, Debugger.GEN, 'localOnRelease', 'CanvasOptionalActivity');
		if (! _doubleClicking)	{
				_canvasController.activityRelease (this);
		}
	}
	
	
	private function localOnReleaseOutside():Void {
		Debugger.log ('localOnReleaseOutside:' + this, Debugger.GEN, 'localOnReleaseOutside', 'CanvasOptionalActivity');
		_canvasController.activityReleaseOutside (this);
	}
	/**
	*
	* @usage
	* @return
	*/
	public function getVisibleWidth():Number {
		return _visibleWidth;
	}
	/**
	*
	* @usage
	* @return
	*/
	public function getVisibleHeight():Number {
		return _visibleHeight;
	}
	
	public function get actChildren():Array {
		return _children;
	} 
	public function get getpanelHeight():Number {
		return panelHeight;
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
				styleObj = _tm.getStyleObject('ACTPanel3')
				break;
			case '4' :
				styleObj = _tm.getStyleObject('ACTPanel4')
				break;
			case '5' :
				styleObj = _tm.getStyleObject('ACTPanel5')
				break;
            default :
                styleObj = _tm.getStyleObject('ACTPanel0')
		}
		return styleObj;
	}
	
	private function setStyles():Void {
		var styleObj = _tm.getStyleObject ('label');
		title_lbl.setStyle (styleObj);
		styleObj = _tm.getStyleObject ('PIlabel');
		actCount_lbl.setStyle ('styleName', styleObj);
		//styleObj = _tm.getStyleObject ('OptHeadPanel');
		//header_pnl.setStyle ('styleName', styleObj);
		//styleObj = _tm.getStyleObject ('OptActContainerPanel');
		styleObj = getAssociatedStyle();
		container_pnl.setStyle ('styleName', styleObj);
	}
}
