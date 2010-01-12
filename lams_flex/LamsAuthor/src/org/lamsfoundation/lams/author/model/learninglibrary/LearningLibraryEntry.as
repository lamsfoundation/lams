package org.lamsfoundation.lams.author.model.learninglibrary
{
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	public class LearningLibraryEntry
	{
		public var learningLibraryID:int;
		public var title:String;
		public var validFlag:Boolean;
		public var toolTemplates:ArrayCollection;
		public var category:int;
		public var icon:String;
		
		// For combined tools
		public var isCombined:Boolean;
		public var subIcon1:String;
		public var subIcon2:String;
		public var subTitle1:String;
		public var subTitle2:String;
		
		public function LearningLibraryEntry(dto:Object)
		{
			this.learningLibraryID = parseInt(dto.learningLibraryID);
			this.title = dto.title;
			this.validFlag = Boolean(dto.validFlag);
			toolTemplates = new ArrayCollection();
			
			for each(var toolDTO:Object in dto.templateActivities) {
				var tool:Tool = new Tool(toolDTO, learningLibraryID);
				this.category = tool.activityCategoryID;
				toolTemplates.addItem(tool);
			}
			
			if (toolTemplates.getItemAt(0) != null){
				this.title = toolTemplates.getItemAt(0).toolName;
				this.icon = toolTemplates.getItemAt(0).libraryActivityUIImage;
			} 
			
			if (toolTemplates.length == 3) {
				this.subIcon1 = toolTemplates.getItemAt(1).libraryActivityUIImage;
				this.subIcon2 = toolTemplates.getItemAt(2).libraryActivityUIImage;
				
				this.subTitle1 = toolTemplates.getItemAt(1).toolName;
				this.subTitle2 = toolTemplates.getItemAt(2).toolName;
				
				if (Application.application.TESTING && Application.application.TESTING_LOCAL) {
					subIcon1 = "assets/test/toolimages" + subIcon1.substring(subIcon1.lastIndexOf("/"));
					subIcon2 = "assets/test/toolimages" + subIcon2.substring(subIcon2.lastIndexOf("/"));
				} else {
					subIcon1 = Application.application.lamsURL + subIcon1;
					subIcon2 = Application.application.lamsURL + subIcon2;
				}
				this.isCombined = true;
			}
		}
		
	}
}