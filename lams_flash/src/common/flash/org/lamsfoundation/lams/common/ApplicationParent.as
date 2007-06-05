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
	public static var C_BRANCH:String = "c_branch";
	public static var C_DEFAULT:String = "default";
	public static var C_LICON:String = "c_licon";
	
	public static var TRANSITION:String = "transition";
	
	public static var SERIAL_NO = "0000-0000-0000-0001-AAAA";
	public static var FLASH_TOOLSIGNATURE_GATE:String = "lagat11";
	public static var FLASH_TOOLSIGNATURE_GROUP:String = "lagrp11";
	
	public static var NORMAL_MODE:String = "author";		// Normal Operation Mode
	public static var EDIT_MODE:String = "editonfly";		// Edit-On-The-Fly Mode

    private var _comms:Communication;
    private var _themeManager:ThemeManager;
    private var _dictionary:Dictionary;
    private var _config:Config;
    private var _workspace:Workspace;
	
	
	private var _customCursor_mc:MovieClip;         //Cursor container
    
	
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
	
	public static function extCall(method:String, param:String):Void {
		var isMac:Boolean = (_root.isMac == "true")?true:false;
		var versionSplit = getVersion().split(",", 2);
		var v:Number = Number(versionSplit[0].substr(versionSplit[0].length - 1, 1));
		
		Debugger.log("ExtCall:: method: " + method + " :: isMac: " + isMac + " :: version: " + getVersion() + ":: v: " + v.toString() , Debugger.GEN, "extCall", "ApplicationParent");
		
		if((v <= 8) && (isMac)) {
			Debugger.log("using Javascript method", Debugger.GEN, "extCall", "ApplicationParent");
			getURL("javascript: " + method + "(" + param + ")");
		} else {
			Debugger.log("using FSCommand method", Debugger.GEN, "extCall", "ApplicationParent");
			fscommand(method, param);
		}

	}
	
	/**
    * Called when Dictionary loaded
	* @param evt:Object	the event object
    */
    private function onDictionaryLoad(evt:Object){
        if(evt.type=='load'){
			loader.complete();
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
			loader.complete();
            _themeLoaded = true;
			Debugger.log('!Theme loaded :',Debugger.CRITICAL,'onThemeLoad','Application');		
        } else {
            Debugger.log('event type not recognised :'+evt.type,Debugger.CRITICAL,'onThemeLoad','Application');
        }
		
    }
	
	/**
	 * Retrieve the help url from config.
	 * @param callback (optional) 
	 */
	public function getHelpURL(callback:Function) {
		var _callback:Function = callback;
		if(callback == null || callback == undefined) {
			_callback = Proxy.create(this,openHelp);	// default callback
		}else {
			Debugger.log('called from Monitor :',Debugger.CRITICAL,'getHelpURL','ApplicationParent');
		}
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getHelpURL',_callback, false);
		
	}
	
	/**
	 * Open the generic help page
	 * @param url generic help url
	 */
	public function openHelp(url:String) {
		var locale:String = _root.lang + _root.country;
		var target:String = this.module + '#' + this.module + '-' + locale;
		
		ApplicationParent.extCall("openURL", url + target);
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
	
	 /**
    * Returns the Cursor conatiner mc
    * 
    * @usage    Import authoring package and then use
	* 
    */
    static function get ccursor():MovieClip {
        //Return root if valid otherwise raise a big system error as app. will not work without it
        if(_instance._customCursor_mc != undefined) {
            return _instance._customCursor_mc;
        } else {
            //TODO DI 11/05/05 Raise error if mc hasn't been created
			
        }
    }
	
	public function get loader():LFLoader{
		if(_root.loader != undefined) { return _root.loader; }
		else {
		}
	}	
	
	public function get module():String{
		return _module;
	}
	
	public static function cloneMovieClip(
		target:MovieClip, newName:String,
		depth:Number, initObject:Object):MovieClip {
		
		target.duplicateMovieClip(newName, depth, initObject);
		
		return target._parent[newName];
	}
	
}