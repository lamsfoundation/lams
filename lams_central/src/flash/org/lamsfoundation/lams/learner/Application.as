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

import org.lamsfoundation.lams.common.comms.*       //communications
import org.lamsfoundation.lams.common.util.*;		// utilities
import org.lamsfoundation.lams.common.ui.*;		// ui
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.learner.Header;
import org.lamsfoundation.lams.learner.ls.*;
import mx.managers.*
import mx.utils.*
/**
* Application - LAMS Learner Application
* @author   Mitchell Seaton
*/
class org.lamsfoundation.lams.learner.Application extends ApplicationParent {
	
	// private constants
	//private var _comms:Communication;
	private var _lesson:Lesson;
	private var _header_mc:MovieClip;
	
	
	private static var SHOW_DEBUGGER:Boolean = false;
	private static var MODULE:String = "learner";
	
    private static var QUESTION_MARK_KEY:Number = 191;
	
	//private var _appRoot_mc:MovieClip;                 //Application root clip
    
	private static var HEADER_X:Number = -1;
	private static var HEADER_Y:Number = -13.5;
	private static var LESSON_X:Number = 0;
	private static var LESSON_Y:Number = 40;
    
	
    private static var APP_ROOT_DEPTH:Number = 10; //depth of the application root
	private static var HEADER_DEPTH:Number = 20;
	
	// UI Elements
	
    private static var UI_LOAD_CHECK_INTERVAL:Number = 50;
	private static var UI_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	
	private var _uiLoadCheckCount = 0;				// instance counter for number of times we have checked to see if theme and dict are loaded
	private var _UILoadCheckIntervalID:Number;         //Interval ID for periodic check on UILoad status
    private var _UILoaded:Boolean;                     //UI Loading status
    private var _lessonLoaded:Boolean;                  //Lesson loaded flag
  	private var _headerLoaded:Boolean;

    
	
	//Application instance is stored as a static in the application class
    private static var _instance:Application = null;     
	private var _container_mc:MovieClip;               //Main container
	
    private var _debugDialog:MovieClip;                //Reference to the debug dialog
    
	
	
	
	/**
    * Application - Constructor
    */
    private function Application(){
		super(this);
		
		trace('Begin Application...');
        _lessonLoaded = false;
        //_seqLibEventDispatched = false;
		_headerLoaded = false;
		_module = Application.MODULE;
		//_headerEventDispatched = false;
        
        
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
        //_comms = new Communication();
		
		
		Key.addListener(this);
		
		setupUI();
		checkUILoaded();
    }
	
	private function setupUI():Void {
		trace('Setting up UI...');
		
		//Create the application root
        _appRoot_mc = _container_mc.createEmptyMovieClip('appRoot_mc',APP_ROOT_DEPTH);
        trace('_appRoot_mc: ' + _appRoot_mc);
		//var depth:Number = _appRoot_mc.getNextHighestDepth();
		
        _header_mc = _container_mc.attachMovie('Header','_header_mc',HEADER_DEPTH, {_x:HEADER_X,_y:HEADER_Y});
	    _header_mc.addEventListener('load',Proxy.create(this,UIElementLoaded));

		_lesson = new Lesson(_appRoot_mc,LESSON_X,LESSON_Y);
        _lesson.addEventListener('load',Proxy.create(this,UIElementLoaded));
		//_seqLib.addEventListener('init', Proxy.create(this,reload));
		
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
				//if(_seqLibLoaded && _headerLoaded){
					clearInterval(_UILoadCheckIntervalID);
					start();
				//}
				
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
                default:
            }
            
            //If all of them are loaded set UILoad accordingly
            if(_lessonLoaded && _headerLoaded){
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
			_lesson.setSize(w,h);
		}
		
		_lesson.setSize(w,h);
		
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
	
	/**
    * returns the the Comms instance
    *
    *public function getComms():Communication{
    *    return _comms;
    *}
	*/
}