import mx.events.*
import org.lamsfoundation.lams.common.style.*

/**
* Manages styles throughout LAMS
* 
* @class StyleManager
* @author DI
*/
class StyleManager {

	//Declarations
    //Event delegation methods required for compiler
    private var dispatchEvent:Function;
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    private var themes:Array;                   //Stores themes for the application
    
	//Constructor
    function StyleManager() {
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
        themes[0]
    }
        

    /*
    * Used to create a style for LAMS UI elements
    */
    private function createStyle(style:mx.styles.CSSStyleDeclaration){
        
    }
    
    /**
    * Broadcast updates to all registered listeners
    */
    public function broadcastUpdate(){
        dispatchEvent({type:'update',target:this});
    }
    
    /*
    Example of setting global styles
    var styleObj = new mx.styles.CSSStyleDeclaration();
    styleObj.styleName = 'newStyle';
    
    _global.styles.newStyle = styleObj;
    
    var styleObj = _global.styles.newStyle = new mx.styles.CSSStyleDeclaration();
    styleObj.setStyle('fontFamily', 'Verdana');
    styleObj.setStyle('fontSize', 10);
    styleObj.setStyle('fontWeight', 'bold');
    styleObj.setStyle('textDecoration', 'underline');
    styleObj.setStyle('color', 0x336699);
    styleObj.setStyle('themeColor', 'haloBlue');
    _root.setStyle('styleName', 'newStyle');
    */
    //Getters+Setters
    
}