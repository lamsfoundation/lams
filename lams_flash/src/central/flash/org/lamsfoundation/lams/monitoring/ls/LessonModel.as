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
		
		for(var i=0; i<lsc.length;i++){
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
	
	public function addNewSequence(seqDTO:Object):Boolean{
		// create new Sequence from DTO
		var seq:Sequence = new Sequence(seqDTO);
		
		_lessonSequences.put(seq.getSequenceID(),seq);
		
		Application.getInstance().getMonitor().getMM().setSequence(seq);
		
		Debugger.log('Added New Sequence to _lessonSequences with ID: ' + seq.getSequenceID(),4,'addNewSequence','LessonModel');
		
		
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