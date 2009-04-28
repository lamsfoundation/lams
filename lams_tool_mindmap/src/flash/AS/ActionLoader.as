package {
	import flash.net.*;
	import actions.*;
	public class ActionLoader extends URLLoader {
		protected var localId:int;
		public var highestRelatedId:int;
		public var action:UserAction;
		public var undoAction:UserAction;
		public var prevRelated:ActionLoader;
		public var nextRelated:ActionLoader;
		public var request:URLRequest;
		public var current:Boolean;
		public function ActionLoader(action:UserAction, server:String, id:int){
			this.localId = id;
			this.action = action;
			this.prevRelated = null;
			this.nextRelated = null;
			this.highestRelatedId = -1;
			this.request = new URLRequest(server);
			this.request.contentType = "application/x-www-form-urlencoded";
			this.request.method = URLRequestMethod.POST;
			var xml:XML = action.toXml();
			xml.ID = id;
			this.request.data = "actionXML="+xml.toXMLString();
			this.current = false;
		}
	}
}