package {
	import flash.display.*;
	import flash.events.*;
	import flash.text.*;
	import flash.filters.*;
	import org.lamsfoundation.lams.common.dictionary.*;
	public class DeleteMessage extends Sprite{
		protected var mindMap:MindMap;
		public var concept:InternalConcept;
		public function get isShown():Boolean {
			return this.visible;
		}
		protected var question:Sprite;
		public function DeleteMessage(mindMap:MindMap, dictionary:XMLDictionary){
			this.mindMap = mindMap;
			this.question = new Sprite();
			this.addChild(this.question);
			var format:TextFormat = new TextFormat("Comic Sans MS", 32, 0xFF6633);
			var questionText:TextField = new TextField();
			questionText.defaultTextFormat = format;
			questionText.autoSize = TextFieldAutoSize.CENTER;
			questionText.selectable = false;
			questionText.text = dictionary.getLabel("local.delete_question");
			question.addChild(questionText);
			var yesButton:Sprite = new Sprite();
			var yesText:TextField = new TextField();
			yesText.defaultTextFormat = format;
			yesText.autoSize = TextFieldAutoSize.CENTER;
			yesText.selectable = false;
			yesText.text = dictionary.getLabel("local.yes");
			yesButton.addChild(yesText);
			yesButton.buttonMode = true;
			yesText.x = -yesText.width/2;
			yesText.y = -yesText.height/2;
			this.question.addChild(yesButton);
			yesButton.y = 60;
			yesButton.addEventListener(MouseEvent.CLICK, this.onClickYes);
			yesButton.addEventListener(MouseEvent.MOUSE_OVER, this.onHoverAnswer);
			yesButton.addEventListener(MouseEvent.MOUSE_OUT, this.onOutAnswer);
			var noButton:Sprite = new Sprite();
			var noText:TextField = new TextField();
			noText.defaultTextFormat = format;
			noText.autoSize = TextFieldAutoSize.CENTER;
			noText.selectable = false;
			noText.text = dictionary.getLabel("local.no");
			noButton.addChild(noText);
			noButton.buttonMode = true;
			noText.x = -noText.width/2;
			noText.y = -noText.height/2;
			this.question.addChild(noButton);
			noButton.y = 60;
			noButton.addEventListener(MouseEvent.CLICK, this.onClickNo);
			noButton.addEventListener(MouseEvent.MOUSE_OVER, this.onHoverAnswer);
			noButton.addEventListener(MouseEvent.MOUSE_OUT, this.onOutAnswer);
			var center:Number = Math.max(questionText.width, yesButton.width+30+noButton.width)/2;
			questionText.x = center - questionText.width/2;
			yesButton.x = center - noButton.width/2 - 15;
			noButton.x = center + yesButton.width/2 + 15;
			question.filters = [new GlowFilter(0xFFFFFF, 1, 32, 32, 32, BitmapFilterQuality.HIGH)];
		}
		public function hide():void {
			this.visible = false;
			this.scaleX = 0.01;
			this.scaleY = 0.01;
			this.mindMap.unblock();
		}
		public function show(concept:InternalConcept):void {
			this.scaleX = 1;
			this.scaleY = 1;
			this.concept = concept;
			this.circle.x = concept.x+concept.box.right+concept.branch.getPosition().x+20;
			this.circle.y = concept.y+concept.box.top+concept.branch.getPosition().y-15;
			this.circle.width = concept.box.width+40;
			this.circle.height = concept.box.height+30;
			this.question.x = -(InternalBranch)(concept.branch).balance*30-((InternalBranch)(concept.branch).balance+1)/2*(this.circle.width+this.question.width)+this.circle.x;
			this.question.y = this.circle.y-10;
			this.visible = true;
			this.mindMap.block();
		}
		protected function onClickNo(event:MouseEvent):void {
			this.hide();
		}
		protected function onClickYes(event:MouseEvent):void {
			this.hide();
			this.mindMap.chooseDeletion(this.concept);
		}
		protected function onHoverAnswer(event:MouseEvent):void {
			Sprite(event.currentTarget).scaleX = Sprite(event.currentTarget).scaleY = 1.1;
		}
		protected function onOutAnswer(event:MouseEvent):void {
			Sprite(event.currentTarget).scaleX = Sprite(event.currentTarget).scaleY = 1;
		}
	}
}