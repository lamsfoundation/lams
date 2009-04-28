package {
	import flash.text.*;
	public class BaseConcept extends Concept{
		public function BaseConcept(word:String, color:int, free:Boolean, owned:Boolean, creator:String, id:int){
			super(word, color, free, owned, creator, id);
		}
		override protected function createTextFormat():TextFormat {
			var textFormat:TextFormat = super.createTextFormat();
			textFormat.size = 24;
			textFormat.bold = true;
			return textFormat;
		}
		override protected function initCircleGraphics():void {
			super.initCircleGraphics();
			this.circle.graphics.lineStyle(2, 0x232323, 1);
		}
	}
}