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

import org.lamsfoundation.lams.common.comms.*
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.authoring.*;

import mx.events.*

/**
* Stores configuration data for LAMS application
* @class	Config
* @author	DI
*/
class Config {
	//Declarations

    //static constants
	//MS of delay between clicks to make a double click
	//TODO - set this to 500 ms when not in IDE
	public static var DOUBLE_CLICK_DELAY:Number = 500;
	//message type codes
	public static var MESSAGE_TYPE_ERROR:Number = 1;
	public static var MESSAGE_TYPE_CRITICAL:Number = 2;
	public static var MESSAGE_TYPE_OK:Number = 3;
	public static var USE_CACHE:Boolean = false;			//FLAG TO TELL DICT AND (todo:add to themes) Theme if thay can use the cached data.
	
	//nulls
	public static var STRING_NULL_VALUE:String = "string_null_value";
	public static var NUMERIC_NULL_VALUE:Number = -111111;
	public static var DATE_NULL_VALUE:Date = new Date(0);
	public static var BOOLEAN_NULL_VALUE:String = "boolean_null_value";
	
	//Config instance is stored as a static in the class
    private static var _instance:Config = null;   
    private static var CONFIG_PREFIX:String = 'config.';    //All config items stored in a cookie with prefix 'config.' 
	private static var CONFIG_BUILD:String = 'build';
	private static var DICT_PREFIX:String = 'dictionary.';
	private static var THEME_PREFIX:String = 'theme.';
	
	
    private var _configData:Object      //Object that stores configuration data
    
    private var _cm:CookieMonster;
    private var _comms:Communication;
	private var _className = 'Config';
    
    private var _version:String;        //Config properties
    private var _language:String;
    private var _theme:String;
	
	//passed in from the query string:
    private var _serverUrl:String;
    private var _userID:Number;
	//where are we? 1=authoring, 2=monitor
	private var _mode:Number;
	
	private var _build:String;

	private var removeCache:Boolean = false;
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	//Constructor
	private function Config() {
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		
        //Get a ref to the cookie monster 
        _cm = CookieMonster.getInstance();
        _comms = Application.getInstance().getComms();
        init();
	}
    
    /**
    * Initialises the Config class, loading config from the server and overwriting with local values
    */
    public function init() {
		
        //Get defaults from server
		loadServerDefaults();
    }
    
    /**
    * Retrieves an instance of the Config singleton
    */ 
    public static function getInstance():Config{
        if(Config._instance == null){
            Config._instance = new Config();
        }
        return Config._instance;
    }
    
    /**
    * Set the default configuration properties 
    */
    private function loadServerDefaults(){
        //createFromCode();
        //serverDefaultsLoaded(_configData);
		
        var callBack = Proxy.create(this,serverDefaultsLoaded);

        _comms.getRequest('flashxml/configData.xml',callBack)
        //_comms.loadXML('lams_authoring/configData.xml',callBack,true,true);
    }
    
    /**
    * TODO - Only to be used whilst testing with dummy data
    */
    private function createFromCode(){
        _configData = {};
        //TODO - DI 16/05/05 Get these from server eventually,stub for now
        _configData.version = '1.1';
        _configData.language = 'en';
        _configData.theme = 'default';

        //Set up languages
        var languages:Array = [];
        
        //English
        var en:Object = {};
        en.label = 'English';
        en.data = 'en';
        languages.push(en);

        //French
        var fr:Object = {};
        fr.label = 'Français';
        fr.data = 'fr';
        languages.push(fr);
        
        _configData.languages = languages;

        //Set up themes
        var themes:Array = [];
        
        //Default
        var defaultTheme:Object = {};
        defaultTheme.label = 'Default';
        defaultTheme.data = 'default';
        themes.push(defaultTheme);

        //Lime
        var limeTheme:Object = {};
        limeTheme.label = 'Lime';
        limeTheme.data = 'lime';
        themes.push(limeTheme);

        //Ruby
        var rubyTheme:Object = {};
        rubyTheme.label = 'Ruby';
        rubyTheme.data = 'ruby';
        themes.push(rubyTheme);

        
        _configData.themes = themes;
        
    }
    
    /**
    * Called when the default config. items have been loaded from server.  Overwrites server defaults with 
    * local where applicable.
    */
    private function serverDefaultsLoaded(serverConfigData:Object) {
        
		//store data from server in local private variable
        _configData = serverConfigData;
		var success:Boolean = false;
		var buildObj:Object = {};
		Debugger.log('Build param value: '+_root.build,Debugger.GEN,'serverDefaultsLoaded','Config');
		try {	
			if (CookieMonster.cookieExists(CONFIG_PREFIX+CONFIG_BUILD)) {
				buildObj = CookieMonster.open(CONFIG_PREFIX+CONFIG_BUILD,true);
				Debugger.log('Build cookie exists: '+String(buildObj),Debugger.GEN,'serverDefaultsLoaded','Config');
				
				//var buildRt:Number = parseFloat(_root.build);
				//var buildNo:Number = Number(buildObj);
				
				var buildNo:String = String(buildObj);
				
				trace('build root no.: ' + _root.build);
				trace('build no.: ' + buildNo);
				
				if(buildNo != _root.build) {
					Debugger.log('Purging cookie data',Debugger.GEN,'serverDefaultsLoaded','Config');
					if(CookieMonster.deleteCookie(CONFIG_PREFIX + CONFIG_BUILD)) {
						saveBuild(_root.build);
					}
					CookieMonster.deleteCookie(CONFIG_PREFIX + 'theme');
					CookieMonster.deleteCookie(CONFIG_PREFIX + 'language');
					removeCache=true;
				}
				
			} else {
				saveBuild(_root.build);
			}
		} catch(err:Error) {
			Debugger.log('Error occured: '+err.message,Debugger.CRITICAL,'serverDefaultsLoaded','Config');
		} finally {
			_build = _root.build;
		}
		
		//Go through server defaults and create config items
        //Loop through server defaults and overwrite if a local version exists
        
		
		for (var prop in serverConfigData) {
            if (CookieMonster.cookieExists(CONFIG_PREFIX+String(prop))){
                Debugger.log('overwriting with local data :' + prop,Debugger.GEN,'serverDefaultsLoaded','Config');
                _configData[prop] = CookieMonster.open(CONFIG_PREFIX+String(prop),true);
            } else {
                //If language config not in cookie, check browser locale before using server
                if(prop=='language'){
                    _configData[prop] = getLanguage();
					saveItem(prop);
					
					// Remove cached data if new build
					if(removeCache) {
						CookieMonster.deleteCookie(DICT_PREFIX+_configData[prop]);
					}
					
                } else if(prop=='theme') {
                    //Default to 'default' if theme can't be found locally
                    _configData[prop] = getTheme();
					saveItem(prop);
					
					// Remove cached data if new build
					if(removeCache) {
						CookieMonster.deleteCookie(THEME_PREFIX+_configData[prop]);
					}
                }
                //...else if(prop=='...'){
					
				
            }
        }
        
        
		
        _serverUrl = _root.serverURL;
		_userID = _root.userID;
		_mode = _root.mode;
		//TODO 31/05/05 Deal with alternative sources other than cookie or server. i.e. Browser + _root (query string)
		Debugger.log('Confirming ServerURL passed in - _serverUrl:' + _serverUrl,Debugger.GEN,'serverDefaultsLoaded','Config');
        
        //Dispatch load event
        dispatchEvent({type:'load',target:this});
    }
    
	/**
	* saves the config build value in a cookie
	*/
	private function saveBuild(build:Object):Void{
		
		if(build == null) {
			Debugger.log('Could not save build with null value',Debugger.CRITICAL,'saveBuild','Config');
			return;
		}
		
		var success:Boolean = false;
		
		success = CookieMonster.save(build,CONFIG_PREFIX + CONFIG_BUILD,true);
		if (!success) {
			Debugger.log('Config item could not be saved: ' +  CONFIG_BUILD ,Debugger.CRITICAL,'saveBuild','Config');
		}
		
		return;
	}
	
    /**
    * gets the language from the root that was passed in by the JSP page containing the SWF
    */
    private function getLanguage():String{
        //TODO: make this a real call to get browser lcoale!
		//return 'en';
        //return 'en';
        
        if(_root.lang){
			Debugger.log('Getting language from root: '+String(_root.lang),Debugger.GEN,'getLanguage','Config');
            return _root.lang;
        } else {
			return 'en';
		}
        
    }
	
	/**
	* gets the theme from the root that was passed in by author JSP page containing the SWF
	*/
	private function getTheme():String{
		if(_root.theme){
			Debugger.log('Getting theme from root: '+String(_root.theme),Debugger.GEN,'getTheme','Config');
			return _root.theme;
			
		} else {
			return 'default';
		}
	}
    
    
    /**
    * @returns a list of the languages stored in Config
    */
    /*
    public function getLanguages():Array{
        
    }
    */
    
    /**
    * Retrieves and returns an item for Application configuration
    * @returns requested configuration item  
    */
    public function getItem(itemID:String):Object {
        //Get the item from the data object and if valid return it
        var item:Object = _configData[itemID];
        if(item){
            return item;
        }else {
            //TODO DI 27/05/05 raise error if config data missing
            Debugger.log('Config item not found: ' + itemID ,Debugger.CRITICAL,'getItem','Config');
        }
    }
    
    /**
    * Called to set a configuration item value
    */
    public function setItem(itemID:String,value:Object):Boolean {
        //Does item exist
        if(_configData[itemID]) {
            _configData[itemID]= value;
            return true;
        }else {
            //TODO DI 30/05/05 raise error if config data can't be saved
            Debugger.log('Config item cannot be found: ' + itemID,Debugger.CRITICAL,'setItem','Config');
            return false;
        }
    }
    
    /**
    * Saves all current configuration items to disk.  Called when OK button clicked on preferences dialog
    */
    public function saveAll() {
        var success:Boolean = false;
        //Go through all child properties of _configData object and save cookies for each
        for(var prop in _configData) {
            success = CookieMonster.save(_configData[prop],CONFIG_PREFIX + String(prop),true);
            if (!success) {
                //TODO DI 30/05/05 raise error if config data can't be saved
                Debugger.log('Config item could not be saved: ' + prop ,Debugger.CRITICAL,'saveConfig','Config');
            }
        }
    }
    
    /**
    * Saves one config item
    */
    public function saveItem(itemID:String):Boolean{
        if(_configData[itemID]) {
            var res = CookieMonster.save(_configData[itemID],CONFIG_PREFIX + String(itemID),true);
            return res;
        } else {
            res = false;
        }
    }
    
    /**
    * Convert a config item to data
    */
    public function toData():Object{
        //At the moment toData just returns _configData as it is ready to serialize already 
        return _configData;
    }
    
	//Getters+Setters
	function get className():String{
		return _className;
	}
	
	function set serverUrl(a):Void{
		_serverUrl = a;
	}
	
	function get serverUrl():String{
		return _serverUrl;
	}
	
	function get userID():Number{
		return _userID;
	}
		/**
	 * 
	 * @usage   
	 * @param   newmode 
	 * @return  
	 */
	public function set mode (newmode:Number):Void {
		_mode = newmode;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get mode ():Number {
		return _mode;
	}
}