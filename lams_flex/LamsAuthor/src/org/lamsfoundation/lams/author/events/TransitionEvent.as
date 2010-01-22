package org.lamsfoundation.lams.author.events
{
	import org.lamsfoundation.lams.author.components.activity.ActivityComponent;
	import org.lamsfoundation.lams.author.components.transition.TransitionComponent;
	
	
	public class TransitionEvent extends AuthorEvent
	{
		public static const TRANSITION_EVENT:String = "transitionEvent";
		public var sourceAcivityComponent:ActivityComponent;
		public var transition:TransitionComponent;
		public var localY:int;
		public var localX:int;
		
		public function TransitionEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

	}
}