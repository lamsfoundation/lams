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


import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*

import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.br.*

import mx.controls.*
import mx.controls.gridclasses.DataGridColumn;
import mx.utils.*
import mx.managers.*
import mx.events.*

/*
* Condition (Tool-Output) based Gates
* Matching Dialog window
* 
* @author  Daniel Carlier
* @version 2.1.1
*/
class GateConditionMatchingDialog extends BranchMappingDialog {

    //References to components + clips 
    private var _gateActivity:GateActivity;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    function GateConditionMatchingDialog(){
        super();
		
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
        //Set the labels
        primary_lst_lbl.text = Dictionary.getValue("branch_mapping_dlg_condition_col_lbl");
        secondary_lst_lbl.text = Dictionary.getValue("gate_btn");
        match_dgd_lbl.text = Dictionary.getValue("branch_mapping_dlg_match_dgd_lbl");
		
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('click',Delegate.create(this, close));
        add_match_btn.addEventListener('click',Delegate.create(this, addMatch));
		remove_match_btn.addEventListener('click',Delegate.create(this, removeMatch));
        
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
		//fire event to say we have loaded
		_container.contentLoaded();
	}
	
	public function loadLists() {
		Debugger.log("GateConditionMatchingDialog->Loading Lists", Debugger.CRITICAL, "loadLists", "GateConditionMatchingDialog");
		Debugger.log("Loading Lists: conditions length: " + conditions.length, Debugger.CRITICAL, "loadLists", "GateConditionMatchingDialog");
		
		conditions_lst.dataProvider = conditions;
		conditions_lst.sortItemsBy("conditionUIID", Array.NUMERIC);
		conditions_lst.labelField = "displayName";
		conditions_lst.hScrollPolicy = "on";
		conditions_lst.maxHPosition = 200;
		
		var gateStates:Array = new Array();
		var openState:ObjState = new ObjState(Dictionary.getValue("gate_open"), "open");
		var closedState:ObjState = new ObjState(Dictionary.getValue("gate_closed")+" ("+Dictionary.getValue('pi_defaultBranch_cb_lbl')+")", "closed");
		
		gateStates.push(openState);
		gateStates.push(closedState);
		
		gate_lst.dataProvider = gateStates;
		gate_lst.labelField = "displayText";
		gate_lst.hScrollPolicy = "on";
		gate_lst.maxHPosition = 200;
		
		var column_condition:DataGridColumn = new DataGridColumn("displayName");
		column_condition.headerText = Dictionary.getValue("branch_mapping_dlg_condition_col_lbl");
		column_condition.width = match_dgd.width/2;
		
		var column_gate:DataGridColumn = new DataGridColumn("gateStateDisplayText");
		column_gate.headerText = "Gate";
		column_gate.width = match_dgd.width/2;
		match_dgd.addColumn(column_condition);
		match_dgd.addColumn(column_gate);
		
		var mappings:Array = app.getCanvas().ddm.getBranchMappingsByActivityUIIDAndType(gateActivity.activityUIID).toolBased;
		
		Debugger.log("GateConditionMatchingDialog->loadLists->mappings.length: "+mappings.length, Debugger.CRITICAL, "loadLists", "GateConditionMatchingDialog");
		
		for(var i=0; i < mappings.length; i++) {
			// mappings[i].condition.gateActivity won't exist for normal branchMappings, hence the check will fail for that case
			if(mappings[i].condition.toolActivity.activityUIID == _gateActivity.toolActivityUIID &&
				mappings[i].condition.gateActivity.activityUIID == _gateActivity.activityUIID) {
				match_dgd.addItem(mappings[i]);
				removeCondition(mappings[i].condition);
			}
		}
	}
   
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object):Void{
        if(event.type=='themeChanged') {
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','ConditionMatchingDialog');
        }
    }
	
	private function removeCondition(c:ToolOutputCondition) {
		var indexList:Array = new Array();
		
		for(var i=0; i<conditions_lst.length; i++) {
			var item = conditions_lst.getItemAt(i);
			if(item.conditionUIID == c.conditionUIID) { conditions_lst.removeItemAt(i); return; }
		}
	}
	
	private function addMatch():Void {
		var selectedConditions:Array = new Array();
		
		// get selected items and put together in match
		if(conditions_lst.selectedItems.length > 0) {
		
			for(var i=0; i<conditions_lst.selectedIndices.length; i++) {
				if(gate_lst.selectedItem != null) {
					setupMatch(conditions_lst.getItemAt(conditions_lst.selectedIndices[i]), gate_lst.selectedItem);
					selectedConditions.push(conditions_lst.selectedIndices[i]);
				} else {
					LFMessage.showMessageAlert(Dictionary.getValue("branch_mapping_no_branch_msg"));
					return;
				}
				
			}
			var delCount = 0;
			for(var i=0; i<selectedConditions.length; i++) {
				conditions_lst.removeItemAt(selectedConditions[i]-delCount);
				delCount++;
			}
			
			
		} else {
			LFMessage.showMessageAlert(Dictionary.getValue("branch_mapping_no_condition_msg"));
		}
	}
	
	private function removeMatch():Void {
		
		var rItem:Object = match_dgd.selectedItem;
		
		if(rItem != null) {
			conditions_lst.addItem(rItem.condition);
			conditions_lst.sortItemsBy("conditionUIID", Array.NUMERIC);
			
			match_dgd.removeItemAt(match_dgd.selectedIndex);
			app.getCanvas().ddm.removeBranchMapping(rItem.entryUIID);
			
		} else {
			LFMessage.showMessageAlert(Dictionary.getValue("branch_mapping_no_condition_msg"));
		}
		
	}
	
	private function setupMatch(condition:ToolOutputCondition, gateState):Void {
		
		var gateOpenWhenConditionMet:Boolean = (gateState.value == "open") ? true : false;
		var toMatch:ToolOutputGateActivityEntry = new ToolOutputGateActivityEntry(null, app.getCanvas().ddm.newUIID(), gateActivity, condition, gateOpenWhenConditionMet);
		
		match_dgd.addItem(toMatch);

		app.getCanvas().ddm.addBranchMapping(toMatch);
	}
	
	/**
    * Called by the OK button 
    */
    private function close(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','GateConditionMatchingDialog');
        
		if(conditions_lst.length > 0) {
			LFMessage.showMessageAlert(Dictionary.getValue("gate_mapping_auto_condition_msg"), Proxy.create(this, cleanupUnmappedConditions));
		} else {
			_container.deletePopUp();
		}
    }
	
	private function cleanupUnmappedConditions(){
        Debugger.log("clearing all unmapped conditions: " + conditions_lst.length, Debugger.CRITICAL, "cleanupUnmappedConditions", "GateConditionMatchingDialog");
		
		for(var i=0; i < conditions_lst.length; i++) {
			setupMatch(conditions_lst.getItemAt(i), new ObjState("closed"));
		}
		
		conditions_lst.removeAll();
		app.getCanvas().ddm.conditions.clear();
		
		close();
	}
	
	/**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        close();
    }
	
	public function set conditions(a:Array){
		_primary = a;
	}
	
	public function get conditions():Array{
		return _primary;
	}
	
	public function get conditions_lst():List {
		return primary_lst;
	}
	
	public function get conditions_label():Label {
		return primary_lst_lbl;
	}
	
	public function get gate_lst():List {
		return secondary_lst;
	}
	
	public function get gate_label():Label {
		return secondary_lst_lbl;
	}
	
	public function set gateActivity(a:GateActivity) {
		_gateActivity = a;
	}
	
	public function get gateActivity():GateActivity {
		return _gateActivity;
	}

}