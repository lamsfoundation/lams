 import mx.controls.*
 import mx.utils.*
 /**
 * @author      DI
 */
 class org.lamsfoundation.lams.common.ws.WorkspaceDialog extends MovieClip{
     
    private var ok_btn:Button;
    private var cancel_btn:Button;
    private var panel:MovieClip;
    
    function WorkspaceDialog(){
        trace('WorkSpaceDialog.constructor');
        //Create a clip that will wait a frame before dispatching init to give components time to setup
        this.onEnterFrame = init;
    }

    private function init(){
        //Delete the enterframe dispatcher
        delete this.onEnterFrame;
        //Add an event listener for the ok button
        ok_btn.addEventListener('click',Delegate.create(this, ok));
        cancel_btn.addEventListener('click',Delegate.create(this, cancel));
        //Tie parent click event (generated on clicking close button) to this instance
        _parent._parent.addEventListener('click',this);
        
        //Register for LFWindow size events
        _parent._parent.addEventListener('size',this);
        
    }
    
    /**
    * Called by the cancel button 
    */
    private function cancel(){
        trace('Cancel');
        //Escape changes + close parent window
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
        //Size the panel
        panel.setSize(w,h);
        //Buttons
    }
    
}