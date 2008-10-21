/***************************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 
 class org.lamsfoundation.lams.authoring.cv.ValidationIssue {
	
	private var _code:String;
	private var _message:String;
	private var _UIID:Number
	
	
	public static var TRANSITION_ERROR_KEY:String  = "validation_error_transitionNoActivityBeforeOrAfter";				// T
	public static var ACTIVITY_TRANSITION_ERROR_KEY:String = "validation_error_activityWithNoTransition";				// AT
	public static var INPUT_TRANSITION_ERROR_TYPE1_KEY:String = "validation_error_inputTransitionType1";				// IT
	public static var INPUT_TRANSITION_ERROR_TYPE2_KEY:String = "validation_error_inputTransitionType2";  				
	public static var OUTPUT_TRANSITION_ERROR_TYPE1_KEY:String = "validation_error_outputTransitionType1";				// OT
	public static var OUTPUT_TRANSITION_ERROR_TYPE2_KEY:String = "validation_error_outputTransitionType2";
	
	public static var TRANSITION_ERROR_CODE:String = "T";
	public static var ACTIVITY_TRANSITION_ERROR_CODE:String = "AT";
	public static var INPUT_TRANSITION_ERROR_CODE:String = "IT";
	public static var OUTPUT_TRANSITION_ERROR_CODE:String = "OT1";
	
	public static var GROUPING_ACTIVITY_MISSING_GROUPING_ERROR_CODE:String = "GM";
	public static var GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_ERROR_CODE:String = "GC";
	
	function ValidationIssue(code:String, message:String, UIID:Number) {
		_code = code;
		_message = message;
		_UIID = UIID;
	}
	
	function get message():String {
		return _message;
	}
	
	function get UIID():Number {
		return _UIID;
	}
	
}
	
	