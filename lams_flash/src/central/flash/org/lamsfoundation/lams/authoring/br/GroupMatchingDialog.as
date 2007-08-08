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
* Group Matching Dialog window for editing user preferences
* @author  DI
*/
class GroupMatchingDialog extends MovieClip implements Dialog {

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    
	private var _groups:Array;
	private var _branches:Array;
	
    private var close_btn:Button;         // Close button
	
	private var add_match_btn:Button;
	private var remove_match_btn:Button;
    
    private var panel:MovieClip;       //The underlaying panel base
    
    private var groups_lst:List;  
    private var branches_lst:List;    
    
    private var groups_lst_lbl:Label;        // Group and Branches list labels
    private var branches_lst_lbl:Label;
	private var match_dgd_lbl:Label;
	
	private var match_dgd:DataGrid;		// Group-Branch Matching listbox
    
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
	private var app:Application;
    
	private var _branchingActivity:BranchingActivity;
	
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    /**
    * constructor
    */
    function GroupMatchingDialog(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
		app = Application.getInstance();
		
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
        
        //Set the text for buttons
        close_btn.label = "Done" //Dictionary.getValue('al_close');
       
        //Set the labels
        groups_lst_lbl.text = Dictionary.getValue('groupmatch_dlg_groups_lst_lbl');
        branches_lst_lbl.text = Dictionary.getValue('groupmatch_dlg_branches_lst_lbl');
        match_dgd_lbl.text = Dictionary.getValue('groupmatch_dlg_match_dgd_lbl');
		
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        close_btn.addEventListener('click',Delegate.create(this, close));
        add_match_btn.addEventListener('click',Delegate.create(this, addMatch));
        remove_match_btn.addEventListener('click',Delegate.create(this, removeMatch));
        
		//Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - close_btn._x;
        yOkOffset = panel._height - close_btn._y;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
		
        //fire event to say we have loaded
		_container.contentLoaded();
	}
	
	public function loadLists() {
		Debugger.log("Loading Lists: branch length: " + _branches.length, Debugger.CRITICAL, "loadLists", "GroupMatchingDialog");
		
		groups_lst.dataProvider = _groups;
		groups_lst.sortItemsBy("groupUIID", Array.NUMERIC);
		groups_lst.labelField = "groupName";
		groups_lst.hScrollPolicy = "on";
		groups_lst.maxHPosition = 200;
		
		branches_lst.dataProvider = _branches;
		branches_lst.labelField = "sequenceName";
		branches_lst.hScrollPolicy = "on";
		branches_lst.maxHPosition = 200;
		
		var column_sequence:DataGridColumn = new DataGridColumn("sequenceName");
		column_sequence.headerText = "Branch";
		
		var column_name:DataGridColumn = new DataGridColumn("groupName");
		column_name.headerText = "Group";
		
		match_dgd.addColumn(column_sequence);
		match_dgd.addColumn(column_name);
		
		var mappings = app.getCanvas().ddm.branchMappings.values();
		
		for(var m in mappings) {
			match_dgd.addItem(mappings[m]);
			removeGroup(mappings[m].group);
		}
	}
	
	private function removeGroup(g:Group) {
		var indexList:Array = new Array();
		
		for(var i=0; i<groups_lst.rowCount; i++) {
			var item = groups_lst.getItemAt(i);
			if(item.groupUIID == g.groupUIID) { groups_lst.removeItemAt(i); return; }
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
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
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
        add_match_btn.setStyle('styleName', styleObj);
        remove_match_btn.setStyle('styleName', styleObj);
		
		
		styleObj = themeManager.getStyleObject('CanvasPanel');
		panel.setStyle('styleName', styleObj);
		
        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        groups_lst_lbl.setStyle('styleName', styleObj);
        branches_lst_lbl.setStyle('styleName', styleObj);
		match_dgd_lbl.setStyle('styleName', styleObj);
    }
    
    /**
    * Called by the OK button 
    */
    private function close(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','org.lamsfoundation.lams.GroupMatchingDialog');
        
        //close popup
        _container.deletePopUp();
    }
	
	private function addMatch():Void {
		var selectedGroups:Array = new Array();
		
		// get selected items and put together in match
		if(groups_lst.selectedItems.length > 0) {
		
			for(var i=0; i<groups_lst.selectedIndices.length; i++) {
				if(branches_lst.selectedItem != null) {
					setupMatch(groups_lst.getItemAt(groups_lst.selectedIndices[i]), branches_lst.selectedItem);
					selectedGroups.push(groups_lst.selectedIndices[i]);
				} else {
					LFMessage.showMessageAlert("No branch selected");
					return;
				}
				
			}
			var delCount = 0;
			for(var i=0; i<selectedGroups.length; i++) {
				groups_lst.removeItemAt(selectedGroups[i]-delCount);
				delCount++;
			}
			
			
		} else {
			LFMessage.showMessageAlert("No groups selected");
		}
	}
	
	private function setupMatch(group:Group, branch:Branch):Void {
		Debugger.log("group: " + group + " branch: " + branch, Debugger.CRITICAL, "setupMatch", "GroupMatchingDialog");
		 
		var gbMatch:GroupBranchActivityEntry = new GroupBranchActivityEntry(null, app.getCanvas().ddm.newUIID(), group, branch.sequenceActivity, _branchingActivity);
		match_dgd.addItem(gbMatch);
		
		app.getCanvas().ddm.addBranchMapping(gbMatch);
		
	}
	
	private function removeMatch():Void {
		
		var rItem:Object = match_dgd.selectedItem;
		
		if(rItem != null) {
			groups_lst.addItem(rItem.group);
			groups_lst.sortItemsBy("groupUIID", Array.NUMERIC);
			
			match_dgd.removeItemAt(match_dgd.selectedIndex);
			app.getCanvas().ddm.removeBranchMapping(rItem.entryUIID);
			
		} else {
			LFMessage.showMessageAlert("No match selected");
		}
		
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
        panel.setSize(w,h);

        //Buttons
        close_btn.move(w-xOkOffset,h-yOkOffset);
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }
	
	public function set groups(a:Array){
		_groups = a;
	}
	
	public function set branches(a:Array){
		_branches = a;
	}
	
	public function set branchingActivity(a:BranchingActivity) {
		_branchingActivity = a;
	}

}