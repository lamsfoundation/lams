package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class Activity
	{
		public var activityID:uint;
		public var activityTitle:String;
		public var activityTypeID:uint;
		public var activityUIID:uint;
		public var activityChildren:ArrayCollection;
		
		public function Activity() {
			
		}
		
		public function setFromArgs(myActivityID:uint, myActivityTitle:String, myActivityTypeID:uint, myActivityUIID:uint):void{
			activityID = myActivityID;
			activityTitle = myActivityTitle
			activityTypeID = myActivityTypeID;
			activityUIID = myActivityUIID;
		}
		
		public function setFromXML(xml:XML):void{
			activityID = xml.@activityID;
			activityTitle = xml.@activityTitle
			activityTypeID = xml.@activityTypeID;
			activityUIID = xml.@activityUIID;
		}
		
		public function setFromWDDX(WDDXObject:Object):void{

		}
	}
}