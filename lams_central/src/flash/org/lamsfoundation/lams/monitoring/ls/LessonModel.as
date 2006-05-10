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

import org.lamsfoundation.lams.monitoring.ls.*;
//import org.lamsfoundation.lams.monitoring.ls.Lesson;
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.monitoring.Application;


/*
* Model for the Monitoring Lessons Kit 
*/
class org.lamsfoundation.lams.monitoring.ls.LessonModel extends Observable {
	private var _className:String = "LessonModel";
   
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	
	private var _lesson:Lesson;
	
	/**
	* Sequence (Lesson) Lesson container
	* 
	*/
	
	private var _lessonSequences:Hashtable;
	
	/**
	* View state data
	*/
	private var _currentlySelectedSequence:Sequence;
	private var _lastSelectedSequence:Sequence;
	/**
	* Constructor.
	*/
	public function LessonModel (lesson:Lesson){
		_lesson = lesson;
		_lessonSequences = new Hashtable();
	}
	
	/**
	* Gets sequence (lesson) library data
	*/
	public function getLessonSequences():Hashtable{
		return _lessonSequences;
	}
	
	
	public function setLessonSequences(lsc:Array):Boolean {
		trace('add learning seqs to map...');
		//clear the old lot
		_lessonSequences.clear();
		
		trace('adding learning seq for length' + lsc.length);
		for(var i=0; i<lsc.length;i++){
			trace('adding learning seq with ID: ' + lsc[i].getSequenceID());
			
			_lessonSequences.put(lsc[i].getSequenceID(),lsc[i]);
		}
		
		Debugger.log('Added '+lsc.length+' Sequences to _lessonSequences',4,'setLessonSequences','LessonModel');
		
		setChanged();
		
		//notify the view there has been a change
		infoObj = {};
		infoObj.updateType = "SEQUENCES_UPDATED";

		notifyObservers(infoObj);
		
		return true;
	}
	
	/**
	 * Gets a learning sequence (lesson) using its ID
	 * @usage   
	 * @param   lessonID
	 * @return  
	 */
	public function getLessonSequence(lessonID:Number):Object{
		return _lessonSequences.get(lessonID);
	}
	
	
	/**
	* Sets currently selected Sequence
	*/
	public function setLastLessonSequence(seq:Sequence):Void{
		
		_lastSelectedSequence = _currentlySelectedSequence;
		_currentlySelectedSequence = seq;
	}
	
	/**
	* Gets currecntly selected Sequence
	*/
	public function getLastLessonSequence():Sequence{
		return _currentlySelectedSequence;
	}
	
	
	/**
	public function getNewSequences():Array{
		if(_learningSequences==null){
			return null;
		}
		
		return getSequencesByState(NEW_STATE_ID);
	}
	
	public function getStartedSequences():Array{
		if(_learningSequences==null){
			return null;
		}
		
		return getSequencesByState(STARTED_STATE_ID);
	}
	
	public function getFinishedSequences():Array{
		if(_learningSequences==null){
			return null;
		}
		
		return getSequencesByState(FINISHED_STATE_ID);
	}
	*/
	/**
	 * Retrieve the Learning Sequences for a specific state
	 * 
	 * @param   stateID The state of the sequences to return
	 * @return  sequences
	 
	
	private function getSequencesByState(stateID:Number):Array{
		var seqs:Array = new Array();
		var keys:Array = _learningSequences.keys();
		
		for(var i=0; i<keys.length;i++){
			var seq:Object = _learningSequences.get(keys[i]);
			var l:Lesson = seq.classInstanceRefs;
			if(l.checkState(stateID)){
				seqs.push(l);
			}
		}
		
		return seqs;
	}
	*/
	/**
	* Gets last selected Lesson
	
	public function getLastSelectedLesson():Lesson{
		return _lastSelectedLesson;
	}
	
	
	public function addLessonToLibrary(LessonDTO:Object):Boolean {
		
		return true;
	}
	
	private function createLessonFromDTO(LessonDTO:Object):Lesson {
		// convert to DTO to Lesson object
		return null;
	}
	
	public function addLesson(lesson:Lesson):Boolean {
		_learningSequences.put(lesson.getLessonID(), lesson);
		return true;
	}
	
	public function removeLesson(lesson:Lesson):Boolean {
		_learningSequences.remove(lesson);
		return true;
	}
	
	public function getLesson(lessonID:Number):Lesson {
		return Lesson(_learningSequences.get(lessonID));
	}
	*/
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
	
	public function get className():String{
        return 'LessonModel';
    }
	
	public function getLesson():Lesson{
		return _lesson;
	}
	
}