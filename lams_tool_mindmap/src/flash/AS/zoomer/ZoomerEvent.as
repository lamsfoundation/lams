package zoomer {
	import flash.events.*;
	public class ZoomerEvent extends Event {
		public function ZoomerEvent(type:String){
			super(type);
		}
		static public var ZOOM_IN:String = "zoomIn";
		static public var ZOOM_OUT:String = "zoomOut";
		static public var STOP_ZOOMING:String = "stopZooming";
	}
}