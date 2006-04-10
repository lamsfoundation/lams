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
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.lb.*;
import mx.managers.*
import mx.utils.*
/**
* Application - LAMS Learner Application
* @author   Mitchell Seaton
*/
class org.lamsfoundation.lams.learner.Application {
	
	// public constants
	
	// private constants
	private var _comms:Communication;
	private var _seqLib:Library;
	
	private var _appRoot_mc:MovieClip;                 //Application root clip
    
	
	private static var LIBRARY_X:Number = 0;
	private static var LIBRARY_Y:Number = 0;
    
	
    private static var APP_ROOT_DEPTH:Number = 10; //depth of the application root
	
	// UI Elements
	
    private static var UI_LOAD_CHECK_INTERVAL:Number = 50;
	private static var UI_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	
	private var _uiLoadCheckCount = 0;				// instance counter for number of times we have checked to see if theme and dict are loaded
	private var _UILoadCheckIntervalID:Number;         //Interval ID for periodic check on UILoad status
    private var _UILoaded:Boolean;                     //UI Loading status
    private var _seqLibLoaded:Boolean;                  //Seq Library loaded flag
    private var _seqLibEventDispatched:Boolean          //Seq Library loaded flag
    
	
	//Application instance is stored as a static in the application class
    private static var _instance:Application = null;     
	private var _container_mc:MovieClip;               //Main container
    
	
	
	
	/**
    * Application - Constructor
    */
    private function Application(){
		trace('Begin Application...');
		
        _seqLibLoaded = false;
        _seqLibEventDispatched = false;
        
        
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
		
		setupUI();
		checkUILoaded();
    }
	
	private function setupUI():Void {
		trace('Setting up UI...');
		
		//Create the application root
        _appRoot_mc = _container_mc.createEmptyMovieClip('appRoot_mc',APP_ROOT_DEPTH);
        trace('_appRoot_mc: ' + _appRoot_mc);
		var depth:Number = _appRoot_mc.getNextHighestDepth();
        _seqLib = new Library(_appRoot_mc,depth++,LIBRARY_X,LIBRARY_Y);
        _seqLib.addEventListener('load',Proxy.create(this,UIElementLoaded));
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
				if(_seqLibLoaded){
					clearInterval(_UILoadCheckIntervalID);
					start();
				}
				
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
                case 'Library' :
					trace('Library loaded...');
                    _seqLibLoaded = true;
                    break;
                default:
            }
            
            //If all of them are loaded set UILoad accordingly
            if(_seqLibLoaded){
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
		
		// start testing - joining a lesson
		var lessonId:Number = 1;
		var l:Lesson = Lesson(_seqLib.getLesson(lessonId));
		l.joinLesson();
		// end testing
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
			_seqLib.setSize(w,h);
		}
		
		_seqLib.setSize(w,h);
		
	}
     
	// onKey*** methods - TODO
	
	
	public function getLibrary():Library {
		return _seqLib;
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
	
	public function getUserID():Number {
		// return mmm - test user
		return 4;
	}
	
	/**
    * returns the the Comms instance
    */
    public function getComms():Communication{
        return _comms;
    }

}