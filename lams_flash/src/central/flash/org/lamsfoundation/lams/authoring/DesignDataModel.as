/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.authoring.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ui.Cursor;
import org.lamsfoundation.lams.authoring.br.BranchConnector;
import org.lamsfoundation.lams.common.*;
import mx.events.*
/*
*
* DesignDataModel stores all the data relating to the design
* 
* Note the hashtable of _activities might contain the following types:
* 		1) ToolActivities
* 		2) Grouping activities which reference a _groupings element
* 		3) Parallel activities which reference 2 or more other _activitiy elements
* 		4) Optional activities which reference 2 or more other _activitiy elements
* 		5) Gate activities
* 
* 
* @author      DC
* @version     0.1
*    
*/

class org.lamsfoundation.lams.authoring.DesignDataModel {
	
	public static var COPY_TYPE_ID_AUTHORING:Number = 1;
	public static var COPY_TYPE_ID_RUN:Number = 2;
	public static var COPY_TYPE_ID_PREVIEW:Number = 3;	
	//LearningDesign Properties:
	private var _objectType:String;
	private var _copyTypeID:Number;

	private var _learningDesignID:Number;
	private var _prevLearningDesignID:Number;	// used for backup retrieval of the learning Design ID
	private var _title:String;
	private var _description:String;
	private var _helpText:String;
	private var _version:String;
	private var _designVersion:Number;
	private var _userID:Number;
	private var _editOverrideUserID:Number;
	private var _editOverrideUserFullName:String;
	private var _duration:Number;
	private var _readOnly:Boolean;
	private var _editOverrideLock:Boolean
	private var _autoSaved:Boolean
	private var _saveMode:Number;
	private var _validDesign:Boolean;	
	private var _modified:Boolean;
	private var _maxID:Number;
	private var _firstActivityID:Number;
	private var _firstActivityUIID:Number;
	
	//James has asked for previous and next fields so I can build up a sequence map of LDs when browsing workspaces...
	private var _parentLearningDesignID:Number;
	private var _activities:Hashtable;
	private var _transitions:Hashtable;
	private var _groupings:Hashtable;
	private var _branches:Hashtable;
	private var _branchMappings:Hashtable;
	private var _outputConditions:Hashtable;
	
	
	private var _licenseID:Number;
	private var _licenseText:String;
	private var _workspaceFolderID:Number;
	private var _createDateTime:Date;
	private var _lastModifiedDateTime:Date;
	private var _dateReadOnly:Date;
	
	private var _contentFolderID:String;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
	public var addEventListener:Function;
    public var removeEventListener:Function;
	
    //Constructor
    function DesignDataModel(){
        //initialise the hashtables
		_activities = new Hashtable("_activities");
		_transitions = new Hashtable("_transitions");
		_groupings = new Hashtable("_groupings");
		_branches = new Hashtable("_branches");
		_branchMappings = new Hashtable("_branchMappings");
		_outputConditions = new Hashtable("_outputConditions");
		
		//set the defualts:
		_objectType = "LearningDesign";
		_copyTypeID = COPY_TYPE_ID_AUTHORING;
		_version = null;
		_readOnly = false;
		_editOverrideLock = false;
		_validDesign = false;
		_autoSaved = false;
		
		_userID = Config.getInstance().userID;
		
		EventDispatcher.initialize(this);
		
		//dispatch an event now the design  has changed
		dispatchEvent({type:'ddmUpdate',target:this});
		
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
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////   UPDATE METHODS    ///////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	/**
	* Adds a template activity to the model. 
	* 
	*/
	public function addActivity(activity:Activity):Object{
		var success = false;
		//create an Activity from the template actvity.
		Debugger.log('activity:'+activity.title+', UIID:'+activity.activityUIID,4,'addActivity','DesignDataModel');
		
		//validate the activity ?		
		//validate if we can do it?
		//dispatch an event before the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
		_activities.put(activity.activityUIID, activity);
		//pull it out to check it
		var tmp:Activity = _activities.get(activity.activityUIID)
		
		if(tmp){
			Debugger.log('Succesfully added:'+tmp.title+':'+tmp.activityUIID,4,'addActivity','DesignDataModel');
		}else{
			return new LFError("Adding activity failed","addActivity",this,'activityUIID:'+activity.activityUIID);
		}
		//dispatch an event now the design has changed
		dispatchEvent({type:'ddmUpdate',target:this});
		
		
		
	}
	
	/**
	 * Removes the activity from the DDM
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	public function removeActivity(activityUIID:Number):Object{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
	
		
		var r:Object = _activities.remove(activityUIID);
		if(r==null){
			return new LFError("Removing activity failed:"+activityUIID,"removeActivity",this,null);
		}else{
			Debugger.log('Removed:'+r.activityUIID,Debugger.GEN,'removeActivity','DesignDataModel');
				dispatchEvent({type:'ddmUpdate',target:this});
			
			
		}
	}
	
	/**
	 * Adds a transition to the DDM
	 * @usage   
	 * @param   transition 
	 * @return  
	 */
	public function addTransition(transition:Transition):Boolean{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
	
		Debugger.log('Transition from:'+transition.fromUIID+', to:'+transition.toUIID,4,'addTransition','DesignDataModel');
		_transitions.put(transition.transitionUIID,transition);
		dispatchEvent({type:'ddmUpdate',target:this});
		
		//TODO some validation would be nice
		return true;
	}
	
	/**
	 * Removes the transition from the DDM
	 * @usage   
	 * @param   transitionUIID 
	 * @return  
	 */
	public function removeTransition(transitionUIID):Object{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
		
		var r:Object = _transitions.remove(transitionUIID);
		if(r==null){
			return new LFError("Removing transition failed:"+transitionUIID,"removeTransition",this,null);
		}else{
			Debugger.log('Removed:'+r.transitionUIID,Debugger.GEN,'removeTransition','DesignDataModel');
			dispatchEvent({type:'ddmUpdate',target:this});
		}
	}
	
	/**
	 * Removes the transitions from the DDM which are connected to the Activity 
	 *  
	 * @param   connectUIID connected Activity UIID
	 */
	
	public function removeTransitionByConnection(connectUIID):Void{
		var keyArray:Array = _transitions.keys();
		for(var i=0; i<keyArray.length; i++){
			var transition:Transition = Transition(_transitions.get(keyArray[i]));
			if(transition.fromUIID == connectUIID || transition.toUIID == connectUIID){
				removeTransition(transition.transitionUIID);
			}
		}
	}
	
	/**
	 * Adds a branch to the DDM
	 * @usage   
	 * @param   branch 
	 * @return  
	 */
	public function addBranch(branch:Branch):Boolean{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
	
		Debugger.log('Branch from:'+branch.fromUIID+', to:'+branch.toUIID,4,'addBranch','DesignDataModel');
		_branches.put(branch.branchUIID,branch);
		dispatchEvent({type:'ddmUpdate',target:this});
		
		//TODO some validation would be nice
		return true;
	}
	
	/**
	 * Adds a branch/group mapping to the DDM
	 * @usage   
	 * @param   branch 
	 * @return  
	 */
	public function addBranchMapping(entry):Boolean{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
	
		Debugger.log('Branch Mapping entry:' + entry.entryUIID,4,'addBranchMapping','DesignDataModel');
		_branchMappings.put(entry.entryUIID, entry);
		
		dispatchEvent({type:'ddmUpdate',target:this});
		
		return true;
	}
	
	public function addGrouping(grp:Grouping):Object{
		//dispatch an event to show the design is going to change
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
		Debugger.log('groupingUIID:'+grp.groupingUIID,Debugger.GEN,'addGrouping','DesignDataModel');
		var r = _groupings.put(grp.groupingUIID,grp);
		/*
		if(r){
			return r;
		}else{
			return new LFError("Adding grouping to hashtable failed","addGrouping",this,'groupingUIID:'+grp.groupingUIID);
		}
		*/
		dispatchEvent({type:'ddmUpdate',target:this});
		
		return true;
	}
	
	public function addOutputCondition(condition:ToolOutputCondition):Boolean {
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
		
		_outputConditions.put(condition.conditionUIID, condition);
		
		dispatchEvent({type:'ddmUpdate',target:this});
		
		return true;
	}
	
	/**
	 * Removes the branch from the DDM
	 * @usage   
	 * @param   branchUIID 
	 * @return  
	 */
	public function removeBranch(branchUIID):Object{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
		
		var r:Object = _branches.remove(branchUIID);
		if(r==null){
			return new LFError("Removing branch failed:"+branchUIID,"removeBranch",this,null);
		}else{
			
			Debugger.log('Removed:'+r.branchUIID,Debugger.GEN,'removeBranch','DesignDataModel');
			dispatchEvent({type:'ddmUpdate',target:this});
		}
	}
	
	/**
	 * Removes the branch from the DDM which are connected to the Activity 
	 *  
	 * @param   connectUIID connected Activity UIID (SequenceActivity)
	 */
	public function removeBranchByConnection(connectUIID):Void{
		var keyArray:Array = _branches.keys();
		for(var i=0; i<keyArray.length; i++){
			var branch:Branch = Branch(_branches.get(keyArray[i]));
			if(branch.fromUIID == connectUIID || branch.toUIID == connectUIID){
				removeBranch(branch.branchUIID);
			}
		}
	}
	
	/**
	 * Removes the branch from the DDM
	 * @usage   
	 * @param   branchUIID 
	 * @return  
	 */
	public function removeBranchMapping(entryUIID):Object{
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate', target:this});
		
		var r:Object = _branchMappings.remove(entryUIID);
		if(r==null){
			return new LFError("Removing branch mapping failed:"+entryUIID,"removeBranch",this,null);
		}else{
			
			Debugger.log('Removed:'+r.entryUIID,Debugger.GEN,'removeBranch','DesignDataModel');
			dispatchEvent({type:'ddmUpdate',target:this});
		}
	}
	
	public function removeOutputCondition(conditionUIID):Object{
	
		//dispatch an event to show the design has changed
		dispatchEvent({type:'ddmBeforeUpdate',target:this});
		
		var r:Object = _outputConditions.remove(conditionUIID);
		if(r==null){
			return new LFError("Removing tool output condition failed:"+conditionUIID,"removeOutputCondition",this,null);
		}else{
			
			Debugger.log('Removed:'+r.conditionUIID,Debugger.GEN,'removeOutputCondition','DesignDataModel');
			
			dispatchEvent({type:'ddmUpdate',target:this});
		}
	
	}
	
	/**
	 * Sets a new design for the DDM.
	 * note the design must be empty to call this, use clearCanvas(true)
	 * @usage   <DDM Instance>.setDesign(design:Object)
	 * @param   design 
	 * @return  success
	 */
	public function setDesign(design:Object):Boolean{
		//note the design must be empty to call this
		//note: Dont fire the update event as we dont want to store this change in an undo!
		//TODO: Validate that the design is clear
		var success:Boolean = false;
		
		//TODO:Validate if design is saved if not notify user
		success = true;
		
		Debugger.log('Setting design ID:'+design.learningDesignID,Debugger.GEN,'setDesign','DesignDataModel');
		Debugger.log('Printing the design revieced:...\n'+ObjectUtils.toString(design),Debugger.VERBOSE,'setDesign','DesignDataModel');
		
		_learningDesignID = design.learningDesignID;
		_title = design.title;
		_description = design.description;
		_helpText = design.helpText;
		_version = design.version;
		_designVersion = design.designVersion;
	
		_userID = design.userID;
		_editOverrideUserID = design.editOverrideUserID;
		_editOverrideUserFullName = design.editOverrideUserFullName;
		_workspaceFolderID = design.workspaceFolderID;
		_createDateTime = design.createDateTime;
		_readOnly = design.readOnly;
		_editOverrideLock = design.editOverrideLock;
		_validDesign = design.validDesign;
		
		_maxID = design.maxID;
		_firstActivityID = design.firstActivityUIID;
		_copyTypeID = design.copyTypeID;
		
		_licenseID = design.licenseID;
		_licenseText = design.licenseText;
			
		_contentFolderID = design.contentFolderID;
		
		//set the activities in the hash table
		for(var i=0; i<design.activities.length;i++){
			//note if the design is being opened - then it must have ui_ids already
			var dto = design.activities[i];
			
			//change to using if _activityTypeID == Activity.TOOL_ACTIVITY_TYPE
			//depending on the objectType call the relevent constructor.
			Debugger.log('Adding activity dto.activityTypeID:'+dto.activityTypeID,Debugger.GEN,'setDesign','DesignDataModel');	
			
			if(dto.activityTypeID == Activity.TOOL_ACTIVITY_TYPE){
			
				var newToolActivity:ToolActivity = new ToolActivity(dto.activityUIID);
				newToolActivity.populateFromDTO(dto);				
				
				_activities.put(newToolActivity.activityUIID,newToolActivity);
				
			} else if(dto.activityTypeID == Activity.SYNCH_GATE_ACTIVITY_TYPE || 
			   dto.activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE || 
			   dto.activityTypeID == Activity.PERMISSION_GATE_ACTIVITY_TYPE ||
			   dto.activityTypeID == Activity.SYSTEM_GATE_ACTIVITY_TYPE ) {
				
				var newGateActivity:GateActivity = new GateActivity(dto.activityUIID);
				newGateActivity.populateFromDTO(dto);				
				
				_activities.put(newGateActivity.activityUIID,newGateActivity);
			
			} else if(dto.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || dto.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
				
				//TODO: Test this!
				var cAct:ComplexActivity= new ComplexActivity(dto.activityUIID);
				cAct.populateFromDTO(dto);				
				
				_activities.put(cAct.activityUIID,cAct);
					
			} else if(dto.activityTypeID == Activity.GROUPING_ACTIVITY_TYPE){
				
				//TODO: Test this code when we are able to save and then open a design with grouping
				var newGroupActivity:GroupingActivity = new GroupingActivity(dto.activityUIID);
				newGroupActivity.populateFromDTO(dto);
				
				_activities.put(newGroupActivity.activityUIID,newGroupActivity);
			
			} else if(dto.activityTypeID == Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE || 
					  dto.activityTypeID == Activity.GROUP_BRANCHING_ACTIVITY_TYPE ||
					  dto.activityTypeID == Activity.TOOL_BRANCHING_ACTIVITY_TYPE){
				
				var newBranchActivity:BranchingActivity = new BranchingActivity(dto.activityUIID);
				newBranchActivity.populateFromDTO(dto);
				
				_activities.put(newBranchActivity.activityUIID,newBranchActivity);
			} else if(dto.activityTypeID = Activity.SEQUENCE_ACTIVITY_TYPE){
				var newSequenceActivity:SequenceActivity = new SequenceActivity(dto.activityUIID);
				newSequenceActivity.populateFromDTO(dto);
				
				_activities.put(newSequenceActivity.activityUIID, newSequenceActivity);
	
			}
		}
		
		//set the transitions in the hashtable
		for(var i=0; i<design.transitions.length;i++){
			//note if the design is being opened - then it must have ui_ids already
			Debugger.log('Adding transition ID:'+design.transitions[i].transitionUIID,Debugger.GEN,'setDesign','DesignDataModel');
			
			var tdto = design.transitions[i];
			var newTransition:Transition = new Transition(tdto.transitionUIID,
											tdto.fromUIID,
											tdto.toUIID,
											tdto.learningDesignID);
			newTransition.transitionID = tdto.transitionID;
			_transitions.put(newTransition.transitionUIID,newTransition);
		}
		
		//set the groupings in the hashtable
		for(var i=0; i<design.groupings.length;i++){
			//TODO: Test this when the server can save and open a design with groupings.
			Debugger.log('Adding grouping UIID:'+design.groupings[i].groupingUIID,Debugger.GEN,'setDesign','DesignDataModel');
			var gdto = design.groupings[i];
			var newGrouping:Grouping = new Grouping(gdto.groupingUIID);
			newGrouping.populateFromDTO(gdto);
			_groupings.put(newGrouping.groupingUIID,newGrouping);
		
		}
		
		for(var i=0; i<design.branchMappings.length;i++){
			var mdto = design.branchMappings[i];
			var newMappingEntry:BranchActivityEntry;
			if(mdto.groupUIID != null) {
				newMappingEntry = new GroupBranchActivityEntry(mdto.entryID, mdto.entryUIID, getGroupByUIID(mdto.groupUIID), SequenceActivity(getActivityByUIID(mdto.sequenceActivityUIID)), BranchingActivity(getActivityByUIID(mdto.branchingActivityUIID)));
			} else if(mdto.condition != null) {
				var condition:ToolOutputCondition = new ToolOutputCondition();
				condition.populateFromDTO(mdto.condition);
				condition.toolActivity = ToolActivity(getActivityByUIID(mdto.condition.toolActivityUIID));
				
				newMappingEntry = new ToolOutputBranchActivityEntry(mdto.entryID, mdto.entryUIID, condition, SequenceActivity(getActivityByUIID(mdto.sequenceActivityUIID)), BranchingActivity(getActivityByUIID(mdto.branchingActivityUIID)));
			}
			
			if(newMappingEntry != null)
				_branchMappings.put(newMappingEntry.entryUIID, newMappingEntry);
		}
		
		return success;
	}
	
	
	/**
	 * Readies the design to be saved, sets any date specific variables to now()
	 * Validates the design, sets the first ID etc..
	 * @usage   
	 * @return  
	 */
	private function prepareDesignForSaving():Void{
		
		//set create date time to now
		_createDateTime = (_editOverrideLock) ? _createDateTime : new Date();
		_lastModifiedDateTime = (_editOverrideLock) ? new Date() : _lastModifiedDateTime;

		if(_learningDesignID == null){
			_learningDesignID = Config.NUMERIC_NULL_VALUE;
		}
	}
	
	private function prepareDesignForAutoSaving():Void{
		
		//set modified date time to now
		_lastModifiedDateTime = new Date();
		_autoSaved = true;

	}
	
	/**
	 * Calls prepare deign and then returns a DTO object ready to be saved to the server
	 * @usage   
	 * @return  
	 */
	public function getDesignForSaving():Object{
		prepareDesignForSaving();
		return toData();
	}
	
	public function getDesignForAutoSave():Object{
		prepareDesignForAutoSaving();
		return toData();
	}
	
	/**
	 * Returns a data object to send in a WDDX packet to the initialise a Lesson for a Normal Session
	 * Static call because DDM doesn't exist in the Wizard model
	 * 
	 * @param   title            
	 * @param   description      
	 * @param   designID         
	 * @param   orgID            
	 * @param   lExportPortfolio 
	 * 
	 * @return  data obj
	 */
	
	public static function getDataForInitializing(title:String, description:String, designID:Number, orgID:Number, lExportPortfolio:Boolean):Object{
		var data:Object = new Object();
	
		if(title) { data.lessonName = title; }
		if(description) { data.lessonDescription = description; }
		
		if(designID) {data.learningDesignID = designID; }
		if(orgID) { data.organisationID = orgID; } 
		else { data.organisationID = Config.NUMERIC_NULL_VALUE; }
		
		if(lExportPortfolio != null) { data.learnerExportPortfolio = lExportPortfolio }
		else { data.learnerExportPortfolio = false }
		
		data.copyType = COPY_TYPE_ID_RUN;
		
		return data;
	}
	
	/**
	 * Returns a data object to send in a WDDX packet to the initialise a Lesson for a Preview Session
	 * DDM exists in 
	 * 
	 * @param   title       
	 * @param   description
	 * 
	 * @return  data obj
	 */
	
	public function getDataForPreview(title:String, description:String):Object{
		var data:Object = new Object();
	
		if(title) { data.lessonName = title; }
		if(description) { data.lessonDescription = description; }
		
		if(_learningDesignID) { data.learningDesignID = _learningDesignID; }
		data.organisationID = Config.NUMERIC_NULL_VALUE;
		
		data.learnerExportPortfolio = false;
		data.copyType = COPY_TYPE_ID_PREVIEW;
		
		return data;
	}

	/**
	 * Get details of currently saved design for use in workspace 
	 *  
	 * @return Design details used in Workspace
	 */
	
	public function getDesignForWorkspace():Object{
		var design:Object = new Object();
			if(_title){				design.title			= _title;				}
			if(_description){		design.description		= _description;			}
			if(_licenseID){			design.licenseID		= _licenseID;			}
			if(_licenseText){		design.licenseText		= _licenseText;			}
			if(_workspaceFolderID){	design.workspaceFolderID= _workspaceFolderID;	}
			design.readOnly = (_readOnly==null) ? false : _readOnly;
			return design;
	}
	
	/**
	 * Returns a DTO of the design
	 * @usage   
	 * @return  
	 */
	public function toData():Object{
		var design:Object = new Object();
		//if null, use default
		
		//TODO: get this sorted - query string
		Debugger.log('1 UserID:'+_userID,Debugger.GEN,'toData','DesignDataModel');
		
		
		if(_copyTypeID == undefined){
			_copyTypeID = COPY_TYPE_ID_AUTHORING;
		}
		
		/*
		* //09-11-05: converted to not including nulls in the packet
		design.objectType = (_objectType) ? _objectType : Config.STRING_NULL_VALUE;
		design.copyTypeID = (_copyTypeID) ? _copyTypeID : Config.NUMERIC_NULL_VALUE;
		design.learningDesignID = (_learningDesignID) ? _learningDesignID : Config.NUMERIC_NULL_VALUE;
		design.title = _title ? _title : Config.STRING_NULL_VALUE;
		design.description = (_description) ? _description : Config.STRING_NULL_VALUE;
		design.helpText = (_helpText) ? _helpText : Config.STRING_NULL_VALUE;
		design.version = (_version) ? _version : Config.STRING_NULL_VALUE;
		design.userID = (_userID) ? _userID : Config.NUMERIC_NULL_VALUE;
		design.duration = (_duration) ? _duration: Config.NUMERIC_NULL_VALUE;
		design.readOnly = (_readOnly!=null) ? _readOnly : Config.BOOLEAN_NULL_VALUE;
		design.validDesign = (_validDesign!=null) ? _validDesign : Config.BOOLEAN_NULL_VALUE;
		design.maxID = (_maxID) ? _maxID : Config.NUMERIC_NULL_VALUE;
		design.firstActivityID = (_firstActivityID) ? _firstActivityID : Config.NUMERIC_NULL_VALUE;
		design.firstActivityUIID = (_firstActivityUIID) ? _firstActivityUIID : Config.NUMERIC_NULL_VALUE;
		design.parentLearningDesignID= (_parentLearningDesignID) ? _parentLearningDesignID: Config.NUMERIC_NULL_VALUE;
		design.licenseID= (_licenseID) ? _licenseID: Config.NUMERIC_NULL_VALUE;
		design.licenseText= (_licenseText) ? _licenseText: Config.STRING_NULL_VALUE;
		design.workspaceFolderID = (_workspaceFolderID) ? _workspaceFolderID : Config.NUMERIC_NULL_VALUE;
		design.createDateTime = (_createDateTime) ? _createDateTime : Config.DATE_NULL_VALUE;
		design.lastModifiedDateTime= (_lastModifiedDateTime) ? _lastModifiedDateTime: Config.DATE_NULL_VALUE;
		design.dateReadOnly = (_dateReadOnly) ? _dateReadOnly: Config.DATE_NULL_VALUE;
		*/
		
		//if the value is null, it is not included in the DTO
		if(_copyTypeID == COPY_TYPE_ID_RUN && !_editOverrideLock){
			design.copyTypeID = COPY_TYPE_ID_AUTHORING;
		}else if(_copyTypeID){	design.copyTypeID 	= _copyTypeID;		}
		
		if(_learningDesignID){	design.learningDesignID	= _learningDesignID;	}
		if(_title){				design.title			= _title;				}
		if(_description){		design.description		= _description;			}
		if(_helpText){			design.helpText			= _helpText;			}
		if(_version){			design.version			= _version;				}
		if(_designVersion){		design.designVersion 	= _designVersion;		}
		if(_userID){			design.userID			= _userID;				}
		if(_editOverrideUserID){	design.editOverrideUserID = _editOverrideUserID;	}
		if(_duration){			design.duration			= _duration;			}
		//readOnly must be in the DTO, so if its null, then give a false
		
		if(_copyTypeID == COPY_TYPE_ID_RUN && !_editOverrideLock){
			design.readOnly = false;
		} else {
			design.readOnly = (_readOnly==null) ? false : _readOnly;
		}
		
		if(_editOverrideLock){		design.editOverrideLock = _editOverrideLock;	}
		else {
			_editOverrideLock = false;
		}
		
		//valid design must be in the DTO, so if its null, then give a false
		design.validDesign = (_validDesign==null) ? false : _validDesign;
		
		design.saveMode =  (_saveMode!=null) ? _saveMode : Config.NUMERIC_NULL_VALUE;
		
		
		if(_maxID){				design.maxID			= _maxID;				}
		if(_firstActivityID){	design.firstActivityID	= _firstActivityID;		}
		if(_firstActivityUIID){	design.firstActivityUIID= _firstActivityID;		}
		if(_parentLearningDesignID){design.parentLearningDesignID= _parentLearningDesignID; 	}
		if(_licenseID){			design.licenseID		= _licenseID;			}
		if(_licenseText){		design.licenseText		= _licenseText;			}
		if(_workspaceFolderID){	design.workspaceFolderID= _workspaceFolderID;	}
		if(_createDateTime){	design.createDateTime 	= _createDateTime;		}
		if(_lastModifiedDateTime){ design.lastModifiedDateTime = _lastModifiedDateTime;		}
		if(_dateReadOnly){		design.dateReadOnly		= _dateReadOnly;		}
		if(_contentFolderID){	design.contentFolderID 	= _contentFolderID;		}
		
		if(_autoSaved) { 		design.autoSaved 		= _autoSaved; 			}
		
		design.activities = new Array();
		var classActs:Array = _activities.values();
		if(classActs.length > 0){
			
			for(var i=0; i<classActs.length; i++){
				var classAct = classActs[i].toData();
				if(classAct != null)
					design.activities.push(classAct);
			}
		}
		
		design.transitions = new Array();
		var classTrans:Array = _transitions.values();
		
		if(classTrans.length > 0)
			for(var i=0; i<classTrans.length; i++)
				design.transitions[i] = classTrans[i].toData();
		
		design.groupings = new Array();
		var classGroups:Array = _groupings.values();
		
		if(classGroups.length > 0)
			for(var i=0; i<classGroups.length; i++)
				design.groupings[i] = classGroups[i].toData(this);
				
				
		design.branchMappings = new Array();
		var classMappingEntries = _branchMappings.values();
		
		if(classMappingEntries.length > 0) {
			for(var i=0; i<classMappingEntries.length; i++) {
				if((classMappingEntries[i].branchingActivity.activityTypeID == Activity.GROUP_BRANCHING_ACTIVITY_TYPE 
						&& classMappingEntries[i].group != null) ||
					(classMappingEntries[i].branchingActivity.activityTypeID == Activity.TOOL_BRANCHING_ACTIVITY_TYPE 
						&& classMappingEntries[i].condition != null 
						&& classMappingEntries[i].condition.toolActivity.activityUIID == classMappingEntries[i].branchingActivity.toolActivityUIID)) {
					design.branchMappings.push(classMappingEntries[i].toData());
				}
			}
		}
		
		return design;
	}
	
	/**
	 * Creates a new UI ID which is unique for this design.
	 * @usage   
	 * @return  newID
	 */
	public function newUIID():Number{
		//if this is the first time then initialise to 1
		if(_maxID == null){
			Debugger.log('MaxID was null, resetting',Debugger.GEN,'newUIID','DesignDataModel');
			_maxID = 0;
		}
		
		//add one for a new ID
		_maxID++;
		Debugger.log('New maxID:'+_maxID,Debugger.GEN,'newUIID','DesignDataModel');
		
		return _maxID;
	}
	
	
	//helper methods
	/**
	 * Retreives a reference to an activity in the DDM using its UIID
	 * @usage   
	 * @param   UIID 
	 * @return  
	 */
	public function getActivityByUIID(UIID:Number):Activity{
		
		var a:Activity = _activities.get(UIID);
		Debugger.log('Returning activity:'+a.activityUIID,Debugger.GEN,'getActivityByUIID','DesignDataModel');
		
		return a;
	}
	
		/**
	 * Retreives a reference to a transition in the DDM using its UIID
	 * @usage   
	 * @param   UIID 
	 * @return  
	 */
	public function getTransitionByUIID(UIID:Number):Transition{
		
		var t:Transition = _transitions.get(UIID);
		Debugger.log('Returning transition:'+t.transitionUIID,Debugger.GEN,'getTransitionByUIID','DesignDataModel');
		return t;
	}
	
	/**
	 * Retreives a reference to a grouping in the DDM using its UIID
	 * @usage   
	 * @param   UIID 
	 * @return  
	 */
	public function getGroupingByUIID(UIID:Number):Grouping{
		_global.breakpoint();
		var g:Grouping = _groupings.get(UIID);
		Debugger.log('Returning grouping:'+ObjectUtils.toString(g)+' for uiid:'+UIID,Debugger.GEN,'getGroupingByUIID','DesignDataModel');
		return g;
	}
	
	/**
	 * Returns a grouping activity based on a groupingUIID 
	 * Used by the property inspector
	 * @usage   
	 * @param   UIID The UIID of the Grouping
	 * @return  
	 */
	public function getGroupingActivityByGroupingUIID(UIID:Number):GroupingActivity{
		//get all the grouping activities:
		var gActs = getGroupingActivities();
		//macthup the createGroupingUIID
		for(var i=0; i<gActs.length; i++){
			if(gActs[i].createGroupingUIID == UIID){
				Debugger.log('Returning grouping activity:'+gActs[i].activityUIID+' for grouping uiid:'+UIID,Debugger.GEN,'getGroupingByUIID','DesignDataModel');
				return gActs[i];
			}
		}
		Debugger.log('Did not find a grouping activity for grouping uiid:'+UIID,Debugger.GEN,'getGroupingActivityByGroupingUIID','DesignDataModel');
		
	}
	
	/**
	 * Returns an Array of all the Grouping Activites
	 * @usage   
	 * @return  
	 */
	public function getGroupingActivities():Array{
		var acts:Array = _activities.values();
		var gActs = new Array();
		for(var i=0; i<acts.length;i++){
			if(acts[i].activityTypeID == Activity.GROUPING_ACTIVITY_TYPE){
				gActs.push(acts[i]);
			}
		}
		
		Debugger.log('Returning '+gActs.length+' grouping activities',Debugger.GEN,'getGroupingActivities','DesignDataModel');
		return gActs;
	}
	
	public function getActivitiesByType(type:Number):Array{
		var acts:Array = _activities.values();
		var tActs = new Array();
		for(var i=0; i<acts.length;i++){
			if(acts[i].activityTypeID == type){
				tActs.push(acts[i]);
			}
		}
		
		return tActs;
	}
	
	public function getGroupByUIID(groupUIID:Number):Group {
		var groupings:Array = _groupings.values();
		
		for(var i=0; i<groupings.length; i++) {
			var group:Group = Group(groupings[i].getGroup(groupUIID));
			if(group != null) return group;
		}
		
		return null;
	}
	
	public function hasBranchMappingsForGroupingUIID(groupingUIID:Number):Boolean {
		var bMappings:Array = _branchMappings.values();
		for(var i=0; i<bMappings.length; i++) {
			if(bMappings[i].group.parentID == groupingUIID) return true;
		}
		
		return false;
	}
	
	public function clearBranchMappingsByGroupingUIID(groupingUIID:Number):Void {
		var bMappings:Array = _branchMappings.values();
		
		for(var i=0; i<bMappings.length; i++) {
			if(bMappings[i].group.parentID == groupingUIID) _branchMappings.remove(bMappings[i].entryUIID);
		}
	}
	
	public function removeBranchMappingsByGroupUIID(groupUIID:Number):Void {
		var bMappings:Array = _branchMappings.values();
		
		for(var i=0; i<bMappings.length; i++) {
			if(bMappings[i].group.groupUIID == groupUIID) _branchMappings.remove(bMappings[i].entryUIID);
		}
	}
	
	public function hasBranchMappingsForCondition(conditionUIID:Number):Boolean {
		var bMappings:Array = _branchMappings.values();
		for(var i=0; i<bMappings.length; i++) {
			if(bMappings[i].condition.conditionUIID == conditionUIID) return true;
		}
		
		return false;
	}
	
	public function hasBranchMappingsForConditionSet(dataProvider:Array):Boolean {
		for(var i=0; i<dataProvider.length; i++) {
			if(hasBranchMappingsForCondition(ToolOutputCondition(dataProvider[i].data).conditionUIID))
				return true;
		}
		
		return false;
	}
	
	public function removeBranchMappingsByCondition(conditionUIID:Number):Void {
		var bMappings:Array = _branchMappings.values();
		
		for(var i=0; i<bMappings.length; i++) {
			if((bMappings[i].condition.conditionUIID == conditionUIID) && (conditionUIID != null)) _branchMappings.remove(bMappings[i].entryUIID);
		}
	}
	
	public function getBranchMappingsByActivityUIIDAndType(activityUIID:Number):Object {
		var bMappings:Array = _branchMappings.values();
		var myMappings:Object = new Object();
		myMappings.groupBased = new Array();
		myMappings.toolBased = new Array();
		myMappings.uncategorised = new Array();
		
		for(var i=0; i<bMappings.length; i++) {
			if(bMappings[i] instanceof GroupBranchActivityEntry)	
					myMappings.groupBased.push(bMappings[i]);
			else if(bMappings[i] instanceof ToolOutputBranchActivityEntry)
				myMappings.toolBased.push(bMappings[i]);
			else 
				myMappings.uncategorised.push(bMappings[i]);
		}
		
		return myMappings;
	}
	
	/**
	 * Retrieves all children of a complexy activity
	 * E.g. child acts in an optional activity
	 * @usage   
	 * @param   activityUIID 
	 * @return  Array of refs to the child acts in the DDM
	 */
	public function getComplexActivityChildren(activityUIID):Array{
		Debugger.log('Looking for chiildren of UIID:'+activityUIID,Debugger.GEN,'getComplexActivityChildren','DesignDataModel');
		_global.breakpoint();
		var k:Array = _activities.keys();
		var children:Array = new Array();
		var count = 0;
		for(var i=0;i<k.length;i++){
			var a = _activities.get(k[i]);
			if(a.parentUIID == activityUIID){
				Debugger.log('Found a child! UIID:'+a.activityUIID,Debugger.GEN,'getComplexActivityChildren','DesignDataModel');
				count++;
				a.orderID = count;
				children.push(a);
			}
		}
		return children;
		
		
		
	}
	
	public function getTransitionsForActivityUIID(UIID):Object{
		var ts:Array = _transitions.values();
		var transObj = new Object();
		var myTransitions:Array = new Array();
		var into = null;
		var out = null;
		var hasTrans:Boolean = false;
		
		for(var i=0; i<ts.length;i++){
			if(ts[i].toUIID == UIID || ts[i].fromUIID == UIID){
				myTransitions.push(ts[i]);
				hasTrans = true;
			}
			if(into != null && out != null){
					break;
			}else{
				if(ts[i].fromUIID == UIID){
					out = ts[i];
				}
				if(ts[i].toUIID == UIID){
					into = ts[i];
				}
			}
		}
		
		transObj.myTransitions = myTransitions;
		transObj.out = out;
		transObj.into = into;
		transObj.hasTrans = hasTrans;
		return transObj;
	}
	
	public function getBranchesForActivityUIID(UIID):Object{
		var br:Array = _branches.values();
		var branchObj = new Object();
		var myBranches:Array = new Array();
		var hub = new Array();
		var target = null;
		var sequence = null;
		
		for(var i=0; i<br.length;i++){
			if(br[i].targetUIID == UIID || br[i].hubUIID == UIID || br[i].sequenceActivity.activityUIID == UIID){
				myBranches.push(br[i]);
			}
			if(target != null && hub != null){
					break;
			}else{
				if(br[i].targetUIID == UIID){
					target = br[i];
				}
				if(br[i].hubUIID == UIID){
					hub.push(br[i]);
				}
				
				if(br[i].sequenceActivity.activityUIID == UIID){
					sequence = br[i].sequenceActivity;
				}
			}
		}
		
		branchObj.myBranches = myBranches;
		branchObj.target = target;
		branchObj.sequence = sequence;
		branchObj.hub = hub;
		return branchObj;
	}
	
	public function getAllConditions():Array {
		return _outputConditions.values();
	}
	public function getAllConditionsForToolOutput(toolActivityUIID:Number):Array {
		var conditions:Array = getAllConditions();
		var rConditions:Array = new Array();
		
		for(var i=0; i<conditions.length; i++) {
			if(conditions[i].toolActivity.activityUIID = toolActivityUIID)
				rConditions.push(conditions[i]);
		}
		
		return rConditions;
	}
	
	/**
	 * Search through all Branching Activities and check if any unused or redundant mappings exist in the DDM
	 * @param remove  remove all instances if true
	 */
	public function hasRedundantBranchMappings(remove:Boolean):Boolean {
		var i;
		var v:Boolean = true;
		
		var gActs:Array = getActivitiesByType(Activity.GROUP_BRANCHING_ACTIVITY_TYPE);
		var tActs:Array = getActivitiesByType(Activity.TOOL_BRANCHING_ACTIVITY_TYPE);
		var cActs:Array = getActivitiesByType(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE);
		
		var mappingsToRemove:Array = new Array();
		
		for(i=0; i<gActs.length; i++) {
			var mappings:Object = getBranchMappingsByActivityUIIDAndType(gActs[i].activityUIID);
			if(mappings.toolBased.length > 0) {
				v = false;
				mappingsToRemove.concat(mappings.toolBased);
			}
			
			for(var j=0; j<mappings.groupBased.length; j++) {
				if(mappings.groupBased[j].group.parentID != GroupingActivity(gActs[i]).createGroupingUIID) {
					v = false;
					mappingsToRemove.push(mappings.groupBased[j]);
				}
			}
		}
		
		for(i=0; i<tActs.length; i++) {
			var mappings:Object = getBranchMappingsByActivityUIIDAndType(gActs[i].activityUIID);
			if(mappings.groupBased.length > 0) {
				v = false;
				mappingsToRemove.concat(mappings.groupBased);
			} 
			
			for(var j=0; j<mappings.toolBased.length; j++) {
				if(mappings.toolBased[j].condition.toolActivity.activityUIID != BranchingActivity(tActs[i]).toolActivityUIID) {
					v = false;
					mappingsToRemove.push(mappings.toolBased[j]);
				}
			}
		}
		
		for(i=0; i<cActs.length; i++) {
			var mappings:Object = getBranchMappingsByActivityUIIDAndType(cActs[i].activityUIID);
			
			for(var j=0; j<mappings.groupBased.length; j++) {
				mappingsToRemove.concat(mappings.groupBased[j]);
				v = false;
			}
			
			for(var j=0; j<mappings.toolBased.length; j++) {
				mappingsToRemove.concat(mappings.toolBased[j]);
				v = false;
			}
		}
		
		if(remove && !v) {
			for(var k=0; k<mappingsToRemove.length; k++) {
				Debugger.log("removing entry: " + mappingsToRemove[k].entryUIID, Debugger.CRITICAL, "hasRedundantBranchMappings", "DDM");
				removeBranchMapping(mappingsToRemove[k].entryUIID);
			}
		}
		
		return !v;
		
	}
	
	public function removeRedundantBranchMappings(returnToSave:Function):Void {
		Cursor.showCursor(ApplicationParent.C_HOURGLASS);
		hasRedundantBranchMappings(true);
		returnToSave();
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
	
	public function get prevLearningDesignID():Number{
		return _prevLearningDesignID;
	}
	
	public function set prevLearningDesignID(a:Number):Void{
		_prevLearningDesignID = a;
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
	}	public function get version():String{
		return _version;
	}
	
	public function set version(a:String):Void{
		_version = a;
	}
	
	public function get designVersion():Number{
		return _designVersion;
	}
	
	public function set designVersion(a:Number):Void{
		_designVersion = a;
	}
	
	public function get userID():Number{
		return _userID;
	}
	
	public function set userID(a:Number):Void{
		_userID = a;
	}
	
	public function get editOverrideUserID():Number{
		return _editOverrideUserID;
	}
	
	public function set editOverrideUserID(a:Number):Void{
		_editOverrideUserID = a;
	}
	
	
	public function get editOverrideUserFullName():String{
		return _editOverrideUserFullName;
	}
	
	public function set editOverrideUserFullName(a:String):Void{
		_editOverrideUserFullName = a;
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
	
	public function get readOnly():Boolean{
		return _readOnly;
	}
	
	public function set readOnly(a:Boolean):Void{
		_readOnly = a;
	}
	
	public function get editOverrideLock():Boolean{
		return _editOverrideLock;
	}
	
	public function set editOverrideLock(a:Boolean):Void{
		_editOverrideLock = a;
	}
	
	public function get validDesign():Boolean{
		return _validDesign;
	}
	
	public function set validDesign(a:Boolean):Void{
		_validDesign = a;
	}
	
	public function get maxID():Number{
		return _maxID;
	}
	
	public function set maxID(a:Number):Void{
		_maxID = a;
	}	public function get firstActivityID():Number{
		return _firstActivityID;
	}
	
	public function set firstActivityID(a:Number):Void{
		_firstActivityID = a;
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
	
	public function get branches():Hashtable{
		return _branches;
	}
	
	public function set branches(a:Hashtable):Void{
		_branches = a;
	}
	
	public function get branchMappings():Hashtable {
		return _branchMappings;
	}
	
	public function set branchMappings(a:Hashtable):Void {
		_branchMappings = a;
	}	
	
	public function set conditions(a:Hashtable):Void {
		_outputConditions = a;
	}
	
	public function get conditions():Hashtable {
		return _outputConditions;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newcopyTypeID 
	 * @return  
	 */
	public function set copyTypeID (newcopyTypeID:Number):Void {
		_copyTypeID = newcopyTypeID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get copyTypeID ():Number {
		return _copyTypeID;
	}
	
	
	/**
	 * 
	 * @usage   
	 * @param   newlastModifiedDateTime 
	 * @return  
	 */
	public function set lastModifiedDateTime (newlastModifiedDateTime:Date):Void {
		_lastModifiedDateTime = newlastModifiedDateTime;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get lastModifiedDateTime ():Date {
		return _lastModifiedDateTime;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newdateReadOnly 
	 * @return  
	 */
	public function set dateReadOnly (newdateReadOnly:Date):Void {
		_dateReadOnly = newdateReadOnly;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get dateReadOnly ():Date {
		return _dateReadOnly;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newduration 
	 * @return  
	 */
	public function set duration (newduration:Number):Void {
		_duration = newduration;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get duration ():Number {
		return _duration;
	}
	
	public function set modified(a:Boolean) {
		_modified = a;
	}
	
	public function get modified():Boolean {
		return _modified;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newfirstActivityUIID 
	 * @return  
	 */
	public function set firstActivityUIID (newfirstActivityUIID:Number):Void {
		_firstActivityUIID = newfirstActivityUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get firstActivityUIID ():Number {
		return _firstActivityUIID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newlicenseID 
	 * @return  
	 */
	public function set licenseID (newlicenseID:Number):Void {
		_licenseID = newlicenseID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get licenseID ():Number {
		return _licenseID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newlicenseText 
	 * @return  
	 */
	public function set licenseText (newlicenseText:String):Void {
		_licenseText = newlicenseText;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get licenseText ():String {
		return _licenseText;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newparentLearningDesignID 
	 * @return  
	 */
	public function set parentLearningDesignID (newparentLearningDesignID:Number):Void {
		_parentLearningDesignID = newparentLearningDesignID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get parentLearningDesignID ():Number {
		return _parentLearningDesignID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param  
	 * @return  
	 */
	public function set contentFolderID (newcontentFolderID:String):Void {
		_contentFolderID = newcontentFolderID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get contentFolderID ():String {
		return _contentFolderID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param 
	 * @return  
	 */
	public function set saveMode (newSaveMode:Number):Void {
		_saveMode = newSaveMode;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get saveMode ():Number {
		return _saveMode;
	}

	
}