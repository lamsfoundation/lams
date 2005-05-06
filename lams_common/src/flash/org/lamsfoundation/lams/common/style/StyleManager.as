import mx.events.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.util.*

/**
* Manages styles throughout LAMS
* 
* @class    StyleManager
* @author   DI
* @usage    import org.lamsfoundation.lams.common.style.*
*           var myStyleManagerReference:StyleManager = StyleManager.getInstance();
*           myStyleManagerReference.getStyleObject(<visual id:string>)
*/
class StyleManager {

	//Declarations
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    //This ensures that the StyleManager is created
    private static var _instance:StyleManager = new StyleManager();
    //Stores themes for the application
    private var themes:Array;                   
    
	//Constructor
    private function StyleManager() {
        Debugger.log('constructor',Debugger.GEN,'constructor','org.lamsfoundation.lams.StyleManager');

        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        
        //Create and populate themes array
        themes = [];
    }
    

    /**
    * Load themes from the Server
    */
    public function loadThemes(){
        //TODO DI 03/05/05 Stub for now but should query server to access themes
    }
    
    /**
    * Return the single instance of the StyleManager
    */
    public static function getInstance():StyleManager{
        return _instance;
    }
        
    /**
    * Notify registered listeners that a Styles update has happened
    */
    public static function broadcastThemeChanged(){
        _instance.dispatchEvent({type:'themeChanged',target:_instance});
        trace('broadcast');
    }
    
    public function getStyleObject(visualElementId:String):mx.styles.CSSStyleDeclaration{
        //Make the styleObject for this theme/visual element and return
        var styleObj = new mx.styles.CSSStyleDeclaration();
        styleObj.setStyle('fontFamily', 'Arial');
        styleObj.setStyle('fontSize', 12);
        styleObj.setStyle('color', 0xff00000);
        styleObj.setStyle('themeColor', 0xff0000);
        styleObj.setStyle('borderStyle', 'outset');
        styleObj.setStyle('LAMSStyle', 'AStyle');
        return styleObj;        
    }
}