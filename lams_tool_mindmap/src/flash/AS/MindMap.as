package {
	import flash.display.*;
	import flash.events.*;
	import flash.geom.*;
	import org.lamsfoundation.lams.common.dictionary.*;
	public class MindMap extends Branch {
		protected var deleteMessage:DeleteMessage;
		protected var switchMessage:SwitchMessage;
		protected var conceptHovered:Concept;
		public var dictionary:XMLDictionary;
		protected var _player:String;
		public function get player():String {
			return this._player;
		}
		protected var ids:int;
		public function get leftUpSpace():Number {
			var result:Number = 0;
			if(this.sonCount>0){
				var i:int;
				for(i=0;i<this.sonCount+this.sonCount%2-2;i+=4){
					result+=(this.son(i).span+sonSpace);
				}
				result+=this.son(this.sonCount+this.sonCount%2-2).upSpace;
			}
			return result;
		}
		public function get rightUpSpace():Number {
			var result:Number = 0;
			if(this.sonCount>1){
				var i:int;
				for(i=1;i<this.sonCount-this.sonCount%2-1;i+=4){
					result+=(this.son(i).span+sonSpace);
				}
				result+=this.son(this.sonCount-this.sonCount%2-1).upSpace;
			}
			return result;
		}
		public function MindMap(baseConcept:BaseConcept, player:String, dictionary:XMLDictionary) {
			super(baseConcept);
			Concept.DEFAULT_TITLE = dictionary.getLabel("local.title");
			this.dictionary = dictionary;
			this._player = player;
			this.base.x = 0;
			this.base.branch = this;
			this.drawGrid();
			this.tree = this;
			this.deleteMessage = new DeleteMessage(this, dictionary);
			this.addChild(this.deleteMessage);
			this.deleteMessage.hide();
			this.switchMessage = new SwitchMessage(this);
			this.addChild(this.switchMessage);
			this.switchMessage.hide();
			this.ids=1;
		}
		public function requestDelete(concept:InternalConcept) {
			this.deleteMessage.show(concept);
		}
		public function requestHandMove(concept:InternalConcept, handPosition:Point) {
			this.switchMessage.show(concept, handPosition.x);
		}
		public function signalConceptHover(concept:Concept, hovering:Boolean):void {
			if (hovering) {
				this.conceptHovered = concept;
				this.dispatchEvent(new InformationEvent(InformationEvent.INFORMATION, this.dictionary.getLabel("local.node_creator")+": <b>"+concept.creator+"</b>"));
			} else {
				if(this.conceptHovered == concept){
					this.dispatchEvent(new InformationEvent(InformationEvent.NO_INFORMATION));
				}
			}
		}
		public function chooseDeletion(concept:InternalConcept):void {
			if(concept.branch.sonCount==0){
				this.deleteConcept(concept);
			}
		}
		public function conceptRemoved(concept:InternalConcept):void {
			if(this.deleteMessage.visible && this.deleteMessage.concept == concept){
				this.deleteMessage.hide();
			}
		}
		public function branchAdded(branch:InternalBranch):void {
			if(this.deleteMessage.visible && this.deleteMessage.concept == branch.father.base){
				this.deleteMessage.hide();
			}
		}
		public function getNewConceptId():int {
			return this.ids++;
		}
		public function allowConceptId(id:int):void {
			this.ids = Math.max(id+1,this.ids);
		}
		protected function deleteConcept(concept:InternalConcept):void {
			(InternalBranch)(concept.branch).father.removeSon(concept);
			concept.branch.proper();
		}
		protected function rebalance():void {
			var i:int;
			for (i=0; i<this.sons.numChildren; i++) {
				this.son(i).balance = 2*(i%2)-1;
			}
		}
		protected function nextBalance():int {
			return 2 * (this.sonCount % 2) - 1;
		}
		override public function addBranch(branch:InternalBranch):void {
			branch.balance = this.nextBalance();
			super.addBranch(branch);
		}
		override public function proper():void {
			var i:int;
			var totalWidthLeft:Number, totalWidthRight:Number;
			totalWidthLeft = 0;
			totalWidthRight = 0;
			for (i=0; i<this.sons.numChildren; i++) {
				this.son(i).properForward();
				if (this.son(i).balance == -1) {
					totalWidthLeft += this.son(i).span+(totalWidthLeft>0?sonSpace:0);
				} else {
					totalWidthRight += this.son(i).span+(totalWidthRight>0?sonSpace:0);
				}
			}
			var dropperYLeft:Number = -leftUpSpace;
			var dropperYRight:Number = -rightUpSpace;
			for (i=0; i<this.sons.numChildren; i++) {
				var son:InternalBranch;
				if (i%2==0) {
					son = this.son(this.indexAtLeftPosition(i/2));
					son.y = dropperYLeft+son.upSpace;
					dropperYLeft += son.span+sonSpace;
				} else {
					son = this.son(this.indexAtRightPosition((i-1)/2));
					son.y = dropperYRight+son.upSpace;
					dropperYRight += son.span+sonSpace;
				}
				son.x = (2*(i%2)-1)*(this.base.box.width/2 + 40);
				son.visible = this.expanded;
			}
			this.drawGrid();
			this.grid.visible = this.expanded;
			if (this.deleteMessage!=null && this.deleteMessage.isShown) {
				this.deleteMessage.show(this.deleteMessage.concept);
			}
			this.base.turnArrow(this.sonCount%2==0);
			this.expandButton.y = -(this.base.box.height/2+26);
		}
		protected function indexAtLeftPosition(position:int):int {
			if(position<0 || position>Math.floor((this.sonCount-1)/2)){
				throw new ArgumentError("MindMap.indexAtLeftPosition: position is out of bounds");
			}
			if(position<Math.ceil(this.sonCount/4)){
				return position*4;
			}else{
				return 4*(Math.ceil(this.sonCount/2)-1-position)+2;
			}
		}
		protected function indexAtRightPosition(position:int):int {
			if(position<0 || position>Math.floor((this.sonCount-2)/2)){
				throw new ArgumentError("MindMap.indexAtRightPosition: position is out of bounds");
			}
			if(position<Math.ceil((this.sonCount-1)/4)){
				return position*4+1;
			}else{
				return 4*(Math.floor(this.sonCount/2)-1-position)+3;
			}
		}
		override protected function drawGridLine(childIndex:int):void {
			var child:InternalBranch = this.son(childIndex);
			this.grid.graphics.moveTo((this.base.box.width/2+5)*((childIndex%2)*2-1), 0);
			this.grid.graphics.curveTo((child.x + (this.base.box.width/2+5)*((childIndex%2)*2-1)) / 2, 0, (child.x + (this.base.box.width/2+5)*((childIndex%2)*2-1)) / 2, child.y / 2);
			this.grid.graphics.curveTo((child.x + (this.base.box.width/2+5)*((childIndex%2)*2-1)) / 2, child.y, child.x - 5*child.balance, child.y);
		}
		override protected function nextBorn(concept:InternalConcept):InternalBranch {
			return new InternalBranch(concept,this.nextBalance());
		}
		override public function removeSon(concept:InternalConcept):void {
			super.removeSon(concept);
			this.rebalance();
		}
		override public function removeBranch(branch:InternalBranch):void {
			super.removeBranch(branch);
			this.rebalance();
		}
	}
}