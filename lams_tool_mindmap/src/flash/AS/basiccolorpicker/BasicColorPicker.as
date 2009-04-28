package basiccolorpicker {
	import flash.display.*;
	import flash.filters.*;
	import fl.motion.*;
	import flash.events.*;
	public class BasicColorPicker extends Sprite{
		static protected var blurring:BlurFilter = new BlurFilter(2, 2, BitmapFilterQuality.HIGH);
		protected var plate:MovieClip;
		protected var enlarger:Animator, shrinker:Animator;
		protected var enlarged:Boolean, enlarging:Boolean, shrinking:Boolean, shouldEnlarge:Boolean, shouldShrink:Boolean;
		protected var rolled:ColorSlice;
		protected var color:int;
		public function get picked():int {
			return this.color;
		}
		public function BasicColorPicker(parts:int, colors:Array){
			this.addChild(this.plate = new MovieClip());
			this.plate.graphics.beginFill(0, 0);
			this.plate.graphics.drawCircle(0,0,70);
			this.plate.graphics.endFill();
			this.plate.graphics.beginFill(0xbbbbbb);
			this.plate.graphics.drawCircle(0,0,50);
			this.plate.scaleX = 0.12;
			this.plate.scaleY = 0.12;
			this.enlarging = false;
			this.shouldEnlarge = false;
			this.shrinking = false;
			this.shouldShrink = false;
			this.enlarged = false;
			var enlarger_xml:XML = 
				<Motion duration="15" xmlns="fl.motion.*" xmlns:geom="flash.geom.*" xmlns:filters="flash.filters.*">
					<source>
						<Source frameRate="60" x="0" y="0" rotation="0" elementType="movie clip" instanceName="plate" symbolName="Symbol 9">
							<dimensions>
								<geom:Rectangle left="-50" top="-50" width="100" height="100"/>
							</dimensions>
							<transformationPoint>
								<geom:Point x="0.5" y="0.5"/>
							</transformationPoint>
						</Source>
					</source>				
					<Keyframe index="0" label="min" tweenSnap="true" tweenSync="true">
						<tweens>
							<CustomEase>
								<geom:Point x="0.6162790697674418" y="0"/>
								<geom:Point x="0.999999991128611" y="0.5138888888888888"/>
							</CustomEase>
						</tweens>
					</Keyframe>				
					<Keyframe index="6" tweenSnap="true" tweenSync="true" scaleX="5" scaleY="5">
						<tweens>
							<CustomEase>
								<geom:Point x="1.5524930735075215e-8" y="0.6736111111111112"/>
								<geom:Point x="0.6325581395348837" y="1"/>
							</CustomEase>
						</tweens>
					</Keyframe>				
					<Keyframe index="11" tweenSnap="true" tweenSync="true" scaleX="4" scaleY="4">
						<tweens>
							<CustomEase>
								<geom:Point x="2.217847247867888e-8" y="0.71875"/>
								<geom:Point x="0.5069767441860465" y="1"/>
							</CustomEase>
						</tweens>
					</Keyframe>				
					<Keyframe index="14" label="max" tweenSnap="true" tweenSync="true" scaleX="4.5" scaleY="4.5">
						<tweens>
							<CustomEase>
								<geom:Point x="0.6767441860465117" y="0"/>
								<geom:Point x="0.999999991128611" y="0.3472222222222222"/>
							</CustomEase>
						</tweens>
					</Keyframe>
				</Motion>
			this.enlarger = new Animator(enlarger_xml, this.plate);
			this.enlarger.addEventListener(MotionEvent.MOTION_END, this.onEnlarge);
			var shrinker_xml:XML = 
				<Motion duration="15" xmlns="fl.motion.*" xmlns:geom="flash.geom.*" xmlns:filters="flash.filters.*">
					<source>
						<Source frameRate="60" x="0" y="0" rotation="0" elementType="movie clip" symbolName="Symbol 9">
							<dimensions>
								<geom:Rectangle left="-50" top="-50" width="100" height="100"/>
							</dimensions>
							<transformationPoint>
								<geom:Point x="0.5" y="0.5"/>
							</transformationPoint>
						</Source>
					</source>				
					<Keyframe index="0" label="max" tweenSnap="true" tweenSync="true" scaleX="4.5" scaleY="4.5">
						<tweens>
							<CustomEase>
								<geom:Point x="0.6767441860465117" y="0"/>
								<geom:Point x="0.999999991128611" y="0.3472222222222222"/>
							</CustomEase>
						</tweens>
					</Keyframe>				
					<Keyframe index="7" tweenSnap="true" tweenSync="true" scaleX="0.77" scaleY="0.77">
						<tweens>
							<CustomEase>
								<geom:Point x="1.7742777979473656e-8" y="0.75"/>
								<geom:Point x="0.6186046511627907" y="1"/>
							</CustomEase>
						</tweens>
					</Keyframe>				
					<Keyframe index="11" tweenSnap="true" tweenSync="true" scaleX="1.23" scaleY="1.23">
						<tweens>
							<CustomEase>
								<geom:Point x="2.5964476891348516e-8" y="0.672604239639338"/>
								<geom:Point x="0.4356021854494147" y="0.9226362282891749"/>
							</CustomEase>
						</tweens>
					</Keyframe>				
					<Keyframe index="14" label="min" tweenSnap="true" tweenSync="true" scaleX="1" scaleY="1">
						<tweens>
							<CustomEase>
								<geom:Point x="0.6162790697674418" y="0"/>
								<geom:Point x="0.999999991128611" y="0.5138888888888888"/>
							</CustomEase>
						</tweens>
					</Keyframe>
				</Motion>
			this.shrinker = new Animator(shrinker_xml, this.plate);
			this.shrinker.addEventListener(MotionEvent.MOTION_END, this.onShrink);
			var i:int;
			for(i=0;i<parts;i++){
				var slice:ColorSlice = new ColorSlice(colors[i], 2*Math.PI/parts, 50);
				this.plate.addChild(slice);
				slice.rotation = i*360/parts;
				slice.filters = [blurring];
				slice.addEventListener(MouseEvent.ROLL_OVER, onRollOverSlice);
				slice.addEventListener(MouseEvent.ROLL_OUT, onRollOutSlice);
			}
			this.color = colors[0];
			this.addEventListener(MouseEvent.MOUSE_DOWN, this.onUserIn);
			this.addEventListener(MouseEvent.MOUSE_UP, this.onUserUp);
			this.addEventListener(MouseEvent.ROLL_OUT, this.onUserOut);
		}
		protected function onUserIn(event:MouseEvent):void {
			if(!this.enlarged){
				this.shouldEnlarge = true;
				this.shouldShrink = false;
				if(!this.enlarging && !this.shrinking){
					this.enlarger.play();
					this.enlarging = true;
				}
			}else{
				this.onUserOut(event);
			}
		}
		protected function onUserOut(event:MouseEvent):void {
			this.shouldEnlarge = false;
			this.shouldShrink = true;
			if(!this.enlarging && !this.shrinking && this.enlarged){
				this.shrinker.play();
				this.shrinking = true;
				this.dispatchEvent(new BasicColorPickerEvent(BasicColorPickerEvent.PICK_COLOR));
			}
		}
		protected function onUserUp(event:MouseEvent):void {
			if(!this.enlarging && !this.shrinking && this.enlarged){
				this.shouldEnlarge = false;
				this.shouldShrink = true;
				this.shrinker.play();
				this.shrinking = true;
				this.dispatchEvent(new BasicColorPickerEvent(BasicColorPickerEvent.PICK_COLOR));
			}
		}
		protected function onEnlarge(event:MotionEvent):void {
			this.enlarging = false;
			this.enlarged = true;
			if(this.shouldShrink){
				this.shrinking = true;
				this.shrinker.play();
			}
		}
		protected function onShrink(event:MotionEvent):void {
			this.shrinking = false;
			this.enlarged = false;
			if(this.shouldEnlarge){
				this.enlarging = true;
				this.enlarger.play();
			}
		}
		protected function onRollOverSlice(event:MouseEvent):void {
			if(this.enlarged){
				this.rolled = (ColorSlice)(event.target);
				this.rolled.filters = [];
				this.color = this.rolled.color;
				this.dispatchEvent(new BasicColorPickerEvent(BasicColorPickerEvent.ROLL_COLOR));
			}
		}
		protected function onRollOutSlice(event:MouseEvent):void {
			if(this.enlarged && this.rolled!=null){
				this.rolled.filters = [blurring];
			}
		}
	}
}