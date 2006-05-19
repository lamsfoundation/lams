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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.wizard.*;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.ws.*;
import mx.managers.*
import mx.utils.*
import mx.events.*;

/*
* Model for the Monitoring Tabs 
*/
class WizardModel extends Observable{
	private var _className:String = "WizardModel";
   
	public var RT_FOLDER:String = "Folder";
	public var RT_ORG:String = "Organisation";
	public var RT_LD:String = "LearningDesign";
	   
	// constants
	private static var LEARNER_ROLE:String = "LEARNER";
	private static var STAFF_ROLE:String = "STAFF";
	private static var TEACHER_ROLE:String = "TEACHER";
	   
	private static var USER_LOAD_CHECK_INTERVAL:Number = 50;
	private static var USER_LOAD_CHECK_TIMEOUT_COUNT:Number = 200;
	  
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	
	// wizard main
	private var _wizard:Wizard;
		
		
	private var _org:Organisation;
	private var _lessonID:Number;
	
	// state data
	private var _staffLoaded:Boolean;
	private var _learnersLoaded:Boolean;
	private var _UserLoadCheckIntervalID:Number;         //Interval ID for periodic check on User Load status
	private var _userLoadCheckCount = 0;				// instance counter for number of times we have checked to see if users are loaded	
	
	//this is the dataprovider for the org tree
	private var _treeDP:XML;
	private var _orgResources:Array;	
	private var _orgs:Array;
	private var _selectedOrgTreeNode:XMLNode;
	private var _selectedLocTreeNode:XMLNode;

	private var _workspaceResultDTO:Object;
	private var _resultDTO:Object;
	private var _stepID:Number;

	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/**
	* Constructor.
	*/
	public function WizardModel (wizard:Wizard){
		_wizard = wizard;
		
		_orgResources = new Array();
		_staffLoaded = false;
		_learnersLoaded = false;
		
		_workspaceResultDTO = new Object();
		_resultDTO = new Object();
		_stepID = 1;
		
		mx.events.EventDispatcher.initialize(this);
	}
	
	// add get/set methods

	public function setOrganisation(org:Organisation){
		_org = org;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "ORGANISATION_UPDATED";
		notifyObservers(infoObj);
	}
	
	public function getOrganisation():Organisation{
		return _org;
	}
	
	public function saveOrgs(orgs:Array){
		_orgs = orgs;
	}
	
	public function getOrgs():Array{
		return _orgs;
	}

	
	public function broadcastViewUpdate(updateType, data){
		//getMonitor().getMV().clearView();
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = updateType;
		infoObj.data = data;
		notifyObservers(infoObj);
		
	}
	
	
	/**
	* Periodically checks if users have been loaded
	*/
	private function checkUsersLoaded() {
		// first time through set interval for method polling
		if(!_UserLoadCheckIntervalID) {
			_UserLoadCheckIntervalID = setInterval(Proxy.create(this, checkUsersLoaded), USER_LOAD_CHECK_INTERVAL);
		} else {
			_userLoadCheckCount++;
			// if dictionary and theme data loaded setup UI
			if(_staffLoaded && _learnersLoaded) {
				clearInterval(_UserLoadCheckIntervalID);
				
				trace('ALL USERS LOADED -CONTINUE');
				// populate learner/staff scrollpanes
				broadcastViewUpdate("USERS_LOADED", null, null);
				
				
			} else if(_userLoadCheckCount >= USER_LOAD_CHECK_TIMEOUT_COUNT) {
				Debugger.log('reached timeout waiting for data to load.',Debugger.CRITICAL,'checkUsersLoaded','MonitorModel');
				clearInterval(_UserLoadCheckIntervalID);		
			}
		}
	}
	
	private function resetUserFlags():Void{
		staffLoaded = false;
		learnersLoaded = false;
		_userLoadCheckCount = 0;
		_UserLoadCheckIntervalID = null;
	}
	
	private function requestLearners(data:Object){
		
		trace('requesting learners...');
		var callback:Function = Proxy.create(this,saveLearners);
		_wizard.requestUsers(LEARNER_ROLE, data.organisationID, callback);
	}

	
	private function requestStaff(data:Object){
		
		trace('requesting staff members...');
		var callback:Function = Proxy.create(this,saveStaff);
		
		_wizard.requestUsers(STAFF_ROLE, data.organisationID, callback);
	}
	
	public function saveLearners(users:Array){
		trace('retrieving back users for org by role: ' + LEARNER_ROLE);
		
		saveUsers(users, LEARNER_ROLE);
		
		dispatchEvent({type:'learnersLoad',target:this});
	}
	
	public function saveStaff(users:Array){
		trace('retrieving back users for org by role: ' + STAFF_ROLE);
		
		saveUsers(users, STAFF_ROLE);
		
		dispatchEvent({type:'staffLoad',target:this});
	}

	private function saveUsers(users:Array, role:String):Void{
		
		for(var i=0; i< users.length; i++){
			var u:Object = users[i];
			
			var user:User = User(organisation.getUser(u.userID));
			if(user != null){
				trace('adding role to existing user: ' + user.getFirstName() + ' ' + user.getLastName());
				user.addRole(role);
			} else {
				user = new User();
				user.populateFromDTO(u);
				user.addRole(role);
				
				trace('adding user: ' + user.getFirstName() + ' ' + user.getLastName() + ' ' + user.getUserId());
				organisation.addUser(user);
			}
		}
	}
	
	public function setDirty(){
		_isDirty = true;
	}

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
		p.x = __x;
		p.y = __y;
		return p;
	}  
	
	/**
	 * Sets up the tree for the 1st time
	 * 
	 * @usage   
	 * @return  
	 */
	public function initOrganisationTree(){
		_treeDP = new XML();
		_orgResources = new Array();
	}
	
	/**
	 * 
	 * @usage   
	 * @param   neworgResources 
	 * @return  
	 */
	public function setOrganisationResource(key:String,neworgResources:XMLNode):Void {
		Debugger.log(key+'='+neworgResources,Debugger.GEN,'setOrganisationResource','org.lamsfoundation.lams.monitoring.mv.MonitorModel');
		_orgResources[key] = neworgResources;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getOrganisationResource(key:String):XMLNode{
		Debugger.log(key+' is returning '+_orgResources[key],Debugger.GEN,'getOrganisationResource','org.lamsfoundation.lams.monitoring.mv.MonitorModel');
		return _orgResources[key];
		
	}

	
	public function get treeDP():XML{
		return _treeDP;
	}

	public function get organisation():Organisation{
		return _org;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newselectedTreeNode 
	 * @return  
	 */
	public function setSelectedTreeNode (newselectedTreeNode:XMLNode):Void {
		_selectedOrgTreeNode = newselectedTreeNode;
		trace('branch: ' + _selectedOrgTreeNode.attributes.isBranch);
		//if(!_selectedOrgTreeNode.attributes.isBranch){
			// get the organisations (node) users by role
			//var roles:Array = new Array(LEARNER_ROLE, STAFF_ROLE, TEACHER_ROLE);
			setOrganisation(new Organisation(_selectedOrgTreeNode.attributes.data));
			resetUserFlags();
			// polling method - waiting for all users to load before displaying users in UI
			checkUsersLoaded();
			
			// load users
			requestLearners(_selectedOrgTreeNode.attributes.data);
			requestStaff(_selectedOrgTreeNode.attributes.data);
			
			trace(staffLoaded);
			trace(learnersLoaded);
		//}

	}
	
	public function getLessonClassData():Object{
		var classData:Object = new Object();
		var r:Object = resultDTO;
		var staff:Object = new Object();
		var learners:Object = new Object();
		if(r){
			trace('getting lesson class data...');
			if(lessonID){classData.lessonID = lessonID;}
			if(r.selectedResourceID){classData.organisationID = r.selectedResourceID;}
			classData.staff = staff;
			classData.learners = learners;
			if(r.staffGroupName){classData.staff.groupName = r.staffGroupName;}
			if(r.selectedStaff){staff.users = r.selectedStaff;}
			if(r.learnersGroupName){classData.learners.groupName = r.learnersGroupName;}
			if(r.selectedLearners){classData.learners.users = r.selectedLearners;}
			return classData;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function getSelectedTreeNode ():XMLNode {
		return _selectedOrgTreeNode;
	}

	public function get learnersLoaded():Boolean{
		return _learnersLoaded;
	}
	
	public function set learnersLoaded(a:Boolean):Void{
		_learnersLoaded = a;
	}
	
	public function get staffLoaded():Boolean{
		return _staffLoaded;
	}
	
	public function set staffLoaded(a:Boolean):Void{
		_staffLoaded = a;
	}
	
	public function get workspaceResultDTO():Object{
		return _workspaceResultDTO;
	}
	
	public function set workspaceResultDTO(a:Object):Void{
		_workspaceResultDTO = a;
	}
	
	public function get resultDTO():Object{
		return _resultDTO;
	}
	
	public function set resultDTO(a:Object):Void{
		_resultDTO = a;
	}
	
	public function get stepID():Number{
		return _stepID;
	}
	
	public function set stepID(a:Number):Void{
		
		var obj = new Object();
		obj.lastStep = stepID;
		obj.currentStep = a;
		
		_stepID = obj.currentStep;
		
		//Set flag for notify observers
		setChanged();
        
		//build and send update object
		infoObj = {};
		infoObj.data = obj;
		infoObj.updateType = "STEP_CHANGED";
		notifyObservers(infoObj);
	}
	
	public function get lessonID():Number{
		return _lessonID;
	}
	
	public function set lessonID(a:Number){
		_lessonID = a;
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
	
	public function get className():String{
        return 'WizardModel';
    }

	public function getWizard():Wizard{
		return _wizard;
	}
	
}
