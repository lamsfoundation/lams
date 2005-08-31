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
	public function ToolbarController (cm:Observable) {
		super (cm);
	}
    
    public function newDesign(){

    }

    public function openDesign(){
        var app = Application.getInstance();
        app.getCanvas().openDesignBySelection();
    }
    
    public function saveDesign(){
        
    }
    
    public function buttonClicked(name:String){
        //trace(name + ' button was clicked');
        //Dispatch button control to relevant area    
        switch (name) {
            case 'new' :
                newDesign();
                break;
            case 'open' :
                //Call main open method on toolbar class
                openDesign();
                break;
            case 'save' :
                saveDesign();
                break;
            default :
                //TODO: DI-Deal with default case where button type is not caught
                trace('Unknown button click');
        }
    }   
}
