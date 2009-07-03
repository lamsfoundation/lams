package org.lamsfoundation.lams.common.dictionary
{
	import flash.events.EventDispatcher;
	
	public class XMLDictionary
	{
		private var eventDispatcher:EventDispatcher;
		private var dictionaryXML:XML;
		
		public function XMLDictionary(xml:XML)
		{
			eventDispatcher = new EventDispatcher();
			dictionaryXML = new XML(xml);
			
			eventDispatcher.dispatchEvent(new XMLDictionaryEvent(XMLDictionaryEvent.COMPLETE));
		}
		
		public function getValue(s:String):String {
			if(!isEmpty()) {
				var value:String = dictionaryXML.language.entry.(@key==s).data;
				return value;
			}
			
			return null;
		}
		
		public function getLabel(s:String):String{
			if(!isEmpty()){
				return dictionaryXML.language.entry.(@key==s).name;
			}
			
			return null;
		}
		
		public function getLabelAndConcatenate(s:String, a:Array):String{
			if(!isEmpty()){
				var concat:String = "";
				for(var i:int = 0; i < a.length; i++){
					concat += a[i];
				}
				
				return getLabel(s) + concat;
			}
			
			return null;
		}
		
		public function getLabelAndReplace(s:String, a:Array, useKey:Boolean):String{
			if(!isEmpty()) {
				var label:String = getLabel(s);
				
				for(var i:int = 0; i < a.length; i++){
					var replaceStr:String = (useKey) ? getLabel(a[i]) : a[i];
					label = label.replace("{" + String(i) + "}", replaceStr);
				}
				
				return label;
			}
			
			return null;
		}
		
		public function isEmpty():Boolean{
			return dictionaryXML.toString() == "";
		}
	}
}