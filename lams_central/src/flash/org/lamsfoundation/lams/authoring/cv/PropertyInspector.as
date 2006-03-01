import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.*
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*
/*
*
* @author      DC
* @version     0.1
* @comments    Property Inspector for the canvas
* 
*/
class PropertyInspector extends MovieClip{
	
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	private var _dictionary:Dictionary;
	 //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog. Will contain any init params that were passed into createPopUp
   
    private var _tm:ThemeManager;
    private var toolDisplayName_lbl:Label;

   
	//Properties tab
    private var title_lbl:Label;
    private var title_txt:TextInput;
    private var desc_lbl:Label;
    private var desc_txt:TextInput;
    
	private var grouping_lbl:Label;
	private var currentGrouping_lbl:Label;
	private var appliedGroupingActivity_cmb:ComboBox;
	
	private var editGrouping_btn:Button;
    private var runOffline_chk:CheckBox;
    private var defineLater_chk:CheckBox;
	
	//gates
	private var gateType_lbl:Label;
	private var gateType_cmb:ComboBox;
	private var startOffset_lbl:Label;
	private var endOffset_lbl:Label;
	private var hours_lbl:Label;
	private var mins_lbl:Label;
	private var hours_stp:NumericStepper;
	private var mins_stp:NumericStepper;
	private var endHours_stp:NumericStepper;
	private var endMins_stp:NumericStepper;

	//grouping 
	private var groupType_lbl:Label;
	private var numGroups_lbl:Label;
	private var numLearners_lbl:Label;
	private var groupType_cmb:ComboBox;
	private var numGroups_rdo:RadioButton;
	private var numLearners_rdo:RadioButton;
	private var rndGroup_radio:RadioButtonGroup;
	private var numGroups_stp:NumericStepper;
	private var numLearners_stp:NumericStepper;
	
	//Complex Activity
	private var min_lbl:Label;
	private var max_lbl:Label;
	private var min_act:ComboBox;
	private var max_act:ComboBox;
	
	//screen assets:
	private var body_pnl:Panel;
	private var bar_pnl:Panel;
	
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	 * Constructor
	 */
	function PropertyInspector(){
		//register to recive updates form the model
		Debugger.log('Constructor',Debugger.GEN,'PropertyInspector','PropertyInspector');
		_tm = ThemeManager.getInstance();
		//Set up this class to use the Flash event delegation model
		EventDispatcher.initialize(this);
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		
		//_dictionary = Dictionary.getInstance();
		//_dictionary.addEventListener('init',Proxy.create(this,setupLabels));
	}
	
	public function init():Void{
		_canvasModel = _container._canvasModel;
		_canvasController = _container._canvasController;
		//_global.breakpoint();
		_canvasModel.addEventListener('viewUpdate',this);
		
		//Debugger.log('_canvasModel: ' + _canvasModel,Debugger.GEN,'init','PropertyInspector');
		setStyles();
		//set up handlers
		title_txt.addEventListener("focusOut",this);
		desc_txt.addEventListener("focusOut",this);
		runOffline_chk.addEventListener("click",this);
		defineLater_chk.addEventListener("click",this);
		
		rndGroup_radio.addEventListener("click",Delegate.create(this,onGroupingMethodChange));
		groupType_cmb.addEventListener("change",Delegate.create(this,onGroupTypeChange));
		gateType_cmb.addEventListener("change",Delegate.create(this,onGateTypeChange));
		appliedGroupingActivity_cmb.addEventListener("change",Delegate.create(this,onAppliedGroupingChange));
		hours_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		mins_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		hours_stp.addEventListener("focusOut",Delegate.create(this,onScheduleOffsetChange));
		mins_stp.addEventListener("focusOut",Delegate.create(this,onScheduleOffsetChange));
		endHours_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		endMins_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		endHours_stp.addEventListener("focusOut",Delegate.create(this,onScheduleOffsetChange));
		endMins_stp.addEventListener("focusOut",Delegate.create(this,onScheduleOffsetChange));
		numGroups_stp.addEventListener("change",Delegate.create(this,updateGroupingMethodData));
		numLearners_stp.addEventListener("change",Delegate.create(this,updateGroupingMethodData));
		numLearners_stp.addEventListener("focusOut",Delegate.create(this,updateGroupingMethodData));
		numGroups_stp.addEventListener("focusOut",Delegate.create(this,updateGroupingMethodData));
		
		
		gateType_lbl.text = Dictionary.getValue('trans_dlg_gatetypecmb');
		hours_lbl.text = Dictionary.getValue('pi_hours');
		mins_lbl.text = Dictionary.getValue('pi_mins');
		startOffset_lbl.text = Dictionary.getValue('pi_start_offset');
		endOffset_lbl.text = Dictionary.getValue('pi_end_offset');
		
		groupType_lbl.text = Dictionary.getValue('pi_group_type');
		numGroups_lbl.text = Dictionary.getValue('pi_num_groups');
		numLearners_lbl.text = Dictionary.getValue('pi_num_learners');
		
		
		
		//populate the synch type combo:
		gateType_cmb.dataProvider = Activity.getGateActivityTypes();
		groupType_cmb.dataProvider = Grouping.getGroupingTypesDataProvider();
		
		
		//hide all the controls at startup
		showGateControls(false);
		showToolActivityControls(false);
		showGroupingControls(false);
		showAppliedGroupingControls(false);
		
		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	
	
	/**
	 * Recieves update events from the model.
	 * @usage   
	 * @param   event 
	 * @return  
	 */
	public function viewUpdate(event:Object):Void{
		//Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','PropertyInspector');
		 //Update view from info object
       
       var cm:CanvasModel = event.target;
	   
	   switch (event.updateType){
            case 'SELECTED_ITEM' :
                updateItemProperties(cm);
                break;
                   
				
            default :
                //Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.CanvasView');
		}

	}
	
	/**
	 * Get called when something is selected in the cavas.
	 * Updates the details in the property inspector widgets
	 * Depending on the type of item selected, different controls will be shown
	 * @usage   
	 * @param   cm 
	 * @return  
	 */
	private function updateItemProperties(cm:CanvasModel):Void{
		//try to cast the selected item to see what we have (instance of des not seem to work)
		if(CanvasActivity(cm.selectedItem) != null){
			// its a Canvas activity then
			Debugger.log('Its a canvas activity',4,'updateItemProperties','PropertyInspector');
			var ca = CanvasActivity(cm.selectedItem);
			var a:Activity = ca.activity;			
			if(a.isGateActivity()){
				//its a gate
				showGroupingControls(false);
				showToolActivityControls(false);
				showGateControls(true);
				showAppliedGroupingControls(true);
				showGateActivityProperties(GateActivity(a));
				checkEnableGateControls();
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
			}else if(a.isGroupActivity()){
				//its a grouping activity
				showGroupingControls(true);
				showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(true);
				populateGroupingProperties(GroupingActivity(a));
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
			
			}else if(a.isOptionalActivity()){
				//its an optional activity
				showOptionalControls(true);
				showGroupingControls(false);
				//showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(true);
				populateGroupingProperties(GroupingActivity(a));
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
			
			}else{
				//its a tool activity
				showOptionalControls(false);
				showGroupingControls(false);
				showToolActivityControls(true);
				showGateControls(false);
				showAppliedGroupingControls(true);
				showToolActivityProperties(ToolActivity(a));
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
			}

			
		}else if(CanvasTransition(cm.selectedItem) != null){
			var ct = CanvasTransition(cm.selectedItem);
			var t:Transition = ct.transition;
			Debugger.log('Its a canvas transition',4,'updateItemProperties','PropertyInspector');
			
			showTransitionProperties(t);
			
			showGroupingControls(false);
			showToolActivityControls(false);
			showGateControls(false);
			
		}else{
			Debugger.log('Its a something we dont know',Debugger.CRITICAL,'updateItemProperties','PropertyInspector');
			showGroupingControls(false);
			showToolActivityControls(false);
			showGateControls(false);
			showOptionalControls(false);
		}
	}
	
	
	
	private function showToolActivityProperties(ta:ToolActivity){
		
		
		toolDisplayName_lbl.text = StringUtils.cleanNull(ta.toolDisplayName);
		//title_txt.text = StringUtils.cleanNull(ta.title);
		desc_txt.text = StringUtils.cleanNull(ta.description);
		runOffline_chk.selected = ta.runOffline;
		defineLater_chk.selected = ta.defineLater;
					
		currentGrouping_lbl.text = "GroupingUIID:"+StringUtils.cleanNull(ta.runOffline.groupingUIID);
			

	}
	
	private function showGateActivityProperties(ga:GateActivity){
		toolDisplayName_lbl.text = Dictionary.getValue('pi_activity_type_gate');
		//loop through combo to fins SI of our gate activity type
		for (var i=0; i<gateType_cmb.dataProvider.length;i++){
			if(ga.activityTypeID == gateType_cmb.dataProvider[i].data){
				gateType_cmb.selectedIndex=i;
			}
		}
		//TODO: set the stepper values too!
		
		
	
	}
	
	private function showTransitionProperties(t:Transition){
		//TODO: show from act and to act
		
	}
	
	private function  populateGroupingProperties(ga:GroupingActivity){
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		toolDisplayName_lbl.text = Dictionary.getValue('pi_activity_type_grouping');
		Debugger.log('This is the grouping object:',Debugger.GEN,'populateGroupingProperties','PropertyInspector');
		ObjectUtils.printObject(g);
		//loop through combo to fins SI of our gate activity type
		for (var i=0; i<groupType_cmb.dataProvider.length;i++){
			if(g.groupingTypeID == groupType_cmb.dataProvider[i].data){
				groupType_cmb.selectedIndex=i;
			}
		}
		
		if(g.groupingTypeID == Grouping.RANDOM_GROUPING){
			if(g.learnersPerGroup != null){
				numLearners_stp.value = g.learnersPerGroup;
			}else if(g.numberOfGroups != null){
				numGroups_stp.value = g.numberOfGroups;
			}
		}else{
			if(g.maxNumberOfGroups != null){
				numGroups_stp.value = g.maxNumberOfGroups;
			}
		}
		
		
	}
	
	/**
	 * Shows which grouping activity is applied to this activity, 
	 * if you click the edit button, the the activity is selected, 
	 * and therefore the Grouping obeject properties 
	 * are shown in the PropertyInspector.
	 * @usage   
	 * @param   a The selected Activity
	 * @return  
	 */
	private function showAppliedGroupingProperties(a:Activity){
		//update the grouping drop down values
		appliedGroupingActivity_cmb.dataProvider = getGroupingActivitiesDP();
		_global.breakpoint();

		var appliedGroupingAct:GroupingActivity = _canvasModel.getCanvas().ddm.getGroupingActivityByGroupingUIID(a.groupingUIID);
		Debugger.log('a.groupingUIID='+a.groupingUIID+', appliedGroupingAct.activityUIID :'+appliedGroupingAct.activityUIID ,Debugger.GEN,'showAppliedGroupingProperties','PropertyInspector');
		
		var dp = appliedGroupingActivity_cmb.dataProvider;
		Debugger.log('dp:'+dp.toString(),Debugger.GEN,'showAppliedGroupingProperties','PropertyInspector');
		appliedGroupingActivity_cmb.selectedIndex = 0;
		for(var i=0; i<dp.length;i++){
			
			Debugger.log('dp[i].data.createGroupingID:'+dp[i].data.createGroupingID,Debugger.GEN,'showAppliedGroupingProperties','PropertyInspector');
			if(appliedGroupingAct.activityUIID == dp[i].data.activityUIID){
				appliedGroupingActivity_cmb.selectedIndex = i;
			}
			
		}
		
		
		
	}

		
	private function showToolActivityControls(v:Boolean){

		desc_lbl.visible = v;
		desc_txt.visible = v;
		grouping_lbl.visible = v;
		currentGrouping_lbl.visible = v;
		runOffline_chk.visible = v;
		defineLater_chk.visible = v;
		editGrouping_btn.visible = v;
	}
	
	private function showOptionalControls(v:Boolean){

		min_lbl.visible = v		
		max_lbl.visible = v;
		min_act.visible = v;
		max_act.visible = v;
		
	}
	
	private function showGateControls(v:Boolean){
		trace('showGateControls....'+v);
		hours_lbl.visible = v;
		mins_lbl.visible = v;
		hours_stp.visible = v;
		mins_stp.visible = v;
		endHours_stp.visible = v;
		endMins_stp.visible = v;
		gateType_lbl.visible = v;
		gateType_cmb.visible = v;
		startOffset_lbl.visible = v;
		endOffset_lbl.visible = v;
	}
	
	/**
	 * Shows or hides the app.lied grouping
	 * AND title fields
	 * @usage   
	 * @param   v 
	 * @return  
	 */
	private function showAppliedGroupingControls(v:Boolean){
		trace('show grp controls.....'+v);
		grouping_lbl.visible = v;
		appliedGroupingActivity_cmb.visible = v;
		
		title_lbl.visible = v;
		title_txt.visible = v;
		
	}
	
	private function showGroupingControls(v:Boolean){
		//grouping 
		groupType_lbl.visible = v;
		groupType_cmb.visible = v;
		if(v){
			showRelevantGroupOptions();
		}else{
			numGroups_lbl.visible = v;
			numLearners_lbl.visible = v;
			numGroups_rdo.visible = v;
			numLearners_rdo.visible = v;
			numGroups_stp.visible = v;
			numLearners_stp.visible = v;
		}
		
	}
	
	private function showRelevantGroupOptions(){
		
		var ga = _canvasModel.selectedItem.activity;
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		Debugger.log('g.groupingTypeID:'+g.groupingTypeID,Debugger.GEN,'showRelevantGroupOptions','org.lamsfoundation.lams.common.cv.PropertyInspector');
		Debugger.log('Grouping.CHOSEN_GROUPING:'+Grouping.CHOSEN_GROUPING,Debugger.GEN,'showRelevantGroupOptions','org.lamsfoundation.lams.common.cv.PropertyInspector');
		Debugger.log('Grouping.RANDOM_GROUPING:'+Grouping.RANDOM_GROUPING,Debugger.GEN,'showRelevantGroupOptions','org.lamsfoundation.lams.common.cv.PropertyInspector');
		
		if(g.groupingTypeID == Grouping.CHOSEN_GROUPING){
			trace('chosen');
			numGroups_lbl.visible = true;
			numGroups_stp.visible = true;
			
			numLearners_rdo.visible = false;
			numGroups_rdo.visible = false;
			numLearners_stp.visible = false;
			numLearners_lbl.visible = false;
			
		}else if(g.groupingTypeID == Grouping.RANDOM_GROUPING){
			trace('random');
			numGroups_lbl.visible = true;
			numGroups_stp.visible = true;
			
			numLearners_rdo.visible = true;
			numGroups_rdo.visible = true;
			numLearners_stp.visible = true;
			numLearners_lbl.visible = true;
			checkEnableGroupsOptions();
		}else{
			//error dont understand the grouping type
		}
		trace('numLearners_stp.visible:'+numLearners_stp.visible);
		
	}
	
	private function reDrawTroublesomeSteppers(){
		numLearners_stp.visible = true;
		numGroups_stp.visible = true;
	}
	
	
	private function checkEnableGateControls(){
		//Debugger.log('Activity.SCHEDULE_GATE_ACTIVITY_TYPE:'+Activity.SCHEDULE_GATE_ACTIVITY_TYPE,Debugger.GEN,'checkEnableGateControls','PropertyInspector');
		//Debugger.log('_canvasModel.selectedItem.activity.activityTypeID:'+_canvasModel.selectedItem.activity.activityTypeID,Debugger.GEN,'checkEnableGateControls','PropertyInspector');
		if(_canvasModel.selectedItem.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE){
			trace('enabling....');
			hours_stp.enabled = true;
			mins_stp.enabled = true;
			endHours_stp.enabled = true;
			endMins_stp.enabled = true;
			
		}
		/**/
		else{
			trace('disabling....');
			hours_stp.enabled = false;
			mins_stp.enabled = false;
			endHours_stp.enabled = false;
			endMins_stp.enabled = false;
		}
		
		//this is a crazy hack to stop the steppter dissapearing after its .enabled property is set.
		//waits 2 frames to re-display the control ( 1 doensnt work!)
		MovieClipUtils.doLater(Proxy.create(this,showGateControlsLater,true));
	}
	
	public function showGateControlsLater(bool:Boolean){
		MovieClipUtils.doLater(Proxy.create(this,showGateControls,bool));
	}
	
	/**
	 * Enables or disables the grouping method steppers
	 * @usage   
	 * @return  
	 */
	private function checkEnableGroupsOptions(){
		var groupingBy = rndGroup_radio.selection.data;
		Debugger.log('groupingBy:'+groupingBy,Debugger.GEN,'checkEnableGroupsOptions','PropertyInspector');
		if(groupingBy == 'num_learners'){
			numGroups_stp.value = 0;
			numGroups_stp.enabled = false;
			numLearners_stp.enabled = true;
		}else{
			numGroups_stp.enabled = true;
			numLearners_stp.value = 0;
			numLearners_stp.enabled = false;
		}
		//this is a crazy hack to stop the steppter dissapearing after its .enabled property is set.
		//waits 2 frames to re-display the control ( 1 doensnt work!)
		MovieClipUtils.doLater(Proxy.create(this,reDrawTroublesomeSteppersLater));
		
	}
	
	public function reDrawTroublesomeSteppersLater(){
		MovieClipUtils.doLater(Proxy.create(this,reDrawTroublesomeSteppers));
	}
	
	/**
	 * Called when there is a change in the values of the group method steppers
	 * Butdates the value inthe grouping class being edited.
	 * @usage   
	 * @return  
	 */
	public function updateGroupingMethodData(){
		var ga = _canvasModel.selectedItem.activity;
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		if(g.groupingTypeID == Grouping.RANDOM_GROUPING){
			//not only one of these should actually have a non 0 value
			g.learnersPerGroup = numLearners_stp.value;
			g.numberOfGroups = numGroups_stp.value;
		}else{
			g.maxNumberOfGroups = numGroups_stp.value;
		}
	}
	
	private function getGroupingActivitiesDP(){
		var gActs = _canvasModel.getCanvas().ddm.getGroupingActivities();
		var gActsDP = new Array();
		gActsDP.push({label:Dictionary.getValue('pi_no_grouping'),data:null});
		for(var i=0; i<gActs.length;i++){
			gActsDP.push({label:gActs[i].title,data:gActs[i]});
		}
		return gActsDP;
	}
	
	
	

	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
      
        //Size the bkg_pnl
		
        body_pnl.setSize(w,h-bar_pnl.height);
        bar_pnl.setSize(w);

        
    }
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
        Debugger.log('Button stykle obejct',Debugger.GEN,'setStyles','PropertyInspector');
		var styleObj = _tm.getStyleObject('button');
		ObjectUtils.printObject(styleObj);
		editGrouping_btn.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('label');
		title_lbl.setStyle('styleName',styleObj);
		desc_lbl.setStyle('styleName',styleObj);
		grouping_lbl.setStyle('styleName',styleObj);
		currentGrouping_lbl.setStyle('styleName',styleObj);
		gateType_lbl.setStyle('styleName',styleObj);
		gateType_lbl.setStyle('styleName',styleObj);
		startOffset_lbl.setStyle('styleName',styleObj);
		endOffset_lbl.setStyle('styleName',styleObj);
		hours_lbl.setStyle('styleName',styleObj);
		mins_lbl.setStyle('styleName',styleObj);
		toolDisplayName_lbl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('combo');
		gateType_cmb.setStyle('styleName',styleObj);
		groupType_cmb.setStyle('styleName',styleObj);
		appliedGroupingActivity_cmb.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('textinput');
		title_txt.setStyle('styleName',styleObj);
		desc_txt.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('checkbox');
		runOffline_chk.setStyle('styleName',styleObj);
		defineLater_chk.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('numericstepper');
		hours_stp.setStyle('styleName',styleObj);
		mins_stp.setStyle('styleName',styleObj);
		endHours_stp.setStyle('styleName',styleObj);
		endMins_stp.setStyle('styleName',styleObj);
		
		
		
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    public function set container(value:MovieClip){
        _container = value;
    }
	
	
	/////////////////////////////////////////////////
	//------------ controller section -------------//
	/////////////////////////////////////////////////
	
	/**
	 *Handles change event fired from the gateType_cmb
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onGateTypeChange(evt:Object){
		_canvasModel.selectedItem.activity.activityTypeID = evt.target.value;
		Debugger.log('Set gate type to: _canvasModel.selectedItem.activity.activityTypeID:'+_canvasModel.selectedItem.activity.activityTypeID,Debugger.GEN,'onGateTypeChange','PropertyInspector');
		checkEnableGateControls();
	}
	
	/**
	 * Handles change event fired from the groupType_cmb
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onGroupTypeChange(evt:Object){
		var ga = _canvasModel.selectedItem.activity;
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		g.groupingTypeID = evt.target.value;
		Debugger.log('Set group type to: '+g.groupingTypeID,Debugger.GEN,'onGroupTypeChange','PropertyInspector');
		showRelevantGroupOptions();
	}
	
	private function onAppliedGroupingChange(evt:Object){
		var a = _canvasModel.selectedItem.activity;
		//get the groupingUIID of the grouping Actviity we have selected.
		var newGroupingUIID = evt.target.value.createGroupingUIID;
		a.groupingUIID = newGroupingUIID;
		
		Debugger.log('Set grouping UIID to: '+a.groupingUIID ,Debugger.GEN,'onAppliedGroupingChange','PropertyInspector');
		_canvasModel.selectedItem.refresh();
	}
	
	/**
	 * Handles click event from the grouping method radio buttons
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onGroupingMethodChange(evt:Object){
		//
		//checkEnableGroupsOptions();
		
		 
		checkEnableGroupsOptions();
		
	}
	
	private function onScheduleOffsetChange(evt:Object){
		
		var startOffsetMins:Number = (hours_stp.value * 60) + mins_stp.value;
		_canvasModel.selectedItem.activity.gateStartTimeOffset = startOffsetMins;
		var endOffsetMins:Number = (endHours_stp.value * 60) + endMins_stp.value;
		_canvasModel.selectedItem.activity.gateEndTimeOffset = endOffsetMins;
		Debugger.log('activity.gateStartTimeOffset :'+_canvasModel.selectedItem.activity.gateStartTimeOffset ,Debugger.GEN,'onScheduleOffsetChange','PropertyInspector');
		Debugger.log('activity.gateEndTimeOffset :'+_canvasModel.selectedItem.activity.gateEndTimeOffset,Debugger.GEN,'onScheduleOffsetChange','PropertyInspector');
		
		
		
		
	}
	
	
   /**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the target
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	 /**/
	public function click(e):Void{
		var tgt:String = new String(e.target);
		//Debugger.log('click tgt:'+tgt,Debugger.GEN,'click','PropertyInspector');
		//Debugger.log('indexOf("defineLater_chk"):'+tgt.indexOf("defineLater_chk"),Debugger.GEN,'click','PropertyInspector');
		//Debugger.log('indexOf("runOffline_chk"):'+tgt.indexOf("runOffline_chk"),Debugger.GEN,'click','PropertyInspector');
		if(tgt.indexOf("defineLater_chk") != -1){
			
			_canvasModel.selectedItem.activity.defineLater = defineLater_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.defineLater:'+_canvasModel.selectedItem.activity.defineLater,Debugger.GEN,'click','PropertyInspector');
		
		}else if(tgt.indexOf("runOffline_chk") != -1){
			
			_canvasModel.selectedItem.activity.runOffline = runOffline_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.runOffline:'+_canvasModel.selectedItem.activity.runOffline,Debugger.GEN,'click','PropertyInspector');
		}
	}
	
	
	/**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function focusOut(e):Void{
		var tgt:String = new String(e.target);
		Debugger.log('focusOut tgt:'+tgt,Debugger.GEN,'focusOut','PropertyInspector');
		
		if(tgt.indexOf("title_txt") != -1){
			//todo check if this is the right place to set edited content, should it be ddm?
			_canvasModel.selectedItem.activity.title = title_txt.text;
			
		}else if(tgt.indexOf("desc_txt") != -1){
			_canvasModel.selectedItem.activity.description= desc_txt.text;
		}
		_canvasModel.selectedItem.refresh();
		
		
	}
	
}

