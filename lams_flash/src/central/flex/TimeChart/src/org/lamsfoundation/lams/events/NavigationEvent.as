package org.lamsfoundation.lams.events
{
	import org.lamsfoundation.lams.vos.*;
	
	import flash.events.Event;

	public class NavigationEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const CHART: String = "chartNavigationEvent"
		
		/*-.........................................Constructor..........................................*/	
		public function NavigationEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}