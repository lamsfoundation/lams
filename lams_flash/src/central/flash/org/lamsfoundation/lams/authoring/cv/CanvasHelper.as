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

import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.br.*
import org.lamsfoundation.lams.authoring.tk.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ws.Workspace
import org.lamsfoundation.lams.common.ApplicationParent
import org.lamsfoundation.lams.common.* 

import mx.managers.*
import mx.utils.*
import mx.transitions.Tween;
import mx.transitions.easing.*;

/**
 * The canvas is the main screen area of the LAMS application where activies are added and sequenced
 * Note - This holds the DesignDataModel _ddm 
 * @version 1.0
 * @since   
 */
class CanvasHelper {
	
	//Model
	private var canvasModel:CanvasModel;

	//Views
	private var canvasView:CanvasView;
	private var _canvasView_mc:MovieClip;
	
	private var canvasComplexView:CanvasComplexView;
	private var _canvasComplexView_mc:MovieClip;
	
	// CookieMonster (SharedObjects)
    private var _cm:CookieMonster;
    private var _comms:Communication;
	
	private var app:Application;
	private var _ddm:DesignDataModel;
	private var _dictionary:Dictionary;
	private var _config:Config;
	private var doc;
	private var _newToolContentID:Number;
	private var _newChildToolContentID:Number;
	private var _undoStack:Array;	
	private var _redoStack:Array;
	private var toolActWidth:Number = 123;
	private var toolActHeight:Number = 50;
	private var complexActWidth:Number = 143;
	private var _isBusy:Boolean;
	
	private static var AUTOSAVE_CONFIG:String = "autosave";
	private static var AUTOSAVE_TAG:String = "cv.ddm.autosave.user.";
   
   private var _bin:MovieClip;	//bin
	
	private var _target_mc:MovieClip;
	
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;


	function CanvasHelper() {
	    mx.events.EventDispatcher.initialize(this);
	}
	
    /**
    * Event dispatched from the view once it's loaded
    */
    public function viewLoaded(evt:Object) {
        if(evt.type=='load') {
			
			canvasModel.activeView = evt.target;
			
			if(evt.target instanceof CanvasBranchView) {
				evt.target.open();
				canvasModel.setDirty();
			} else if(evt.target instanceof CanvasComplexView) {
				// open complex view
				evt.target.showActivity();
			} else {
				canvasModel.getCanvas().addBin(evt.target);
				
				var autosave_config_interval = Config.getInstance().getItem(AUTOSAVE_CONFIG);
				Debugger.log("autosave interval: " + autosave_config_interval, Debugger.CRITICAL, "viewLoaded", "CanvasHelper");
				
				if(autosave_config_interval > 0) {
					if(CookieMonster.cookieExists(AUTOSAVE_TAG + _root.userID)) {
						canvasModel.autoSaveWait = true;
					}
					setInterval(Proxy.create(this,autoSave), autosave_config_interval);
				}
				
				clearCanvas(true);
				
				dispatchEvent({type:'load',target:this});
			}
			
		} else {
            Debugger.log('Event type not recognised : ' + evt.type,Debugger.CRITICAL,'viewLoaded','Canvas');
        }
    }
	
	/**
	 * Called by Comms after a design has been loaded, usually set as the call back of something like openDesignByID.
	 * Will accept a learningDesign DTO and then render it all out.
	 * @usage   
	 * @param   designData 
	 * @return  
	 */
    public function setDesign(designData:Object){
       
		Debugger.log('designData.title:'+designData.title+':'+designData.learningDesignID,4,'setDesign','Canvas');
		
		if(clearCanvas(true)){
			
			_ddm.setDesign(designData);
			if(_ddm.editOverrideLock && !_ddm.validDesign) Application.getInstance().getToolbar().setButtonState("cancel_btn", false, true);
			
			if(canvasModel.importing){ 
				Application.getInstance().getWorkspace().getWorkspaceModel().clearWorkspaceCache(_ddm.workspaceFolderID);
				
				_undoStack = new Array();
				_redoStack = new Array();
				
				canvasModel.importing = false;
			} else if(canvasModel.editing){
				// TODO: stuff to do before design is displayed
				// do we need editing flag in CanvasModel?
			}
			
			checkValidDesign();
			checkReadOnlyDesign();
			canvasModel.setDesignTitle();
			canvasModel.lastBranchActionType = CanvasModel.OPEN_FROM_FILE;
			canvasModel.setDirty();
			
			LFMenuBar.getInstance().enableExport(!canvasModel.autoSaveWait);
			LFMenuBar.getInstance().enableInsertDesign(!canvasModel.autoSaveWait);
		
		}else{
			Debugger.log('Set design failed as old design could not be cleared',Debugger.CRITICAL,"setDesign",'Canvas');		
		}
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
		Debugger.log('noWarn:'+noWarn,4,'clearCanvas','Canvas');
		if(noWarn){
			_ddm = new DesignDataModel();
			
			//as its a new instance of the ddm,need to add the listener again
			_ddm.addEventListener('ddmUpdate',Proxy.create(this,onDDMUpdated));
			_ddm.addEventListener('ddmBeforeUpdate',Proxy.create(this,onDDMBeforeUpdate));
			
			checkValidDesign();
			checkReadOnlyDesign();
			
			if(canvasModel.activeView != null) {
				canvasModel.closeAllComplexViews();

				for(var i=0; i<canvasModel.openBranchingActivities.length; i++)
					canvasModel.openBranchingActivities[i].activity.branchView.removeMovieClip();
			
				canvasModel.openBranchingActivities.clear();
			}
			
			canvasModel.activeView = canvasView;
			canvasModel.currentBranchingActivity = null;
			
			canvasModel.getCanvas().addBin(canvasModel.activeView);
			canvasModel.broadcastViewUpdate("SIZE");
			
			canvasModel.selectedItem = null;
			canvasModel.setDirty();
			
			canvasModel.clearAllElements();
			createContentFolder();
			
			return true;
		}else{
			var fn:Function = Proxy.create(ref,confirmedClearDesign, ref);
			LFMessage.showMessageConfirm(Dictionary.getValue('new_confirm_msg'), fn,null);
			Debugger.log('Set design failed as old design could not be cleared',Debugger.CRITICAL,"setDesign",'Canvas');		
		}
	}
		
	/**
	 * Auto-saves current DDM (Learning Design on Canvas) to SharedObject
	 * 
	 * @usage   
	 * @return  
	 */
	
	private function autoSave(){
		if(!canvasModel.autoSaveWait && (canvasModel.activitiesDisplayed.size() > 0)) {
			if(!ddm.readOnly) {
				var tag:String = AUTOSAVE_TAG + _root.userID;
				
				var dto:Object = _ddm.getDesignForSaving();
				dto.lastModifiedDateTime = new Date();
				dto.readOnly = true;
				
				// remove existing auto-saved ddm
				if (CookieMonster.cookieExists(tag)) {
					CookieMonster.deleteCookie(tag);
				}
				
				// auto-save existing ddm
				Debugger.log("auto-saving...", Debugger.CRITICAL, "autoSave", "CanvasHelper");
		
				var res = CookieMonster.save(dto,tag,true);
				
				if(!res){
					// error auto-saving
					var msg:String = Dictionary.getValue('cv_autosave_err_msg');
					LFMessage.showMessageAlert(msg, Proxy.create(this, openSystemSettings, 1));
				}
			}
		} else if(canvasModel.autoSaveWait) {
			discardAutoSaveDesign();
		}
		
	}
	
	public function openSystemSettings(tabID:Number):Void {
		System.showSettings(tabID);
	}
	
	/**
	 * Removes an activity from Design Data Model using its activityUIID.  
	 * Called by the bin
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	public function removeActivity(activityUIID:Number){
		Debugger.log('activityUIID:'+activityUIID,4,'removeActivity','Canvas');
		
		// remove transitions and/or branches connected to this activity being removed
		_ddm.removeTransitionByConnection(activityUIID);
		
		canvasModel.clearGroupedActivities(activityUIID);
		
		if(canvasModel.activeView instanceof CanvasBranchView)
			_ddm.removeBranchByConnection(activityUIID, CanvasBranchView(canvasModel.activeView).activity);
		
		_ddm.removeActivity(activityUIID);
		
		canvasModel.setDirty();
		canvasModel.selectedItem = null;
	}
	
	/**
	 * Removes an transition by using its transitionUIID.  
	 * Called by the bin
	 * @usage   
	 * @param   transitionUIID 
	 * @return  
	 */
	public function removeTransition(transitionUIID:Number){	
		_ddm.removeTransition(transitionUIID);
		canvasModel.setDirty();	
		canvasModel.selectedItem = null;
	}
	
	/**
	 * Removes an transition by using its branchUIID.  
	 * Called by the bin
	 * @usage   
	 * @param   branchUIID 
	 * @return  
	 */
	public function removeBranch(branchUIID:Number){	
		_ddm.removeBranch(branchUIID);
		canvasModel.setDirty();	
		canvasModel.selectedItem = null;
	}
	
	/**
	 * Show auto-save confirmation message
	 * 
	 * @usage   
	 * @return  
	 */

	public function showRecoverMessage() {
		var recData:Object = CookieMonster.open(AUTOSAVE_TAG + _root.userID,true);
		
		LFMessage.showMessageConfirm(Dictionary.getValue('cv_autosave_rec_msg'), Proxy.create(this, recoverDesign, recData), Proxy.create(this, discardAutoSaveDesign), null, null, Dictionary.getValue('cv_autosave_rec_title'));
	}
	
	
	/**
	 * Recover design data from SharedObject and save.
	 * 
	 * @usage   
	 * @return  
	 */
	
	public function recoverDesign(recData:Object) {
		setDesign(recData);
		discardAutoSaveDesign();
	}
	
	private function discardAutoSaveDesign() {
		canvasModel.autoSaveWait = false;
		CookieMonster.deleteCookie(AUTOSAVE_TAG + _root.userID);
		LFMenuBar.getInstance().enableRecover(false);
	}
	
	public function showDesignValidationIssues(responsePacket){
		Debugger.log(responsePacket.messages.length+' issues',Debugger.GEN,'showDesignValidationIssues','Canvas');
		var dp = new Array();
		for(var i=0; i<responsePacket.messages.length;i++){
			var dpElement = {};
			dpElement.Issue = responsePacket.messages[i].message;
			dpElement.Activity =  _ddm.getActivityByUIID(responsePacket.messages[i].UIID).title;
			dpElement.uiid = responsePacket.messages[i].UIID;
			dp.push(dpElement);
		}
		//show the window, on load, populate it
		var cc:CanvasController = canvasView.getController();
		var validationIssuesDialog = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:Dictionary.getValue('ld_val_title'),closeButton:true,scrollContentPath:"ValidationIssuesDialog",validationIssues:dp, canvasModel:canvasModel,canvasController:cc});
	}
	
	/**
	 * Close Window
	 * 
	 * @usage   
	 * @return  
	 */
	
	public function closeReturnExt() {
		ApplicationParent.extCall("closeWindow", null);
	}
	
	/**
	 * Reopen Monitor client
	 * 
	 * @usage   
	 * @param   lessonID 	Lesson to load in Monitor
	 * @return  
	 */
	
	public function reopenMonitor(lessonID) {
		Debugger.log('finishing and closing Edit On The Fly',Debugger.CRITICAL,'finishEditOnFly','Canvas');
		
		ApplicationParent.extCall("openMonitorLesson", lessonID);
	}
	
	/**
	 * Finish Edit-On-The-Fly
	 * 
	 * @usage   
	 * @param   forced 
	 * @return  
	 */
	
	public function finishEditOnFly(forced:Boolean) {
		Debugger.log('finishing and closing Edit On The Fly',Debugger.CRITICAL,'finishEditOnFly','Canvas');
		Debugger.log('valid design: ' + _ddm.validDesign,Debugger.CRITICAL,'finishEditOnFly','Canvas');
		Debugger.log('modified: ' + _ddm.modified,Debugger.CRITICAL,'finishEditOnFly','Canvas');
		
		var callback:Function = Proxy.create(this,reopenMonitor);
        canvasModel.editing = false;
		
		if(forced) {
			ApplicationParent.extCall("setSaved", "true");
			finishLearningDesignCall(callback);
			return;
		}
		
		if(!_ddm.modified) {
			if(_ddm.validDesign) finishLearningDesignCall(callback);
			else LFMessage.showMessageAlert(Dictionary.getValue("cv_eof_finish_invalid_msg"));
		} else LFMessage.showMessageConfirm(Dictionary.getValue("cv_eof_finish_modified_msg"), Proxy.create(this,finishEditOnFly, true), null);
	}
	
	private function finishLearningDesignCall(callback:Function) {
		Application.getInstance().getComms().getRequest('authoring/author.do?method=finishLearningDesignEdit&learningDesignID='+_ddm.learningDesignID,callback, false);
	}
	
	/**
	 * 
	 * 
	 * @usage   
	 * @param   acts 
	 * @return  
	 */
	
	public function updateToolActivities(responsePacket){
		Debugger.log(responsePacket.activities.length+' activities to be updated...',Debugger.GEN,'updateToolActivities','Canvas');
		for(var i=0; i<responsePacket.activities.length; i++){
			var ta:ToolActivity = ToolActivity(_ddm.getActivityByUIID(responsePacket.activities[i].activityUIID));
			ta.toolContentID = responsePacket.activities[i].toolContentID;
			ta.readOnly = responsePacket.activities[i].readOnly;
			Debugger.log('setting new tool content ID for activity ' + ta.activityID + ' (toolContentID:' + ta.toolContentID + ')',Debugger.GEN,'updateToolActivities','Canvas');
		
		}
		
		canvasModel.setDirty();
	}
	
	private function setNewChildContentID(r, ta:ToolActivity){
		if(r instanceof LFError){
			r.showMessageConfirm();
		}else{
			_newChildToolContentID = r;
			ta.toolContentID = _newChildToolContentID;
		}
		
	}
	
	public function checkValidDesign(){
		if(_ddm.validDesign){
			Application.getInstance().getToolbar().setButtonState('preview_btn', true);
			Application.getInstance().getToolbar().setButtonState('preview_btn_click_target', true, false);
			LFMenuBar.getInstance().enableExport(true);
		}else{
			Application.getInstance().getToolbar().setButtonState('preview_btn', false);
			Application.getInstance().getToolbar().setButtonState('preview_btn_click_target', true, true);
			LFMenuBar.getInstance().enableExport(false);
		}
		
	}
	
	public function checkReadOnlyDesign(){
		if(_ddm.readOnly){
			if(!_ddm.editOverrideLock) {
				LFMenuBar.getInstance().enableSave(false);
				canvasView.showReadOnly(true);
			} else {
				canvasView.showEditOnFly(true);
			}
			
		} else {
			LFMenuBar.getInstance().enableSave(true);
			canvasView.showReadOnly(false);
		}
		canvasModel.setDesignTitle();
	}
	
	/**
	 * Called when the user initiates a paste.  recieves a reference to the item to be copied
	 * @usage   
	 * @param   o Item to be copied
	 * @return  
	 */
	public function setPastedItem(o:Object){
		if(o.data instanceof CanvasActivity && o.data.activity.activityCategoryID != Activity.CATEGORY_SYSTEM){
			Debugger.log('instance is CA',Debugger.GEN,'setPastedItem','Canvas');
			var callback:Function = Proxy.create(this, setNewContentID, o);
			Application.getInstance().getComms().getRequest('authoring/author.do?method=copyToolContent&toolContentID='+o.data.activity.toolContentID+'&userID='+_root.userID,callback, false);
		} else if(o.data instanceof ToolActivity){
			Debugger.log('instance is Tool',Debugger.GEN,'setPastedItem','Canvas');
			var callback:Function = Proxy.create(this, setNewContentID, o);
			Application.getInstance().getComms().getRequest('authoring/author.do?method=copyToolContent&toolContentID='+o.toolContentID+'&userID='+_root.userID,callback, false);
		} else if(o.data instanceof CanvasOptionalActivity || o.data instanceof CanvasParallelActivity || (o.data instanceof CanvasActivity && o.data.activity.isBranchingActivity())){
			Debugger.log('instance is Complex', Debugger.GEN,'setPastedItem','Canvas');
			var callback:Function = Proxy.create(this, setNewContents, o);
			Application.getInstance().getComms().sendAndReceive(getToolContentArray(o.data), 'servlet/authoring/copyMultipleToolContent?userID='+_root.userID, callback, false);
		} else{
			LFMessage.showMessageAlert(Dictionary.getValue('al_activity_paste_invalid'));
			Debugger.log('Cant paste this item!',Debugger.GEN,'setPastedItem','Canvas');
		}
	}
	
	/**
	 * Array of tool content IDs within the complex activity.
	 * 
	 * @param   ca Canvas object
	 * @return  array of tool content IDs
	 */
	
	private function getToolContentArray(ca):Object {
		var o = new Object();
		
		o.toolContentIDs = getAllToolContentIDs(ca.activity.activityUIID);
		Debugger.log('toolContentIDs: ' + o.toolContentIDs,Debugger.CRITICAL,'getToolContentArray','CanvasHelper');
		
		return o;
	}
	
	private function getAllToolContentIDs(activityUIID:Number, appendStr:String):String {
		var idString:String = new String();
		if(appendStr != null) idString.concat(appendStr);
		
		var _children:Array = _ddm.getComplexActivityChildren(activityUIID);
		Debugger.log('children length: ' + _children.length, Debugger.CRITICAL,'getAllToolContentIDs','CanvasHelper');
		
		for(var i=0; i<_children.length; i++) {
			Debugger.log('tool id: ' + ToolActivity(_children[i]).toolContentID, Debugger.CRITICAL,'getAllToolContentIDs','CanvasHelper');
			
			if(ToolActivity(_children[i]).toolContentID != null) { 
				if(idString.length <= 0) idString = idString.concat(ToolActivity(_children[i]).toolContentID);
				else idString = idString.concat("," + ToolActivity(_children[i]).toolContentID);
			} else {
				idString = getAllToolContentIDs(_children[i].activityUIID, idString);
			}
			
			Debugger.log('id string: ' + idString, Debugger.CRITICAL,'getAllToolContentIDs','CanvasHelper');
		}
		
		return idString;
	}
	
	private function setNewContentID(r, o){
		if(r instanceof LFError){
			r.showMessageConfirm();
		}else{
			_newToolContentID = r;
			if (o.data instanceof CanvasActivity){
				return pasteToolItem(o.data.activity, o, _newToolContentID);
			}else if(o.data instanceof ToolActivity){
				return pasteToolItem(o.data, o, _newToolContentID);
			}
		}
	}
	
	private function setNewContents(r, o) {
		Debugger.log("setting new contents: " + r, Debugger.CRITICAL, "setNewContents", "CanvasHelper");
		var _newIDMap:Hashtable = new Hashtable("newIDMap");
		
		if(r instanceof LFError){
			r.showMessageConfirm();
		} else {
			
			var _newIDMapArray = String(r).split(",");
			for(var i=0; i<_newIDMapArray.length; i++) {
				var itemArray:Array = _newIDMapArray[i].split("=");
				_newIDMap.put(itemArray[0], itemArray[1]);
			}
			
			if (o.data instanceof CanvasOptionalActivity || o.data instanceof CanvasParallelActivity){
				return pasteComplexItem(o.data.activity, o, _newIDMap);
			} else if(o.data instanceof CanvasActivity && o.data.activity.isBranchingActivity()) {
				return pasteBranchingItem(o.data.activity, o, _newIDMap);
			} else if(o.data instanceof ComplexActivity){
				return pasteComplexItem(o.data, o, _newIDMap);
			}
		}
	}
	
	private function pasteComplexItem(complexToCopy:ComplexActivity, o:Object, toolContentIDMap:Hashtable, parentAct:ComplexActivity):ComplexActivity {
		Debugger.log("pasting new cocomplex: " + complexToCopy.title, Debugger.CRITICAL, "pasteComplexItem", "CanvasHelper");
		
		var newComplexActivity:ComplexActivity = complexToCopy.clone();
		newComplexActivity.activityUIID = _ddm.newUIID();
		
		// TODO: improve for dropping into branching view
		if(parentAct == null) {
			newComplexActivity.xCoord = o.data.activity.xCoord + o.data.getVisibleWidth() + 10;
			newComplexActivity.yCoord = o.data.activity.yCoord + 10;
		} else {
			newComplexActivity.parentUIID = parentAct.activityUIID;
			newComplexActivity.parentActivityID = parentAct.activityID;
		}
		
		if(parentAct == null) canvasModel.haltRefresh(true);
		
		copyChildren(complexToCopy, newComplexActivity, o, toolContentIDMap);
		
		if(parentAct == null) canvasModel.haltRefresh(false);
		
		pasteActivityItem(newComplexActivity, o);
		
		return newComplexActivity;
	}
	
	private function pasteSequenceItem(sequenceToCopy:SequenceActivity, o:Object, toolContentIDMap:Hashtable, parentAct:ComplexActivity):SequenceActivity {
		Debugger.log("pasting new sequence: " + sequenceToCopy.title, Debugger.CRITICAL, "pasteSequenceItem", "CanvasHelper");
		if(sequenceToCopy.isDefault & sequenceToCopy.empty)
			return null;
			
		var newSequenceActivity:SequenceActivity = sequenceToCopy.clone();
		newSequenceActivity.activityUIID = _ddm.newUIID();
		
		newSequenceActivity.parentUIID = parentAct.activityUIID;
		newSequenceActivity.parentActivityID = parentAct.activityID;
		
		copySequence(sequenceToCopy, newSequenceActivity, o, toolContentIDMap);
		
		pasteActivityItem(newSequenceActivity, o);
		
		return newSequenceActivity;
	}
	
	private function pasteBranchingItem(branchingToCopy:BranchingActivity, o:Object, toolContentIDMap:Hashtable, parentAct:ComplexActivity):BranchingActivity {
		Debugger.log("pasting new branching: " + branchingToCopy.title, Debugger.CRITICAL, "pasteBranchingItem", "CanvasHelper");
		
		var newBranchingActivity:BranchingActivity = branchingToCopy.clone();
		newBranchingActivity.activityUIID = _ddm.newUIID();
		
		if(parentAct == null) {
			newBranchingActivity.xCoord = o.data.activity.xCoord + o.data.getVisibleWidth() + 10;
			newBranchingActivity.yCoord = o.data.activity.yCoord + 10;
		} else {
			newBranchingActivity.parentUIID = parentAct.activityUIID;
			newBranchingActivity.parentActivityID = parentAct.activityID;
		}
		
		copyChildren(branchingToCopy, newBranchingActivity, o, toolContentIDMap);
		
		pasteActivityItem(newBranchingActivity, o);
		
		return newBranchingActivity;
	}
	
	private function pasteToolItem(toolToCopy:ToolActivity, o:Object, newToolContentID:Number, parentAct:ComplexActivity):ToolActivity{
		//clone the activity
		var newToolActivity:ToolActivity = toolToCopy.clone();
		newToolActivity.activityUIID = _ddm.newUIID();
		
		if (newToolContentID != null || newToolContentID != undefined){
			newToolActivity.toolContentID = newToolContentID;
		}
		
		if(parentAct == null) {
			newToolActivity.xCoord = o.data.activity.xCoord + 10;
			newToolActivity.yCoord = o.data.activity.yCoord + 10;
		} else {
			newToolActivity.parentUIID = parentAct.activityUIID;
			newToolActivity.parentActivityID = parentAct.activityID;
		}
		
		pasteActivityItem(newToolActivity, o);
		
		return newToolActivity;
	}
	
	private function pasteActivityItem(activity:Activity, o:Object) {
		canvasModel.selectedItem = activity;
		
		if(canvasModel.activeView instanceof CanvasBranchView && activity.parentUIID == null)
			activity.parentUIID = CanvasBranchView(canvasModel.activeView).defaultSequenceActivity.activityUIID;
		
		if(o.type == Application.CUT_TYPE){ 
			Application.getInstance().setClipboardData(activity, Application.COPY_TYPE);
			removeActivity(o.data.activity.activityUIID); 
		} else {
			// TODO: Fix for RTL
			if(o.count <= 1) { activity.title = Dictionary.getValue('prefix_copyof')+activity.title; }
			else { activity.title = Dictionary.getValue('prefix_copyof_count', [o.count])+activity.title; }
		}
		
		_ddm.addActivity(activity);
		canvasModel.setDirty();
	}
	
	private function copySequence(sequenceToCopy, parentAct:SequenceActivity, o:Object, toolContentIDMap:Hashtable) {
		Debugger.log("copy sequence: " + parentAct.activityUIID, Debugger.CRITICAL, "copySequence", "CanvasHelper");
		
		var activityMap:Hashtable = new Hashtable("activityMap");
		copyChildren(sequenceToCopy, parentAct, o, toolContentIDMap, activityMap);
		
		var activityMapKeys:Array = activityMap.keys();
		Debugger.log("activityMapKeys len: " + activityMapKeys.length, Debugger.CRITICAL, "copySequence", "CanvasHelper");
		
		for(var i=0; i<activityMapKeys.length; i++) {
			var fromActivityMapObj:Object = activityMap.get(activityMapKeys[i]);
			var toActivityMapObj:Object = activityMap.get(fromActivityMapObj.transitionOut.toUIID);
			
			Debugger.log("fromActivityMapObj: " + fromActivityMapObj, Debugger.CRITICAL, "copySequence", "CanvasHelper");
			Debugger.log("toActivityMapObj: " + toActivityMapObj, Debugger.CRITICAL, "copySequence", "CanvasHelper");
			
			if(fromActivityMapObj != null && toActivityMapObj != null)
				var transitionCopy:Transition = copyTransition(fromActivityMapObj.transitionOut, fromActivityMapObj.activityCopy, toActivityMapObj.activityCopy);		
		}
		
		return;
	}
	
	private function copyChildren(activity:ComplexActivity, parentAct:ComplexActivity, o:Object, toolContentIDMap:Hashtable, activityMap:Hashtable) {
		Debugger.log("copying children of: " + activity.title, Debugger.CRITICAL, "copyChildren", "CanvasHelper");
		Debugger.log("tool content id map size: " + toolContentIDMap.size(), Debugger.CRITICAL, "copyChildren", "CanvasHelper");
		
		
		var children:Array = _ddm.getComplexActivityChildren(activity.activityUIID);
		var activityCopy:Activity;
		var defaultBranchCopy:Activity;
		
		for(var i=0; i<children.length; i++){
			if(children[i] instanceof ToolActivity) activityCopy = pasteToolItem(children[i], o, toolContentIDMap.get(children[i].toolContentID), parentAct);
			else if(children[i] instanceof SequenceActivity) activityCopy = pasteSequenceItem(children[i], o, toolContentIDMap, parentAct);
			else if(children[i] instanceof BranchingActivity) activityCopy = pasteBranchingItem(children[i], o, toolContentIDMap, parentAct);
			else if(children[i] instanceof ComplexActivity) activityCopy = pasteComplexItem(children[i], o, toolContentIDMap, parentAct);
		
			if(activityMap != null) {
				var myTransitions:Object = _ddm.getTransitionsForActivityUIID(children[i].activityUIID);
				if(myTransitions.out != null || myTransitions.into != null) {
					var dataObj:Object = new Object();
					dataObj.transitionOut = myTransitions.out;
					dataObj.transitionIn = myTransitions.into;
					
					dataObj.activity = children[i];
					dataObj.activityCopy = activityCopy;
					
					activityMap.put(children[i].activityUIID, dataObj);
				}
				
				if(activity.firstActivityUIID == children[i].activityUIID) {
					parentAct.firstActivityUIID = activityCopy.activityUIID;
				}
				
			} else if(activity.isBranchingActivity()) {
				if(activity.firstActivityUIID == children[i].activityUIID) {
					parentAct.firstActivityUIID = activityCopy.activityUIID;
				}
			}
		}
	}
	
	private function copyTransition(transitionToCopy:Transition, fromActivity:Activity, toActivity:Activity):Transition {
		Debugger.log("copying transition: " + transitionToCopy, Debugger.CRITICAL, "copyTransition", "CanvasHelper");
		
		var transitionCopy:Transition = transitionToCopy.clone();
		transitionCopy.transitionUIID = _ddm.newUIID();
		transitionCopy.fromUIID = fromActivity.activityUIID;
		transitionCopy.toUIID = toActivity.activityUIID;
		
		_ddm.addTransition(transitionCopy);
		
		return transitionCopy;
	}
	
	public function closeBranchView(prevActiveView) {
		var branchingAct:CanvasActivity = CanvasActivity(canvasModel.openBranchingActivities.pop());
		var parentBranching:CanvasActivity = (canvasModel.openBranchingActivities.length > 0) ? CanvasActivity(canvasModel.openBranchingActivities[canvasModel.openBranchingActivities.length-1]) : null;

		if(prevActiveView != null) 
			canvasModel.activeView = prevActiveView;
		else
			canvasModel.activeView = (parentBranching.activity.isBranchingActivity()) ? parentBranching.activity.branchView : canvasView;
		
		canvasModel.currentBranchingActivity = (parentBranching.activity.isBranchingActivity()) ? parentBranching : null;
		
		if(canvasModel.activeView instanceof CanvasComplexView)
			CanvasComplexView(canvasModel.activeView).branchingToClear.push(branchingAct);
		
		Debugger.log("activeView: " + canvasModel.activeView, Debugger.CRITICAL, "closeBranchView", "CanvasHelper");
		Debugger.log("currentBranchingActivity: " + canvasModel.currentBranchingActivity, Debugger.CRITICAL, "closeBranchView", "CanvasHelper");
		
	}
	
	/**
    * Opens the help->about dialog
    */
    public function openAboutLams() {
		
		var controller:CanvasController = canvasView.getController();
		
		var dialog:MovieClip = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('about_popup_title_lbl', [Dictionary.getValue('stream_reference_lbl')]),closeButton:true,scrollContentPath:'AboutLams'});
		dialog.addEventListener('contentLoaded',Delegate.create(controller, controller.openDialogLoaded));
		
	}
	
	public function openBranchView(ba, visible:Boolean){
		var cx:Number = ba._x + ba.getVisibleWidth()/2;
		var cy:Number = ba._y + ba.getVisibleHeight()/2;
		var isVisible:Boolean = (visible == null) ? true : visible;
		
		var target:MovieClip = canvasModel.activeView.branchContent; //(canvasModel.activeView instanceof CanvasBranchView) ? canvasModel.activeView.branchContent : _canvasView_mc.branchContent;
		
		Debugger.log("canvasModel.activeView: "+ canvasModel.activeView, Debugger.CRITICAL, "openBranchView", "CanvasHelper");
		
		var _branchView_mc:MovieClip = target.createChildAtDepth("canvasBranchView", target.getNextHighestDepth(), {_x: cx, _y: cy, _canvasBranchingActivity:ba, _open:isVisible, _prevActiveView: canvasModel.activeView});	
		var branchView:CanvasBranchView = CanvasBranchView(_branchView_mc);
		
		branchView.init(canvasModel, undefined);
		
		//Add listener to view so that we know when it's loaded
        branchView.addEventListener('load', Proxy.create(this, viewLoaded));
		
		canvasModel.addObserver(branchView);
		
		ba.activity.branchView = branchView;
		
		canvasModel.openBranchingActivities.push(ba);
	}
	
	public function openComplexView(ca:Object):Void {
		
		var target:MovieClip = canvasModel.activeView.complexViewer; // (canvasModel.activeView instanceof CanvasBranchView || canvasModel) ? canvasModel.activeView.complexViewer : _canvasView_mc.complexViewer;
		
		var cx:Number;
		var cy:Number;
		
		var parentAct:Activity = ddm.getActivityByUIID(ca.activity.parentUIID);
		var grandParentActivity:MovieClip = canvasModel.activitiesDisplayed.get(parentAct.parentUIID);
		var parentActivity:MovieClip = canvasModel.activitiesDisplayed.get(parentAct.activityUIID);
		
		if(canvasModel.activeView instanceof CanvasComplexView) {
			if(canvasModel.activeView.complexActivity == ca) {
				return;
			}
				
			target = canvasModel.activeView.complexViewer;
			
			Debugger.log("parentAct: " + parentAct.activityUIID, Debugger.CRITICAL, "openComplexView", "CanvasHelper");
			Debugger.log("parentAct type: " + parentAct.activityTypeID, Debugger.CRITICAL, "openComplexView", "CanvasHelper");
			Debugger.log("gpAct: " + grandParentActivity.activity.activityUIID, Debugger.CRITICAL, "openComplexView", "CanvasHelper");
			
			if(parentAct.isSequenceActivity() && canvasModel.activeView.openActivity instanceof CanvasOptionalActivity) {
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
		
		Debugger.log("co ord x: " + cx +  " y: " + cy, Debugger.CRITICAL, "openComplexView", "CanvasHelper");
		
		_canvasComplexView_mc = target.createChildAtDepth("canvasComplexView", DepthManager.kTop, {_x: cx, _y: cy, _complexActivity:ca, _parentActivity:parentActivity, _visible:false, _prevActiveView: canvasModel.activeView});
		canvasComplexView = CanvasComplexView(_canvasComplexView_mc);
		
		canvasComplexView.init(canvasModel, undefined);
		canvasComplexView.addEventListener('load', Proxy.create(this, viewLoaded));
		
		canvasModel.addObserver(canvasComplexView);
	}
	
	/**
	 * recieves event fired after update to the DDM
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function onDDMUpdated(evt:Object):Void{
		
		Debugger.log('DDM has been updated, _ddm.validDesign:'+_ddm.validDesign,Debugger.GEN,'onDDMUpdated','Canvas');
		//if its valid, its not anymore!
		if(_ddm.validDesign){
			_ddm.validDesign = false;
			checkValidDesign();
		}
		
		_ddm.modified = true;
		
		ApplicationParent.extCall('setSaved', 'false');
	}
	
	
	/**
	 * recieves event fired before updating the DDM
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function onDDMBeforeUpdate(evt:Object):Void{
		
		Debugger.log('DDM about to be updated',Debugger.GEN,'onDDMBeforeUpdate','Canvas');
		//take a snapshot of the design and save it in the undoStack
		var snapshot:Object = _ddm.toData();
		_undoStack.push(snapshot);
		_redoStack = new Array();
	}
	
	public function get ddm():DesignDataModel{
		return _ddm;
	}
	
	private function createContentFolder():Void{
		var callback:Function = Proxy.create(this,setNewContentFolderID);
		Application.getInstance().getComms().getRequest('authoring/author.do?method=createUniqueContentFolder&userID='+_root.userID,callback, false);
		
	}
	
	private function setNewContentFolderID(o:Object) {
		app.toolkit.model.broadcastLibraryUpdate();
		
		if(o instanceof LFError){
			o.showMessageConfirm();
		}else{
			if(StringUtils.isNull(_ddm.contentFolderID)) { _ddm.contentFolderID = String(o); }
		}
	}
	
	/**
	 * Called when a user confirms its ok to clear the design
	 * @usage   
	 * @param   ref 
	 * @return  
	 */
	public function confirmedClearDesign(ref):Void{
		var fn:Function = Proxy.create(ref,clearCanvas,true);
		fn.apply();
	}
}
	