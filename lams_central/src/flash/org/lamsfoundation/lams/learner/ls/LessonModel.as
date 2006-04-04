import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.util.Observable;


/*
* Model for the Lesson
*/
class LessonModel extends Observable {
	private var _className:String = "LessonModel";
   
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	
	private var _lesson:Lesson;
	
	// unique Lesson identifier
	private var _lessonID:Number;
	
	/**
	* View state data
	*/
	private var _lessonName:String;
	private var _lessonDescription:String;
	private var _lessonStateID:Number;
	private var _active:Boolean;
	/**
	* Constructor.
	*/
	public function LessonModel (lesson:Lesson){
		_lesson = lesson;
		_active = false;
	}
	
	/**
	 * Set Lesson's unique ID
	 * 
	 * @param   lessonID 
	 */
	
	public function setLessonID(lessonID:Number){
		_lessonID = lessonID;
	}
	
	/**
	 * Get Lesson's unique ID
	 *   
	 * @return  Lesson ID
	 */
	
	public function getLessonID():Number {
		return _lessonID;
	}
    
	/**
	 * Set the lesson's name
	 * 
	 * @param   lessonName 
	 */
	
	public function setLessonName(lessonName:String){
		_lessonName = lessonName;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "NAME";
		notifyObservers(infoObj);
	}
	
	/**
	 * Get the lesson's name
	 * 
	 * @return Lesson Name
	 */
	
	public function getLessonName():String {
		return _lessonName;
	}
	
	/**
	 * Set the lesson's description
	 *
	 * @param   lessonDescription  
	 */
	
	public function setLessonDescription(lessonDescription:String){
		_lessonDescription = lessonDescription;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "DESCRIPTION";
		notifyObservers(infoObj);
	}
	
	/**
	 * Get the lesson's description
	 * 
	 * @return  lesson description
	 */
	public function getLessonDescription():String {
		return _lessonDescription;
	}
	
	public function setLessonStateID(lessonStateID:Number) {
		_lessonStateID = lessonStateID;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "STATE";
		notifyObservers(infoObj);
	}
	
	public function setActive() {
		_active = true;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "STATUS";
		notifyObservers(infoObj);
	}
	
	public function setInactive() {
		_active = false;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "STATUS";
		notifyObservers(infoObj);
	}
	
	public function getStatus():Boolean {
		return _active;
	}
	
	/**
    * set the size on the model, this in turn will set a changed flag and notify observers (views)
    * @param width - Tookit width
    * @param height - Toolkit height
    */
    public function setSize(width:Number,height:Number) {
		__width = width;
		__height = height;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SIZE";
		notifyObservers(infoObj);
    }
    
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getSize():Object{
		var s:Object = {};
		s.w = __width;
		s.h = __height;
		return s;
	}  
    
	/**
    * sets the model x + y vars
	*/
	public function setPosition(x:Number,y:Number):Void{
        //Set state variables
		__x = x;
		__y = y;
        //Set flag for notify observers
		setChanged();
        
		//build and send update object
		infoObj = {};
		infoObj.updateType = "POSITION";
		notifyObservers(infoObj);
	}  

	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getPosition():Object{
		var p:Object = {};
		p.x = x;
		p.y = y;
		return p;
	}  
    
    //Accessors for x + y coordinates
    public function get x():Number{
        return __x;
    }
    
    public function get y():Number{
        return __y;
    }

    //Accessors for x + y coordinates
    public function get width():Number{
        return __width;
    }
    
    public function get height():Number{
        return __height;
    }
	function get className():String{
        return 'LessonModel';
    }
	
	**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getLesson():Lesson{
		return _lesson;
	}
	

}