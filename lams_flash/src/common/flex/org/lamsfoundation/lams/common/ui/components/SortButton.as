package org.lamsfoundation.lams.common.ui.components
{
	import flash.display.*;
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.controls.RadioButton;
	import mx.events.FlexEvent;
	
	public class SortButton extends HBox
	{
		[Embed(source="../../../../../../assets/images/upArrow.swf")]
		[Bindable]
		private var upArrow:Class;
		
		[Embed(source="../../../../../../assets/images/downArrow.swf")]
		[Bindable]
		private var downArrow:Class;
		
		private var button:Button;
		private var buttonLabel:Label;	
		
		public static const NA:String = "not selected";
		public static const ASC:String = "ascending";
		public static const DSC:String = "descending";
		
		[Inspectable]
		private var _sortBy:String;
		
		[Inspectable]
		private var _selected:Boolean;
		
		[Inspectable]
		private var _sortDirection:String;
		
		public function SortButton()
		{
			// call button's constructor
			super();
			
			// initialize sortDirection
			if(_sortDirection == "" || _sortDirection != NA || _sortDirection != ASC || _sortDirection != DSC){
				_sortDirection = NA;
				_selected = false;
			}
			
			// create ui components
			button = new Button();
			button.addEventListener(MouseEvent.CLICK, clickHandler);
			
			buttonLabel = new Label();
			
			// set stles (sortDirection and ui components must be initialized first)
			setStyles();

			// add init event listener
			addEventListener(FlexEvent.INITIALIZE, initializeHandler);
		}
		
		private function setStyles():void{
			this.setStyle("gap", 0);
			
			if(_sortDirection == ASC){
				button.setStyle("icon", upArrow);
			}
			else if(_sortDirection == DSC){
				button.setStyle("icon", downArrow);
			}
			else{
				button.setStyle("icon", null);
			}
		}
		
		public function get sortBy():String{
			return _sortBy;
		}
		
		public function set sortBy(sortBy:String):void{
			_sortBy = sortBy;
		}
		
		public function get selected():Boolean{
			return _selected;
		}
		
		public function set selected(selected:Boolean):void{
			if(selected){
				if(_sortDirection == NA || _sortDirection == DSC){
					_sortDirection = ASC;
					button.setStyle("icon", upArrow);
				}
				else if(sortDirection == ASC){
					_sortDirection = DSC;
					button.setStyle("icon", downArrow);
				}
			}
			else{
				_sortDirection = NA;
				button.setStyle("icon", null);
			}
			
			_selected = selected;
		}
		
		public function get sortDirection():String{
			return _sortDirection;
		}
		
		public function set sortDirection(sortDirection:String):void{
			_sortDirection = sortDirection;
		}
		
		// gets called when the component has been initialized
        private function initializeHandler(event:FlexEvent):void
        {
            // paint the button
            button.width = 17;
            button.height = 17;
            addChild(button);

            // paint the label
            buttonLabel.text = this.label;
            addChild(buttonLabel);
        }
        
        // gets called when the button is clicked
        protected function clickHandler(event:MouseEvent):void
		{
			// dispatch an event and let the sortButtonGroup take care of the rest
			dispatchEvent(new SortEvent(_sortBy, _sortDirection));
		}
	}
}