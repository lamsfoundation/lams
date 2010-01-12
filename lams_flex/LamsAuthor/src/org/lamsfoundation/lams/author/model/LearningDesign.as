package org.lamsfoundation.lams.author.model
{
	public class LearningDesign
	{
		public static var COPY_TYPE_ID_AUTHORING:Number = 1;
		public static var COPY_TYPE_ID_RUN:Number = 2;
		public static var COPY_TYPE_ID_PREVIEW:Number = 3;
		
		// Learning design properties
		private var learningDesignID:Number;
		private var title:String;
		private var contentFolderID:String;
		private var userID:Number;
		private var workspaceFolderID:Number;
		private var lastModifiedDateTime:Date;
		private var designVersion:Number;
		
		private var copyTypeID:Number;
		private var version:String;
		private var designVersion:Number;
		private var createDateTime:Date;
		private var lastModifiedDateTime:Date;
		
		// Learning Design state
		private var readOnly:Boolean;
		private var editOverrideLock:Boolean
		private var maxID:Number;
		private var validDesign:Boolean;	
		private var saveMode:Number;				// Not found in wddx xml
		private var autoSaved:Boolean;				// Not found in wddx xml
		private var modified:Boolean;				// Not found in wddx xml
		
		// First activity
		private var firstActivityID:Number;
		private var firstActivityUIID:Number;
		
		// Floating activity
		private var floatingActivityUIID:Number;
		private var floatingActivityID:Number;
		
		// Collections
		private var branchMappings:Hashtable;
		private var competences:Hashtable;
		private var groupings:Hashtable;
		private var activities:Hashtable;
		private var transitions:Hashtable;
		private var outputConditions:Hashtable;  	// Not obvious in wddx xml
		private var branches:Hashtable;			// Not obvious in wddx xml
		
		// Not found in wddx xml
		private var objectType:String;
		private var prevLearningDesignID:Number;
		private var helpText:String;
		private var editOverrideUserID:Number;
		private var editOverrideUserFullName:String;
		private var duration:Number;
		private var parentLearningDesignID:Number;
		private var licenseID:Number;
		private var licenseText:String;
		private var dateReadOnly:Date;
		
		public function LearningDesign()
		{
		}

	}
}