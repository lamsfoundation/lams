import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.authoring.*
import mx.managers.*
import mx.events.*
import mx.utils.*

/**
* Authoring view for the canvas
* @author   DI
*/
class org.lamsfoundation.lams.common.ws.WorkspaceView extends AbstractView {
	//Canvas clip
	private var _workspace_mc:MovieClip;
	private var _workspaceDialog:MovieClip;
	private var _popup:MovieClip;
	private var okClickedCallback:Function;
	private var _workspaceController:WorkspaceController;
	
	/*
	* Constructor
	*/
	public function WorkspaceView (m:Observable, c:Controller){
		// Invoke superconstructor, which sets up MVC relationships.
		// This view has no user inputs, so no controller is required.
		super (m, c);
		//register to recive updates form the model
		WorkspaceModel(m).addEventListener('viewUpdate',this);
		//_workspaceController = getController();
	}
	
	/**
	 * Recieved update events from the WorkspaceModel. Dispatches to relevent handler depending on update.Type
	 * @usage   
	 * @param   event
	 */
	public function viewUpdate(event:Object):Void{
		Debugger.log('Recived an Event dispather UPDATE!, updateType:'+event.updateType+', target'+event.target,4,'viewUpdate','WorkspaceView');
		 //Update view from info object
        //Debugger.log('Recived an UPDATE!, updateType:'+infoObj.updateType,4,'update','CanvasView');
       var wm:WorkspaceModel = event.target;
	   //set a ref to the controller for ease (sorry mvc guru)
	   _workspaceController = getController();
	   switch (event.updateType){
            case 'CREATE_DIALOG' :
                createWorkspaceDialog(event.data.pos,event.data.tabToSelect);
                break;
			
			break;
            default :
                Debugger.log('unknown update type :' + event.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.WorkspaceView');
		}

	}

    /**
    * Create a popup dialog containing workspace
    * @param    pos - Position, either 'centre' or an object containing x + y coordinates
    * @param    callback - The function to call and pass the selectedID to
    */
    public function createWorkspaceDialog(pos:Object){
        var dialog:MovieClip;
        //Check to see whether this should be a centered or positioned dialog
        if(typeof(pos)=='string'){
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('ws_dlg_title'),closeButton:true,scrollContentPath:'workspaceDialog'});
        } else {
            dialog = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue('ws_dlg_title'),closeButton:true,scrollContentPath:'workspaceDialog',_x:pos.x,_y:pos.y});
        }
		
		Debugger.log('_workspaceController:'+_workspaceController,4,'createWorkspaceDialogOpen','WorkspaceView');
        //Assign dialog load handler
        dialog.addEventListener('contentLoaded',Delegate.create(_workspaceController,_workspaceController.openDialogLoaded));
		
    }
	

	
	
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceDialog 
	 * @return  
	 */
	public function set workspaceDialog (newworkspaceDialog:MovieClip):Void {
		_workspaceDialog = newworkspaceDialog;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get workspaceDialog ():MovieClip {
		return _workspaceDialog;
	}

	
	
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  CanvasController
	 */
	public function getController():WorkspaceController{
		trace('getController in wsv');
		var c:Controller = super.getController();
		return WorkspaceController(c);
	}

	/**
    * Returns the default controller for this view .
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model:Observable):Controller {
        trace('default controller in wsv');
		return new WorkspaceController(model);
    }
	
	

    
    
}
