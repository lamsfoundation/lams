package com.asfusion.controls.ratingclasses
{
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.StyleManager;
	import com.asfusion.controls.ratingclasses.*;
	
	public class RatingStyle
	{
		/*------------------------------------------------------------------------------------------------
		*                                          Static Methods
		-------------------------------------------------------------------------------------------------*/
		/**
		 * @private
		 */	
		public static function initDefaultStyles(className:String):void 
        {
            var style:CSSStyleDeclaration = new CSSStyleDeclaration();
            style.defaultFactory = function():void
		    {
				this.disabledSkin = Star;
				this.overSkin = OverStar;
				this.unselectedSkin = Star;
				this.upSkin = SelectedStar;
				this.selectedSkin = SelectedStar;
				
				this.horizontalGap = 5;
				this.paddingBottom = 0;
				this.paddingTop = 0;
				this.paddingLeft = 0;
				this.paddingRight = 0;
		    }
		    	
		    StyleManager.setStyleDeclaration(className, style, true);

        }
	}
}