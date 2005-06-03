import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.style.*
import org.lamsfoundation.lams.common.dict.*
import mx.controls.*
import mx.utils.*

/**
* Main application menu for LAMS
*  
* @author   DI
*/
class LFMenuBar extends MovieClip {

    // instances on the stage
    private var _mb:MenuBar;
    
    // variables in this script
    private var file_menu:Menu;
    private var tools_menu:Menu;
    private var help_menu:Menu;
    
    private var view_xml:XML; // to illustrate creating a menu with xml
    
    private var app:Application;
    private var tm:ThemeManager;

    //Constructor    
    function LFMenuBar(){
        //Set up init for next frame and make invisible this frame
        this.onEnterFrame = init;
        this._visible = false;
        
        //Get a reference to the application
        app = Application.getInstance();
        tm = ThemeManager.getInstance();
    }

    public function init() {
        //Kill enter frame and make visible now its initiated
        this._visible = true;
        delete this.onEnterFrame;
        /*=================
            FILE MENU
        =================*/
        file_menu = _mb.addMenu(Dictionary.getValue(17));
        
        // "new" is the linkage id of the movie clip to be used as the icon for the "New" menu item.
        //file_menu.addMenuItem({label:"New", instanceName:"newInstance", icon:"new"});
        //_global.breakpoint();
        file_menu.addMenuItem({label:Dictionary.getValue(9), instanceName:"newItem"});
        file_menu.addMenuItem({label:Dictionary.getValue(10), instanceName:"openItem"});
        file_menu.addMenuItem({label:Dictionary.getValue(11), instanceName:"revertItem", enabled:false});
        file_menu.addMenuItem({label:Dictionary.getValue(12), instanceName:"closeItem"});
        file_menu.addMenuItem({type:"separator"});
        file_menu.addMenuItem({label:Dictionary.getValue(13), instanceName:"saveItem"});


        /*=================
            TOOLS MENU
        =================*/
        tools_menu = _mb.addMenu(Dictionary.getValue(18));
        tools_menu.addMenuItem({label:Dictionary.getValue(14), instanceName:"drawTransitionalItem"});
        tools_menu.addMenuItem({label:Dictionary.getValue(15), instanceName:"drawOptionalItem"});
        tools_menu.addMenuItem({type:"separator"});
        tools_menu.addMenuItem({label:Dictionary.getValue(21), instanceName:"prefsItem"});


        /*=================
            HELP MENU
        =================*/
        help_menu = _mb.addMenu(Dictionary.getValue(19));
        help_menu.addMenuItem({label:Dictionary.getValue(20), instanceName:"aboutItem"});
        
        //set up listeners
        // register the listeners with the separate menus
        file_menu.addEventListener("change", Delegate.create(this,fileMenuClicked));
        tools_menu.addEventListener("change", Delegate.create(this,toolsMenuClicked));
        
        //Register for theme changed events and set Initial style
        tm.addEventListener('themeChanged',this);
        setStyles();
    }  
    
    /**
    * 
    */
    public function setSize(w:Number,h:Number) {
        _mb.setSize(w,h);
    }
    
    /**
    * event handler for file menu click
    */
    private function fileMenuClicked(eventObj:Object):Void{
        //Which item was clicked ?      
        switch (eventObj.menuItem) {
            case eventObj.menu.newItem : 
                trace('new selected');
                break;
            case eventObj.menu.openItem :
                app.getWorkspace().userSelectItem();
                break;
        }        
    }
    
    /**
    * event handler for tool menu click
    */
    private function toolsMenuClicked(eventObj:Object):Void{
        //Which item was clicked ?
        switch (eventObj.menuItem) {
            case eventObj.menu.prefsItem : 
                app.showPrefsDialog();
                break;
        }        
    }    
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object){
        if(event.type=='themeChanged'){
            //Theme has changed so update objects to reflect new styles
            setStyles();
        }else {
            Debugger.log('themeChanged event broadcast with an object.type not equal to "themeChanged"',Debugger.CRITICAL,'themeChanged','org.lamsfoundation.lams.WorkspaceDialog');
        }
    }
    
    /**
    * Set the styles for the menu called from init. and themeChanged event handler
    */
    private function setStyles() {
        var styleObj = tm.getStyleObject('LFMenuBar');
        _mb.setStyle('styleName',styleObj);
    }
}