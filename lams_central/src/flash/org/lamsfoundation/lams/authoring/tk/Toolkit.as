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
		//Application.getInstance().getComms().getRequest('flashxml/all_library_details.xml',callback,false);
		//Application.getInstance().getComms().getRequest('http://dolly.uklams.net/lams/lams_authoring/liblist.xml',callback,true);
		//TODO: sort out with the aussies what is going on with this structure
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getAllLearningLibraryDetails',callback);
	}

	
	/**
	 * Call back, get the response from getToolkitLibraries server call
	 * @usage   
	 * @param   toolkits 
	 * @return  
	 */
	public function setToolkitLibraries(toolkits:Array):Void{
		
		Debugger.log('Recieved toolkits array length:'+toolkits.length,4,'setToolkitActivities','Toolkit');
		//TODO: Validate if correct data?
		//go throught the recieved toolkits and make ToolActivities from them:
		for(var i=0; i< toolkits.length; i++){
			var toolkit:Object = toolkits[i];
			
			var classInstanceRefs = new Array();
			//loop through the template activities, there should only be one for LAMS 1.1
			//but coding a loop so we can use it later
			
			//this templateActivities array may contain other types, such as Parallel activities check with Fiona.
			
			for(var j=0; j<toolkit.templateActivities.length; j++){
				var ta:Object = toolkit.templateActivities[j];
				//TODO:ta.activityUIID ashould be null
				//TODO - prune out non conforming activityTypeIDs
				
				//switch constructors based on the activityTypeID.
				Debugger.log("in toolkit:"+toolkit.learningLibraryID+" Checking ACTIVITY_TYPE:"+ta.activityTypeID,4,'setToolkitActivities','Toolkit');
				switch(ta.activityTypeID){
					case Activity.TOOL_ACTIVITY_TYPE:
						var toolAct:ToolActivity = new ToolActivity(null);
						toolAct.populateFromDTO(ta);
						classInstanceRefs.push(toolAct);
						break;
					case Activity.GROUPING_ACTIVITY_TYPE:
						new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						toolkits.splice(i,1);
						break;
					case Activity.SYNCH_GATE_ACTIVITY_TYPE:
						new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						toolkits.splice(i,1);
						break;
					case Activity.SCHEDULE_GATE_ACTIVITY_TYPE:
						new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						toolkits.splice(i,1);
						break;
					case Activity.PERMISSION_GATE_ACTIVITY_TYPE:
						new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						toolkits.splice(i,1);
						break;
					case Activity.PARALLEL_ACTIVITY_TYPE:
						var cAct:ComplexActivity = new ComplexActivity(null);
						cAct.populateFromDTO(ta);
						classInstanceRefs.push(cAct);
						//new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						//toolkits.splice(i,1);
						break;
					case Activity.OPTIONS_ACTIVITY_TYPE:
						/*
						var optAct:ComplexActivity = new ComplexActivity(null);
						optAct.populateFromDTO(ta);
						classInstanceRefs.push(optAct);
						*/
						new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						toolkits.splice(i,1);
						break;
					case Activity.SEQUENCE_ACTIVITY_TYPE:
						new LFError("Not able to handle this ACTIVITY_TYPE:"+ta.activityTypeID+", functionality not implemented","setToolkitLibraries",this);
						toolkits.splice(i,1);
						break;
					
				}
				
				/*
				var toolAct:ToolActivity = new ToolActivity(null);
				toolAct.activityCategoryID = ta.activityCategoryID;
				//toolAct.activityID = ta.null;    --- i changed this cos i dont know why it was hardcoded to null?>
				toolAct.activityID = ta.activityID;
				toolAct.activityTypeID = ta.activityTypeID;
				toolAct.description = ta.description;
				toolAct.helpText = ta.helpText;
				//NOTE 09-11-05: this has changed from libraryActivityUiImage to libraryActivityUIImage
				toolAct.libraryActivityUIImage = ta.libraryActivityUIImage;
				toolAct.title = ta.title;
				toolAct.learningLibraryID = toolkit.learningLibraryID;
				//TODO get this field included in lib packet
				toolAct.groupingSupportType = Activity.GROUPING_SUPPORT_OPTIONAL;
				
				toolAct.authoringURL = ta.tool.authoringURL;
				toolAct.toolContentID = ta.tool.toolContentID;
				toolAct.toolID = ta.tool.toolID;
				toolAct.toolDisplayName = ta.tool.toolDisplayName;
				toolAct.supportsContribute = ta.tool.supportsContribute;
				toolAct.supportsDefineLater = ta.tool.supportsDefineLater;
				toolAct.supportsModeration = ta.tool.supportsModeration;
				toolAct.supportsRunOffline = ta.tool.supportsRunOffline;
				
				toolActivities.push(toolAct);
				*/
			}
			
			//put the instance ref array into the toolkit object
			toolkit.classInstanceRefs = classInstanceRefs;
			
			
		}
		
		
		//sets these in the toolkit model in a hashtable by lib id
		toolkitModel.setToolkitLibraries(toolkits);
		
		
		
	/**
	 * Returns the TemplateActivity as Toolactivity
	 * @usage   
	 * @return  ToolActivity
	 */
	 
	 /*
	function getAsToolActivity():ToolActivity{
		activityUIID:Number, 
							activityTypeID:Number, 
							activityCategoryID:Number, 
							learningLibraryID:Number,
							libraryActivityUIImage:String, 
							toolContentUIID:Number, 
							toolUIID:Number){
		
		
		var myToolAct = new ToolActivity(	_templateActivityData.activityUIID,
											_templateActivityData.activityCategoryID,
											_
		
		
		
		);
		
		
		
	}

*/

	}
	
	/**
	 * Retrieves the defaultContentID for a given learning libraryID and toolID.
	 * It is the content ID that was in the library packet when it arrived
	 * @usage   
	 * @param   lid - The learning library id
	 * @param   tid - The tool id
	 * @return  default content ID
	 */
	public function getDefaultContentID(lid:Number,tid:Number):Number{
		var ll:Object = toolkitModel.getLearningLibrary(lid);
		for (var i=0; i<ll.classInstanceRefs.length; i++){
			var ta:Activity = ll.classInstanceRefs[i];
			if(ta.activityTypeID == Activity.TOOL_ACTIVITY_TYPE){
				var toolAct = ToolActivity(ta);
				if(tid == toolAct.toolID){
					Debugger.log('DefaultContentID:'+toolAct.toolContentID,Debugger.GEN,'getDefaultContentID','Toolkit');
					return toolAct.toolContentID;
					
				}
			}
		}
		
		return null;
		
		
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
