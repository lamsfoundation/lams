import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.common.util.*;

import mx.managers.*;
/**
* Lesson - LAMS Application
* @author   Mitchell Seaton
*/
class Lesson {
	
	// Model
	private var lessonModel:LessonModel;
	// View
	private var lessonView:LessonView;
	private var lessonView_mc:MovieClip;
	
	private var _libraryView:LibraryView;
	private var _lesson:Lesson;
	
	private var _instance:Lesson;
	private var _className:String = "Lesson";
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/*
	* Lesson Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	function Lesson(target_mc:MovieClip,x:Number,y:Number,lessonID:Number, libraryView:LibraryView){
		mx.events.EventDispatcher.initialize(this);
        
		//Create the model
		lessonModel = new LessonModel(this);
		lessonModel.setLessonID(lessonID);
		
		//Create the view
		lessonView_mc = target_mc.createChildAtDepth("lessonView",DepthManager.kTop);	
		
		lessonView = LessonView(lessonView_mc);
		lessonView.init(lessonModel,undefined);
       
        lessonView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//Register view with model to receive update events
		lessonModel.addObserver(lessonView);

        //Set the position by setting the model which will call update on the view
        lessonModel.setPosition(x,y);
		
		_libraryView = LibraryView(libraryView);
	}
	
	/**
	* event broadcast when a new lesson is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Lesson');
		//lessonModel.setDefaultState();
		if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
     
	
	 
	public function joinLesson():Boolean {
		
		var callback:Function = Proxy.create(this,startLesson);
		
		// call action
		var lessonId:Number = lessonModel.getLessonID();
		var userId:Number = Application.getInstance().getUserID();
		
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=joinLession&userId='+String(userId)+'&lessonId='+String(lessonId), callback, false);
			
		return true;
	}
	
	public function exitLesson():Boolean {
		var callback:Function = Proxy.create(this,closeLesson);
		
		// call action
		var lessonId:Number = lessonModel.getLessonID();
		
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=exitLession&lessonId='+String(lessonId), callback, false);
		
		return true;
	}
	
	private function startLesson(pkt:Object){
		trace('received message back from server...');
		
		// set lesson as active
		//_lessonModel.setActive();
	}  
	
	private function closeLesson(pkt:Object){
		trace('receiving message back from server...');
		
		// set lesson as inactive
		//_lessonModel.setInactive();
	}
	
	public function getLessonID():Number {
		return lessonModel.getLessonID();
	}
	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		lessonModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        lessonModel.setPosition(x,y);
    }
	
	public function getLibrary():LibraryView {
		return _libraryView;
	}

	//Dimension accessor methods
	public function get width():Number{
		return lessonModel.width;
	}
	
	public function get height():Number{
		return lessonModel.height;
	}
	
	public function get x():Number{
		return lessonModel.x;
	}
	
	public function get y():Number{
		return lessonModel.y;
	}

    function get className():String { 
        return 'Lesson';
    }
    
}