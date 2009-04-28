package basiccolorpicker {
	import flash.events.*;
	public class BasicColorPickerEvent extends Event {
		public function BasicColorPickerEvent(type:String){
			super(type);
		}
		static public var ROLL_COLOR:String = "rollColor";
		static public var PICK_COLOR:String = "pickColor";
	}
}