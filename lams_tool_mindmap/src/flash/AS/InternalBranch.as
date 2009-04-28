package {
	import flash.events.*;
	import flash.errors.*;
	import flash.geom.*;
	public class InternalBranch extends Branch{
		public function get span():Number {
			var result:Number = 0;
			var i:int;
			for (i=0; i<this.sons.numChildren; i++) {
				result+=this.son(i).span;
				if (i>0) {
					result+=sonSpace;
				}
			}
			return Math.max(this.base.box.height, result);
		}
		public function get upSpace():Number {
			var result:Number = 0;
			if(this.sonCount>0){
				var i:int;
				for(i=0;i<this.sonCount-1;i+=2){
					result+=(this.son(i).span+sonSpace);
				}
				result+=this.son(this.sonCount-1).upSpace;
			}
			return result;
		}
		public function set mindMap(mindMap:MindMap) {
			this.tree = mindMap;
			var i:int;
			for (i=0; i<this.sons.numChildren; i++) {
				this.son(i).mindMap = mindMap;
			}
			if(mindMap!=null){
				mindMap.branchAdded(this);
			}
		}
		public var father:Branch;
		public function get balance():int {
			return this.orientation;
		}
		public function set balance(value:int) {
			this.orientation = value;
			var i:int;
			for(i=0;i<this.sons.numChildren;i++){
				this.son(i).balance = value;
			}
			if(value==-1)this.base.turnArrow(true);
			else this.base.turnArrow(false);
		}
		protected var orientation:int;
		public function InternalBranch(concept:InternalConcept, balance:int) {
			super(concept);
			this.mindMap = null;
			this.father = null;
			this.balance = balance;
		}
		public function properForward():void {
			this.base.x = this.balance*this.base.box.width/2;
			var i:int;
			for (i=0; i<this.sons.numChildren; i++) {
				this.son(i).properForward();
			}
			var dropperY:Number = -this.upSpace;
			for (i=0; i<this.sons.numChildren; i++) {
				var son:InternalBranch = this.son(this.indexAtPosition(i));
				son.y = dropperY+son.upSpace;
				son.x = this.balance*(this.base.box.width + 40);
				son.visible = this.expanded;
				dropperY += son.span+sonSpace;
			}
			this.drawGrid();
			this.grid.visible = this.expanded;
			this.expandButton.x = this.balance*this.base.box.width;
		}
		protected function indexAtPosition(position:int):int {
			if(position<0 || position>=this.sonCount){
				throw new ArgumentError("InternalBranch.indexAtPosition: position is out of bounds");
			}
			if(position<Math.ceil(this.sonCount/2)){
				return position*2;
			}else{
				return 2*(this.sonCount-position)-1;
			}
		}
		override public function proper():void {
			if (this.father==null) {
				this.properForward();
			} else {
				this.father.proper();
			}
		}		
		override protected function drawGridLine(childIndex:int):void {
			var child:InternalBranch = this.son(childIndex);
			this.grid.graphics.moveTo((this.base.box.width+5)*this.balance, 0);
			this.grid.graphics.curveTo((child.x + (this.base.box.width+5)*this.balance) / 2, 0, (child.x + (this.base.box.width+5)*this.balance) / 2, child.y / 2);
			this.grid.graphics.curveTo((child.x + (this.base.box.width+5)*this.balance) / 2, child.y, child.x - 5*child.balance, child.y);
		}
		override protected function nextBorn(concept:InternalConcept):InternalBranch {
			return new InternalBranch(concept, this.balance);
		}
		override public function addBranch(branch:InternalBranch):void {
			super.addBranch(branch);
			branch.balance = this.balance;
		}
	}
}