package org.lamsfoundation.lams.author.events
{
	import flash.events.Event;

	public class AuthorEvent extends Event
	{
		public function AuthorEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}