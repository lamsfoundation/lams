import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.authoring.*
import org.lamsfoundation.lams.authoring.cv.*
import org.lamsfoundation.lams.authoring.tk.*
/**
* Controller for the toolkit
*/
class ToolkitController extends AbstractController {
	private var _toolkitModel:ToolkitModel;
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function ToolkitController (cm:Observable) {		super (cm);
		_toolkitModel = ToolkitModel(model);
		//Debugger.log('fINISHED',4,'Constructor','ToolkitController');
	}
    
	/**
	*Called by ToolkitActivity when one in clicked
	* @param toolkitActivity - the toolkitActivity that was clicked
	*/
	public function selectTemplateActivity(templateActivity:TemplateActivity):Void{
		//Debugger.log('templateActivity:'+templateActivity.getTemplateActivityData().title,4,'selectTemplateActivity','ToolkitController');
		//_global.breakpoint();
		_toolkitModel = ToolkitModel(model);
		_toolkitModel.setSelectedTemplateActivity(templateActivity);
	}
	
	/**
	*Called by the view when a template activity icon is dropped
	*/
	public function iconDrop(dragIcon_mc:MovieClip):Void{
		//lets do a test to see if we got the canvas
		var cv:Canvas = Application.getInstance().getCanvas();
		var canvasView:MovieClip = cv.getCanvasView().getViewMc();
	
		//SEE IF ITS HIT the canvas
		var isCanvasDrop:Boolean = canvasView.hitTest(dragIcon_mc);
		Debugger.log('isCanvasDrop:'+isCanvasDrop,4,'dropIcon','TemplateActivity');
		if(isCanvasDrop){			//remove the drag icon
			dragIcon_mc.removeMovieClip();
			
			var ta:TemplateActivity;
			ta = _toolkitModel.getSelectedTemplateActivity();			Debugger.log('ta:'+ta.getTemplateActivityData().title,4,'canvasDrop','ToolkitController');		
			cv.setDroppedTemplateActivity(ta);
		}
		
				
	
	
	
	
	}
	
	
	
	
       
}
