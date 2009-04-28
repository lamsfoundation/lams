package zoomer{
	import flash.display.*;
	import flash.events.*;
	import flash.geom.*;
	public class Zoomer extends MovieClip {
		protected static var ACTIVE_FRAME:int;
		protected static var IDLE_FRAME:int;
		protected function set idle(value:Boolean) {
			if(value){
				if(this.currentFrame!=IDLE_FRAME){
					if(this.currentFrame < ACTIVE_FRAME){
						if(this.currentFrame == IDLE_FRAME+1) this.gotoAndStop("IDLE_FRAME");
						else this.gotoAndPlay(this.totalFrames+2 -(this.currentFrame-IDLE_FRAME));
					}else{
						this.play();
					}
				}else{
					this.stop();
				}
			}else{
				if(this.currentFrame!=ACTIVE_FRAME){
					if(this.currentFrame > ACTIVE_FRAME){
						if(this.currentFrame == ACTIVE_FRAME+1) this.gotoAndStop("ACTIVE_FRAME");
						else this.gotoAndPlay(ACTIVE_FRAME+1 -(this.currentFrame-ACTIVE_FRAME));
					}else{
						this.play();
					}
				}else{
					this.stop();
				}
			}
		}
		public function Zoomer(){
			var i:int;
			for(i=0;i<this.currentLabels.length;i++){
				if(this.currentLabels[i].name=="activeState") ACTIVE_FRAME = this.currentLabels[i].frame;
				if(this.currentLabels[i].name=="idleState") IDLE_FRAME = this.currentLabels[i].frame;
			}
			this.plus.addEventListener(MouseEvent.MOUSE_DOWN, this.onPlusPress);
			this.minus.addEventListener(MouseEvent.MOUSE_DOWN, this.onMinusPress);
			this.minus.addEventListener(MouseEvent.MOUSE_UP, this.onReleaseZoom);
			this.plus.addEventListener(MouseEvent.MOUSE_UP, this.onReleaseZoom);
			this.addEventListener(Event.ENTER_FRAME, this.onFrame);
		}
		protected function onPlusPress(event:MouseEvent):void {
			this.dispatchEvent(new ZoomerEvent(ZoomerEvent.ZOOM_IN));
		}
		protected function onMinusPress(event:MouseEvent):void {
			this.dispatchEvent(new ZoomerEvent(ZoomerEvent.ZOOM_OUT));
		}
		protected function onReleaseZoom(event:MouseEvent):void {
			this.dispatchEvent(new ZoomerEvent(ZoomerEvent.STOP_ZOOMING));
		}
		protected function onFrame(event:Event):void {
			if(Point.distance(new Point(this.mouseX, this.mouseY), new Point())<=20){
				this.idle = false;
			} else {
				this.idle = true;
			}
		}
	}
}