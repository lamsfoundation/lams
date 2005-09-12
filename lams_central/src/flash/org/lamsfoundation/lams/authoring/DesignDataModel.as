import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
/*
*
* @author      DC
* @version     0.1
* @comments    DesignDataModel stores a complete learning design
*/

class org.lamsfoundation.lams.authoring.DesignDataModel {
		//State / internal properties
	private var _currentMaxID:Number;	
	//LearningDesign Properties:
	
	private var _objectType:String;
	private var _learningDesignID:Number;
	private var _uiID:Number;
	private var _title:String;
	private var _description:String;
	private var _helpText:String;
	private var _version:Number;
	
	private var _userID:Number;
	private var _workspaceFolderID:Number;
	private var _createDateTime:Date;
	private var _readOnlyFlag:Boolean;
	private var _validDesignFlag:Boolean;
	
	private var _maxID:Number;
	private var _firstID:Number;
	
	private var _activities:Hashtable;
	private var _transitions:Hashtable;
	private var _groupings:Hashtable;
	
	
    
     
    //Constructor
    function DesignDataModel(){
        //initialise the hashtables
		_activities = new Hashtable("_activities");
		_transitions = new Hashtable("_transitions");
		
		
    }
	
	/**
	 * Validates the design data model
	 * @usage   
	 * @return  
	 */
	public function validate():Boolean{
		var success:Boolean= false;
		Debugger.log("******* FUNCTION NOT IMPLEMENTED",Debugger.CRITICAL,'validate','DesignDataModel');
		return success;
	}
	
	//Helper methods.
	
	/**
	* Adds a template activity to the model. 
	* 
	*/
	public function addActivity(activity:Activity):Boolean{
		var success = false;
		//create an Activity from the template actvity.
		Debugger.log('activity:'+activity.title,4,'addActivity','DesignDataModel');
		
		
		//validate the activity ??
		
		//validate if we can do it?
		
		//generate a new ID
		
		//add to DDM
		
		ObjectUtils.printObject(activity);
		_activities.put(activity.activityUIID, activity);
		
		
		//TODO: Better validation of the addition
		success = true;
		
		
		return success;
	}
	
	public function addTransition(transition:Transition):Boolean{
		
		Debugger.log('Transition from:'+transition.fromUIID+', to:'+transition.toUIID,4,'addActivity','DesignDataModel');
		_transitions.put(transition.uiID,transition);
		
		//TODO some validation would be nice
		return true;
	}
	
	
	/**
	* Clears the current design in the DDM. 
	* @returns success
	*/
	public function clearDesign():Boolean{
		var success:Boolean = false;
		
		//TODO:Validate if design is saved if not notify user
		success = true;
		_objectType = null;
		_learningDesignID = null;
		_title = null;
		_description = null;
		_helpText = null;
		_version = null;
	
		_userID = null;
		_workspaceFolderID = null;
		_createDateTime = null;
		_readOnlyFlag = null;
		_validDesignFlag = null;
		
		_maxID = null;
		_firstID = null;
		
		_activities = new Hashtable("_activities");
		_transitions = new Hashtable("_transitions");
		
		success = true;
		
		Debugger.log('Cleared design:'+success,Debugger.GEN,'clearDesign','DesignDataModel');
		return success;
	}	/**
	 * Sets a new design for the DDM.
	 * @usage   <DDM Instance>.setDesign(design:Object)
	 * @param   design 
	 * @return  success
	 */
	public function setDesign(design:Object):Boolean{
		var success:Boolean = false;
		//TODO:Validate if design is saved if not notify user
		success = true;
		//_global.breakpoint();
		Debugger.log('Printing the design revieced:...',Debugger.GEN,'setDesign','DesignDataModel');
		ObjectUtils.printObject(design);
		_objectType = design.objectType;
		_learningDesignID = design.learning_design_id;
		_title = design.title;
		_description = design.description;
		_helpText = design.help_text;
		_version = design.version;
	
		_userID = design.user_id;
		_workspaceFolderID = design.workspace_folder_id;
		_createDateTime = design.create_date_time;
		_readOnlyFlag = design.read_only_flag;
		_validDesignFlag = design.valid_design_flag;
		
		_maxID = design.max_id;
		_firstID = design.first_id;
		
		//set the activities in the hash table
		for(var i=0; i<design.activities.length;i++){
			//note if the design is being opened - then it must have ui_ids already
			Debugger.log('Adding activity ID:'+design.activities[i].activityUIID,Debugger.GEN,'setDesign','DesignDataModel');
			_activities.put(design.activities[i].activityUIID,design.activities[i]);
		}
		
		//set the activities in the hash table
		for(var i=0; i<design.transitions.length;i++){
			//note if the design is being opened - then it must have ui_ids already
			Debugger.log('Adding transition ID:'+design.transitions[i].transitionUIID,Debugger.GEN,'setDesign','DesignDataModel');
			_transitions.put(design.transitions[i].transitionUIID,design.transitions[i]);
		}
				
		return success;
	}
	
	public function toData():Object{
		var design = {};
		design.objectType = _objectType;
		design.learningDesignID = _learningDesignID;
		design.uiID = _uiID;
		design.title = _title;
		design.description = _description;
		design.helpText = _helpText;
		design.version = _version;
		design.userID = _userID;
		design.workspaceFolderID = objectType;
		design.createDateTime = _createDateTime;
		design.readOnlyFlag = _readOnlyFlag;
		design.validDesignFlag = _validDesignFlag;
		design.maxID = _maxID;
		design.firstID = _firstID;
		design.activities = _activities.values();
		design.transitions = _transitions.values();
		design.groupings = _groupings.values();
		return design;
	}
	
	/**
	 * Creates a new UI ID which is unique for this design.
	 * @usage   
	 * @return  newID
	 */
	public function newUIID():Number{
		//if this is the first time then initialise to 1
		if(_currentMaxID == null){
			_currentMaxID = 0;
		}
		//add one for a new ID
		_currentMaxID++;
		return _currentMaxID;
	}
	
	
	//helper methods
	public function getActivityByUIID(UIID:Number):Activity{
		
		var a:Activity = _activities.get(UIID);
		Debugger.log('Returning activity:'+a.activityUIID,Debugger.GEN,'getActivityByUIID','DesignDataModel');
		return a;
	}
	
	
	//Getters and setters for the properties
	public function get objectType():String{
		return _objectType;
	}
	
	public function set objectType(a:String):Void{
		_objectType = a;
	}
	
	public function get learningDesignID():Number{
		return _learningDesignID;
	}
	
	public function set learningDesignID(a:Number):Void{
		_learningDesignID = a;
	}
	
	public function get title():String{
		return _title;
	}
	
	public function set title(a:String):Void{
		_title = a;
	}
	
	public function get description():String{
		return _description;
	}
	
	public function set description(a:String):Void{
		_description = a;
	}
	
	public function get helpText():String{
		return _helpText;
	}
	
	public function set helpText(a:String):Void{
		_helpText = a;
	}	public function get version():Number{
		return _version;
	}
	
	public function set version(a:Number):Void{
		_version = a;
	}
	
	public function get userID():Number{
		return _userID;
	}
	
	public function set userID(a:Number):Void{
		_userID = a;
	}
	
	public function get workspaceFolderID():Number{
		return _workspaceFolderID;
	}
	
	public function set workspaceFolderID(a:Number):Void{
		_workspaceFolderID = a;
	}
	
	public function get createDateTime():Date{
		return _createDateTime;
	}
	
	public function set createDateTime(a:Date):Void{
		_createDateTime = a;
	}
	
	public function get readOnlyFlag():Boolean{
		return _readOnlyFlag;
	}
	
	public function set readOnlyFlag(a:Boolean):Void{
		_readOnlyFlag = a;
	}
	
	public function get validDesignFlag():Boolean{
		return _validDesignFlag;
	}
	
	public function set validDesignFlag(a:Boolean):Void{
		_validDesignFlag = a;
	}
	
	public function get maxID():Number{
		return _maxID;
	}
	
	public function set maxID(a:Number):Void{
		_maxID = a;
	}	public function get firstID():Number{
		return _firstID;
	}
	
	public function set firstID(a:Number):Void{
		_firstID = a;
	}	public function get activities():Hashtable{
		return _activities;
	}
	
	public function set activities(a:Hashtable):Void{
		_activities = a;
	}

	public function get transitions():Hashtable{
		return _transitions;
	}
	
	public function set transitions(a:Hashtable):Void{
		_transitions = a;
	}

	
	
	
	
}