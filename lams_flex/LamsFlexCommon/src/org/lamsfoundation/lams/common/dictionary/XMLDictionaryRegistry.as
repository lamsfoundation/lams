package org.lamsfoundation.lams.common.dictionary
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	public class XMLDictionaryRegistry extends EventDispatcher
	{
		public static const UPDATE_LABELS:String = "updatedLabels";
		public static const UPDATE_LABELS_REPLACE:String = "updateLabelsReplace";
		public static const UPDATE_LABELS_CONCATENTATE:String = "updateLabelsConcatenate";
		public static const UPDATE_LABELS_INSERT:String = "updateLabelsInsert";
		
		private var _xmlDictionary:XMLDictionary;
		private var keyRegistry:Array;
		private var fallbackXML:XML;
		
		public function XMLDictionaryRegistry(value:XML, fallbackXMLIn:XML=null)
		{
			fallbackXML = fallbackXMLIn;
			xml = value;
			
		}
		
		public function set xml(value:XML):void {
			_xmlDictionary = new XMLDictionary(value, fallbackXML);
			
			this.dispatchEvent(new Event("updatedLabels"));
			this.dispatchEvent(new Event("updatedLabelsReplace"));
			this.dispatchEvent(new Event("updatedLabelsConcatentate"));
			this.dispatchEvent(new Event("updatedLabelsInsert"));
			this.dispatchEvent(new Event("updatedValues"));
		}
		
		[Bindable (event="updatedValues")]
		public function getValue(key:String):Object {
			return _xmlDictionary.getValue(key);
		}
		
		[Bindable (event="updatedLabels")]
		public function getLabel(key:String):String {
			
			if (_xmlDictionary.getLabel(key) != null && _xmlDictionary.getLabel(key) != "") {
				return _xmlDictionary.getLabel(key);
			} else {
				return "??" + key + "??";
			}
			
		}
		
		[Bindable (event="updatedLabelsReplace")] 
		public function getLabelAndReplace(key:String, values:Array):String {
			return _xmlDictionary.getLabelAndReplace(key, values, true);
		}
		
		[Bindable (event="updatedLabelsConcatentate")]
		public function getLabelAndConcatenate(key:String, values:Array):String {
			return _xmlDictionary.getLabelAndConcatenate(key, values);
		}
		
		[Bindable (event="updatedLabelsInsert")]
		public function getLabelAndInsert(key:String, values:Array):String {
			return _xmlDictionary.getLabelAndReplace(key, values, false);
		}
		
		public function dispatchEvents():void {
			this.dispatchEvent(new Event("updatedLabels"));
			this.dispatchEvent(new Event("updatedLabelsReplace"));
			this.dispatchEvent(new Event("updatedLabelsConcatentate"));
			this.dispatchEvent(new Event("updatedLabelsInsert"));
			
			this.dispatchEvent(new Event("updatedValues"));
		}
	}
}