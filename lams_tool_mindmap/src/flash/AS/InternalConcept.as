package {
	import flash.text.*;
	import flash.events.*;
	import fl.motion.*;
	import flash.geom.*;
	public class InternalConcept extends Concept{
		protected function get deleteButtonQueue():int {
			if(this.canDelete){
				if(this.free) {
					return 3;
				}
				return 2;
			}
			return -1;
		}
		protected var deleteButton:DeleteSign;
		protected var deleteAnimator:Animator;
		protected function get pickupButtonQueue():int {
			if(this.free && this.owned)return 2;
			return -1;
		}
		protected var pickupButton:HandSign;
		protected var pickupAnimator:Animator;
		protected function get canDelete():Boolean {
			return this.owned && (this.branch==null || this.branch.sonCount == 0);
		}
		override protected function get buttonCount():int {
			return super.buttonCount+(this.canDelete?1:0)+(this.free&&this.owned?1:0);
		}
		override public function set owned(owned:Boolean) {
			if(this.deleteButton!=null)this.removeChild(this.deleteButton);
			if(this.pickupButton!=null)this.removeChild(this.pickupButton);
			super.owned = owned;
		}
		public function InternalConcept(word:String, color:int, free:Boolean, owned:Boolean, player:String, id:int){
			super(word, color, free, owned, player, id);
		}
		override protected function createTextFormat():TextFormat {
			var textFormat:TextFormat = super.createTextFormat();
			textFormat.size = 16;
			textFormat.bold = false;
			return textFormat;
		}
		override protected function initCircleGraphics():void {
			super.initCircleGraphics();
			this.circle.graphics.lineStyle(1, 0x555555, 1);
		}
		override protected function startShowAnimations():void {
			super.startShowAnimations();
			if(this.pickupButtonQueue>=0){
				this.pickupButton.visible = true;
				this.pickupAnimator = this.createEllipseShowAnimator(this.pickupButton, this.pickupButtonQueue);
				this.pickupAnimator.addEventListener(MotionEvent.MOTION_END, this.onShowEnd);
				this.pickupAnimator.play();
			}
			if(this.deleteButtonQueue>=0){
				this.deleteButton.visible = true;
				this.deleteAnimator = this.createEllipseShowAnimator(this.deleteButton, this.deleteButtonQueue);
				this.deleteAnimator.addEventListener(MotionEvent.MOTION_END, this.onShowEnd);
				this.deleteAnimator.play();
			}
		}
		override protected function startHideAnimations():void {
			super.startHideAnimations();
			if(this.pickupButtonQueue>=0){
				this.pickupAnimator = this.createEllipseHideAnimator(this.pickupButton, this.pickupButtonQueue);
				this.pickupAnimator.addEventListener(MotionEvent.MOTION_END, this.onHideEnd);
				this.pickupAnimator.play();
			}
			if(this.deleteButtonQueue>=0){
				this.deleteAnimator = this.createEllipseHideAnimator(this.deleteButton, this.deleteButtonQueue);
				this.deleteAnimator.addEventListener(MotionEvent.MOTION_END, this.onHideEnd);
				this.deleteAnimator.play();
			}
		}
		override protected function addButtons():void {
			this.deleteButton = new DeleteSign();
			this.deleteButton.addEventListener(MouseEvent.CLICK, this.onClickDelete);
			this.deleteButton.addEventListener(MouseEvent.MOUSE_OVER, this.onHoverDelete);
			this.deleteButton.addEventListener(MouseEvent.MOUSE_OUT, this.onOutDelete);
			this.deleteButton.width = 20;
			this.deleteButton.height = 20;
			this.addChild(this.deleteButton);
			this.deleteButton.visible = false;
			if(this.owned && this.free){
				this.pickupButton = new HandSign();
				this.addChild(this.pickupButton);
				this.pickupButton.addEventListener(MouseEvent.MOUSE_DOWN, this.onClickHand);
				this.pickupButton.visible = false;
			}
			super.addButtons();
		}
		override protected function placeButtons():void {
			super.placeButtons();
			if(this.pickupButtonQueue>=0){
				var pickupButtonPosition:Point = this.getEllipseAnimationPoint(this.getButtonEndPosition(this.pickupButtonQueue, this.buttonFloat), this.buttonFloat);
				this.pickupButton.x = pickupButtonPosition.x;
				this.pickupButton.y = pickupButtonPosition.y;
			}
			if(this.deleteButtonQueue>=0){
				var deleteButtonPosition:Point = this.getEllipseAnimationPoint(this.getButtonEndPosition(this.deleteButtonQueue, this.buttonFloat), this.buttonFloat);
				this.deleteButton.x = deleteButtonPosition.x;
				this.deleteButton.y = deleteButtonPosition.y;
			}else{
				this.deleteButton.alpha=0;
			}
		}
		protected function onClickDelete(event:MouseEvent):void {
			if(this.canDelete && this.isShown){
				this.branch.mindMap.requestDelete(this);
			}
		}
		protected function onHoverDelete(event:MouseEvent):void {
			var keepAlpha:Number = this.deleteButton.alpha;
			this.deleteButton.transform.colorTransform = new ColorTransform(0, 0, 0, 1, 200, 30, 30, 0);
			this.deleteButton.alpha = keepAlpha;
		}
		protected function onOutDelete(event:MouseEvent):void {
			var keepAlpha:Number = this.deleteButton.alpha;
			this.deleteButton.transform.colorTransform = new ColorTransform(1, 1, 1, 1, 0, 0, 0, 0);
			this.deleteButton.alpha = keepAlpha;
		}
		protected function onClickHand(event:MouseEvent):void {
			if(this.isShown){
				this.branch.mindMap.requestHandMove(this, new Point(this.pickupButton.x, this.pickupButton.y));
			}
		}
	}
}