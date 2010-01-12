package org.lamsfoundation.lams.vos
{
	
	public class CompletedActivity
	{
		
		public var aid:uint;
		public var title:String;
		
		public var start_datetime:Number;
		public var completed_datetime:Number;
		
		public var dataObject:Object
		
		// constructor
		public function CompletedActivity(completedActivity:Object)
		{
			this.aid = completedActivity.completedActivityId;
			this.title = completedActivity.completedActivityTitle;
			this.start_datetime = completedActivity.startDateTime;
			this.completed_datetime = completedActivity.completedDateTime;
		}
		
		public function createDataObject(learnerProgress:LearnerProgress):void {
			var c_started__datetime:Number = (this.start_datetime/1000);
			var c_completed__datetime:Number = (this.completed_datetime/1000);
			var c_duration:Number = c_completed__datetime - c_started__datetime;
													
			dataObject = {Learner: learnerProgress.learner_username, 
								Activity: this.aid,
								Title: this.title, 
								Completed: Math.floor(c_completed__datetime), 
								Duration: Math.floor(c_duration)};
				
		}
		
		public function cloneDataObject(learner:String):Object {
			var dataObj:Object = {Learner: learner, 
									Activity: this.aid,
									Title: this.title,
									Completed: this.dataObject.Completed,
									Duration: this.dataObject.Duration};
									
			return dataObj;
		}

	}
}