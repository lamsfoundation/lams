import mx.controls.*
import mx.utils.*
import mx.managers.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*

/**
* @author      DI
*/
class WorkspaceDialog extends MovieClip{
 
    //private static var OK_OFFSET:Number = 50;
    //private static var CANCEL_OFFSET:Number = 50;

    //References    
    private var _container:MovieClip;       //The container window that holds the dialog
    private var ok_btn:Button;              //OK+Cancel buttons
    private var cancel_btn:Button;
    private var panel:MovieClip;            //The underlaying panel base
    private var treeview:MovieClip;         //Treeview for navigation through workspace folder structure
    private var fm:FocusManager;            //Reference to focus manager
    private var myLabel_lbl:Label;          //Text labels
    private var themeManager:ThemeManager;  //Text labels
    
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
    

    /**
    * constructor
    */
    function WorkspaceDialog(){
        trace('WorkSpaceDialog.constructor');
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init(){
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
        //set the reference to the StyleManager
        themeManager = ThemeManager.getInstance();
        trace('*&^*-- themeManager:' +themeManager);
        
        //Set the container reference
        //_container = _parent._parent;
        Debugger.log('container=' + _container,Debugger.GEN,'init','org.lamsfoundation.lams.wsDialog');

        //Set the text on the labels
        myLabel_lbl.text = 'text label';
        
        //Set the text for buttons
        ok_btn.label = Dictionary.getValue(1);
        cancel_btn.label = Dictionary.getValue(2);
        
        //Set up focus manager
        fm = _container.getFocusManager();
        fm.enabled = true;
        ok_btn.setFocus();
        
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        //Tie parent click event (generated on clicking close button) to this instance
        _container.addEventListener('click',this);
        //Register for LFWindow size events
        _container.addEventListener('size',this);
        
        Debugger.log('setting offsets',Debugger.GEN,'init','org.lamsfoundation.lams.common.ws.WorkspaceDialog');

        //work out offsets from bottom RHS of panel
        xOkOffset = panel._width - ok_btn._x + ok_btn.width/2;
        yOkOffset = panel._height - ok_btn._y + ok_btn.height/2;
        xCancelOffset = panel._width - cancel_btn._x + cancel_btn.width/2;
        yCancelOffset = panel._height - cancel_btn._y + cancel_btn.height/2;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
    }
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(){
        //Theme has changed so update objects to reflect new styles
        setStyles();
    }
    
    /**
    * Called on initialisation and themeChanged event handler
    */
    private function setStyles(){
        //LFWindow, goes first to prevent being overwritten with inherited styles.
        var styleObj = themeManager.getStyleObject('LFWindow');
        _container.setStyle('styleName',styleObj);

        //Get the button style from the style manager
        styleObj = themeManager.getStyleObject('button');
        
        //apply to both buttons
        Debugger.log('styleObject : ' + styleObj,Debugger.GEN,'setStyles','org.lamsfoundation.lams.WorkspaceDialog');
        ok_btn.setStyle('styleName',styleObj);
        cancel_btn.setStyle('styleName',styleObj);
        
        //Get label style and apply to label
        styleObj = themeManager.getStyleObject('label');
        myLabel_lbl.setStyle('styleName',styleObj);
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
        trace('OK');
       //If validation successful commit + close parent window
    }
    
    /**
    * Event dispatched by parent container when close button clicked
    */
    private function click(e:Object){
        trace('WorkspaceDialog.click');
        e.target.deletePopUp();
    }
    
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
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