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
import org.lamsfoundation.lams.authoring.cv.ICanvasActivity;
import org.lamsfoundation.lams.authoring.cv.CanvasParallelActivity;
import org.lamsfoundation.lams.authoring.br.CanvasBranchView;

import mx.utils.*
import mx.controls.*
import mx.behaviors.*
import mx.core.*
import mx.events.*
import mx.effects.*

import org.lamsfoundation.lams.authoring.cv.CanvasModel;

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
		if (forObj == "LearnerIcon"){
			_monitorModel.isDragging = true;
			
			act.startDrag(false);
			
			Debugger.log('activityClick CanvasActivity:'+act.Learner.getUserName(),Debugger.GEN,'activityClick','MonitorController');
		}else {
			_monitorModel.selectedItem = act;
		}
    }
   
	/**
	 * Returns the parent that is an member activity of the canvas (ICanvasActivity)
	 * 
	 * @usage   
	 * @param   path 	dropTarget path
	 * @return  path to activity movieclip
	 */
	
    private function findParentActivity(path:Object):Object {
		if(path instanceof ICanvasActivity)
			return path;
		else if(path == ApplicationParent.root)
			return null;
		else
			return findParentActivity(path._parent);
    }
	
	/**
	 * Returns the child of a complex activity that matches with the dropTarget
	 * 
	 * @usage   
	 * @param   children 	array of children movieclips
	 * @param   target   	dropTarget path
	 * @return  the matching activity object or null if not found
	 */
	private function matchChildActivity(children:Array, target:Object):Object {
		for(var i=0; i<children.length; i++) {
			if(target == children[i]) {
				return children[i];
			} else {
				Debugger.log("no match found: " + target , Debugger.CRITICAL, "matchChildActivity", "MonitorController");
			}
		}
		
		return null;
	}
	
	
	/**
	 * Check the force complete order for validity
	 * 
	 * @usage   
	 * @param   indexArray 	Top-level canvas member activities on the canvas
	 * @param   index      	index position 
	 * @param   isChild    
	 * @return  
	 */
	
	private function checkHit(cActivity:Object, learnerObj:Object, indexArray:Array):Boolean {
		
		var actUIIDToCompare = learnerObj.activity.activityUIID;
		var parentUIIDToCompare = learnerObj.activity.parentUIID;
		
		Debugger.log("completed: " + checkifActivityNotComplete(learnerObj.Learner, cActivity.activity.activityID), Debugger.CRITICAL, "checkHit", "MonitorController");
		
		//if learner is on the next or further activity - get new progress data.
		if(checkifActivityNotComplete(learnerObj.Learner, cActivity.activity.activityID)) {
			if(actUIIDToCompare == cActivity.activity.activityUIID)
				activitySnapBack(learnerObj);
			else {
				forceCompleteTransfer(learnerObj, cActivity, indexArray);
			}
				
		} else {
			activitySnapBack(learnerObj);
			var msg:String = Dictionary.getValue('al_error_forcecomplete_invalidactivity',[learnerObj.Learner.getFullName(), cActivity.activity.title]) ;
			LFMessage.showMessageAlert(msg);
		}
		
		return true;
	}
   
	/**
	 * Force complete up to target activity
	 * 
	 * @usage   
	 * @param   learnerObj 
	 * @param   cActivity  
	 * @param   indexArray 
	 * @return  
	 */
	
	private function forceCompleteTransfer(learnerObj:Object, cActivity:Object, indexArray:Array):Void {
		var activityUIIDToCheck:Number = (cActivity.activity.parentUIID != null) ? cActivity.activity.parentUIID : cActivity.activity.activityUIID;
		var prevActivityID:Number = indexArray[checkLearnerCurrentActivity(indexArray,activityUIIDToCheck) - 1].activity.activityID;
		var URLToSend:String = _root.monitoringURL+"forceComplete&lessonID="+_root.lessonID+"&learnerID="+learnerObj.Learner.getLearnerId()+"&activityID="+prevActivityID;
		
		var fnOk:Function = Proxy.create(this, this.reloadProgress, this, URLToSend);
		var fnCancel:Function = Proxy.create(this, this.activitySnapBack, learnerObj);
		
		LFMessage.showMessageConfirm(Dictionary.getValue('al_confirm_forcecomplete_toactivity',[learnerObj.Learner.getFullName(), cActivity.activity.title]), fnOk,fnCancel);
		
	}
	
	/**
	 * Check learner's progress data if activity has been completed
	 * 
	 * @usage   
	 * @param   learnerProgress 
	 * @param   activityID      
	 * @return  
	 */
	
	private function checkifActivityNotComplete(learnerProgress:Progress, activityID:Number):Boolean {
		return (Progress.compareProgressData(learnerProgress, activityID) != "completed_mc") ? true : false;
	}
	
	/**
	 * Return activity index
	 * 
	 * @usage   
	 * @param   arr     
	 * @param   actUIID 
	 * @return  index 
	 */
	
	private function checkLearnerCurrentActivity(arr:Array, actUIID:Number):Number{
		for (var i=0; i<arr.length; i++){
			if (arr[i].activity.activityUIID == actUIID){
				return i;
			}
		}
	}
	
	public function activityRelease(act:Object, forObj:String):Void{
		
		Debugger.log('activityRelease CanvasActivity:'+act.activity.activityID,Debugger.GEN,'activityRelease','MonitorController');
		
		if (forObj == "LearnerIcon"){
			
			var hasHit:Boolean = false;
			var indexArray:Array = _monitorModel.activitiesOnCanvas();
			
			if(_monitorModel.isDragging){
				act.stopDrag();
			}
			
			var dropTarget:Object = findParentActivity(eval(act._droptarget));
				
			//run a loop to check which activity has been hitted by the learner.
			for (var i=0; i<indexArray.length; i++){ 
				var cActivity = indexArray[i];
				
				if(dropTarget != null) {
					if(dropTarget.activity.parentUIID != null 
							&& cActivity.children != null
							&& !cActivity.activity.isBranchingActivity())
						cActivity = matchChildActivity(cActivity.children, dropTarget);
					
					hasHit = (cActivity.activity.activityUIID == dropTarget.activity.activityUIID) ? checkHit(cActivity, act, indexArray) : hasHit;
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
			
			Debugger.log("droptarget: " + eval(act._droptarget), Debugger.CRITICAL, "activityRelease", "MonitoringController");
			
			if (!hasHit){
				activitySnapBack(act);
				
				if(eval(act._droptarget)._parent instanceof LearnerIcon) return;
				
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
	
	public function activityReleaseOutside(ca:Object):Void{
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityID,Debugger.GEN,'activityReleaseOutside','MonitorController');
	}
   
	public function activityDoubleClick(ca:Object, forTabView:String, learnerID:Number, fromContextMenu:Boolean):Void{
		
		Debugger.log("ca.activity.isBranchingActivity(): "+ca.activity.isBranchingActivity(), Debugger.GEN, "activityDoubleClick", "MonitorController");
		if(ca.activity.isBranchingActivity() && !fromContextMenu) {
			_monitorModel.openBranchActivityContent(ca, true);
		} else {
					
			var _learnerID:Number;
			var URLToSend:String;
			setBusy();
			
			if(forTabView == "MonitorTabView"){
				URLToSend = _root.serverURL+_root.monitoringURL+'getActivityMonitorURL&activityID='+ca.activity.activityID+'&lessonID='+_root.lessonID;
				URLToSend += '&contentFolderID='+MonitorModel(getModel()).getSequence().contentFolderID
			} else {
				if(learnerID != null){
					_learnerID = learnerID;
				} else {
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
			
			if(forTabView != "MonitorTabView" && forTabView != "MonitorTabViewLearner"){
				if(ca.activityStatus == undefined){
			
					var alertMSG:String = Dictionary.getValue('al_doubleclick_todoactivity',[ca.learnerName, ca.activity.title]);
					getURL("javascript:alert('"+alertMSG+"');");
					
				} else {
					JsPopup.getInstance().launchPopupWindow(URLToSend, 'MonitorLearnerActivity', 600, 800, true, true, false, false, false);
				}
			} else {
				JsPopup.getInstance().launchPopupWindow(URLToSend, 'MonitorLearnerActivity', 600, 800, true, true, false, false, false);
			}

		   clearBusy();
	   }
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
		_monitorModel.setSelectedTab(evt.target.selectedIndex)
		
		if (_monitorModel.getSequence() != null)
			_monitorModel.changeTab(evt.target.selectedIndex);
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
		var tgt:String = new String(evt.target);
		if(tgt.indexOf("editClass_btn") != -1){
			_monitorModel.setDialogOpen("LM_DIALOG");
		} else if(tgt.indexOf("viewLearners_btn") != -1){
			_monitorModel.setDialogOpen("VM_DIALOG");
		} else if(tgt.indexOf("start_btn") != -1){
			_monitorModel.getMonitor().startLesson(false, _root.lessonID);
		} else if(tgt.indexOf("exportPortfolio_btn") != -1){
			exportClassPortfolio();
		}else if(tgt.indexOf("refresh_btn") != -1){
			if(_monitorModel.activeView instanceof CanvasBranchView) {
				_monitorModel.activeView.removeMovieClip();
				_monitorModel.getMonitor.closeBranchView();
			}
			
			_app.reloadLearningDesign(_monitorModel.getSequence(), Proxy.create(_monitorModel, _monitorModel.refreshAllData));
		}else if(tgt.indexOf("help_btn") != -1){
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
					var confirmMsg:String = Dictionary.getValue('ls_remove_confirm_msg');
					var warningMsg:String = Dictionary.getValue('ls_remove_warning_msg', [LessonTabView(getView()).showStatus(_monitorModel.getSequence().state).toLowerCase()]);
					
					var warningNoHandler = Proxy.create(_monitorModel, _monitorModel.removeSequence);
					var confirmOkHandler = Proxy.create(this, removalAlert, warningMsg, null, warningNoHandler);
					
					removalAlert(confirmMsg, confirmOkHandler, null);
					
					break;
				case LessonTabView.NULL_CBI :
					// error msg
					break;
				case LessonTabView.ACTIVE_CBI :
					_monitorModel.activateSequence();
					break;
				case LessonTabView.DISABLE_CBI :
					_monitorModel.suspendSequence();
					break;
				case LessonTabView.ARCHIVE_CBI :
					_monitorModel.archiveSequence();
					break;
				case LessonTabView.UNARCHIVE_CBI :
					_monitorModel.unarchiveSequence();
					break;
				default :
			}
			
			clearBusy();
		} else {
			Debugger.log("Unable to run method as controller is busy", Debugger.CRITICAL, "changeStatus", "MonitorController");
		}
	}
	
	public function setBusy(){
		_isBusy = true;
	}
	
	public function clearBusy(){
		_isBusy = false;
	}
	
	public function get appData():Object{
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
	
	public function openAboutDialogLoaded(evt:Object) {
        if(evt.type == 'contentLoaded'){
			//set up UI
			//note this function registers the dialog to recieve view updates
			evt.target.scrollContent.setUpContent();		
			
        } else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
		
    }
}