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
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.*
import org.lamsfoundation.lams.common.util.*

/*
*Activity Data storage class. USed as a base class for extending to be Tool, Gate and Complex
* <pre><code>
* 
* * static final variables indicating the type of activities
* 	/******************************************************************
	public static var TOOL_ACTIVITY_TYPE:Number = 1;
	public static var GROUPING_ACTIVITY_TYPE:Number = 2;
	public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
	public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
	public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
	public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
	public static var OPTIONAL_ACTIVITY_TYPE:Number = 7;
	public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;

	* static final variables indicating the the category of activities
    *******************************************************************
	public static var CATEGORY_SYSTEM:Number = 1;
	public static var CATEGORY_COLLABORATION:Number = 2;
	public static var CATEGORY_ASSESSMENT:Number = 3;
	public static var CATEGORY_CONTENT:Number = 4;
	public static var CATEGORY_SPLIT:Number = 5;
	/******************************************************************
	
	
	/**
	 * static final variables indicating the grouping_support of activities
	 *******************************************************************
	 public static var GROUPING_SUPPORT_NONE:Number = 1;
	 public static var GROUPING_SUPPORT_OPTIONAL:Number = 2;
	 public static var GROUPING_SUPPORT_REQUIRED:Number = 3;
	/******************************************************************

* </code></pre>
* @author      DC
* @version     0.1  
*/
class org.lamsfoundation.lams.authoring.Activity {
	
	
	
	/*
	//---------------------------------------------------------------------
    // Class Level Constants
    //---------------------------------------------------------------------
	/**
	 * static final variables indicating the type of activities
	 * available for a LearningDesign 
	******************************************************************/
	public static var TOOL_ACTIVITY_TYPE:Number = 1;
	public static var GROUPING_ACTIVITY_TYPE:Number = 2;
	public static var NO_GATE_ACTIVITY_TYPE:Number = 30
	public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
	public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
	public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
	public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
	public static var OPTIONAL_ACTIVITY_TYPE:Number = 7;
	public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
	public static var SYSTEM_GATE_ACTIVITY_TYPE:Number = 9;
	public static var CHOSEN_BRANCHING_ACTIVITY_TYPE:Number = 10;
	public static var GROUP_BRANCHING_ACTIVITY_TYPE:Number = 11;
	public static var TOOL_BRANCHING_ACTIVITY_TYPE:Number = 12;
	
	/******************************************************************/
	
	/**
	* static final variables indicating the the category of activities
    *******************************************************************/
	public static var CATEGORY_SYSTEM:Number = 1;
	public static var CATEGORY_COLLABORATION:Number = 2;
	public static var CATEGORY_ASSESSMENT:Number = 3;
	public static var CATEGORY_CONTENT:Number = 4;
	public static var CATEGORY_SPLIT:Number = 5;
	/******************************************************************/
	
	
	/**
	 * static final variables indicating the grouping_support of activities
	 *******************************************************************/
	 public static var GROUPING_SUPPORT_NONE:Number = 1;
	 public static var GROUPING_SUPPORT_OPTIONAL:Number = 2;
	 public static var GROUPING_SUPPORT_REQUIRED:Number = 3;
	/******************************************************************/
	
	//Activity Properties:
	// * indicates required field
	//---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	
	private var _objectType:String;			//*	
	private var _activityTypeID:Number;		//*	

	
	private var _activityID:Number;				
	private var _activityCategoryID:Number;	//*			
	
	private var _activityUIID:Number;			//*

	private var _learningLibraryID:Number;		//*
	//TODO: This will be removed by mai this week.
	private var _learningDesignID:Number;			
	private var _libraryActivityID:Number;		

	private var _parentActivityID:Number;		
	private var _parentUIID:Number;		
	

	private var _orderID:Number;		
	
	private var _groupingID:Number;			
	private var _groupingUIID:Number;
	private var _isActivitySelected:String
	
	
	private var _title:String;					//*	
	private var _description:String;			//*
	private var _helpText:String;					
	private var _xCoord:Number;
	private var _yCoord:Number;
	private var _libraryActivityUIImage:String;	
	private var _applyGrouping:Boolean;
	private var _activityToolContentID:Number;

	private var _runOffline:Boolean;
	/*
	* these have now been removed, set in the tool content instead
	private var _offlineInstructions:String;
	private var _onlineInstructions:String;
	*/
	private var _defineLater:Boolean;
	private var _createDateTime:Date;

	private var _groupingSupportType:Number; //*
	
	private var _stopAfterActivity:Boolean;
	private var _readOnly:Boolean;
	
	private var _viewID:Boolean;
	
     
    //Constructor
	 /**
	 * Creates an activity with the minimum of fields. 
	 * 
	 * @param   learningActivityTypeId 
	 * @param   learningLibraryId      
	 * @param   toolId                 
	 * @param   toolContentId          
	 * @param   helpText               
	 * @param   libraryActivityUIImage  
	 */
    function Activity(activityUIID:Number){
        Debugger.log('activityUIID:'+activityUIID,Debugger.GEN,'constructor','Activity');
		_activityUIID = activityUIID;
		
		//defaults
		_objectType = "Activity";
		_applyGrouping = false;
		_stopAfterActivity = false;
		_runOffline = false;
		_defineLater = false;
		_readOnly = false;
		_createDateTime = new Date();
		
	}
	
	//static class level methods
	/**
	 * Created an array of activity types to be can be used as a dataprovider
	 * @usage   
	 * @return  
	 */
	public static function getGateActivityTypes():Array{
		var types:Array = [];
		types.addItem({label: Dictionary.getValue('synch_act_lbl'), data: SYNCH_GATE_ACTIVITY_TYPE});
		types.addItem({label: Dictionary.getValue('sched_act_lbl'), data: SCHEDULE_GATE_ACTIVITY_TYPE});
		types.addItem({label: Dictionary.getValue('perm_act_lbl'), data: PERMISSION_GATE_ACTIVITY_TYPE});
		return types;
	}
	
	/**
	 * Created an array of activity types to be can be used as a dataprovider
	 * @usage   
	 * @return  
	 */
	public static function getBranchingActivityTypes():Array{
		var types:Array = [];
		types.addItem({label: Dictionary.getValue('chosen_branch_act_lbl'), data: CHOSEN_BRANCHING_ACTIVITY_TYPE});
		types.addItem({label: Dictionary.getValue('group_branch_act_lbl'), data: GROUP_BRANCHING_ACTIVITY_TYPE});
		types.addItem({label: Dictionary.getValue('tool_branch_act_lbl'), data: TOOL_BRANCHING_ACTIVITY_TYPE});
		return types;
	}
	
	
	//helper methods
	
	
	public function isGateActivity():Boolean{
		if (_activityTypeID == SYNCH_GATE_ACTIVITY_TYPE){
			return true;
		}else if(_activityTypeID == SCHEDULE_GATE_ACTIVITY_TYPE){
			return true
		}else if (_activityTypeID == PERMISSION_GATE_ACTIVITY_TYPE){
			return true;
		}else if (_activityTypeID == SYSTEM_GATE_ACTIVITY_TYPE){
			return true;
		}else{
			return false;
		}
	}

	public function isSystemGateActivity():Boolean{
		return _activityTypeID == SYSTEM_GATE_ACTIVITY_TYPE;
	}

	public function isGroupActivity():Boolean{
		if (_activityTypeID == GROUPING_ACTIVITY_TYPE){
			return true;
		}
	}
	
	public function isOptionalActivity():Boolean{
		if (_activityTypeID == OPTIONAL_ACTIVITY_TYPE){
			return true;
		}
	}
	
	
	public function isParallelActivity():Boolean{
		if (_activityTypeID == PARALLEL_ACTIVITY_TYPE){
			return true;
		}
	}
	
	public function isBranchingActivity():Boolean{
		if (_activityTypeID == CHOSEN_BRANCHING_ACTIVITY_TYPE || 
			_activityTypeID == GROUP_BRANCHING_ACTIVITY_TYPE ||
			_activityTypeID == TOOL_BRANCHING_ACTIVITY_TYPE){
			return true;
		} else {
			return false;
		}
	}
	
	public function isSequenceActivity():Boolean{
		return _activityTypeID == SEQUENCE_ACTIVITY_TYPE;
	}
	
	/**
	 * Populates all the fields in this activity from a dto object contaning the following fields
	 * NB: This is not very clever, if the field in the dto is blank then the value is overwritten in the class...
	 * <code><pre>

			//activity properties:
			_activityTypeID = dto.activityTypeID;
			_activityID = dto.activityID;
			_activityCategoryID = dto.activityCategoryID;
			_activityUIID = dto.activityUIID;
			_learningLibraryID = dto.learningLibraryID;
			_learningDesignID = dto.learningDesignID;
			_libraryActivityID = dto.libraryActivityID;
			_parentActivityID = dto.parentActivityID;
			_parentUIID = dto.parentUIID
			_orderID = dto.orderID
			_groupingID = dto.groupingID;
			_groupingUIID = dto.groupingUIID
			_title = dto.title;
			_description = dto.description;
			_helpText =  dto.helpText;
			_yCoord = dto.yCoord;
			_xCoord = dto.xCoord;
			_libraryActivityUIImage = dto.libraryActivityUIImage;
			_applyGrouping = dto.applyGrouping;
			_runOffline = dto.runOffline;
			_defineLater = dto.defineLater;
			_createDateTime = dto.createDateTime;
			_groupingSupportType = dto.groupingSupportType;
</pre></code>
	 * 
	 * 
	 * @usage   
	 * @param   dto //the dto containing these fields
	 * @return  
	 */
	public function populateFromDTO(dto:Object){
	
			//activity properties:
			_activityTypeID = dto.activityTypeID;
			_activityID = dto.activityID;
			_activityCategoryID = dto.activityCategoryID;
			_activityUIID = dto.activityUIID;
			_learningLibraryID = dto.learningLibraryID;
			_learningDesignID = dto.learningDesignID;
			_libraryActivityID = dto.libraryActivityID;
			
			if(StringUtils.isWDDXNull(dto.parentActivityID)) { _parentActivityID = null }
			else { _parentActivityID = dto.parentActivityID; }
			
			if(StringUtils.isWDDXNull(dto.parentUIID)) {_parentUIID = null }
			else { _parentUIID = dto.parentUIID}
			
			_orderID = dto.orderID
			_groupingID = dto.groupingID;
			_groupingUIID = dto.groupingUIID
			_title = dto.activityTitle;
			_description = dto.description;
			_helpText =  dto.helpText;
			_yCoord = dto.yCoord;
			_xCoord = dto.xCoord;
			_libraryActivityUIImage = dto.libraryActivityUIImage;
			_applyGrouping = dto.applyGrouping;
			_runOffline = dto.runOffline;
			_defineLater = dto.defineLater;
			_createDateTime = dto.createDateTime;
			_groupingSupportType = dto.groupingSupportType;
			_readOnly = dto.readOnly;
			_stopAfterActivity = dto.stopAfterActivity;

	}
	
	public function toData(){
		var dto:Object = new Object();
		//DC - Changed mode of toData to be ommiting fields with undefined values
		
		if(_activityTypeID){		dto.activityTypeID 		= _activityTypeID;			}
		if(_activityID){			dto.activityID			= _activityID;				}
		if(_activityCategoryID){	dto.activityCategoryID	= _activityCategoryID;		}
		if(_activityUIID){			dto.activityUIID		= _activityUIID;			}
		if(_learningLibraryID){		dto.learningLibraryID	= _learningLibraryID;		}
		if(_learningDesignID){		dto.learningDesignID	= _learningDesignID;		}
		if(_libraryActivityID){		dto.libraryActivityID	= _libraryActivityID;		}
		if(_orderID){				dto.orderID				= _orderID;					}
		if(_groupingID){			dto.groupingID			= _groupingID;				}
		if(_groupingUIID){			dto.groupingUIID		= _groupingUIID;			}
		if(_title){					dto.activityTitle		= _title;					}
		if(_description){			dto.description			= _description;				}
		if(_helpText){				dto.helpText			= _helpText;				}
		if(_yCoord){				dto.yCoord				= _yCoord;					}
		if(_xCoord){				dto.xCoord				= _xCoord;					}
		if(_libraryActivityUIImage){dto.libraryActivityUIImage= _libraryActivityUIImage;}
		
		dto.parentUIID = (_parentUIID==null) ? Config.NUMERIC_NULL_VALUE : _parentUIID;
		dto.parentActivityID = (_parentActivityID==null) ? Config.NUMERIC_NULL_VALUE : _parentActivityID;
		
		//bnools need to be included - so do as follows:
		dto.applyGrouping = (_applyGrouping==null) ? false : _applyGrouping;
		dto.runOffline = (_runOffline==null) ? false : _runOffline;
		
		if(isBranchingActivity())
			if(_activityTypeID == GROUP_BRANCHING_ACTIVITY_TYPE)
				dto.defineLater = _defineLater;
			else
				dto.defineLater = false;
		else
			dto.defineLater = (_defineLater==null) ? false : _defineLater;
		
		
		if(_createDateTime){		dto.createDateTime		= _createDateTime;			}
		if(_groupingSupportType){	dto.groupingSupportType = _groupingSupportType;		}
		if(_readOnly){	dto.readOnly = _readOnly;	 }
		if(_stopAfterActivity != null){ dto.stopAfterActivity = _stopAfterActivity } 
		
		return dto;
	}
	
	public function clone():Activity{
		var dto:Object = toData();
		var n = new Activity(null);
		n.populateFromDTO(dto);
		return n;

	}
	
	//getters and setters:
	public function set objectType(a:String):Void{
		_objectType = a;
	}
	public function get objectType():String{
		return _objectType;
	}

	/**
	 * 
	 * @usage   
	 * @param   newactivityTypeID 
	 * @return  
	 */
	public function set activityTypeID (newactivityTypeID:Number):Void {
		_activityTypeID = newactivityTypeID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activityTypeID ():Number {
		return _activityTypeID;
	}
	
	public function set activityToolContentID (newToolContentID:Number) {
		_activityToolContentID = newToolContentID;
	}
	
	public function get activityToolContentID ():Number {
		return _activityToolContentID;
	}

	public function set activityID(a:Number):Void{
		_activityID = a;
	}
	
	public function get activityID():Number{
		return _activityID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newactivityCategoryID 
	 * @return  
	 */
	public function set activityCategoryID (newactivityCategoryID:Number):Void {
		_activityCategoryID = newactivityCategoryID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activityCategoryID ():Number {
		return _activityCategoryID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newactivityUIID 
	 * @return  
	 */
	public function set activityUIID (newactivityUIID:Number):Void {
		_activityUIID = newactivityUIID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get activityUIID ():Number {
		return _activityUIID;
	}
	
	public function set learningLibraryID(a:Number):Void{
		_learningLibraryID = a;
	}
	
	public function get learningLibraryID():Number{
		return _learningLibraryID;
	}	public function set learningDesignID(a:Number):Void{
		_learningDesignID = a;
	}
	public function get learningDesignID():Number{
		return _learningDesignID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newlibraryActivityID 
	 * @return  
	 */
	public function set libraryActivityID (newlibraryActivityID:Number):Void {
		_libraryActivityID = newlibraryActivityID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get libraryActivityID ():Number {
		return _libraryActivityID;
	}

	/**
	 * 
	 * @usage   
	 * @param   newparentActivityID 
	 * @return  
	 */
	public function set parentActivityID (newparentActivityID:Number):Void {
		_parentActivityID = newparentActivityID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get parentActivityID ():Number {
		return _parentActivityID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newparentUIID 
	 * @return  
	 */
	public function set parentUIID (newparentUIID:Number):Void {
		_parentUIID = newparentUIID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get parentUIID ():Number {
		return _parentUIID;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   neworderID 
	 * @return  
	 */
	public function set orderID (neworderID:Number):Void {
		_orderID = neworderID;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get orderID ():Number {
		return _orderID;
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
	
	public function set helpText(a:String):Void{
		_helpText = a;
	}
	public function get helpText():String{
		return _helpText;
	}
	
	/**
	 * Rounds the value to an integer
	 * @usage   
	 * @param   a 
	 * @return  
	 */
	public function set xCoord(a:Number):Void{
		_xCoord = Math.round(a);
	}
	public function get xCoord():Number{
		return _xCoord;
	}
	/**
	 * Rounds the value to an integer
	 * @usage   
	 * @param   a 
	 * @return  
	 */
	public function set yCoord(a:Number):Void{
		_yCoord = Math.round(a);
	}
	public function get yCoord():Number{
		return _yCoord;
	}
		
	public function set libraryActivityUIImage(a:String):Void{
		_libraryActivityUIImage = a;
	}
	public function get libraryActivityUIImage():String{
		return _libraryActivityUIImage;
	}
	
	public function set runOffline(a:Boolean):Void{
		_runOffline = a;
	}
	public function get runOffline():Boolean{
		return _runOffline;
	}
	
	public function set defineLater(a:Boolean):Void{
		_defineLater = a;
	}
	public function get defineLater():Boolean{
		return _defineLater;
	}
	
	public function set createDateTime(a:Date):Void{
		_createDateTime = a;
	}
	public function get createDateTime():Date{
		return _createDateTime;
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
	 * @param   selected CA 
	 * @return  
	 */
	public function set selectActivity (stat:String):Void {
		_isActivitySelected = stat;
	}
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get selectActivity ():String {
		return _isActivitySelected;
	}

	/**
	 * 
	 * @usage   
	 * @param   newapplyGrouping 
	 * @return  
	 */
	public function set applyGrouping (newapplyGrouping:Boolean):Void {
		_applyGrouping = newapplyGrouping;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get applyGrouping ():Boolean {
		return _applyGrouping;
	}
	
	/**
	 * 
	 * @usage   
	 * @param   newgroupingSupportType 
	 * @return  
	 */
	public function set groupingSupportType (newgroupingSupportType:Number):Void {
		_groupingSupportType = newgroupingSupportType;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get groupingSupportType ():Number {
		return _groupingSupportType;
	}

	/**
	 * 
	 * @usage   
	 * @param   newgroupingSupportType 
	 * @return  
	 */
	public function set readOnly (readOnly:Boolean):Void {
		_readOnly = readOnly;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get readOnly ():Boolean {
		return _readOnly;
	}
	
	public function isReadOnly():Boolean {
		return _readOnly;
	}
	
	/**
	 * 
	 * @usage   
	 * @return  
	 */
	public function get stopAfterActivity():Boolean {
		return _stopAfterActivity;
	}
	
	public function set stopAfterActivity(a:Boolean):Void {
		_stopAfterActivity = a;
	}
	
	public function getDictionaryLabel():String {
		if(isParallelActivity())
			return "pi_parallel_title";
		else if(isOptionalActivity())
			return "opt_activity_title";
		else if(isBranchingActivity() || isSequenceActivity())
			return "pi_activity_type_branching";
		else if(isGroupActivity())
			return "pi_activity_type_grouping";
		else
			return null;
	}
}