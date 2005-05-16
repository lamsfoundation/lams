import mx.events.*
import mx.transitions.easing.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.comms.*

/**
* Manages themes throughout LAMS using the Flash event delegation model.  Themes are comprised of Visual elements  
* which contains styles. 
* E.g., 
*   Theme:              'default' or 'lime'
*   Visual element      'LFWindow' or 'Button' 
*   StyleObject         contains style attributes such as 'ThemeColor', 'Color', 'borderStyle' etc.
* 
* 
* @class    ThemeManager
* @author   DI
* @usage    //retrieving style info
*           import org.lamsfoundation.lams.common.style.*
*           myThemeManagerReference:ThemeManager = ThemeManager.getInstance();
*           myThemeManagerReference.getStyleObject(<visual id:string>)
* 
*           //Registering for theme changed events
*           myThemeManagerReference.addEventListener('themeChanged',<function or method to handle event>);
*  
*           //NOTE: make sure that event listeners are removed when object goes out of scope e.g.
*           myThemeManagerReference.removeEventListener('themeChanged',<function or method to handle event>);
* 
*/
class ThemeManager {

	//Declarations
    //This ensures that the ThemeManager is created
    private static var _instance:ThemeManager;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    //Stores themes for the application
    private var themes:Hashtable;
    private var _selectedTheme:String;        
    
	//Constructor
    private function ThemeManager() {
        Debugger.log('constructor',Debugger.GEN,'constructor','org.lamsfoundation.lams.ThemeManager');

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
        //Base style object for 'default' theme
        var baseStyleObj = new mx.styles.CSSStyleDeclaration();
        baseStyleObj.setStyle('color', 0x333648);
        baseStyleObj.setStyle('themeColor', 0x266DEE);
        baseStyleObj.setStyle('borderStyle', 'inset');
        
        //Create default theme
        var defaultTheme = new Theme('default',baseStyleObj);

        //----BUTTON------------------------------------------------
        //Style object
        var buttonSO = new mx.styles.CSSStyleDeclaration();
        buttonSO.setStyle('fontFamily', '_sans');
        buttonSO.setStyle('fontSize', 10);
        buttonSO.setStyle('color', 0x333648);
        buttonSO.setStyle('themeColor', 0x266DEE);
        buttonSO.setStyle('emphasizedStyleDeclaraion', 0x266DEE);
        
        //Visual Element
        var buttonVisualElement = new VisualElement('button',buttonSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(buttonVisualElement);
        //----------------------------------------------------------

        //----LABEL-------------------------------------------------
        //Style object
        var labelSO = new mx.styles.CSSStyleDeclaration();
        labelSO.setStyle('fontFamily', '_sans');
        labelSO.setStyle('fontSize', 14);
        labelSO.setStyle('color', 0x333648);
        //Visual Element
        var labelVisualElement = new VisualElement('label',labelSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(labelVisualElement);
        //--------------------------------------------------------
        
        //----LFWINDOW--------------------------------------------
        //Style object
        var LFWindowSO = new mx.styles.CSSStyleDeclaration();
        LFWindowSO.setStyle('fontFamily', '_sans');
        LFWindowSO.setStyle('fontSize', 14);
        LFWindowSO.setStyle('color', 0x333648);
        LFWindowSO.setStyle('themeColor', 0x266DEE);
        LFWindowSO.setStyle('borderStyle', 'inset');
        //Visual Element
        var LFWindowVisualElement = new VisualElement('LFWindow',LFWindowSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(LFWindowVisualElement);
        //-----------------------------------------------------

        //----TREE VIEW ---------------------------------------
        //Style object
        var treeviewSO = new mx.styles.CSSStyleDeclaration();
        treeviewSO.setStyle('fontFamily', '_sans');
        treeviewSO.setStyle('fontSize', 14);
        treeviewSO.setStyle('color', 0x333648);
        treeviewSO.setStyle('themeColor', 0x266DEE);
        //treeviewSO.setStyle('rollOverColor', 0xC4C7D5);
        treeviewSO.setStyle('openEasing', 'Elastic');
        //Visual Element
        var treeviewVisualElement = new VisualElement('treeview',treeviewSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(treeviewVisualElement);
        //------------------------------------------------------
        
        //----DATA GRID------------------------------------------
        //Style object
        var datagridSO = new mx.styles.CSSStyleDeclaration();
        datagridSO.setStyle('fontFamily', '_sans');
        datagridSO.setStyle('fontSize', 14);
        datagridSO.setStyle('color', 0x333648);
        datagridSO.setStyle('themeColor', 0x266DEE);
        //datagridSO.setStyle('rollOverColor', 0xC4C7D5);
        datagridSO.setStyle('openEasing', 'Elastic');
        //Visual Element
        var datagridVisualElement = new VisualElement('datagrid',datagridSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(datagridVisualElement);
        //------------------------------------------------------


        //----COMBO------------------------------------------
        //Style object
        var comboSO = new mx.styles.CSSStyleDeclaration();
        comboSO.setStyle('fontFamily', '_sans');
        comboSO.setStyle('fontSize', 14);
        comboSO.setStyle('color', 0x333648);
        comboSO.setStyle('themeColor', 0x266DEE);
        comboSO.setStyle('openEasing', Elastic.easeOut);
        //Visual Element
        var comboVisualElement = new VisualElement('combo',comboSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(comboVisualElement);
        //------------------------------------------------------


        //----LFBUTTON----------------------------------------------
        //NOTE:This style is used in conjunction with LFButtonSkin class. For usage, see common.style.LFButtonSkin.as
        //Style object
        var LFButtonSO = new mx.styles.CSSStyleDeclaration();
        LFButtonSO.setStyle('fontFamily', 'Tahoma');
        LFButtonSO.setStyle('fontSize', 10);
        LFButtonSO.setStyle('color', 0xff0000);
        LFButtonSO.setStyle('themeColor', 0xff0000);
        LFButtonSO.setStyle('borderStyle', 'outset');
        //Custom LAMS styles
        LFButtonSO.setStyle('up', 0xCCCCCC);
        LFButtonSO.setStyle('over', 0xFAF270);
        LFButtonSO.setStyle('down', 0xccff3c);
        //Visual Element
        var LFButtonVisualElement = new VisualElement('LFButton',LFButtonSO);
        //add visual element to the default theme
        defaultTheme.addVisualElement(LFButtonVisualElement);
        //----------------------------------------------------------


        //Add the default Theme to the themes   
        themes.put(defaultTheme.name,defaultTheme);
        
        //Set the selected theme to the default theme initially
        _selectedTheme = 'default';
        
        //serialize(defaultTheme);
    }
    
    /**
    * Retrieves an instance of the ThemeManager singleton, creating it if necessary
    */ 
    public static function getInstance():ThemeManager{
        if(ThemeManager._instance == null){
            ThemeManager._instance = new ThemeManager();
        }
        return ThemeManager._instance;
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
    
    /**
    * Serialize a Theme/visualElement/StyleObject relation
    */
    public function serialize(theme:Theme){
        var wddx = new Wddx();
        var serializedTheme:String  = wddx.serialize(theme);
        trace(serializedTheme);
    }
}