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

import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.wizard.*
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import mx.controls.*;
import mx.managers.*;
import mx.containers.*;
import mx.events.*;
import mx.utils.*;

/**
*Monitoring view for the Monitor
* Relects changes in the MonitorModel
*/

class org.lamsfoundation.lams.monitoring.mv.tabviews.LessonTabView extends AbstractView{
	public static var _tabID:Number = 0;
	
	// combo box static data for status change
	public static var ACTIVE_CBI:Number = 1;
	public static var DISABLE_CBI:Number = 2;
	public static var ARCHIVE_CBI:Number = 3;
	
	private var _className = "LessonTabView";
	//constants:
	private var _tm:ThemeManager;
	private var mm:MonitorModel;
	
	//TabView clips
	private var reqTasks_depth:Number = 4000;
	private var listCount:Number = 0; 
	private var requiredTaskList:Array = new Array();
	private var _monitorReqTask_mc:MovieClip;
	private var reqTasks_scp:MovieClip;
	private var monitorTabs_tb:MovieClip;
	private var _lessonStateArr:Array;
	//Labels
	private var status_lbl:Label;
	private var learner_lbl:Label;
	private var class_lbl:Label;
	private var elapsed_lbl:Label;
	private var manageClass_lbl:Label;
	private var manageStatus_lbl:Label;
	private var manageStart_lbl:Label;
	//private var manageMin_lbl:Label;
	//private var manageHour_lbl:Label;
	private var manageDate_lbl:Label;
	
		
	//Text Items
    private var LSTitle_txt:TextField;
	private var LSDescription_txt:TextField;
	private var sessionStatus_txt:TextField;
	private var numLearners_txt:TextField;
	private var group_txt:TextField;
	private var duration_txt:TextField;
	private var lessonManager:TextField;
	private var taskManager:TextField;
	private var startMsg_txt:TextField;
	
	//Button
	private var viewLearners_btn:Button;
	private var editClass_btn:Button;
	private var statusApply_btn:Button;
	private var schedule_btn:Button;
	private var start_btn:Button;
	
	private var scheduleDate_dt:DateField;
	private var scheduleTime:MovieClip;
	//private var startHour_stp:NumericStepper;
	//private var startMin_stp:NumericStepper;
	
	//COMBO
	private var changeStatus_cmb:ComboBox;
	
    //private var _transitionLayer_mc:MovieClip;
	//private var _activityLayerComplex_mc:MovieClip;
	//private var _activityLayer_mc:MovieClip;
	
	//private var _transitionPropertiesOK:Function;
    private var _lessonTabView:LessonTabView;
	private var _monitorController:MonitorController;
	private var _dialog:MovieClip;

    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	//public var menu:ContextMenu;

	
	/**
	* Constructor
	*/
	function LessonTabView(){
		_lessonTabView = this;
		this._visible = false;
		_tm = ThemeManager.getInstance();
        //Init for event delegation
        mx.events.EventDispatcher.initialize(this);
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		super (m, c);
		
		
	}    
	
	/**
 * Recieved update events from the CanvasModel. Dispatches to relevent handler depending on update.Type
 * @usage   
 * @param   event
 */
public function update (o:Observable,infoObj:Object):Void{
		
       mm = MonitorModel(o);
	   
	   switch (infoObj.updateType){
		    case 'SIZE' :
			    setSize(mm);
                break;
			case 'POSITION' :
				setPosition(mm);
                break;
			case 'TODOS' :
				populateContributeActivities();
                break;
			case 'TABCHANGE' :
				if (infoObj.tabID == _tabID){
				trace("TabID for Selected tab is (LessonTab TABCHANGE): "+infoObj.tabID)
					this._visible = true;
					//mm.setDirty();
					MovieClipUtils.doLater(Proxy.create(this,draw));
				}else {
					this._visible = false;
				}
				break;
			case 'SEQUENCE' :
				if (infoObj.tabID == _tabID){
				trace("TabID for Selected tab is (LessonTab): "+infoObj.tabID)
					this._visible = true;
					MovieClipUtils.doLater(Proxy.create(this,draw));
				}else {
					this._visible = false;
				}
				break;
			case 'LM_DIALOG' :
				_monitorController = getController();
				showLessonManagerDialog(mm);
				break;
			case 'VM_DIALOG' :
				_monitorController = getController();
				showLearnersDialog(mm);
				break;
			case 'USERS_LOADED' :
				//_dialog.checkLearners(mm.organisation.getLearners());
				//_dialog.checkStaff(mm.organisation.getStaff());
				//_monitorController.clearBusy();
				break;
			case 'LEARNERS_LOADED' :
				_dialog.checkLearners(mm.organisation);
				break;
			case 'STAFF_LOADED' :
				_dialog.checkStaff(mm.organisation);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonTabView');
		}

	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		listCount = 0; 
		this.onEnterFrame = setupLabels;
		//get the content path for the sp
		_monitorReqTask_mc = reqTasks_scp.content;
		_monitorController = getController();
		
		editClass_btn.addEventListener("click", _monitorController);
		viewLearners_btn.addEventListener("click", _monitorController);
		schedule_btn.addEventListener("click", Delegate.create(this, scheduleLessonStart));
		start_btn.addEventListener("click", _monitorController);
		statusApply_btn.addEventListener("click", Delegate.create(this, changeStatus))
		//Debugger.log('_canvas_mc'+_canvas_mc,Debugger.GEN,'draw','CanvasView');
	
		trace("Loaded LessonTabView Data"+ this)
	
		startMsg_txt.visible = false;
	
		var seq:Sequence = mm.getSequence();
	
		populateStatusList(seq.state);
		
		if(seq.state != Sequence.ACTIVE_STATE_ID){
		// hide start buttons etc
			scheduleTime._visible = false;
			scheduleDate_dt.visible = false;
			start_btn.visible = false;
			schedule_btn.visible = false;
			manageDate_lbl.visible = false;
			//manageStart_lbl.visible = false;
		/**	
			if(seq.isStarted()){
				startMsg_txt.text = "Currently Started."
			} else {
				startMsg_txt.text = "Scheduled to start at "
			}
			
			startMsg_txt.visible = true;
			*/
		}
		
		//setStyles();
		//populateLessonDetails();
		var requestLessonID:Number = seq.getSequenceID()
		
		var callback:Function = Proxy.create(this,populateLessonDetails);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getLessonLearners&lessonID='+String(requestLessonID),callback, false);
		//getLessonLearners(lessonID)
		
		trace('seq id: ' + mm.getSequence().getSequenceID());
		trace('last seq id: ' + mm.getLastSelectedSequence().getSequenceID());
		if (mm.getSequence().getSequenceID() == mm.getLastSelectedSequence().getSequenceID()){
			if(mm.getToDos() == null){
				mm.getMonitor().getContributeActivities(mm.getSequence().getSequenceID());
			} else {
				populateContributeActivities();
			}
		}else{
			mm.getMonitor().getContributeActivities(mm.getSequence().getSequenceID());
		}
		
		dispatchEvent({type:'load',target:this});
	}
	
	/**
	 * Populate the lesson details from HashTable Sequence in MOnitorModel
	*/
	private function populateLessonDetails(users:Array):Void{
		//var mm:Observable = getModel();
		trace("Number of learners are: "+users.length)
		var s:Object = mm.getSequence();
		//trace("Number of Learners Sequence are : "+_wm.getLessonClassData().learners.length);
		LSTitle_txt.text = s.name;
		LSDescription_txt.text = s.description;
		sessionStatus_txt.text = showStatus(s.state);
		numLearners_txt.text = " of "+String(users.length)
		//group_txt.text = s._seqDescription
		//duration_txt.text = s._seqDescription
		  
	}
	
	private function populateStatusList(stateID:Number):Void{
		changeStatus_cmb.removeAll();
		
		switch(stateID){
			case Sequence.SUSPENDED_STATE_ID :
				changeStatus_cmb.addItem("Select Status", LessonTabView.ACTIVE_CBI);
				changeStatus_cmb.addItem("Active", LessonTabView.ACTIVE_CBI);
				changeStatus_cmb.addItem("Archive", LessonTabView.ARCHIVE_CBI);
				break;
			case Sequence.ARCHIVED_STATE_ID :
				changeStatus_cmb.addItem("Select Status", LessonTabView.ACTIVE_CBI);
				changeStatus_cmb.addItem("Activate", LessonTabView.ACTIVE_CBI);
				break;
			case Sequence.ACTIVE_STATE_ID :
				changeStatus_cmb.addItem("Select Status", LessonTabView.ACTIVE_CBI);
				changeStatus_cmb.addItem("Archive", LessonTabView.ARCHIVE_CBI);
				break;
			default :
			//	if(mm.getSequence().isStarted()){
				changeStatus_cmb.addItem("Select Status", LessonTabView.ARCHIVE_CBI);
				changeStatus_cmb.addItem("Disable", LessonTabView.DISABLE_CBI);
				changeStatus_cmb.addItem("Archive", LessonTabView.ARCHIVE_CBI);
			//	}	changeStatus_cmb.addItem("Archive", LessonTabView.ARCHIVE_CBI);
				
		}
	}

	private function showStatus(seqStatus:Number):String{
		var seqStat:String;
		switch(String(seqStatus)){
			case '6' :
				seqStat = "Archived"
				break;
			case '3' :
				seqStat = "Started"
				break;
			case '4' :
				seqStat = "Suspended"
				break;
			default:
				seqStat = "Active"
		}
		return seqStat
	}
	
	/**
	 * Apply status change
	 *   
	 * 
	 * @param   evt Apply onclick event
	 */
	private function changeStatus(evt:Object):Void{
		var stateID:Number = changeStatus_cmb.selectedItem.data;
		switch(stateID){
			case ACTIVE_CBI :
				mm.activateSequence();
				break;
			case DISABLE_CBI :
				mm.suspendSequence();
				break;
			case ARCHIVE_CBI :
				mm.archiveSequence();
				break;
			default :
				trace('no such combo box item');
				
		}
	}
	
	private function scheduleLessonStart(evt:Object):Void{
		var datetime:String = getScheduleDateTime(scheduleDate_dt.selectedDate, scheduleTime.f_returnTime());
		mm.getMonitor().startLesson(true, _root.lessonID, datetime);
	}

	private function populateContributeActivities():Void{
		if (requiredTaskList.length == 0){
			var todos:Array = mm.getToDos();
			// show isRequired activities in scrollpane
			for (var i=0; i<todos.length; i++){
				trace('main CA title: ' + todos[i].title);
				var array:Array = getEntries(todos[i]);
				drawIsRequiredTasks(todos[i], array, 0);
			}
		}
	}
	
	/**
	 * Return isRequired entries
	 * 
	 * @usage   
	 * @param   ca ContributeActivity
	 * @return  Array of isRequired entries
	 */
	
	private function getEntries(ca:Object):Array{
		var array:Array = new Array();
		for (var i=0; i<ca.childActivities.length; i++){
			trace(ca.title+"'s Child Activity "+i+" is: "+ca.childActivities[i].title)
			var tmp:Array = getEntries(ca.childActivities[i]);
			if(tmp.length > 0){
				var obj:Object = {}
				obj.entries = tmp;
				obj.child= ca.childActivities[i];
				array.push(obj);
			}
			
			//var tmp:Array = getEntries(ca.childActivities[i]);
			//drawIsRequiredChildTasks(ca, ca.childActivities[i], tmp);
			//return null;
		}
		for (var j=0; j<ca.contributeEntries.length; j++){ 
			trace("Contribute Entry for "+ca.title+" is: "+ca.contributeEntries[j].contributionType)
			if(ca.contributeEntries[j].isRequired){
				// show isRequired entry
				array.push(ca.contributeEntries[j]);
			}
		}
		return array;
	}
	
	/**
	 * Draws isRequired tasks
	 *   
	 * @param   ca    
	 * @param   array Holds CA required entries for CA and child CA's
	 * @return  
	 */
	
	private function drawIsRequiredTasks(ca:Object, array:Array, x:Number):Void{
		//var o:Object;
		
		if(array.length > 0){
			// write ca title / details to screen with x position
			requiredTaskList[listCount] = _monitorReqTask_mc.attachMovie("contributeActivityRow", "contributeActivityRow"+listCount, this._monitorReqTask_mc.getNextHighestDepth(), {_x:x, _y:19*listCount})
			reqTasks_scp.redraw(true);
			requiredTaskList[listCount].contributeActivity.background = true;
			requiredTaskList[listCount].contributeActivity._width=reqTasks_scp._width-20
			
			if (ca._parentActivityID == null){
				requiredTaskList[listCount].contributeActivity.text = "  "+ca.title
				requiredTaskList[listCount].contributeActivity.backgroundColor = 0xD5E6FF;
			}else {
				requiredTaskList[listCount].contributeActivity.text = "\t"+ca.title
				requiredTaskList[listCount].contributeActivity.backgroundColor = 0xF9F2DD;
			}
			
			listCount++
		}
		
		for(var i=0; i<array.length; i++){
			var o:Object = array[i];
			
			if(o instanceof ContributeActivity){
				// normal CA entries
				trace('write out entry with GO link'+o.taskURL);
				requiredTaskList[listCount] =_monitorReqTask_mc.attachMovie("contributeEntryRow", "contributeEntryRow"+listCount, this._monitorReqTask_mc.getNextHighestDepth(), {_x:x, _y:19*listCount})
				reqTasks_scp.redraw(true);
				requiredTaskList[listCount].contributeEntry.text = "\t\t"+mm.getMonitor().getCELiteral(o._contributionType);
				requiredTaskList[listCount].goContribute._x = reqTasks_scp._width-50
				requiredTaskList[listCount].goContribute.onRelease = function (){
					trace("Contrybute Type is: "+o.taskURL);
					getURL(String(o.taskURL), "_blank");
					//getURL("http://localhost:8080/lams/monitoring/monitoring.do?method=getAllContributeActivities&lessonID=4", "_blank");
				}
				requiredTaskList[listCount].goContribute.setStyle("fontSize", "9"); 
				listCount++
			}else{
				// child CA
				trace('child entries length:' + o.entrievs.length)
				if(o.entries.length > 0){
					trace('now drawing child');
					// write child ca title (indented - x + 10 position)
					drawIsRequiredTasks(o.child, o.entries, x);
				}
				
			}
		}
		
		reqTasks_scp.redraw(true)
	}
	
	/**
    * Opens the lesson manager dialog
    */
    public function showLessonManagerDialog(mm:MonitorModel) {
		trace('doing Lesson Manager popup...');
		trace('app root: ' + mm.getMonitor().root);
		trace('lfwindow: ' + LFWindow);
        var dialog:MovieClip = PopUpManager.createPopUp(mm.getMonitor().root, LFWindow, true,{title:"Edit Class",closeButton:true,scrollContentPath:'selectClass'});
		dialog.addEventListener('contentLoaded',Delegate.create(_monitorController,_monitorController.openDialogLoaded));
		
    }
	
	/**
    * Opens the lesson manager dialog
    */
    public function showLearnersDialog(mm:MonitorModel) {
		trace('doing Learners popup...');
		trace('app root: ' + mm.getMonitor().root);
		trace('lfwindow: ' + LFWindow);
        var opendialog:MovieClip = PopUpManager.createPopUp(mm.getMonitor().root, LFWindow, true,{title:"View Learners",closeButton:true,scrollContentPath:'learnersDialog'});
		opendialog.addEventListener('contentLoaded',Delegate.create(_monitorController,_monitorController.openDialogLoaded));
		
    }
	
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceDialog 
	 * @return  
	 */
	public function set dialog (dialog:MovieClip):Void {
		_dialog = dialog;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get dialog ():MovieClip {
		return _dialog;
	}
	
	public function setupLabels(){
		
		//max_lbl.text = Dictionary.getValue('pi_max_act');
		
		//populate the synch type combo:
		status_lbl.text = "Status:"
		learner_lbl.text = "Learners:"
		class_lbl.text = "Class:"
		elapsed_lbl.text = "Elapsed duration:"
		manageClass_lbl.text = "Class:"
		manageStatus_lbl.text = "Status:"
		manageStart_lbl.text = "Start:"
		manageDate_lbl.text = "Date"
			
		//Button
		viewLearners_btn.label = "View Learners"
		editClass_btn.label = "Edit Class"
		statusApply_btn.label = "Apply"
		schedule_btn.label = "Schedule"
		start_btn.label = "Start Now"
		
		_lessonStateArr = ["CREATED", "NOT_STARTED", "STARTED", "SUSPENDED", "FINISHED", "ARCHIVED", "DISABLED"];
		
		taskManager.border = true
		taskManager.borderColor = 0x003366;
		lessonManager.border = true
		lessonManager.borderColor = 0x003366;
		taskManager.background = true
		taskManager.backgroundColor = 0xEAEAEA;
		lessonManager.background = true
		lessonManager.backgroundColor = 0xEAEAEA;
		//Call to apply style to all the labels and input fields
		delete this.onEnterFrame; 
		setStyles();
			
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
        
		//LABELS
		var styleObj = _tm.getStyleObject('label');
		status_lbl.setStyle('styleName',styleObj);
		learner_lbl.setStyle('styleName',styleObj);
		class_lbl.setStyle('styleName',styleObj);
		manageClass_lbl.setStyle('styleName',styleObj);
		manageStatus_lbl.setStyle('styleName',styleObj);
		manageStart_lbl.setStyle('styleName',styleObj);
		manageDate_lbl.setStyle('styleName',styleObj);
		
		//BUTTONS
		styleObj = _tm.getStyleObject('button');
		viewLearners_btn.setStyle('styleName',styleObj);
		editClass_btn.setStyle('styleName',styleObj);
		schedule_btn.setStyle('styleName',styleObj);
		start_btn.setStyle('styleName',styleObj);
		statusApply_btn.setStyle('styleName',styleObj);
		
		//COMBO
		styleObj = _tm.getStyleObject('combo');
		changeStatus_cmb.setStyle('styleName',styleObj);
		scheduleDate_dt.setStyle('styleName',styleObj);
		
		//STEPPER
		styleObj = _tm.getStyleObject('numericstepper');
		//startHour_stp.setStyle('styleName',styleObj);
		//startMin_stp.setStyle('styleName',styleObj);
		
		//SCROLLPANE
		reqTasks_scp.border_mc.setStyle('_visible',false);
		
    }
    
	public function getScheduleDateTime(date:Date, timeStr:String):String{
		var bs:String = "%2F";		// backslash char
		var dayStr:String;
		var monthStr:String;
		
		trace('output time: ' + timeStr);
		var day = date.getDate();
		if(day<10){
			dayStr=String(0)+day;
		} else {
			dayStr=day.toString();
		}
		
		var month = date.getMonth()+1;
		if(month<10){
			monthStr=String(0)+month;
		} else {
			monthStr = month.toString();
		}
		
		var dateStr = dayStr + bs + monthStr + bs + date.getFullYear();
		trace('selected date: ' + dateStr);
		return dateStr + '+' + timeStr;
	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		trace("Monitor Tab Widtht: "+s.w+" Monitor Tab Height: "+s.h);
		lessonManager.setSize(s.w-20,lessonManager._height);
		taskManager.setSize(s.w-20,lessonManager._height);
		//qTasks_scp.setSize(s.w._width,reqTasks_scp._height);
		reqTasks_scp.setSize(s.w-30,reqTasks_scp._height);
		for (var i=0; i<requiredTaskList.length; i++){
			requiredTaskList[i].contributeActivity._width = reqTasks_scp._width-20;
		}
		//contributeActivity.textWidth
				
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
        var p:Object = mm.getPosition();
		trace("X pos set in Model is: "+p.x+" and Y pos set in Model is "+p.y)
		for (var i=0; i<requiredTaskList.length; i++){
			requiredTaskList[i].goContribute._x = reqTasks_scp._width-50;
		}	
        //this._x = p.x;
        //this._y = p.y;
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