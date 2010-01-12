package com.asfusion.controls.ratingclasses
{
	import mx.styles.*;
	import mx.managers.ISystemManager;
	
	[Mixin]
	/**
	 * Default skin for selected item (star)
	 * */
	public class SelectedStar extends Star
	{
		private static var className:String = "SelectedStar";
		
		/*------------------------------------------------------------------------------------------------
		*                                          Static Methods
		-------------------------------------------------------------------------------------------------*/	
		
		/*-.........................................init..................................................*/
		/**
		 * @private
		 */
		public static function init(systemManager:ISystemManager) : void 
		{
	        var style:CSSStyleDeclaration = new CSSStyleDeclaration();
            style.defaultFactory = function():void
		    {
		        this.backgroundColor = 0x000000;
		        this.backgroundAlpha = 1;
		    }
		    	
		    StyleManager.setStyleDeclaration(className, style, true);
		}
	}
}