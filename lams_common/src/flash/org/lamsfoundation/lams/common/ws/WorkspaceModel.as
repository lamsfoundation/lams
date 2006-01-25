import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.ws.*;
import org.lamsfoundation.lams.common.util.*
import mx.events.*
import mx.utils.*
/*
* Model for the Canvas
*/
class org.lamsfoundation.lams.common.ws.WorkspaceModel extends Observable {
	//ref to the wsp containter
	private var _workspace:Workspace;
	//private data
	private var _workspaceID:Number;
	private var _rootFolderID:Number;
	//this is hte inital data
	//private var _workspaceData:Object;
	
	
	private var _workspaceResources:Hashtable;				//this contains refs to the tree nodes stored by resourceID
	private var _accessibleWorkspaceFoldersDTOCopy:Object; // this is used so we can start again after we kill the cache
	
	//this is the dartaprovider for the tree
	private var _treeDP:XML;

	private var _selectedTreeNode:XMLNode;
	
	private var _currentTab:String; //tells us which tab should be displayed - LOCATION or PROPERTIES
	private var _defaultTab:String;
	private var _currentMode:String; //Tells us which mode the dialogue should be in - SAVE, SAVEAS, OPEN...
	

	
	
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
		_workspaceResources = new Hashtable();
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
		_accessibleWorkspaceFoldersDTOCopy = dto;
		_treeDP = new XML();
		//add top level
		_treeDP.addTreeNode("My Workspace",0);
		//add org folder container
		var fChild:XMLNode = _treeDP.firstChild;
		var orgNode:XMLNode = fChild.addTreeNode("Organisations",null);
		orgNode.attributes.isBranch = true;
				
		for(var i=0;i<dto.ORGANISATIONS.length;i++){
			var n = dto.ORGANISATIONS[i];
			var nNode:XMLNode = orgNode.addTreeNode(n.name,n);
			nNode.attributes.isBranch = true;
			_workspaceResources.put(n.resourceID,nNode);
			
		}	
				
		//add the prvate folder:
		var privateNode:XMLNode = fChild.addTreeNode("Private",dto.PRIVATE);
		privateNode.attributes.isBranch = true;
		
		Debugger.log('privateNode.attributes.data.resourceID:'+privateNode.attributes.data.resourceID,Debugger.GEN,'openResourceInTree','org.lamsfoundation.lams.WorkspaceModel');
		//privateNode.attributes.data = dto.PRIVATE;
		_workspaceResources.put(dto.PRIVATE.resourceID,privateNode);
				
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
	 * @param   resourceToOpen 
	 * @return  
	 */
	public function openResourceInTree(resourceToOpen:Number):Void{
		Debugger.log('resourceToOpen :'+resourceToOpen ,Debugger.GEN,'openResourceInTree','org.lamsfoundation.lams.WorkspaceModel');
		//lets see if its in the hash table already (prob not)
		if(_workspaceResources.get(resourceToOpen).attributes.data.contents == undefined){
			Debugger.log('Requesting...',Debugger.GEN,'openResourceInTree','org.lamsfoundation.lams.WorkspaceModel');
			//get that resource
			//TODO: Waiting ofr bqack end to fix up the problems with DB -
			_workspace.requestFolderContents(resourceToOpen);
			
	
		}else{
			Debugger.log('Already in hashtable',Debugger.GEN,'openResourceInTree','org.lamsfoundation.lams.WorkspaceModel');
			//just update the tree
			
			
		}
		
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
		Debugger.log('dto.workspaceFolderID:'+dto.workspaceFolderID+', parentWorkspaceFolderID:'+dto.parentWorkspaceFolderID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
		//_global.breakpoint();
		if(_workspaceResources.containsKey(dto.workspaceFolderID)){
			nodeToUpdate = _workspaceResources.get(dto.workspaceFolderID);
			Debugger.log('nodeToUpdate.attributes.data.resourceID:'+nodeToUpdate.attributes.data.resourceID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			Debugger.log('nodeToUpdate.attributes.data.workspaceFolderID:'+nodeToUpdate.attributes.data.workspaceFolderID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
		}else{
			//think this wont ever happen as must have been listed by prevous node
			Debugger.log('Did not find:'+dto.workspaceFolderID+' so creating a new XMLNode - this may/will fail',Debugger.CRITICAL,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			nodeToUpdate = new XMLNode();
		}
		
		
		
		for(var i=0; i<dto.contents.length; i++){
			var cNode = nodeToUpdate.addTreeNode(dto.contents[i].name,dto.contents[i]);
			cNode.attributes.data.parentWorkspaceFolderID = dto.parentWorkspaceFolderID;
			//workspaceFolderID should always be the ID of the folder this resource is contained in
			cNode.attributes.data.workspaceFolderID = dto.workspaceFolderID;
			//check if its a folder
			if(dto.contents[i].resourceType=="Folder"){
				cNode.attributes.isBranch=true;
				//copy the resourceID to folderID
				//thisnk there is no need for this line! it might be wrong./.
				//cNode.attributes.data.workspaceFolderID=dto.contents[i].resourceID;
				
			}else{
				
			}
			Debugger.log('Adding new node to _workspaceResources ID :'+dto.contents[i].resourceID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			_workspaceResources.put(dto.contents[i].resourceID,cNode);
		}
		
		//dispatch an update to the view
		broadcastViewUpdate('REFRESH_TREE',nodeToUpdate);
		
		
		
	}
	
	/**
	 * Clears the store of workspace resources - 
	 * prob could make more granular by passing in a folder ID to clearTODO:
	 * @usage   
	 * @return  
	 */
	public function clearWorkspaceCache(){
		_workspaceResources = new Hashtable();
		//set up the inital folders again.. use the copy from startup time
		parseDataForTree(_accessibleWorkspaceFoldersDTOCopy);
	}
	
	//getts n setters
	
	
	/**
	 * 
	 * @usage   
	 * @param   newworkpaceResources 
	 * @return  
	 */
	public function set workspaceResources (newworkspaceResources:Hashtable):Void {
		_workspaceResources = newworkspaceResources;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get workspaceResources ():Hashtable {
		return _workspaceResources;
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


}
