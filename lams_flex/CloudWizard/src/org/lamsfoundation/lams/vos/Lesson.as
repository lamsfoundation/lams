package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	import org.lamsfoundation.lams.common.util.WDDXUtil;
	
	public class Lesson
	{
		public var lessonID:uint;
		public var lessonName:String;
		public var lessonDescription:String;
		public var organisationID:uint;
		public var learningDesignID:uint;
		
		// Advanced options
		public var learnerExportPortfolio:Boolean;
		public var enablePresence:Boolean;
		public var enableLiveEdit:Boolean;
		public var enableIm:Boolean;
		
		public var enableTimeLimits:Boolean;
		public var scheduledNumberDaysToLessonFinish:int;
		public var enableIndividualTimeLimit:Boolean;
		
		public var enableScheduling:Boolean;
		
		public var numberLessonsSplit:int;
		public var numberLearnersSplit:int;
	
		// new Lesson copy
		public var copyType:uint = 2;
	
		// Lesson class - learners and staff users
		public var learners:UserCollection;
		public var staff:UserCollection;
	
		public function Lesson() {
			learners = new UserCollection();
			staff = new UserCollection();
		}
		
		public function get toData():Object {
			var data:Object = new Object();
			data.lessonID = this.lessonID;
			data.lessonName = this.lessonName;
			data.lessonDescription = WDDXUtil.toWDDXNull(this.lessonDescription);
			data.organisationID = this.organisationID;
			data.learningDesignID = this.learningDesignID;
			data.learnerExportPortfolio = this.learnerExportPortfolio;
			
			data.enablePresence = this.enablePresence;
			data.enableLiveEdit = this.enableLiveEdit;
			data.enableIm = this.enableIm;
			
			if (this.enableTimeLimits && this.enableIndividualTimeLimit) {
				data.scheduledNumberDaysToLessonFinish = WDDXUtil.toWDDXNull(this.scheduledNumberDaysToLessonFinish);
			}
			
			data.numberLessonsSplit = WDDXUtil.toWDDXNull(this.numberLessonsSplit);
			data.numberLearnersSplit = WDDXUtil.toWDDXNull(this.numberLearnersSplit);
			
			data.copyType = this.copyType;
			
			data.learners = learners.toData;
			data.staff = staff.toData;
			
			return data;
		}
	}
	
}