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

class org.lamsfoundation.lams.monitoring.mv.tabviews.LearnerTabView extends AbstractView{
	public static var _tabID:Number = 2;
	private var _className = "LearnerTabView";
	//constants:
	private var GRID_HEIGHT:Number;
	private var GRID_WIDTH:Number;
	private var H_GAP:Number;
	private var V_GAP:Number;
	private var ACT_X:Number = 0;
	private var ACT_Y:Number = 40;
	private var xOffSet:Number = 10;
	private var actWidth:Number = 120;
	private var actLenght:Number = 0;
	private var activeLearner:Number;
	private var prevLearner:Number;
	private var learnersDrawn:Number;
	private var learnerListArr:Array;
	
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	private var _learnerTabView:LearnerTabView;

	
	//Canvas clip
	
	private var _learnerTabViewContainer_mc:MovieClip
	private var bkg_pnl:MovieClip;
	private var learnerMenuBar:MovieClip;
	//private var _helpLayer_mc:MovieClip;
	private var _gridLayer_mc:MovieClip;
	private var _learnersLayer_mc:MovieClip;
	private var _activityLayerComplex_mc:MovieClip;
	private var _activityLayer_mc:MovieClip;
	private var completed_mc:MovieClip;
	private var refresh_btn:Button;
	private var help_btn:Button;
	
	//private var _transitionPropertiesOK:Function;
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function LearnerTabView(){
		_learnerTabView = this;
		_learnerTabViewContainer_mc = this;
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
							break;
						}
						trace("learnerDrawn are: "+learnersDrawn)
						trace("all learner progress length is: "+mm.allLearnersProgress.length)
						if (learnersDrawn != mm.allLearnersProgress.length){
							drawAllLearnersDesign(mm, infoObj.tabID)
						}
						if(mm.getIsProgressChanged()){
							reloadProgress(false);
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
						drawActivity(infoObj.data, mm, infoObj.learner)
						//MovieClipUtils.doLater(Proxy.create(this,draw));
					}
					break;
				case 'REMOVE_ACTIVITY' :
					if (infoObj.tabID == _tabID){
						trace("REMOVE_ACTIVITY")
						removeActivity(infoObj.data, mm)
						//MovieClipUtils.doLater(Proxy.create(this,draw));
					}
					break;
				
				case 'DRAW_DESIGN' :
					if (infoObj.tabID == _tabID){
						trace("TabID for Selected tab is (LearnerTab): "+infoObj.tabID)
						drawAllLearnersDesign(mm, infoObj.tabID)
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
		//activityArr = new Array;
		//bkg_pnl = this.attachMovie("Panel", "bkg_pnl", this.getNextHighestDepth());

		//set up the Movie Clips to load relevant  

		//_helpLayer_mc = this.attachMovie("RefreshBar", "RefreshBar", _helpLayer_mc.getNextHighestDepth()) 
		learnerMenuBar = this.attachMovie("RefreshBar", "RefreshBar", this.getNextHighestDepth())
		learnerMenuBar = eval(learnerMenuBar)
		//_helpLayer_mc.createEmptyMovieClip("_helpLayer_mc", this.getNextHighestDepth());
		
		//refresh_btn.addEventListener("click", Delegate.create(this, reloadProgress));
		_learnersLayer_mc = this.createEmptyMovieClip("_learnersLayer_mc", this.getNextHighestDepth());
		_gridLayer_mc = this.createEmptyMovieClip("_gridLayer_mc", this.getNextHighestDepth());
		
		_activityLayerComplex_mc = this.createEmptyMovieClip("_activityLayerComplex_mc", this.getNextHighestDepth());
		_activityLayer_mc = this.createEmptyMovieClip("_activityLayer_mc", this.getNextHighestDepth(),{_y:learnerMenuBar._height});
		
		learnerMenuBar.refresh_btn.onRelease = Proxy.create (this, reloadProgress);
		trace("Help layer path: "+ learnerMenuBar.refresh_btn.label)
		//setSize (mm)
		dispatchEvent({type:'load',target:this});
	}
	
	
	
	//private function initEventListeners(){
		//refresh_btn.addEventListener("click", Delegate.create(this, reloadProgress));
	//}
	/**
	* Sets last selected Sequence
	*/
	private function setPrevLearner(learner:Number):Void{
		prevLearner = learner;
	}
	
	/**
	* Gets last selected Sequence
	*/
	private function getPrevLearner():Number{
		return prevLearner;
	}
	
	/*
	* Clear Method to clear movies from scrollpane
	* 
	*/
	private function clearLearnersData(array:Array):Array{
		if(array != null){
			for (var i=0; i <array.length; i++){
				array[i].removeMovieClip();
			}
		}
		array = new Array();
	return array;
	}
	
	/**
	 * Reloads the learner Progress and 
	 * @Param isChanged Boolean Value to pass it to setIsProgressChanged in monitor model so 		that it sets it to true if refresh button is clicked and sets it to fasle as soon as latest data is loaded and design is redrawn.
	 * @usage   
	 * @return  nothing
	 */
	private function reloadProgress(isChanged:Boolean){
		
		//if (learnersDrawn != mm.allLearnersProgress.length){
			trace("reloading Progress data for Learners")
			learnersDrawn = 0;
			ACT_X = 0;
			ACT_Y = 35;
			//for(var i=0; i<_activityLayer_mc.children.length;i++){
				_activityLayer_mc.removeMovieClip();
				_activityLayer_mc = this.createEmptyMovieClip("_activityLayer_mc", this.getNextHighestDepth(),{_y:learnerMenuBar._height});
			//}
			if (isChanged == false){
				mm.setIsProgressChanged(false);
				
			}else {
				mm.setIsProgressChanged(true);
			}
			mm.getMonitor().getProgressData(mm.getSequence());
			
		//}
		
	}
	private function drawAllLearnersDesign(mm:MonitorModel, tabID:Number){
		//learnerListArr = clearLearnersData(learnerListArr)
		trace("activity layer height: "+_activityLayer_mc._height)
		for (var j=0; j<mm.allLearnersProgress.length; j++){
			learnersDrawn = j+1
			ACT_X = 0;
			ACT_Y = (j*80)+35;
			mm.drawDesign(tabID, mm.allLearnersProgress[j]);
			
			//ACT_Y = Math.floor(_activityLayer_mc._y + _activityLayer_mc._height);
			//Math.floor(ACT_Y);
		}
	}
	
	/*public function compareProgressData(learner, activityID):String{
		trace ("activity ID passed is: "+activityID)
		trace("Number of Activities completed in the lesson are: "+learner.getCompletedActivities().length)
		
		var arrLearnerProgComp = learner.getCompletedActivities()
		for (var i=0; i<arrLearnerProgComp.length; i++){
			if (activityID == arrLearnerProgComp[i]){
				var clipName:String = "completed_mc";
				return clipName;
			}
		
		}
		
		var arrLearnerProgAttempt = learner.getAttemptedActivities()
		trace("Attempted activities are: "+arrLearnerProgAttempt.length)
		for (var j=0; j<arrLearnerProgAttempt.length; j++){
			trace("Activity Id Passed is "+activityID+" and attempted ID is "+arrLearnerProgAttempt[j])
			if (activityID == arrLearnerProgAttempt[j]){
				if (activityID == learner.getCurrentActivityId()){
					var clipName:String = "current_mc";
					return clipName;
				}else {
					var clipName:String = "attempted_mc";
					return clipName;
				}
			}
			
		}
		//arrLearnerProg = learner.getCurrentActivityId()
		if (activityID == learner.getCurrentActivityId()){
			var clipName:String = "current_mc";
			return clipName;
		}
	}*/
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
	 * Draws new activity to monitor tab view stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity,mm:MonitorModel, learner:Object):Boolean{
		
		Debugger.log('The activity:'+a.title+','+a.activityTypeID+' is now be drawn',Debugger.CRITICAL,'drawActivity','LearnerTabView');
		if (ACT_X == 0){
			 var z:Object = mm.getSize();
		trace("Monitor Tab Grid Width: "+s.w+" Monitor Tab Grid Height: "+s.h);
			_activityLayer_mc.createTextField("learnerName"+learner.getLearnerId(), _activityLayer_mc.getNextHighestDepth(), ACT_X, ACT_Y, z.w, 20);
			var learnerName_txt = _activityLayer_mc["learnerName"+learner.getLearnerId()];
			trace("Learnermername field is: "+learnerName_txt)
			var nameTextFormat = new TextFormat();
			nameTextFormat.bold = true;
			nameTextFormat.font = "Verdana";
			nameTextFormat.size = 11;
			learnerName_txt.border = true;
			learnerName_txt.selectable = false;
			learnerName_txt.background = true;
			learnerName_txt.backgroundColor = 0xCCCCCC;
			learnerName_txt.setNewTextFormat(nameTextFormat);
			learnerName_txt.text = "\t"+learner.getUserName()
			trace("Ypos for name field is: "+ACT_Y)
		}
		var s:Boolean = false;
		
		var ltv = LearnerTabView(this);
		
		var mc = getController();
		
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGateActivity() || a.isGroupActivity() ){
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("LearnerActivity", DepthManager.kTop,{_activity:a,_controller:mc,_view:ltv, _x:ACT_X, _y:ACT_Y+40, actLabel:a.title, learner:learner});
			ACT_X = newActivity_mc._x + newActivity_mc._width;
		}else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE || a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			//get the children
			var children:Array = mm.getMonitor().ddm.getComplexActivityChildren(a.activityUIID);
			
			var newActivity_mc = _activityLayer_mc.createChildAtDepth("LearnerComplexActivity",DepthManager.kTop,{_activity:a,_children:children,_controller:mc,_view:ltv, _x:ACT_X, _y:ACT_Y+40, learner:learner});
			ACT_X = newActivity_mc._x + newActivity_mc._width;
		}else{
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','LearnerTabView');
		}
		
		var actItems:Number = mm.activitiesDisplayed.size()
		if (actItems < mm.getActivityKeys().length){
			mm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
		}
		s = true;
		//actLenght++;
		//mm.getMonitor().getMV().getMonitorScp().redraw(true);
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
		learnerMenuBar.help_btn._x = s.w - 80
		//Create the grid.  The grid is re-drawn each time the canvas is resized.
		//var grid_mc = Grid.drawGrid(_gridLayer_mc,Math.round(s.w-10),Math.round(s.h-10),V_GAP,H_GAP);
				
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
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new MonitorController(model);
    }
	
}