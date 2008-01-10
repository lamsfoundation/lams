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
 
import org.lamsfoundation.lams.authoring.br.*;
import org.lamsfoundation.lams.authoring.Application;
import org.lamsfoundation.lams.authoring.BranchingActivity;
import org.lamsfoundation.lams.authoring.ToolActivity;
import org.lamsfoundation.lams.authoring.ToolOutputDefinition;
import org.lamsfoundation.lams.authoring.ToolOutputCondition;

import org.lamsfoundation.lams.common.Dialog;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.LFMessage;

import mx.controls.*
import mx.controls.gridclasses.DataGridColumn;
import mx.utils.*
import mx.managers.*
import mx.events.*

/**
 *
 * @author Mitchell Seaton
 * @version 2.1
 **/
class ToolOutputConditionsDialog extends MovieClip implements Dialog {
	private static var STP_MAX:Number = 9999;
	private static var STP_MIN:Number = 0;
	
	private static var OPTION_GTE:Number = 0;
	private static var OPTION_LTE:Number = 1;
	private static var OPTION_RANGE:Number = 2;
	
	private static var TOOL_OUTPUTS_URL_POSTFIX:String = "outputs";
	
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    
	private var _definitions:Array;
	private var _conditions:Array;
	
	private var _selectedDefinition:ToolOutputDefinition;
	private var _selectedIndex:Number;
	
	private var _selectedOption:Object;
	private var _selectedOptionIndex:Number;
	
	private var _toolContentID:Number;
	
	private var _toolOutputDefin_cmb:ComboBox;
	private var _toolOutputLongOptions_cmb:ComboBox;
	
	private var _condition_item_dgd:DataGrid;
	
	private var _start_value_stp:NumericStepper;
	private var _end_value_stp:NumericStepper;
	
	private var add_btn:Button;
	private var close_btn:Button;
	private var cancel_btn:Button;
	private var remove_item_btn:Button;
	private var clear_all_btn:Button;
	private var help_btn:Button;
	
	private var _condition_from_lbl:Label;
	private var _condition_to_lbl:Label;
	
	private var _bgpanel:MovieClip;       //The underlaying panel base
    
    private var app:Application;
	private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
	
	private var _itemCount:Number;
	
	private var _branchingActivity:BranchingActivity;
	private var _toolActivity:ToolActivity;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
	function ToolOutputConditionsDialog(){
		_itemCount = 0;
		this._visible = false;
		
		app = Application.getInstance();
		
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
        
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
		//EVENTS
        //Add event listeners for buttons
        close_btn.addEventListener('click', Delegate.create(this, close));
        cancel_btn.addEventListener('click', Delegate.create(this, cancel));
		
		add_btn.addEventListener('click',Delegate.create(this, addButton_onPress));
		help_btn.addEventListener('click',Delegate.create(this, helpButton_onPress));
		remove_item_btn.addEventListener('click', Delegate.create(this, removeItemButton_onPress));
		clear_all_btn.addEventListener('click', Delegate.create(this, clearAllButton_onPress));
		
		_toolOutputDefin_cmb.addEventListener('change', Delegate.create(this, itemChanged));
		_toolOutputLongOptions_cmb.addEventListener('change', Delegate.create(this, optionChanged));
		
		_condition_item_dgd.addEventListener('cellEdit', Delegate.create(this, itemEdited));
		_condition_item_dgd.addEventListener('cellFocusIn', Delegate.create(this, itemSelected));
		
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
		//work out offsets from bottom RHS of panel
        xOkOffset = _bgpanel._width - close_btn._x;
        yOkOffset = _bgpanel._height - close_btn._y;
		
		 //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
		
		setLabels();
        setStyles();
		
        //fire event to say we have loaded
		_container.contentLoaded();
	}

	/**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(evt:Object):Void{
        if(evt.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }
	
	private function setLabels(){
		_condition_from_lbl.text = Dictionary.getValue("to_conditions_dlg_from_lbl");
		_condition_to_lbl.text = Dictionary.getValue("to_conditions_dlg_to_lbl");
		
		add_btn.label = Dictionary.getValue("to_conditions_dlg_add_btn_lbl");
		help_btn.label = Dictionary.getValue("to_conditions_dlg_help_btn_lbl");
		clear_all_btn.label = Dictionary.getValue("to_conditions_dlg_clear_all_btn_lbl");
		remove_item_btn.label = Dictionary.getValue("to_conditions_dlg_remove_item_btn_lbl");
	
		//Set the text for buttons
        close_btn.label = Dictionary.getValue('al_done');
		cancel_btn.label = Dictionary.getValue('al_cancel');
	}
	
	/**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName', styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        close_btn.setStyle('styleName', styleObj);
		cancel_btn.setStyle('styleName', styleObj);
		
		add_btn.setStyle('styleName', styleObj);
		help_btn.setStyle('styleName', styleObj);
		remove_item_btn.setStyle('styleName', styleObj);
		clear_all_btn.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
		_condition_from_lbl.setStyle('styleName', styleObj);
		_condition_to_lbl.setStyle('styleName', styleObj);
		
		styleObj = themeManager.getStyleObject('picombo');
		_toolOutputDefin_cmb.setStyle('styleName', styleObj);
		_toolOutputLongOptions_cmb.setStyle('styleName', styleObj);
		
		styleObj = themeManager.getStyleObject('numericstepper');
		_start_value_stp.setStyle('styleName', styleObj);
		_end_value_stp.setStyle('styleName', styleObj);
    }
	
	private function showElements(b:Boolean):Void {
		_toolOutputLongOptions_cmb.visible = b;
	
		_condition_item_dgd.visible = b;
	
		_start_value_stp.visible = b;
		_end_value_stp.visible = b;
	
		add_btn.visible = b;
		remove_item_btn.visible = b;
		clear_all_btn.visible = b;
	
		_condition_from_lbl.visible = b;
		_condition_to_lbl.visible = b;
	}
	
	public function setupContent():Void {
		_toolOutputLongOptions_cmb.addItem({label: Dictionary.getValue("to_conditions_dlg_options_item_header_lbl"), data: null});
		_toolOutputLongOptions_cmb.addItem({label: Dictionary.getValue("to_conditions_dlg_gte_lbl"), data: OPTION_GTE});
		_toolOutputLongOptions_cmb.addItem({label: Dictionary.getValue("to_conditions_dlg_lte_lbl"), data: OPTION_LTE});
		_toolOutputLongOptions_cmb.addItem({label: Dictionary.getValue("to_conditions_dlg_range_lbl"), data: OPTION_RANGE});
		
		var column_name:DataGridColumn = new DataGridColumn("conditionName");
		column_name.headerText = Dictionary.getValue("to_conditions_dlg_condition_items_name_col_lbl");
		column_name.editable = true;
		column_name.width = _condition_item_dgd.width*0.4;
		
		var column_value:DataGridColumn = new DataGridColumn("conditionValue");
		column_value.headerText = Dictionary.getValue("to_conditions_dlg_condition_items_value_col_lbl");
		column_value.editable = false;
		column_value.width = _condition_item_dgd.width*0.6;
		
		_condition_item_dgd.addColumn(column_name);
		_condition_item_dgd.addColumn(column_value);
		
		showElements(false);
		
		// wait second frame for steppers to be setup
		this.onEnterFrame = initSetup;
		
	}
	
	private function initSetup():Void {
		delete this.onEnterFrame;
		
		var branches:Array = app.getCanvas().ddm.getBranchMappingsByActivityUIIDAndType(_branchingActivity.activityUIID).toolBased;
		Debugger.log("branches length: " + branches.length, Debugger.CRITICAL, "initSetup", "ToolOutputConditionsDialog");
		
		for(var i=0; i<branches.length; i++) {
			
			if(branches[i].condition.toolActivity.activityUIID == _toolActivity.activityUIID && 
				branches[i].condition.branchingActivity.activityUIID == _branchingActivity.activityUIID) {
					
				Debugger.log("moving condition: " + branches[i].condition.type, Debugger.CRITICAL, "initSetup", "ToolOutputConditionsDialog");
			
				addCondition(branches[i].condition);
				if(_condition_item_dgd.length==1) setDefinition(branches[i].condition.name);
			}
		}

		itemChanged(null);
		
		this._visible = true;
	}
	
	private function setDefinition(name:String):Void {
		var items:Array = _toolOutputDefin_cmb.dataProvider;
		
		for(var i=0; i < items.length; i++)
			if(items[i].name == name)
				_toolOutputDefin_cmb.selectedIndex = i;
	}
	
	private function addButton_onPress():Void {
		if(validateCondition(_selectedDefinition))
			if(_toolOutputLongOptions_cmb.selectedItem.data == OPTION_GTE) 
				addCondition(ToolOutputCondition.createLongCondition(app.getCanvas().ddm.newUIID(), Dictionary.getValue("to_condition_untitled_item_lbl", [Number(_condition_item_dgd.length+1)]), _selectedDefinition, _toolActivity, _branchingActivity, _start_value_stp.value, null));
			else if(_toolOutputLongOptions_cmb.selectedItem.data == OPTION_LTE)
				addCondition(ToolOutputCondition.createLongCondition(app.getCanvas().ddm.newUIID(), Dictionary.getValue("to_condition_untitled_item_lbl", [Number(_condition_item_dgd.length+1)]), _selectedDefinition, _toolActivity, _branchingActivity, null, _start_value_stp.value));
			else 
				addCondition(ToolOutputCondition.createLongCondition(app.getCanvas().ddm.newUIID(), Dictionary.getValue("to_condition_untitled_item_lbl", [Number(_condition_item_dgd.length+1)]), _selectedDefinition, _toolActivity, _branchingActivity, _start_value_stp.value, _end_value_stp.value));
	}
	
	private function helpButton_onPress():Void {
		var url:String = _toolActivity.helpURL + TOOL_OUTPUTS_URL_POSTFIX;
		JsPopup.getInstance().launchPopupWindow(url, 'ToolActivityOutputsHelp_' + _toolActivity.activityUIID, 600, 800, true, true, false, false, false);
	}
	
	private function clearAllButton_onPress(evt:Object, c:Boolean):Void {
		if(!app.getCanvas().ddm.hasBranchMappingsForConditionSet(_condition_item_dgd.dataProvider) || c) {
			Debugger.log("length: " + _condition_item_dgd.dataProvider.length, Debugger.CRITICAL, "clearAllButton_onPress", "ToolOutputConditionsDialog");
			
			for(var i=0; i<=_condition_item_dgd.dataProvider.length; i++) {
				var conditionUIID:Number = ToolOutputCondition(_condition_item_dgd.dataProvider[i].data).conditionUIID;
				
				if(c) {
					app.getCanvas().ddm.removeBranchMappingsByCondition(conditionUIID);
				}
				
			}
			
			app.getCanvas().ddm.conditions.clear();
			_condition_item_dgd.removeAll();

		} else {
			LFMessage.showMessageConfirm(Dictionary.getValue("branch_mapping_dlg_condition_linked_msg", [Dictionary.getValue("branch_mapping_dlg_condition_linked_all")]), Proxy.create(this, clearAllButton_onPress, evt, true), Proxy.create(this, returnDefinitionState), Dictionary.getValue("al_continue"), null);
		}
		
	}
	
	private function removeItemButton_onPress(evt:Object, c:Boolean):Void {
		var _selectedItem:ToolOutputCondition = ToolOutputCondition(_condition_item_dgd.dataProvider[_condition_item_dgd.selectedIndex].data);
		
		if(!app.getCanvas().ddm.hasBranchMappingsForCondition(_selectedItem.conditionUIID) || c) {
			removeCondition(_selectedItem.conditionUIID, _condition_item_dgd.selectedIndex);
		} else {
			LFMessage.showMessageConfirm(Dictionary.getValue("branch_mapping_dlg_condition_linked_msg", [Dictionary.getValue("branch_mapping_dlg_condition_linked_single")]), Proxy.create(this, removeItemButton_onPress, evt, true), null, "continue", null, "");
		}
		
		if(c)
			app.getCanvas().ddm.removeBranchMappingsByCondition(_selectedItem.conditionUIID);
		
	}
	
	/**
	 * Called by ADD button
	 * 
	 * @usage   
	 * @return  
	 */
	
	private function addCondition(condition:ToolOutputCondition):Void {
		
		switch(condition.type) {
			case ToolOutputDefinition.LONG :
				if(condition.startValue != null && condition.endValue != null)
					_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: Dictionary.getValue("branch_mapping_dlg_condition_col_value", [String(condition.startValue), String(condition.endValue)]), data: condition});
				else if(condition.startValue != null && condition.endValue == null)
					_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: Dictionary.getValue("branch_mapping_dlg_condition_col_value_max", [String(condition.startValue)]), data: condition});
				else if(condition.startValue == null && condition.endValue != null)
					_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: Dictionary.getValue("branch_mapping_dlg_condition_col_value_min", [String(condition.endValue)]), data: condition});
				else
					_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: Dictionary.getValue("branch_mapping_dlg_condition_col_value_exact", [String(condition.exactMatchValue)]), data: condition});
				
				break;
			case ToolOutputDefinition.BOOL: 
				_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: String(condition.exactMatchValue), data: condition});
				break;
			default: 
				Debugger.log("No type found", Debugger.GEN, "addCondition", "ToolOutputConditionsDialog");
		}
		
		app.getCanvas().ddm.addOutputCondition(condition);
		
	}
	
	private function removeCondition(conditionUIID:Number, index:Number):Void {
		app.getCanvas().ddm.removeOutputCondition(conditionUIID);
		_condition_item_dgd.removeItemAt(index);
	}
	
	private function removeAllItems(c:Boolean):Void {
		clearAllButton_onPress(null, true);
		if(c) selectDefinition();
	}
	
	private function validateCondition(selectedDefinition:ToolOutputDefinition):Boolean {
		
		switch(selectedDefinition.type) {
			case ToolOutputDefinition.LONG:
				if(_toolOutputLongOptions_cmb.selectedItem.data == OPTION_GTE) {
					return validateLongCondition(_start_value_stp.value, null);
				} else if(_toolOutputLongOptions_cmb.selectedItem.data == OPTION_LTE) {
					return validateLongCondition(null, _start_value_stp.value);
				} else {
					return validateLongCondition(_start_value_stp.value, _end_value_stp.value);
				}
				break;
			case ToolOutputDefinition.BOOL:
				return true;
			default: 
				return false;
		}
	
	}
	
	private function validateLongCondition(start_value:Number, end_value:Number) {
		Debugger.log("validating Long Condition", Debugger.CRITICAL, "validateLongCondition", "ToolOutputconditiosDialog")
		
		if(start_value > end_value) {
			LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_direction", [Dictionary.getValue("to_condition_start_value"), Dictionary.getValue("to_condition_end_value")]), null);
			return false;
		}
		
		// TODO: Update error messages to be more meaningful
		for(var i=0; i<_condition_item_dgd.dataProvider.length; i++) {
			var condition:ToolOutputCondition = ToolOutputCondition(_condition_item_dgd.dataProvider[i].data);
			
			if(condition.exactMatchValue != null) {
				if(start_value == condition.exactMatchValue) {
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_start_value")]), null);
					return false;
				} else if(end_value == condition.exactMatchValue) {
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_end_value")]), null);
					return false;
				} else if(start_value < condition.exactMatchValue && end_value == null) {
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_end_value")]), null);
					return false;
				} else if(end_value > condition.exactMatchValue && start_value == null) {
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_end_value")]), null);
					return false;
				}
			} else { 
				if(start_value >= condition.startValue && start_value <= condition.endValue && start_value != null) {
					Debugger.log("1", Debugger.CRITICAL, "validateLongCondition", "ToolOutputConditionsDialog");
		
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_start_value")]), null);
					return false;
				} else if(end_value >= condition.startValue && end_value <= condition.endValue && end_value != null) {
					Debugger.log("2", Debugger.CRITICAL, "validateLongCondition", "ToolOutputConditionsDialog");
		
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_end_value")]), null);
					return false;
				} else if(end_value >= condition.endValue && start_value == null && condition.endValue != null) {
					Debugger.log("3", Debugger.CRITICAL, "validateLongCondition", "ToolOutputConditionsDialog");
		
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_start_value")]), null);
					return false;
				} else if(start_value <= condition.startValue && end_value == null && condition.startValue != null) {
					Debugger.log("4", Debugger.CRITICAL, "validateLongCondition", "ToolOutputConditionsDialog");
		
					LFMessage.showMessageAlert(Dictionary.getValue("to_condition_invalid_value_range", [Dictionary.getValue("to_condition_end_value")]), null);
					return false;
				}
			}
		}
			
		return true;
	}
	
	private function itemChanged(evt:Object):Void {
		Debugger.log("type: " + _selectedDefinition.type, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		Debugger.log("index: " + _toolOutputDefin_cmb.selectedIndex, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		
		if(_selectedIndex == _toolOutputDefin_cmb.selectedIndex)
			return;
		
		Debugger.log("has mappings: " + app.getCanvas().ddm.hasBranchMappingsForConditionSet(_condition_item_dgd.dataProvider), Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		Debugger.log("dp length: " + _condition_item_dgd.dataProvider.length, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		
		if(app.getCanvas().ddm.hasBranchMappingsForConditionSet(_condition_item_dgd.dataProvider) && evt != null) {
			LFMessage.showMessageConfirm(Dictionary.getValue("branch_mapping_dlg_condition_linked_msg", [Dictionary.getValue("branch_mapping_dlg_condition_linked_all")]), Proxy.create(this, removeAllItems, true), Proxy.create(this, returnDefinitionState), Dictionary.getValue("al_continue"), null);
		} else if(evt != null) removeAllItems(true);
		else selectDefinition();
			
	}
	
	
	private function optionChanged(evt:Object):Void {
		Debugger.log("index: " + _toolOutputLongOptions_cmb.selectedIndex, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		
		if(_selectedOptionIndex == _toolOutputLongOptions_cmb.selectedIndex && _selectedOptionIndex != null)
			return;
		
		selectOption();
	}
	
	private function selectOption():Void {
		_selectedOption = _toolOutputLongOptions_cmb.dataProvider[_toolOutputLongOptions_cmb.selectedIndex];
		
		add_btn.enabled = true;
				
		if(_selectedOption.data == OPTION_GTE) {
			showSteppers(true, false);
		} else if(_selectedOption.data == OPTION_LTE) {
			showSteppers(false, true);
		} else if(_selectedOption.data == OPTION_RANGE) {
			showSteppers(true, true);
		} else {
			showSteppers(false, false);
			add_btn.enabled = false;
		}
		
		_selectedOptionIndex = _toolOutputLongOptions_cmb.selectedIndex;
	}
	
	private function selectDefinition():Void {
		var ddm = app.getCanvas().ddm;
		
		_selectedDefinition = _toolOutputDefin_cmb.dataProvider[_toolOutputDefin_cmb.selectedIndex];
		Debugger.log("select definition: " + _selectedDefinition.description, Debugger.CRITICAL, "selectDefinition", "ToolOutputConditionsDialog");
		
		switch(_selectedDefinition.type) {
			case ToolOutputDefinition.LONG:
				_condition_item_dgd.visible = true;
				_toolOutputLongOptions_cmb.visible = true;
				
				add_btn.visible = true;
				optionChanged();
				
				_start_value_stp.minimum = (_selectedDefinition.startValue != null) ? Number(_selectedDefinition.startValue) : STP_MIN;
				_end_value_stp.minimum = (_selectedDefinition.startValue != null) ? Number(_selectedDefinition.startValue) : STP_MIN;
				_start_value_stp.maximum = (_selectedDefinition.endValue != null) ? Number(_selectedDefinition.endValue) : STP_MAX;
				_end_value_stp.maximum = (_selectedDefinition.endValue != null) ? Number(_selectedDefinition.endValue) : STP_MAX;
				
				_start_value_stp.value = (_selectedDefinition.startValue != null) ? Number(_selectedDefinition.startValue) : STP_MIN;
				_end_value_stp.value = (_selectedDefinition.endValue != null) ? Number(_selectedDefinition.endValue) : STP_MIN;
				
				
				clear_all_btn.visible = true;
				remove_item_btn.visible = true;
				
				break;
			
			case ToolOutputDefinition.BOOL:
				_condition_item_dgd.visible = true;
				_toolOutputLongOptions_cmb.visible = false;
				
				add_btn.visible = false;
				showSteppers(false, false);
				
				if(_condition_item_dgd.dataProvider.length <= 0) {
					addCondition(ToolOutputCondition.createBoolCondition(ddm.newUIID(), _selectedDefinition, _toolActivity, _branchingActivity, true));
					addCondition(ToolOutputCondition.createBoolCondition(ddm.newUIID(), _selectedDefinition, _toolActivity, _branchingActivity, false));
				}
				
				break;
			default:
				showElements(false);
				_toolOutputLongOptions_cmb.selectedIndex = 0;
				Debugger.log("type not found", Debugger.GEN, "itemChanged", "ToolOutputConditionsDialog");
				
		}
		
		_selectedIndex = _toolOutputDefin_cmb.selectedIndex;
		
	}
	
	private function returnDefinitionState() {
		_toolOutputDefin_cmb.selectedIndex = _selectedIndex;
		_selectedDefinition = _toolOutputDefin_cmb.dataProvider[_selectedIndex];
	}
	
	private function itemEdited(evt:Object):Void {
		var item = _condition_item_dgd.getItemAt(evt.itemIndex);
		var condition:ToolOutputCondition = ToolOutputCondition(item.data);
		condition.displayName = item.conditionName;
		
		app.getCanvas().ddm.addOutputCondition(condition);
		
	}
	
	public function itemSelected(evt:Object):Void {
		var item = _condition_item_dgd.getItemAt(evt.itemIndex);
	}
	
	private function showSteppers(a:Boolean, b:Boolean):Void {
		repositionSteppers(a,b);
		
		_start_value_stp.visible = a;
		_condition_from_lbl.visible = a;
		_end_value_stp.visible = b;
		_condition_to_lbl.visible = b;
		
	}
	
	private function repositionSteppers(a:Boolean, b:Boolean):Void {
		if(a&&b) {
			_condition_from_lbl._x = 14;
			_start_value_stp._x = _condition_from_lbl._x + _condition_from_lbl._width + 5;
			_condition_to_lbl._x = _start_value_stp._x + _start_value_stp._width + 20;
			_end_value_stp._x = _condition_to_lbl._x + _condition_to_lbl._width + 5;
		} else if(a&&!b) {
			_condition_from_lbl._x = 14;
			_start_value_stp._x = _condition_from_lbl._x + _condition_from_lbl._width + 5;
		} else if(!a&&b) {
			_condition_to_lbl._x = 14;
			_end_value_stp._x = _condition_to_lbl._x + _condition_to_lbl._width + 5;
		}
	}

	public static function getOutputType(type:String):String {
		switch(type) {
			case ToolOutputDefinition.BOOL:
				return Dictionary.getValue("to_conditions_dlg_defin_bool_type");
				break;
			case ToolOutputDefinition.LONG:
				return Dictionary.getValue("to_conditions_dlg_defin_long_type");
				break;
			default:
				return "";
		}
	}
	
    /**
    * Called by the CLOSE button 
    */
    private function close(){
		app.pi.openConditionMatchDialog();
		 
        _container.deletePopUp();
    }
	
	private function cancel(){
		// remove any entries
		app.getCanvas().ddm.conditions.clear();
		_condition_item_dgd.removeAll();
		
        _container.deletePopUp();
	}
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
		close();
    }
    
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
		//Size the panel
        _bgpanel.setSize(w,h);

		_condition_item_dgd.setSize(w - 2*_condition_item_dgd._x, h*0.45);
		_condition_item_dgd.getColumnAt(0).width = _condition_item_dgd.width*0.4;
        _condition_item_dgd.getColumnAt(1).width = _condition_item_dgd.width*0.6;
        
		_toolOutputDefin_cmb.setSize(w - 2*_toolOutputDefin_cmb._x - help_btn.width - 5, 22);
		_toolOutputLongOptions_cmb.setSize(_toolOutputDefin_cmb._width, 22);
		
		//Buttons
		add_btn._x = _toolOutputLongOptions_cmb._x + _toolOutputLongOptions_cmb._width - add_btn.width;
		remove_item_btn.move(_condition_item_dgd._x + _condition_item_dgd._width - remove_item_btn.width, _condition_item_dgd._y + _condition_item_dgd._height + 5);
		clear_all_btn.move(remove_item_btn._x - clear_all_btn.width - 5, remove_item_btn._y);
		
		help_btn._x = w - _toolOutputDefin_cmb._x - help_btn.width;
        
		close_btn.move(w-xOkOffset,h-yOkOffset);
		cancel_btn.move(close_btn._x - cancel_btn.width - 5, h-yOkOffset);
		
    }
    
    //Gets+Sets
    
	public function set definitions(a:Array):Void {
		_definitions = new Array();
		
		_definitions.push(new ToolOutputDefinition());
		
		for(var i=0; i< a.length; i++) {
			var newTOD:ToolOutputDefinition = new ToolOutputDefinition();
			newTOD.populateFromDTO(a[i]);
			
			_definitions.push(newTOD);
		}
		
		_toolOutputDefin_cmb.dataProvider = _definitions;
		_toolOutputDefin_cmb.labelFunction = function(itemObj) {
			
			return (itemObj.type == null) ? Dictionary.getValue("to_conditions_dlg_defin_item_header_lbl") : Dictionary.getValue("to_conditions_dlg_defin_item_fn_lbl", [itemObj.description, ToolOutputConditionsDialog.getOutputType(itemObj.type)]);
		}
		
		_toolOutputDefin_cmb.redraw(true);

	}
	
	public function get definitions():Array {
		return _definitions;
	}
	
	public function get toolContentID():Number {
		return _toolContentID;
	}
	
	public function set toolContentID(a:Number):Void {
		_toolContentID = a;
	}
	
	/**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
	public function set branchingActivity(a:BranchingActivity) {
		_branchingActivity = a;
	}
	
	public function set toolActivity(a:ToolActivity) {
		_toolActivity = a;
		definitions = _toolActivity.definitions;
	}

}