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
import org.lamsfoundation.lams.common.util.StringUtils;
import org.lamsfoundation.lams.common.Config;

/*
* This class represents the sequence activity.
* For reference these are the activity types
* <pre><code>
* public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
* </code></pre>
* @author      Mitchell Seaton
* @version     2.1
* @see		   Activity
*/
class SequenceActivity extends ComplexActivity{
	
	private var _firstActivityUIID:Number;
	private var _empty:Boolean;
	
	function SequenceActivity(activityUIID:Number){
		super(activityUIID);
		_activityTypeID = SEQUENCE_ACTIVITY_TYPE;
		_firstActivityUIID = null;
		_empty = true;
	}
	
	/**
	 * Creates a complex activity from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		if(StringUtils.isWDDXNull(dto.firstActivityUIID)) _firstActivityUIID = null;
		else _firstActivityUIID = dto.firstActivityUIID;
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
		dto.firstActivityUIID = (_firstActivityUIID == null) ? Config.NUMERIC_NULL_VALUE : _firstActivityUIID;

		return dto;
	}
	
	/**
	 * Creates an exact copy of this SequenceActivity
	 * @usage   
	 * @return  the copy
	 */
	public function clone():SequenceActivity{
		var dto:Object = toData();
		var sa = new SequenceActivity();
		sa.populateFromDTO(dto);
		return sa;
	}

	public function get firstActivityUIID():Number{
		return _firstActivityUIID;
	}
	
	public function set firstActivityUIID(a:Number):Void{
		_firstActivityUIID = a;
	}
	
	public function set empty(b:Boolean):Void{
		_empty = b;
	}
	
	public function get empty():Boolean{
		return _empty;
	}

}

