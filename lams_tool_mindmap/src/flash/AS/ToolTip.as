package {
	import flash.text.*;
	import flash.events.*;
	import flash.display.*;
	public class ToolTip extends Sprite {
		protected var textField:TextField;
		protected var showing:Boolean;
		public function ToolTip() {
			this.textField = new TextField();
			this.textField.height = 20;
			this.textField.multiline = false;
			this.textField.autoSize = TextFieldAutoSize.LEFT;
			this.textField.type = TextFieldType.DYNAMIC;
			this.textField.selectable = false;
			var textFormat:TextFormat = new TextFormat("Georgia", 12, 0x262626);
			this.textField.defaultTextFormat = textFormat;
			this.addChild(this.textField);
			this.visible = false;
			this.showing = false;
		}
		public function show(word:String):void {
			if (word!="") {
				this.textField.htmlText = word;
				this.visible = true;
			}
		}
		public function hide():void {
			this.visible = false;
		}
	}
}