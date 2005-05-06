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
    //This ensures that the StyleManager is created
    private static var _instance:StyleManager;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    //Stores themes for the application
    private var themes:Hashtable;
    private var _selectedTheme:String;        
    
	//Constructor
    private function StyleManager() {
        Debugger.log('constructor',Debugger.GEN,'constructor','org.lamsfoundation.lams.StyleManager');

        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        
        //Create and populate themes array
        themes = new Hashtable('Themes');
    }
    
    /**
    * Load themes from the Server
    */
    public function loadThemes(){
        //TODO DI 03/05/05 Stub for now but should query server to access themes
        //Base styles for default theme
        var baseStyleObj = new mx.styles.CSSStyleDeclaration();
        baseStyleObj.setStyle('color', 0xff00000);
        baseStyleObj.setStyle('themeColor', 0xff0000);
        baseStyleObj.setStyle('borderStyle', 'inset');
        
        var defaultTheme = new Theme('default',baseStyleObj);

        //Workspace style object
        var wsSO = new mx.styles.CSSStyleDeclaration();
        wsSO.setStyle('fontFamily', 'Arial');
        wsSO.setStyle('fontSize', 12);
        wsSO.setStyle('color', 0x00000ff);
        wsSO.setStyle('themeColor', 0x0000ff);
        wsSO.setStyle('borderStyle', 'outset');
        
        var wsVisualElement = new VisualElement('workspace',wsSO);

        //Add the workspace element to the default theme
        defaultTheme.addVisualElement(wsVisualElement);
        
        //Add the default Theme to the themes   
        themes.put(defaultTheme.name,defaultTheme);
        
        //Set the selected theme to the default theme initially
        _selectedTheme = 'default';
    }
    
    /**
    * Retrieves an instance of the StyleManager singleton, creating it if necessary
    */ 
    public static function getInstance():StyleManager{
        if(StyleManager._instance == null){
            StyleManager._instance = new StyleManager();
        }
        return StyleManager._instance;
    }
        
    /**
    * Notify registered listeners that a Styles update has happened
    */
    public static function broadcastThemeChanged(){
        _instance.dispatchEvent({type:'themeChanged',target:_instance});
        trace('broadcast');
    }
    
    /**
    * sets the current theme
    */
    public function selectTheme(theme:String){
        _selectedTheme = theme;
    }
    
    /**
    * Returns a style object with styles for the VisualElementId passed in
    */
    public function getStyleObject(visualElementId:String):mx.styles.CSSStyleDeclaration{
        //Get relevant style object from selected theme
        //Get selected theme
        var theme:Theme = themes.get(_selectedTheme);
        //Get the correct visual element
        //TODO DI 06/05/05 check for errors here with ID not found
        var visualElement:VisualElement = theme.getVisualElement(visualElementId);
        //Construct style object by superimposing base style and visua
        //TODO DI 06/05/05 implement, for now just return style for visual element
        return visualElement.styleObject;
    }
}