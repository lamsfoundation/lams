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
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import com.polymercode.Draw;
import mx.utils.*


/*
* Makes changes to the Canvas Authoring model's data based on user input.
*/
class org.lamsfoundation.lams.authoring.cv.CanvasController extends AbstractController {
	
	private var _canvasModel:CanvasModel;
	private var _canvasView:CanvasView;
	private var _pi:PropertyInspectorNew;
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
		_pi = new PropertyInspectorNew();
		app = Application.getInstance();
		_isBusy = false;
	}
   
    public function activityClick(ca:Object):Void{
		_canvasModel.selectedItem = null;
	    Debugger.log('activityClick CanvasActivity:'+ca.activity.activityUIID + ' orderID: ' + ca.activity.orderID,Debugger.GEN,'activityClick','CanvasController');
	    Debugger.log('Check if transition tool active :'+_canvasModel.isTransitionToolActive(),Debugger.GEN,'activityClick','CanvasController');
	   //if transition tool active
	    if(_canvasModel.isTransitionToolActive()){
		   
			var transitionTarget = createValidTransitionTarget(ca);
		    if(transitionTarget instanceof LFError){
				transitionTarget.showErrorAlert(null); 
		   }else{
			   	_canvasModel.addActivityToTransition(transitionTarget);
				_canvasModel.getCanvas().view.initDrawTempTrans();
			}
			
	    }else{
			
		   //just select the activity
			
			var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(ca.activity.parentUIID)
					
			if(ca.activity.parentUIID != null && parentAct.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				
				// _canvasModel.selectedItem = ca;
				 _canvasModel.isDragging = false;
			} else {
				//_canvasModel.selectedItem = ca;
				_canvasModel.isDragging = true;
				ca.startDrag(false);
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
			//_canvasModel.getCanvas().view.removeTempTrans();	
			app.controlKeyPressed = "";
		}
	    _canvasModel.getCanvas().stopActiveTool();
		//_canvasModel.selectedItem = ca;
		var optionalOnCanvas:Array  = _canvasModel.findOptionalActivities();
		var parallelOnCanvas:Array  = _canvasModel.findParallelActivities();
		
		 
	    if(_canvasModel.isDragging){
			ca.stopDrag();
			 _canvasModel.isDragging = false;
			if (ca.activity.parentUIID != null){
				trace ("testing Optional child on Canvas "+ca.activity.activityUIID)
				for (var i=0; i<optionalOnCanvas.length; i++){
				//trace ("testing Optional on Canvas "+i)
					if (ca.activity.parentUIID == optionalOnCanvas[i].activity.activityUIID){
						if (optionalOnCanvas[i].locked == false){
							
							if (ca._x > 142 || ca._x < -129 || ca._y < -55 || ca._y > optionalOnCanvas[i].getpanelHeight){
								trace (ca.activity.activityUIID+" had a hitTest with canvas.")
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
					//trace ("testing Optional on Canvas "+i)
					if (ca.activity.activityUIID != optionalOnCanvas[i].activity.activityUIID ){
						if (ca.hitTest(optionalOnCanvas[i])){
							if (optionalOnCanvas[i].locked == true){
								var msg:String = Dictionary.getValue('act_lock_chk');
								LFMessage.showMessageAlert(msg);
							}else{
								if (ca.activity.isGateActivity()){
									activitySnapBack(ca);
									var msg:String = Dictionary.getValue('cv_gateoptional_hit_chk');
									LFMessage.showMessageAlert(msg);
								}else {
									//_canvasModel.addParentToActivity(optionalOnCanvas[i].activity.activityUIID, ca)
									if (_canvasModel.getCanvas().ddm.getTransitionsForActivityUIID(ca.activity.activityUIID).hasTrans){
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
			
			
			//give it the new co-ords and 'drop' it
			ca.activity.xCoord = ca._x;
			ca.activity.yCoord = ca._y;
			
			
			//refresh the transitions
			//TODO: refresh the transitions as you drag...
			var myTransitions = _canvasModel.getCanvas().ddm.getTransitionsForActivityUIID(ca.activity.activityUIID);
			myTransitions = myTransitions.myTransitions
			//run in a loop to support branches, maybe more then 2 transitions.
			for (var i=0; i<myTransitions.length;i++){
				Debugger.log('removing transition for redraw:'+myTransitions[i].transitionUIID,Debugger.GEN,'activityRelease','CanvasController');
				var t = _canvasModel.transitionsDisplayed.remove(myTransitions[i].transitionUIID);
				t.removeMovieClip();
				
			}
			clearAllSelections(optionalOnCanvas, parallelOnCanvas)		
			_canvasModel.setDirty();
			_canvasModel.selectedItem = ca;
			Debugger.log('ca.activity.xCoord:'+ca.activity.xCoord,Debugger.GEN,'activityRelease','CanvasController');
			
			
		}else {
			if (_canvasModel.isTransitionToolActive()){
				_canvasModel.getCanvas().stopTransitionTool();
				//_canvasModel.resetTransitionTool();
				_canvasModel.getCanvas().view.removeTempTrans();
				new LFError("You cannot create a Transition between the same Activities","addActivityToTransition",this);
			}
			clearAllSelections(optionalOnCanvas, parallelOnCanvas)
			_canvasModel.setDirty();
			_canvasModel.selectedItem = ca;
		}
	}
   
   
    private function activitySnapBack(ca:Object){
		ca._x = ca.activity.xCoord;
		ca._y = ca.activity.yCoord;
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
		if(_canvasModel.getCanvas().ddm.readOnly){
			// throw alert warning
			LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_dbclick_readonly'));
		}else{
			_canvasModel.selectedItem = ca;
			if(ca.activity.activityTypeID == Activity.GROUPING_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.SYNCH_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PERMISSION_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				if (!_pi.isPIExpanded()){
					_canvasModel.setPIHeight(_pi.piFullHeight());
					//_pi.showExpand(false);
				}
			}else if (ca.activity.parentUIID != null && parentAct.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE && (ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE)){
				if (!_pi.isPIExpanded()){
					_canvasModel.setPIHeight(_pi.piFullHeight());
					
				}
			}else{
				Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityUIID,Debugger.CRITICAL,'activityDoubleClick to open Content','CanvasController');
				_canvasModel.openToolActivityContent(ca.activity);
			}
		}
		_canvasModel.getCanvas().ddm.validDesign = false;
		_canvasModel.getCanvas().checkValidDesign();
    }
   
	/**
	 * Method to invoke when mouse is release outside on an anctivity
	 * 
	 * @usage   
	 * @param   ca 		//canvas activity 
	 * @return  
	 */
	public function activityReleaseOutside(ca:Object):Void{
		
		_canvasModel.getCanvas().view.removeTempTrans();
		
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityUIID,Debugger.GEN,'activityReleaseOutside','CanvasController');
	   Debugger.log('activityReleaseOutside Check if Transition tool active:'+_canvasModel.isTransitionToolActive(),Debugger.GEN,'activityReleaseOutside','CanvasController');
	   if(_canvasModel.isTransitionToolActive()){
				
			
			//get a ref to the CanvasActivity the transition pen is over when released
			var currentCursor:MovieClip = Cursor.getCurrentCursorRef();
			//Debugger.log("currentCursor:"+currentCursor, Debugger.GEN,'activityReleaseOutside','CanvasController');
			//strip the last mc off the path as its the click target
			//_global.breakpoint();
			Debugger.log("currentCursor._droptarget:"+currentCursor._droptarget, Debugger.GEN,'activityReleaseOutside','CanvasController');
			//var _dt:String = currentCursor._droptarget;
			//var idx:Number = _dt.indexOf("/", 1);
			//var _dt = _dt.substring(idx, _dt.length);
			
			var dt:String = new String(eval(currentCursor._droptarget));
			
			Debugger.log("currentCursor._droptarget eval:"+dt, Debugger.GEN,'activityReleaseOutside','CanvasController');
			
			var i:Number = dt.lastIndexOf(".");
			dt = dt.substring(0,i);
			Debugger.log("Subst:"+dt, Debugger.GEN,'activityReleaseOutside','CanvasController');
			var transitionTarget_mc:MovieClip = eval(dt);
			Debugger.log("Transition drop target:"+transitionTarget_mc, Debugger.GEN,'activityReleaseOutside','CanvasController');
			
			var transitionTarget = createValidTransitionTarget(transitionTarget_mc);
			
			if(transitionTarget instanceof LFError){
				_canvasModel.resetTransitionTool();
				_canvasModel.getCanvas().stopTransitionTool();
				LFMessage.showMessageAlert(transitionTarget);
			}else{
				var td = _canvasModel.addActivityToTransition(transitionTarget);
				
				_canvasModel.resetTransitionTool();
				_canvasModel.getCanvas().stopTransitionTool();
				if(td instanceof LFError){
					_canvasModel.resetTransitionTool();
					_canvasModel.getCanvas().stopTransitionTool();
					LFMessage.showMessageAlert(td);
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
				
				/**if (ca.hitTest(_canvasModel.getCanvas().bin)){
					//_canvasModel.getCanvas().removeActivity(ca.activity.activityUIID);
					_canvasModel.removeActivityOnBin(ca.activity.activityUIID);
				}*/
				
			}
		   
		}
	}
   
	private function isActivityOnBin(ca:Object):Void{
				
		if (ca.hitTest(_canvasModel.getCanvas().bin)){
			trace("Activity "+ca.activity.title+" has hit the bin")
			if (ca.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				trace("Complex Activity has hit the bin")
				_canvasModel.removeComplexActivity(ca);
			}else {
				_canvasModel.removeActivityOnBin(ca.activity.activityUIID);
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
   
    public function transitionDoubleClick(ct:CanvasTransition):Void{
	   Debugger.log('transitionDoubleClick CanvasTransition:'+ct.transition.transitionUIID,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   
	   _canvasModel.getCanvas().stopActiveTool();
	   
	   //TODO: fix this, its null
	   _canvasView =  CanvasView(getView());
	   Debugger.log('_canvasView:'+_canvasView,Debugger.GEN,'transitionDoubleClick','CanvasController');
	   _canvasView.createTransitionPropertiesDialog("centre",Delegate.create(this, transitionPropertiesOK));
	  
		
	    _canvasModel.selectedItem = ct;
    }
   
    public function transitionRelease(ct:CanvasTransition):Void{
	Debugger.log("transitionRelease Transition:" + ct.transition.uiID, Debugger.GEN, "transitionRelease", "CanvasController");
		if(_canvasModel.isDragging){
			ct.stopDrag();
			
			if (ct.hitTest(_canvasModel.getCanvas().bin)){
				_canvasModel.getCanvas().removeTransition(ct.transition.transitionUIID);
			}
			else {
				trace("transitions x position:"+ct._x)
				if (ct._x != ct.xPosition){
					
					var t = _canvasModel.transitionsDisplayed.remove(ct.transition.transitionUIID);
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
		trace("transitionrelease outside")
		transitionRelease(ct);
		
	}
   
	private function setBusy():Void{
		if(_isBusy){
			//Debugger.log('!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
			//Debugger.log('!!!!!!!!!!!!!!!!!!!! HASHTABLE ACCESED WHILE BUSY !!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
			//Debugger.log('!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
		}
		_isBusy=true;
	}
	
	private function clearBusy():Void{
		_isBusy=false;
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
		_canvasModel.createGateTransition(_canvasModel.selectedItem.transition.transitionUIID,evt.gate);
		
		
		
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
		Debugger.log(canvas_mc,Debugger.GEN,'canvasRelease','CanvasController');
		Debugger.log('_canvasModel.activeTool:'+_canvasModel.activeTool,Debugger.GEN,'canvasRelease','CanvasController');
		_canvasModel.selectedItem = null;
		if(_canvasModel.activeTool == CanvasModel.GATE_TOOL){
			var p = new Point(canvas_mc._xmouse, canvas_mc._ymouse); 
			_canvasModel.createNewGate(Activity.PERMISSION_GATE_ACTIVITY_TYPE,p);
			_canvasModel.getCanvas().stopGateTool();
		}
		
		if(_canvasModel.activeTool == CanvasModel.OPTIONAL_TOOL){
			var p = new Point(canvas_mc._xmouse-(complexActWidth/2), canvas_mc._ymouse); 
			_canvasModel.createNewOptionalActivity(Activity.OPTIONAL_ACTIVITY_TYPE,p);
			//_canvasModel.createNewOptionalActivity(p);
			_canvasModel.getCanvas().stopOptionalActivity();
			
		}
		if(_canvasModel.activeTool == CanvasModel.GROUP_TOOL){
			var p = new Point(canvas_mc._xmouse-(toolActWidth/2), canvas_mc._ymouse-(toolActHeight/2)); 
			_canvasModel.createNewGroupActivity(p);
			_canvasModel.getCanvas().stopGroupTool();
			
		}
		
	}
	
	private function createValidTransitionTarget(transitionTargetObj:Object):Object{
			var targetCA:Object;
			Debugger.log("My transitionTargetObj is :"+transitionTargetObj.activity.activityUIID, Debugger.GEN,'createValidTransitionTarget','CanvasController');
			//see what we can cast to
			if(CanvasActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasActivity", Debugger.GEN,'createValidTransitionTarget','CanvasController');
				targetCA = CanvasActivity(transitionTargetObj);
				return targetCA;
			}else if(CanvasParallelActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasParallelActivity", Debugger.GEN,'createValidTransitionTarget','CanvasController');
				targetCA = CanvasParallelActivity(transitionTargetObj);
				return targetCA;
			}else if(CanvasOptionalActivity(transitionTargetObj)!=null){
				Debugger.log("Casting to CanvasOptionalActivity", Debugger.GEN,'createValidTransitionTarget','CanvasController');
				targetCA = CanvasOptionalActivity(transitionTargetObj);
				return targetCA;
			}
	}
 
}
