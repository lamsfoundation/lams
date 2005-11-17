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
        
        //TODO DI 09/06/05 - When server is ready change to getRequest()
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

        //Lime
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
                } else if(prop=='theme') {
                    //Default to 'default' if theme can't be found locally
                    _configData[prop] = 'default';
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
    * gets the language from the root that was passed in by the JSP page containing the SWF
    */
    private function getLanguage():String{
        //return 'en';
        return 'en';
        /*
        if(_root.language){
            return _root.language;
        }
        */
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