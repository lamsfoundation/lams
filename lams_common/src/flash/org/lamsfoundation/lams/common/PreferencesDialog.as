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

import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*

import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.authoring.*

/*
* Preferences Dialog window for editing user preferences
* @author  DI
*/
class PreferencesDialog extends MovieClip implements Dialog{

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    private var cfg:Config;            //local config reference
    
    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
    
    private var panel:MovieClip;       //The underlaying panel base
    
    private var lang_cb:ComboBox;    //Theme + language labels
    private var theme_cb:ComboBox;    
    
    private var lang_lbl:Label;        //Theme + language labels
    private var theme_lbl:Label;
    
    
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
    private var currentLanguage:String;     //Language and theme settings for current and new 
    private var newLanguage:String;

    private var currentTheme:String;
    private var newTheme:String;
    
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;

    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    
    /**
    * constructor
    */
    function PreferencesDialog(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);

        //set up local confi reference
        cfg = Config.getInstance();
        
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
        //Store current language for rollback if needed
        currentLanguage = newLanguage = String(cfg.getItem('language'));
        currentTheme = newTheme = String(cfg.getItem('theme'));
        
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
        //Set the text for buttons
        ok_btn.label = Dictionary.getValue('prefs_dlg_ok');
        cancel_btn.label = Dictionary.getValue('prefs_dlg_cancel');
        
        //Set the labels
        lang_lbl.text = Dictionary.getValue('prefs_dlg_lng_lbl');
        theme_lbl.text = Dictionary.getValue('prefs_dlg_theme_lbl');
        
        //get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        //Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        
        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - ok_btn._x;
        yOkOffset = panel._height - ok_btn._y;
        xCancelOffset = panel._width - cancel_btn._x;
        yCancelOffset = panel._height - cancel_btn._y;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
        
        //Populate themes and languages combo
        //Languages is an array containing objects with label and data properties
        var languages = cfg.getItem('languages');
        lang_cb.dataProvider = languages;
        //Select current language
        var language = cfg.getItem('language');
        //Go through all options to find current language index and select it in combo
        for(var i=0;i<lang_cb.length;i++){
            if(lang_cb.getItemAt(i).data == language){
                lang_cb.selectedIndex = i;
                break;
            }
        }
        //Set up event handler
        lang_cb.addEventListener('change',Delegate.create(this,languageSelectionChanged));
        
        //THEMES
        var themes = cfg.getItem('themes');
        theme_cb.dataProvider = themes;
        //Select current theme
        var theme = cfg.getItem('theme');
        //Go through all options to find current language index and select it in combo
        for(var i=0;i<theme_cb.length;i++){
            if(theme_cb.getItemAt(i).data == theme){
                theme_cb.selectedIndex = i;
                break;
            }
        }
        //Set up event handler
        theme_cb.addEventListener('change',Delegate.create(this,themeSelectionChanged));
    }
    
    
    /**
    * Event fired when theme combo box has been used to change the theme selection
    * @param evt:Object containing type and source as per Flash event delegation model
    */
    private function themeSelectionChanged(evt:Object) {
        //Verify event type
        if(evt.type == 'change'){
            trace("Value changed to " + evt.target.value);
            //Store selected language
            newTheme = evt.target.value;
        } else {
            Debugger.log('wrong event type for event :',Debugger.CRITICAL,'themeSelectionChanged','org.lamsfoundation.lams.PreferencesDialog');
        }
    }
    
    
    /**
    * Event fired when language combo box has been used to change the language selection
    * evt Object containing type and source as per Flash event delegation model
    */
    private function languageSelectionChanged(evt:Object) {
        //Verify event type
        if(evt.type == 'change'){
            trace("Value changed to " + evt.target.value);
            //Store selected language
            newLanguage = evt.target.value;
        } else {
            Debugger.log('wrong event type for event :',Debugger.CRITICAL,'languageChanged','org.lamsfoundation.lams.PreferencesDialog');
        }
    }
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object):Void{
        if(event.type=='themeChanged') {
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }
    
    /**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName',styleObj);

        //Get the button style from the style manager and apply to both buttons
        styleObj = themeManager.getStyleObject('button');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);
      
        //Apply combo style 
        styleObj = themeManager.getStyleObject('combo');
        lang_cb.setStyle('styleName',styleObj);
        theme_cb.setStyle('styleName',styleObj);

        //Apply label style 
        styleObj = themeManager.getStyleObject('label');
        lang_lbl.setStyle('styleName',styleObj);
        theme_lbl.setStyle('styleName',styleObj);
    }

    /**
    * Called by the cancel button 
    */
    private function cancel(){
        trace('Cancel');
        //close parent window
        _container.deletePopUp();
    }
    
    /**
    * Called by the OK button 
    */
    private function ok(){
        Debugger.log('OK Clicked',Debugger.GEN,'ok','org.lamsfoundation.lams.PreferencesDialog');
        //Validate and set prefs
        var validateOk:Boolean = true;   //Set to false if any validation fails and rollback changes if it does
        
        //Language - Has it changed?
        if(currentLanguage!=newLanguage){
            Debugger.log('language changed to :' + newLanguage,Debugger.GEN,'ok','PreferencesDialog');        
            validateOk = cfg.setItem('language',newLanguage);
            validateOk = cfg.saveItem('language');
        }
        
        //Theme - Has it changed?
        if(currentTheme!=newTheme){
            Debugger.log('Theme changed to :' + newTheme,Debugger.GEN,'ok','PreferencesDialog');        
            //Save the change to the config class
            validateOk = cfg.setItem('theme',newTheme);
            validateOk = cfg.saveItem('theme');
            //Load the new theme and then broadcast change
            themeManager.loadTheme(newTheme);
            themeManager.broadcastThemeChanged();
        }
        
        //validateOk = cfg.setItem('theme',newTheme);
        //If changing data didn't validate then rollback changes
        if(!validateOk){
           Debugger.log('Not all changes could be saved',Debugger.MED,'ok','PreferencesDialog');
           //TODO DI 01/06/05 Popup alert with error for user informing of error and consequences
        } else {
           //close popup
           _container.deletePopUp();
        }
    }
    
    /**
    * If an alert was spawned by this dialog this method is called when it's closed
    */
    private function alertClosed(evt:Object){
        //Should prefs dialog be closed?
        //TODO DI 01/06/05 check for delete of dialog
        //_container.deletePopUp();
    }
    
    /**
    * Event dispatched by parent container when close button clicked
    */
    public function click(e:Object):Void{
        trace('PreferencesDialog.click');
        e.target.deletePopUp();
    }
    
    
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number):Void{
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the panel
        panel.setSize(w,h);

        //Buttons
        ok_btn.move(w-xOkOffset,h-yOkOffset);
        cancel_btn.move(w-xCancelOffset,h-yCancelOffset);
    }
    
    //Gets+Sets
    /**
    * set the container refernce to the window holding the dialog
    */
    function set container(value:MovieClip){
        _container = value;
    }

}