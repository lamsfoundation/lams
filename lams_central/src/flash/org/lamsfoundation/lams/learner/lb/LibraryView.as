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
import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*;

import mx.managers.*;
import mx.controls.*;
import mx.events.*

/**
* Learner view for the Sequences (Lesson) Library
* @author Mitchell Seaton
*/
class LibraryView extends AbstractView {
	
	private var _className = "LibraryView";
	private var _depth:Number;
	
	private var bkg_pnl:MovieClip;
	
	private var _library_mc:MovieClip;
	
	private var seqState_acc:MovieClip;
	private var lsns:MovieClip;
	
	//private var learningSequences_sp:MovieClip;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;    

	/**
	* Constructor
	*/
	public function LibraryView(){	
        //Set up this class to use the Flash event delegation model
        EventDispatcher.initialize(this);     
	}
	
	/**
	* Initialisation - sets up the mvc relations ship Abstract view.
	* 
	*/
	public function init(m:Observable, c:Controller){
		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		trace('Initiating library view...');
		this.onEnterFrame = createLibrary;
	}
	
	/**
	* Sets up the Library (clip)
	*/
	public function createLibrary() {
		trace('creating Library View');
		
		delete this.onEnterFrame;
		_library_mc = this;
		_depth = this.getNextHighestDepth();
		
		//Add the button handlers, essentially this is handing on clicked event to controller.
        //var controller = getController();
		//controller.getActiveLessons();
		
		 //Now that view is setup dispatch loaded event
		dispatchEvent({type:'load',target:this});
	   
	}
	
	/*
	* Updates state of the Library, called by Library Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
	    //Cast the generic observable object into the Toolbar model.
        var lm:LibraryModel = LibraryModel(o);
			//Update view from info object
        switch (infoObj.updateType) {
            case 'POSITION' :
                setPosition(lm);
                break;
            case 'SIZE' :
                setSize(lm);
                break;
			case 'SEQUENCES_UPDATED' :
                updateSequences(o);
                break;
			case 'SEQ_SELECTED' :
				updateSelectedSequence(o);
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LibraryView');
        }
	}
	
	/**
	* Updates learning sequence library.  Creates a Lesson mc / class for each one.
	* NOTE: Each library element contains a single lesson object.
	*
	* @param   o   		The model object that is broadcasting an update.
	*/
	private function updateSequences(o:Observable){
		
		seqState_acc.createChild("View", "started", {label: "Started sequences"});
		seqState_acc.createChild("View", "finished", {label: "Finished sequences"});
		
		//set SP the content path:
		//learningSequences_sp.contentPath = "empty_mc";
		var controller = getController();
		
		var lbv = LibraryView(this);
		var lbm = LibraryModel(o);
		
		//get the hashtable
		var myStartedSeqs:Array = lbm.getStartedSequences();
		var myFinishedSeqs:Array = lbm.getFinishedSequences();
		
		//loop through the sequences
		//var keys:Array = mySeqs.keys();
		lsns = seqState_acc.started.createChild("DataGrid", "Data_dtg");
		lsns.setSize(seqState_acc.width, seqState_acc.height-43);
		lsns.addEventListener("cellPress", controller);
		
		trace('doing started seq list');
		for(var i=0; i<myStartedSeqs.length; i++){
			
			trace('attaching sequence');
			
			var learningSeq:Object = myStartedSeqs[i];
			
			//NOW we pass in the Lesson instance
			//var _seq:Sequence = learningSeq.classInstanceRefs;
			
			var seqObject = {Lesson:learningSeq.getSequenceName(), ID:learningSeq.getSequenceID(), Status:learningSeq.getSequenceStateID()};
			lsns.addItem(seqObject);
						
			
		}
		
		lsns = seqState_acc.finished.createChild("DataGrid", "Data_dtg");
		lsns.setSize(seqState_acc.width, seqState_acc.height-43);
		lsns.addEventListener("cellPress", controller);
		
		trace('doing finished seq list');
		for(var i=0; i<myFinishedSeqs.length; i++){
			
			trace('attaching sequence');
			
			var learningSeq:Object = myFinishedSeqs[i];
			
			//NOW we pass in the Lesson instance
			//var _seq:Sequence = learningSeq.classInstanceRefs;
			
			var seqObject = {Lesson:learningSeq.getSequenceName(), ID:learningSeq.getSequenceID(), Status:learningSeq.getSequenceStateID()};
			lsns.addItem(seqObject);
						
			
		}
		
	}
	
	
	/**
	*The currently selected Lesson
	* 
	* @param   o   		The model object that is broadcasting an update.
	*/
	private function updateSelectedSequence(o:Observable):Void{

		// view changes - based on active/inactive setting
		
		
	}
	
	 /**
    * Sets the size of the Toolbar on stage, called from update
    */
	private function setSize(lm:LibraryModel):Void{
		
        var s:Object = lm.getSize();
        
		//Size panel
		trace('lesson view  setting width to '+s.w);
		bkg_pnl.setSize(s.w,bkg_pnl._width);
	}
	
    /**
    * Sets the position of the Lesson on stage, called from update
    * @param lm Lesson model object 
    */
	private function setPosition(lm:LibraryModel):Void{
        var p:Object = lm.getPosition();
        this._x = p.x;
        this._y = p.y;
	}
	
	/**
	* Gets the LibraryModel
	*
	* @returns model 
	*/
	public function getModel():LibraryModel{
			return LibraryModel(model);
	}
/*
	public function getScrollPane():MovieClip {
		return learningSequences_sp;
	}
*/
    /**
    * Returns the default controller for this view (LibraryController).
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model:Observable):Controller {
        return new LibraryController(model);
    }
}