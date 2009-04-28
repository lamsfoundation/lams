package {
	import flash.display.*;
	import flash.text.*;
	import flash.geom.*;
	import flash.filters.*;
	import flash.events.*;
	import fl.motion.*;
	import flash.xml.*;
	import basiccolorpicker.*;
	import actions.*;
	public class Concept extends Sprite {
		static public var DEFAULT_TITLE:String;
		protected var textField:TextField;
		public function get word():String {
			return this.textField.text;
		}
		public function set word(word:String) {
			if(word=="")word = " ";
			this.textField.text = word;
			this.fixText();
			this.proper();
		}
		protected var _color:int;
		public function get color():int {
			return this._color;
		}
		public function set color(color:int) {
			this._color = color;
			this.drawCircle();
		}
		protected var circle:Sprite;
		public function get box():Rectangle {
			return this.circle.getRect(this);
		}
		protected var _branch:Branch;
		public function get branch():Branch {
			return this._branch;
		}
		public function set branch(branch:Branch) {
			this._branch = branch;
		}
		protected function get buttonCount():int {
			return 1 + (this.owned?1:0);
		}
		protected var _owned:Boolean
		public function get owned():Boolean {
			return this._owned;
		}
		public function set owned(owned:Boolean) {
			if(this.newSonButton!=null)this.removeChild(this.newSonButton);
			if(this.colorPicker!=null)this.removeChild(this.colorPicker);
			this._owned = owned;
			this.addButtons();
			this.formatText();
		}
		protected var id:int;
		public function get ID():int {
			return this.id;
		}
		public function set ID(value:int) {
			if(!this.free){
				this.id = value;
			}
		}
		protected function get newSonButtonQueue():int {
			return 0;
		}
		protected var newSonButton:NewNodeSign;
		protected var newSonAnimator:Animator;
		protected function get colorPickerQueue():int {
			if(this.owned)return 1;
			return -1;
		}
		protected var colorPicker:BasicColorPicker;
		protected var colorPickerAnimator:Animator;
		protected var free:Boolean;
		protected var textUnsent:Boolean;
		protected var oldText:String;
		protected var firstTime:Boolean;
		protected var block:Boolean;
		public function get blocked():Boolean {
			return this.block;
		}
		public function set blocked(value:Boolean) {
			this.block = value;
			if(value){
				if(this.owned) this.textField.type = TextFieldType.DYNAMIC;
			}else{
				if(this.owned) this.textField.type = TextFieldType.INPUT;
			}
			this.setHiding();
		}
		protected var shouldShow:Boolean, shouldHide:Boolean, isShowing:int, isHiding:int, isShown:Boolean;
		protected const buttonFloat:Number=12;
		protected const defaultColor:int=0xFFFFFF;
		protected var _creator:String;
		public function get creator():String {
			return this._creator;
		}
		public function Concept(word:String, color:int, free:Boolean, owned:Boolean, creator:String, id:int) {
			this.free = free;
			this.textUnsent = false;
			this.firstTime = false;
			this._owned = owned;
			this._creator = creator;
			this._branch = null;
			this.circle = new Sprite();
			this.addChild(circle);
			this.textField = new TextField();
			this.textField.autoSize = TextFieldAutoSize.CENTER;
			this.textField.maxChars = 100;
			this.formatText();
			this.addChild(this.textField);
			this.textField.addEventListener(FocusEvent.FOCUS_OUT, onTextBlur);
			this.addButtons();
			this.word = word;
			this.oldText = word;
			this.color = color;
			this.shouldShow = false;
			this.shouldHide = false;
			this.isShown = false;
			this.isShowing = 0;
			this.isHiding = 0;
			this.blocked = false;
			this.id = id;
		}
		public function turnArrow(left:Boolean){
			if(left){
				this.newSonButton.scaleX = Math.abs(this.newSonButton.scaleX);
			}else{
				this.newSonButton.scaleX = -Math.abs(this.newSonButton.scaleX);
			}
		}
		public function setShowing():void {
			if(!this.blocked){
				this.shouldShow = true;
				this.shouldHide = false;
				if (this.isHiding==0 && this.isShowing==0 && !this.isShown) {
					this.isShowing = this.buttonCount;
					this.startShowAnimations();
				}
			}
		}
		public function setHiding():void {
			this.shouldHide = true;
			this.shouldShow = false;
			if (this.isHiding==0 && this.isShowing==0 && this.isShown) {
				this.isHiding = this.buttonCount;
				this.isShown = false;
				this.startHideAnimations();
			}
		}
		public function toXml():XML {
			var result:XML = <concept/>
			result.text = this.word;
			result.color = this.color.toString(16);
			result.creator = this.creator;
			result.id = this.id;
			return result;
		}
		public function fromXml(xml:XML):void {
			this.color = (int)("0x"+xml.color);
			this.word = xml.text;
			this._creator = xml.creator;
			this.id = int(xml.id);
			this.owned = xml.edit=="0"?false:true;
			if(this.branch!=null){
				this.branch.mindMap.allowConceptId(this.id);
			}
		}
		protected function formatText():void {
			this.textField.defaultTextFormat = this.createTextFormat();
			if (this.owned) {
				this.textField.type = TextFieldType.INPUT;
				this.textField.addEventListener(Event.CHANGE, this.onTextChange);
			} else {
				this.textField.type = TextFieldType.DYNAMIC;
			}
		}
		protected function createTextFormat():TextFormat {
			var textFormat:TextFormat = new TextFormat();
			textFormat.align = TextFormatAlign.CENTER;
			textFormat.font = "Georgia";
			return textFormat;
		}
		protected function startShowAnimations():void {
			this.newSonButton.visible=true;
			this.newSonAnimator = this.createEllipseShowAnimator(this.newSonButton, this.newSonButtonQueue);
			this.newSonAnimator.addEventListener(MotionEvent.MOTION_END, this.onShowEnd);
			this.newSonAnimator.play();
			if(this.colorPickerQueue>=0){
				this.colorPicker.visible = true;
				this.colorPickerAnimator = this.createEllipseShowAnimator(this.colorPicker, this.colorPickerQueue);
				this.colorPickerAnimator.addEventListener(MotionEvent.MOTION_END, this.onShowEnd);
				this.colorPickerAnimator.play();
			}
		}
		protected function startHideAnimations():void {
			this.newSonAnimator = this.createEllipseHideAnimator(this.newSonButton, this.newSonButtonQueue);
			this.newSonAnimator.addEventListener(MotionEvent.MOTION_END, this.onHideEnd);
			this.newSonAnimator.play();
			if(this.colorPickerQueue>=0){
				this.colorPickerAnimator = this.createEllipseHideAnimator(this.colorPicker, this.colorPickerQueue);
				this.colorPickerAnimator.addEventListener(MotionEvent.MOTION_END, this.onHideEnd);
				this.colorPickerAnimator.play();
			}
		}
		protected function addButtons():void {
			this.newSonButton = new NewNodeSign();
			this.newSonButton.width = 20;
			this.newSonButton.height = 20;
			this.addChild(this.newSonButton);
			this.newSonButton.addEventListener(MouseEvent.CLICK, this.onClickNewSon);
			this.newSonButton.addEventListener(MouseEvent.MOUSE_OVER, this.onHoverNewSon);
			this.newSonButton.addEventListener(MouseEvent.MOUSE_OUT, this.onOutNewSon);
			this.newSonButton.visible = false;
			if(this.owned){
				this.colorPicker = new BasicColorPicker(15, [0xFFFFFF, 0x0909EE, 0x6699CC, 0xEE09EE, 0x999999, 0x6633BB ,0xEE0909, 0x090909, 0xEEEE09, 0xEE6609, 0x663309, 0x09EE09, 0x666609, 0x09EEEE, 0x095509]);
				this.addChild(this.colorPicker);
				this.colorPicker.addEventListener(BasicColorPickerEvent.PICK_COLOR, this.onPickColor);
				this.colorPicker.visible = false;
			}
		}
		protected function placeButtons():void {
			var newSonButtonPosition:Point = this.getEllipseAnimationPoint(this.getButtonEndPosition(this.newSonButtonQueue, this.buttonFloat), this.buttonFloat);
			this.newSonButton.x = newSonButtonPosition.x;
			this.newSonButton.y = newSonButtonPosition.y;
			if(this.colorPickerQueue>=0){
				var colorPickerPosition:Point = this.getEllipseAnimationPoint(this.getButtonEndPosition(this.colorPickerQueue, this.buttonFloat), this.buttonFloat);
				this.colorPicker.x = colorPickerPosition.x;
				this.colorPicker.y = colorPickerPosition.y;
			}
		}
		protected function proper():void {
			var glowing:GlowFilter = new GlowFilter(0xffffff, 1, 5, 5, 3, 3);
			var wordFilters:Array = [glowing];
			this.textField.filters = wordFilters;
			this.textField.x = -this.textField.width/2;
			this.textField.y = -this.textField.height/2;
			this.drawCircle();
			this.placeButtons();
			this.dispatchEvent(new Event(Event.CHANGE));
		}
		protected function initCircleGraphics():void {
			this.circle.graphics.clear();
		}
		protected function drawCircle():void {
			this.initCircleGraphics();
			var angle:Number = Math.PI/4;
			var X:Number,Y:Number;
			var a:Number,b:Number;
			b = this.textField.height/2*1.8;
			a = b*this.textField.width/2/Math.sqrt(b*b - Math.pow(this.textField.height/2,2));
			a = Math.max(a, 25);
			var tg = Math.tan(angle);
			X = -(a*a*tg)/Math.sqrt(a*a*tg*tg+b*b);
			Y = -b*Math.sqrt(a*a-X*X)/a;
			var matrix:Matrix;
			matrix = new Matrix();
			matrix.createGradientBox(2*Math.abs(X), 2*Math.abs(Y), Math.PI/2 - Math.atan(tg*Math.abs(X)/Math.abs(Y)), X, Y);
			var mainColor:BasicColor, lightColor:BasicColor, darkColor:BasicColor;
			mainColor = new BasicColor();
			mainColor.color = this.color;
			lightColor = new BasicColor();
			lightColor.color = this.color;
			lightColor.L += 0.15;
			darkColor = new BasicColor();
			darkColor.color = this.color;
			darkColor.L -= (lightColor.L - mainColor.L);
			this.circle.graphics.beginGradientFill(GradientType.LINEAR, [lightColor.color, this.color, darkColor.color], [1,1,1], [0, 127, 255], matrix);
			this.circle.graphics.drawEllipse(-a, -b, 2*a, 2*b);
			this.circle.graphics.endFill();
		}
		public function getEllipseAnimationPoint(xRatio:Number, float:Number):Point {
			var pX:Number, pY:Number;
			var a:Number, b:Number;
			a=this.box.width/2;
			b=this.box.height/2;
			var u:Number;
			u=xRatio*a;
			var bigSqrt:Number = Math.sqrt(b*b*u*u+a*a*(a*a-u*u));
			var smallSqrt:Number = Math.sqrt(a*a-u*u);
			pX = float*b*u/bigSqrt+u;
			pY = -b/a*smallSqrt-float*a*smallSqrt/bigSqrt;
			return new Point(pX,pY);
		}
		protected function getButtonEndPosition(index:int, float:Number):Number {
			if (index==0) {
				return 0.95;
			} else {
				var previousRatio:Number = this.getButtonEndPosition(index-1, float);
				var previousPosition:Point = this.getEllipseAnimationPoint(previousRatio, float);
				var thisRatio:Number = previousRatio-0.01;
				while (Point.distance(previousPosition, this.getEllipseAnimationPoint(thisRatio, float)) < 20) {
					thisRatio-=0.01;
				}
				return thisRatio;
			}
		}
		protected function getButtonStartPosition(index:int, float:int):Number {
			return -0.95 + index * 0.1;
		}
		protected function createEllipseShowAnimator(object:Sprite, index:int):Animator {
			var animator:Animator = new Animator(null, object);
			animator.motion.keyframes = new Array();
			var startRatio:Number, endRatio:Number;
			startRatio = this.getButtonStartPosition(index, this.buttonFloat);
			endRatio = this.getButtonEndPosition(index, this.buttonFloat);
			var i:int;
			var F1:int =9, F2:int =12, F3:int =14;
			animator.motion.duration = F3+1;
			var position:Point;
			var frame:Keyframe;
			for (i=0; i<=F1; i++) {
				animator.motion.keyframes[i] = new Keyframe();
				frame = animator.motion.keyframes[i];
				position = this.getEllipseAnimationPoint((endRatio*1.05-startRatio)/(F1*F1)*i*i+startRatio, this.buttonFloat);
				frame.x = position.x-object.x;
				frame.y = position.y-object.y;
				frame.color = new Color(1,1,1,1/(F1*F1)*i*i);
				frame.index = i;
			}
			for (i=F1+1; i<=F2; i++) {
				animator.motion.keyframes[i] = new Keyframe();
				frame = animator.motion.keyframes[i];
				var A:Number, B:Number, C:Number;
				A = (endRatio*1.05 - endRatio*0.95)/Math.pow(F1-F2, 2);
				B = -2*A*F2;
				C = endRatio*0.95+A*F2*F2;
				position = this.getEllipseAnimationPoint(A*i*i+B*i+C, this.buttonFloat);
				frame.x = position.x-object.x;
				frame.y = position.y-object.y;
				frame.index = i;
			}
			for (i=F2+1; i<=F3; i++) {
				animator.motion.keyframes[i] = new Keyframe();
				frame = animator.motion.keyframes[i];
				position = this.getEllipseAnimationPoint((endRatio-endRatio*0.95)/ (Math.LOG2E * Math.log(F3-F2+1))*Math.LOG2E*Math.log(i-F2+1)+endRatio*0.95, this.buttonFloat);
				frame.x = position.x-object.x;
				frame.y = position.y-object.y;
				frame.index = i;
			}
			return animator;
		}
		protected function createEllipseHideAnimator(object:Sprite, index:int):Animator {
			var animator:Animator = new Animator(null, object);
			animator.motion.keyframes = new Array();
			var i:int;
			var F:int = 9;
			animator.motion.duration = F+1;
			var position:Point;
			var frame:Keyframe;
			for (i=0; i<=F; i++) {
				animator.motion.keyframes[i] = new Keyframe();
				frame = animator.motion.keyframes[i];
				frame.color = new Color(1,1,1,1-1/(F*F)*i*i);
				frame.index = i;
			}
			return animator;
		}
		protected function fixText(){
			if(this.textField.width > 300){
				this.textField.wordWrap = true;
				this.textField.width = 300;
			}else{
				if(this.textField.numLines==1){
					this.textField.wordWrap = false;
				}
			}
		}
		protected function onTextChange(event:Event):void {
			this.fixText();
			this.proper();
			if(!this.free && this.owned && !this.firstTime){
				this.textUnsent = true;
			}
		}
		protected function onClickNewSon(event:MouseEvent):void {
			if(this.isShown){
				var newId:int;
				if(this.free){
					newId = this.branch.mindMap.getNewConceptId()
				}else{
					newId = -this.branch.mindMap.getNewConceptId();
				}
				var newConcept:InternalConcept = new InternalConcept(DEFAULT_TITLE, this.defaultColor, this.free, true, this.branch.mindMap.player, newId);
				this.branch.addSon(newConcept);
				this.placeButtons();
				this.branch.expanded = true;
				this.branch.proper();
				if(!this.free){
					newConcept.blocked=true;
					newConcept.alpha = 0.6;
					Concept(newConcept).textField.type = TextFieldType.INPUT;
					Concept(newConcept).textField.setSelection(0, newConcept.word.length);
					Concept(newConcept).firstTime = true;
				}
				this.stage.focus = Concept(newConcept).textField;
				Concept(newConcept).textField.setSelection(0, newConcept.word.length);
			}
		}
		protected function onHoverNewSon(event:MouseEvent):void {
			this.newSonButton.transform.colorTransform = new ColorTransform(0, 0, 0, 1, 30, 200, 30, 0);
		}
		protected function onOutNewSon(event:MouseEvent):void {
			this.newSonButton.transform.colorTransform = new ColorTransform(1, 1, 1, 1, 0, 0, 0, 0);
		}
		protected function onShowEnd(event:MotionEvent):void {
			Animator(event.target).removeEventListener(MotionEvent.MOTION_END, this.onShowEnd);
			this.isShowing--;
			if (this.isShowing==0) {
				this.isShown = true;
				if (this.shouldHide) {
					this.setHiding();
				}
			}
		}
		protected function onHideEnd(event:MotionEvent):void {
			Animator(event.target).removeEventListener(MotionEvent.MOTION_END, this.onHideEnd);
			Animator(event.target).target.visible = false;
			this.isHiding--;
			if (this.isHiding == 0) {
				if (this.shouldShow) {
					this.setShowing();
				}
			}
		}
		protected function onPickColor(event:BasicColorPickerEvent):void {
			if(this.owned){
				if(!this.free){
					ServerMindMap(this.branch.mindMap).alertAction(new ColorAction(this.ID, this.colorPicker.picked), new ColorAction(this.ID, this.color));
				}
				this.color = this.colorPicker.picked;
				this.proper();
			}
		}
		protected function onTextBlur(event:FocusEvent):void {
			if(!this.free && this.owned){
				if(this.textUnsent){
					ServerMindMap(this.branch.mindMap).alertAction(new TextAction(this.ID, this.word), new TextAction(this.ID, this.oldText));
					this.textUnsent = false;
					this.oldText = this.word;
				}
				if(this.firstTime){
					ServerMindMap(this.branch.mindMap).alertAction(new CreateAction(InternalBranch(this.branch).father.base.ID, InternalConcept(this), this.ID), null);
					this.firstTime = false;
					this.alpha = 1;
					this.blocked = true;
				}
			}
		}
	}
}