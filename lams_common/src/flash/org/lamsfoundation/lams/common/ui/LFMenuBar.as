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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*
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
    private var tools_menu:Menu;
    private var help_menu:Menu;
    private var env:String;
    private var view_xml:XML; // to illustrate creating a menu with xml
    
    private var app:Application;
    private var tm:ThemeManager;
	private var _dictionary:Dictionary;
    
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
       
		
        //Get a reference to the application, ThemeManager and Dictionary
        app = Application.getInstance();
        tm = ThemeManager.getInstance();
	
		_dictionary = Dictionary.getInstance();
		_dictionary.addEventListener('init',Delegate.create(this,setupMenuItems));
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
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_revert'), instanceName:"revertItem", enabled:false});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_new'), instanceName:"closeItem"});
        file_menu.addMenuItem({type:"separator"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_save'), instanceName:"saveItem"});
        file_menu.addMenuItem({label:Dictionary.getValue('mnu_file_saveas'), instanceName:"saveItemAs"});


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
		edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_cut'), instanceName:"cutItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_copy'), instanceName:"copyItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_paste'), instanceName:"pasteItem"});
       

        /*=================
            TOOLS MENU
        =================*/
		//_global.breakpoint();
        tools_menu = _mb.addMenu(Dictionary.getValue('mnu_tools'));
		
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_tools_trans'), instanceName:"drawTransitionalItem"});
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_tools_opt'), instanceName:"drawOptionalItem"});
        tools_menu.addMenuItem({type:"separator"});
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_tools_prefs'), instanceName:"prefsItem"});


        /*=================
            HELP MENU
        =================*/
        help_menu = _mb.addMenu(Dictionary.getValue('mnu_help'));
        help_menu.addMenuItem({label:Dictionary.getValue('mnu_help_abt'), instanceName:"aboutItem"});
        
        //set up listeners
        // register the listeners with the separate menus
        file_menu.addEventListener("change", Delegate.create(this,fileMenuClicked));
        edit_menu.addEventListener("change", Delegate.create(this,editMenuClicked));
        tools_menu.addEventListener("change", Delegate.create(this,toolsMenuClicked));
		
		//Now that menu items have been set up make the menu bar visible
		this._visible = true;
		Debugger.log('Finished setting up set visible to:'+this._visible,Debugger.GEN,'setupMenuItems','LFMenuBar');
		
		}else{
		
		/*=================
            LESSON MENU
        =================*/
		//_global.breakpoint();
        tools_menu = _mb.addMenu(Dictionary.getValue('mnu_tools'));
		
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_lesson_create'), instanceName:"drawTransitionalItem"});
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_lesson_disable'), instanceName:"drawOptionalItem"});
        tools_menu.addMenuItem({type:"separator"});
        tools_menu.addMenuItem({label:Dictionary.getValue('mnu_lesson_archive'), instanceName:"prefsItem"});
		
		/*=================
            EDIT MENU
        =================*/
        edit_menu = _mb.addMenu(Dictionary.getValue("mnu_edit"));
        
        // "new" is the linkage id of the movie clip to be used as the icon for the "New" menu item.
		edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_cut'), instanceName:"cutItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_copy'), instanceName:"copyItem"});
        edit_menu.addMenuItem({label:Dictionary.getValue('mnu_edit_paste'), instanceName:"pasteItem"});
		
		/*=================
            HELP MENU
        =================*/
        help_menu = _mb.addMenu(Dictionary.getValue('mnu_help'));
        help_menu.addMenuItem({label:Dictionary.getValue('mnu_help_abt'), instanceName:"aboutItem"});
        
        //set up listeners
        // register the listeners with the separate menus
        file_menu.addEventListener("change", Delegate.create(this,fileMenuClicked));
        edit_menu.addEventListener("change", Delegate.create(this,editMenuClicked));
        tools_menu.addEventListener("change", Delegate.create(this,toolsMenuClicked));
		
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
                trace('new selected');
                break;
            case eventObj.menu.openItem :
                app.getWorkspace().userSelectItem();
                break;
			case eventObj.menu.saveItem:
				Debugger.log('Clicked Flie > Save',Debugger.GEN,'fileMenuClicked','LFMenuBar');
                app.getCanvas().saveDesign();
                break;
				
			case eventObj.menu.saveItemAs:
			//TODO: go through workspace to save design in location
				Debugger.log('Clicked Flie > Save As',Debugger.GEN,'fileMenuClicked','LFMenuBar');
                app.getCanvas().saveDesignToServerAs();
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
				 app.getCanvas().undo();
                break;
            case eventObj.menu.redoItem :
                app.getCanvas().redo();
                break;
			 case eventObj.menu.cutItem :
                app.cut();
                break;
			 case eventObj.menu.copyItem :
                app.copy();
                break;
			 case eventObj.menu.pasteItem :
                app.paste();
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
                app.showPrefsDialog();
                break;
				
			case eventObj.menu.drawTransitionalItem :
				app.getCanvas().toggleTransitionTool();
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