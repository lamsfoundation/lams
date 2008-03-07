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
import org.lamsfoundation.lams.monitoring.Application;
import org.lamsfoundation.lams.monitoring.Organisation;
import org.lamsfoundation.lams.monitoring.User;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.cv.CanvasActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasOptionalActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasComplexView;
import org.lamsfoundation.lams.authoring.br.CanvasBranchView;
import org.lamsfoundation.lams.common.ui.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.Progress;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.* ;

import mx.utils.*;
import mx.managers.*;
import mx.events.*;

class Monitor {
	//Constants
	public static var USE_PROPERTY_INSPECTOR = true;

	private var _className:String = "Monitor";

	// Root movieclip
	private var _root_mc:MovieClip;
	
	// Model
	private var monitorModel:MonitorModel;
	
	// View
	private var monitorView:MonitorView;
	private var monitorLockView:MonitorLockView;
	private var monitorView_mc:MovieClip;
	private var monitorLockView_mc:MovieClip;

	private var canvasComplexView:CanvasComplexView;
	private var _canvasComplexView_mc:MovieClip;

	private var locked:Boolean;

	private var app:Application;
	private var _sequence:Sequence;
	private var _ddm:DesignDataModel;
	private var _dictionary:Dictionary;

	private var _currentUserRole:String;

	private var _pi:MovieClip; //Property inspector
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	private var _onOKCallBack:Function;
	
	/**
	 * Monitor Constructor Function
	 * 
	 * @usage   
	 * @return  target_mc		//Target clip for attaching view
	 */
	public function Monitor(target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number, locked:Boolean, isEditingUser:Boolean){
		mx.events.EventDispatcher.initialize(this);
		
		// Set root movieclip
		_root_mc = target_mc;
		this.locked = locked;
		app = Application.getInstance();
		
		//Create the model
		monitorModel = new MonitorModel(this);
		monitorModel.locked = locked;
		
		_dictionary = Dictionary.getInstance();
		
		if(monitorView_mc == null || monitorView_mc == undefined) {
			createNormalViewModel(x, y, w, h);
		}
		
		if(monitorModel.locked) {
			createLockedViewModel(x, y, w, h, isEditingUser);
			monitorView_mc._visible = false;
		} 
		
        //Set the position by setting the model which will call update on the view
        monitorModel.setPosition(x,y);
		monitorModel.setSize(w,h);
		monitorModel.initOrganisationTree();
		
	}
	
	private function createNormalViewModel(x:Number,y:Number,w:Number,h:Number) {
		//Create the view
		monitorView_mc = _root_mc.createChildAtDepth("monitorView",DepthManager.kTop);	
		
		monitorView = MonitorView(monitorView_mc);
		
		//Register view with model to receive update events
		monitorModel.addObserver(monitorView);
		
		monitorView.init(monitorModel,undefined,x,y,w,h);
		
		monitorView.addEventListener('load',Proxy.create(this,viewLoaded));
		monitorView.addEventListener('tload',Proxy.create(this,tabsLoaded));
		
		monitorModel.addEventListener('learnersLoad',Proxy.create(this,onUserLoad));
		monitorModel.addEventListener('staffLoad',Proxy.create(this,onUserLoad));
		
	}
	
	private function createLockedViewModel(x:Number,y:Number,w:Number,h:Number, isEditingUser:Boolean) {
		monitorLockView_mc = _root_mc.createChildAtDepth("monitorLockView",DepthManager.kTop);	
		monitorLockView = MonitorLockView(monitorLockView_mc);
		
		//Register view with model to receive update events
		monitorModel.addObserver(monitorLockView);
		
		monitorLockView.init(monitorModel,undefined,x,y,w,h,isEditingUser);
        monitorLockView.addEventListener('load',Proxy.create(this,viewLoaded));
		
	}
	
	/**
	* event broadcast when Monitor is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Monitor');
		
		if(evt.type=='load') {

			if(evt.target instanceof CanvasBranchView) {
				monitorModel.activeView = evt.target;
			
				evt.target.open();
				monitorModel.setDirty(false);
			} else if(evt.target instanceof CanvasComplexView) {
				monitorModel.activeView = evt.target;
			
				// open complex view
				evt.target.showActivity();
			} else if((monitorLockView != null || !locked) && monitorView != null) {
				dispatchEvent({type:'load',target:this});
			}
			
        } else {
            //Raise error for unrecognized event
        }
    }
	
	private function tabsLoaded(evt:Object){
        Debugger.log('tabsLoaded called',Debugger.GEN,'tabsLoaded','Monitor');
		monitorModel.activeView = MonitorView(evt.target).getMonitorTabView();
			
		monitorModel.setSequence(app.sequence);
		saveDataDesignModel(null);
		
    }
	
	/**
    * Opens the help->about dialog
    */
    public function openAboutLams() {
		
		var controller:MonitorController = monitorView.getController();
		
		var dialog:MovieClip = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('about_popup_title_lbl', [Dictionary.getValue('stream_reference_lbl')]),closeButton:true,scrollContentPath:'AboutLams'});
		dialog.addEventListener('contentLoaded',Delegate.create(controller, controller.openAboutDialogLoaded));
		
	}


	/**
    * Called when Users loaded for role type
	* @param evt:Object	the event object
    */
    private function onUserLoad(evt:Object){
        if(evt.type=='staffLoad'){
            monitorModel.staffLoaded = true;
			Debugger.log('Staff loaded :',Debugger.CRITICAL,'onUserLoad','Monitor');			
        } else if(evt.type=='learnersLoad'){
			monitorModel.learnersLoaded = true;
			Debugger.log('Learners loaded :',Debugger.CRITICAL,'onUserLoad','Monitor');			
		} else {
            Debugger.log('event type not recognised :'+evt.type,Debugger.CRITICAL,'onUserLoad','Monitor');
        }
    }
	
	
	/**
	* Opens a design using workspace and user to select design ID
	* passes the callback function to recieve selected ID
	*/
	public function openDesignBySelection(){
        //Work space opens dialog and user will select view
        var callback:Function = Proxy.create(this, openDesignById);
		var ws = Application.getInstance().getWorkspace();
        ws.userSelectDesign(callback);
	}
    
	/**
	 * Request design from server using supplied ID.
	 * @usage   
	 * @param   designId 
	 * @return  
	 */
    private function openDesignById(workspaceResultDTO:Object){
		
		ObjectUtils.toString(workspaceResultDTO);
		var designId:Number = workspaceResultDTO.selectedResourceID;
		var lessonName:String = workspaceResultDTO.resourceName;
		var lessonDesc:String = workspaceResultDTO.resourceDescription;
        var callback:Function = Proxy.create(this,setLesson);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=initializeLesson&learningDesignID='+designId+'&userID='+_root.userID+'&lessonName='+lessonName+'&lessonDescription='+lessonDesc,callback, false);

    }
	
	private function loadLessonToMonitor(lessonID:Number){
		var callback:Function = Proxy.create(monitorModel,monitorModel.loadSequence);
		if(_sequence != null) {
			monitorModel.setSequence(_sequence);
		} else {
			Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getLessonDetails&lessonID=' + String(lessonID) + '&userID=' + _root.userID,callback, false);
		}
	}
	
	public function closeAndRefresh() {
		ApplicationParent.extCall("closeWindowRefresh", null);
	}
	
	public function reloadLessonToMonitor(){
		_sequence = null;
		loadLessonToMonitor(_root.lessonID);
	}
	
	public function startLesson(isScheduled:Boolean, lessonID:Number, datetime:String){
		Debugger.log('populating seq object for start date:'+datetime,Debugger.CRITICAL,'startLesson','Monitor');
		var callback:Function = Proxy.create(this, onStartLesson);
		
		if(isScheduled){
			Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=startOnScheduleLesson&lessonStartDate=' + datetime + '&lessonID=' + lessonID + '&userID=' + _root.userID, callback);
		} else {
			//getMV.
			Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=startLesson&lessonID=' + lessonID + '&userID=' + _root.userID, callback);
		}
	}
	
	private function onStartLesson(b:Boolean){
		trace('receive back after lesson started..');
		if(b){
			trace('lesson started');
			loadLessonToMonitor(_root.lessonID);
			
		} else {
			// error occured
			trace('error occurred starting lesson');
		}
	}
	
	/**
	 * Create LessonClass using wizard data and CreateLessonClass servlet
	 * 
	 */
	
	public function createLessonClass():Void{
		var dto:Object = monitorModel.getLessonClassData();
		var callback:Function = Proxy.create(this,onCreateLessonClass);
		
		Application.getInstance().getComms().sendAndReceive(dto,"monitoring/createLessonClass?userID=" + _root.userID,callback,false);
		
	}
	
	public function onCreateLessonClass(r):Void{
		if(r instanceof LFError) {
			r.showErrorAlert();
		} else if(r) {
			// lesson class created
			monitorModel.broadcastViewUpdate("SAVED_LC", null);
			loadLessonToMonitor(_root.lessonID);
		} else {
			// failed creating lesson class
		}
	}
	
	/**
	 * Set new Lesson in Monitoring
	 * @usage   
	 * @param   lesson ID
	 * @return  
	 */
    private function setLesson(lessonID:Number){
       // refresh Lesson Library
	   Application.getInstance().getLesson().addNew(lessonID);
	   
    }

	public function requestUsers(role:String, orgID:Number, callback:Function){
		Application.getInstance().getComms().getRequest('workspace.do?method=getUsersFromOrganisationByRole&organisationID='+orgID+'&role='+role,callback, false);
	
	}

	/**
	 * server call for learning Dseign and sent it to the save it in DataDesignModel
	 * 
	 * @usage   
	 * @param   		seq type Sequence;
	 * @return  		Void
	 */
	public function openLearningDesign(seq:Sequence){
		trace('opening learning design...'+ seq.learningDesignID);
		var designID:Number  = seq.learningDesignID;
        var callback:Function = Proxy.create(this,saveDataDesignModel);
           
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designID,callback, false);
		
	}
	
	private function saveDataDesignModel(learningDesignDTO:Object){
		trace('returning learning design...');
		trace('saving model data...');
		var seq:Sequence = Sequence(monitorModel.getSequence());
		_ddm = new DesignDataModel();
		
		//  clear canvas
		clearCanvas(true);
			
		if(learningDesignDTO != null) { _ddm.setDesign(learningDesignDTO); seq.setLearningDesignModel(_ddm); }
		else if(seq.getLearningDesignModel() != null){ _ddm = seq.getLearningDesignModel(); }
		else { openLearningDesign(seq); }
		
		monitorModel.setSequence(seq);
		monitorModel.broadcastViewUpdate('PROGRESS', null, monitorModel.getSelectedTab());
	
	}

	public function getContributeActivities(seqID:Number):Void{
		trace('getting all contribute activities for sequence: ' + seqID);
        var callback:Function = Proxy.create(monitorModel,monitorModel.setToDos);
           
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getAllContributeActivities&lessonID='+seqID,callback, false);
		
	}
	
	public function getProgressData(seq:Object){
		var seqId:Number = seq.getSequenceID();
		Debugger.log('getting progress data for Sequence: '+seqId, Debugger.GEN, "getProgressData", "Monitor");
		var callback:Function = Proxy.create(this, saveProgressData);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getAllLearnersProgress&lessonID=' + seqId, callback, false);
	}
	
	public function getInitialLearnersProgress(seq:Object) { // Not used atm but leaving here in case we ever want to implement batch loading
		var seqId:Number = seq.getSequenceID();
		Debugger.log('getting initial progress data for Sequence: '+seqId, Debugger.CRITICAL, "getInitialLearnersProgress", "Monitor");
		var callback:Function = Proxy.create(this, saveProgressData);	
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getInitialLearnersProgress&lessonID=' + seqId,callback, false);
	}
	
	public function getAdditionalLearnersProgress(seq:Object) { // Not used atm but leaving here in case we ever want to implement batch loading
		var seqId:Number = seq.getSequenceID();
		Debugger.log('getting additional progress data for Sequence: '+seqId, Debugger.CRITICAL, "getInitialLearnersProgress", "Monitor");
		var callback:Function = Proxy.create(this, saveProgressData);
		//Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getAdditionalLearnersProgress&lessonID='+seqId+'&lastUserID='+learnerProgressList[learnersProgressList.length-1].userName,callback,false);
	}
	
	private function saveProgressData(progressDTO:Object){
		Debugger.log("saveProgressData invoked", Debugger.GEN, "saveProgressData", "Monitor");
		var allLearners = new Array();
		for(var i=0; i< progressDTO.length; i++){	
			var prog:Object = progressDTO[i];
			var lessonProgress:Progress = new Progress();
			lessonProgress.populateFromDTO(prog);
			allLearners.push(lessonProgress);
		}
			
		//sets these in the monitor model in a hashtable by learnerID
		monitorModel.activeView = monitorView.getMonitorTabView();
		monitorModel.setLessonProgressData(allLearners);
		monitorModel.backupLearnersProgress(monitorModel.allLearnersProgress);
		
		dispatchEvent({type:'load',target:this});		
		
		Debugger.log("Progress data saved...", Debugger.GEN, "saveProgressData", "Monitor");
	}
	
	public function getCELiteral(taskType:Number):String{
		var seqStat:String;
		
		switch(String(taskType)){
			case '1' :
				seqStat = Dictionary.getValue("ls_seq_status_moderation"); // "Moderation"
				break;
			case '2' :
				seqStat = Dictionary.getValue("ls_seq_status_define_later"); // "Define Later"
				break;
			case '3' :
				seqStat = Dictionary.getValue("ls_seq_status_perm_gate"); // "Permission Gate"
				break;
			case '4' :
				seqStat = Dictionary.getValue("ls_seq_status_synch_gate"); // "Syncronise Gate"
				break;
			case '5' :
				seqStat = Dictionary.getValue("ls_seq_status_sched_gate"); // "Schedule Gate"
				break;
			case '6' :
				seqStat = Dictionary.getValue("ls_seq_status_choose_grouping"); // "Choose Grouping"
				break;
			case '7' :
				seqStat = Dictionary.getValue("ls_seq_status_contribution"); // "Contribution"
				break;
			case '8' :
				seqStat = Dictionary.getValue("ls_seq_status_system_gate"); // "System Gate"
				break;
			case '9' :
				seqStat = Dictionary.getValue("ls_seq_status_teacher_branching"); // "Teacher Chosen Branching"
				break;
			default:
				seqStat = Dictionary.getValue("ls_seq_status_not_set"); // "Not yet set"
		}
		
		return seqStat;
	}
	
	/**
	 * Clears the design in the canvas.but leaves other state variables (undo etc..)
	 * @usage   
	 * @param   noWarn 
	 * @return  
	 */
	public function clearCanvas(noWarn:Boolean):Boolean{
		var s = false;
		var ref = this;
		Debugger.log('noWarn:'+noWarn,4,'clearCanvas','Monitor');
		if(noWarn){
			
			_ddm = new DesignDataModel();
			//as its a new instance of the ddm,need to add the listener again
			//_ddm.addEventListener('ddmUpdate',Proxy.create(this,onDDMUpdated));
			
			Debugger.log('noWarn2:'+noWarn,4,'clearCanvas','Monitor');//_ddm.addEventListener('ddmBeforeUpdate',Proxy.create(this,onDDMBeforeUpdate));
			
			monitorModel.setDirty();
			return true;
		}else{
			//var fn:Function = Proxy.create(ref,confirmedClearDesign, ref);
			//LFMessage.showMessageConfirm(Dictionary.getValue('new_confirm_msg'), fn,null);
			Debugger.log('Set design failed as old design could not be cleared',Debugger.CRITICAL,"setDesign",'Canvas');		
		}
	}
	
	public function openBranchView(ba, visible:Boolean){
		
		var cx:Number = ba._x + ba.getVisibleWidth()/2;
		var cy:Number = ba._y + ba.getVisibleHeight()/2;
		var isVisible:Boolean = (visible == null) ? true : visible;
		
		var target:MovieClip = monitorModel.activeView.branchContent;
		var _branchView_mc:MovieClip = target.createChildAtDepth("canvasBranchView", DepthManager.kTop, {_x: cx, _y: cy, _canvasBranchingActivity:ba, _open:isVisible});	
		var branchView:CanvasBranchView = CanvasBranchView(_branchView_mc);
		
		branchView.init(monitorModel, monitorView.getController());
		
		//Add listener to view so that we know when it's loaded
        branchView.addEventListener('load', Proxy.create(this, viewLoaded));
		
		monitorModel.addObserver(branchView);
		ba.activity.branchView = branchView;
		
		var actToPush = monitorModel.getMonitor().ddm.getActivityByUIID(ba.activity.activityUIID);
		
		Debugger.log("Pushing activity: "+actToPush.title+" to the stack", Debugger.CRITICAL, "openBranchActivityContent", "MonitorModel");
		Debugger.log("It has a UIID of: "+actToPush.activityUIID, Debugger.CRITICAL, "openBranchActivityContent", "MonitorModel");
		
		monitorModel.openBranchingActivities.push(ba.activity.activityUIID);
	}
	
	public function closeBranchView() {
		var parentBranching:CanvasActivity = null;
		var isCBV:Boolean = false;
		
		if(monitorModel.activeView.activity.parentUIID != null) 
			parentBranching = CanvasActivity(monitorModel.activitiesDisplayed.get(_ddm.getActivityByUIID(monitorModel.activeView.activity.parentUIID).parentUIID));

		monitorModel.activeView = (parentBranching.activity.isBranchingActivity()) ? parentBranching.activity.branchView : monitorView.getMonitorTabView();
		monitorModel.currentBranchingActivity = (parentBranching.activity.isBranchingActivity()) ? parentBranching : null;
		
		var poppedActivityUIID:Number = monitorModel.openBranchingActivities.pop();
		var poppedActivity = monitorModel.getMonitor().ddm.getActivityByUIID(poppedActivityUIID);
		
		Debugger.log("Closing branching activity: "+poppedActivity.title, Debugger.CRITICAL, "closeBranchView", "Monitor");
		Debugger.log("It had a UIID of: "+poppedActivityUIID, Debugger.CRITICAL, "openBranchActivityContent", "Monitor");
	}
	
	public function openComplexView(ca:Object):Void {
		
		var target:MovieClip = (monitorModel.activeView instanceof CanvasBranchView) ? monitorModel.activeView.complexViewer : monitorView.getMonitorTabView().complexViewer;
		
		var cx:Number;
		var cy:Number;
		
		var parentAct:Activity = ddm.getActivityByUIID(ca.activity.parentUIID);
		var grandParentActivity:MovieClip = monitorModel.activitiesDisplayed.get(parentAct.parentUIID);
		var parentActivity:MovieClip = monitorModel.activitiesDisplayed.get(parentAct.activityUIID);
		
		if(monitorModel.activeView instanceof CanvasComplexView) {
			if(monitorModel.activeView.complexActivity == ca) {
				return;
			}
				
			target = monitorModel.activeView.complexViewer;
			
			Debugger.log("parentAct: " + parentAct.activityUIID, Debugger.CRITICAL, "openComplexView", "Monitor");
			Debugger.log("parentAct type: " + parentAct.activityTypeID, Debugger.CRITICAL, "openComplexView", "Monitor");
			Debugger.log("gpAct: " + grandParentActivity.activity.activityUIID, Debugger.CRITICAL, "openComplexView", "Monitor");
			
			if(parentAct.isSequenceActivity() && monitorModel.activeView.openActivity instanceof CanvasOptionalActivity) {
				cx = parentAct.xCoord + ca._x;
				cy = parentAct.yCoord + ca._y;
			} else {
				cx = ca._x;
				cy = ca._y;
			}
		} else {
		
			if(parentAct.isSequenceActivity() && grandParentActivity instanceof CanvasOptionalActivity) {
				cx = grandParentActivity._x +  parentAct.xCoord + ca._x;
				cy = grandParentActivity._y + parentAct.yCoord + ca._y;
			} else {
				cx = parentActivity._x + ca._x;
				cy = parentActivity._y + ca._y;
			}
		}
		
		Debugger.log("co ord x: " + cx +  " y: " + cy, Debugger.CRITICAL, "openComplexView", "Monitor");
		
		_canvasComplexView_mc = target.createChildAtDepth("canvasComplexView", DepthManager.kTop, {_x: cx, _y: cy, _complexActivity:ca, _parentActivity:parentActivity, _visible:false, _prevActiveView: monitorModel.activeView});
		canvasComplexView = CanvasComplexView(_canvasComplexView_mc);
		
		canvasComplexView.init(monitorModel, undefined);
		canvasComplexView.addEventListener('load', Proxy.create(this, viewLoaded));
		
		monitorModel.addObserver(canvasComplexView);
	}
	
	
	/**
	 * Open the Help page for the selected Tool (Canvas) Activity
	 *  
	 * @param   ca 	CanvasActivity
	 * @return  
	 */
	
	public function getHelp(ca:CanvasActivity) {

		if(ca.activity.helpURL != undefined || ca.activity.helpURL != null) {
			Debugger.log("Opening help page: " + ca.activity.helpURL + app.module, Debugger.GEN, 'getHelp', 'Monitor');
			
			ApplicationParent.extCall("openURL", ca.activity.helpURL + app.module);
		} else {
			if (ca.activity.activityTypeID == Activity.GROUPING_ACTIVITY_TYPE){
				var callback:Function = Proxy.create(this, openSystemToolHelp, Application.FLASH_TOOLSIGNATURE_GROUP);
				app.getHelpURL(callback);
			}else if (ca.activity.activityTypeID == Activity.SYNCH_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PERMISSION_GATE_ACTIVITY_TYPE){
				var callback:Function = Proxy.create(this, openSystemToolHelp, Application.FLASH_TOOLSIGNATURE_GATE);
				app.getHelpURL(callback);
			} else if(ca.activity.isBranchingActivity()) {
				var callback:Function = Proxy.create(this, openSystemToolHelp, Application.FLASH_TOOLSIGNATURE_BRANCHING);
				app.getHelpURL(callback);
			} else {
				LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_helpURL_undefined', [ca.activity.toolDisplayName]));
			}
		}
	}
	
	private function openSystemToolHelp(url:String, toolSignature:String){
		var target:String = toolSignature + app.module;
		url = ApplicationParent.addLocaleToURL(url);
		
		ApplicationParent.extCall("openURL", url + target);
	}

	public function setupEditOnFly(learningDesignID:Number) {
		Debugger.log("Checking for permission to edit and setting up design with ID: " + learningDesignID,Debugger.GEN,'setupEditOnFly','Monitor');
			
		var callback:Function = Proxy.create(this,readyEditOnFly, true);
        
		Application.getInstance().getComms().getRequest('eof/authoring/editLearningDesign?learningDesignID=' + learningDesignID + '&userID=' + _root.userID + '&p=' + Math.random() ,callback, false);
		
	}

	public function readyEditOnFly(r:Object) {
		if(r instanceof LFError) { 
			r.showErrorAlert();
			return;
		} else if(!Boolean(r)) {
			ApplicationParent.extCall("reloadWindow", null);
			return;
		}
		
		Debugger.log("Check OK. Proceed with opening design.",Debugger.GEN,'setupEditOnFly','Monitor');
		
		//var loader_url = Config.getInstance().serverUrl + "lams_preloader.swf?loadFile=lams_authoring.swf&loadLibrary=lams_authoring_library.swf&serverURL=" + Config.getInstance().serverUrl + "&userID=" + _root.userID  + "&build=" + _root.build + "&lang=" + _root.lang + "&country=" + _root.country + "&langDate=" + _root.langDate + "&theme=" + _root.theme + "&uniqueID=undefined" + "&layout=" + ApplicationParent.EDIT_MODE + "&learningDesignID=" + monitorModel.getSequence().learningDesignID;
		//Debugger.log("url: " + loader_url, Debugger.CRITICAL, 'openEditOnFly', 'MonitorView');
		
		//JsPopup.getInstance().launchPopupWindow(loader_url , 'AuthoringWindow', 570, 796, true, true, false, false, false);
		
		var designID:Number = monitorModel.getSequence().learningDesignID;
		if(designID != null)
			ApplicationParent.extCall("openAuthorForEditOnFly", String(designID));
		else
			ApplicationParent.extCall("openAuthorForEditOnFly", String(App.sequence.learningDesignID));
		
	}

	/**
	 * 
	 * @usage   
	 * @param   newonOKCallback 
	 * @return  
	 */
	public function set onOKCallback (newonOKCallback:Function):Void {
		_onOKCallBack = newonOKCallback;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get onOKCallback ():Function {
		return _onOKCallBack;
	}

	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		monitorModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        monitorModel.setPosition(x,y);
    }
	
	//Dimension accessor methods
	public function get width():Number{
		return monitorModel.width;
	}
	
	public function get height():Number{
		return monitorModel.height;
	}
	
	public function get x():Number{
		return monitorModel.x;
	}
	
	public function get y():Number{
		return monitorModel.y;
	}

    function get className():String { 
        return _className;
    }
	public function getMM():MonitorModel{
		return monitorModel;
	}
	public function getMV():MonitorView{
		return monitorView;
	}

	public function get ddm():DesignDataModel{
		return _ddm;
	}
	
	public function get model():MonitorModel {
		return monitorModel;
	}
	
	public function get root():MovieClip{
		return _root_mc;
	}
	
	public function get App():Application{
		return Application.getInstance();
	}
}