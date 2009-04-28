package {
	import flash.events.*;
	public class ServerMindMapEvent extends Event {
		static public var FAILED_INCOMING_CONNECTION:String = "failedIncomingConnection";
		static public var WIN_INCOMING_CONNECTION:String = "winIncomingConnection";
		static public var FAILED_OUTGOING_CONNECTION:String = "failedOutgoingConnection";
		static public var WIN_OUTGOING_CONNECTION:String = "winOutgoingConnection";
		public function ServerMindMapEvent(type:String){
			super(type);
		}
	}
}