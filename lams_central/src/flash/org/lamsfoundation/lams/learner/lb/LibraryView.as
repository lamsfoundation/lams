import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*;

import mx.managers.*;
import mx.controls.*;
import mx.events.*

/**
* Learner view for the Sequences (Lesson) Library
* @author Mitchell Seaton
*/
class LibraryView extends AbstractView {
	
	private var _className = "LibraryView";
	private var _depth:Number;
	
	private var bkg_pnl:MovieClip;
	
	private var _library_mc:MovieClip;
	
	private var learningSequences_sp:MovieClip;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;    

	/**
	* Constructor
	*/
	public function LibraryView(){	
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);     
	}
	
	/**
	* Initialisation - sets up the mvc relations ship Abstract view.
	* 
	*/
	public function init(m:Observable, c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		MovieClipUtils.doLater(Proxy.create(this,createLibrary));
		
	}
	
	/**
	* Sets up the Library (clip)
	*/
	public function createLibrary() {
		_library_mc = this;
		_depth = this.getNextHighestDepth();
		
		//Add the button handlers, essentially this is handing on clicked event to controller.
        var controller = getController();
		controller.getActiveLessons();
		
		 //Now that view is setup dispatch loaded event
		dispatchEvent({type:'load',target:this});
	   
	}
	
	/*
	* Updates state of the Library, called by Library Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
	    //Cast the generic observable object into the Toolbar model.
        var lm:LibraryModel = LibraryModel(o);
			//Update view from info object
        switch (infoObj.updateType) {
            case 'POSITION' :
                setPosition(lm);
                break;
            case 'SIZE' :
                setSize(lm);
                break;
			case 'SEQUENCES_UPDATED' :
                updateSequences(o);
                break;
			case 'LESSON_SELECTED' :
				updateSelectedLesson(o);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LibraryView');
        }
	}
	
	/**
	* Updates learning sequence library.  Creates a Lesson mc / class for each one.
	* NOTE: Each library element contains a single lesson object.
	*
	* @param   o   		The model object that is broadcasting an update.
	*/
	private function updateSequences(o:Observable){

		var yPos:Number = 0;
		
		//set SP the content path:
		learningSequences_sp.contentPath = "empty_mc";
		
		var lbv = LibraryView(this);
		var lbm = LibraryModel(o);
		
		//get the hashtable
		var mySeqs:Hashtable = lbm.getLearningSequences();
	
		//loop through the sequences
		var keys:Array = mySeqs.keys();
		
		for(var i=0; i<keys.length; i++){
			
			var learningSeq:Object = mySeqs.get(keys[i]);
			
			//NOW we pass in the Lesson instance
			var _lesson:Lesson = learningSeq.classInstanceRefs;
			
			var lesson_mc = learningSequences_sp.content.attachMovie("Lesson","ls_"+_lesson.getLessonID(),_depth++,{_lesson:Lesson,_libraryView:lbv});
			
			//position it
			lesson_mc._y = yPos;
			yPos += lesson_mc._height;
						
			
		}
		
		
	}
	
	
	/**
	*The currently selected Lesson
	* 
	* @param   o   		The model object that is broadcasting an update.
	*/
	private function updateSelectedLesson(o:Observable):Void{

		//get the model
		var lm = LibraryModel(o);
		
		//set the states of Lessons
		var l = lm.getLastSelectedLesson();
		l.setInactive();
		var c = lm.getSelectedLesson();
		c.setActive();
		
		
	}
	
	 /**
    * Sets the size of the Toolbar on stage, called from update
    */
	private function setSize(lm:LibraryModel):Void{
		
        var s:Object = lm.getSize();
        
		//Size panel
		trace('lesson view  setting width to '+s.w);
		bkg_pnl.setSize(s.w,bkg_pnl._width);
	}
	
    /**
    * Sets the position of the Lesson on stage, called from update
    * @param lm Lesson model object 
    */
	private function setPosition(lm:LibraryModel):Void{
        var p:Object = lm.getPosition();
        this._x = p.x;
        this._y = p.y;
	}
	
	/**
	* Gets the LibraryModel
	*
	* @returns model 
	*/
	public function getModel():LibraryModel{
			return LibraryModel(model);
	}

    /**
    * Returns the default controller for this view (LibraryController).
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model:Observable):Controller {
        return new LibraryController(model);
    }
}