package org.lamsfoundation.lams.author.controller
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.utils.Dictionary;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.events.CloseEvent;
	
	import org.lamsfoundation.lams.author.components.CanvasBox;
	import org.lamsfoundation.lams.author.components.activity.ActivityComponent;
	import org.lamsfoundation.lams.author.components.transition.TransitionComponent;
	import org.lamsfoundation.lams.author.model.activity.Activity;
	import org.lamsfoundation.lams.author.model.transition.Transition;
	import org.lamsfoundation.lams.author.model.transition.TransitionFactory;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class TransitionController
	{
		private static var _instance:TransitionController;
		
		private var currentTransitionComponent:TransitionComponent;
		private var currentFromActivityActivityComponent:ActivityComponent;
		
		// Reference to all transition components via uiid
		[Bindable] public var transitionComponents:Dictionary = new Dictionary();
		
		public function TransitionController() {
			if (_instance != null) {
				throw new Error("This is a singleton, do not instanciate, instead use TransitionController.instance");
			}
		}
		
		public static function get instance():TransitionController {
			if (_instance == null) {
				_instance = new TransitionController();
			}
			return _instance;
		}
			
		public function startTransition(event:MouseEvent, activityComponent:ActivityComponent):void {
			// Set the activityComponent state to drawing
			activityComponent.state = Constants.ACTIVITY_STATE_DRAWING;
			
			// Set the global state to drawing
			AuthorController.instance.uiState = Constants.UI_STATE_DRAWING;
			
			var possibleBranch:Boolean = activityComponent.rootActivity.transitionFrom != null;
			var possibleFirstActivityInBranch:Activity = null;
			if (possibleBranch) {
				possibleFirstActivityInBranch = activityComponent.rootActivity.transitionFrom.toActivity;
			}
			
			this.currentFromActivityActivityComponent = activityComponent;

			// Create a transition component
			currentTransitionComponent = new TransitionComponent();
			currentTransitionComponent.startX = activityComponent.centerX;
			currentTransitionComponent.startY = activityComponent.centerY;
			currentTransitionComponent.endX = activityComponent.centerX;
			currentTransitionComponent.endY = activityComponent.centerY;
			currentTransitionComponent.invalidateProperties()
			
			// Deselect the currently selected activity
			AuthorController.instance.deSelectActivityComponent();
			
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			canvasBox.addChild(currentTransitionComponent);
			
			Application.application.systemManager.addEventListener(MouseEvent.MOUSE_MOVE, systemManagerMouseMoveHandler, true);
			Application.application.systemManager.addEventListener(MouseEvent.MOUSE_UP, systemManagerMouseUpHandler, true);
			Application.application.systemManager.stage.addEventListener(Event.MOUSE_LEAVE, stageMouseLeaveHandler);
		}
		
		protected function stopTransition():void
		{
		    AuthorController.instance.uiState = Constants.UI_STATE_NORMAL;
		    Application.application.systemManager.removeEventListener(MouseEvent.MOUSE_MOVE, systemManagerMouseMoveHandler, true);			
		    Application.application.systemManager.removeEventListener(MouseEvent.MOUSE_UP, systemManagerMouseUpHandler, true);
		    Application.application.systemManager.stage.removeEventListener(Event.MOUSE_LEAVE, stageMouseLeaveHandler);
		}
		
		private function systemManagerMouseMoveHandler(event:MouseEvent):void
	    {
	    	var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
	    	
	    	var pt:Point = new Point(event.stageX,event.stageY);
	    	pt = canvasBox.globalToLocal(pt);
	    	
	    	this.currentTransitionComponent.endX = pt.x;
	    	this.currentTransitionComponent.endY = pt.y;
	    	
	    	currentTransitionComponent.invalidateDisplayList();
	    }
	    
	    /**
	    * This event fires at the end of a transition.
	    * 
	    * Needs to be handled by the controller as it changes the learningDesing state
	    * 
	    */ 
	    private function systemManagerMouseUpHandler(event:MouseEvent):void
	    {
    		stopTransition();
    		finishTransition(this.currentFromActivityActivityComponent, this.currentTransitionComponent);
	    }
	    
	    private function stageMouseLeaveHandler(event:MouseEvent):void
	    {
	    	stopTransition();
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
		public function finishTransition(sourceActivityComponent:ActivityComponent, transitionComponent:TransitionComponent):void {
			
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			var validTransition:Boolean = false;
			for (var i:int = 0; i < canvasBox.numChildren; i++) {
		    		
	    		if (canvasBox.getChildAt(i) is ActivityComponent) {
	    			
	    			var destActivityComponent:ActivityComponent = canvasBox.getChildAt(i) as ActivityComponent;
	    			if (destActivityComponent.state == Constants.ACTIVITY_STATE_MOUSE_OVER && destActivityComponent.canAcceptTransitions) {

	    				var validationString:String = validateTransition(transitionComponent.transition, sourceActivityComponent.rootActivity, destActivityComponent.rootActivity);
	    				
	    				switch (validationString) {
	    					case "valid": 
		    					validTransition = true;	
		    					
		    					var nextUIID:int = AuthorController.instance.generateUIID();
		    					
		    					var transition:Transition = TransitionFactory.createTransition(this.currentFromActivityActivityComponent.rootActivity, 
		    						destActivityComponent.rootActivity, Constants.TRANSITION_TYPE_NORMAL, nextUIID);
		    					
		    					this.currentTransitionComponent.transition = transition;
		    					
		    					
		    					currentFromActivityActivityComponent.state = Constants.ACTIVITY_STATE_NORMAL;
		    					currentFromActivityActivityComponent.transitionFromComponent = currentTransitionComponent;
		    					destActivityComponent.transitionToComponent = currentTransitionComponent;
		    					

		    					destActivityComponent.updateTransitionPositions();
								sourceActivityComponent.updateTransitionPositions();
								break;
							case "branchAttempt":
								validTransition = true;	
								Alert.show("Looks like your trying to create a branch. Do you want to generate a branch point?","",
									Alert.OK|Alert.CANCEL,Application.application.canvasArea,
									function clickListener(event:CloseEvent):void {
										if (event.detail == Alert.OK) {
											//automaticallyGenerateBranch(transition, sourceActivityComponent, destActivityComponent);
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
	    				AuthorController.instance.changeCursorState(Constants.CURSOR_STATE_NORMAL);			
	    				break;
	    			}
	    		}
	    	}
	    	
	    	if (!validTransition) {
	    		this.currentTransitionComponent.graphics.clear();
	    		canvasBox.removeChild(currentTransitionComponent);
	    		this.currentFromActivityActivityComponent.transitionFromComponent = null;
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
		public function validateTransition(transition:Transition, sourceActivity:Activity, destActivity:Activity):String {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			// Check if there is more than one transition coming our of the source activity
			//if (transition.possibleBranch) {
			//	return "branchAttempt";
			//}
			
			// Check if the destination activity already has a transition
			if (destActivity.transitionTo != null) {
				return Application.application.dictionary.getLabelAndInsert("cv_invalid_trans_target_to_activity", [destActivity.title]);
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
		private function isLoopingLearningDesign(fromActivity:Activity, toActivity:Activity):Boolean{
			
			//var toTransitions = _cv.ddm.getTransitionsForActivityUIID(toAct)
			if (toActivity.transitionFrom != null){
				var nextActivity:Activity = toActivity.transitionFrom.toActivity;
				if (nextActivity == fromActivity){
					return true;
				}else {
					return isLoopingLearningDesign(fromActivity, nextActivity)
				}
			} else {
				return false;
			}

		}
		
		private function automaticallyGenerateBranch(transition:TransitionComponent, sourceActivity:ActivityComponent, destActivity:ActivityComponent):void {
			/* var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
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
        	sourceActivity.updateTransitionPositions(); */
        	
		}
		
		 /**
		 * This function is invoked when a transition is dropped into the bin
		 * it deletes the transition
		 * 
		 * TODO: Check the state of the LD and set it to invalid if neccessary
		 * 
		 * @param transition
		 */
		public function deleteTransition(transitionComponent:TransitionComponent):void {
			var canvasBox:CanvasBox = Application.application.canvasArea.canvasBox;
			
			 if (transitionComponent != null) {
				
				this.transitionComponents[transitionComponent.transition.transitionUUID] = null;
				
				if (transitionComponent.transition.fromActivity != null) {
					// Need to remove transition from model and from view
					var fromActivityComponent:ActivityComponent = AuthorController.instance.activityComponents[transitionComponent.transition.fromActivity.activityUIID] as ActivityComponent;
					fromActivityComponent.transitionFromComponent = null;	
					transitionComponent.transition.fromActivity.transitionFrom = null;
					
				} 
				if (transitionComponent.transition.toActivity != null) {
					var toActivityComponent:ActivityComponent = AuthorController.instance.activityComponents[transitionComponent.transition.toActivity.activityUIID] as ActivityComponent;
					toActivityComponent.transitionToComponent = null;	
					transitionComponent.transition.toActivity.transitionTo = null;
				}
				
				canvasBox.removeChild(transitionComponent);
				transitionComponent = null;
			}
		}
			    

	}
}