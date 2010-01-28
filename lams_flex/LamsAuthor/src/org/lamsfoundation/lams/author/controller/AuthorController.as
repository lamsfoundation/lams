package org.lamsfoundation.lams.author.controller
{
	import flash.geom.Point;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	
	import org.lamsfoundation.lams.author.components.CanvasBox;
	import org.lamsfoundation.lams.author.components.LearningLibraryEntryComponent;
	import org.lamsfoundation.lams.author.components.activity.*;
	import org.lamsfoundation.lams.author.components.toolbar.SystemActivityComponent;
	import org.lamsfoundation.lams.author.components.transition.TransitionComponent;
	import org.lamsfoundation.lams.author.events.TransitionEvent;
	import org.lamsfoundation.lams.author.model.learninglibrary.LearningLibraryEntry;
	import org.lamsfoundation.lams.author.util.Constants;
	import org.lamsfoundation.lams.common.dictionary.XMLDictionaryRegistry;
	
	public class AuthorController
	{
		
		private var dictionaryFallback:XML;
		
	 	public var transitionArray:ArrayCollection = new ArrayCollection();
		
		public function AuthorController(){}
		
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
   			//Application.application.canvasArea.compLearningLibrary2.loadLearningLibrary();
		}
		
		
		public function setDictionaryFallback(xml:XML):void {
			dictionaryFallback = new XML(xml);
		}
		
		public function setDictionary(xml:XML):void {
			Application.application.dictionary = new XMLDictionaryRegistry(xml, dictionaryFallback);
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
			currentMousePoint = canvasBox.globalToLocal(currentMousePoint);
			
			if (event.dragInitiator is ActivityComponent) {
            	var activityComponent:ActivityComponent;
            	activityComponent = event.dragInitiator as ActivityComponent;
            	activityComponent.x = currentMousePoint.x - activityComponent.mouseOffSetX;
          		activityComponent.y = currentMousePoint.y - activityComponent.mouseOffSetY;
          		activityComponent.setCenter();
          		activityComponent.updateTransitionPositions();
            } else if (event.dragInitiator is LearningLibraryEntryComponent) {
            	var learningLibraryComponent:LearningLibraryEntryComponent = event.dragInitiator as LearningLibraryEntryComponent;
            	
            	if (learningLibraryComponent.learningLibraryEntry.isCombined) {
            		var combinedActivityComponent:CombinedActivityComponent = new CombinedActivityComponent();
                	combinedActivityComponent.learningLibraryEntry = learningLibraryComponent.learningLibraryEntry;
                	combinedActivityComponent.load()
					combinedActivityComponent.x = currentMousePoint.x;
              		combinedActivityComponent.y = currentMousePoint.y
                	canvasBox.addChild(combinedActivityComponent);
                	combinedActivityComponent.setCenter();
            	} else {
            		var toolActivityComponent:ToolActivityComponent = new ToolActivityComponent();
                	toolActivityComponent.tool = learningLibraryComponent.learningLibraryEntry.toolTemplates[0];
                	toolActivityComponent.load()
					toolActivityComponent.x = currentMousePoint.x;
              		toolActivityComponent.y = currentMousePoint.y;
                	canvasBox.addChild(toolActivityComponent);
                	toolActivityComponent.setCenter();
            	}
            } else if (event.dragInitiator is SystemActivityComponent) {
            	var systemActivityComponent:SystemActivityComponent = event.dragInitiator as SystemActivityComponent;
            	
            	switch (systemActivityComponent.type) {
            		case Constants.SYTEM_ACTIVITY_TYPE_OPTIONAL:
            			var optionalActivityComponent:OptionalActivityComponent = new OptionalActivityComponent();
	                	optionalActivityComponent.x = currentMousePoint.x;
		              	optionalActivityComponent.y = currentMousePoint.y;
		              	canvasBox.addChild(optionalActivityComponent);
	                	optionalActivityComponent.setCenter();
            			break;
            	}
            }
                
            // Return cursor state to normal
			Application.application.changeCursorState(Constants.CURSOR_STATE_NORMAL);  
		}
		
		
		/**
		 * Handles transitions.
		 * 
		 * This is fired when a transition is finished.. 
		 * ie after the user's mouse button goes up when drawing an activity.
		 * 
		 * First - find the activity (if any) that the transition landed on.
		 * Second - Validate the transition against the learning design
		 * Third - If valid, update the two activities involded and the learning design.
		 * 
		 * @param event The transition event fired by the source activity
		 */ 
		public function drawTransition(event:TransitionEvent):void {
			
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			var validTransition:Boolean = false;
			for (var i:int = 0; i < canvasBox.numChildren; i++) {
		    		
	    		if (canvasBox.getChildAt(i) is ActivityComponent) {
	    			
	    			var activityComponent:ActivityComponent = canvasBox.getChildAt(i) as ActivityComponent;
	    			if (activityComponent.state == Constants.ACTIVITY_STATE_MOUSE_OVER && activityComponent.canAcceptTransitions) {
	    				
	    				
	    				var validationString:String = validateTransition(event, activityComponent);
	    				if (validationString == "valid") {
	    					validTransition = true;	
	    					
	    					// Apply the transition
	    					transitionArray.addItem(event.transition);
	    					activityComponent.transitionIn = event.transition;	
	    					event.transition.toActivity = activityComponent;
	    					event.sourceAcivityComponent.transitionOut = event.transition;
	    					activityComponent.updateTransitionPositions();
							event.sourceAcivityComponent.updateTransitionPositions();
	    				} else {
	    					Alert.show(validationString);
	    				}
	    				
	    				// Return cursor state to normal
	    				Application.application.changeCursorState(Constants.CURSOR_STATE_NORMAL);			
	    				break;
	    			}
	    		}
	    	}
	    	
	    	if (!validTransition) {
	    		event.transition.graphics.clear();
	    		canvasBox.removeChild(event.transition);
	    		event.sourceAcivityComponent.transitionOut = null;
	    	}
		}
		
		/**
		 * 
		 * Validates a transition against the learning design
		 *  
		 * @param event The TransitionEvent fired by the source acitivity
		 * @param destActivity The activity at the end of the transition
		 * @return "valid" if valid, otherwise the translated error message.
		 * 
		 */
		public function validateTransition(event:TransitionEvent, destActivity:ActivityComponent):String {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			// Check if the destination activity already has a transition
			if (destActivity.transitionIn != null) {
				return Application.application.dictionary.getLabelAndInsert("cv_invalid_trans_target_to_activity", [destActivity.title.text]);
			}
			
			// Check if there is a circuit
			if (isLoopingLearningDesign(event.sourceAcivityComponent, destActivity)){
				return Application.application.dictionary.getLabel("cv_invalid_trans_circular_sequence");
			}
			
			return "valid";
		}
		
		
		/**
		 * A recursive function that checks whether the learning design has a circuit in it 
		 * as a result of adding a transition between the two activities
		 *  
		 * @param fromActivity the source of the transition
		 * @param toActivity the destination of the transition
		 * @return true if loop exists, false otherwise
		 * 
		 */
		private function isLoopingLearningDesign(fromActivity:ActivityComponent, toActivity:ActivityComponent):Boolean{
			
			//var toTransitions = _cv.ddm.getTransitionsForActivityUIID(toAct)
			if (toActivity.transitionOut != null){
				var nextActivity:ActivityComponent = toActivity.transitionOut.toActivity;
				if (nextActivity == fromActivity){
					return true;
				}else {
					return isLoopingLearningDesign(fromActivity, nextActivity)
				}
			}else {
				return false;
			}
		}
	}
}