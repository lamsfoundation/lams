package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class LearningDesign
	{
		public var activities:ArrayCollection;
		public var branchMappings:ArrayCollection;
		public var competences:ArrayCollection;
		public var contentFolderID:String;
		public var copyTypeID:uint;
		public var createDateTime:Date;
		public var designVersion:uint;
		public var editOverrideLock:Boolean;
		public var firstActivityID:uint;
		public var firstActivityUIID:uint;
		public var floatingActivityID:uint;
		public var groupings:ArrayCollection;
		public var lastModifiedDateTime:Date;
		public var learningDesignID:uint;
		public var maxID:uint;
		public var originalLearningDesignID:uint;
		public var readOnly:Boolean;
		public var title:String;
		public var transitions:ArrayCollection;
		public var userID:uint;
		public var validDesign:Boolean;
		public var version:String;
		public var workspaceFolderID:uint;

		public function LearningDesign() {
			
		}
		
		public function setFromWDDX(WDDXObject:Object):void{
			 activities = WDDXObject.activities;
			 branchMappings = WDDXObject.branchMappings;
			 competences = WDDXObject.competences;
			 contentFolderID = WDDXObject.contentFolderID;
			 copyTypeID = WDDXObject.copyTypeID;
			 createDateTime = new Date(WDDXObject.createDateTime);
			 designVersion = WDDXObject.designVersion;
			 editOverrideLock = WDDXObject.editOverrideLock;
			 firstActivityID = WDDXObject.firstActivityID;
			 firstActivityUIID = WDDXObject.firstActivityUIID;
			 floatingActivityID = WDDXObject.floatingActivityID;
			 groupings = WDDXObject.groupings;
			 lastModifiedDateTime = new Date(WDDXObject.lastModifiedDateTime);
			 learningDesignID = WDDXObject.learningDesignID;
			 maxID = WDDXObject.maxID;
			 originalLearningDesignID = WDDXObject.originalLearningDesignID;
			 readOnly = WDDXObject.readOnly;
			 title = WDDXObject.title;
			 transitions = WDDXObject.transitions;
			 userID = WDDXObject.userID;
			 validDesign = WDDXObject.validDesign;
			 version = WDDXObject.version;
			 workspaceFolderID = WDDXObject.workspaceFolderID;
		}
	}
}