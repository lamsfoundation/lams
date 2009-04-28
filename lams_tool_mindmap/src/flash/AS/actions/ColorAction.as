package actions{
	import flash.errors.*;
	public class ColorAction extends UserAction {
		protected var color:int;
		public function ColorAction(nodeId:int, color:int){
			super(nodeId);
			this.color = color;
			this.type = 2;
		}
		override public function apply(tree:MindMap):void {
			var concept:Concept = tree.getConceptById(this.nodeId);
			if(concept==null){
				throw new Error("ColorAction: concept does not exist!");
			}else{
				concept.color = this.color;
			}
		}
		override public function fromXml(xml:XML):void {
			super.fromXml(xml);
			this.color = int("0x"+xml.color);
		}
		override public function toXml():XML {
			var xml:XML = super.toXml();
			xml.color = this.color.toString(16);
			return xml;
		}
	}
}