package actions{
	import flash.errors.*;
	public class TextAction extends UserAction {
		protected var text:String;
		public function TextAction(nodeId:int, text:String){
			super(nodeId);
			this.text = text;
			this.type = 3;
		}
		override public function apply(tree:MindMap):void {
			var concept:Concept = tree.getConceptById(this.nodeId);
			if(concept==null){
				throw new Error("TextAction: Concept does not exist!");
			}else{
				concept.word = this.text;
			}
		}
		override public function fromXml(xml:XML):void {
			super.fromXml(xml);
			this.text = xml.text;
		}
		override public function toXml():XML {
			var xml:XML = super.toXml();
			xml.text = this.text;
			return xml;
		}
	}
}