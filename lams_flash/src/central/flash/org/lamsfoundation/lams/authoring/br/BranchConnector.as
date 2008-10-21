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

import org.lamsfoundation.lams.common.style.ThemeManager;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.br.*;
import org.lamsfoundation.lams.monitoring.mv.*;

import mx.controls.Label;
import mx.managers.DepthManager;

class org.lamsfoundation.lams.authoring.br.BranchConnector extends CanvasConnection {
	
	public static var DIR_FROM_START:Number = 0;
	public static var DIR_TO_END:Number = 1;
	public static var DIR_SINGLE:Number = 2;
	
	private var _branch:Branch;

	private var bcLabel_mc:MovieClip;
	private var bcLabel:Label;

	private var _tm:ThemeManager;
	
	function BranchConnector(){
		super();
		
		Debugger.log("_branch.targetUIID:"+_branch.targetUIID,4,'Constructor','BranchConnector');
		Debugger.log("_branch.direction:"+_branch.direction,4,'Constructor','BranchConnector');
		
        //set the reference to the StyleManager
        _tm = ThemeManager.getInstance();
		
		MovieClipUtils.doLater(Proxy.create(this, init));
	}
	
	public function init():Void{
		_drawnLineStyle = 0xAFCE63;
		arrow_mc.setStyle("backgroundColor", "0xAFCE63");
		
		if(branch.direction == DIR_SINGLE)
			drawActivityless();
		else
			draw();
		
		Debugger.log("is open: " + model.activeView.isOpen, Debugger.CRITICAL, "init", "BranchConnector");
		
		if(branch.direction != DIR_TO_END && model.activeView.isOpen)
			createBranchLabel();
	}
	
	/**
	 * Renders the branch to stage
	 * @usage   
	 * @return  
	 */
	private function draw():Void{
		
		var fromAct_mc = (branch.direction == DIR_FROM_START) ? model.activeView.startHub : model.getActivityMCByUIID(_branch.targetUIID);	
		var toAct_mc = (branch.direction == DIR_TO_END) ? model.activeView.endHub : model.getActivityMCByUIID(_branch.targetUIID);
		
		var fromOTC:Object = getFromOTC(fromAct_mc);
		var toOTC:Object = getToOTC(toAct_mc);
		
		Debugger.log("fromAct: " + fromAct_mc);
		
		_startPoint = (branch.direction == DIR_FROM_START) ? new Point(fromAct_mc._x + fromOTC.x,fromAct_mc._y + fromOTC.y)
													: new Point(fromAct_mc.getActivity().xCoord + fromOTC.x,fromAct_mc.getActivity().yCoord + fromOTC.y);
		_endPoint = (branch.direction == DIR_TO_END) ? new Point(toAct_mc._x + toOTC.x, toAct_mc._y + toOTC.y)
											  : new Point(toAct_mc.getActivity().xCoord + toOTC.x, toAct_mc.getActivity().yCoord + toOTC.y);
		
		createConnection(fromAct_mc, toAct_mc, _startPoint, _endPoint, fromOTC, toOTC);
	}
	
	private function drawActivityless():Void {
		var fromAct_mc = model.activeView.startHub;	
		var toAct_mc = model.activeView.endHub;
		
		var fromOTC:Object = getFromOTC(fromAct_mc);
		var toOTC:Object = getToOTC(toAct_mc);
		
		_startPoint = new Point(fromAct_mc._x + fromOTC.x,fromAct_mc._y + fromOTC.y);
		_endPoint = new Point(toAct_mc._x + toOTC.x, toAct_mc._y + toOTC.y)
		
		Debugger.log("sp x: " + _startPoint.x + " y: " + _startPoint.y, Debugger.CRITICAL, "drawActivityless", "BranchConnector");
		Debugger.log("ep x: " + _endPoint.x + " y: " + _endPoint.y, Debugger.CRITICAL, "drawActivityless", "BranchConnector");
		
		createConnection(fromAct_mc, toAct_mc, _startPoint, _endPoint, fromOTC, toOTC);
	}
	
	public function createBranchLabel():Void {
		Debugger.log("arrow mc: " + arrow_mc._x + " " + arrow_mc._y, Debugger.CRITICAL, "createBranchLabel", "BranchConnector");
		Debugger.log("quad: " + _quadrant, Debugger.CRITICAL, "createBranchLabel", "BranchConnector");
		
		if(bcLabel_mc != null)
			bcLabel_mc.removeMovieClip();
		
		var _offset:Number = 10;
		
		var _xpos = (_quadrant == CanvasConnection.Q1 || _quadrant == CanvasConnection.Q4) ? arrow_mc._x + _offset : arrow_mc._x - _offset;
		var _ypos = (_quadrant == CanvasConnection.Q1 || _quadrant == CanvasConnection.Q2) ? arrow_mc._y + _offset : arrow_mc._y - _offset - 22;
		
		bcLabel_mc = this.attachMovie("Label", "bcLabel" + _branch.branchUIID, model.activeView.labelContainer.getNextHighestDepth(), {_x: _xpos, _y: _ypos, text:_branch.sequenceName, _width: 40, _height: 22, autoSize: "center", _visible: false});
		bcLabel = Label(bcLabel_mc);
		bcLabel.setStyle('styleName', _tm.getStyleObject("label"));
		
		bcLabel._x -= bcLabel._width/2;
		bcLabel._visible = true;
	}
	
	public function updateBranchLabel():Void {
		if(branchLabel != null)
			branchLabel = _branch.sequenceActivity.title;
		
	}
	
	public function set branchLabel(a:String):Void {
		bcLabel.text = a;
	}
	
	public function get branchLabel():Label {
		return bcLabel;
	}
	
	public function get branch():Branch{
		return _branch;
	}
	
	public function set branch(b:Branch){
		_branch = b;
	}
	
	public function get activity():Activity {
		return Activity(_branch.sequenceActivity);
	}
	
	private function onPress():Void{
			// check double-click
			var now:Number = new Date().getTime();
			Debugger.log('now - _dcStartTime:'+(now - _dcStartTime)+' Config.DOUBLE_CLICK_DELAY:'+Config.DOUBLE_CLICK_DELAY,Debugger.GEN,'onPress','CanvasTransition');
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				_doubleClicking = true;
				_canvasController.branchDoubleClick(this);
			
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasTransition');
				_doubleClicking = false;
				_canvasController.branchClick(this);
			}
			
			_dcStartTime = now;
	
	}

	private function onRelease():Void{
		if(!_doubleClicking){
			_canvasController.branchRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		_canvasController.branchReleaseOutside(this);
	}
}  