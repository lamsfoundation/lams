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

import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.ComplexActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasActivity;
//import org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity;
import org.lamsfoundation.lams.authoring.Transition;
//import org.lamsfoundation.lams.authoring.cv.*;
import mx.managers.*
import mx.containers.*;
import mx.events.*
import mx.utils.*
import mx.controls.TabBar;


/**
*Monitoring Tab view for the Monitor
* Reflects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.tabviews.MonitorTabView extends AbstractView{
	public static var _tabID:Number = 1;
	private var _className = "MonitorTabView";
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	private var drawDesignCalled:String;
	
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	private var _monitorTabView:MonitorTabView;
	
	//Canvas clip
	//private var _monitor_mc:MovieClip;
	private var monitorTabs_tb:MovieClip;
	private var _monitorTabViewContainer_mc:MovieClip
	private var bkg_pnl:MovieClip;
	private var _gridLayer_mc:MovieClip;
	private var _transitionLayer_mc:MovieClip;
	private var _activityLayerComplex_mc:MovieClip;
	private var _activityLayer_mc:MovieClip;
	
	//private var _transitionPropertiesOK:Function;
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
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
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		mm = MonitorModel(model)
        //Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;
		//drawDesignCalled = false;
		MovieClipUtils.doLater(Proxy.create(this,draw)); 
		
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
				case 'TABCHANGE' :
					if (infoObj.tabID == _tabID){
						this._visible = true;
						trace("TabID for Selected tab is (TABCHANGE): "+infoObj.tabID)
						if (mm.activitiesDisplayed.isEmpty()){
							mm.getMonitor().openLearningDesign(mm.getSequence());
						}else if (drawDesignCalled == undefined){
							drawDesignCalled = "called";
							mm.drawDesign(infoObj.tabID);
						}
					}else {
						this._visible = false;
					}
					break;
				case 'PROGRESS' :
					if (infoObj.tabID == _tabID){
						mm.getMonitor().getProgressData(mm.getSequence())
					}
					break;
				case 'DRAW_ACTIVITY' :
					if (infoObj.tabID == _tabID){
						trace("DRAWING_ACTIVITY")
						drawActivity(infoObj.data, mm)
						//MovieClipUtils.doLater(Proxy.create(this,draw));
					}
					break;
				case 'DRAW_TRANSITION' :
					if (infoObj.tabID == _tabID){
						trace("DRAWING_Transition")
						drawTransition(infoObj.data, mm)
					}
					
				case 'REMOVE_ACTIVITY' :
					//if (infoObj.tabID == _tabID){
						trace("REMOVE_ACTIVITY")
						removeActivity(infoObj.data, mm)
						//MovieClipUtils.doLater(Proxy.create(this,draw));
					//}
					break;
				
				case 'REMOVE_TRANSITION' :
					//if (infoObj.tabID == _tabID){
						trace("REMOVE_ACTIVITY")
						removeTransition(infoObj.data, mm)
						//MovieClipUtils.doLater(Proxy.create(this,draw));
					//}
					break;
				
				case 'DRAW_DESIGN' :
					if (infoObj.tabID == _tabID){
						drawDesignCalled = "called";
						trace("TabID for Selected tab is (MonitorTab): "+infoObj.tabID)
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

		//set up the 
		//_canvas_mc = this;
		_gridLayer_mc = this.createEmptyMovieClip("_gridLayer_mc", this.getNextHighestDepth());
		_transitionLayer_mc = this.createEmptyMovieClip("_transitionLayer_mc", this.getNextHighestDepth());
		_activityLayerComplex_mc = this.createEmptyMovieClip("_activityLayerComplex_mc", this.getNextHighestDepth());
		_activityLayer_mc = this.createEmptyMovieClip("_activityLayer_mc", this.getNextHighestDepth());
			
		trace("Loaded MonitorTabView Data"+ this)
		//setSize (mm)
		dispatchEvent({type:'load',target:this});
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
		trace("in removeActivity")
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
		//Debugger.log('t.uiID:'+t.transitionUIID,Debugger.CRITICAL,'removeTransition','CanvasView');
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
		Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','MonitorTabView');
		var s:Boolean = false;
		
		var mtv = MonitorTabView(this);
		
		var mc = getController();
		
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGateActivity() || a.isGroupActivity() ){
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a,_monitorController:mc,_monitorTabView:mtv, _module:"monitoring"});
		}
		if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			//get the children
			var children:Array = mm.getMonitor().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children,_monitorController:mc,_monitorTabView:mtv,fromModuleTab:"monitorMonitorTab"});
		}
		if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			var children:Array = mm.getMonitor().ddm.getComplexActivityChildren(a.activityUIID);
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_activity:a,_children:children,_monitorController:mc,_monitorTabView:mtv,fromModuleTab:"monitorMonitorTab"});	
		}else{
			//Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','MonitorTabView');
		}
		
		var actItems:Number = mm.activitiesDisplayed.size()
		if (actItems < mm.getActivityKeys().length){
			mm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
		}
		s = true;
		//mm.getMonitor().getMV().getMonitorScp().redraw(true); 
		return s;
	}
	
	/**
	 * Draws a transition on the Monitor Tab View.
	 * @usage   
	 * @param   t  The transition to draw
	 * @param   mm  the Monitor Model.
	 * @return  
	 */
	private function drawTransition(t:Transition,mm:MonitorModel):Boolean{
		var s:Boolean = true;
		
		var mtv = MonitorTabView(this);
		
		var mc = getController();
		
		var newTransition_mc:MovieClip = _transitionLayer_mc.createChildAtDepth("MonitorTransition",DepthManager.kTop,{_transition:t,_monitorController:mc,_monitorTabView:mtv});
		
		var trnsItems:Number = mm.transitionsDisplayed.size()
		if (trnsItems < mm.getTransitionKeys().length){
			mm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		}
		
		Debugger.log('drawn a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'drawTransition','MonitorTabView');
		return s;
		
	}
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		trace("Monitor Tab Grid Width: "+s.w+" Monitor Tab Grid Height: "+s.h);
		//monitor_scp.setSize(s.w,s.h);
		bkg_pnl.setSize(s.w,s.h);
		//Create the grid.  The grid is re-drawn each time the canvas is resized.
		var grid_mc = Grid.drawGrid(_gridLayer_mc,Math.round(s.w-10),Math.round(s.h-10),V_GAP,H_GAP);
				
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
	
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():MonitorController{
		var c:Controller = super.getController();
		return MonitorController(c);
	}
	
	public function getMonitorTab():MovieClip{
		return monitorTabs_tb;
	}
	 /*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
}