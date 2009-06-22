package org.lamsfoundation.lams.events
{
	import flash.events.Event;
	
	import org.lamsfoundation.lams.vos.Activity;

	public class LearnerActivityEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const LAUNCH_ACTIVITY:String = "launchActivity";
		
		/*-.........................................Properties..........................................*/

		public var activity:Activity;
		public var activityID:uint;
		public var lessonID:uint;
		public var userID:uint;
		
		/*-.........................................Constructor..........................................*/
		
		public function LearnerActivityEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=true)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}