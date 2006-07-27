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

import org.lamsfoundation.lams.learner.ls.*;
import org.lamsfoundation.lams.common.LearnerComplexActivity;
import org.lamsfoundation.lams.common.mvc.*
import org.lamsfoundation.lams.common.util.*
import org.lamsfoundation.lams.common.dict.*
import org.lamsfoundation.lams.common.comms.Communication;
import org.lamsfoundation.lams.learner.*
import org.lamsfoundation.lams.authoring.Activity;

import mx.managers.*;
import mx.controls.*;
import mx.events.*

/*
* Make changes to Lesson's model data based on user input
*/
class LessonController extends AbstractController {
	
	/**
	* Constructor
	*
	* @param   cm   The model to modify.
	*/
	private var _lessonModel:LessonModel;
	private var _app:Application;
	private var _comms:Communication;
	private var _isBusy:Boolean;
	
	//These are defined so that the compiler can 'see' the events that are added at runtime by EventDispatcher
    private var dispatchEvent:Function;
	
	public function LessonController (cm:Observable) {
		super (cm);
		_app = Application.getInstance();
		_comms = _app.getComms();
		_lessonModel = LessonModel(model);
		_isBusy = false;
		
		EventDispatcher.initialize(this);     
	}
	
	/**
	 * Recieves the click events from the Lesson buttons.  Based on the label
	 * the relevent method is called to action the user request
	 * @param   evt 
	 */
	public function click(evt):Void{
		trace(String(evt.target));
		
		var tgt:String = new String(evt.target);
		if(tgt.indexOf("export_btn") != -1){
			_lessonModel.getLesson().exportLesson();
		}
		
	}
	
	public function activityClick(ca:Object):Void{
		//if (ca.activityTypeID==Activity.PARALLEL_ACTIVITY_TYPE){
			
			Debugger.log('activityClick CanvasActivity:'+ca.activity.activityID,Debugger.GEN,'activityClick','LessonController');
		//}
    }
   
	public function activityDoubleClick(ca:Object):Void{
		setBusy()
		Debugger.log('activityDoubleClick CanvasActivity:'+ca.activity.activityID + ' status: ' + ca.activityStatus + 'type id: ' + ca.activity.activityTypeID,Debugger.GEN,'activityDoubleClick','LessonController');
	   
		if(ca.activity.activityTypeID == Activity.TOOL_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.OPTIONAL_ACTIVITY_TYPE || ca.activity.activityTypeID == Activity.PARALLEL_ACTIVITY_TYPE){
			
			if(ca.activityStatus != undefined){
				var URLToSend:String = 'learning/learner.do?method=forwardToLearnerActivityURL&activityID='+ca.activity.activityID+'&userID='+_root.userID+'&lessonID='+_root.lessonID;
				
				if(ca.activityStatus == 'completed_mc' && ca.activity.activityTypeID != Activity.OPTIONAL_ACTIVITY_TYPE){
					_lessonModel.getLesson().getActivityURL(URLToSend, true);
				} else if(ca.activityStatus == 'attempted_mc' && _root.mode == 'preview') {
					_lessonModel.getLesson().moveToActivity(_lessonModel.progressData.getCurrentActivityId(), ca.activity.activityID);
				} else {
					_lessonModel.getLesson().getActivityURL(URLToSend, false);
				}
			} else if(_root.mode == 'preview') {
				/* if child Activity is double-clicked then load the parent Activity */
				if(ca.activity.parentActivityID != null || ca.activity.parentActivityID != undefined){
					_lessonModel.getLesson().moveToActivity(_lessonModel.progressData.getCurrentActivityId(), ca.activity.parentActivityID);
				} else {
					_lessonModel.getLesson().moveToActivity(_lessonModel.progressData.getCurrentActivityId(), ca.activity.activityID);
				}
			} else {
				var alertMSG:String = Dictionary.getValue('al_doubleclick_todoactivity')
				getURL("javascript:alert('"+alertMSG+"');");
				
			}
		}
	  
	   clearBusy();
	}
   
    public function activityRelease(ca:Object):Void{
	   Debugger.log('activityRelease LearnerActivity:'+ca.activity.activityID,Debugger.GEN,'activityRelease','LessonController');
	    
	}
	
    public function activityReleaseOutside(ca:Object):Void{
	   Debugger.log('activityReleaseOutside CanvasActivity:'+ca.activity.activityID,Debugger.GEN,'activityReleaseOutside','LessonController');
    }
	
	public function complexActivityRelease(ca:Object, dbClick:Boolean):Void{
		if(!dbClick){
			if(ca.locked){
				Debugger.log('***1*** CA dbclick: ' + dbClick + 'CA lock: '+ca.locked,Debugger.GEN,'complexActivityRelease','LessonController');
				_lessonModel.setCurrentActivityOpen(ca);
			} else {
				Debugger.log('***2*** CA dbclick: ' + dbClick + 'CA lock: '+ca.locked,Debugger.GEN,'complexActivityRelease','LessonController');
				_lessonModel.setCurrentActivityOpen(null);
			}
		} else {
			Debugger.log('***3*** CA dbclick: ' + dbClick + 'CA lock: '+ca.locked,Debugger.GEN,'complexActivityRelease','LessonController');
		}
	}
	
	public function setBusy(){
		_isBusy = true;
	}
	
	public function clearBusy(){
		_isBusy = false;
	}
	
	public function get appData():Object{
		trace("called monitor application")
		var myObj:Object = new Object();
		myObj.compX = Application.LESSON_X
		myObj.compY = Application.LESSON_Y
		myObj.ttHolder = Application.tooltip;
		return myObj;
		
	}
}