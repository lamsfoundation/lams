package org.lamsfoundation.lams.author.model.transition
{
	import org.lamsfoundation.lams.author.model.activity.Activity;
	
	public class Transition
	{
		public var transitionUUID:int;
		public var toActivity:Activity;
		public var fromActivity:Activity;
		
		[Bindable] public var endX:int;
		[Bindable] public var endY:int;
		
		public function Transition(fromActivity:Activity, toActivity:Activity, transitionUUID:int) {
			this.transitionUUID = transitionUUID;
			this.fromActivity = fromActivity;
			this.toActivity = toActivity;
			
			this.toActivity.transitionTo = this;
			this.fromActivity.transitionFrom = this;
		}
	}
}