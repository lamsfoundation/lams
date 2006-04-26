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
import org.lamsfoundation.lams.common.Sequence;
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
	private var _currentlySelectedSequence:Sequence;
	private var _lastSelectedSequence:Sequence;
	
	/**
	* Sequence (Sequence) Library container
	* 
	*/
	private var _learningSequences:Hashtable;

	/**
	* constants
	*/
	public static var CREATE_STATE_ID:Number = 1;
	public static var NEW_STATE_ID:Number = 2;
	public static var STARTED_STATE_ID:Number = 3;
	public static var SUSPENDED_STATE_ID:Number = 4;
	public static var FINISHED_STATE_ID:Number = 5;
	public static var ARCHIVED_STATE_ID:Number = 6;
	public static var REMOVED_STATE_ID:Number = 7;
	
	/**
	* Constructor.
	*/
	public function LibraryModel (library:Library){
		trace('new lib model created...');
		_library = library;
		_learningSequences = new Hashtable();
	}
	
	public function setLearningSequences(lsc:Array):Boolean {
		trace('add learning seqs to map...');
		//clear the old lot
		_learningSequences.clear();
		
		for(var i=0; i<lsc.length;i++){
			trace('adding learning seq ' + lsc[i].getSequenceID());
			_learningSequences.put(lsc[i].getSequenceID(),lsc[i]);
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
	* Gets sequence (seq) library data
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
	public function getLearningSequence(seqID:Number):Object{
		return _learningSequences.get(seqID);
	}
	
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
	
	/**
	 * Retrieve the Learning Sequences for a specific state
	 * 
	 * @param   stateID The state of the sequences to return
	 * @return  sequences
	 */
	
	private function getSequencesByState(stateID:Number):Array{
		var seqs:Array = new Array();
		var keys:Array = _learningSequences.keys();
		
		for(var i=0; i<keys.length;i++){
			var seq:Object = _learningSequences.get(keys[i]);
			//var s:Sequence = seq.classInstanceRefs;
			if(seq.checkState(stateID)){
				seqs.push(seq);
			}
		}
		
		return seqs;
	}
	
	/**
	* Sets currently selected Sequence
	*/
	public function setSelectedSequence(seq:Sequence):Void{
		//Debugger.log('templateActivity:'+templateActivity,4,'setSelectedTemplateActivity','ToolkitModel');
		
		
		//_global.breakpoint();
		//set the sates
		_lastSelectedSequence = _currentlySelectedSequence;
		_currentlySelectedSequence = seq;
		
		// exit current seq and join selected seq
		
		if(_lastSelectedSequence != null) {
			getLibrary().exitSequence(_lastSelectedSequence);
			_lastSelectedSequence.setInactive();
		}	
		if(_currentlySelectedSequence != null) {
			getLibrary().joinSequence(_currentlySelectedSequence);
			_currentlySelectedSequence.setActive();
		}
		
		//for observer thang
		setChanged();
		//send an update
		infoObj = {};
		infoObj.updateType = "SEQ_SELECTED";
		notifyObservers(infoObj);
	}
	
	/**
	* Gets currecntly selected Sequence
	*/
	public function getSelectedSequence():Sequence{
		return _currentlySelectedSequence;
	}
	
	/**
	* Gets last selected Sequence
	*/
	public function getLastSelectedSequence():Sequence{
		return _lastSelectedSequence;
	}
	
	/**
	 * Add sequence to the Library
	 * 
	 * @param   seq 
	 * @return  
	 */
	
	public function addSequence(seq:Sequence):Boolean {
		_learningSequences.put(seq.getSequenceID(), seq);
		return true;
	}
	
	/**
	 * Remove sequence from the Library
	 *   
	 * @param   seq 
	 * @return  
	 */
	
	public function removeSequence(seq:Sequence):Boolean {
		_learningSequences.remove(seq);
		return true;
	}
	
	/**
	 * Retrieve a Sequence from the Library
	 * 
	 * @param   seqID 
	 * @return  
	 */
	
	public function getSequence(seqID:Number):Sequence {
		return Sequence(_learningSequences.get(seqID));
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