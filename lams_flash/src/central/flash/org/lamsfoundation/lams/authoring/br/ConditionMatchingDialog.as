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
* Tool-Output based Condition 
* Matching Dialog window
* 
* @author  Mitchell Seaton
*/
class ConditionMatchingDialog extends BranchMappingDialog {

    //References to components + clips 
    private var _branchingActivity:BranchingActivity;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    function ConditionMatchingDialog(){
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
        conditions_label.text = Dictionary.getValue('condmatch_dlg_cond_lst_lbl');
        branches_label.text = Dictionary.getValue('branch_mapping_dlg_branches_lst_lbl');
        match_dgd_lbl.text = Dictionary.getValue('branch_mapping_dlg_match_dgd_lbl');
		
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
		Debugger.log("Loading Lists: branch length: " + branches.length, Debugger.CRITICAL, "loadLists", "ConditionMatchingDialog");
		Debugger.log("Loading Lists: branching act: " + _branchingActivity.activityUIID, Debugger.CRITICAL, "loadLists", "ConditionMatchingDialog");
		
		conditions_lst.dataProvider = conditions;
		conditions_lst.sortItemsBy("conditionUIID", Array.NUMERIC);
		conditions_lst.labelField = "displayName";
		conditions_lst.hScrollPolicy = "on";
		conditions_lst.maxHPosition = 200;
		
		branches_lst.dataProvider = branches;
		branches_lst.labelField = "sequenceName";
		branches_lst.hScrollPolicy = "on";
		branches_lst.maxHPosition = 200;
		
		var column_sequence:DataGridColumn = new DataGridColumn("sequenceName");
		column_sequence.headerText = Dictionary.getValue("branch_mapping_dlg_branch_col_lbl");
		column_sequence.width = match_dgd.width/2;
		
		var column_condition:DataGridColumn = new DataGridColumn("displayName");
		column_condition.headerText = Dictionary.getValue("branch_mapping_dlg_condition_col_lbl");
		column_condition.width = match_dgd.width/2;
		
		match_dgd.addColumn(column_sequence);
		match_dgd.addColumn(column_condition);
		
		var mappings:Array = app.getCanvas().ddm.getBranchMappingsByActivityUIIDAndType(_branchingActivity.activityUIID).toolBased;
		
		Debugger.log("Loading Lists: mappings length: " + mappings.length, Debugger.CRITICAL, "loadLists", "ConditionMatchingDialog");
		
		for(var i=0; i < mappings.length; i++) {
			match_dgd.addItem(mappings[i]);
			removeCondition(mappings[i].condition);
		}
		
	}
	
	private function removeCondition(c:ToolOutputCondition) {
		var indexList:Array = new Array();
		
		for(var i=0; i<conditions_lst.length; i++) {
			var item = conditions_lst.getItemAt(i);
			if(item.conditionUIID == c.conditionUIID) { conditions_lst.removeItemAt(i); return; }
		}
	}
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object):Void{
        if(event.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','ConditionMatchingDialog');
        }
    }
    
    /**
    * Called by the OK button 
    */
    private function close(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','ConditionMatchingDialog');
        
		// check for any remaining conditions
		if(conditions_lst.length > 0) {
			LFMessage.showMessageAlert(Dictionary.getValue("branch_mapping_auto_condition_msg"), Proxy.create(this, cleanupUnmappedConditions));
		} else {
			//close popup
			_container.deletePopUp();
		}
		
		
    }
	
	private function cleanupUnmappedConditions(){
        Debugger.log("clearing all unmapped conditions: " + conditions_lst.length, Debugger.CRITICAL, "cleanupUnmappedConditions", "ConditionMatchingDialog");
		
		for(var i=0; i < conditions_lst.length; i++) {
			setupMatch(conditions_lst.getItemAt(i), _branchingActivity.defaultBranch);
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
    
	
	private function addMatch():Void {
		var selectedConditions:Array = new Array();
		
		// get selected items and put together in match
		if(conditions_lst.selectedItems.length > 0) {
		
			for(var i=0; i<conditions_lst.selectedIndices.length; i++) {
				if(branches_lst.selectedItem != null) {
					setupMatch(conditions_lst.getItemAt(conditions_lst.selectedIndices[i]), branches_lst.selectedItem);
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
	
	private function setupMatch(condition:ToolOutputCondition, branch:Branch):Void {
		Debugger.log("condition: " + condition + " branch: " + branch, Debugger.CRITICAL, "setupMatch", "ConditionMatchingDialog");
		 
		var toMatch:ToolOutputBranchActivityEntry = new ToolOutputBranchActivityEntry(null, app.getCanvas().ddm.newUIID(), condition, branch.sequenceActivity, _branchingActivity);
		match_dgd.addItem(toMatch);
		
		app.getCanvas().ddm.addBranchMapping(toMatch);
		
	}
	
	private function removeMatch():Void {
		
		var rItem:Object = match_dgd.selectedItem;
		
		if(rItem != null) {
			conditions_lst.addItem(rItem.condition);
			conditions_lst.sortItemsBy("conditionUIID", Array.NUMERIC);
			
			match_dgd.removeItemAt(match_dgd.selectedIndex);
			app.getCanvas().ddm.removeBranchMapping(rItem.entryUIID);
			
		} else {
			LFMessage.showMessageAlert(Dictionary.getValue("branch_mapping_no_branch_msg"));
		}
		
	}
	
	public function set conditions(a:Array){
		_primary = a;
	}
	
	public function set branches(a:Array){
		_secondary = a;
	}
	
	public function get conditions():Array{
		return _primary;
	}
	
	public function get branches():Array{
		return _secondary;
	}
	
	public function get conditions_lst():List {
		return primary_lst;
	}
	
	public function get conditions_label():Label {
		return primary_lst_lbl;
	}
	
	public function get branches_lst():List {
		return secondary_lst;
	}
	
	public function get branches_label():Label {
		return secondary_lst_lbl;
	}
	
	public function set branchingActivity(a:BranchingActivity) {
		_branchingActivity = a;
	}

}