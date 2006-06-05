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
import org.lamsfoundation.lams.common.util.*;
import org.lamsfoundation.lams.common.Progress;
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
		Application.getInstance().getComms().getRequest('learning/learner.do?method=joinLesson&lessonId='+String(lessonId), callback, false);
			
		// get Learning Design for lesson
		openLearningDesign();
			
		return true;
	}
	
	public function exitLesson():Boolean {
		var callback:Function = Proxy.create(this,closeLesson);
		
		// call action
		var lessonId:Number = lessonModel.ID;
		
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=exitLesson&lessonId='+String(lessonId), callback, false);
		
		return true;
	}
	
	private function storeLessonData(dto:Object){
		lessonModel.populateFromDTO(dto);
		joinLesson();
	}
	
	private function startLesson(pkt:Object){
		trace('received message back from server aftering joining lesson...');
		
		// check was successful join
		getFlashProgress();
		
		// set lesson as active
		lessonModel.setActive();
		trace('pktobject value: '+String(pkt));
		getURL('http://localhost:8080/lams/learning'+String(pkt)+'?progressId='+lessonModel.getLessonID(),'contentFrame');
		
	}  
	
	private function getFlashProgress():Void{
		var callback:Function = Proxy.create(this,saveProgressData);
		var lessonId:Number = lessonModel.ID;
		Application.getInstance().getComms().getRequest('learning/learner.do?method=getFlashProgressData&progressId='+String(lessonId), callback, false);
	}
	
	private function saveProgressData(progressDTO:Object):Void{
		var p:Progress = new Progress();
		p.populateFromDTO(progressDTO);
		lessonModel.setProgressData(p);
		Debugger.log('progress data receieved for user..' + progressDTO,Debugger.CRITICAL,'saveProgressData','org.lamsfoundation.lams.Lesson');
	}
	
	private function closeLesson(pkt:Object){
		trace('receiving message back from server...');
		
		// set lesson as inactive
		lessonModel.setInactive();
		
		// deactivate Progress movie
	}
	
	private function openLearningDesign(){
		trace('opening learning design...');
		var designId:Number = lessonModel.learningDesignID;

        var callback:Function = Proxy.create(this,saveDataDesignModel);
           
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designId,callback, false);
		
	}
	
	private function saveDataDesignModel(learningDesignDTO:Object){
		trace('returning learning design...');
		trace('saving model data...');
		
		var model:DesignDataModel = new DesignDataModel();
		model.setDesign(learningDesignDTO);
		lessonModel.setLearningDesignModel(model);
		
		// activite Progress movie
		
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

    function get className():String { 
        return 'Lesson';
    }
    
}