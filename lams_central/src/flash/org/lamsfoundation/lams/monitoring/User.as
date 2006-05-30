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
import org.lamsfoundation.lams.common.*;
/*
* User - singleton class representing a user (teacher, staff, learner)
*/
class User {
	
	private var _userId:Number;
	private var _firstName:String;
	private var _lastName:String;
	private var _userName:String;
	private var _userRoles:Array;
	private var _userProgress:Progress;
	
	private static var _instance:User = null;
	
	/**
	* Constructor.
	*/
	public function User (dto:Object){
		_userProgress = null;
		_userRoles = new Array();
		
		if(dto != null) { 
			populateFromDTO(dto);
		}
	}
	
	/**
	 * 
	 * @return the User
	 */
	public static function getInstance():User{
		if(User._instance == null){
            User._instance = new User();
        }
        return User._instance;
	}
	
	public function populateFromDTO(userDTO:Object){
		_userId = userDTO.userID;
		_firstName = userDTO.firstName;
		_lastName = userDTO.lastName;
		_userName = userDTO.login;
		//setRoles(userDTO.roles);
	}
	
	public function getDTO():Object{
		var obj = new Object();
		obj.userID = _userId;
		obj.firstName = _firstName;
		obj.lastName = _lastName;
		obj.login = _userName;
		
		return obj;
	}
	
	public function getUserId():Number{
		return _userId;
	}
	
	public function getFirstName():String{
		return _firstName;
	}
	
	public function getLastName():String{
		return _lastName;
	}
	
	public function getUsername():String{
		return _userName;
	}
	
	public function setRoles(roles:Array):Boolean{
		
		_userRoles = new Array();
		
		for(var i=0; i< roles.length; i++){
			var r:String = String(roles[i]);
			_userRoles.push(r);
		}
		return true;
	}
	
	public function addRole(role:String):Boolean{
		_userRoles.push(role);
		return true;
	}
	
	public function getRoles():Array{
		return _userRoles;
	}
	
	public function hasRole(roleName:String):Boolean{
		for(var i=0; i<_userRoles.length;i++){
			if(_userRoles[i] == roleName){
				return true;
			}
		}
		return false;
	}
	
	public function setProgress(userProgress:Progress){
		_userProgress = userProgress;
	}
	
	public function getProgress():Progress{
		return _userProgress;
	}
	
}