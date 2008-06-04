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


import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.SequenceActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasActivity;
import org.lamsfoundation.lams.authoring.br.*;

import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.style.*;

/**
 *
 * @author mseaton
 * @version 2.1
 **/
class CanvasBranchingDiagram extends MovieClip  {
	
	//private static var CIRCLE_SIZE:Number = 3;
	private static var ACT_SIZE:Number = 4;
	private static var BRANCH_LIMIT:Number = 5;
	
	private static var ACTIVITY_LIMIT:Number = 6;
	
	private var MARGIN_X:Number;
	private var MARGIN_Y:Number;
	private static var SPACING:Number = 15;
	
	private var container:MovieClip;
	private var line_mc:MovieClip;
	
	private var _branchingDetails:Array;
	private var _branchingActivity:Activity;
	private var activitylessDrawn:Boolean;
	private var activitylessToBeDrawn:Boolean;
		
	private var _empty:Boolean;
	
	private var _canvasActivity:CanvasActivity;
	private var _ddm:DesignDataModel;
	private var _tm:ThemeManager;
	
	// style colors
	private var lineColor:Number;
	private var fillColor:Number;
	private var stopColor:Number;
	
	public function CanvasBranchingDiagram() {
		// get branching details
		_canvasActivity = CanvasActivity(this._parent);
		_tm = ThemeManager.getInstance();
		
		activitylessDrawn = false;
		activitylessToBeDrawn = false;
		
		_empty = false;
		
		MARGIN_X = 5;
		MARGIN_Y = 5;
		
		getDetails();
		setStyles();
		
		MovieClipUtils.doLater(Proxy.create(this, draw));
	}
	
	private function draw():Void {
		if(container != null) container.removeMovieClip();
		if(line_mc != null) line_mc.removeMovieClip();
		
		line_mc = this.createEmptyMovieClip("line_mc", this.getNextHighestDepth());
		container = this.createEmptyMovieClip("_container_mc", this.getNextHighestDepth());
		
		// draw based on branching details
		Debugger.log("drawing branching details in : " + container, Debugger.CRITICAL, "draw", "CanvasBranchingDiagram");
		
		if(_branchingDetails.length > 0) {		
			registerEndPoints();
			line_mc.lineStyle(0, lineColor, 100, true, "normal", "square", "miter", 1);
			
			// draw activityless
			for(var i=0; i < _branchingDetails.length; i++) {
				if(_branchingDetails[i].firstActivityUIID == null) {
					activitylessToBeDrawn = true;
				}
			}
			
			for(var i=0; (i < _branchingDetails.length && i < BRANCH_LIMIT); i++) {
				if(_branchingDetails[i].firstActivityUIID != null) {
					setPointAction(true, false, false);
						
					var count:Number = (activitylessDrawn && i==1) ? i-1 : i;
					count = (activitylessToBeDrawn && !activitylessDrawn && i>=1) ? count+1 : count;
					
					var j = 1;
					
					var nextActivity:Activity = _ddm.getActivityByUIID(_branchingDetails[i].firstActivityUIID);
					
					while(j <= ACTIVITY_LIMIT && nextActivity != null) {
						line_mc.lineTo(MARGIN_X+(j*SPACING), MARGIN_Y+(SPACING*count));
						
						var assocColorObj:Object = getAssociatedStyle(nextActivity);
						
						if(j == ACTIVITY_LIMIT && _branchingDetails[i].noActivities > ACTIVITY_LIMIT) {
							drawActivity(container, MARGIN_X+(j*SPACING), MARGIN_Y+(SPACING*count), ACT_SIZE, true, false, assocColorObj);
							line_mc.moveTo(MARGIN_X+(j*SPACING) + (ACT_SIZE*4) + 1, MARGIN_Y+(SPACING*count));
						} else if((j == ACTIVITY_LIMIT || j == _branchingDetails[i].noActivities) && !_branchingDetails[i].hasEndBranch) {
							drawActivity(container, MARGIN_X+(j*SPACING), MARGIN_Y+(SPACING*count), ACT_SIZE, false, true, assocColorObj);
						} else {
							drawActivity(container, MARGIN_X+(j*SPACING), MARGIN_Y+(SPACING*count), ACT_SIZE, false, false, assocColorObj);
						}
						
						nextActivity = getNextActivity(nextActivity.activityUIID);
						
						j++;
					}
					
					if(_branchingDetails[i].hasEndBranch) setPointAction(false, true, true); // end point
					
				} else {
					setPointAction(true, false, false);
					setPointAction(false, true, true);
					
					activitylessDrawn = true;
				}
			}
		}
		
		setPosition();
	}
	
	private function getNextActivity(activityUIID:Number):Activity {
		var transitionObj:Object = _ddm.getTransitionsForActivityUIID(activityUIID);
		return _ddm.getActivityByUIID(transitionObj.out.toUIID);
	}
	
	private function setPointAction(_moveTo:Boolean, _lineTo:Boolean, _isEnd:Boolean):Object {
		var p:Object = new Object();
		p.x = (_isEnd) ? MARGIN_X+((ACTIVITY_LIMIT+1)*SPACING) : MARGIN_X;
		p.y = (BRANCH_LIMIT%2 != 0) ? MARGIN_Y+(SPACING*(BRANCH_LIMIT%2)) : MARGIN_Y+(SPACING*((BRANCH_LIMIT/2)-1));
		
		if(_moveTo)
			line_mc.moveTo(p.x, p.y);
			
		if(_lineTo)
			line_mc.lineTo(p.x, p.y);

		return p;
	}
	
	private function getDetails():Void {
		_branchingDetails = new Array();
		Debugger.log("activity: " + _branchingActivity.activityUIID, Debugger.CRITICAL, "getDetails", "CanvasBranchingDiagram");
		
		var sequences:Array = _ddm.getComplexActivityChildren(_branchingActivity.activityUIID);
		
		if(sequences.length > 0) { 
			_empty = false;
			
			for(var i=0; i<sequences.length; i++) {
				 var acts:Array = _ddm.getComplexActivityChildren(sequences[i].activityUIID);
				 
				 if(!SequenceActivity(sequences[i]).isDefault && (!SequenceActivity(sequences[i]).stopAfterActivity || SequenceActivity(sequences[i]).firstActivityUIID != null))
					_branchingDetails.push({firstActivityUIID: SequenceActivity(sequences[i]).firstActivityUIID, noActivities: acts.length, hasEndBranch: !sequences[i].stopAfterActivity});
			}
		} 
		
		if(_branchingDetails.length <= 0){
			// no sequences show default icon
			_empty = true;
		}
	}
	
	public function refresh():Void {
		activitylessToBeDrawn = false;
		activitylessDrawn = false;
		
		getDetails();
		draw();
	}
	
	private function setStyles():Void {
		var so = _tm.getStyleObject("branchingDiagram");
		
		lineColor = so.lineColor;
		fillColor = so.fillColor;
		stopColor = so.stopColor;
	}
	
	public function setPosition():Void {
		_x = _canvasActivity.getVisibleWidth()/2 - (((ACTIVITY_LIMIT+1)*SPACING)+(2*MARGIN_X))/2;
		_y = (_branchingDetails.length <= BRANCH_LIMIT) ? _canvasActivity.getVisibleHeight()/2 - (((_branchingDetails.length+1)*SPACING)+(2*MARGIN_Y))/2 : _canvasActivity.getVisibleHeight()/2 - (((BRANCH_LIMIT+1)*SPACING)+(2*MARGIN_Y))/2;
	}
	
	private function registerEndPoints():Void {
		var startPoint:Object = setPointAction(false, false, false);
		var endPoint:Object = setPointAction(false, false, true);
		
		drawActivity(container, startPoint.x, startPoint.y, ACT_SIZE);
		drawActivity(container, endPoint.x, endPoint.y, ACT_SIZE);
	}
	
	/**
	private function drawCircle(mc:MovieClip, x:Number, y:Number, r:Number, indicateMore:Boolean, indicateStops:Boolean):Void {
		  
		  
		  if(indicateStops) mc.lineStyle(0, stopColor);
		  else mc.lineStyle(0, lineColor);
		  
		  if(indicateStops) mc.beginFill(stopColor);
		  else mc.beginFill(fillColor);
		  
		  mc.moveTo(x+r, y);
		  mc.curveTo(r+x, Math.tan(Math.PI/8)*r+y, Math.sin(Math.PI/4)*r+x, Math.sin(Math.PI/4)*r+y);
		  mc.curveTo(Math.tan(Math.PI/8)*r+x, r+y, x, r+y);
		  mc.curveTo(-Math.tan(Math.PI/8)*r+x, r+y, -Math.sin(Math.PI/4)*r+x, Math.sin(Math.PI/4)*r+y);
		  mc.curveTo(-r+x, Math.tan(Math.PI/8)*r+y, -r+x, y);
		  mc.curveTo(-r+x, -Math.tan(Math.PI/8)*r+y, -Math.sin(Math.PI/4)*r+x, -Math.sin(Math.PI/4)*r+y);
		  mc.curveTo(-Math.tan(Math.PI/8)*r+x, -r+y, x, -r+y);
		  mc.curveTo(Math.tan(Math.PI/8)*r+x, -r+y, Math.sin(Math.PI/4)*r+x, -Math.sin(Math.PI/4)*r+y);
		  mc.curveTo(r+x, -Math.tan(Math.PI/8)*r+y, r+x, y);
		  mc.endFill();
		 
		 if(indicateMore) {
			 drawCircle(mc, x+(CIRCLE_SIZE*2)+1, y, CIRCLE_SIZE-1);
			 drawCircle(mc, x+(CIRCLE_SIZE*4)+1, y, CIRCLE_SIZE-2);
		 }
		  
	}
	*/
	
	private function drawActivity(mc:MovieClip, x:Number, y:Number, r:Number, indicateMore:Boolean, indicateStops:Boolean, useAssocColor:Object):Void {
		  if(indicateStops) mc.lineStyle(0.5, stopColor);
		  else mc.lineStyle(0.5, lineColor);
		  
		  if(useAssocColor != null) mc.beginFill(useAssocColor.backgroundColor);
		  else mc.beginFill(fillColor);
		  
		  mc.moveTo(x-r, y-r);
		  mc.lineTo(x+r, y-r);
		  mc.lineTo(x+r, y+r);
		  mc.lineTo(x-r, y+r);
		  mc.lineTo(x-r, y-r);
		  
		  if(indicateMore) {
			 drawActivity(mc, x+(ACT_SIZE*2)+1, y, ACT_SIZE-1);
			 drawActivity(mc, x+(ACT_SIZE*4)+1, y, ACT_SIZE-2);
		 }
	}
	
	private function getAssociatedStyle(_activity:Activity):Object{
		var styleObj:Object = new Object();
		
		if(_root.actColour == "true") {
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
		} else {
			styleObj = _tm.getStyleObject('ACTPanel');
		}
		
		return styleObj;
	}
	
	public function get empty():Boolean {
		return _empty;
	}


}