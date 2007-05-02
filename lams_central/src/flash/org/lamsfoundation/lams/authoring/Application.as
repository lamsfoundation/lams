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

import org.lamsfoundation.lams.authoring.*       	//Design Data model n stuffimport org.lamsfoundation.lams.authoring.*       	//Design Data model n stuff
import org.lamsfoundation.lams.authoring.tk.*       //Toolkit
import org.lamsfoundation.lams.authoring.tb.*       //Toolbar
import org.lamsfoundation.lams.authoring.cv.*       //Canvas
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
class org.lamsfoundation.lams.authoring.Application extends ApplicationParent {
	
	private static var SHOW_DEBUGGER:Boolean = false;
	
	private static var MODULE:String = "authoring";

	private static var _controlKeyPressed:String;
	public static var TOOLBAR_X:Number = 0;
    public static var TOOLBAR_Y:Number = 21;
	public static var TOOLBAR_HEIGHT:Number = 35;

    public static var TOOLKIT_X:Number = 0;
    public static var TOOLKIT_Y:Number = 55;
    
    public static var CANVAS_X:Number = 180;
    public static var CANVAS_Y:Number = 55;
    public static var CANVAS_W:Number = 1000;
    public static var CANVAS_H:Number = 200;
    
	public static var PI_X:Number = 180;
	public static var PI_Y:Number = 551;
	public static var PI_W:Number = 616;
	
    public static var WORKSPACE_X:Number = 200;
    public static var WORKSPACE_Y:Number = 200;
    public static var WORKSPACE_W:Number = 300;
    public static var WORKSPACE_H:Number = 200;
    
	private static var LOADING_ROOT_DEPTH:Number = 100;	//depth of the loading movie
    private static var APP_ROOT_DEPTH:Number = 10; //depth of the application root
    private static var DIALOGUE_DEPTH:Number = 20;	//depth of the dialogue box
    private static var TOOLTIP_DEPTH:Number = 60;	//depth of the tooltip
    private static var CURSOR_DEPTH:Number = 70;   //depth of the cursors
	private static var CCURSOR_DEPTH:Number = 201;
    private static var MENU_DEPTH:Number = 25;   //depth of the menu
	private static var PI_DEPTH:Number = 35;   //depth of the menu
    private static var TOOLBAR_DEPTH:Number = 50;   //depth of the menu
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
	private static var F12_KEY:Number = 123;
	
	public static var CUT_TYPE:Number = 0;
	public static var COPY_TYPE:Number = 1;
	
	private static var COMPONENT_NO = 9;

	private var _uiLoadCheckCount = 0;				// instance counter for number of times we have checked to see if theme and dict are loaded
	private var _dataLoadCheckCount = 0;			
	
	private var _ddm:DesignDataModel;
    private var _toolbar:Toolbar;
    private var _toolkit:Toolkit;
    private var _canvas:Canvas;
    private var _workspace:Workspace;
	private var _PI:PropertyInspectorNew;
	private var _ccm:CustomContextMenu;
	private var _debugDialog:MovieClip;                //Reference to the debug dialog
    
    private var _dialogueContainer_mc:MovieClip;       //Dialog container
    private var _tooltipContainer_mc:MovieClip;        //Tooltip container
    private var _cursorContainer_mc:MovieClip;         //Cursor container
    private var _menu_mc:MovieClip;                    //Menu bar clip
    private var _container_mc:MovieClip;              //Main container
	private var _pi_mc:MovieClip;
    private var _toolbarContainer_mc:MovieClip;		//Container for Toolbar
    private var _UILoadCheckIntervalID:Number;         //Interval ID for periodic check on UILoad status
    private var _UILoaded:Boolean;                     //UI Loading status
    
	private var _DataLoadCheckIntervalID:Number;
	
    //UI Elements
    private var _toolbarLoaded:Boolean;                //These are flags set to true when respective element is 'loaded'
    private var _canvasLoaded:Boolean;
    private var _toolkitLoaded:Boolean;
    private var _menuLoaded:Boolean;
	private var _showCMItem:Boolean;
	private var _piLoaded:Boolean;
	//clipboard
	private var _clipboardData:Object;
	private var _clipboardPasteCount:Number;
	
	// set up Key Listener
	//private var keyListener:Object;
    
    //Application instance is stored as a static in the application class
    private static var _instance:Application = null;     

    /**
    * Application - Constructor
    */
    private function Application(){
		super(this);
		_toolkitLoaded = false;
        _canvasLoaded  = false;
        _menuLoaded = false;
        _toolbarLoaded = false;  
		_piLoaded = false;
		_module = Application.MODULE;
		_PI = new PropertyInspectorNew();
		_ccm = CustomContextMenu.getInstance();
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

		loader.start(COMPONENT_NO);
		
		_customCursor_mc = _container_mc.createEmptyMovieClip('_customCursor_mc', CCURSOR_DEPTH);			
		
		//add the cursors:
		Cursor.addCursor(C_HOURGLASS);
		Cursor.addCursor(C_OPTIONAL);
		Cursor.addCursor(C_TRANSITION);
		Cursor.addCursor(C_GATE);
		Cursor.addCursor(C_GROUP);
		
		
		//Get the instance of config class
        _config = Config.getInstance();
		
        //Assign the config load event to
        _config.addEventListener('load',Delegate.create(this,configLoaded));
        
        //Set up Key handler 
        //TODO take out after testing and uncomment same key handler in ready();
        Key.addListener(this);
		_container_mc.tabChildren = true;
    }
    
    /**
    * Called when the config class has loaded
    */
    private function configLoaded(){
        //Now that the config class is ready setup the UI and data, call to setupData() first in 
		//case UI element constructors use objects instantiated with setupData()
        loader.complete();
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
                case 'Toolkit' :
                    _toolkitLoaded = true;
                    break;
                case 'Canvas' :
                    _canvasLoaded = true;
                    break;
                case 'LFMenuBar' :
                    _menuLoaded = true;
                    break;
                case 'Toolbar' :
                    _toolbarLoaded = true;
                    break;
				case 'PropertyInspectorNew' :
                    _piLoaded = true;
                    break;
                default:
            }
			
			loader.complete();
            
            //If all of them are loaded set UILoad accordingly
            if(_toolkitLoaded && _canvasLoaded && _menuLoaded && _toolbarLoaded){
                _UILoaded=true;                
            } 
            
        }   
    }
    
		
    /**
    * Create all UI Elements
    */
    private function setupUI(){
		//Make the base context menu hide built in items so we don't have zoom in etc 
		_ccm.showCustomCM(_ccm.loadMenu("application", "authoring"))
		
        //Create the application root
        _appRoot_mc = _container_mc.createEmptyMovieClip('appRoot_mc',APP_ROOT_DEPTH);
        //Create screen elements
        _dialogueContainer_mc = _container_mc.createEmptyMovieClip('_dialogueContainer_mc',DIALOGUE_DEPTH);
        _cursorContainer_mc = _container_mc.createEmptyMovieClip('_cursorContainer_mc',CURSOR_DEPTH);			
 		_toolbarContainer_mc = _container_mc.createEmptyMovieClip('_toolbarContainer_mc',TOOLBAR_DEPTH);
		_pi_mc = _container_mc.createEmptyMovieClip('_pi_mc',PI_DEPTH);
		
		_tooltipContainer_mc = _container_mc.createEmptyMovieClip('_tooltipContainer_mc',TOOLTIP_DEPTH);

        //MENU
        _menu_mc = _container_mc.attachMovie('LFMenuBar','_menu_mc',MENU_DEPTH, {_x:0,_y:0});
        _menu_mc.addEventListener('load',Proxy.create(this,UIElementLoaded));

        
        var depth:Number = _appRoot_mc.getNextHighestDepth();
		
		//TOOLBAR
        _toolbar = new Toolbar(_toolbarContainer_mc,_toolbarContainer_mc.getNextHighestDepth(),TOOLBAR_X,TOOLBAR_Y);
        _toolbar.addEventListener('load',Proxy.create(this,UIElementLoaded));
		
		//CANVAS
        _canvas = new Canvas(_appRoot_mc,depth++,CANVAS_X,CANVAS_Y,CANVAS_W,495);
        _canvas.addEventListener('load',Proxy.create(this,UIElementLoaded));
        
		//WORKSPACE
        _workspace = new Workspace();
        //_workspace.addEventListener('load',Proxy.create(this,UIElementLoaded));

		//TOOLKIT  
		_toolkit = new Toolkit(_appRoot_mc,depth++,TOOLKIT_X,TOOLKIT_Y);
        _toolkit.addEventListener('load',Proxy.create(this,UIElementLoaded));
		
		_pi_mc = _pi_mc.attachMovie('PropertyInspectorNew','_pi_mc',PI_DEPTH, {_x:PI_X,_y:PI_Y, _canvasModel:_canvas.model, _canvasController:_canvas.view.getController()});
		_pi_mc.addEventListener('load',Proxy.create(this,UIElementLoaded));
		
		setTabIndex();
    }
    
	private function setTabIndex(selectedTab:String){
		
		//All Buttons Tab Index
		_menu_mc.tabIndex = 100;
		_toolbarContainer_mc.tabIndex = 200;
		//_toolkit.tabIndex = 3;
		_pi_mc.tabIndex = 400;
	}
	
    /**
    * Runs when application setup has completed.  At this point the init/loading screen can be removed and the user can
    * work with the application
    */
    private function start(){
		
        //Fire off a resize to set up sizes
        onResize();
		
		//Remove the loading screen
		loader.stop();
		
		if(SHOW_DEBUGGER){
			showDebugger();
		}
		
		if(getCanvas().getCanvasModel().autoSaveWait) {
			// enable menu item - recover...
			LFMenuBar.getInstance().enableRecover(true);
		}
		
    }
    
    /**
    * Opens the preferences dialog
    */
    public function showPrefsDialog() {
        PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue("prefs_dlg_title"),closeButton:true,scrollContentPath:'preferencesDialog'});
    }
	
    /**
    * Receives events from the Stage resizing
    */
    public function onResize(){
        //Debugger.log('onResize',Debugger.GEN,'main','org.lamsfoundation.lams.Application');

        //Get the stage width and height and call onResize for stage based objects
        var w:Number = Stage.width;
        var h:Number = Stage.height;
		
		var someListener:Object = new Object();
		trace("onResize called")
		someListener.onMouseUp = function () {
			
			//Menu - only need to worry about width
			_menu_mc.setSize(w,_menu_mc._height);

			//Canvas
			_canvas.setSize(w-_toolkit.width,h-(CANVAS_Y+_canvas.model.getPIHeight()));
			_toolkit.setSize(_toolkit.width,h-TOOLKIT_Y);
			
			//Toolbar
			_toolbar.setSize(w, TOOLBAR_HEIGHT);
			
			//Property Inspector
			_pi_mc.setSize(w-_toolkit.width,_pi_mc._height)
			_pi_mc._y = h - _canvas.model.getPIHeight();
			_pi_mc.showExpand(false);
			
			//var pi = _canvas.getPropertyInspector();
			//pi._y = h;//- pi._height;
			//pi._y = h - 210;
			
		}
		//Mouse.addListener(someListener);


        
        //Menu - only need to worry about width
        _menu_mc.setSize(w,_menu_mc._height);

        //Canvas
        _toolkit.setSize(_toolkit.width,h-TOOLKIT_Y);
		_canvas.setSize(w-_toolkit.width,h-(CANVAS_Y+_canvas.model.getPIHeight()));
       //Toolbar
        _toolbar.setSize(w, TOOLBAR_HEIGHT);
		//Property Inspector
		_pi_mc.setSize(w-_toolkit.width,_pi_mc._height)
		_pi_mc._y = h - _canvas.model.getPIHeight();
		var piHeight:Number = _canvas.model.getPIHeight();
		_pi_mc.showExpand(false)
		if (piHeight != _pi_mc.piFullHeight()){
			_pi_mc.showExpand(true);
		}
		
		//var pi = _canvas.getPropertyInspector();
		//pi._y = h;//- pi._height;
		//pi._y = h - 210;
		
		
		
		
		
    }
	
	/**
    * Handles KEY Releases for Application
    */
	private function onKeyUp(){
		Debugger.log('Key released.',Debugger.GEN,'onKeyUp','Application');
		if(!Key.isDown(Key.CONTROL)) {
			if(_controlKeyPressed == ApplicationParent.TRANSITION) {
				Debugger.log('Control Key released.',Debugger.GEN,'onKeyUp','Application');
				
				var c:String = Cursor.getCurrentCursor();
				
				if(c == ApplicationParent.C_TRANSITION){	
					_controlKeyPressed = "";
					_canvas.stopTransitionTool();
				}
			}
		}
		
	}
    
    /**
    * Handles KEY presses for Application
    */
    private function onKeyDown(){
		
		//var mouseListener:Object = new Object();
        //Debugger.log('Key.isDown(Key.CONTROL): ' + Key.isDown(Key.CONTROL),Debugger.GEN,'onKeyDown','Application');
        //Debugger.log('Key: ' + Key.getCode(),Debugger.GEN,'onKeyDown','Application');
		//the debug window:
        if (Key.isDown(Key.CONTROL) && Key.isDown(Key.ALT) && Key.isDown(QUESTION_MARK_KEY)) {
            if (!_debugDialog.content){
                showDebugger();
            }else {
               hideDebugger();
            }               
        }else if (Key.isDown(Key.CONTROL) && Key.isDown(X_KEY)) {
			//for copy and paste
			//assuming that we are in the canvas...
             cut();
        }else if (Key.isDown(Key.CONTROL) && Key.isDown(C_KEY)) {
           copy();
        }else if (Key.isDown(F12_KEY)) {
			trace("P Pressed")
			PropertyInspectorNew(_pi_mc).localOnRelease();
			
        }else if (Key.isDown(Key.CONTROL) && Key.isDown(V_KEY)) {
			paste();
			
        }else if (Key.isDown(Key.CONTROL) && Key.isDown(Z_KEY)) {
			//undo
			_canvas.undo();
			
        }else if (Key.isDown(Key.CONTROL) && Key.isDown(Y_KEY)) {
			
			
        }else if(Key.isDown(Key.CONTROL)) {
			var c:String = Cursor.getCurrentCursor();
			if(c != ApplicationParent.C_TRANSITION){	
				_controlKeyPressed = ApplicationParent.TRANSITION;
				_canvas.startTransitionTool()
			}
			
			
	    }
		
    }
	
	public function transition_keyPressed(){
		_controlKeyPressed = "transition";
		if(_canvas.model.activeTool != "TRANSITION"){
			_canvas.toggleTransitionTool();
		}
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
	public function setClipboardData(obj:Object, type:Number):Void{
		// initialise new clipboard object
		_clipboardData = new Object();
		_clipboardData.data = obj;
		_clipboardData.type = type;
		_clipboardData.count = 0;
		
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
		trace("testing cut");
		var ca = _canvas.model.selectedItem
		if (CanvasActivity(ca) != null){
			if (ca.activity.parentUIID == null || ca.activity.parentUIID == undefined){
				setClipboardData(ca, CUT_TYPE);
			}else {
				LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_cut_invalid'));
			}
		}else {
			LFMessage.showMessageAlert(Dictionary.getValue('al_activity_copy_invalid'));
		}
		//_canvas.removeActivity(_canvas.model.selectedItem.activity.activityUIID);
	}
	
	public function copy():Void{
		trace("testing copy");
		var ca = _canvas.model.selectedItem
		if (CanvasActivity(ca) != null){
			if (ca.activity.parentUIID == null || ca.activity.parentUIID == undefined){
				setClipboardData(ca, COPY_TYPE);
			}else {
				LFMessage.showMessageAlert(Dictionary.getValue('cv_activity_copy_invalid'));
			}
		}else{
			LFMessage.showMessageAlert(Dictionary.getValue('al_activity_copy_invalid'));
		}
	}
	
	public function openEditActivtiyContent():Void{
		trace("testing openEditActivtiyContent");
		var ca = _canvas.model.selectedItem
		if (CanvasActivity(ca) != null){ 
			_canvas.view.getController().activityDoubleClick(ca);
		}else {
			LFMessage.showMessageAlert(Dictionary.getValue('al_activity_openContent_invalid'));
		}
	}
	
	public function paste():Void{
		trace("testing paste");
		_clipboardData.count++;
		_canvas.setPastedItem(getClipboardData());
	}
	
	public function expandPI():Void{
		if (!_PI.isPIExpanded()){
			_canvas.model.setPIHeight(_PI.piFullHeight());
		}
	}
	
	public function help():Void{
		var ca = _canvas.model.selectedItem
		if (CanvasActivity(ca) != null){
			_canvas.getHelp(ca);
		}
	}
    
	/**
	* get the ddm form the canvas.. this method is here as the ddm used to be stored inthe application.
    * returns the the Design Data Model
    */
    public function getDesignDataModel():DesignDataModel{
        return  _canvas.ddm;
    }
	
    /**
    * returns the the toolkit instance
    */
    public function getToolkit():Toolkit{
        return _toolkit;
    }

    /**
    * returns the the toolbar instance
    */
    public function getToolbar():Toolbar{
        return _toolbar;
    }

    /**
    * returns the the canvas instance
    */
    public function getCanvas():Canvas{
        return _canvas;
    }
	public function get controlKeyPressed():String{
        return _controlKeyPressed;
    }
	
	public function set controlKeyPressed(key:String){
        _controlKeyPressed = key;
    }
    /**
    * returns the the workspace instance
    */
    public function getWorkspace():Workspace{
        return _workspace;
    }

   /**
    * Opens the help->about dialog
    */
    public function showAboutDialog() {
        var dialog:MovieClip = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:"About - LAMS Author",closeButton:true,scrollContentPath:'helpaboutDialog'});
		//dialog.addEventListener('contentLoaded',);
		// dict: title:Dictionary.getValue('ls_win_helpabout_title')
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
    }

	 /**
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
    *           import org.lamsfoundation.lams.authoring;
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