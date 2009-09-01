package org.lamsfoundation.lams.common.ui.components
{
	import flash.events.Event;

	public class VideoDisplayEvent extends Event
	{
		public static const RESET:String = "videoDisplayReset";
		public static const READY:String = "videoDisplayReady";
		public static const READYSEEK:String = "videoDisplayReadySeek";
		public static const PAUSE:String = "videoDisplayPause";
		public static const PAUSESEEK:String = "videoDisplayPauseSeek";
		public static const UNPAUSE:String = "videoDisplayUnpause";
		public static const METADATA:String = "videoDisplayMetadata";
		public static const STARTCAM:String = "videoDisplayStartCam";
		public static const STOPCAM:String = "videoDisplayStopCam";
		public static const STARTPUBLISH:String = "videoDisplayStartPublish";
		public static const STOPPUBLISH:String = "videoDisplayStopPublish";
		public static const COMPLETE:String = "videoDisplayComplete";
		public static const BUFFERING:String = "videoDisplayBuffering";
		
		public var metadata:Object;
		
		public function VideoDisplayEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}