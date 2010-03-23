package org.lamsfoundation.lams.author.events
{
	import org.lamsfoundation.lams.author.components.activity.ActivityComponent;
	import org.lamsfoundation.lams.author.components.transition.TransitionComponent;
	
	
	public class TransitionEvent extends AuthorEvent
	{
		public static const TRANSITION_COMPLETE:String = "transitionComplete";
		public static const DELETE_TRANSITION:String = "deleteTransition";
		
		
		public var sourceActivityComponent:ActivityComponent;
		public var transition:TransitionComponent;
		public var localY:int;
		public var localX:int;
		
		public function TransitionEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

	}
}