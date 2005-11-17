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
		workspaceModel = new WorkspaceModel(this);
		
		//Create the authoring view and register with model
		workspaceView = new WorkspaceView(workspaceModel, undefined);
		workspaceModel.addObserver(workspaceView);
		init();
	}
	
	private function init():Void{
		//find out the users wsp:
		requestUserWorkspace();
			
	}
	
	/**
	 * Retrieves the workspace details for the current user from the server.
	 * @usage   
	 */
	private function requestUserWorkspace():Void{
		var callback:Function = Proxy.create(this,recievedUserWorkspace);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=getWorkspace&userID='+uid,callback, false);
	}
	
	/**
	 * Invoked when getUserWorkspace returns result from server.
	 * Sets the data in the model
	 * @usage   
	 */
	public function recievedUserWorkspace(dto):Void{
		Debugger.log('workspaceID:'+dto.workspaceID+',rootFolderID:'+dto.rootFolderID,Debugger.GEN,'recievedUserWorkspace','Workspace');			
		workspaceModel.rootFolderID = dto.rootFolderID;
		workspaceModel.workspaceID = dto.workspaceID;
		//if the wsp data is null, then must be 1st time in (probabbly is ay!? - we are in init phase)
		requestWorkspaceFolders();

		
	}
	
	public function requestFolderContents(folderID:Number):Void{
		var callback:Function = Proxy.create(this,recievedFolderContents);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=getFolderContents&folderID='+folderID+'&mode='+Config.getInstance().mode+'&userID='+uid,callback, false);
		
	}
	
	public function recievedFolderContents(dto:Object):Void{
		workspaceModel.setFolderContents(dto);
		
	}
	
	
	
	/**
	 * Retrieves the available folders:
	 * The information returned is categorized under 3 main heads
		PRIVATE The folder which belongs to the given User
		RUN_SEQUENCES The folder in which user stores his lessons
		ORGANISATIONS List of folders (root folder only) which belong to organizations of which user is a member
	 */
	private function requestWorkspaceFolders():Void{
		var callback:Function = Proxy.create(this,recievedWorkspaceFolders);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=getAccessibleWorkspaceFolders&userID='+uid,callback, false);
		
	}
	
	private function recievedWorkspaceFolders(dto:Object):Void{
		Debugger.log('Got the available folders - PRIVATE.workspaceFolderID:'+dto.PRIVATE.workspaceFolderID,Debugger.GEN,'recievedWorkspaceFolders','Workspace');			
		//_global.breakpoint();
		workspaceModel.parseDataForTree(dto);
		
		//TODO: Enable the Workspace buttons on the UI now we have the minimum data
		
		
	}
	
    /**
    * This is the method called when the user opens a design
    * 
    */
    public function userSelectItem(){
		workspaceModel.openDesignBySelection();
    }
    
    /**
    * Called when design has been selected from within the workspace dialog, inovked via callback method.
    */
    public function itemSelected(designId:Number){
        Debugger.log('!!designID:'+designId,Debugger.GEN,'itemSelected','org.lamsfoundation.lams.Workspace');

        //Design has been chosen, get Canvas to open design
        Application.getInstance().getCanvas().openDesignById(designId);
    }
}
