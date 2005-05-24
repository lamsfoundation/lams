import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.authoring.*

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
	}
	
    /**
    * This is the method called when the user opens a design
    * 
    * @param id - the Learning design ID 
    */
    public function userSelectItem(){
        trace('Workspace.userSelectItem');
        //todo DI 07/04/05 write code user design selection, just a stub at the moment, calls itemSelected
        itemSelected(1);
        //Open the workspace dialog in the centre of the screen
        workspaceView.createWorkspacePopup('centre');
    }
    
    private function itemSelected(designId:Number){
        //Design has been chosen, get Canvas to open design
        Application.getInstance().getCanvas().openDesignById(designId);
    }
}
