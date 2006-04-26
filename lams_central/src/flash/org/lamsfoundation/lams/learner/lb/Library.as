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
import org.lamsfoundation.lams.common.Sequence;
import org.lamsfoundation.lams.authoring.DesignDataModel;
import org.lamsfoundation.lams.learner.lb.*;
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
		trace(libraryView);
        libraryView_mc.addEventListener('load',Proxy.create(this,viewLoaded));
        
		//Register view with model to receive update events
		libraryModel.addObserver(libraryView);

        //Set the position by setting the model which will call update on the view
        libraryModel.setPosition(x,y);
		libraryModel.setSize(null,Stage.height-y);
		
		
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
            getActiveSequences();
        }else {
            //Raise error for unrecognized event
        }
    }
	
	public function getActiveSequences():Void {
		trace('getting active sequences...');
		
		var callback:Function = Proxy.create(this,setActiveSequences);
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=getActiveLessons&userId='+_root.userID, callback, false);
	
		
	}
	
	private function setActiveSequences(seqs:Array):Void {
		trace('received active lesson/seq data back...');
		// get data and create Sequence obj's
		
		Debugger.log('Received active sequences (lessons) array length:'+seqs.length,4,'setActiveLessons','Library');
		
		var lns = new Array();
		
		// go through list of DTO's and make Lesson objects to add to hash map
		for(var i=0; i< seqs.length; i++){
			var s:Object = seqs[i];
			
			
			//var sp_mc:MovieClip = libraryView.getScrollPane();
			//sp_mc.contentPath = "empty_mc";
			//trace(sp_mc);
			//trace(sp_mc.content);
			//var lesson:Lesson = new Lesson(sp_mc.content, LESSON_X, LESSON_Y+(LESSON_H*i), libraryView);
			
			var seq:Sequence = new Sequence();
			seq.populateFromDTO(s);
			trace('pushing seq with id: ' + seq.getSequenceID());
			lns.push(seq);
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
	
	public function getSequence(seqId:Number):Sequence {
		return libraryModel.getSequence(seqId);
	}
	
	public function select(seq:Sequence):Void {
		var libraryController = libraryView.getController();
		libraryController.selectSequence(seq);	
	}

	public function joinSequence(seq:Object):Boolean {
		
		var callback:Function = Proxy.create(this,startSequence);
		
		// call action
		var seqId:Number = seq.getSequenceID();

		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=joinLesson&userId='+_root.userID+'&lessonId='+String(seqId), callback, false);
			
		// get Learning Design for lesson
		openLearningDesign(seq);
			
		return true;
	}
	
	public function exitSequence(seq:Object):Boolean {
		var callback:Function = Proxy.create(this,closeSequence);
		
		// call action
		var seqId:Number = seq.getSequenceID();
		
		// do request
		Application.getInstance().getComms().getRequest('learning/learner.do?method=exitLesson&lessonID='+String(seqId), callback, false);
		
		return true;
	}
	
	private function startSequence(pkt:Object){
		trace('received message back from server aftering joining lesson...');
		
		// check was successful join
		
		// start the selected sequence
		var seq:Sequence = Sequence(libraryModel.getSelectedSequence());
		trace(seq);
		trace('pktobject value: '+String(pkt));
		getURL('http://localhost:8080/lams/learning'+String(pkt)+'?progressId='+seq.getSequenceID(),'contentFrame');
		
	}  
	
	private function closeSequence(pkt:Object){
		trace('receiving message back from server...');
		
		// stop current sequence
		
		// deactivate Progress movie
		
	}
	
	private function openLearningDesign(seq:Object){
		trace('opening learning design...');
		var designId:Number = seq.getLearningDesignID();

        var callback:Function = Proxy.create(this,saveDataDesignModel);
           
		Application.getInstance().getComms().getRequest('authoring/author.do?method=getLearningDesignDetails&learningDesignID='+designId,callback, false);
		
	}
	
	private function saveDataDesignModel(learningDesignDTO:Object){
		trace('returning learning design...');
		trace('saving model data...');
		var seq:Sequence = Sequence(libraryModel.getSelectedSequence());
		var model:DesignDataModel = new DesignDataModel();
		
		model.setDesign(learningDesignDTO);
		seq.setLearningDesignModel(model);
		
		// activite Progress movie
		
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
	