import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.ws.*;
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
//import mx.utils.ObjectCopy;
import mx.events.*
import mx.utils.*
/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.common.ws.WorkspaceModel extends Observable {
	public var RT_FOLDER:String = "Folder";
	public var RT_LD:String = "LearningDesign";
	public var RT_LESSON:String = "Lesson";
	public var RT_FILE:String = "File";
	
	
	public var READ_ACCESS:Number = 1;
	public var MEMBERSHIP_ACCESS:Number = 2;
	public var OWNER_ACCESS:Number = 3;
	public var NO_ACCESS:Number = 4;
	

	//ref to the wsp containter
	private var _workspace:Workspace;
	//private data
	private var _workspaceID:Number;
	private var _rootFolderID:Number;
	//this is hte inital data
	//private var _workspaceData:Object;
	
	
	//private var _workspaceResources:Hashtable;				//this contains refs to the tree nodes stored by resourceID
	private var _workspaceResources:Array;				//this contains refs to the tree nodes stored by resourceType_resourceID
	private var _accessibleWorkspaceFoldersDTOCopy:Object; // this is used so we can start again after we kill the cache
	
	//this is the dartaprovider for the tree
	private var _treeDP:XML;

	private var _selectedTreeNode:XMLNode;
	
	private var _currentTab:String; //tells us which tab should be displayed - LOCATION or PROPERTIES
	private var _defaultTab:String;
	private var _currentMode:String; //Tells us which mode the dialogue should be in - SAVE, SAVEAS, OPEN...
	private var _clipboard:Object;
	private var _clipboardMode:String; // tells us if its a cut or copy 
	private var _folderIDPendingRefresh:Number; // The ID of the folder an operation has just been done on, will be refreshed...
	private var _folderIDPendingRefreshList:Array; // The List of ID of the folder an operation has just been done on, will be refreshed...
	

	

	

	
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;

	/*
	* Constructor.
	*/
	public function WorkspaceModel (w:Workspace){
		//Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);
		_workspace = w;
		_workspaceResources = new Array();
		_defaultTab = "LOCATION";
		
		
	}	
	
	
	/**
	 * Open the workspace dialog in the centre of the screen
	 * Pass in the function to be called when a design is selected??
	 * @usage   
	 * @return  
	 */
	public function openDesignBySelection(){
		
        //workspaceView.createWorkspaceDialogOpen('centre',Delegate.create(this,itemSelected));
		
		var dto:Object = {};
		dto.pos='centre';
		Debugger.log('_workspace.itemSelected:'+_workspace.itemSelected,Debugger.GEN,'openDesignBySelection','WorkspaceModel');
		broadcastViewUpdate('CREATE_DIALOG',dto);
		
	}
	
	/**
	 * Pops up a dialogue with inputs to set the meta data for the design.
	 * @usage   
	 * @param   _ddm        
	 * @param   tabToSelect 
	 * @return  
	 */
	public function userSetDesignProperties(tabToSelect:String){
		_currentTab = tabToSelect;
		var dto:Object = {};
		dto.pos='centre';
		broadcastViewUpdate('CREATE_DIALOG',dto);
		
		
	}
	
	public function showTab(tabToSelect:String){
		if(tabToSelect == undefined){
			tabToSelect = _defaultTab;
		}
		broadcastViewUpdate('SHOW_TAB',tabToSelect);
		
	}
	
	public function getUsersWorkspace(){
		
	}
	
	public function getFolderContents(){
		
	}
	
	
	/**
    * Notify registered listeners that a data model change has happened.
	* Note - Both the VorkspaceView and the WorkspaceDialog are listening (so careful what you say!)
	* @param   _updateType = the string identifier of this type of update, allows the listener to dispatch to correct function
	* @param   _data = the data to be sent to the view handler
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
    }
	
	/**
	 * Converts the de-serialised WDDX XML from an object into 
	 * an xml format the tree likes
	 * This is parsing data from the call to getAccessibleWOrkspaceFolders, so we know that all the
	 * elements must be folders, as such we must set isBranch to be true; Also some other minor hacks to make the data match what we get with getFodlerContents()
	 * @usage   
	 * @param   dto - contains:		
	 * PRIVATE The folder which belongs to the given User
	 * RUN_SEQUENCES The folder in which user stores his lessons
	 * ORGANISATIONS List of folders (root folder only) which belong to organizations of which user is a member
	 */
	public function parseDataForTree(dto:Object):Void{
		_global.breakpoint();


		_accessibleWorkspaceFoldersDTOCopy = ObjectCopy.copy(dto);
		//this xml will be implementing the TreeDataProvider , see: http://livedocs.macromedia.com/flash/mx2004/main_7_2/wwhelp/wwhimpl/common/html/wwhelp.htm?context=Flash_MX_2004&file=00002902.html
		_treeDP = new XML();
		//add top level
		//create the data obj:
		var mdto= {};
		mdto.creationDateTime = new Date(null);
		mdto.description = "";
		mdto.lastModifiedDateTime = new Date(null);
		mdto.name =  Dictionary.getValue('ws_tree_mywsp');
		mdto.parentWorkspaceFolderID = null;
		//read only
		mdto.permissionCode = 1;
		mdto.resourceID = "x1";
		mdto.resourceType = "Folder";
		mdto.workspaceFolderID = null;
		
		
		_treeDP.addTreeNode(mdto.name,mdto);
		
		//add org folder container
		var fChild:XMLNode = _treeDP.firstChild;
		var o_oto = {};
		o_oto.creationDateTime = new Date(null);
		o_oto.description = "";
		o_oto.lastModifiedDateTime = new Date(null);
		o_oto.name = Dictionary.getValue('ws_tree_orgs');
		o_oto.parentWorkspaceFolderID = null;
		//read only
		o_oto.permissionCode = 1;
		o_oto.resourceID = "x2";
		o_oto.resourceType = "Folder";
		o_oto.workspaceFolderID = "x1";
		var orgNode:XMLNode = fChild.addTreeNode(o_oto.name,o_oto);
		orgNode.attributes.isBranch = true;
				
		for(var i=0;i<dto.ORGANISATIONS.length;i++){
			var n = dto.ORGANISATIONS[i];
			var nNode:XMLNode = orgNode.addTreeNode(n.name,n);
			nNode.attributes.isBranch = true;
			setWorkspaceResource(n.resourceType+'_'+n.resourceID,nNode);
			
		}	
				
		//add the prvate folder:
		var key = dto.PRIVATE.resourceType+'_'+dto.PRIVATE.resourceID;
		
		dto.PRIVATE.workspaceFolderID="x1";
		var privateNode:XMLNode = fChild.addTreeNode("Private:"+key,dto.PRIVATE);
		privateNode.attributes.isBranch = true;
		
		Debugger.log('privateNode.attributes.data.resourceID:'+privateNode.attributes.data.resourceID,Debugger.GEN,'openResourceInTree','org.lamsfoundation.lams.WorkspaceModel');
		//privateNode.attributes.data = dto.PRIVATE;
		setWorkspaceResource(key,privateNode);
		//_workspaceResources.put(dto.PRIVATE.resourceID,privateNode);
				
		/*
		var runNode:XMLNode = fChild.addTreeNode("Run Sequences",dto.RUN_SEQUENCES);
		runNode.attributes.isBranch = true;
		
		privateNode.attributes.data.resourceID = dto.RUN_SEQUENCES.workspaceFolderID;
		//runNode.attributes.data = dto.RUN_SEQUENCES;
		_workspaceResources.put(dto.RUN_SEQUENCES.workspaceFolderID,runNode);
		*/
		
	}
	
	/**
	 * Called when the user expands a tree node
	 * @usage   
	 * @param   resourceToOpen ID:Number of resource to open
	 * @return  
	 */
	public function openFolderInTree(resourceToOpen:Number):Void{
		Debugger.log('resourceToOpen :'+resourceToOpen ,Debugger.GEN,'openFolderInTree','org.lamsfoundation.lams.WorkspaceModel');
		//lets see if its in the hash table already (prob not)
		//if(_workspaceResources.get(resourceToOpen).attributes.data.contents == undefined){
			Debugger.log('Requesting...',Debugger.GEN,'openFolderInTree','org.lamsfoundation.lams.WorkspaceModel');
			//get that resource
			_workspace.requestFolderContents(resourceToOpen);
			
	
		//}else{
		//	Debugger.log('Already in hashtable',Debugger.GEN,'openResourceInTree','org.lamsfoundation.lams.WorkspaceModel');
			//just update the tree, nothing to do...
			
			
		//}
		
	}
	
	/**
	 * Callback from loading some folder data.
	 * Adds the node to the tree (and therfore by ref to the treeDP
	 * Also adds a ref to the node to the _workspaceResources hashtable with the resourceID as the key
	 * the contents of the folder as retrieved by getFolderContents() will contain a fodler or a learningdesign:
	 * <code><pre>
	 * - creationDateTime = 2004-12-23T0:0:0+10:0
		 description = Folder
		 lastModifiedDateTime = 2004-12-23T0:0:0+10:0
		 name = Mary Morgan Run Sequences Folder
		 permissionCode = 3.0
		 resourceID = 7.0
		 resourceType = Folder
    or
		creationDateTime = 2006-1-24T9:42:14+10:0
		description = An example description
		lastModifiedDateTime = 2006-1-24T10:42:14+10:0
		name = LD Created:Tue Jan 24 10:42:14 GMT+1100 2006
		permissionCode = 3.0
		resourceID = 6.0
	    resourceType = LearningDesign
	    versionDetails
	  
  </pre></code>

	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function setFolderContents(dto:Object){
		var nodeToUpdate:XMLNode;
		Debugger.log('looking for:Folder_'+dto.workspaceFolderID+', parentWorkspaceFolderID:'+dto.parentWorkspaceFolderID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
		_global.breakpoint();
		//if(_workspaceResources.containsKey(dto.workspaceFolderID)){
		if(getWorkspaceResource('Folder_'+dto.workspaceFolderID)!=null){
			nodeToUpdate = getWorkspaceResource('Folder_'+dto.workspaceFolderID);
			Debugger.log('nodeToUpdate.attributes.data.resourceID:'+nodeToUpdate.attributes.data.resourceID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			Debugger.log('nodeToUpdate.attributes.data.workspaceFolderID:'+nodeToUpdate.attributes.data.workspaceFolderID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');

		}else{
			//think this wont ever happen as must have been listed by prevous node
			Debugger.log('Did not find:'+dto.resourceType+'_'+dto.workspaceFolderID+' so creating a new XMLNode - this may/will fail',Debugger.CRITICAL,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			return false;
		}
		
		
		// go throught the contents of DTO and add it aas children to the node to update.
		for(var i=0; i<dto.contents.length; i++){
			var key:String = dto.contents[i].resourceType+'_'+dto.contents[i].resourceID;
			//TODO: Remove ID from label on demply
			//var cNode = nodeToUpdate.addTreeNode(dto.contents[i].name,dto.contents[i]);
			var cNode = nodeToUpdate.addTreeNode(dto.contents[i].name+':'+key,dto.contents[i]);
			cNode.attributes.data.parentWorkspaceFolderID = dto.parentWorkspaceFolderID;
			//workspaceFolderID should always be the ID of the folder this resource is contained in
			cNode.attributes.data.workspaceFolderID = dto.workspaceFolderID;
			//check if its a folder
			if(dto.contents[i].resourceType==RT_FOLDER){
				cNode.attributes.isBranch=true;
				//copy the resourceID to folderID
				//thisnk there is no need for this line! it might be wrong./.
				//cNode.attributes.data.workspaceFolderID=dto.contents[i].resourceID;
				
			}else{
				
			}
			Debugger.log('Adding new node to _workspaceResources ID :'+key,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			setWorkspaceResource(key,cNode);
		}
		
		//dispatch an update to the view
		broadcastViewUpdate('UPDATE_CHILD_FOLDER',nodeToUpdate);
		
		
		
	}
	
	/**
	 * Clears the store of workspace resources - 
	 * TODO:prob could make more granular by passing in a folder ID to clear
	 * @usage   
	 * @return  
	 */
	public function clearWorkspaceCache(folderIDPendingRefresh){
		
		Debugger.log('removing children of:'+folderIDPendingRefresh,Debugger.GEN,'clearWorkspaceCache','org.lamsfoundation.lams.WorkspaceModel');
		
		//get this node and clear its children
		var nodeToUpdate:XMLNode;
		var key:String = 'Folder_'+folderIDPendingRefresh;
		var wspResource:XMLNode = getWorkspaceResource(key);
		if(wspResource!=null){
			if (wspResource.hasChildNodes()) {
			   var deleteQue:Array = new Array();
			  //mental. but true - you have to add themn to an array and then delete them from the array, 
			  //as deleting in the loop them will remove the reference from nextSibling
			  // also childNodes:Array not seem to work properly.
			  // use firstChild to iterate through the child nodes of wspResource
			  for (var aNode:XMLNode = wspResource.firstChild; aNode != null; aNode=aNode.nextSibling) {
				deleteQue.push(aNode);
				//var deletedNode:XMLNode = aNode.removeTreeNode();
			  }
			  
			  for(var i=0; i<deleteQue.length;i++){
				  var deletedNode:XMLNode = deleteQue[i].removeTreeNode();
				  //Debugger.log('Removed node:'+deletedNode,Debugger.GEN,'clearWorkspaceCache','org.lamsfoundation.lams.WorkspaceModel');
			  }
			}else{
				Debugger.log('No Child nodes to delete',Debugger.GEN,'clearWorkspaceCache','org.lamsfoundation.lams.WorkspaceModel');
			}
			_folderIDPendingRefresh = null;
/*			//this only deletes some children, dont know why, see solution above
			for(var i=0; i<wspResource.childNodes.length;i++){
				var deletedNode:XMLNode = wspResource.childNodes[i].removeTreeNode();
				Debugger.log('Removed node:'+deletedNode,Debugger.GEN,'clearWorkspaceCache','org.lamsfoundation.lams.WorkspaceModel');
				
			}
	*/		
		}else{
			//think this wont ever happen as must have been listed by prevous node
			Debugger.log('Failing! - Did not find folderIDPendingRefresh:'+folderIDPendingRefresh+' Cant do anything',Debugger.CRITICAL,'clearWorkspaceCache','org.lamsfoundation.lams.WorkspaceModel');
		}
		
		broadcastViewUpdate('REFRESH_TREE',null);
		
	}
	
	public function clearWorkspaceCacheMultiple(){
		if(_folderIDPendingRefreshList != null){
			for (var i=0;i<_folderIDPendingRefreshList.length;i++){
				clearWorkspaceCache(_folderIDPendingRefreshList[i]);
				//now open this node in the tree
				autoOpenFolderInTree(_folderIDPendingRefreshList[i]);
			}
			_folderIDPendingRefreshList = new Array();
		}else{
			//raise error
		}
	}
	

	public function autoOpenFolderInTree(folderID){
		var nodeToOpen:XMLNode = getWorkspaceResource('Folder_'+folderID);
		Debugger.log('Opening node:'+nodeToOpen,Debugger.GEN,'autoOpenFolderInTree','org.lamsfoundation.lams.WorkspaceModel');
		broadcastViewUpdate('REFRESH_FOLDER',nodeToOpen);
	}
	
	public function setClipboardItem(item:Object,mode:String){
		//mode not used :-) no cut, only copy functionality this time.
		_clipboardMode = mode;
		_clipboard = item;
		
	}
	
	public function getClipboardItem():Object{
		return _clipboard;
	}
	
	/**
	 * Checks to see if the user can wrote to this resource
	 * If the resource is a folder, then can we write inside it
	 * If the resource is a file/Design then can we overwrite it.
	 * @usage   
	 * @param   resourceType 
	 * @param   resourceID   
	 * @return  
	 */
	public function isWritableResource(resourceType,resourceID){
		var rData = getWorkspaceResource(resourceType+'_'+resourceID).attributes.data;
		Debugger.log(resourceType+'_'+resourceID+'has permission code:'+rData.permissionCode,Debugger.GEN,'isWritableResource','org.lamsfoundation.lams.WorkspaceModel');
		if(rData.permissionCode == READ_ACCESS){
			return false;
		}
		if(rData.permissionCode == MEMBERSHIP_ACCESS){
			return false;
		}
		if(rData.permissionCode == OWNER_ACCESS){
			return true;
		}
		if(rData.permissionCode == NO_ACCESS){
			return false;
		}
		if(rData.permissionCode == undefined || rData.permissionCode == null){
			return false;
		}
	
	}
	
	
	
	//getts n setters
	/**
	 * 
	 * @usage   
	 * @param   newworkpaceResources 
	 * @return  
	 */
	public function setWorkspaceResource(key:String,newworkspaceResources:XMLNode):Void {
		Debugger.log(key+'='+newworkspaceResources,Debugger.GEN,'setWorkspaceResource','org.lamsfoundation.lams.WorkspaceModel');
		_workspaceResources[key] = newworkspaceResources;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getWorkspaceResource(key:String):XMLNode{
		Debugger.log(key+' is returning '+_workspaceResources[key],Debugger.GEN,'getWorkspaceResource','org.lamsfoundation.lams.WorkspaceModel');
		return _workspaceResources[key];
		
	}

	
	public function get treeDP():XML{
		return _treeDP;
	}

	
	
	/**
	 * 
	 * @usage   
	 * @param   newworkspaceID 
	 * @return  
	 */
	public function set workspaceID (newworkspaceID:Number):Void {
		_workspaceID = newworkspaceID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get workspaceID ():Number {
		return _workspaceID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newrootFolderID 
	 * @return  
	 */
	public function set rootFolderID (newrootFolderID:Number):Void {
		_rootFolderID = newrootFolderID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get rootFolderID ():Number {
		return _rootFolderID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getWorkspace():Workspace{
		return _workspace;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newselectedTreeNode 
	 * @return  
	 */
	public function setSelectedTreeNode (newselectedTreeNode:XMLNode):Void {
		_selectedTreeNode = newselectedTreeNode;
		//dispatch an update to the view
		broadcastViewUpdate('ITEM_SELECTED',_selectedTreeNode);
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getSelectedTreeNode ():XMLNode {
		return _selectedTreeNode;
	}

	/**
	 * 
	 * @usage   
	 * @param   newcurrentTab 
	 * @return  
	 */
	public function set currentTab (newcurrentTab:String):Void {
		_currentTab = newcurrentTab;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get currentTab ():String {
		return _currentTab;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newcurrentMode 
	 * @return  
	 */
	public function set currentMode (newcurrentMode:String):Void {
		_currentMode = newcurrentMode;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get currentMode ():String {
		return _currentMode;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newfolderIDPendingRefresh 
	 * @return  
	 */
	public function set folderIDPendingRefresh (newfolderIDPendingRefresh:Number):Void {
		_folderIDPendingRefresh = newfolderIDPendingRefresh;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get folderIDPendingRefresh ():Number {
		return _folderIDPendingRefresh;
	}


	/**
	 * 
	 * @usage   
	 * @param   newfolderIDPendingRefreshList 
	 * @return  
	 */
	public function set folderIDPendingRefreshList (newfolderIDPendingRefreshList:Array):Void {
		_folderIDPendingRefreshList = newfolderIDPendingRefreshList;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get folderIDPendingRefreshList ():Array{
		return _folderIDPendingRefreshList;
	}

}
