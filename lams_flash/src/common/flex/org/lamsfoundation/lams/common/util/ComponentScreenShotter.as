package org.lamsfoundation.lams.common.util
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.events.EventDispatcher;
	import flash.geom.Rectangle;
	import flash.utils.ByteArray;
	
	import mx.core.UIComponent;
	
	public class ComponentScreenShotter extends EventDispatcher
	{
		private var _componentToCapture:UIComponent;
		
		public function ComponentScreenShotter(componentToCapture:UIComponent = null)
		{
			_componentToCapture = componentToCapture;
		}
		
		public function takeScreenshot():void{
			if(_componentToCapture){
			 	// create a new preview image
			 	var previewImage:BitmapData = new BitmapData(_componentToCapture.width, _componentToCapture.height) ;
			 	
			 	// draw the screenshoter frame
				previewImage.draw(_componentToCapture);
				
				// make a bitmap
				var bm:Bitmap = new Bitmap(previewImage);
	
				// get the raw data
				var bytes:ByteArray = previewImage.getPixels(new Rectangle(0, 0, _componentToCapture.width, _componentToCapture.height));
																
				var screenShotEvent:ScreenShotterEvent = new ScreenShotterEvent(ScreenShotterEvent.SCREENSHOTTAKEN);
				screenShotEvent.previewImage = previewImage;
				
				dispatchEvent(screenShotEvent);
			}	 	
		}
		
		public function set componentToCapture(componentToCapture:UIComponent):void{
			_componentToCapture = componentToCapture;
		}

	}
}