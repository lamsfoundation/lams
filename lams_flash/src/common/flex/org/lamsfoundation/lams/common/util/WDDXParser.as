package org.lamsfoundation.lams.common.util
{
	
	import flash.xml.XMLNode;
	
	import pl.cfml.coldfusion.as3.utils.WDDX;
	
	public class WDDXParser
	{
		
		public function WDDXParser()
		{
		}

		public function parseObject(node:XMLNode):Object {
			var xml:XML = new XML(node);
			if(node == null)
				return null;
			
			var data:Object = WDDX.fromWDDX(xml.toXMLString());
			
			
			return data.messageValue;
		}
		
		public function createWDDX(data:Object):XML {
			if(data == null)
				return null;
			
			var xml:String = WDDX.toWDDX(data);
			var returnXML:XML = new XML(unescape(xml));
			
			trace('xml: ' + returnXML);
			return returnXML;
		}

	}
}