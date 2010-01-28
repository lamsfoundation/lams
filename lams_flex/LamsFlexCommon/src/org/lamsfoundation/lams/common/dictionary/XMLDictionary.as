package org.lamsfoundation.lams.common.dictionary
{
	import flash.events.EventDispatcher;
	
	public class XMLDictionary
	{
		private var eventDispatcher:EventDispatcher;
		private var dictionaryXML:XML;
		private var fallbackXML:XML;
		
		
		public function XMLDictionary(xml:XML, fallbackXMLIn:XML = null)
		{
			eventDispatcher = new EventDispatcher();
			dictionaryXML = new XML(xml);
			
			fallbackXML = new XML(fallbackXMLIn);
			
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
			if(!isEmpty()) {
				var value:String = dictionaryXML.language.entry.(@key==s).name;
				
				// If we dont get a value, default to fallback
				if ((value == null || value == "") && fallbackXML != null && fallbackXML.toString() != ""){
					value = fallbackXML.language.entry.(@key==s).name;
				} 
				
				// If a label has been found, return it, otherwise print an error string
				if (!(value == null || value == "")) {
					return value;
				}
			} 
			
			return "??" + s + "??";
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