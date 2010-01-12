package org.lamsfoundation.lams.author.model
{
	public class Activity
	{
		// Activity types
		public static var TOOL_ACTIVITY_TYPE:Number = 1;
		public static var GROUPING_ACTIVITY_TYPE:Number = 2;
		public static var NO_GATE_ACTIVITY_TYPE:Number = 30
		public static var SYNCH_GATE_ACTIVITY_TYPE:Number = 3;
		public static var SCHEDULE_GATE_ACTIVITY_TYPE:Number = 4;
		public static var PERMISSION_GATE_ACTIVITY_TYPE:Number = 5;
		public static var PARALLEL_ACTIVITY_TYPE:Number = 6;
		public static var OPTIONAL_ACTIVITY_TYPE:Number = 7;
		public static var SEQUENCE_ACTIVITY_TYPE:Number = 8;
		public static var SYSTEM_GATE_ACTIVITY_TYPE:Number = 9;
		public static var CHOSEN_BRANCHING_ACTIVITY_TYPE:Number = 10;
		public static var GROUP_BRANCHING_ACTIVITY_TYPE:Number = 11;
		public static var TOOL_BRANCHING_ACTIVITY_TYPE:Number = 12;
		public static var OPTIONS_WITH_SEQUENCES_TYPE:Number = 13;
		public static var CONDITION_GATE_ACTIVITY_TYPE:Number = 14;
		public static var REFERENCE_ACTIVITY_TYPE:Number = 15;
	
		// Activity categories
		public static var CATEGORYSYSTEM:Number = 1;
		public static var CATEGORYCOLLABORATION:Number = 2;
		public static var CATEGORYASSESSMENT:Number = 3;
		public static var CATEGORYCONTENT:Number = 4;
		public static var CATEGORYSPLIT:Number = 5;

		// Grouping support types
		public static var GROUPINGSUPPORTNONE:Number = 1;
		public static var GROUPINGSUPPORTOPTIONAL:Number = 2;
		public static var GROUPINGSUPPORTREQUIRED:Number = 3;
		
		// Activity Properties
		public var activityID:Number;
		public var activityUIID:Number;
		public var activityCategoryID:Number;
		public var activityTypeID:Number;
		public var learningLibraryID:Number;
		public var learningDesignID:Number;
		public var parentActivityID:Number;		
		public var parentUIID:Number;
		public var orderID:Number;
		public var groupingID:Number;			
		public var groupingUIID:Number;
		public var title:String;
		public var description:String;
		public var helpText:String;
		public var xCoord:Number;	
		public var yCoord:Number;
		public var libraryActivityUIImage:String;	// Possibly not needed because of Tool object
		public var createDateTime:Date;
		public var groupingSupportType:Number;	// Possibly not needed because of Tool object	
		
		// Activity state
		public var runOffline:Boolean;
		public var applyGrouping:Boolean;
		public var stopAfterActivity:Boolean;
		public var readOnly:Boolean;
		public var defineLater:Boolean;
		public var isActivitySelected:String		// Not found in wddx xml
		
		
		// Not found in wddx xml
		public var objectType:String;
		public var libraryActivityID:Number;
		public var activityToolContentID:Number;
		public var viewID:Boolean;
		//public var branchView:CanvasBranchView;
		public var i18nActivityTypeString:String;
	     
	    function Activity(activityUIID:Number){
		}

	}
}