package org.lamsfoundation.lams.author.events
{
	import flash.events.Event;

	public class AuthorEvent extends Event
	{
		public static const INIT_DATA_EVENT:String = "initDataEvent";
		
		public function AuthorEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}