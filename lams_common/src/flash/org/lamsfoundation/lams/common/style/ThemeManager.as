import mx.events.*
import mx.utils.*
import mx.transitions.easing.*
import org.lamsfoundation.lams.common.Config
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.comms.*
import org.lamsfoundation.lams.authoring.*

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
    
    //The current theme
    private var _currentThemeName:String;
    private var _theme:Theme;
    private var comms:Communication;
    private var app:Application;
    
    
	//Constructor
    private function ThemeManager() {
        Debugger.log('constructor',Debugger.GEN,'constructor','org.lamsfoundation.lams.ThemeManager');

        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);

        //Get comms and application references        
        app = Application.getInstance();
        comms = app.getComms();
    }
    
    /**
    * Load themes from the Server
    */
    public function loadTheme(theme:String){
        //TODO DI 03/05/05 Stub for now but should query server to access themes
        //Set the selected theme to the default theme initially
        _currentThemeName = theme;
        
        //Cookie or server?
        if(CookieMonster.cookieExists('theme.'+theme)&&Config.USE_CACHE) {
            //Whilst waiting for theme to load from disk - show busy
            Cursor.showCursor(Application.C_HOURGLASS);            
            openFromDisk(theme);
            Cursor.showCursor(Application.C_DEFAULT);            
        }else {
			//testing style creator
			createThemeFromCode(theme);
            //openFromServer(theme);
        }        
    }
    
    /**
    * Opens Theme from server
    */
    public function openFromServer(theme) {
        var callBack = Delegate.create(this,themeBackFromServer);
        //loadXML(requestURL:String,handler:Function,isFullURL:Boolean,deserialize:Boolean)        
        //TODO DI 08/06/05 Stub for now until server can provide theme data as wrapped packet
        //comms.getRequest('lams_authoring/defaultTheme.xml',callBack);
        //loadXML(requestURL,handler,isFullURL,deserialize)
        //comms.loadXML('lams_authoring/defaultTheme.xml',callBack,false,true);
        
        comms.loadXML('flashxml/' + theme + 'Theme.xml',callBack,true,true);
        Debugger.log('Loading theme data from server for: ' + theme,Debugger.GEN,'openFromServer','ThemeManager');
    }
    
    /**
    * Opens Theme from server
    */
    public function themeBackFromServer(themeDTO:Object){
        Debugger.log('theme back from server',Debugger.GEN,'themeBackFromServer','ThemeManager');
        //Create from the server response
        createFromData(themeDTO);

        //Now that theme has been loaded by server, cache it 
        saveToDisk();
    }
    
    
    /**
    * TODO: THIS IS ONLY USED FOR TESTING WHILST SERVER DOES NOT SUPPORT STRUCTURE
    * REMOVE WHEN NO LONGER REQUIRED
    */
    public function createThemeFromCode(theme:String) {
        switch (theme) {
            case 'default' :
                //Base style object for 'default' theme
                var baseStyleObj = new mx.styles.CSSStyleDeclaration();
                baseStyleObj.setStyle('color', 0x333648);
                baseStyleObj.setStyle('themeColor', 0x266DEE);
                baseStyleObj.setStyle('borderStyle', 'inset');
				baseStyleObj.setStyle('fontFamily', 'Verdana');
                baseStyleObj.setStyle('fontSize', 10);
                baseStyleObj.setStyle('color', 0x333648);
                
                //Create default theme
                _theme = new Theme('default',baseStyleObj);
				
				//NOTE all the items below do not need to re-define any vaules already defined above in the base SO
				// when the SO is requested for a Visual Elemnent, the SO is merged with the base Style Object
        
                //----BUTTON------------------------------------------------
                //Style object
                var buttonSO = new mx.styles.CSSStyleDeclaration();
                buttonSO.setStyle('emphasizedStyleDeclaration', 0x266DEE);
                
                //Visual Element
                var buttonVisualElement = new VisualElement('button',buttonSO);
                //add visual element to the default theme
                _theme.addVisualElement(buttonVisualElement);
                //----------------------------------------------------------
        
                //----LABEL-------------------------------------------------
                //Style object
                var labelSO = new mx.styles.CSSStyleDeclaration();
				//add any costom items here
                //Visual Element
                var labelVisualElement = new VisualElement('label',labelSO);
                //add visual element to the default theme
                _theme.addVisualElement(labelVisualElement);
                //--------------------------------------------------------
                
                //----LFWINDOW--------------------------------------------
                //Style object
                var LFWindowSO = new mx.styles.CSSStyleDeclaration();
                LFWindowSO.setStyle('fontSize', 14);
                LFWindowSO.setStyle('borderStyle', 'inset');
                //Visual Element
                var LFWindowVisualElement = new VisualElement('LFWindow',LFWindowSO);
                //add visual element to the default theme
                _theme.addVisualElement(LFWindowVisualElement);
                //-----------------------------------------------------
        
                //----TREE VIEW ---------------------------------------
                //Style object
                var treeviewSO = new mx.styles.CSSStyleDeclaration();
                //treeviewSO.setStyle('rollOverColor', 0xC4C7D5);
                treeviewSO.setStyle('openEasing', 'Elastic');
                //Visual Element
                var treeviewVisualElement = new VisualElement('treeview',treeviewSO);
                //add visual element to the default theme
                _theme.addVisualElement(treeviewVisualElement);
                //------------------------------------------------------
                
                //----DATA GRID------------------------------------------
                //Style object
                var datagridSO = new mx.styles.CSSStyleDeclaration();
                //datagridSO.setStyle('rollOverColor', 0xC4C7D5);
                datagridSO.setStyle('openEasing', 'Elastic');
                //Visual Element
                var datagridVisualElement = new VisualElement('datagrid',datagridSO);
                //add visual element to the default theme
                _theme.addVisualElement(datagridVisualElement);
                //------------------------------------------------------
        
				//----checkbox------------------------------------------
                //Style object
                var checkboxSO = new mx.styles.CSSStyleDeclaration();
                //Visual Element
                var checkboxVisualElement = new VisualElement('checkbox',checkboxSO);
                //add visual element to the default theme
                _theme.addVisualElement(comboVisualElement);
                //------------------------------------------------------
        
                //----COMBO------------------------------------------
                //Style object
                var comboSO = new mx.styles.CSSStyleDeclaration();
                comboSO.setStyle('openEasing', Elastic.easeOut);
                //Visual Element
                var comboVisualElement = new VisualElement('combo',comboSO);
                //add visual element to the default theme
                _theme.addVisualElement(comboVisualElement);
                //------------------------------------------------------
        
                //----LF MENU-------------------------------------------
                //Style object
                var lfMenuSO = new mx.styles.CSSStyleDeclaration();
                lfMenuSO.setStyle('openEasing', Elastic.easeOut);
                //Visual Element
                var lfMenuVisualElement = new VisualElement('LFMenuBar',lfMenuSO);
                //add visual element to the default theme
                _theme.addVisualElement(lfMenuVisualElement);
                //------------------------------------------------------
        
                //----LFBUTTON----------------------------------------------
                //NOTE:This style is used in conjunction with LFButtonSkin class. For usage, see common.style.LFButtonSkin.as
                //Style object
                /*
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
                _theme.addVisualElement(LFButtonVisualElement);
                */
                //----------------------------------------------------------
                break;
            case 'lime' :
                //Base style object for 'default' theme
                var baseStyleObj = new mx.styles.CSSStyleDeclaration();
                baseStyleObj.setStyle('color', 0x333648);
                baseStyleObj.setStyle('themeColor', 0xBFFFBF);
                baseStyleObj.setStyle('borderStyle', 'inset');
                
                //Create default theme
                _theme = new Theme('lime',baseStyleObj);
        
                //----BUTTON------------------------------------------------
                //Style object
                var buttonSO = new mx.styles.CSSStyleDeclaration();
                buttonSO.setStyle('fontFamily', '_sans');
                buttonSO.setStyle('fontSize', 10);
                buttonSO.setStyle('color', 0x333648);
                buttonSO.setStyle('themeColor', 0xBFFFBF);
                buttonSO.setStyle('emphasizedStyleDeclaration', 0x266DEE);
                
                //Visual Element
                var buttonVisualElement = new VisualElement('button',buttonSO);
                //add visual element to the default theme
                _theme.addVisualElement(buttonVisualElement);
                //----------------------------------------------------------
        
                //----LABEL-------------------------------------------------
                //Style object
                var labelSO = new mx.styles.CSSStyleDeclaration();
                labelSO.setStyle('fontFamily', '_sans');
                labelSO.setStyle('fontSize', 14);
                labelSO.setStyle('color', 0x007300);
                //Visual Element
                var labelVisualElement = new VisualElement('label',labelSO);
                //add visual element to the default theme
                _theme.addVisualElement(labelVisualElement);
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
                _theme.addVisualElement(LFWindowVisualElement);
                //-----------------------------------------------------
        
                //----TREE VIEW ---------------------------------------
                //Style object
                var treeviewSO = new mx.styles.CSSStyleDeclaration();
                treeviewSO.setStyle('fontFamily', '_sans');
                treeviewSO.setStyle('fontSize', 14);
                treeviewSO.setStyle('color', 0x333648);
                treeviewSO.setStyle('themeColor', 0xBFFFBF);
                //treeviewSO.setStyle('rollOverColor', 0xC4C7D5);
                treeviewSO.setStyle('openEasing', 'Elastic');
                //Visual Element
                var treeviewVisualElement = new VisualElement('treeview',treeviewSO);
                //add visual element to the default theme
                _theme.addVisualElement(treeviewVisualElement);
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
                _theme.addVisualElement(datagridVisualElement);
                //------------------------------------------------------
        
        
                //----COMBO------------------------------------------
                //Style object
                var comboSO = new mx.styles.CSSStyleDeclaration();
                comboSO.setStyle('fontFamily', '_sans');
                comboSO.setStyle('fontSize', 14);
                comboSO.setStyle('color', 0x333648);
                comboSO.setStyle('themeColor', 0xBFFFBF);
                comboSO.setStyle('openEasing', Elastic.easeOut);
                //Visual Element
                var comboVisualElement = new VisualElement('combo',comboSO);
                //add visual element to the default theme
                _theme.addVisualElement(comboVisualElement);
                //------------------------------------------------------
        
                //----LF MENU-------------------------------------------
                //Style object
                var lfMenuSO = new mx.styles.CSSStyleDeclaration();
                lfMenuSO.setStyle('fontFamily', '_sans');
                lfMenuSO.setStyle('fontSize', 14);
                lfMenuSO.setStyle('color', 0x333648);
                lfMenuSO.setStyle('themeColor', 0x266DEE);
                lfMenuSO.setStyle('openEasing', Elastic.easeOut);
                //Visual Element
                var lfMenuVisualElement = new VisualElement('LFMenuBar',lfMenuSO);
                //add visual element to the default theme
                _theme.addVisualElement(lfMenuVisualElement);
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
                _theme.addVisualElement(LFButtonVisualElement);
                //----------------------------------------------------------
                break;
            case 'ruby' :
                //Base style object for 'default' theme
                var baseStyleObj = new mx.styles.CSSStyleDeclaration();
                baseStyleObj.setStyle('fontFamily', '_sans');
                baseStyleObj.setStyle('fontSize', 10);
                baseStyleObj.setStyle('color', 0xBE0101);
                baseStyleObj.setStyle('themeColor', 0xFF0000);
                baseStyleObj.setStyle('borderStyle', 'outset');

                //Create default theme
                _theme = new Theme('ruby',baseStyleObj);
        
                //----BUTTON------------------------------------------------
                //Style object
                var buttonSO = new mx.styles.CSSStyleDeclaration();
                buttonSO.setStyle('color', 0x6D78D1);
                
                //Visual Element
                var buttonVisualElement = new VisualElement('button',buttonSO);
                //add visual element to the default theme
                _theme.addVisualElement(buttonVisualElement);
            default:        
        }
        
		//New theme loaded so dispatch a load event
        dispatchEvent({type:'load',target:this});
		
        _currentThemeName = theme;
        
        //Now theme has been created save it to disk and broadcast themeChanged event
        saveToDisk();
        broadcastThemeChanged();
    }
	
	private function setGlobalDefaults():Void{
		//go throughu the _theme and set the _global styles
		_global.styles.Label.setStyle('styleName',getStyleObject('label'));
		
		
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
    public function broadcastThemeChanged(){
        dispatchEvent({type:'themeChanged',target:_instance});
        trace('broadcast');
    }
    
    
    /**
    * Returns a style object with styles for the VisualElementId passed in
	* Returns the base style object + any features overwritten by the extended visual elemnt definition.
    */
    public function getStyleObject(visualElementId:String):mx.styles.CSSStyleDeclaration{
        //TODO DI 06/05/05 check for errors here with ID not found
        //TODO DI 06/05/05 implement, for now just return style for visual element

        //Get base style object theme + overwrite properties with ones in visual element for selected theme if found 
        var baseSO = _theme.baseStyleObject;

        //Get the correct visual element
        var visualElement:VisualElement = _theme.getVisualElement(visualElementId);
        //Was it found?
        if(visualElement) {
            var customSO = visualElement.styleObject;
            var sObj:Object;
            
            //Overwrite baseSO styles with those in customSO 
            //Do this by converting both into a data object, copy over and then convert back from data object
            customSO = styleObjectToData(customSO);
            baseSO = styleObjectToData(baseSO);
            
            //Merge/overwrite 
			//TODO: This may not be working properly, button is not reciving style infor from base.
            for (var prop in customSO){
                baseSO[prop] = customSO[prop];
            }  
            
            //Convert back
            baseSO = dataToStyleObject(baseSO);
			
			
        }
        
        //Construct style object by superimposing base style and visua
        return baseSO;
    }
    
    /**
    * @returns an object containing the serializable (data) parts of this class
    */
    public function toData():Object{
        //Create the empty object for holding data
        var obj:Object = {};
        
        //Call
        obj = _theme.toData();
        return obj;
    }
    
    /**
    * Creates the theme from a data object
    * 
    * @param   dataObj - data object containing structure of themes needed to populate themes hash table
    */
    public function createFromData(dataObj):Object{
        _theme = Theme.createFromData(dataObj);
		//New theme loaded so dispatch a load event
        dispatchEvent({type:'load',target:this});
        return this;
    }  
    
    /**
    * Save the Theme manager data to disk 
    * @return  
    */
    public function saveToDisk():Void{
        //Convert to data object and then serialize before saving to a cookie
        var dataObj:Object = toData();
        CookieMonster.save(dataObj,'theme.' + _currentThemeName,true);
    }
    
    /**
    * Open the Theme manager data from disk 
    * @usage   
    * @return  
    */
    public function openFromDisk():Void{
        var dataObj:Object = CookieMonster.open('theme.' + _currentThemeName,true);
        createFromData(dataObj);
    }
    
    /**
     * Converts style object to a data format that can be serialized
     * @param   so - an MX style object
     * @return  Object
     */
    public static function styleObjectToData(so:Object):Object{
        //Create return obj
        var obj:Object = {};
        //Parse SO to get data
        for(var prop in so) {
            //Dont pick up functions, just objects and variables
            if(typeof(so[prop])!='function'){
                //Assign style object property to object property of same name
                obj[prop] = so.getStyle(String(prop));  
            }
        }
        return obj;
    }
    
    /**
    * Converts data  to style object (inverse method for 'styleObjectToData')
    * @usage   
    * @param   dataObj 
    * @return  
    */
    public static function dataToStyleObject(dataObj:Object):mx.styles.CSSStyleDeclaration{
        //Create Style Object
        var so = new mx.styles.CSSStyleDeclaration();     
        //Parse data object and copy over properties
        for(var prop in dataObj) {
            so.setStyle(String(prop),dataObj[prop]);
        }
        return so;
    }    
}