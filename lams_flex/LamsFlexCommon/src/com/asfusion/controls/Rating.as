/* Copyright 2007 Nahuel Foronda (AsFusion)

Licensed under the Apache License, Version 2.0 (the "License"); 

you may not use this file except in compliance with the License. 
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 WARRANTIES OR CONDITIONS OF ANY KIND,  either express or implied. See the License 
 for the specific language governing permissions and limitations under the License.

*/
package com.asfusion.controls
{
	import com.asfusion.controls.ratingclasses.*;
	
	import flash.display.*;
	import flash.events.MouseEvent;
	
	import mx.core.EdgeMetrics;
	import mx.core.IFlexDisplayObject;
	import mx.core.UIComponent;
	import mx.managers.ISystemManager;
	
	/**
	 * Dispatched when the user changes the selected value by clicking on an item or when 
	 * the selectedValue property is programatically changed. 
	 */
	[Event(name="selectionChange", type="com.asfusion.controls.ratingclasses.RatingEvent")]
	
	/**
	 *  Gap between items
	 * 
	 *  @default 5
	 */
	[Style(name="horizontalGap", type="Number", format="Length", inherit="no")]
	/**
	 *  Number of pixels between the container's bottom border
	 *  and the bottom of its content area.
	 *  
	 *  @default 0 
	 */
	[Style(name="paddingBottom", type="Number", format="Length", inherit="no")]
	
	/**
	 *  Number of pixels between the container's top border
	 *  and the top of its content area.
	 *  
	 *  @default 0
	 */
	[Style(name="paddingTop", type="Number", format="Length", inherit="no")]
	
	/**
	 *  Number of pixels between the container's left border
	 *  and the left of its content area.
	 *  
	 *  @default 0 
	 */
	[Style(name="paddingLeft", type="Number", format="Length", inherit="no")]
	
	/**
	 *  Number of pixels between the container's right border
	 *  and the right of its content area.
	 *  
	 *  @default 0
	 */
	[Style(name="paddingRight", type="Number", format="Length", inherit="no")]
	/**
	 *  Name of the class to use as the skin for the items when
	 *  showing a user-selected value
	 *  @default 
	 */
	[Style(name="selectedSkin", type="Class", inherit="no")]
	
	/**
	 * Name of the class to use as the skin for the items when
	 * showing a value. 
	 */
	[Style(name="upSkin", type="Class", inherit="no")]
	
	/**
	 * Name of the class to use as the skin for the items
	 * when not showing a value
	 */
	[Style(name="unselectedSkin", type="Class", inherit="no")]
	
	/**
	 * Name of the class to use as the skin for the items
	 * when user rolls over them
	 */
	[Style(name="overSkin", type="Class", inherit="no")]
	
	
	[Mixin]
	
	/**
	 * Control that shows a certain number of items in a row to represent a "rating".
	 * If user selects an item, the "selectedValue" property will change and a RatingEvent will
	 * be dispatched.  
	 */
	public class Rating extends UIComponent
	{
		/*------------------------------------------------------------------------------------------------
		*                                        Protected properties
		-------------------------------------------------------------------------------------------------*/	
		/** @private */
		protected var selectedSkin:Class;
		/** @private */
		protected var unselectedSkin:Class;
		/** @private */
		protected var upSkin:Class;
		/** @private */
		protected var disabledSkin:Class;
		/** @private */
		protected var overSkin:Class;
		/** @private */
		protected var horizontalGap:Number;
		/** @private */
		protected var paddingBottom:Number;
		/** @private */
		protected var paddingTop:Number;
		/** @private */
		protected var paddingLeft:Number;
		/** @private */
		protected var paddingRight:Number;
		/** @private */
		protected var unselectedLayer:UIComponent;
		/** @private */
		protected var selectedLayer:UIComponent;
		/** @private */
		protected var upLayer:UIComponent;
		/** @private */
		protected var disabledLayer:UIComponent;
		/** @private */
		protected var overLayer:UIComponent;
		/** @private */
		protected var selectedMaskLayer:Shape;
		/** @private */
		protected var glassLayer:Shape;
		/** @private */
		protected var needLayout:Boolean;
		/** @private */
		protected var valueChanged:Boolean;
		/** @private */
		protected var selectedValueChanged:Boolean;
		/** @private */
		protected var itemCountChanged:Boolean;
		/** @private */
		protected var itemCreationComplete:Boolean;
		/** @private */
		protected var skinChanged:Boolean;
		/** @private */
		protected var itemWidth:int;
		/** @private */
		protected var itemHeight:int;
		/** @private */
		protected var metrics:EdgeMetrics;
		/** @private */
		protected var rollOverValue:uint;
		
		
		private static var className:String = "Rating"; 
		
		/*------------------------------------------------------------------------------------------------
		*                                        Constructor
		-------------------------------------------------------------------------------------------------*/	
		public function Rating()
		{
			super();
			addEventListener(MouseEvent.MOUSE_OUT,handleItemRollOut);
            addEventListener(MouseEvent.ROLL_OUT, handleRollOut);     
            addEventListener(MouseEvent.MOUSE_OVER, handleItemRollOver);
            addEventListener(MouseEvent.CLICK, handleClick);
		}
		
		/*------------------------------------------------------------------------------------------------
		*                                        Static Functions
		-------------------------------------------------------------------------------------------------*/	
		
		/*-.........................................init..................................................*/
		/**
		 * @private
		 */
		public static function init(systemManager:ISystemManager) : void 
		{
	         RatingStyle.initDefaultStyles(className);
		}
		
		/*------------------------------------------------------------------------------------------------
		*                                    Public Setters and Getters
		-------------------------------------------------------------------------------------------------*/	
		
		/*-.........................................value................................................*/			
		private var _value:Number;
		
		/**
		 * 	Value of the rating. It is the value shown unless showSelectedValue is set to true and user
		 * has changed the selected value. 
		 */
		[Bindable] 
		public function set value(value:Number):void
		{
			valueChanged = (_value != value);
			_value = value;
			invalidateDisplayList();
		}
		
		public function get value():Number
		{
			return _value;
		}

		/*-......................................votedValue............................................*/			
		private var _votedValue:Number;
		
		[Bindable] 
		public function set votedValue(value:Number):void
		{
			_votedValue = value;
		}
		
		public function get votedValue():Number
		{
			return _votedValue;
		}
		
		/*-......................................voted.............................................*/			
		private var _voted:Boolean;
		
		[Bindable] 
		public function set voted(value:Boolean):void
		{
			_voted = value;
		}
		
		public function get voted():Boolean
		{
			return _voted;
		}
		
		/*-.........................................selectedValue................................................*/			
		private var _selectedValue:uint;
		
		/**
		 * The value selected by the user by clicking on an item.
		 */
		[Bindable (event="selectionChange")] 
		public function set selectedValue(value:uint):void
		{
			selectedValueChanged = (_selectedValue != value);
			
			if(selectedValueChanged)
			{
				_selectedValue = value;
				invalidateDisplayList();
				var event: RatingEvent = new RatingEvent(RatingEvent.SELECTION_CHANGE);
				event.selectedValue = value;
				dispatchEvent(event);
			}
		}
		
		public function get selectedValue():uint
		{
			return _selectedValue;
		}
		
		
		/*-.........................................itemCount................................................*/					
		private var _itemCount:uint = 5;
		
		/**
		 * 	Number of items(stars) in the rating component
		 */
		[Bindable] 
		public function set itemCount(value:uint):void
		{
			itemCountChanged = (_itemCount != value);
			_itemCount = value;
			invalidateProperties();
		}
		
		public function get itemCount():uint
		{
			return _itemCount;
		}
		
		/*-.........................................liveRollOver................................................*/			
		private var _liveRollOver:Boolean = true;
		
		/**
		 * 	Whether the component shows rollOver
		 */
		[Bindable]
		public function set liveRollOver(value:Boolean):void
		{
			 _liveRollOver = value;
		}
		public function get liveRollOver():Boolean
		{
			return  _liveRollOver
		}
		
		/*-.........................................showSelectedValue................................................*/		
		private var _showSelectedValue:Boolean;
		
		/**
		 * 	Whether the component shows the selected value or not. If this property is true, the compoent will show
		 *  the selectedValue instead of showing the value.
		 */	
		[Bindable] 
		public function set showSelectedValue(value:Boolean):void
		{
			 _showSelectedValue = value;
		}
		public function get showSelectedValue():Boolean
		{
			return  _showSelectedValue
		}

		
		/*-.........................................visibleLayer................................................*/	
		private var _visibleLayer:UIComponent;
		/**
		 * @private
		 */
		protected function set visibleLayer(value:UIComponent):void
		{
			if(_visibleLayer && _visibleLayer != value) _visibleLayer.visible = false;
			 _visibleLayer = value;
			 _visibleLayer.visible = true;
		}
		/**
		 * @private
		 */
		protected function get visibleLayer():UIComponent
		{
			return  _visibleLayer;
		}
		
		/*------------------------------------------------------------------------------------------------
		*                                          Override Methods
		-------------------------------------------------------------------------------------------------*/	
		
		/**
		 * @private
		 */
		/*-.........................................createChildren.....................................*/
		override protected function createChildren():void
		{
			addChild(createGlassLayer());
			if(itemCount) createItems();
		}
		
		/**
		 * @private
		 */
		/*-.........................................commitProperties.....................................*/	
		override protected function commitProperties():void
		{
			super.commitProperties();
			if(itemCreationComplete && itemCountChanged || skinChanged)
			{
				removeAll();
				createItems();
				invalidateDisplayList();
				itemCountChanged = false;
				skinChanged = false;
			}
		}
		
		/**
		 * @private
		 */
		/*-.........................................updateDisplayList.....................................*/	
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			layoutItems();
			updateValue();
			updateSelectedValue();
			
			glassLayer.width = unscaledWidth;
			glassLayer.height = unscaledHeight;
		}
		
		/**
		 * @private
		 */
		/*-.........................................measure.....................................*/		
		override protected function measure():void
		{
			if(!itemCreationComplete)
			{
				measuredMinWidth = measuredWidth = 0;
				measuredMinHeight = measuredHeight = 0;
			}
			else
			{
				measuredHeight = metrics.bottom + metrics.top + itemHeight;
				measuredWidth  = metrics.left + metrics.right + (itemCount * itemWidth) + ((itemCount -1) * horizontalGap);
				
				measuredMinWidth = measuredWidth;
				measuredMinHeight = measuredHeight;
			}
		}
		
		/**
		 * @private
		 */
		/*-.........................................stylesInitialized.....................................*/		
		override public function stylesInitialized():void
		{
			readStyleValues();
		}
		
		/**
		 * @private
		 */
		/*-.........................................styleChanged.....................................*/
		override public function styleChanged(styleProp:String):void
		{
			
			super.styleChanged(styleProp);
			
			if(	styleProp == 'selectedSkin' || 
				styleProp == 'unselectedSkin' ||
				styleProp == 'upSkin'||
				styleProp == 'overSkin'||
				styleProp == 'disabledSkin')
			{
				this[styleProp] = getStyle(styleProp);
				skinChanged = true;
				invalidateProperties();
			}
			
			else if(styleProp == 'horizontalGap'||
					styleProp == 'paddingBottom'||
					styleProp == 'paddingTop' ||
					styleProp == 'paddingRight' ||
					styleProp == 'paddingLeft' )
			{
				this[styleProp] = getStyle(styleProp);
			}

			
			
			else if(styleProp == "styleName" && itemCreationComplete)
			{
				readStyleValues();
				skinChanged = true;
				invalidateProperties();
			}
			needLayout = true;
			invalidateDisplayList();
		}
		/*------------------------------------------------------------------------------------------------
		*                                          Protected Methods
		-------------------------------------------------------------------------------------------------*/	
		/**
		 * @private
		 */
		/*-.........................................readStyleValues.....................................*/
		protected function readStyleValues():void
		{
			selectedSkin 	= getStyle("selectedSkin");
			unselectedSkin 	= getStyle("unselectedSkin");
			upSkin 			= getStyle("upSkin");
			overSkin 		= getStyle("overSkin");		
			disabledSkin 	= getStyle("disabledSkin");
			
			horizontalGap 	= getStyle("horizontalGap");
			paddingBottom 	= getStyle("paddingBottom");
			paddingTop 		= getStyle("paddingTop");
			paddingRight 	= getStyle("paddingRight");
			paddingLeft 	= getStyle("paddingLeft");
			
			metrics = new EdgeMetrics(paddingLeft, paddingTop, paddingRight, paddingBottom);
		}
		
		/**
		 * @private
		 */
		/*-.........................................createItems.....................................*/
		protected function createItems():void
		{
			unselectedLayer	= new UIComponent();
			selectedLayer	= new UIComponent();
			overLayer		= new UIComponent();
			upLayer			= new UIComponent();
			
			for( var i:uint; i < itemCount; i++)
			{
				var unselectedItem:IFlexDisplayObject = new unselectedSkin();
				unselectedLayer.addChild(DisplayObject(unselectedItem));
				
				var selectedItem:IFlexDisplayObject = new selectedSkin();
				selectedLayer.addChild(DisplayObject(selectedItem));
				
				var upItem:IFlexDisplayObject = new upSkin();
				upLayer.addChild(DisplayObject(upItem));
				
				var overItem:IFlexDisplayObject = new overSkin();
				overLayer.addChild(DisplayObject(overItem));
				
				itemWidth	= Math.max(unselectedItem.measuredWidth,  selectedItem.measuredWidth,  overItem.measuredWidth,  upItem.measuredWidth);
				itemHeight	= Math.max(unselectedItem.measuredHeight, selectedItem.measuredHeight, overItem.measuredHeight, upItem.measuredHeight);
			}
			
			addChild(unselectedLayer);
			addChild(selectedLayer);
			addChild(upLayer);
			addChild(overLayer);
			addChild(createSelectedMask());
			
			unselectedLayer.visible = false;
			selectedLayer.visible 	= false;
			upLayer.visible 		= false;
			overLayer.visible 		= false;
			
			itemCreationComplete = true;
			needLayout = true;
		}
		
		/**
		 * @private
		 */
		/*-.........................................createSelectedMask.....................................*/
		protected function createSelectedMask():DisplayObject
		{
			if(! selectedMaskLayer)
			{
				selectedMaskLayer = new Shape();
				var g:Graphics = selectedMaskLayer.graphics;
				g.beginFill(0xFFFFFF);
				g.drawRect(0, 0, 10, 10);
				g.endFill();
			}
			selectedMaskLayer.visible 	= false;
			selectedMaskLayer.x = paddingLeft;
			return selectedMaskLayer;
		}
		
		/**
		 * @private
		 */
		/*-.........................................createGlassLayer.....................................*/
		protected function createGlassLayer():DisplayObject
		{
			if(! glassLayer)
			{
				glassLayer = new Shape();
				var g:Graphics = glassLayer.graphics;
				g.beginFill(0x00000,0);
				g.drawRect(0, 0, 10, 10);
				g.endFill();
			}
			glassLayer.visible 	= true;
			return glassLayer;
		}
		
		/**
		 * @private
		 */
		/*-.........................................removeAll.....................................*/
		protected function removeAll():void
		{
			removeChild(unselectedLayer);
			removeChild(selectedLayer);
			removeChild(overLayer);
			removeChild(upLayer);
			removeChild(selectedMaskLayer);
		}
		
		/**
		 * @private
		 */
		/*-.........................................layoutItems.....................................*/
		protected function layoutItems():void
		{
			if(!needLayout) return;
			
			var xPos:int = paddingLeft;
			var yPos:int = paddingTop;
			
			for(var i:uint; i < itemCount; i++)
			{
				unselectedLayer.getChildAt(i).x = xPos;
				unselectedLayer.getChildAt(i).y = yPos;
				
				selectedLayer.getChildAt(i).x 	= xPos;
				selectedLayer.getChildAt(i).y 	= yPos;
				
				upLayer.getChildAt(i).x 		= xPos;
				upLayer.getChildAt(i).y 		= yPos;
				
				overLayer.getChildAt(i).x 		= xPos;
				overLayer.getChildAt(i).y 		= yPos;
				
				xPos += itemWidth + horizontalGap;
			}
			needLayout = false;
			unselectedLayer.visible = true;
		}
		
		/**
		 * @private
		 */
		/*-.........................................updateValue.....................................*/
		protected function updateValue():void
		{
			if(!valueChanged) return;
			
			var floor:int = Math.floor(value);
			
			selectedMaskLayer.width = floor * (itemWidth + horizontalGap) + (value - floor) * itemWidth;
			selectedMaskLayer.height = itemHeight;
			upLayer.mask = selectedMaskLayer;
			
			visibleLayer = upLayer;
			valueChanged = false;
		}
		
		/**
		 * @private
		 */
		/*-.........................................updateSelectedValue.....................................*/
		protected function updateSelectedValue():void
		{
			if(!selectedValueChanged || !showSelectedValue) return;
			
			for( var i:uint; i < itemCount; i++)
			{
				var item:DisplayObject = selectedLayer.getChildAt(i);
				item.visible = (selectedValue > i);
			}
			visibleLayer = selectedLayer;
			
			selectedValueChanged = false;
		}
		
		/**
		 * @private
		 */
		/*-.........................................updateItemRollOver.....................................*/
		protected function updateItemRollOver():void
		{
			rollOverValue = 0;
			for( var i:uint; i < itemCount; i++)
			{
				var item:DisplayObject = overLayer.getChildAt(i);
				item.visible = (mouseX > item.x- horizontalGap/2);
				
				if(item.visible) rollOverValue++;
			}
		}	
		
		/**
		 * @private
		 */
		/*-.........................................forceUpdateRollOver.....................................*/
		protected function forceUpdateRollOver(rating:int):void{
			for( var i:int = 0; i < _itemCount; i++)
			{
				if(i < rating){
					var item:DisplayObject = overLayer.getChildAt(i);
					item.visible = true;			
				}
				else{
					var item:DisplayObject = overLayer.getChildAt(i);
					item.visible = false;	
				}
			}
		}
		
		/*------------------------------------------------------------------------------------------------
		*                                          Mouse Events
		-------------------------------------------------------------------------------------------------*/	
		
		/**
		 * @private
		 */
		/*-.........................................handleRollOut.....................................*/
		protected function handleRollOut(event:MouseEvent):void
		{
			if(enabled){
				overLayer.visible = false;
				if(visibleLayer) visibleLayer.visible = true;
			}
		}
		
		/**
		 * @private
		 */
		/*-.........................................handleItemRollOver.....................................*/
		protected function handleItemRollOver(event:MouseEvent):void
		{
			if(enabled){
				if(voted){
					rollOverValue = _votedValue;
					overLayer.visible = true;
					if(visibleLayer) visibleLayer.visible = false;
					forceUpdateRollOver(rollOverValue);
				}
				else if(liveRollOver)
				{
					updateItemRollOver();
					overLayer.visible = true;
					if(visibleLayer) visibleLayer.visible = false;
				}
			}
		}
		
		/**
		 * @private
		 */
		/*-.........................................handleItemRollOut.....................................*/
		protected function handleItemRollOut(event:MouseEvent):void
		{
			if(enabled){
				if(liveRollOver && !voted)
				{
					updateItemRollOver();
				}
			}
		}
		
		/**
		 * @private
		 */
		/*-.........................................handleClick.....................................*/
		protected function handleClick(event:MouseEvent):void
		{
			if(enabled){
				if(!voted){
					selectedValue = rollOverValue;
				}				
			}
		}
	}
}

