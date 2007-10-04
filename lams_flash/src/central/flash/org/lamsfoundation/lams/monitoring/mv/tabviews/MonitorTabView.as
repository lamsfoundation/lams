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

import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*

import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.monitoring.*;

import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.ComplexActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasActivity;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.authoring.Transition;

import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*
import mx.controls.*;


/**
*Monitoring Tab view for the Monitor
* Reflects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.tabviews.MonitorTabView extends CommonCanvasView {
	
	public static var _tabID:Number = 1;
	private var _className = "MonitorTabView";
	
	private var _tm:ThemeManager;
	
	private var mm:MonitorModel;
	private var _monitorTabView:MonitorTabView;

	private var learner_X:Number = 22;
	private var learner_Y:Number = 19;
	
	private var drawDesignCalled:String;
	
	private var finishedLearnersList:Array;
	
	private var learnerMenuBar:MovieClip;
	private var monitorTabs_tb:MovieClip;
	private var _monitorTabViewContainer_mc:MovieClip;
	private var bkg_pnl:MovieClip;
	private var _learnerContainer_mc:MovieClip;
	
	/**
	* Constructor
	*/
	function MonitorTabView(){
		_monitorTabView = this;
		_monitorTabViewContainer_mc = this;
		
		_tm = ThemeManager.getInstance();
		
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}

	/**
	* 
	*/
	public function init(m:Observable,c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		mm = MonitorModel(model)
		
        //Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;
		
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
		mm.getMonitor().getMV().getMonitorSequenceScp()._visible = false;
		
    }    
	
	/**
	 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function update (o:Observable,infoObj:Object):Void{
			
		   var mm:MonitorModel = MonitorModel(o);
		   
		   switch (infoObj.updateType){
				case 'POSITION' :
					setPosition(mm);
					break;
				case 'SIZE' :
					setSize(mm);
					break;
				case 'SELECTED_ITEM' :
					highlightActivity(mm);
					break;
				case 'TABCHANGE' :
					if (infoObj.tabID == _tabID && !mm.locked){
						setStyles();
						mm.getMonitor().getMV().getMonitorSequenceScp()._visible = true;
						hideMainExp(mm);
						mm.broadcastViewUpdate("JOURNALSSHOWHIDE", false);

						if (mm.activitiesDisplayed.isEmpty() || mm.transitionsDisplayed.isEmpty()){
							mm.getMonitor().openLearningDesign(mm.getSequence());
						} else {
							
							if (drawDesignCalled == undefined){
								drawDesignCalled = "called";
								mm.drawDesign(infoObj.tabID);
							}
							
							if(mm.getIsProgressChangedSequence()){
								reloadProgress(false);
							}
						}
						
						LFMenuBar.getInstance().setDefaults();
						
					}else {
						mm.getMonitor().getMV().getMonitorSequenceScp()._visible = false;
						//this._visible = false;
					}
					break;
				case 'PROGRESS' :
					if (infoObj.tabID == _tabID){
						if(!mm.locked){
							mm.getMonitor().getProgressData(mm.getSequence());
						} else {
							ApplicationParent.extCall("reloadWindow", null);
						}
					} 
					break;
				case 'RELOADPROGRESS' :	
					if (infoObj.tabID == _tabID && !mm.locked){
						reloadProgress(true)
					}
					break;	
				case 'DRAW_ACTIVITY' :
					if (infoObj.tabID == _tabID && !mm.locked){
						drawActivity(infoObj.data, mm)
					}
					break;
				case 'CLONE_ACTIVITY' :
					if (infoObj.tabID == _tabID && !mm.locked){
						drawActivity(infoObj.data, mm)
					}
					break;
				case 'HIDE_ACTIVITY' :
					if (infoObj.tabID == _tabID && !mm.locked){
						hideActivity(infoObj.data, mm)
						
					}
					break;
				case 'DRAW_TRANSITION' :
					if (infoObj.tabID == _tabID && !mm.locked){
						drawTransition(infoObj.data, mm)
					}
					break;
				case 'HIDE_TRANSITION' :
					if (infoObj.tabID == _tabID && !mm.locked){
						hideTransition(infoObj.data, mm)
					}
					break;
					
				case 'REMOVE_ACTIVITY' :
						removeActivity(infoObj.data, mm)
					break;
				case 'REMOVE_TRANSITION' :
						removeTransition(infoObj.data, mm)
					break;
				case 'DRAW_DESIGN' :
					if (infoObj.tabID == _tabID && !mm.locked){
						
						setStyles();
						setSize(mm);
						
						drawDesignCalled = "called";
						
						mm.drawDesign(infoObj.tabID);
						
					}
					break;
				default :
					Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.MonitorTabView');
			}

	}
	
	/**
    * layout visual elements on the MonitorTabView on initialisation
    */
	private function draw(){
		
		bkg_pnl = this.attachMovie("Panel", "bkg_pnl", this.getNextHighestDepth());

		gridLayer = this.createEmptyMovieClip("_gridLayer_mc", this.getNextHighestDepth());
		
		transitionLayer = this.createEmptyMovieClip("_transitionLayer_mc", this.getNextHighestDepth());
		activityComplexLayer = this.createEmptyMovieClip("_activityComplexLayer_mc", this.getNextHighestDepth());
		activityLayer = this.createEmptyMovieClip("_activityLayer_mc", this.getNextHighestDepth(),{_y:learnerMenuBar._height});
		
		// creates learner icon on initial draw
		_learnerContainer_mc = this.createEmptyMovieClip("_learnerContainer_mc", this.getNextHighestDepth());
		
		var s:Object = mm.getSize();
		
		setStyles();
		
		dispatchEvent({type:'load',target:this});
	}
			
	private function hideMainExp(mm:MonitorModel):Void{
		mm.broadcastViewUpdate("EXPORTSHOWHIDE", true);
		mm.broadcastViewUpdate("EDITFLYSHOWHIDE", true);
	}
	
	/**
	 * Reloads the learner Progress and 
	 * @Param isChanged Boolean Value to pass it to setIsProgressChanged in monitor model so that it sets it to true if refresh button is clicked and sets it to fasle as soon as latest data is loaded and design is redrawn.
	 * @usage   
	 * @return  nothing
	 */
	private function reloadProgress(isChanged:Boolean){
			var s:Object = mm.getSize();
			drawDesignCalled = undefined;
			
			//Remove all the movies drawn on the transition and activity movieclip holder
			this._learnerContainer_mc.removeMovieClip();
			this.transitionLayer.removeMovieClip();
			this.activityLayer.removeMovieClip();
			
			//Recreate both Transition holder and Activity holder Movieclips
			transitionLayer = this.createEmptyMovieClip("_transitionLayer_mc", this.getNextHighestDepth());
			activityLayer = this.createEmptyMovieClip("_activityLayer_mc", this.getNextHighestDepth(),{_y:learnerMenuBar._height});
			_learnerContainer_mc = this.createEmptyMovieClip("_learnerContainer_mc", this.getNextHighestDepth());
			
			if (isChanged == false){
				mm.setIsProgressChangedSequence(false);
				
			} else {
				mm.setIsProgressChangedLesson(true);
				mm.setIsProgressChangedLearner(true);
			}
			
			mm.transitionsDisplayed.clear();
			mm.activitiesDisplayed.clear();
			
			mm.getMonitor().getProgressData(mm.getSequence());
	}
	
	/**
	 * Remove the activityies from screen on selection of new lesson
	 * 
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	private function removeActivity(a:Activity,mm:MonitorModel){
		//dispatch an event to show the design  has changed
		var r = mm.activitiesDisplayed.remove(a.activityUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		
	}
	
	/**
	 * Remove the transitions from screen on selection of new lesson
	 * 
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	private function removeTransition(t:Transition,mm:MonitorModel){
		var r = mm.transitionsDisplayed.remove(t.transitionUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		return s;
	}
	
	/**
	 * Draws new activity to monitor tab view stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity,mm:MonitorModel):Boolean{
		
		var mtv = MonitorTabView(this);
		var mc = getController();
		var newActivity_mc = null;
		
		Debugger.log("activityTypeID: " + a.activityTypeID,Debugger.CRITICAL,'drawActivity','MonitorTabView');
		
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGroupActivity() ){
			newActivity_mc = activityLayer.createChildAtDepth("CanvasActivity",DepthManager.kBottom,{_activity:a,_monitorController:mc, _monitorView:mtv, _module:"monitoring", learnerContainer:_learnerContainer_mc});
		} else if (a.isGateActivity()){
			newActivity_mc = activityLayer.createChildAtDepth("CanvasGateActivity",DepthManager.kBottom,{_activity:a,_monitorController:mc, _monitorView:mtv, _module:"monitoring"});
		} else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			var children:Array = mm.getMonitor().ddm.getComplexActivityChildren(a.activityUIID);
			newActivity_mc = activityLayer.createChildAtDepth("CanvasParallelActivity",DepthManager.kBottom,{_activity:a,_children:children, _monitorController:mc,_monitorTabView:mtv,fromModuleTab:"monitorMonitorTab",learnerContainer:_learnerContainer_mc});
		} else if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			var children:Array = mm.getMonitor().ddm.getComplexActivityChildren(a.activityUIID);
			newActivity_mc = activityComplexLayer.createChildAtDepth("CanvasOptionalActivity",DepthManager.kBottom,{_activity:a,_children:children,_monitorController:mc,_monitorTabView:mtv,fromModuleTab:"monitorMonitorTab",learnerContainer:_learnerContainer_mc});	
		} else if(a.isBranchingActivity()){	
			var newActivity_mc = activityLayer.createChildAtDepth("CanvasActivity",DepthManager.kBottom,{_activity:a,_monitorController:mc, _monitorView:mtv, _module:"monitoring", learnerContainer:_learnerContainer_mc});
		} else {
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','MonitorTabView');
		}
		
		var actItems:Number = mm.activitiesDisplayed.size()
		
		if (actItems < mm.getActivityKeys().length && newActivity_mc != null){
			mm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
		}
		
		mm.getMonitor().getMV().getMonitorSequenceScp().redraw(true);
		
		return true;
	}
	
	/**
	* Gets the Y Coordinate of the lowest activity that will be displayed on the canvas from the DesignDataModel
	* 
	* @usage   
	* @param    
	* @return  The Y Coordinate
	*/
	private function getLowestActivityYCoord():Number {
			
		var lowestActivity:Number = 0; //lowest position in layout
		var activeSeq:Sequence = MonitorModel(getModel()).getSequence();
		var activitiesHash:Hashtable = activeSeq.getLearningDesignModel().activities;
		var activityKeys:Array = activitiesHash.keys();
		
		for(var i=0; i < activityKeys.length; i++) {
			
			if (activitiesHash.get(activityKeys[i]).yCoord  > lowestActivity) {
				lowestActivity = activitiesHash.get(activityKeys[i]).yCoord;
			}
		} 
		Debugger.log("Lowest activity Y Coord from DDM: "+activitiesHash.get(activityKeys[i]).yCoord, Debugger.GEN, 'getLowestActivityYCoord', 'MonitorTabView');
		return lowestActivity;
	}
	
	/**
	 * Add to canvas stage but keep hidden from view.
	 * 
	 * @usage   
	 * @param   a  
	 * @param   cm 
	 * @return  true if successful
	 */
	
	private function hideActivity(a:Activity, mm:MonitorModel):Boolean {
		if (a.isSystemGateActivity()){
			var newActivityObj = new Object();
			newActivityObj.activity = a;
			
			mm.activitiesDisplayed.put(a.activityUIID,newActivityObj);
			
			Debugger.log('Gate activity a.title:'+a.title+','+a.activityUIID+' added (hidden) to the cm.activitiesDisplayed hashtable:'+newActivityObj,4,'hideActivity','CanvasView');
		}
		
		return true;
	}
	
	/**
	 * Draws a transition on the Monitor Tab View.
	 * @usage   
	 * @param   t  The transition to draw
	 * @param   mm  the Monitor Model.
	 * @return  
	 */
	private function drawTransition(t:Transition,mm:MonitorModel):Boolean{
		var mtv = MonitorTabView(this);
		var mc = getController();
		
		var newTransition_mc:MovieClip = transitionLayer.createChildAtDepth("MonitorTransition",DepthManager.kTop,{_transition:t,_monitorController:mc,_monitorTabView:mtv});
		
		var trnsItems:Number = mm.transitionsDisplayed.size()
		if (trnsItems < mm.getTransitionKeys().length){
			mm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		}
		
		Debugger.log('drawn a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'drawTransition','MonitorTabView');
		return true;
		
	}
	
	/**
	 * Hides a transition on the canvas.
	 * 
	 * @usage   
	 * @param   t  The transition to hide
	 * @param   cm  The canvas model
	 * @return  true if successful
	 */
	
	private function hideTransition(t:Transition, mm:MonitorModel):Boolean{
		var mtv = MonitorTabView(this);
		var mc = getController();
		
		var newTransition_mc:MovieClip = transitionLayer.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t,_monitorController:mc,_monitorTabView:mtv, _visible:false});
	
		mm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		Debugger.log('drawn (hidden) a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'hideTransition','CanvasView');
		
		return true;
	}
	
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 */
	private function setStyles():Void{
		var styleObj = _tm.getStyleObject('CanvasPanel');
		bkg_pnl.setStyle('styleName',styleObj);
	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{

		var activitySpan:Number = getLowestActivityYCoord();
        var verticalSpacing:Number = 100;
		var s:Object = mm.getSize();
		
		Debugger.log("Setting canvas size, activitySpan: "+activitySpan, Debugger.CRITICAL, 'setSize', 'MonitorTabView');
		
		if (s.h > activitySpan) {
			bkg_pnl.setSize(s.w, s.h);
			var grid_mc = Grid.drawGrid(gridLayer,Math.round(s.w),Math.round(s.h),V_GAP,H_GAP);
		} 
		else {
			bkg_pnl.setSize(s.w,activitySpan + verticalSpacing);
			
			//Create the grid.  The grid is re-drawn each time the canvas is resized.
			var grid_mc = Grid.drawGrid(gridLayer,Math.round(s.w),Math.round(activitySpan + verticalSpacing), V_GAP, H_GAP);
		}		
			
		mm.getMonitor().getMV().getMonitorSequenceScp().redraw(true);
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
        var p:Object = mm.getPosition();
		trace("X pos set in Model is: "+p.x+" and Y pos set in Model is "+p.y)
        this._x = p.x;
        this._y = p.y;
		
	}
	
	public function getLearnerIcon():MovieClip {
		return _learnerContainer_mc;
	}
	
	public function get ddm():DesignDataModel {
		return mm.getMonitor().ddm;
	}
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
}