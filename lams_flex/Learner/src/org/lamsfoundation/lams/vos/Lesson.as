package org.lamsfoundation.lams.vos
{
	import org.lamsfoundation.lams.common.util.WDDXParser;
	
	[Bindable]
	public class Lesson
	{
		public var createDateTime:Date;
		public var createDateTimeString:String;
		public var learnerExportAvailable:Boolean;
		public var learnerImAvailable:Boolean;
		public var learnerPresenceAvailable:Boolean;
		public var learningDesignID:uint;
		public var lessonID:uint;
		public var lessonName:String;
		public var lessonStateID:uint;
		public var organisationID:uint;
		public var startDateTime:Date;

		public function Lesson() {
			
		}
		
		public function setFromWDDX(WDDXObject:Object):void{
			createDateTime =  new Date(WDDXObject.createDateTime);
			createDateTimeString = WDDXObject.createDateTimeString;
			learnerExportAvailable = WDDXObject.learnerExportAvailable;
			learnerImAvailable = WDDXObject.learnerImAvailable;
			learnerPresenceAvailable = WDDXObject.learnerPresenceAvailable;
			learningDesignID = WDDXObject.learningDesignID;
			lessonID = WDDXObject.lessonID;
			lessonName = WDDXObject.lessonName;
			lessonStateID = WDDXObject.lessonStateID;
			organisationID = WDDXObject.organisationID;
			startDateTime = new Date(WDDXObject.startDateTime);
		}
	}
}