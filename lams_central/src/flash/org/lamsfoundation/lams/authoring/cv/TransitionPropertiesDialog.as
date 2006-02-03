import org.lamsfoundation.lams.authoring.cv.*;
import org.lamsfoundation.lams.authoring.*;
import mx.controls.*
import mx.utils.*
import mx.managers.*
import mx.events.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.style.*

/**
* @author      DC
*/
class TransitionPropertiesDialog extends MovieClip{
 
  
    //References to components + clips 
    private var _container:MovieClip;       //The container window that holds the dialog
    private var ok_btn:Button;              //OK+Cancel buttons
    private var cancel_btn:Button;
    private var bkg_pnl:MovieClip;          //The underlaying panel base
    private var gateType_cmb:ComboBox;		//combo to allow you select the sybnc =gate type
	private var gateType_lbl:Label;
	private var section_lbl:Label;
	
	
        
    private var fm:FocusManager;            //Reference to focus manager
    private var themeManager:ThemeManager;  //Theme manager
    
    //Dimensions for resizing
    private var xOkOffset:Number;
    private var yOkOffset:Number;
    private var xCancelOffset:Number;
    private var yCancelOffset:Number;
    
    private var _okCallBack:Function;
    
    //These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;
    
    
    /**
    * constructor
    */
    function TransitionPropertiesDialog(){
        //trace('WorkSpaceDialog.constructor');
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
        
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    /**
    * Called a frame after movie attached to allow components to initialise
    */
    private function init(){
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        
		//text for labels
		section_lbl.text = Dictionary.getValue('trans_dlg_gate');
		gateType_lbl.text = Dictionary.getValue('trans_dlg_gatetypecmb');
		
		//Set the text for buttons
        ok_btn.label = Dictionary.getValue('trans_dlg_ok');
        cancel_btn.label = Dictionary.getValue('trans_dlg_cancel');
        
		//populate the synch type combo:
		gateType_cmb.dataProvider = Activity.getGateActivityTypes();
		
        /*
		//get focus manager + set focus to OK button, focus manager is available to all components through getFocusManager
        fm = _container.getFocusManager();
        fm.enabled = true;
        ok_btn.setFocus();
        */
        
        Debugger.log('ok_btn.tabIndex: '+ok_btn.tabIndex,Debugger.GEN,'init','org.lamsfoundation.lams.WorkspaceDialog');
        
        //Add event listeners for ok, cancel and close buttons
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        //Tie parent click event (generated on clicking close button) to this instance
        _container.addEventListener('click',this);
        //Register for LFWindow size events
        _container.addEventListener('size',this);
        
        Debugger.log('setting offsets',Debugger.GEN,'init','org.lamsfoundation.lams.common.ws.WorkspaceDialog');

        //work out offsets from bottom RHS of panel
        xOkOffset = bkg_pnl._width - ok_btn._x;
        yOkOffset = bkg_pnl._height - ok_btn._y;
        xCancelOffset = bkg_pnl._width - cancel_btn._x;
        yCancelOffset = bkg_pnl._height - cancel_btn._y;
        
        //Register as listener with StyleManager and set Styles
        themeManager.addEventListener('themeChanged',this);
        setStyles();
        
        //Fire contentLoaded event, this is required by all dialogs so that creator of LFWindow can know content loaded
        _container.contentLoaded();
    }
    
    /**
    * Event fired by StyleManager class to notify listeners that Theme has changed
    * it is up to listeners to then query Style Manager for relevant style info
    */
    public function themeChanged(event:Object){
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
       //Fire callback with selectedId
       dispatchEvent({type:'okClicked',target:this});
       _container.deletePopUp();
    }
    
    /**
    * Event dispatched by parent container when close button clicked
    */
    private function click(e:Object){
        e.target.deletePopUp();
    }
    
   
    
    /**
    * Main resize method, called by scrollpane container/parent
    */
    public function setSize(w:Number,h:Number){
        //Debugger.log('setSize',Debugger.GEN,'setSize','org.lamsfoundation.lams.common.ws.WorkspaceDialog');
        //Size the bkg_pnl
        bkg_pnl.setSize(w,h);

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