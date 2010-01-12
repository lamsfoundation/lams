package com.asfusion.controls.ratingclasses
{
	import mx.core.UIComponent;
	import mx.styles.*;
	import mx.managers.ISystemManager;
	import flash.display.Graphics;

	[Mixin]
	
	/**
	 * Default skin for unselected item (star)
	 * */
	public class Star extends UIComponent
	{
		protected var outerRadius:Number;
		protected var innerRadius:Number;
		protected var borderThickness:Number;
		protected var borderColor:Number;
		protected var borderAlpha:Number;
		protected var backgroundColor:Number;
		protected var backgroundAlpha:Number;
		
		private   var maxSize:Number;
		private   var ratio:Number;
		
		private static var className:String = "Star"; 
		
		/*------------------------------------------------------------------------------------------------
		*                                          Static Methods
		-------------------------------------------------------------------------------------------------*/	
		
		/**
		 * @private
		 */
		/*-.........................................init..................................................*/
		public static function init(systemManager:ISystemManager) : void 
		{
	        var style:CSSStyleDeclaration = new CSSStyleDeclaration();
            style.defaultFactory = function():void
		    {
		    	this.borderColor = 0x555555;
		        this.outerRadius = 50;
		        this.innerRadius = 25;
		        this.borderThickness = 1;
		        this.borderAlpha = 1;
		        this.width = 14;
		        this.height = 15;
		        this.backgroundColor = 0xffffff;
		        this.backgroundAlpha = 0;
		    }
		    	
		    StyleManager.setStyleDeclaration(className, style, true);
		}
		
		/*------------------------------------------------------------------------------------------------
		*                                          Ocerride Methods
		-------------------------------------------------------------------------------------------------*/	
		/**
		 * @private
		 */
		override protected function createChildren():void
		{
			drawShape(graphics, measuredWidth/2, measuredHeight/2, (maxSize/2)/ratio,(maxSize/2) );
		}
		
		/**
		 * @private
		 */
		/*-.........................................stylesInitialized.....................................*/
		override public function stylesInitialized():void
		{
			readStyleValues();
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
			outerRadius		= getStyle("outerRadius");
			innerRadius 	= getStyle("innerRadius");
			borderThickness = getStyle("borderThickness");
			borderColor		= getStyle("borderColor");
			borderAlpha		= getStyle("borderAlpha");
			measuredWidth	= getStyle("width");
			measuredHeight	= getStyle("height");
			backgroundColor = getStyle("backgroundColor");
			backgroundAlpha = getStyle("backgroundAlpha");
			
			maxSize = Math.min(measuredHeight, measuredWidth);
			ratio= outerRadius/innerRadius;
		}
		
		/**
		 * @private
		 */
		/*-.........................................drawShape.....................................*/
		protected function drawShape(graphics:Graphics, x:Number, y:Number,  innerRadius:Number, outerRadius:Number, points:Number = 5, angle:Number=90 ):void
		{
			graphics.clear();
	        graphics.lineStyle(borderThickness,borderColor,borderAlpha);
	        if(backgroundAlpha > 0)
	        {
	        	graphics.beginFill(backgroundColor, backgroundAlpha);
	        }
	        var count:int = Math.abs(points);
	        if (count>=2) 
	        {
	               
	            // calculate distance between points
	            var step:Number = (Math.PI*2)/points;
	            var halfStep:Number = step/2;
	            
	            // calculate starting angle in radians
	            var start:Number = (angle/180)*Math.PI;
	            graphics.moveTo(x+(Math.cos(start)*outerRadius), y-(Math.sin(start)*outerRadius));
	                
	            // draw lines
	            for (var i:int=1; i<=count; i++) 
	            {
	                graphics.lineTo(x+Math.cos(start+(step*i)-halfStep)*innerRadius, 
	                y-Math.sin(start+(step*i)-halfStep)*innerRadius);
	                graphics.lineTo(x+Math.cos(start+(step*i))*outerRadius, 
	                y-Math.sin(start+(step*i))*outerRadius);
	            }
	        }
	        if(backgroundAlpha > 0)
	        {
	        	graphics.endFill();
	        }
		}
	}
}