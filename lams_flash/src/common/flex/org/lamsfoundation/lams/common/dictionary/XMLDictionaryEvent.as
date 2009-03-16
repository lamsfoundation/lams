package org.lamsfoundation.lams.common.dictionary
{
	import flash.events.Event;

	public class XMLDictionaryEvent extends Event
	{
		
		public static const COMPLETE: String	= "completeXMLDictionaryEvent";

		public function XMLDictionaryEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}