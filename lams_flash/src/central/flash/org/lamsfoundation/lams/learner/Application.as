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

import org.lamsfoundation.lams.common.comms.*;      //communications
import org.lamsfoundation.lams.common.dict.*;		// dictionary
import org.lamsfoundation.lams.common.style.*;		// styles/themes
import org.lamsfoundation.lams.common.util.*;		// utilities
import org.lamsfoundation.lams.common.ui.*;		// ui
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import mx.managers.*
import mx.utils.*
/**
* Application - LAMS Learner Application
* @author   Mitchell Seaton
*/
class Application extends ApplicationParent {
	
	// private constants
	//private var _comms:Communication;
	private var _lesson:Lesson;
	private var _header_mc:MovieClip;
	private var _scratchpad_mc:MovieClip;

	private static var SHOW_DEBUGGER:Boolean = false;
	private static var MODULE:String = "learner";
	
    private static var QUESTION_MARK_KEY:Number = 191;
	
	public static var HEADER_X:Number = 0;
	public static var HEADER_Y:Number = 0;
	public static var LESSON_X:Number = 0;
	public static var LESSON_Y:Number = 82;
	public static var SPAD_X:Number = 0;
	public static var SPAD_Y:Number = 554;
	public static var SPAD_H:Number = 220;
    
	
    private static var APP_ROOT_DEPTH:Number = 10; //depth of the application root
	private static var HEADER_DEPTH:Number = 20;
	private static var TOOLTIP_DEPTH:Number = 60;	//depth of the cursors
	
	private static var CCURSOR_DEPTH:Number = 101;
	
	// UI Elements
	
    private static var UI_LOAD_CHECK_INTERVAL:Number = 50;
	private static var UI_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	private static var DATA_LOAD_CHECK_INTERVAL:Number = 50;
	private static var DATA_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	
	private var _uiLoadCheckCount = 0;				// instance counter for number of times we have checked to see if theme and dict are loaded
	private var _dataLoadCheckCount = 0;			
	
	private var _UILoadCheckIntervalID:Number;         //Interval ID for periodic check on UILoad status
    private var _DataLoadCheckIntervalID:Number;
	
	private var _dictionaryLoaded:Boolean;             //Dictionary loaded flag
    private var _dictionaryEventDispatched:Boolean     //Event status flag
    private var _themeLoaded:Boolean;                  //Theme loaded flag
    private var _themeEventDispatched:Boolean          //Dictionary loaded flag
	
	private var _UILoaded:Boolean;                     //UI Loading status
    
	private var _lessonLoaded:Boolean;                  //Lesson loaded flag
  	private var _headerLoaded:Boolean;
	private var _scratchpadLoaded:Boolean;
    
	
	//Application instance is stored as a static in the application class
    private static var _instance:Application = null;     
	private var _container_mc:MovieClip;               //Main container
	private var _tooltipContainer_mc:MovieClip;        //Tooltip container
    private var _debugDialog:MovieClip;                //Reference to the debug dialog


	/**
    * Application - Constructor
    */
    private function Application(){
		super(this);
		
        _lessonLoaded = false;
		_headerLoaded = false;
		_scratchpadLoaded = false;
		
		_module = Application.MODULE;
 
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
		
		_customCursor_mc = _container_mc.createEmptyMovieClip('_customCursor_mc', CCURSOR_DEPTH);			
		
		//add the cursors:
		Cursor.addCursor(C_HOURGLASS);

    	//Get the instance of config class
        _config = Config.getInstance();
        
        //Assign the config load event to
        _config.addEventListener('load',Delegate.create(this,configLoaded));
        
		
		Key.addListener(this);

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
				Debugger.log('reached timeout waiting for data to load.',Debugger.CRITICAL,'checkDataLoaded','Application');
				clearInterval(_DataLoadCheckIntervalID);
				
        
			}
		}
	}
    
	
	private function setupUI():Void {
		trace('Setting up UI...');
		
		//Create the application root
        _appRoot_mc = _container_mc.createEmptyMovieClip('appRoot_mc',APP_ROOT_DEPTH);
        
        _header_mc = _appRoot_mc.createChildAtDepth('LHeader', DepthManager.kTop, {_x:HEADER_X,_y:HEADER_Y});
		_tooltipContainer_mc = _container_mc.createEmptyMovieClip('_tooltipContainer_mc',TOOLTIP_DEPTH);
	    _header_mc.addEventListener('load',Proxy.create(this,UIElementLoaded));

		_lesson = new Lesson(_appRoot_mc,LESSON_X,LESSON_Y);
        _lesson.addEventListener('load',Proxy.create(this,UIElementLoaded));
		
		_scratchpad_mc = _container_mc.createChildAtDepth('LScratchPad', DepthManager.kTop, {_x:SPAD_X, _y:SPAD_Y, _lessonModel:_lesson.model, _lessonController:_lesson.view.getController()});
		_scratchpad_mc.addEventListener('load', Proxy.create(this, UIElementLoaded));
	}
	
	/**
    * Runs periodically and dispatches events as they are ready
    */
    private function checkUILoaded() {
		trace('checking UI loaded...');
        //If it's the first time through then set up the interval to keep polling this method
        if(!_UILoadCheckIntervalID) {
            _UILoadCheckIntervalID = setInterval(Proxy.create(this,checkUILoaded),UI_LOAD_CHECK_INTERVAL);
        } else {
			_uiLoadCheckCount++;
            
			//If UI loaded check which events can be broadcast
			if(_UILoaded){
				clearInterval(_UILoadCheckIntervalID);
				start();
				
				if(_uiLoadCheckCount >= UI_LOAD_CHECK_TIMEOUT_COUNT){
					//if we havent loaded the library by the timeout count then give up
					Debugger.log('raeached time out waiting to load library, giving up.',Debugger.CRITICAL,'checkUILoaded','Application');
					clearInterval(_UILoadCheckIntervalID);
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
                case 'Lesson' :
					trace('Lesson loaded...');
                    _lessonLoaded = true;
                    break;
				case 'Header' :
					trace('Header loaded...');
					_headerLoaded = true;
					break;
				case 'Scratchpad' :
					trace('Scratchpad loaded...');
					_scratchpadLoaded = true;
					break;
                default:
            }
            
            //If all of them are loaded set UILoad accordingly
            if(_lessonLoaded && _headerLoaded && _scratchpadLoaded){
                _UILoaded=true;                
            } 
            
        }   
    }
	
	/**
    * Runs when application setup has completed.  At this point the init/loading screen can be removed and the user can
    * work with the application
    */
    private function start(){
		trace('starting...');
		
        //Fire off a resize to set up sizes
        onResize();
		
		if(SHOW_DEBUGGER){
			showDebugger();
		}
		
		// load lesson
		_lesson.getLesson();
	}
    
	/**
    * Receives events from the Stage resizing
    */
    public function onResize(){
		
		//Get the stage width and height and call onResize for stage based objects
        var w:Number = Stage.width;
        var h:Number = Stage.height;
		
		var someListener:Object = new Object();
		someListener.onMouseUp = function () {
			_lesson.setSize(w,h-(LESSON_Y+_lesson.model.getSpadHeight()));
			
			_scratchpad_mc._y = h - _lesson.model.getSpadHeight();
		}
		
		Header(_header_mc).resize(w);
		
		_lesson.setSize(w,h-(LESSON_Y+_lesson.model.getSpadHeight()));
		//Property Inspector
		//_pi_mc.setSize(w-_toolkit.width,_pi_mc._height)
		_scratchpad_mc._y = h - _lesson.model.getSpadHeight();
		
	}
	
	/**
	 * Updated the progress data in the lesson model with received progress data
	 * 
	 * @param   attempted 
	 * @param   completed 
	 * @param   current 
	 */
	
	public function refreshProgress(attempted:String, completed:String, current:String, lessonID:String, version:Number){
		Debugger.log('attempted: ' + attempted,Debugger.CRITICAL,'refreshProgress','Application');
		Debugger.log('completed: ' + completed,Debugger.CRITICAL,'refreshProgress','Application');
		Debugger.log('current: ' + current,Debugger.CRITICAL,'refreshProgress','Application');
		Debugger.log('version: ' + version,Debugger.CRITICAL,'refreshProgress','Application');
        Debugger.log('_root lesson ID: ' + _root.lessonID + ' passed in lesson ID: ' + lessonID,Debugger.CRITICAL,'refreshProgress','Application');
        //Debugger.log('_root unique ID: ' + _root.uniqueID + ' passed in unique ID: ' + uniqueID,Debugger.CRITICAL,'refreshProgress','Application');
        
		if(_root.lessonID == lessonID){
			var attemptedArray:Array = attempted.split("_");
			var completedArray:Array = completed.split("_");
			if(_lesson.model.learningDesignModel != null) {
				if(version != null && version != _lesson.model.learningDesignModel.designVersion) {
					// TODO apply progress data arrays after design is reloaded instead of re-getting the flash progress data
					_lesson.reloadLearningDesign();
				} else {
					_lesson.updateProgressData(attemptedArray, completedArray, Number(current));
				}
			}
		}
	}
     
	 
	/**
    * Returns the tooltip conatiner mc
    * 
    * @usage    Import monioring package and then use
	* 
    */
    static function get tooltip():MovieClip {
		trace("tooltip called")
        //Return root if valid otherwise raise a big system error as app. will not work without it
        if(_instance._tooltipContainer_mc != undefined) {
            return _instance._tooltipContainer_mc;
        } else {
            //TODO DI 11/05/05 Raise error if mc hasn't been created
			
        }
    }
    
	
	// onKey*** methods - TODO
	
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
	
	public function getLesson():Lesson{
		return _lesson;
	}
	
	public function getHeader():Header{
		return Header(_header_mc);
	}
	
	public function getScratchpad():Scratchpad{
		return Scratchpad(_scratchpad_mc);
	}
	
	public function showDebugger():Void{
		_debugDialog = PopUpManager.createPopUp(Application.root, LFWindow, false,{title:'Debug',closeButton:true,scrollContentPath:'debugDialog'});
	}
	
	public function hideDebugger():Void{
		_debugDialog.deletePopUp();
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
        }
	}

}