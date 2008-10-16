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
import org.lamsfoundation.lams.authoring.br.CanvasBranchView;
import org.lamsfoundation.lams.monitoring.mv. *;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.common.style. *;

import mx.controls. *;
import mx.managers. *;

/**
* CanvasOptionalActivity
* This is the UI / view representation of a complex (Optional) activity
*/
class org.lamsfoundation.lams.authoring.cv.CanvasOptionalActivity extends MovieClip implements ICanvasActivity{

	public static var ACT_TYPE:Number = 0;
	public static var SEQ_TYPE:Number = 1;

	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD_OFFSET_Y:Number = 57;
	private var CHILD_INCRE:Number = 60;
	private var MAX_LEARNER_ICONS:Number = 10;
	
	private var PANEL_OFFSET_X:Number = null;
	
	private var CONTAINER_PANEL_W:Number = 142.8;
	
	public static var PLUS_MARGIN_X:Number = 15;
	public static var PLUS_MARGIN_Y:Number = 10;
	
	private var newContainerXCoord:Number; 
	private var newContainerYCoord:Number;
	
	private var learnerOffset_X:Number = 4;
	private var learnerOffset_Y:Number = 3;
	private var learnerContainer:MovieClip;
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _canvasBranchView:CanvasBranchView;
	private var _canvasComplexView:CanvasComplexView;
	private var _monitorController:MonitorController;
	private var _monitorTabView:MonitorTabView;
	private var _ca = ComplexActivity;
	
	//Set by the init obj
	private var _activity:Activity;
	private var _children:Array;
	private var children_mc:Array;
	private var _panelHeight:Number;
	private var actMinOptions:Number;
	private var actMaxOptions:Number;
	private var noSeqActivities:Number;
	
	//refs to screen items:
	private var container_pnl:Panel;
	private var header_pnl:Panel;
	private var act_pnl:Panel;
	
	private var title_lbl:Label;
	private var actCount_lbl:Label;
	
	// type
	private var _type:Number;
	
	//locals
	private var childActivities_mc:MovieClip;
	private var optionalActivity_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	
	// Only for Monitor Optional Container children
	private var fromModuleTab:String;
	private var learner:Object = new Object();
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	//---------------------------//
	private var child_mc:MovieClip;
	private var _locked:Boolean = false;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	function CanvasOptionalActivity()	{
		optionalActivity_mc = this;
		
		_visible = false;
		
		_tm = ThemeManager.getInstance ();
		_dictionary = Dictionary.getInstance();
		
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		_ca = new ComplexActivity(_activity.activityUIID)
		_activity.activityCategoryID = Activity.CATEGORY_SYSTEM;
		
		_type = (ComplexActivity(_activity).isSequenceBased) ? SEQ_TYPE : ACT_TYPE;
		
		MovieClipUtils.doLater(Proxy.create(this, init));
	}
	
	public function init():Void {
		Debugger.log("type: " + _type, Debugger.CRITICAL, "init", "CanvasOptionalActivity");
		switch(_type) {
			case ACT_TYPE:
				initActivityType();
				break;
			case SEQ_TYPE:
				initSequenceType();
				break;
			default:
				Debugger.log("No valid type.", Debugger.CRITICAL, "init", "CanvasOptionalActivity");
		}
	}
	
	public function initActivityType():Void {
		
		clickTarget_mc.onPress = Proxy.create(this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this, localOnReleaseOutside);
		
		actMinOptions = _ca.minOptions;
		actMaxOptions = _ca.maxOptions;
		
		showStatus(false);
		
		removeAllChildren();		
		
		for (var i=0; i < _children.length; i++)		{
			if(fromModuleTab == "monitorMonitorTab"){
				if(_canvasBranchView != null) {
					children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_canvasBranchView, _module:"monitoring", learnerContainer:learnerContainer});
				} else if(_canvasComplexView != null) {
					children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_canvasComplexView, _module:"monitoring", learnerContainer:learnerContainer});
				} else {
					children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_monitorTabView, _module:"monitoring", learnerContainer:learnerContainer});
				}
			} else {
				if(_canvasBranchView != null) {
					children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _canvasController:_canvasController, _canvasBranchView:_canvasBranchView})
				} else if(_canvasComplexView != null) {
					children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _canvasController:_canvasController, _canvasComplexView:_canvasComplexView});
				} else {
					children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _canvasController:_canvasController, _canvasView:_canvasView});
				}
			}
			
			//set the positioning co-ords
			children_mc[i].activity.xCoord = CHILD_OFFSET_X;
			children_mc[i].activity.yCoord = CHILD_OFFSET_Y + (i * CHILD_INCRE);
			
			children_mc[i]._visible = true;

		}
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
		
	}
	
	public function initSequenceType():Void {
		clickTarget_mc.onPress = Proxy.create(this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this, localOnReleaseOutside);
		
		showStatus(false);
		
		CHILD_OFFSET_X = 4;
		CHILD_OFFSET_Y = 48;
		PANEL_OFFSET_X = CHILD_OFFSET_X;
		
		CHILD_INCRE = 57;
		
		removeAllChildren();
		
		var _newVisibleWidth:Number = null;
				
		for(var i=0; i < _children.length; i++) {
			Debugger.log("opt child act: " + _children[i].activityUIID, Debugger.CRITICAL, "initSequenceType", "CanvasOptionalActivity");
			
			if(_children[i].isSequenceActivity()) {
				if(fromModuleTab == "monitorMonitorTab") {
					 if(_canvasBranchView != null) {
						 children_mc[i] = childActivities_mc.attachMovie("CanvasSequenceActivity", "CanvasSequenceActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_canvasBranchView, _module:"monitoring", learnerContainer:learnerContainer})
					 } else if(_canvasComplexView) {
						 children_mc[i] = childActivities_mc.attachMovie("CanvasSequenceActivity", "CanvasSequenceActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_canvasComplexView, _module:"monitoring", learnerContainer:learnerContainer})
					 } else {
						 children_mc[i] = childActivities_mc.attachMovie("CanvasSequenceActivity", "CanvasSequenceActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_monitorTabView, _module:"monitoring", learnerContainer:learnerContainer});
					 }				} else {
					if(_canvasBranchView != null) {
						children_mc[i] = childActivities_mc.attachMovie("CanvasSequenceActivity", "CanvasSequenceActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _canvasController:_canvasController, _canvasBranchView:_canvasBranchView});
					} else if(_canvasComplexView != null) {
						children_mc[i] = childActivities_mc.attachMovie("CanvasSequenceActivity", "CanvasSequenceActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _canvasController:_canvasController, _canvasComplexView:_canvasComplexView});
					} else {
						children_mc[i] = childActivities_mc.attachMovie("CanvasSequenceActivity", "CanvasSequenceActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _canvasController:_canvasController, _canvasView:_canvasView});
					}
				}
				//set the positioning co-ords
				children_mc[i].activity.xCoord = CHILD_OFFSET_X;
				children_mc[i].activity.yCoord = CHILD_OFFSET_Y + (i * CHILD_INCRE);
			
				children_mc[i]._visible = true;
				
				if(_visibleWidth < children_mc[i].getVisibleWidth())  {
					_newVisibleWidth = children_mc[i].getVisibleWidth();
					PANEL_OFFSET_X = (CHILD_OFFSET_X*2);
				}
				
				if(_newVisibleWidth != null) _visibleWidth = _newVisibleWidth;
			}
		}
		
		// set width for children
		for(var i=0; i<children_mc.length; i++)
			if(_newVisibleWidth > children_mc[i].getVisibleWidth()) children_mc[i].setSize(_visibleWidth, null);
		
		ComplexActivity(_activity).noSequences = _children.length;
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
	}
	
	public function removeAllChildren(removeBranchView:Boolean):Void {
		for(var j=0; j<children_mc.length; j++) {
			var childActMC = (children_mc[j] instanceof CanvasActivity) ? CanvasActivity(children_mc[j]) : CanvasSequenceActivity(children_mc[j]);
			if(childActMC instanceof CanvasSequenceActivity) childActMC.removeAllChildren(removeBranchView);
			else if(childActMC.activity.isBranchingActivity() && childActMC.activity.branchView != null && removeBranchView) {
				childActMC.activity.branchView.removeMovieClip();
			}
			
			childActMC.removeMovieClip();
			
		}
		
		children_mc = new Array();
		
	}
	
	public function updateChildren(newChildren:Array):Void {
		_visible = false;
		_visibleWidth = CONTAINER_PANEL_W;
		
		if(newChildren != null) _children = newChildren;
		
		init();
	}
	
	public function refresh():Void {
		draw();
	}
	
	public function refreshChildren():Void {
		for(var i = 0; i < children_mc.length; i ++)		{
			children_mc[i].setSelected(false);
		}
	}
	
	public function getLastItems(i:Number):Array {
		var retArr:Array = new Array();
		
		for(var j=i; j>0; j--)
			retArr.push(children_mc[children_mc.length - j]);
		
		return retArr;
	}
	
	private function showStatus(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		todo_mc._visible = isVisible;
	}
	
	public function setSelected(isSelected){}
	
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
	
	private function drawLearners():Void {
		var mm:MonitorModel = MonitorModel(_monitorController.getModel());
		
		var learner_X = (mm.activeView instanceof CanvasComplexView) ? this._x + learnerOffset_X : _activity.xCoord + learnerOffset_X;
		var learner_Y = (mm.activeView instanceof CanvasComplexView) ? this._y + learnerOffset_Y : _activity.yCoord + learnerOffset_Y;
		
		// get the length of learners from the Monitor Model and run a for loop.
		for (var j=0; j<mm.allLearnersProgress.length; j++){
			var learner:Object = new Object();
			learner = mm.allLearnersProgress[j]
				
			//Gets a true if learner's currect activityID matches this activityID else false.
			var isLearnerCurrentAct:Boolean = Progress.isLearnerCurrentActivity(learner, _activity.activityID);
			var hasPlus:Boolean = false;
			
			if (isLearnerCurrentAct){
				var actX:Number = (mm.activeView instanceof CanvasComplexView) ? this._x : _activity.xCoord;
				
				if (learner_X > (actX + 92)){
					learner_X = actX + learnerOffset_X ;
					learner_Y = 27;
					hasPlus = true;
					
					learnerContainer.attachMovie("learnerIcon", "learnerIcon"+learner.getUserName(), learnerContainer.getNextHighestDepth(),{_activity:_activity, learner:learner, _monitorController:_monitorController, _x:learner_X, _y:learner_Y, _hasPlus:hasPlus });
					learnerContainer.attachMovie("plusIcon", "plusIcon", learnerContainer.getNextHighestDepth(), {_activity:_activity, _monitorController:_monitorController, _x:learner_X+PLUS_MARGIN_X, _y:learner_Y+PLUS_MARGIN_Y});
					return;
				}
					
				learnerContainer.attachMovie("learnerIcon", "learnerIcon"+learner.getUserName(), learnerContainer.getNextHighestDepth(),{_activity:_activity, learner:learner, _monitorController:_monitorController, _x:learner_X, _y:learner_Y, _hasPlus:hasPlus});
				learner_X = learner_X+10;
			}
		}
	}
	
	
	private function draw (){
		
		var numOfChildren = _children.length;
		_panelHeight = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
		
		setStyles();
		
		//write text
		title_lbl.text = _activity.title;
		
		actCount_lbl.text = (type == ACT_TYPE) ? Dictionary.getValue('lbl_num_activities', [children_mc.length]) : Dictionary.getValue('lbl_num_sequences', [children_mc.length]);
		
		//position the container (this)
		if(numOfChildren > 1){
			container_pnl._height = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
		} else {
			container_pnl._height = CHILD_OFFSET_Y + CHILD_INCRE;
		}
		
		if(_type == SEQ_TYPE) {
			container_pnl._height += 3;
			container_pnl._width = _visibleWidth + PANEL_OFFSET_X;
			header_pnl._width = _visibleWidth - 6 + PANEL_OFFSET_X;
			
			clickTarget_mc._width = _visibleWidth + PANEL_OFFSET_X;
		}
		
		_visibleHeight = container_pnl._height;
		
		if(!_canvasComplexView) {
			_x = _activity.xCoord;
			_y = _activity.yCoord;
		}

		//dimentions of container (this)
		setLocking();
			
		if(fromModuleTab == "monitorMonitorTab")
			drawLearners();
		
		Debugger.log ("I am in Draw :" + _activity.title + 'uiID:' + _activity.activityUIID + ' children:' + _children.length, Debugger.GEN, 'Draw', 'CanvasOptionalActivity');
		
		_visible = true;
		
	}
	
	private function setLocking():Void{
		if (_locked){
			clickTarget_mc._height = container_pnl._height;
		}else{
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
			if (_locked && !(_activity.isReadOnly() && (fromModuleTab == null || fromModuleTab == undefined))) {
				_locked = false;
			}else {
				if(_activity.isReadOnly() && (fromModuleTab == null || fromModuleTab == undefined)) { 
					/** TODO: Change label warning */ 
					LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_dbclick_readonly')); 
				}
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
	
	public function get children():Array {
		return children_mc;
	}
	
	public function get panelHeight():Number {
		return _panelHeight;
	}
	
	private function getAssociatedStyle():Object{
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
	
	private function setStyles():Void {
		var styleObj = _tm.getStyleObject ('label');
		title_lbl.setStyle (styleObj);
		styleObj = _tm.getStyleObject ('PIlabel');
		actCount_lbl.setStyle ('styleName', styleObj);
		styleObj = _tm.getStyleObject ('OptHeadPanel');
		header_pnl.setStyle ('styleName', styleObj);
		
		styleObj = getAssociatedStyle();
		container_pnl.setStyle ('styleName', styleObj);
	}
	
	public function set controller(a):Void {
		if(a instanceof CanvasController)
			_canvasController = a;
		else
			_monitorController = a;
	}
	
	public function get type():Number {
		return _type;
	}
}
