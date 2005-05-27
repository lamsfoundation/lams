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

    //Config instance is stored as a static in the class
    private static var _instance:Config = null;   
    
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
		setDefaults();
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
    * Loads application configuration data for LAMS, can load from a URL or cookie
    */
    public function load(src:Object){
        if(typeof(src)=='string'){
            //load from URL otherwise 
        }else {
            //load from config cookie			//TODO: FIx this
            //_configData = _cm.open('config');
        }
    }
    
    /**
    * Set the default configuration properties 
    */
    public function setDefaults(){
        //TODO - DI 16/05/05 Get these from server eventually
        _version = '1.1';
        _language = 'uk';
        _theme = 'default';
        _serverUrl = 'http://dolly.uklams.net:8080/lams/';
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