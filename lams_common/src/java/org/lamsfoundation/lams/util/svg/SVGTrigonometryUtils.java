/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.util.svg;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


/**
 * @author Andrey Balan
 */
public class SVGTrigonometryUtils {

    /**
     * @param rectangleActivity we draw rectangle based on this activity 
     * @param toActivity draw transition to this activity
     * @return
     */
    public static Point2D getRectangleAndLineSegmentIntersection(ActivityTreeNode rectangleActivity, ActivityTreeNode toActivity) {
	//rectangles dimensions
	int rectangleX = rectangleActivity.getActivityCoordinates().x;
	int rectangleY = rectangleActivity.getActivityCoordinates().y;
	int width = rectangleActivity.getActivityDimension().width;
	int height = rectangleActivity.getActivityDimension().height;
	
	//construct rectangle's lines
	Line2D topLine = new Line2D.Double(rectangleX, rectangleY, rectangleX + width, rectangleY);
	Line2D rightLine = new Line2D.Double(rectangleX + width, rectangleY, rectangleX + width, rectangleY + height);
	Line2D bottomLine = new Line2D.Double(rectangleX, rectangleY + height, rectangleX + width, rectangleY + height);
	Line2D leftLine = new Line2D.Double(rectangleX, rectangleY, rectangleX, rectangleY + height);
	
	//calculate MiddlePoint of the second rectangle
	int transitionToX = toActivity.getActivityCoordinates().x + toActivity.getActivityDimension().width /2;
	int transitionToY = toActivity.getActivityCoordinates().y + toActivity.getActivityDimension().height /2;
	
	Line2D transitionLine = new Line2D.Double(rectangleX + width/2, rectangleY + height/2, transitionToX, transitionToY);
	
	Point2D intersectionPoint = null;
	if (isLineSegmentsIntersect(topLine, transitionLine)) {
	    intersectionPoint = getLinesIntersection(topLine, transitionLine);
	} else if (isLineSegmentsIntersect(rightLine, transitionLine)) {
	    intersectionPoint = getLinesIntersection(rightLine, transitionLine);
	} else if (isLineSegmentsIntersect(bottomLine, transitionLine)) {
	    intersectionPoint = getLinesIntersection(bottomLine, transitionLine);
	} else if (isLineSegmentsIntersect(leftLine, transitionLine)) {
	    intersectionPoint = getLinesIntersection(leftLine, transitionLine);
	}
	
	return intersectionPoint;
    }

    /**
     * Computes the intersection between first line (x1, y1)--(x2, y2) and second one (x3, y3)--(x4, y4)
     * 
     * @return Point where the lines intersect, or null if they don't
     */
    private static Point2D getLinesIntersection(Line2D line1, Line2D line2) {
	double x1 = line1.getX1();
	double y1 = line1.getY1();
	double x2 = line1.getX2();
	double y2 = line1.getY2();
	double x3 = line2.getX1();
	double y3 = line2.getY1();
	double x4 = line2.getX2();
	double y4 = line2.getY2();

	double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
	if (d == 0)
	    return null;

	double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
	double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

	return new Point2D.Double(xi, yi);
    }

    /** Do line segments (x1, y1)--(x2, y2) and (x3, y3)--(x4, y4) intersect? */
    private static boolean isLineSegmentsIntersect(Line2D line1, Line2D line2) {
	double x1 = line1.getX1();
	double y1 = line1.getY1();
	double x2 = line1.getX2();
	double y2 = line1.getY2();
	double x3 = line2.getX1();
	double y3 = line2.getY1();
	double x4 = line2.getX2();
	double y4 = line2.getY2();

	int d1 = computeDirection(x3, y3, x4, y4, x1, y1);
	int d2 = computeDirection(x3, y3, x4, y4, x2, y2);
	int d3 = computeDirection(x1, y1, x2, y2, x3, y3);
	int d4 = computeDirection(x1, y1, x2, y2, x4, y4);
	return (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
		|| (d1 == 0 && isOnSegment(x3, y3, x4, y4, x1, y1)) || (d2 == 0 && isOnSegment(x3, y3, x4, y4, x2, y2))
		|| (d3 == 0 && isOnSegment(x1, y1, x2, y2, x3, y3)) || (d4 == 0 && isOnSegment(x1, y1, x2, y2, x4, y4));
    }

    private static boolean isOnSegment(double xi, double yi, double xj, double yj, double xk, double yk) {
	return (xi <= xk || xj <= xk) && (xk <= xi || xk <= xj) && (yi <= yk || yj <= yk) && (yk <= yi || xk <= yj);
    }

    private static int computeDirection(double xi, double yi, double xj, double yj, double xk, double yk) {
	double a = (xk - xi) * (yj - yi);
	double b = (xj - xi) * (yk - yi);

	return a < b ? -1 : a > b ? 1 : 0;
    }

}
