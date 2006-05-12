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

//import org.lamsfoundation.lams.authoring.*       	//Design Data model n stuffimport org.lamsfoundation.lams.authoring.*       	//Design Data model n stuff
import org.lamsfoundation.lams.monitoring.*
import org.lamsfoundation.lams.monitoring.ls.*       //Lessons
import org.lamsfoundation.lams.monitoring.mv.* 		 //Monitor
//import org.lamsfoundation.lams.authoring.tb.*       //Toolbar
//import org.lamsfoundation.lams.authoring.cv.*       //Canvas
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
* Application - LAMS Application
* @author   DI
*/
class org.lamsfoundation.lams.monitoring.Application extends ApplicationParent {
	
		
	private static var SHOW_DEBUGGER:Boolean = false;
	/*
    private static var TOOLBAR_X:Number = 10;
    private static var TOOLBAR_Y:Number = 35;*/
	private static var _controlKeyPressed:String;
	private static var TOOLBAR_X:Number = 0;
    private static var TOOLBAR_Y:Number = 21;
    private static var LESSONS_X:Number = 0;
    private static var LESSONS_Y:Number = 21;
    
    private static var MONITOR_X:Number = 240;
    private static var MONITOR_Y:Number = 55;
    private static var MONITOR_W:Number = 550;
    private static var MONITOR_H:Number = 550;
    
    private static var WORKSPACE_X:Number = 200;
    private static var WORKSPACE_Y:Number = 200;
    private static var WORKSPACE_W:Number = 300;
    private static var WORKSPACE_H:Number = 200;
    
    private static var APP_ROOT_DEPTH:Number = 10; //depth of the application root
    private static var DIALOGUE_DEPTH:Number = 20;	//depth of the cursors
    private static var TOOLTIP_DEPTH:Number = 30;	//depth of the cursors
    private static var CURSOR_DEPTH:Number = 40;   //depth of the cursors
    private static var MENU_DEPTH:Number = 25;   //depth of the menu
    
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
	
	//private var _ddm:DesignDataModel;
    //private var _toolbar:Toolbar;
    private var _lessons:Lesson;
    private var _monitor:Monitor;
    private var _workspace:Workspace;
    private var _comms:Communication;
    private var _themeManager:ThemeManager;
    private var _dictionary:Dictionary;
    private var _config:Config;
    private var _debugDialog:MovieClip;                //Reference to the debug dialog
    
    
    private var _dialogueContainer_mc:MovieClip;       //Dialog container
    private var _tooltipContainer_mc:MovieClip;        //Tooltip container
    private var _cursorContainer_mc:MovieClip;         //Cursor container
    private var _menu_mc:MovieClip;                    //Menu bar clip
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
    private var _monitorLoaded:Boolean;
    private var _lessonsLoaded:Boolean;
    private var _menuLoaded:Boolean;
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
        _menuLoaded = false;
        _lessonsLoaded = false;
		_monitorLoaded = false;
		//_toolbarLoaded = false;
		//Mouse.addListener(someListener);
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
		//Cursor.addCursor(C_OPTIONAL);
		//Cursor.addCursor(C_TRANSITION);
		//Cursor.addCursor(C_GATE);
		//Cursor.addCursor(C_GROUP);
		
		
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
        //Debugger.log('UIElementLoaded: ' + evt.target.className,Debugger.GEN,'UIElementLoaded','Application');
        if(evt.type=='load'){
            //Which item has loaded
            switch (evt.target.className) {
				case 'LFMenuBar' :
                    _menuLoaded = true;
                    break;
                //case 'Toolbar' :
                //  _toolbarLoaded = true;
				//	break;
				case 'Lesson' :
					_lessonsLoaded = true;
                    break;
                case 'Monitor' :
                    _monitorLoaded = true;
                    break;
                default:
            }
            
            //If all of them are loaded set UILoad accordingly
			if(_menuLoaded && _lessonsLoaded && _monitorLoaded){
                _UILoaded=true;                
            } 
            //if(_toolkitLoaded && _canvasLoaded && _menuLoaded && _toolbarLoaded){
              //  _UILoaded=true;                
            //} 
            
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
		
		

        //MENU
        _menu_mc = _container_mc.attachMovie('LFMenuBar','_menu_mc',MENU_DEPTH, {env:'Monitoring',_x:0,_y:0});
        _menu_mc.addEventListener('load',Proxy.create(this,UIElementLoaded));

		

        //TOOLBAR
        var depth:Number = _appRoot_mc.getNextHighestDepth();
        //_toolbar = new Toolbar(_appRoot_mc,TOOLBAR_X,TOOLBAR_Y);
        //_toolbar.addEventListener('load',Proxy.create(this,UIElementLoaded));
		
		//LESSONS  
		_lessons = new Lesson(_appRoot_mc,LESSONS_X,LESSONS_Y);
        _lessons.addEventListener('load',Proxy.create(this,UIElementLoaded));
       
	   //MONITOR
        _monitor = new Monitor(_appRoot_mc,depth++,MONITOR_X,MONITOR_Y,MONITOR_W,MONITOR_H);
        _monitor.addEventListener('load',Proxy.create(this,UIElementLoaded));
        
        //WORKSPACE
        _workspace = new Workspace();
        //_workspace.addEventListener('load',Proxy.create(this,UIElementLoaded));
		
		
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
		
		_monitor.getOrganisations();
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
		
		//var someListener:Object = new Object();
		//someListener.onMouseUp = function () {
			
		//Menu - only need to worry about width
        _menu_mc.setSize(w,_menu_mc.height);

        //MONITOR
        //_monitor.setSize(w-_lessons.width,h-MONITOR_Y);
        //_toolkit.setSize(_toolkit.width,h-TOOLKIT_Y);

        //Toolbar
        //_toolbar.setSize(null,_toolbar.height);
		//Property Inspector
		//var pi = _canvas.getPropertyInspector();
		//pi._y = h;//- pi._height;
		//pi._y = h - 210;
		
		//}
		//Mouse.addListener(someListener);
       
       //Canvas
	   _lessons.setSize(_lessons.width,h-LESSONS_Y);
        _monitor.setSize(w-MONITOR_X,h-MONITOR_Y);
        

        //Toolbar
        //_toolbar.setSize(w,_toolbar.height);
		//Property Inspector
		//var pi = _canvas.getPropertyInspector();
		//pi._y = h;//- pi._height;
		//pi._y = h - 210;
		
		
		
		
		
    }
	
	/**
    * Handles KEY Releases for Application
    */
	
	public function transition_keyPressed(){
		_controlKeyPressed = "transition";
		//if(_canvas.model.activeTool != "TRANSITION"){
		//	_canvas.toggleTransitionTool();
		//}
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
	

    
	/**
	* get the ddm from the canvas.. this method is here as the ddm used to be stored inthe application.
    * returns the the Design Data Model
    
    public function getDesignDataModel():DesignDataModel{
        return  _canvas.ddm;
    }
	*/
    /**
    * returns the the toolkit instance
    */
    //public function getToolkit():Toolkit{
     //   return _toolkit;
   // }

    /**
    * returns the the toolbar instance
    */
   // public function getToolbar():Toolbar{
    //    return _toolbar;
    //}

    /**
    * returns the the canvas instance
    */
    public function getMonitor():Monitor{
        return _monitor;
    }
	public function get controlKeyPressed():String{
        return _controlKeyPressed;
    }
   
	
	/**
	* returns the lesson instance
	*/
	public function getLesson():Lesson{
		return _lessons;
	}

    /**
    * Returns the Application root, use as _root would be used
    * 
    * @usage    Import authoring package and then use as root e.g.
    * 
    *           import org.lamsfoundation.lams.authoring;
    *           Application.root.attachMovie('myLinkageId','myInstanceName',depth);
    */
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