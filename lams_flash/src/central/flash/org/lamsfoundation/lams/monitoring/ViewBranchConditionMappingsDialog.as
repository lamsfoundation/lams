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

import org.lamsfoundation.lams.authoring.Activity
import org.lamsfoundation.lams.authoring.BranchingActivity
import org.lamsfoundation.lams.monitoring.*

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
class org.lamsfoundation.lams.monitoring.ViewBranchConditionMappingsDialog extends MovieClip implements Dialog {

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
   
	private var _branchingActivity:BranchingActivity;
	private var _mappings:Array;
	
	private var close_btn:Button;         	// Close button
	private var panel:MovieClip;       	//The underlaying panel base
    private var conditions_dgd:DataGrid;
	private var conditions_dgd_lbl:Label;
    
	private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    private var xOkOffset:Number;
    private var yOkOffset:Number;
	
    /**
    * constructor
    */
    function ViewBranchConditionMappingsDialog(){
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
        
		//get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
		
        //Set the labels
        conditions_dgd_lbl.text = Dictionary.getValue('branch_mapping_dlg_conditions_dgd_lbl');
		
        //EVENTS
        close_btn.addEventListener('click', Delegate.create(this, close));
        
        //Set the text for buttons
        close_btn.label = Dictionary.getValue("ls_win_learners_close_btn");
		
		//work out offsets from bottom RHS of panel
        xOkOffset = panel._width - close_btn._x;
        yOkOffset = panel._height - close_btn._y;
		
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
		
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
	   
        //fire event to say we have loaded
		_container.contentLoaded();
	}
	
	public function loadLists() {
		var column_first:DataGridColumn;
		if(_branchingActivity.activityTypeID == Activity.TOOL_BRANCHING_ACTIVITY_TYPE) {
			column_first = new DataGridColumn("displayName");
			column_first.headerText = Dictionary.getValue("branch_mapping_dlg_condition_col_lbl");
		} else {
			column_first = new DataGridColumn("groupName");
			column_first.headerText = Dictionary.getValue("branch_mapping_dlg_group_col_lbl");
		} 
		
		column_first.width = conditions_dgd.width/2;
		
		var column_sequence:DataGridColumn = new DataGridColumn("sequenceName");
		column_sequence.headerText = Dictionary.getValue("branch_mapping_dlg_branch_col_lbl");
		column_sequence.width = conditions_dgd.width/2;
		
		conditions_dgd.addColumn(column_first);
		conditions_dgd.addColumn(column_sequence);
		
		for(var i=0; i<_mappings.length; i++)
			conditions_dgd.addItem(_mappings[i]);
		
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
        }
    }
    
    /**
    * Called by the OK button 
    */
    private function close(){
		_container.deletePopUp();
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
		
		styleObj = themeManager.getStyleObject('BGPanel');
		panel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
		conditions_dgd_lbl.setStyle('styleName', styleObj);
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
        panel.setSize(w,h);

        //Buttons
        close_btn.move(w-xOkOffset,h-yOkOffset);
    }
    
	
	public function set branchingActivity(a:BranchingActivity) {
		_branchingActivity = a;
	}
	
	public function get branchingActivity():BranchingActivity {
		return _branchingActivity;
	}
	
	/**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
	public function set mappings(a:Array){
		_mappings = a;
	}
}