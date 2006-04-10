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

import org.lamsfoundation.lams.authoring.*;
/*
*
* @author      DC
* @version     0.1
* @comments    Tool Activity Data storage class. 
* @see		   Activity
*/
class GateActivity extends Activity{
	
	private static var GATE_ACTIVITY_LEVEL_LEARNER = 1;
	private static var GATE_ACTIVITY_LEVEL_GROUP = 2;
	private static var GATE_ACTIVITY_LEVEL_CLASS = 3;

	
	private var _gateActivityLevelID:Number; 	// defaults to learner for 1.1
	private var _gateStartTimeOffset:Number;	//the no. mins after the start of lesson that this gate will open
	private var _gateEndTimeOffset:Number;		//the no. mins after the sart that this gate will close again (optional)
	private var _gateOpen:Boolean;	
	
	private var _gateEndDateTime:Date;			// not used - now only off set
	private var _gateStartDateTime:Date;		// not used - now only off set
	
	

	function GateActivity(activityUIID:Number,activityTypeID){
		super(activityUIID);
		_activityTypeID = activityTypeID;
		//defaults to class
		_gateActivityLevelID = GATE_ACTIVITY_LEVEL_LEARNER;
		_activityCategoryID = CATEGORY_SYSTEM;
		_groupingSupportType = GROUPING_SUPPORT_OPTIONAL;
	}
	
	
	
	/**
	 * Creates a complex activity from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		_gateActivityLevelID = dto.gateActivityLevelID;
		if(_activityTypeID == Activity.SCHEDULE_GATE_ACTIVITY_TYPE){
			_gateStartTimeOffset = dto.gateStartTimeOffset;
			_gateEndTimeOffset = dto.gateEndTimeOffset;
		}
	}
	
	/**
	 * Creates an object containing all the props of the ComplexActivity.  
	 * If a value is null then it is ommitted... if itsd the null value from const 
	 * then its included
	 * @usage   
	 * @return  the DTO
	 */
	public function toData():Object{
		var dto:Object = super.toData();
		if(_gateActivityLevelID){	dto.gateActivityLevelID = _gateActivityLevelID;	}
		if(_gateStartTimeOffset){	dto.gateStartTimeOffset = _gateStartTimeOffset;	}
		if(_gateEndTimeOffset)	{	dto.gateEndTimeOffset= _gateEndTimeOffset;		}
		return dto;
	}
	
	
	
	
	/**
	 * 
	 * @usage   
	 * @param   newgateActivityLevelID 
	 * @return  
	 */
	public function set gateActivityLevelID (newgateActivityLevelID:Number):Void {
		_gateActivityLevelID = newgateActivityLevelID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateActivityLevelID ():Number {
		return _gateActivityLevelID;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateEndDateTime 
	 * @return  
	 */
	public function set gateEndDateTime (newgateEndDateTime:Date):Void {
		_gateEndDateTime = newgateEndDateTime;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateEndDateTime ():Date {
		return _gateEndDateTime;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateEndTimeOffset 
	 * @return  
	 */
	public function set gateEndTimeOffset (newgateEndTimeOffset:Number):Void {
		_gateEndTimeOffset = newgateEndTimeOffset;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateEndTimeOffset ():Number {
		return _gateEndTimeOffset;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateOpen 
	 * @return  
	 */
	public function set gateOpen (newgateOpen:Boolean):Void {
		_gateOpen = newgateOpen;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateOpen ():Boolean {
		return _gateOpen;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateStartDateTime 
	 * @return  
	 */
	public function set gateStartDateTime (newgateStartDateTime:Date):Void {
		_gateStartDateTime = newgateStartDateTime;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateStartDateTime ():Date {
		return _gateStartDateTime;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newgateStartTimeOffset 
	 * @return  
	 */
	public function set gateStartTimeOffset (newgateStartTimeOffset:Number):Void {
		_gateStartTimeOffset = newgateStartTimeOffset;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get gateStartTimeOffset ():Number {
		return _gateStartTimeOffset;
	}

	
	
}

