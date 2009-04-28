package basiccolorpicker {
	import flash.display.*;
	public class ColorSlice extends Sprite {
		protected var _color:int;
		public function get color():int {
			return this._color;
		}
		public function ColorSlice(color:int, angle:Number, radius:Number){
			this._color = color;
			with (this.graphics){
				beginFill(color);
				lineTo(radius, 0);
				curveTo(radius, -radius*Math.tan(angle/2), radius*Math.cos(angle), -radius*Math.sin(angle));
				lineTo(0,0);
				endFill();
			}
		}
	}
}