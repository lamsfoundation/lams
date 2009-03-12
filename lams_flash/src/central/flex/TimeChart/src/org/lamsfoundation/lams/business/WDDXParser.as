package org.lamsfoundation.lams.business
{
	
	import flash.xml.XMLNode;
	import mx.collections.ArrayCollection;
	import org.lamsfoundation.lams.vos.LearnerProgress;
	
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

	}
}