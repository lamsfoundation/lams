package org.lamsfoundation.lams.events
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.vos.Lesson;

	public class LearnerEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const GET_LESSON:String = "getLessonEvent";
		public static const JOIN_LESSON:String = "joinLessonEvent";
		public static const RESUME_LESSON:String = "resumeLessonEvent";
		public static const EXIT_LESSON:String = "exitLessonEvent";
		public static const GET_FLASH_PROGRESS:String = "getFlashProgressEvent";
		public static const GET_LEARNING_DESIGN_DETAILS:String = "getLearningDesignDetailsEvent";
		
		/*-.........................................Properties..........................................*/

		public var lessonID:uint;
		public var lesson:Lesson;
		
		/*-.........................................Constructor..........................................*/
		
		public function LearnerEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}