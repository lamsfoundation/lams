package org.lamsfoundation.lams.author.events
{
	import flash.events.Event;
	
	import org.lamsfoundation.lams.author.components.activity.ToolActivityComponent;

	public class ToolActivityEvent extends Event
	{
		public static const GET_OUTPUT_DEFINITIONS:String = "getOutputDefinitions";
		
		public var toolActivityComponent:ToolActivityComponent;
		
		public function ToolActivityEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}