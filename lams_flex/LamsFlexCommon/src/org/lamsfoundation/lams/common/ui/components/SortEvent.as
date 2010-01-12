package org.lamsfoundation.lams.common.ui.components
{
	import flash.events.Event;

	public class SortEvent extends Event
	{
		public static const EVENT_TYPE:String = "sortEvent";
		private var _sortBy:String;
		private var _sortDirection:String;
		
		public function SortEvent(sortBy:String, sortDirection:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(EVENT_TYPE, bubbles, cancelable);
			
			_sortBy = sortBy;
			_sortDirection = sortDirection;
		}
		
		public function get sortBy():String{
			return _sortBy;
		}
		
		public function get sortDirection():String{
			return _sortDirection;
		}
	}
}