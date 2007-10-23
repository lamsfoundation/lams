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

			monitorModel.activeView = evt.target;
			
			if(evt.target instanceof CanvasBranchView) {
				evt.target.open();
				monitorModel.setDirty();
			} else if((monitorLockView != null || !locked) && monitorView != null) {
				dispatchEvent({type:'load',target:this});
			}
			
        } else {
            //Raise error for unrecognized event
        }
    }
	
	private function tabsLoaded(evt:Object){
        Debugger.log('tabsLoaded called',Debugger.GEN,'viewLoaded','Monitor');
		
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
		trace('getting progress data for Sequence: '+seqId);
		
		var callback:Function = Proxy.create(this, saveProgressData);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getAllLearnersProgress&lessonID=' + seqId, callback, false);
	}
	
	private function saveProgressData(progressDTO:Object){
		trace('returning progress data...'+progressDTO.length);
		var allLearners = new Array();
		for(var i=0; i< progressDTO.length; i++){
			
			var prog:Object = progressDTO[i];
			var lessonProgress:Progress = new Progress();
			lessonProgress.populateFromDTO(prog);
			//trace('pushing lesson with id: ' + lessonModel.getLessonID());
			allLearners.push(lessonProgress);
		}
			
		//sets these in the monitor model in a hashtable by learnerID
		monitorModel.setLessonProgressData(allLearners);	
		dispatchEvent({type:'load',target:this});		
		trace('progress data saved...');
	}
	
	public function getCELiteral(taskType:Number):String{
		trace("Type passed: "+taskType)
		var seqStat:String;
		switch(String(taskType)){
			case '1' :
				seqStat = "Moderation"
				break;
			case '2' :
				seqStat = "Define Later"
				break;
			case '3' :
				seqStat = "Permission Gate"
				break;
			case '4' :
				seqStat = "Syncronise Gate"
				break;
			case '5' :
				seqStat = "Schedule Gate"
				break;
			case '6' :
				seqStat = "Choose Grouping"
				break;
			case '7' :
				seqStat = "Contribution"
				break;
			default:
				seqStat = "Not yet set"
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
		
		var _branchView_mc:MovieClip = MovieClip(monitorView.getMonitorTabView()).createChildAtDepth("canvasBranchView", DepthManager.kTop, {_x: cx, _y: cy, _canvasBranchingActivity:ba, _open:isVisible});	
		var branchView:CanvasBranchView = CanvasBranchView(_branchView_mc);
		
		monitorModel.addObserver(branchView);
		branchView.init(monitorModel, undefined);
		
		//Add listener to view so that we know when it's loaded
        branchView.addEventListener('load', Proxy.create(this, viewLoaded));
		
		ba.branchView = branchView;
		
	}
	
	public function closeBranchView() {
		monitorModel.activeView = monitorView.getMonitorTabView();
		monitorModel.currentBranchingActivity = null;
	}
	
	/**
	 * Open the Help page for the selected Tool (Canvas) Activity
	 *  
	 * @param   ca 	CanvasActivity
	 * @return  
	 */
	
	public function getHelp(ca:CanvasActivity) {

		if(ca.activity.helpURL != undefined || ca.activity.helpURL != null) {
			Debugger.log("Opening help page with locale " + _root.lang + _root.country + ": " + ca.activity.helpURL,Debugger.GEN,'getHelp','Monitor');
			
			var locale:String = _root.lang + _root.country;
			getURL(ca.activity.helpURL + app.module + "#" + ca.activity.toolSignature + app.module + "-" + locale, '_blank');
		} else {
			if (ca.activity.activityTypeID == Activity.GROUPING_ACTIVITY_TYPE){
				var callback:Function = Proxy.create(this, openGroupHelp);
				app.getHelpURL(callback)
			}else if (ca.activity.activityTypeID == Activity.SYNCH_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PERMISSION_GATE_ACTIVITY_TYPE){
				var callback:Function = Proxy.create(this, openGateHelp);
				app.getHelpURL(callback)
			}else {
				LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_helpURL_undefined', [ca.activity.toolDisplayName]));
			}
		}
	}
	
	private function openGroupHelp(url:String){
		var actToolSignature:String = Application.FLASH_TOOLSIGNATURE_GROUP
		var locale:String = _root.lang + _root.country;
		var target:String = actToolSignature + app.module + '#' + actToolSignature + app.module + '-' + locale;
		getURL(url + target, '_blank');
	}
	
	private function openGateHelp(url:String){
		var actToolSignature:String = Application.FLASH_TOOLSIGNATURE_GATE
		var locale:String = _root.lang + _root.country;
		var target:String = actToolSignature + app.module + '#' + actToolSignature + app.module + '-' + locale;
		getURL(url + target, '_blank');
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