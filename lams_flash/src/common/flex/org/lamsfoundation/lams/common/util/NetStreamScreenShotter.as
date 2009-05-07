package org.lamsfoundation.lams.common.util
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.events.NetStatusEvent;
	import flash.events.TimerEvent;
	import flash.geom.Rectangle;
	import flash.media.SoundTransform;
	import flash.media.Video;
	import flash.net.NetConnection;
	import flash.net.NetStream;
	import flash.utils.ByteArray;
	import flash.utils.Timer;
	
	import mx.core.Application;
	import mx.core.UIComponent;
	
	public class NetStreamScreenShotter extends UIComponent
	{
		private var _nc:NetConnection;
		private var _filename:String;
		private var _bufferTime:int = 2;
		
		private var ssTimer:Timer;
		private var screenshotNs:NetStream;
		private var screenshotVideo:Video;
			
		public function NetStreamScreenShotter()
		{
			super();

			this.visible = false;
			this.width = 320;
			this.height = 240;
			
			// create new video object
			screenshotVideo = new Video();
			screenshotVideo.width = this.width;
			screenshotVideo.height = this.height;
				
			// fix video player
			this.addChild(screenshotVideo);
			screenshotVideo.visible = true;
			
			// add the ui component to the application
			Application.application.addChild(this);
		}
		
		public function set nc(myNc:NetConnection):void{
			_nc = myNc;
		}
		
		public function set bufferTime(myBufferTime:int):void{
			_bufferTime = myBufferTime;
		}
		
		public function set filename(myFilename:String):void{
			_filename = myFilename;
		}
		
		public function takeScreenshot():void{
			if(screenshotNs != null){
				screenshotNs.close();
				screenshotNs = null;
			}
			
			// create the screenshot netstream
			screenshotNs = createNetStreamForSS();
			
			// attach the netstream to the video
			screenshotVideo.attachNetStream(screenshotNs);
			
			// stream from the server
			screenshotNs.play(_filename, -2);
		}
		
		// creates a new netstream object for screenshotting
		private function createNetStreamForSS():NetStream{
			// if nc is not null
			if(_nc){
				// create a net stream object
				var ns:NetStream = new NetStream(_nc);
				ns.bufferTime = _bufferTime * 1000;
				
				// listeners and callback stuff for netstatus object
				var client:Object = new Object();
				client.onMetaData = function():void{};
				client.onPlayStatus = function():void{};
				ns.client = client;
				
				// mute the net stream
				ns.soundTransform = new SoundTransform(0);
				
				// connect the netstatus handler
				ns.addEventListener(NetStatusEvent.NET_STATUS, nsStatusHandlerForSS);
									
				return ns;
			}
			else
				return null;
		}
		
		private function nsStatusHandlerForSS(event:NetStatusEvent):void {
			// get the info object
			var infoObject:Object = event.info;
			
			trace(VideoDisplayUtil.printInfoObject(infoObject));
			
			switch(infoObject.code){
				case "NetStream.Play.Start":{					
					if(ssTimer != null){
						ssTimer.stop();
						ssTimer = null;
					}
					
					ssTimer = new Timer(_bufferTime * 1000, 1);
					ssTimer.addEventListener(TimerEvent.TIMER_COMPLETE, returnScreenShot);
					ssTimer.start();
					break;
				}
			}
		}
		
		private function returnScreenShot(event:TimerEvent = null):void{
			// detach the netstream for security reasons			
			screenshotVideo.attachNetStream(null);
		 	
		 	// create a new preview image
		 	var previewImage:BitmapData = new BitmapData(this.width, this.height) ;
		 	
		 	// draw the screenshoter frame
			previewImage.draw(this);
			
			// make a bitmap
			var bm:Bitmap = new Bitmap(previewImage);

			// get the raw data
			var bytes:ByteArray = previewImage.getPixels(new Rectangle(0, 0, 320, 240));
											
			screenshotNs.close();
			screenshotNs = null;
			
			var screenShotEvent:ScreenShotterEvent = new ScreenShotterEvent(ScreenShotterEvent.SCREENSHOTTAKEN, true, true);
			screenShotEvent.previewImage = previewImage;
			
			dispatchEvent(screenShotEvent);
		}

	}
}