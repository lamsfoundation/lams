import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.common.util.*
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
        //Open the workspace dialog in the centre of the screen
        workspaceView.createWorkspaceDialog('centre',Delegate.create(this,itemSelected));
    }
    
    /**
    * Called when design has been selected from within the workspace dialog, inovked via callback method.
    */
    private function itemSelected(designId:Number){
        Debugger.log('!!designID:'+designId,Debugger.GEN,'itemSelected','org.lamsfoundation.lams.Workspace');

        //Design has been chosen, get Canvas to open design
        Application.getInstance().getCanvas().openDesignById(designId);
    }
}
