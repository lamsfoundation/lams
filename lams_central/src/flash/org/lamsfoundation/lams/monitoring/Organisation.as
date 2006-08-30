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

import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.common.util.*;

/*
* Organistation - singleton class representing an organisation
*/
class Organisation {
	
	private var _parentOrg:Number = null;
	private var _orgId:Number;
	private var _orgTypeId:Number;
	private var _orgName:String;
	private var _orgDesc:String;
	private var _users:Hashtable;
	
	private static var _instance:Organisation = null;
	
	/**
	* constants
	*/
	public static var COURSE_MANAGER_ROLE:String = "COURSE MANAGER";
	public static var MONITOR_ROLE:String = "MONITOR";
	public static var LEARNER_ROLE:String = "LEARNER";
	
	/**
	* Constructor.
	*/
	public function Organisation (dto:Object){
		_users = new Hashtable();
		if(dto != null) { 
			populateFromDTO(dto);
		}
	}
	
	/**
	 * 
	 * @return the Org
	 */
	public static function getInstance():Organisation{
		if(Organisation._instance == null){
            Organisation._instance = new Organisation();
        }
        return Organisation._instance;
	}
	
	public function populateFromDTO(orgDTO:Object){
		_parentOrg = orgDTO.parentID;
		_orgId = orgDTO.organisationID;
		_orgTypeId = orgDTO.organisationTypeId;
		_orgName = orgDTO.name;
		_orgDesc = orgDTO.description;
	}
	
	public function getOrganisationID():Number{
		return _orgId;
	}
	
	public function getName():String{
		return _orgName;
	}
	
	public function setParent(parentOrg:Number){
		_parentOrg = parentOrg;
	}
	
	public function getParent():Number{
		return _parentOrg;
	}
	
	public function setUsers(users:Array):Boolean {
		_users.clear();
		
		for(var i=0; i<users.length;i++){
			var u:Object = users[i];
			var user:User = new User();
			user.populateFromDTO(u);
			_users.put(user.getUserId(),user);
		}
		
		return true;
	}
	
	public function getUsers():Hashtable {
		return _users;
	}
	
	public function getUser(userID:Number):Object{
		return _users.get(userID);
	}
	
	public function addUser(user:User):Boolean {
		_users.put(user.getUserId(), user);
		return true;
	}
	
	public function removeUser(user:User):Boolean {
		_users.remove(user);
		return true;
	}
	
	public function getCourseManagers():Array{
		if(_users==null){
			return null;
		}
		
		return getUsersByRole(COURSE_MANAGER_ROLE);
	}
	
	public function getMonitors():Array{
		if(_users==null){
			return null;
		}
		
		return getUsersByRole(MONITOR_ROLE);
	}
	
	public function getLearners():Array{
		if(_users==null){
			return null;
		}
		
		return getUsersByRole(LEARNER_ROLE);
	}
	
	public function isLearner(key:Number):Boolean{
		if(_users.containsKey(key)){
			var user:User = User(_users.get(key));
			if(user.hasRole(LEARNER_ROLE)){
				return true;
			}
		}
		
		return false;
	}
	
	public function isMonitor(key:Number):Boolean{
		if(_users.containsKey(key)){
			var user:User = User(_users.get(key));
			if(user.hasRole(MONITOR_ROLE)){
				return true;
			}
		}
		
		return false;
	}
	
	private function getUsersByRole(roleName:String):Array{
		var usrs:Array = new Array();
		var keys:Array = _users.keys();
		trace('getting users by role...');
		for(var i=0; i<keys.length;i++){
			var user:User = User(_users.get(keys[i]));
			//var u:User = user.classInstanceRefs;
			if(user.hasRole(roleName)){
				usrs.push(user);
			}
		}
		
		return usrs;
	}
	
	public function clearUsers():Void{
		_users.clear();
	}
	
	function get className():String{
        return 'Organisation';
    }
}