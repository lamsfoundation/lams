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

import org.lamsfoundation.lams.learner.Application;
import org.lamsfoundation.lams.learner.lb.*;
import org.lamsfoundation.lams.learner.ls.Lesson;
import org.lamsfoundation.lams.common.util.*; 
import mx.managers.*;
/**
* Library - LAMS Application
* @author   Mitchell Seaton
*/
class Library {
	// Model
	private var libraryModel:LibraryModel;
	// View
	private var libraryView:LibraryView;
	private var libraryView_mc:MovieClip;
	
	private var _className:String = "Library";
	
	public static var LESSON_X:Number = 0;
	public static var LESSON_Y:Number = 0;
	public static var LESSON_H:Number = 22;
	public static var LESSON_W:Number = 123;
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/*
	* Library Constructor
	*
	* @param   target_mc	Target clip for attaching view
	*/
	function Library(target_mc:MovieClip,x:Number,y:Number){
		trace('[new Library]');
		
		mx.events.EventDispatcher.initialize(this);
		
		//Create the model
		libraryModel = new LibraryModel(this);
		
		//Create the view
		libraryView_mc = target_mc.createChildAtDepth("libraryView",DepthManager.kTop);	
		trace(libraryView_mc);
		
		libraryView = LibraryView(libraryView_mc);
		libraryView.init(libraryModel,undefined);
       
        libraryView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//Register view with model to receive update events
		libraryModel.addObserver(libraryView);

        //Set the position by setting the model which will call update on the view
        libraryModel.setPosition(x,y);
		
		
		
	}
	
	/**
	* event broadcast when a new library is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Library');
		
		if(evt.type=='load') {
            getActiveLessons();
        }else {
            //Raise error for unrecognized event
        }
    }
	
	public function getActiveLessons():Void {
		trace('getting active lessons...');
		
		var callback:Function = Proxy.create(this,setActiveLessons);
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=getActiveLessons&userID='+_root.userID, callback, false);
	
		
	}
	
	private function setActiveLessons(lessons:Array):Void {
		trace('received active lesson data back...');
		// get data and create Lesson obj's
		
		Debugger.log('Recieved active sequences (lessons) array length:'+lessons.length,4,'setToolkitActivities','Toolkit');
		
		var lns = new Array();
		
		// go through list of DTO's and make Lesson objects to add to hash map
		for(var i=0; i< lessons.length; i++){
			var ln:Object = lessons[i];
			
			
			
			var lesson:Lesson = new Lesson(libraryView_mc, LESSON_X, LESSON_Y+(LESSON_H*i), libraryView);
			lesson.populateFromDTO(ln);
			trace('pushing lesson with id: ' + lesson.getLessonID());
			lns.push(lesson);
		}
			
		//sets these in the toolkit model in a hashtable by lib id
		libraryModel.setLearningSequences(lns);	
		
		dispatchEvent({type:'load',target:this});
		
	}
	
	/**
	* Used by application to set the size
	* @param width The desired width
	* @param height the desired height
	*/
	public function setSize(width:Number, height:Number):Void{
		libraryModel.setSize(width, height);
	}
    
    public function setPosition(x:Number,y:Number){
        //Set the position within limits
        //TODO DI 24/05/05 write validation on limits
        libraryModel.setPosition(x,y);
    }
	
	public function getLesson(lessonId:Number):Lesson {
		return libraryModel.getLesson(lessonId);
	}

	//Dimension accessor methods
	public function get width():Number{
		return libraryModel.width;
	}
	
	public function get height():Number{
		return libraryModel.height;
	}
	
	public function get x():Number{
		return libraryModel.x;
	}
	
	public function get y():Number{
		return libraryModel.y;
	}

    function get className():String { 
        return 'Library';
    }
    
}
	