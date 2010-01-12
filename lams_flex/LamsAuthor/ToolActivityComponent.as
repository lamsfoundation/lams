package org.lamsfoundation.lams.author.components.activity
{
	import mx.controls.Image;
	
	public class ToolActivityComponent extends ActivityComponent
	{
		//import org.lamsfoundation.lams.author.model.ToolActivity;
		//import org.lamsfoundation.lams.author.model.LearningLibraryEntry;
		
		//public var _toolActivity:ToolActivity;
		
		
		public var _title:String;
		public var _image:Image;
		
		public function ToolActivityComponent()
		{
			this.height = 30;
			this.width = 30;
			
			this.setStyle("backgroundColor", "green");



		}

	}
}