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
import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.util.*;/**
* Transition data class. Transitions are used in the design to join up activities. Stored in the DDM
*
* @author      DC
* @version     0.1
* @comments    DesignDataModel stores a complete learning design
*/
class org.lamsfoundation.lams.authoring.Transition {
	
	//Transition properties
	
	private var _transitionID:Number;
	private var _transitionUIID:Number;
	private var _fromActivityID:Number;
	private var _fromUIID:Number;
	private var _toActivityID:Number;
	private var _toUIID:Number;
	
	private var _title:String;
	private var _description:String;
	
	private var _createDateTime:Date;
	//TODO 05-10-05: This will be removed by mai this week
	private var _learningDesignID:Number;
	
	
	function Transition(transitionUIID,
						fromUIID,
						toUIID,
						learningDesignID){
		_transitionUIID = transitionUIID;
		_fromUIID = fromUIID;
		_toUIID = toUIID;
		_learningDesignID = learningDesignID;
		
		Debugger.log('Created a new transition, transitionUIID:'+transitionUIID,Debugger.GEN,'Constructor','Transition');
		
	}
	
	public function isToGateActivity():Boolean{
		var ddm = Application.getInstance().getDesignDataModel();
		var a:Activity = ddm.getActivityByUIID(this.toActivityID);
		return a.isGateActivity();
	}
	
	
	public function toData():Object{
		var dto:Object = new Object();
		dto.transitionID = (_transitionID) ?  _transitionID : Config.NUMERIC_NULL_VALUE;
		dto.transitionUIID= (transitionUIID) ?  transitionUIID : Config.NUMERIC_NULL_VALUE;
		dto.fromActivityID = (_fromActivityID) ?  _fromActivityID : Config.NUMERIC_NULL_VALUE;
		dto.fromUIID = (_fromUIID) ?  _fromUIID : Config.NUMERIC_NULL_VALUE;
		dto.toActivityID = (_toActivityID) ?  _toActivityID : Config.NUMERIC_NULL_VALUE;
		dto.toUIID = (_toUIID) ?  _toUIID : Config.NUMERIC_NULL_VALUE;
		dto.title = (_title) ?  _title : Config.STRING_NULL_VALUE;
		dto.description = (_description) ?  _description : Config.STRING_NULL_VALUE;
		dto.createDateTime = (_createDateTime) ?  _createDateTime : Config.DATE_NULL_VALUE;
		dto.learningDesignID = (_learningDesignID) ?  _learningDesignID : Config.NUMERIC_NULL_VALUE;
		return dto;
	}

	public function set transitionID(a:Number):Void{
		_transitionID = a;
	}
	public function get transitionID():Number{
		return _transitionID;
	}	public function set transitionUIID(a:Number):Void{
		_transitionUIID = a;
	}
	
	public function get transitionUIID():Number{
		return _transitionUIID;
	}
	
	public function set fromActivityID(a:Number):Void{
		_fromActivityID = a;
	}
	public function get fromActivityID():Number{
		return _fromActivityID;
	}
	
	public function set fromUIID(a:Number):Void{
		_fromUIID = a;
	}
	public function get fromUIID():Number{
		return _fromUIID;
	}
	
	public function set toActivityID(a:Number):Void{
		_toActivityID = a;
	}
	public function get toActivityID():Number{
		return _toActivityID;
	}	public function set toUIID(a:Number):Void{
		_toUIID = a;
	}
	public function get toUIID():Number{
		return _toUIID;
	}
	public function set title(a:String):Void{
		_title = a;
	}
	public function get title():String{
		return _title;
	}
	
	public function set description(a:String):Void{
		_description = a;
	}
	public function get description():String{
		return _description;
	}
	
	public function set createDateTime(a:Date):Void{
		_createDateTime = a;
	}
	public function get createDateTime():Date{
		return _createDateTime;
	}
	
	
	public function set learningDesignID(a):Void{
		_learningDesignID = a;
	}
	public function get learningDesignID():Number{
		return _learningDesignID;
	}
	
}