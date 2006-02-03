import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.authoring.tk.*;import org.lamsfoundation.lams.common.util.*;//import org.springsoft.aslib.*;

/**
* Model for the Toolbar
*/
class ToolkitModel extends Observable {	private var _className:String = "ToolkitModel";
   
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;

	/**
	* View state data
	*/
	private var _currentlySelectedTemplateActivity:TemplateActivity;
	private var _lastSelectedTemplateActivity:TemplateActivity;
	private var _currentToolkitDescription:String;
		/**
	* Container for the Libraries. There maybe many template actvities per learning library. 
	* Currently (1.1) there is only ever one.
	*/
	private var _toolkitLearningLibraries:Hashtable;
	/**
	* Constructor.
	*/
	public function ToolkitModel(){		Debugger.log('Running',4,'Constructor','ToolkitModel');
		_toolkitLearningLibraries = new Hashtable();
	}
		/**
	* Sets the data for the toolkit (ToolkitLibraries). Called by the toolkit container.  Sends the update to the view. (LIBRARIES_UPDATED
	 * @usage   
	 * @param   toolkits array of toolkits, also contains the classInstanceRefs array.
	 * @return  
	 */
	public function setToolkitLibraries(tls:Array):Boolean{		//Debugger.log('_className:'+_className,4,'setToolkitLibraryActivities','ToolkitModel');				//_global.breakpoint();
		var updateOk:Boolean=false;
		//clear the old lot:
		_toolkitLearningLibraries.clear();
		//populate the hashtable.

		for(var i=0; i<tls.length;i++){			_toolkitLearningLibraries.put(tls[i].learningLibraryID,tls[i]);
		}
		Debugger.log('Added '+tls.length+' Libraries to _toolkitLearningLibraries',4,'setToolkitLibraryActivities','ToolkitModel');
		
		
		
		
		//SET A DIRTY FLAG
		setChanged();
		//notify the view there has been a change
		infoObj = {};
		infoObj.updateType = "LIBRARIES_UPDATED";
		//notify observer's this is "this" as if by magic
		notifyObservers(infoObj);
		
		Debugger.log('Finished, about to return updateOk',4,'setToolkitLibraryActivities','ToolkitModel');
		return updateOk;
	}
	/**
	* Gets toolkit data
	*/
	public function getToolkitLibraries():Hashtable{
		return _toolkitLearningLibraries;
	}
	
	/**
	* Sets currecntly selected templateactivity
	*/
	public function setSelectedTemplateActivity(templateActivity:TemplateActivity):Void{
		//Debugger.log('templateActivity:'+templateActivity,4,'setSelectedTemplateActivity','ToolkitModel');		
		
		//_global.breakpoint();		//set the sates
		_lastSelectedTemplateActivity = _currentlySelectedTemplateActivity;
		_currentlySelectedTemplateActivity = templateActivity;
		//for observer thang
		setChanged();
		//send an update
		infoObj = {};
		infoObj.updateType = "TEMPLATE_ACTIVITY_SELECTED";
		notifyObservers(infoObj);
	}
	
	/**
	* Gets currecntly selected templateactivity
	*/
	public function getSelectedTemplateActivity():TemplateActivity{
		return _currentlySelectedTemplateActivity;
	}
	
	/**
	* Gets last selected templateactivity
	*/
	public function getLastSelectedTemplateActivity():TemplateActivity{
		return _lastSelectedTemplateActivity;
	}
	
	/**
	* Sends update to view for TOOL_DESCRIPTION
	*/
	public function setToolkitDescription(desc:String):Void{
		_currentToolkitDescription  = desc;
		infoObj.updateType = "TOOL_DESCRIPTION";
		//notify observer's this is "this" as if by magic
		notifyObservers(infoObj);
	}
	
	public function getToolkitDescription():String{
		return _currentToolkitDescription;
	}
	
	/**
	 * Gets a learning Library using its ID
	 * @usage   
	 * @param   learningLibraryID 
	 * @return  
	 */
	public function getLearningLibrary(learningLibraryID:Number):Object{
		return _toolkitLearningLibraries.get(learningLibraryID);
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
    
    //Acessors for x + y coordinates
    public function get x():Number{
        return __x;
    }
    
    public function get y():Number{
        return __y;
    }

    //Acessors for x + y coordinates
    public function get width():Number{
        return __width;
    }
    
    public function get height():Number{
        return __height;
    }
	function get className():String{
        return 'ToolkitModel';
    }
	
    
}


