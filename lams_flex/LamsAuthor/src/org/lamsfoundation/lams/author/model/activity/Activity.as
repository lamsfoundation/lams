package org.lamsfoundation.lams.author.model.activity
{
	import org.lamsfoundation.lams.author.model.activity.group.GroupActivity;
	import org.lamsfoundation.lams.author.model.transition.Transition;
	
	public class Activity
	{
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
		[Bindable] public var activityUIID:int;
		public var activityCategoryID:Number;
		[Bindable] public var activityTypeID:int;
		public var learningLibraryID:Number;
		public var learningDesignID:Number;
		public var parentActivityID:Number;		
		public var parentUIID:int;
		public var orderID:Number;
		public var groupingID:Number;			
		[Bindable] public var groupingUIID:Number;
		[Bindable] public var title:String;
		public var description:String;
		public var helpText:String;
		[Bindable]public var xCoord:Number;	
		[Bindable]public var yCoord:Number;
		public var libraryActivityUIImage:String;	// Possibly not needed because of Tool object
		public var createDateTime:Date;
		public var groupingSupportType:Number;	// Possibly not needed because of Tool object	
		
		// Activity state
		[Bindable] public var runOffline:Boolean;
		public var applyGrouping:Boolean;
		public var stopAfterActivity:Boolean;
		public var readOnly:Boolean;
		[Bindable] public var defineLater:Boolean;
		public var isActivitySelected:String		// Not found in wddx xml
			
		// Not found in wddx xml
		public var objectType:String;
		public var libraryActivityID:Number;
		public var activityToolContentID:Number;
		public var viewID:Boolean;
		//public var branchView:CanvasBranchView;
		public var i18nActivityTypeString:String;
		
		//Transitions
		public var transitionTo:Transition;
		public var transitionFrom:Transition;
	     
	    function Activity(activityUIID:int){
	    	this.activityUIID = activityUIID;
		}
	}
}