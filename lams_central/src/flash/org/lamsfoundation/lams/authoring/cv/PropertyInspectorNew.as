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
class PropertyInspectorNew extends MovieClip{
	
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	private var _dictionary:Dictionary;
	 //References to components + clips 
   	private var _piIsExpended:Boolean;
	//private var PI_mc:MovieClip;
    private var _tm:ThemeManager;
    private var toolDisplayName_lbl:Label;
	private var clickTarget_mc:MovieClip;
	private var _depth:Number;
	private var delimitLine:MovieClip;
	private var expand_mc:MovieClip;
	private var collapse:MovieClip;
	
	//Properties tab
    private var title_lbl:Label;
    private var title_txt:TextInput;
    private var desc_lbl:Label;
    private var desc_txt:TextInput;
    private var piHeightHide:Number = 23;
	private var piHeightFull:Number = 106;
	private var grouping_lbl:Label;
	private var applied_grouping_lbl:Label;
	//private var grouping_opt_lbl:Label;
	private var currentGrouping_lbl:Label;
	private var appliedGroupingActivity_cmb:ComboBox;
	//private var appliedGroupingActivity_opt_cmb:ComboBox;
	
	private var editGrouping_btn:Button;
    private var runOffline_chk:CheckBox;
    private var defineLater_chk:CheckBox;
	
	//genral info
	private var total_num_activities_lbl:Label;
	
	//gates
	private var gateType_lbl:Label;
	private var gateType_cmb:ComboBox;
	private var startOffset_lbl:Label;
	private var endOffset_lbl:Label;
	private var days_lbl:Label;
	private var hours_lbl:Label;
	private var mins_lbl:Label;
	private var hoursEnd_lbl:Label;
	private var minsEnd_lbl:Label;
	private var days_stp:NumericStepper;
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
	private var numRandomGroups_stp:NumericStepper;
	private var numLearners_stp:NumericStepper;
	
	//Complex Activity
	private var min_lbl:Label;
	private var max_lbl:Label;
	private var minAct_stp:NumericStepper ;
	private var maxAct_stp:NumericStepper;
	
	//screen assets:
	private var body_pnl:Panel;
	private var bar_pnl:Panel;
	private var cover_pnl:Panel;
	//Defined so compiler can 'see' events added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
	
	
	/**
	 * Constructor
	 */
 
	public function PropertyInspectorNew(){
		//register to recive updates form the model
		Debugger.log('Constructor',Debugger.GEN,'PropertyInspectorNew','PropertyInspectorNew');
		_tm = ThemeManager.getInstance();
		//Set up this class to use the Flash event delegation model
		EventDispatcher.initialize(this);
		_dictionary = Dictionary.getInstance();
		
		//let it wait one frame to set up the components.
		MovieClipUtils.doLater(Proxy.create(this,init));
		//_dictionary.addEventListener('init',Proxy.create(this,setupLabels));
	}
	
	public function init():Void{
		
		//var yPos:Number = 0;
		//_depth = this.getNextHighestDepth();
		//set SP the content path:
		//PI_sp.contentPath = "empty_mc";
		//PI_mc = PI_sp.content.attachMovie("PI_items","PI_items",_depth++);
		
		_canvasModel = _canvasModel;
		_canvasController = _canvasController;
		_piIsExpended = false;
		_canvasModel.selectedItem = null;
		_canvasModel.setPIHeight(piHeightHide);
		//_global.breakpoint();
		_canvasModel.addEventListener('viewUpdate',this);
		clickTarget_mc.onRelease = Proxy.create (this, localOnRelease);
		clickTarget_mc.onReleaseOutside = Proxy.create (this, localOnReleaseOutside);
		//Debugger.log('_canvasModel: ' + _canvasModel,Debugger.GEN,'init','PropertyInspector');
		
		//set up handlers
		title_txt.addEventListener("focusOut",this);
		desc_txt.addEventListener("focusOut",this);
		runOffline_chk.addEventListener("click",this);
		defineLater_chk.addEventListener("click",this);
		
		rndGroup_radio.addEventListener("click",Delegate.create(this,onGroupingMethodChange));
		groupType_cmb.addEventListener("change",Delegate.create(this,onGroupTypeChange));
		gateType_cmb.addEventListener("change",Delegate.create(this,onGateTypeChange));
		appliedGroupingActivity_cmb.addEventListener("change",Delegate.create(this,onAppliedGroupingChange));
		minAct_stp.addEventListener("change",Delegate.create(this,updateOptionalData));
		minAct_stp.addEventListener("focusOut",Delegate.create(this,updateOptionalData));
		maxAct_stp.addEventListener("change",Delegate.create(this,updateOptionalData));
		maxAct_stp.addEventListener("focusOut",Delegate.create(this,updateOptionalData));
		days_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		hours_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		mins_stp.addEventListener("change",Delegate.create(this,onScheduleOffsetChange));
		days_stp.addEventListener("focusOut",Delegate.create(this,onScheduleOffsetChange));
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
		numRandomGroups_stp.addEventListener("change",Delegate.create(this,updateGroupingMethodData));
		numRandomGroups_stp.addEventListener("focusOut",Delegate.create(this,updateGroupingMethodData));
		
		
		this.onEnterFrame = setupLabels;
		
		this.tabChildren = true;
		setTabIndex();
		
	}
	
	public function setupLabels(){
		
		//trace("I am in PI setupLabels")
		//trace("PI_mc "+PI_mc)
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b>"
		gateType_lbl.text = Dictionary.getValue('trans_dlg_gatetypecmb');
		days_lbl.text = Dictionary.getValue('pi_days');
		hours_lbl.text = Dictionary.getValue('pi_hours');
		mins_lbl.text = Dictionary.getValue('pi_mins');
		hoursEnd_lbl.text = Dictionary.getValue('pi_hours');
		minsEnd_lbl.text = Dictionary.getValue('pi_mins');
		startOffset_lbl.text = Dictionary.getValue('pi_start_offset');
		endOffset_lbl.text = Dictionary.getValue('pi_end_offset');
		
		groupType_lbl.text = Dictionary.getValue('pi_group_type');
		numGroups_lbl.text = Dictionary.getValue('pi_num_groups');
		numLearners_lbl.text = Dictionary.getValue('pi_num_learners');
		
		//Properties tab
		title_lbl.text = Dictionary.getValue('pi_lbl_title');
		desc_lbl.text = Dictionary.getValue('pi_lbl_desc');
		grouping_lbl.text = Dictionary.getValue('pi_lbl_group');
		//grouping_opt_lbl.text = Dictionary.getValue('pi_lbl_group');
		currentGrouping_lbl.text = Dictionary.getValue('pi_lbl_currentgroup');
		defineLater_chk.label = Dictionary.getValue('pi_definelater');
		runOffline_chk.label = Dictionary.getValue('pi_runoffline');
				
		//Complex Activity
		min_lbl.text = Dictionary.getValue('pi_min_act');
		max_lbl.text = Dictionary.getValue('pi_max_act');
		
			//populate the synch type combo:
		gateType_cmb.dataProvider = Activity.getGateActivityTypes();
		groupType_cmb.dataProvider = Grouping.getGroupingTypesDataProvider();
		
		//Call to apply style to all the labels and input fields
		setStyles();
		
		//fire event to say we have loaded
		
		delete this.onEnterFrame; 
		//hide all the controls at startup
		
		delimitLine._visible = false;
		hideAllSteppers(false);
		showGroupingControls(false);
		showGeneralControls(false);
		showOptionalControls(false);
		showToolActivityControls(false);
		showGateControls(false);
		showAppliedGroupingControls(false);
		showGeneralInfo(true);
		
		dispatchEvent({type:'load',target:this});
	}
	private function hideAllSteppers(v):Void{ 
		hours_stp.visible = v
		mins_stp.visible = v
		endHours_stp.visible = v
		endMins_stp.visible = v
		numGroups_stp.visible = v
		numRandomGroups_stp.visible = v
		numLearners_stp.visible = v
		minAct_stp.visible = v
		maxAct_stp.visible = v
	}
	
	private function setTabIndex(selectedTab:String){
		
		//Tool Activities
		title_txt.tabIndex = 401
		applied_grouping_lbl.tabIndex = 402
		appliedGroupingActivity_cmb.tabIndex = 402
		runOffline_chk.tabIndex = 403
		defineLater_chk.tabIndex = 404
		
		//Optional Activities
		desc_txt.tabIndex = 402
		minAct_stp.tabIndex = 403
		maxAct_stp.tabIndex = 404
		
		//Gate Activities
		//gateType_cmb.enabled = true
		gateType_cmb.tabIndex = 402
		days_stp.tabIndex = 403
		hours_stp.tabIndex = 404
		mins_stp.tabIndex = 405
		endHours_stp.tabIndex = 406
		endMins_stp.tabIndex = 407
		
		//Grouping Activities
		//groupType_cmb.enabled = true
		groupType_cmb.tabIndex = 402
		numGroups_stp.tabIndex = 403
		numGroups_rdo.tabIndex = 404
		numRandomGroups_stp.tabIndex = 405
		numLearners_rdo.tabIndex = 406
		numLearners_stp.tabIndex = 407
		
	}
	
	public function localOnRelease():Void{
		
		if (_piIsExpended){
			trace("P Pressed in 'localOnRelease' and _piIsExpended is: "+_piIsExpended)
			_piIsExpended = false
			_canvasModel.setPIHeight(piHeightHide);
			showExpand(true);
			
		}else {
			trace("P Pressed in 'localOnRelease' and _piIsExpended is: "+_piIsExpended)
			_piIsExpended = true
			_canvasModel.setPIHeight(piHeightFull);
			showExpand(false);
			//Application.getInstance().onResize();
		}
	}
	
	public function isPIExpanded():Boolean{
		return _piIsExpended;
	}
	
	public function piFullHeight():Number{
		return piHeightFull;
	}
	
	
	public function showExpand(e:Boolean){
      
        //show hide the icons
		expand_mc._visible = e;
                
    }
	
	public function localOnReleaseOutside():Void{
		Debugger.log('Release outside so no event has been fired, current state is: ' + _piIsExpended,Debugger.GEN,'localOnReleaseOutside','PropertyInspectorNew');
		
	}
	/**
	 * Recieves update events from the model.
	 * @usage   
	 * @param   event 
	 * @return  
	 */
	public function viewUpdate(event:Object):Void{
		Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','PropertyInspectorNew');
		 //Update view from info object
      
       var cm:CanvasModel = event.target;
	   
	   switch (event.updateType){
            case 'SELECTED_ITEM' :
				//title_txt.setFocus();
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
			cover_pnl.visible = false;
			// its a Canvas activity then
			Debugger.log('Its a canvas activity',4,'updateItemProperties','PropertyInspector');
			var ca = CanvasActivity(cm.selectedItem);
			var a:Activity = ca.activity;	
			var cao = CanvasOptionalActivity(cm.selectedItem);
			var cap = CanvasParallelActivity(cm.selectedItem);
			var caco:ComplexActivity = ComplexActivity(cao.activity);
			var cacp:ComplexActivity = ComplexActivity(cap.activity);
			if(a.isGateActivity()){
				//its a gate
				delimitLine._visible = true;
				showGroupingControls(false);
				showToolActivityControls(false);
				showGeneralInfo(false);
				showOptionalControls(false);
				showGateControls(true);
				showAppliedGroupingControls(false);
				//showGeneralProperties(a)
				checkEnableGateControls();
				showGateActivityProperties(GateActivity(a));
				
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
				desc_txt.text = StringUtils.cleanNull(a.description);
				showGeneralControls(true);
				//PI_sp.refreshPane();
			}else if(a.isGroupActivity()){
				//its a grouping activity
				delimitLine._visible = true;
				showGroupingControls(true);
				showGeneralControls(true);
				showOptionalControls(false);
				showGeneralInfo(false);
				showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(false);
				//showGeneralProperties(a)
				populateGroupingProperties(GroupingActivity(a));
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
				desc_txt.text = StringUtils.cleanNull(a.description);
				//PI_sp.refreshPane();
			
			}else if(a.isOptionalActivity()){
				//its a grouping activity
				delimitLine._visible = true;
				
				showGeneralControls(true);
				showGroupingControls(false);
				//showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showGeneralInfo(false);
				showAppliedGroupingControls(false);
				showOptionalControls(true);
				//showGeneralProperties(cca)
				populateGroupingProperties(GroupingActivity(caco));
				showAppliedGroupingProperties(caco);
				showOptionalActivityProperties(caco);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
				desc_txt.text = StringUtils.cleanNull(a.description);
				//PI_sp.refreshPane();
			}else if(a.isParallelActivity()){
				//its a grouping activity
				delimitLine._visible = true;
				//its an parallel activity
				showOptionalControls(false);
				showGeneralControls(true);
				showGeneralInfo(false);
				showGroupingControls(false);
				//showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(true);
				//showGeneralProperties(cca)
				populateGroupingProperties(GroupingActivity(cacp));
				showAppliedGroupingProperties(cacp);
				showParallelActivityProperties(cacp);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
				desc_txt.text = StringUtils.cleanNull(a.description);
			}else{
				//its a tool activity
				delimitLine._visible = true;	
				showOptionalControls(false);
				showGeneralControls(true);
				showGroupingControls(false);
				showGeneralInfo(false);
				showAppliedGroupingControls(true);
				showToolActivityControls(true);
				showGateControls(false);
				//showAppliedGroupingControls(true);
				showToolActivityProperties(ToolActivity(a));
				//showGeneralProperties(a)
				showAppliedGroupingProperties(a);
				//show the title
				title_txt.text = StringUtils.cleanNull(a.title);
				desc_txt.text = StringUtils.cleanNull(a.description);
				//PI_sp.refreshPane();
			}

			
		}else if(CanvasOptionalActivity(cm.selectedItem) != null){
			cover_pnl.visible = false;
			var co = CanvasOptionalActivity(cm.selectedItem);
			var cca:ComplexActivity = ComplexActivity(co.activity);
				//its an optional activity
				delimitLine._visible = true;
				
				showGeneralControls(true);
				showGroupingControls(false);
				//showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showGeneralInfo(false);
				showAppliedGroupingControls(false);
				showOptionalControls(true);
				//showGeneralProperties(cca)
				populateGroupingProperties(GroupingActivity(cca));
				showAppliedGroupingProperties(cca);
				showOptionalActivityProperties(cca);
				//show the title
				title_txt.text = StringUtils.cleanNull(cca.title);
				desc_txt.text = StringUtils.cleanNull(cca.description);
				//PI_sp.refreshPane();
		}else if(CanvasParallelActivity(cm.selectedItem) != null){
			cover_pnl.visible = false;
			var co = CanvasParallelActivity(cm.selectedItem);
			var cca:ComplexActivity = ComplexActivity(co.activity);
				delimitLine._visible = true;
				//its an parallel activity
				showOptionalControls(false);
				showGeneralControls(true);
				showGeneralInfo(false);
				showGroupingControls(false);
				//showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(true);
				//showGeneralProperties(cca)
				populateGroupingProperties(GroupingActivity(cca));
				showAppliedGroupingProperties(cca);
				showParallelActivityProperties(cca);
				//show the title
				title_txt.text = StringUtils.cleanNull(cca.title);
				desc_txt.text = StringUtils.cleanNull(cca.description);
		}else if(CanvasTransition(cm.selectedItem) != null){
			cover_pnl.visible = false;
			var ct = CanvasTransition(cm.selectedItem);
			var t:Transition = ct.transition;
				Debugger.log('Its a canvas transition',4,'updateItemProperties','PropertyInspector');
				delimitLine._visible = false;
				showTransitionProperties(t);
				//showGeneralProperties(t)
				showGeneralInfo(false);
				showGeneralControls(false);
				showOptionalControls(false);
				showGroupingControls(false);
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(false);
				//PI_sp.complete;
			
		}else{
			cover_pnl.visible = false;
				Debugger.log('Its a something we dont know',Debugger.CRITICAL,'updateItemProperties','PropertyInspector');
				showGeneralInfo(true);
				delimitLine._visible = false;
				toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> "//Dictionary.getValue('pi_title_generalinfo');
				showGroupingControls(false);
				showGeneralControls(false);
				showOptionalControls(false);
				showRelevantGroupOptions();
				showToolActivityControls(false);
				showGateControls(false);
				showAppliedGroupingControls(false);
		
				//PI_sp.complete
		}
	}
	
	private function showToolActivityControls(v:Boolean){
		
			
		//desc_lbl.visible = v;
		//desc_txt.visible = v;
		var a = _canvasModel.selectedItem.activity;
		var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(a.parentUIID)
		if (a.parentUIID != null && parentAct.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE) {
			applied_grouping_lbl.visible = v
			appliedGroupingActivity_cmb.visible = false;
			
		}else {
			applied_grouping_lbl.visible = false
			appliedGroupingActivity_cmb.visible = v;
		}
		
		
		grouping_lbl.visible = v;
		currentGrouping_lbl.visible = v;
		runOffline_chk.visible = v;
		defineLater_chk.visible = v;
		editGrouping_btn.visible = v;
	}
	
	
	private function showGeneralInfo(v:Boolean){

		total_num_activities_lbl.visible = v;
	} 
	
	private function showGeneralControls(v:Boolean){

		title_lbl.visible = v;
		title_txt.visible = v;
	} 
	
	private function showOptionalControls(v:Boolean){
		
		min_lbl.visible = v;	
		max_lbl.visible = v;
		minAct_stp.visible = v;
		maxAct_stp.visible = v;
		desc_lbl.visible = v;
		desc_txt.visible = v;
		//grouping_opt_lbl.visible = v; 
		//appliedGroupingActivity_opt_cmb.visible = v;
		grouping_lbl.visible = false;
		
		
	}
	
	private function showGateControls(v:Boolean){
		trace('showGateControls....'+v);
		days_lbl.visible = v;
		hours_lbl.visible = v;
		mins_lbl.visible = v;
		hoursEnd_lbl.visible = v;
		minsEnd_lbl.visible = v;
		days_stp.visible = v;
		hours_stp.visible = v;
		mins_stp.visible = v;
		//endHours_stp.visible = v;
		//endMins_stp.visible = v;
		gateType_lbl.visible = v;
		gateType_cmb.visible = v;
		startOffset_lbl.visible = v;
		//endOffset_lbl.visible = v;
		
	}
	
	/**
	 * Shows or hides the app.lied grouping
	 * AND title fields
	 * @usage   
	 * @param   v 
	 * @return  
	 */
	private function showAppliedGroupingControls(v:Boolean){
		//trace('show grp controls.....'+v);
		grouping_lbl.visible = v;
		appliedGroupingActivity_cmb.visible = v;
		//checkEnabledGroupControl()
	
	}
	
	private function checkEnabledGroupControl(){
		var ca = _canvasModel.selectedItem;
		var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(ca.activity.parentUIID)
		if (ca.activity.parentUIID != null && parentAct.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE) {
			appliedGroupingActivity_cmb.enabled = false;
		}else {
			appliedGroupingActivity_cmb.enabled = false;
		}
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
			numRandomGroups_stp.visible = v;
			numLearners_stp.visible = v;
			
		}
		
	}
	
	private function showToolActivityProperties(ta:ToolActivity){
		
		
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> - "+StringUtils.cleanNull(ta.toolDisplayName);
		//title_txt.text = StringUtils.cleanNull(ta.title);
		//desc_txt.text = StringUtils.cleanNull(ta.description);
		runOffline_chk.selected = ta.runOffline;
		defineLater_chk.selected = ta.defineLater;
					
		currentGrouping_lbl.text = "GroupingUIID:"+StringUtils.cleanNull(ta.runOffline.groupingUIID);
			

	}
	
	private function showGeneralProperties(ta:ToolActivity){
		
		//desc_txt.text = StringUtils.cleanNull(ta.description);
	}
	
	private function showOptionalActivityProperties(ca:ComplexActivity){
		
	
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> - "+Dictionary.getValue('pi_optional_title');
		//title_txt.text = StringUtils.cleanNull(ta.title);
		//desc_txt.text = StringUtils.cleanNull(ta.description);
		runOffline_chk.selected = ca.runOffline;
		defineLater_chk.selected = ca.defineLater;
		trace("min options when starting: "+ca.minOptions)
		if (ca.minOptions == undefined){
			minAct_stp.value = 0
		}else{
			minAct_stp.value = ca.minOptions
		}
		
		if (ca.maxOptions == undefined){
			maxAct_stp.value = 0
		}else{
			maxAct_stp.value = ca.maxOptions
		}
		currentGrouping_lbl.text = "GroupingUIID:"+StringUtils.cleanNull(ca.runOffline.groupingUIID);
			

	}
	
	private function updateOptionalData(){
		var oa = _canvasModel.selectedItem.activity;
		var	o = ComplexActivity(oa);
		o.minOptions = minAct_stp.value;
		o.maxOptions = maxAct_stp.value;
		oa.init();
	}
	
	private function showParallelActivityProperties(ca:ComplexActivity){
		
		
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> - "+Dictionary.getValue('pi_parallel_title');
		//title_txt.text = StringUtils.cleanNull(ta.title);
		//desc_txt.text = StringUtils.cleanNull(ta.description);
		runOffline_chk.selected = ca.runOffline;
		defineLater_chk.selected = ca.defineLater;
					
		currentGrouping_lbl.text = "GroupingUIID:"+StringUtils.cleanNull(ca.runOffline.groupingUIID);
			

	}
	
	private function showGateActivityProperties(ga:GateActivity){
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> - "+Dictionary.getValue('pi_activity_type_gate');
		//loop through combo to find SI of our gate activity type
		trace("gate Type is: "+ga.activityTypeID)
		for (var i=0; i<gateType_cmb.dataProvider.length;i++){
			//if(ga.activityTypeID == gateType_cmb.dataProvider[i].data){
			if(_canvasModel.selectedItem.activity.activityTypeID == gateType_cmb.dataProvider[i].data){
				
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
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> - "+Dictionary.getValue('pi_activity_type_grouping');
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
				numRandomGroups_stp.value = g.numberOfGroups;
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
				applied_grouping_lbl.text = dp[i].label
			}
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
			numLearners_lbl.visible = false;
			numGroups_stp.visible = true;
			numRandomGroups_stp.visible = false;
			numLearners_stp.visible = false;
			numLearners_rdo.visible = false;
			numGroups_rdo.visible = false;
			
			
			
			
		}else if(g.groupingTypeID == Grouping.RANDOM_GROUPING){
			trace('random');
			numGroups_lbl.visible = true;
			numLearners_lbl.visible = true;
			numGroups_stp.visible = false;
			numRandomGroups_stp.visible = true;
			numLearners_stp.visible = true;
			numLearners_rdo.visible = true;
			numGroups_rdo.visible = true;
			
			
			
			checkEnableGroupsOptions();
		}else{
			//error dont understand the grouping type
		}
		trace('numLearners_stp.visible:'+numLearners_stp.visible);
		
	}
	
	private function reDrawTroublesomeSteppers(){
		numLearners_stp.visible = true;
		numRandomGroups_stp.visible = true;
		
	}
	
	private function checkEnableGateControls(){
		//Debugger.log('Activity.SCHEDULE_GATE_ACTIVITY_TYPE:'+Activity.SCHEDULE_GATE_ACTIVITY_TYPE,Debugger.GEN,'checkEnableGateControls','PropertyInspector');
		//Debugger.log('_canvasModel.selectedItem.activity.activityTypeID:'+_canvasModel.selectedItem.activity.activityTypeID,Debugger.GEN,'checkEnableGateControls','PropertyInspector');
		if(_canvasModel.selectedItem.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE){
			trace('enabling....');
			days_stp.enabled = true;
			hours_stp.enabled = true;
			mins_stp.enabled = true;
			endHours_stp.enabled = true;
			endMins_stp.enabled = true;
					
		}
		/**/
		else{
			trace('disabling....');
			days_stp.enabled = false;
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
			numRandomGroups_stp.value = 0;
			numRandomGroups_stp.enabled = false;
			numLearners_stp.enabled = true;
		}else{
			numRandomGroups_stp.enabled = true;
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
			g.numberOfGroups = numRandomGroups_stp.value;
		}else{
			g.maxNumberOfGroups = numGroups_stp.value;
		}
	}
	
	private function getGroupingActivitiesDP(){
		var gActs = _canvasModel.getCanvas().ddm.getGroupingActivities();
		var gActsDP = new Array();
		var ga = _canvasModel.selectedItem.activity;
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		gActsDP.push({label:Dictionary.getValue('pi_no_grouping'),data:null});
		for(var i=0; i<gActs.length;i++){
			if (ga.createGroupingUIID != gActs[i].createGroupingUIID){
				trace("Grouping "+gActs[i].title+" has UIID: "+ gActs[i].createGroupingUIID );
				gActsDP.push({label:gActs[i].title,data:gActs[i]});
			}
		}
		return gActsDP;
	}
	
	
	

	/**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
      
        //Size the bkg_pnl
		clickTarget_mc._width = w;
        body_pnl.setSize(w,h-bar_pnl.height);
		cover_pnl.setSize(w,h-bar_pnl.height);
        bar_pnl.setSize(w);

        
    }
	
	
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
       // Debugger.log('Button stykle obejct',Debugger.GEN,'setStyles','PropertyInspector');
		var styleObj = _tm.getStyleObject('button');
		//ObjectUtils.printObject(styleObj);
		editGrouping_btn.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('PIlabel');
		
		toolDisplayName_lbl.setStyle('styleName',styleObj);
		title_lbl.setStyle('styleName',styleObj);
		desc_lbl.setStyle('styleName',styleObj);
		min_lbl.setStyle('styleName',styleObj);
		max_lbl.setStyle('styleName',styleObj);
		grouping_lbl.setStyle('styleName',styleObj);
		currentGrouping_lbl.setStyle('styleName',styleObj);
		gateType_lbl.setStyle('styleName',styleObj);
		gateType_lbl.setStyle('styleName',styleObj);
		startOffset_lbl.setStyle('styleName',styleObj);
		endOffset_lbl.setStyle('styleName',styleObj);
		days_lbl.setStyle('styleName', styleObj);
		hours_lbl.setStyle('styleName',styleObj);
		mins_lbl.setStyle('styleName',styleObj);
		hoursEnd_lbl.setStyle('styleName',styleObj);
		minsEnd_lbl.setStyle('styleName',styleObj);
		numGroups_lbl.setStyle('styleName',styleObj);
		numLearners_lbl.setStyle('styleName',styleObj);
		groupType_lbl.setStyle('styleName',styleObj);
		applied_grouping_lbl.setStyle('styleName',styleObj);
		//grouping_opt_lbl.setStyle('styleName',styleObj);
		title_txt.setStyle('styleName',styleObj);
		desc_txt.setStyle('styleName',styleObj);
		runOffline_chk.setStyle('styleName',styleObj);
		defineLater_chk.setStyle('styleName',styleObj);
		days_stp.setStyle('styleName', styleObj);
		hours_stp.setStyle('styleName',styleObj);
		mins_stp.setStyle('styleName',styleObj);
		endHours_stp.setStyle('styleName',styleObj);
		endMins_stp.setStyle('styleName',styleObj);
		minAct_stp.setStyle('styleName',styleObj);
		maxAct_stp.setStyle('styleName',styleObj);
		
		
		styleObj = _tm.getStyleObject('picombo');
		gateType_cmb.setStyle('styleName',styleObj);
		groupType_cmb.setStyle('styleName',styleObj);
		appliedGroupingActivity_cmb.setStyle('styleName',styleObj);
		
		//styleObj = _tm.getStyleObject('textinput');
		//title_txt.setStyle('styleName',styleObj);
		//desc_txt.setStyle('styleName',styleObj);
		
		//styleObj = _tm.getStyleObject('checkbox');
		//runOffline_chk.setStyle('styleName',styleObj);
		//defineLater_chk.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('numericstepper');
		days_stp.setStyle('styleName',styleObj);
		hours_stp.setStyle('styleName',styleObj);
		mins_stp.setStyle('styleName',styleObj);
		endHours_stp.setStyle('styleName',styleObj);
		endMins_stp.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('BGPanel');
		bar_pnl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('WZPanel');
		body_pnl.setStyle('styleName',styleObj);
		cover_pnl.setStyle('styleName',styleObj);
		
		
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
		var a = _canvasModel.selectedItem;
		//get the groupingUIID of the grouping Actviity we have selected.
		var newGroupingUIID = evt.target.value.createGroupingUIID;
		a.activity.groupingUIID = newGroupingUIID;
		trace("activity Type is for "+a.activity.title+ " is: "+a.activity.activityTypeID)
		if (a.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
			trace("Number of Children are: "+a.actChildren.length)
			for (var k=0; k<a.actChildren.length; k++){
				trace("Child has title : "+a.actChildren[k].title)
				a.actChildren[k].groupingUIID = newGroupingUIID;
				//var ca = CanvasActivity(a.actChildren[k])
				//ca.refresh();
			}
			a.init();
		}
		
		Debugger.log('Set grouping UIID to: '+a.activity.groupingUIID ,Debugger.GEN,'onAppliedGroupingChange','PropertyInspector');
		//_canvasModel.selectedItem.refresh();
		a.refresh()
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
		
		var startOffsetMins:Number = (days_stp.value * 60 * 24) + (hours_stp.value * 60) + mins_stp.value;
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
			_canvasModel.selectedItem.refresh(_canvasModel.selectedItem.activity.runOffline)
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
		if (_canvasModel.selectedItem.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE || _canvasModel.selectedItem.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE){
			_canvasModel.selectedItem.init();
		}
		
	}
	
}

