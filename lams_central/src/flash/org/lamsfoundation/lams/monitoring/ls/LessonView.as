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
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.monitoring.*;
import org.lamsfoundation.lams.monitoring.ls.*;
//import org.lamsfoundation.lams.learner.ls.*;
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
class org.lamsfoundation.lams.monitoring.ls.LessonView extends AbstractView {
	
	private var _className = "LessonView";
	private var _depth:Number;
	
	private var bkg_pnl:MovieClip;
	
	private var _lesson_mc:MovieClip;
	
	private var lessonState_acc:MovieClip;
	private var lsns_Active:MovieClip;
	private var lsns_Archive:MovieClip;
	private var lsns_Disabled:MovieClip;
	private var lsViewExist:Boolean 
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;    

	/**
	* Constructor
	*/
	public function LessonView(){	
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
		lsViewExist = false;
		trace('Initiating lesson view...');
		this.onEnterFrame = createLessons;
	}
	
	/**
	* Sets up the Library (clip)
	*/
	public function createLessons() {
		trace('creating Lessons View');
		
		delete this.onEnterFrame;
		_lesson_mc = this;
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
        var lm:LessonModel = LessonModel(o);
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
			case 'LESSON_SELECTED' :
				updateSelectedLesson(o);
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

		//get a reference to Observed LessonModel
		var lbm = LessonModel(o);
		
		//get the hashtable
		var mySeqs:Hashtable = lbm.getLessonSequences();
		if (!lsViewExist){
			lessonState_acc.createChild("View", "active", {label: "Active"});
			lessonState_acc.createChild("View", "disabled", {label: "Disabled"});
			lessonState_acc.createChild("View", "archive", {label: "Archive"});
		
			lsns_Active = lessonState_acc.active.createChild("DataGrid", "Data_dtg");
			lsns_Active.setSize(lessonState_acc._width, lessonState_acc._height-63);
			lsns_Archive = lessonState_acc.archive.createChild("DataGrid", "Data_dtg");
			lsns_Archive.setSize(lessonState_acc._width, lessonState_acc._height-63);
			lsns_Disabled = lessonState_acc.disabled.createChild("DataGrid", "Data_dtg");
			lsns_Disabled.setSize(lessonState_acc._width, lessonState_acc._height-63);
			var lvc = getController();
			lsns_Active.addEventListener("cellPress",lvc);
			lsViewExist = true;
		}
		lsns_Active.removeAll(); 
		lsns_Archive.removeAll();
		lsns_Disabled.removeAll();
		//loop through the sequences
		var keys:Array = mySeqs.keys();
		trace("Length of Keys: "+keys.length)
		for(var i=0; i<keys.length; i++){
			
			//trace('attaching lesson movie for id: ' + keys[i]);
			var learningSeq:Object = mySeqs.get(keys[i]);
			
			var anObject= {Lesson:learningSeq.getSequenceName(), Started:learningSeq.getStartDateTime(), data:learningSeq.getSequenceID()};
			
			// Organize Sequences based on their StateID "7" for DISABLED, "6" for ARCHIVED and all the rest of them for ACTIVE.
			switch (learningSeq.getSequenceStateID().toString()) {
                case '6' :
                    lsns_Archive.addItem(anObject);
					break;
				case '7' :
					lsns_Disabled.addItem(anObject);
                    break;
                default:
					lsns_Active.addItem(anObject);
					lsns_Active.removeColumnAt(2);
            }
			
		}
		
		
		//lsns_Active.onRelease = Proxy.create(this,this['select']);
		 //Now that view is setup dispatch loaded event
       //dispatchEvent({type:'load',target:this});
		
	}
	
	
	/**
	*The currently selected Lesson
	* 
	* @param   o   		The model object that is broadcasting an update.
	*/
	private function updateSelectedLesson(o:Observable):Void{

		//get the model
		var lm = LessonModel(o);
		
		//set the states of Lessons
		//var l = lm.getLastSelectedLesson();
		//l.setInactive();
		//var c = lm.getSelectedLesson();
		//c.setActive();
		
		
	}
	
	 /**
    * Sets the size of the Toolbar on stage, called from update
    */
	private function setSize(lm:LessonModel):Void{
		
        var s:Object = lm.getSize();
		var p:Object = lm.getPosition();
		//Size panel
		trace('lesson view  setting width to '+s.w);
		//bkg_pnl.setSize(s.w,bkg_pnl._width);
		
		       //Panel
        bkg_pnl.setSize(bkg_pnl._width,s.h);
        
        //Calculate Accordion size
        var accWidth:Number = bkg_pnl._width;
        var accHeight:Number = bkg_pnl._height;
        
		lessonState_acc.setSize(accWidth, accHeight);
		lsns_Active.setSize(accWidth, accHeight-63);
		lsns_Archive.setSize(accWidth, accHeight-63);
		lsns_Disabled.setSize(accWidth, accHeight-63);
             
	}
	
    /**
    * Sets the position of the Lesson on stage, called from update
    * @param lm Lesson model object 
    */
	private function setPosition(lm:LessonModel):Void{
        var p:Object = lm.getPosition();
        this._x = p.x;
        this._y = p.y;
	}
	
	/**
	* Gets the LibraryModel
	*
	* @returns model 
	*/
	public function getModel():LessonModel{
			return LessonModel(model);
	}

	public function getController():LessonController{
		var l:Controller = super.getController();
		return LessonController(l);
	}
    /**
    * Returns the default controller for this view (LessonController).
	* Overrides AbstractView.defaultController()
    */
    public function defaultController (model:Observable):Controller {
        return new LessonController(model);
    }
}