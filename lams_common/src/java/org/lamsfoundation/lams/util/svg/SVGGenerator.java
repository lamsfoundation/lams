package org.lamsfoundation.lams.util.svg;
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

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jdom.JDOMException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

/**
 * Generates SVG document based on exported learning design's xml file.
 * 
 *       To be able to see resulted SVG image in swing component use the following lines. 
         JSVGCanvas canvas = new JSVGCanvas();
         JFrame f = new JFrame();
         f.getContentPane().add(canvas);
         canvas.setSVGDocument(svgGenerator.getSVGDocument());
         f.pack();
         f.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
         f.setVisible(true); 
 * 
 * @author Andrey Balan
 */
public class SVGGenerator extends SVGConstants{
    
    private SVGDocument doc;
    
    private SVGGenerator(SVGDocument svgGDocument) {
	doc = svgGDocument;
    }
    
    /**
     * Sets up Svg root and defs. 
     */    
    public static SVGGenerator getInstance() {
	
        // Create an SVG document.
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        SVGDocument doc = (SVGDocument) impl.createDocument(SVG_NAMESPACE, "svg", null);
        // Get the root element (the 'svg' element).
        Element svgRoot = doc.getDocumentElement();
        // Set the width and height attributes on the root 'svg' element.
        svgRoot.setAttributeNS(null, "width", Integer.toString(CANVAS_WIDTH));
        svgRoot.setAttributeNS(null, "height", Integer.toString(CANVAS_HEIGHT));
        svgRoot.setAttributeNS(null, "xmlns", SVG_NAMESPACE);
        svgRoot.setAttributeNS(null, "xmlns:xlink", SVG_NAMESPACE_XLINK);
        
        //create arrow definition
	Element defs = doc.createElementNS(SVG_NAMESPACE, "defs");
	svgRoot.appendChild(defs);
	Element marker = doc.createElementNS(SVG_NAMESPACE, "marker");
	marker.setAttributeNS(null, "id", "Triangle");
	marker.setAttributeNS(null, "viewBox", "0 0 10 10");
	marker.setAttributeNS(null, "refX", "0");
	marker.setAttributeNS(null, "refY", "5");
	marker.setAttributeNS(null, "markerUnits", "strokeWidth");
	marker.setAttributeNS(null, "markerWidth", "6");
	marker.setAttributeNS(null, "markerHeight", "5");
	marker.setAttributeNS(null, "orient", "auto");
	defs.appendChild(marker);
	Element path = doc.createElementNS(SVG_NAMESPACE, "path");
	path.setAttributeNS(null, "d", "M 0 0 L 10 5 L 0 10 z");
	marker.appendChild(path);	
		
	return new SVGGenerator(doc);
    }
    
    public SVGDocument getSVGDocument() {
	return doc;
    }
    
    public void generateSvg(LearningDesignDTO learningDesign) throws JDOMException, IOException {

        //initialize all tree nodes
        ArrayList<AuthoringActivityDTO> activities = learningDesign.getActivities();
        HashMap<Long, ActivityTreeNode> allNodes = new HashMap<Long, ActivityTreeNode>();
        for (AuthoringActivityDTO activity : activities) {
            ActivityTreeNode node = new ActivityTreeNode(activity);
            allNodes.put(activity.getActivityID(), node);
        }
        
        //construct activities tree
        ActivityTreeNode root = new ActivityTreeNode();        
        for (ActivityTreeNode node : allNodes.values()) {
            AuthoringActivityDTO activity = node.getActivity();
            if (activity.getParentActivityID() == null) {
        	root.add(node);
            } else {
        	Long parentId = activity.getParentActivityID();
        	ActivityTreeNode parent = allNodes.get(parentId);
        	parent.add(node);
            }
        }
	
        //**************** Draw transitions********************************************************
        Element svgRoot = doc.getDocumentElement();
        ArrayList<TransitionDTO> transitions = learningDesign.getTransitions();
        for (TransitionDTO transition : transitions) {
            
            ActivityTreeNode fromActivity = allNodes.get(transition.getFromActivityID());
            ActivityTreeNode toActivity = allNodes.get(transition.getToActivityID());
	    Point2D fromIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(fromActivity, toActivity);
	    Point2D toIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(toActivity, fromActivity);            
            
            //skip optional sequence's childs
            if (fromActivity.isOptionalSequenceActivityChild()) {
        	continue;
            }            
	    
	    // Create the line
	    Element line = doc.createElementNS(SVG_NAMESPACE, "line");
	    line.setAttributeNS(null, "id", transition.getFromActivityID() + "_to_" + transition.getToActivityID());
	    line.setAttributeNS(null, "x1", Double.toString(fromIntersection.getX()));
	    line.setAttributeNS(null, "y1", Double.toString(fromIntersection.getY()));
	    line.setAttributeNS(null, "x2", Double.toString(toIntersection.getX()));
	    line.setAttributeNS(null, "y2", Double.toString(toIntersection.getY()));
	    line.setAttributeNS(null, "style", "stroke:#8C8FA6;stroke-width:2;opacity:1");
	    line.setAttributeNS(null, "parentID", "0");
	    
	    double a = (toIntersection.getX() - fromIntersection.getX());
	    double b = (toIntersection.getY() - fromIntersection.getY());
	    double yArrowShift = 5* b/Math.sqrt(a*a + b*b);
	    double xArrowShift = 5* a/Math.sqrt(a*a + b*b);
	    // Create the arrowhead	    
	    Element arrowhead = doc.createElementNS(SVG_NAMESPACE, "line");
	    arrowhead.setAttributeNS(null, "id", "arrowhead_" + transition.getFromActivityID() + "_to_" + transition.getToActivityID());
	    arrowhead.setAttributeNS(null, "x1", Double.toString(fromIntersection.getX()));
	    arrowhead.setAttributeNS(null, "y1", Double.toString(fromIntersection.getY()));
	    arrowhead.setAttributeNS(null, "x2", Double.toString((fromIntersection.getX() + toIntersection.getX())/2 - xArrowShift));
	    arrowhead.setAttributeNS(null, "y2", Double.toString((fromIntersection.getY() + toIntersection.getY())/2 - yArrowShift));
	    arrowhead.setAttributeNS(null, "style", "fill:#8C8FA6;stroke:#8C8FA6;stroke-width:2;opacity:1");
	    arrowhead.setAttributeNS(null, "marker-end", "url(#Triangle)");
	    arrowhead.setAttributeNS(null, "parentID", "0");

	    // Attach the line to the root 'svg' element.
	    svgRoot.appendChild(line);
	    svgRoot.appendChild(arrowhead);
        }
        
        //**************** Draw activities ********************************************************
        //tree traverse
        treeTraverse(root);
    }
    
    /**
     * Recursive tree traverse. 
     * 
     * @param doc
     * @param svgRoot
     * @param learningDesign
     * @param node
     */
    private void treeTraverse(ActivityTreeNode node) {
	AuthoringActivityDTO activity = node.getActivity();
	
	//draw root's activity, unless it's the start root which doesn't contain activity
	if (activity != null) {
	    createActivity(node);
	    
	    //in case of branching activity don't traverse child activities
	    if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		    || activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		    || activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) {
		return;
	    }
	}
	
        //traverse child subtrees
        for  (ActivityTreeNode child : node.getChildren()) {
            treeTraverse(child);
        }	
    }
    
    /**
     * Adds activity to SVG DOM.
     * 
     * @param doc
     * @param svgRoot
     * @param learningDesign
     * @param activity
     */
    private void createActivity(ActivityTreeNode node) {
	
	AuthoringActivityDTO activity = node.getActivity();
	
	// Create current activity element
	Element g = doc.createElementNS(SVG_NAMESPACE, "g");
	String activityId = activity.getActivityID().toString();
	g.setAttributeNS(null, "id", activityId);
	String parentID = (activity.getParentActivityID() == null) ? "0" : activity.getParentActivityID().toString();
	g.setAttributeNS(null, "parentID", parentID);
	// Attach the g element to the root 'svg' element.
	Element svgRoot = doc.getDocumentElement();
	svgRoot.appendChild(g);

	int x = node.getActivityCoordinates().x;
	int y = node.getActivityCoordinates().y;
	// activities with parents (paralles, optionals, branching, etc)
	if (activity.getParentActivityID() != null) {
	    AuthoringActivityDTO parentActivity = node.getParentActivity();
	    x += (parentActivity.getxCoord() == null) ?  0 : parentActivity.getxCoord();
	    y += (parentActivity.getyCoord() == null) ?  0 : parentActivity.getyCoord();
	}

	Integer width = node.getActivityDimension().width;
	Integer height = node.getActivityDimension().height;	
	String text = activity.getActivityTitle();

	// if this is a stop gate we need to draw an octogon instead
	if (activity.getActivityTypeID().equals(Activity.SYNCH_GATE_ACTIVITY_TYPE) 
		|| activity.getActivityTypeID().equals(Activity.SCHEDULE_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.PERMISSION_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.CONDITION_GATE_ACTIVITY_TYPE)) {
	    // don't care about SYSTEM_GATE_ACTIVITY_TYPE
	    x += 8;
	    y -= 2;

	    String finalProportions = "";
	    for (double[] proportion : GATE_PROPORTIONS) {
		finalProportions += (x + proportion[0]) + "," + (y + proportion[1]) + " ";
	    }

	    Element polygon = doc.createElementNS(SVG_NAMESPACE, "polygon");
	    polygon.setAttributeNS(null, "style", "fill:red;stroke:#000000;stroke-width:0.5px");
	    polygon.setAttributeNS(null, "points", finalProportions);
	    g.appendChild(polygon);

	    // calculate midpoint for STOP text
	    double x1 = x + GATE_PROPORTIONS[6][0];
	    double x2 = x + GATE_PROPORTIONS[2][0];
	    double midpointX = (x1 + x2) / 2;

	    double y1 = y + GATE_PROPORTIONS[6][1];
	    double y2 = y + GATE_PROPORTIONS[2][1];
	    double midpointY = (y1 + y2) / 2 + 3;

	    createText("Gate_" + activityId, midpointX, midpointY, "0", "middle", "10", "Verdana", "fill:#FFFFFF;stroke:#FFFFFF;stroke-width:.5;", "STOP", g);
	    
	} else if (activity.getActivityTypeID().equals(Activity.PARALLEL_ACTIVITY_TYPE)) {
	    // This is a parallel activity

	    String style = "stroke:black;stroke-width:1;opacity:1;fill:#d0defd";
	    
	    // if the parallel is grouped, show it
	    if (activity.getApplyGrouping()) {
		createGroupingEffect("grouping-" + activityId, x, y, width, height, style, g);
	    }
	    
	    //TODO may be switch to using the following operators...    createRectangle(null, x, y, width, height, style, g);
	    Element parallelContainer = doc.createElementNS(SVG_NAMESPACE, "rect");
	    parallelContainer.setAttributeNS(null, "x", Integer.toString(x));
	    parallelContainer.setAttributeNS(null, "y", Integer.toString(y));
	    parallelContainer.setAttributeNS(null, "width", Integer.toString(width));
	    parallelContainer.setAttributeNS(null, "height", Integer.toString(height));
	    parallelContainer.setAttributeNS(null, "style", style);
	    g.appendChild(parallelContainer);
	    
	    Element parallelHeader = doc.createElementNS(SVG_NAMESPACE, "rect");
	    parallelHeader.setAttributeNS(null, "x", Integer.toString(x +4));
	    parallelHeader.setAttributeNS(null, "y", Integer.toString(y +5));
	    parallelHeader.setAttributeNS(null, "width", Integer.toString(width -8));
	    parallelHeader.setAttributeNS(null, "height", Integer.toString(23));
	    parallelHeader.setAttributeNS(null, "style", "fill:#A9C8FD;stroke:#E1F0FD;stroke-width:2.2;opacity:1");
	    g.appendChild(parallelHeader);

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	} else if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) {
	    // This is a branching activity

	    // Given that for now all parallel activities are just two activities, we can hard code the width and height
	    String style = "stroke:black;stroke-width:1;opacity:1;fill:#d0defd";
	    
	    // if the parallel is grouped, show it
	    if (activity.getApplyGrouping()) {
		createGroupingEffect("grouping-" + activityId, x, y, width, height, style, g);
	    }
	    
	    Element branchingContainer = doc.createElementNS(SVG_NAMESPACE, "rect");
	    branchingContainer.setAttributeNS(null, "x", Integer.toString(x));
	    branchingContainer.setAttributeNS(null, "y", Integer.toString(y));
	    branchingContainer.setAttributeNS(null, "width", Integer.toString(width));
	    branchingContainer.setAttributeNS(null, "height", Integer.toString(height));
	    branchingContainer.setAttributeNS(null, "style", style);
	    g.appendChild(branchingContainer);
	    
	    int startingPointX = x + 26;
	    int startingPointY = y + 40;
	    if (node.getChildCount() == 4) {
		startingPointY -= 12;
	    } else if (node.getChildCount() > 4) {
		startingPointY -= 2*12;
	    }
	    
	    Element startingPoint = doc.createElementNS(SVG_NAMESPACE, "rect");
	    startingPoint.setAttributeNS(null, "x", Integer.toString(startingPointX));
	    startingPoint.setAttributeNS(null, "y", Integer.toString(startingPointY));
	    startingPoint.setAttributeNS(null, "width", Double.toString(BRANCHING_ACTIVITY_POINT + 0.5));
	    startingPoint.setAttributeNS(null, "height", Double.toString(BRANCHING_ACTIVITY_POINT + 0.5));
	    startingPoint.setAttributeNS(null, "style", "fill:#000000");
	    g.appendChild(startingPoint);
	    
	    Iterator<ActivityTreeNode> sequenceNodeIterator = node.getChildren().iterator();
	    for (int sequenceIndex = -1; sequenceNodeIterator.hasNext() && (sequenceIndex < 4); sequenceIndex++) {
		ActivityTreeNode sequenceNode = sequenceNodeIterator.next();
		double previousActivityPointX = startingPointX + BRANCHING_ACTIVITY_POINT/2;
		double previousActivityPointY = startingPointY + BRANCHING_ACTIVITY_POINT/2;		

		// Create the lines
		Iterator<ActivityTreeNode> activityNodeIterator = sequenceNode.getChildren().iterator();
		for (int activityIndex=1; activityNodeIterator.hasNext() && (activityIndex <= 6); activityIndex++, activityNodeIterator.next()) {
		    double activityPointX = startingPointX + activityIndex*BRANCHING_STEP + BRANCHING_ACTIVITY_POINT/2;
		    double activityPointY = startingPointY + sequenceIndex*BRANCHING_STEP + BRANCHING_ACTIVITY_POINT/2;
		    
		    Element line = doc.createElementNS(SVG_NAMESPACE, "line");
		    line.setAttributeNS(null, "x1", Double.toString(previousActivityPointX));
		    line.setAttributeNS(null, "y1", Double.toString(previousActivityPointY));
		    line.setAttributeNS(null, "x2", Double.toString(activityPointX));
		    line.setAttributeNS(null, "y2", Double.toString(activityPointY));
		    line.setAttributeNS(null, "style", "stroke:black;stroke-width:1;");
		    g.appendChild(line);

		    previousActivityPointX = activityPointX;
		    previousActivityPointY = activityPointY;		    
		}

		//check if we need to draw line connecting last activity with endingPoint
		if (!sequenceNode.getActivity().getStopAfterActivity()) {
		    Element line = doc.createElementNS(SVG_NAMESPACE, "line");
		    line.setAttributeNS(null, "x1", Double.toString(previousActivityPointX));
		    line.setAttributeNS(null, "y1", Double.toString(previousActivityPointY));
		    line.setAttributeNS(null, "x2", Double.toString(x + 132 + BRANCHING_ACTIVITY_POINT / 2));
		    line.setAttributeNS(null, "y2", Double.toString(startingPointY + BRANCHING_ACTIVITY_POINT / 2));
		    line.setAttributeNS(null, "style", "stroke:black;stroke-width:1;");
		    g.appendChild(line);

		}
		
		 // Create activity points
		activityNodeIterator = sequenceNode.getChildren().iterator();
		for (int activityIndex=1; activityNodeIterator.hasNext() && (activityIndex <= 6); activityIndex++) {
		    ActivityTreeNode activityNode = activityNodeIterator.next();
		    String activityStyle = sequenceNode.getActivity().getStopAfterActivity()&&!activityNodeIterator.hasNext() ? "stroke:red" : "stroke:black";
		    activityStyle += ";stroke-width:0.8;opacity:1" + activityNode.getActivityColor();
		    double activityPointX = startingPointX + activityIndex*BRANCHING_STEP;
		    double activityPointY = startingPointY + sequenceIndex*BRANCHING_STEP;
		    
		    Element activityPoint = doc.createElementNS(SVG_NAMESPACE, "rect");
		    activityPoint.setAttributeNS(null, "x", Double.toString(activityPointX));
		    activityPoint.setAttributeNS(null, "y", Double.toString(activityPointY));
		    activityPoint.setAttributeNS(null, "width", "" + BRANCHING_ACTIVITY_POINT);
		    activityPoint.setAttributeNS(null, "height", "" + BRANCHING_ACTIVITY_POINT);
		    activityPoint.setAttributeNS(null, "style", activityStyle);
		    g.appendChild(activityPoint);
		}		
	    }
	    
	    Element endingPoint = doc.createElementNS(SVG_NAMESPACE, "rect");
	    endingPoint.setAttributeNS(null, "x", Integer.toString(x + 132));
	    endingPoint.setAttributeNS(null, "y", Integer.toString(startingPointY));
	    endingPoint.setAttributeNS(null, "width", Double.toString(BRANCHING_ACTIVITY_POINT + 0.5));
	    endingPoint.setAttributeNS(null, "height", Double.toString(BRANCHING_ACTIVITY_POINT + 0.5));
	    endingPoint.setAttributeNS(null, "style", "fill:#000000");
	    g.appendChild(endingPoint);  	    

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x + BRANCHING_ACTIVITY_WIDTH/2, y +90, null, "middle", "11.4", "Verdana", null, text, g);
	    }
	    
	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_WITH_SEQUENCES_TYPE)) {
	    // This is an optional sequence
	    
	    Element optionalContainer = doc.createElementNS(SVG_NAMESPACE, "rect");
	    optionalContainer.setAttributeNS(null, "x", Integer.toString(x));
	    optionalContainer.setAttributeNS(null, "y", Integer.toString(y));
	    optionalContainer.setAttributeNS(null, "width", Integer.toString(width));
	    optionalContainer.setAttributeNS(null, "height", Integer.toString(height));
	    optionalContainer.setAttributeNS(null, "style", "opacity:1;fill:#d0defd");
	    g.appendChild(optionalContainer);

	    Element optionalHeader = doc.createElementNS(SVG_NAMESPACE, "rect");
	    optionalHeader.setAttributeNS(null, "x", Integer.toString(x +4));
	    optionalHeader.setAttributeNS(null, "y", Integer.toString(y +5));
	    optionalHeader.setAttributeNS(null, "width", Integer.toString(width -8));
	    optionalHeader.setAttributeNS(null, "height", Integer.toString(CONTAINER_HEADER_HEIGHT));
	    optionalHeader.setAttributeNS(null, "style", "fill:#A9C8FD;stroke:#E1F0FD;stroke-width:2.2;opacity:1");
	    g.appendChild(optionalHeader);

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	    int optionalSequencesSize = node.getChildCount();
	    createText("Children-" + activityId, x +9, y +19*2+1, null, "start", "11", "Arial", "fill:#828990", optionalSequencesSize + " - Sequences", g);

	} else if (activity.getActivityTypeID().equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {	
	    // This is a sequence within an optional
	    
	    ActivityTreeNode parentNode = (ActivityTreeNode) node.getParent();
	    int indexInSiblings = parentNode.getIndex(node) % 6;
	    String color;
	    switch (indexInSiblings) {
	    case 1:
		color = "BCD0FF";
		break;
	    case 2:
		color = "C7F9AE";
		break;
	    case 3:
		color = "FFEDC3";
		break;
	    case 4:
		color = "EDDDF9";
		break;
	    case 5:
		color = "E9E9E9";
		break;
	    default:
		color = "FFFFB3";
	    }
		
	    Element sequenceContainer = doc.createElementNS(SVG_NAMESPACE, "rect");
	    sequenceContainer.setAttributeNS(null, "x", Integer.toString(x));
	    sequenceContainer.setAttributeNS(null, "y", Integer.toString(y));
	    sequenceContainer.setAttributeNS(null, "width", Integer.toString(width));
	    sequenceContainer.setAttributeNS(null, "height", Integer.toString(height));
	    sequenceContainer.setAttributeNS(null, "style", "stroke:#E1F0FD;stroke-width:.4;opacity:1;fill:#" + color);
	    g.appendChild(sequenceContainer);
	    
	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_ACTIVITY_TYPE)) {
	    // This is an optional activity
		
	    int childActivitiesSize = node.getChildCount();

	    // Create rect
	    Element optionalContainer = doc.createElementNS(SVG_NAMESPACE, "rect");
	    optionalContainer.setAttributeNS(null, "x", Integer.toString(x));
	    optionalContainer.setAttributeNS(null, "y", Integer.toString(y));
	    optionalContainer.setAttributeNS(null, "width", Integer.toString(width));
	    optionalContainer.setAttributeNS(null, "height", Integer.toString(height));
	    optionalContainer.setAttributeNS(null, "style", "opacity:1;fill:#d0defd");
	    g.appendChild(optionalContainer);

	    Element optionalHeader = doc.createElementNS(SVG_NAMESPACE, "rect");
	    optionalHeader.setAttributeNS(null, "x", Integer.toString(x + 4));
	    optionalHeader.setAttributeNS(null, "y", Integer.toString(y + 5));
	    optionalHeader.setAttributeNS(null, "width", Integer.toString(width - 8));
	    optionalHeader.setAttributeNS(null, "height", Integer.toString(CONTAINER_HEADER_HEIGHT));
	    optionalHeader.setAttributeNS(null, "style", "fill:#A9C8FD;stroke:#E1F0FD;stroke-width:2.2;opacity:1");
	    g.appendChild(optionalHeader);
	    
	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	    createText("Children-" + activityId, x +9, y +19*2+1, null, "start", "11", "Arial", "fill:#828990", childActivitiesSize + " - Activities", g);

	} else if (activity.getActivityTypeID().equals(Activity.FLOATING_ACTIVITY_TYPE)) {
	    // This is a support activity
	    
	    Element supportContainer = doc.createElementNS(SVG_NAMESPACE, "rect");
	    supportContainer.setAttributeNS(null, "x", Integer.toString(x));
	    supportContainer.setAttributeNS(null, "y", Integer.toString(y));
	    supportContainer.setAttributeNS(null, "width", Integer.toString(width));
	    supportContainer.setAttributeNS(null, "height", Integer.toString(height));
	    supportContainer.setAttributeNS(null, "style", "opacity:1;fill:#d0defd");
	    g.appendChild(supportContainer);

	    Element supportHeader = doc.createElementNS(SVG_NAMESPACE, "rect");
	    supportHeader.setAttributeNS(null, "x", Integer.toString(x +4));
	    supportHeader.setAttributeNS(null, "y", Integer.toString(y +5));
	    supportHeader.setAttributeNS(null, "width", Integer.toString(width -8));
	    supportHeader.setAttributeNS(null, "height", Integer.toString(CONTAINER_HEADER_HEIGHT));
	    supportHeader.setAttributeNS(null, "style", "fill:#A9C8FD;stroke:#E1F0FD;stroke-width:2.2;opacity:1");
	    g.appendChild(supportHeader);

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	    int supportActivityChildrenSize = node.getChildCount();
	    createText("Children-" + activityId, x +9, y +19*2+1, null, "start", "11", "Arial", "fill:#828990", supportActivityChildrenSize + " - Activities", g);

	} else {
	    // This is a tool activity
		
	    // if this activity is a children of a sequence activity, if it is, then we need to change its size
	    if (node.isOptionalSequenceActivityChild()) {
		ActivityTreeNode parentNode = (ActivityTreeNode) node.getParent();
		AuthoringActivityDTO grandParentActivity = parentNode.getParentActivity();
		x += (grandParentActivity.getxCoord() == null) ?  0 : grandParentActivity.getxCoord();
		y += (grandParentActivity.getyCoord() == null) ?  0 : grandParentActivity.getyCoord();
		text = null;
	    }
	    
	    String style = "stroke:black;stroke-width:0.8;opacity:1" + node.getActivityColor();

	    // if activity uses a grouping we need to add a second rect layer to show that it's grouped
	    if (activity.getApplyGrouping()) {
		createGroupingEffect("grouping-" + activityId, x, y, width, height, style, g);
	    }

	    // Create rect
	    Element activityRectangle = doc.createElementNS(SVG_NAMESPACE, "rect");
	    activityRectangle.setAttributeNS(null, "id", "act" + activityId);
	    activityRectangle.setAttributeNS(null, "x", Integer.toString(x));
	    activityRectangle.setAttributeNS(null, "y", Integer.toString(y));
	    activityRectangle.setAttributeNS(null, "width", width.toString());
	    activityRectangle.setAttributeNS(null, "height", height.toString());
	    activityRectangle.setAttributeNS(null, "style", style);
	    g.appendChild(activityRectangle);

	    // Create text label
	    if (text != null) {
		int xText = x + (width / 2);
		int yText = y + (height / 2) + 18;
		createText("TextElement-" + activityId, xText, yText, null, "middle", "11.4", "Verdana", null, text, g);
	    }

	    // Create image
	    int imageX = x + (width / 2) - 15;
	    int imageY = y + (height / 2) - 22;
	    if (node.isOptionalSequenceActivityChild()) {
		imageX += 2;
		imageY += 7;
	    }
	    String imagePath = activity.getLibraryActivityUIImage();
	    // if png_filename is empty then this is a grouping act:
	    String imageFileName;
	    if (StringUtils.isBlank(imagePath)) {
		imageFileName = "icon_grouping.png";
	    } else {
		imageFileName = FileUtil.getFileName(imagePath);
		imageFileName = imageFileName.replaceFirst(".swf$", ".png");
	    }
	    imageFileName = "http://lamscommunity.org/lamscentral/images/acts/" + imageFileName;
	    Element imageNode = doc.createElementNS(SVG_NAMESPACE, "image");
	    imageNode.setAttributeNS(null, "id", "image-" + activityId);
	    imageNode.setAttributeNS(null, "x", Integer.toString(imageX));
	    imageNode.setAttributeNS(null, "y", Integer.toString(imageY));
	    imageNode.setAttributeNS(SVG_NAMESPACE_XLINK, "xlink:href", imageFileName);
	    imageNode.setAttributeNS(null, "width", Integer.toString(30));
	    imageNode.setAttributeNS(null, "height", Integer.toString(30));
	    g.appendChild(imageNode);
	}

    }
    
    private void createRectangle(String id, double x, double y, Integer width, Integer height, String style, Element g) {

	if (style == null) {
	    style = "";
	}
	
	Element rectangle = doc.createElementNS(SVG_NAMESPACE, "rect");
	if (id != null) {	
	    rectangle.setAttributeNS(null, "id", id);
	}
	rectangle.setAttributeNS(null, "x", Double.toString(x));
	rectangle.setAttributeNS(null, "y", Double.toString(y));
	rectangle.setAttributeNS(null, "width", Double.toString(width));
	rectangle.setAttributeNS(null, "height", Double.toString(height));
	rectangle.setAttributeNS(null, "style", style);
	g.appendChild(rectangle);
    }    
    
    private void createGroupingEffect(String id, double x, double y, double width, double height, String style,
	    Element g) {

	Element groupingRectangle = doc.createElementNS(SVG_NAMESPACE, "rect");
	
	groupingRectangle.setAttributeNS(null, "id", id);
	groupingRectangle.setAttributeNS(null, "x", Double.toString(x + 4));
	groupingRectangle.setAttributeNS(null, "y", Double.toString(y + 4));
	groupingRectangle.setAttributeNS(null, "width", Double.toString(width));
	groupingRectangle.setAttributeNS(null, "height",Double.toString( height));
	groupingRectangle.setAttributeNS(null, "style", style + ";stroke:#3b3b3b;stroke-width:3");
	
	g.appendChild(groupingRectangle);
    }

    private void createText(String id, double x, double y, String dy, String textAnchor, String fontSize,
	    String fontFamily, String style, String text, Element g) {

	if (dy == null) {
	    dy = "0";
	}
	if (textAnchor == null) {
	    textAnchor = "start";
	}
	if (fontSize == null) {
	    fontSize = "11.4";
	}
	if (fontFamily == null) {
	    fontFamily = "Verdana";
	}
	
	//trim text to fit into container
	if (text.length() > 21) {
	    text = text.substring(0, 20);    
	}
	
	Element textNode = doc.createElementNS(SVG_NAMESPACE, "text");
	Node textContent = doc.createTextNode(text);
	textNode.appendChild(textContent);
	
	textNode.setAttributeNS(null, "id", id);
	textNode.setAttributeNS(null, "x", "" + x);
	textNode.setAttributeNS(null, "y", "" + y);
	textNode.setAttributeNS(null, "dy", dy);
	textNode.setAttributeNS(null, "text-anchor", textAnchor);
	textNode.setAttributeNS(null, "font-size", fontSize);
	textNode.setAttributeNS(null, "font-family", fontFamily);
	if (style != null) {
	    textNode.setAttributeNS(null, "style", style);
	}
	
	g.appendChild(textNode);
    }
    
    public static void main(String[] args) throws JDOMException, IOException {
	
	if (args.length != 1) {
	    System.err.println("Usage: java SVGGenerator fullFilePath");
	    System.exit(1);
	}
	
	String fullFilePath = args[0];
	
	// import learning design
	LearningDesignDTO learningDesign = (LearningDesignDTO) FileUtil.getObjectFromXML(null, fullFilePath);
	
        SVGGenerator svgGenerator = SVGGenerator.getInstance();
        svgGenerator.generateSvg(learningDesign);
        
//        // Stream out svg document to display
//        OutputFormat format = new OutputFormat(svgGenerator.getSVGDocument());
//        format.setLineWidth(65);
//        format.setIndenting(true);
//        format.setIndent(2);
//        Writer out = new StringWriter();
//        XMLSerializer serializer = new XMLSerializer(out, format);
//        serializer.serialize(svgGenerator.getSVGDocument());
//        System.out.println(out.toString());
        
        OutputFormat format = new OutputFormat(svgGenerator.getSVGDocument());
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
	// Create file
        String svgFileName = FileUtil.getFileName(fullFilePath);
        String fileExtension = FileUtil.getFileExtension(svgFileName);
        svgFileName = svgFileName.replaceFirst(fileExtension + "$", "svg");
        String svgFileFullPath = FileUtil.getFullPath(FileUtil.getFileDirectory(fullFilePath), svgFileName);
	FileWriter fstream = new FileWriter(svgFileFullPath);
	BufferedWriter out = new BufferedWriter(fstream);
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(svgGenerator.getSVGDocument());
        System.out.println("Creating a file " + svgFileFullPath );
	// Close the output stream
	out.close();
    }
    
}
