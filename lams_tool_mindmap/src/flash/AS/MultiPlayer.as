package {
	import flash.display.*;
	import flash.events.*;
	import flash.net.*;
	import flash.external.*;
	import org.lamsfoundation.lams.common.dictionary.*;
	public class MultiPlayer extends MovieClip{
		protected var xmlLabels:XML;
		protected var originalXml:XML;
		public function MultiPlayer() {
			var request:URLRequest;
			if (this.loaderInfo.parameters.dictionary == undefined) {
				request = new URLRequest("dictionary.xml");
			}else{
				request = new URLRequest(this.loaderInfo.parameters.dictionary);
			}
			var labelsLoader:URLLoader = new URLLoader(request);
			labelsLoader.addEventListener(Event.COMPLETE, this.onLabelsLoad);
			if (this.loaderInfo.parameters.xml == "") {
				ExternalInterface.addCallback("setMindmap", this.greenLight);
				ExternalInterface.call("flashLoaded");
			} else {
				if (this.loaderInfo.parameters.xml == undefined) {
					request = new URLRequest("tree.xml");
				}else{
					request = new URLRequest(this.loaderInfo.parameters.xml);
				}
				var treeLoader:URLLoader = new URLLoader(request);
				treeLoader.addEventListener(Event.COMPLETE, this.onTreeLoad);
			}
		}
		public function greenLight(treeData:String):void {
			this.originalXml = new XML(treeData);
			if(this.xmlLabels!=null){
				this.startApplication(this.originalXml.branch[0], int(this.originalXml.lastActionId), this.xmlLabels);
			}
		}
		protected function startApplication(originalXml:XML, lastActionId:int, xmlLabels:XML) {
			var user:String;
			var pollServer:String, notifyServer:String;
			var id:int;
			var dictionary:XMLDictionary;
			if (this.loaderInfo.parameters.user==undefined) {
				user = "Liviu";
			} else {
				user = this.loaderInfo.parameters.user;
			}
			if (this.loaderInfo.parameters.pollServer==undefined) {
				pollServer = "actions.xml";
			} else {
				pollServer = this.loaderInfo.parameters.pollServer;
			}
			if (this.loaderInfo.parameters.notifyServer==undefined) {
				notifyServer = "some server";
			} else {
				notifyServer = this.loaderInfo.parameters.notifyServer;
			}
			if (this.loaderInfo.parameters.id==undefined) {
				id = 126;
			} else {
				id = int(this.loaderInfo.parameters.id);
			}
			dictionary = new XMLDictionary(xmlLabels);
			var mindMap:ServerMindMap = new ServerMindMap(originalXml, user, pollServer, notifyServer, lastActionId, dictionary);
			var application:Design;
			application = new Design(mindMap, stage);
		}
		protected function onTreeLoad(event:Event):void {
			this.originalXml = new XML(URLLoader(event.target).data);
			if(this.xmlLabels != null){
				this.startApplication(this.originalXml.branch[0], int(this.originalXml.lastActionId[0]), this.xmlLabels);
			}
		}
		protected function onLabelsLoad(event:Event):void {
			this.xmlLabels = new XML(URLLoader(event.target).data);
			if(this.originalXml != null){
				this.startApplication(this.originalXml.branch[0], int(this.originalXml.lastActionId), this.xmlLabels);
			}
		}
	}
}