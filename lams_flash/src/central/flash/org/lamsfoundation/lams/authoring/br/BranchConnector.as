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

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.br.*;

class org.lamsfoundation.lams.authoring.br.BranchConnector extends CanvasConnection {
	
	public static var DIR_FROM_START:Number = 0;
	public static var DIR_TO_END:Number = 1;
	
	private var _branch:Branch;

	function BranchConnector(){
		super();
		
		Debugger.log("_branch.targetUIID:"+_branch.targetUIID,4,'Constructor','BranchConnector');
		Debugger.log("_branch.direction:"+_branch.direction,4,'Constructor','BranchConnector');
		
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		_drawnLineStyle = 0xEA00FF;
		draw();
	}
	
	/**
	 * Renders the branch to stage
	 * @usage   
	 * @return  
	 */
	private function draw():Void{

		var cv:Canvas = Application.getInstance().getCanvas();
		
		var fromAct_mc = (branch.direction == DIR_FROM_START) ? cv.model.activeView.startHub : cv.model.getActivityMCByUIID(_branch.targetUIID);	
		var toAct_mc = (branch.direction == DIR_TO_END) ? cv.model.activeView.endHub : cv.model.getActivityMCByUIID(_branch.targetUIID);
		
		var fromOTC:Object = getFromOTC(fromAct_mc);
		var toOTC:Object = getToOTC(toAct_mc);
		
		Debugger.log('fromAct_mc.getActivity().xCoord:' + fromAct_mc.getActivity().xCoord , 4, 'draw', 'BranchConnector');	
		Debugger.log('offsetToCentre_x: ' + fromOTC.x, 4, 'draw', 'BranchConnector');	
		
		_startPoint = (branch.direction == DIR_FROM_START) ? new Point(fromAct_mc._x + fromOTC.x,fromAct_mc._y + fromOTC.y)
													: new Point(fromAct_mc.getActivity().xCoord + fromOTC.x,fromAct_mc.getActivity().yCoord + fromOTC.y);
		_endPoint = (branch.direction == DIR_TO_END) ? new Point(toAct_mc._x + toOTC.x, toAct_mc._y + toOTC.y)
											  : new Point(toAct_mc.getActivity().xCoord + toOTC.x, toAct_mc.getActivity().yCoord + toOTC.y);
		
		createConnection(fromAct_mc, toAct_mc, _startPoint, _endPoint, fromOTC, toOTC);
			
	}
	
	public function get branch():Branch{
		return _branch;
	}
	
	public function set branch(b:Branch){
		_branch = b;
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