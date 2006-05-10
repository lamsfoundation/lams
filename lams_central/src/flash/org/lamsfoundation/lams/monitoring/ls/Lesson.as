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

import org.lamsfoundation.lams.monitoring.Application;
import org.lamsfoundation.lams.monitoring.ls.*;
//import org.lamsfoundation.lams.monitoring.ls.Lesson;
import org.lamsfoundation.lams.common.util.*; 
import org.lamsfoundation.lams.common.Sequence;
import mx.managers.*;
/**
* Monitoring - LAMS Application
* @author   Pradeep Sharma
*/
class org.lamsfoundation.lams.monitoring.ls.Lesson {
	
	// Model
	private var lessonModel:LessonModel;
	// View
	private var lessonView:LessonView;
	private var lessonView_mc:MovieClip;
	
	private var _className:String = "Lesson";
	
	private var dispatchEvent:Function;       
    public var addEventListener:Function;  
    public var removeEventListener:Function;
	
	/**
	 * Lesson Constructor Function
	 * 
	 * @usage   
	 * @return  target_mc		//Target clip for attaching view
	 */
	public function Lesson(target_mc:MovieClip, x:Number, y:Number){
		
		mx.events.EventDispatcher.initialize(this);
		
		//Create the model
		lessonModel = new LessonModel(this);
		
		//Create the view
		lessonView_mc = target_mc.createChildAtDepth("lessonView",DepthManager.kTop);	
		trace(lessonView_mc);
		
		lessonView = LessonView(lessonView_mc);
		lessonView.init(lessonModel,undefined);
       
        lessonView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//Register view with model to receive update events
		lessonModel.addObserver(lessonView);

        //Set the position by setting the model which will call update on the view
        lessonModel.setPosition(x,y);
		lessonModel.setSize(null,Stage.height-y);
	}
	
	/**
	* event broadcast when a new lesson is loaded 
	*/ 
	public function broadcastInit(){
		dispatchEvent({type:'init',target:this});		
	}
 
	
	private function viewLoaded(evt:Object){
        Debugger.log('viewLoaded called',Debugger.GEN,'viewLoaded','Lesson');
		
		if(evt.type=='load') {
            getAllLessons();
        }else {
            //Raise error for unrecognized event
        }
    }
	
	
	public function getAllLessons():Void {
		trace('getting all lessons...');
		
		var callback:Function = Proxy.create(this,setAllLessons);
		// do request
		Application.getInstance().getComms().getRequest('monitoring/monitoring.do?method=getAllLessons&userID=4', callback);
	
	}
	
	
	private function setAllLessons(lessons:Array):Void {
		trace('received active lesson data back...');
		// get data and create Lesson obj's
		
		Debugger.log('Received all (lessons) array:'+lessons,4,'setActiveLessons','Lesson');
		
		var lns = new Array();
		
		// go through list of DTO's and make Lesson objects to add to hash map
		for(var i=0; i< lessons.length; i++){
			var ln:Object = lessons[i];
			
			
			//var lesson:Lesson = new Lesson(sp_mc.content, LESSON_X, LESSON_Y+(LESSON_H*i), libraryView);
			var seq:Sequence = new Sequence();
			seq.populateFromDTO(ln);
			
			//trace('pushing lesson with id: ' + lessonModel.getLessonID());
			lns.push(seq);
		}
			
		//sets these in the lesson model in a hashtable by lesson id
		lessonModel.setLessonSequences(lns);	
		
		dispatchEvent({type:'load',target:this});
		
	}
	
	public function refresh():Void{
		getAllLessons();
	}
	
	//public function populateFromDTO(dto:Object){
	//	lessonModel.populateFromDTO(dto);
	//}
	
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
        return _className;
    }
}