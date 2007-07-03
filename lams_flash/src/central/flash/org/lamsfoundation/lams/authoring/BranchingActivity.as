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
/*
*
* @author      Mitchell Seaton
* @version     2.1
* @comments    Branching Activity Data storage class. 
* @see		   Activity
*/
class BranchingActivity extends ComplexActivity {
	
	private var _startXCoord:Number;
	private var _startYCoord:Number;
	private var _endXCoord:Number;
	private var _endYCoord:Number;
	
	private static var DEFAULT_STARTX:Number = 0;
	private static var DEFAULT_STARTY:Number = 0;
	private static var DEFAULT_ENDX:Number = 0;
	private static var DEFAULT_ENDY:Number = 0;
	
	function BranchingActivity(activityUIID:Number){
		super(activityUIID);
		_activityTypeID = BRANCHING_ACTIVITY_TYPE;
		
		_startXCoord = DEFAULT_STARTX;
		_startYCoord = DEFAULT_STARTY;
		_endXCoord = DEFAULT_ENDX;
		_endYCoord = DEFAULT_ENDY;
		
	}
	
	/**
	 * Creates from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		_startXCoord = dto.startXCoord;
		_startYCoord = dto.startYCoord;
		_endXCoord = dto.endXCoord;
		_endYCoord = dto.endXCoord;
		
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
		
		if(_startXCoord) dto.startXCoord = _startXCoord;
		if(_startYCoord) dto.startYCoord = _startYCoord;
		if(_endXCoord) dto.endXCoord = _endXCoord;
		if(_endYCoord) dto.endYCoord = _endYCoord;
		
		return dto;
	}
	
	public function set startXCoord(a:Number):Void{
		_startXCoord = Math.round(a);
	}
	
	public function get startXCoord():Number{
		return _startXCoord;
	}
	
	public function set startYCoord(a:Number):Void{
		_startYCoord = Math.round(a);
	}
	
	public function get startYCoord():Number{
		return _startYCoord;
	}
	
	public function set endXCoord(a:Number):Void{
		_endXCoord = Math.round(a);
	}
	
	public function get endXCoord():Number{
		return _endXCoord;
	}
	
	public function set endYCoord(a:Number):Void{
		_endYCoord = Math.round(a);
	}
	
	public function get endYCoord():Number{
		return _endYCoord;
	}
}

