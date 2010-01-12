package org.lamsfoundation.lams.author.components.activity
{
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Image;
	import mx.controls.Label;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	
	public class LearningLibraryEntryComponent extends HBox
	{
		public var _icon:Image;
		public var _title:Label;
		
		
		public function LearningLibraryEntryComponent(iconLoc:String, title:String)
		{
			this.height = 30;
			this.setStyle("verticalAlign", "middle");
			
			// Adding event listener for drag
			this.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			_icon = new Image();
			_icon.source = iconLoc;
			this.addChild(_icon);

			_title = new Label();
			_title.text = title;
			_title.height = 30;
			_title.setStyle("textAlign", "center");
			//_title.setConstraintValue("verticalCenter", 0);
			//_title.setConstraintValue("top", 40);
			this.addChild(_title);
			
			
		}
		
		// The mouseMove event handler for the Image control
        // initiates the drag-and-drop operation.
        private function mouseMoveHandler(event:MouseEvent):void 
        {                
            var dragInitiator:LearningLibraryEntryComponent = event.currentTarget as LearningLibraryEntryComponent;
            
            var dragImage:Image = Image(dragInitiator._icon);
            
			var ds:DragSource = new DragSource();
            ds.addData(dragImage, "img");    
            
            var imageProxy:Image = new Image();
            imageProxy.source = dragImage.source;
            imageProxy.height=30;
            imageProxy.width=30;   
            
                        
			
            DragManager.doDrag(dragInitiator, ds, event, imageProxy, 0, 0, 1.00, false);
        }
	}
}