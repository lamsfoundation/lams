import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.tk.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.* 
import mx.managers.*
import mx.utils.*

/**
 * The canvas is the main screen area of the LAMS application where activies are added and sequenced
 * Note - This holds the DesignDataModel _ddm 
 * @version 1.0
 * @since   
 */
class org.lamsfoundation.lams.authoring.cv.Canvas {
	
	//Constants
	public static var USE_PROPERTY_INSPECTOR = true;
	
	//Model
	private var canvasModel:CanvasModel;

	//View
	private var canvasView:CanvasView;
    
	private var _canvasView_mc:MovieClip;
	private var app:Application;
	private var _ddm:DesignDataModel;
	private var _dictionary:Dictionary;
	private var _config:Config;
	private var _undoStack:Array;	
	private var _redoStack:Array;	
	
	
    private var _pi:MovieClip; //Property inspector
    private var _bin:MovieClip;//bin
	
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
        mx.events.EventDispatcher.initialize(this);
        
		//Design Data Model.
		_ddm = new DesignDataModel();
		
		//Create the model.
		//pass in a ref to this container
		canvasModel = new CanvasModel(this);
		
		_dictionary = Dictionary.getInstance();
		
		//Create the view
		_canvasView_mc = target_mc.createChildAtDepth("canvasView",DepthManager.kTop);		

        //Cast toolkit view clip as ToolkitView and initialise passing in model
		canvasView = CanvasView(_canvasView_mc);
		canvasView.init(canvasModel,undefined,x,y,w,h);
        
        
        //Get reference to application and design data model
		app = Application.getInstance();
		
		_undoStack = new Array();
		_redoStack = new Array();
		
		//some initialisation:


		//Add listener to view so that we know when it's loaded
        canvasView.addEventListener('load',Proxy.create(this,viewLoaded));
        
		_ddm.addEventListener('ddmUpdate',Proxy.create(this,onDDMUpdated));
		_ddm.addEventListener('ddmBeforeUpdate',Proxy.create(this,onDDMBeforeUpdate));
		
		

        //Register view with model to receive update events
		canvasModel.addObserver(canvasView);
        

        //Set the position by setting the model which will call update on the view
        canvasModel.setPosition(x,y);
        //Initialise size to the designed size
        canvasModel.setSize(w,h);
		
		//muist comne after the canvasView as we use the defaultController method to get a controller ref.
		_dictionary.addEventListener('init',Proxy.create(this,setupPI));
		
		
		
		//if in monitor, dont do it!
		initBin();
		
		
	}
	
	
	
    public function setupPI(){
		if(USE_PROPERTY_INSPECTOR){
			initPropertyInspector();
		}
	}
    /**
    * Event dispatched from the view once it's loaded
    */
    public function viewLoaded(evt:Object) {
        //Debugger.log('Canvas view loaded: ' + evt.type,Debugger.GEN,'viewLoaded','Canvas');
        if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            Debugger.log('Event type not recognised : ' + evt.type,Debugger.CRITICAL,'viewLoaded','Canvas');
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
        ws.userSelectItem(callback);
	}
    
	/**
	 * Request design from server using supplied ID.
	 * @usage   
	 * @param   designId 
	 * @return  
	 */
    public function openDesignById(workspaceResultDTO:Object){
		
		ObjectUtils.toString(workspaceResultDTO);
		var designId:Number = workspaceResultDTO.selectedResourceID;

        var callback:Function = Proxy.create(this,setDesign);
           
		//Application.getInstance().getComms().getRequest('flashxml/learning_design.xml',callback, false);
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designId,callback, false);
		//var designObject:Object = Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesign&learningDesignID='+designId,callback);
        
        
    }
	
	public function saveDesign(){
		if(_ddm.learningDesignID == undefined){
			saveDesignToServerAs();
		}else{
			saveDesignToServer();
		}
	}
    
	/**
	 * Launch workspace browser dialog and set the design metat data for saving
	 * E.g. Title, Desc, Folder etc... also license if required?
	 * @usage   
	 * @param	tabToShow	The tab to be selected when the dialogue opens.
	 * @return  
	 */
	public function saveDesignToServerAs(){
		//clear the learningDesignID so it will not overwrite the existing one
		_ddm.learningDesignID = null;
		
        var onOkCallback:Function = Proxy.create(this, saveDesignToServer);
		var ws = Application.getInstance().getWorkspace();
        ws.setDesignProperties("LOCATION",onOkCallback);
		

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
		
		
		var dto:Object = _ddm.getDesignForSaving();
		
		var callback:Function = Proxy.create(this,onStoreDesignResponse);
		
		Application.getInstance().getComms().sendAndReceive(dto,"authoring/storeLearningDesignDetails",callback,false);
		//Application.getInstance().getComms().sendAndReceive(dto,"http://dolly.uklams.net:8080/lams/authoring/authorServlet",onStoreDesignResponse,true);
		//Application.getInstance().getComms().sendAndReceive(dto,"http://geo.uklams.net/testing/printPost.php",onStoreDesignResponse,true);
		
		return true;
			//public function sendAndReceive(dto:Object, requestURL:String,handler:Function,isFullURL){
	}
	
	/**
	 * now contains a validation response packet
	 * Displays to the user the results of the response.
	 * @usage   
	 * @param   r //the validation response
	 * @return  
	 */
	public function onStoreDesignResponse(r):Void{
		//Debugger.log('Response:'+ObjectUtils.printObject(response),Debugger.GEN,'onStoreDesignResponse','Canvas');
		if(r instanceof LFError){
			r.showErrorAlert();
		}else{
			//_global.breakpoint();
			//Debugger.log('_ddm.learningDesignID:'+_ddm.learningDesignID,Debugger.GEN,'setDroppedTemplateActivity','Canvas');		

			_ddm.learningDesignID = r.learningDesignID;
			_ddm.validDesign = r.valid;
			
			
			Debugger.log('_ddm.learningDesignID:'+_ddm.learningDesignID,Debugger.GEN,'onStoreDesignResponse','Canvas');		
			
			
			if(_ddm.validDesign){
				//var msg:String = "Congratulations! - Your design is valid has been saved"+r.learningDesignID;
				//TODO take this from the dictionary
				var msg:String = Dictionary.getValue('cv_valid_design_saved');
				LFMessage.showMessageAlert(msg);
			}else{
				
				var msg:String = Dictionary.getValue('cv_invalid_design_saved');
				//public static function howMessageConfirm(msg, okHandler:Function, cancelHandler:Function,okLabel:String,cancelLabel:String){
				var okHandler = Proxy.create(this,showDesignValidationIssues,r);
				LFMessage.showMessageConfirm(msg,okHandler,null,Dictionary.getValue('cv_show_validation'));
				
			}
			
			checkValidDesign();
			
		}
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
	
	public function checkValidDesign(){
		if(_ddm.validDesign){
			Application.getInstance().getToolbar().setButtonState('preview',true);
		}else{
			Application.getInstance().getToolbar().setButtonState('preview',false);
		}
		
	}
	
	/**
	 * Called when a template activity is dropped onto the canvas
	 * @usage   
	 * @param   ta TemplateActivity
	 * @return  
	 */
	public function setDroppedTemplateActivity(ta:TemplateActivity):Void{
		
		var actToCopy:Activity = ta.mainActivity;
		//loosly typed this var as it might be any type of activity
		var actToAdd:Activity;
		
		Debugger.log('actToCopy.activityTypeID:'+actToCopy.activityTypeID,Debugger.GEN,'setDroppedTemplateActivity','Canvas');			
		//_global.breakpoint();
		switch(actToCopy.activityTypeID){
			
			case(Activity.TOOL_ACTIVITY_TYPE):
				 actToAdd = ToolActivity(actToCopy.clone());
				//give it a new UIID:
				actToAdd.activityUIID = _ddm.newUIID();
			break;
			case(Activity.OPTIONAL_ACTIVITY_TYPE):
				actToAdd = Activity(actToCopy.clone());
				//give it a new UIID:
				actToAdd.activityUIID = _ddm.newUIID();
			
			case(Activity.PARALLEL_ACTIVITY_TYPE):
			
				actToAdd = Activity(actToCopy.clone());
				
				//give it a new UIID:
				actToAdd.activityUIID = _ddm.newUIID();
				
				
				Debugger.log('parallel activity given new UIID of:'+actToAdd.activityUIID ,Debugger.GEN,'setDroppedTemplateActivity','Canvas');			
				 //now get this acts children and add them to the design (WHINEY VOICE:"will somebody pleeeease think of the children.....")
				for(var i=0;i<ta.childActivities.length;i++){
					//TODO: Find out if other types of activity can be held by complex acts
					var child:Activity = Activity(ta.childActivities[i].clone());
					child.activityUIID = _ddm.newUIID();
					//tell it who's the daddy (set its parent UIID)
					child.parentUIID = actToAdd.activityUIID;
					Debugger.log('child.parentUIID:'+child.parentUIID,Debugger.GEN,'setDroppedTemplateActivity','Canvas');			
					child.learningDesignID = _ddm.learningDesignID;
					//does not need mouse co-ords as in in container act.
					
					_ddm.addActivity(child);
					
				}
				 
			break;
			
			default:
				new LFError("NOT ready to handle activity this Activivty type","Canvas.setDroppedTemplateActivity",this,ObjectUtils.printObject(ta));
				
		}
		
		//Set up the main activity for the canvas:
		
		
		//assign it the LearningDesignID
		actToAdd.learningDesignID = _ddm.learningDesignID;
		//give it the mouse co-ords
		actToAdd.xCoord = canvasView.getViewMc()._xmouse;
		actToAdd.yCoord = canvasView.getViewMc()._ymouse;
				
		Debugger.log('actToAdd:'+actToAdd.title+':'+actToAdd.activityUIID,4,'setDroppedTemplateActivity','Canvas');		

		_ddm.addActivity(actToAdd);
		//refresh the design
		canvasModel.setDirty();
		//select the new thing
		canvasModel.selectedItem = (canvasModel.activitiesDisplayed.get(actToAdd.activityUIID));
	}
	

/*	public function addActivity(a:Activity){
		
	}
	*/
	/**
	 * Removes an activity from Design Data Model using its activityUIID.  
	 * Called by the bin
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	public function removeActivity(activityUIID:Number){
		//Debugger.log('activityUIID:'+activityUIID,4,'removeActivity','Canvas');		
		_ddm.removeActivity(activityUIID);
		canvasModel.setDirty();		
		//select the new thing
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
		//Debugger.log('transitionUIID:'+transitionUIID,4,'removeTransition','Canvas');		
		_ddm.removeTransition(transitionUIID);
		canvasModel.setDirty();		
		//select the new thing
		canvasModel.selectedItem = null;
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
			checkValidDesign();
			canvasModel.setDirty();
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
		//_global.breakpoint();
		var s = false;
		var ref = this;
		Debugger.log('noWarn:'+noWarn,4,'clearCanvas','Canvas');
		if(noWarn){
			_ddm = new DesignDataModel();
			//as its a new instance of the ddm,need to add the listener again
			_ddm.addEventListener('ddmUpdate',Proxy.create(this,onDDMUpdated));
			_ddm.addEventListener('ddmBeforeUpdate',Proxy.create(this,onDDMBeforeUpdate));
			checkValidDesign();
			canvasModel.setDirty();
			return true;
		}else{
			var fn:Function = Proxy.create(ref,confirmedClearDesign, ref);
			LFMessage.showMessageConfirm(Dictionary.getValue('new_confirm_msg'), fn,null);
			Debugger.log('Set design failed as old design could not be cleared',Debugger.CRITICAL,"setDesign",'Canvas');		
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
	 * Called when a user confirms its ok to clear the design
	 * @usage   
	 * @param   ref 
	 * @return  
	 */
	public function confirmedClearDesign(ref):Void{
		var fn:Function = Proxy.create(ref,clearCanvas,true);
		fn.apply();
	}
	
	/**
	 * Called when the user initiates a paste.  recieves a reference to the item to be copied
	 * @usage   
	 * @param   o Item to be copied
	 * @return  
	 */
	public function setPastedItem(o:Object):Object{
		trace("called on right click");
		if (o instanceof CanvasActivity){
			//clone the activity
			var newToolActivity:ToolActivity = o.activity.clone();
			newToolActivity.title = Dictionary.getValue('prefix_copyof')+newToolActivity.title;
			newToolActivity.activityUIID = _ddm.newUIID();
			
			_ddm.addActivity(newToolActivity);
			canvasModel.setDirty();
			return newToolActivity;
		}else{
			Debugger.log('Cant paste this item!',Debugger.GEN,'setPastedItem','Canvas');
		}
	}
	
		
	/**
	 * Called from the toolbar usually - starts or stops the gate tool
	 * @usage   
	 * @return  
	 */
	public function toggleGroupTool():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==Application.C_GROUP){
			stopGroupTool();
		}else{
			startGroupTool();
		}
	}
	
	public function toggleGateTool():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==Application.C_GATE){
			stopGateTool();
		}else{
			startGateTool();
		}
	}
	
	public function toggleOptionalActivity():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==Application.C_OPTIONAL){
			stopOptionalActivity();
		}else{
			startOptionalActivity();
		}
	}
	
	public function toggleTransitionTool():Void{
		var c:String = Cursor.getCurrentCursor();
		if(c==Application.C_TRANSITION){
			stopTransitionTool();
		}else{
			startTransitionTool();
		}
	}
	
	
	public function startGateTool(){
		Debugger.log('Starting gate tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(Application.C_GATE);
		canvasModel.activeTool = CanvasModel.GATE_TOOL;
	}
		
	public function stopGateTool(){
		Debugger.log('Stopping gate tool',Debugger.GEN,'stopGateTool','Canvas');
		Cursor.showCursor(Application.C_DEFAULT);
		canvasModel.activeTool = null;
	}
	
	
	public function startOptionalActivity(){
		Debugger.log('Starting Optioanl Activity',Debugger.GEN,'startOptionalActivity','Canvas');
		Cursor.showCursor(Application.C_OPTIONAL);
		canvasModel.activeTool = CanvasModel.OPTIONAL_TOOL;
	}
		
	public function stopOptionalActivity(){
		Debugger.log('Stopping Optioanl Activity',Debugger.GEN,'stopOptionalActivity','Canvas');
		Cursor.showCursor(Application.C_DEFAULT);
		canvasModel.activeTool = null;
	}
	public function startGroupTool(){
		Debugger.log('Starting group tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(Application.C_GROUP);
		canvasModel.activeTool = CanvasModel.GROUP_TOOL;
	}
	
	public function stopGroupTool(){
		Debugger.log('Stopping group tool',Debugger.GEN,'startGateTool','Canvas');
		Cursor.showCursor(Application.C_DEFAULT);
		canvasModel.activeTool = null;
	}
	
	
	/**
	 * Called by the top menu bar and the tool bar to start the transition tool, switches cursor.
	 * @usage   
	 * @return  
	 */
	public function startTransitionTool():Void{
		//Debugger.log('Starting transition tool',Debugger.GEN,'startTransitionTool','Canvas');			
		Cursor.showCursor(Application.C_TRANSITION);
		canvasModel.startTransitionTool();
	}
	
	/**
	 * Called by the top menu bar and the tool bar to stop the transition tool, switches cursor.
	 * @usage   
	 * @return  
	 */
	public function stopTransitionTool():Void{
		//Debugger.log('Stopping transition tool',Debugger.GEN,'stopTransitionTool','Canvas');			
		Cursor.showCursor(Application.C_DEFAULT);
		canvasModel.stopTransitionTool();
	}
	
	/**
	 * Method to open Preview popup window.
	 */
	public function launchPreviewWindow():Void{
		if(_ddm.validDesign){
			 
			var designID = _ddm.learningDesignID
			var uID = Config.getInstance().userID;
			Debugger.log('Launching Preview Window',Debugger.GEN,'launchPreviewWindow','Canvas');
			var callback:Function = Proxy.create(this,onLaunchPreviewResponse); 
			Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=startPreviewLesson&userID='+uID+'&learningDesignID='+designID+'&title=preview&description=started%20automatically ',callback, false);
			
			
		}//Cursor.showCursor(Application.C_GATE);
		//canvasModel.activeTool = null;    //CanvasModel.GATE_TOOL;
	}

	/**
	 * now contains a Lession ID response from wddx packet
	 * Returns the lessionID to send it to popup method in JsPopup .
	 * @usage   http://localhost:8080/lams/learning/learner.do?method=joinLesson&userId=4&lessonId=12 
	 * @param   r //the validation response
	 * @return  
	 */
	public function onLaunchPreviewResponse(r):Void{
		//Debugger.log('Response:'+ObjectUtils.printObject(response),Debugger.GEN,'onStoreDesignResponse','Canvas');
		if(r instanceof LFError){
			r.showMessageConfirm();
		}else{
			//LFMessage.showMessageAlert('calling javascript from Austhor.jsp');
			var uID = Config.getInstance().userID;
			var serverUrl = Config.getInstance().serverUrl;
			//Create an instance of JsPopup to access launchPopupWindow method.
			JsPopup.getInstance().launchPopupWindow(serverUrl+'learning/learner.do?method=joinLesson&userId='+uID+'&lessonId='+r, 'Preview of Lession '+r.startPreviewSession, 570, 796, true, true, true);
			Debugger.log('Recieved Lesson ID: '+r ,Debugger.GEN,'onLaunchPreviewResponse','Canvas');
			//_global.breakpoint();
			//Debugger.log('_ddm.learningDesignID:'+_ddm.learningDesignID,Debugger.GEN,'onStoreDesignResponse','Canvas');		
			
			//var msg:String = "Congratulations! - Your design is valid has been saved with ID:"+r.learningDesignID;
		}
	}
	/*
	public function cut():Void{
		Debugger.log('Cut',Debugger.GEN,'cut','Canvas');
		
		
	}

	public function copy():Void{
		Debugger.log('Copy',Debugger.GEN,'copy','Canvas');
		
	}
	
	public function paste():Void{
		Debugger.log('Paste',Debugger.GEN,'paster','Canvas');
		
	}
	
	*/
	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number,height:Number):Void{
		canvasModel.setSize(width, height);
	}
	
	/**
	 * Initialises the property inspector
	 * @usage   
	 */
	public function initPropertyInspector():Void{
		//note the init obnject parameters are passed into the _container object in the embeded class (*in this case PropertyInspector)
		//we are setting up a vew so we need to pass the model and controller to it
		var cc:CanvasController = canvasView.getController();
		_pi = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:Dictionary.getValue('property_inspector_title'),closeButton:true,scrollContentPath:"PropertyInspector",_canvasModel:canvasModel,_canvasController:cc});
		//Assign dialog load handler
        _pi.addEventListener('contentLoaded',Delegate.create(this,piLoaded));
        //okClickedCallback = callBack;
    }
	
	/**
	 * Sts up the bin
	 * @usage   
	 * @return  
	 */
	public function initBin():Void{
		//Debugger.log('Running',Debugger.GEN,'initBin','Canvas');
		var cc:CanvasController = canvasView.getController();
		_bin = _canvasView_mc.attachMovie("Bin", "Bin", _canvasView_mc.getNextHighestDepth(),{_canvasController:cc,_canvasView:canvasView});
		//Debugger.log('_bin:'+_bin,Debugger.GEN,'initBin','Canvas');

	}
	
	/**
	 * Fired whern property inspector's contentLoaded is fired
	 * Positions the PI
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function piLoaded(evt:Object) {
        if(evt.type == 'contentLoaded'){
			//call a resize to line up the PI
			Application.getInstance().onResize();
			
           
        }else {
            //TODO raise wrong event type error 
        }
		
	}
	
	
	/**
	 * recieves event fired after update to the DDM
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function onDDMUpdated(evt:Object):Void{
		//_global.breakpoint();
		//var _ddm:DesignDataModel = evt.target;
		Debugger.log('DDM has been updated, _ddm.validDesign:'+_ddm.validDesign,Debugger.GEN,'onDDMUpdated','Canvas');
		//if its valid, its not anymore!
		if(_ddm.validDesign){
			_ddm.validDesign = false;
			checkValidDesign();
		}
	}
	
	
	/**
	 * recieves event fired before updating the DDM
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	public function onDDMBeforeUpdate(evt:Object):Void{
		//_global.breakpoint();
		//var _ddm:DesignDataModel = evt.target;
		Debugger.log('DDM about to be updated',Debugger.GEN,'onDDMBeforeUpdate','Canvas');
		//take a snapshot of the design and save it in the undoStack
		var snapshot:Object = _ddm.toData();
		_undoStack.push(snapshot);
		
		_redoStack = new Array();
		
		
	}
	
	
	/**
	 * Undo the last change to the DDM.
	 * TODO: Does not handle moving activities on the canvas, only when actual change to activities or transitions.  
	 * Need to generate update event when re-position activities
	 * @usage   
	 * @return  
	 */
	public function undo():Void{
		
		
		//_global.breakpoint();
		//Debugger.log('Before executing _undoStack.length:'+_undoStack.length,Debugger.GEN,'undo','Canvas');
		//Debugger.log('Before executing _redoStack.length:'+_redoStack.length,Debugger.GEN,'undo','Canvas');
		
		if(_undoStack.length>0){
			//get the last state off the stack
			var snapshot = _undoStack.pop();
			
			//get a copy of the current design and stick it in redo
			_redoStack.push(_ddm.toData());
			
			clearCanvas(true);
			//set the current design to the snapshot value
			_ddm.setDesign(snapshot,true);
			canvasModel.setDirty();
			
			//Debugger.log('After executing _undoStack.length:'+_undoStack.length,Debugger.GEN,'undo','Canvas');
			//Debugger.log('After executing _redoStack.length:'+_redoStack.length,Debugger.GEN,'undo','Canvas');
			
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
		
		//Debugger.log('Before executing _undoStack.length:'+_undoStack.length,Debugger.GEN,'redo','Canvas');
		//Debugger.log('Before executing _redoStack.length:'+_redoStack.length,Debugger.GEN,'redo','Canvas');
		//_global.breakpoint();
		
		if(_redoStack.length > 0){
			//get the last state off the stack
			var snapshot = _redoStack.pop();
			
			_undoStack.push(_ddm.toData());
			
			clearCanvas(true);
			
			_ddm.setDesign(snapshot,true);
			canvasModel.setDirty();
			
			//Debugger.log('After executing _undoStack.length:'+_undoStack.length,Debugger.GEN,'undo','Canvas');
			//Debugger.log('After executing _redoStack.length:'+_redoStack.length,Debugger.GEN,'undo','Canvas');
			
		}else{
			Debugger.log("Cannot Redo! no data on stack!",Debugger.GEN,'redo','Canvas');
		}
	
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
	
	public function get className():String{
		return 'Canvas';
	}
	
	public function get ddm():DesignDataModel{
		return _ddm;
	}
	
	public function getPropertyInspector():MovieClip{
		return _pi;
	}

	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get bin ():MovieClip {
		return _bin;
	}

	
	
}
