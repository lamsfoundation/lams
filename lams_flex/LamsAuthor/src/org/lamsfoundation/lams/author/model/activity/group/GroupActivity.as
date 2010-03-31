package org.lamsfoundation.lams.author.model.activity.group
{
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.author.controller.AuthorController;
	import org.lamsfoundation.lams.author.model.activity.Activity;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class GroupActivity extends Activity
	{
		[Bindable] public var groups:ArrayCollection = new ArrayCollection();
		[Bindable] public var toolTip:String;
		
		[Bindable] public var useGroupNumber:Boolean = true;
		[Bindable] public var viewStudentsBeforeSelection:Boolean = false;
		[Bindable] public var equalGroupSizes:Boolean = false;
		
		[Bindable] public var groupingTypeID:int;
		[Bindable] public var numberOfGroups:int = 2;
		[Bindable] public var maxNumberOfGroups:int;
		[Bindable] public var learnersPerGroups:int = 1;
		
		public var isNullGrouping:Boolean = false;  // Non persistent field
		
		public function GroupActivity(UIID:int, groupingTypeID:int)
		{
			super(activityUIID);
			this.activityTypeID = Constants.ACTIVITY_TYPE_GROUPING;
			this.groupingTypeID = groupingTypeID;
			
			this.title = Application.application.dictionary.getLabel("grouping_act_title");

			switch (groupingTypeID) {
				case Constants.GROUPING_TYPE_RANDOM:
					this.toolTip = Application.application.dictionary.getLabel('pi_random_group_activity');
					break;
				case Constants.GROUPING_TYPE_TEACHER_CHOSEN:
					this.toolTip = Application.application.dictionary.getLabel('pi_monitor_group_activity');
					break;
				case Constants.GROUPING_TYPE_LEARNER_CHOICE:
					this.toolTip = Application.application.dictionary.getLabel('pi_learner_group_activity');
					break;
				
			} 
		}
		
		/**
		 * Set the number of groups
		 * Need to create new group instances when there are more groups
		 * Or delete them if there are less
		 * 
		 * @param groupNum
		 * 
		 */
		public function setGroupNum(groupNum:int):void {
			if (groupNum > numberOfGroups) {
				for (var i:int = numberOfGroups; i<groupNum; i++){
					var newUIID:int = AuthorController.instance.generateUIID();
					var group:Group = new Group(newUIID, this.activityUIID,  generateGroupName(), i-1);
					groups.addItem(group);
				}
			} else {
				for (var j:int=numberOfGroups - 1; j>groupNum-1; j--){
					groups.removeItemAt(j);
				}
			}
			numberOfGroups = groupNum;
		}
		
		private function generateGroupName():String {
			return Application.application.dictionary.getLabel('group_btn') + " " + (numberOfGroups + 1);
		}
		
		public static function get nullGroupActivity():GroupActivity {
			var nullGroup:GroupActivity = new GroupActivity(0, 0);
			nullGroup.title = Application.application.dictionary.getLabel("none_act_lbl");
			nullGroup.isNullGrouping = true;
			return nullGroup;
		}
	}
}