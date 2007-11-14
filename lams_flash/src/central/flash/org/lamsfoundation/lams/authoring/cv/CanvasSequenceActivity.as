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
import org.lamsfoundation.lams.authoring.br.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;
import org.lamsfoundation.lams.common.style.*
import mx.controls.*
import com.polymercode.Draw;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*

/**  
* CanvasActivity - 
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasSequenceActivity extends MovieClip implements ICanvasActivity{
  
	public static var TOOL_ACTIVITY_WIDTH:Number = 138;
	public static var TOOL_ACTIVITY_HEIGHT:Number = 55.5;
	public static var ICON_WIDTH:Number = 25;
	public static var ICON_HEIGHT:Number = 25;
	
	private var CHILD_OFFSET_X : Number = 5;
	private var CHILD_OFFSET_Y : Number = 5;
	private var CHILD_INCRE : Number = 55.5;
	
	//this is set by the init object
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _canvasBranchView:CanvasBranchView;
	
	private var _monitorController:MonitorController;
	private var _monitorView;
	
	private var _controller;
	
	private var mm:MonitorModel; // used only when called from Monitor Environment
	private var _canvasModel:CanvasModel;
	
	private var _tm:ThemeManager;
	private var _ccm:CustomContextMenu;
	
	//TODO:This should be ToolActivity
	private var _activity:Activity;
	
	private var _children:Array;
	private var children_mc:Array;
	
	private var childActivities_mc:MovieClip;
	private var learnerContainer:MovieClip;
	
	private var _isSelected:Boolean;
	private var app:ApplicationParent;
	
	private var _module:String;
	
	private var icon_mc:MovieClip;
	private var icon_mcl:MovieClipLoader;
	
	private var bkg_pnl:MovieClip;
	private var act_pnl:MovieClip;
	private var emptyIcon_mc:MovieClip;
	
	private var clickTarget_mc:MovieClip;
	
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	
	private var _visibleWidth:Number;
	private var _visibleHeight:Number;
	
	private var _base_mc:MovieClip;
	private var _selected_mc:MovieClip;

	private var fade_mc:MovieClip;
	private var bgNegative:String = "original";
	private var authorMenu:ContextMenu;
	
	private var _ddm:DesignDataModel;
	
	function CanvasSequenceActivity(){
		_visible = false;
		_tm = ThemeManager.getInstance();
		_ccm = CustomContextMenu.getInstance();
		_ddm = getDDM();
		
		//Get reference to application and design data model
		app = ApplicationParent.getInstance();
		
		//let it wait one frame to set up the components.
		//this has to be set b4 the do later :)
		
		_visibleHeight = CanvasSequenceActivity.TOOL_ACTIVITY_HEIGHT;
		_visibleWidth = CanvasSequenceActivity.TOOL_ACTIVITY_WIDTH;
		
		_base_mc = this;
		
		//call init if we have passed in the _activity as an initObj in the attach movie,
		//otherwise wait as the class outside will call it
		if(_activity != undefined){
			init();
		}
		
	}
	
	public function init(initObj):Void{
		clickTarget_mc.onPress = Proxy.create (this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		
		if(initObj){
			_module = initObj._module;
			if (_module == "monitoring"){
				_monitorView = initObj._monitorView;
				_monitorController = initObj._monitorController;
			}else {
				_canvasView = initObj._canvasView;
				_canvasController = initObj._canvasController;
			}
			
			_activity = initObj.activity;
		}
		
		_canvasModel = CanvasModel(_canvasController.getModel());
		_children = _ddm.getComplexActivityChildren(_activity.activityUIID);
		
		showAssets(false);
		
		if (_activity.selectActivity == "false"){
			_isSelected = false;
			refresh();
		}
		
		removeAllChildren();
		
		children_mc = new Array();
		
		for(var i=0; i<_children.length; i++) {
			if(_module == "monitoring")
				children_mc[i] = childActivities_mc.attachMovie("CanvasActivityMin", "CanvasActivityMin"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_monitorView, _module:"monitoring", learnerContainer:learnerContainer, _sequenceChild:true});
			else
				children_mc[i] = childActivities_mc.attachMovie("CanvasActivityMin", "CanvasActivityMin"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _canvasController:_canvasController, _canvasView:_canvasView, _sequenceChild:true});
		
			if(i == 0)
				SequenceActivity(_activity).firstActivityUIID = children_mc[i].activity.activityUIID;
			//set the positioning co-ords
			children_mc[i].activity.xCoord = CHILD_OFFSET_X + (i * CHILD_INCRE);
			children_mc[i].activity.yCoord = CHILD_OFFSET_Y;
			
			children_mc[i]._visible = true;
		
		}
		
		var _newVisibleWidth = (_children.length*CHILD_INCRE) + (CHILD_OFFSET_X*2) + 6;
		if(_newVisibleWidth > CanvasSequenceActivity.TOOL_ACTIVITY_WIDTH)
			_visibleWidth = _newVisibleWidth;
		
		setStyles();
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
	}
	
	
	public function updateChildren():Void {
		_visible = false;
		init();
	}
	
	public function refreshChildren():Void {
		for(var i = 0; i < children_mc.length; i ++) {
			children_mc[i].setSelected(false);
		}
	}
	
	public function removeAllChildren():Void {
		for(var j=0; j<children_mc.length; j++)
			children_mc[j].removeMovieClip();
	}
	
	private function showAssets(isVisible:Boolean){
		icon_mc._visible = isVisible;
		clickTarget_mc._visible = isVisible;
		fade_mc._visible = isVisible;
	}
	
	/**
	 * Updates the CanvasActivity display fields with the current data
	 * @usage   
	 * @return  
	 */
	public function refresh(setNegative:Boolean):Void{
		bgNegative = String(setNegative);
		setStyles();
		
		draw();
		
		setSelected(_isSelected);
	}
	
	public function setSelected(isSelected){
		Debugger.log(_activity.title+" isSelected:"+isSelected,4,'setSelected','CanvasActivity');
		//var MARGIN = 5;
		
		if(isSelected){
					
			_isSelected = isSelected;
					
		} else {
		}
		
	}
	
	private function setUpActIcon(icon_mc):Void{
		icon_mc._x = (_visibleWidth / 2) - (icon_mc._width / 2);
		icon_mc._y = (_visibleHeight / 2) - (icon_mc._height / 2);
	}
	
	/**
	 * Add + icon to indicate that more users are currently at the Activity. 
	 * We are unable to display all the users across the Activity's panel.
	 * 
	 * @usage   
	 * @param   target  The target reference, this class OR a parent
	 * @param   x_pos  	The X position of the icon
	 * @return  
	 */

	
	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){
		
		// TODO: draw learners?
		
		setStyles();
		
		var theIcon_mc:MovieClip;
		
		clickTarget_mc._visible = true;
		fade_mc._visible = false;
		
		if(_children.length <= 0) {
			emptyIcon_mc._visible = true;
			
			icon_mc._visible = false;
			theIcon_mc = emptyIcon_mc;
			
			setUpActIcon(theIcon_mc);
			theIcon_mc._visible = true;
		}
		
		act_pnl.setSize(_visibleWidth, _visibleHeight);
		
		clickTarget_mc._width = _visibleWidth;
		clickTarget_mc._height = _visibleHeight;
		
		fade_mc._width = _visibleWidth;
		fade_mc._height = _visibleHeight;
		
		if(_activity.isReadOnly() && getDDM().editOverrideLock == 1){
			fade_mc._visible = true;
		}

		_visible = true;
		
		_x = _activity.xCoord;
		_y = _activity.yCoord;
		
		if(_activity.runOffline){
			bgNegative = "true"
			setStyles();
		}
		
	}
	
	private function localOnPress():Void{
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				if (app.controlKeyPressed != "transition"){
					_doubleClicking = true;
					
					if (_module == "monitoring"){
						_monitorController.activityDoubleClick(this, "MonitorTabView");
					} else {
						_canvasController.activityDoubleClick(this);
					}
				}
				
				app.controlKeyPressed = "";
				
			}else{
				
				_doubleClicking = false;
				
				if (_module == "monitoring"){
					_monitorController.activityClick(this);
				}else {
					_canvasController.activityClick(this);
				}			
				
			}
			
			_dcStartTime = now;
	
	}
	
	private function localOnRelease():Void{
		if(!_doubleClicking){
			
			if (_module == "monitoring"){
				_monitorController.activityRelease(this);
			}else {
				_canvasController.activityRelease(this);
			}
			
		}
		
	}
	
	private function localOnReleaseOutside():Void{
		if (_module == "monitoring"){
			_monitorController.activityReleaseOutside(this);
		}else {
			_canvasController.activityReleaseOutside(this);
		}
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
	
	public function setPosition(x:Number, y:Number):Void {
		_x = x;
		_y = y;
	}

	
	public function set isSetSelected(v:Boolean):Void{
		_isSelected = v;
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
		var styleObj:Object = new Object();
		
		styleObj = _tm.getStyleObject('ACTPanel' + String(_activity.orderID%6));
				
		return styleObj;
	}


	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
	
		var styleObj = getAssociatedStyle();
		act_pnl.setStyle('styleName', styleObj);
		
    }
	
	private function getDDM():DesignDataModel {
		if(_module == "monitoring") {
			return _monitorView.ddm;
		} else {
			return _canvasView.ddm;
		}
	}
	
	public function get children():Array {
		return children_mc;
	}
	
	public function setSize(w:Number, h:Number):Void {
		_visibleWidth = w;
		
		draw();
	}

}