package com.asfusion.controls.ratingclasses
{
	import mx.styles.*;
	import mx.managers.ISystemManager;
	
	[Mixin]
	/**
	 * Default skin for rollover item (star)
	 * */
	public class OverStar extends Star
	{
		private static var className:String = "OverStar"; 
		
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
		        this.backgroundColor = 0xFFE30E;
		        this.backgroundAlpha = 1;
		    }
		    	
		    StyleManager.setStyleDeclaration(className, style, true);
		}
	}
}