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


import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.monitoring.mv.*
import org.lamsfoundation.lams.monitoring.Application;
import org.lamsfoundation.lams.monitoring.ContributeActivity;
import org.lamsfoundation.lams.wizard.*
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ToolTip;
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
	public static var REMOVE_CBI:Number = 5;
	
	public static var NOT_STARTED_STATUS:Number = 2;
	public static var STARTED_STATUS:Number = 3;
	public static var SUSPENDED_STATUS:Number = 4;
	public static var ARCHIVED_STATUS:Number = 6;
	public static var REMOVED_STATUS:Number = 7;
	
	private var _className = "LessonTabView";
	
	//constants:
	private var _tm:ThemeManager;
	private var _tip:ToolTip;
	private var mm:MonitorModel;
	
	private var isLessonLaunchChecked:Boolean;
	
	//TabView clips
	private var reqTasks_depth:Number = 4000;
	private var listCount:Number = 0; 
	private var requiredTaskList:Array = new Array();
	private var _monitorReqTask_mc:MovieClip;
	private var monitorLesson_scp:MovieClip;
	private var reqTasks_scp:MovieClip;
	private var monitorTabs_tb:MovieClip;
	private var _monitorLesson_mc:MovieClip;
	private var _lessonStateArr:Array;
	private var bkg_pnl:MovieClip;
	
	//Labels
	private var status_lbl:Label;
	private var learner_lbl:Label;
	private var learnerURL_lbl:Label;
	private var learnerURL_txt:TextInput;
	private var class_lbl:Label;
	private var elapsed_lbl:Label;
	private var manageClass_lbl:Label;
	private var manageStatus_lbl:Label;
	private var manageStart_lbl:Label;
	private var manageDate_lbl:Label;
	private var manageTime_lbl:Label;
	private var start_date_lbl:Label;
	private var schedule_date_lbl:Label;
	private var btnLabel:String;
	private var learner_expp_cb:CheckBox;
	private var learner_expp_cb_lbl:Label;
	
	//Text Items
	private var LSTitle_lbl:Label;
	private var LSDescription_lbl:Label;
	
	private var sessionStatus_txt:Label;
	private var numLearners_txt:Label;
	private var class_txt:Label;
	private var duration_txt:TextField;
	private var startMsg_txt:TextField;
	private var editLockMessage_txt:TextField;
	
	private var lessonManager:TextField;
	private var taskManager:TextField;
	private var lessonManager_lbl:Label;
	private var taskManager_lbl:Label;
	
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

	
	/**
	* Constructor
	*/
	function LessonTabView(){
		trace("loaded lesson tab view")
		_lessonTabView = this;
		
		_tm = ThemeManager.getInstance();
		_tip = new ToolTip();
		
		//Init for event delegation
        mx.events.EventDispatcher.initialize(this);
		
	}
	
	/**
	* Called to initialise Canvas  . CAlled by the Canvas container
	*/
	public function init(m:Observable,c:Controller){
		super (m, c);
		
		isLessonLaunchChecked = true;
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
				if (infoObj.tabID == _tabID && !mm.locked){
					
					mm.getMonitor().getMV().getMonitorLessonScp()._visible = true;
					hideMainExp(mm);
					mm.broadcastViewUpdate("JOURNALSSHOWHIDE", false);
					
					if(mm.getIsProgressChangedLesson()){
						trace("I am calling reloadProgress now")
						reloadProgress(false);
					}
					
					setMenu();
				
				} else {
					mm.getMonitor().getMV().getMonitorLessonScp()._visible = false;
				}
				break;
			case 'SEQUENCE' :
				if (infoObj.tabID == _tabID && !mm.locked){
						mm.getMonitor().getMV().getMonitorLessonScp()._visible = true;
						hideMainExp(mm);
						mm.broadcastViewUpdate("JOURNALSSHOWHIDE", false);
						
						MovieClipUtils.doLater(Proxy.create(this,draw));
					
				}else {
					mm.getMonitor().getMV().getMonitorLessonScp()._visible = false;
				}
				break;
			case 'RELOADPROGRESS' :	
					if (infoObj.tabID == _tabID && !mm.locked){
						trace("called Reload progress")
						reloadProgress(true);
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
				if (infoObj.tabID == _tabID && !mm.locked){
					populateLessonDetails();
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
		statusApply_btn.addEventListener("click", Proxy.create(this, changeStatus));
		learner_expp_cb.addEventListener("click", Delegate.create(this, toogleExpPortfolio));
		this.addEventListener("apply", Proxy.create(_monitorController, _monitorController.changeStatus));
		
		editClass_btn.onRollOver = Proxy.create(this,this['showToolTip'], editClass_btn, "ls_manage_editclass_btn_tooltip");
		editClass_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		viewLearners_btn.onRollOver = Proxy.create(this,this['showToolTip'], viewLearners_btn, "ls_manage_learners_btn_tooltip");
		viewLearners_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		schedule_btn.onRollOver = Proxy.create(this,this['showToolTip'], schedule_btn, "ls_manage_schedule_btn_tooltip");
		schedule_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		start_btn.onRollOver = Proxy.create(this,this['showToolTip'], start_btn, "ls_manage_start_btn_tooltip");
		start_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		statusApply_btn.onRollOver = Proxy.create(this,this['showToolTip'], statusApply_btn, "ls_manage_apply_btn_tooltip");
		statusApply_btn.onRollOut = Proxy.create(this,this['hideToolTip']);
		
		setScheduleDateRange();

		dispatchEvent({type:'load',target:this});
	}


	private function setScheduleDateRange():Void{
		
		var mydate = new Date();
		var year = mydate.getFullYear();
		var month = mydate.getMonth();
		var date = mydate.getDate();
		
		Debugger.log('schedule date range starts from :'+date + "/" +month+ "/" +year,Debugger.CRITICAL,'setScheduleDateRange','org.lamsfoundation.lams.WizardView');
		scheduleDate_dt.selectableRange = {rangeStart: new Date(year, month, date)};
	}
	
	
	private function hideMainExp(mm:MonitorModel):Void{
		mm.broadcastViewUpdate("EXPORTSHOWHIDE", false)
		mm.broadcastViewUpdate("EDITFLYSHOWHIDE", false);
	}
	

	/**
	 * Reloads the learner Progress and 
	 * @Param isChanged Boolean Value to pass it to setIsProgressChanged in monitor model so 		that it sets it to true if refresh button is clicked and sets it to fasle as soon as latest data is loaded and design is redrawn.
	 * @usage   
	 * @return  nothing
	 */
	private function reloadProgress(isChanged:Boolean){
		
			if (isChanged == false){
				mm.setIsProgressChangedLesson(false);
				
			}else {
				mm.setIsProgressChangedLearner(true);
				mm.setIsProgressChangedSequence(true)
			}
			mm.getMonitor().reloadLessonToMonitor();
	}
	
	/**
    * layout visual elements on the canvas on initialisation
    */
	private function draw(){
		Debugger.log('Lesson Launch set in sysadmin :'+_root.lessonLaunch, Debugger.CRITICAL,'Draw','org.lamsfoundation.lams.LessonTabView');
		listCount = 0; 
		this.onEnterFrame = setupLabels;
		
		_monitorReqTask_mc = reqTasks_scp.content;
		_monitorController = getController();
		
		startMsg_txt.visible = false;
		editLockMessage_txt.visible = false;
	
		var seq:Sequence = mm.getSequence();
		
		if (_root.lessonLaunch == "false" && isLessonLaunchChecked){
			rearrangeAll();		
			isLessonLaunchChecked = false;
		}
		
		populateStatusList(seq.state);
		populateLessonDetails();
		enableEditClass(seq.state);
		
		var requestLessonID:Number = seq.ID;
		
		if (mm.getSequence().ID == mm.getLastSelectedSequence().ID){
			if(mm.getToDos() == null){
				mm.getMonitor().getContributeActivities(mm.getSequence().ID);
			} else {
				populateContributeActivities();
			}
		}else{
			mm.getMonitor().getContributeActivities(mm.getSequence().ID);
		}
		
		setMenu();
		setStyles();
		mm.getMonitor().getMV().getMonitorLessonScp().redraw(true);
		
		
	}
	
	private function rearrangeAll():Void{
		learnerURL_lbl.visible = false;
		learnerURL_txt.visible = false;
		class_lbl._y = class_lbl._y - 30
		class_txt._y = class_txt._y - 30
		lessonManager._y = lessonManager._y - 30
		taskManager._y = taskManager._y - 30

		lessonManager_lbl._x = lessonManager._x + 2
		lessonManager_lbl._y = lessonManager._y
		taskManager_lbl._x = taskManager._x + 2
		taskManager_lbl._y = taskManager._y
		
		manageClass_lbl._y = manageClass_lbl._y - 30
		manageStatus_lbl._y = manageStatus_lbl._y - 30
		manageStart_lbl._y = manageStart_lbl._y - 30
		manageDate_lbl._y = manageDate_lbl._y - 30
		manageTime_lbl._y = manageTime_lbl._y - 30
		start_date_lbl._y = start_date_lbl._y - 30
		schedule_date_lbl._y = schedule_date_lbl._y - 30
		scheduleDate_dt._y = scheduleDate_dt._y - 30
		scheduleTime._y = scheduleTime._y - 30
		viewLearners_btn._y = viewLearners_btn._y - 30
		editClass_btn._y = editClass_btn._y - 30
		changeStatus_cmb._y = changeStatus_cmb._y - 30
		statusApply_btn._y = statusApply_btn._y - 30
		schedule_btn._y = schedule_btn._y - 30
		start_btn._y = start_btn._y - 30
		reqTasks_scp._y = reqTasks_scp._y - 30
		learner_expp_cb._y = learner_expp_cb._y - 30
		learner_expp_cb_lbl._y = learner_expp_cb_lbl._y - 30
		
		
	}
	/**
	 * Populate the lesson details from HashTable Sequence in MOnitorModel
	*/
	private function populateLessonDetails():Void{
		
		var s:Object = mm.getSequence();
		var limit:Number = 120;
		
		LSTitle_lbl.text = "<b>" + s.name + "</b>";
		LSDescription_lbl.text = String(s.description).substr(0, limit);
		
		if(s.description.length > limit) { LSDescription_lbl.text += "..."; }
		
		sessionStatus_txt.text = showStatus(s.state);
		numLearners_txt.text = String(s.noStartedLearners) + " "  + Dictionary.getValue('ls_of_text')+" "+String(s.noPossibleLearners);
		learnerURL_txt.text = _root.serverURL+"launchlearner.do?lessonID="+_root.lessonID;

		class_txt.text = s.organisationName;
		learner_expp_cb.selected = s.learnerExportAvailable;
	}
	
	private function populateStatusList(stateID:Number):Void{
		changeStatus_cmb.removeAll();
		
		switch(stateID){
			case Sequence.SUSPENDED_STATE_ID : 
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_enable'), LessonTabView.ACTIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_remove'), LessonTabView.REMOVE_CBI);	
				break;
			case Sequence.ARCHIVED_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_activate'), LessonTabView.UNARCHIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_remove'), LessonTabView.REMOVE_CBI);
				break;
			case Sequence.ACTIVE_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_disable'), LessonTabView.DISABLE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_remove'), LessonTabView.REMOVE_CBI);
				break;
			case Sequence.NOTSTARTED_STATE_ID :  
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_remove'), LessonTabView.REMOVE_CBI);
				break;
			case Sequence.REMOVED_STATE_ID :
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				break;
			default :  // started state
				changeStatus_cmb.addItem(Dictionary.getValue('ls_manage_status_cmb'), LessonTabView.NULL_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_disable'), LessonTabView.DISABLE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_archive'), LessonTabView.ARCHIVE_CBI);
				changeStatus_cmb.addItem(Dictionary.getValue('ls_status_cmb_remove'), LessonTabView.REMOVE_CBI);
		}
	
	}
	
	private function enableEditClass(stateID:Number):Void{
		
		switch(stateID){
			case Sequence.ACTIVE_STATE_ID :
				showStartFields(true, true);
				editClass_btn.enabled = true;
				break;
			case Sequence.NOTSTARTED_STATE_ID :
				showStartFields(true, false);
				editClass_btn.enabled = true;
				break;
			case Sequence.STARTED_STATE_ID :
				showStartFields(false, false);
				editClass_btn.enabled = true;
				break;
			default :
				showStartFields(false, false);
				editClass_btn.enabled = false;
			
		}
	}
	
	private function showStartFields(a:Boolean, b:Boolean){
		var s:Object = mm.getSequence();
		
		// is started?
		if(s.isStarted){
			schedule_date_lbl.visible = false;
			start_date_lbl.text = s.getStartDateTime();
			start_date_lbl.visible = true;
					
		} else {
			// is scheduled to start?
			start_date_lbl.visible = false;
			if(s.isScheduled){
				schedule_date_lbl.visible = true;
				schedule_date_lbl.text = s.getScheduleDateTime();
				
			} else {
				schedule_date_lbl.visible = false;
			}
			
		}
		
		start_btn.visible = a;

		scheduleTime._visible = b;
		scheduleDate_dt.visible = b;
		schedule_btn.visible = b;
		manageDate_lbl.visible = b;
		manageTime_lbl.visible = b;
		
	}

	public function showStatus(seqStatus:Number):String{
		var seqStat:String;
		var s:Object = mm.getSequence();
		
		switch(seqStatus){
			case LessonTabView.ARCHIVED_STATUS :
				seqStat = Dictionary.getValue('ls_status_archived_lbl');
				break;
			case LessonTabView.REMOVED_STATUS :
				seqStat = Dictionary.getValue('ls_status_removed_lbl');
				break;
			case LessonTabView.STARTED_STATUS :
				seqStat = Dictionary.getValue('ls_status_started_lbl');
				break;
			case LessonTabView.SUSPENDED_STATUS :
				seqStat = Dictionary.getValue('ls_status_disabled_lbl');
				break;
			case LessonTabView.NOT_STARTED_STATUS:
				if(s.isScheduled){ seqStat = Dictionary.getValue('ls_status_scheduled_lbl'); }
				else {
					seqStat = Dictionary.getValue('ls_status_active_lbl');
				}
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
	
	public function scheduleLessonStart(evt:Object):Void{
		 Debugger.log('setting Schedule Date :' + scheduleDate_dt.selectedDate,Debugger.CRITICAL,'scheduleLessonStart','org.lamsfoundation.lams.LessonTabView');
		if (scheduleDate_dt.selectedDate == null || scheduleDate_dt.selectedDate == undefined){
			LFMessage.showMessageAlert(Dictionary.getValue('al_validation_schstart'), null, null);
		}else {
			var schDT = getScheduleDateTime(scheduleDate_dt.selectedDate, scheduleTime.f_returnTime());
			if (!schDT.validTime){
				LFMessage.showMessageAlert(Dictionary.getValue('al_validation_schtime'), null, null);
				return;
			}else {
				mm.getMonitor().startLesson(true, _root.lessonID, schDT.dateTime);
			}
			
		}
		
	}

	private function populateContributeActivities():Void{
		if (requiredTaskList.length == 0){
			var todos:Array = mm.getToDos();
			
			// show isRequired activities in scrollpane
			for (var i=0; i<todos.length; i++){
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
			var tmp:Array = getEntries(ca.childActivities[i]);
			if(tmp.length > 0){
				var obj:Object = {}
				obj.entries = tmp;
				obj.child= ca.childActivities[i];
				array.push(obj);
			}
		}
		
		for (var j=0; j<ca.contributeEntries.length; j++){ 
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
				
				requiredTaskList[listCount].goContribute.onRelease = function (){
					trace("Contribute Type is: "+o.taskURL);
					JsPopup.getInstance().launchPopupWindow(o.taskURL, 'ContributeActivity', 600, 800, true, true, false, false, false);
				}
				
				requiredTaskList[listCount].goContribute.onRollOver = Proxy.create(this,this['showToolTip'], requiredTaskList[listCount].goContribute, "goContribute_btn_tooltip", reqTasks_scp._y+requiredTaskList[listCount]._y+requiredTaskList[listCount]._height, reqTasks_scp._x);
				requiredTaskList[listCount].goContribute.onRollOut = Proxy.create(this,this['hideToolTip']);
				
				var styleObj = _tm.getStyleObject('button');
				requiredTaskList[listCount].goContribute.setStyle('styleName',styleObj); 
				
				listCount++;
			
			}else{
				// child CA
				if(o.entries.length > 0){
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
		var dialog:MovieClip = PopUpManager.createPopUp(mm.getMonitor().root, LFWindow, true,{title:Dictionary.getValue('ls_win_editclass_title'),closeButton:true,scrollContentPath:'selectClass'});
		dialog.addEventListener('contentLoaded',Delegate.create(_monitorController,_monitorController.openDialogLoaded));
		
    }
	
	/**
    * Opens the lesson manager dialog
    */
    public function showLearnersDialog(mm:MonitorModel) {
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
	
	public function showToolTip(btnObj, btnTT:String, goBtnYpos:Number, goBtnXpos:Number):Void{
		var ttData = mm.getTTData();
		
		if(goBtnYpos != null && goBtnXpos != null){
			var xpos:Number = mm.getMonitor().getMV().getMonitorLessonScp().width - 150;
			var ypos:Number = ttData.monitorY + (goBtnYpos +5);
		}else {
			var xpos:Number = ttData.monitorX + btnObj._x;
			var ypos:Number = ttData.monitorY + (btnObj._y+btnObj.height)+5;
		}
		
		var ttMessage = Dictionary.getValue(btnTT);
		_tip.DisplayToolTip(ttData.ttHolderMC, ttMessage, xpos, ypos);
	}
	
	public function hideToolTip():Void{
		_tip.CloseToolTip();
	}
	public function setupLabels(){
		
		//populate the synch type combo:
		status_lbl.text = "<b>"+Dictionary.getValue('ls_status_lbl')+"</b>";
		learner_lbl.text = "<b>"+Dictionary.getValue('ls_learners_lbl')+"</b>";
		learnerURL_lbl.text = "<b>"+Dictionary.getValue('ls_learnerURL_lbl')+"</b>";
		class_lbl.text = "<b>"+Dictionary.getValue('ls_class_lbl')+"</b>";
		elapsed_lbl.text = "<b>"+Dictionary.getValue('ls_duration_lbl')+"</b>";
		manageClass_lbl.text = "<b>"+Dictionary.getValue('ls_manage_class_lbl')+"</b>";
		manageStatus_lbl.text = "<b>"+Dictionary.getValue('ls_manage_status_lbl')+"</b>";
		manageStart_lbl.text = "<b>"+Dictionary.getValue('ls_manage_start_lbl')+"</b>";
		manageDate_lbl.text = Dictionary.getValue('ls_manage_date_lbl');
		manageTime_lbl.text = Dictionary.getValue('ls_manage_time_lbl');
		learner_expp_cb_lbl.text = Dictionary.getValue('ls_manage_learnerExpp_lbl');
		
		//Button
		viewLearners_btn.label = Dictionary.getValue('ls_manage_learners_btn');
		editClass_btn.label = Dictionary.getValue('ls_manage_editclass_btn');
		statusApply_btn.label = Dictionary.getValue('ls_manage_apply_btn');
		schedule_btn.label = Dictionary.getValue('ls_manage_schedule_btn');
		start_btn.label = Dictionary.getValue('ls_manage_start_btn');
		
		taskManager.border = true
		taskManager.borderColor = 0x003366;
		taskManager_lbl.text = Dictionary.getValue('ls_tasks_txt');
		taskManager_lbl._x = taskManager._x + 2;
		taskManager_lbl._y = taskManager._y;
		
		lessonManager.border = true
		lessonManager.borderColor = 0x003366;
		lessonManager_lbl.text = Dictionary.getValue('ls_manage_txt');
		lessonManager_lbl._x = lessonManager._x + 2;
		lessonManager_lbl._y = lessonManager._y;
		
		taskManager.background = true
		taskManager.backgroundColor = 0xEAEAEA;
		lessonManager.background = true
		lessonManager.backgroundColor = 0xEAEAEA;
		
		delete this.onEnterFrame; 
		
		//Call to apply style to all the labels and input fields
		setStyles();
			
	}
	
	
	private function toogleExpPortfolio(evt:Object) {
		Debugger.log("Toogle Staff Selection", Debugger.GEN, "toogleStaffSelection", "WizardView");
		var target:CheckBox = CheckBox(evt.target);
		
		var callback:Function = Proxy.create(this,confirmOutput);
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=learnerExportPortfolioAvailable&lessonID='+_root.lessonID+'&learnerExportPortfolio='+target.selected, callback, false);
	}
	
	
	public function confirmOutput(r):Void{
		if(r instanceof LFError) {
				r.showErrorAlert();
		} else {
			if (learner_expp_cb.selected){
				var msg:String = Dictionary.getValue('ls_confirm_expp_enabled') ;
				LFMessage.showMessageAlert(msg);

			}else {
				var msg:String = Dictionary.getValue('ls_confirm_expp_disabled') ;
				LFMessage.showMessageAlert(msg);
			}
			
		}
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
		learnerURL_lbl.setStyle('styleName',styleObj);
		class_lbl.setStyle('styleName',styleObj);
		manageClass_lbl.setStyle('styleName',styleObj);
		manageStatus_lbl.setStyle('styleName',styleObj);
		manageStart_lbl.setStyle('styleName',styleObj);
		
		schedule_date_lbl.setStyle('styleName', styleObj);
		sessionStatus_txt.setStyle('styleName', styleObj);
		numLearners_txt.setStyle('styleName', styleObj);
		class_txt.setStyle('styleName', styleObj);
		lessonManager_lbl.setStyle('styleName', styleObj);
		taskManager_lbl.setStyle('styleName', styleObj);
		
		// Check box label
		learner_expp_cb_lbl.setStyle('styleName', styleObj);
		
		
		//SMALL LABELS
		styleObj = _tm.getStyleObject('PIlabel');
		manageDate_lbl.setStyle('styleName',styleObj);
		manageTime_lbl.setStyle('styleName',styleObj);
		
		
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
	
	private function setMenu(b:Boolean):Void{
		var fm:Menu = LFMenuBar.getInstance().fileMenu;
		fm.setMenuItemEnabled(fm.getMenuItemAt(1), editClass_btn.enabled);
		fm.setMenuItemEnabled(fm.getMenuItemAt(2), start_btn.visible);
		fm.setMenuItemEnabled(fm.getMenuItemAt(3), schedule_btn.visible);
		
		var vm:Menu = LFMenuBar.getInstance().viewMenu;
		vm.setMenuItemEnabled(vm.getMenuItemAt(0), true);
	}
    
	public function getScheduleDateTime(date:Date, timeStr:String):Object{
		var bs:String = "%2F";		// backslash char
		var dayStr:String;
		var monthStr:String;
		var mydate = new Date();
		var dtObj = new Object();
		
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
		
		if (day == mydate.getDate() && month == mydate.getMonth()+1 && date.getFullYear() == mydate.getFullYear()){
			dtObj.validTime = validateTime()
		} else {
			dtObj.validTime = true
		}
		
		dtObj.dateTime = dateStr + '+' + timeStr;
		return dtObj;
	}
	
	private function validateTime():Boolean{
		var mydate = new Date();
		var checkHours:Number;
		var hours = mydate.getHours();
		var minutes = mydate.getMinutes();
		var selectedHours = Number(scheduleTime.tHour.text);
		var selectedMinutes = Number(scheduleTime.tMinute.text);
		if (scheduleTime.tMeridian.selectedItem.data == "AM"){
			checkHours = 0
		}
		
		if (scheduleTime.tMeridian.selectedItem.data == "PM"){
			if (Number (selectedHours) == 12){
				checkHours = 0
			}else {
				checkHours = 12
			}
		}
		
		
		if (hours > (Number(selectedHours+checkHours))){
			return false;
		}else if (hours == Number(selectedHours+checkHours)){
			if (minutes > selectedMinutes){
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	
	/**
    * Sets the size of the canvas on stage, called from update
    */
	private function setSize(mm:MonitorModel):Void{
        var s:Object = mm.getSize();
		
		lessonManager.setSize(s.w-20,lessonManager._height);
		taskManager.setSize(s.w-20,lessonManager._height);
		
		var scpHeight:Number = mm.getMonitor().getMV().getMonitorLessonScp()._height;
		reqTasks_scp.setSize(s.w-30,(scpHeight-reqTasks_scp._y)-20);
		
		for (var i=0; i<requiredTaskList.length; i++){
			requiredTaskList[i].contributeActivity._width = reqTasks_scp._width-20;
			requiredTaskList[i].goContribute._x = reqTasks_scp._width-50
		}
		mm.getMonitor().getMV().getMonitorLessonScp().redraw(true);
	}
	
	 /**
    * Sets the position of the canvas on stage, called from update
    * @param cm Canvas model object 
    */
	private function setPosition(mm:MonitorModel):Void{
        var p:Object = mm.getPosition();
		
		for (var i=0; i<requiredTaskList.length; i++){
			requiredTaskList[i].goContribute._x = reqTasks_scp._width-50;
		}	
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