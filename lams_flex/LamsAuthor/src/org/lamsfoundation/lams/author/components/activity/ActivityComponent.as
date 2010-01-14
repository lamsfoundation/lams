package org.lamsfoundation.lams.author.components.activity
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.core.Application;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	
	import org.lamsfoundation.lams.author.components.transition.Transition;
	import org.lamsfoundation.lams.author.events.TransitionEvent;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class ActivityComponent extends VBox
	{
		public var xpos:int;
		public var ypos:int;
		public var title:Label;
		
		public var centerX:int;
		public var centerY:int;
		
		
		private var transitionIn:Transition;
		private var transitionOut:Transition;
		
		// The mouseMove event handler for the Image control
	    // initiates the drag-and-drop operation.
	    public function mouseMoveHandler(event:MouseEvent):void 
	    {                
	        switch (Application.application.cursorState) {
	        	case Constants.CURSOR_STATE_NORMAL:
	        		var activityComponent:ActivityComponent = event.currentTarget as ActivityComponent;
			        var ds:DragSource = new DragSource();
			        ds.addData(activityComponent, "img");               
			        DragManager.doDrag(activityComponent, ds, event);
			        setCenter();
			   		
			   		if (transitionOut != null)
			   		{
			   			transitionOut.startX = centerX;
			   			transitionOut.startY = centerY;
			   			transitionOut.invalidateDisplayList();
			   		} 
			   		
			   		
			   		if (transitionIn != null) {
			   			transitionIn.endX = centerX;
			   			transitionIn.endY = centerY;
			   			transitionIn.invalidateDisplayList();
			   		}
			   		
			        break;
	        }
	    }
	    
	    public function load():void {
	    	
	    }
	    
	    
	    public function mouseDown(event:MouseEvent):void {
	    	
	    	switch (Application.application.cursorState) {
	    		case Constants.CURSOR_STATE_TRANSITION:
	    		var transitionEvent:TransitionEvent = new TransitionEvent(TransitionEvent.TRANSITION_EVENT);
	    		transitionEvent.sourceAcivityComponent = this;
	    		transitionEvent.localX = event.localX;
	    		transitionEvent.localY = event.localY;
	    		//Application.application.dispatchEvent(transitionEvent);
	    		startDragging(transitionEvent)
	    		break;	
	    	}
	    	
	    }
	    
	    public function setCenter():void {
	    	centerX = x + height/2;
			centerY = y + height/2 -10;
	    }
	    
	    
	    
        private function startDragging(event:TransitionEvent):void {
		 
			if (transitionOut != null) {
				parent.removeChild(transitionOut);
			}
			
			setCenter();
			transitionOut = new Transition();
			transitionOut.startX = event.sourceAcivityComponent.centerX;
			transitionOut.startY = event.sourceAcivityComponent.centerY;
			transitionOut.endX = event.sourceAcivityComponent.centerX;
			transitionOut.endY = event.sourceAcivityComponent.centerY;
			transitionOut.invalidateProperties();
			
			parent.addChild(transitionOut);
			
			systemManager.addEventListener(MouseEvent.MOUSE_MOVE, systemManager_mouseMoveHandler, true);
			systemManager.addEventListener(MouseEvent.MOUSE_UP, systemManager_mouseUpHandler, true);
			systemManager.stage.addEventListener(Event.MOUSE_LEAVE, stage_mouseLeaveHandler);
		
		}
		
		protected function stopDragging():void
		{
		    systemManager.removeEventListener(MouseEvent.MOUSE_MOVE, systemManager_mouseMoveHandler, true);			
		    systemManager.removeEventListener(MouseEvent.MOUSE_UP, systemManager_mouseUpHandler, true);
		    systemManager.stage.removeEventListener(Event.MOUSE_LEAVE, stage_mouseLeaveHandler);
		}
		
		private function systemManager_mouseMoveHandler(event:MouseEvent):void
	    {
	    	var pt:Point = new Point(event.stageX,event.stageY);
	    	pt = parent.globalToLocal(pt);
	    	
	    	transitionOut.endX = pt.x;
	    	transitionOut.endY = pt.y;
	    	
	    	transitionOut.invalidateDisplayList();
	    }
	    
	    private function systemManager_mouseUpHandler(event:MouseEvent):void
	    {
	    	stopDragging();
	    	
	    	var validTransition:Boolean = false;
	    	for (var i:int = 0; i < parent.numChildren; i++) {
	    		
	    		if (parent.getChildAt(i) is ActivityComponent) {
	    			var activityComponent:ActivityComponent = parent.getChildAt(i) as ActivityComponent;
	    			
	    			var boundPoint:Point = activityComponent.contentToGlobal(new Point(activityComponent.x,activityComponent.y));
	    			var eventPoint:Point = contentToGlobal(new Point(event.stageX,event.stageY));
	    			
	    			
	    			var maxx:int = boundPoint.x + activityComponent.width;
	    			var maxy:int = boundPoint.y + activityComponent.height;	    			

	    			if (eventPoint.x > boundPoint.x && 
	    					eventPoint.x < maxx &&
	    					eventPoint.y > boundPoint.y &&
	    					eventPoint.y < maxy) {
	    				// We are within the bounds of an activity
	    						
	    				activityComponent.transitionIn = transitionOut;	
	    				validTransition = true;		
	    			}
	    		}
	    	}
	    	
	    	if (!validTransition) {
	    		parent.removeChild(transitionOut);
	    		transitionOut = null;
	    	}
	    	
	    }
	    
	    private function stage_mouseLeaveHandler(event:MouseEvent):void
	    {
	    	stopDragging();
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    

	}
	
	
}