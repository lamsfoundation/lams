package org.lamsfoundation.lams.common.util
{
	import flash.display.BitmapData;
	import flash.events.Event;

	public class ScreenShotterEvent extends Event
	{
		public static const SCREENSHOTTAKEN:String = "screenShotTaken";
		
		private var _previewImage:BitmapData;
		
		public function ScreenShotterEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
		public function set previewImage(myPreviewImage:BitmapData):void{
			_previewImage = myPreviewImage;
		}
		
		public function get previewImage():BitmapData{
			return _previewImage;
		}
		
	}
}