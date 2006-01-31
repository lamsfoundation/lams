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

	private var _onOKCallBack:Function;
	

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
		//Application.getInstance().getComms().getRequest('workspace.do?method=getFolderContents&folderID='+folderID+'&mode='+Config.getInstance().mode+'&userID='+uid,callback, false);
		Application.getInstance().getComms().getRequest('workspace.do?method=getFolderContentsExcludeHome&folderID='+folderID+'&mode='+Config.getInstance().mode+'&userID='+uid,callback, false);
		
	}
	
	public function recievedFolderContents(dto:Object):Void{
		workspaceModel.setFolderContents(dto);
		
	}
	

	/**
	 * Asks the server to copy one resource to another location, if its the same folderID, 
	 * then we have to change the name - append Copy of
	 * @usage   
	 * @param   resourceID     	//the resource to be copied, e.g. folder, LD or somethign else
	 * @param   targetFolderID 	//the fodler to copy the resource too
	 * @param   resourceType   	//the resource type string LearningDesign or Folder as of 1.1
	 * @return  
	 */
	public function requestCopyResource(resourceID:Number,targetFolderID:Number,resourceType:String){
		Debugger.log(resourceID+',to folder '+targetFolderID+','+resourceType,Debugger.GEN,'copyResourceResponse','Workspace');			
		var callback:Function = Proxy.create(this,copyResourceResponse);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=copyResource&resourceID='+resourceID+'&targetFolderID='+targetFolderID+'&resourceType='+resourceType+'&userID='+uid,callback, false);
		//http://localhost:8080/lams/workspace.do?method=copyResource&resourceID=10&targetFolderID=6&resourceType=FOLDER&userID=4
	}
	
	public function copyResourceResponse(dto:Object):Void{
		if(dto instanceof LFError){
			dto.showErrorAlert();
		}
		Debugger.log('New resourceID:'+dto,Debugger.GEN,'copyResourceResponse','Workspace');
		//pass the folder that contained this resource
		
		workspaceModel.clearWorkspaceCache(workspaceModel.folderIDPendingRefresh);
		//now open this node in the tree
		workspaceModel.autoOpenFolderInTree(workspaceModel.folderIDPendingRefresh);
		
				// if it comes back ok, clear the clipboard & refresh
		// if not, alertUser
	}
	
	public function requestDeleteResource(resourceID:Number,resourceType:String){
		Debugger.log('resourceID:'+resourceID+', resourceType'+resourceType,Debugger.GEN,'copyResourceResponse','Workspace');			
		var callback:Function = Proxy.create(this,deleteResourceResponse);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=deleteResource&resourceID='+resourceID+'&resourceType='+resourceType+'&userID='+uid,callback, false);
		
	}
	
	public function deleteResourceResponse(dto:Object){
		if(dto instanceof LFError){
			dto.showErrorAlert();
		}
		workspaceModel.clearWorkspaceCache(workspaceModel.folderIDPendingRefresh);
		//now open this node in the tree
		workspaceModel.autoOpenFolderInTree(workspaceModel.folderIDPendingRefresh);
	}
	
	public function requestNewFolder(parentFolderID:Number,folderName:String){
		Debugger.log('parentFolderID:'+parentFolderID+', folderName'+folderName,Debugger.GEN,'requestNewFolder','Workspace');			
		var callback:Function = Proxy.create(this,requestNewFolderResponse);
        var uid:Number = Config.getInstance().userID;
		Application.getInstance().getComms().getRequest('workspace.do?method=createFolderForFlash&parentFolderID='+parentFolderID+'&name='+folderName+'&userID='+uid,callback, false);
		
	}
	
	public function requestNewFolderResponse(dto:Object){
		if(dto instanceof LFError){
			dto.showErrorAlert();
		}
		workspaceModel.clearWorkspaceCache(workspaceModel.folderIDPendingRefresh);
		//now open this node in the tree
		workspaceModel.autoOpenFolderInTree(workspaceModel.folderIDPendingRefresh);
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
		Application.getInstance().getComms().getRequest('workspace.do?method=getAccessibleWorkspaceFoldersNew&userID='+uid,callback, false);
		
	}
	
	private function recievedWorkspaceFolders(dto:Object):Void{
		Debugger.log('Got the available folders - PRIVATE.resourceID:'+dto.PRIVATE.resourceID,Debugger.GEN,'recievedWorkspaceFolders','Workspace');			
		//_global.breakpoint();
		workspaceModel.parseDataForTree(dto);
		
		//TODO: Enable the Workspace buttons on the UI now we have the minimum data
		
		
	}
	
	
	
    /**
    * This is the method called when the user opens a design
    * 
    */
    public function userSelectItem(callback){
		_onOKCallBack = callback;
		//var fn:Function = Delegate.create(this,itemSelected);
		workspaceModel.currentMode = "OPEN";
		workspaceModel.openDesignBySelection();
    }
	
	/**
	 * Shows the workspace browsing dialoge to set a design;s properties
	 * Usually used for saving a design by the canvas.
	 * @usage   
	 * @param   _ddm         The design in question
	 * @param   tabToSelect  The tab to show, can be: SAVE_AS, PROPERTIES, 
	 * @param   onOkCallback The function to call when the user clicks OK.
	 * @return  
	 */
	public function setDesignProperties(tabToSelect:String,onOKCallback):Void{
		_onOKCallBack = onOKCallback;
		workspaceModel.currentMode = "SAVEAS";
		workspaceModel.userSetDesignProperties(tabToSelect,onOKCallback);
		
	}
    
    /**
    * Called when design has been selected from within the workspace dialog, inovked via callback method.
    */
    public function itemSelected(designId:Number){
        Debugger.log('!!designID:'+designId,Debugger.GEN,'itemSelected','org.lamsfoundation.lams.Workspace');
		_onOKCallBack(designId);
        //Design has been chosen, get Canvas to open design
        //Application.getInstance().getCanvas().openDesignById(designId);
    }
	
	public function getDefaultWorkspaceID():Number{
		return workspaceModel.workspaceID;
	}
	
	public function getDefaultRootFolderID():Number{
		return workspaceModel.rootFolderID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newonOKCallback 
	 * @return  
	 */
	public function set onOKCallback (newonOKCallback:Function):Void {
		_onOKCallBack = newonOKCallback;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get onOKCallback ():Function {
		return _onOKCallBack;
	}
	
	
	
	
}
