import org.lamsfoundation.lams.learner.ls.LessonModel;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.learner.*

/*
* Make changes to Lesson's model data based on user input
*/
class LessonController extends AbstractController {
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	private var _lessonModel:LessonModel;
	private var _app:Application;
	private var _comms:Communication;

	
	public function LessonController (cm:Observable) {
		super (cm);
		_app = Application.getInstance();
		_comms = _app.getComms();
		_lessonModel = LessonModel(model);
	}
	
	/**
	 * Recieves the click events from the Lesson buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function click(evt):Void{
		
		_lessonModel = LessonModel(model);
		Debugger.log('click evt.target.label:'+evt.target.label,Debugger.GEN,'click','LessonController');
		var tgt:String = new String(evt.target);
		if(tgt.indexOf("join") != -1){
			_lessonModel.getLesson().joinLesson();
		}
	}
	
	
}