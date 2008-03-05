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
import org.lamsfoundation.lams.authoring.SequenceActivity;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.authoring.cv.ICanvasActivity;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.Application;
import org.lamsfoundation.lams.monitoring.mv.MonitorModel;
import org.lamsfoundation.lams.monitoring.mv.MonitorController;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LessonTabView;
import org.lamsfoundation.lams.common.style. *;

import mx.controls. *;
import mx.managers. *
/**
* CanvasOptionalActivity
* This is the UI / view representation of a complex (Optional) activity
*/
class LearnerComplexActivity extends MovieClip implements ICanvasActivity
{
	private var CHILD_OFFSET_X : Number = 8;
	private var CHILD_OFFSET_Y : Number = 57;
	private var CHILD_INCRE : Number = 60;
	
	private var LABEL_W:Number = 130;
	private var LABEL_H:Number = 22;
	
	private var count:Number;
	
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
	
	private var child_mc:MovieClip;
	private var _visibleHeight:Number;
	private var _visibleWidth:Number;
	
	private var _tm:ThemeManager;
	private var _ddm:DesignDataModel;
	private var app:ApplicationParent;
	
	private var activeSequence:SequenceActivity;
	
	function LearnerComplexActivity () {
		complexActivity_mc = this;
		activeSequence = null;
		
		app = ApplicationParent.getInstance();
		
		_visible = false;
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		MovieClipUtils.doLater(Proxy.create (this, init));
	}
	
	public function init():Void {
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
			} else {
				childrenArray = orderParallelActivities(_children[1],_children[0]);
			}
		} else {
			childrenArray = _children;
		}
		
		drawChildren(childrenArray, children_mc);
		
		childHolder_mc._visible = false;
		
		MovieClipUtils.doLater(Proxy.create(this, draw, Proxy.create(this, checkIfSequenceActive)));
	}
	
	private function drawChildren(children:Array, container:Array, _count:Number):Void {
		count = (_count != null) ? _count : 0;
		
		var childCoordY:Number = 0;
		
		for(var i=0; i<children.length; i++) {
			var progStatus:String = Progress.compareProgressData(learner, children[i].activityID);
			Debugger.log("progStatus: " + progStatus, Debugger.CRITICAL, "drawChildren", "LearnerComplexActivity");
			
			var learnerAct:LearnerActivity = LearnerActivity(childHolder_mc.createChildAtDepth("LearnerActivity_forComplex", childHolder_mc.getNextHighestDepth(), {_activity:children[i], _controller:_controller, _view:_view, learner:learner, actStatus:progStatus, _complex:true, xPos:this._x, yPos:childCoordY}));
			Debugger.log('attaching child movieL ' + learnerAct,Debugger.CRITICAL,'drawChildren','LearnerComplexActivity');
			
			//set the positioning co-ords
			//learnerAct.activityStatus = progStatus;
			
			learnerAct._y = (count*21);
			
			childCoordY = this._y + ((count+1)*21) + 29;
			
			learnerAct._visible = true;
			
			Debugger.log('x: ' + learnerAct._x + ' y: ' +  learnerAct._y, Debugger.CRITICAL, 'drawChildren', 'LearnerComplexActivity');
        
			var parentAct:Activity = (model instanceof LessonModel) ? model.learningDesignModel.getActivityByUIID(Activity(children[i]).parentUIID) : model.ddm.getActivityByUIID(Activity(children[i]).parentUIID);
			
			if((activity.isBranchingActivity() && parentAct.isSequenceActivity()) || (activity.isOptionsWithSequencesActivity() && parentAct.isSequenceActivity())) {
				/** TODO: Use for Sequence in Optional */
				learnerAct.lineTopVisible = (i != 0) ? false : true;
				learnerAct.lineBottomVisible = (i == children.length-1) ? true : false;
			}
			
			container.push(learnerAct);
			count++;
			
			if(learnerAct.activity == activeSequence) {
				var actOrder:Array = model.getDesignOrder(activeSequence.firstActivityUIID);
				drawChildren(actOrder, container, count);
			}
		}
		
	}
	
	private function showStatus(isVisible:Boolean) {
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
		attempted_mc._visible = isVisible;
		todo_mc._visible = isVisible;
	}
	
	public function get activity():Activity {
		return getActivity();
	}
	
	public function set activity(a:Activity) {
		setActivity(a);
	}
	
	public function getActivity():Activity {
		return _activity;
	}
	
	public function setActivity(a:Activity) {
		_activity = a;
	}
	
	public function refresh() {
		showStatus(false);
		learner = (model instanceof LessonModel) ? model.progressData : learner;
		actStatus = null;
		
		checkIfSequenceActive();
		
		draw();
	}
	
	/** TODO: Use for Sequence in Optional */
	private function checkIfSequenceActive():Void {
		for(var i=0; i<children_mc.length; i++) {
			
			var isChildCurrent:Boolean = (Progress.compareProgressData(learner, children_mc[i].activity.activityID) == 'current_mc');
			var isChildAttempted:Boolean = (Progress.compareProgressData(learner, children_mc[i].activity.activityID) == 'attempted_mc');
			
			if(isChildCurrent && children_mc[i].activity.isSequenceActivity()) {
				// set activesequence if is current
				if(children_mc[i].activity != activeSequence)
					removeAllChildrenAndInputSequence(children_mc[i].activity);
			} else if(isChildAttempted && children_mc[i].activity.isSequenceActivity()) {
				// check children of sequence (level 1) for current activity
				if(model.checkSequenceHasCurrentActivity(children_mc[i].activity, learner))
					removeAllChildrenAndInputSequence(children_mc[i].activity);
			}
		}
		
		Debugger.log("refreshing children: " + children_mc.length, Debugger.CRITICAL, "checkIfSequenceActive", "LearnerComplexActivity");
		
		var closeBox:Boolean = true;
		for(var i=0; i<children_mc.length; i++) {
			if(children_mc[i].activityStatus != "completed_mc") closeBox = false;
			children_mc[i].refresh();
			
			/**if(activity.isBranchingActivity() 
				&& children_mc[i].activity.isSequenceActivity() 
				&& (children_mc[i].isAttempted || children_mc[i].isCompleted)) 
				removeAllChildrenAndInputSequence(SequenceActivity(children_mc[i].activity));*/
		
		}	
		
		if(closeBox && locked) {
			collapse();
			controller.complexActivityRelease(this, false);
		}
	}
	
	private function removeAllChildren():Void {
		
		Debugger.log("removing children len: " + children_mc.length, Debugger.CRITICAL, "removeAllChildren", "LearnerComplexActivity");
		
		for(var i=0; i<children_mc.length;i++){
			Debugger.log("removing children: " + children_mc[i].activity.activityUIID, Debugger.CRITICAL, "removeAllChildren", "LearnerComplexActivity");
			LearnerActivity(children_mc[i]).destroy();
		}
		
		children_mc = new Array();
	}
	
	/** TODO: Use for Sequence in Optional */
	private function removeAllChildrenAndLoadSequences(activity:Activity):Void {
		activeSequence = null;
		removeAllChildren();
		
		children_mc = new Array();
		
		drawChildren(_children, children_mc);
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
	}
	
	/** TODO: Use for Sequence in Optional */
	public function removeAllChildrenAndInputSequence(activity:SequenceActivity):Void {
		activeSequence = activity;
		redrawComplex();
		
		if(!locked) { 
			localOnPress();
			localOnRelease();
		}
	}
	
	private function redrawComplex():Void {
		removeAllChildren();
		drawChildren(_children, children_mc);
		MovieClipUtils.doLater(Proxy.create(this, draw));
	}
	
	private function hasCurrentChild(){
		for(var i=0; i<children_mc.length;i++){
			if(children_mc[i].isCurrent){
				return true;
			}
		}
		
		return false;
	}
	
	private function draw (callback:Function){
		var toolTitle:String;
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
					if(hasCurrentChild()) { current_mc._visible = true; } 
					else { attempted_mc._visible = true; } }
				else { current_mc._visible = true; }
                break;
			default :
				todo_mc._visible = true;
		}
		
		var numOfChildren:Number = children_mc.length;
		panelHeight = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);

		//write text
		toolTitle = _activity.title;
		
		if (toolTitle.length > 19)
			toolTitle = toolTitle.substr(0, 17)+"...";
		
		title_lbl.text = toolTitle;
		containerPanelHeader.title_lbl.text = toolTitle;
		container_pnl.setStyle("backgroundColor", 0x4289FF);
		
		//position the container (this)
		container_pnl._height = 16 + (numOfChildren * 21);
		
		_visible = true;
		
		if(callback != null)
			callback();
		
		if(_view instanceof LearnerTabView)
			LearnerTabView(_view).drawNext();
			
	}
	
	public function showToolTip(btnObj, btnTT:String):Void{
		var appData = getAppData();
		var Xpos = appData.compX+ this._x - 10;
		var Ypos = appData.compY+( (this._y+btnObj._height)-4);
		var ttHolder = appData.ttHolder;
		
		if (btnTT == undefined || btnTT == null || btnTT == "" || btnTT == "undefined"){
			var ttMessage = "<b>"+ _activity.title+"</b>"
		}else {
			var ttMessage = "<b>"+ _activity.title+"</b> \n"+Dictionary.getValue(btnTT);
		}
		
		var ttWidth = 140;
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos, undefined, ttWidth);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	private function localOnRollOver(){
		if(isCompleted){
			showToolTip(this.clickTarget_mc, "completed_act_tooltip");
		} else if(isCurrent){
			showToolTip(this.clickTarget_mc, "current_act_tooltip");
		} else if(isAttempted){
			showToolTip(this.clickTarget_mc, "current_act_tooltip");
		} else {
			if(String(_activity.title).length > 19){
				showToolTip(this.clickTarget_mc, "undefined");
			}
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
			draw();
			controller.activityDoubleClick(this);
		
		} else {
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
	
	public function get model(){
		if(isLearnerModule())
			return LessonModel(_controller.getModel());
		else
			return MonitorModel(_controller.getModel());
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
	
	public function setSelected(isSelected) {
	}
	
	public function get isCurrent():Boolean {
		return (actStatus == 'current_mc');
	}
	
	public function get isCompleted():Boolean {
		return (actStatus == 'completed_mc');
	}
	
	public function get isAttempted():Boolean {
		return (actStatus == 'attempted_mc');
	}
	
	public function get learnerID():Number{
		return learner.getLearnerId();
	}
	
	public function getActiveSequence():SequenceActivity {
		return activeSequence;
	}
}
