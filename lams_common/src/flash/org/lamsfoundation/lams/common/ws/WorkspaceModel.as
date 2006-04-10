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
	
	private var _availableLicenses:Array;
	

	
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
		_availableLicenses = new Array();
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
	
	/**
	 * Shows one or other of the tabs in the dialog
	 * @usage   
	 * @param   tabToSelect can be either LOCATION or PROPERTIES
	 * @return  
	 */
	public function showTab(tabToSelect:String){
		if(tabToSelect == undefined){
			tabToSelect = _defaultTab;
		}
		broadcastViewUpdate('SHOW_TAB',tabToSelect);
		
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
	 * Sets up the tree for the 1st time
	 * Creates the dummy root folder with ID -1
	 * @usage   
	 * @return  
	 */
	public function initWorkspaceTree(){
		Debugger.log('Running',Debugger.GEN,'initWorkspaceTree','org.lamsfoundation.lams.WorkspaceModel');
		_treeDP = new XML();
		_workspaceResources = new Array();
		//add top level
		//create the data obj:
		var mdto= {};
		mdto.creationDateTime = new Date(null);
		mdto.description = "";
		mdto.lastModifiedDateTime = new Date(null);
		//why is this returning undefines
		mdto.name = Dictionary.getValue('ws_tree_mywsp');
		trace(Dictionary.getValue('ws_dlg_location_button'));
		mdto.parentWorkspaceFolderID = null;
		//read only
		mdto.permissionCode = 1;
		mdto.resourceID = "-1";
		mdto.resourceType = RT_FOLDER;
		mdto.workspaceFolderID = null;
		
		
		var rootNode = _treeDP.addTreeNode(mdto.name,mdto);
		rootNode.attributes.isBranch = true;
		setWorkspaceResource(RT_FOLDER+'_'+mdto.resourceID,rootNode);
		//ObjectUtils.printObject(getWorkspaceResource(RT_FOLDER+'_'+mdto.resourceID));
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
			Debugger.log('Did not find:'+dto.resourceType+'_'+dto.workspaceFolderID+' Something bad has happened',Debugger.CRITICAL,'setFolderContents','org.lamsfoundation.lams.WorkspaceModel');
		}
		
		
		// go throught the contents of DTO and add it aas children to the node to update.
		for(var i=0; i<dto.contents.length; i++){
			var key:String = dto.contents[i].resourceType+'_'+dto.contents[i].resourceID;
			
			var cNode = nodeToUpdate.addTreeNode(dto.contents[i].name,dto.contents[i]);
			//var cNode = nodeToUpdate.addTreeNode(dto.contents[i].name+':'+key,dto.contents[i]);
			cNode.attributes.data.parentWorkspaceFolderID = dto.parentWorkspaceFolderID;
			//workspaceFolderID should always be the ID of the folder this resource is contained in
			cNode.attributes.data.workspaceFolderID = dto.workspaceFolderID;
			//check if its a folder
			if(dto.contents[i].resourceType==RT_FOLDER){
				cNode.attributes.isBranch=true;	
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
				  //mental. but true - you have to add them to an array and then delete them from the array, 
				  //as deleting in the loop them will remove the reference from nextSibling
				  // also childNodes:Array not seem to work properly.
				  // use firstChild to iterate through the child nodes of wspResource
				  for (var aNode:XMLNode = wspResource.firstChild; aNode != null; aNode=aNode.nextSibling) {
					deleteQue.push(aNode);
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
	
	/**
	 * If mroe than one fodler is pending refresh - use this and ensure the _folderIDPendingRefreshList is populated
	 * @usage   
	 * @return  
	 */
	public function clearWorkspaceCacheMultiple(){
		if(_folderIDPendingRefreshList != null){
			for (var i=0;i<_folderIDPendingRefreshList.length;i++){
				var r = clearWorkspaceCache(_folderIDPendingRefreshList[i]);
				if(r=='CLEARED_ALL'){
					return;
				}else{
					//now open this node in the tree
					autoOpenFolderInTree(_folderIDPendingRefreshList[i]);
				}
			}
			_folderIDPendingRefreshList = new Array();
		}else{
			//raise error
		}
	}
	

	/**
	 * Tells the dialog to open a folder, as if the user had expanded the node
	 * NOTE - this manually generated the onOpen event for the controller
	 * @usage   
	 * @param   folderID The folder to open
	 * @return  
	 */
	public function autoOpenFolderInTree(folderID){
		var nodeToOpen:XMLNode = getWorkspaceResource('Folder_'+folderID);
		Debugger.log('Opening node:'+nodeToOpen,Debugger.GEN,'autoOpenFolderInTree','org.lamsfoundation.lams.WorkspaceModel');
		
		broadcastViewUpdate('REFRESH_FOLDER',nodeToOpen);
	}
	
	/**
	 * Opens folder but does not generate the event (therefore does not request the children, currently not used
	 * @usage   
	 * @param   folderID The folder to open
	 * @return  
	 */
	public function setFolderOpen(folderID){
		var nodeToOpen:XMLNode = getWorkspaceResource('Folder_'+folderID);
		Debugger.log('Basic open folder call:'+nodeToOpen,Debugger.GEN,'setFolderOpen','org.lamsfoundation.lams.WorkspaceModel');
		
		broadcastViewUpdate('OPEN_FOLDER',nodeToOpen);
		
	}
	
	/**
	 * Telkls the dialog that the _availableLicenses array is now full and the combo can be populated
	 * @usage   
	 * @return  
	 */
	public function populateLicenseDetails(){
		broadcastViewUpdate('POPULATE_LICENSE_DETAILS',_availableLicenses);
	}
	
	
	/**
	 * Sticks an item on the clipboard
	 * @usage   
	 * @param   item 
	 * @param   mode 
	 * @return  
	 */
	public function setClipboardItem(item:Object,mode:String){
		//mode not used :-) no cut, only copy functionality this time.
		_clipboardMode = mode;
		_clipboard = item;
		
	}
	
	/**
	 * retrieves the item from the clipboard
	 * @usage   
	 * @return  
	 */
	public function getClipboardItem():Object{
		return _clipboard;
	}
	
	/**
	 * Checks to see if the user can write to this resource
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

	/**
	 * <wddxPacket version='1.0'><header/><data>
	 * <struct type='Lorg.lamsfoundation.lams.util.wddx.FlashMessage;'>
	 * <var name='messageKey'><string>getAvailableLicenses</string></var>
	 * <var name='messageType'><number>3.0</number></var><var name='messageValue'>
	 * <array length='6'><struct type='Lorg.lamsfoundation.lams.learningdesign.dto.LicenseDTO;'>
	 * <var name='code'><string>by-nc-sa</string></var>
	 * <var name='defaultLicense'><boolean value='true'/></var>
	 * <var name='licenseID'><number>1.0</number></var>
	 * <var name='name'><string>Attribution-Noncommercial-ShareAlike 2.5</string></var>
	 * <var name='pictureURL'><string>http://localhost:8080/lams//images/license/byncsa.jpg</string></var>
	 * <var name='url'><string>http://creativecommons.org/licenses/by-nc-sa/2.5/</string></var>
	 * </struct>
	 * * @usage   
	 * @param   newavailableLicenses 
	 * @return  
	 */
	public function setAvailableLicenses (newavailableLicenses:Array):Void {
		newavailableLicenses.sortOn('name');
		_availableLicenses = newavailableLicenses;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getAvailableLicenses ():Array {
		return _availableLicenses;
	}
}
