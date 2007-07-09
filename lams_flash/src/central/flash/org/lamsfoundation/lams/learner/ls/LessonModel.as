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

import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.Progress;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.authoring.Activity;
import org.lamsfoundation.lams.authoring.Transition;

/*
* Model for the Lesson
*/
class LessonModel extends Observable {
	private var _className:String = "LessonModel";
   
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _spadHeight:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	
	private var _lesson:Lesson;
	
	// unique Lesson identifier
	private var _lessonID:Number;
	
	private var ddmActivity_keys:Array;
	private var ddmTransition_keys:Array;
	private var _activitiesDisplayed:Hashtable;
	/**
	* View state data
	*/
	private var _lessonName:String;
	private var _lessonDescription:String;
	private var _lessonStateID:Number;
	private var _learningDesignID:Number;
	private var _learnerExportAvailable:Boolean;
	
	/* the learningDesignModel gets set when you join a lesson */
	private var _learningDesignModel:DesignDataModel;
	
	private var _progressData:Progress;
	private var _currentActivityOpen:Object;
	private var _active:Boolean;
	
	
	/**
	* Constructor.
	*/
	public function LessonModel (lesson:Lesson){
		_lesson = lesson;
		_active = false;
		_learningDesignModel = null;
		_progressData = null;
		_currentActivityOpen = null;
		
		ddmActivity_keys = new Array();
		ddmTransition_keys = new Array();
		_activitiesDisplayed = new Hashtable("_activitiesDisplayed");
	}
	
	public function populateFromDTO(dto:Object){
		_lessonID = dto.lessonID;
		_lessonName = dto.lessonName;
		_lessonDescription = dto.lessonDescription;
		_lessonStateID = dto.lessonStateID;
		_learningDesignID = dto.learningDesignID;
		_learnerExportAvailable = dto.learnerExportAvailable;
		
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "LESSON";
		notifyObservers(infoObj);
	}
	
	
	public function setSpadHeight(h:Number){
		_spadHeight = h
		Application.getInstance().onResize();
	}
	
	public function getSpadHeight(){
		return _spadHeight;
	}
	
	/**
	 * Set Lesson's unique ID
	 * 
	 * @param   lessonID 
	 */
	
	public function setLessonID(lessonID:Number){
		_lessonID = lessonID;
	}
	
	/**
	 * Get Lesson's unique ID
	 *   
	 * @return  Lesson ID
	 */
	
	public function getLessonID():Number {
		return _lessonID;
	}
	
	public function get ID():Number{
		return _lessonID;
	}
    
	/**
	 * Set the lesson's name
	 * 
	 * @param   lessonName 
	 */
	
	public function setLessonName(lessonName:String){
		_lessonName = lessonName;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "NAME";
		notifyObservers(infoObj);
	}
	
	/**
	 * Get the lesson's name
	 * 
	 * @return Lesson Name
	 */
	
	public function getLessonName():String {
		return _lessonName;
	}
	
	public function get name():String{
		return _lessonName;
	}
	
	/**
	 * Set the lesson's description
	 *
	 * @param   lessonDescription  
	 */
	
	public function setLessonDescription(lessonDescription:String){
		_lessonDescription = lessonDescription;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "DESCRIPTION";
		notifyObservers(infoObj);
	}
	
	/**
	 * Get the lesson's description
	 * 
	 * @return  lesson description
	 */
	public function getLessonDescription():String {
		return _lessonDescription;
	}
	
	public function get description():String{
		return _lessonDescription;
	}
	
	public function setLessonStateID(lessonStateID:Number) {
		_lessonStateID = lessonStateID;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "STATE";
		notifyObservers(infoObj);
	}
	
	public function getLessonStateID():Number {
		return _lessonStateID;
	}
	
	public function get stateID():Number{
		return _lessonStateID;
	}
	
	public function setLearningDesignID(learningDesignID:Number){
		_learningDesignID = learningDesignID;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "DESIGN";
		notifyObservers(infoObj);
	}
	
	public function getLearningDesignID():Number{
		return _learningDesignID;
	}
	
	public function get learningDesignID():Number{
		return _learningDesignID;
	}
	
	public function setLearningDesignModel(learningDesignModel:DesignDataModel){
		_learningDesignModel = learningDesignModel;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "DESIGNMODEL";
		notifyObservers(infoObj);
	}
	
	public function set learnerExportAvailable(b:Boolean) {
		_learnerExportAvailable = b;
	}
	
	public function get learnerExportAvailable():Boolean {
		return _learnerExportAvailable;
	}
	
	public function getLearningDesignModel():DesignDataModel{
		return _learningDesignModel;
	}
	
	public function get learningDesignModel():DesignDataModel{
		return _learningDesignModel;
	}
	
	public function setProgressData(progressData:Progress){
		_progressData = progressData;
		
		setChanged();
		
		// send update
		infoObj = {};
		infoObj.updateType = "PROGRESS";
		notifyObservers(infoObj);
	}
	
	public function getProgressData():Progress{
		return _progressData;
	}
	
	public function get progressData():Progress{
		return _progressData;
	}
	
	public function setActive() {
		_active = true;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "STATUS";
		notifyObservers(infoObj);
	}
	
	public function setInactive() {
		_active = false;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "STATUS";
		notifyObservers(infoObj);
	}
	
	public function getStatus():Boolean {
		return _active;
	}
	
	private function orderDesign(activity:Activity, order:Array):Void{
		order.push(activity);
		
		for(var i=0;i<ddmTransition_keys.length;i++){
			var transitionKeyToCheck:Number = ddmTransition_keys[i];
			var ddmTransition:Transition = learningDesignModel.transitions.get(transitionKeyToCheck);
			
				if (ddmTransition.fromUIID == activity.activityUIID){
					var ddm_activity:Activity = learningDesignModel.activities.get(ddmTransition.toUIID);
					orderDesign(ddm_activity, order);
				}
				
		}
		
	}
	
	private function setDesignOrder(){
		trace("set Design order called")
		ddmActivity_keys = learningDesignModel.activities.keys();
		ddmTransition_keys = learningDesignModel.transitions.keys();
		
		var orderedActivityArr:Array = new Array();
		var trIndexArray:Array;
		var dataObj:Object;
		var ddmfirstActivity_key:Number = learningDesignModel.firstActivityID;
		var learnerFirstActivity:Activity = learningDesignModel.activities.get(ddmfirstActivity_key);

		// recursive method to order design
		orderDesign(learnerFirstActivity, orderedActivityArr);
		return orderedActivityArr;
		
	}
	
	public function setCurrentActivityOpen(ca:Object){
		
		if(_currentActivityOpen != null && ca != null){
			 setChanged();
		
			//send an update
			infoObj = {};
			infoObj.updateType = "CLOSE_COMPLEX_ACTIVITY";
			infoObj.data = _currentActivityOpen;
			notifyObservers(infoObj);
		}
		
		_currentActivityOpen = ca;
		
	}
	
	public function getCurrentActivityOpen():Object{
		return _currentActivityOpen;
	}
	
	/**
	 * get the design in the DesignDataModel and update the Monitor Model accordingly.
	 * NOTE: Design elements are added to the DDM here.
	 * 
	 * @usage   
	 * @return  
	 */
	public function drawDesign(){
		var indexArray:Array = setDesignOrder();
		
		//go through the design and get the activities and transitions 
		var dataObj:Object;
		ddmActivity_keys = learningDesignModel.activities.keys();
		
		//loop through 
		for(var i=0;i<indexArray.length;i++){
					
			var keyToCheck:Number = indexArray[i].activityUIID;
			var ddm_activity:Activity = learningDesignModel.activities.get(keyToCheck);
			
			if(ddm_activity.parentActivityID > 0 || ddm_activity.parentUIID > 0){
				return;
			}else {
				broadcastViewUpdate("DRAW_ACTIVITY",ddm_activity);
			}
		}

	}
	
	public function updateDesign(){
		var indexArray:Array = setDesignOrder();
		
		//go through the design and get the activities and transitions 
		var dataObj:Object;
		ddmActivity_keys = learningDesignModel.activities.keys();

		//loop through 
		for(var i=0;i<indexArray.length;i++){
					
			var keyToCheck:Number = indexArray[i].activityUIID;
			
			var ddm_activity:Activity = learningDesignModel.activities.get(keyToCheck);

			if(ddm_activity.parentActivityID > 0 || ddm_activity.parentUIID > 0){
				return;
			} else {
				broadcastViewUpdate("UPDATE_ACTIVITY",ddm_activity);
			}
		}
	}
	
	public function broadcastViewUpdate(updateType, data){
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = updateType;
		infoObj.data = data;
		notifyObservers(infoObj);
		
	}
	
	/**
    * set the size on the model, this in turn will set a changed flag and notify observers (views)
    * @param width - Tookit width
    * @param height - Toolkit height
    */
    public function setSize(width:Number,height:Number) {
		__width = width;
		__height = height;
		
		setChanged();
		
		//send an update
		infoObj = {};
		infoObj.updateType = "SIZE";
		notifyObservers(infoObj);
    }
    
	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getSize():Object{
		var s:Object = {};
		s.w = __width;
		s.h = __height;
		return s;
	}  
    
	/**
    * sets the model x + y vars
	*/
	public function setPosition(x:Number,y:Number):Void{
        //Set state variables
		__x = x;
		__y = y;
		
        //Set flag for notify observers
		setChanged();
        
		//build and send update object
		infoObj = {};
		infoObj.updateType = "POSITION";
		notifyObservers(infoObj);
	}  

	/**
	* Used by View to get the size
	* @returns Object containing width(w) & height(h).  obj.w & obj.h
	*/
	public function getPosition():Object{
		var p:Object = {};
		p.x = x;
		p.y = y;
		return p;
	}  
    
	public function get activitiesDisplayed():Hashtable{
		return _activitiesDisplayed;
	}
	
	public function getActivityKeys():Array{
		return ddmActivity_keys;
	}
	
    //Accessors for x + y coordinates
    public function get x():Number{
        return __x;
    }
    
    public function get y():Number{
        return __y;
    }

    //Accessors for x + y coordinates
    public function get width():Number{
        return __width;
    }
    
    public function get height():Number{
        return __height;
    }
	function get className():String{
        return 'LessonModel';
    }
	
	/**
	 * 
	 * @return the Lesson  
	 */
	public function getLesson():Lesson{
		return _lesson;
	}
	

}