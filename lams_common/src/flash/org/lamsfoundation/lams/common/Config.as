import org.lamsfoundation.lams.common.*
/**
* Stores configuration data for LAMS application
* @class	Config
* @author	DI
*/
class Config {

	//Declarations

    //Application instance is stored as a static in the application class
    private static var _instance:Config = null;     
	private var _className = 'Config';

	//Constructor
	private function Config() {
        
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
    function load(src:Object){
        if(typeof(src)=='string'{
            //load from URL otherwise 
        }else {
            //load from config cookie
        }
    }
    

	//Getters+Setters
	function get className():String{
		return _className;
	}
}