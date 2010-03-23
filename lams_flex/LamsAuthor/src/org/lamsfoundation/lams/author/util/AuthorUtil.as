package org.lamsfoundation.lams.author.util
{
	import flash.display.BitmapData;
	import flash.display.Bitmap;
	import flash.display.DisplayObject;
	import flash.geom.Matrix;
	import flash.geom.Point;
	
	import mx.controls.Image;
	
	public class AuthorUtil
	{
		public function AuthorUtil(){}
		
		/**
		 * Gets the midpoint between the two points
		 * (x1 + x2)/2 , (y1 + y2)/2
		 * 
		 * @param point1
		 * @param point2
		 * @return 
		 * 
		 */
		public static function getMidpoint(point1:Point, point2:Point):Point {
			var x:Number = (point1.x + point2.x) / 2;
			var y:Number = (point1.y + point2.y) / 2;
			return new Point(x,y);
		}
		
		
		public static function getBitmapData(target:DisplayObject):BitmapData
		{
			var bd:BitmapData = new BitmapData( target.width, target.height );
			var m:Matrix = new Matrix();
			bd.draw( target, m );
			return bd;
	   	}
	   	
	   	public static function getImage(target:DisplayObject):Image
		{
			var image:Image = new Image();
	        image.source = new Bitmap(getBitmapData(target));
			return image;
	   	}

	}
}