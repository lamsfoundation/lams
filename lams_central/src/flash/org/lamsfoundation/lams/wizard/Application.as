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

import org.lamsfoundation.lams.wizard.*;	// Wizard

import org.lamsfoundation.lams.common.ws.*          //Workspace
import org.lamsfoundation.lams.common.comms.*       //communications
import org.lamsfoundation.lams.common.util.*        //Utils
import org.lamsfoundation.lams.common.dict.*        //Dictionary
import org.lamsfoundation.lams.common.ui.*          //User interface
import org.lamsfoundation.lams.common.style.*       //Themes/Styles
import org.lamsfoundation.lams.common.*             
import mx.managers.*
import mx.utils.*

/**
* Application - Adding new sequence - LAMS Monitor (wizard)
* @author   M Seaton
*/
class org.lamsfoundation.lams.wizard.Application extends ApplicationParent {
	
		
	private static var SHOW_DEBUGGER:Boolean = false;
	
	private static var MODULE:String = "wizard";
	
	private static var _controlKeyPressed:String;
    
    private static var WIZARD_X:Number = 0;
    private static var WIZARD_Y:Number = 0;
    private static var WIZARD_W:Number = 584;
    private static var WIZARD_H:Number = 550;
    
    private static var APP_ROOT_DEPTH:Number = 10; //depth of the application root
    private static var DIALOGUE_DEPTH:Number = 20;	//depth of the cursors
    private static var TOOLTIP_DEPTH:Number = 30;	//depth of the cursors
    private static var CURSOR_DEPTH:Number = 40;   //depth of the cursors
    
    private static var UI_LOAD_CHECK_INTERVAL:Number = 50;
	private static var UI_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	private static var DATA_LOAD_CHECK_INTERVAL:Number = 50;
	private static var DATA_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;

    private static var QUESTION_MARK_KEY:Number = 191;
    private static var X_KEY:Number = 88;
    private static var C_KEY:Number = 67;
	private static var D_KEY:Number = 68;
	//private static var T_KEY:Number = 84;
    private static var V_KEY:Number = 86;
    private static var Z_KEY:Number = 90; 
    private static var Y_KEY:Number = 89;
	
	

	private var _uiLoadCheckCount = 0;				// instance counter for number of times we have checked to see if theme and dict are loaded
	private var _dataLoadCheckCount = 0;			
	
    private var _wizard:Wizard;
    private var _workspace:Workspace;
    private var _comms:Communication;
    private var _themeManager:ThemeManager;
    private var _dictionary:Dictionary;
    private var _config:Config;
    private var _debugDialog:MovieClip;                //Reference to the debug dialog
    
    
    private var _dialogueContainer_mc:MovieClip;       //Dialog container
    private var _tooltipContainer_mc:MovieClip;        //Tooltip container
    private var _cursorContainer_mc:MovieClip;         //Cursor container
    private var _container_mc:MovieClip;               //Main container
    
    //Data flags
    private var _dictionaryLoaded:Boolean;             //Dictionary loaded flag
    private var _dictionaryEventDispatched:Boolean     //Event status flag
    private var _themeLoaded:Boolean;                  //Theme loaded flag
    private var _themeEventDispatched:Boolean          //Dictionary loaded flag
    private var _UILoadCheckIntervalID:Number;         //Interval ID for periodic check on UILoad status
    private var _UILoaded:Boolean;                     //UI Loading status
    
	private var _DataLoadCheckIntervalID:Number;
	
    //UI Elements
    //private var _toolbarLoaded:Boolean;                //These are flags set to true when respective element is 'loaded'
    private var _wizardLoaded:Boolean;
	private var _showCMItem:Boolean;
	
	//clipboard
	private var _clipboardData:Object;
	// set up Key Listener
	//private var keyListener:Object;
    
    //Application instance is stored as a static in the application class
    private static var _instance:Application = null;     

    /**
    * Application - Constructor
    */
    private function Application(){
		super(this);
		_module = Application.MODULE;
		// loaded flag
    }
    
    /**
    * Retrieves an instance of the Application singleton
    */ 
    public static function getInstance():Application{
        if(Application._instance == null){
            Application._instance = new Application();
        }
        return Application._instance;
    }

    /**
    * Main entry point to the application
    */
    public function main(container_mc:MovieClip){
        _container_mc = container_mc;
        _UILoaded = false;
        
		//add the cursors:
		Cursor.addCursor(C_HOURGLASS);
		
		
		//Comms object - do this before any objects are created that require it for server communication
        _comms = new Communication();

    	//Get the instance of config class
        _config = Config.getInstance();
        
        //Assign the config load event to
        _config.addEventListener('load',Delegate.create(this,configLoaded));
        
        //Set up Key handler 
        //TODO take out after testing and uncomment same key handler in ready();
        Key.addListener(this);
		//setupUI();
		//setupData();
		//checkDataLoaded();
    }
    
    /**
    * Called when the config class has loaded
    */
    private function configLoaded(){
        //Now that the config class is ready setup the UI and data, call to setupData() first in 
		//case UI element constructors use objects instantiated with setupData()
        setupData();
		checkDataLoaded();
		
    }
    
    /**
    * Loads and sets up event listeners for Theme, Dictionary etc.  
    */
    private function setupData() {
		
        //Get the language, create+load dictionary and setup load handler.
		var language:String = String(_config.getItem('language'));
        _dictionary = Dictionary.getInstance();
        _dictionary.addEventListener('load',Delegate.create(this,onDictionaryLoad));
		_dictionary.load(language);
        
		
		
        //Set reference to StyleManager and load Themes and setup load handler.
        var theme:String = String(_config.getItem('theme'));
        _themeManager = ThemeManager.getInstance();
        _themeManager.addEventListener('load',Delegate.create(this,onThemeLoad));
        _themeManager.loadTheme(theme);
		Debugger.getInstance().crashDumpSeverityLevel = Number(_config.getItem('crashDumpSeverityLevelLog'));
		Debugger.getInstance().severityLevel = Number(_config.getItem('severityLevelLog')); 
		
    }
    
    /**
    * Called when Dictionary loaded
	* @param evt:Object	the event object
    */
    private function onDictionaryLoad(evt:Object){
        if(evt.type=='load'){
            _dictionaryLoaded = true;
			Debugger.log('Dictionary loaded :',Debugger.CRITICAL,'onDictionaryLoad','Application');			
        } else {
            Debugger.log('event type not recognised :'+evt.type,Debugger.CRITICAL,'onDictionaryLoad','Application');
        }
    }
    
    /**
    * Called when the current selected theme has been loaded
	* @param evt:Object	the event object
    */
    private function onThemeLoad(evt:Object) {
        if(evt.type=='load'){
            _themeLoaded = true; 
			Debugger.log('!Theme loaded :',Debugger.CRITICAL,'onThemeLoad','Application');		
        } else {
            Debugger.log('event type not recognised :'+evt.type,Debugger.CRITICAL,'onThemeLoad','Application');
        }
		
    }
	 
	/**
	* Periodically checks if data has been loaded
	*/
	private function checkDataLoaded() {
		// first time through set interval for method polling
		if(!_DataLoadCheckIntervalID) {
			_DataLoadCheckIntervalID = setInterval(Proxy.create(this, checkDataLoaded), DATA_LOAD_CHECK_INTERVAL);
		} else {
			_dataLoadCheckCount++;
			// if dictionary and theme data loaded setup UI
			if(_dictionaryLoaded && _themeLoaded) {
				clearInterval(_DataLoadCheckIntervalID);
				
				setupUI();
				checkUILoaded();
				
        
			} else if(_dataLoadCheckCount >= DATA_LOAD_CHECK_TIMEOUT_COUNT) {
				Debugger.log('reached timeout waiting for data to load.',Debugger.CRITICAL,'checkUILoaded','Application');
				clearInterval(_UILoadCheckIntervalID);
				
        
			}
		}
	}
    
    /**
    * Runs periodically and dispatches events as they are ready
    */
    private function checkUILoaded() {
        //If it's the first time through then set up the interval to keep polling this method
        if(!_UILoadCheckIntervalID) {
            _UILoadCheckIntervalID = setInterval(Proxy.create(this,checkUILoaded),UI_LOAD_CHECK_INTERVAL);
        } else {
			_uiLoadCheckCount++;
            //If all events dispatched clear interval and call start()
            if(_dictionaryEventDispatched && _themeEventDispatched){
				//Debugger.log('Clearing Interval and calling start :',Debugger.CRITICAL,'checkUILoaded','Application');	
                clearInterval(_UILoadCheckIntervalID);
				start();
            }else {
                //If UI loaded check which events can be broadcast
                if(_UILoaded){
					//Debugger.log('ALL UI LOADED, waiting for all true to dispatch init events: _dictionaryLoaded:'+_dictionaryLoaded+'_themeLoaded:'+_themeLoaded ,Debugger.GEN,'checkUILoaded','Application');

                    //If dictionary is loaded and event hasn't been dispatched - dispatch it
                    if(_dictionaryLoaded && !_dictionaryEventDispatched){
						_dictionaryEventDispatched = true;
                        _dictionary.broadcastInit();
                    }
                    //If theme is loaded and theme event hasn't been dispatched - dispatch it
                    if(_themeLoaded && !_themeEventDispatched){
						_themeEventDispatched = true;
                        _themeManager.broadcastThemeChanged();
                    }
					
					if(_uiLoadCheckCount >= UI_LOAD_CHECK_TIMEOUT_COUNT){
						//if we havent loaded the dict or theme by the timeout count then give up
						Debugger.log('raeached time out waiting to load dict and themes, giving up.',Debugger.CRITICAL,'checkUILoaded','Application');
						var msg:String = "";
						if(!_themeEventDispatched){
							msg+=Dictionary.getValue("app_chk_themeload");
						}
						if(!_dictionaryEventDispatched){
							msg+="The lanaguage data has not been loaded.";
						}
						msg+=Dictionary.getValue("app_fail_continue");
						var e:LFError = new LFError(msg,"Canvas.setDroppedTemplateActivity",this,'_themeEventDispatched:'+_themeEventDispatched+' _dictionaryEventDispatched:'+_dictionaryEventDispatched);
						e.showErrorAlert();
						//todo:  give the user a message
						clearInterval(_UILoadCheckIntervalID);
					}
                }
            }
        }
    }
    
    /**
    * This is called by each UI element as it loads to notify Application that it's loaded
    * When all UIElements are loaded the Application can set UILoaded flag true allowing events to be dispatched
    * and methods called on the UI Elements
    * 
    * @param UIElementID:String - Identifier for the Element that was loaded
    */
    public function UIElementLoaded(evt:Object) {
       if(evt.type=='load'){
            //Which item has loaded
            switch (evt.target.className) {
				case 'Wizard' :
                    _wizardLoaded = true;
                    break;
                default:
            }
            
            //If all of them are loaded set UILoad accordingly
			if(_wizardLoaded){
                _UILoaded=true;                
            }
            
        }   
    }
    
	public function showCustomCM(showCMItem:Boolean, cmItems):Object{
		
		var root_cm:ContextMenu = new ContextMenu();  
		root_cm.hideBuiltInItems();  
		trace("CM Item label: "+cmItems.cmlabel)
		for (var i=0; i<cmItems.length; i++){
			trace("CM Item length: "+cmItems[i].handler)
			var menuItem_cmi:ContextMenuItem = new ContextMenuItem(cmItems[i].cmlabel.toString(), cmItems[i].handler);
			root_cm.customItems.push(menuItem_cmi);
		}
		
		if (showCMItem == false) {
			for(var i=0; i<root_cm.customItems.length; i++){
				root_cm.customItems[i].enabled = false;
			}
		} else {
			for(var i=0; i<root_cm.customItems.length; i++){
				root_cm.customItems[i].enabled = true;
			}
		}

		
		
		//this.menu = root_cm;  
		//_root.menu = root_cm;   
		return root_cm;
	}
	
	
		
		
	
    /**
    * Create all UI Elements
    */
    private function setupUI(){
		//Make the base context menu hide built in items so we don't have zoom in etc 
		// Change this to false to remove
		 var myCopy:Array = new Array();
		var menuArr:Array = new Array();
		menuArr[0] =["Copy", copy];
		menuArr[1] = ["Paste", paste];
		
		for (var i=0; i<menuArr.length; i++){
			var myObj:Object = new Object();
			myObj.cmlabel = menuArr[i][0];
			myObj.handler = menuArr[i][1]; 
			myCopy[i]= myObj;
			
		} 
        var test_cm = showCustomCM(false, myCopy, this)
		_root.menu = test_cm; 
        //Create the application root
        _appRoot_mc = _container_mc.createEmptyMovieClip('appRoot_mc',APP_ROOT_DEPTH);
        //Create screen elements
        _dialogueContainer_mc = _container_mc.createEmptyMovieClip('_dialogueContainer_mc',DIALOGUE_DEPTH);
        _tooltipContainer_mc = _container_mc.createEmptyMovieClip('_tooltipContainer_mc',TOOLTIP_DEPTH);
        _cursorContainer_mc = _container_mc.createEmptyMovieClip('_cursorContainer_mc',CURSOR_DEPTH);
		
        var depth:Number = _appRoot_mc.getNextHighestDepth();
		
		// WIZARD 
		_wizard = new Wizard(_appRoot_mc,WIZARD_X, WIZARD_Y, WIZARD_W, WIZARD_H);
        _wizard.addEventListener('load',Proxy.create(this,UIElementLoaded));
        //WORKSPACE
        _workspace = new Workspace();
		
    }
    
    /**
    * Runs when application setup has completed.  At this point the init/loading screen can be removed and the user can
    * work with the application
    */
    private function start(){
		//TODO: Remove the loading screen
		
        //Fire off a resize to set up sizes
        onResize();
		//TODO Remove loading screen
		if(SHOW_DEBUGGER){
			showDebugger();
		}

    }
    
    /**
    * Opens the preferences dialog
    
    public function showPrefsDialog() {
        PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue("prefs_dlg_title"),closeButton:true,scrollContentPath:'preferencesDialog'});
    }
	*/
	
	/**
    * Opens the lesson manager dialog
    */
    //public function showLessonManagerDialog() {
    //    PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue("lesson_dlg_title"),closeButton:true,scrollContentPath:'selectClass'});
    //}
    
    /**
    * Receives events from the Stage resizing
    */
    public function onResize(){
        //Debugger.log('onResize',Debugger.GEN,'main','org.lamsfoundation.lams.Application');

        //Get the stage width and height and call onResize for stage based objects
        var w:Number = Stage.width;
        var h:Number = Stage.height;
		
		_wizard.setSize(w,h);
    }
	
	/**
    * Handles KEY Releases for Application
    */
	
	public function transition_keyPressed(){
		_controlKeyPressed = "transition";

	}
	public function showDebugger():Void{
		_debugDialog = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:'Debug',closeButton:true,scrollContentPath:'debugDialog'});
	}
	
	public function hideDebugger():Void{
		_debugDialog.deletePopUp();
	}
	
	/**
	 * stores a reference to the object
	 * @usage   
	 * @param   obj 
	 * @return  
	 */
	public function setClipboardData(obj:Object):Void{
		_clipboardData = obj;
		trace("clipBoard data id"+_clipboardData);
	}
	
	/**
	 * returns a reference to the object on the clipboard.  
	 * Note it must be cloned to be used. this should be taken care of by the destination class
	 * @usage   
	 * @return  
	 */
	public function getClipboardData():Object{
		return _clipboardData;
	}
	
	
	public function cut():Void{
		 //setClipboardData(_canvas.model.selectedItem);
	}
	
	public function copy():Void{
		trace("testing copy");
		 //setClipboardData(_canvas.model.selectedItem);
	}
	
	public function paste():Void{
		trace("testing paste");
		//_canvas.setPastedItem(getClipboardData());
	}
	
	public function get controlKeyPressed():String{
        return _controlKeyPressed;
    }

	public function getWizard():Wizard{
		return _wizard;
	}

    /**
    * Returns the Dialogue conatiner mc
    * 
    * @usage    Import authoring package and then use
	* 
    */
    static function get dialogue():MovieClip {
        //Return root if valid otherwise raise a big system error as app. will not work without it
        if(_instance._dialogueContainer_mc != undefined) {
            return _instance._dialogueContainer_mc;
        } else {
            //TODO DI 11/05/05 Raise error if mc hasn't been created
			
        }
    }	 /**
    * Returns the tooltip conatiner mc
    * 
    * @usage    Import authoring package and then use
	* 
    */
    static function get tooltip():MovieClip {
        //Return root if valid otherwise raise a big system error as app. will not work without it
        if(_instance._tooltipContainer_mc != undefined) {
            return _instance._tooltipContainer_mc;
        } else {
            //TODO DI 11/05/05 Raise error if mc hasn't been created
			
        }
    }
    	 /**
    * Returns the Cursor conatiner mc
    * 
    * @usage    Import authoring package and then use
	* 
    */
    static function get cursor():MovieClip {
        //Return root if valid otherwise raise a big system error as app. will not work without it
        if(_instance._cursorContainer_mc != undefined) {
            return _instance._cursorContainer_mc;
        } else {
            //TODO DI 11/05/05 Raise error if mc hasn't been created
			
        }
    }
	
	/**
    * Returns the Application root, use as _root would be used
    * 
    * @usage    Import authoring package and then use as root e.g.
    * 
    *           import org.lamsfoundation.lams.monitoring;
    *           Application.root.attachMovie('myLinkageId','myInstanceName',depth);
    */
    static function get root():MovieClip {
        //Return root if valid otherwise raise a big system error as app. will not work without it
        if(_instance._appRoot_mc != undefined) {
            return _instance._appRoot_mc;
        } else {
            //TODO DI 11/05/05 Raise error if _appRoot hasn't been created
			
        }
    }
}