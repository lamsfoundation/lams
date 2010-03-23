package org.lamsfoundation.lams.author.controller
{
	import flash.geom.Point;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.UIComponent;
	import mx.events.CloseEvent;
	import mx.events.DragEvent;
	
	import org.lamsfoundation.lams.author.components.CanvasBox;
	import org.lamsfoundation.lams.author.components.LearningLibraryEntryComponent;
	import org.lamsfoundation.lams.author.components.activity.*;
	import org.lamsfoundation.lams.author.components.activity.group.GroupActivityComponent;
	import org.lamsfoundation.lams.author.components.activity.group.LearnerGroupActivityComponent;
	import org.lamsfoundation.lams.author.components.activity.group.MonitorGroupActivityComponent;
	import org.lamsfoundation.lams.author.components.activity.group.RandomGroupActivityComponent;
	import org.lamsfoundation.lams.author.components.toolbar.SystemToolComponent;
	import org.lamsfoundation.lams.author.components.transition.TransitionComponent;
	import org.lamsfoundation.lams.author.model.learninglibrary.LearningLibraryEntry;
	import org.lamsfoundation.lams.author.util.AuthorUtil;
	import org.lamsfoundation.lams.author.util.Constants;
	import org.lamsfoundation.lams.common.dictionary.XMLDictionaryRegistry;
	
	public class AuthorController
	{
		
		private var dictionaryFallback:XML;
		
		private static var _instance:AuthorController;
		
		
	 	public var transitionArray:ArrayCollection = new ArrayCollection();
	 	
	 	public var activities:Dictionary = new Dictionary();
		
		public function AuthorController(){
			if (_instance != null) {
				throw new Error("This is a singleton, do not instantiate, instead use AuthorController.instance");
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
			//currentMousePoint = canvasBox.globalToLocal(currentMousePoint);	
			
			// This condition is true if the activity was already on the canvas
			if (event.dragInitiator is ActivityComponent) {
            	var activityComponent:ActivityComponent;
            	activityComponent = event.dragInitiator as ActivityComponent;
            	activityComponent.x = currentMousePoint.x - activityComponent.mouseOffSetX;
          		activityComponent.y = currentMousePoint.y - activityComponent.mouseOffSetY;
          		activityComponent.setCenter();
          		activityComponent.updateTransitionPositions();
          		activityComponent.selectActivity();
            } else {
            	// Adding a new activity to the canvas
            	
            	// Get the next UIID
				var nextActivityUIID:int = getNewUIID();
            	
            	if (event.dragInitiator is LearningLibraryEntryComponent) {
	            	var learningLibraryComponent:LearningLibraryEntryComponent = event.dragInitiator as LearningLibraryEntryComponent;
	            	
	            	// Get the current toolContentID then increment the global counter
					var toolContentID:int = Application.application.nextToolContentID;
					Application.application.nextToolContentID++;
	            	
	            	if (learningLibraryComponent.learningLibraryEntry.isCombined) {
	            		
	            		var act1UIID:int = getNewUIID();
	            		var act2UIID:int = getNewUIID();
	            		var combinedActivityComponent:CombinedActivityComponent = new CombinedActivityComponent();
	                	combinedActivityComponent.toolContentID = toolContentID;
	                	combinedActivityComponent.learningLibraryEntry = learningLibraryComponent.learningLibraryEntry;
	                	combinedActivityComponent.loadCombined(nextActivityUIID, act1UIID, act2UIID)
						combinedActivityComponent.x = currentMousePoint.x;
	              		combinedActivityComponent.y = currentMousePoint.y
	                	canvasBox.addChild(combinedActivityComponent);
	                	combinedActivityComponent.setCenter();
	                	combinedActivityComponent.selectActivity();
	                	activities[nextActivityUIID] = combinedActivityComponent;
	            	} else {
	            		var toolActivityComponent:ToolActivityComponent = new ToolActivityComponent();
	                	toolActivityComponent.toolContentID = toolContentID;
	                	toolActivityComponent.tool = learningLibraryComponent.learningLibraryEntry.toolTemplates[0];
	                	toolActivityComponent.load(nextActivityUIID)
						toolActivityComponent.x = currentMousePoint.x  - toolActivityComponent.mouseOffSetX;
	              		toolActivityComponent.y = currentMousePoint.y  - toolActivityComponent.mouseOffSetY;
	                	canvasBox.addChild(toolActivityComponent);
	                	toolActivityComponent.setCenter();
	                	toolActivityComponent.selectActivity();
	                	activities[nextActivityUIID] = toolActivityComponent;
	            	}
	            } else if (event.dragInitiator is SystemToolComponent) {
	            	var systemToolComponent:SystemToolComponent = event.dragInitiator as SystemToolComponent;
	            	
	            	switch (systemToolComponent.type) {
	            		case Constants.SYSTEM_ACTIVITY_TYPE_OPTIONAL:
	            			var optionalActivityComponent:OptionalActivityComponent = new OptionalActivityComponent();       			
		                	optionalActivityComponent.x = currentMousePoint.x;
			              	optionalActivityComponent.y = currentMousePoint.y;
			              	canvasBox.addChild(optionalActivityComponent);
		                	optionalActivityComponent.setCenter();
		                	optionalActivityComponent.load(nextActivityUIID);
		                	optionalActivityComponent.selectActivity();
		                	activities[nextActivityUIID] = optionalActivityComponent;
	            			break;
	            		case Constants.SYSTEM_ACTIVITY_TYPE_GROUP_RANDOM:
	            			var randomGroup:RandomGroupActivityComponent = new RandomGroupActivityComponent();
		                	randomGroup.x = currentMousePoint.x;
			              	randomGroup.y = currentMousePoint.y;
			              	canvasBox.addChild(randomGroup);
		                	randomGroup.setCenter();
		                	randomGroup.load(nextActivityUIID);
		                	randomGroup.selectActivity();
		                	activities[nextActivityUIID] = randomGroup;
	            			break;
	            		case Constants.SYSTEM_ACTIVITY_TYPE_GROUP_MONITOR:
	            			var monitorGroup:MonitorGroupActivityComponent = new MonitorGroupActivityComponent();
		                	monitorGroup.x = currentMousePoint.x;
			              	monitorGroup.y = currentMousePoint.y;
			              	canvasBox.addChild(monitorGroup);
		                	monitorGroup.setCenter();
		                	monitorGroup.load(nextActivityUIID);
		                	monitorGroup.selectActivity();
		                	activities[nextActivityUIID] = monitorGroup;
	            			break;
	            		case Constants.SYSTEM_ACTIVITY_TYPE_GROUP_LEARNER:
	            			var learnerGroup:GroupActivityComponent = new LearnerGroupActivityComponent();
		                	learnerGroup.x = currentMousePoint.x;
			              	learnerGroup.y = currentMousePoint.y;
			              	canvasBox.addChild(learnerGroup);
		                	learnerGroup.setCenter();
		                	learnerGroup.load(nextActivityUIID);
		                	learnerGroup.selectActivity();
		                	activities[nextActivityUIID] = learnerGroup;
	            			break;
	            		case Constants.SYSTEM_ACTIVITY_TYPE_BRANCH:
	            			var branchPoint:BranchPoint = new BranchPoint;
	            			branchPoint.x = currentMousePoint.x;
			              	branchPoint.y = currentMousePoint.y;
			              	canvasBox.addChild(branchPoint);
		                	branchPoint.setCenter();
		                	branchPoint.load(nextActivityUIID);
		                	branchPoint.selectActivity();
		                	activities[nextActivityUIID] = branchPoint;
	            			break;
	            	}
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
		public function drawTransition(sourceActivityComponent:ActivityComponent, transition:TransitionComponent):void {
			
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			var validTransition:Boolean = false;
			for (var i:int = 0; i < canvasBox.numChildren; i++) {
		    		
	    		if (canvasBox.getChildAt(i) is ActivityComponent) {
	    			
	    			var destActivityComponent:ActivityComponent = canvasBox.getChildAt(i) as ActivityComponent;
	    			if (destActivityComponent.state == Constants.ACTIVITY_STATE_MOUSE_OVER && destActivityComponent.canAcceptTransitions) {
	    				
	    				
	    				var validationString:String = validateTransition(transition, sourceActivityComponent, destActivityComponent);
	    				
	    				switch (validationString) {
	    					case "valid": 
		    					validTransition = true;	
		    					
		    					// Apply the transition
		    					transitionArray.addItem(transition);
		    					sourceActivityComponent.state = Constants.ACTIVITY_STATE_NORMAL;
		    					destActivityComponent.transitionIn = transition;	
		    					transition.toActivity = destActivityComponent;
		    					sourceActivityComponent.transitionOut = transition;
		    					destActivityComponent.updateTransitionPositions();
								sourceActivityComponent.updateTransitionPositions();
								break;
							case "branchAttempt":
								validTransition = true;	
								Alert.show("Looks like your trying to create a branch. Do you want to generate a branch point?","",
									Alert.OK|Alert.CANCEL,Application.application.canvasArea,
									function clickListener(event:CloseEvent):void {
										if (event.detail == Alert.OK) {
											automaticallyGenerateBranch(transition, sourceActivityComponent, destActivityComponent);
										} else {
											Alert.show("no");
										}
									}
									
									,null,Alert.OK);
								break;
							default:
								Alert.show(validationString);
								break;
	    				}

	    				// Return cursor state to normal
	    				Application.application.changeCursorState(Constants.CURSOR_STATE_NORMAL);			
	    				break;
	    			}
	    		}
	    	}
	    	
	    	if (!validTransition) {
	    		transition.graphics.clear();
	    		canvasBox.removeChild(transition);
	    		sourceActivityComponent.transitionOut = null;
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
		public function validateTransition(transition:TransitionComponent, sourceActivity:ActivityComponent, destActivity:ActivityComponent):String {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			// Check if there is more than one transition coming our of the source activity
			if (transition.possibleBranch) {
				return "branchAttempt";
			}
			
			// Check if the destination activity already has a transition
			if (destActivity.transitionIn != null) {
				return Application.application.dictionary.getLabelAndInsert("cv_invalid_trans_target_to_activity", [destActivity.title.text]);
			}
			
			// Check if there is a circuit
			if (isLoopingLearningDesign(sourceActivity, destActivity)){
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
		
		private function automaticallyGenerateBranch(transition:TransitionComponent, sourceActivity:ActivityComponent, destActivity:ActivityComponent):void {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			var destActivity2:ActivityComponent = sourceActivity.transitionOut.possibleFirstActivityInBranch;
			
			// Remove old transitions
        	deleteTransition(transition.possibleFirstActivityInBranch.transitionIn);
        	deleteTransition(transition);
			
			var sourcePoint:Point = new Point(sourceActivity.centerX, sourceActivity.centerY);
			
			// Find midpoint between two dest activities
			var midPoint:Point = AuthorUtil.getMidpoint(new Point(destActivity.x, destActivity.y),new Point(destActivity2.x, destActivity2.y));
			
			// Find point 30 pixels away from the source activity in the direction of the midpoint
			// This is the point of the branch point
			var resultPoint:Point = Point.interpolate(sourcePoint, midPoint, 0.5);
			
			var nextActivityUIID:int = getNewUIID();
			
			// Add the branch point to the canvas
			var branchPoint:BranchPoint = new BranchPoint();
        	canvasBox.addChild(branchPoint);
        	branchPoint.x = resultPoint.x;
          	branchPoint.y = resultPoint.y;
          	
        	branchPoint.load(nextActivityUIID);
        	branchPoint.selectActivity();
        	activities[nextActivityUIID] = branchPoint;	
        	
        	// Create the transition to the branch activity
        	var transition:TransitionComponent = new TransitionComponent();
        	canvasBox.addChild(transition);
        	transition.startX = sourceActivity.centerX;
        	transition.startY = sourceActivity.centerY;
        	transition.endX = branchPoint.centerX;
        	transition.endY = branchPoint.centerY;
        	transition.toActivity = branchPoint;
        	transition.fromActivity = sourceActivity;
        	
        	// link with activities
        	sourceActivity.transitionOut = transition;
        	branchPoint.transitionIn = transition;
        	branchPoint.updateTransitionPositions();
        	sourceActivity.updateTransitionPositions();
        	
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
			if (activityComponent.transitionIn != null) {
				canvasBox.removeChild(activityComponent.transitionIn);
				transitionArray.removeItemAt(transitionArray.getItemIndex(activityComponent.transitionIn));
				activityComponent.transitionIn.fromActivity.transitionOut = null;
				activityComponent.transitionIn = null;				
			}
			
			// Remove transition out if needed
			if (activityComponent.transitionOut != null) {
				canvasBox.removeChild(activityComponent.transitionOut);
				transitionArray.removeItemAt(transitionArray.getItemIndex(activityComponent.transitionOut));
				activityComponent.transitionOut.toActivity.transitionIn = null;
				activityComponent.transitionOut = null;				
			}
			
			// Remove activity
			canvasBox.removeChild(activityComponent);
			activityComponent = null;
		}
		
		/**
		 * This function is invoked when a transition is dropped into the bin
		 * it deletes the transition
		 * 
		 * TODO: Check the state of the LD and set it to invalid if neccessary
		 * 
		 * @param transition
		 */
		public function deleteTransition(transition:TransitionComponent):void {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			if (transition != null) {
				if (transitionArray.getItemIndex(transition) != -1) {
					transitionArray.removeItemAt(transitionArray.getItemIndex(transition));
				}
				if (transition.fromActivity != null) {
					transition.fromActivity.transitionOut = null;
				} 
				if (transition.toActivity != null) {
					transition.toActivity.transitionIn = null;
				}
				
				canvasBox.removeChild(transition);
				transition = null;
			}
		}
		
		private function getNewUIID():int{
			var UIID:int = Application.application.nextUIID;
			Application.application.nextUIID++;
			return UIID;
		}
	}
}