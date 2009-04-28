package actions{
	import flash.utils.*;
	public class UserAction {
		static public function createEmptyAction(type:int):UserAction {
			switch(type){
				case 0: return new DeleteAction(0);
				case 1:	return new CreateAction(0, new InternalConcept("",0,false,false,"",0), 0);
				case 2:	return new ColorAction(0, 0);
				case 3:	return new TextAction(0, "");
			}
			return null;
		}
		static public const types:Array = ["actions.DeleteAction", "actions.CreateAction", "actions.ColorAction", "actions.TextAction"];
		static public function related(action1:UserAction, action2:UserAction):Boolean {
			if(action1.nodeId==action2.nodeId){
				if(action1.type == action2.type && (action1.type==2 || action1.type==3)){
					return true;
				}
			}
			return false;
		}
		public var nodeId:int;
		public var type:int;
		public function UserAction(nodeId:int){
			this.nodeId = nodeId;
		}
		public function apply(tree:MindMap):void {
		}
		public function fromXml(xml:XML):void {
			this.nodeId = xml.nodeID;
		}
		public function toXml():XML {
			var xml:XML = <action/>
			xml.type = this.type;
			xml.nodeID = this.nodeId;
			return xml;
		}
	}
}