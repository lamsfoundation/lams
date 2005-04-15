import org.lamsfoundation.lams.common.ws.CanvasModel
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*

/*
* Makes changes to the Canvas Authoring model's data based on user input.
*/
class org.lamsfoundation.lams.common.ws.WorkspaceController extends AbstractController {
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function WorkspaceController (cm:Observable) {
		super (cm);
	}
   
}
