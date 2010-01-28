package org.lamsfoundation.lams.author.components.transition
{
	import com.dncompute.graphics.GraphicsUtil;
	
	import flash.geom.Point;
	
	import org.lamsfoundation.lams.author.components.activity.ActivityComponent;
	
	public class TransitionComponent extends DrawingTool
	{
		public var toActivity:ActivityComponent;
		public var fromActivity:ActivityComponent;

		public function TransitionComponent()
		{
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

	}
}