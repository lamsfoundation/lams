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


/**  
* -+ - 
*/  
class org.lamsfoundation.lams.authoring.cv.CanvasTransition extends MovieClip{  
	//set by passing initObj to mc.createClass()
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _transition:Transition;
	
	private var _drawnLineStyle:Number;
	
	private var arrow_mc:MovieClip;
	private var stopArrow_mc:MovieClip;
	private var stopSign_mc:MovieClip;
	
	
	private var _startPoint:Point;
	private var _midPoint:Point;
	private var _endPoint:Point;
	private var _fromAct_edgePoint:Point;
	private var _toAct_edgePoint:Point;
	private var xPos:Number;
	
	
	private var _dcStartTime:Number = 0;
	private var _doubleClicking:Boolean;
		
	
	function CanvasTransition(){
		
		Debugger.log("_transition.fromUIID:"+_transition.fromUIID,4,'Constructor','CanvasTransition');
		Debugger.log("_transition.toUIID:"+_transition.toUIID,4,'Constructor','CanvasTransition');
		arrow_mc._visible = false;
		stopArrow_mc._visible = false;
		stopSign_mc._visible = false;
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		//init();
	}
	
	public function init():Void{
		//Debugger.log('Running,',4,'init','CanvasTransition');
		//todo: all a get style for this
		_drawnLineStyle = 0x777E9D;
		draw();
	}

	public function get transition():Transition{
		return _transition;
	}
	
	public function set transition(t:Transition){
		_transition = t;
	}
	
	public function get midPoint():Point{
		return _midPoint;
	}
	
	public function get toAct_edgePoint():Point{
		return _toAct_edgePoint;
	}
	
	public function get fromAct_edgePoint():Point{
		return _fromAct_edgePoint;
	}
	
	/**
	 * Renders the transition to stage
	 * @usage   
	 * @return  
	 */
	private function draw():Void{		//Debugger.log('',4,'draw','CanvasTransition');

		var cv:Canvas = Application.getInstance().getCanvas();
		
		//var fromAct = cv.ddm.getActivityByUIID(_transition.fromUIID);
		//var toAct = cv.ddm.getActivityByUIID(_transition.toUIID);
		
		var fromAct_mc;
		var toAct_mc;
		
		if(_transition.mod_fromUIID != null) fromAct_mc = cv.model.getActivityMCByUIID(_transition.mod_fromUIID);
		else fromAct_mc = cv.model.getActivityMCByUIID(_transition.fromUIID);
		
		if(_transition.mod_toUIID != null) toAct_mc = cv.model.getActivityMCByUIID(_transition.mod_toUIID);
		else toAct_mc = cv.model.getActivityMCByUIID(_transition.toUIID);
		
		//var startPoint:Point = MovieClipUtils.getCenterOfMC(fromAct_mc);
		//var endPoint:Point = MovieClipUtils.getCenterOfMC(toAct_mc);
		
		//TODO: check if its a gate transition and if so render a shorty
		var isGateTransition = toAct_mc.activity.isGateActivity();
		
		
		var offsetToCentre_x = fromAct_mc.getVisibleWidth() / 2;
		var offsetToCentre_y = fromAct_mc.getVisibleHeight() / 2;
		Debugger.log('fromAct_mc.getActivity().xCoord:'+fromAct_mc.getActivity().xCoord,4,'draw','CanvasTransition');	
		Debugger.log('offsetToCentre_x:'+offsetToCentre_x,4,'draw','CanvasTransition');	

		
		_startPoint = new Point(fromAct_mc.getActivity().xCoord+offsetToCentre_x,fromAct_mc.getActivity().yCoord+offsetToCentre_y);
		
		var toOTC_x:Number = toAct_mc.getVisibleWidth() /2;
		var toOTC_y:Number = toAct_mc.getVisibleHeight() /2;
		
		_endPoint = new Point(toAct_mc.getActivity().xCoord+toOTC_x,toAct_mc.getActivity().yCoord+toOTC_y);
		
		Debugger.log('fromAct_mc:'+fromAct_mc,4,'draw','CanvasTransition');
		Debugger.log('toAct_mc:'+toAct_mc,4,'draw','CanvasTransition');
		
		this.lineStyle(2, _drawnLineStyle);
		this.moveTo(_startPoint.x, _startPoint.y);
		
		//this.dashTo(startX, startY, endX, endY, 8, 4);
		this.lineTo(_endPoint.x, _endPoint.y);
		Debugger.log('drawn line from:'+_startPoint.x+','+_startPoint.y+'to:'+_endPoint.x+','+_endPoint.y,4,'draw','CanvasTransition');
		
		// calc activity root angles
		var fromAct_Angle:Number = Math.atan2(offsetToCentre_y, offsetToCentre_x);
		var toAct_Angle:Number = Math.atan2(toOTC_y,toOTC_x);
		
		var fromAct_Deg:Number = convertToDegrees(fromAct_Angle);
		var toAct_Deg:Number = convertToDegrees(toAct_Angle);
		
		Debugger.log("fromAct root angle: " + fromAct_Deg, Debugger.CRITICAL, "draw", "CanvasTransition");
		Debugger.log("toAct root angle: " + toAct_Deg, Debugger.CRITICAL, "draw", "CanvasTransition");
		
		// gradient
		var angle:Number = Math.atan2((_endPoint.y- _startPoint.y),(_endPoint.x- _startPoint.x));
		var degs:Number = convertToDegrees(angle);
		
		Debugger.log("angle: " + angle, Debugger.CRITICAL, "draw", "CanvasTransition");
		Debugger.log("degs: " + degs, Debugger.CRITICAL, "draw", "CanvasTransition");
		
		// get edgepoint for connected activities 
		_fromAct_edgePoint = calcEdgePoint(degs, offsetToCentre_x, offsetToCentre_y, fromAct_Deg,  _startPoint);
		_toAct_edgePoint = (degs >= 0) ? calcEdgePoint(degs - 180, toOTC_x, toOTC_y, toAct_Deg, _endPoint)
										: calcEdgePoint(degs + 180, toOTC_x, toOTC_y, toAct_Deg, _endPoint);
		
		// calc midpoint
		if(_fromAct_edgePoint != null & _toAct_edgePoint != null) {
			arrow_mc._x = (_fromAct_edgePoint.x + _toAct_edgePoint.x)/2;
			arrow_mc._y = (_fromAct_edgePoint.y + _toAct_edgePoint.y)/2;
		} else {
			arrow_mc._x = (_startPoint.x + _endPoint.x)/2;
			arrow_mc._y = (_startPoint.y + _endPoint.y)/2;
		}
		
		Debugger.log("startPoint: (" + _startPoint.x + ", " + _startPoint.y + ")", Debugger.CRITICAL, "draw", "CanvasTransition");
		Debugger.log("fromAct edge point: (" + _fromAct_edgePoint.x + ", " + _fromAct_edgePoint.y + ")", Debugger.CRITICAL, "draw", "CanvasTransition");
		Debugger.log("toAct edge point: (" + _toAct_edgePoint.x + ", " + _toAct_edgePoint.y + ")", Debugger.CRITICAL, "draw", "CanvasTransition");
		Debugger.log("endPoint: (" + _endPoint.x + ", " + _endPoint.y + ")", Debugger.CRITICAL, "draw", "CanvasTransition");
		
		Debugger.log("mid point: (" + _midPoint.x + ", " + _midPoint.y + ")", Debugger.CRITICAL, "draw", "CanvasTransition");
		
		arrow_mc._rotation = degs;
		arrow_mc._visible = true;
		
		_midPoint = new Point(arrow_mc._x,arrow_mc._y);
		
		xPos = this._x
		trace("x position of start point: "+xPos)
		
		/*
		stopArrow_mc._rotation = degs;
			
		// calculate the position for the stop sign
		stopSign_mc._x = arrow_mc._x;
		stopSign_mc._y = arrow_mc._y;
		stopArrow_mc._x = arrow_mc._x;
		stopArrow_mc._y = arrow_mc._y;
		*/
			
	}
	/*
	private function updateSynchStatus():Void{
		
		if(completionType == "synchronize_teacher"){
			stopSign._visible = true;
			stopSignArrow._visible = true;
		}else{
			stopSign._visible = false;
			stopSignArrow._visible = false;
		}
		
	}
*/

	public function get xPosition():Number{
		return xPos;
	}
	
	private static function convertToDegrees(angle:Number):Number {
		return Math.round(angle*180/Math.PI);
	}
	
	private static function convertToRadians(degrees:Number):Number {
		return degrees/180*Math.PI;
	}
	
	private function getQuadrant(d:Number) {
		if(d >= 0 && d < 90) {
			return "q3";
		} else if(d < 0 && d >= - 90) {
			return "q2";
		} else if(d < -90 && d >= -180) {
			return  "q1";
		} else {
			return "q4";
		}
	}
	
	private function calcEdgePoint(_d:Number, x_offset:Number, y_offset:Number, _act_d:Number, point:Point):Point {
		var _edgePoint:Point = new Point();
		var d:Number = _d;
		var quad:String = getQuadrant(d);
	
		switch(quad) {
			case "q1":
				d = 180 + d;
				
				_edgePoint.y = (d >= _act_d) ? point.y - y_offset : point.y - (x_offset * calcTangent(d, false));
				_edgePoint.x = (d >= _act_d) ? point.x - (y_offset * calcTangent(d, true)) : point.x - x_offset;;
				
				break;
			case "q2":
				d = Math.abs(d);
				
				_edgePoint.y = (d >= _act_d) ? point.y - y_offset : point.y - (x_offset * calcTangent(d, false));
				_edgePoint.x = (d >= _act_d) ? point.x + (y_offset * calcTangent(d, true)) : point.x + x_offset;;
				
				break;
			case "q3":
			
				_edgePoint.y = (d >= _act_d) ? point.y + y_offset : point.y + (x_offset * calcTangent(d, false));
				_edgePoint.x = (d >= _act_d) ? point.x + (y_offset * calcTangent(d, true)) : point.x + x_offset;
			
					
				break;
			case "q4":
				d = 180 - d;
			
				_edgePoint.y = (d >= _act_d) ? point.y + y_offset : point.y + (x_offset * calcTangent(d, false));
				_edgePoint.x = (d >= _act_d) ? point.x - (y_offset * calcTangent(d, true)) : point.x - x_offset;;
				
				break;
			default:
				// ERR: No Quadrant found
				break;
		}
		
		return _edgePoint;
	}
	
	private function calcTangent(_d:Number, _use_adj_angle:Boolean) {
		var d:Number = (_use_adj_angle) ? 90 - _d: _d;
		return Math.tan(convertToRadians(d));
	}
	
	private function onPress():Void{
			// check double-click
			var now:Number = new Date().getTime();
			//Debugger.log('now:'+now,Debugger.GEN,'onPress','CanvasTransition');
			//Debugger.log('_dcStartTime:'+_dcStartTime,Debugger.GEN,'onPress','CanvasTransition');
			Debugger.log('now - _dcStartTime:'+(now - _dcStartTime)+' Config.DOUBLE_CLICK_DELAY:'+Config.DOUBLE_CLICK_DELAY,Debugger.GEN,'onPress','CanvasTransition');
			if((now - _dcStartTime) <= Config.DOUBLE_CLICK_DELAY){
				//Debugger.log('DoubleClicking:'+this,Debugger.GEN,'onPress','CanvasTransition');
				_doubleClicking = true;
				_canvasController.transitionDoubleClick(this);
				
			
			}else{
				Debugger.log('SingleClicking:+'+this,Debugger.GEN,'onPress','CanvasTransition');
				_doubleClicking = false;
				
				//Debugger.log('_canvasController:'+_canvasController,Debugger.GEN,'onPress','CanvasTransition');
				_canvasController.transitionClick(this);
				
	
			}
			
			_dcStartTime = now;
	
	}

	private function onRelease():Void{
		if(!_doubleClicking){
			//Debugger.log('Releasing:'+this,Debugger.GEN,'onRelease','CanvasTransition');
			_canvasController.transitionRelease(this);
		}
		
	}
	
	private function onReleaseOutside():Void{
		//Debugger.log('ReleasingOutside:'+this,Debugger.GEN,'onReleaseOutside','CanvasTransition');
		_canvasController.transitionReleaseOutside(this);
	}
	

	

}