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
	
	
	private var _toolOutputDefin_cmb:ComboBox;
	private var _condition_item_dgd:DataGrid;
	
	private var _start_value_stp:NumericStepper;
	private var _end_value_stp:NumericStepper;
	
	private var add_btn:Button;
	private var close_btn:Button;
	
	private var _output_type_lbl:Label;
	private var _condition_range_lbl:Label;
	
	private var _bgpanel:MovieClip;       //The underlaying panel base
    
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
	
	function ToolOutputConditionsDialog(){
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
        //Add event listeners close button
        close_btn.addEventListener('click',Delegate.create(this, close));
        add_btn.addEventListener('click',Delegate.create(this, addCondition));
		
		_toolOutputDefin_cmb.addEventListener('change', Delegate.create(this, itemChanged));
		
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
		add_btn.label = "+ Add";
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
		add_btn.setStyle('styleName', styleObj);
       
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
	
	/**
	 * Called by ADD button
	 * 
	 * @usage   
	 * @return  
	 */
	
	private function addCondition():Void {
		
	}
	
	private function itemChanged(evt:Object):Void {
		_selectedDefinition = _toolOutputDefin_cmb.dataProvider[_toolOutputDefin_cmb.selectedIndex];
		
		_output_type_lbl.text = "Output Type: " + _selectedDefinition.type;

		Debugger.log("type: " + _selectedDefinition.type, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");

		Debugger.log("val: " + evt.target.value, Debugger.CRITICAL, "itemChanged", "ToolOutputConditionsDialog");
		
		if(_selectedDefinition.type == "OUTPUT_LONG") {
			
			_start_value_stp.visible = true;
			_end_value_stp.visible = true;
			
			_start_value_stp.minimum = Number(_selectedDefinition.startValue);
			_end_value_stp.minimum = Number(_selectedDefinition.startValue);
			_start_value_stp.maximum = Number(_selectedDefinition.endValue);
			_end_value_stp.maximum = Number(_selectedDefinition.endValue);
			
			_start_value_stp.value = Number(_selectedDefinition.startValue);
			_end_value_stp.value = Number(_selectedDefinition.endValue);
		} else {
			_start_value_stp.visible = false;
			_end_value_stp.visible = false;
		}
			
	}
	
    /**
    * Called by the CLOSE button 
    */
    private function close(){
		
        _container.deletePopUp();
    }
	
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        e.target.deletePopUp();
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
		_toolOutputDefin_cmb.labelField = "name";
		
		_toolOutputDefin_cmb.redraw(true);
	}
	
	public function get definitions():Array {
		return _definitions;
	}
	
	/**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
}