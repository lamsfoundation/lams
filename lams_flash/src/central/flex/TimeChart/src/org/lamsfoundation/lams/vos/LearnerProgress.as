package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	public class LearnerProgress
	{
		public var learner_uid:uint;
		public var learner_fname:String;
		public var learner_lname:String;
		
		[Bindable]
		public var learner_username:String;
		
		public var lesson_id:uint;
		public var lesson_name:String;
		public var lesson_complete:Boolean;
		
		public var lesson_starttime:Number;
		public var learner_starttime:Number;
		
		[Bindable]
		private var completed_activities:ArrayCollection;
	
		private var sort:Sort;
	
		// constructor
		public function LearnerProgress(learnerProgress:Object)
		{
			completed_activities = new ArrayCollection();
			
			sort = new Sort();
			sort.fields = [new SortField("completed_datetime", false), new SortField("aid", false)];
			
			this.learner_uid = learnerProgress.learnerId;
			this.learner_fname = learnerProgress.firstName;
			this.learner_lname = learnerProgress.lastName;
			this.learner_username = learnerProgress.userName;
			
			this.lesson_id = learnerProgress.lessonId;
			this.lesson_name = learnerProgress.lessonName;
			this.lesson_complete = learnerProgress.lessonComplete;
			this.lesson_starttime = learnerProgress.lessonStartTime;
			this.learner_starttime = learnerProgress.learnerStartTime;
		}
		
		public function addCompletedActivity(completedActivity:CompletedActivity):void {
			completed_activities.addItem(completedActivity);
		}
		
		public function getCompletedActivity(index:int):CompletedActivity {
			return this.completedActivities.getItemAt(index) as CompletedActivity;
		}
		
		public function getNoOfCompletedActivities():int {
			return completed_activities.length;
		}
		
		public function get completedActivities():ArrayCollection {
			completed_activities.sort = sort;
			completed_activities.refresh();
			
			return completed_activities;
		}
		
		public function get userName():String {
			return learner_username;
		}

	}
}