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

class org.lamsfoundation.lams.authoring.cv.CanvasConnection extends MovieClip{  
	
	private var _canvasController:CanvasController;
	private var _canvasView:CanvasView;
	private var _canvasBranchView:CanvasBranchView;
	
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
	
	function CanvasConnection(){
		arrow_mc._visible = false;
		stopArrow_mc._visible = false;
		stopSign_mc._visible = false;
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
	
	public function get xPosition():Number{
		return xPos;
	}
	
	public function createConnection(fromAct_mc, toAct_mc, _startPoint:Point, _endPoint:Point, fromOTC:Object, toOTC:Object) {
		Debugger.log('fromAct_mc:'+fromAct_mc,4,'draw','CanvasConnection');
		Debugger.log('toAct_mc:'+toAct_mc,4,'draw','CanvasConnection');
		
		this.lineStyle(2, _drawnLineStyle);
		this.moveTo(_startPoint.x, _startPoint.y);
		this.lineTo(_endPoint.x, _endPoint.y);
		
		// calc activity root angles
		var fromAct_Angle:Number = Math.atan2(fromOTC.y, fromOTC.x);
		var toAct_Angle:Number = Math.atan2(toOTC.y,toOTC.x);
		
		var fromAct_Deg:Number = convertToDegrees(fromAct_Angle);
		var toAct_Deg:Number = convertToDegrees(toAct_Angle);
		
		// gradient
		var angle:Number = Math.atan2((_endPoint.y - _startPoint.y),(_endPoint.x - _startPoint.x));
		var degs:Number = convertToDegrees(angle);
		
		// get edgepoint for connected activities 
		_fromAct_edgePoint = calcEdgePoint(degs, fromOTC.x, fromOTC.y, fromAct_Deg,  _startPoint);
		_toAct_edgePoint = (degs >= 0) ? calcEdgePoint(degs - 180, toOTC.x, toOTC.y, toAct_Deg, _endPoint)
										: calcEdgePoint(degs + 180, toOTC.x, toOTC.y, toAct_Deg, _endPoint);
		// calc midpoint
		if(_fromAct_edgePoint != null & _toAct_edgePoint != null) {
			arrow_mc._x = (_fromAct_edgePoint.x + _toAct_edgePoint.x)/2;
			arrow_mc._y = (_fromAct_edgePoint.y + _toAct_edgePoint.y)/2;
		} else {
			arrow_mc._x = (_startPoint.x + _endPoint.x)/2;
			arrow_mc._y = (_startPoint.y + _endPoint.y)/2;
		}
		
		arrow_mc._rotation = degs;
		arrow_mc._visible = true;
		
		_midPoint = new Point(arrow_mc._x,arrow_mc._y);
		
		xPos = this._x;
				
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
	
	public function getToOTC(toAct_mc):Object {
		var _toOTC = new Object();
		_toOTC.x = toAct_mc.getVisibleWidth()/2;
		_toOTC.y = toAct_mc.getVisibleHeight()/2;
		return _toOTC;
	}
	
	public function getFromOTC(fromAct_mc):Object {
		var _fromOTC = new Object();
		_fromOTC.x = fromAct_mc.getVisibleWidth()/2;
		_fromOTC.y = fromAct_mc.getVisibleHeight()/2;
		return _fromOTC;
	}
}