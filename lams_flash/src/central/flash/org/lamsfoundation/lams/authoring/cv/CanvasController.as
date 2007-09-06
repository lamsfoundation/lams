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

import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.br.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import com.polymercode.Draw;

import mx.utils.*;
import mx.managers.*;


/*
* Makes changes to the Canvas Authoring model's data based on user input.
*/
class org.lamsfoundation.lams.authoring.cv.CanvasController extends AbstractController {
	
	private var _canvasModel:CanvasModel;
	private var _canvasView:CanvasView;
	private var _pi:PropertyInspector;
	private var app:Application;
	private var _isBusy:Boolean;
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function CanvasController (cm:Observable) {
		super (cm);
		
		//have to do an upcast
		_canvasModel = CanvasModel(getModel());
		_canvasView = CanvasView(getView());
		_pi = new PropertyInspector();
		app = Application.getInstance();
		_isBusy = false;
	}
   
    public function activityClick(ca:Object):Void{
		_canvasModel.selectedItem = null;
	    
		Debugger.log('activityClick CanvasActivity:'+ca.activity.activityUIID + ' orderID: ' + ca.activity.orderID,Debugger.GEN,'activityClick','CanvasController');
	    Debugger.log('Check if transition tool active :'+_canvasModel.isTransitionToolActive(),Debugger.GEN,'activityClick','CanvasController');
	    
		//if transition tool active
	    if(_canvasModel.isTransitionToolActive()){
		    
			var transitionTarget = createValidTransitionTarget(ca, true);
		    if(transitionTarget instanceof LFError){
				transitionTarget.showErrorAlert(null); 
		    }else{
				_canvasModel.activeView.fingerprint = transitionTarget;
			   	var td = _canvasModel.addActivityToConnection(transitionTarget);
				_canvasModel.activeView.initDrawTempTrans();
			}
			
	    }else{
			
		   //just select the activity
			var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(ca.activity.parentUIID)
					
			if(ca.activity.parentUIID != null && parentAct.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				_canvasModel.isDragging = false;
			} else {
				ca.startDrag(false);
				_canvasModel.isDragging = true;
			}
		}
	   
    }
   
    public function clearAllSelections(optionalOnCanvas:Array, parallelOnCanvas:Array){
		for (var i=0; i<optionalOnCanvas.length; i++){
			for(var j=0; j < optionalOnCanvas[i].actChildren.length; j++){
				optionalOnCanvas[i].actChildren[j].setSelected(false);
			}
			optionalOnCanvas[i].refreshChildren();
		}
		
		for (var m=0; m<parallelOnCanvas.length; m++){
			for(var n=0; n < parallelOnCanvas[m].actChildren.length; n++){
				parallelOnCanvas[m].actChildren[n].selectActivity = "false";
			}
			parallelOnCanvas[m].refreshChildren();
		}
    }
	
   
    public function activityRelease(ca:Object):Void{
	    Debugger.log('activityRelease CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityRelease','CanvasController');
	    
		if (_canvasModel.isTransitionToolActive()){
			_canvasModel.getCanvas().stopTransitionTool();
			_canvasModel.activeView.removeTempTrans();	
			app.controlKeyPressed = "";
		}
	    
		_canvasModel.getCanvas().stopActiveTool();
		var optionalOnCanvas:Array  = _canvasModel.findOptionalActivities();
		var parallelOnCanvas:Array  = _canvasModel.findParallelActivities();
		 
	    if(_canvasModel.isDragging){
			ca.stopDrag();
			_canvasModel.isDragging = false;
			
			if (ca.activity.parentUIID != null && _canvasModel.getCanvas().ddm.getActivityByUIID(ca.activity.parentUIID).activityTypeID != Activity.SEQUENCE_ACTIVITY_TYPE){
				for (var i=0; i<optionalOnCanvas.length; i++){
					if (ca.activity.parentUIID == optionalOnCanvas[i].activity.activityUIID){
						if (optionalOnCanvas[i].locked == false){
							
							if (ca._x > 142 || ca._x < -129 || ca._y < -55 || ca._y > optionalOnCanvas[i].getpanelHeight){
								//give it the new co-ords and 'drop' it
								ca.activity.xCoord = (_xmouse - _canvasModel.getPosition().x) - (_canvasModel.getCanvas().taWidth/2);
								ca.activity.yCoord = (_ymouse - _canvasModel.getPosition().y) - (_canvasModel.getCanvas().taHeight/2);
								_canvasModel.removeOptionalCA(ca, optionalOnCanvas[i].activity.activityUIID);
							} else {
								activitySnapBack(ca);
							}
						}
					}
				}
				
			} else {
			
				//if we are on the optional Activity remove this activity from canvas and assign it a parentID of optional activity and place it in the optional activity window.
				for (var i=0; i<optionalOnCanvas.length; i++){
					if(ca.activity.activityUIID != optionalOnCanvas[i].activity.activityUIID ){
						if(ca.hitTest(optionalOnCanvas[i])){
							if(optionalOnCanvas[i].locked == true){
								var msg:String = Dictionary.getValue('act_lock_chk');
								LFMessage.showMessageAlert(msg);
							}else{
								if(ca.activity.isGateActivity()){
									activitySnapBack(ca);
									var msg:String = Dictionary.getValue('cv_gateoptional_hit_chk');
									LFMessage.showMessageAlert(msg);
								}else {
									if(_canvasModel.getCanvas().ddm.getTransitionsForActivityUIID(ca.activity.activityUIID).hasTrans){
										activitySnapBack(ca);
										var msg:String = Dictionary.getValue('cv_invalid_optional_activity', [ca.activity.title]);
										LFMessage.showMessageAlert(msg);
									}else {
										_canvasModel.addParentToActivity(optionalOnCanvas[i].activity.activityUIID, ca)
									}
								}
							}						
						}
					}
				}
			}
			
			//if we are on the bin - trash it
			isActivityOnBin(ca);
			
			//get a view if ther is not one
			if(!_canvasView){
				_canvasView =  CanvasView(getView());
			}
			
			// restricting (x, y) position to positive plane only
			Debugger.log("ca x: " + ca._x, Debugger.CRITICAL, "activityRelease", "CanvasController");
			Debugger.log("ca y: " + ca._y, Debugger.CRITICAL, "activityRelease", "CanvasController");
			if(ca._x < 0) ca._x = 0;
			if(ca._y < 0) ca._y = 0;
			if(ca._x > _canvasView.gridLayer._width - ca.getVisibleWidth()) ca._x = _canvasView.gridLayer._width - ca.getVisibleWidth();
			if(ca._y > _canvasView.gridLayer._height - ca.getVisibleHeight()) ca._y = _canvasView.gridLayer._height - ca.getVisibleHeight();
			
			//give it the new co-ords and 'drop' it
			if(!ca.branchConnector) {
				ca.activity.xCoord = ca._x;
				ca.activity.yCoord = ca._y;
				
				if(ca.activity.isBranchingActivity()) ca.setPosition(ca._x, ca._y);
				
			} else {
				if(_canvasModel.activeView.isStart(ca)) {
					ca.activity.startXCoord = ca._x;
					ca.activity.startYCoord = ca._y;
				} else if(_canvasModel.activeView.isEnd(ca)) {
					ca.activity.endXCoord = ca._x;
					ca.activity.endYCoord = ca._y;
				}
			}
			
			//refresh the transitions
			//TODO: refresh the transitions as you drag...
			var myTransitions = _canvasModel.getCanvas().ddm.getTransitionsForActivityUIID(ca.activity.activityUIID);
			myTransitions = myTransitions.myTransitions;
			
			//run in a loop to support branches, maybe more then 2 transitions.
			for (var i=0; i<myTransitions.length;i++){
				var t = _canvasModel.transitionsDisplayed.remove(myTransitions[i].transitionUIID);
				t.removeMovieClip();
			}
			
			// check for modified transition connected to activity
			var modTransitions = _canvasModel.getModTransitionsForActivityUIID(ca.activity.activityUIID);
			modTransitions = modTransitions.modTransitions;
			
			for (var i=0; i<modTransitions.length;i++){
				Debugger.log('removing transition for redraw:'+modTransitions[i].transitionUIID,Debugger.GEN,'activityRelease','CanvasController');
				var t = _canvasModel.transitionsDisplayed.remove(modTransitions[i].transitionUIID);
				t.removeMovieClip();
			}
			
			// refresh any branches connected to activities
			//TODO: refresh the branches as you drag...
			var myBranches = _canvasModel.getCanvas().ddm.getBranchesForActivityUIID(ca.activity.activityUIID);
			myBranches = myBranches.myBranches;
			
			for (var i=0; i<myBranches.length; i++){
				Debugger.log('removing branch for redraw:'+myBranches[i].branchUIID,Debugger.GEN,'activityRelease','CanvasController');
				var b = _canvasModel.branchesDisplayed.remove(myBranches[i].branchUIID);
				b.removeMovieClip();
			}
			
			clearAllSelections(optionalOnCanvas, parallelOnCanvas);
			_canvasModel.selectedItem = ca;	
			_canvasModel.setDirty();
			Debugger.log('ca.activity.xCoord:'+ca.activity.xCoord,Debugger.GEN,'activityRelease','CanvasController');
			
			
		} else {
			if (_canvasModel.isTransitionToolActive()){
				_canvasModel.getCanvas().stopTransitionTool();
				_canvasModel.activeView.removeTempTrans();
				new LFError("You cannot create a Transition between the same Activities","addActivityToTransition",this);
			}
			
			clearAllSelections(optionalOnCanvas, parallelOnCanvas);
			_canvasModel.selectedItem = ca;
			_canvasModel.setDirty();
		}
	}
   
    private function activitySnapBack(ca:Object){
		if(ca.branchConnector) {
			
			if(_canvasModel.activeView.isStart(ca)) {
				ca._x = ca.activity.startXCoord;
				ca._y = ca.activity.startYCoord;
			} else if(_canvasModel.activeView.isEnd(ca)) {
				ca._x = ca.activity.endXCoord;
				ca._y = ca.activity.endYCoord;
			}
				
		} else {
			ca._x = ca.activity.xCoord;
			ca._y = ca.activity.yCoord;
		}
	}
	
	/**
	 * called when user double click on the activity
	 * 
	 * @usage   
	 * @param   ca 
	 * @return  
	 */
	public function activityDoubleClick(ca:Object):Void{
	    Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityUIID,Debugger.CRITICAL,'activityDoubleClick','CanvasController');
		var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(ca.activity.parentUIID)
		_canvasModel.getCanvas().stopActiveTool();
		_canvasModel.getCanvas().stopTransitionTool();	
		if(!isActivityReadOnly(ca, Dictionary.getValue("cv_element_readOnly_action_mod"))) {
			if(_canvasModel.getCanvas().ddm.readOnly && !_canvasModel.getCanvas().ddm.editOverrideLock){
				// throw alert warning
				LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_dbclick_readonly'));
			}else{
				_canvasModel.selectedItem = ca;
				if(ca.activity.activityTypeID == Activity.GROUPING_ACTIVITY_TYPE || 
				   ca.activity.activityTypeID == Activity.SYNCH_GATE_ACTIVITY_TYPE || 
				   ca.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE || 
				   ca.activity.activityTypeID == Activity.PERMISSION_GATE_ACTIVITY_TYPE || 
				   ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
					if (!_pi.isPIExpanded()){
						_canvasModel.setPIHeight(_pi.piFullHeight());
					}
				} else if (ca.activity.parentUIID != null && parentAct.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE && (ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE)){
					if (!_pi.isPIExpanded()){
						_canvasModel.setPIHeight(_pi.piFullHeight());
						
					}
				} else if(ca.activity.isBranchingActivity()) {
					Debugger.log('activityDoubleClick CanvasBranchActivity:'+ca.activity.activityUIID,Debugger.CRITICAL,'activityDoubleClick','CanvasController');
	   
					_canvasModel.openBranchActivityContent(ca);
					
					// invalidate design after opening tool content window
					_canvasModel.getCanvas().ddm.validDesign = false;
					_canvasModel.getCanvas().checkValidDesign();
				} else {
					Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityUIID,Debugger.CRITICAL,'activityDoubleClick to open Content','CanvasController');
					_canvasModel.openToolActivityContent(ca.activity);
					
					// invalidate design after opening tool content window
					_canvasModel.getCanvas().ddm.validDesign = false;
					_canvasModel.getCanvas().checkValidDesign();
				}
			}
		}
    }
   
	/**
	 * Method to invoke when mouse is release outside on an anctivity
	 * 
	 * @usage   
	 * @param   ca 		//canvas activity 
	 * @return  
	 */
	public function activityReleaseOutside(ca:Object):Void{
		
		_canvasModel.activeView.removeTempTrans();
		
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityReleaseOutside','CanvasController');
	   Debugger.log('activityReleaseOutside Check if Transition tool active:'+_canvasModel.isTransitionToolActive(),Debugger.GEN,'activityReleaseOutside','CanvasController');
	   
	   if(_canvasModel.isTransitionToolActive()){
			
			//get a ref to the CanvasActivity the transition pen is over when released
			var currentCursor:MovieClip = Cursor.getCurrentCursorRef();
			var dt:String = new String(eval(currentCursor._droptarget));
			Debugger.log("currentCursor._droptarget eval:"+dt, Debugger.GEN,'activityReleaseOutside','CanvasController');
			
			var i:Number = dt.lastIndexOf(".");
			dt = dt.substring(0,i);
			
			var transitionTarget_mc:MovieClip = eval(dt);
			Debugger.log("Transition drop target:"+transitionTarget_mc, Debugger.GEN,'activityReleaseOutside','CanvasController');
			
			var transitionTarget = createValidTransitionTarget(transitionTarget_mc);
			
			if(transitionTarget instanceof LFError){
				_canvasModel.resetTransitionTool();
				_canvasModel.getCanvas().stopTransitionTool();
				LFError(transitionTarget).showErrorAlert(null);
			} else {
				_canvasModel.activeView.fingerprint = transitionTarget;
				var td = _canvasModel.addActivityToConnection(transitionTarget);
						
				_canvasModel.resetTransitionTool();
				_canvasModel.getCanvas().stopTransitionTool();
				
				if(td instanceof LFError){
					_canvasModel.resetTransitionTool();
					_canvasModel.getCanvas().stopTransitionTool();
					LFError(td).showErrorAlert(null);
				}
			}
			
			_canvasModel.resetTransitionTool();
			_canvasModel.getCanvas().stopActiveTool();
			_canvasModel.getCanvas().stopTransitionTool();
			app.controlKeyPressed = ""
			
	   }else{
			if(_canvasModel.isDragging){
				ca.stopDrag();
				
				//if we are on the bin - trash it
				isActivityOnBin(ca);
				
				if(ca._x < 0) ca._x = 0;
				if(ca._y < 0) ca._y = 0;
				if(ca._x > _canvasView.gridLayer._width - ca.getVisibleWidth()) ca._x = _canvasView.gridLayer._width - ca.getVisibleWidth();
				if(ca._y > _canvasView.gridLayer._height - ca.getVisibleHeight()) ca._y = _canvasView.gridLayer._height - ca.getVisibleHeight();
			
				// give it the new co-ords and 'drop' it
				ca.activity.xCoord = ca._x;
				ca.activity.yCoord = ca._y;
			}
		}
	}
    
	private function isActivityOnBin(ca:Object):Void{
		if (ca.hitTest(_canvasModel.getCanvas().bin)) {
			if(!isActivityReadOnly(ca, Dictionary.getValue("cv_element_readOnly_action_del")) && !ca.branchConnector){
				if (ca.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
					_canvasModel.removeComplexActivity(ca);
				} else {
					_canvasModel.removeActivityOnBin(ca.activity.activityUIID);
				}
			} else {
				activitySnapBack(ca);
			}
		}
	}
   
    public function transitionClick(ct:CanvasTransition):Void{
	    Debugger.log('transitionClick Transition:'+ct.transition.uiID,Debugger.GEN,'transitionClick','CanvasController');
	    _canvasModel.getCanvas().stopActiveTool();
		
		_canvasModel.selectedItem = ct;
		_canvasModel.isDragging = true;
		ct.startDrag(false);
	   
	}
	
	public function branchClick(bc:BranchConnector):Void{
	    Debugger.log('branchClick Transition:'+bc.branch.branchUIID,Debugger.GEN,'branchClick','CanvasController');
	    _canvasModel.getCanvas().stopActiveTool();
		
		_canvasModel.selectedItem = bc;
		_canvasModel.isDragging = true;
		bc.startDrag(false);
	   
	}
	
    public function transitionDoubleClick(ct:CanvasTransition):Void{
		Debugger.log('transitionDoubleClick CanvasTransition:'+ct.transition.transitionUIID,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   
		_canvasModel.getCanvas().stopActiveTool();
	   
		//TODO: fix this, its null
		_canvasView =  CanvasView(getView());
	   
		if(!isTransitionTargetReadOnly(ct, Dictionary.getValue("cv_element_readOnly_action_mod"))) _canvasModel.activeView.createTransitionPropertiesDialog("centre",Delegate.create(this, transitionPropertiesOK));
		
	    _canvasModel.selectedItem = ct;
    }
    
    public function branchDoubleClick(bc:BranchConnector):Void{
		Debugger.log('branchDoubleClick CanvasConnection:' + bc.branch.branchUIID,Debugger.GEN,'branchDoubleClick','CanvasController');
	   
		_canvasModel.getCanvas().stopActiveTool();
	   
		//TODO: fix this, its null
		_canvasView =  CanvasView(getView());
	    _canvasModel.selectedItem = bc;
    }
   
    public function transitionRelease(ct:CanvasTransition):Void{
		Debugger.log("transitionRelease Transition:" + ct.transition.uiID, Debugger.GEN, "transitionRelease", "CanvasController");
		if(_canvasModel.isDragging){
			ct.stopDrag();
			
			if(ct.hitTest(_canvasModel.getCanvas().bin) && !isTransitionTargetReadOnly(ct, Dictionary.getValue("cv_element_readOnly_action_del"))){
				_canvasModel.getCanvas().removeTransition(ct.transition.transitionUIID); 
			} else {
				if (ct._x != ct.xPosition){
					var t = _canvasModel.transitionsDisplayed.remove(ct.transition.transitionUIID);
					t.removeMovieClip();
					_canvasModel.setDirty();
				}
			}
		}
	
    }
    
    public function branchRelease(bc:BranchConnector):Void{
		Debugger.log("branchRelease Transition:" + bc.branch.branchUIID, Debugger.GEN, "branchRelease", "CanvasController");
		if(_canvasModel.isDragging){
			bc.stopDrag();
			if(bc.hitTest(_canvasModel.getCanvas().bin) && !isBranchTargetReadOnly(bc, Dictionary.getValue("cv_element_readOnly_action_del"))){
				var branchesToDelete:Array;
				if(bc.branch.direction == BranchConnector.DIR_FROM_START) {
					branchesToDelete = _canvasModel.getCanvas().ddm.getBranchesForActivityUIID(bc.branch.sequenceActivity.activityUIID);
					for(var i=0; i<branchesToDelete.myBranches.length; i++) {
						_canvasModel.getCanvas().removeBranch(branchesToDelete.myBranches[i].branchUIID);
					}
				} else {
					_canvasModel.getCanvas().removeBranch(bc.branch.branchUIID);
				}
				
			} else {
				if (bc._x != bc.xPosition){
					var t = _canvasModel.branchesDisplayed.remove(bc.branch.branchUIID);
					t.removeMovieClip();
					_canvasModel.setDirty();
				}
			}
		}
	
    }
	
	private function transitionSnapBack(ct:Object){
		ct.reDraw();
	}
	
	public function transitionReleaseOutside(ct:CanvasTransition):Void{
		transitionRelease(ct);
	}
   
   	public function branchReleaseOutside(bc:BranchConnector):Void{
		branchRelease(bc);
	}
   
	public function setBusy():Void{
		if(_isBusy){
			//Debugger.log('!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
			//Debugger.log('!!!!!!!!!!!!!!!!!!!! HASHTABLE ACCESED WHILE BUSY !!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
			//Debugger.log('!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
		}
		_isBusy=true;
	}
	
	public function clearBusy():Void{
		_isBusy=false;
	}
	
	public function get isBusy():Boolean {
		return _isBusy;
	}
	
	
	/**
	 * Transition Properties OK Handler
	 * @usage   
	 * @return  
	 */
	public function transitionPropertiesOK(evt:Object):Void{
		Debugger.log(evt.gate,Debugger.GEN,'transitionPropertiesOK','CanvasController');
		//cm creates gate act and another transition to make [trans][gate][trans] seq
		//sets in ddm
		//flags dirty to refresh view
		var parent:SequenceActivity = null;
		if(_canvasModel.activeView instanceof CanvasBranchView) {
			parent = _canvasModel.activeView.defaultSequenceActivity;
		}
		
		_canvasModel.createGateTransition(_canvasModel.selectedItem.transition.transitionUIID, evt.gate, parent);
	}
		
	/**
	* Clled when we get a click on the canvas, if its in craete gate mode then create a gate
	* 
	 * public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
	 * public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
	 * public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
	 * @usage   
	 * @param   canvas_mc 
	 * @return  
	 */
	public function canvasRelease(canvas_mc:MovieClip){
		
		var toolActWidth:Number = _canvasModel.getCanvas().toolActivityWidth;
		var toolActHeight:Number = _canvasModel.getCanvas().toolActivityHeight;
		
		var complexActWidth:Number = _canvasModel.getCanvas().complexActivityWidth;
		
		Debugger.log(_canvasModel.activeView + " " + _canvasModel.activeView instanceof CanvasBranchView,Debugger.GEN,'canvasRelease','CanvasController');
		Debugger.log('_canvasModel.activeTool:'+_canvasModel.activeTool,Debugger.GEN,'canvasRelease','CanvasController');
		
		_canvasModel.selectedItem = null;
		
		var parent:SequenceActivity = null;
		if(_canvasModel.activeView instanceof CanvasBranchView) {
			parent = _canvasModel.activeView.defaultSequenceActivity;
		}
		
		if(_canvasModel.activeTool == CanvasModel.GATE_TOOL){
			var p = new Point(_canvasModel.activeView.content._xmouse, _canvasModel.activeView.content._ymouse); 
			_canvasModel.createNewGate(Activity.PERMISSION_GATE_ACTIVITY_TYPE,p,parent);
			_canvasModel.getCanvas().stopGateTool();
		}
		
		if(_canvasModel.activeTool == CanvasModel.OPTIONAL_TOOL){
			var p = new Point(_canvasModel.activeView.content._xmouse-(complexActWidth/2), _canvasModel.activeView.content._ymouse); 
			_canvasModel.createNewOptionalActivity(Activity.OPTIONAL_ACTIVITY_TYPE,p,parent);
			_canvasModel.getCanvas().stopOptionalActivity();
			
		}
		
		if(_canvasModel.activeTool == CanvasModel.GROUP_TOOL){
			var p = new Point(_canvasModel.activeView.content._xmouse-(toolActWidth/2), _canvasModel.activeView.content._ymouse-(toolActHeight/2)); 
			_canvasModel.createNewGroupActivity(p,parent);
			_canvasModel.getCanvas().stopGroupTool();
			
		}
		
		if(_canvasModel.activeTool == CanvasModel.BRANCH_TOOL){
			var p = new Point(_canvasModel.activeView.content._xmouse-(toolActWidth/2), _canvasModel.activeView.content._ymouse-(toolActHeight/2)); 
			_canvasModel.createNewBranchActivity(Activity.CHOOSEN_BRANCHING_ACTIVITY_TYPE,p,parent);
			_canvasModel.getCanvas().stopBranchTool();
		}
		
	}
	
	private function createValidTransitionTarget(transitionTargetObj:Object, isInitTarget:Boolean):Object{
			var targetCA:Object;
			Debugger.log("My transitionTargetObj is :"+transitionTargetObj.activity.activityUIID, Debugger.GEN,'createValidTransitionTarget','CanvasController');
			//see what we can cast to
			if(CanvasActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasActivity", Debugger.GEN,'createValidTransitionTarget','CanvasController');
				targetCA = CanvasActivity(transitionTargetObj);
				return (isValidTransitionTarget(targetCA) || isInitTarget) ? targetCA : new LFError();
			}else if(CanvasParallelActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasParallelActivity", Debugger.GEN,'createValidTransitionTarget','CanvasController');
				targetCA = CanvasParallelActivity(transitionTargetObj);
				return (isValidTransitionTarget(targetCA) || isInitTarget) ? targetCA : new LFError();
			}else if(CanvasOptionalActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasOptionalActivity", Debugger.GEN,'createValidTransitionTarget','CanvasController');
				targetCA = CanvasOptionalActivity(transitionTargetObj);
				return (isValidTransitionTarget(targetCA) || isInitTarget) ? targetCA : new LFError();
			}
	}
	
	private function isValidTransitionTarget(target:Object):Boolean {
		return !Activity(target.activity).isReadOnly();
	}
 
	private function isTransitionTargetReadOnly(ct:CanvasTransition, action:String):Boolean {
		if(_canvasModel.getActivityMCByUIID(ct.transition.toUIID).activity.isReadOnly()) {
			LFMessage.showMessageAlert(Dictionary.getValue('cv_trans_readOnly', [action]));
			return true;
		} else {
			return false;
		}
	}
	
	private function isBranchTargetReadOnly(bc:BranchConnector, action:String):Boolean {
		if(_canvasModel.getActivityMCByUIID(bc.branch.targetUIID).activity.isReadOnly()) {
			LFMessage.showMessageAlert(Dictionary.getValue('cv_trans_readOnly', [action])); // TODO: change dictionary message.
			return true;
		} else {
			return false;
		}
	}
	
	private function isActivityReadOnly(ca:Object, action:String):Boolean {
		if(Activity(ca.activity).isReadOnly()) {
			LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_readOnly', [action]));
			return true;
		} else {
			return false; 
		}
	}
 
	public function openDialogLoaded(evt:Object) {
        
		if(evt.type == 'contentLoaded'){
			//set up UI
			//note this function registers the dialog to recieve view updates
			evt.target.scrollContent.setUpContent();		
			
        } else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
		
    }
	
	public function openToolOutputConditionsDialog(ta:ToolActivity){
		// open group to branch matching window
		app.dialog = PopUpManager.createPopUp(Application.root, LFWindow, true, {title:Dictionary.getValue('to_conditions_dlg_title_lbl'), closeButton:true, viewResize:false, scrollContentPath:'ToolOutputConditionsDialog'});
		app.dialog.addEventListener('contentLoaded', Proxy.create(this, ToolOutputConditionsDialogLoaded, ta));
	}
	
	private function ToolOutputConditionsDialogLoaded(evt:Object, ta:ToolActivity) {
		evt.target.scrollContent.branchingActivity = BranchingActivity(_canvasModel.selectedItem.activity);
		evt.target.scrollContent.definitions = ta.definitions;
		evt.target.scrollContent.setupContent();
	}
	
}
