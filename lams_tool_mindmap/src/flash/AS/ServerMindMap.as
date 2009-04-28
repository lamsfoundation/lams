package {
	import flash.events.*;
	import flash.net.*;
	import flash.utils.*;
	import flash.errors.*;
	import actions.*;
	import org.lamsfoundation.lams.common.dictionary.*;
	public class ServerMindMap extends MindMap {
		public var pollServer:String;
		public var notifyServer:String;
		protected var id:int;
		protected var lastActionId:int;
		protected var nextLocalId:int;
		protected var pollTimer:Timer;
		protected var loaders:Array;
		public function ServerMindMap(originalXml:XML, player:String, pollServer:String, notifyServer:String, actionId:int, dictionary:XMLDictionary){
			super(new BaseConcept("", 0, false, false, "", -1), player, dictionary);
			this.fromXml(originalXml, false);
			this.loaders = new Array();
			this.pollServer = pollServer;
			this.notifyServer = RequestTools.continueURL(notifyServer)+"lastActionId="+actionId;
			this.id = id;
			this.lastActionId = actionId;
			this.nextLocalId = 0;
			this.pollTimer = new Timer(1000, 1);
			this.pollTimer.addEventListener(TimerEvent.TIMER, this.onTimer);
			this.requestActions();
		}
		public function alertAction(action:UserAction, reverse:UserAction):void {
			this.sendAction(action, reverse);
		}
		protected function requestActions():void {
			this.pollTimer.reset();
			var request:URLRequest = new URLRequest(RequestTools.continueURL(RequestTools.antiCache(this.pollServer)));
			request.method = URLRequestMethod.GET;
			var urlVariables:URLVariables = new URLVariables("lastActionID="+this.lastActionId);
			request.data = urlVariables;
			var loader:URLLoader = new URLLoader();
			loader.addEventListener(Event.COMPLETE, onPollLoad);
			loader.addEventListener(IOErrorEvent.IO_ERROR, onPollFail);
			loader.load(request);
		}
		protected function sendAction(action:UserAction, reverse:UserAction):void {
			var loader:ActionLoader = new ActionLoader(action, this.notifyServer, this.nextLocalId++);
			this.loaders.push(loader);
			loader.nextRelated = null;
			loader.current = true;
			loader.undoAction = reverse;
			var i:int=this.loaders.length-2;
			while(i>=0 && !UserAction.related(loader.action, this.loaders[i].action)){
				i--;
			}
			if(i<0){
				loader.prevRelated = null;
			}else{
				loader.prevRelated = this.loaders[i];
				loader.prevRelated.current = false;
				this.loaders[i].nextRelated = loader;
			}
			loader.addEventListener(Event.COMPLETE, this.onActionSent);
			loader.addEventListener(IOErrorEvent.IO_ERROR, this.onSendError);
			loader.load(loader.request);
		}
		protected function onPollLoad(event:Event):void {
			this.dispatchEvent(new ServerMindMapEvent(ServerMindMapEvent.WIN_INCOMING_CONNECTION));
			var actionsXml:XML = new XML(URLLoader(event.target).data).actions[0];
			var action:UserAction;
			var xmlNode:XML;
			for each (xmlNode in actionsXml.action){
				var actionClass:Class = Class(getDefinitionByName(UserAction.types[xmlNode.type]));
				action = UserAction.createEmptyAction(int(xmlNode.type));
				actionClass(action).fromXml(xmlNode);
				try{
					actionClass(action).apply(this);
				}catch(error:Error){
					break;
				}
				this.lastActionId = Math.max(int(xmlNode.ID), this.lastActionId);
			}
			this.pollTimer.reset();
			this.pollTimer.start();
		}
		protected function onPollFail(event:IOErrorEvent):void {
			this.dispatchEvent(new ServerMindMapEvent(ServerMindMapEvent.FAILED_INCOMING_CONNECTION));
			this.requestActions();
		}
		protected function onActionSent(event:Event):void {
			this.dispatchEvent(new ServerMindMapEvent(ServerMindMapEvent.WIN_OUTGOING_CONNECTION));
			var loader:ActionLoader = ActionLoader(event.target);
			var xml:XML = new XML(loader.data);
			var reject:Boolean;
			var neighbour:ActionLoader;
			if(xml.ok == "0" || int(xml.id) < loader.highestRelatedId) reject = true;
			else {
				reject = false;
				neighbour = loader.prevRelated;
				while(neighbour!=null){
					neighbour.highestRelatedId = int(xml.id);
					neighbour = neighbour.prevRelated;
				}
				neighbour = loader.nextRelated;
				while(neighbour!=null){
					neighbour.highestRelatedId = int(xml.id);
					neighbour = neighbour.nextRelated;
				}
			}
			var concept:InternalConcept;
			if(reject){
				if(loader.nextRelated == null){
					if(loader.current){
						switch(loader.action.type){
							case 0:
							concept = InternalConcept(this.getConceptById(loader.action.nodeId));
							concept.blocked = false;
							concept.alpha = 1; 
							break;
							case 1:							
							concept = InternalConcept(this.getConceptById(loader.action.nodeId));
							InternalBranch(concept.branch).removeBranch(InternalBranch(CreateAction(loader.action).reference.branch));
							this.proper();
							break;
							default:
							loader.undoAction.apply(this);
						}
						if(loader.prevRelated!=null){
							loader.prevRelated.current = true;
						}
					}
				}else{
					loader.nextRelated.undoAction = loader.undoAction;
				}
			}else{
				if(!loader.current){
					while(neighbour.nextRelated!=null){
						neighbour = neighbour.nextRelated;
					}
					if(!neighbour.current){
						loader.action.apply(this);
					}
				}else{
					switch(loader.action.type){
						case 0:
						concept = InternalConcept(this.getConceptById(loader.action.nodeId));
						this.deleteConcept(concept);
						break;
						case 1:
						concept = InternalConcept(this.getConceptById(CreateAction(loader.action).tempId));
						concept.blocked = false;
						concept.ID = int(xml.data);
						this.allowConceptId(concept.ID);
						break;
					}
				}
			}
			if(loader.prevRelated!=null){
				loader.prevRelated.nextRelated = loader.nextRelated;
			}
			if(loader.nextRelated!=null){
				loader.nextRelated.prevRelated = loader.prevRelated;
			}
			this.loaders.splice(this.loaders.indexOf(loader), 1);
		}
		protected function onSendError(event:IOErrorEvent):void {
			this.dispatchEvent(new ServerMindMapEvent(ServerMindMapEvent.FAILED_OUTGOING_CONNECTION));
			var loader:ActionLoader = ActionLoader(event.target);
			loader.load(loader.request);
		}
		protected function onTimer(event:TimerEvent):void {
			this.requestActions();
		}
		override public function chooseDeletion(concept:InternalConcept):void {
			concept.blocked = true;
			concept.alpha = 0.4;
			this.sendAction(new DeleteAction(concept.ID), null);
		}
	}
}