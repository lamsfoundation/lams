package com.dncompute.geom {
	
	import flash.geom.Point;
	
	
	/**
	 * 
	 * Copyright (c) 2008 Noel Billig (www.dncompute.com)
	 * 
	 * Permission is hereby granted, free of charge, to any person obtaining a copy
	 * of this software and associated documentation files (the "Software"), to deal
	 * in the Software without restriction, including without limitation the rights
	 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	 * copies of the Software, and to permit persons to whom the Software is
	 * furnished to do so, subject to the following conditions:
	 *
	 * The above copyright notice and this permission notice shall be included in
	 * all copies or substantial portions of the Software.
	 *
	 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	 * THE SOFTWARE.
	 * 
	 */
	 
	/**
	 * 
	 * The line segment intersection code used here is based off of Erik Gustavsson's 
	 * tutorial found here: http://www.zikko.se/resources/lineIntersection.php
	 * That page reads: "All content on these pages may be used, copied and modifed freely."
	 * 
	 */
	public class GeomUtil {
		
		/**
		 *
		 * Returns the point of intersection between the line connecting point a1 to a2
		 * and the line connecting point b1 to b2.
		 *
		 */
		public static function getLineSegmentIntersection(
				a1:Point,a2:Point,
				b1:Point,b2:Point
				):Point {
			
			//XXX: Add an optimization here to see if the bounding boxes even overlap
			
			//Figure out where the lines intersect
			var intersection:Point = getLineIntersection(a1,a2,b1,b2);
			
			//They could be parellel, in which case there is no line intersection
			if (intersection == null) return null;
			
			//If the lines intersect, check and see if the intersection falls on the segments
			if (inRange(intersection,a1,a2) && inRange(intersection,b1,b2)) {
				return intersection;
			}
			
			return null;
			
		}
		
		
		/**
		 *
		 * Calculate the intersection between two lines. The intersection point
		 * may not necesarily occur on either line segment. To only get the line
		 * segment intersection, use <code>getLineSegmentIntersection</code> instead
		 *
		 */
		public static function getLineIntersection ( 
				a1:Point,a2:Point,
				b1:Point,b2:Point
				):Point {
		
			//calculate directional constants
			var k1:Number = (a2.y-a1.y) / (a2.x-a1.x);
			var k2:Number = (b2.y-b1.y) / (b2.x-b1.x);
			
			// if the directional constants are equal, the lines are parallel,
			// meaning there is no intersection point.
			if( k1 == k2 ) return null;
			
			var x:Number,y:Number;
			var m1:Number,m2:Number;
			
			// an infinite directional constant means the line is vertical
			if( !isFinite(k1) ) {
				
				// so the intersection must be at the x coordinate of the line
				x = a1.x;
				m2 = b1.y - k2 * b1.x;
				y = k2 * x + m2;
				
			// same as above for line 2
			} else if ( !isFinite(k2) ) {
				
				m1 = a1.y - k1 * a1.x;
				x = b1.x;
				y = k1 * x + m1;

			// if neither of the lines are vertical
			} else {
				
				m1 = a1.y - k1 * a1.x;
				m2 = b1.y - k2 * b1.x;				
				x = (m1-m2) / (k2-k1);
				y = k1 * x + m1;
				
			}
			
			return new Point(x,y);
		}
		
		
		private static function inRange(pnt:Point,a:Point,b:Point):Boolean {
			
			if (a.x != b.x) {
				return pnt.x <= a.x != pnt.x < b.x;
			} else {
				return pnt.y <= a.y != pnt.y < b.y;
			}
			
		}
		
		
	}
	
}