package {
	import flash.display.*;
	import flash.events.*;
	import flash.geom.*;
	import flash.filters.*;
	import flash.ui.*;
	import flash.utils.*;
	public class SwitchMessage extends Sprite{
		protected var target:Concept;
		protected var dragging:InternalConcept;
		protected var mindMap:MindMap;
		protected var closedHand:ClosedHand;
		protected var ghost:Sprite;
		protected var ghostConcept:InternalConcept;
		protected var timer:Timer;
		protected var longEnough:Boolean;
		public function SwitchMessage(mindMap:MindMap){
			this.mindMap = mindMap;
			this.ghostConcept = new InternalConcept("", 0, false, false, "", -1);
			this.ghostConcept.filters = [new GlowFilter(0x2277CC, 1, 16, 16, 1, BitmapFilterQuality.HIGH)];
			this.ghost = new Sprite();
			this.ghost.addChild(this.ghostConcept);
			this.addChild(this.ghost);
			this.closedHand = new ClosedHand();
			this.closedHand.addEventListener(MouseEvent.MOUSE_UP, this.onReleaseHand);
			this.addChild(this.closedHand);
			this.addEventListener(Event.ENTER_FRAME, this.onFrame);
			this.longEnough = true;
			this.timer = new Timer(300, 1);
			this.timer.addEventListener(TimerEvent.TIMER, this.onTimer);
		}
		public function hide():void {
			this.dragging = null;
			this.visible = false;
			this.timer.reset();
			this.scaleX = 0.01;
			this.scaleY = 0.01;
		}
		public function show(concept:InternalConcept, handX:int):void {
			this.scaleX = 1;
			this.scaleY = 1;
			this.visible = true;
			this.mindMap.block();
			var branch:InternalBranch = (InternalBranch)(concept.branch);
			var branchPosition:Point = branch.getPosition();
			Mouse.hide();
			concept.branch.obscure(true);
			this.ghostConcept.word = concept.word;
			this.ghostConcept.color = concept.color;
			this.ghostConcept.x = -handX;
			this.ghostConcept.y = concept.box.height/concept.box.width*Math.sqrt(Math.pow(concept.box.width/2,2)-Math.pow(handX,2));
			this.dragging = concept;
			this.longEnough = false;
			this.timer.start();
		}
		public function switchConcept(concept:Concept):void {
			if(concept!=this.target){
				this.target = concept;
				this.circle.x = concept.x+concept.box.right+concept.branch.getPosition().x+20;
				this.circle.y = concept.y+concept.box.top+concept.branch.getPosition().y-15;
				this.circle.width = concept.box.width+40;
				this.circle.height = concept.box.height+30;
			}
		}
		protected function getClosestValidConcept(sourceBranch:Branch, invalidBranch:InternalBranch):Concept {
			var branchPosition:Point;
			branchPosition = sourceBranch.getPosition();
			var shortestDistance:Number = Point.distance(new Point(this.mouseX, this.mouseY), new Point(branchPosition.x+sourceBranch.base.x, branchPosition.y+sourceBranch.base.y));
			var closestConcept:Concept = sourceBranch.base;
			var i:int;
			var distance:Number;
			var concept:Concept;
			for(i=0;i<sourceBranch.sonCount;i++){
				if(sourceBranch.son(i) != invalidBranch){
					concept = this.getClosestValidConcept(sourceBranch.son(i), invalidBranch);
					branchPosition = concept.branch.getPosition();
					distance = Point.distance(new Point(this.mouseX, this.mouseY), new Point(branchPosition.x+concept.x, branchPosition.y+concept.y));
					if(distance<shortestDistance){
						closestConcept = concept;
						shortestDistance = distance;
					}
				}
			}
			return closestConcept;
		}
		protected function refresh():void {
			this.ghost.x = this.mouseX;
			this.ghost.y = this.mouseY;
			this.closedHand.x = this.mouseX;
			this.closedHand.y = this.mouseY;
			this.switchConcept(this.getClosestValidConcept(this.mindMap, InternalBranch(this.dragging.branch)));
		}
		protected function onReleaseHand(event:MouseEvent):void {
			var branch:InternalBranch = InternalBranch(this.dragging.branch);
			branch.obscure(false);
			Mouse.show();
			if(branch.father!=this.target.branch && this.longEnough){
				branch.father.removeBranch(branch);
				this.target.branch.addBranch(branch);
				this.mindMap.proper();
			}
			this.mindMap.unblock();
			this.dragging = null;
			this.target = null;
			this.hide();
		}
		protected function onTimer(event:TimerEvent):void {
			this.longEnough = true;
		}
		protected function onFrame(event:Event):void {
			if(this.dragging!=null){
				this.refresh();
			}
		}
	}
}