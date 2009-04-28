package actions{
	import flash.errors.*;
	public class DeleteAction extends UserAction {
		public function DeleteAction(nodeId:int){
			super(nodeId);
			this.type = 0;
		}
		override public function apply(tree:MindMap):void {
			var concept:InternalConcept = InternalConcept(tree.getConceptById(this.nodeId));
			if(concept==null){
				throw new Error("DeleteAction: Concept does not exist!");
			}else{
				InternalBranch(concept.branch).father.removeBranch(InternalBranch(concept.branch));
				tree.proper();
			}
		}
	}
}