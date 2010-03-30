package org.lamsfoundation.lams.author.model.activity
{
	import org.lamsfoundation.lams.author.components.toolbar.SystemToolComponent;
	import org.lamsfoundation.lams.author.controller.AuthorController;
	import org.lamsfoundation.lams.author.model.learninglibrary.LearningLibraryEntry;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class ActivityFactory
	{
		public function ActivityFactory(){}
		
		/**
		 * Gets an activity instance given a learningLibraryEntry 
		 * @param learningLibraryEntry the learningLibraryEntry
		 * @param UIID the user interface id for the new activity
		 * @return the activity
		 * 
		 */
		public static function getActivityInstance(learningLibraryEntry:LearningLibraryEntry, UIID:int):Activity {
			
			switch (learningLibraryEntry.activityTypeID) {
				case Constants.ACTIVITY_TYPE_TOOL:
					return getToolActivityInstance(learningLibraryEntry, UIID);
					break;
				case Constants.ACTIVITY_TYPE_COMBINED:
					return getCombinedActivityInstance(learningLibraryEntry, UIID);	
					break;
				default:
					return null;
				
			}
		}
		
		/**
		 * Gets a system activity instance given a systemToolComponent
		 * this is for groupings,optionals,branching,gates etc 
		 * @param learningLibraryEntry the learningLibraryEntry
		 * @param UIID the user interface id for the new activity
		 * @return the activity
		 * 
		 */
		public static function getSystemActivityInstance(systemToolComponent:SystemToolComponent, UIID:int):Activity {
			
			switch (systemToolComponent.activityTypeID) {
				case Constants.ACTIVITY_TYPE_GROUPING:
					return getGroupActivityInstance(systemToolComponent, UIID);
					break;
				case Constants.ACTIVITY_TYPE_OPTIONAL_ACTIVITY: 
					return getOptionalActivityInstance(systemToolComponent, UIID);
					break;
				default:
					return null;
				
			}
		}
		
		private static function getToolActivityInstance(learningLibraryEntry:LearningLibraryEntry, UIID:int):ToolActivity {
			var toolActivity:ToolActivity = new ToolActivity(UIID);
    		toolActivity.tool = learningLibraryEntry.toolTemplates[0];
    		toolActivity.title = toolActivity.tool.toolName;
    		return toolActivity;
		}
		
		private static function getCombinedActivityInstance(learningLibraryEntry:LearningLibraryEntry, UIID:int):CombinedActivity {
			var tool1UIID:int = UIID;
    		var tool2UIID:int = UIID;
    		if (UIID != 0) {
    			// this is a canvas instance, need new uuids
    			tool1UIID = AuthorController.instance.generateUIID();
    			tool2UIID = AuthorController.instance.generateUIID();
    		}
    		
    		var combinedActivity:CombinedActivity = new CombinedActivity(UIID);
    		combinedActivity.tool = learningLibraryEntry.toolTemplates[0];
    		combinedActivity.title = combinedActivity.tool.toolName;
    		
    		// Create toolactivity 1
    		var toolActivity1:ToolActivity = new ToolActivity(tool1UIID);
			toolActivity1.tool = learningLibraryEntry.toolTemplates[1];
    		toolActivity1.title = toolActivity1.tool.toolName;
    		toolActivity1.groupingEnabled = false;
    		combinedActivity.toolActivity1 = toolActivity1;
    		
    		// Create toolactivity 2
    		var toolActivity2:ToolActivity = new ToolActivity(tool1UIID);
			toolActivity2.tool = learningLibraryEntry.toolTemplates[2];
    		toolActivity2.title = toolActivity2.tool.toolName;
    		toolActivity2.groupingEnabled = false;
    		combinedActivity.toolActivity2 = toolActivity2;
    		
    		return combinedActivity;
		}
		
		private static function getGroupActivityInstance(systemToolComponent:SystemToolComponent, UIID:int):GroupActivity {
			var groupActivity:GroupActivity = new GroupActivity(UIID, systemToolComponent.groupingType);
			return groupActivity;
		}
		
		private static function getOptionalActivityInstance(systemToolComponent:SystemToolComponent, UIID:int):OptionalActivity {
			var optionalActivity:OptionalActivity = new OptionalActivity(UIID);
			return optionalActivity;
		}
		
		

	}
}