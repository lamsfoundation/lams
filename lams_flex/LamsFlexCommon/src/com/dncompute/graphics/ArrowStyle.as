package com.dncompute.graphics {
	
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
	 
	/*
	 * @param width - if width is -1, the arrow is equilateral
	 * @param shaftPosition - Determines how far along the arrow 
	 * 		shaft the head is connected. -1 will result in a 
	 * 		diamond shape, 0 will be a standard triangle, .5 will 
	 *      be a recessed arrow (like a mouse pointer). Anything
	 * 		greater than 1 will invert the shape.
	 * @param shaftControlPosition - let's you curve the line where
	 * 		the base points meet the shaft. Determines what percentage
	 * 		from the shaft-head to base points to place the control points
	 * @param shaftControlSize - Less than .5 sharpens the
	 * 		shape, whereas anything greater than .5 makes it bulbous
	 */
	public class ArrowStyle {
		
		public var headWidth:Number=-1; //Relative width of arrow head
		
		/**
		 * 
		 * Not used in drawArrowHead because the length is 
		 * determined by the points passed in
		 * 
		 */
		public var headLength:Number=10; //Pixel Length of arrow head
				
		
		public var shaftThickness:Number=2;
		public var shaftPosition:Number=0;
		
		/**
		 *  Not used in drawArrow, only drawArrowHead
		 * 	This let's you curve the line at the base of the arrow
		 */
		public var shaftControlPosition:Number=.5;
		/**
		 * Not used in drawArrow, only drawArrowHead
		 * This let's you curve the line at the base of the arrow
		 */
		public var shaftControlSize:Number=.5;
		
		
		public var edgeControlPosition:Number=.5;
		public var edgeControlSize:Number=.5;
				
		public function ArrowStyle(presets:Object=null) {
			if (presets != null) {
				for (var name:String in presets) {
					this[name] = presets[name];
				}
			}
		}

	}
	
}