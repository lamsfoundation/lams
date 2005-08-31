import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.authoring.tk.*;
import org.lamsfoundation.lams.common.util.*;
import mx.managers.*;

/*
* Toolit (formally Library) is where all the available tools are displayed.  
* Seen on the left hand side of the app.
*/
class Toolkit {
	// Model
	private var toolkitModel:ToolkitModel;
	// View
	private var toolkitView:ToolkitView;
	private var toolkitView_mc:MovieClip;
	
	private var _className:String = "Toolkit";
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	/*
	* Toolkit Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	function Toolkit(target_mc:MovieClip,depth:Number,x:Number,y:Number){
        mx.events.EventDispatcher.initialize(this);
        
		//Create the model
		toolkitModel = new ToolkitModel();
		
		//Create the view
		toolkitView_mc = target_mc.createChildAtDepth("toolkitView",DepthManager.kTop);		
        toolkitView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
        //Cast toolkit view clip as ToolkitView and initialise passing in model
		toolkitView = ToolkitView(toolkitView_mc);
		toolkitView.init(toolkitModel,undefined);

        //Register view with model to receive update events
		toolkitModel.addObserver(toolkitView);

        //Set the position by setting the model which will call update on the view
        toolkitModel.setPosition(x,y);
        
        //Initialise size to the designed size
        toolkitModel.setSize(toolkitView_mc._width,toolkitView_mc._height);
        
		//go and get some toolkits
		getToolkitLibraries();
	}
    
    /**
    * Called by view when loaded
    */
    private function viewLoaded() {
        //dispatch load event
        Debugger.log('dispatching event',Debugger.GEN,'viewLoaded','Toolkit');
        dispatchEvent({type:'load',target:this});
    }
	
	
	/*
	* Gets the latest toolkit activities from the server.
	* sets up a call back via comms object
	* 
	*/
	public function getToolkitLibraries():Void{
		Debugger.log('Running',4,'getToolkitActivities','Toolkit');
		
		var callback:Function = Proxy.create(this,setToolkitLibraries);
		Application.getInstance().getComms().getRequest('http://dolly.uklams.net/lams/lams_authoring/all_library_details.xml',callback,true);
		//Application.getInstance().getComms().getRequest('http://dolly.uklams.net/lams/lams_authoring/liblist.xml',callback,true);
		//Application.getInstance().getComms().getRequest('authoring/author.do?method=getAllLearningLibraryDetails',callback);
	}
	
	public function setToolkitLibraries(toolkits:Array):Void{
		
		Debugger.log('Recieved toolkits array length:'+toolkits.length,4,'setToolkitActivities','Toolkit');
		//TODO: Validate if correct data?
		toolkitModel.setToolkitLibraries(toolkits);
	}
    
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		toolkitModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        toolkitModel.setPosition(x,y);
    }
	

	//Dimension accessor methods
	public function get width():Number{
		return toolkitModel.width;
	}
	
	public function get height():Number{
		return toolkitModel.height;
	}
	
	public function get x():Number{
		return toolkitModel.x;
	}
	
	public function get y():Number{
		return toolkitModel.y;
	}

    function get className():String { 
        return 'Toolkit';
    }
    
}
