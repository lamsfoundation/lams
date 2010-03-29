package org.lamsfoundation.lams.author.components.transition
{
	import com.dncompute.graphics.GraphicsUtil;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.managers.DragManager;
	
	import org.lamsfoundation.lams.author.controller.AuthorController;
	import org.lamsfoundation.lams.author.model.transition.Transition;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class TransitionComponent extends UIComponent
	{
		public var transition:Transition;
		public var startX:Number;
		public var startY:Number;
		
		public var endX:Number;
		public var endY:Number;
		
		// This flag is set if when the transition is created, the source activty alread has an out transition
		public var possibleBranch:Boolean = false;
		
		// A variable used in the controller for possible branch transitions
		// Used to generate branch points where needed.
		//public var possibleFirstActivityInBranch:ActivityComponent = null;
		
		

		public function TransitionComponent()
		{
			this.addEventListener(MouseEvent.MOUSE_MOVE, mouseMove);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			
			graphics.clear();
			
			var origin:Point = new Point(startX,startY);
			var destination:Point = new Point(endX,endY);
			
			var lineThickness:Number = 2;
			var lineColor:Number = 0x999999;
			var lineAlpha:Number = 1;
			
			// Find the middle point to draw the arrow
			var middle:Point = Point.interpolate(origin, destination, 0.5);
			var middle1:Point = Point.interpolate(origin, destination, 0.51);
			
			// Draw an arrow to half-way
			graphics.beginFill(lineColor,lineAlpha);
			GraphicsUtil.drawArrow(graphics,origin,middle, {shaftThickness:lineThickness,headWidth:10,headLength:10, shaftPosition:0,edgeControlPosition:0.5});
			
			// Draw the rest of the line
			graphics.lineStyle(2, 0x999999, 1);
			graphics.moveTo(middle1.x,middle1.y);
			graphics.lineTo(endX,endY);
			
			// Setting the z index to lower than the activities
			if (parent != null) {
				parent.setChildIndex(this, 0);
			}
			
		}
		
		// The mouseMove event handler for the Image control
	    // initiates the drag-and-drop operation.
	    private function mouseMove(event:MouseEvent):void 
	    {                
	        if (AuthorController.instance.cursorState == Constants.CURSOR_STATE_NORMAL) {
	        	var ds:DragSource = new DragSource();
	        	ds.addData(this, "dragTransition");    
	        	DragManager.doDrag(this, ds, event, this.getImageForDrag());
	        }
	    }
	    
	    public function getImageForDrag():TransitionComponent{
	    	var ret:TransitionComponent = new TransitionComponent();
	    	ret.endX = this.endX;
	    	ret.endY = this.endY;
	    	ret.startX = this.startX;
	    	ret.startY = this.startY;
	    	ret.invalidateDisplayList();
	    	return ret;
	    }
	}
}