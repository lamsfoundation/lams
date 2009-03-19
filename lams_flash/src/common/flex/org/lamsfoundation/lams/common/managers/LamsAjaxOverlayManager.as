package org.lamsfoundation.lams.common.managers
{
	import mx.collections.ArrayCollection;
	import mx.containers.Canvas;
	import mx.controls.Image;
	import mx.core.Application;
	import mx.core.UIComponent;
	
	public class LamsAjaxOverlayManager
	{
		private static var overlays:ArrayCollection = new ArrayCollection();
		
		[Embed(source="../../../../../../../../../lams_central/web/images/ajax-loader-big.swf")]
		[Bindable]
		private static var ajaxIcon:Class;
		
		public function LamsAjaxOverlayManager()
		{
			
		}
		
		public static function showOverlay(obj:UIComponent):void{
			var overlaySet:Object = isInOverlays(obj);
			
			if(overlaySet != null && overlaySet.overlay.visible == true)
				return;
				
			if(!overlaySet){
				overlaySet = createNewOverlaySet(obj);
				overlays.addItem(overlaySet);
			}
				
			overlaySet.object.enabled = false;
			overlaySet.overlay.visible = true;
		}
		
		public static function hideOverlay(obj:UIComponent):void{
			var overlaySet:Object = isInOverlays(obj);
			if(overlaySet){
				overlaySet.object.enabled = true;
				overlaySet.overlay.visible = false;
				
				overlaySet.object = null;
			}
		}
		
		private static function createNewOverlaySet(obj:Object):Object{
			var newOverlaySet:Object = new Object();
			newOverlaySet.object = obj;
			
			var newOverlayCanvas:Canvas = getUnusedOverlay();
			
			if(!newOverlayCanvas)
				newOverlayCanvas = new Canvas();
			
			newOverlayCanvas.x = obj.x;
			newOverlayCanvas.y = obj.y;
			newOverlayCanvas.height = obj.height;
			newOverlayCanvas.width = obj.width;
			
			Application.application.addChild(newOverlayCanvas);
			
			var ajaxImage:Image = new Image();
			ajaxImage.source = ajaxIcon;
			ajaxImage.setStyle("horizontalCenter", 0);
			ajaxImage.setStyle("verticalCenter", 0);
			
			newOverlayCanvas.addChild(ajaxImage);
			
			newOverlaySet.overlay = newOverlayCanvas;
			
			return newOverlaySet;
		}
		
		private static function isInOverlays(obj:UIComponent):Object{
			for each(var overlay:Object in overlays){
				if(overlay.object == obj)
					return overlay;
			}
			
			return null;
		}
		
		private static function getUnusedOverlay():Canvas{
			for each(var overlay:Object in overlays){
				if(overlay.object == null)
					return overlay.canvas;
			}
			
			return null
		}
	}
}