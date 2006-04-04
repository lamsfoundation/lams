import org.lamsfoundation.lams.learner.lb.*
import org.lamsfoundation.lams.common.util*   
import mx.managers.*;
/**
* Library - LAMS Application
* @author   Mitchell Seaton
*/
class Library {
	// Model
	private var libraryModel:LibraryModel;
	// View
	private var libraryView:LibraryView;
	private var libraryView_mc:MovieClip;
	
	private var _className:String = "Library";
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/*
	* Library Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	function Library(target_mc:MovieClip,x:Number,y:Number,lessonID:Number){
		mx.events.EventDispatcher.initialize(this);
        
		//Create the model
		libraryModel = new LibraryModel(libraryID);
		
		//Create the view
		libraryView_mc = target_mc.createChildAtDepth("libraryView",DepthManager.kTop);	
		
		libraryView = LibraryView(libraryView_mc);
		libraryView.init(libraryModel,undefined);
       
        libraryView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//Register view with model to receive update events
		libraryModel.addObserver(libraryView);

        //Set the position by setting the model which will call update on the view
        libraryModel.setPosition(x,y);
		
	}
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Library');
		
		if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
	
	public function getActiveLessons():Void {
		Debugger.log('Running',Debugger.GEN,'getActiveLessons','Library');
		
		var callback:Function = Proxy.create(this, getLessonList);
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=getActiveLessons', callback, false);
	
		
	}
	
	private function getActiveLessons(Data:Object):Void {
		trace('received active lesson data back...');
		// get data and create Lesson obj's
		
		// go through list of DTO's and add to hash map
	}
	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		libraryModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        libraryModel.setPosition(x,y);
    }
	

	//Dimension accessor methods
	public function get width():Number{
		return libraryModel.width;
	}
	
	public function get height():Number{
		return libraryModel.height;
	}
	
	public function get x():Number{
		return libraryModel.x;
	}
	
	public function get y():Number{
		return libraryModel.y;
	}

    function get className():String { 
        return 'Library';
    }
    
}
	