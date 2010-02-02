package org.lamsfoundation.lams.author.events
{
	import flash.events.Event;
	import org.lamsfoundation.lams.author.components.activity.ActivityComponent;

	public class AuthorActivityEvent extends Event
	{
		public static const DELETE_ACTIVITY_EVENT:String = "deleteActivityEvent";
		public var activityComponent:ActivityComponent;
		
		public function AuthorActivityEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}