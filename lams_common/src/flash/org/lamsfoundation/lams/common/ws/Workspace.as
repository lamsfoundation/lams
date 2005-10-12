import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.*
import mx.utils.*

/**
* 
* Workspace provides methods for retrieving a user workspace, necessary for selecting a design 
* 
* @author   DI
*/
class org.lamsfoundation.lams.common.ws.Workspace {
	//Model
	private var workspaceModel:WorkspaceModel;
	//View
	private var workspaceView:WorkspaceView;

	/**
	 * workspace Constructor
	 *
	 * @param   target_mc	Target clip for attaching view
	 */
	public function Workspace (){
		//Create the model.
		workspaceModel = new WorkspaceModel();
		
		//Create the authoring view and register with model
		workspaceView = new WorkspaceView(workspaceModel, undefined);
		workspaceModel.addObserver(workspaceView);
		init();
	}
	
	private function init():Void{
		//find out the users wsp:
		getUserWorkspace();
			
	}
	
	/**
	 * Retrieves the workspace details for the current user from the server.
	 * @usage   
	 */
	public function getUserWorkspace():Void{
		var callback:Function = Proxy.create(this,setUserWorkspace);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=getWorkspace&userID='+uid,callback, false);
	}
	
	/**
	 * Invoked when getUserWorkspace returns result from server.
	 * Sets the data in the model
	 * @usage   
	 */
	public function setUserWorkspace(dto):Void{
		Debugger.log('workspaceID:'+dto.workspaceID+',rootFolderID:'+dto.rootFolderID,Debugger.GEN,'setUserWorkspace','Workspace');			
		workspaceModel.rootFolderID = dto.rootFolderID;
		workspaceModel.workspaceID = dto.workspaceID;
	}
	
    /**
    * This is the method called when the user opens a design
    * 
    * @param id - the Learning design ID 
    */
    public function userSelectItem(){
        //Open the workspace dialog in the centre of the screen
        workspaceView.createWorkspaceDialog('centre',Delegate.create(this,itemSelected));
    }
    
    /**
    * Called when design has been selected from within the workspace dialog, inovked via callback method.
    */
    private function itemSelected(designId:Number){
        Debugger.log('!!designID:'+designId,Debugger.GEN,'itemSelected','org.lamsfoundation.lams.Workspace');

        //Design has been chosen, get Canvas to open design
        Application.getInstance().getCanvas().openDesignById(designId);
    }
}
