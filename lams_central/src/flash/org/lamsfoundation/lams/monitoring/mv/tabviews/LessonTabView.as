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
	public static var NULL_CBI:Number = 0;
	public static var ACTIVE_CBI:Number = 1;
	public static var DISABLE_CBI:Number = 2;
	public static var ARCHIVE_CBI:Number = 3;
	public static var UNARCHIVE_CBI:Number = 4;
	
	public static var STARTED_STATUS:Number = 3;
	public static var SUSPENDED_STATUS:Number = 4;
	public static var ARCHIVED_STATUS:Number = 6;
	
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
	private var bkg_pnl:MovieClip;
	
	//Labels
	private var status_lbl:Label;
	private var learner_lbl:Label;
	private var class_lbl:Label;
	private var elapsed_lbl:Label;
	private var manageClass_lbl:Label;
	private var manageStatus_lbl:Label;
	private var manageStart_lbl:Label;
	private var manageDate_lbl:Label;
	private var btnLabel:String;
		
	//Text Items
    private var LSTitle_txt:TextField;
	private var LSDescription_txt:TextField;
	private var sessionStatus_txt:TextField;
	private var numLearners_txt:TextField;
	private var class_txt:TextField;
	private var duration_txt:TextField;
	private var startMsg_txt:TextField;
	
	private var lessonManager:TextField;
	private var taskManager:TextField;
	
	//Button
	private var viewLearners_btn:Button;
	private var editClass_btn:Button;
	private var statusApply_btn:Button;
	private var schedule_btn:Button;
	private var start_btn:Button;
	
	private var scheduleDate_dt:DateField;
	private var scheduleTime:MovieClip;
	
	//COMBO
	private var changeStatus_cmb:ComboBox;
	
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
		btnLabel = Dictionary.getValue('td_goContribute_btn');
		MovieClipUtils.doLater(Proxy.create(this,setupTab));
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
					hideMainExp(mm);
					//mm.setDirty();
					//MovieClipUtils.doLater(Proxy.create(this,draw));
					if(mm.getIsProgressChanged()){
						trace("I am calling reloadProgress now")
						reloadProgress(false);
					}
				}else {
					this._visible = false;
				}
				break;
			case 'SEQUENCE' :
				if (infoObj.tabID == _tabID){
				trace("TabID for Selected tab is (LessonTab): "+infoObj.tabID)
					this._visible = true;
					hideMainExp(mm);
					MovieClipUtils.doLater(Proxy.create(this,draw));
					
				}else {
					this._visible = false;
				}
				break;
			case 'RELOADPROGRESS' :	
					if (infoObj.tabID == _tabID){
						trace("called Reload progress")
						reloadProgress();
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
			case 'LEARNERS_LOADED' :
				_dialog.checkLearners(mm.organisation);
				break;
			case 'STAFF_LOADED' :
				_dialog.checkStaff(mm.organisation);
				break;
			case 'DRAW_DESIGN' :
				if (infoObj.tabID == _tabID){
					//drawDesignCalled = "called";
					trace("TabID for Selected tab is (LessonTab): "+infoObj.tabID)
					populateLessonDetails();
					//mm.setIsProgressChanged(false);
				}
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonTabView');
		}

	}
	
	private function setupTab(){
		
		_monitorController = getController();
		
		editClass_btn.addEventListener("click", _monitorController);
		viewLearners_btn.addEventListener("click", _monitorController);
		schedule_btn.addEventListener("click", Delegate.create(this, scheduleLessonStart));
		start_btn.addEventListener("click", _monitorController);
		statusApply_btn.addEventListener("click", Delegate.create(this, changeStatus))
		this.addEventListener("apply", Delegate.create(_monitorController, _monitorController.changeStatus));

	}
	
	private function hideMainExp(mm:MonitorModel):Void{
		//var mcontroller = getController();
		mm.broadcastViewUpdate("EXPORTSHOWHIDE", false)
	}
	
	/**
	 * Reloads the learner Progress and 
	 * @Param isChanged Boolean Value to pass it to setIsProgressChanged in monitor model so 		that it sets it to true if refresh button is clicked and sets it to fasle as soon as latest data is loaded and design is redrawn.
	 * @usage   
	 * @return  nothing
	 */
	private function reloadProgress(isChanged:Boolean){
		
			trace("reloading Progress data for Learners")
			//mm.getMonitor().reloadLessonToMonitor();
			
		
			
			
			if (isChanged == false){
				mm.setIsProgressChanged(false);
				
			}else {
				mm.setIsProgressChanged(true);
			}
			mm.getMonitor().getProgressData(mm.getSequence());
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
		
		trace("Loaded LessonTabView Data"+ this)
	
		startMsg_txt.visible = false;
	
		var seq:Sequence = mm.getSequence();
	
		populateStatusList(seq.state);
		populateLessonDetails();
		enableEditClass(seq.state);
		
		var requestLessonID:Number = seq.ID;
		
		trace('seq id: ' + mm.getSequence().ID);
		trace('last seq id: ' + mm.getLastSelectedSequence().ID);
		if (mm.getSequence().ID == mm.getLastSelectedSequence().ID){
			if(mm.getToDos() == null){
				mm.getMonitor().getContributeActivities(mm.getSequence().ID);
			} else {
				populateContributeActivities();
			}
		}else{
			mm.getMonitor().getContributeActivities(mm.getSequence().ID);
		}
		
		
		setStyles();
		
		dispatchEvent({type:'load',target:this});
	}
	
	/**
	 * Populate the lesson details from HashTable Sequence in MOnitorModel
	*/
	private function populateLessonDetails():Void{
		
		var s:Object = mm.getSequence();
		
		LSTitle_txt.text = s.name;
		LSDescription_txt.text = s.description;
		sessionStatus_txt.text = showStatus(s.state);
		numLearners_txt.text = String(s.noStartedLearners) + " "  + Dictionary.getValue('ls_of_text')+" "+String(s.noPossibleLearners);
		trace("current logged in learners are: "+mm.allLearnersProgress.length)
		//numLearners_txt.text = mm.allLearnersProgress.length + " "  + Dictionary.getValue('ls_of_text')+" "+String(s.noPossibleLearners);
		class_txt.text = s.organisationName;
	}
	
	private function populateStatusList(stateID:Number):Void{
		changeStatus_cmb.removeAll();
		
		switch(stateID){
			case Sequence.SUSPENDED_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_enable'), LessonTabView.ACTIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				break;
			case Sequence.ARCHIVED_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_activate'), LessonTabView.UNARCHIVE_CBI);
				break;
			case Sequence.ACTIVE_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				break;
			case Sequence.NOTSTARTED_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				break;
			default :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_disable'), LessonTabView.DISABLE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				
		}
	
	}
	private function enableEditClass(stateID:Number):Void{
		
		switch(stateID){
			case Sequence.ACTIVE_STATE_ID :
				showStartFields(true);
				editClass_btn.enabled = true;
				break;
			case Sequence.NOTSTARTED_STATE_ID :
				showStartFields(true);
				editClass_btn.enabled = true;
				break;
			default :
				showStartFields(false);
				editClass_btn.enabled = false;
			
		}
	}
	
	private function showStartFields(b:Boolean){
		scheduleTime._visible = b;
		scheduleDate_dt.visible = b;
		start_btn.visible = b;
		schedule_btn.visible = b;
		manageDate_lbl.visible = b;
		
		/**	
			if(seq.isStarted()){
				startMsg_txt.text = "Currently Started."
			} else {
				startMsg_txt.text = "Scheduled to start at "
			}
			
			startMsg_txt.visible = true;
			*/
	}

	private function showStatus(seqStatus:Number):String{
		var seqStat:String;
		
		switch(seqStatus){
			case LessonTabView.ARCHIVED_STATUS :
				seqStat = Dictionary.getValue('ls_status_archived_lbl');
				break;
			case LessonTabView.STARTED_STATUS :
				seqStat = Dictionary.getValue('ls_status_started_lbl');
				break;
			case LessonTabView.SUSPENDED_STATUS :
				seqStat = Dictionary.getValue('ls_status_disabled_lbl');
				break;
			default:
				seqStat = Dictionary.getValue('ls_status_active_lbl');
		}
		
		return seqStat;
	}
	
	/**
	 * Apply status change
	 *   
	 * 
	 * @param   evt Apply onclick event
	 */
	private function changeStatus(evt:Object):Void{
		dispatchEvent({type:"apply", target: this});
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
				requiredTaskList[listCount] =_monitorReqTask_mc.attachMovie("contributeEntryRow", "contributeEntryRow"+listCount, this._monitorReqTask_mc.getNextHighestDepth(), {_x:x, _y:19*listCount, buttonLabel:btnLabel})
				reqTasks_scp.redraw(true);
				requiredTaskList[listCount].contributeEntry.text = "\t\t"+mm.getMonitor().getCELiteral(o._contributionType);
				requiredTaskList[listCount].goContribute._x = reqTasks_scp._width-50
				//requiredTaskList[listCount].goContribute.label = Dictionary.getValue('td_goContribute_btn');
				requiredTaskList[listCount].goContribute.onRelease = function (){
					trace("Contribute Type is: "+o.taskURL);
					JsPopup.getInstance().launchPopupWindow(o.taskURL, 'ContributeActivity', 398, 570, true, true, false, false, false);
					
					//getURL(String(o.taskURL), "_blank");
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
        var dialog:MovieClip = PopUpManager.createPopUp(mm.getMonitor().root, LFWindow, true,{title:Dictionary.getValue('ls_win_editclass_title'),closeButton:true,scrollContentPath:'selectClass'});
		dialog.addEventListener('contentLoaded',Delegate.create(_monitorController,_monitorController.openDialogLoaded));
		
    }
	
	/**
    * Opens the lesson manager dialog
    */
    public function showLearnersDialog(mm:MonitorModel) {
		trace('doing Learners popup...');
		trace('app root: ' + mm.getMonitor().root);
		trace('lfwindow: ' + LFWindow);
        var opendialog:MovieClip = PopUpManager.createPopUp(mm.getMonitor().root, LFWindow, true,{title:Dictionary.getValue('ls_win_learners_title'),closeButton:true,scrollContentPath:'learnersDialog'});
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
		status_lbl.text = Dictionary.getValue('ls_status_lbl');
		learner_lbl.text = Dictionary.getValue('ls_learners_lbl');
		class_lbl.text = Dictionary.getValue('ls_class_lbl');
		elapsed_lbl.text = Dictionary.getValue('ls_duration_lbl');
		manageClass_lbl.text = Dictionary.getValue('ls_manage_class_lbl');
		manageStatus_lbl.text = Dictionary.getValue('ls_manage_status_lbl');
		manageStart_lbl.text = Dictionary.getValue('ls_manage_start_lbl');
		manageDate_lbl.text = Dictionary.getValue('ls_manage_date_lbl');
			
		//Button
		viewLearners_btn.label = Dictionary.getValue('ls_manage_learners_btn');
		editClass_btn.label = Dictionary.getValue('ls_manage_editclass_btn');
		statusApply_btn.label = Dictionary.getValue('ls_manage_apply_btn');
		schedule_btn.label = Dictionary.getValue('ls_manage_schedule_btn');
		start_btn.label = Dictionary.getValue('ls_manage_start_btn');
		
		//_lessonStateArr = ["CREATED", "NOT_STARTED", "STARTED", "SUSPENDED", "FINISHED", "ARCHIVED", "DISABLED"];
		
		taskManager.border = true
		taskManager.borderColor = 0x003366;
		taskManager.text = Dictionary.getValue('ls_tasks_txt');
		
		lessonManager.border = true
		lessonManager.borderColor = 0x003366;
		lessonManager.text = Dictionary.getValue('ls_manage_txt');
		
		taskManager.background = true
		taskManager.backgroundColor = 0xEAEAEA;
		lessonManager.background = true
		lessonManager.backgroundColor = 0xEAEAEA;
		
		delete this.onEnterFrame; 
		
		//Call to apply style to all the labels and input fields
		setStyles();
			
	}
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and apply them
	 * directly to the instance
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
		
		//BG PANEL
		styleObj = _tm.getStyleObject('BGPanel');
		bkg_pnl.setStyle('styleName',styleObj);
		
		//STEPPER
		//styleObj = _tm.getStyleObject('numericstepper');
		//startHour_stp.setStyle('styleName',styleObj);
		//startMin_stp.setStyle('styleName',styleObj);
		
		//SCROLLPANE
		styleObj = _tm.getStyleObject('scrollpane');
		reqTasks_scp.setStyle('styleName', styleObj);
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
		bkg_pnl.setSize(s.w-20,s.h);
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