package {
	import flash.events.*;
	public class InformationEvent extends Event {
		static public var INFORMATION:String = "information";
		static public var NO_INFORMATION:String = "noInformation";
		public var message:String;
		public function InformationEvent(type:String, message:String=""){
			super(type);
			this.message = message;
		}
	}
}