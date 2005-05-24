import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.ws.*
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.authoring.*
import mx.managers.*

/**
* Authoring view for the canvas
* @author   DI
*/
class org.lamsfoundation.lams.common.ws.WorkspaceView extends AbstractView 
{
	//Canvas clip
	private var _workspace_mc:MovieClip;
	private var workspaceDialog:MovieClip;
	
	/*
	* Constructor
	*/
	public function WorkspaceView (m:Observable, c:Controller){
		// Invoke superconstructor, which sets up MVC relationships.
		// This view has no user inputs, so no controller is required.
		super (m, c);
	}
	
	/*
	* Updates state of the canvas, called by Canvas Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
		//Go through update object and update mc with visual changes required 
	}
    
    /**
    * Create a popup dialog containing workspace
    * @param    pos - Position, either 'centre' or an object containing x + y coordinates
    */
    public function createWorkspacePopup(pos:Object){
        //Check to see whether this should be a centered or positioned dialog
        if(typeof(pos)=='string'){
            var popup = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue(0),closeButton:true,scrollContentPath:'workspaceDialog'});
            //popup.centre();
        } else {
            var popup = PopUpManager.createPopUp(Application.root, LFWindow, true,{title:Dictionary.getValue(0),closeButton:true,scrollContentPath:'workspaceDialog',_x:pos.x,_y:pos.y});
        }
    }
}
