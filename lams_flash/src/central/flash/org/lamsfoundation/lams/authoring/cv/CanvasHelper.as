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
	private var canvasBranchView:CanvasBranchView;
	private var _canvasView_mc:MovieClip;
	private var _canvasBranchView_mc:MovieClip;
	
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
			} else {
				canvasModel.getCanvas().addBin(evt.target.activityLayer);
				
				var autosave_config_interval = Config.getInstance().getItem(AUTOSAVE_CONFIG);
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
			
			if(canvasModel.importing){ 
				Application.getInstance().getWorkspace().getWorkspaceModel().clearWorkspaceCache(_ddm.workspaceFolderID);
				canvasModel.importing = false;
			} else if(canvasModel.editing){
				// TODO: stuff to do before design is displayed
				// do we need editing flag in CanvasModel?
			}
			
			checkValidDesign();
			checkReadOnlyDesign();
			canvasModel.setDesignTitle();
			canvasModel.setDirty();
			LFMenuBar.getInstance().enableExport(!canvasModel.autoSaveWait);
		
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
			
			if(canvasModel.activeView instanceof CanvasBranchView) {
				canvasModel.activeView.removeMovieClip();
				closeBranchView();
			}
			
			canvasModel.setDirty();
			
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
				var res = CookieMonster.save(dto,tag,true);
				
				if(!res){
					// error auto-saving
					var msg:String = Dictionary.getValue('cv_autosave_err_msg');
					LFMessage.showMessageAlert(msg);
				}
			}
		} else if(canvasModel.autoSaveWait) {
			discardAutoSaveDesign();
		}
		
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
		
		// remove transitions connected to this activity being removed
		_ddm.removeTransitionByConnection(activityUIID);
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
			Application.getInstance().getToolbar().setButtonState('preview',true);
			LFMenuBar.getInstance().enableExport(true);
		}else{
			Application.getInstance().getToolbar().setButtonState('preview',false);
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
		if (o.data instanceof CanvasActivity){
			Debugger.log('instance is CA',Debugger.GEN,'setPastedItem','Canvas');
			var callback:Function = Proxy.create(this,setNewContentID, o);
			Application.getInstance().getComms().getRequest('authoring/author.do?method=copyToolContent&toolContentID='+o.data.activity.toolContentID+'&userID='+_root.userID,callback, false);
		
		} else if(o.data instanceof ToolActivity){
			Debugger.log('instance is Tool',Debugger.GEN,'setPastedItem','Canvas');
			var callback:Function = Proxy.create(this,setNewContentID, o);
			Application.getInstance().getComms().getRequest('authoring/author.do?method=copyToolContent&toolContentID='+o.toolContentID+'&userID='+_root.userID,callback, false);
			
		} else{
			Debugger.log('Cant paste this item!',Debugger.GEN,'setPastedItem','Canvas');
		}
	}
	
	private function setNewContentID(r, o){
		if(r instanceof LFError){
			r.showMessageConfirm();
		}else{
			_newToolContentID = r;
			if (o.data instanceof CanvasActivity){
				return pasteItem(o.data.activity, o, _newToolContentID);
			}else if(o.data instanceof ToolActivity){
				return pasteItem(o.data, o, _newToolContentID);
			}
		}
		
	}
	
	private function pasteItem(toolToCopy:ToolActivity, o:Object, newToolContentID:Number):Object{
		//clone the activity
		var newToolActivity:ToolActivity = toolToCopy.clone();
		newToolActivity.activityUIID = _ddm.newUIID();
		if (newToolContentID != null || newToolContentID != undefined){
			newToolActivity.toolContentID = newToolContentID;
		}
		newToolActivity.xCoord = o.data.activity.xCoord + 10
		newToolActivity.yCoord = o.data.activity.yCoord + 10
		canvasModel.selectedItem = newToolActivity;
			
		if(o.type == Application.CUT_TYPE){ 
			Application.getInstance().setClipboardData(newToolActivity, Application.COPY_TYPE);
			removeActivity(o.data.activity.activityUIID); 
		} else {
			if(o.count <= 1) { newToolActivity.title = Dictionary.getValue('prefix_copyof')+newToolActivity.title; }
			else { newToolActivity.title = Dictionary.getValue('prefix_copyof_count', [o.count])+newToolActivity.title; }
		}
		
		_ddm.addActivity(newToolActivity);
		canvasModel.setDirty();
		
		return newToolActivity;
	}
	
	public function closeBranchView() {
		canvasModel.activeView = canvasView;
		canvasModel.currentBranchingActivity = null;
	}
	
	/**
    * Opens the help->about dialog
    */
    public function openAboutLams() {
		
		var controller:CanvasController = canvasView.getController();
		
		var dialog:MovieClip = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('about_popup_title_lbl', [Dictionary.getValue('stream_reference_lbl')]),closeButton:true,scrollContentPath:'AboutLams'});
		dialog.addEventListener('contentLoaded',Delegate.create(controller, controller.openDialogLoaded));
		
	}
	
	public function openBranchView(ba){
		
		var cx:Number = ba._x + ba.getVisibleWidth()/2;
		var cy:Number = ba._y + ba.getVisibleHeight()/2;
		
		var _branchView_mc:MovieClip = _canvasView_mc.content.createChildAtDepth("canvasBranchView", DepthManager.kTop, {_x: cx, _y: cy, _canvasBranchingActivity:ba});	
		var branchView:CanvasBranchView = CanvasBranchView(_branchView_mc);
		branchView.init(canvasModel,undefined);
		
		//Add listener to view so that we know when it's loaded
        branchView.addEventListener('load', Proxy.create(this,viewLoaded));
		
		canvasModel.addObserver(branchView);
		
		ba.branchView = branchView;
		
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
	