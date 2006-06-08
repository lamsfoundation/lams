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
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.ICanvasActivity;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.monitoring.mv.*;
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
	
	//this is set by the init object
	private var _controller:AbstractController;
	private var _view:AbstractView;
	
	//Set by the init obj
	private var _activity : Activity;
	private var _children : Array;
	private var _panelHeight : Number;
	
	//refs to screen items:
	private var container_pnl : Panel;
	private var title_lbl : Label;
	
	//locals
	private var childActivities_mc : MovieClip;
	private var complexActivity_mc : MovieClip;
	
	private var clickTarget_mc : MovieClip;
	private var _dcStartTime : Number = 0;
	private var _doubleClicking : Boolean;
	private var _locked:Boolean;
	
	// Only for Learner Optional Container children
	private var learner:Object = new Object();
	private var containerPanelHeader:MovieClip;
	private var completed_mc:MovieClip;
	private var current_mc:MovieClip;
	private var todo_mc:MovieClip;
	private var childHolder_mc:MovieClip;
	//---------------------------//
	
	private var child_mc : MovieClip;
	private var _visibleHeight : Number;
	private var _visibleWidth : Number;
	
	private var _tm : ThemeManager;
	private var _ddm : DesignDataModel;
	
	function LearnerComplexActivity ()
	{
		complexActivity_mc = this
		_ddm = new DesignDataModel ();
		_visible = false;
		_tm = ThemeManager.getInstance ();
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		
		MovieClipUtils.doLater (Proxy.create (this, init));
	}
	
	public function init () : Void
	{
		clickTarget_mc.onPress = Proxy.create (this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		
		_locked = false;
		showStatus(false);
		childActivities_mc = this;
		var children_mc : Array = new Array ();
		
		for (var i = 0; i < _children.length; i ++)
		{
			var progStatus:String = Progress.compareProgressData(learner, _children [i].activityID);
			children_mc [i] = childHolder_mc.attachMovie("LearnerActivity_forComplex", "LearnerActivityVertical_forOptional"+i, childHolder_mc.getNextHighestDepth(), {_activity:_children[i], _controller:_controller, _view:_view, learner:learner, actStatus:progStatus});
			
			//set the positioning co-ords
			children_mc [i]._y = (i*21);
			
			children_mc [i]._visible = true;
			childHolder_mc._visible = false;
		}
		
		MovieClipUtils.doLater (Proxy.create (this, draw));
	}
	
	private function showStatus(isVisible:Boolean){
		completed_mc._visible = isVisible;
		current_mc._visible = isVisible;
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
	private function draw (){
		
		var actStatus:String = Progress.compareProgressData(learner, _activity.activityID);
		switch (actStatus){
		    case 'completed_mc' :
				completed_mc._visible = true;
                break;
            case 'current_mc' :
				current_mc._visible = true;
                break;
            case 'attempted_mc' :
			    current_mc._visible = true;
                break;
			default :
				todo_mc._visible = true;
                //Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}
		
		var numOfChildren = _children.length;
		panelHeight = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
		setStyles();
		
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
	
	
	private function localOnPress ():Void{
		this.swapDepths(this._parent.getNextHighestDepth());
		
		// check double-click
		var now : Number = new Date ().getTime ();
		if ((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY)	{
			Debugger.log ('DoubleClicking:' + this, Debugger.GEN, 'localOnPress', 'LearnerOptionalActivity');
			_doubleClicking = true;
			//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
			draw ();
		}else {
			Debugger.log ('SingleClicking:+' + this, Debugger.GEN, 'localOnPress', 'LearnerOptionalActivity');
			_doubleClicking = false;
		}
		_dcStartTime = now;
	}
	
	
	private function localOnRelease ():Void{
		Debugger.log ('_doubleClicking:' + _doubleClicking + ', localOnRelease:' + this, Debugger.GEN, 'localOnRelease', 'LearnerOptionalActivity');
			if (_locked){
				_locked = false;
				gotoAndStop('collapse')
				childHolder_mc._visible = false;
				draw ();
				
			}else {
				_locked = true;
				childHolder_mc._visible = true;
				gotoAndStop('expand')
				draw ();			
			}
	}
	
	
	private function localOnReleaseOutside():Void {
		Debugger.log ('localOnReleaseOutside:' + this, Debugger.GEN, 'localOnReleaseOutside', 'LearnerOptionalActivity');
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
	public function get panelHeight():Number {
		return _panelHeight;
	}
	private function setStyles():Void {
		var styleObj = _tm.getStyleObject ('label');
		title_lbl.setStyle (styleObj);
	}
}
