package {
	import flash.display.*;
	import flash.events.*;
	import flash.net.*;
	import flash.text.*;
	import flash.external.*;
	import flash.errors.*;
	import org.lamsfoundation.lams.common.dictionary.*;
	public class SinglePlayer extends MovieClip {
		protected var xmlLabels:XML;
		protected var originalXml:XML;
		protected var mindMap:MindMap;
		public function SinglePlayer() {
			var originalXml:XML;
			var request:URLRequest;
			if (this.loaderInfo.parameters.dictionary == undefined) {
				request = new URLRequest("dictionary.xml");
			}else{
				request = new URLRequest(RequestTools.antiCache(this.loaderInfo.parameters.dictionary));
			}
			var labelsLoader:URLLoader = new URLLoader(request);
			labelsLoader.addEventListener(Event.COMPLETE, this.onLabelsLoad);
			if (this.loaderInfo.parameters.xml == "") {
				try{
					ExternalInterface.addCallback("setMindmap", this.greenLight);
					ExternalInterface.call("flashLoaded");
				}catch(error:Error){
				}
			} else {
				if (this.loaderInfo.parameters.xml == undefined) {
					request = new URLRequest("stree.xml");
				} else {
					request = new URLRequest(RequestTools.antiCache(this.loaderInfo.parameters.xml));
				}
				var treeLoader:URLLoader = new URLLoader(request);
				treeLoader.addEventListener(Event.COMPLETE, this.onTreeLoad);
			}
		}
		public function greenLight(treeData:String):void {
			this.originalXml = new XML(treeData);
			if(this.xmlLabels!=null){
				this.startApplication(this.originalXml, this.xmlLabels);
			}
		}
		protected function startApplication(originalXml:XML, xmlLabels:XML):void {
			var user:String;
			if (this.loaderInfo.parameters.user==undefined) {
				user = "Liviu";
			} else {
				user = this.loaderInfo.parameters.user;
			}
			var dictionary:XMLDictionary = new XMLDictionary(xmlLabels);
			this.mindMap = new MindMap(new BaseConcept("", 0, true, false, "", -1), user, dictionary);
			this.mindMap.fromXml(originalXml, true);
			var application:Design;
			application = new Design(this.mindMap, stage);
		}
		protected function onTreeLoad(event:Event):void {
			this.originalXml = new XML(URLLoader(event.target).data);
			if(this.xmlLabels != null){
				this.startApplication(this.originalXml, this.xmlLabels);
			}
		}
		protected function onLabelsLoad(event:Event):void {
			this.xmlLabels = new XML(URLLoader(event.target).data);
			if(this.originalXml != null){
				this.startApplication(this.originalXml, this.xmlLabels);
			}
		}
	}
}