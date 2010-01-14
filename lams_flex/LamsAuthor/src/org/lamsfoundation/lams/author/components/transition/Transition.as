package org.lamsfoundation.lams.author.components.transition
{
	public class Transition extends DrawingTool
	{
		public function Transition()
		{
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			
			graphics.clear();
			graphics.lineStyle(3, 0x999999, 1);
			//graphics.beginFill(0x999999);
			graphics.moveTo(startX,startY);
			graphics.lineTo(endX,endY);
			//graphics.lineTo(endX-10, endY+5);
        	//graphics.lineTo(endX+10, endY+5);
        	//graphics.lineTo(endX, endY-20);

			parent.setChildIndex(this, 0);

		}

	}
}