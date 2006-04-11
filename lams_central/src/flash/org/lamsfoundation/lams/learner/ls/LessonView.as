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

import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.lb.*;

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*;

import mx.managers.*;
import mx.controls.*;
import mx.events.*

/**
* Learner view for the Lesson
* @author Mitchell Seaton
*/
class LessonView extends AbstractView {
	
	private var _className = "LessonView";
	private var _depth:Number;
	
	// Lesson clip
	private var _lesson_mc:MovieClip;
	
	// Lesson labels
	private var _lessonName:Label;
	private var _lessonDescription:Label;
	private var _lessonStateID:Label;
	
	
	// Lesson buttons
	private var select_btn:Button;
	
	private var bkg_pnl:MovieClip;
	
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
	* Also creates a doLater handler for createToolkit
	*/
	public function init(m:Observable, c:Controller){

		//Invoke superconstructor, which sets up MVC relationships.
		super (m, c);
		
		MovieClipUtils.doLater(Proxy.create(this,createLesson));		
	}
	
	/**
	* Sets up the lesson (clip)
	*/
	public function createLesson() {
		trace('creating new Lesson ...');
        setStyles();
		
        _lesson_mc = this;
		_depth = this.getNextHighestDepth();
		
		//Add the button handlers, essentially this is handing on clicked event to controller.
        var controller = getController();
		
		select_btn.addEventListener("click",controller);
		
		select_btn.onPress = Proxy.create(this,this['select']);
		select_btn.onRollOver = Proxy.create(this,this['rollOver']);
		select_btn.onRollOut = Proxy.create(this,this['rollOut']);
		
		
        //Now that view is setup dispatch loaded event
       dispatchEvent({type:'load',target:this});
	   
	}
	
	/*
	* Updates state of the Lesson, called by Lesson Model
	*
	* @param   o   		The model object that is broadcasting an update.
	* @param   infoObj  object with details of changes to model
	*/
	public function update (o:Observable,infoObj:Object):Void {
	    //Cast the generic observable object into the Toolbar model.
        var lm:LessonModel = LessonModel(o);
		trace('getting lesson update...');
        //Update view from info object
        switch (infoObj.updateType) {
            case 'POSITION' :
                setPosition(lm);
                break;
            case 'SIZE' :
                setSize(lm);
                break;
			case 'NAME' :
                setLessonName(lm);
                break;
			case 'DESCRIPTION' :
				setLessonDescription(lm);
				break;
			case 'STATE' :
				setLessonStateID(lm);
				break;
			case 'STATUS' :
				setStatus(lm);
				break;
			case 'DESIGN' :
				break;
			case 'DESIGNMODEL' :
				trace('updating design model for lesson..');
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
        }
	}
    
	
	 /**
    * Sets the size of the Lesson on stage, called from update
    */
	private function setSize(lm:LessonModel):Void{
		
        var s:Object = lm.getSize();
        
		//Size panel
		trace('lesson view  setting width to '+s.w);
		bkg_pnl.setSize(s.w,bkg_pnl._width);
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
	
	private function setLessonName(lm:LessonModel):Void {
		var name:String = lm.getLessonName();
		this._lessonName.text = name;
	}
	
	private function setLessonDescription(lm:LessonModel):Void {
		var desc:String = lm.getLessonDescription();
		this._lessonDescription.text = desc;
	}
	
	private function setLessonStateID(lm:LessonModel):Void {
		var state:Number = lm.getLessonStateID();
		this._lessonStateID.text = "State ID: " + String(state);
	}
	
	private function setStatus(lm:LessonModel):Void {
		var status:Boolean = lm.getStatus();
		trace('inside setStatus method of View');
		// do stuff based on status value
		if(status) {
			trace('joined lesson... lesson is active');
			// show active
		} else {
			// show inactive
			trace('exited lesson... lesson is inactive');
		}
	}
	
	private function select():Void{
		var l:Lesson = this.getModel().getLesson();
		var libraryController = l.getLibrary().getController();
		libraryController.selectLesson(this);			
	}
	
	private function rollOver():Void{
		// mouse over style
		//join_btn.setStyle("backgroundColor",0xFFFFFF);
		//join_btn.setStyle("borderStyle","outset");
	}
	
	private function rollOut():Void{
		// original style
		//join_btn.setStyle("styleName",_styleName);
	}
	 
	
	/**
    * Set the styles for the Lesson
    */
    private function setStyles() {
		// no styles to set 
	}
	
	
	
	/**
	* Gets the LessonModel
	*
	* @returns model 
	*/
	public function getModel():LessonModel{
			return LessonModel(model);
	}
	
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new LessonController(model);
    }
        
}