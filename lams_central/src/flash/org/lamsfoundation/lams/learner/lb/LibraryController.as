import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.learner.*
import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.learner.ls.Lesson;

/**
* Controller for the sequence library
*/
class LibraryController extends AbstractController {
	private var _libraryModel:LibraryModel;
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	public function LibraryController (cm:Observable) {
		super (cm);
		_libraryModel = LibraryModel(model);
	}
	
	// control methods
	
	/**
	*Called by Lesson when one in clicked
	* @param lesson - the lesson that was clicked
	*/
	public function selectLesson(lesson:Lesson):Void{
		_libraryModel = LibraryModel(model);
		_libraryModel.setSelectedLesson(lesson);
	}
	
	public function getActiveLessons():Void {
		_libraryModel = LibraryModel(model);
		_libraryModel.getLibrary().getActiveLessons();
	}
	
}