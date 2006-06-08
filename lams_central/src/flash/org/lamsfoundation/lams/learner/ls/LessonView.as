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

import org.lamsfoundation.lams.authoring.Activity;

import mx.managers.*;
import mx.controls.*;
import mx.events.*

/**
* Learner view for the Lesson
* @author Mitchell Seaton
*/
class LessonView extends AbstractView {
	
	private static var ACTIVITY_OFFSET = 65;
	private var _className = "LessonView";
	private var _depth:Number;
	
	// Lesson clip
	private var _lesson_mc:MovieClip;
	
	private var bkg_pnl:MovieClip;
	private var _lessonName:Label;
	private var progress_scp:MovieClip;
	private var _activityList:Array;
	
	private var ACT_X:Number = -32.5;
	private var ACT_Y:Number = 32.5;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;     
    public var addEventListener:Function;
    public var removeEventListener:Function;    


	/**
	* Constructor
	*/
	public function LessonView(){
		_activityList = new Array();
		
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
			case 'STATUS' :
				removeAll(lm);
				break;
			case 'DRAW_ACTIVITY' :
				drawActivity(infoObj.data, lm);
				break;
			case 'REMOVE_ACTIVITY' :
				removeActivity(infoObj.data, lm);
				break;
			case 'LESSON' :
				trace('setting lesson name');
				_lessonName.text = lm.name;
				break;
			case 'DESIGNMODEL' :
				trace('updating design model for lesson..');
				break;
			case 'PROGRESS' :
				Debugger.log('progress data receieved for user..' + lm.progressData.getUserName(),Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
				
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
        }
	}
    
	
	/**
	 * Remove the activityies from screen on selection of new lesson
	 * 
	 * @usage   
	 * @param   activityUIID 
	 * @return  
	 */
	private function removeActivity(a:Activity,lm:LessonModel){
		//dispatch an event to show the design  has changed
		trace("in removeActivity")
		var r = lm.activitiesDisplayed.remove(a.activityUIID);
		r.removeMovieClip();
		var s:Boolean = (r==null) ? false : true;
		
	}
	
	private function removeAll(lm:LessonModel){
		var keys = lm.activitiesDisplayed.keys();
		for(var i=0; i<keys.length; i++){
			var r = lm.activitiesDisplayed.remove(keys[i]);
			r.removeMovieClip();
		}
		ACT_X = -25;
		ACT_Y = 32.5;
	}
	
	/**
	 * Draws new activity to monitor tab view stage.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   cm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function drawActivity(a:Activity,lm:LessonModel):Boolean{
		
		Debugger.log('The activity:'+a.title+','+a.activityTypeID+' is now be drawn',Debugger.CRITICAL,'drawActivity','LessonView');
		
		var _activityLayer_mc = progress_scp.content;
		
		var s:Boolean = false;
		var newActivity_mc:MovieClip;
		var lv:LessonView = LessonView(this);
		var lc = getController();
		
		
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGateActivity() || a.isGroupActivity() ){
			newActivity_mc = _activityLayer_mc.attachMovie("LearnerActivity", "LearnerActivity" + a.activityID, _activityLayer_mc.getNextHighestDepth(),{_activity:a,_controller:lc,_view:lv, _x:ACT_X+25, _y:ACT_Y, actLabel:a.title, learner:lm.progressData});
			ACT_Y = newActivity_mc._y + ACTIVITY_OFFSET;
			Debugger.log('The activity:'+a.title+','+a.activityTypeID+' is tool/gate/group activity',Debugger.CRITICAL,'drawActivity','LessonView');
		}else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE || a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			//get the children
			var children:Array = lm.learningDesignModel.getComplexActivityChildren(a.activityUIID);
			Debugger.log('The activity:'+a.title+','+a.activityTypeID+' is is parellel (complex) activity',Debugger.CRITICAL,'drawActivity','LessonView');
		
			newActivity_mc = _activityLayer_mc.attachMovie("LearnerComplexActivity", "LearnerComplexActivity" + a.activityID, _activityLayer_mc.getNextHighestDepth(),{_activity:a,_children:children,_controller:lc,_view:lv, _x:ACT_X+25, _y:ACT_Y, learner:lm.progressData});
			ACT_Y = newActivity_mc._y + ACTIVITY_OFFSET;
			
		}else{
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','LessonView');
		}
		
		var actItems:Number = lm.activitiesDisplayed.size()
		if (actItems < lm.getActivityKeys().length){
			lm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
		}
		
		_activityList.push(newActivity_mc);
		
		return true;
	}
	
	 /**
    * Sets the size of the Lesson on stage, called from update
    */
	private function setSize(lm:LessonModel):Void{
		
        var s:Object = lm.getSize();
        
		//Size panel
		trace('lesson view  setting width to '+s.w);
		bkg_pnl.setSize(s.w,s.h);
		progress_scp.setSize(s.w, s.h-progress_scp._y)
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