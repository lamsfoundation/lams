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

class org.lamsfoundation.lams.authoring.cv.CanvasTransition extends CanvasConnection {  
	
	private var _transition:Transition;
	
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
	
	function CanvasTransition(){
		super();
		
		Debugger.log("_transition.fromUIID:"+_transition.fromUIID,4,'Constructor','CanvasTransition');
		Debugger.log("_transition.toUIID:"+_transition.toUIID,4,'Constructor','CanvasTransition');
		
		MovieClipUtils.doLater(Proxy.create(this,init));
	}
	
	public function init():Void{
		_drawnLineStyle = 0x777E9D;
		draw();
	}

	public function get transition():Transition{
		return _transition;
	}
	
	public function set transition(t:Transition){
		_transition = t;
	}
	
	/**
	 * Renders the transition to stage
	 * @usage   
	 * @return  
	 */
	private function draw():Void{

		var cv:Canvas = Application.getInstance().getCanvas();
		
		var fromAct_mc;
		var toAct_mc;
		
		if(_transition.mod_fromUIID != null) fromAct_mc = cv.model.getActivityMCByUIID(_transition.mod_fromUIID);
		else fromAct_mc = cv.model.getActivityMCByUIID(_transition.fromUIID);
			
		if(_transition.mod_toUIID != null) toAct_mc = cv.model.getActivityMCByUIID(_transition.mod_toUIID);
		else toAct_mc = cv.model.getActivityMCByUIID(_transition.toUIID);
		
		var fromOTC:Object = getFromOTC(fromAct_mc);
		var toOTC:Object = getToOTC(toAct_mc);
		
		Debugger.log('fromAct_mc.getActivity().xCoord:' + fromAct_mc.getActivity().xCoord , 4, 'draw', 'CanvasTransition');	
		Debugger.log('offsetToCentre_x: ' + fromOTC.x, 4, 'draw', 'CanvasTransition');	
		
		_startPoint = new Point(fromAct_mc.getActivity().xCoord + fromOTC.x, fromAct_mc.getActivity().yCoord + fromOTC.y);
		_endPoint = new Point(toAct_mc.getActivity().xCoord + toOTC.x, toAct_mc.getActivity().yCoord + toOTC.y);
		
		createConnection(fromAct_mc, toAct_mc, _startPoint, _endPoint, fromOTC, toOTC);
			
	}

	private function onPress():Void{
			// check double-click
			var now:Number = new Date().getTime();
			Debugger.log('now - _dcStartTime:'+(now - _dcStartTime)+' Config.DOUBLE_CLICK_DELAY:'+Config.DOUBLE_CLICK_DELAY,Debugger.GEN,'onPress','CanvasTransition');
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				_doubleClicking = true;
				_canvasController.transitionDoubleClick(this);
				
			
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasTransition');
				_doubleClicking = false;
				_canvasController.transitionClick(this);
			}
			
			_dcStartTime = now;
	
	}

	private function onRelease():Void{
		if(!_doubleClicking){
			_canvasController.transitionRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		_canvasController.transitionReleaseOutside(this);
	}
	
}