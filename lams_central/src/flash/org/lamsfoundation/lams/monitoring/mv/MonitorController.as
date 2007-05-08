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

import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.monitoring.mv.*;
import org.lamsfoundation.lams.monitoring.mv.tabviews.*;
import org.lamsfoundation.lams.authoring.Activity;
import mx.utils.*
import mx.controls.*
import mx.behaviors.*
import mx.core.*
import mx.events.*
import mx.effects.*

/**
* Controller for the sequence library
*/
class MonitorController extends AbstractController {
	private var _monitorModel:MonitorModel;
	private var _monitorController:MonitorController;
	private var _app:Application;
	private var _isBusy:Boolean;
	
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function MonitorController (mm:Observable) {
		super (mm);
		_monitorModel = MonitorModel(model);
		_monitorController = this;
		_app = Application.getInstance();
		_isBusy = false;
		
	}
	
	public function activityClick(act:Object, forObj:String):Void{
		//if (ca.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
		if (forObj == "LearnerIcon"){
			_monitorModel.isDragging = true;
			act.startDrag(false);
			//Cursor.showCursor(Application.C_WHITEARROW);
			Debugger.log('activityClick CanvasActivity:'+act.Learner.getUserName(),Debugger.GEN,'activityClick','MonitorController');
		}else {
			_monitorModel.selectedItem = act;
		}
   }
   
	public function activityRelease(act:Object, forObj:String):Void{
		Debugger.log('activityRelease CanvasActivity:'+act.activity.activityID,Debugger.GEN,'activityRelease','MonitorController');
		if (forObj == "LearnerIcon"){
			if(_monitorModel.isDragging){
				act.stopDrag();
			}
			var hasHit:Boolean = false;
			var actUIIDToCompare = act.activity.activityUIID;
			if (act.activity.parentUIID != null){
				var parentAct:Activity = _monitorModel.getMonitor().ddm.getActivityByUIID(act.activity.parentUIID)
				actUIIDToCompare = parentAct.activityUIID;
				
			}
			var indexArray:Array = _monitorModel.activitiesOnCanvas()	//setDesignOrder();
			var currentActOrder:Number = checkLearnerCurrentActivity(indexArray, actUIIDToCompare)
			
			trace("current activity order: "+currentActOrder)
			//run a loop to check which activity has been hitted by the learner.
			for (var i=0; i<indexArray.length; i++){ 
				trace("acitity "+i+" in loop is: "+indexArray[i].activity.title)
				if (act.hitTest(indexArray[i])){
					var actHitOrder:Number = checkLearnerCurrentActivity(indexArray, indexArray[i].activity.activityUIID)
					//if learner is on the next activity - get new progress data.
					if (actHitOrder > currentActOrder){
						
						var URLToSend:String = _root.monitoringURL+"forceComplete&lessonID="+_root.lessonID+"&learnerID="+act.Learner.getLearnerId()+"&activityID="+indexArray[i-1].activity.activityID
						var ref = this;
						var fnOk:Function = Proxy.create(ref,reloadProgress, ref, URLToSend);
						var fnCancel:Function = Proxy.create(ref,activitySnapBack, act);
						LFMessage.showMessageConfirm(Dictionary.getValue('al_confirm_forcecomplete_toactivity',[act.Learner.getFullName(), indexArray[i].activity.title]), fnOk,fnCancel);
						
					}else if (actHitOrder == currentActOrder ){
						activitySnapBack(act)
					}else if (actHitOrder < currentActOrder){
						activitySnapBack(act)
						var msg:String = Dictionary.getValue('al_error_forcecomplete_invalidactivity',[act.Learner.getFullName(), indexArray[i].activity.title]) ;
						LFMessage.showMessageAlert(msg);
					}
				hasHit = true;
				
				}
				
			}
			if (act.hitTest(_monitorModel.endGate)){
				_monitorModel.endGate.doorClosed._visible = false;
				_monitorModel.endGate.doorOpen._visible = true;
				var URLToSend:String = _root.monitoringURL+"forceComplete&lessonID="+_root.lessonID+"&learnerID="+act.Learner.getLearnerId()+"&activityID=null";
				var ref = this;
				var fnOk:Function = Proxy.create(ref,reloadProgress, ref, URLToSend);
				var fnCancel:Function = Proxy.create(ref,activitySnapBack, act);
				LFMessage.showMessageConfirm(Dictionary.getValue('al_confirm_forcecomplete_tofinish',[act.Learner.getFullName(), indexArray[i].activity.title]), fnOk,fnCancel);
				hasHit = true;
			}
			if (!hasHit){
				activitySnapBack(act)
				var msg:String = Dictionary.getValue('al_error_forcecomplete_notarget',[act.Learner.getFullName()]) ;
				LFMessage.showMessageAlert(msg);
			}
		}
		
	}
	
	private function reloadProgress(ref, URLToSend){
		var callback:Function = Proxy.create(ref, getProgressData);
		Application.getInstance().getComms().getRequest(URLToSend,callback, false);
						
		
	}
	private function activitySnapBack(act:Object){
		act._x = act.xCoord;
		act._y = act.yCoord;
		_monitorModel.endGate.doorClosed._visible = true;
		_monitorModel.endGate.doorOpen._visible = false;
	}
	
	private function getProgressData(r){
		trace('force complete ok');
		if(r instanceof LFError) {
			r.showErrorAlert();
		} else if(r) {
			LFMessage.showMessageAlert(r);
			_monitorModel.broadcastViewUpdate("RELOADPROGRESS", null, _monitorModel.getSelectedTab())
		}
	}
	
	private function checkLearnerCurrentActivity(arr:Array, actUIID:Number):Number{
		for (var i=0; i<arr.length; i++){
			//trace("activity "+ i +" is "+arr[i].activity.title)
			if (arr[i].activity.activityUIID == actUIID){
				return i
			}
		}
	}
	
	public function activityReleaseOutside(ca:Object):Void{
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityID,Debugger.GEN,'activityReleaseOutside','MonitorController');
	}
   
	public function activityDoubleClick(ca:Object, forTabView:String, learnerID:Number):Void{
		var _learnerID:Number;
		var URLToSend:String;
		setBusy()
		
	   if (forTabView == "MonitorTabView"){
			//getActivityMonitorURL&activityID=31&lessonID=4
			//Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityID,Debugger.GEN,'activityDoubleClick','MonitorController');
			URLToSend = _root.serverURL+_root.monitoringURL+'getActivityMonitorURL&activityID='+ca.activity.activityID+'&lessonID='+_root.lessonID;
			URLToSend += '&contentFolderID='+MonitorModel(getModel()).getSequence().contentFolderID
			//Debugger.log('activityDoubleClick URL:'+URLToSend,Debugger.CRITICAL,'activityDoubleClick','MonitorController');
			
		}else {
			if (learnerID != null){
				_learnerID = learnerID;
				trace("learnerId if passed is: "+_learnerID)
			}else {
				trace("learnerId if not passed is: "+ca.learnerID)
				_learnerID = ca.learnerID;
			}
			if (forTabView == "MonitorTabViewLearner"){
				Debugger.log('activityDoubleClick CanvasActivity:'+ca.activityID,Debugger.GEN,'activityDoubleClick','MonitorController');
				URLToSend = _root.serverURL+_root.monitoringURL+'getLearnerActivityURL&activityID='+ca.activityID+'&userID='+_learnerID+'&lessonID='+_root.lessonID;
			}else {
				URLToSend = _root.serverURL+_root.monitoringURL+'getLearnerActivityURL&activityID='+ca.activity.activityID+'&userID='+_learnerID+'&lessonID='+_root.lessonID;
			}
		}

		Debugger.log('Opening url (ca.activityID) :'+URLToSend+" Opening url (ca.activityID)"+URLToSend,Debugger.CRITICAL,'openToolActivityContent','MonitorModel');
		if (forTabView != "MonitorTabView" && forTabView != "MonitorTabViewLearner"){
			if (ca.activityStatus == undefined){
		
				var alertMSG:String = Dictionary.getValue('al_doubleclick_todoactivity',[ca.learnerName, ca.activity.title]);
				getURL("javascript:alert('"+alertMSG+"');");
				
			}else {
				JsPopup.getInstance().launchPopupWindow(URLToSend, 'MonitorLearnerActivity', 600, 800, true, true, false, false, false);
			}
		}else {
			JsPopup.getInstance().launchPopupWindow(URLToSend, 'MonitorLearnerActivity', 600, 800, true, true, false, false, false);
		}

	   clearBusy()
	}
   
   
	// add control methods
	
	/**
	 * Event listener for when when tab is clicked
	 * 
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function change(evt):Void{
		trace(evt.target);
		trace("test: "+ String(evt.target.selectedIndex))
		_monitorModel.setSelectedTab(evt.target.selectedIndex)
		if (_monitorModel.getSequence() == null){
			trace ("None of Sequence is selected yet!");
		}else {
			_monitorModel.changeTab(evt.target.selectedIndex);
		}
	
	
	}
	
	private function exportClassPortfolio():Void{
		var exp_url:String = _root.serverURL+"learning/exportWaitingPage.jsp?mode=teacher&lessonID="+_root.lessonID;
		
		JsPopup.getInstance().launchPopupWindow(exp_url, 'ExportPortfolio', 410, 640, true, true, false, false, false);
	}

	private function openJournalEntries():Void{
		var journals_url:String = _root.serverURL+"learning/notebook.do?method=viewAllJournals&lessonID="+_root.lessonID;
		
		JsPopup.getInstance().launchPopupWindow(journals_url, 'JournalEntries', 570, 796, true, true, false, false, false);
	
	}
	
	private function openEditOnFly():Void{
		var fnOk:Function = Proxy.create(this, setupEditOnFly);
		LFMessage.showMessageConfirm(Dictionary.getValue('al_confirm_live_edit'), fnOk, null);
	}
	
	private function setupEditOnFly() {
		Debugger.log('!opening edit on fly!',Debugger.CRITICAL,'openEditOnFly','org.lamsfoundation.lams.MonitorController');
		
		_monitorModel.getMonitor().setupEditOnFly(_monitorModel.getSequence().learningDesignID);
	}

	public function click(evt):Void{
		trace(evt.target);
		var tgt:String = new String(evt.target);
		if(tgt.indexOf("editClass_btn") != -1){
			trace('you clicked edit class button..');
			_monitorModel.setDialogOpen("LM_DIALOG");
		} else if(tgt.indexOf("viewLearners_btn") != -1){
			_monitorModel.setDialogOpen("VM_DIALOG");
		} else if(tgt.indexOf("start_btn") != -1){
			trace('you clicked start class button..');
			_monitorModel.getMonitor().startLesson(false, _root.lessonID);
		} else if(tgt.indexOf("exportPortfolio_btn") != -1){
			trace('you clicked exportPortfolio button..');
			exportClassPortfolio();
		}else if(tgt.indexOf("refresh_btn") != -1){
			trace('you clicked refresh button..');
			_monitorModel.refreshAllData();
		}else if(tgt.indexOf("help_btn") != -1){
			trace('you clicked help button..');
			_monitorModel.tabHelp();
		}else if(tgt.indexOf("viewJournals_btn") != -1){
			trace('you clicked journal entries button..');
			openJournalEntries();
		}else if(tgt.indexOf("editFly_btn") != -1){
			openEditOnFly();
		}else if(tgt.indexOf("action_btn") != -1){
			_monitorModel.broadcastViewUpdate("TRIGGER_ACTION", null);
		}
	}

	
	/**
	 * called when the dialog is loaded, calles methods to set up content in dialogue
	 * also sets up the okClicked event listener
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
    public function openDialogLoaded(evt:Object) {
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.MonitorController');
        //Check type is correct
		trace('open dialog evt type:' + evt.type);
        if(evt.type == 'contentLoaded'){
			//set a ref to the view
			evt.target.scrollContent.monitorView = LessonTabView(getView());
			
			//set a ref to the dia in the view
			LessonTabView(getView()).dialog = evt.target.scrollContent;
			
			//set up UI
			//note this function registeres the dialog to recieve view updates
			evt.target.scrollContent.setUpContent();		
			
        } else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	

	/**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
        Debugger.log('!okClicked:'+evt.type+', now follows the resultDTO:',Debugger.GEN,'okClicked','org.lamsfoundation.lams.MonitorController');
        //Check type is correct
		if(evt.type == 'okClicked'){
			
            //Call the callback, passing in the design selected designId

			//invalidate the cache of folders
			//_workspaceModel.clearWorkspaceCache(evt.target.resultDTO.organisationID);
			
			//pass the resultant DTO back to the class that called us.
            //_monitorModel.getMonitor().onOKCallback(evt.target.resultDTO);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	/**
    * Invoked when the node is opened.  it must be a folder
    */
    public function onTreeNodeOpen (evt:Object){
		var treeview = evt.target;
		var nodeToOpen:XMLNode = evt.node;
		Debugger.log('nodeToOpen organisationID:'+nodeToOpen.attributes.data.organisationID,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.MonitorController');
		Debugger.log('nodeToOpen org name:'+nodeToOpen.attributes.data.name,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.MonitorController');
		//if this ndoe has children then the 
		//data has already been got, nothing to do
		
    }
	
	/**
    * Treeview data changed event handler
    */
    public function onTreeNodeClose (evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onTreeNodeClose','org.lamsfoundation.lams.MonitorController');
		var treeview = evt.target;
    }
		
	/**
	 * Save Lesson Class after Lesson is initialized
	 *  
	 * @param   lessonID 
	 * @return  
	 */
	
	public function editLessonClass(resultDTO:Object){
		_monitorModel.resultDTO = resultDTO;
		trace('editing lesson class');
		_monitorModel.getMonitor().createLessonClass();
	}

	/**
	 * Apply status change
	 *   
	 * 
	 * @param   evt Apply onclick event
	 */
	public function changeStatus(evt:Object):Void{
		if(!_isBusy){
			setBusy();
			var stateID:Number = evt.target.changeStatus_cmb.selectedItem.data;
			switch(stateID){
				case LessonTabView.REMOVE_CBI :
					trace('removing...');
					var confirmMsg:String = Dictionary.getValue('ls_remove_confirm_msg');
					var warningMsg:String = Dictionary.getValue('ls_remove_warning_msg', [_monitorModel.getSequence().getSequenceName()]);
					
					var warningNoHandler = Proxy.create(_monitorModel, _monitorModel.removeSequence);
					var confirmOkHandler = Proxy.create(this, removalAlert, warningMsg, null, warningNoHandler);
					
					removalAlert(confirmMsg, confirmOkHandler, null);
					
					break;
				case LessonTabView.NULL_CBI :
					// error msg
					trace('nothing selected...');
					break;
				case LessonTabView.ACTIVE_CBI :
					trace('activating...');
					_monitorModel.activateSequence();
					break;
				case LessonTabView.DISABLE_CBI :
					trace('suspending...');
					_monitorModel.suspendSequence();
					break;
				case LessonTabView.ARCHIVE_CBI :
					trace('archiving...');
					_monitorModel.archiveSequence();
					break;
				case LessonTabView.UNARCHIVE_CBI :
					trace('unarchiving...');
					_monitorModel.unarchiveSequence();
					break;
				default :
					trace('no such combo box item');
					
			}
			
			clearBusy();
		}
	}
	
	public function setBusy(){
		_isBusy = true;
	}
	
	public function clearBusy(){
		_isBusy = false;
	}
	
	public function get appData():Object{
		trace("called monitor application")
		var myObj:Object = new Object();
		myObj.compX = Application.MONITOR_X
		myObj.compY = Application.MONITOR_Y
		myObj.ttHolder = Application.tooltip;
		return myObj;
		
	}
	
	/**
	* Alert message after applying the remove action on a archived lesson.
	* 
	* @param msg		Message to display
	* @param okHandler	Method to pass to onPress of OK button
	*/
	private function removalAlert(msg:String, okHandler:Function, cancelHandler:Function) {
		LFMessage.showMessageConfirm(msg,okHandler,cancelHandler,Dictionary.getValue('al_yes'),Dictionary.getValue('al_no'));
	}
}