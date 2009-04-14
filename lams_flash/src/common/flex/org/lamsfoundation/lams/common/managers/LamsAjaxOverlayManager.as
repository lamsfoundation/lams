package org.lamsfoundation.lams.common.managers
{
	import flash.display.DisplayObjectContainer;
	
	import mx.collections.ArrayCollection;
	import mx.containers.Canvas;
	import mx.controls.Alert;
	import mx.controls.Image;
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
		
		// show an overlay
		public static function showOverlay(obj:UIComponent):void{			
			// check for an existing overlay
			var overlaySet:Object = isInOverlays(obj);
			
			// if an overlay exists
			if(overlaySet != null){
				// don't try show
				return;
			}
			
			// create a new one
			overlaySet = createNewOverlaySet(obj);
			
			// get needed objects
			var objUi:UIComponent = UIComponent(overlaySet.object);
			var overlay:Canvas = Canvas(overlaySet.overlay);
			var objParent:DisplayObjectContainer = objUi.parent;
			
			// disable the object under the overlay
			objUi.enabled = false;
			
			// set overlay to be invisible
			overlay.visible = false;

			// set object's properties to overlay's
			overlay.x = objUi.x;
			overlay.y = objUi.y;
						
			if(objUi.getStyle("top"))
				overlay.setStyle("top", objUi.getStyle("top"));			
			else
				overlay.clearStyle("top");

			if(objUi.getStyle("bottom"))
				overlay.setStyle("bottom", objUi.getStyle("bottom"));
			else
				overlay.clearStyle("bottom");
				
			if(objUi.getStyle("left"))
				overlay.setStyle("left", objUi.getStyle("left"));
			else
				overlay.clearStyle("left");
			
			if(objUi.getStyle("right"))
				overlay.setStyle("right", objUi.getStyle("right"));
			else
				overlay.clearStyle("right");

			if(objUi.percentHeight)
				overlay.percentHeight = objUi.percentHeight;
			else
				overlay.height = objUi.height;
				
			if(objUi.percentWidth)
				overlay.percentWidth = objUi.percentWidth;
			else
				overlay.width = objUi.width;
				
			// remove object from its parent
			objParent.removeChild(objUi);

			// set object be full inside overlay
			objUi.setStyle("top", 0);
			objUi.setStyle("bottom", 0);
			objUi.setStyle("left", 0);
			objUi.setStyle("right", 0);
			
			// add the object to the overlay (under the spinner)
			overlay.addChildAt(objUi, 0);
			
			// add the overlay to the parent
			objParent.addChild(overlay);
			
			// show the overlay
			overlay.visible = true;
		}
		
		// hides an overlay
		public static function hideOverlay(obj:UIComponent):void{
			// check for an existing overlay
			var overlaySet:Object = isInOverlays(obj);
			
			// if an overlay exists
			if(overlaySet){
				// get needed objects
				var objUi:UIComponent = UIComponent(overlaySet.object);
				var overlay:Canvas = Canvas(overlaySet.overlay);
				var objParent:DisplayObjectContainer = overlay.parent;
				
				// remove the overlay
				objParent.removeChild(overlay);
				
				// remove the object from the overlay
				overlay.removeChild(objUi);
				
				// make object invisible temporarily
				objUi.visible = false;
				
				// return the object to it's original state
				objUi.x = overlay.x;
				objUi.y = overlay.y;
				
				if(overlay.percentHeight)
					objUi.percentHeight = overlay.percentHeight;
				else
					objUi.height = objUi.height;
					
				if(overlay.percentWidth)
					objUi.percentWidth = overlay.percentWidth;
				else
					objUi.width = overlay.width;
				
				if(overlay.getStyle("top"))
					objUi.setStyle("top", overlay.getStyle("top"));
				else
					objUi.clearStyle("top");
					
				if(overlay.getStyle("bottom"))
					objUi.setStyle("bottom", overlay.getStyle("bottom"));
				else
					objUi.clearStyle("bottom");
					
				if(overlay.getStyle("left"))
					objUi.setStyle("left", overlay.getStyle("left"));
				else
					objUi.clearStyle("left");
					
				if(overlay.getStyle("right"))
					objUi.setStyle("right", overlay.getStyle("right"));
				else
					objUi.clearStyle("right");
				
				// add the object
				objParent.addChild(objUi);
				
				// make object visible
				objUi.visible = true;
				
				// re-enable the object
				objUi.enabled = true;
				
				// set the object to null (for overlay recycling)			
				overlaySet.object = null;
			}
		}
		
		// creates a new overlay set
		private static function createNewOverlaySet(obj:Object):Object{			
			var newOverlaySet:Object = new Object();
			
			// check for unused overlay
			var newOverlayCanvas:Canvas = getUnusedOverlay();
			
			// if no recycled overlay was found
			if(!newOverlayCanvas){
				// create a new one
				trace("creating new");
				newOverlayCanvas = new Canvas();
				
				// create and prepare the spinner
				var ajaxImage:Image = new Image();
				ajaxImage.source = ajaxIcon;
				ajaxImage.setStyle("horizontalCenter", 0);
				ajaxImage.setStyle("verticalCenter", 0);
			
				// add the spinner
				newOverlayCanvas.addChild(ajaxImage);
				
				// set the object and overlay
				newOverlaySet.object = obj;
				newOverlaySet.overlay = newOverlayCanvas;
			
				// add it to the collection
				trace("adding new");
				overlays.addItem(newOverlaySet);
			}
			else{
				// set the object and overlay
				trace("updating old");
				newOverlaySet = updateOverlaySet(obj, newOverlayCanvas);
			}	
			
			return newOverlaySet;
		}
		
		// checks if object has an overlay
		private static function isInOverlays(obj:UIComponent):Object{
			trace(overlays.length);
			for each(var overlaySet:Object in overlays){
				if(overlaySet.object == obj){
					return overlaySet;
				}
			}
			
			return null;
		}
		
		// recycles an overlay
		private static function getUnusedOverlay():Canvas{
			for each(var overlaySet:Object in overlays){
				if(overlaySet.object == null){
					trace("recycling");
					return overlaySet.overlay;
				}
			}
			
			return null
		}
		
		// update the overlayset
		private static function updateOverlaySet(obj:Object, newOverlay:Canvas):Object{
			for each(var overlaySet:Object in overlays){
				if(overlaySet.overlay == newOverlay){
					trace("updating");
					overlaySet.object = obj;
					overlaySet.overlay = newOverlay;
					return overlaySet;
				}
			}
			
			return null
		}
	}
}