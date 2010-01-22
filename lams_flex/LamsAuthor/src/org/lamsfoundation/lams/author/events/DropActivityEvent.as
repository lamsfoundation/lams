package org.lamsfoundation.lams.author.events
{
	import mx.events.DragEvent;
	
	public class DropActivityEvent extends DragEvent
	{
		public static const DROP_ACTIVITY_EVENT:String = "dropActivityEvent";
		
		public function DropActivityEvent(type:String, event:DragEvent, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.dragInitiator = event.dragInitiator;
			this.dragSource = event.dragSource;
			this.action = event.action;
			this.ctrlKey = event.ctrlKey;
			this.altKey = event.altKey;
			this.shiftKey = event.shiftKey;
		}
	}
}