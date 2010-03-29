package org.lamsfoundation.lams.author.model.activity
{
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class GroupActivity extends Activity
	{
		public var groupingType:int;
		[Bindable]public var numGroups:Number = 2;
		[Bindable]public var groupNames:ArrayCollection = new ArrayCollection();
		[Bindable]public var toolTip:String;
		
		public function GroupActivity(UIID:int, groupingType:int)
		{
			super(activityUIID);
			this.activityTypeID = Constants.ACTIVITY_TYPE_GROUPING;
			this.groupingType = groupingType;
			
			this.title = Application.application.dictionary.getLabel("grouping_act_title");
			this.groupNames.addItem(Application.application.dictionary.getLabel('group_btn') + " 1");
			this.groupNames.addItem(Application.application.dictionary.getLabel('group_btn') + " 2");
			
			switch (groupingType) {
				case Constants.GROUPING_TYPE_RANDOM:
					this.toolTip = Application.application.dictionary.getLabel('pi_random_group_activity');
					break;
				case Constants.GROUPING_TYPE_TEACHER_CHOSEN:
					this.toolTip = Application.application.dictionary.getLabel('pi_monitor_group_activity');
					break;
				case Constants.GROUPING_TYPE_STUDENT_CHOICE:
					this.toolTip = Application.application.dictionary.getLabel('pi_learner_group_activity');
					break;
				
			} 
		}
	}
}