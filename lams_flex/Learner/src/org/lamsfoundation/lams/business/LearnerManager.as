package org.lamsfoundation.lams.business
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.external.*;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.collections.ArrayCollection;
	
	import org.lamsfoundation.lams.common.dictionary.XMLDictionaryRegistry;
	import org.lamsfoundation.lams.vos.Activity;
	import org.lamsfoundation.lams.vos.LearningDesign;
	import org.lamsfoundation.lams.vos.Lesson;
	import org.lamsfoundation.lams.vos.Progress;
	
	public class LearnerManager extends EventDispatcher
	{	
		/*-.........................................Properties from FlashVars ..........................................*/
		private var _serverUrl:String;
		private var _lessonID:uint;
		private var _userID:uint;
		
		/*-.........................................Properties from service calls ..........................................*/
		private var _dictionary:XMLDictionaryRegistry;
		private var _lesson:Lesson;
		private var _learningDesign:LearningDesign;
		private var _learningDesignXML:XMLList;
		private var _progress:Progress;
		
		/*-......................................... Static variables ..........................................*/
		private static var withAttributes:String = "?";
		private static var and:String = "&";
		private static var learnerAction:String = "learning/learner.do";
		private static var launchActivityMethod:String = "method=forwardToLearnerActivityURL";
		
		/*-.........................................Constructor..........................................*/
		 public function LearnerManager(serverUrl:String, lessonID:uint, userID:uint)
        {
        	// set initial variables
        	_dictionary = new XMLDictionaryRegistry(new XML());
        	_serverUrl = serverUrl;
        	_lessonID = lessonID;
        	_userID = userID;
            
            // add external interface callbacks
            ExternalInterface.addCallback("setProgressUpdate", setProgressUpdate);
        	ExternalInterface.addCallback("setLearningDesigns", setLearningDesign);
        }
        
		/*-.........................................Setters and Getters..........................................*/
		[Bindable (event="serverURLChanged")]
		public function get serverURL():String
		{
			return _serverUrl;
		}
		
		[Bindable (event="lessonIDChanged")]
		public function get lessonID():uint
		{
			return _lessonID;
		}

		[Bindable (event="userIDChanged")]
		public function get userID():uint
		{
			return _userID;
		}
		
		[Bindable (event="dictionaryChanged")]
		public function get dictionary():XMLDictionaryRegistry
		{
			return _dictionary;
		}
		
		[Bindable (event="lessonChanged")]
		public function get lesson():Lesson{
			return _lesson;
		}
		
		[Bindable (event="learningDesignChanged")]
		public function get learningDesign():LearningDesign{
			return _learningDesign;
		}
		
		[Bindable (event="learningDesignXMLChanged")]
		public function get learningDesignXML():XMLList{
			return _learningDesignXML;
		}
		
		[Bindable (event="progressChanged")]
		public function get progress():Progress{
			return _progress;
		}
		
		/*-.........................................Methods..........................................*/
		
		public function setServerURL(serverURL:String):void {
			_serverUrl = serverURL;
			
			dispatchEvent(new Event("serverURLChanged"));
		}
		
		public function setLessonID(lessonID:uint):void {
			_lessonID = lessonID;
			
			dispatchEvent(new Event("lessonIDChanged"));
		}
		
		public function setUserID(userID:uint):void {
			_userID = userID;
			
			dispatchEvent(new Event("userIDChanged"));
		}
		
		public function setDictionary(xml:XML):void {
			_dictionary.xml = xml;
			
			dispatchEvent(new Event("dictionaryChanged"));
		}
		
		public function setLesson(lesson:Object):void {
			if(!_lesson){
				_lesson = new Lesson();
			}
			
			_lesson.setFromWDDX(lesson);
			
			dispatchEvent(new Event("lessonChanged"));
		}

		public function setLearningDesign(learningDesign:Object):void {
			if(!_learningDesign){
				_learningDesign = new LearningDesign();
			}
			
			_learningDesign.setFromWDDX(learningDesign);
			
			dispatchEvent(new Event("learningDesignChanged"));
						
			_learningDesignXML = constructLearningDesignXML(_learningDesign);
			
			dispatchEvent(new Event("learningDesignXMLChanged"));
		}
				
		public function setProgress(progress:Object):void {
			if(!_progress){
				_progress = new Progress();
			}
			
			_progress.setFromWDDX(progress);
			
			dispatchEvent(new Event("progressChanged"));
		}
		
		public function setProgressUpdate(progress:Object):void{
			if(!_progress){
				_progress = new Progress();
			}
			
			_progress.setFromArgs(progress.attemptedActivities, progress.completedActivities, progress.currentActivity.activityId);
			
			dispatchEvent(new Event("progressChanged"));
		}
		
		public function getLesson():Lesson{
			return _lesson;
		}
		
		private function constructLearningDesignXML(learningDesign:LearningDesign):XMLList{
			// base of our XML
			var baseXML:XML =	<LearningDesign createDateTime={_learningDesign.createDateTime} title={_learningDesign.title}>
						  		</LearningDesign>;
			
			// make an XMLList with our base
			var learningDesignXML:XMLList = new XMLList(baseXML);
			
			// let the XML creating begin, start it up with the first activityUIID
			learningDesignXML[0].appendChild(createActivityXML(learningDesign.firstActivityUIID, learningDesign));
			
			return learningDesignXML;
		}
		
		private function createActivityXML(activityUIID:uint, learningDesign:LearningDesign):XMLList{
			var baseXML:XML;
			var activityXML:XMLList;
			
			var activity:Object;
			var childActivities:ArrayCollection = new ArrayCollection();
			var nextActivity:Object;
					
			// find the activity and its children
			for(var i:int = 0; i < _learningDesign.activities.length; i++){
				var x:Object = _learningDesign.activities.getItemAt(i);
				if(x.activityUIID == activityUIID){
					activity = x;
				}else if(x.parentUIID == activityUIID){
					childActivities.addItem(x);
				}
			}
			
			// if no activity is found, return null XML
			if(!activity){
				return null;
			}
			
			// create the base activity XML
			baseXML =	<Activity
							activityID={activity.activityID}
							activityTitle={activity.activityTitle}
							activityTypeID={activity.activityTypeID}
							activityUIID={activity.activityUIID}
							activityProgress="unattempted">
						</Activity>;
			
			// create the XMLList with the base
			activityXML = new XMLList(baseXML);
			
			// for each child activity, create some more XML and append it to the current activity's node
			if(childActivities.length != 0){
				for(i = 0; i < childActivities.length; i++){
					x = childActivities.getItemAt(i);
					activityXML[0].appendChild(createActivityXML(x.activityUIID, _learningDesign));
					
					/*
					if the activity being checked is a branch (sequence with transitions),
					ignore the other children and break the loop and let the transition
					loop finish up the XML creation
					*/
					if(uint(activity.activityTypeID) == 8){
						break;
					}
				}
			}
			
			// find the next activity to the current activity and create a node for it
			for(i = 0; i < _learningDesign.transitions.length; i++){
				x = _learningDesign.transitions.getItemAt(i);
				if(x.fromUIID == activityUIID){
					if(x.toUIID){
						activityXML += createActivityXML(x.toUIID, _learningDesign);
					}
					
					break;
				}
			}
			
			return activityXML;
		}
		
		public function launchActivity(xml:XML):void{
			var activity:Activity = new Activity();
			activity.setFromXML(xml);
			var url:String;
			
			url = serverURL + learnerAction + withAttributes + launchActivityMethod + and
				+ "activityID=" + String(activity.activityID) + and
				+ "userID=" + String(userID) + and
				+ "lessonID=" + String(lessonID);
				
			navigateToURL(new URLRequest(url), '_blank');
		}
	}
}