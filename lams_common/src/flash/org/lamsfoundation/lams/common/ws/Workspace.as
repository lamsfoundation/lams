/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.common.ws.*
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
		//requestUserWorkspace();
		workspaceModel.initWorkspaceTree();
		requestAvailableLicenses();
			
	}
	

	
	/**
	 * Retrieves the workspace details for the current user from the server.
	 * DEPRICATED ! - we dontthink we have a use for this anymore
	 * @usage   
	 */
	private function requestUserWorkspace():Void{
		var callback:Function = Proxy.create(this,recievedUserWorkspace);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=getWorkspace&userID='+uid,callback, false);
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

	}
	
	/**
	 * Called when the user opens a node and we dont already have the children in the cache. 
	 * either becasue never opened beofre or becasuse the cache was cleared for that folder
	 * @usage   
	 * @param   folderID 
	 * @return  
	 */
	public function requestFolderContents(folderID:Number):Void{
		var callback:Function = Proxy.create(this,recievedFolderContents);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=getFolderContents&folderID='+folderID+'&mode='+Config.getInstance().mode+'&userID='+uid,callback, false);
		//ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=getFolderContentsExcludeHome&folderID='+folderID+'&mode='+Config.getInstance().mode+'&userID='+uid,callback, false);
		
	}
	
	/**
	 * Response handler for requestFolderContents
	 * @usage   
	 * @param   dto The WDDX object containing the children of this folder
	 * @return  
	 */
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
		var callback:Function = Proxy.create(this,generalWorkspaceOperationResponseHandler);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=copyResource&resourceID='+resourceID+'&targetFolderID='+targetFolderID+'&resourceType='+resourceType+'&userID='+uid,callback, false);
		//http://localhost:8080/lams/workspace.do?method=copyResource&resourceID=10&targetFolderID=6&resourceType=FOLDER&userID=4
	}
	
	/**
	 * Handler for most of the workspace file operations, it just invalidates the cache from the folderID pending refresh
	 * and sends an open event to the dialog
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function generalWorkspaceOperationResponseHandler(dto:Object){
		if(dto instanceof LFError){
			dto.showErrorAlert();
			
		}
		//make a copy as the function deletes it after its finished.
		var toRefresh = workspaceModel.folderIDPendingRefresh;
		Debugger.log('reponse ID:'+dto,Debugger.GEN,'generalWorkspaceOperationResponseHandler','Workspace');
		//pass the folder that contained this resource
		
		workspaceModel.clearWorkspaceCache(workspaceModel.folderIDPendingRefresh);
		//now open this node in the tree
		workspaceModel.autoOpenFolderInTree(toRefresh);
		
	}
	
	public function requestDeleteResource(resourceID:Number,resourceType:String){
		Debugger.log('resourceID:'+resourceID+', resourceType'+resourceType,Debugger.GEN,'copyResourceResponse','Workspace');			
		var callback:Function = Proxy.create(this,generalWorkspaceOperationResponseHandler);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=deleteResource&resourceID='+resourceID+'&resourceType='+resourceType+'&userID='+uid,callback, false);
		
	}

	public function requestNewFolder(parentFolderID:Number,folderName:String){
		Debugger.log('parentFolderID:'+parentFolderID+', folderName'+folderName,Debugger.GEN,'requestNewFolder','Workspace');			
		var callback:Function = Proxy.create(this,generalWorkspaceOperationResponseHandler);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=createFolderForFlash&parentFolderID='+parentFolderID+'&name='+folderName+'&userID='+uid,callback, false);
		
	}

	public function requestRenameResource(resourceID:Number,resourceType:Number,newName:String){
		Debugger.log('resourceID:'+resourceID+', resourceType'+resourceType+', newName:'+newName,Debugger.GEN,'requestRenameResource','Workspace');			
		var callback:Function = Proxy.create(this,generalWorkspaceOperationResponseHandler);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=renameResource&resourceID='+resourceID+'&resourceType='+resourceType+'&name='+newName+'&userID='+uid,callback, false);
	}

	public function requestMoveResource(resourceID:Number, targetFolderID:Number, resourceType:String){
		Debugger.log('resourceID:'+resourceID+', resourceType'+resourceType+', targetFolderID:'+targetFolderID,Debugger.GEN,'requestMoveResource','Workspace');			
		var callback:Function = Proxy.create(this,requestMoveResourceResponse);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=moveResource&resourceID='+resourceID+'&resourceType='+resourceType+'&targetFolderID='+targetFolderID+'&userID='+uid,callback, false);
	}
	
	public function requestMoveResourceResponse(dto){
		if(dto instanceof LFError){
			dto.showErrorAlert();
		}
		
		workspaceModel.clearWorkspaceCacheMultiple();

		
	}
	
	/**
	 * Retrieves the available folders:
	 * The information returned is categorized under 3 main heads
		PRIVATE The folder which belongs to the given User
		RUN_SEQUENCES The folder in which user stores his lessons
		ORGANISATIONS List of folders (root folder only) which belong to organizations of which user is a member
		
		NB THis dunction is not used in the new folder structure
	
	private function requestWorkspaceFolders():Void{
		var callback:Function = Proxy.create(this,recievedWorkspaceFolders);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('workspace.do?method=getAccessibleWorkspaceFoldersNew&userID='+uid,callback, false);
		
	}
	
	private function recievedWorkspaceFolders(dto:Object):Void{
		Debugger.log('Got the available folders - PRIVATE.resourceID:'+dto.PRIVATE.resourceID,Debugger.GEN,'recievedWorkspaceFolders','Workspace');			
		//_global.breakpoint();
		workspaceModel.parseDataForTree(dto);

	}
	 */
	/**
	 * Gets a list of available licenses to apply to the designs.  Mostly they are creative commons licenses
	 * @usage   
	 * @return  
	 */
	public function requestAvailableLicenses(){
		var callback:Function = Proxy.create(this,recievedAvailableLicenses);
        var uid:Number = Config.getInstance().userID;
		ApplicationParent.getInstance().getComms().getRequest('authoring/author.do?method=getAvailableLicenses',callback, false);
	}
	
	/**
	 * The handler for requestAvailableLicenses
	 * @usage   
`	 * @param   dto An array contaning objects of each of the licenses. Each one has a url, an id, a code and a imageURL, see the model for more description
	 * @return  
	 */
	public function recievedAvailableLicenses(dto:Array){
		workspaceModel.setAvailableLicenses(dto);
	}
	
	
	
	/**
	 * Shows the workspace browsing dialoge to open a design
	 * Usually used by the canvas.
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
