package {
	import flash.display.*;
	import flash.filters.*;
	import flash.events.*;
	import flash.geom.*;
	public class Branch extends Sprite {
		protected static  var sonSpace:Number = 38;
		public var base:Concept;
		public var sons:Sprite;
		protected var grid:Sprite;
		public function get sonCount():int {
			return this.sons.numChildren;
		}
		public function get mindMap():MindMap {
			return this.tree;
		}
		protected var displayKids:Boolean;
		public function get expanded():Boolean {
			return this.displayKids;
		}
		public function set expanded(value:Boolean){
			this.displayKids = value;
			this.expandButton.gotoAndStop(value?2:1);
		}
		protected var tree:MindMap;
		protected var expandButton:ExpandSign;
		protected var isHoveringBase:Boolean;
		public function Branch(concept:Concept) {
			this.grid = new Sprite();
			this.addChild(this.grid);
			this.base = concept;
			this.base.branch = this;
			this.base.addEventListener(Event.CHANGE, this.onBaseChange);
			this.addChild(this.base);
			this.sons = new Sprite();
			this.addChild(this.sons);
			this.applyCosmetics();
			this.expandButton = new ExpandSign();
			this.expandButton.width = this.expandButton.height = 8;
			this.expandButton.addEventListener(MouseEvent.MOUSE_DOWN, this.onExpandPress);
			this.expanded = true;
			this.proper();
			this.isHoveringBase = false;
			this.addChild(this.expandButton);
			this.addEventListener(Event.ENTER_FRAME, this.onFrame);
		}
		public function drawGrid():void {
			this.grid.graphics.clear();
			this.grid.graphics.lineStyle(2);
			var i:int;
			for (i=0; i<this.sonCount; i++) {
				this.drawGridLine(i);
				this.son(i).drawGrid();
			}
		}
		public function son(index:int):InternalBranch {
			if (index<0 || index>=this.sonCount) {
				return null;
			}
			return InternalBranch(this.sons.getChildAt(index));
		}
		public function addSon(concept:InternalConcept):void {
			var newSon:InternalBranch = this.nextBorn(concept);
			concept.branch = newSon;
			this.sons.addChild(newSon);
			newSon.father = this;
			newSon.mindMap = this.tree;
		}
		public function addBranch(branch:InternalBranch):void {
			this.sons.addChild(branch);
			branch.father = this;
			branch.mindMap = this.tree;
		}
		public function removeSon(concept:InternalConcept):void {
			if(concept.branch!=null && concept.branch.sonCount==0){
				this.removeBranch(InternalBranch(concept.branch));
			}
		}
		public function removeBranch(branch:InternalBranch):void {
			try{
				this.sons.removeChild(branch);
			}catch(error:ArgumentError){};
			this.mindMap.conceptRemoved(InternalConcept(branch.base));
		}
		public function getConceptById(id:int):Concept {
			if(this.base.ID == id){
				return this.base;
			}else{
				var i:int;
				for(i=0;i<this.sonCount;i++){
					var concept:Concept = this.son(i).getConceptById(id);
					if(concept!=null){
						return concept;
					}
				}
			}
			return null;
		}
		public function getPosition():Point {
			if(this.tree == this){
				return new Point(0,0);
			}else{
				var position:Point;
				position = (InternalBranch)(this).father.getPosition().add(new Point(this.x, this.y));
				return position;
			}
		}
		public function block():void {
			this.base.blocked=true;
			var i:int;
			for(i=0;i<this.sonCount;i++){
				this.son(i).block();
			}
		}
		public function unblock():void {
			this.base.blocked=false;
			var i:int;
			for(i=0;i<this.sonCount;i++){
				this.son(i).unblock();
			}
		}
		public function toXml():XML {
			var result:XML = <branch/>
			result.concept = this.base.toXml();
			result.nodes = <nodes/>
			var i:int;
			for(i=0;i<this.sonCount;i++){
				result.nodes.branch[i] = this.son(i).toXml();
			}
			return result;
		}
		public function fromXml(xml:XML, free:Boolean):void {
			this.base.fromXml(new XML(xml.concept[0].toXMLString()));
			var i:int;
			while(this.sonCount>0){
				this.removeBranch(this.son(0));
			}
			var node:XML;
			var branch:InternalBranch;
			for each(node in xml.nodes.branch){
				branch = new InternalBranch(new InternalConcept("", 0, free, false, "", -1), 0);
				branch.mindMap = this.tree;
				branch.fromXml(node, free);
				this.addBranch(branch);
			}
			this.proper();
		}
		public function obscure(obscure:Boolean):void {
			this.base.alpha = obscure?0.4:1;
			this.alpha = obscure?0.4:1;
		}
		protected function applyCosmetics():void {
			var shadowFilter:DropShadowFilter = new DropShadowFilter(5, 62, 0, 1, 5, 5, 0.34, BitmapFilterQuality.HIGH);
			this.base.filters = [shadowFilter];
		}
		protected function onBaseChange(event:Event):void {
			this.proper();
		}
		protected function onExpandPress(event:MouseEvent):void {
			if(!this.base.blocked){
				this.expanded = !this.expanded;
				this.proper();
			}
		}
		protected function onFrame(event:Event):void {
			if(this.base.mouseX>this.base.box.left-4 && this.base.mouseX<this.base.box.right+35 && this.base.mouseY>this.base.box.top-35 && this.mouseY<this.base.box.bottom){
				this.base.setShowing();
				if(!this.isHoveringBase){
					this.mindMap.signalConceptHover(this.base, true);
				}
				if(this.sonCount>0 && !this.base.blocked){
					this.expandButton.visible = true;
				}
				this.isHoveringBase = true;
			}else{
				this.base.setHiding();
				if(this.isHoveringBase){
					this.mindMap.signalConceptHover(this.base, false);
				}
				if(this.expanded){
					this.expandButton.visible = false;
				}
				this.isHoveringBase = false;
			}
		}
		public function proper():void {
		}
		protected function drawGridLine(childIndex:int):void {
		}
		protected function nextBorn(concept:InternalConcept):InternalBranch {
			return null;
		}
	}
}