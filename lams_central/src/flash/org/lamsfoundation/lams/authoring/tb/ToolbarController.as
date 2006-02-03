import org.lamsfoundation.lams.authoring.tb.ToolbarModel
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*


/*
* Makes changes to the Canvas Authoring model's data based on user input.
*/
class org.lamsfoundation.lams.authoring.tb.ToolbarController extends AbstractController {
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	private var _app:Application;
	
	public function ToolbarController (cm:Observable) {
		super (cm);
		_app = Application.getInstance();
	}
    
    
    
	/**
	 * Recieves the click events from the Toolbar buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function click(evt):Void{
		Debugger.log('click evt.target.label:'+evt.target.label,Debugger.GEN,'click','ToolbarController');
		switch (evt.target.label) {
            case 'New' :
                _app.getCanvas().clearCanvas(false);
                break;
            case 'Open' :
                //Call main open method on toolbar class
				_app.getCanvas().openDesignBySelection();
                break;
            case 'Save' :
                _app.getCanvas().saveDesign();
                break;
			case 'Copy' :
                _app.copy();
                break;
			case 'Paste' :
                _app.copy();
                break;
			case 'Transition' :
                _app.getCanvas().toggleTransitionTool();
                break;
			case 'Optional' :
                
                break;
			case 'Preview' :
                
                break;
            default :
                //TODO: DI-Deal with default case where button type is not caught
                trace('Unknown button click');
        }
	}
     
}
