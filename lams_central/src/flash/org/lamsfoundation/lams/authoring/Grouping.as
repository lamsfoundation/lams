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
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;/**
* Grouping data class. Groupings are craeted by grouping activities.  They contain data about how to group the users.
*
* @author      DC
* @version     0.1
* @comments    
*/
class org.lamsfoundation.lams.authoring.Grouping {
	
	public static var RANDOM_GROUPING:Number = 1;
	public static var CHOSEN_GROUPING:Number = 2;
	
	private var _groupingID:Number;
	private var _groupingUIID:Number;
	private var _groupingTypeID:Number;
	private var _numberOfGroups:Number;
	private var _maxNumberOfGroups:Number;
	private var _learnersPerGroups:Number;
	
	
	function Grouping(uiid){
		_groupingUIID = uiid;
		Debugger.log('Created a new Grouping:'+_groupingUIID,Debugger.GEN,'Constructor','Grouping');
	}
	//static class level methods
	/**
	 * Created an array of grouping types to be can be used as a dataprovider
	 * @usage   
	 * @return  
	 */
	public static function getGroupingTypesDataProvider():Array{
		var types:Array = [];
		types.addItem({label: Dictionary.getValue('random_grp_lbl'), data: RANDOM_GROUPING});
		types.addItem({label: Dictionary.getValue('chosen_grp_lbl'), data: CHOSEN_GROUPING});
		return types;
	}

	/**
	 * Populates the class using a DTO.  All fields are overwritten, even if they dont exist in the DTO
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
			_groupingID = dto.groupingID;
			_groupingUIID = dto.groupingUIID;
			_groupingTypeID= dto.groupingTypeID;
			_numberOfGroups = dto.numberOfGroups;
			_maxNumberOfGroups = dto.maxNumberOfGroups;
			_learnersPerGroups = dto.learnersPerGroups;

	}
	
	
	/**
	 * Returnd a DatTransferObject with all the fields.  If a field is null it is excluded
	 * @usage   
	 * @return  
	 */
	public function toData():Object{
		var dto:Object = new Object();
		if(_groupingID){		dto.groupingID = _groupingID;				}
		if(_groupingUIID){		dto.groupingUIID = _groupingUIID;			}
		if(_groupingTypeID){	dto.groupingTypeID = _groupingTypeID;		}
		if(_numberOfGroups){	dto.numberOfGroups = _numberOfGroups;		}
		if(_maxNumberOfGroups){	dto.maxNumberOfGroups = _maxNumberOfGroups;	}
		if(_learnersPerGroups){	dto.learnersPerGroups = _learnersPerGroups;	}
		return dto;
	}
/**
	 * 
	 * @usage   
	 * @param   newgroupingID 
	 * @return  
	 */
	public function set groupingID (newgroupingID:Number):Void {
		_groupingID = newgroupingID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingID ():Number {
		return _groupingID;
	}
	/**
	 * 
	 * @usage   
	 * @param   newgroupingUIID 
	 * @return  
	 */
	public function set groupingUIID (newgroupingUIID:Number):Void {
		_groupingUIID = newgroupingUIID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingUIID ():Number {
		return _groupingUIID;
	}
	/**
	 * 
	 * @usage   
	 * @param   newgroupingTypeID 
	 * @return  
	 */
	public function set groupingTypeID (newgroupingTypeID:Number):Void {
		_groupingTypeID = newgroupingTypeID;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingTypeID ():Number {
		return _groupingTypeID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newnumberOfGroups 
	 * @return  
	 */
	public function set numberOfGroups (newnumberOfGroups:Number):Void {
		_numberOfGroups = newnumberOfGroups;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get numberOfGroups ():Number {
		return _numberOfGroups;
	}
	/**
	 * 
	 * @usage   
	 * @param   newmaxNumberOfGroups 
	 * @return  
	 */
	public function set maxNumberOfGroups (newmaxNumberOfGroups:Number):Void {
		_maxNumberOfGroups = newmaxNumberOfGroups;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get maxNumberOfGroups ():Number {
		return _maxNumberOfGroups;
	}
	/**
	 * 
	 * @usage   
	 * @param   newlearnersPerGroups 
	 * @return  
	 */
	public function set learnersPerGroups (newlearnersPerGroups:Number):Void {
		_learnersPerGroups = newlearnersPerGroups;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get learnersPerGroups ():Number {
		return _learnersPerGroups;
	}




	
}