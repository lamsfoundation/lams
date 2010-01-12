package org.lamsfoundation.lams.common.ui.components
{
	import mx.containers.HBox;
	import mx.events.FlexEvent;
	
	public class SortButtonGroup extends HBox
	{
		// an array of buttons
		private var buttons:Array = [];
		
		// the selected button at the moment
		private var selectedButton:SortButton;
		
		private var _sortBy:String;
		private var _sortDirection:String;
		
		public function SortButtonGroup()
		{
			// call super constructor
			super();
			
			// add init event listener
			addEventListener(FlexEvent.INITIALIZE, initializeHandler);
		}
		
		// gets called when the component has been initialized
        private function initializeHandler(event:FlexEvent):void{
        	// get the buttons in the group
        	buttons = getChildren();
        	
        	// for all buttons, add sortEvent listener and check for a selected button
        	var foundSelected:Boolean = false;
        	for(var i:int = 0; i < buttons.length; i++){
        		var button:SortButton = buttons[i];
				button.addEventListener(SortEvent.EVENT_TYPE, sortEventHandler);
				
				if(button.selected == true){
					if(foundSelected){
						button.selected = false;
					}
					else{
						foundSelected = true;
						selectedButton = button;
						_sortBy = button.sortBy;
						_sortDirection = button.sortDirection;
						dispatchEvent(new SortEvent(button.sortBy, button.sortDirection));
					}
				}
			}
        }
        
		private function sortEventHandler(event:SortEvent):void{
			selectedButton = SortButton(event.target);
			
			for(var i:int = 0; i < buttons.length; i++){
				var button:SortButton = buttons[i];
				if(button != selectedButton){
					button.selected = false;
				}
				else{
					button.selected = true;
					_sortDirection = button.sortDirection;
					_sortBy = button.sortBy;
				}	
			}
			
			dispatchEvent(new SortEvent(_sortBy, _sortDirection));
		}
		
		public function get sortDirection():String{
			return _sortDirection;
		}
		
		public function get sortBy():String{
			return _sortBy;
		}
	}
}