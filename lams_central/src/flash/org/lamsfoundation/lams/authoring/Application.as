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
class org.lamsfoundation.lams.authoring.Application {
	
	//public constants
	public static var C_HOURGLASS = "c_hourglass";
	public static var C_TRANSITION = "c_pen";
	public static var C_OPTIONAL = "c_optionalBoxPen";
	public static var C_DEFAULT = "default";
	
    private static var TOOLBAR_X:Number = 10;
    private static var TOOLBAR_Y:Number = 17;    private static var TOOLKIT_X:Number = 0;
    private static var TOOLKIT_Y:Number = 74;
    
    private static var CANVAS_X:Number = 180;
    private static var CANVAS_Y:Number = 74;
    private static var CANVAS_W:Number = 1000;
    private static var CANVAS_H:Number = 200;
    
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
    
    private static var QUESTION_MARK_KEY:Number = 191;
	
	private var _ddm:DesignDataModel;
    private var _toolbar:Toolbar;
    private var _toolkit:Toolkit;
    private var _canvas:Canvas;
    private var _workspace:Workspace;
    private var _comms:Communication;
    private var _themeManager:ThemeManager;
    private var _dictionary:Dictionary;
    private var _config:Config;
    private var _debugDialog:MovieClip;                //Reference to the debug dialog
    
    
    private var _appRoot_mc:MovieClip;                 //Application root clip
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
    
    //UI Elements
    private var _toolbarLoaded:Boolean;                //These are flags set to true when respective element is 'loaded'
    private var _canvasLoaded:Boolean;
    private var _toolkitLoaded:Boolean;
    private var _menuLoaded:Boolean;
    
    //Application instance is stored as a static in the application class
    private static var _instance:Application = null;     

    /**
    * Application - Constructor
    */
    private function Application(){
        _themeLoaded = false;
        _themeEventDispatched = false;
        _dictionaryLoaded = false;
        _dictionaryEventDispatched = false;
        _toolkitLoaded = false;
        _canvasLoaded  = false;
        _menuLoaded = false;
        _toolbarLoaded = false;  
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
        
		//Comms object - do this before any objects are created that require it for server communication
        _comms = new Communication();

    	//Get the instance of config class
        _config = Config.getInstance();
        
        //Assign the config load event to
        _config.addEventListener('load',Delegate.create(this,configLoaded));
        
        //Set up Key handler 
        //TODO take out after testing and uncomment same key handler in ready();
        Key.addListener(this);
    }
    
    /**
    * Called when the config class has loaded
    */
    private function configLoaded(){
        //Now that the config class is ready setup the UI and data, call to setupData() first in 
		//case UI element constructors use objects instantiated with setupData()
        setupData();
        setupUI();
        //Start off polling check for UI load status
        checkUILoaded();
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
        
		//Design Data Model.
		_ddm = new DesignDataModel();
		
        //Set reference to StyleManager and load Themes and setup load handler.
        var theme:String = String(_config.getItem('theme'));
        _themeManager = ThemeManager.getInstance();
        _themeManager.addEventListener('load',Delegate.create(this,onThemeLoad));
        _themeManager.loadTheme(theme);
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
    * Runs periodically and dispatches events as they are ready
    */
    private function checkUILoaded() {
        //If it's the first time through then set up the interval to keep polling this method
        if(!_UILoadCheckIntervalID) {
            _UILoadCheckIntervalID = setInterval(Proxy.create(this,checkUILoaded),UI_LOAD_CHECK_INTERVAL);
        } else {
            //If all events dispatched clear interval and call start()
            if(_dictionaryEventDispatched && _themeEventDispatched){
				Debugger.log('Clearing Interval and calling start :',Debugger.CRITICAL,'checkUILoaded','Application');	
                clearInterval(_UILoadCheckIntervalID);
				start();
            }else {
                //If UI loaded check which events can be broadcast
                if(_UILoaded){
					Debugger.log('ALL UI LOADED',Debugger.GEN,'checkUILoaded','Application');

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
        Debugger.log('UIElementLoaded: ' + evt.target.className,Debugger.GEN,'UIElementLoaded','Application');
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
                default:
            }
            
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
		var root_cm:ContextMenu = new ContextMenu();  
		root_cm.hideBuiltInItems();  
		_root.menu = root_cm;   
        
        //Create the application root
        _appRoot_mc = _container_mc.createEmptyMovieClip('appRoot_mc',APP_ROOT_DEPTH);
        //Create screen elements
        _dialogueContainer_mc = _container_mc.createEmptyMovieClip('_dialogueContainer_mc',DIALOGUE_DEPTH);
        _tooltipContainer_mc = _container_mc.createEmptyMovieClip('_tooltipContainer_mc',TOOLTIP_DEPTH);
        _cursorContainer_mc = _container_mc.createEmptyMovieClip('_cursorContainer_mc',CURSOR_DEPTH);
		
		//add the cursors:
		Cursor.addCursor(C_HOURGLASS);
		Cursor.addCursor(C_OPTIONAL);
		Cursor.addCursor(C_TRANSITION);

        //MENU
        _menu_mc = _container_mc.attachMovie('LFMenuBar','_menu_mc',MENU_DEPTH, {_x:0,_y:0});
        _menu_mc.addEventListener('load',Proxy.create(this,UIElementLoaded));

        //TOOLBAR
        var depth:Number = _appRoot_mc.getNextHighestDepth();
        _toolbar = new Toolbar(_appRoot_mc,TOOLBAR_X,TOOLBAR_Y);
        _toolbar.addEventListener('load',Proxy.create(this,UIElementLoaded));

        //CANVAS
        _canvas = new Canvas(_appRoot_mc,depth++,CANVAS_X,CANVAS_Y,CANVAS_W,CANVAS_H);
        _canvas.addEventListener('load',Proxy.create(this,UIElementLoaded));
        
        //WORKSPACE
        _workspace = new Workspace();
        //_workspace.addEventListener('load',Proxy.create(this,UIElementLoaded));

		//TOOLKIT  
		_toolkit = new Toolkit(_appRoot_mc,depth++,TOOLKIT_X,TOOLKIT_Y);
        _toolkit.addEventListener('load',Proxy.create(this,UIElementLoaded));
    }
    
    /**
    * Runs when application setup has completed.  At this point the init screen can be removed and the user can
    * work with the application
    */
    private function start(){
        //Fire off a resize to set up sizes
        onResize();
		//TODO Remove loading screen
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
        
        //Menu - only need to worry about width
        _menu_mc.setSize(w,_menu_mc._height);

        //Canvas
        _canvas.setSize(w-_toolkit.width,h-CANVAS_Y);
        _toolkit.setSize(_toolkit.width,h-TOOLKIT_Y);

        //Toolbar
        //_toolbar.setSize(w,_toolbar.height);
    }
    
    /**
    * Handles KEY presses for Application
    */
    private function onKeyDown(){
        
		//the debug window:
        if (Key.isDown(Key.CONTROL) && Key.isDown(Key.ALT) && Key.isDown(QUESTION_MARK_KEY)) {
            if (!_debugDialog.content){
                _debugDialog = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:'Debug',closeButton:true,scrollContentPath:'debugDialog'});
            }else {
               _debugDialog.deletePopUp();
            }                
        }
    }
    
	/**
    * returns the the Design Data Model
    */
    public function getDesignDataModel():DesignDataModel{
        return _ddm;
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
	
    /**
    * returns the the workspace instance
    */
    public function getWorkspace():Workspace{
        return _workspace;
    }

    /**
    * returns the the Comms instance
    */
    public function getComms():Communication{
        return _comms;
    }
    
    /**
    * returns the the Dictionary instance
    */
    public function getDictionary():Dictionary{
        return _dictionary;
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
}