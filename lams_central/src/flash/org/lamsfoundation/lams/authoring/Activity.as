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
	public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
	public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
	public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
	public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
	public static var OPTIONAL_ACTIVITY_TYPE:Number = 7;
	public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
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
	
	
	private var _title:String;					//*	
	private var _description:String;			//*
	private var _helpText:String;					
	private var _xCoord:Number;
	private var _yCoord:Number;
	private var _libraryActivityUIImage:String;	
	private var _applyGrouping:Boolean;


	private var _runOffline:Boolean;
	/*
	* these have now been removed, set in the tool content instead
	private var _offlineInstructions:String;
	private var _onlineInstructions:String;
	*/
	private var _defineLater:Boolean;
	private var _createDateTime:Date;

	private var _groupingSupportType:Number; //*
	
	
     
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
		//assign the values:
		
		_activityUIID = activityUIID;
		//set default calues
		_objectType = "Activity"; //should be "Activity"
		_applyGrouping = false;
		_runOffline = false;
		_defineLater = false;
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
		//types.addItem({label: Dictionary.getValue('none_act_lbl'), data: 0});
		types.addItem({label: Dictionary.getValue('synch_act_lbl'), data: SYNCH_GATE_ACTIVITY_TYPE});
		types.addItem({label: Dictionary.getValue('sched_act_lbl'), data: SCHEDULE_GATE_ACTIVITY_TYPE});
		types.addItem({label: Dictionary.getValue('perm_act_lbl'), data: PERMISSION_GATE_ACTIVITY_TYPE});
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
		}else{
			return false;
		}
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
			
	
	

		
	}
	
	public function toData(){
		var dto:Object = new Object();
		//DC - Changed mode of toData to be ommiting fields with undefined values
		/*
		dto.objectType = (_objectType) ?  _objectType : Config.STRING_NULL_VALUE;
		dto.activityTypeID = (_activityTypeID) ?  _activityTypeID : Config.NUMERIC_NULL_VALUE;
		dto.activityID = (_activityID) ?  _activityID : Config.NUMERIC_NULL_VALUE;
		dto.activityCategoryID = (_activityCategoryID) ?  _activityCategoryID : Config.NUMERIC_NULL_VALUE;
		dto.activityUIID = (_activityUIID) ?  _activityUIID : Config.NUMERIC_NULL_VALUE;
		dto.learningLibraryID = (_learningLibraryID) ?  _learningLibraryID : Config.NUMERIC_NULL_VALUE;
		dto.learningDesignID = (_learningDesignID) ?  _learningDesignID : Config.NUMERIC_NULL_VALUE;
		dto.libraryActivityID = (_libraryActivityID) ?  _libraryActivityID : Config.NUMERIC_NULL_VALUE;
		dto.parentActivityID = (_parentActivityID) ?  _parentActivityID : Config.NUMERIC_NULL_VALUE;
		dto.parentUIID = (_parentUIID) ?  _parentUIID : Config.NUMERIC_NULL_VALUE;
		dto.orderID = (_orderID) ?  _orderID : Config.NUMERIC_NULL_VALUE;
		dto.groupingID = (_groupingID) ?  _groupingID : Config.NUMERIC_NULL_VALUE;
		dto.groupingUIID = (_groupingUIID) ?  _groupingUIID : Config.NUMERIC_NULL_VALUE;
		dto.title = (_title) ?  _title : Config.STRING_NULL_VALUE;
		dto.description = (_description) ?  _description : Config.STRING_NULL_VALUE;
		dto.helpText = (_helpText) ?  _helpText : Config.STRING_NULL_VALUE;
		dto.yCoord = (_yCoord) ?  _yCoord : Config.NUMERIC_NULL_VALUE;
		dto.xCoord = (_xCoord) ?  _xCoord : Config.NUMERIC_NULL_VALUE;
		dto.libraryActivityUIImage = (_libraryActivityUIImage) ?  _libraryActivityUIImage : Config.STRING_NULL_VALUE;
		dto.applyGrouping= (_applyGrouping!=null) ?  _applyGrouping : Config.BOOLEAN_NULL_VALUE;
		dto.runOffline= (_runOffline!=null) ?  _runOffline : Config.BOOLEAN_NULL_VALUE;
		//dto.onlineInstructions = (_onlineInstructions) ?  _onlineInstructions: Config.STRING_NULL_VALUE;
		//dto.offlineInstructions = (_offlineInstructions) ?  _offlineInstructions : Config.STRING_NULL_VALUE;
		dto.defineLater= (_defineLater!=null) ?  _defineLater : Config.BOOLEAN_NULL_VALUE;
		dto.createDateTime= (_createDateTime) ?  _createDateTime : Config.DATE_NULL_VALUE;
		//dto.groupingSupportType = (_groupingSupportType) ?  _groupingSupportType : Config.NUMERIC_NULL_VALUE;
		dto.groupingSupportType = 2;
		*/
		if(_activityTypeID){		dto.activityTypeID 		= _activityTypeID;			}
		if(_activityID){			dto.activityID			= _activityID;				}
		if(_activityCategoryID){	dto.activityCategoryID	= _activityCategoryID;		}
		if(_activityUIID){			dto.activityUIID		= _activityUIID;			}
		if(_learningLibraryID){		dto.learningLibraryID	= _learningLibraryID;		}
		if(_learningDesignID){		dto.learningDesignID	= _learningDesignID;		}
		if(_libraryActivityID){		dto.libraryActivityID	= _libraryActivityID;		}
		if(_parentActivityID){		dto.parentActivityID	= _parentActivityID;		}
		if(_parentUIID){			dto.parentUIID			= _parentUIID;				}
		if(_orderID){				dto.orderID				= _orderID;					}
		if(_groupingID){			dto.groupingID			= _groupingID;				}
		if(_groupingUIID){			dto.groupingUIID		= _groupingUIID;			}
		if(_title){					dto.title				= _title;					}
		if(_description){			dto.description			= _description;				}
		if(_helpText){				dto.helpText			= _helpText;				}
		if(_yCoord){				dto.yCoord				= _yCoord;					}
		if(_xCoord){				dto.xCoord				= _xCoord;					}
		if(_libraryActivityUIImage){dto.libraryActivityUIImage= _libraryActivityUIImage;}
		//bnools need to be included - so do as follows:
		dto.applyGrouping = (_applyGrouping==null) ? false : _applyGrouping;
		dto.runOffline = (_runOffline==null) ? false : _runOffline;
		dto.defineLater = (_defineLater==null) ? false : _defineLater;
		if(_createDateTime){		dto.createDateTime		= _createDateTime;			}
		if(_groupingSupportType){	dto.groupingSupportType = _groupingSupportType;		}
		
		
		
		return dto;
	}
	
	public function clone():Activity{
		/*
		var n = new Activity(null);
		//objectType is in the constructor
		n.activityTypeID = _activityTypeID;
		n.activityID = _activityID;
		n.activityCategoryID = _activityCategoryID;
		n.activityUIID = _activityUIID;
		n.learningLibraryID = _learningLibraryID;
		n.learningDesignID = _learningDesignID;
		n.libraryActivityID = _libraryActivityID;
		n.parentActivityID = _parentActivityID;
		n.parentUIID = _parentUIID
		n.orderID = _orderID
		n.groupingID = _groupingID;
		n.groupingUIID = _groupingUIID
		n.title = _title;
		n.description = _description;
		n.helpText =  _helpText;
		n.yCoord = _yCoord;
		n.xCoord = _xCoord;
		n.libraryActivityUIImage = _libraryActivityUIImage;
		n.applyGrouping = _applyGrouping;
		n.runOffline = _runOffline;
		//n.offlineInstructions = _offlineInstructions;
		n.defineLater = _defineLater;
		n.createDateTime = _createDateTime;
		n.groupingSupportType = _groupingSupportType;
		*/
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
		trace('returning:'+_groupingUIID);
		return _groupingUIID;
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

	
	

	

	
}