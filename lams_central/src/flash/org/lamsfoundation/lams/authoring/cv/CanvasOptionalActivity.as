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
import org.lamsfoundation.lams.common. *;
import org.lamsfoundation.lams.common.util. *;
import org.lamsfoundation.lams.common.ui. *;
import org.lamsfoundation.lams.authoring. *;
import org.lamsfoundation.lams.authoring.cv. *;
import org.lamsfoundation.lams.monitoring.mv. *;
import org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView;
//import org.lamsfoundation.lams.authoring.cv.DesignDataModel;
import org.lamsfoundation.lams.common.style. *;
import mx.controls. *;
import mx.managers. *
/**
* CanvasOptionalActivity
* This is the UI / view representation of a complex (Optional) activity
*/
class org.lamsfoundation.lams.authoring.cv.CanvasOptionalActivity extends MovieClip implements ICanvasActivity
{
	//class org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity extends MovieClip {
	private var CHILD_OFFSET_X : Number = 8;
	private var CHILD_OFFSET_Y : Number = 57;
	private var CHILD_INCRE : Number = 60;
	
	//this is set by the init object
	private var _canvasController : CanvasController;
	private var _canvasView : CanvasView;
	private var _monitorController : MonitorController;
	private var _monitorView : MonitorView;
	//Set by the init obj
	private var _activity : Activity;
	private var _children : Array;
	private var panelHeight : Number;
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
	private var _learnerTabView:LearnerTabView;
	private var actStatus:String;
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
	
	function CanvasOptionalActivity ()
	{
		optionalActivity_mc = this
		_ddm = new DesignDataModel ();
		_visible = false;
		_tm = ThemeManager.getInstance ();
		_visibleHeight = container_pnl._height;
		_visibleWidth = container_pnl._width;
		//init();
		MovieClipUtils.doLater (Proxy.create (this, init));
	}
	
	public function init () : Void
	{
		clickTarget_mc.onPress = Proxy.create (this, localOnPress);
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		
		_ddm.getComplexActivityChildren(_activity.activityUIID);
		_locked = false;
		showStatus(false);
		childActivities_mc = this;
		var children_mc : Array = new Array ();
		
		for (var i = 0; i < _children.length; i ++)
		{
			if (fromModuleTab != "monitorLearnerTab"){
				children_mc [i] = childActivities_mc.attachMovie ("CanvasActivity", "CanvasActivity" + i, childActivities_mc.getNextHighestDepth (), {_activity : _children [i] , _canvasController : _canvasController, _canvasView : _canvasView});
				//set the positioning co-ords
			children_mc [i].activity.xCoord = CHILD_OFFSET_X;
			children_mc [i].activity.yCoord = CHILD_OFFSET_Y + (i * CHILD_INCRE);
			
			}else {
				trace("child's activityID is "+_children [i].activityID)
				children_mc [i] = childActivities_mc.attachMovie ("CanvasActivityLinear_forOptional", "CanvasActivity" + i, childActivities_mc.getNextHighestDepth (), {_activity : _children [i] , _monitorController : _monitorController, _monitorView : _monitorView, actLabel:_children [i].title, learner:learner});
				//set the positioning co-ords
				children_mc [i]._y = (i*21)+8;
				children_mc [i]._x = 57;
			
			
			}
			children_mc [i]._visible = true;
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
		
		actStatus = _learnerTabView.compareProgressData(learner, _activity.activityID);
		switch (actStatus){
		    case 'completed_mc' :
				//trace("TabID for Selected tab is: "+infoObj.tabID)
				completed_mc._visible = true;
		
                break;
            case 'current_mc' :
				current_mc._visible = true;
                break;
            //case 'toto_mc' :
			    //todo_mc._visible = true;
                //break;
			default :
				todo_mc._visible = true;
                //Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorView');
		}
		clickTarget_mc.swapDepths(childActivities_mc.getNextHighestDepth());
		var numOfChildren = _children.length
		panelHeight = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
		setStyles ()
		//write text
		title_lbl.text = 'Optional Activities'
		//_activity.title = 'Optional Activities';
		actCount_lbl.text = _children.length + " activities";
		header_pnl.borderType = 'outset';
		act_pnl.borderType = 'inset';
		container_pnl.setStyle ("backgroundColor", 0x4289FF);
		//position the container (this)
		if (fromModuleTab != "monitorLearnerTab"){
			if (numOfChildren > 1)
			{
				container_pnl._height = CHILD_OFFSET_Y + (numOfChildren * CHILD_INCRE);
			}
			_x = _activity.xCoord;
			_y = _activity.yCoord;
		}else {
			containerPanelHeader.title_lbl.text = 'Optional Activities'
			container_pnl._height = 16+(numOfChildren * 21);
		}
		//dimentions of container (this)
		if (_locked)
		{
			padlockClosed_mc._visible = true;
			clickTarget_mc._height = container_pnl._height;
		}else
		{
			padlockOpen_mc._visible = true;
			padlockClosed_mc._visible = false;
			clickTarget_mc._height = 45;
		}
		Debugger.log ("I am in Draw :" + _activity.title + 'uiID:' + _activity.activityUIID + ' children:' + _children.length, Debugger.GEN, 'Draw', 'CanvasOptionalActivity');
		_visible = true;
		//child1_mc._visible = true;
	}
	private function localOnPress () : Void
	{
		// check double-click
		var now : Number = new Date ().getTime ();
		if ((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY)
		{
			Debugger.log ('DoubleClicking:' + this, Debugger.GEN, 'localOnPress', 'CanvasOptionalActivity');
			_doubleClicking = true;
			//if we double click on the glass mask - then open the container to allow the usr to see the activities inside.
			if (_locked)
			{
				_locked = false;
			}else
			{
				_locked = true;
			}
			draw ();
		}else
		{
			Debugger.log ('SingleClicking:+' + this, Debugger.GEN, 'localOnPress', 'CanvasOptionalActivity');
			_doubleClicking = false;
			_canvasController.activityClick (this);
		}
		_dcStartTime = now;
	}
	private function localOnRelease () : Void
	{
		Debugger.log ('_doubleClicking:' + _doubleClicking + ', localOnRelease:' + this, Debugger.GEN, 'localOnRelease', 'CanvasOptionalActivity');
		if ( ! _doubleClicking)
		{
			_canvasController.activityRelease (this);
		}
	}
	private function localOnReleaseOutside () : Void
	{
		Debugger.log ('localOnReleaseOutside:' + this, Debugger.GEN, 'localOnReleaseOutside', 'CanvasOptionalActivity');
		_canvasController.activityReleaseOutside (this);
	}
	/**
	*
	* @usage
	* @return
	*/
	public function getVisibleWidth () : Number 
	{
		return _visibleWidth;
	}
	/**
	*
	* @usage
	* @return
	*/
	public function getVisibleHeight () : Number 
	{
		return _visibleHeight;
	}
	public function get locked () : Boolean 
	{
		return _locked;
	}
	public function get getpanelHeight () : Number 
	{
		return panelHeight;
	}
	private function setStyles () : Void
	{
		var styleObj = _tm.getStyleObject ('label');
		title_lbl.setStyle (styleObj);
		styleObj = _tm.getStyleObject ('smlLabel');
		actCount_lbl.setStyle ('styleName', styleObj);
	}
}
