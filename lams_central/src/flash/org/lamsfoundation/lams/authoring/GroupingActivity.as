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
class GroupingActivity extends Activity{
	
	private var _createGroupingID:Number;
	private var _createGroupingUIID:Number;

	
	function GroupingActivity(activityUIID:Number){
		super(activityUIID);
		_activityTypeID = GROUPING_ACTIVITY_TYPE;
		_activityCategoryID = CATEGORY_SYSTEM;
		_groupingSupportType = GROUPING_SUPPORT_OPTIONAL;
	}
	
	
	
	/**
	 * Creates from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		_createGroupingID = dto.createGroupingID;
		_createGroupingUIID = dto.createGroupingUIID;
	}
	
	/**
	 * Creates an object containing all the props
	 * If a value is null then it is ommitted... if itsd the null value from const 
	 * then its included
	 * @usage   
	 * @return  the DTO
	 */
	public function toData():Object{
		var dto:Object = super.toData();
		if(_createGroupingID){	dto.createGroupingID = _createGroupingID;	}
		if(_createGroupingUIID){	dto.createGroupingUIID = _createGroupingUIID;	}
		return dto;
	}
	

	
	//get and sets
	
	
	/**
	 * 
	 * @usage   
	 * @param   newcreateGroupingID 
	 * @return  
	 */
	public function set createGroupingID (newcreateGroupingID:Number):Void {
		_createGroupingID = newcreateGroupingID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get createGroupingID ():Number {
		return _createGroupingID;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newcreateGroupingUIID 
	 * @return  
	 */
	public function set createGroupingUIID (newcreateGroupingUIID:Number):Void {
		_createGroupingUIID = newcreateGroupingUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get createGroupingUIID ():Number {
		return _createGroupingUIID;
	}

	
	

	
}

