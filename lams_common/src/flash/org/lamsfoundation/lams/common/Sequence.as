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

import org.lamsfoundation.lams.common.*;

import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.authoring.DesignDataModel;


/*
* Model for the Sequence
*/
class Sequence {
	private var _className:String = "Sequence";
	
	private static var _instance:Sequence = null;
	
	public static var FINISHED_STATE_ID:Number = 5;
	/**
	* View state data
	*/
	private var _seqName:String;
	private var _seqDescription:String;
	private var _seqStateID:Number;
	private var _seqID:Number;
	
	private var _seqCreatedDate:Date;
	private var _seqStartDate:Date;
	
	private var _learningDesignID:Number;
	private var _organisationID:Number;
	private var _learningDesignModel:DesignDataModel;
	
	private var _progress:Progress;
	
	private var _active:Boolean;
	
	
	/**
	* Constructor.
	*/
	public function Sequence (){
		_active = false;
		_learningDesignModel = null;
		_progress = null;
	}
	
	/**
	 * 
	 * @return the Sequence 
	 */
	public static function getInstance():Sequence{
		if(Sequence._instance == null){
            Sequence._instance = new Sequence();
        }
        return Sequence._instance;
	}
	
	public function populateFromDTO(dto:Object){
		trace('populating seq object for seq:' + dto.lessonID);
		_seqID = dto.lessonID;
		_seqName = dto.lessonName;
		_seqDescription = dto.lessonDescription;
		_seqStateID = dto.lessonStateID;
		_learningDesignID = dto.learningDesignID;
		_seqCreatedDate = dto.createDateTime;
		_seqStartDate = dto.startDateTime;
		_organisationID = dto.organisationID;
	}
	
	
	/**
	 * Set seq's unique ID
	 * 
	 * @param   seqID 
	 */
	
	public function setSequenceID(seqID:Number){
		_seqID = seqID;
	}
	
	/**
	 * Get Sequence's unique ID
	 *   
	 * @return  Sequence ID
	 */
	
	public function getSequenceID():Number {
		return _seqID;
	}

    /**
	 * Set User Organisation ID
	 * 
	 * @param   organisationIDID 
	 */
	
	public function setOrganisationID(organisationID:Number){
		_organisationID = organisationID;
	}
	
	/**
	 * Get User Organisation ID
	 *   
	 * @return  Organisation ID
	 */
	
	public function getOrganisationID():Number {
		return _organisationID;
	}
    

	/**
	 * Set the seq's name
	 * 
	 * @param   seqName 
	 */
	
	public function setSequenceName(seqName:String){
		_seqName = seqName;
		
	}
	
	/**
	 * Get the seq's name
	 * 
	 * @return Sequence Name
	 */
	
	public function getSequenceName():String {
		return _seqName;
	}
	
	/**
	 * Set the seq's description
	 *
	 * @param   seqDescription  
	 */
	
	public function setSequenceDescription(seqDescription:String){
		_seqDescription = seqDescription;
		
	}
	
	/**
	 * Get the seq's description
	 * 
	 * @return  seq description
	 */
	public function getSequenceDescription():String {
		return _seqDescription;
	}
	
	public function setSequenceStateID(seqStateID:Number) {
		_seqStateID = seqStateID;
		
	}
	
	public function getSequenceStateID():Number {
		return _seqStateID;
	}
	
	public function setLearningDesignID(learningDesignID:Number){
		_learningDesignID = learningDesignID;
		
	}
	
	public function getLearningDesignID():Number{
		return _learningDesignID;
	}
	
	public function setLearningDesignModel(learningDesignModel:DesignDataModel){
		_learningDesignModel = learningDesignModel;
		
	}
	
	public function getLearningDesignModel():DesignDataModel{
		return _learningDesignModel;
	}
	
	public function setCreateDateTime(seqCreatedDate:Date){
		_seqCreatedDate = seqCreatedDate;
	}
	
	public function getCreateDateTime():Date{
		return _seqCreatedDate;
	}
	
	public function setStartDateTime(seqStartDate:Date){
		_seqStartDate = seqStartDate;
	}
	
	public function getStartDateTime():String{
		var mytest:String = (_seqStartDate.getDate()+" "+(StringUtils.getMonthAsString(_seqStartDate.getMonth()+1))+" "+_seqStartDate.getFullYear());;
		return mytest;
		//return _seqStartDate;
	}
	
	public function setActive() {
		_active = true;
		trace('setting seq active...');
		
	}
	
	public function setInactive() {
		_active = false;
		trace('setting seq inactive...');
		
	}
	
	public function getStatus():Boolean {
		return _active;
	}
	
	public function setProgress(progress:Progress){
		_progress = progress;
	}
	
	public function getProgress():Progress{
		return _progress;
	}

	public function checkState(stateID:Number):Boolean {
		if(getSequenceStateID()==stateID){
			return true
		} else {
			return false;
		}
	}
	
	public function isFinished():Boolean {
		return checkState(FINISHED_STATE_ID);
	}

	function get className():String{
        return 'Sequence';
    }
	
	
	

}