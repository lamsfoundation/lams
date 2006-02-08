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
		var tgt:String = new String(evt.target);
		
		if(tgt.indexOf("new") != -1){
			_app.getCanvas().clearCanvas(false);
		
		}else if(tgt.indexOf("open") != -1){
			_app.getCanvas().openDesignBySelection();
		}else if(tgt.indexOf("save") != -1){
			_app.getCanvas().saveDesign();
		}else if(tgt.indexOf("copy") != -1){
			_app.copy();
		}else if(tgt.indexOf("paste") != -1){
			_app.paste();
		}else if(tgt.indexOf("trans") != -1){
			_app.getCanvas().toggleTransitionTool();
		}else if(tgt.indexOf("optional") != -1){
			
		}else if(tgt.indexOf("gate") != -1){
			_app.getCanvas().startGateTool();
		}else if(tgt.indexOf("preview") != -1){
			
		}
	}
     
}
