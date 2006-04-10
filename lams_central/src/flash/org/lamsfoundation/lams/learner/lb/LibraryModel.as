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

import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.learner.ls.Lesson;
import org.lamsfoundation.lams.common.util.Observable;
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.learner.Application;


/*
* Model for the Library
*/
class LibraryModel extends Observable {
	private var _className:String = "LibraryModel";
   
	private var __width:Number;
	private var __height:Number;
	private var __x:Number;
	private var __y:Number;
	private var _isDirty:Boolean;
	private var infoObj:Object;
	
	private var _library:Library;
	
	/**
	* View state data
	*/
	private var _currentlySelectedLesson:Lesson;
	private var _lastSelectedLesson:Lesson;
	
	/**
	* Sequence (Lesson) Library container
	* 
	*/
	private var _learningSequences:Hashtable;

	
	/**
	* Constructor.
	*/
	public function LibraryModel (library:Library){
		_library = library;
		_learningSequences = new Hashtable();
	}
	
	public function setLearningSequences(lsc:Array):Boolean {
		trace('add learning seqs to map...');
		//clear the old lot
		_learningSequences.clear();
		
		for(var i=0; i<lsc.length;i++){
			trace('adding learning seq ' + lsc[i].getLessonID());
			_learningSequences.put(lsc[i].getLessonID(),lsc[i]);
		}
		
		Debugger.log('Added '+lsc.length+' Sequences to _learningSequences',4,'setLearningSequences','LibraryModel');
		
		setChanged();
		
		//notify the view there has been a change
		infoObj = {};
		infoObj.updateType = "SEQUENCES_UPDATED";

		notifyObservers(infoObj);
		
		return true;
	}
	
	/**
	* Gets sequence (lesson) library data
	*/
	public function getLearningSequences():Hashtable{
		return _learningSequences;
	}
	
	/**
	 * Gets a learning sequence (lesson) using its ID
	 * @usage   
	 * @param   lessonID
	 * @return  
	 */
	public function getLearningSequence(lessonID:Number):Object{
		return _learningSequences.get(lessonID);
	}
	
	/**
	* Sets currently selected Lesson
	*/
	public function setSelectedLesson(lesson:Lesson):Void{
		//Debugger.log('templateActivity:'+templateActivity,4,'setSelectedTemplateActivity','ToolkitModel');
		
		
		//_global.breakpoint();
		//set the sates
		_lastSelectedLesson = _currentlySelectedLesson;
		_currentlySelectedLesson = lesson;
		
		// exit current lesson and join selected lesson
		
		if(_lastSelectedLesson != null)
			_lastSelectedLesson.exitLesson();
			
		if(_currentlySelectedLesson != null)
			_currentlySelectedLesson.joinLesson();
		
		
		//for observer thang
		setChanged();
		//send an update
		infoObj = {};
		infoObj.updateType = "LESSON_SELECTED";
		notifyObservers(infoObj);
	}
	
	/**
	* Gets currecntly selected Lesson
	*/
	public function getSelectedLesson():Lesson{
		return _currentlySelectedLesson;
	}
	
	/**
	* Gets last selected Lesson
	*/
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
        return 'LibraryModel';
    }
	
	public function getLibrary():Library{
		return _library;
	}
	
}