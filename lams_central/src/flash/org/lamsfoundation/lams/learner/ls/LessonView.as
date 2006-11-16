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

import org.lamsfoundation.lams.learner.*;
import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.learner.lb.*;

import org.lamsfoundation.lams.common.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.mvc.*;
import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.style.*;
import org.lamsfoundation.lams.common.dict.*;

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
	private static var LESSON_NAME_LENGTH_LIMIT = 20;
	private static var ACT_TITLE_LENGTH_LIMIT = 20;
	private static var SCROLL_CHECK_INTERVAL:Number = 10;
	private static var SCROLL_LOOP_FACTOR:Number = 2;
	private static var STRING_CONT:String = "...";
	
	private var ScrollCheckIntervalID:Number = null;
	
	private var _className = "LessonView";
	private var _depth:Number;
	
	// Lesson clip
	private var _lesson_mc:MovieClip;
	
	private var bkg_pnl:MovieClip;
	private var lessonHead_pnl:MovieClip;
	private var progress_scp:MovieClip;
	private var _activityList:Array;
	
	private var _tm:ThemeManager;
	private var _dictionary:Dictionary;
	
	private var ACT_X:Number = -20;
	private static var ACT_X_OFFSET:Number = 65;
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
		
		_tm = ThemeManager.getInstance();
		
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
		
		this.onEnterFrame = createLesson;	
	}
	
	/**
	* Sets up the lesson (clip)
	*/
	public function createLesson() {
		//Delete the enterframe dispatcher
        delete this.onEnterFrame;
		
		trace('creating new Lesson ...');
        setStyles();
		
        _lesson_mc = this;
		_depth = this.getNextHighestDepth();

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
				Debugger.log('status updated',Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
				removeAll(lm);
				break;
			case 'DRAW_ACTIVITY' :
				drawActivity(infoObj.data, lm);
				break;
			case 'UPDATE_ACTIVITY' :
				Debugger.log('updating activity: ' + infoObj.data.activityUIID,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
				updateActivity(infoObj.data, lm);
				break;
			case 'REMOVE_ACTIVITY' :
				removeActivity(infoObj.data, lm);
				break;
			case 'LESSON' :
				trace('setting lesson name');
				setLessonName(lm.name);
				Application.getInstance().getHeader().showExportButton(lm.learnerExportAvailable);
				break;
			case 'DESIGNMODEL' :
				trace('updating design model for lesson..');
				break;
			case 'PROGRESS' :
				Debugger.log('progress data receieved for user..' + lm.progressData.getUserName(),Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
				break;
			case 'PROGRESS_UPDATE' :
				Debugger.log('progress data receieved for user..' + lm.progressData.getUserName(),Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
				//removeAll(lm);
				lm.updateDesign();
				break;
			case 'CLOSE_COMPLEX_ACTIVITY' :
				closeComplexActivity(infoObj.data, lm);
				Debugger.log('closing complex activity' + infoObj.data.activity.activityUIID,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
				break;
            default :
                Debugger.log('unknown update type :' + infoObj.updateType,Debugger.CRITICAL,'update','org.lamsfoundation.lams.LessonView');
        }
	}
    
	private function setLessonName(lessonName:String){
		if (lessonName.length > LESSON_NAME_LENGTH_LIMIT){
			lessonName = lessonName.substr(0, LESSON_NAME_LENGTH_LIMIT)+STRING_CONT;
		}
		Application.getInstance().getHeader().setLessonName(lessonName);
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
		ACT_X = -20;
		ACT_Y = 32.5;
	}
	
	private function closeComplexActivity(ca:Object, lm:LessonModel):Void{
		if(lm.activitiesDisplayed.containsKey(ca.activity.activityUIID)){
			var a:LearnerComplexActivity = LearnerComplexActivity(lm.activitiesDisplayed.get(ca.activity.activityUIID));
			if(a.locked){
				a.collapse();
				Debugger.log('closed complex activity' + ca.activity.activityUIID,Debugger.CRITICAL,'closeComplexActivity','org.lamsfoundation.lams.LessonView');

			} else {
				Debugger.log('not closed complex activity' + ca.activity.activityUIID,Debugger.CRITICAL,'closeComplexActivity','org.lamsfoundation.lams.LessonView');
			}
		} else {
			Debugger.log('no entry in table: ' + ca.activity.activityUIID,Debugger.CRITICAL,'closeComplexActivity','org.lamsfoundation.lams.LessonView');

			// no object found in table
		}
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
		
		var activityTitle:String = a.title;
		if(activityTitle.length > ACT_TITLE_LENGTH_LIMIT) {
			activityTitle = activityTitle.substr(0,ACT_TITLE_LENGTH_LIMIT) + STRING_CONT;
		}
		
		
		//take action depending on act type
		if(a.activityTypeID==Activity.TOOL_ACTIVITY_TYPE || a.isGroupActivity() ){
			newActivity_mc = _activityLayer_mc.attachMovie("LearnerActivity", "LearnerActivity" + a.activityID, _activityLayer_mc.getNextHighestDepth(),{_activity:a,_controller:lc,_view:lv, _x:(progress_scp._width/2)-ACT_X_OFFSET, _y:ACT_Y, actLabel:activityTitle, learner:lm.progressData, _complex:false});
			ACT_Y = newActivity_mc._y + ACTIVITY_OFFSET;
			//_activityList.push(newActivity_mc);
			Debugger.log('The activity:'+a.title+','+a.activityTypeID+' is tool/gate/group activity',Debugger.CRITICAL,'drawActivity','LessonView');
		} else if(a.isGateActivity()){
			newActivity_mc = _activityLayer_mc.attachMovie("LearnerGateActivity", "LearnerGateActivity" + a.activityID, _activityLayer_mc.getNextHighestDepth(),{_activity:a,_controller:lc,_view:lv, _x:(progress_scp._width/2)-ACT_X_OFFSET, _y:ACT_Y, actLabel:activityTitle, learner:lm.progressData, _complex:false});
			ACT_Y = newActivity_mc._y + ACTIVITY_OFFSET;
			//_activityList.push(newActivity_mc);
		} else if(a.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE || a.activityTypeID==Activity.OPTIONAL_ACTIVITY_TYPE){
			//get the children
			var children:Array = lm.learningDesignModel.getComplexActivityChildren(a.activityUIID);
			Debugger.log('The activity:'+a.title+','+a.activityTypeID+' is is parellel (complex) activity',Debugger.CRITICAL,'drawActivity','LessonView');
		
			newActivity_mc = _activityLayer_mc.attachMovie("LearnerComplexActivity", "LearnerComplexActivity" + a.activityID, _activityLayer_mc.getNextHighestDepth(),{_activity:a,_children:children,_controller:lc,_view:lv, _x:(progress_scp._width/2)-ACT_X_OFFSET, _y:ACT_Y, learner:lm.progressData});
			ACT_Y = newActivity_mc._y + ACTIVITY_OFFSET;
			//_activityList.push(newActivity_mc);
		}else{
			Debugger.log('The activity:'+a.title+','+a.activityUIID+' is of unknown type, it cannot be drawn',Debugger.CRITICAL,'drawActivity','LessonView');
		}
		_activityList.push(newActivity_mc);
		var actItems:Number = lm.activitiesDisplayed.size()
		if (actItems < lm.getActivityKeys().length){
			lm.activitiesDisplayed.put(a.activityUIID,newActivity_mc);
		}
		
		progress_scp.redraw(true);
		
		return true;
	}
	
	
	/**
	 * Updates existing activity on learner progress bar.
	 * @usage   
	 * @param   a  - Activity to be drawn
	 * @param   lm - Refernce to the model
	 * @return  Boolean - successfullit
	 */
	private function updateActivity(a:Activity,lm:LessonModel):Boolean{
		
		
		var targetPos;
		
		if(lm.activitiesDisplayed.containsKey(a.activityUIID)){
			var activityMC:Object = lm.activitiesDisplayed.get(a.activityUIID);
			activityMC.refresh();
			
			Debugger.log('progess data:'+lm.progressData.getCurrentActivityId()+' a.activityID:'+a.activityID,Debugger.CRITICAL,'updateActivity','LessonView');
			
			if(lm.progressData.getCurrentActivityId() == a.activityID){
					//progress_scp.vPosition = activityMC._x;
				var scrollBarPosition = activityMC._y - (ACTIVITY_OFFSET*1.5);
				Debugger.log('|| sb pos: ' + scrollBarPosition + ' || max pos: ' + progress_scp.vScroller.maxPos+ ' || activity Y:'+activityMC._y+' || progress_scp.content._height:'+progress_scp.content._height,Debugger.CRITICAL,'updateActivity','LessonView');
				
				if((scrollBarPosition >= 0) && (scrollBarPosition <= progress_scp.vScroller.maxPos)){
					targetPos = scrollBarPosition;
				} else if(scrollBarPosition >= progress_scp.vScroller.maxPos){
					targetPos = progress_scp.vScroller.maxPos;
				} else {
					targetPos = progress_scp.vScroller.minPos;
				}
				
				if(progress_scp.vScroller._visible) {
					Debugger.log('adjusting scrollbar position to target: ' + targetPos,Debugger.CRITICAL,'updateActivity','LessonView');
					if(ScrollCheckIntervalID == null) {
						ScrollCheckIntervalID = setInterval(Proxy.create(this,adjustScrollBar,targetPos),SCROLL_CHECK_INTERVAL);
					}
				}
			}
		
		} else {
			drawActivity(a, lm);
		}
		
		
		
		
		return true;
	}
	
	private function adjustScrollBar(targetVal:Number) {
		
		var offset:Number;
		var count:Number = 0;
		
		while(count < SCROLL_LOOP_FACTOR) {
			var currentVal:Number = Number(progress_scp.vPosition);
			count++;
			
			if(Math.round(currentVal) == Math.round(targetVal)) {
				if(ScrollCheckIntervalID) {
					Debugger.log('clearing Scroll Check Interval: ' + targetVal,Debugger.CRITICAL,'adjustScrollBar','LessonView');
					clearInterval(ScrollCheckIntervalID);
					ScrollCheckIntervalID = null;
				}
					
				return;
			} else {
			
				if(Math.round(currentVal) < Math.round(targetVal)) {
					offset = 1;
				} else {
					offset = -1;
				}
				
				progress_scp.vPosition = currentVal + offset;
				Debugger.log('adjusting scroll position - offset: ' + offset+ ' - currentVal: ' + currentVal + ' - targetVal: ' + targetVal,Debugger.CRITICAL,'adjustScrollBar','LessonView');
				
			}
		}
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
		var styleObj = _tm.getStyleObject('WZPanel');
		bkg_pnl.setStyle('styleName', styleObj);
		
		styleObj = _tm.getStyleObject('scrollpane');
		progress_scp.setStyle('styleName', styleObj);
	}
	
	/**
	* Gets the LessonModel
	*
	* @returns model 
	*/
	public function getModel():LessonModel{
			return LessonModel(model);
	}
	/**
	 * Overrides method in abstract view to ensure cortect type of controller is returned
	 * @usage   
	 * @return  LessonController
	 */
	public function getController():LessonController{
		var c:Controller = super.getController();
		return LessonController(c);
	}
	/*
    * Returns the default controller for this view.
    */
    public function defaultController (model:Observable):Controller {
        return new LessonController(model);
    }
        
}