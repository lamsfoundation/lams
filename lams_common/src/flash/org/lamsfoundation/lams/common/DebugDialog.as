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
class DebugDialog extends MovieClip implements Dialog{

    //References to components + clips 
    private var _container:MovieClip;  //The container window that holds the dialog
    private var cfg:Config;            //local config reference
    
    private var ok_btn:Button;         //OK+Cancel buttons
    private var cancel_btn:Button;
    private var test_btn:Button;
    
    private var panel:MovieClip;       //The underlaying panel base
    
    private var messages_ta:TextArea;
    
   
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
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
    
    private var debug:Debugger;
    
    
    /**
    * constructor
    */
    function DebugDialog(){
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);

        //set up local confi reference
        cfg = Config.getInstance();
        debug = Debugger.getInstance();
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init():Void{
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        
        setupEvents();
        setupOffsets();
        setStyles();
        
        test_btn.onRelease = function () {
            _global.breakpoint();
            Debugger.log('CRITICAL ERROR',Debugger.CRITICAL,'click','debugDialog');            
        }
        
        showDebugLog();
    }
    
    private function setupOffsets() {
        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - ok_btn._x;
        yOkOffset = panel._height - ok_btn._y;
        xCancelOffset = panel._width - cancel_btn._x;
        yCancelOffset = panel._height - cancel_btn._y;
    }
    
    private function setupEvents() {
        //EVENTS
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        //Assign Click (close button) and resize handlers
        _container.addEventListener('click',this);
        _container.addEventListener('size',this);
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        
        //Register with the Debugger class to pick up log messages
        debug.addEventListener('update',Delegate.create(this, onDebugUpdate))
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
    * Called when debug log is changed
    */
    public function onDebugUpdate(evt:Object){
        if(evt.type=='update') {
            showDebugLog();
        }
    }
    
    /**
    * Gets the log, formats it and populates the text area with it
    */
    private function showDebugLog(){
        //Set the text in the text area + scroll to the bottom
        messages_ta.text = debug.getFormattedMsgLog();
        messages_ta.vPosition = messages_ta.maxVPosition;
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
    }

    /**
    * Called by the cancel button 
    */
    private function cancel(){
        //close parent window
        _container.deletePopUp();
    }
    
    /**
    * Called by the OK button 
    */
    private function ok(){
        //close popup
        _container.deletePopUp();
    }

    /**
    * Clean up on unload
    */
    private function onUnload(){
        //Remove event listeners from debug
        debug.removeEventListener('update',onDebugUpdate);
        themeManager.removeEventListener('themeChanged',themeChanged);
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