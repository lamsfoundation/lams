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
class Canvas extends CanvasHelper {
	
    //Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	/**
	* Canvas Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	public function Canvas (target_mc:MovieClip,depth:Number,x:Number,y:Number,w:Number,h:Number){
		super();
	    mx.events.EventDispatcher.initialize(this);
        
		_target_mc = target_mc;
		
		//Design Data Model.
		_ddm = new DesignDataModel();
		
		//Create the model.
		//pass in a ref to this container
		canvasModel = new CanvasModel(this);
		
		_dictionary = Dictionary.getInstance();
		
		//Create the view
		_canvasView_mc = _target_mc.createChildAtDepth("canvasView",DepthManager.kTop);		

        //Cast toolkit view clip as ToolkitView and initialise passing in model
		canvasView = CanvasView(_canvasView_mc);
		canvasView.init(canvasModel,undefined,x,y,w,h);
        
        
        //Get reference to application and design data model
		app = Application.getInstance();
		
		
        //Get a ref to the cookie monster 
        _cm = CookieMonster.getInstance();
        _comms = ApplicationParent.getInstance().getComms();
		
		_undoStack = new Array();
		_redoStack = new Array();
		_isBusy = false;
		//some initialisation:


		//Add listener to view so that we know when it's loaded
        canvasView.addEventListener('load', Proxy.create(this,viewLoaded));
        
		_ddm.addEventListener('ddmUpdate', Proxy.create(this,onDDMUpdated));
		_ddm.addEventListener('ddmBeforeUpdate', Proxy.create(this,onDDMBeforeUpdate));
		
        //Register view with model to receive update events
		canvasModel.addObserver(canvasView);

        //Set the position by setting the model which will call update on the view
        canvasModel.setPosition(x,y);
        //Initialise size to the designed size
        canvasModel.setSize(w,h);
		
	}

	public function saveDesign(){
		if((_ddm.learningDesignID == undefined || _ddm.learningDesignID == "" || _ddm.learningDesignID == null || _ddm.learningDesignID =="undefined") || _ddm.learningDesignID == Config.NUMERIC_NULL_VALUE && (_ddm.title == "" || _ddm.title == undefined || _ddm.title == null)){
			
			// raise alert if design is empty
			if (canvasModel.activitiesDisplayed.size() < 1){
				Cursor.showCursor(Application.C_DEFAULT);
				var msg:String = Dictionary.getValue('al_empty_design');
				LFMessage.showMessageAlert(msg);
			}else {
				saveDesignToServerAs(Workspace.MODE_SAVE);
			}
		
		}else if(_ddm.readOnly && !_ddm.editOverrideLock){
			saveDesignToServerAs(Workspace.MODE_SAVEAS);
		}else if(_ddm.editOverrideLock){
			var errors:Array = canvasModel.validateDesign();
			
			if(errors.length > 0) {
				var errorPacket = new Object();
				errorPacket.messages = errors;
				
				var msg:String = Dictionary.getValue('cv_invalid_design_on_apply_changes');
				var okHandler = Proxy.create(this,showDesignValidationIssues, errorPacket);
				LFMessage.showMessageConfirm(msg,okHandler,null,Dictionary.getValue('cv_show_validation'));
				Cursor.showCursor(Application.C_DEFAULT);
			} else {
				saveDesignToServer();	// design is valid, save normal
			}
		
		}else{
			saveDesignToServer();
		}
	}
 
	/**
	 * Launch workspace browser dialog and set the design metat data for saving
	 * E.g. Title, Desc, Folder etc... also license if required?
	 * @usage   
	 * @param	tabToShow	The tab to be selected when the dialogue opens.
	 * @param 	mode		save mode
	 * @return  
	 */
	public function saveDesignToServerAs(mode:String){
		// if design as not been previously saved then we should use SAVE mode
		if(_ddm.learningDesignID == null) { mode = Workspace.MODE_SAVE }
		else { 
			//hold exisiting learningDesignID value in model (backup)
			_ddm.prevLearningDesignID = _ddm.learningDesignID;
			
			//clear the learningDesignID so it will not overwrite the existing one
			_ddm.learningDesignID = null;
		}
		
		
        var onOkCallback:Function = Proxy.create(this, saveDesignToServer);
		var ws = Application.getInstance().getWorkspace();
        ws.setDesignProperties("LOCATION", mode, onOkCallback);
		

	}
	/**
	 * Updates the design with the detsils form the workspace :
	 * 	* <code>
	*	_resultDTO.selectedResourceID 	//The ID of the resource that was selected when the dialogue closed
	*	_resultDTO.resourceName 		//The contents of the Name text field
	*	_resultDTO.resourceDescription 	//The contents of the description field on the propertirs tab
	*	_resultDTO.resourceLicenseText 	//The contents of the license text field
	*	_resultDTO.resourceLicenseID 	//The ID of the selected license from the drop down.
    *</code>
	* And then saves the design to the sever by posting XML via comms class
	 * @usage   
	 * @return  
	 */
	public function saveDesignToServer(workspaceResultDTO:Object):Boolean{
		_global.breakpoint();

		//TODO: Set the results from wsp into design.
		if(workspaceResultDTO != null){
			if(workspaceResultDTO.selectedResourceID != null){
				//must be overwriting an existing design as we have a new resourceID
				_ddm.learningDesignID = workspaceResultDTO.selectedResourceID;
			}
			_ddm.workspaceFolderID = workspaceResultDTO.targetWorkspaceFolderID;
			_ddm.title = workspaceResultDTO.resourceName;
			_ddm.description = workspaceResultDTO.resourceDescription;
			_ddm.licenseText = workspaceResultDTO.resourceLicenseText;
			_ddm.licenseID = workspaceResultDTO.resourceLicenseID;
		}
		
		var mode:String = Application.getInstance().getWorkspace().getWorkspaceModel().currentMode;
		_ddm.saveMode = (mode == Workspace.MODE_SAVEAS) ? 1 : 0;
		
		Debugger.log('SAVE MODE:'+_ddm.saveMode,Debugger.CRITICAL,'saveDesignToServer','Canvas');
		
		if(_ddm.hasRedundantBranchMappings(false)) {
			Cursor.showCursor(Application.C_DEFAULT);
			LFMessage.showMessageConfirm(Dictionary.getValue("redundant_branch_mappings_msg"), Proxy.create(_ddm, _ddm.removeRedundantBranchMappings, Proxy.create(this, saveDesignToServer, workspaceResultDTO)), null, Dictionary.getValue("al_continue"), null);
		} else {
		
			var dto:Object = _ddm.getDesignForSaving();
			var callback:Function = Proxy.create(this,onStoreDesignResponse);
			
			Application.getInstance().getComms().sendAndReceive(dto,"servlet/authoring/storeLearningDesignDetails",callback,false);
		}	
		
		return true;
	}
	
	/**
	 * now contains a validation response packet
	 * Displays to the user the results of the response.
	 * @usage   
	 * @param   r //the validation response
	 * @return  
	 */
	public function onStoreDesignResponse(r):Void{
		Application.getInstance().getWorkspace().getWV().clearDialog();
		
		if(r instanceof LFError){
			// reset old learning design ID if failed completing a save-as operation
			if(_ddm.prevLearningDesignID != null && _ddm.saveMode == 1) {
				_ddm.prevLearningDesignID = null;
			}
			
			Cursor.showCursor(Application.C_DEFAULT);
			r.showErrorAlert();
		}else{
			discardAutoSaveDesign();

			_ddm.learningDesignID = r.learningDesignID;
			_ddm.validDesign = r.valid;
			
			if(_ddm.saveMode == 1){
				Debugger.log('save mode: ' +_ddm.saveMode,Debugger.GEN,'onStoreDesignResponse','Canvas');		
				Debugger.log('updating activities.... ',Debugger.GEN,'onStoreDesignResponse','Canvas');		
			
				updateToolActivities(r);
				
				_ddm.readOnly = false;
				_ddm.copyTypeID = DesignDataModel.COPY_TYPE_ID_AUTHORING;
			
			} else {
				Debugger.log('save mode: ' +_ddm.saveMode,Debugger.GEN,'onStoreDesignResponse','Canvas');		
			
			}
			
			_ddm.modified = false;
			
			ApplicationParent.extCall("setSaved", "true");
			
			LFMenuBar.getInstance().enableExport(true);
			Debugger.log('_ddm.learningDesignID:'+_ddm.learningDesignID,Debugger.GEN,'onStoreDesignResponse','Canvas');		
			
			
			if(_ddm.validDesign){
				var msg:String = Dictionary.getValue('cv_valid_design_saved');
				var _requestSrc = _root.requestSrc;
				if(_requestSrc != null) {
					//show the window, on load, populate it
					var cc:CanvasController = canvasView.getController();
					var saveConfirmDialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('al_alert'),closeButton:false,scrollContentPath:"SaveConfirmDialog",msg:msg, requestSrc:_requestSrc, canvasModel:canvasModel,canvasController:cc});
	
				} else if(_ddm.editOverrideLock) {
					var finishEditHandler = Proxy.create(this,finishEditOnFly);
					msg = Dictionary.getValue('cv_eof_changes_applied');
					LFMessage.showMessageAlert(msg, finishEditHandler);
				} else {
					LFMessage.showMessageAlert(msg);
				}
			} else {
				var msg:String = Dictionary.getValue('cv_invalid_design_saved');
				var okHandler = Proxy.create(this,showDesignValidationIssues,r);
				LFMessage.showMessageConfirm(msg,okHandler,null,Dictionary.getValue('cv_show_validation'));
			}
			
			checkValidDesign();
			checkReadOnlyDesign();
			Cursor.showCursor(Application.C_DEFAULT);
		}
	}
	
	/**
	 * Called when a template activity is dropped onto the canvas
	 * @usage   
	 * @param   ta TemplateActivity
	 * @return  
	 */
	public function setDroppedTemplateActivity(ta:TemplateActivity, taParent:Number):Void{
		
		var actToCopy:Activity = ta.mainActivity;
		//loosly typed this var as it might be any type of activity
		var actToAdd:Activity;
		var actType:String;
		Debugger.log('actToCopy.activityTypeID:'+actToCopy.activityTypeID,Debugger.GEN,'setDroppedTemplateActivity','Canvas');			
		
		switch(actToCopy.activityTypeID){
			
			case(Activity.TOOL_ACTIVITY_TYPE):
				actType = "Tool"
				 actToAdd = ToolActivity(actToCopy.clone());
				//give it a new UIID:
				actToAdd.activityUIID = _ddm.newUIID();
			break;
			case(Activity.OPTIONAL_ACTIVITY_TYPE):
				actToAdd = Activity(actToCopy.clone());
				//give it a new UIID:
				actToAdd.activityUIID = _ddm.newUIID();
			
			case(Activity.PARALLEL_ACTIVITY_TYPE):
				actType = "Parallel"
				actToAdd = Activity(actToCopy.clone());
				
				//give it a new UIID:
				actToAdd.activityUIID = _ddm.newUIID();
				
				
			Debugger.log('parallel activity given new UIID of:'+actToAdd.activityUIID ,Debugger.GEN,'setDroppedTemplateActivity','Canvas');			
			//now get this acts children and add them to the design (WHINEY VOICE:"will somebody pleeeease think of the children.....")
			for(var i=0;i<ta.childActivities.length;i++){
					
					//Note: The next few line os code is now execute in the setNewChildContentID method
					//Find out if other types of activity can be held by complex acts 
					
					var child:Activity = ToolActivity(ta.childActivities[i].clone());
					child.activityUIID = _ddm.newUIID();
					//tell it who's the daddy (set its parent UIID)
					child.parentUIID = actToAdd.activityUIID;
					Debugger.log('child.parentUIID:'+child.parentUIID,Debugger.GEN,'setDroppedTemplateActivity','Canvas');			
					child.learningDesignID = _ddm.learningDesignID;
					//does not need mouse co-ords as in in container act.
					
					_ddm.addActivity(child);
					var callback:Function = Proxy.create(this,setNewChildContentID, child);
					var passChildToolID = ta.childActivities[i].toolID;
					Application.getInstance().getComms().getRequest('authoring/author.do?method=getToolContentID&toolID='+passChildToolID,callback, false);
			}				 
			break;
			
			
			default:
				new LFError("NOT ready to handle activity this Activivty type","Canvas.setDroppedTemplateActivity",this,ObjectUtils.printObject(ta));
				
		}
		
		//Set up the main activity for the canvas:
		
		
		//assign it the LearningDesignID
		actToAdd.learningDesignID = _ddm.learningDesignID;
		
		//give it the mouse co-ords
		if (actType = "Parallel"){
			actToAdd.xCoord = canvasModel.activeView.content._xmouse - (complexActWidth/2);
			actToAdd.yCoord = canvasModel.activeView.content._ymouse;
		}
		if(actType = "Tool"){
			actToAdd.xCoord = canvasModel.activeView.content._xmouse - (toolActWidth/2);
			actToAdd.yCoord = canvasModel.activeView.content._ymouse - (toolActHeight/2);
		}
				
		Debugger.log('actToAdd:'+actToAdd.title+':'+actToAdd.activityUIID + ":seq" + canvasModel.activeView.defaultSequenceActivity,4,'setDroppedTemplateActivity','Canvas');		
		
		if(canvasModel.activeView.defaultSequenceActivity != null) {
			actToAdd.parentUIID = canvasModel.activeView.defaultSequenceActivity.activityUIID;
		}

		_ddm.addActivity(actToAdd);
		
		//refresh the design
		canvasModel.setDirty();
		canvasModel.selectedItem = (canvasModel.activitiesDisplayed.get(actToAdd.activityUIID));
		
		//select the new thing
		if (taParent != undefined || taParent != null){
			actToAdd.parentUIID = taParent;
			canvasModel.removeActivity(actToAdd.activityUIID);
			canvasModel.removeActivity(taParent);
		}
		
		canvasModel.setDirty();
		
	}

	/**
	 * Revert canvas back to last saved design
	 * 
	 * @usage   
	 * @return  
	 */
	
	public function revertCanvas():Boolean {
		// can only revert canvas if design is saved
		if(_ddm.learningDesignID != null) {
			
			openDesignForEditOnFly(_ddm.learningDesignID);
			
		} else {
			// throw error alert
			return false;
		}
	}
	
	
	/**
	 * Returns canvas to init state, ready for new design
	 * @usage   
	 * @param   noWarn 
	 * @return  
	 */
	public function resetCanvas(noWarn:Boolean):Boolean{
		_undoStack = new Array();
		_redoStack = new Array();
		
		var r = clearCanvas(noWarn);
		
		return r;
		
	}
	
	/**
	* Opens a design using workspace and user to select design ID
	* passes the callback function to recieve selected ID
	*/
	public function openDesignBySelection(){
        //Work space opens dialog and user will select view
		if(_ddm.modified){
			LFMessage.showMessageConfirm(Dictionary.getValue('cv_design_unsaved'), Proxy.create(this,doOpenDesignBySelection), null);
		} else {
			doOpenDesignBySelection();
		}
	}
    
	public function doOpenDesignBySelection():Void{
		var callback:Function = Proxy.create(this, openDesignById);
		var ws = Application.getInstance().getWorkspace();
        ws.userSelectItem(callback);
	}
	
	/**
	 * Request design from server using supplied ID.
	 * @usage   
	 * @param   designId 
	 * @return  
	 */
    public function openDesignById(workspaceResultDTO:Object){
		Application.getInstance().getWorkspace().getWV().clearDialog();
		ObjectUtils.toString(workspaceResultDTO);
		var designId:Number = workspaceResultDTO.selectedResourceID;

        var callback:Function = Proxy.create(this,setDesign);
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designId,callback, false);
	
    }
	
	/**
	 * Request imported design from server
	 * 
	 * @usage   
b	 * @param   learningDesignID 
	 * @return  
	 */
	
	public function openDesignByImport(learningDesignID:Number){
		var callback:Function = Proxy.create(this,setDesign, true);
        canvasModel.importing = true;
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+learningDesignID,callback, false);
		
	}
	
	/**
	 * Request runtime-sequence design from server to be editted.
	 * 
	 * @usage   
	 * @param   learningDesignID 
	 * @return  
	 */
	
	public function openDesignForEditOnFly(learningDesignID:Number){
		var callback:Function = Proxy.create(this,setDesign, true);
        canvasModel.editing = true;
		
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+learningDesignID,callback, false);
		
	}
	
	public function getToolOutputDefinitions(ta:ToolActivity) {
		var callback:Function; 
       
		if(ta.toolContentID) {
			callback = Proxy.create(this, setToolOutputDefinitions, ta);
			Application.getInstance().getComms().getRequest('authoring/author.do?method=getToolOutputDefinitions&toolContentID='+ta.toolContentID, callback, false);
		} else {
			callback = Proxy.create(this, setToolContentForDefinitions, ta);
			canvasModel.getNewToolContentID(ta, callback);
		}
			
	}
	
	public function setToolContentForDefinitions(toolContentID:Number, ta:ToolActivity):Void {
		ta.toolContentID = toolContentID;
		
		getToolOutputDefinitions(ta);
	}
	
	public function setToolOutputDefinitions(r:Object, toolActivity:ToolActivity) {
		if(r instanceof LFError){
			Cursor.showCursor(Application.C_DEFAULT);
			r.showErrorAlert();
		}
		
		if(r.length > 0) {
			for(var i=0; i < r.length; i++) {
				Debugger.log("adding def: " + r[i].name + " desc: " + r[i].description, Debugger.CRITICAL, "setToolOutputDefinitions", "Canvas");
				toolActivity.addDefinition(r[i]);
			}
		}
				
		canvasModel.broadcastViewUpdate("OPEN_CONDITIONS_DIALOG", toolActivity);
		Debugger.log("total def: " + toolActivity.definitions.length, Debugger.CRITICAL, "setToolOutputDefinitions", "Canvas");
				
	}
	
	/**
	 * Called from the toolbar usually - starts or stops the gate tool
	 * @usage   
	 * @return  
	 */
	public function toggleGroupTool():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==ApplicationParent.C_GROUP){
			stopGroupTool();
		}else{
			startGroupTool();
		}
	}
	
	public function toggleBranchTool():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==ApplicationParent.C_BRANCH){
			stopBranchTool();
		}else{
			startBranchTool();
		}
	}
	
	public function toggleGateTool():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==ApplicationParent.C_GATE){
			stopGateTool();
		}else{
			startGateTool();
		}
	}
	
	public function toggleOptionalActivity():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==ApplicationParent.C_OPTIONAL){
			stopOptionalActivity();
		}else{
			startOptionalActivity(false);
		}
	}
	
	public function toggleOptionalSequenceActivity():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==ApplicationParent.C_OPTIONAL){
			stopOptionalActivity();
		}else{
			startOptionalActivity(true);
		}
	}
	
	public function toggleTransitionTool():Void{
		Debugger.log('Switch on Transition Tool', Debugger.GEN,'toogleTransitionTool','Canvas');
		var c:String = Cursor.getCurrentCursor();
		Debugger.log('Current Cursor: ' + c, Debugger.GEN, 'toogleTransitionTool', 'Canvas');
		
		if(c==ApplicationParent.C_TRANSITION) {
				stopTransitionTool();
		} else {
				startTransitionTool();
		}
		
	}
	
	public function stopActiveTool(){
		Debugger.log('Stopping Active Tool: ' + canvasModel.activeTool, Debugger.GEN,'stopActiveTool','Canvas');
		switch(canvasModel.activeTool){
			case CanvasModel.GATE_TOOL :
				stopGateTool();
				break;
			case CanvasModel.OPTIONAL_TOOL :
				stopOptionalActivity();
				break;
			case CanvasModel.GROUP_TOOL :
				stopGroupTool();
				break;
			case CanvasModel.TRANSITION_TOOL :
				stopTransitionTool();
				break;
			case CanvasModel.BRANCH_TOOL :
				stopBranchTool();
				break;
			default :
				Debugger.log('No tool active. Setting Default.', Debugger.GEN,'stopActiveTool','Canvas');
				Cursor.showCursor(ApplicationParent.C_DEFAULT);
				canvasModel.activeTool = "none";

		}
	}
	
	public function startGateTool(){
		Debugger.log('Starting gate tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(ApplicationParent.C_GATE);
		canvasModel.activeTool = CanvasModel.GATE_TOOL;
	}
		
	public function stopGateTool(){
		Debugger.log('Stopping gate tool',Debugger.GEN,'stopGateTool','Canvas');
		Cursor.showCursor(ApplicationParent.C_DEFAULT);
		canvasModel.activeTool = "none";
	}
	
	
	public function startOptionalActivity(isSequence:Boolean){
		Debugger.log('Starting Optioanl Activity',Debugger.GEN,'startOptionalActivity','Canvas');
		Cursor.showCursor(ApplicationParent.C_OPTIONAL);
		
		canvasModel.activeTool = (isSequence) ? CanvasModel.OPTIONAL_SEQ_TOOL : CanvasModel.OPTIONAL_TOOL;
	}
		
	public function stopOptionalActivity(){
		Debugger.log('Stopping Optioanl Activity',Debugger.GEN,'stopOptionalActivity','Canvas');
		Cursor.showCursor(ApplicationParent.C_DEFAULT);
		canvasModel.activeTool = "none";
	}
	public function startGroupTool(){
		Debugger.log('Starting group tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(ApplicationParent.C_GROUP);
		canvasModel.activeTool = CanvasModel.GROUP_TOOL;
	}
	
	public function stopGroupTool(){
		Debugger.log('Stopping group tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(ApplicationParent.C_DEFAULT);
		canvasModel.activeTool = "none";
	}
	
	public function startBranchTool(){
		Debugger.log('Starting branch tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(ApplicationParent.C_BRANCH);
		canvasModel.activeTool = CanvasModel.BRANCH_TOOL;
	}
	
	public function stopBranchTool(){
		Debugger.log('Stopping branch tool',Debugger.GEN,'startBranchTool','Canvas');
		Cursor.showCursor(ApplicationParent.C_DEFAULT);
		canvasModel.activeTool = "none";
	}
	
	/**
	 * Called by the top menu bar and the tool bar to start the transition tool, switches cursor.
	 * @usage   
	 * @return  
	 */
	public function startTransitionTool():Void{
		Debugger.log('Starting transition tool',Debugger.GEN,'startTransitionTool','Canvas');			
		Cursor.showCursor(ApplicationParent.C_TRANSITION);
		canvasModel.lockAllComplexActivities();
		canvasModel.startTransitionTool();
		
	}
	
	/**
	 * Called by the top menu bar and the tool bar to stop the transition tool, switches cursor.
	 * @usage   
	 * @return  
	 */
	public function stopTransitionTool():Void{
		Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','Canvas');			
		Cursor.showCursor(ApplicationParent.C_DEFAULT);
		canvasModel.unlockAllComplexActivities();
		canvasModel.stopTransitionTool();
	}
	
		/**
	 * Method to open Preview popup window.
	 */
	public function launchPreviewWindow():Void{
		if(_ddm.validDesign){
			Debugger.log('Launching Preview Window (initialising)',Debugger.GEN,'launchPreviewWindow','Canvas');
			var callback:Function = Proxy.create(this,onInitPreviewResponse); 
			Application.getInstance().getComms().sendAndReceive(_ddm.getDataForPreview(Dictionary.getValue('preview_btn'),"started%20automatically"),"monitoring/initializeLesson",callback,false)
		}
	}

	public function onInitPreviewResponse(r):Void{
		if(r instanceof LFError) {
			r.showMessageConfirm();
		} else {
			Debugger.log('Launching Preview Window (starting lesson ' + r + ')',Debugger.GEN,'onInitPreviewResponse','Canvas');
			var callback:Function = Proxy.create(this,onLaunchPreviewResponse); 
			Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=startPreviewLesson&lessonID='+r,callback, false);
		}
	}

	/**
	 * now contains a Lession ID response from wddx packet
	 * Returns the lessionID to send it to popup method in JsPopup .
	 * @usage   http://localhost:8080/lams/learning/learner.do?method=joinLesson&userId=4&lessonId=12 
	 * @param   r //the validation response
	 * @return  
	 */
	public function onLaunchPreviewResponse(r):Void{
		if(r instanceof LFError){
			r.showMessageConfirm();
		}else{
			var uID = Config.getInstance().userID;
			var serverUrl = Config.getInstance().serverUrl;

			// open preview in new window
			ApplicationParent.extCall("openPreview", r);
			Debugger.log('Recieved Lesson ID: '+r ,Debugger.GEN,'onLaunchPreviewResponse','Canvas');
		}
	}
	
	/**
	* Method to open Import popup window
	*/
	public function launchImportWindow():Void{
		Debugger.log('Launching Import Window',Debugger.GEN,'launchImportWindow','Canvas');
		if(_ddm.modified){
			LFMessage.showMessageConfirm(Dictionary.getValue('cv_design_unsaved'), Proxy.create(this,doImportLaunch), null);
		} else {
			doImportLaunch();
		}
	}
	
	public function doImportLaunch():Void{
		var serverUrl = Config.getInstance().serverUrl;
		JsPopup.getInstance().launchPopupWindow(serverUrl+'authoring/importToolContent.do?method=import', 'Import', 298, 800, true, true, false, false, false);
	}
	
	/**
	* Method to open Export popup window
	*/
	public function launchExportWindow():Void{
		Debugger.log('Launching Export Window',Debugger.GEN,'launchExportWindow','Canvas');
		
		if(_ddm.learningDesignID == null) {
			LFMessage.showMessageAlert(Dictionary.getValue('cv_design_export_unsaved'), null);
		}else if(_ddm.modified){
			LFMessage.showMessageConfirm(Dictionary.getValue('cv_design_unsaved'), Proxy.create(this,doExportLaunch), null);
		} else {
			doExportLaunch();
		}
		
	}
	
	public function doExportLaunch():Void{
		var serverUrl = Config.getInstance().serverUrl;
		var learningDesignID = _ddm.learningDesignID;
		JsPopup.getInstance().launchPopupWindow(serverUrl+'authoring/exportToolContent.do?learningDesignID=' + learningDesignID, 'Export', 298, 712, true, true, false, false, false);
	}
		
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number,height:Number):Void{
		canvasModel.setSize(width, height);
	}
	
	public function addBin(target:MovieClip) {
		if(_bin != null) hideBin();
		
		var cc:CanvasController = canvasView.getController();
		_bin = target.attachMovie("Bin", "Bin", target.getNextHighestDepth(), {_canvasController:cc, _targetView:target});
	}
	
	public function hideBin():Void{
		_bin.removeMovieClip();
	}
	
	/**
	 * Undo the last change to the DDM.
	 * TODO: Does not handle moving activities on the canvas, only when actual change to activities or transitions.  
	 * Need to generate update event when re-position activities
	 * @usage   
	 * @return  
	 */
	public function undo():Void{
		
		if(_undoStack.length>0){
			//get the last state off the stack
			var snapshot = _undoStack.pop();
			
			//get a copy of the current design and stick it in redo
			_redoStack.push(_ddm.toData());
			
			clearCanvas(true);
			//set the current design to the snapshot value
			_ddm.setDesign(snapshot,true);
			canvasModel.setDirty();
			
		}else{
			Debugger.log("Cannot Undo! no data on stack!",Debugger.GEN,'redo','Canvas');
		}
	}
	
	/**
	 * Redo last what was undone by the undo method.
	 * NOTE: if a new edit is made, the re-do stack is cleared
	 * @usage   
	 * @return  
	 */
	public function redo():Void{
		
		if(_redoStack.length > 0){
			//get the last state off the stack
			var snapshot = _redoStack.pop();
			
			_undoStack.push(_ddm.toData());
			
			clearCanvas(true);
			
			_ddm.setDesign(snapshot,true);
			canvasModel.setDirty();
			
		}else{
			Debugger.log("Cannot Redo! no data on stack!",Debugger.GEN,'redo','Canvas');
		}
	
	}
	
	/**
	 * Open the Help page for the selected Tool (Canvas) Activity
	 *  
	 * @param   ca 	CanvasActivity
	 * @return  
	 */
	
	public function getHelp(ca:CanvasActivity) {

		if(ca.activity.helpURL != undefined || ca.activity.helpURL != null) {
			Debugger.log("Opening help page with locale " + _root.lang + _root.country + ": " + ca.activity.helpURL,Debugger.GEN,'getHelp','Canvas');
			var locale:String = _root.lang + _root.country;
			
			ApplicationParent.extCall("openURL", ca.activity.helpURL + app.module + "#" + ca.activity.toolSignature + app.module + "-" + locale);
		
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
		var target:String = actToolSignature + app.module + '#' + actToolSignature+ app.module + '-' + locale;
		
		ApplicationParent.extCall("openURL", url + target);
	}
	
	private function openGateHelp(url:String){
		var actToolSignature:String = Application.FLASH_TOOLSIGNATURE_GATE
		var locale:String = _root.lang + _root.country;
		var target:String = actToolSignature + app.module + '#' + actToolSignature + app.module + '-' + locale;
		ApplicationParent.extCall("openURL", url + target);
	}
	
	public function get toolActivityWidth():Number{
		return toolActWidth;
	}
	
	public function get toolActivityHeight():Number{
		return toolActHeight;
	}
	
	public function get complexActivityWidth():Number{
		return complexActWidth;
	}
	
	private function setBusy():Void{
		if(_isBusy){
			//Debugger.log('!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
			//Debugger.log('!!!!!!!!!!!!!!!!!!!! HASHTABLE ACCESED WHILE BUSY !!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
			//Debugger.log('!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!',1,'checkBusy','org.lamsfoundation.lams.common.util.Hashtable');
		}
		_isBusy=true;
	}
	
	private function clearBusy():Void{
		_isBusy=false;
	}
	/**
	* Used by application to set the Position
	* @param x
	* @param y
	*/
	public function setPosition(x:Number,y:Number):Void{
		canvasModel.setPosition(x,y);
	}
	
	public function get model():CanvasModel{
		return getCanvasModel();
	}
	
	public function getCanvasModel():CanvasModel{
			return canvasModel;
	}
	
	public function get view():MovieClip{
		return getCanvasView();
	}
		
	public function getCanvasView():MovieClip{
		return canvasView;
	}
	
	public function get taWidth():Number{
		return toolActWidth	
	}
	
	public function get taHeight():Number{
		return toolActHeight
	}

	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get bin ():MovieClip {
		return _bin;
	}

	public function get className():String{
		return 'Canvas';
	}
}
