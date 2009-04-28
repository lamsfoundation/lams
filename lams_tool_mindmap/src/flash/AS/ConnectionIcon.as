package {
	import flash.display.*;
	import flash.filters.*;
	public class ConnectionIcon extends MovieClip {
		protected var leftTraffic:Boolean, rightTraffic:Boolean;
		protected var full:Boolean;
		public function set incoming(value:Boolean){
			if(value!=this.leftTraffic){
				this.leftTraffic = value;
				this.display();
			}
		}
		public function set outgoing(value:Boolean){
			if(value!=this.rightTraffic){
				this.rightTraffic = value;
				this.display();
			}
		}
		public function ConnectionIcon(){
			this.filters = [new GlowFilter(0xFFFFFF, 1, 8, 8, 16, BitmapFilterQuality.HIGH)];
			this.gotoAndStop("idle");
			this.full = this.rightTraffic = this.leftTraffic = true;
		}
		protected function display():void {
			if(this.leftTraffic){
				if(this.rightTraffic){
					if(!this.full){
						this.full = true;
						this.gotoAndPlay("fullConnection");
					}
				}else{
					this.gotoAndStop("noOutgoing");
				}
			}else{
				if(this.rightTraffic){
					this.gotoAndStop("noIncoming");
				}else{
					this.gotoAndStop("brokenConnection");
				}
			}
		}
	}
}