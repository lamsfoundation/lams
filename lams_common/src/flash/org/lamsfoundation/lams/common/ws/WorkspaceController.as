import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import mx.utils.*

/*
* Makes changes to the model's data based on user input.
*/
class org.lamsfoundation.lams.common.ws.WorkspaceController extends AbstractController {
	
	private var _workspaceModel:WorkspaceModel;
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function WorkspaceController (wm:Observable) {
		super (wm);
		_workspaceModel = WorkspaceModel(wm);
	}
   
   
   
   /**
    * called when the dialog is loaded
    */
    public function openDialogLoaded(evt:Object) {
        Debugger.log('!evt.type:'+evt.type,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.WorkspaceController');
        //Check type is correct
        if(evt.type == 'contentLoaded'){
            //Set up callback for ok button click
            Debugger.log('!evt.target.scrollContent:'+evt.target.scrollContent,Debugger.GEN,'openDialogLoaded','org.lamsfoundation.lams.WorkspaceView');
            evt.target.scrollContent.addEventListener('okClicked',Delegate.create(this,okClicked));
			//set a ref to the view
			evt.target.scrollContent.workspaceView = getView();
			//set a ref to the dia in the view
			getView().workspaceDialogue = evt.target.scrollContent;
			
			//set up UI
			evt.target.scrollContent.setUpContent();
			
			
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
    
    /**
    * Workspace dialog OK button clicked handler
    */
    private function okClicked(evt:Object) {
        Debugger.log('!okClicked:'+evt.type,Debugger.GEN,'okClicked','org.lamsfoundation.lams.WorkspaceController');
        //Check type is correct
		if(evt.type == 'okClicked'){
            //Call the callback, passing in the design selected designId
			
            //okClickedCallback(evt.target.selectedDesignId);
            _workspaceModel.getWorkspace().itemSelected(evt.target.selectedDesignId);
        }else {
            //TODO DI 25/05/05 raise wrong event type error 
        }
    }
	
	/**
    * Invoked when the node is opened.  it must be a folder
    */
    public function onTreeNodeOpen (evt:Object){
		var treeview = evt.target;
		var nodeToOpen:XMLNode = evt.node;
		
		//if this ndoe has children then the 
		//data has already been got, nothing to do
		
		if(!nodeToOpen.hasChildNodes()){
		
			Debugger.log('nodeToOpen workspaceFolderID:'+nodeToOpen.attributes.data.workspaceFolderID,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.WorkspaceController');
			Debugger.log('nodeToOpen resourceID:'+nodeToOpen.attributes.data.resourceID,Debugger.GEN,'onTreeNodeOpen','org.lamsfoundation.lams.WorkspaceController');
			
			//if the resourceID is null then use the folderID
			var resourceToOpen = (nodeToOpen.attributes.data.resourceID) ? nodeToOpen.attributes.data.resourceID : nodeToOpen.attributes.data.workspaceFolderID;
			
			
			//TODO: I think it must be a folder ID, depoends if this event is fired for an "open" reousrce click
			_workspaceModel.openResourceInTree(resourceToOpen);
				
		}
		
	   
    }
	
	/**
    * Treeview data changed event handler
    */
    public function onTreeNodeClose (evt:Object){
		Debugger.log('type::'+evt.type,Debugger.GEN,'onTreeNodeClose','org.lamsfoundation.lams.WorkspaceController');
		var treeview = evt.target;
	   
	   
	   
	   
	   
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
