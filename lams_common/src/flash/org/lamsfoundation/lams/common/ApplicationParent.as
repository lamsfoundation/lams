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

import org.lamsfoundation.lams.common.*  
import org.lamsfoundation.lams.common.util.*        //Utils
import org.lamsfoundation.lams.common.dict.*        //Dictionary
import org.lamsfoundation.lams.common.ui.*          //User interface
import org.lamsfoundation.lams.common.style.*       //Themes/Styles
import org.lamsfoundation.lams.common.comms.*       //communications
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.Canvas;
import org.lamsfoundation.lams.common.ws.*;
import org.lamsfoundation.lams.learner.*

class ApplicationParent {
	 
	//public constants
	public static var C_HOURGLASS:String = "c_hourglass";
	public static var C_TRANSITION:String = "c_pen";
	public static var C_OPTIONAL:String = "c_optionalBoxPen";
	public static var C_GATE:String = "c_gate";
	public static var C_GROUP:String = "c_group";
	public static var C_DEFAULT:String = "default";
	
	
    private var _comms:Communication;
    private var _themeManager:ThemeManager;
    private var _dictionary:Dictionary;
    private var _config:Config;
    private var _workspace:Workspace;
	
	//Data flags
    private var _dictionaryLoaded:Boolean;             //Dictionary loaded flag
    private var _dictionaryEventDispatched:Boolean     //Event status flag
    private var _themeLoaded:Boolean;                  //Theme loaded flag
    private var _themeEventDispatched:Boolean          //Dictionary loaded flag
    
	//Application instance is stored as a static in the application parent class
    private static var _instance:ApplicationParent = null;     
	private var _module:String;

	private var _appRoot_mc:MovieClip;                 //Application root clip
    

	// constructor
	public function ApplicationParent(app:Object) {
		_instance = ApplicationParent(app);

		_themeLoaded = false;
        _themeEventDispatched = false;
        _dictionaryLoaded = false;
        _dictionaryEventDispatched = false;
        
		//Comms object - do this before any objects are created that require it for server communication
        _comms = new Communication();

	}
	
	/**
    * Retrieves an instance of the Application singleton
    */ 
    public static function getInstance():ApplicationParent{
        if(ApplicationParent._instance == null){
            ApplicationParent._instance = new ApplicationParent();
        }
        return ApplicationParent._instance;
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
    * returns the the workspace instance
    */
    public function getWorkspace():Workspace{
        return _workspace;
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
	
	public function get module():String{
		return _module;
	}
	
	
	
}