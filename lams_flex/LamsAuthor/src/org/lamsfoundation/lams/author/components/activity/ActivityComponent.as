package org.lamsfoundation.lams.author.components.activity
{
	import flash.events.MouseEvent;
	
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	
	public class ActivityComponent extends VBox
	{
		public var xpos:int;
		public var ypos:int;
		public var title:Label;
		
		public function ActivityComponent() {
			
		}
		
		// The mouseMove event handler for the Image control
	    // initiates the drag-and-drop operation.
	    public function mouseMoveHandler(event:MouseEvent):void 
	    {                
	        var activityComponent:ActivityComponent = event.currentTarget as ActivityComponent;
	        var ds:DragSource = new DragSource();
	        ds.addData(activityComponent, "img");               
	
	        DragManager.doDrag(activityComponent, ds, event);
	 
	    }
	    
	    public function load():void {
	    	
	    }

	}
	
	
}