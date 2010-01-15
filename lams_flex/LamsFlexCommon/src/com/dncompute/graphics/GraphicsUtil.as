package com.dncompute.graphics {
	
	import com.dncompute.geom.GeomUtil;
	
	import flash.display.Graphics;
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
	 */
	 
	 
	public class GraphicsUtil {
		
		public static function drawArrow(graphics:Graphics,
				start:Point,end:Point,
				style:Object=null):void {
			
			if (start.equals(end)) return;
			
			var arrowStyle:ArrowStyle;
			if (style == null) {
				arrowStyle = new ArrowStyle();
			} else if (style is ArrowStyle) {
				arrowStyle = style as ArrowStyle;
			} else {
				arrowStyle = new ArrowStyle(style);
			}
			
			var fullVect:Point = end.subtract(start);
			var halfWidth:Number = (arrowStyle.headWidth != -1) ? arrowStyle.headWidth/2 : arrowStyle.headLength/2; 
			
			//Figure out the line start/end points
			var startNorm:Point = new Point(fullVect.y,-fullVect.x);
			startNorm.normalize(arrowStyle.shaftThickness/2);
			var start1:Point = start.add(startNorm);
			var start2:Point = start.subtract(startNorm);
			var end1:Point = end.add(startNorm);
			var end2:Point = end.subtract(startNorm);
			
			//figure out where the arrow head starts
			var headPnt:Point = fullVect.clone();
			headPnt.normalize(headPnt.length-arrowStyle.headLength);
			headPnt = headPnt.add(start);
			
			//calculate the arrowhead corners
			var headPntNorm:Point = startNorm.clone();
			headPntNorm.normalize(halfWidth);
			var edge1:Point = headPnt.add(headPntNorm);
			var edge2:Point = headPnt.subtract(headPntNorm);
			
			//Figure out where the arrow connects the the shaft, then calc the intersections
			var shaftCenter:Point = Point.interpolate(end,headPnt,arrowStyle.shaftPosition);
			var inter1:Point = GeomUtil.getLineIntersection(start1,end1,shaftCenter,edge1);
			var inter2:Point = GeomUtil.getLineIntersection(start2,end2,shaftCenter,edge2);
			
			//Figure out the control points
			var edgeCenter:Point = Point.interpolate(end,headPnt,arrowStyle.edgeControlPosition);
			var edgeNorm:Point = startNorm.clone();
			edgeNorm.normalize(halfWidth*arrowStyle.edgeControlSize);
			var edgeCntrl1:Point = edgeCenter.add(edgeNorm);
			var edgeCntrl2:Point = edgeCenter.subtract(edgeNorm);


			graphics.moveTo(start1.x,start1.y);
			graphics.lineTo(inter1.x,inter1.y);
			graphics.lineTo(edge1.x,edge1.y);
			graphics.curveTo(edgeCntrl1.x,edgeCntrl1.y,end.x,end.y);
			graphics.curveTo(edgeCntrl2.x,edgeCntrl2.y,edge2.x,edge2.y);
			graphics.lineTo(inter2.x,inter2.y);
			graphics.lineTo(start2.x,start2.y);
			graphics.lineTo(start1.x,start1.y);
		}
		
		public static function drawArrowHalfway(graphics:Graphics,
				start:Point,end:Point,
				style:Object=null):void {
			
			
			var endOrig:Point = end.clone();
			end = Point.interpolate(start, end, 0.4);
			
			if (start.equals(end)) return;
			
			var arrowStyle:ArrowStyle;
			if (style == null) {
				arrowStyle = new ArrowStyle();
			} else if (style is ArrowStyle) {
				arrowStyle = style as ArrowStyle;
			} else {
				arrowStyle = new ArrowStyle(style);
			}
			
			var fullVect:Point = end.subtract(start);
			var halfWidth:Number = (arrowStyle.headWidth != -1) ? arrowStyle.headWidth/2 : arrowStyle.headLength/2; 
			
			//Figure out the line start/end points
			var startNorm:Point = new Point(fullVect.y,-fullVect.x);
			startNorm.normalize(arrowStyle.shaftThickness/2);
			var start1:Point = start.add(startNorm);
			var start2:Point = start.subtract(startNorm);
			var end1:Point = end.add(startNorm);
			var end2:Point = end.subtract(startNorm);
			
			//figure out where the arrow head starts
			var headPnt:Point = fullVect.clone();
			headPnt.normalize(headPnt.length-arrowStyle.headLength);
			headPnt = headPnt.add(start);
			
			//calculate the arrowhead corners
			var headPntNorm:Point = startNorm.clone();
			headPntNorm.normalize(halfWidth);
			var edge1:Point = headPnt.add(headPntNorm);
			var edge2:Point = headPnt.subtract(headPntNorm);
			
			//Figure out where the arrow connects the the shaft, then calc the intersections
			var shaftCenter:Point = Point.interpolate(end,headPnt,arrowStyle.shaftPosition);
			var inter1:Point = GeomUtil.getLineIntersection(start1,end1,shaftCenter,edge1);
			var inter2:Point = GeomUtil.getLineIntersection(start2,end2,shaftCenter,edge2);
			
			//Figure out the control points
			var edgeCenter:Point = Point.interpolate(end,headPnt,arrowStyle.edgeControlPosition);
			var edgeNorm:Point = startNorm.clone();
			edgeNorm.normalize(halfWidth*arrowStyle.edgeControlSize);
			var edgeCntrl1:Point = edgeCenter.add(edgeNorm);
			var edgeCntrl2:Point = edgeCenter.subtract(edgeNorm);


			graphics.moveTo(start1.x,start1.y);
			graphics.lineTo(inter1.x,inter1.y);
			graphics.lineTo(edge1.x,edge1.y);
			graphics.curveTo(edgeCntrl1.x,edgeCntrl1.y,end.x,end.y);
			graphics.curveTo(edgeCntrl2.x,edgeCntrl2.y,edge2.x,edge2.y);
			graphics.lineTo(inter2.x,inter2.y);

		}
	}
	
}