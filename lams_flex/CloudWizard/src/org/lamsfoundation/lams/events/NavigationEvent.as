package org.lamsfoundation.lams.events
{
	import flash.events.Event;
	
	import org.lamsfoundation.lams.vos.*;

	public class NavigationEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const TAB_SELECT: String = "tabSelectNavigationEvent"
		public static const LESSON: String = "lessonNavigationEvent"
		public static const LEARNERS: String = "learnersNavigationEvent"
		public static const ADVANCED: String = "advancedNavigationEvent"
		public static const CONDITIONS: String = "conditionsNavigationEvent"
		
		public var selectedIndex:uint;
		
		/*-.........................................Constructor..........................................*/	
		public function NavigationEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}