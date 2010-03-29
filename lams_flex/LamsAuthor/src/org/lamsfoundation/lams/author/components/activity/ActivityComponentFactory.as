package org.lamsfoundation.lams.author.components.activity
{
	import mx.core.IUIComponent;
	
	import org.lamsfoundation.lams.author.components.LearningLibraryEntryComponent;
	import org.lamsfoundation.lams.author.components.activity.group.GroupActivityComponent;
	import org.lamsfoundation.lams.author.components.toolbar.SystemToolComponent;
	import org.lamsfoundation.lams.author.controller.AuthorController;
	import org.lamsfoundation.lams.author.model.activity.Activity;
	import org.lamsfoundation.lams.author.model.activity.ActivityFactory;
	import org.lamsfoundation.lams.author.model.activity.CombinedActivity;
	import org.lamsfoundation.lams.author.model.activity.GroupActivity;
	import org.lamsfoundation.lams.author.model.activity.OptionalActivity;
	import org.lamsfoundation.lams.author.model.activity.ToolActivity;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class ActivityComponentFactory
	{
		public function ActivityComponentFactory(){}
		
		public static function getActivityComponentInstance(activity:Activity):ActivityComponent {
			switch (activity.activityTypeID) {
				case Constants.ACTIVITY_TYPE_TOOL:
					var toolActivityComponent:ToolActivityComponent = new ToolActivityComponent();
					toolActivityComponent.activity = activity as ToolActivity;
					return toolActivityComponent;
					break;
				case Constants.ACTIVITY_TYPE_COMBINED:
					var combinedActivityComponent:CombinedActivityComponent = new CombinedActivityComponent();
					combinedActivityComponent.activity = activity as CombinedActivity;
					combinedActivityComponent.initialize();
					return combinedActivityComponent;
					break;
				case Constants.ACTIVITY_TYPE_GROUPING:
					var groupActivityComponent:GroupActivityComponent = new GroupActivityComponent();
					groupActivityComponent.activity = activity as GroupActivity;
					groupActivityComponent.initialize();
					return groupActivityComponent;
				case Constants.ACTIVITY_TYPE_OPTIONAL_ACTIVITY:
					var optionalActivityComponent:OptionalActivityComponent = new OptionalActivityComponent();
					optionalActivityComponent.activity = activity as OptionalActivity;
					return optionalActivityComponent;
				default:
					return null;
			}
		}
		
		public static function getActivityComponentInstanceFromDrag(dragInitiator:IUIComponent, UIID:int):ActivityComponent {
			if (dragInitiator is LearningLibraryEntryComponent) {
				var learningLibraryComponent:LearningLibraryEntryComponent = dragInitiator as LearningLibraryEntryComponent;
				var activity:Activity = ActivityFactory.getActivityInstance(learningLibraryComponent.learningLibraryEntry, UIID);
		        var activityComponent:ActivityComponent = getActivityComponentInstance(activity);
				return activityComponent;
			} else if (dragInitiator is SystemToolComponent){
				var systemToolComponent:SystemToolComponent = dragInitiator as SystemToolComponent;
				var systemActivity:Activity = ActivityFactory.getSystemActivityInstance(systemToolComponent, UIID);
				var systemActivityComponent:ActivityComponent = getActivityComponentInstance(systemActivity);
				return systemActivityComponent;
			} else {
				return null;
			}
		}

	}
}