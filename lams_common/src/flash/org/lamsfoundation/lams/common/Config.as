import org.lamsfoundation.lams.common.comms.*
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.authoring.*;

/**
* Stores configuration data for LAMS application
* @class	Config
* @author	DI
*/
class Config {
	//Declarations

    //static constants
	//MS of delacy between clicks to make a double click
	public static var DOUBLE_CLICK_DELAY:Number = 500;
	
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
    private var _serverUrl:String;
	
    

	//Constructor
	private function Config() {
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
        //TODO - DI 16/05/05 Get these from server eventually,stub for now
        var dummyServerDefaults = {};
        dummyServerDefaults.version = '1.1';
        dummyServerDefaults.language = 'en';
        dummyServerDefaults.theme = 'default';
        dummyServerDefaults.serverUrl = 'http://dolly.uklams.net:8080/lams/';

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
        
        dummyServerDefaults.languages = languages;

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
        
        dummyServerDefaults.themes = themes;

        serverDefaultsLoaded(dummyServerDefaults);
        /*
        _comms.getRequest('http://dolly.uklams.net/lams/lams_authoring',Proxy.create(this,itemReceivedFromServer))
        */
    }
    
    /**
    * Called when the default config. items have been loaded from server.  Overwrites server defaults with 
    * local where applicable.
    */
    private function serverDefaultsLoaded(serverConfigData:Object) {
        //Go through server defaults and create config items
        //Loop through server defaults and overwrite if a local version exists
        for (var prop in serverConfigData) {
            if (CookieMonster.cookieExists(CONFIG_PREFIX+String(prop))){
                Debugger.log('overwriting :' + prop,Debugger.GEN,'serverDefaultsLoaded','Config');
                serverConfigData[prop] = CookieMonster.open(CONFIG_PREFIX+String(prop),true);
            } else {
                //If language config not in cookie, check browser locale before using server
                if(prop=='language'){
                    serverConfigData[prop] = getLanguage();
                } else if(prop=='theme') {
                    //Default to 'default' if theme can't be found locally
                    serverConfigData[prop] = 'default';
                }
                //...else if(prop=='...'){
            }
        }
        
        //TODO 31/05/05 Deal with alternative sources other than cookie or server. i.e. Browser + _root (query string)
        
        //Store final configuration in local private variable
        _configData = serverConfigData;
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
    public function saveItem(ItemID:String):Boolean{
        var res = CookieMonster.save(_configData[ItemID],CONFIG_PREFIX + String(ItemID),true);
        return res;
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
}