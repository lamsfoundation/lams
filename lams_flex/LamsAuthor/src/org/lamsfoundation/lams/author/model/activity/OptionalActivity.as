package org.lamsfoundation.lams.author.model.activity
{
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class OptionalActivity extends Activity
	{
		[Bindable] public var maxOptions:int = 0;
		[Bindable] public var minOptions:int = 0;
		[Bindable] public var instructions:String;
		
		[Bindable] public var activities:ArrayCollection = new ArrayCollection();
		
		public function OptionalActivity(activityUIID:Number)
		{
			super(activityUIID);
			this.activityTypeID = Constants.ACTIVITY_TYPE_OPTIONAL_ACTIVITY;
			this.title = Application.application.dictionary.getLabel('opt_activity_title');
		}
	}
}