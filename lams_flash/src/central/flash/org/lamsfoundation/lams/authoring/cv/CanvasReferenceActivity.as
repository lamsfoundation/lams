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
* CanvasReferenceActivity
* This is the UI / view representation of a complex (Reference) activity
*/
class org.lamsfoundation.lams.authoring.cv.CanvasReferenceActivity extends MovieClip implements ICanvasActivity{

	private var CHILD_OFFSET_X:Number = 8;
	private var CHILD_OFFSET_Y:Number = 57;
	private var CHILD_INCRE:Number = 134.8;
	
	private var CONTAINER_PANEL_W:Number = 142.8;
	
	public static var PLUS_MARGIN_X:Number = 15;
	public static var PLUS_MARGIN_Y:Number = 10;
	
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
	
	private var _panelWidth:Number;
	
	private var actMinActivities:Number;
	private var actMaxActivities:Number;
	
	//refs to screen items:
	private var container_pnl:Panel;
	private var header_pnl:Panel;
	
	private var title_lbl:Label;
	private var actCount_lbl:Label;
	
	//locals
	private var childActivities_mc:MovieClip;
	private var referenceActivity_mc:MovieClip;
	private var clickTarget_mc:MovieClip;
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	
	private var fromModuleTab:String;
	private var child_mc:MovieClip;
	private var _locked:Boolean = false;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	function CanvasReferenceActivity()	{
		referenceActivity_mc = this;
		
		_visible = false;
		
		_tm = ThemeManager.getInstance ();
		_dictionary = Dictionary.getInstance();
		
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		_ca = new ComplexActivity(_activity.activityUIID)
		_activity.activityCategoryID = Activity.CATEGORY_SYSTEM;
		
		MovieClipUtils.doLater(Proxy.create(this, init));
	}
	
	public function init():Void {
		
		clickTarget_mc.onPress = Proxy.create(this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this, localOnReleaseOutside);
		
		actMinActivities = _ca.minActivities;
		actMaxActivities = _ca.maxActivities;
		
		if (controller instanceof CanvasController) {
			_canvasView.model.ddm.referenceActivityID = _activity.activityID;
			_canvasView.model.ddm.referenceActivityUIID = _activity.activityUIID;
		}
		
		removeAllChildren();		
		
		for (var i=0; i < _children.length; i++) {
			if(fromModuleTab == "monitorMonitorTab") {
				children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_monitorTabView, _module:"monitoring", learnerContainer:null});
			} else {
				children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth (), {_activity:_children[i] , _canvasController:_canvasController, _canvasView:_canvasView});
			}
			
			//set the positioning co-ords
			children_mc[i].activity.xCoord = CHILD_OFFSET_X + (i * CHILD_INCRE);
			children_mc[i].activity.yCoord = CHILD_OFFSET_Y;
			
			children_mc[i]._visible = true;
		}
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
	}
	
	public function removeAllChildren():Void {
		for(var j=0; j<children_mc.length; j++) {
			var childActMC = CanvasActivity(children_mc[j]);
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
	
	private function draw (){
		
		var numOfChildren = _children.length;
		
		if (numOfChildren == 0) {
			_panelWidth = 143.1;
			title_lbl._width = _panelWidth - 15;
		}
		else {
			_panelWidth = CHILD_OFFSET_X + (numOfChildren * CHILD_INCRE);
			title_lbl._width = _panelWidth - 15;
		}
		
		setStyles();
		
		//write text
		title_lbl.text = _activity.title;
		actCount_lbl.text = Dictionary.getValue('lbl_num_activities', [children_mc.length]);

		//position the container (this)
		if(numOfChildren > 1){
			container_pnl._width = CHILD_OFFSET_X + (numOfChildren * CHILD_INCRE);
			clickTarget_mc._width = container_pnl._width;
			header_pnl.setSize(container_pnl._width - 6.8, header_pnl._height); // 6.8 corresponds to the right spacing

		} else {
			container_pnl._width = CHILD_OFFSET_X + CHILD_INCRE;
		}
		
		_visibleHeight = container_pnl._height;
		
		if(!_canvasComplexView) {
			_x = _activity.xCoord;
			_y = _activity.yCoord;
		}

		//dimensions of container (this)
		setLocking();
		
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
			Debugger.log ('DoubleClicking:' + this, Debugger.GEN, 'localOnPress', 'CanvasReferenceActivity');
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
			Debugger.log ('SingleClicking:+' + this, Debugger.GEN, 'localOnPress', 'CanvasReferenceActivity');
			_doubleClicking = false;
			_canvasController.activityClick (this);
		}
		_dcStartTime = now;
	}
	
	
	private function localOnRelease ():Void{
		Debugger.log ('_doubleClicking:' + _doubleClicking + ', localOnRelease:' + this, Debugger.GEN, 'localOnRelease', 'CanvasReferenceActivity');
		if (! _doubleClicking)	{
				_canvasController.activityRelease (this);
		}
	}
	
	
	private function localOnReleaseOutside():Void {
		Debugger.log ('localOnReleaseOutside:' + this, Debugger.GEN, 'localOnReleaseOutside', 'CanvasReferenceActivity');
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
	
	public function get panelWidth():Number {
		return _panelWidth;
	}
	
	private function getAssociatedStyle():Object{
		
		var styleObj:Object = new Object();
		styleObj = _tm.getStyleObject('ACTPanel1')
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
	
	public function get controller():Object {
		if (fromModuleTab == "monitorMonitorTab") {
			return _monitorController;
		} else {
			return _canvasController;
		}
	}
	
	public function set controller(a):Void {
		if(a instanceof CanvasController)
			_canvasController = a;
		else
			_monitorController = a;
	}
}
