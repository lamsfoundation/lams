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
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.authoring.cv.ICanvasActivity;
import org.lamsfoundation.lams.learner.ls.LessonController;
import org.lamsfoundation.lams.monitoring.mv.MonitorController;
import org.lamsfoundation.lams.common.style. *;
import mx.controls. *;
import mx.managers. *
/**
* CanvasOptionalActivity
* This is the UI / view representation of a complex (Optional) activity
*/
class LearnerComplexActivity extends MovieClip implements ICanvasActivity
{
	//class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip {
	private var CHILD_OFFSET_X : Number = 8;
	private var CHILD_OFFSET_Y : Number = 57;
	private var CHILD_INCRE : Number = 60;
	
	private var LABEL_W:Number = 130;
	private var LABEL_H:Number = 22;
	
	//this is set by the init object
	private var _controller:AbstractController;
	private var _view:AbstractView;
	private var _tip:ToolTip;
	//Set by the init obj
	private var _activity : Activity;
	private var _children : Array;
	private var _panelHeight : Number;
	
	//refs to screen items:
	private var container_pnl : Panel;
	private var title_lbl:MovieClip;
	private var labelHolder_mc:MovieClip;
	//locals
	private var actStatus:String;
	private var childActivities_mc : MovieClip;
	private var complexActivity_mc : MovieClip;
	
	private var clickTarget_mc : MovieClip;
	private var _dcStartTime : Number = 0;
	private var _doubleClicking : Boolean;
	private var _locked:Boolean;
	
	// Only for Learner Optional Container children
	private var learner:Progress;
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var attempted_mc:MovieClip;
	private var todo_mc:MovieClip;
	private var childHolder_mc:MovieClip;
	var children_mc:Array
	//---------------------------//
	
	private var child_mc : MovieClip;
	private var _visibleHeight : Number;
	private var _visibleWidth : Number;
	
	private var _tm : ThemeManager;
	private var _ddm : DesignDataModel;
	private var app:ApplicationParent;
	
	function LearnerComplexActivity ()
	{
		complexActivity_mc = this
		_ddm = new DesignDataModel ();
		app = ApplicationParent.getInstance();
		_visible = false;
		_tm = ThemeManager.getInstance ();
		_tip = new ToolTip();
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		MovieClipUtils.doLater (Proxy.create (this, init));
	}
	
	public function init () : Void
	{
		var styleObj = _tm.getStyleObject('smallLabel');
		title_lbl = labelHolder_mc.attachMovie("Label", "actTitle", this.getNextHighestDepth(), {_width:LABEL_W, _height:LABEL_H, autoSize:"center", styleName:styleObj});
		

		children_mc = new Array();
		var childrenArray:Array;
		
		childActivities_mc = this;
		_locked = false;
		showStatus(false);
		clickTarget_mc.onRollOver = Proxy.create(this, localOnRollOver);
		clickTarget_mc.onRollOut = Proxy.create(this, localOnRollOut);
		clickTarget_mc.onPress = Proxy.create(this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create(this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create(this, localOnReleaseOutside);
		
		
		if(_activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
			if(_children[0].orderID < _children[1].orderID){
				childrenArray = orderParallelActivities(_children[0],_children[1]);
			}else{
				childrenArray = orderParallelActivities(_children[1],_children[0]);
			}
		} else {
			childrenArray = _children;
		}
		var childCoordY = 0
		for (var i = 0; i < childrenArray.length; i ++)
		{
			var progStatus:String = Progress.compareProgressData(learner, childrenArray[i].activityID);
			
			children_mc [i] = childHolder_mc.attachMovie("LearnerActivity_forComplex", "LearnerActivity_forComplex"+i, childHolder_mc.getNextHighestDepth(), {_activity:childrenArray[i], _controller:_controller, _view:_view, learner:learner, actStatus:progStatus, _complex:true, xPos:this._x, yPos:childCoordY});
			Debugger.log('attaching child movieL ' + children_mc[i],Debugger.CRITICAL,'init','LearnerComplexActivity');
        
			//set the positioning co-ords
			children_mc [i]._y = (i*21);
			childCoordY = this._y + ((i+1)*21)+29 ;
			children_mc [i]._visible = true;
			
		}
		
		childHolder_mc._visible = false;
		
		MovieClipUtils.doLater (Proxy.create (this, draw));
	}
	
	private function showStatus(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		attempted_mc._visible = isVisible;
		todo_mc._visible = isVisible;
		
	}
	
	public function get activity () : Activity
	{
		return getActivity ();
	}
	
	public function set activity (a : Activity)
	{
		setActivity (a);
	}
	
	public function getActivity () : Activity
	{
		return _activity;
	}
	
	public function setActivity (a : Activity)
	{
		_activity = a;
	}
	
	public function refresh(){
		showStatus(false);
		learner = controller.getModel().progressData;
		actStatus = null;
		
		for(var i=0; i<children_mc.length;i++){
			children_mc[i].refresh();
		}
		
		draw();
	}
	
	private function hasCurrentChild(){
		for(var i=0; i<children_mc.length;i++){
			if(children_mc[i].activityStatus == 'current_mc'){
				return true;
			}
		}
		return false;
	}
	
	private function draw (){
		
		if (actStatus == null || actStatus == undefined){
			actStatus = Progress.compareProgressData(learner, _activity.activityID);
		}
		
		switch (actStatus){
		    case 'completed_mc' :
				completed_mc._visible = true;
                break;
            case 'current_mc' :
				current_mc._visible = true;
                break;
            case 'attempted_mc' :
			if(_root.mode == 'preview'){  
				if(hasCurrentChild()){current_mc._visible = true; } 
				else { attempted_mc._visible = true; } }
			else {current_mc._visible = true; }
                break;
			default :
				todo_mc._visible = true;
                //Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}
		
		var numOfChildren = _children.length;
		panelHeight = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);

		//write text
		if(_activity.title != undefined){
			title_lbl.text = _activity.title;
			containerPanelHeader.title_lbl.text = _activity.title;
		} else {
			if(_activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE){
				title_lbl.text = 'Optional Activities';
				containerPanelHeader.title_lbl.text = 'Optional Activities';
			} else {
				title_lbl.text = 'Parallel Activities';
				containerPanelHeader.title_lbl.text = 'Parallel Activities';
			}
		}
		
		container_pnl.setStyle ("backgroundColor", 0x4289FF);
		
		//position the container (this)
		container_pnl._height = 16+(numOfChildren * 21);
		
		Debugger.log ("I am in Draw :" + _activity.title + 'uiID:' + _activity.activityUIID + ' children:' + _children.length, Debugger.GEN, 'Draw', 'LearnerOptionalActivity');
		_visible = true;
		
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var appData = getAppData();
		var Xpos = appData.compX+ this._x;
		var Ypos = appData.compY+( (this._y+btnObj._height)-4);
		var ttHolder = appData.ttHolder;
		trace("x pos: "+Xpos+" and y pos: "+Ypos+" and tt holder is: "+ttHolder)
		var ttMessage = Dictionary.getValue(btnTT);
		var ttWidth = 140;
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, undefined, ttWidth);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function localOnRollOver(){
		if (actStatus == "completed_mc"){
			showToolTip(this.completed_mc, "completed_act_tooltip");
		}else if (actStatus == "current_mc"){
			showToolTip(this.current_mc, "current_act_tooltip");
		}else if (actStatus == "attempted_mc"){
			showToolTip(this.current_mc, "current_act_tooltip");
		}
		
	}
	
	private function localOnRollOut(){
		
		hideToolTip();
	}

	private function localOnPress():Void{
		hideToolTip();
		this.swapDepths(this._parent.getNextHighestDepth());
		// check double-click
		var now : Number = new Date ().getTime ();
		if ((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY)	{
			Debugger.log ('DoubleClicking:' + this, Debugger.GEN, 'localOnPress', 'LearnerOptionalActivity');
			_doubleClicking = true;
			//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
			draw ();
			controller.activityDoubleClick(this);
		
		}else {
			Debugger.log ('SingleClicking:+' + this, Debugger.GEN, 'localOnPress', 'LearnerOptionalActivity');
			_doubleClicking = false;
		}
		_dcStartTime = now;
	}
	
	
	private function localOnRelease():Void{
		Debugger.log ('_doubleClicking:' + _doubleClicking + ', localOnRelease:' + this, Debugger.GEN, 'localOnRelease', 'LearnerOptionalActivity');
			if (_locked && !_doubleClicking){
				collapse();
				controller.complexActivityRelease(this, _doubleClicking);
				
			} else {
				expand();
				controller.complexActivityRelease(this,_doubleClicking);
				
			}
	}
	
	
	private function localOnReleaseOutside():Void {
		Debugger.log ('localOnReleaseOutside:' + this, Debugger.GEN, 'localOnReleaseOutside', 'LearnerOptionalActivity');
	}
	
	public function collapse():Void{
		_locked = false;
		gotoAndStop('collapse');
		childHolder_mc._visible = false;
		draw();
	}
	
	public function expand():Void{
		_locked = true;
		childHolder_mc._visible = true;
		gotoAndStop('expand')
		draw();
	}
	
	/**
	 * return new children array
	 * 
	 * @usage   
	 * @param   child1 First child
	 * @param   child2 Second child
	 * @return  new ordered children array
	 */
	
	private function orderParallelActivities(child1:Activity, child2:Activity):Array{
		var a:Array = new Array();
		a.push(child1);
		a.push(child2);
		
		return a;
	}
	
	public function get controller(){
		if(isLearnerModule()){
			return LessonController(_controller);
		} else {
			return MonitorController(_controller);
		}
	}
	
	public function getAppData():Object{
		return controller.appData;
	}
	
	public function isLearnerModule():Boolean{
		if(app.module == 'learner'){
			return true;
		} else {
			return false;
		}
	}
	
	public function get activityStatus():String{
		return actStatus;
	}
	
	public function set progressData(a:Progress){
		learner = a;
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
	public function get locked():Boolean {
		return _locked;
	}
	public function get activityChildren():Array {
		return _children;
	}
	public function get doubleClicking():Boolean {
		return _doubleClicking;
	}
	public function get panelHeight():Number {
		return _panelHeight;
	}

}
