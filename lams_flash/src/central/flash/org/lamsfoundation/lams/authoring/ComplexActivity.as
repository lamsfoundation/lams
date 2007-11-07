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
/*
* This class represents all the complex activity types.  they are not much different, so we can handle them in one class.
* For reference these are the activity types
* <pre><code>
* public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
* public static var OPTIONAL_ACTIVITY_TYPE:Number = 7;
* public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
* </code></pre>
* @author      DC
* @version     0.1
* @see		   Activity
*/
class ComplexActivity extends Activity{
	
	private var _maxOptions:Number;
	private var _minOptions:Number;
	private var _optionsInstructions:String;
	
	private var _firstActivityUIID:Number;
	
	private var _isSequenceBased:Boolean;
	private var _noSequences:Number;
	
	function ComplexActivity(activityUIID:Number){
		super(activityUIID);
		_firstActivityUIID = null;
	}
	
	/**
	 * Creates a complex activity from a dto... which is nice
	 * @usage   
	 * @param   dto 
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
		super.populateFromDTO(dto);
		if(_activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE){
			_maxOptions = dto.maxOptions;
			_minOptions = dto.minOptions;
			//TODO: This is missing in the Library packet - tell mai.
			_optionsInstructions = dto.optionsInstructions;
		}
		
		if(StringUtils.isWDDXNull(dto.defaultActivityUIID)) _firstActivityUIID = null;
		else _firstActivityUIID = dto.defaultActivityUIID;
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
		if(_activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE){
			if(_maxOptions){	dto.maxOptions = _maxOptions;		}
			if(_minOptions){	dto.minOptions = _minOptions;		}
			if(_optionsInstructions){	dto.optionsInstructions = _optionsInstructions;		}
		}
		return dto;
	}
	
	/**
	 * Creates an exact copy of this ComplexActivity
	 * @usage   
	 * @return  the copy
	 */
	public function clone():ComplexActivity{
		var dto:Object = toData();
		var ca = new ComplexActivity();
		ca.populateFromDTO(dto);
		return ca;
	}
	
	/**
	 * Used by OPTIONAL_ACTIVITY_TYPE
	 * @usage   
	 * @param   newmaxOptions 
	 * @return  
	 */
	public function set maxOptions (newmaxOptions:Number):Void {
		_maxOptions = newmaxOptions;
	}
	/**
	 * used by OPTIONAL_ACTIVITY_TYPE
	 * @usage   
	 * @return  
	 */
	public function get maxOptions ():Number {
		return _maxOptions;
	}

	
	/**
	 * used by OPTIONAL_ACTIVITY_TYPE
	 * @usage   
	 * @param   newminOptions 
	 * @return  
	 */
	public function set minOptions (newminOptions:Number):Void {
		_minOptions = newminOptions;
	}
	/**
	 * used by OPTIONAL_ACTIVITY_TYPE
	 * @usage   
	 * @return  
	 */
	public function get minOptions ():Number {
		return _minOptions;
	}

	
	/**
	 * 
	 * @usage   
	 * @param   newoptionsInstructions 
	 * @return  
	 */
	public function set optionsInstructions (newoptionsInstructions:String):Void {
		_optionsInstructions = newoptionsInstructions;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get optionsInstructions ():String {
		return _optionsInstructions;
	}
	
	public function get firstActivityUIID():Number{
		return _firstActivityUIID;
	}
	
	public function set firstActivityUIID(a:Number):Void{
		_firstActivityUIID = a;
	}

	public function get isSequenceBased():Boolean{
		return _isSequenceBased;
	}
	
	public function set isSequenceBased(a:Boolean):Void{
		_isSequenceBased = a;
	}
	
	public function get noSequences():Number{
		return _noSequences;
	}
	
	public function set noSequences(a:Number):Void{
		_noSequences = a;
	}
}

