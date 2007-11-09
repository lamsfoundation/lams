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
  
	public static var TOOL_ACTIVITY_WIDTH:Number = 123.1;
	public static var TOOL_ACTIVITY_HEIGHT:Number = 50.5;
	public static var ICON_WIDTH:Number = 25;
	public static var ICON_HEIGHT:Number = 25;
	
	private var CHILD_OFFSET_X : Number = 8;
	private var CHILD_OFFSET_Y : Number = 57;
	private var CHILD_INCRE : Number = 60;
	
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
	
	private var canvasActivity_mc:MovieClip;
	private var canvasActivityGrouped_mc:MovieClip;
	
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
		
		_visibleHeight = CanvasActivity.TOOL_ACTIVITY_HEIGHT;
		_visibleWidth = CanvasActivity.TOOL_ACTIVITY_WIDTH;
		
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
				children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _monitorController:_monitorController, _monitorView:_monitorView, _module:"monitoring", learnerContainer:learnerContainer});
			else
				children_mc[i] = childActivities_mc.attachMovie("CanvasActivity", "CanvasActivity"+i, childActivities_mc.getNextHighestDepth(), {_activity:_children[i] , _canvasController:_canvasController, _canvasView:_canvasView});
		}
		
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
		canvasActivity_mc._visible = isVisible;
		clickTarget_mc._visible = isVisible;
		canvasActivityGrouped_mc._visible = isVisible;
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
		var MARGIN = 5;
		
		if(isSelected){
			//draw a selected border
			var tgt_mc;
			if(_activity.groupingUIID > 0){
				tgt_mc = canvasActivityGrouped_mc;
			}else{
				tgt_mc = canvasActivity_mc;
			}
			
			Debugger.log("tgt_mc:"+tgt_mc,4,'setSelected','CanvasActivity');
				
			//vars
			var tl_x = tgt_mc._x - MARGIN; 											//top left x
			var tl_y = tgt_mc._y - MARGIN;											//top left y
			var tr_x = tgt_mc._x + tgt_mc._width + MARGIN;							//top right x
			var tr_y = tl_y;														//top right y
			var br_x = tr_x;														//bottom right x
			var br_y = tgt_mc._y + tgt_mc._height + MARGIN;							//bottom right y
			var bl_x = tl_x;														//biottom left x															
			var bl_y = br_y;														//bottom left y
				
			if(_selected_mc){
				_selected_mc.removeMovieClip();
			}
			
			_selected_mc = _base_mc.createEmptyMovieClip('_selected_mc',_base_mc.getNextHighestDepth());
				
			var dashStyle:mx.styles.CSSStyleDeclaration = _tm.getStyleObject("CAHighlightBorder");
			var color:Number = dashStyle.getStyle("color");
				
			Draw.dashTo(_selected_mc,tl_x,tl_y,tr_x,tr_y,2,3,2,color);
			Draw.dashTo(_selected_mc,tr_x,tr_y,br_x,br_y,2,3,2,color);
			Draw.dashTo(_selected_mc,br_x,br_y,bl_x,bl_y,2,3,2,color);
			Draw.dashTo(_selected_mc,bl_x,bl_y,tl_x,tl_y,2,3,2,color);
						
			_isSelected = isSelected;
					
		} else {
			//hide the selected border
			_selected_mc.removeMovieClip();
		}
		
	}
	
	private function setUpActIcon(icon_mc):Void{
		icon_mc._x = (CanvasActivity.TOOL_ACTIVITY_WIDTH / 2) - (icon_mc._width / 2);
		icon_mc._y = (CanvasActivity.TOOL_ACTIVITY_HEIGHT / 2) - (icon_mc._height / 2) - 6;
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
			
		if(_activity.isReadOnly() && getDDM().editOverrideLock == 1){
			fade_mc._visible = true;
		}

		if(_children.length <= 0) {
			emptyIcon_mc._visible = true;
			
			icon_mc._visible = false;
			theIcon_mc = emptyIcon_mc;
			
			setUpActIcon(theIcon_mc);
			theIcon_mc._visible = true;
		}
		
		//chose the background mc
		if(_activity.groupingUIID > 0){
			canvasActivityGrouped_mc._visible = true;
			canvasActivity_mc._visible = false;
		}else{
			canvasActivity_mc._visible = true;
			canvasActivityGrouped_mc._visible = false;
		}
			
		clickTarget_mc._width = TOOL_ACTIVITY_WIDTH;
		clickTarget_mc._height = TOOL_ACTIVITY_HEIGHT;
		
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
					}else {
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
		styleObj = _tm.getStyleObject('ACTPanel');

		return styleObj;
	}


	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
		/**var my_color:Color = new Color(this);
		var transNegative = {ra:-100, ga:-100, ba:-100, rb:255, gb:255, bb:255};
		var transPositive = {ra:100, ga:100, ba:100, rb:0, gb:0, bb:0};
		
		if (bgNegative == "true"){
			my_color.setTransform(transNegative);
		}else if(bgNegative == "false"){
			my_color.setTransform(transPositive);
		}else if(bgNegative == "original"){
			
			if (this.activity.parentUIID != null || this.activity.parentUIID != undefined){
				if (_module != "monitoring"){
					var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(this.activity.parentUIID)
				}else {
					var parentAct = mm.getMonitor().ddm.getActivityByUIID(this.activity.parentUIID)
				}
				*/	
				var styleObj = getAssociatedStyle();
				act_pnl.setStyle('styleName',styleObj);
				
			//} else {
		//		styleObj = getAssociatedStyle();
		//		act_pnl.setStyle('styleName',styleObj);
		//	}
		//	
		//}
		
    }
	
	private function getDDM():DesignDataModel {
		if(_module == "monitoring") {
			return _monitorView.ddm;
		} else {
			return _canvasView.ddm;
		}
	}

}