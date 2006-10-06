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
import org.lamsfoundation.lams.common.ApplicationParent;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
//import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.ws.Workspace
import mx.controls.*
import mx.utils.*

/**
* Main application menu for LAMS
*  
* @author   DI
*/
class org.lamsfoundation.lams.common.ui.LFMenuBar extends MovieClip {

    // instances on the stage
    private var _mb:MenuBar;
    
    // variables in this script
    private var file_menu:Menu;
    private var edit_menu:Menu;
    private var view_menu:Menu;
	private var go_menu:Menu;
    private var tools_menu:Menu;
    private var help_menu:Menu;
    private var env:String;
    private var view_xml:XML; // to illustrate creating a menu with xml
    
    private var app:ApplicationParent;
    private var tm:ThemeManager;
	private var _dictionary:Dictionary;
    
	private static var _instance:LFMenuBar = null; 
	
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;    
    
    //Constructor    
    function LFMenuBar(){
        mx.events.EventDispatcher.initialize(this);
        //Set up init for next frame and make invisible this frame
        this.onEnterFrame = init;
        this._visible = false;
		_instance = this;
		
        //Get a reference to the application, ThemeManager and Dictionary
        app = ApplicationParent.getInstance();
        tm = ThemeManager.getInstance();
	
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Delegate.create(this,setupMenuItems));
    }
	
	 /**
    * Retrieves an instance of the LFMenuBar singleton
    */ 
    public static function getInstance():LFMenuBar{
        if(LFMenuBar._instance == null){
            LFMenuBar._instance = new LFMenuBar();
        }
        return LFMenuBar._instance;
    }

    public function init() {
		Debugger.log('init called',Debugger.GEN,'init','LFMenuBar');
		
        //Kill enter frame 
        delete this.onEnterFrame;
        
        //Register for theme changed events and set Initial style
        tm.addEventListener('themeChanged',this);
        //setStyles();
        //Broadcast event
        dispatchEvent({type:'load',target:this});
		this.tabChildren = true;
    } 
	
	
	
	private function setTabIndex(selectedTab:String){
		
		//All Buttons Tab Index
		file_menu.tabIndex = 101
		edit_menu.tabIndex = 102
		tools_menu.tabIndex = 103
		help_menu.tabIndex = 104
			
	}
	/**
	* Set up the actual menu items
	*/
	private function setupMenuItems(){
		
		//trace("Called From: "+env)
		if (env != "Monitoring"){
			
		/*=================
            FILE MENU
        =================*/
        file_menu = _mb.addMenu(Dictionary.getValue('mnu_file'));
        
        // "new" is the linkage id of the movie clip to be used as the icon for the "New" menu item.
        //file_menu.addMenuItem({label:"New", instanceName:"newInstance", icon:"new"});
        //_global.breakpoint();
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_new'), instanceName:"newItem"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_open'), instanceName:"openItem"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_recover'), instanceName:"recoverItem", enabled:false});
        //file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_close'), instanceName:"closeItem"});
        file_menu.addMenuItem({type:"separator"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_save'), instanceName:"saveItem"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_saveas'), instanceName:"saveItemAs"});
		file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_import'), instanceName:"importItem"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_export'), instanceName:"exportItem", enabled:false});
		file_menu.addMenuItem({type:"separator"});
		file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_exit'), instanceName:"exitItem"});
		
		/*=================
            EDIT MENU
        =================*/
        edit_menu = _mb.addMenu(Dictionary.getValue("mnu_edit"));
        
        // "new" is the linkage id of the movie clip to be used as the icon for the "New" menu item.
        //file_menu.addMenuItem({label:"New", instanceName:"newInstance", icon:"new"});
        //_global.breakpoint();
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_undo'), instanceName:"undoItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_redo'), instanceName:"redoItem"});
        edit_menu.addMenuItem({type:"separator"});
		//edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_cut'), instanceName:"cutItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_copy'), instanceName:"copyItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_paste'), instanceName:"pasteItem"});
       

        /*=================
            TOOLS MENU
        =================*/
		//_global.breakpoint();
        tools_menu = _mb.addMenu(Dictionary.getValue('mnu_tools'));
		
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_tools_trans'), instanceName:"drawTransitionalItem"});
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_tools_opt'), instanceName:"drawOptionalItem"});
        //tools_menu.addMenuItem({type:"separator"});
        //tools_menu.addMenuItem({label:Dictionary.getValue('mnu_tools_prefs'), instanceName:"prefsItem", enabled:false});

        /*=================
            HELP MENU
        =================*/
        help_menu = _mb.addMenu(Dictionary.getValue('mnu_help'));
        help_menu.addMenuItem({label:Dictionary.getValue('mnu_help_help'), instanceName:"helpGenItem"});
		help_menu.addMenuItem({label:Dictionary.getValue('mnu_help_abt'), instanceName:"aboutItem"});
        
		
        //set up listeners
        // register the listeners with the separate menus
        file_menu.addEventListener("change", Delegate.create(this,fileMenuClicked));
        edit_menu.addEventListener("change", Delegate.create(this,editMenuClicked));
        tools_menu.addEventListener("change", Delegate.create(this,toolsMenuClicked));
		help_menu.addEventListener("change", Delegate.create(this, helpMenuClicked));
		
		//Now that menu items have been set up make the menu bar visible
		this._visible = true;
		Debugger.log('Finished setting up set visible to:'+this._visible,Debugger.GEN,'setupMenuItems','LFMenuBar');
		
		} else {
		
			
		/*=================
            FILE MENU
        =================*/
        file_menu = _mb.addMenu(Dictionary.getValue('mnu_file'));
        
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_refresh'), instanceName:"refreshItem", enabled:true});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_editclass'), instanceName:"editClassItem", enabled:false});
		file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_start'), instanceName:"startItem", enabled:false});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_schedule'), instanceName:"scheduleItem", enabled:false});
        
		file_menu.addMenuItem({type:"separator"});
		file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_exit'), instanceName:"exitItem"});
        
		
		/*=================
            EDIT MENU
        =================*/
        edit_menu = _mb.addMenu(Dictionary.getValue("mnu_edit"));
        
        // "new" is the linkage id of the movie clip to be used as the icon for the "New" menu item.
		edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_cut'), instanceName:"cutItem", enabled:false});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_copy'), instanceName:"copyItem", enabled:false});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_paste'), instanceName:"pasteItem", enabled:false});
		
		/*=================
            VIEW MENU
        =================*/
		//_global.breakpoint();
        view_menu = _mb.addMenu(Dictionary.getValue('mnu_view'));
		
        view_menu.addMenuItem({label:Dictionary.getValue('mnu_view_learners'), instanceName:"viewLearners", enabled:false});
		
		/*=================
            GO MENU
        =================*/
		//_global.breakpoint();
        go_menu = _mb.addMenu(Dictionary.getValue('mnu_go'));
		
        go_menu.addMenuItem({label:Dictionary.getValue('mnu_go_lesson'), instanceName:"goLessonTab"});
        go_menu.addMenuItem({label:Dictionary.getValue('mnu_go_schedule'), instanceName:"goScheduleTab"});
        go_menu.addMenuItem({label:Dictionary.getValue('mnu_go_learners'), instanceName:"goLearnerTab"});
		//go_menu.addMenuItem({label:Dictionary.getValue('mnu_go_todo'), instanceName:"goTodoTab"});

		/*=================
            HELP MENU
        =================*/
        help_menu = _mb.addMenu(Dictionary.getValue('mnu_help'));
        help_menu.addMenuItem({label:Dictionary.getValue('mnu_help_help'), instanceName:"helpGenItem"});
		help_menu.addMenuItem({label:Dictionary.getValue('mnu_help_abt'), instanceName:"aboutItem"});
        
        //set up listeners
        // register the listeners with the separate menus
        file_menu.addEventListener("change", Delegate.create(this,fileMenuClicked));
        edit_menu.addEventListener("change", Delegate.create(this,editMenuClicked));
        view_menu.addEventListener("change", Delegate.create(this,viewMenuClicked));
		go_menu.addEventListener("change", Delegate.create(this,goMenuClicked));
		help_menu.addEventListener("change", Delegate.create(this, helpMenuClicked));
		//Now that menu items have been set up make the menu bar visible
		this._visible = true;
		Debugger.log('Finished setting up set visible to:'+this._visible,Debugger.GEN,'setupMenuItems','LFMenuBar');
		}
	}
    
    /**
    * Size function
	* @param w:Number	width
	* @param h:Number	height
    */
    public function setSize(w:Number,h:Number) {
        _mb.setSize(w,h);
    }
    
    /**
    * event handler for file menu click
    */
    private function fileMenuClicked(eventObj:Object):Void{
        //Which item was clicked ?      
        switch (eventObj.menuItem) {
            case eventObj.menu.newItem : 
                org.lamsfoundation.lams.authoring.Application(app).getCanvas().clearCanvas(false);
                break;
            case eventObj.menu.openItem :
                org.lamsfoundation.lams.authoring.Application(app).getCanvas().openDesignBySelection();
                break;
			case eventObj.menu.recoverItem :
				org.lamsfoundation.lams.authoring.Application(app).getCanvas().showRecoverMessage();
				break;
			case eventObj.menu.saveItem:
				Debugger.log('Clicked Flie > Save',Debugger.GEN,'fileMenuClicked','LFMenuBar');
                org.lamsfoundation.lams.authoring.Application(app).getCanvas().saveDesign();
                break;
				
			case eventObj.menu.saveItemAs:
			//TODO: go through workspace to save design in location
				Debugger.log('Clicked File > Save As',Debugger.GEN,'fileMenuClicked','LFMenuBar');
                org.lamsfoundation.lams.authoring.Application(app).getCanvas().saveDesignToServerAs(Workspace.MODE_SAVEAS);
                break;
			case eventObj.menu.importItem:
				Debugger.log('Clicked File > Import',Debugger.GEN,'fileMenuClicked','LFMenuBar');
				org.lamsfoundation.lams.authoring.Application(app).getCanvas().launchImportWindow();
				break;
			case eventObj.menu.exportItem:
				Debugger.log('Clicked File > Export',Debugger.GEN,'fileMenuClicked','LFMenuBar');
				org.lamsfoundation.lams.authoring.Application(app).getCanvas().launchExportWindow();
				break;
			case eventObj.menu.editClassItem:
				org.lamsfoundation.lams.monitoring.Application(app).getMonitor().getMM().setDialogOpen("LM_DIALOG");
				break;
			case eventObj.menu.refreshItem:
				org.lamsfoundation.lams.monitoring.Application(app).getMonitor().getMM().refreshAllData();
				break;
			case eventObj.menu.startItem:
				org.lamsfoundation.lams.monitoring.Application(app).getMonitor().startLesson(false, _root.lessonID);
				break;
			case eventObj.menu.scheduleItem:
				org.lamsfoundation.lams.monitoring.Application(app).getMonitor().getMV().getLessonTabView().scheduleLessonStart();
				break;
			case eventObj.menu.exitItem:
				fscommand("closeWindow", null);
				break;
        }        
    }

    /**
    * event handler for file menu click
    */
    private function editMenuClicked(eventObj:Object):Void{
        //Which item was clicked ?      
        switch (eventObj.menuItem) {
            case eventObj.menu.undoItem : 
                trace('new selected');
				org.lamsfoundation.lams.authoring.Application(app).getCanvas().undo();
                break;
            case eventObj.menu.redoItem :
               org.lamsfoundation.lams.authoring.Application(app).getCanvas().redo();
                break;
			 case eventObj.menu.cutItem :
                org.lamsfoundation.lams.authoring.Application(app).cut();
                break;
			 case eventObj.menu.copyItem :
                org.lamsfoundation.lams.authoring.Application(app).copy();
                break;
			 case eventObj.menu.pasteItem :
                org.lamsfoundation.lams.authoring.Application(app).paste();
                break;
        }        
    }
	
	
    /**
    * event handler for go menu click
    */
    private function goMenuClicked(eventObj:Object):Void{
		var tab:MovieClip = org.lamsfoundation.lams.monitoring.Application(app).getMonitor().getMV().getMonitorTab();
		switch(eventObj.menuItem) {
			case eventObj.menu.goLessonTab : 
				tab.selectedIndex = 0;
				break;
			case eventObj.menu.goScheduleTab : 
				tab.selectedIndex = 1;
				break;
			case eventObj.menu.goLearnerTab : 
				tab.selectedIndex = 2;
				break;
			case eventObj.menu.goTodoTab :
				tab.selectedIndex = 3;
				break;
				
			dispatchEvent({type:'change', target: tab});
		}
	}
    
	 /**
    * event handler for view menu click
    */
    private function viewMenuClicked(eventObj:Object):Void{
		switch(eventObj.menuItem) {
			case eventObj.menu.viewLearners : 
				org.lamsfoundation.lams.monitoring.Application(app).getMonitor().getMM().setDialogOpen("VM_DIALOG");
				break;
		}
	}
	
    /**
    * event handler for tool menu click
    */
    private function toolsMenuClicked(eventObj:Object):Void{
        //Which item was clicked ?
        switch (eventObj.menuItem) {
			case eventObj.menu.prefsItem : 
                org.lamsfoundation.lams.authoring.Application(app).showPrefsDialog();
                break;
			case eventObj.menu.drawTransitionalItem :
				org.lamsfoundation.lams.authoring.Application(app).getCanvas().toggleTransitionTool();
				break;
			case eventObj.menu.archiveItem:
				break;
			case eventObj.menu.enableItem:
				break;
			case eventObj.menu.disableItem:
				break;
        }        
    }    
	
	 /**
    * event handler for help menu click
    */
    private function helpMenuClicked(eventObj:Object):Void{
		switch (eventObj.menuItem) {
			case eventObj.menu.helpItem :
				break;
			case eventObj.menu.helpGenItem :
				app.getHelpURL();
				break;
			case eventObj.menu.aboutItem :
			if (env != "Monitoring"){
				org.lamsfoundation.lams.authoring.Application(app).getCanvas().openAboutLams();
			}else {
				org.lamsfoundation.lams.monitoring.Application(app).getMonitor().openAboutLams();
			}
				//LFMessage.showMessageAlert("Serial No: " + ApplicationParent.SERIAL_NO);
				break;
		}
	}
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object){
        Debugger.log('themeChanged',Debugger.GEN,'themeChanged','LFMenuBar');        
        if(event.type=='themeChanged'){
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','LFMenuBar');
        }
    }
    
	public function enableExport(enable:Boolean){
		file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(7), enable);
	}
	
	public function enableSave(enable:Boolean){
		file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(4), enable);
	}
	
	public function enableRecover(enable:Boolean){
		file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(2), enable);
	}
	
	public function get fileMenu():Menu {
		return file_menu;
	}
	
	public function get viewMenu():Menu {
		return view_menu;
	}
	
	public function setDefaults():Void{
		//file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(0), true);
		file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(1), false);
		file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(2), false);
		file_menu.setMenuItemEnabled(file_menu.getMenuItemAt(3), false);
		
		view_menu.setMenuItemEnabled(view_menu.getMenuItemAt(0), false);
	}
	
    /**
    * Set the styles for the menu called from init. and themeChanged event handler
    */
    private function setStyles() {
        var styleObj = tm.getStyleObject('LFMenuBar');
        _mb.setStyle('styleName',styleObj);
    }
    
    function get className():String { 
        return 'LFMenuBar';
    }
}