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
import org.lamsfoundation.lams.authoring.ToolOutputDefinition;
import org.lamsfoundation.lams.authoring.ToolOutputCondition;
import org.lamsfoundation.lams.authoring.Application;

import org.lamsfoundation.lams.common.Dialog;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.dict.*;
import org.lamsfoundation.lams.common.util.*;

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
	//References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    
	private var _definitions:Array;
	private var _conditions:Array;
	private var _selectedDefinition:ToolOutputDefinition;
	private var _toolContentID:Number;
	
	private var _toolOutputDefin_cmb:ComboBox;
	private var _condition_item_dgd:DataGrid;
	
	private var _start_value_stp:NumericStepper;
	private var _end_value_stp:NumericStepper;
	
	private var add_btn:Button;
	private var close_btn:Button;
	private var cancel_btn:Button;
	private var remove_item_btn:Button;
	private var clear_all_btn:Button;
	
	private var _output_type_lbl:Label;
	private var _condition_range_lbl:Label;
	
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
		remove_item_btn.addEventListener('click', Delegate.create(this, removeItemButton_onPress));
		clear_all_btn.addEventListener('click', Delegate.create(this, clearAllButton_onPress));
		
		_toolOutputDefin_cmb.addEventListener('change', Delegate.create(this, itemChanged));
		
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
		_condition_range_lbl.text = "Range:";
		_output_type_lbl.text = "Output Type:";
		
		//Set the text for buttons
        close_btn.label = Dictionary.getValue('al_done');
		cancel_btn.label = Dictionary.getValue('al_cancel');
		
		add_btn.label = "+ Add";
		clear_all_btn.label = "Clear All";
		remove_item_btn.label = "- Remove";
		
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
		remove_item_btn.setStyle('styleName', styleObj);
		clear_all_btn.setStyle('styleName', styleObj);
       
		styleObj = themeManager.getStyleObject('CanvasPanel');
		_bgpanel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
		_condition_range_lbl.setStyle('styleName', styleObj);
		_output_type_lbl.setStyle('styleName', styleObj);
		
		styleObj = themeManager.getStyleObject('picombo');
		_toolOutputDefin_cmb.setStyle('styleName', styleObj);
		
		styleObj = themeManager.getStyleObject('numericstepper');
		_start_value_stp.setStyle('styleName', styleObj);
		_end_value_stp.setStyle('styleName', styleObj);
    }
	
	public function setupContent():Void {
		
		var column_name:DataGridColumn = new DataGridColumn("conditionName");
		column_name.headerText = "Name";
		column_name.editable = true;
		
		var column_value:DataGridColumn = new DataGridColumn("conditionValue");
		column_value.headerText = "Condition";
		column_value.editable = false;
		
		_condition_item_dgd.addColumn(column_name);
		_condition_item_dgd.addColumn(column_value);
		
		// wait second frame for steppers to be setup
		
		this.onEnterFrame = initSetup;
		
	}
	
	private function initSetup():Void {
		delete this.onEnterFrame;
		
		// get branch conditions by branch activity uiid from ddm
		
		// selected definition and add items to list that already exist in ddm for this matchup
		
		// else no items do normal startup
		itemChanged();
		
		this._visible = true;
	}
	
	private function addButton_onPress():Void {
		addCondition(ToolOutputCondition.createLongCondition(app.getCanvas().ddm.newUIID(), "unnamed", _selectedDefinition, _start_value_stp.value, _end_value_stp.value));
	}
	
	private function clearAllButton_onPress():Void {
		_condition_item_dgd.removeAll();
	}
	
	private function removeItemButton_onPress():Void {
		var selectedItem = _condition_item_dgd.getItemAt(_condition_item_dgd.selectedIndex);
		app.getCanvas().ddm.removeOutputCondition(selectedItem.conditionUIID);
		
		_condition_item_dgd.removeItemAt(_condition_item_dgd.selectedIndex);
	}
	
	/**
	 * Called by ADD button
	 * 
	 * @usage   
	 * @return  
	 */
	
	private function addCondition(condition:ToolOutputCondition):Void {
		if(_condition_item_dgd.getItemAt(0).data == null)
			_condition_item_dgd.removeItemAt(0);
		
		switch(condition.type) {
			case ToolOutputDefinition.LONG :
				if(condition.startValue != null && condition.endValue != null)
					_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: String(condition.startValue) + " TO " + String(condition.endValue), data: condition});
				else
					_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: "exact(" + String(condition.exactMatchValue) + ")", data: condition});
				
				break;
			case ToolOutputDefinition.BOOL: 
				_condition_item_dgd.addItem({conditionName: condition.displayName, conditionValue: String(condition.exactMatchValue), data: condition});
				break;
			default: 
				Debugger.log("No type found", Debugger.GEN, "addCondition", "ToolOutputConditionsDialog");
		}
		
		app.getCanvas().ddm.addOutputCondition(condition);
		
	}
	
	private function removeCondition(conditionUIID:Number):Void {
		app.getCanvas().ddm.removeOutputCondition(conditionUIID);
	}
	
	private function removeAllItems():Void {
		for(var i=0; i<_condition_item_dgd.length; i++) {
			var item = _condition_item_dgd.getItemAt(i);
			app.getCanvas().ddm.removeOutputCondition(item.conditionUIID);
		}
		
		_condition_item_dgd.removeAll();
	}
	
	private function itemChanged(evt:Object):Void {
		Debugger.log("type: " + _selectedDefinition.type, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		
		var ddm = app.getCanvas().ddm;
		_selectedDefinition = _toolOutputDefin_cmb.dataProvider[_toolOutputDefin_cmb.selectedIndex];
		_output_type_lbl.text = "Output Type: " + _selectedDefinition.type;

		switch(_selectedDefinition.type) {
			case ToolOutputDefinition.LONG:
				removeAllItems();
		
				_start_value_stp.visible = true;
				_end_value_stp.visible = true;
				
				_start_value_stp.minimum = Number(_selectedDefinition.startValue);
				_end_value_stp.minimum = Number(_selectedDefinition.startValue);
				_start_value_stp.maximum = Number(_selectedDefinition.endValue);
				_end_value_stp.maximum = Number(_selectedDefinition.endValue);
				
				_start_value_stp.value = Number(_selectedDefinition.startValue);
				_end_value_stp.value = Number(_selectedDefinition.endValue);
				
				add_btn.visible = true;
				clear_all_btn.enabled = true;
				remove_item_btn.enabled = true;
				
				_condition_range_lbl.visible = true;
				
				break;
			
			case ToolOutputDefinition.BOOL:
				removeAllItems();
				
				_start_value_stp.visible = false;
				_end_value_stp.visible = false;
				
				add_btn.visible = false;
				clear_all_btn.enabled = false;
				remove_item_btn.enabled = false;
				
				_condition_range_lbl.visible = false;
				
				// add default conditions for boolean output type
				addCondition(ToolOutputCondition.createBoolCondition(ddm.newUIID(), _selectedDefinition, true));
				addCondition(ToolOutputCondition.createBoolCondition(ddm.newUIID(), _selectedDefinition, false));
				
				break;
			default:
				Debugger.log("type not found", Debugger.GEN, "itemChanged", "ToolOutputConditionsDialog");
		}
			
	}
	
	private function itemEdited(evt:Object):Void {
		var item = _condition_item_dgd.getItemAt(evt.itemIndex);
		var condition:ToolOutputCondition = ToolOutputCondition(item.data);
		condition.displayName = item.conditionName;
		
		app.getCanvas().ddm.addOutputCondition(condition);
		
	}
	
	public function itemSelected(evt:Object):Void {
		var item = _condition_item_dgd.getItemAt(evt.itemIndex);
		
		Debugger.log("current selection: " + Selection.getFocus(), Debugger.CRITICAL, "itemSelected", "GroupNamingDialog");
	}

	public static function getOutputType(type:String):String {
		switch(type) {
			case ToolOutputDefinition.BOOL:
				return "boolean";
				break;
			case ToolOutputDefinition.LONG:
				return "long";
				break;
			default:
				return "undefined";
		}
	}
	
    /**
    * Called by the CLOSE button 
    */
    private function close(){
		app.pi.openConditionMatchingDialog();
		 
        _container.deletePopUp();
    }
	
	private function cancel(){
		// remove any entries
		removeAllItems();
		
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

        //Buttons
        close_btn.move(w-xOkOffset,h-yOkOffset);
		
    }
    
    //Gets+Sets
    
	public function set definitions(a:Array):Void {
		_definitions = new Array();
		
		for(var i=0; i< a.length; i++) {
			var newTOD = new ToolOutputDefinition();
			newTOD.populateFromDTO(a[i]);
			
			_definitions.push(newTOD);
		}
		
		_toolOutputDefin_cmb.dataProvider = _definitions;
		_toolOutputDefin_cmb.labelFunction = function(itemObj){
			return (itemObj.name + " (" + ToolOutputConditionsDialog.getOutputType(itemObj.type) + ")");
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
}