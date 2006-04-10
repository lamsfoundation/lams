/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

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
* D:C 14-02-06 //BASE STYLE OBJ not being used - see the note on getStyleObject as its crashing the player
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
			
			// show default cursor
			Cursor.showCursor(Application.C_DEFAULT);
		
        }else {
			//testing style creator
			//createThemeFromCode(theme);
			
			openFromServer(theme);
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
	* //BASE STYLE OBJ not being used - see the node on getStyleObject
    */
    public function createThemeFromCode(theme:String) {
        switch (theme) {
            case 'default' :
                //Base style object for 'default' theme
				//BASE STYLE OBJ not being used - see the node on getStyleObject
                var baseStyleObj = new mx.styles.CSSStyleDeclaration();
                baseStyleObj.setStyle('color', 0x333648);
                baseStyleObj.setStyle('themeColor', 0x669BF2);
                
				baseStyleObj.setStyle('fontFamily', 'Verdana');
                baseStyleObj.setStyle('fontSize', 10);
                
                //Create default theme
                _theme = new Theme('default',baseStyleObj);
			
        
                //----BUTTON------------------------------------------------
                //Style object
                var buttonSO = new mx.styles.CSSStyleDeclaration();
				buttonSO.setStyle('color', 0x333648);
                buttonSO.setStyle('themeColor', 0x669BF2);
                
				buttonSO.setStyle('fontFamily', 'Verdana');
                buttonSO.setStyle('fontSize', 9);
                buttonSO.setStyle('emphasizedStyleDeclaration', 0x669BF2);
                
                //Visual Element
                var buttonVisualElement = new VisualElement('button',buttonSO);
                //add visual element to the default theme
                _theme.addVisualElement(buttonVisualElement);
                //----------------------------------------------------------
        
                //----Text Area-------------------------------------------------
                //Style object
                var txaSO = new mx.styles.CSSStyleDeclaration();
				txaSO.setStyle('color', 0x333648);
				txaSO.setStyle('fontFamily', 'Verdana');
                txaSO.setStyle('fontSize', 10);
                //Visual Element
                var txaVisualElement = new VisualElement('textarea',txaSO);
                //add visual element to the default theme
                _theme.addVisualElement(txaVisualElement);
                //--------------------------------------------------------
				
        
                //----LABEL-------------------------------------------------
                //Style object
                var labelSO = new mx.styles.CSSStyleDeclaration();
                labelSO.setStyle('fontFamily', 'Verdana');
                labelSO.setStyle('fontSize', 10);
                labelSO.setStyle('color', 0x333648);
				labelSO.setStyle('embedFonts', false);
                //Visual Element
                var labelVisualElement = new VisualElement('label',labelSO);
                //add visual element to the default theme
                _theme.addVisualElement(labelVisualElement);
                //--------------------------------------------------------
                
				
                //----TextInput-------------------------------------------------
                //Style object
                var txiSO = new mx.styles.CSSStyleDeclaration();
				txiSO.setStyle('color', 0x333648);
                txiSO.setStyle('themeColor', 0x669BF2);
                
				txiSO.setStyle('fontFamily', 'Verdana');
                txiSO.setStyle('fontSize', 10);
                //Visual Element
                var txiVisualElement = new VisualElement('textinput',txiSO);
                //add visual element to the default theme
                _theme.addVisualElement(txiVisualElement);
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
				
				//Style object
                var spSO = new mx.styles.CSSStyleDeclaration();
                spSO.setStyle('themeColor', 0x669BF2);
                //Visual Element
                var spVisualElement = new VisualElement('scrollpane',spSO);
                //add visual element to the default theme
                _theme.addVisualElement(spVisualElement);
                //------------------------------------------------------
        
                //----TREE VIEW ---------------------------------------
                //Style object
                var treeviewSO = new mx.styles.CSSStyleDeclaration();
                //treeviewSO.setStyle('rollOverColor', 0xC4C7D5);
				treeviewSO.setStyle('color', 0x333648);
                treeviewSO.setStyle('themeColor', 0x669BF2);
                
				treeviewSO.setStyle('fontFamily', 'Verdana');
                treeviewSO.setStyle('fontSize', 10);
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
				checkboxSO.setStyle('color', 0x333648);
                checkboxSO.setStyle('themeColor', 0x669BF2);
                
				checkboxSO.setStyle('fontFamily', 'Verdana');
                checkboxSO.setStyle('fontSize', 10);
                //Visual Element
                var checkboxVisualElement = new VisualElement('checkbox',checkboxSO);
                //add visual element to the default theme
                _theme.addVisualElement(checkboxVisualElement);
                //------------------------------------------------------
				
				//----Small LABEL-------------------------------------------------
                //Style object
                var smlLabelSO = new mx.styles.CSSStyleDeclaration();
                smlLabelSO.setStyle('fontFamily', 'Tahoma');
                smlLabelSO.setStyle('fontSize', 9);
                smlLabelSO.setStyle('color', 0x333648);
				
                //Visual Element
                var smlLabelVisualElement = new VisualElement('smlLabel',smlLabelSO);
                //add visual element to the default theme
                _theme.addVisualElement(smlLabelVisualElement);
                //--------------------------------------------------------
				
                //----COMBO------------------------------------------
                //Style object
                var comboSO = new mx.styles.CSSStyleDeclaration();
				comboSO.setStyle('color', 0x333648);
                comboSO.setStyle('themeColor', 0x669BF2);
                comboSO.setStyle('fontFamily', 'Verdana');
                comboSO.setStyle('fontSize', 10);
                comboSO.setStyle('openEasing', Elastic.easeOut);
                //Visual Element
                var comboVisualElement = new VisualElement('combo',comboSO);
                //add visual element to the default theme
                _theme.addVisualElement(comboVisualElement);
                //------------------------------------------------------
        
                //----LF MENU-------------------------------------------
                //Style object
                var lfMenuSO = new mx.styles.CSSStyleDeclaration();
				lfMenuSO.setStyle('color', 0x333648);
                lfMenuSO.setStyle('themeColor', 0x669BF2);
				lfMenuSO.setStyle('fontFamily', 'Verdana');
                lfMenuSO.setStyle('fontSize', 10);
                lfMenuSO.setStyle('openEasing', Elastic.easeOut);
                //Visual Element
                var lfMenuVisualElement = new VisualElement('LFMenuBar',lfMenuSO);
                //add visual element to the default theme
                _theme.addVisualElement(lfMenuVisualElement);
                //------------------------------------------------------
			
				//----BGPanel-------------------------------------------
                //Style object
                var BGPanelSO = new mx.styles.CSSStyleDeclaration();
				BGPanelSO.setStyle('borderStyle', 'outset');
				BGPanelSO.setStyle('backgroundColor', 0xC2D5FE);
				
                //Visual Element
                var BGPanelVisualElement = new VisualElement('BGPanel',BGPanelSO);
                //add visual element to the default TAPanelVisualElement
                _theme.addVisualElement(BGPanelVisualElement);
                //------------------------------------------------------
				
				//----ACTPanel-------------------------------------------
                //Style object
                var ACTPanelSO = new mx.styles.CSSStyleDeclaration();
				ACTPanelSO.setStyle('borderStyle', 'none');
				ACTPanelSO.setStyle('backgroundColor', 0xC2D5FE);
				
                //Visual Element
			var ACTPanelVisualElement = new VisualElement('ACTPanel',ACTPanelSO);
                //add visual element to the default ACTPanelVisualElement
                _theme.addVisualElement(ACTPanelVisualElement);
                //------------------------------------------------------
				
				//----TAPanel-------------------------------------------
                //Style object
                var TAPanelSO = new mx.styles.CSSStyleDeclaration();
				TAPanelSO.setStyle('borderStyle', 'solid');
				TAPanelSO.setStyle('backgroundColor', 0xC2D5FE);
				TAPanelSO.setStyle('borderColor', 0x000000);
				
				
                //Visual Element
                var TAPanelVisualElement = new VisualElement('TAPanel',TAPanelSO);
                //add visual element to the default TAPanelVisualElement
                _theme.addVisualElement(TAPanelVisualElement);
                //------------------------------------------------------
			
				//----CanvasPanel-------------------------------------------
                //Style object
                var CAPanelSO = new mx.styles.CSSStyleDeclaration();
				
				CAPanelSO.setStyle('backgroundColor', 0xF4F5FD);
				
				
				
                //Visual Element
                var CAPanelVisualElement = new VisualElement('CanvasPanel',CAPanelSO);
                //add visual element to the default TAPanelVisualElement
                _theme.addVisualElement(CAPanelVisualElement);
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
		//_global.styles.Label.setStyle('styleName',getStyleObject('label'));
		var BGPanelVisualElement = new VisualElement('BGPanel',getStyleObject('BGPanel'));
		//add visual element to the default theme
		_theme.addVisualElement(BGPanelVisualElement);
		
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
	* DC: Well its not returning base style obj anymore as it seems to splat the player when you try and run the merge
	* As such each style definition will have to define all thier own styles.  This might speed everything up a fair bit
    */
    public function getStyleObject(visualElementId:String):mx.styles.CSSStyleDeclaration{
        //TODO DI 06/05/05 check for errors here with ID not found
        //TODO DI 06/05/05 implement, for now just return style for visual element

        //Get base style object theme + overwrite properties with ones in visual element for selected theme if found 
        var baseSO = _theme.baseStyleObject;
		
		

        //Get the correct visual element
        var visualElement:VisualElement = _theme.getVisualElement(visualElementId);
		if(visualElement){
			return visualElement.styleObject;
		}else{
			Debugger.log('Visual element "'+visualElementId+'" not found, returning default',Debugger.HIGH,'getStyleObject','ThemeManager');
			return _theme.getVisualElement(visualElementId).styleObject;
		}
		
 //this bit is v.inefficent, seems to crash player - esp in toolkit
		
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
            baseSO = dataToStyleObject(customSO);
			
			
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
		
		Debugger.log('Theme data: '+_theme+' found, returning'+dataObj,Debugger.HIGH,'getStyleObject','ThemeManager');
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
    
	public static function DecToHex(number:Number):String {
		//converts dec to integer
		var I:Number = Math.floor(number);
		//hexadecimal array
		var hex:Array = ("0123456789ABCDEF").split("");
		
		//internal use
		var res:String = "";
		var count:Number = 0;
		
		while(I >= 16){
		I/=16;
		count++;
		}
		for(var n:Number=count; n>0; n--){
		res += hex[Math.floor(I)];
		I = (I-Math.floor(I))*16;
		}
		
		res += hex[I];
		/* WARNING SECTION - Can be removed */
		//warns if given dec was not integer
		//dec was converted to integer
		//if(this != Math.floor(this)){
		//trace("Warning: Given number "+this+" was not integer.");
		//trace(" Integer "+Math.floor(this)+" was used instead.");
		//}
		/* ENDS WARNING SECTION */
		
		return res;
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
				
				Debugger.log('From XML Theme data : '+prop+' found, returning'+so.getStyle(prop),Debugger.HIGH,'getStyleObject','ThemeManager');
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
			if (typeof(dataObj[prop])=='number' && String(prop)!='fontSize'){
					var dataVal = DecToHex(dataObj[prop]);
					so.setStyle(String(prop), 0+"\x"+dataVal);
					trace("style parsed for "+String(prop)+ " is: "+0+"\x"+dataVal);
			} else {
				so.setStyle(String(prop), dataObj[prop]);
				trace("style parsed for "+String(prop)+ " is: "+dataObj[prop]);
			}
        }
        return so;
    }    
}