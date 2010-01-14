package org.lamsfoundation.lams.author.components.transition
{
	import flash.events.MouseEvent;
	
	public class DrawingToolManager
	{
		public function DrawingToolManager()
		{
		}
		
		public static function createTool(className:Class, event:MouseEvent):DrawingTool{
			
			/*
			if( className != DrawingTool ){
				throw new Error('className must extend the DrawingTool class, please check axel.cfwebtools.com and search for "Drawing"');
				return;
			}
			*/
			
			var newClass:DrawingTool = new className();
			
			newClass.startX = event.localX;
	    	newClass.startY = event.localY;
	    	newClass.endX = event.localX;
	    	newClass.endY = event.localY;
	    	
	    	newClass.invalidateDisplayList();
	    	
			return newClass;
		}

	}
}