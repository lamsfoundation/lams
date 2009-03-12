package org.lamsfoundation.lams.events
{
	import flash.events.Event;

	public class DictionaryEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const LOAD: String 			= "loadEvent";
		public static const GET: String 			= "getEvent";
		public static const UPDATE: String			= "updateEvent";
		
		
		/*-.........................................Properties..........................................*/
		public var labelKey:String;
		public var result:String;

		/*-.........................................Constructor..........................................*/
		
		public function DictionaryEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}