import org.lamsfoundation.lams.common.comms.*       //communications
import org.lamsfoundation.lams.common.*    ]
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.ls.LessonModel;
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
	
	private static var LESSON_X:Number = 5;
    private static var LESSON_Y:Number = 5;
    
	
	// UI Elements
	
	//Application instance is stored as a static in the application class
    private static var _instance:Application = null;     
	private var _container_mc:MovieClip;               //Main container
    
	
	/**
    * Application - Constructor
    */
    private function Application(){
        
        
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