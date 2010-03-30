package org.lamsfoundation.lams.author.controller
{
	import flash.geom.Point;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.managers.CursorManager;
	
	import org.lamsfoundation.lams.author.components.CanvasBox;
	import org.lamsfoundation.lams.author.components.LearningLibraryEntryComponent;
	import org.lamsfoundation.lams.author.components.activity.*;
	import org.lamsfoundation.lams.author.components.toolbar.SystemToolComponent;
	import org.lamsfoundation.lams.author.model.activity.Activity;
	import org.lamsfoundation.lams.author.model.activity.GroupActivity;
	import org.lamsfoundation.lams.author.model.activity.ToolActivity;
	import org.lamsfoundation.lams.author.model.learninglibrary.LearningLibraryEntry;
	import org.lamsfoundation.lams.author.util.AuthorUtil;
	import org.lamsfoundation.lams.author.util.Constants;
	import org.lamsfoundation.lams.common.dictionary.XMLDictionaryRegistry;
	
	public class AuthorController
	{
		
		private var dictionaryFallback:XML;
		
		private static var _instance:AuthorController;
		
		private var nextUIID:int = 1;
		private var nextToolContentID:int = 1;
			 	
	 	public var activities:Dictionary = new Dictionary();
	 	
	 	[Bindable] public var selectedActivityComponent:ActivityComponent;
	 	
	 	[Embed("assets/icons/transition.png")] public var transitionCursor:Class;
	 	
	 	
	 	// Reference to all activity components via UIID (key)
	 	public var activityComponents:Dictionary = new Dictionary();
	 	

	 	
	 	// STATES
		public var cursorState:int = Constants.CURSOR_STATE_NORMAL; // State of cursor transition/help/dataflow etc...
		public var uiState:int = Constants.UI_STATE_NORMAL; 		// ui state


		public function AuthorController(){
			if (_instance != null) {
				throw new Error("This is a singleton, do not instanciate, instead use AuthorController.instance");
			}
		}
		
		public static function get instance():AuthorController {
			if (_instance == null) {
				_instance = new AuthorController();
			}
			return _instance;
		}
		
		/**
		 * Sets the learningLibrary object on startup.
		 * 
		 * Cannot use mate dependency injection here as it does after the creationComplete.
		 * Ie the LearningLibraryComponent doesnt have a (non-user) event that would fire after
		 * the learningLibrary data had been populated
		 * 
		 * @param learningLibraryIn
		 */
		public function setLearningLibrary(learningLibraryIn:ArrayCollection):void {
			
			var learningLibrary:Dictionary = new Dictionary();
			
			for each(var learningLibraryEntryObj:Object in learningLibraryIn) {
	   			var learningLibraryEntry:LearningLibraryEntry = new LearningLibraryEntry(learningLibraryEntryObj);
	   			learningLibrary[learningLibraryEntry.learningLibraryID] = learningLibraryEntry;

	   		}
	   		   			
   			// Set the application learning library
   			Application.application.learningLibrary = learningLibrary;
   			Application.application.canvasArea.compLearningLibrary.loadLearningLibrary();
		}
		
		
		public function setDictionaryFallback(xml:XML):void {
			dictionaryFallback = new XML(xml);
		}
		
		public function setDictionary(xml:XML):void {
			Application.application.dictionary = new XMLDictionaryRegistry(xml, dictionaryFallback);
		}
		
		public function generateUIID():int {
			var ret:int = this.nextUIID;
			nextUIID++;
			return ret;
		}
		
		public function generateToolContentID():int {
			var ret:int = this.nextToolContentID;
			nextToolContentID++;
			return ret;
		}
		
		/**
		 * The drop activity function is fired when the user drops an activity directly into the canvas
		 * ie not inside a complex activity like optional. The event is fired by the CanvasBox
		 * 
		 * @param event The drag event fired when the activity is dropped on the canvas
		 */
		public function dropActivity(event:DragEvent):void {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			// Get the current mouse point, and convert it to co-ordinates in CanvasBox
			var comp:UIComponent = UIComponent(event.currentTarget);
			var currentMousePoint:Point = new Point(comp.mouseX, comp.mouseY);
			//currentMousePoint = canvasBox.localToGlobal(currentMousePoint);	
			
			// This condition is true if the activity was already on the canvas
			if (event.dragInitiator is ActivityComponent) {
            	var activityComponent:ActivityComponent;
            	activityComponent = event.dragInitiator as ActivityComponent;
          		activityComponent.rootActivity.xCoord = currentMousePoint.x - activityComponent.mouseOffSetX;
	           	activityComponent.rootActivity.yCoord = currentMousePoint.y - activityComponent.mouseOffSetY;
	           	activityComponent.setCenter();
	           	activityComponent.visible = true;
				activityComponent.updateTransitionPositions();
          		selectActivityComponent(activityComponent);
            } else if (event.dragInitiator is LearningLibraryEntryComponent || event.dragInitiator is SystemToolComponent) {
            	// Adding a new activity to the canvas
            	
            	// Get the next UIID
				var nextActivityUIID:int = generateUIID();
				
	           	// Get the offset point to place the activity
	           	var point:Point = new Point(0,0);
	           	if (event.dragInitiator is LearningLibraryEntryComponent) {
	           		var learningLibraryEntryComponent:LearningLibraryEntryComponent = event.dragInitiator as LearningLibraryEntryComponent
	           		point.x = learningLibraryEntryComponent.mouseOffSetX
	           		point.y = learningLibraryEntryComponent.mouseOffSetY;
	           	} else {
	           		var systemToolComponent:SystemToolComponent = event.dragInitiator as SystemToolComponent
	           		point.x = systemToolComponent.mouseOffSetX
	           		point.y = systemToolComponent.mouseOffSetY;
	           	}
	           	
	           	var newActivityComponent:ActivityComponent = ActivityComponentFactory.getActivityComponentInstanceFromDrag(event.dragInitiator, nextActivityUIID);
	           	newActivityComponent.rootActivity.xCoord = currentMousePoint.x - point.x;
	           	newActivityComponent.rootActivity.yCoord = currentMousePoint.y - point.y;
	            canvasBox.addChild(newActivityComponent);
	            selectActivityComponent(newActivityComponent);
	            activities[nextActivityUIID] = newActivityComponent.rootActivity;	
	            activityComponents[nextActivityUIID] = newActivityComponent;
            }
            
                
            // Return cursor state to normal
			AuthorController.instance.changeCursorState(Constants.CURSOR_STATE_NORMAL);  
		}
		
		
		/**
		 * This function is invoked when an activity is dragged into the bin
		 * it deletes the activity and any related transitions
		 * 
		 * TODO: Check the state of the LD and set it to invalid if neccessary
		 * 
		 * @param activityComponent the acitvity object
		 * 
		 */
		public function deleteActivity(activityComponent:ActivityComponent):void {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;			
			activityComponent.deSelectActivity();
			
			// Remove transition in, if needed
			if (activityComponent.transitionToComponent != null) {
				TransitionController.instance.deleteTransition(activityComponent.transitionToComponent);		
			}
			
			// Remove transition out if needed
			if (activityComponent.transitionFromComponent != null) {
				TransitionController.instance.deleteTransition(activityComponent.transitionFromComponent);
			}
			
			// Remove from reference array
			this.activityComponents[activityComponent.rootActivity.activityUIID] = null;
			
			// Remove activity
			canvasBox.removeChild(activityComponent);
			activityComponent = null;
		}
		
				
		
		public function selectActivityComponent(activityComponent:ActivityComponent):void {
			deSelectActivityComponent();
			this.selectedActivityComponent = activityComponent;
			activityComponent.selectActivity(); 
			Application.application.canvasArea.propertyInspector.openPropertyInspector();
		}
		
		public function deSelectActivityComponent():void {
			if (this.selectedActivityComponent!= null) {
				this.selectedActivityComponent.deSelectActivity();
				this.selectedActivityComponent = null;
				Application.application.canvasArea.propertyInspector.closePropertyInspector();
			}
		}
		
		public function getAllPossibleGroupings(activityComponent:ActivityComponent):ArrayCollection {
			var currentAvailableGroups:ArrayCollection = new ArrayCollection();	
			
			
			// Make a null grouping for this activity
			var nullGrouping:GroupActivity = new GroupActivity(0, 1);
			nullGrouping.title = Application.application.dictionary.getLabel("none_act_lbl");
			currentAvailableGroups.addItem(nullGrouping);			
			currentAvailableGroups.addAll(activityComponent.allPossibleGroupings);
			
			return currentAvailableGroups;
			
			// TODO: Need to traverse the learning design array to find any previous activities
			// that are grouping activities
			
			
			/* 
			var activity:Activity = activityComponent.rootActivity;
			
			if (activity.transitionIn != null){
				var activityCmp:ActivityComponent = this.transitionIn.fromActivity;
				
				while (true) {
					if (activityCmp is GroupActivityComponent) {
						var entry:Object = new Object();
						entry.activity.activityUIID = activityCmp.activity.activityUIID;
						entry.activity.title = activityCmp.activity.title;
						ret.addItem(entry);
					}
					
					if (activityCmp.transitionIn != null) {
						activityCmp = activityCmp.transitionIn.fromActivity;
					} else {
						break;
					}
				}  
			} */
			
			
			/* groupingCombo.selectedItem = nullGrouping;
			for each (var item:Object in currentAvailableGroups) {
				if (activityComponent.groupingUIID == item.activityUIID) {
					groupingCombo.selectedItem = item;
				}
			} */
		}
		
		

		public function changeCursorState(state:int):void{
			cursorState = state;
			CursorManager.removeCursor(CursorManager.currentCursorID);
			switch (cursorState) {
				case Constants.CURSOR_STATE_NORMAL:
					break;
				case Constants.CURSOR_STATE_TRANSITION:
					CursorManager.setCursor(transitionCursor);
					break;
			}
		}
		
		public static function activitySupportsGrouping(activityTypeID:int, activity:Activity):Boolean {
	   		if (AuthorUtil.activitySupportsGrouping(activityTypeID)) {
	   			if (activity is ToolActivity) {
	   				return (activity as ToolActivity).groupingEnabled;
	   			} else {
	   				return true;
	   			}
	   		} else {
	   			return false;
	   		}
	   		
	   	}
	}
}