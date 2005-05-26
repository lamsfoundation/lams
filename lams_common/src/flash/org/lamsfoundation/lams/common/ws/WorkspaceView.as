import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.authoring.*
import mx.managers.*
import mx.utils.*

/**
* Authoring view for the canvas
* @author   DI
*/
class org.lamsfoundation.lams.common.ws.WorkspaceView extends AbstractView 
{
	//Canvas clip
	private var _workspace_mc:MovieClip;
	private var workspaceDialog:MovieClip;
	private var _popup:MovieClip;
	private var okClickedCallback:Function;
	
	/*
	* Constructor
	*/
	public function WorkspaceView (m:Observable, c:Controller){
		// Invoke superconstructor, which sets up MVC relationships.
		// This view has no user inputs, so no controller is required.
		super (m, c);
	}
	
	/*
	* Updates state of the canvas, called by Canvas Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
		//Go through update object and update mc with visual changes required 
	}
    
    /**
    * Create a popup dialog containing workspace
    * @param    pos - Position, either 'centre' or an object containing x + y coordinates
    */
    public function createWorkspaceDialog(pos:Object,callBack:Function){
        var dialog:MovieClip;
        //Check to see whether this should be a centered or positioned dialog
        if(typeof(pos)=='string'){
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue(0),closeButton:true,scrollContentPath:'workspaceDialog'});
        } else {
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue(0),closeButton:true,scrollContentPath:'workspaceDialog',_x:pos.x,_y:pos.y});
        }
        //Assign dialog load handler
        dialog.addEventListener('contentLoaded',Delegate.create(this,dialogLoaded));
        okClickedCallback = callBack;
    }
    
    /**
    * called when the dialog is loaded
    */
    public function dialogLoaded(evt:Object) {
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'dialogLoaded','org.lamsfoundation.lams.WorkspaceView');
        //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'dialogLoaded','org.lamsfoundation.lams.WorkspaceView');
            evt.target.scrollContent.addEventListener('okClicked',Delegate.create(this,okClicked));
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
    
    /**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
        Debugger.log('!okClicked:',Debugger.GEN,'okClicked','org.lamsfoundation.lams.WorkspaceView');
        //Check type is correct
        if(evt.type == 'okClicked'){
            //Call the callback, passing in the design selected designId
            okClickedCallback(evt.target.selectedDesignId);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
}
