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
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.common.style.*;

import com.polymercode.Draw;
import mx.controls.*;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*

/**  
* CanvasBranchingActivity
* This is the UI / view representation of a complex (branching) activity
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasBranchingActivity extends MovieClip implements ICanvasActivity{
  
  
	public static var TOOL_ACTIVITY_WIDTH:Number = 123.1;
	public static var TOOL_ACTIVITY_HEIGHT:Number = 50.5;
	
	public static var ICON_WIDTH:Number = 25;
	public static var ICON_HEIGHT:Number = 25;
  
	private var newContainerXCoord:Number; 
	private var newContainerYCoord:Number;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	
	private var _monitorController:MonitorController;
	private var _monitorView;
	
	private var _tm:ThemeManager;
	private var _ddm:DesignDataModel;
	private var _ccm:CustomContextMenu;
	
	private var app:Application;
	
	private var _activity:Activity;
	private var _children:Array;
	
	private var bkg_pnl:MovieClip;
	private var act_pnl:MovieClip;
	private var branchIcon_mc:MovieClip;
	
	private var title_lbl:MovieClip;
	private var _module:String;
	
	private var canvasActivity_mc:MovieClip;
	private var canvasActivityGrouped_mc:MovieClip;
	
	private var _base_mc:MovieClip;
	private var _selected_mc:MovieClip;
	private var fade_mc:MovieClip;
	
	private var bgNegative:String = "original";
	private var authorMenu:ContextMenu;
	
	private var clickTarget_mc:MovieClip;
	
	private var learnerOffset_X:Number = 4;
	private var learnerOffset_Y:Number = 3;
	
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	
	private var _locked:Boolean;
	private var _isSelected:Boolean;
	
	function CanvasBranchingActivity(){
		Debugger.log("_activity:"+_activity.title+'uiID:'+_activity.activityUIID+' children:'+_children.length,Debugger.GEN,'Constructor','CanvasBranchingActivity');
		
		_locked = false;
		
		_tm = ThemeManager.getInstance();
		_ccm = CustomContextMenu.getInstance();
		app = Application.getInstance();
		
		_visibleHeight = CanvasBranchingActivity.TOOL_ACTIVITY_HEIGHT;
		_visibleWidth = CanvasBranchingActivity.TOOL_ACTIVITY_WIDTH;
		
		_base_mc = this;
		
		//call init if we have passed in the _activity as an initObj in the attach movie,
		//otherwise wait as the class outside will call it
		if(_activity != undefined){
			init();
		}
	}
	
	public function init(initObj):Void{
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

		//_ddm.getComplexActivityChildren(_activity.activityUIID);

		showAssets(false);
		
		if (_activity.selectActivity == "false"){
			_isSelected = false;
			refresh();
		}
		
		setStyles();
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
		
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
			if(_activity.groupingUIID > 0) {
				tgt_mc = canvasActivityGrouped_mc;
			} else {
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
	
	public function refreshChildren():Void {
		
	}
	
	private function showAssets(isVisible:Boolean){
		title_lbl._visible = isVisible;
		
		canvasActivity_mc._visible = isVisible;
		canvasActivityGrouped_mc._visible = isVisible;
		
		clickTarget_mc._visible = isVisible;
		fade_mc._visible = isVisible;
	}
	public function get activity():Activity{
		return activity
	}
	
	public function set activity(a:Activity){
		_activity = a;
	}
	
	public function getActivity():Activity{
		return _activity;

	}
	
	public function setActivity(a:Activity){
		_activity = a;
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
	
	/**
	 * Does the work of laying out the screen assets.
	 * Depending on type of Activity different bits will be shown
	 * @usage   
	 * @return  
	 */
	private function draw(){
		
		Debugger.log(_activity.title+',_activity.isBranchingActivity():'+_activity.isBranchingActivity(),4,'draw','CanvasBranchingActivity');
		setStyles();
		
		var theIcon_mc:MovieClip;
		title_lbl._visible = true;
		clickTarget_mc._visible = true;
		fade_mc._visible = false;
			
		if(_activity.isReadOnly() && app.canvas.ddm.editOverrideLock == 1){
			Debugger.log("Making transparent layer visible. ", Debugger.CRITICAL, 'draw', 'CanvasActivity');
			fade_mc._visible = true;
		}
		
		//chose the icon:
		if(_activity.isBranchingActivity()){
			branchIcon_mc._visible = true;
			theIcon_mc = branchIcon_mc;
		}else{
			branchIcon_mc._visible = false;
			theIcon_mc = null;
		}
			
		theIcon_mc._visible = true;
			
		//chose the background mc
		if(_activity.groupingUIID > 0){
			canvasActivityGrouped_mc._visible = true;
			canvasActivity_mc._visible=false;
		}else{
			canvasActivity_mc._visible=true;
			canvasActivityGrouped_mc._visible = false;
		}
			
		//write text
		title_lbl.text = _activity.title;
			
		clickTarget_mc._width = TOOL_ACTIVITY_WIDTH;
		clickTarget_mc._height= TOOL_ACTIVITY_HEIGHT;

		//position
		_x = _activity.xCoord;
		_y = _activity.yCoord;
		
		Debugger.log('canvasActivity_mc._visible'+canvasActivity_mc._visible,4,'draw','CanvasActivity');
		
		if (_activity.runOffline){
			bgNegative = "true"
			setStyles();
		}
	}
	
	private function setLocking():Void{
		if(_locked){
			//rollOver_mc._visible = true;
		}else{
			//rollOver_mc._visible = true;
		}
	}
	
	public function set locked(setLock:Boolean):Void {
		_locked = setLock;
		
		setLocking();
		
	}
	
	public function get locked():Boolean {
		return _locked;
	}
	
	private function onRollOver():Void{
		if (_module == "monitoring"){
			_ccm.showCustomCM(_ccm.loadMenu("activity", "monitoring"))
		}else {
			_ccm.showCustomCM(_ccm.loadMenu("activity", "authoring"))
		}
	}
	
	private function onRollOut():Void{
		if (_module == "monitoring"){
			_ccm.showCustomCM(_ccm.loadMenu("canvas", "monitoring"))
		}else {
			_ccm.showCustomCM(_ccm.loadMenu("canvas", "authoring"))
		}
	}	
	
	private function onPress():Void{
			
			// check double-click
			var now:Number = new Date().getTime();
			
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				Debugger.log('DoubleClicking:'+this,Debugger.GEN,'localOnPress','CanvasBranchingActivity');
				_doubleClicking = true;
				
				//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
				if(_locked && !_activity.isReadOnly()){
					locked = false;
				} else {
					if(_activity.isReadOnly()) { /** TODO: Change label warning */ LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_dbclick_readonly')); }
					locked = true;
				}
				
				draw();
				
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'localOnPress','CanvasBranchingActivity');
				_doubleClicking = false;
				_canvasController.activityClick(this);
				
			}
			
			_dcStartTime = now;
	
	}
	
	private function onRelease():Void{
		Debugger.log('_doubleClicking:'+_doubleClicking+', localOnRelease:'+this,Debugger.GEN,'localOnRelease','CanvasParallelActivity');
		if (!_doubleClicking)	{
				_canvasController.activityRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		Debugger.log('localOnReleaseOutside:'+this,Debugger.GEN,'localOnReleaseOutside','CanvasParallelActivity');
		_canvasController.activityReleaseOutside(this);
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	
	private function setStyles() {
		var my_color:Color = new Color(this);
		var styleObj;
		
		var transNegative = {ra:-100, ga:-100, ba:-100, rb:255, gb:255, bb:255};
		var transPositive = {ra:100, ga:100, ba:100, rb:0, gb:0, bb:0};
		
		styleObj = _tm.getStyleObject('CALabel');
		title_lbl.setStyle('styleName',styleObj);
		title_lbl.setStyle("textAlign", "center")
		
		if(bgNegative == "true") {
			my_color.setTransform(transNegative);
		} else if(bgNegative == "false") {
			my_color.setTransform(transPositive);
		} else if(bgNegative == "original") {
			styleObj = getAssociatedStyle()	//_tm.getStyleObject('ACTPanel')
			act_pnl.setStyle('styleName',styleObj);
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
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get children():Array {
		return _children;
	} 


}