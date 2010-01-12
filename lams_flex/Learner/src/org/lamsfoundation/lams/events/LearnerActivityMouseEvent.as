package org.lamsfoundation.lams.events
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.events.ListEvent;

	public class LearnerActivityMouseEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const SIMPLE_ACTIVITY_CLICK:String = "simpleActivityClick";
		public static const SIMPLE_ACTIVITY_DOUBLE_CLICK:String = "simpleActivityDoubleClick";
		public static const COMPLEX_ACTIVITY_CLICK:String = "complexActivityClick";
		public static const COMPLEX_ACTIVITY_DOUBLE_CLICK:String = "complexActivityDoubleClick";
		public static const COMPLEX_ACTIVITY_ITEM_CLICK:String = "complexActivityItemClick";
		public static const COMPLEX_ACTIVITY_ITEM_DOUBLE_CLICK:String = "complexActivityItemDoubleClick";
		
		public var mouseEvent:MouseEvent;
		public var listEvent:ListEvent;
		
		public function LearnerActivityMouseEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=true)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}