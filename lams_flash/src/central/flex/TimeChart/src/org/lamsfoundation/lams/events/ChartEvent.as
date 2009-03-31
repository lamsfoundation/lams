package org.lamsfoundation.lams.events
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.vos.LearnerProgress;

	public class ChartEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const UPDATE: String 				= "updateLearnerProgressEvent";
		public static const SELECT: String 				= "showLearnerProgressEvent";
		
		public static const LOAD: String 				= "loadLearnerProgressEvent";
		public static const LOAD_LEARNER: String  		= "loadLearnerProgressSingleEvent";
		public static const LOAD_CLASS: String 			= "loadLearnerProgressAllEvent";
		
		/*-.........................................Properties..........................................*/
		public var learnerProgressList:ArrayCollection;
		public var learnerProgress:LearnerProgress;
		
		public var lessonID:Number; 
		public var learnerID:Number;
		
		/*-.........................................Constructor..........................................*/
		
		public function ChartEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			lessonID = Application.application.parameters.lessonID;
			learnerID = Application.application.parameters.learnerID;
			
			// determine if we are loading chart data for class or a single learner
			if(type == LOAD) {
				if(lessonID && learnerID)
					type = LOAD_LEARNER;
				else
					type = LOAD_CLASS;
			}
				
			super(type, bubbles, cancelable);
			
		}
		
	}
}