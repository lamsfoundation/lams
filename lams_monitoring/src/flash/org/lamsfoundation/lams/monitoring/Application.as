

/**
* Application - LAMS Monitoring Application
* @author   DC
*/
class org.lamsfoundation.lams.monitoring.Application {
	
	 

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
      
    }
    
   
}