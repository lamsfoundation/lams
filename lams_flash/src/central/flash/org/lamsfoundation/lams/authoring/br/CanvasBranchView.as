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
import org.lamsfoundation.lams.common.Dialog;
import org.lamsfoundation.lams.common.ToolTip;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.br.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.CommonCanvasView;
import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.monitoring.mv.MonitorModel;
import org.lamsfoundation.lams.monitoring.mv.MonitorController;
import org.lamsfoundation.lams.monitoring.mv.tabviews.MonitorTabView;

import mx.controls.*;
import mx.containers.*;
import mx.managers.*;
import mx.transitions.TransitionManager;
import mx.utils.*;

/**
*Authoring view for the canvas
* Relects changes in the CanvasModel
*/

class org.lamsfoundation.lams.authoring.br.CanvasBranchView extends CommonCanvasView {	
	
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
	
	private var _container:MovieClip;  //The container window that holds the dialog
    
	private var _cm:CanvasModel;
	private var _mm:MonitorModel;
	
    private var _canvasBranchView:CanvasBranchView;
	private var _canvasBranchingActivity:CanvasActivity;
	
	private var canvas_scp:ScrollPane;
	
	private var startTransX:Number;
	private var startTransY:Number;
	
	private var lastScreenWidth:Number = 500;
	private var lastScreenHeight:Number = 300;

	private var grid_mc:Object;
	public static var hSpace:Number = 30;
	public static var vSpace:Number = 30;
	
	private var _binLayer_mc:MovieClip;

	private var currentActivity_x:Number;
	private var currentActivity_y:Number;

	private var close_mc:MovieClip;
	
	// connector hubs for branch connections into and out of sequence activities
	private var cHubStart_mc:MovieClip;
	private var cHubEnd_mc:MovieClip;
	
	private var _branchLayer:MovieClip;
	private var _defaultSequenceActivity:SequenceActivity;
	private var _fingerprint:MovieClip;
	
	private var _learnerContainer_mc:MovieClip;
	private var _labelContainer_mc:MovieClip;
	
	private var _open:Boolean;
	private var _isOpen:Boolean;
	private var _isBranchChild:Boolean;
	private var _eventsEnabled:Boolean;
	
	/**
	* Constructor
	*/
	function CanvasBranchView(){
		_canvasBranchView = this;
		
		currentActivity_x = null;
		currentActivity_y = null;
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		defaultSequenceActivity = null;
		fingerprint = null;
		_eventsEnabled = true;
		
		//Init for event delegation
        mx.events.EventDispatcher.initialize(this);
		
		this._visible = false;
		
	}
    
	/**
	* Called to initialise Canvas  . Called by the Canvas container
	*/
	public function init(m:Observable, c:Controller){

		super(m, c);
		
		//Set up parameters for the grid
		H_GAP = 10;
		V_GAP = 10;

		_cm = (m instanceof CanvasModel) ? CanvasModel(m) : null;
		_mm = (m instanceof MonitorModel) ? MonitorModel(m) : null;
		
		Debugger.log('_cm:'+_cm, Debugger.CRITICAL,'init','CanvasBranchView');
		Debugger.log('_mm:'+_mm, Debugger.CRITICAL,'init','CanvasBranchView');
	    
		//register to recive updates form the model
		
		if(_cm != null) _cm.addEventListener('viewUpdate', Proxy.create(this, viewUpdate));
		if(_mm != null) _mm.addEventListener('viewUpdate', Proxy.create(this, viewUpdate));
		
		if (_cm != null) _isBranchChild = (_cm.activeView instanceof CanvasBranchView);
		if (_mm != null) _isBranchChild = (_mm.activeView instanceof CanvasBranchView);
		
		_isOpen = false;
		
		MovieClipUtils.doLater(Proxy.create(this, draw)); 
    }   
	
	/**
    * set the container reference to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
    
	/**
	 * Recieved update events from the MonitorModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function update (o:Observable, infoObj:Object):Void{
		   var mm:MonitorModel = MonitorModel(o);
		   infoObj.target = mm;
		   
		   viewUpdate(infoObj);
	}
	
	/**
	 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function viewUpdate(event:Object):Void{
		Debugger.log('Received an Event dispatcher UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','CanvasBranchView');
		
		if(!_eventsEnabled && event.updateType != 'SET_ACTIVE' && event.updateType != 'SIZE') {
			Debugger.log('Events disabled.'+event.target,4,'viewUpdate','CanvasBranchView');
			return;
		}
		
		var _model = event.target;
		
		Debugger.log("_model instance: " + (_model instanceof MonitorModel), Debugger.CRITICAL, "viewUpdate", "CanvasBranchView");
		
		var isCanvasModel:Boolean;
		var isMonitorModel:Boolean;
		
		isMonitorModel = model instanceof MonitorModel;
		isCanvasModel = model instanceof CanvasModel;
		
		Debugger.log('isCanvasModel:'+isCanvasModel, Debugger.CRITICAL,'viewUpdate','CanvasBranchView');
		Debugger.log('isMonitorModel:'+isMonitorModel,Debugger.CRITICAL,'viewUpdate','CanvasBranchView');

		
		switch (event.updateType){ 
			case 'POSITION':
                setPosition(_model);
                break;
            case 'SIZE':
                setSize(_model);
                break;
			case 'ADD_SEQUENCE':
				var result = addSequence(event.data, _model);
				Debugger.log("addSequence: " + result, Debugger.CRITICAL, "viewUpdate", "CanvasModel");
				break;
            case 'DRAW_ACTIVITY_SEQ':
                drawActivity(event.data, _model);
                break;
			case 'HIDE_ACTIVITY':
                hideActivity(event.data, _model);
                break;
            case 'REMOVE_ACTIVITY':
                removeActivity(event.data, _model);
                break;
            case 'DRAW_TRANSITION':
                drawTransition(event.data, _model);
				break;
			case 'DRAW_BRANCH':
				drawBranch(event.data, _model);
				break;
			case 'REMOVE_BRANCH':
				removeBranch(event.data, _model);
				break;
			case 'HIDE_TRANSITION':
                hideTransition(event.data, _model);
				break;
			case 'REMOVE_TRANSITION':
				removeTransition(event.data, _model);
				break;
			case 'SELECTED_ITEM':
                highlightActivity(_model);
                break;
			case 'DRAW_ALL' :
				drawAll(event.data, _model);
				break;
			case 'SET_ACTIVE':
				Debugger.log('setting activie :' + event.updateType + " event.data: " + event.data.activity.activityUIID + " condition: " + (event.data.activity.activityUIID == this.activity.activityUIID),Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasBranchView');
				Debugger.log('this object :' + this.activity.activityUIID,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasBranchView');
				
				transparentCover._visible = (event.data == this) ? false : true;
				
				Debugger.log("is open: " + _isOpen, Debugger.CRITICAL, "viewUpdate", "CanvasBranchView");
				
				if(event.data.activity.activityUIID == this.activity.activityUIID) {
					_eventsEnabled = true;
					
					getController().activityClick(this.startHub);
					getController().activityRelease(this.startHub);
				} else {
					_eventsEnabled = false;
				}
				
				break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasBranchView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		
		//get the content path for the sp
		content = canvas_scp.content;
		
		bkg_pnl = content.createClassObject(Panel, "bkg_pnl", getNextHighestDepth());
		
		//set up the layers
		gridLayer = content.createEmptyMovieClip("_gridLayer_mc", content.getNextHighestDepth());
		transitionLayer = content.createEmptyMovieClip("_transitionLayer_mc", content.getNextHighestDepth());
		branchLayer = content.createEmptyMovieClip("_branchLayer_mc", content.getNextHighestDepth());
		_labelContainer_mc = content.createEmptyMovieClip("_labelContainer_mc", content.getNextHighestDepth());
		
		activityComplexLayer = content.createEmptyMovieClip("_activityComplexLayer_mc", content.getNextHighestDepth());
		activityLayer = content.createEmptyMovieClip("_activityLayer_mc", content.getNextHighestDepth());
		
		_learnerContainer_mc = content.createEmptyMovieClip("_learnerContainer_mc", content.getNextHighestDepth());
		
		transparentCover = content.createClassObject(Panel, "_transparentCover_mc", content.getNextHighestDepth(), {_visible: false, enabled: true, _alpha: 50});
		transparentCover.onPress = Proxy.create(this, onTransparentCoverClick);
		
		binLayer = content.createEmptyMovieClip("_binLayer_mc", content.getNextHighestDepth());
		
		complexViewer = content.createEmptyMovieClip("_complex_viewer_mc", content.getNextHighestDepth());
		branchContent = content.createEmptyMovieClip("_branch_content_mc", DepthManager.kTopmost);
		
		bkg_pnl.onRelease = function(){
			Application.getInstance().getCanvas().getCanvasView().getController().canvasRelease(this);
		}
		
		bkg_pnl.useHandCursor = false;
		
		close_mc = content.attachMovie("collapse_mc", "close_mc", DepthManager.kTop);
		
		close_mc.onRelease = Proxy.create(this, localOnRelease);
		close_mc.onReleaseOutside = Proxy.create(this, localOnReleaseOutside);
		close_mc.onRollOver = Proxy.create(this, this['showToolTip'], close_mc, "close_mc_tooltip");
		close_mc.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		setupConnectorHubs();
		loadSequenceActivities();
		
		setStyles();
		setSize(model);
		
		if(_open) {
			this._visible = true;
		
			//Dispatch load event 
			dispatchEvent({type:'load',target:this});
		}
		
		if(model.isDirty)
			model.refreshDesign();
	}
	
	private function setupConnectorHubs() {
		
		Debugger.log('adding hubs for branch activity:' + _canvasBranchingActivity,Debugger.CRITICAL,'setupConenctorHubs','org.lamsfoundation.lams.CanvasBranchView');
		
		var _canvasSize:Object = model.getSize();
		
		var _startx = CanvasActivity.HUB_CONNECTOR_MARGIN;
		var _starty = ((_canvasSize.h - 2*CanvasBranchView.vSpace)/2) - CanvasActivity.BRANCH_ICON_HEIGHT;
		
		var _endx = (_canvasSize.w - 2*CanvasBranchView.hSpace) - CanvasActivity.HUB_CONNECTOR_MARGIN - CanvasActivity.BRANCH_ICON_WIDTH;
		var _endy = ((_canvasSize.h - 2*CanvasBranchView.vSpace)/2) - CanvasActivity.BRANCH_ICON_HEIGHT;

		var hubStartDir_x = (ApplicationParent.isRTL()) ? _endx : _startx;
		var hubEndDir_x = (ApplicationParent.isRTL()) ?  _startx : _endx;
		
		// start-point connector hub
		cHubStart_mc = (model instanceof CanvasModel) ? activityLayer.createChildAtDepth("CanvasBranchingConnectorStart",DepthManager.kTop,{_activity: activity, _canvasController:getController(), _canvasBranchView:_canvasBranchView, _x: hubStartDir_x , _y: _starty, branchConnector:true})
												  : activityLayer.createChildAtDepth("CanvasBranchingConnectorStart",DepthManager.kTop,{_activity: activity, _monitorController:getController(), _canvasBranchView:_canvasBranchView, _x: activity.startXCoord , _y: activity.startYCoord, branchConnector:true});
		// end-point connector hub
		cHubEnd_mc = (model instanceof CanvasModel) ? activityLayer.createChildAtDepth("CanvasBranchingConnectorEnd",DepthManager.kTop,{_activity: activity, _canvasController:CanvasController(getController()), _canvasBranchView:_canvasBranchView, _x: hubEndDir_x , _y: _endy, branchConnector:true})
												: activityLayer.createChildAtDepth("CanvasBranchingConnectorEnd",DepthManager.kTop,{_activity: activity, _monitorController:MonitorController(getController()), _canvasBranchView:_canvasBranchView, _x: activity.endXCoord , _y: activity.endYCoord, branchConnector:true});
	
		this.onEnterFrame = hitConnectorHubs;
		
	}
	
	private function hitConnectorHubs() {
		delete this.onEnterFrame;
		
		startHub.hit();
		endHub.hit();
	}
	
	private function loadSequenceActivities() {
		var sequenceActs:Array = (model instanceof CanvasModel) ? _cm.getCanvas().ddm.getComplexActivityChildren(activity.activityUIID) : _mm.getMonitor().ddm.getComplexActivityChildren(activity.activityUIID);
		Debugger.log("Sequence Activities length: " + sequenceActs.length, Debugger.CRITICAL, "loadSequenceActivities", "CanvasBranchView");
		
		model.haltRefresh(true);
		
		for(var i=0; i<sequenceActs.length;  i++) {
			Debugger.log('firstActivityUIID:' + activity.firstActivityUIID, Debugger.CRITICAL, "loadSequenceActivities", "CanvasBranchView");
			
			if(SequenceActivity(sequenceActs[i]).activityUIID == activity.firstActivityUIID) {
				// (monitoring) entering this if statement
				model.addNewBranch(SequenceActivity(sequenceActs[i]), activity, true);
			} else {
				model.addNewBranch(SequenceActivity(sequenceActs[i]), activity, false);
			}
			
		}
		
		
		if(defaultSequenceActivity == null)
			createInitialSequenceActivity(sequenceActs.length+1);
		
		model.haltRefresh(false);

	}
	
	private function createInitialSequenceActivity(num:Number) {
		model.createNewSequenceActivity(activity, num, null, true);
	}
	
	public function localOnRelease():Void{
		Debugger.log("close called", Debugger.CRITICAL, "localOnRelease", "CanvasBranchView");
		
		model.closeAllComplexViews();
		close();
	}
	
	public function open(doTransition:Boolean):Void {
		Debugger.log("calling open: " + _open, Debugger.CRITICAL, "open", " CanvasBranchView");
		if(model instanceof CanvasModel) model.getCanvas().addBin(this.binLayer);
			
		setSize(model);
		
		if(doTransition || doTransition == null) {
			var tm:TransitionManager = new TransitionManager(this);
			tm.startTransition({type:mx.transitions.Zoom, 
							direction:0, duration:1, easing:mx.transitions.easing.Bounce.easeOut});
			tm.addEventListener("allTransitionsInDone", finishedOpen);
		}
		
	}
	
	public function finishedOpen(evt:Object):Void {
		Debugger.log("evt target: " + evt.target, Debugger.CRITICAL, "finishedOpen", "CanvasBranchView");
		Debugger.log("evt content: " + evt.target.content, Debugger.CRITICAL, "finishedOpen", "CanvasBranchView");
		evt.target.content.isOpen = true;
		evt.target.content.loadLabels();
	}
	
	public function loadLabels():Void {
		var bkeys:Array = model.branchesDisplayed.keys();
		
		for(var i=0; i<bkeys.length; i++) {
			var bc = model.branchesDisplayed.get(bkeys[i]);
			if(bc.branch.sequenceActivity.parentUIID == this.activity.activityUIID) {
				if(bc.branch.direction != BranchConnector.DIR_TO_END)
					bc.createBranchLabel();
			}
			
		}
	}
	
	private function close():Void {
		if(model instanceof CanvasModel) model.getCanvas().hideBin(); //typo
		
		this.activity.clear = false;
		
		Debugger.log("this._canvasBranchingActivity: "+ this._canvasBranchingActivity, Debugger.CRITICAL, "close", "CanvasBranchView");
		
		this._canvasBranchingActivity.refresh();
		
		model.selectedItem = null;
		
		var bkeys:Array = model.branchesDisplayed.keys();
		
		for(var i=0; i<bkeys.length; i++) {
			var bc = model.branchesDisplayed.get(bkeys[i]);
			if(bc.branch.sequenceActivity.parentUIID == this.activity.activityUIID) {
				if(bc.branchLabel != null)
					bc.branchLabel.removeMovieClip();
			}
			
		}
		
		TransitionManager.start(this, {type:mx.transitions.Zoom, direction:1, duration:0.5, easing:mx.transitions.easing.Strong.easeIn});
		
		if(model instanceof CanvasModel) {
			model.getCanvas().closeBranchView();
		} else {
			model.getMonitor().getMV().getMonitorTabView().showAssets(true);
			model.getMonitor().closeBranchView();
		}
		
		Debugger.log("model.activeView :  " + model.activeView, Debugger.CRITICAL, "finishedClose", "CanvasBranchView");
		
		if(model instanceof CanvasModel) {
			if(model.activeView instanceof CanvasBranchView) model.getCanvas().addBin(model.activeView.binLayer);
			else model.getCanvas().addBin(model.activeView);
		}
		
		model.broadcastViewUpdate("SIZE");
		
		
		_isOpen = false;
		
		Debugger.log("closing branch view... : " + model.activeView.activity.activityUIID, Debugger.CRITICAL, "close", "CanvasBranchView");
		
	}
	
	public function localOnReleaseOutside():Void{
	}
	
	private function addSequence(a:SequenceActivity, cm):Boolean{
		if(a.parentUIID == activity.activityUIID) {
			if(a.firstActivityUIID == null)
				defaultSequenceActivity = a;
			
			var sequenceObj = new Object();
			sequenceObj.activity = a;
			
			cm.activitiesDisplayed.put(a.activityUIID, sequenceObj);
			
		} else { 
			return false;
		}
		
		Debugger.log("default sequence: " + defaultSequenceActivity.activityUIID, Debugger.CRITICAL, "addSequence", "CanvasBranchView");
			
		return true;
	}
	
	/**
	 * Draws new or replaces existing activity to canvas stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfull
	 */
	private function drawActivity(a:Activity, cm):Boolean {
		if(this.activity.activityUIID != ddm.getActivityByUIID(a.parentUIID).parentUIID) 
			return false;
		
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		
		var fromModuleTab:String = null;
		var _module = null;
		
		if(cm instanceof MonitorModel) {
			fromModuleTab = "monitorMonitorTab";
			_module = "monitoring";
		}
		
		Debugger.log('I am in drawActivity and Activity typeID :'+a.activityTypeID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasBranchView');
		
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGroupActivity()){
			Debugger.log('controller cbc :'+MonitorController(cbc), Debugger.CRITICAL, 'drawActivity','CanvasBranchView');
			Debugger.log('controller obj cbc :'+cbc, Debugger.CRITICAL, 'drawActivity','CanvasBranchView');
		
			var newActivity_mc = (_module != "monitoring") ? activityLayer.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a, _canvasController:cbc, _canvasBranchView:cbv})
															: activityLayer.createChildAtDepth("CanvasActivity",DepthManager.kTop,{_activity:a, _monitorController:cbc, _canvasBranchView:cbv, _module:_module, learnerContainer:_learnerContainer_mc});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Tool or gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasBranchView');
		}
		else if (a.isGateActivity()){
			var newActivity_mc = (_module != "monitoring") ? activityLayer.createChildAtDepth("CanvasGateActivity",DepthManager.kTop,{_activity:a, _canvasController:cbc, _canvasBranchView:cbv})
															: activityLayer.createChildAtDepth("CanvasGateActivity",DepthManager.kTop,{_activity:a, _monitorController:cbc, _canvasBranchView:cbv, _module:_module, learnerContainer:_learnerContainer_mc});
			
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Gate activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable:'+newActivity_mc,4,'drawActivity','CanvasBranchView');
		}
		else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			//get the children
			var children:Array = cm.ddm.getComplexActivityChildren(a.activityUIID);
			Debugger.log("children.length: " + children.length, Debugger.CRITICAL, "drawActivity", "CanvasBranchView");
			
			var newActivity_mc = (_module != "monitoring") ? activityLayer.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children, _canvasController:cbc,_canvasBranchView:cbv, _locked:a.isReadOnly()})
															: activityLayer.createChildAtDepth("CanvasParallelActivity",DepthManager.kTop,{_activity:a,_children:children, _monitorController:cbc,_canvasBranchView:cbv, _locked:a.isReadOnly(), fromModuleTab:fromModuleTab, learnerContainer:_learnerContainer_mc});
			
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Parallel activity a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasBranchView');
		}
		else if(a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE || a.activityTypeID==Activity.OPTIONS_WITH_SEQUENCES_TYPE){
			var children:Array = cm.ddm.getComplexActivityChildren(a.activityUIID);
			Debugger.log("children.length: " + children.length, Debugger.CRITICAL, "drawActivity", "CanvasBranchView");
			
			var newActivity_mc = (_module != "monitoring") ? activityComplexLayer.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_activity:a,_children:children, _canvasController:cbc,_canvasBranchView:cbv,_locked:a.isReadOnly()})
															: activityComplexLayer.createChildAtDepth("CanvasOptionalActivity",DepthManager.kTop,{_activity:a,_children:children, _monitorController:cbc,_canvasBranchView:cbv,_locked:a.isReadOnly(), fromModuleTab:fromModuleTab, learnerContainer:_learnerContainer_mc});
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			
			Debugger.log('Optional activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasBranchView');
		}
		else if(a.isBranchingActivity()) {	
			BranchingActivity(a).clear = activity.clear;
			
			var newActivity_mc = (_module != "monitoring") ? activityLayer.createChildAtDepth("CanvasBranchingActivity",DepthManager.kTop,{_activity:a, _canvasController:cbc,_canvasBranchView:cbv, showDiagram: true})
															: activityLayer.createChildAtDepth("CanvasBranchingActivity",DepthManager.kTop,{_activity:a, _monitorController:cbc,_canvasBranchView:cbv, _module:_module, learnerContainer:_learnerContainer_mc});
															
			cm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
			Debugger.log('Branching activity Type a.title:'+a.title+','+a.activityUIID+' added to the cm.activitiesDisplayed hashtable :'+newActivity_mc,4,'drawActivity','CanvasBranchView');
	
		}else{
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','CanvasBranchView');
		}

		return true;
	}
	
	private function drawAll(objArr:Array, model:Observable){
		Debugger.log("drawing all: " + objArr.length + " model: " + model, Debugger.CRITICAL, "drawAll", "CanvasBranchView");
				
		for (var i=0; i<objArr.length; i++){
			viewUpdate(objArr[i]);
		}
	}
	
	/**
	 * Add to canvas stage but keep hidden from view.
	 * 
	 * @usage   
	 * @param   a  
	 * @param   cm 
	 * @return  true if successful
	 */
	
	private function hideActivity(a:Activity, cm):Boolean {
		if(this.activity.activityUIID != ddm.getActivityByUIID(a.parentUIID).parentUIID) 
			return false;
			
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		
		if (a.isSystemGateActivity()){
			var newActivityObj = new Object();
			newActivityObj.activity = a;
			
			cm.activitiesDisplayed.put(a.activityUIID,newActivityObj);
			
			Debugger.log('Gate activity a.title:'+a.title+','+a.activityUIID+' added (hidden) to the cm.activitiesDisplayed hashtable:'+newActivityObj,4,'hideActivity','CanvasView');
		}
		
		return true;
	}
	
	/**
	 * Removes existing activity from canvas stage. DOES not affect DDM.  called by an update, so DDM change is already made
	 * @usage   
	 * @param   a  - Activity to be Removed
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfull
	 */
	private function removeActivity(a:Activity, cm):Boolean{
		if(this.activity.activityUIID != ddm.getActivityByUIID(a.parentUIID).parentUIID) 
			return false;
		
		if(a.isBranchingActivity())
			a.branchView.removeMovieClip();
		
		var r = cm.activitiesDisplayed.remove(a.activityUIID);
		r.removeMovieClip();
		
		var s:Boolean = (r==null) ? false : true;
		
		return s;
	}
	
	/**
	 * Draws a transition on the canvas.
	 * @usage   
	 * @param   t  The transition to draw
	 * @param   cm  the canvas model.
	 * @return  
	 */
	private function drawTransition(t:Transition, cm):Boolean{
		if(!isActivityOnLayer(cm.activitiesDisplayed.get(t.fromUIID), this.activityLayers) && !isActivityOnLayer(cm.activitiesDisplayed.get(t.toUIID), this.activityLayers)) 
			return false;
		
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		var newTransition_mc:MovieClip = transitionLayer.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t, controller:cbc, _canvasBranchView:cbv});
		
		cm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		Debugger.log('drawn a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'drawTransition','CanvasBranchView');
		
		var parentID = cm.getCanvas().ddm.getActivityByUIID(t.fromUIID).parentUIID;
		cm.moveActivitiesToBranchSequence(t.toUIID, cm.getCanvas().ddm.getActivityByUIID(parentID));
		
		return true;
	}
	
	/**
	 * Draws a branch on the canvas.
	 * @usage   
	 * @param   t  The branch to draw
	 * @param   cm  the canvas model.
	 * @return  
	 */
	private function drawBranch(b:Branch, cm):Boolean{
		Debugger.log("br activity: " + activity.activityUIID, Debugger.CRITICAL, "drawBranch", "CanvasBranchView");
		
		Debugger.log("drawing branch: " + b.branchUIID, Debugger.CRITICAL, "drawBranch", "CanvasBranchView");
		Debugger.log("branch target: " + b.targetUIID, Debugger.CRITICAL, "drawBranch", "CanvasBranchView");
		
		Debugger.log("branch dir: " + b.direction, Debugger.CRITICAL, "drawBranch", "CanvasBranchView");
		Debugger.log("isActivityOnLayer: " + isActivityOnLayer(cm.activitiesDisplayed.get(b.targetUIID), this.activityLayers), Debugger.CRITICAL, "drawBranch", "CanvasBranchView");
		
		if(!isActivityOnLayer(cm.activitiesDisplayed.get(b.targetUIID), this.activityLayers)) 
			if((b.direction == BranchConnector.DIR_SINGLE && b.targetUIID == activity.activityUIID))
				continue;
			else
				return false;
		else if(b.direction == BranchConnector.DIR_SINGLE && b.targetUIID != activity.activityUIID)
			return false;
		
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		
		if(activity.defaultBranch == null) {
			activity.defaultBranch = b;
		}
		
		if(b.direction == BranchConnector.DIR_FROM_START) b.sequenceActivity.firstActivityUIID = b.targetUIID;
		
		var newBranch_mc:MovieClip = branchLayer.createChildAtDepth("BranchConnector",DepthManager.kTop,{_branch:b, controller:cbc, _canvasBranchView:cbv});
		cm.branchesDisplayed.put(b.branchUIID,newBranch_mc);
		
		Debugger.log('drawn a branch:'+b.branchUIID+','+newBranch_mc,Debugger.GEN,'drawBranch','CanvasBranchView');
		
		if(b.direction == BranchConnector.DIR_FROM_START) {
			cm.moveActivitiesToBranchSequence(b.targetUIID, b.sequenceActivity);
		} else {
			b.sequenceActivity.stopAfterActivity = false;
		}
		
		b.sequenceActivity.isDefault = false;
		
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
	
	private function hideTransition(t:Transition, cm):Boolean{
		if(!cm.isActiveView(this)) return false;
		
		var cbv = CanvasBranchView(this);
		var cbc = getController();
		var newTransition_mc:MovieClip = transitionLayer.createChildAtDepth("CanvasTransition",DepthManager.kTop,{_transition:t, controller:cbc, _canvasBranchView:cbv, _visible:false});
		
		cm.transitionsDisplayed.put(t.transitionUIID,newTransition_mc);
		
		Debugger.log('drawn (hidden) a transition:'+t.transitionUIID+','+newTransition_mc,Debugger.GEN,'hideTransition','CanvasView');
		
		return true;
	}
	
	/**
	 * Removes a transition from the canvas
	 * @usage   
	 * @param   t  The transition to remove
	 * @param   cm  The canvas model
	 * @return  
	 */
	private function removeTransition(t:Transition, cm){
		if(!cm.isActiveView(this) || cm instanceof MonitorModel) return false;
		
		var r = cm.transitionsDisplayed.remove(t.transitionUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		
		return s;
	}

	/**
	 * Removes a branch from the canvas
	 * @usage   
	 * @param   b  The branch to remove
	 * @param   cm  The canvas model
	 * @return  
	 */
	private function removeBranch(b:Branch, cm){
		Debugger.log("activeView: " + cm.isActiveView(this), Debugger.CRITICAL, "removeBranch", "CanvasBranchView");
		Debugger.log("b.direction: " + b.direction, Debugger.CRITICAL, "removeBranch", "CanvasBranchView");
		
		if(!cm.isActiveView(this)) return false;
		
		if(b.direction == BranchConnector.DIR_SINGLE) { 
			continue;
		} else if(b.direction == BranchConnector.DIR_FROM_START) { 
			//cm.moveActivitiesToBranchSequence(b.sequenceActivity.firstActivityUIID, defaultSequenceActivity);
			cm.migrateActivitiesToSequence(b.sequenceActivity, defaultSequenceActivity);
			cm.getCanvas().removeActivity(b.sequenceActivity.activityUIID); // b.sequenceActivity.firstActivityUIID = null;
		} else if(b.direction == BranchConnector.DIR_TO_END) { 
			b.sequenceActivity.stopAfterActivity = true; 
		}
		
		var r = cm.branchesDisplayed.remove(b.branchUIID);
		r.branchLabel.removeMovieClip();
		r.removeMovieClip();
		
		if(cm instanceof MonitorModel) cm.ddm.branches.remove(b.branchUIID);
		
		var s:Boolean = (r==null) ? false : true;
		return s;
	}

	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(model):Void{
		var thisCA:CanvasActivity = _canvasBranchingActivity;
		
		var s:Object = model.getSize();
		
		var cx:Number = thisCA._x + thisCA.getVisibleWidth()/2;
		var cy:Number = thisCA._y + thisCA.getVisibleHeight()/2;
		
		s.w -= 2*hSpace;
		s.h -= 2*vSpace;
		
		canvas_scp.setSize(s.w,s.h);
		bkg_pnl.setSize(s.w, s.h);
		transparentCover.setSize(s.w, s.h);
		
		//Create the grid.  The gris is re-drawn each time the canvas is resized.
		grid_mc = Grid.drawGrid(gridLayer,Math.round(s.w),Math.round(s.h),V_GAP,H_GAP);
		
		//position bin in canvas.  
		var bin = model.getCanvas().bin;
		if(bin._parent == this.binLayer) {
			bin._x = (s.w - bin._width) - 10;
			bin._y = (s.h - bin._height) - 10;
		}
			
		//canvas_scp.redraw(true);
		
		setPosition(model, cx, cy);
	}
	
	/**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(model, cx, cy){
      //  var ba = (_ba != null || _ba == undefined) ? cm.activitiesDisplayed.get(activity.activityUIID) : _ba;
		
		Debugger.log("model: " + model.activitiesDisplayed, Debugger.CRITICAL, "setPosition", "CanvasBranchView");
		Debugger.log("act UIID: " + activity.activityUIID, Debugger.CRITICAL, "setPosition", "CanvasBranchView");
		
		Debugger.log("cx: " + cx, Debugger.CRITICAL, "setPosition", "CanvasBranchView");
		
		if(cx != null && cy != null) {
			//var cx:Number = ba._x + ba.getVisibleWidth()/2;
			//var cy:Number = ba._y + ba.getVisibleHeight()/2;
			
			//Debugger.log("current: " + ba.activity.activityUIID, Debugger.CRITICAL, "setPosition", "CanvasBranchView");
			
			if(_isBranchChild) 
				Debugger.log("bc cx: " + cx + " // bc cy: " + cy, Debugger.CRITICAL, "setPosition", "CanvasBranchView");
			
			var hPosition:Number = (model instanceof CanvasModel) ? model.getCanvas().view.getScrollPaneHPosition() : model.getMonitor().getMV().getMonitorSequenceScp().hPosition;
			var vPosition:Number = (model instanceof CanvasModel) ? model.getCanvas().view.getScrollPaneVPosition() : model.getMonitor().getMV().getMonitorSequenceScp().vPosition;
			
			canvas_scp._x = (_isBranchChild) ? -cx : -cx + hSpace + hPosition;
			canvas_scp._y = (_isBranchChild) ? -cy : -cy + vSpace + vPosition;
			
			close_mc._x = bkg_pnl._x + bkg_pnl.width - close_mc._width - 10;
			close_mc._y = bkg_pnl._y + 10;
		}
		
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
        
		var styleObj = _tm.getStyleObject('CanvasPanel');
		bkg_pnl.setStyle('styleName', styleObj);
		transparentCover.setStyle('styleName', styleObj);
		
    }
	
	public function get startHub():CanvasActivity {
		return CanvasActivity(cHubStart_mc);
	}
	
	public function get endHub():CanvasActivity {
		return CanvasActivity(cHubEnd_mc);
	}
	
	public function isStart(a:MovieClip):Boolean {
		if(startHub == a) return true;
		else return false;
	}
	
	public function isEnd(a:MovieClip):Boolean {
		if(endHub == a) return true;
		else return false;
	}
	
	public function get activity():BranchingActivity {
		return BranchingActivity(_canvasBranchingActivity.activity);
	}
	
	public function get defaultSequenceActivity():SequenceActivity {
		return _defaultSequenceActivity;
	}
	
	public function set defaultSequenceActivity(a:SequenceActivity):Void{
		_defaultSequenceActivity = a;
		_defaultSequenceActivity.isDefault = true;
	}
	
	public function get branchLayer():MovieClip {
		return _branchLayer;
	}
	
	public function set branchLayer(a:MovieClip):Void{
		_branchLayer = a;
	}
	
	public function get labelContainer():MovieClip {
		return _labelContainer_mc;
	}
	
	public function get fingerprint():MovieClip {
		return _fingerprint;
	}
	
	public function set fingerprint(a:MovieClip):Void{
		if(a == startHub || a == endHub)
			_fingerprint = a;
	}
	
	/**
	 * Overrides method in abstract view to ensure correct type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController() {
		var c:Controller = super.getController();
		
		if(model instanceof CanvasModel) {
			Debugger.log("canvas controller: " + c, Debugger.CRITICAL,'getController','CanvasBranchView');
			return CanvasController(c);
		} else {
			Debugger.log("monitor controller: " + c, Debugger.CRITICAL,'getController','CanvasBranchView');
			return MonitorController(c);
		}
	}
	
	/**
    * Returns the default controller for this view .
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model):Object {
        return (model instanceof CanvasModel) ? new CanvasController(model) : new MonitorController(model);
    }
	
	public function setOpen(a:Boolean):Void {
		_open = a;
	}
	
	public function get model():Object {
		if(_cm != null) return _cm;
		if(_mm != null) return _mm;
		return null;
	}
	
	public function get ddm():DesignDataModel {
		if(model instanceof CanvasModel) return CanvasModel(model).ddm
		else return MonitorModel(model).ddm;
	}
	
	private function getXY():Object {
		var pos:Object = new Object();
		
		pos.x = (model instanceof CanvasModel) ? org.lamsfoundation.lams.authoring.Application.CANVAS_X + hSpace : org.lamsfoundation.lams.monitoring.Application.MONITOR_X + hSpace;
		pos.y =  (model instanceof CanvasModel) ? org.lamsfoundation.lams.authoring.Application.CANVAS_Y + vSpace : org.lamsfoundation.lams.monitoring.Application.MONITOR_Y + vSpace;
		
		Debugger.log("pos x: " + pos.x + " pos y: " + pos.y, Debugger.CRITICAL, "getXY", "CanvasBranchView");
		
		return pos;
	}
	
	public function showToolTip(mcObj, btnTT:String):Void{
		var pos = getXY();
		var Xpos = pos.x + mcObj._x - 20;
		var Ypos = pos.y + mcObj._y - 20;
		
		var ttHolder = ApplicationParent.tooltip;
		var ttMessage = Dictionary.getValue(btnTT);
		
		_tip.DisplayToolTip(ttHolder, ttMessage, Xpos, Ypos);
		
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	
	public function get isOpen():Boolean {
		return _isOpen;
	}
	
	public function set isOpen(a:Boolean):Void {
		_isOpen = a;
	}
	
	public function get binLayer():MovieClip {
		return _binLayer_mc;
	}
	
	public function set binLayer(a:MovieClip):Void {
		_binLayer_mc = a;
	}
}