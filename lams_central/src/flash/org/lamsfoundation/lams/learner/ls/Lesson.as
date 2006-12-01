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
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.Progress;
import org.lamsfoundation.lams.common.ui.Cursor;
import org.lamsfoundation.lams.common.ui.LFMessage;
import org.lamsfoundation.lams.common.dict.Dictionary;
import org.lamsfoundation.lams.authoring.DesignDataModel;

import mx.managers.*;
/**
* Lesson - LAMS Application
* @author   Mitchell Seaton
*/
class Lesson {
	
	// Model
	private var lessonModel:LessonModel;
	// View
	private var lessonView:LessonView;
	private var lessonView_mc:MovieClip;
	
	private var _libraryView:LibraryView;
	private var _lesson:Lesson;
	
	private var _instance:Lesson;
	private var _className:String = "Lesson";
	private var _finishedDesign:Boolean = false;
	
    private static var LOAD_CHECK_INTERVAL:Number = 50;
	private static var LOAD_CHECK_TIMEOUT_COUNT:Number = 300;
	
	private var _loadCheckCount = 0;
	private var _loadCheckIntervalID:Number;
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/*
	* Lesson Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	function Lesson(target_mc:MovieClip,x:Number,y:Number){
		mx.events.EventDispatcher.initialize(this);
        
		//Create the model
		lessonModel = new LessonModel(this);
		//Create the view
		lessonView_mc = target_mc.createChildAtDepth("Lesson",DepthManager.kTop);	
		trace(lessonView_mc);
		lessonView = LessonView(lessonView_mc);
		lessonView.init(lessonModel,undefined);
       
        lessonView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//Register view with model to receive update events
		lessonModel.addObserver(lessonView);

        //Set the position by setting the model which will call update on the view
        lessonModel.setPosition(x,y);
		
	}
	
	/**
	* event broadcast when a new lesson is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	public function populateFromDTO(dto:Object){
		lessonModel.populateFromDTO(dto);
	}
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Lesson');
		//lessonModel.setDefaultState();
		if(evt.type=='load') {
            dispatchEvent({type:'load',target:this});
        }else {
            //Raise error for unrecognized event
        }
    }
     
	public function getLesson():Boolean {
		var callback:Function = Proxy.create(this,storeLessonData);
		
		// call action
		var lessonId:Number = _root.lessonID;
		//var userId:Number = Application.getInstance().getUserID();

		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=getLesson&lessonID='+String(lessonId), callback, false);
			
		return true;
	}
	 
	public function joinLesson():Boolean {
		
		var callback:Function = Proxy.create(this,startLesson);
		
		// call action
		var lessonId:Number = lessonModel.ID;
		//var userId:Number = Application.getInstance().getUserID();

		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=joinLesson&lessonID='+String(lessonId), callback, false);
			
		// get Learning Design for lesson
		openLearningDesign();
		
		return true;
	}
	
	public function resumeLesson():Boolean{
		var callback:Function = Proxy.create(this,continueLesson);
		
		// call action
		var lessonId:Number = lessonModel.ID;
		//var userId:Number = Application.getInstance().getUserID();

		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=joinLesson&lessonID='+String(lessonId), callback, false);
			
		return true;
	}
	
	public function continueLesson(pkt:Object){
		getURL(_root.serverURL + 'learning'+String(pkt)+'?progressId='+lessonModel.getLessonID(),'contentFrame');
		
	}
	
	public function exitLesson():Boolean {
		var callback:Function = Proxy.create(this, exitConfirm);
		
		// call action
		var lessonId:Number = lessonModel.ID;
		
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=exitLesson&lessonID='+String(lessonId), callback, false);
		
		return true;
	}
	
	public function exitConfirm(pkt:Object){
		if(pkt) { getURL('javascript:top.window.close();'); }
	}
	
	public function exportLesson(){
		// do export call
		var exp_url:String = _root.serverURL + 'learning/exportWaitingPage.jsp?mode=learner&lessonID='+String(lessonModel.ID);
		
		JsPopup.getInstance().launchPopupWindow(exp_url, 'ExportPortfolioLearner', 410, 640, true, true, false, false, false);
	
	}
	
	private function storeLessonData(dto:Object){
		lessonModel.populateFromDTO(dto);
		joinLesson();
			
	}
	
	private function startLesson(pkt:Object){
		trace('received message back from server aftering joining lesson...');
		
		// set lesson as active
		lessonModel.setActive();
		trace('pktobject value: '+String(pkt));
		getURL(_root.serverURL + 'learning'+String(pkt)+'?lessonID='+lessonModel.getLessonID(),'contentFrame');
		
		
		// check was successful join
		getFlashProgress();
		
		
	}  
	
	public function moveToActivity(fromAct, toAct){
		var callback:Function = Proxy.create(this, afterMoveActivity);
		
		// call action
		var lessonId:Number = lessonModel.ID;
		
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=forceMove&lessonID='+String(lessonId)+'&learnerID='+_root.userID+'&currentActivityID='+fromAct+'&destActivityID='+toAct, callback, false);
			
		return true;
		
	}
	
	private function afterMoveActivity(pkt:Object){
		getURL(_root.serverURL + 'learning'+String(pkt)+'?lessonID='+lessonModel.getLessonID(),'contentFrame');
	}
	
	private function getFlashProgress():Void{
		Debugger.log('Loading flash progress.',Debugger.CRITICAL,'getFlashProgress','Lesson');
				
		if(!finishedDesign) { 
			// first time through set interval for method polling
			if(!_loadCheckIntervalID) {
				_loadCheckIntervalID = setInterval(Proxy.create(this, getFlashProgress), LOAD_CHECK_INTERVAL);
			} else {
				_loadCheckCount++;
				Debugger.log('Waiting (' + _loadCheckCount + ') for data to load...',Debugger.CRITICAL,'getFlashProgress','Lesson');
				
				// if design loaded
				if(finishedDesign) {
					clearInterval(_loadCheckIntervalID);
					
					callFlashProgress();
			
				} else if(_loadCheckCount >= LOAD_CHECK_TIMEOUT_COUNT) {
					Debugger.log('Reached timeout waiting for data to load.',Debugger.CRITICAL,'getFlashProgress','Lesson');
					clearInterval(_loadCheckIntervalID);
					var msg:String = Dictionary.getValue('al_timeout');
					LFMessage.showMessageAlert(msg);
					
					// clear count and restart polling check
					_loadCheckCount = 0;
					getFlashProgress();
				}
			}
		} else {
			callFlashProgress();
			clearInterval(_loadCheckIntervalID);
		}
		
	}
	
	private function callFlashProgress():Void {
		var callback:Function = Proxy.create(this,saveProgressData);
		var lessonId:Number = lessonModel.ID;
		Application.getInstance().getComms().getRequest('learning/learner.do?method=getFlashProgressData&lessonID='+String(lessonId), callback, false);
	}
	
	private function saveProgressData(progressDTO:Object):Void{
		var p:Progress = new Progress();
		p.populateFromDTO(progressDTO);
		lessonModel.setProgressData(p);
		
		Debugger.log('progress data receieved for user..' + progressDTO,Debugger.CRITICAL,'saveProgressData','org.lamsfoundation.lams.Lesson');
		
		lessonModel.broadcastViewUpdate("PROGRESS_UPDATE", null);
	}
	
	public function updateProgressData(attempted:Array, completed:Array, current:Number):Void{
		var p:Progress = lessonModel.progressData;
		if(p){
			p.currentActivityId = current;
			p.attemptedActivities = attempted;
			p.completedActivities = completed;
			lessonModel.broadcastViewUpdate("PROGRESS_UPDATE", null);
		}
	}
	
	private function closeLesson(pkt:Object){
		trace('receiving message back from server...');
		
		// set lesson as inactive
		//lessonModel.setInactive();
		
		// deactivate Progress movie
		
		// load exit jsp
		getURL(_root.serverURL + 'learning'+String(pkt), 'contentFrame');
	}
	
	private function openLearningDesign(){
		trace('opening learning design...');
		finishedDesign = false;
		
		var designId:Number = lessonModel.learningDesignID;
        var callback:Function = Proxy.create(this,saveDataDesignModel);
           
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designId,callback, false);
		
	}
	
	private function saveDataDesignModel(learningDesignDTO:Object){
		trace('returning learning design...');
		trace('saving model data...');
		if(learningDesignDTO instanceof LFError) {
			Cursor.showCursor(Application.C_DEFAULT);
			learningDesignDTO.showErrorAlert();
		} else {
			var model:DesignDataModel = new DesignDataModel();
			model.setDesign(learningDesignDTO);
			lessonModel.setLearningDesignModel(model);
		}
		
	}
	
	public function getLessonID():Number {
		return lessonModel.ID;
	}
	
	public function checkState(stateID:Number):Boolean {
		if(lessonModel.getLessonStateID()==stateID){
			return true
		} else {
			return false;
		}
	}
	
	/** Loads the Activity page in frame or popup-window depending on the status of the Acvtivity. */
	public function getActivityURL(request:String, popup:Boolean){
		
		if(popup){
			popupActivity(_root.serverURL + request);
		} else {
			loadActivity(_root.serverURL + request);
		}
		
	}
	
	private function loadActivity(url:String){
		Debugger.log('loading activity path using forward: ' + url,Debugger.CRITICAL,'loadActivity','org.lamsfoundation.lams.Lesson');

		getURL(url,"contentFrame");
	}
	
	private function popupActivity(url:String){
		Debugger.log('loading activity (popup window) path using forward: ' + url,Debugger.CRITICAL,'loadActivity','org.lamsfoundation.lams.Lesson');

		JsPopup.getInstance().launchPopupWindow(url, 'LearnerActivity', 600, 800, true, true, true, false, false);
	
	}
	
	
	//class accesssor methods
	public function get model():LessonModel{
		return lessonModel;
	}
	
	public function get view():LessonView{
		return lessonView;
	}
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		lessonModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        lessonModel.setPosition(x,y);
    }

	//Dimension accessor methods
	public function get width():Number{
		return lessonModel.width;
	}
	
	public function get height():Number{
		return lessonModel.height;
	}
	
	public function get x():Number{
		return lessonModel.x;
	}
	
	public function get y():Number{
		return lessonModel.y;
	}
	
	public function set finishedDesign(a:Boolean){
		_finishedDesign = a;
	}
	
	public function get finishedDesign():Boolean{
		return _finishedDesign;
	}

    function get className():String { 
        return 'Lesson';
    }
    
}