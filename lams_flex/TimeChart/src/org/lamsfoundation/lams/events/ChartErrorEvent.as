package org.lamsfoundation.lams.events
{
	import flash.events.Event;

	public class ChartErrorEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const SHOW_ERROR:String 					= "showErrorEvent";
		
		
		/*-.........................................Properties..........................................*/
		public var message:String;
		/*-.........................................Constructor..........................................*/
		
		public function ChartErrorEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}