package org.lamsfoundation.lams.author.model.activity
{
	import org.lamsfoundation.lams.author.model.learninglibrary.Tool;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class CombinedActivity extends Activity
	{
		[Bindable] public var tool:Tool;
		
		[Bindable] public var toolActivity1:ToolActivity;
		[Bindable] public var toolActivity2:ToolActivity;
		
	
		
		public function CombinedActivity(activityUIID:Number)
		{
			super(activityUIID);
			this.activityTypeID = Constants.ACTIVITY_TYPE_COMBINED;
		}
		
	}
}