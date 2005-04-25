/*
 *
 * @author
 * @version
 */
 import mx.controls.*
 import mx.utils.*
 class org.lamsfoundation.lams.common.ws.WorkspaceDialog extends MovieClip{
     
    private var ok_btn:Button;
    private var cancel_btn:Button;
    
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
        _parent.addEventListener('click',this);
    }
    
    private function cancel(){
        trace('Cancel');
        //Escape changes + close parent window
    }
    
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
    
    public function size(width,height){
        trace('size')
    }
    
}