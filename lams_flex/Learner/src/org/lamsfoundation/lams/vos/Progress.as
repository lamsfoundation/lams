package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class Progress
	{
		public var attemptedActivities:ArrayCollection;
		public var completedActivities:ArrayCollection;
		public var currentActivityId:uint;
		public var firstName:String;
		public var lastName:String;
		public var learnerID:uint;
		public var lessonComplete:Boolean;
		public var lessonID:uint;
		public var lessonName:String;
		public var userName:String;

		public function Progress() {
			
		}
		
		public function setFromArgs(myAttemptedActivities:ArrayCollection, myCompletedActivities:ArrayCollection, myCurrentActivityId:uint):void{
			attemptedActivities = myAttemptedActivities;
			completedActivities = myCompletedActivities;
			currentActivityId = myCurrentActivityId;
		}
		
		public function setFromWDDX(WDDXObject:Object):void{
			attemptedActivities = WDDXObject.attemptedActivities;
			completedActivities = WDDXObject.completedActivities;
			currentActivityId = WDDXObject.currentActivityId;
			firstName = WDDXObject.firstName;
			lastName = WDDXObject.lastName;
			learnerID = WDDXObject.learnerId;
			lessonComplete = WDDXObject.lessonComplete;
			lessonID = WDDXObject.lessonId;
			lessonName = WDDXObject.lessonName;
			userName = WDDXObject.userName;
		}
	}
}