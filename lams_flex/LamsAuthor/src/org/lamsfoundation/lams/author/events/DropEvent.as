package org.lamsfoundation.lams.author.events
{
	public class DropEvent extends DragEvent
	{
		public static const DROP_EVENT:String = "dropEvent";
		
		public function DropEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}