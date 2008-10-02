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
import org.lamsfoundation.lams.authoring.cmpt.*;
import org.lamsfoundation.lams.authoring.br.BranchConnector;
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
* @comments    Property Inspector Controls for the canvas
* 
*/
class PropertyInspectorControls extends MovieClip {
	
	private var _canvasModel:CanvasModel;
	private var _canvasController:CanvasController;
	private var _dictionary:Dictionary;
	
	 //References to components + clips 
   	private var _piIsExpended:Boolean;
    private var _tm:ThemeManager;
	private var _app:ApplicationParent;
	
    private var toolDisplayName_lbl:Label;
	private var clickTarget_mc:MovieClip;
	private var _depth:Number;
	private var delimitLine:MovieClip;
	private var expand_mc:MovieClip;
	private var collapse_mc:MovieClip;
	
	private static var ICON_OFFSET:Number = 20;
	
	//Properties tab
    private var title_lbl:Label;
    private var title_txt:TextInput;
	private var noselection_txt:TextInput;
    private var desc_lbl:Label;
    private var desc_txt:TextInput;
    private var piHeightHide:Number = 23;
	private var piHeightFull:Number = 106;
	private var grouping_lbl:Label;
	private var applied_grouping_lbl:Label;
	private var currentGrouping_lbl:Label;
	private var appliedGroupingActivity_cmb:ComboBox;
	
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
	
	//branches
	private var branchType_lbl:Label;
	private var branchType_cmb:ComboBox;
	
	private var toolActs_cmb:ComboBox;
	private var branchToolActs_lbl:Label;
	
	private var _group_match_btn:Button;
	private var _tool_output_match_btn:Button;
	private var _conditions_setup_btn:Button;
	private var _define_monitor_cb:CheckBox;

	//grouping 
	private var groupType_lbl:Label;
	private var numGroups_lbl:Label;
	private var numLearners_lbl:Label;
	private var equalGroupSizes_lbl:Label;
	private var groupType_cmb:ComboBox;
	private var numGroups_rdo:RadioButton;
	private var numLearners_rdo:RadioButton;
	private var rndGroup_radio:RadioButtonGroup;
	private var numGroups_stp:NumericStepper;
	private var numRandomGroups_stp:NumericStepper;
	private var numLearners_stp:NumericStepper;
	
	private var	equalGroupSizes_chk:CheckBox;
	private var _group_naming_btn:Button;
	
	//Competence
	private var _map_competence_btn:Button;
	
	//Complex Activity
	private var min_lbl:Label;
	private var max_lbl:Label;
	private var noSeqAct_lbl:Label;
	private var minAct_stp:NumericStepper;
	private var maxAct_stp:NumericStepper;
	private var noSeqAct_cmb:ComboBox;
	
	// Branch Connector
	private var _pi_defaultBranch_cb:CheckBox;
	
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
	public function PropertyInspectorControls(){
		//register to recive updates form the model
		Debugger.log('Constructor',Debugger.GEN,'PropertyInspector','PropertyInspector');
		
		_tm = ThemeManager.getInstance();
		_app = ApplicationParent.getInstance();
		
		//Set up this class to use the Flash event delegation model
		EventDispatcher.initialize(this);
		
		_dictionary = Dictionary.getInstance();
		
	}
	
	private function setButtonSizes():Void {
		var offset:Number = 10;
		this.createTextField("btn_text", this.getNextHighestDepth(), -100, -100, 10, 18); 
		var btn_text = this["btn_text"]
		btn_text.autoSize = true;
		btn_text.html = true;
		
		btn_text.htmlText = _group_match_btn.label;
		_group_match_btn.setSize(btn_text.textWidth + offset, 22);
		
		btn_text.htmlText = _group_naming_btn.label;
		_group_naming_btn.setSize(btn_text.textWidth + offset, 22);
		
		btn_text.htmlText = _tool_output_match_btn.label;
		_tool_output_match_btn.setSize(btn_text.textWidth + offset, 22);
		
		btn_text.htmlText = _conditions_setup_btn.label;
		_conditions_setup_btn.setSize(btn_text.textWidth + offset, 22);
		
		btn_text.removeTextField();
	}
	
	private function hideAllSteppers(v):Void{ 

		days_stp.visible = v
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
		gateType_cmb.tabIndex = 402
		days_stp.tabIndex = 403
		hours_stp.tabIndex = 404
		mins_stp.tabIndex = 405
		endHours_stp.tabIndex = 406
		endMins_stp.tabIndex = 407
		
		//Grouping Activities
		groupType_cmb.tabIndex = 402
		numGroups_stp.tabIndex = 403
		numGroups_rdo.tabIndex = 404
		numRandomGroups_stp.tabIndex = 405
		numLearners_rdo.tabIndex = 406
		numLearners_stp.tabIndex = 407
		
	}
	
	private function showToolActivityControls(v:Boolean, e:Boolean){
		
		var a = _canvasModel.selectedItem.activity;
		var parentAct = _canvasModel.getCanvas().ddm.getActivityByUIID(a.parentUIID)
		
		if(a.parentUIID != null && parentAct.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE) {
			applied_grouping_lbl.visible = v
			appliedGroupingActivity_cmb.visible = false;
			
			applied_grouping_lbl.enabled = e;
			
		} else {
			applied_grouping_lbl.visible = false
			appliedGroupingActivity_cmb.visible = v;
			
			appliedGroupingActivity_cmb.enabled = e;
			
		}
		
		grouping_lbl.visible = v;
		currentGrouping_lbl.visible = v;
		runOffline_chk.visible = v;
		defineLater_chk.visible = v;
		editGrouping_btn.visible = v;
		
		_map_competence_btn.visible = v;
		
		if(e != null) {
			grouping_lbl.enabled = e;
			currentGrouping_lbl.enabled = e;
			runOffline_chk.enabled = e;
			defineLater_chk.enabled = e;
			editGrouping_btn.enabled = e;
			
			_map_competence_btn.enabled = e;
		}
	}
	
	private function showGeneralInfo(v:Boolean, e:Boolean){
		total_num_activities_lbl.visible = v;
		total_num_activities_lbl.enabled = (e != null) ? e : true;
	} 
	
	private function showGeneralControls(v:Boolean, e:Boolean){

		title_lbl.visible = v;
		title_txt.visible = v;
		
		if(e != null) {
			title_lbl.enabled = e;
			title_txt.enabled = e;
		}
	} 
	
	private function showOptionalSequenceControls(v:Boolean, e:Boolean){
		noSeqAct_lbl.visible = v;
		noSeqAct_cmb.visible = v;
		
		if(e != null) {
			noSeqAct_cmb.enabled = e;
			noSeqAct_lbl.enabled = e;
		}

	}
	
	private function showOptionalActivityControls(v:Boolean, e:Boolean){
		min_lbl.visible = v;	
		max_lbl.visible = v;
		minAct_stp.visible = v;
		maxAct_stp.visible = v;
		desc_lbl.visible = v;
		desc_txt.visible = v;
		
		if(e != null) {
			min_lbl.enabled = e;	
			max_lbl.enabled = e;
			minAct_stp.enabled = e;
			maxAct_stp.enabled = e;
			desc_lbl.enabled = e;
			desc_txt.enabled = e;
		}
		
		grouping_lbl.visible = false;
	}
	
	private function showOptionalControls(v:Boolean, e:Boolean){
		if(!v) {
			showOptionalActivityControls(v);
			showOptionalSequenceControls(v);
		}

	}
	
	private function checkEnableOptionalControls(e:Boolean):Void {
		if(e != null) {
			minAct_stp.enabled = e;
			maxAct_stp.enabled = e;
		} else {
			minAct_stp.enabled = true;
			maxAct_stp.enabled = true;
		}
		
		if(_canvasModel.selectedItem.activity.isOptionsWithSequencesActivity()) {
			if(e != null)
				noSeqAct_cmb.enabled = e;
			else
				noSeqAct_cmb.enabled = true;
				
			
			MovieClipUtils.doLater(Proxy.create(this, showOptionalControlsLater, true));
			
		} else {	
			MovieClipUtils.doLater(Proxy.create(this, showOptionalControlsLater, false));
		}
	
	}
	
	private function showOptionalControlsLater(isSequence:Boolean):Void {
		if(isSequence) {
			min_lbl.text = Dictionary.getValue('pi_min_act', [Dictionary.getValue('pi_seq')]);
			max_lbl.text = Dictionary.getValue('pi_max_act', [Dictionary.getValue('pi_seq')]);
			
			MovieClipUtils.doLater(Proxy.create(this, showOptionalSequenceControls, true));
			MovieClipUtils.doLater(Proxy.create(this, showOptionalActivityControls, true));
		} else {
			min_lbl.text = Dictionary.getValue('pi_min_act', [Dictionary.getValue('pi_act')]);
			max_lbl.text = Dictionary.getValue('pi_max_act', [Dictionary.getValue('pi_act')]);
			
			MovieClipUtils.doLater(Proxy.create(this, showOptionalActivityControls, true));
		}
	}
	
	private function showGateControls(v:Boolean, e:Boolean){
		 		
		var steppers_visible:Boolean = (_canvasModel.selectedItem.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE);

		days_lbl.visible = (steppers_visible && v);
		hours_lbl.visible = (steppers_visible && v);
		mins_lbl.visible = (steppers_visible && v);
		hoursEnd_lbl.visible = v;
		minsEnd_lbl.visible = v;
		days_stp.visible = (steppers_visible && v);
		hours_stp.visible = (steppers_visible && v);
		mins_stp.visible = (steppers_visible && v);
		gateType_lbl.visible = v;
		gateType_cmb.visible = v;
		startOffset_lbl.visible = (steppers_visible && v);
		
		if(e != null) {
			days_lbl.enabled = e;
			hours_lbl.enabled = e;
			mins_lbl.enabled = e;
			hoursEnd_lbl.enabled = e;
			minsEnd_lbl.enabled = e;
			days_stp.enabled = e;
			hours_stp.enabled = e;
			mins_stp.enabled = e;
			gateType_lbl.enabled = e;
			gateType_cmb.enabled = e;
			startOffset_lbl.enabled = e;
		}
		
	}
	
	private function showBranchingControls(v:Boolean, e:Boolean){
		branchType_lbl.visible = v;
		branchType_cmb.visible = v;
		
		hideAllSteppers(false);
		
		var _activityTypeID:Number = _canvasModel.selectedItem.activity.activityTypeID;
		
		if(_activityTypeID == Activity.GROUP_BRANCHING_ACTIVITY_TYPE) {
			
			showGroupBasedBranchingControls(v, e);
			showAppliedGroupingControls(v, e);
			
		} else if(_activityTypeID == Activity.TOOL_BRANCHING_ACTIVITY_TYPE) {
			
			showToolBasedBranchingControls(v, e);
			
			if(toolActs_cmb.visible) {
				
				toolActs_cmb.dataProvider = _canvasModel.getDownstreamActivities(ToolActivity, true);
				
				if(_canvasModel.selectedItem.activity.toolActivityUIID != null) {
					var dp = toolActs_cmb.dataProvider;
				
					for(var i=0; i < dp.length; i++)
						if(dp[i].data == _canvasModel.selectedItem.activity.toolActivityUIID)
							toolActs_cmb.selectedIndex = i;
							
					if(toolActs_cmb.selectedIndex == 0) {
						_canvasModel.selectedItem.activity.toolActivityUIID = null;
						branchToolInputChange(_canvasModel.selectedItem, toolActs_cmb.dataProvider[0].data);
					}
				}
			}
			
		} else {
			showGroupBasedBranchingControls(false);
			showAppliedGroupingControls(false);
			showToolBasedBranchingControls(false);
		}
		
		if(e != null) {
			branchType_lbl.enabled = e;
			branchType_cmb.enabled = e;
			
			_group_match_btn.enabled = e;
			_tool_output_match_btn.enabled = e;
			
			toolActs_cmb.enabled = e;
		}
	}
	
	/**
	 * Shows or hides the app.lied grouping
	 * AND title fields
	 * @usage   
	 * @param   v 
	 * @return  
	 */
	private function showAppliedGroupingControls(v:Boolean, e:Boolean){
		if(_canvasModel.selectedItem.activity.activityTypeID != Activity.GROUP_BRANCHING_ACTIVITY_TYPE) {
			grouping_lbl._y = 52;
			appliedGroupingActivity_cmb._y = 52;
		} else {
			grouping_lbl._y = 75;
			appliedGroupingActivity_cmb._y = 75;
		}
			
		grouping_lbl.visible = v;
		appliedGroupingActivity_cmb.visible = v;
		
		if(e != null) {
			grouping_lbl.enabled = e;
			appliedGroupingActivity_cmb.enabled = e;
		}
	
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
	
	private function showGroupingControls(v:Boolean, e:Boolean){
		groupType_lbl.visible = v;
		groupType_cmb.visible = v;
		
		if(v){
			showRelevantGroupOptions();
		}else{
			numGroups_lbl.visible = v;
			numLearners_lbl.visible = v;
			equalGroupSizes_lbl.visible = v;
			equalGroupSizes_chk.visible = v;
			numGroups_rdo.visible = v;
			numLearners_rdo.visible = v;
			numGroups_stp.visible = v;
			numRandomGroups_stp.visible = v;
			numLearners_stp.visible = v;
			_group_naming_btn.visible = v;
		}
		
	}
		
	private function showRelevantGroupOptions(e:Boolean){
		
		var ga = _canvasModel.selectedItem.activity;
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		
		Debugger.log("showRelevantGroupOptions->g.groupingTypeID: "+g.groupingTypeID, Debugger.GEN, "showRelevantGroupOptions", "PIC*");
		
		if(g.groupingTypeID == Grouping.CHOSEN_GROUPING){
			numGroups_lbl.visible = true;
			numLearners_lbl.visible = false;
			equalGroupSizes_lbl.visible = false;
			equalGroupSizes_chk.visible = false;
			numGroups_stp.visible = true;
			numRandomGroups_stp.visible = false;
			numLearners_stp.visible = false;
			numLearners_rdo.visible = false;
			numGroups_rdo.visible = false;
			
			_group_naming_btn.visible = true;
			
			if(e != null) {
				numGroups_lbl.enabled = e;
				_group_naming_btn.enabled = e;
			}
		} 
		else if(g.groupingTypeID == Grouping.LEARNER_CHOICE_GROUPING){
			numGroups_lbl.visible = true;
			numLearners_lbl.visible = true;
			equalGroupSizes_lbl.visible = true;
			equalGroupSizes_chk.visible = true;
			numRandomGroups_stp.visible = false;
			numGroups_stp.visible = true;
			numLearners_stp.visible = true;
			numLearners_rdo.visible = true;
			numGroups_rdo.visible = true;
			
			_group_naming_btn.visible = true;
			
			if(e != null) {
				numGroups_lbl.enabled = e;
				numLearners_lbl.enabled = e;
				equalGroupSizes_lbl.enabled = e;
				//numGroups_stp.enabled = e;
				//numLearners_stp.enabled = e;
				numLearners_rdo.enabled = e;
				numGroups_rdo.enabled = e;
				equalGroupSizes_chk.enabled = e;
				
				_group_naming_btn.enabled = e;
			}
			
			checkGroupRadioOptions(e);
			checkEnableGroupsOptions(e);
		} 
		else if(g.groupingTypeID == Grouping.RANDOM_GROUPING) {
			numGroups_lbl.visible = true;
			numLearners_lbl.visible = true;
			equalGroupSizes_lbl.visible = false;
			equalGroupSizes_chk.visible = false;
			numGroups_stp.visible = false;
			numRandomGroups_stp.visible = true;
			numLearners_stp.visible = true;
			numLearners_rdo.visible = true;
			numGroups_rdo.visible = true;
			
			_group_naming_btn.visible = true;
			
			if(e != null) {
				numGroups_lbl.enabled = e;
				numLearners_lbl.enabled = e;
				numGroups_stp.enabled = e;
				numRandomGroups_stp.enabled = e;
				numLearners_stp.enabled = e;
				numLearners_rdo.enabled = e;
				numGroups_rdo.enabled = e;
				
				_group_naming_btn.enabled = e;
			}
			
			checkGroupRadioOptions(e);
			checkEnableGroupsOptions(e);
			
		}else{
			//error dont understand the grouping type
		}
		
	}
	
	private function reDrawTroublesomeSteppers(e:Boolean){
		numLearners_stp.visible = true;
		
		var g:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.createGroupingUIID);
		
		if (g.groupingTypeID == Grouping.RANDOM_GROUPING) {
			numRandomGroups_stp.visible = true; 
			numGroups_stp.visible = false;
		}
		else if (g.groupingTypeID == Grouping.LEARNER_CHOICE_GROUPING) {
			numRandomGroups_stp.visible = false;
			numGroups_stp.visible = true;
		}
	}
	
	private function checkEnableGateControls(e:Boolean){
		if(_canvasModel.selectedItem.activity.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE) {
			if(e != null) {
				days_stp.enabled = e;
				hours_stp.enabled = e;
				mins_stp.enabled = e;
				endHours_stp.enabled = e;
				endMins_stp.enabled = e;
			} else {
				days_stp.enabled = true;
				hours_stp.enabled = true;
				mins_stp.enabled = true;
				endHours_stp.enabled = true;
				endMins_stp.enabled = true;
			}
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
	private function checkEnableGroupsOptions(e:Boolean){
		var groupingBy = rndGroup_radio.selection.data;
		
		Debugger.log('groupingBy:'+groupingBy,Debugger.GEN,'checkEnableGroupsOptions','PropertyInspector');
		var g:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.createGroupingUIID);
		
		if(groupingBy == 'num_learners'){
			numRandomGroups_stp.value = 0;
			numGroups_stp.value = 0;
			g.numberOfGroups = 0;
			g.equalGroupSizes = null;
			equalGroupSizes_chk.selected = false;
			
			numRandomGroups_stp.enabled = false;
			numGroups_stp.enabled = false;
			
			numLearners_stp.enabled = (e != null) ? e : true;
			
			equalGroupSizes_lbl.visible = false;
			equalGroupSizes_chk.visible = false;
			_group_naming_btn.visible = false;
			
		}else{
			numRandomGroups_stp.enabled = (e != null) ? e : true;
			numGroups_stp.enabled = (e != null) ? e : true;
			
			numLearners_stp.value = 0;
			g.learnersPerGroups = 0;
			
			numLearners_stp.enabled = false;
			
			_group_naming_btn.enabled = (e != null) ? e : true;
			_group_naming_btn.visible = true;
			
			if (g.groupingTypeID == Grouping.LEARNER_CHOICE_GROUPING) {
				equalGroupSizes_chk.enabled = (e != null) ? e : true;
				equalGroupSizes_lbl.visible = true;
				equalGroupSizes_chk.visible = true;
			}
		}
		
		//this is a crazy hack to stop the steppter dissapearing after its .enabled property is set.
		//waits 2 frames to re-display the control ( 1 doensnt work!)
		MovieClipUtils.doLater(Proxy.create(this,reDrawTroublesomeSteppersLater));
		
	}
	
	private function checkGroupRadioOptions(e:Boolean) {
		var g:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.createGroupingUIID);
		
		if(g.numberOfGroups > 0 && g.learnersPerGroups <= 0) { 	
			numGroups_rdo.selected = true; 
			_group_naming_btn.visible = true; 
			_group_naming_btn.enabled = (e != null) ? e : true;
			
			equalGroupSizes_lbl.visible = false;
			equalGroupSizes_chk.visible = false;
						
		} else if(g.learnersPerGroups > 0 && g.numberOfGroups <= 0) { 
			numLearners_rdo.selected = true; 
			_group_naming_btn.visible = false;
			
			if (g.groupingTypeID == Grouping.LEARNER_CHOICE_GROUPING) {
				equalGroupSizes_lbl.visible = true;
				equalGroupSizes_chk.visible = true;
			}
		}
		else { // this is the case where both the steppers have 0 values
			Debugger.log("checkGroupRadioOptions else", Debugger.GEN, "checkGroupRadioOptions", "PIC*");
			numGroups_rdo.selected = true;
			_group_naming_btn.visible = false;
		}
	}
	
	public function reDrawTroublesomeSteppersLater(){
		MovieClipUtils.doLater(Proxy.create(this,reDrawTroublesomeSteppers));
	}
	
	private function populateGroupingProperties(ga:GroupingActivity){
		Debugger.log("populating Grouping Properties createGroupingUIID: " + ga.createGroupingUIID, Debugger.CRITICAL, "populateGroupingProperties", "PropertyInspectorControls");
		
		var g:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		toolDisplayName_lbl.text = "<b>"+Dictionary.getValue('pi_title')+"</b> - "+Dictionary.getValue('pi_activity_type_grouping');
		
		Debugger.log('This is the grouping object:',Debugger.GEN,'populateGroupingProperties','PropertyInspectorControls');
		ObjectUtils.printObject(g);
		
		//loop through combo to fins SI of our gate activity type
		for (var i=0; i<groupType_cmb.dataProvider.length;i++)
			if(g.groupingTypeID == groupType_cmb.dataProvider[i].data) groupType_cmb.selectedIndex=i;
		
		if(g.groupingTypeID == Grouping.RANDOM_GROUPING) {
			numLearners_stp.value = (g.learnersPerGroups != null) ? g.learnersPerGroups : 0;
			numRandomGroups_stp.value = (g.numberOfGroups != null) ? g.numberOfGroups : 0;
		}
		else if (g.groupingTypeID == Grouping.LEARNER_CHOICE_GROUPING) {
			
			numGroups_stp.value = (g.numberOfGroups != null) ? g.numberOfGroups : 0;
			numLearners_stp.value = (g.learnersPerGroups != null) ? g.learnersPerGroups : 0;
			equalGroupSizes_chk.selected = (g.equalGroupSizes != null) ? g.equalGroupSizes : false;
		}
		else { // Teacher Chosen Grouping
			numGroups_stp.value = (g.maxNumberOfGroups != null) ? g.maxNumberOfGroups : 0;
		}
		
	}
	
	/**
	 * Called when there is a change in the values of the group method steppers
	 * Butdates the value inthe grouping class being edited.
	 * @usage   
	 * @return  
	 */
	public function updateGroupingMethodData(evt:Object){
		
		Debugger.log("updating grouping method data: " + g.groupingUIID, Debugger.CRITICAL, "updateGroupingMethodData", "PropertyInspectorControls");
		
		if(!_canvasController.isBusy() && (evt.type == 'focusOut' || evt.type == 'click')) {
			var g:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.createGroupingUIID);
		
			doUpdateGroupingMethodData(g);
		}
	}
	
	private function doUpdateGroupingMethodData(g:Grouping) {
		
		if(g.groupingTypeID == Grouping.RANDOM_GROUPING){
			//note only one of these should actually have a non 0 value
			g.learnersPerGroups = numLearners_stp.value;
			g.numberOfGroups = numRandomGroups_stp.value;
			
			numGroups_stp.value = 0;
			g.maxNumberOfGroups = 0;
		}
		else if(g.groupingTypeID == Grouping.LEARNER_CHOICE_GROUPING){
			g.learnersPerGroups = numLearners_stp.value;
			g.numberOfGroups = numGroups_stp.value;
			g.maxNumberOfGroups = 0;
			g.equalGroupSizes = equalGroupSizes_chk.selected;
		}else{
			g.maxNumberOfGroups = numGroups_stp.value;
			
			numRandomGroups_stp.value = 0;
			numLearners_stp.value = 0;
			g.learnersPerGroups = 0;
			g.numberOfGroups = 0;
		}
				
		setModified();
		
		_canvasController.clearBusy();
		
	}
	
	private function getGroupingActivitiesDP(){
		var gActs:Array = _canvasModel.getDownstreamActivities(GroupingActivity, false);
		Debugger.log("grouping acts len: " + gActs.length, Debugger.CRITICAL, "getGroupingActivitiesDP", "PIC");
		
		var gActsDP = new Array();
		var ga = _canvasModel.selectedItem.activity;
		var g = _canvasModel.getCanvas().ddm.getGroupingByUIID(ga.createGroupingUIID);
		
		gActsDP.push({label:Dictionary.getValue('pi_no_grouping'), data:null});
		
		for(var i=0; i<gActs.length;i++){
			if (ga.createGroupingUIID != gActs[i].data.createGroupingUIID){
				gActsDP.push({label:gActs[i].label, data:gActs[i].data});
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
		
		// size the expand/colapse icon
		expand_mc._x = w - ICON_OFFSET;
		collapse_mc._x = w - ICON_OFFSET;
		expand_mc.redraw(true);
		collapse_mc.redraw(true);
    }
	
	/**
	 * Get the CSSStyleDeclaration objects for each component and applies them
	 * directly to the instanced
	 * @usage   
	 * @return  
	 */
	private function setStyles() {
		var styleObj = _tm.getStyleObject('button');
		editGrouping_btn.setStyle('styleName', styleObj);
		
		_group_match_btn.setStyle('styleName', styleObj);
		_group_naming_btn.setStyle('styleName', styleObj);
		
		_tool_output_match_btn.setStyle('styleName', styleObj);
		_conditions_setup_btn.setStyle('styleName', styleObj);
		
		_map_competence_btn.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('PIlabel');
		
		toolDisplayName_lbl.setStyle('styleName', styleObj);
		title_lbl.setStyle('styleName', styleObj);
		desc_lbl.setStyle('styleName', styleObj);
		min_lbl.setStyle('styleName', styleObj);
		max_lbl.setStyle('styleName', styleObj);
		noSeqAct_lbl.setStyle('styleName', styleObj);
		grouping_lbl.setStyle('styleName', styleObj);
		currentGrouping_lbl.setStyle('styleName', styleObj);
		gateType_lbl.setStyle('styleName', styleObj);
		branchType_lbl.setStyle('styleName', styleObj);
		branchToolActs_lbl.setStyle('styleName', styleObj);
		startOffset_lbl.setStyle('styleName', styleObj);
		endOffset_lbl.setStyle('styleName', styleObj);
		days_lbl.setStyle('styleName', styleObj);
		hours_lbl.setStyle('styleName', styleObj);
		mins_lbl.setStyle('styleName', styleObj);
		hoursEnd_lbl.setStyle('styleName', styleObj);
		minsEnd_lbl.setStyle('styleName', styleObj);
		numGroups_lbl.setStyle('styleName', styleObj);
		equalGroupSizes_lbl.setStyle('styleName', styleObj);
		numLearners_lbl.setStyle('styleName', styleObj);
		groupType_lbl.setStyle('styleName', styleObj);
		applied_grouping_lbl.setStyle('styleName', styleObj);
		title_txt.setStyle('styleName', styleObj);
		desc_txt.setStyle('styleName', styleObj);
		runOffline_chk.setStyle('styleName', styleObj);
		defineLater_chk.setStyle('styleName', styleObj);
		days_stp.setStyle('styleName', styleObj);
		hours_stp.setStyle('styleName', styleObj);
		mins_stp.setStyle('styleName', styleObj);
		endHours_stp.setStyle('styleName', styleObj);
		endMins_stp.setStyle('styleName', styleObj);
		minAct_stp.setStyle('styleName', styleObj);
		maxAct_stp.setStyle('styleName', styleObj);
		
		_pi_defaultBranch_cb.setStyle('styleName', styleObj);
		_define_monitor_cb.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('picombo');
		gateType_cmb.setStyle('styleName', styleObj);
		branchType_cmb.setStyle('styleName', styleObj);
		groupType_cmb.setStyle('styleName', styleObj);
		appliedGroupingActivity_cmb.setStyle('styleName', styleObj);
		toolActs_cmb.setStyle('styleName', styleObj);
		noSeqAct_cmb.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('numericstepper');
		days_stp.setStyle('styleName', styleObj);
		hours_stp.setStyle('styleName', styleObj);
		mins_stp.setStyle('styleName', styleObj);
		endHours_stp.setStyle('styleName', styleObj);
		endMins_stp.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('BGPanel');
		bar_pnl.setStyle('styleName',styleObj);
		
		styleObj = _tm.getStyleObject('WZPanel');
		body_pnl.setStyle('styleName', styleObj);
		cover_pnl.setStyle('styleName', styleObj);
		
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
		checkEnableGateControls(!_canvasModel.selectedItem.activity.readOnly);
		
		setModified();
	}
	
	/**
	 *Handles change event fired from the branchType_cmb
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onBranchTypeChange(evt:Object){
		_canvasModel.selectedItem.activity.activityTypeID = evt.target.value;
		
		if(evt.target.value == Activity.GROUP_BRANCHING_ACTIVITY_TYPE) {
			var selectedGroup = appliedGroupingActivity_cmb.dataProvider[appliedGroupingActivity_cmb.selectedIndex].data;
			
			if(selectedGroup != null) {
				_canvasModel.selectedItem.activity.groupingUIID = selectedGroup.createGroupingUIID;
			}
			
			_canvasModel.selectedItem.activity.toolActivityUIID = null;
			
			showGroupBasedBranchingControls(true, !_canvasModel.selectedItem.activity.readOnly);
			showAppliedGroupingControls(true, !_canvasModel.selectedItem.activity.readOnly);
			
			showToolBasedBranchingControls(false);

			
		} else if(evt.target.value == Activity.TOOL_BRANCHING_ACTIVITY_TYPE) {
			showToolBasedBranchingControls(true, !_canvasModel.selectedItem.activity.readOnly);
			
			_canvasModel.selectedItem.activity.groupingUIID = null;
			
			showGroupBasedBranchingControls(false);
			showAppliedGroupingControls(false);
		
			if(toolActs_cmb.visible) {
				toolActs_cmb.dataProvider = _canvasModel.getDownstreamActivities(ToolActivity, true);
				for(var i=0; i < toolActs_cmb.dataProvider.length; i++)
					selectToolActivityItem(i, toolActs_cmb.dataProvider[i].data);
			}
				
		} else {
			
			_canvasModel.selectedItem.activity.groupingUIID = null;
			_canvasModel.selectedItem.activity.toolActivityUIID = null;
			
			showToolBasedBranchingControls(false);
			showGroupBasedBranchingControls(false);
			showAppliedGroupingControls(false);
		}
		
		_canvasModel.selectedItem.refresh();
		
		setModified();
	}
	
	private function selectToolActivityItem(index:Number, UIID:Number):Void {
		
		var mappings:Array = _canvasModel.getCanvas().ddm.getBranchMappingsByActivityUIIDAndType(_canvasModel.selectedItem.activity.activityUIID).toolBased;
		
		Debugger.log("Loading Lists: mappings length: " + mappings.length, Debugger.CRITICAL, "loadLists", "ConditionMatchingDialog");
		
		for(var i=0; i < mappings.length; i++) {
			if(mappings[i].condition.toolActivity.activityUIID == UIID &&
				mappings[i].condition.branchingActivity.activityUIID == _canvasModel.selectedItem.activity.activityUIID) {
				toolActs_cmb.selectedIndex = index;
				branchToolInputChange(_canvasModel.selectedItem, toolActs_cmb.dataProvider[index].data);
			}
		}
	}
	
	/**
	 * Handles change event fired from the groupType_cmb
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onGroupTypeChange(evt:Object){
		var g:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.createGroupingUIID);
		
		Debugger.log('groupingUIID: '+g.groupingUIID,Debugger.GEN,'onGroupTypeChange','PropertyInspector');
		
		updateGroupingType(g, evt.target.value);
		
	}
	
	private function updateGroupingType(g:Grouping, typeID:Number) {
		g.groupingTypeID = typeID;
		
		Debugger.log('Set group type to: '+g.groupingTypeID,Debugger.GEN,'onGroupTypeChange','PropertyInspectorControls');
		showRelevantGroupOptions(!_canvasModel.selectedItem.activity.readOnly);
		doUpdateGroupingMethodData(g);
		
		setModified();
	}
	
	private function onAppliedGroupingChange(evt:Object){
		var a = _canvasModel.selectedItem;
		
		// get the groupingUIID of the grouping Actviity we have selected.
		var newGroupingUIID = evt.target.value.createGroupingUIID;
		a.activity.groupingUIID = newGroupingUIID;
		if (a.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
			for (var k=0; k<a.actChildren.length; k++){
				a.actChildren[k].groupingUIID = newGroupingUIID;
			}
			a.init();
		}
		
		Debugger.log('Set grouping UIID to: '+a.activity.groupingUIID ,Debugger.GEN,'onAppliedGroupingChange','PropertyInspector');
		a.refresh()
		
		if(a.activity.activityTypeID == Activity.GROUP_BRANCHING_ACTIVITY_TYPE) {
			
			if(a.activity.groupingUIID == null) {
				a.activity.defineLater = null;
				showGroupBasedBranchingControls(false);
			} else { 
				showGroupBasedBranchingControls(true, !_canvasModel.selectedItem.activity.readOnly); 
				onDefineMonitorSelect();
			}
		}
		
		setModified();
	}
	
	private function onBranchToolInputChange(evt:Object) {
		Debugger.log('branch input change: ' + evt.target.value, Debugger.CRITICAL, "onBranchToolInputChange", "PIC*");
		
		branchToolInputChange(_canvasModel.selectedItem, evt.target.value);
	}
	
	private function branchToolInputChange(ca, toolActivityUIID) {
		var mappings:Array = _canvasModel.getCanvas().ddm.getBranchMappingsByActivityUIIDAndType(ca.activity.activityUIID).toolBased;
		
		ca.activity.toolActivityUIID = (toolActivityUIID != 0) ? toolActivityUIID : null;
		ca.refresh();
			
		var sequences:Array = _canvasModel.getCanvas().ddm.getComplexActivityChildren(_canvasModel.selectedItem.activity.activityUIID);
	
		if(hasConnectedSequences(sequences)) {
			_conditions_setup_btn.visible = (ca.activity.toolActivityUIID != null) ? true : false;
			_tool_output_match_btn.visible = (ca.activity.toolActivityUIID != null) ? true : false;
		}
		
		setModified();
	}
	
	private function hasConnectedSequences(s:Array):Boolean {
		for(var i=0; i<s.length; i++) {
			var seqAct:SequenceActivity = SequenceActivity(s[i]);
			
			if(seqAct.firstActivityUIID != null)
				return true;
			else if(seqAct.empty && !seqAct.isDefault)
				return true;
		}
		
		return false;
	}
	
	private function showToolBasedBranchingControls(v:Boolean, e:Boolean) {
		toolActs_cmb.visible = v;
		branchToolActs_lbl.visible = v;
		
		if(!v) { _tool_output_match_btn.visible = false; _conditions_setup_btn.visible = false; return; }
		
		var sequences:Array = _canvasModel.getCanvas().ddm.getComplexActivityChildren(_canvasModel.selectedItem.activity.activityUIID);
		
		Debugger.log("has connected seq: " + hasConnectedSequences(sequences), Debugger.CRITICAL, "showToolBasedBranchingControls", "PIC*");
		
		if(hasConnectedSequences(sequences)) {
			if(_canvasModel.selectedItem.activity.toolActivityUIID != null) {
				_tool_output_match_btn.visible = v;
				_conditions_setup_btn.visible = v;
			} else {
				_tool_output_match_btn.visible = false;
				_conditions_setup_btn.visible = false;
			}
		}
		
		if(e != null) {
			toolActs_cmb.enabled = e;
			_tool_output_match_btn.enabled = e;
			_conditions_setup_btn.enabled = e;
		}
	}
	
	private function showGroupBasedBranchingControls(v:Boolean, e:Boolean) {
		if(!v) { 
			_group_match_btn.visible = false;
			_define_monitor_cb.visible = false; 
			
			return; 
		}		
		
		var ca = _canvasModel.selectedItem;
		var sequences:Array = _canvasModel.getCanvas().ddm.getComplexActivityChildren(ca.activity.activityUIID);
		
		if(hasConnectedSequences(sequences) && ca.activity.groupingUIID != null) {
			var grouping:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(ca.activity.groupingUIID);
			
			if(grouping.learnersPerGroups > 0)
				_canvasModel.selectedItem.activity.defineLater = true;
			
			_define_monitor_cb.selected = _canvasModel.selectedItem.activity.defineLater;
			
			_group_match_btn.visible = ((grouping.numberOfGroups > 0 || grouping.maxNumberOfGroups > 0) && !_define_monitor_cb.selected) ? v : false;
			_define_monitor_cb.visible = (grouping.numberOfGroups > 0 || grouping.maxNumberOfGroups > 0 || grouping.learnersPerGroups > 0) ? v : false;

			if(e != null && !e) {
				_group_match_btn.enabled = e;
				_define_monitor_cb.enabled = (grouping.learnersPerGroups > 0) ? false : e;
			} else {
				_define_monitor_cb.enabled = (grouping.learnersPerGroups > 0) ? false : true;
			}
			
		}
	}
	
	/**
	 * Handles click event from the grouping method radio buttons
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
	private function onGroupingMethodChange(evt:Object){
		checkEnableGroupsOptions(!_canvasModel.selectedItem.activity.readOnly);
		
		setModified();
	}
	
	
	private function onScheduleOffsetChange(evt:Object){
		
		var startOffsetMins:Number = (days_stp.value * 60 * 24) + (hours_stp.value * 60) + mins_stp.value;
		GateActivity(_canvasModel.selectedItem.activity).gateStartTimeOffset = startOffsetMins;
		
		var endOffsetMins:Number = (endHours_stp.value * 60) + endMins_stp.value;
		GateActivity(_canvasModel.selectedItem.activity).gateEndTimeOffset = endOffsetMins;
		
		Debugger.log('activity.gateStartTimeOffset :'+GateActivity(_canvasModel.selectedItem.activity).gateStartTimeOffset ,Debugger.GEN,'onScheduleOffsetChange','PropertyInspector');
		Debugger.log('activity.gateEndTimeOffset :'+GateActivity(_canvasModel.selectedItem.activity).gateEndTimeOffset,Debugger.GEN,'onScheduleOffsetChange','PropertyInspector');
		
		setModified();
		
	}
	
	private function onGroupMatchClick(evt:Object){
		
		// open group to branch matching window
		_app.dialog = PopUpManager.createPopUp(Application.root, LFWindow, true, {title:Dictionary.getValue('groupmatch_dlg_title_lbl'), closeButton:true, resize:false, scrollContentPath:'GroupMatchingDialog'});
		_app.dialog.addEventListener('contentLoaded', Delegate.create(this, groupMatchDialogLoaded));
		
		setModified();
	}
	
	private function onGroupNamingClick(evt:Object){
	
		// open group to branch matching window
		_app.dialog = PopUpManager.createPopUp(Application.root, LFWindow, true, {title:Dictionary.getValue('groupnaming_dlg_title_lbl'), closeButton:true, resize:false, scrollContentPath:'GroupNamingDialog'});
		_app.dialog.addEventListener('contentLoaded', Delegate.create(this, GroupNamingDialogLoaded));
		
		setModified();
	}
	
	private function onMapCompetenceClick(evt:Object){
		// open group to competence mapping window
		_app.dialog = PopUpManager.createPopUp(Application.root, LFWindow, true, {title:Dictionary.getValue("competence_mappings_btn"), closeButton:true, resize:false, scrollContentPath:'CompetenceMappingDialog'});
	}
	
	public function openConditionMatchDialog():Void {
		onConditionMatchClick();
	}
	
	private function onConditionMatchClick(evt:Object){
		// open group to branch matching window
		_app.dialog = PopUpManager.createPopUp(Application.root, LFWindow, true, {title:Dictionary.getValue('condmatch_dlg_title_lbl'), closeButton:true, resize:false, scrollContentPath:'ConditionMatchingDialog'});
		_app.dialog.addEventListener('contentLoaded', Delegate.create(this, ConditionMatchDialogLoaded));
		
		setModified();
	}
	
	private function onConditionsSetupClick(evt:Object){
		// show tool outputs to branch mappings dialog
		var ta:ToolActivity = ToolActivity(_canvasModel.getCanvas().ddm.getActivityByUIID(_canvasModel.selectedItem.activity.toolActivityUIID));
		_canvasModel.getCanvas().getToolOutputDefinitions(ta);
		
		setModified();
	}
	
	private function groupMatchDialogLoaded(evt:Object) {
		var grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.groupingUIID);
		
		evt.target.scrollContent.branchingActivity = BranchingActivity(_canvasModel.selectedItem.activity);
		evt.target.scrollContent.groups = grouping.getGroups(_canvasModel.getCanvas().ddm);
		evt.target.scrollContent.sequences = getValidSequences(_canvasModel.getCanvas().ddm.getComplexActivityChildren(_canvasModel.selectedItem.activity.activityUIID));
		evt.target.scrollContent.loadLists();
		
	}
	
	private function GroupNamingDialogLoaded(evt:Object) {
		var grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.createGroupingUIID);
		
		Debugger.log("grouping: " + grouping, Debugger.CRITICAL, "GroupNamingDialogLoaded", "PropertyInspectorControls");
		
		evt.target.scrollContent.grouping = grouping;
		evt.target.scrollContent.groups = grouping.getGroups(_canvasModel.getCanvas().ddm);
		evt.target.scrollContent.setupGrid();
	}
	
	private function ConditionMatchDialogLoaded(evt:Object) {
		var conditions:Array = _canvasModel.getCanvas().ddm.getAllConditionsForToolOutput(BranchingActivity(_canvasModel.selectedItem.activity));
		
		evt.target.scrollContent.branchingActivity = BranchingActivity(_canvasModel.selectedItem.activity);
		evt.target.scrollContent.conditions = conditions;
		evt.target.scrollContent.sequences = getValidSequences(_canvasModel.getCanvas().ddm.getComplexActivityChildren(_canvasModel.selectedItem.activity.activityUIID));
		evt.target.scrollContent.loadLists();
	}
	
	/**
	 * @depricated
	 * 
	 * @usage   
	 * @param   branches 
	 * @return  
	 *
	private function getValidBranches(branches:Array):Array {
		Debugger.log("validating br len: " + branches.length, Debugger.CRITICAL, "getvalidbranches", "PIC*");
		
		for(var i=0; i < branches.length; i++) {
			Debugger.log("validating br: " + branches[i].sequenceActivity.title, Debugger.CRITICAL, "getvalidbranches", "PIC*");
			Debugger.log("validating br dir: " + branches[i].direction, Debugger.CRITICAL, "getvalidbranches", "PIC*");
			
			if(branches[i].direction != BranchConnector.DIR_FROM_START && branches[i].direction != BranchConnector.DIR_SINGLE) {
				branches.splice(i, 1);
				i=i-1;
			}
		}
		
		return branches;
	}
	*/
	
	private function getValidSequences(seqs:Array):Array {
		for(var i=0; i < seqs.length; i++) {
			var sequence:SequenceActivity = SequenceActivity(seqs[i]);
			
			if(sequence.isDefault && sequence.empty) {
				seqs.splice(i, 1);
				i=i-1;
			} else if(!sequence.empty && sequence.firstActivityUIID == null) {
				seqs.splice(i, 1);
				i=i-1;
			}
		}
		
		return seqs;
	}
	
	public function onDefaultBranchSelect(evt:Object):Void {
		if(_pi_defaultBranch_cb.selected) {
			_canvasModel.activeView.activity.defaultBranch = BranchConnector(_canvasModel.selectedItem).branch;
			_pi_defaultBranch_cb.enabled = false;
		}
		
		setModified();
	}
	
	public function onDefineMonitorSelect(evt:Object):Void {
			
		_canvasModel.selectedItem.activity.defineLater = _define_monitor_cb.selected;

		var grouping:Grouping = _canvasModel.getCanvas().ddm.getGroupingByUIID(_canvasModel.selectedItem.activity.groupingUIID);
		_group_match_btn.visible = ((grouping.numberOfGroups > 0 || grouping.maxNumberOfGroups > 0) && !_define_monitor_cb.selected) ? true : false;
		
		if(_define_monitor_cb.selected) {
			_canvasModel.getCanvas().ddm.clearBranchMappingsByGroupingUIID(grouping.groupingUIID);
		}
		
		setModified();
	}
    
	/**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the target
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	 /**/
	public function click(e):Void{
		var tgt:String = new String(e.target);
		
		if(tgt.indexOf("defineLater_chk") != -1){
			
			_canvasModel.selectedItem.activity.defineLater = defineLater_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.defineLater:'+_canvasModel.selectedItem.activity.defineLater,Debugger.GEN,'click','PropertyInspector');
		}else if(tgt.indexOf("runOffline_chk") != -1){
			
			_canvasModel.selectedItem.activity.runOffline = runOffline_chk.selected;
			Debugger.log('_canvasModel.selectedItem.activity.runOffline:'+_canvasModel.selectedItem.activity.runOffline,Debugger.GEN,'click','PropertyInspector');
			_canvasModel.selectedItem.refresh(_canvasModel.selectedItem.activity.runOffline)
		}
		
		setModified();
	}
	
	
	/**
	 * Recieves the click events from the canvas views (inc Property Inspector) buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function focusOut(e):Void{
		var tgt:String = new String(e.target);
		if(tgt.indexOf("title_txt") != -1){
			//TODO: check if this is the right place to set edited content, should it be ddm?
			_canvasModel.selectedItem.activity.title = title_txt.text;
		} else if(tgt.indexOf("desc_txt") != -1){
			_canvasModel.selectedItem.activity.description = desc_txt.text;
		}

		_canvasModel.selectedItem.refresh();
		
		if (_canvasModel.selectedItem.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE) {
			_canvasModel.selectedItem.init();
		} else if (_canvasModel.selectedItem.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || _canvasModel.selectedItem.activity.activityTypeID == Activity.OPTIONS_WITH_SEQUENCES_TYPE) {
			//_canvasModel.selectedItem.updateChildren();
		} else if(_canvasModel.selectedItem instanceof BranchConnector) {
			if(_canvasModel.selectedItem.branch.isEnd)
				_canvasModel.branchesDisplayed.get(_canvasModel.getCanvas().ddm.getBranchesForActivityUIID(SequenceActivity(_canvasModel.selectedItem.branch.sequenceActivity).firstActivityUIID).target.branchUIID).updateBranchLabel();
			else
				_canvasModel.selectedItem.updateBranchLabel();
		}
		
		setModified();
		
	}
	
	/**
	* 
	* Invalidate design (set modified/unsaved flag) when Activity properties are modified
	* 
	*/
	private function setModified():Void{
		_canvasModel.getCanvas().ddm.validDesign = false;
		_canvasModel.getCanvas().checkValidDesign();
		
		ApplicationParent.extCall("setSaved", "false");
	}
	
	private function moveItem(item:Object, xDiff:Number, yDiff:Number) {
		item._x += xDiff;
		item._y += yDiff;
	}
	
}

