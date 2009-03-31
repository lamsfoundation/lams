package org.lamsfoundation.lams.events
{
	import flash.events.Event;

	public class WizardErrorEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const SHOW_ERROR:String 					= "showErrorEvent";
		
		
		/*-.........................................Properties..........................................*/
		public var message:String;
		/*-.........................................Constructor..........................................*/
		
		public function WizardErrorEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}