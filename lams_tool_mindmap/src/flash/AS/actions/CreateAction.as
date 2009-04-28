package actions {
	import flash.errors.*;
	public class CreateAction extends UserAction {
		protected var concept:InternalConcept;
		public var tempId:int;
		public function get reference():InternalConcept {
			return this.concept;
		}
		public function CreateAction(fatherId:int, concept:InternalConcept, tempId:int){
			super(fatherId);
			this.concept = new InternalConcept(concept.word, concept.color, false, concept.owned, concept.creator, concept.ID);
			this.tempId = tempId;
			this.type = 1;
		}
		override public function apply(tree:MindMap):void {
			var concept:Concept = tree.getConceptById(this.nodeId);
			if(concept==null){
				throw new Error("CreateAction: father concept does not exist!");
			}else{
				concept.branch.addSon(new InternalConcept(this.concept.word,this.concept.color, false, this.concept.owned, this.concept.creator, this.concept.ID));
				tree.proper();
			}
		}
		override public function fromXml(xml:XML):void {
			super.fromXml(xml);
			this.concept = new InternalConcept("",0,false,false,"",0);
			this.concept.fromXml(XML(xml.concept));
		}
		override public function toXml():XML {
			var xml:XML = super.toXml();
			xml.concept = this.concept.toXml();
			return xml;
		}
	}
}