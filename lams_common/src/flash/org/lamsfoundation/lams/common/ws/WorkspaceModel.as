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
	
	//this contains refs to the tree nodes stored by resourceID
	private var _workspaceResources:Hashtable;
	//this is the dartaprovider for the tree
	private var _treeDP:XML;

	
	
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
		
		
	}	
	
	
	public function openDesignBySelection(){
		//Open the workspace dialog in the centre of the screen
		//Pass in the function to be called when a design is selected
        //workspaceView.createWorkspaceDialogOpen('centre',Delegate.create(this,itemSelected));
		
		var dto:Object = {};
		dto.pos='centre';
		Debugger.log('_workspace.itemSelected:'+_workspace.itemSelected,Debugger.GEN,'openDesignBySelection','WorkspaceModel');
		dto.callback=Delegate.create(_workspace,_workspace.itemSelected);
		broadcastViewUpdate('CREATE_DIALOG',dto);
		
	}
	
	public function getUsersWorkspace(){
		
	}
	
	public function getFolderContents(){
		
	}
	
	
	/**
    * Notify registered listeners that a data model change has happened
    */
    public function broadcastViewUpdate(_updateType,_data){
        dispatchEvent({type:'viewUpdate',target:this,updateType:_updateType,data:_data});
        trace('broadcast');
    }
	
	/**
	 * Converts the de-serialised WDDX XML from an object into 
	 * an xml format the tree likes
	 * This is parsing data from the call to getAccessibleWOrkspaceFolders, so we know that all the
	 * elements must be folders, as such we must set isBranch to be true;
	 * @usage   
	 * @param   dto - contains:		
	 * PRIVATE The folder which belongs to the given User
	 * RUN_SEQUENCES The folder in which user stores his lessons
	 * ORGANISATIONS List of folders (root folder only) which belong to organizations of which user is a member
	 */
	public function parseDataForTree(dto:Object):Void{
		_treeDP = new XML();
		//add top level
		_treeDP.addTreeNode("My Workspace",0);
		//add 3 folders
		var fChild:XMLNode = _treeDP.firstChild;
		var orgNode:XMLNode = fChild.addTreeNode("Organisations",null);
		orgNode.attributes.isBranch = true;
				
		var privateNode:XMLNode = fChild.addTreeNode("Private",dto.PRIVATE);
		privateNode.attributes.isBranch = true;
		//privateNode.attributes.data = dto.PRIVATE;
		_workspaceResources.put(dto.PRIVATE.workspaceFolderID,privateNode);
				
		
		var runNode:XMLNode = fChild.addTreeNode("Run Sequences",dto.RUN_SEQUENCES);
		runNode.attributes.isBranch = true;
		//runNode.attributes.data = dto.RUN_SEQUENCES;
		_workspaceResources.put(dto.RUN_SEQUENCES.workspaceFolderID,runNode);
		
		
		for(var i=0;i<dto.ORGANISATIONS.length;i++){
			var n = dto.ORGANISATIONS[i];
			var nNode:XMLNode = orgNode.addTreeNode(n.name,n);
			nNode.attributes.isBranch = true;
			//nNode.attributes.data = n;
			_workspaceResources.put(n.workspaceFolderID,nNode);
			
		}
		
		/*
		for(var i=0;i<dto.PRIVATE.length;i++){
			var n = dto.PRIVATE[i];
			nNode = privateNode.addTreeNode(n.name,n);
			nNode.attributes.isBranch = true;
			nNode.attributes.data = n;
			_workspaceResources.put(n.workspaceFolderID,nNode);
			
		}		
		
		for(var i=0;i<dto.RUN_SEQUENCES.length;i++){
			var n = dto.RUN_SEQUENCES[i];
			nNode = runNode.addTreeNode(n.name,n);
			nNode.attributes.isBranch = true;
			nNode.attributes.data = n;
			_workspaceResources.put(n.workspaceFolderID,nNode);
		}
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
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function setFolderContents(dto:Object){
		var nodeToUpdate:XMLNode;
		Debugger.log('dto.workspaceFolderID:'+dto.workspaceFolderID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
		//_global.breakpoint();
		if(_workspaceResources.containsKey(dto.workspaceFolderID)){
			nodeToUpdate = _workspaceResources.get(dto.workspaceFolderID);
			Debugger.log('nodeToUpdate.attributes.data.resourceID:'+nodeToUpdate.attributes.data.resourceID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			Debugger.log('nodeToUpdate.attributes.data.workspaceFolderID:'+nodeToUpdate.attributes.data.workspaceFolderID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
		}else{
			//think this wont ever happen as must have been listed by prevous node
			nodeToUpdate = new XMLNode();
		}
		
		
		
		for(var i=0; i<dto.contents.length; i++){
			var cNode = nodeToUpdate.addTreeNode(dto.contents[i].name,dto.contents[i]);
			//check if its a folder
			if(dto.contents[i].resourceType=="Folder"){
				cNode.attributes.isBranch=true;
				//copy the resourceID to folderID
				cNode.attributes.data.workspaceFolderID=dto.contents[i].resourceID;
			}
			Debugger.log('Adding new node to _workspaceResources ID :'+dto.contents[i].resourceID,Debugger.GEN,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
			_workspaceResources.put(dto.contents[i].resourceID,cNode);
		}
		
		//dispatch an update to the view
		broadcastViewUpdate('REFRESH_TREE',nodeToUpdate);
		
		
		
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
	

	
	

}
