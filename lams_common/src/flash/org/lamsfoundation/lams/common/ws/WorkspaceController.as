import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ui.*
import mx.utils.*

/*
* Makes changes to the model's data based on user input.
*/
class org.lamsfoundation.lams.common.ws.WorkspaceController extends AbstractController {
	
	private var _workspaceModel:WorkspaceModel;
	private var _workspaceController:WorkspaceController;
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function WorkspaceController (wm:Observable) {
		super (wm);
		_workspaceController = this;
		_workspaceModel = WorkspaceModel(wm);
	}
   
   
   

	/**
	 * called when the dialog is loaded, calles methods to set up content in dialogue
	 * also sets up the okClicked event listener
	 * @usage   
	 * @param   evt 
	 * @return  
	 */
    public function openDialogLoaded(evt:Object) {
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.WorkspaceController');
        //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.WorkspaceView');
            evt.target.scrollContent.addEventListener('okClicked',Delegate.create(this,okClicked));
            evt.target.scrollContent.addEventListener('locationTabClick',Delegate.create(this,locationTabClick));
            evt.target.scrollContent.addEventListener('propertiesTabClick',Delegate.create(this,propertiesTabClick));
            //evt.target.scrollContent.addEventListener('click',Delegate.create(this,click));
			//set a ref to the view
			evt.target.scrollContent.workspaceView = getView();
			//set a ref to the dia in the view
			getView().workspaceDialogue = evt.target.scrollContent;
			
			
			//set up UI
			evt.target.scrollContent.setUpContent();
			
			//select the right tab, dont pass anything to show the default tab
			_workspaceModel.showTab(_workspaceModel.currentTab);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
    /**/
	private function locationTabClick(evt:Object) {
        Debugger.log('locationTabClick:'+evt.type,Debugger.GEN,'locationTabClick','org.lamsfoundation.lams.WorkspaceController');
        _workspaceModel.showTab("LOCATION");
    }
	
	private function propertiesTabClick(evt:Object) {
        Debugger.log('propertiesTabClick:'+evt.type,Debugger.GEN,'propertiesTabClick','org.lamsfoundation.lams.WorkspaceController');
        _workspaceModel.showTab("PROPERTIES");
    }
	
    /**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
        Debugger.log('!okClicked:'+evt.type+', now follows the resultDTO:',Debugger.GEN,'okClicked','org.lamsfoundation.lams.WorkspaceController');
        //Check type is correct
		if(evt.type == 'okClicked'){
            //Call the callback, passing in the design selected designId
			_global.breakpoint();
            //okClickedCallback(evt.target.selectedDesignId);
            //_workspaceModel.getWorkspace().itemSelected(evt.target.selectedDesignId);
            //_workspaceModel.getWorkspace().onOKCallback(evt.target.selectedDesignId);
			
			//invalidate the cache of folders
			_workspaceModel.clearWorkspaceCache(evt.target.resultDTO.targetWorkspaceFolderID);
			
			//pass the resultant DTO back to the class that called us.
            _workspaceModel.getWorkspace().onOKCallback(evt.target.resultDTO);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	    /**
    * Workspace dialog OK button clicked handler
    */
    private function clickFromDialog(evt:Object) {
        Debugger.log('clickFromDialog:'+evt.type,Debugger.GEN,'clickFromDialog','org.lamsfoundation.lams.WorkspaceController');
        
    }
	
	/**
    * Invoked when the node is opened.  it must be a folder
    */
    public function onTreeNodeOpen (evt:Object){
		var treeview = evt.target;
		var nodeToOpen:XMLNode = evt.node;
		Debugger.log('nodeToOpen workspaceFolderID:'+nodeToOpen.attributes.data.workspaceFolderID,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.WorkspaceController');
		Debugger.log('nodeToOpen resourceID:'+nodeToOpen.attributes.data.resourceID,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.WorkspaceController');
		//if this ndoe has children then the 
		//data has already been got, nothing to do
		
		if(!nodeToOpen.hasChildNodes()){
		
			
			
			//if the resourceID is null then use the folderID
			//var resourceToOpen = (nodeToOpen.attributes.data.resourceID) ? nodeToOpen.attributes.data.resourceID : nodeToOpen.attributes.data.workspaceFolderID;
			
			// DC24-01-06 this resource ID must refer to a folder as its been marked as a branch
			var resourceToOpen = nodeToOpen.attributes.data.resourceID;
			
			
			//TODO: I think it must be a folder ID, depoends if this event is fired for an "open" reousrce click
			_workspaceModel.openFolderInTree(resourceToOpen);
				
		}else{
			Debugger.log('nodeToOpen already has children in cache',Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.WorkspaceController');
			
		}
		
	   
    }
	
	/**
    * Treeview data changed event handler
    */
    public function onTreeNodeClose (evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onTreeNodeClose','org.lamsfoundation.lams.WorkspaceController');
		var treeview = evt.target;
	   
	   
	   
	   
	   
    }
	
	public function onTreeNodeChange (evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onTreeNodeChange','org.lamsfoundation.lams.WorkspaceController');
		var treeview = evt.target;
		_workspaceModel.setSelectedTreeNode(treeview.selectedNode);
		
		
		
	}
	
	public function onDragComplete(evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onDragComplete','org.lamsfoundation.lams.WorkspaceController');
		var treeview = evt.target;
		//_workspaceModel.setSelectedTreeNode(treeview.selectedNode);
		
		
		
	}
	
	
	
		/**
	 * Handles the events from the cut, copy, paste n delete buttons
	 * @usage   
	 * @param   e 
	 * @return  
	 */
	public function fileOperationRequest(e:Object){
		var tgt:String = new String(e.target);
		var workspaceDialogue = getView().workspaceDialogue;
		Debugger.log('type:'+e.type+',target:'+tgt,Debugger.GEN,'fileOperationRequest','org.lamsfoundation.lams.WorkspaceController');
		//get the selected node:
		var snode = workspaceDialogue.treeview.selectedNode;
		
		//Number(snode.attributes.data.resourceID);
		//check target for button name
		/* TODO: Add cut implementation, for now just scrap it :-)
		if(tgt.indexOf("cut_btn") != -1){
			_workspaceModel.setClipboardItem(snode,"CUT");
			//TODO: Be nice to dim the branch in the tree
			
		}else 
		*/
		_global.breakpoint();
		if(tgt.indexOf("copy_btn") != -1){
			_workspaceModel.setClipboardItem(snode.attributes.data);
			
		}else if(tgt.indexOf("paste_btn") != -1){
			var itemToPaste = _workspaceModel.getClipboardItem();
			if(itemToPaste != null){
				//find out the selected folderID:
				//get the selected node:
				var snodeData = workspaceDialogue.treeview.selectedNode.attributes.data;
				var selectedFolderID:Number;
				//if its a folder then the resourceID is the selected folder ID, otherwise its the parent
				if(snodeData.resourceType == "Folder"){
					selectedFolderID = snodeData.resourceID;
				}else{
					selectedFolderID = snodeData.workspaceFolderID;
				}
				
				_workspaceModel.folderIDPendingRefresh = selectedFolderID;
				
				Debugger.log('Selected (target) folder ID=:'+selectedFolderID,Debugger.GEN,'fileOperationRequest','org.lamsfoundation.lams.WorkspaceController');
				
				
					_workspaceModel.getWorkspace().requestCopyResource(itemToPaste.resourceID,selectedFolderID,itemToPaste.resourceType);
			}else{
				//nothing to paste..
			}

		}else if(tgt.indexOf("delete_btn") != -1){
			//find out the selected folderID:
			//get the selected node:
			var snodeData = workspaceDialogue.treeview.selectedNode.attributes.data;
			_workspaceModel.folderIDPendingRefresh = snodeData.workspaceFolderID;
			_workspaceModel.getWorkspace().requestDeleteResource(snodeData.resourceID,snodeData.resourceType);
			
		}else if(tgt.indexOf("new_btn") != -1){
			//check we can create a folder here
			var snodeData = workspaceDialogue.treeview.selectedNode.attributes.data;
			if(snodeData != null){
				Dialog.createInputDialog('__Enter the new folder name__', '__OK__', '__Cancel__', Delegate.create(_workspaceController ,setNewFolderName),null);
			}else{
				//no where to make new folder
			}
			
			//_workspaceModel.getWorkspace().requestCreateFolder();
			
		}
		//get selected node from the tree
		
		//check its ok do do the operation
			
		//if so do the operation - sned to controller, balh balhblah  		
		
		//invalidate the cahe, re,load the data, refresh the tree
		
		
		//TODO: integrate with key listener for canvas!! CTRL-C is handels by the canvas at the mo... need to set somethign in application.#
		
	}
	
	public function setNewFolderName(newName:String){
		Debugger.log('newName:'+newName,Debugger.GEN,'setNewFolderName','org.lamsfoundation.lams.WorkspaceController');
		var workspaceDialogue = getView().workspaceDialogue;
		var snodeData = workspaceDialogue.treeview.selectedNode.attributes.data;
		var selectedFolderID:Number;
		//if its a folder then the resourceID is the selected folder ID, otherwise its the parent
		if(snodeData.resourceType == "Folder"){
			selectedFolderID = snodeData.resourceID;
		}else{
			selectedFolderID = snodeData.workspaceFolderID;
		}
		_workspaceModel.folderIDPendingRefresh = selectedFolderID;
		//TODO: Validate is allowed name
		_workspaceModel.getWorkspace().requestNewFolder(selectedFolderID,newName);
	}
	
	
	
	//override the super version
	public function getView(){
		var v = super.getView();
		return WorkspaceView(v);
		
	}
	
	/**
    * Treeview data changed event handler
    
    private function onTvChange (event:Object){
        if (treeview == event.target) {
            var node = treeview.selectedItem;
            
            // If this is a branch, expand/collapse it
            if (treeview.getIsBranch(node)) {
                treeview.setIsOpen(node, !treeview.getIsOpen(node), true);
            }
            
            // If this is a hyperlink, jump to it
            var url = node.attributes.url;
            if (url) {
                getURL(url, "_top");
            }
            
            // Clear any selection
            treeview.selectedNode = null;
        }
    }
    
   */
}
