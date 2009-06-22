package org.lamsfoundation.lams.events
{
	import flash.events.Event;

	public class LearnerErrorEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const SHOW_ERROR:String = "showErrorEvent";
		
		
		/*-.........................................Properties..........................................*/
		public var message:String;
		/*-.........................................Constructor..........................................*/
		
		public function LearnerErrorEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}