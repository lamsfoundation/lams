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

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
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
 * @author Andrey Balan
 */
public class SVGGenerator extends SVGConstants{
    
    public final static int OUTPUT_FORMAT_SVG  = 1;
    public final static int OUTPUT_FORMAT_PNG  = 2;
    public final static int OUTPUT_FORMAT_SYSTEM_OUT  = 3;
    
    private SVGDocument doc;
    private Integer adjustedDocumentWidth = null;
    
    /**
     * Sets up Svg root and defs. 
     */    
    private void initializeSvgDocument() {
	
        // Create an SVG document.
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        doc = (SVGDocument) impl.createDocument(SVG_NAMESPACE, "svg", null);
        // Get the root element (the 'svg' element).
        Element svgRoot = doc.getDocumentElement();
        // Set the width and height attributes on the root 'svg' element.
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
	
	// Create root g element
	Element g = doc.createElementNS(SVG_NAMESPACE, "g");
	g.setAttributeNS(null, "id", ROOT_ELEMENT_ID);
	svgRoot.appendChild(g);
    }
    
    public void adjustDocumentWidth(Integer width) {
	this.adjustedDocumentWidth = width;
    }
    
    public Dimension getDocumentWidthHeight() {
	
        Element svg = doc.getDocumentElement();
        String widthStr = svg.getAttributeNS(null, "width");
        String heightStr = svg.getAttributeNS(null, "height");
        
        int width = Integer.parseInt(widthStr);
        int height = Integer.parseInt(heightStr);
        
        return new Dimension(width, height);
    }
    
    /**
     * Generates SVG image based on learning design provided.
     * 
     * @param learningDesign
     * @throws JDOMException
     * @throws IOException
     */
    public void generateSvgDom(LearningDesignDTO learningDesign) throws JDOMException, IOException {
	initializeSvgDocument();

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
        Element svgRoot = doc.getElementById(ROOT_ELEMENT_ID);
        ArrayList<TransitionDTO> transitions = learningDesign.getTransitions();
        for (TransitionDTO transition : transitions) {
            
            ActivityTreeNode fromActivity = allNodes.get(transition.getFromActivityID());
            ActivityTreeNode toActivity = allNodes.get(transition.getToActivityID());
	    Point2D fromIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(fromActivity, toActivity);
	    Point2D toIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(toActivity, fromActivity);            
            
            //skip optional sequence's childs and lines between overlapped activities
            if (fromActivity.isOptionalSequenceActivityChild() 
        	    || (fromIntersection == null) || (toIntersection == null)) {
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
	    double yArrowShift = (a*a + b*b == 0) ? 0 : 5* b/Math.sqrt(a*a + b*b);
	    double xArrowShift = (a*a + b*b == 0) ? 0 : 5* a/Math.sqrt(a*a + b*b);
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
        
        setUpDocumentWidthHeight(allNodes.values());
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
	    
	    //in case of branching activity OR child of optional sequence activity OR optional actvity
	    //don't traverse child activities
	    if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		    || activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		    || activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)
		    || node.isOptionalSequenceActivityChild()
		    || node.isOptionalActivityChild()) {
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
	Element svgRoot = doc.getElementById(ROOT_ELEMENT_ID);
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

	// if this activity is a children of a sequence activity, if it is, then we need to change its size
	if (node.isOptionalSequenceActivityChild()) {
	    ActivityTreeNode parentNode = (ActivityTreeNode) node.getParent();
	    AuthoringActivityDTO grandParentActivity = parentNode.getParentActivity();
	    x += (grandParentActivity.getxCoord() == null) ? 0 : grandParentActivity.getxCoord();
	    y += (grandParentActivity.getyCoord() == null) ? 0 : grandParentActivity.getyCoord();
	    
	    //create activity's rectangle
	    createRectangle(node, x, y, g);

	    createImage(node, x, y, g);

	// this activity is a children of an optional activity
	} else if (node.isOptionalActivityChild()) {

	    //create activity's rectangle
	    createRectangle(node, x, y, g);

	    // Create text label
	    if (text != null) {
		int xText = x + (width / 2);
		int yText = y + (height / 2) + 18;
		createText("TextElement-" + activityId, xText, yText, null, "middle", "11.4", "Verdana", null, text, g);
	    }
	
	    createImage(node, x, y, g);
	    
	} else if (activity.getActivityTypeID().equals(Activity.SYNCH_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.SCHEDULE_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.PERMISSION_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.CONDITION_GATE_ACTIVITY_TYPE)) {
	    // if this is a stop gate we need to draw an octogon instead (and don't care about SYSTEM_GATE_ACTIVITY_TYPE)
	    x += 8;
	    y -= 2;

	    String finalProportions = "";
	    for (double[] proportion : GATE_PROPORTIONS) {
		finalProportions += (x + proportion[0]) + "," + (y + proportion[1]) + " ";
	    }

	    Element polygon = doc.createElementNS(SVG_NAMESPACE, "polygon");
	    polygon.setAttributeNS(null, "style", node.getActivityCss());
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

	    // if the parallel is grouped, show it
	    if (activity.getApplyGrouping()) {
		createGroupingEffect(node, x, y, g);
	    }
	    
	    //Create parallelContainer
	    createRectangle(node, x, y, g);
	    
	    createActvityHeader(node, x, y, g);
	    
	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	} else if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) {
	    // This is a branching activity

	    // if the parallel is grouped, show it
	    if (activity.getApplyGrouping()) {
		createGroupingEffect(node, x, y, g);
	    }
	    
	    //Create branchingContainer
	    createRectangle(node, x, y, g);
	    
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
	    
	    TreeSet<ActivityTreeNode> sequenceNodes = new TreeSet<ActivityTreeNode>(new ActivityTreeNodeComparator());
	    sequenceNodes.addAll(node.getChildren());
	    Iterator<ActivityTreeNode> sequenceNodeIterator = sequenceNodes.iterator();
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
	    
	    //Create optionalContainer
	    createRectangle(node, x, y, g);
	    
	    createActvityHeader(node, x, y, g);

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	    createText("Children-" + activityId, x +9, y +19*2+1, null, "start", "11", "Arial", "fill:#828990", node.getChildCount() + " - Sequences", g);

	} else if (activity.getActivityTypeID().equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {	
	    // This is a sequence within an optional
	    
	    //Create sequenceContainer
	    createRectangle(node, x, y, g);
	    
	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_ACTIVITY_TYPE)) {
	    // This is an optional activity
		
	    int childActivitiesSize = node.getChildCount();

	    // Create optionalContainer
	    createRectangle(node, x, y, g);
	    
	    createActvityHeader(node, x, y, g);

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	    createText("Children-" + activityId, x +9, y +19*2+1, null, "start", "11", "Arial", "fill:#828990", childActivitiesSize + " - Activities", g);

	} else if (activity.getActivityTypeID().equals(Activity.FLOATING_ACTIVITY_TYPE)) {
	    // This is a support activity

	    // Create supportContainer
	    createRectangle(node, x, y, g);
	    
	    createActvityHeader(node, x, y, g);

	    if (StringUtils.isNotEmpty(text)) {
		createText("TextElement-" + activityId, x +9, y +19, null, "start", "12", "Arial", "fill:#828990", text, g);
	    }
	    
	    int supportActivityChildrenSize = node.getChildCount();
	    createText("Children-" + activityId, x +9, y +19*2+1, null, "start", "11", "Arial", "fill:#828990", supportActivityChildrenSize + " - Activities", g);

	} else {
	    // This is a tool activity
		
	    // if activity uses a grouping we need to add a second rect layer to show that it's grouped
	    if (activity.getApplyGrouping()) {
		createGroupingEffect(node, x, y, g);
	    }

	    //create activity's rectangle
	    createRectangle(node, x, y, g);

	    // Create text label
	    if (text != null) {
		int xText = x + (width / 2);
		int yText = y + (height / 2) + 18;
		createText("TextElement-" + activityId, xText, yText, null, "middle", "11.4", "Verdana", null, text, g);
	    }

	    createImage(node, x, y, g);
	}

    }
    
    /**
     * Returns estimated width and height of the whole SVG document.
     * 
     * @param nodes
     * @param x
     * @param y
     * @param g
     * @return
     */
    private void setUpDocumentWidthHeight(Collection<ActivityTreeNode> nodes) {
	int maxX = 0;
	int maxY = 0;
	for (ActivityTreeNode node : nodes) {
	    Dimension dimension = node.getActivityDimension();
	    
	    int rightestActivityPoint = node.getActivityCoordinates().x + dimension.width;
	    if (rightestActivityPoint > maxX) {
		maxX = rightestActivityPoint;
	    }
	    int bottomActivityPoint = node.getActivityCoordinates().y + dimension.height;
	    if (bottomActivityPoint > maxY) {
		maxY = bottomActivityPoint;
	    }
	}
	
	int minX = Integer.MAX_VALUE;
	int minY = Integer.MAX_VALUE;
	for (ActivityTreeNode node : nodes) {
	    AuthoringActivityDTO activity = node.getActivity();
	    if (activity.getParentActivityID() == null) {
		int leftActivityPoint = node.getActivityCoordinates().x;
		if (leftActivityPoint < minX) {
		    minX = leftActivityPoint;
		}
		int topActivityPoint = node.getActivityCoordinates().y;
		if (topActivityPoint < minY) {
		    minY = topActivityPoint;
		}
	    }
	}
	
        Element svg = doc.getDocumentElement();
        
	//Removes padding of the SVG image.
        minX--;
        minY--;
        int width = maxX - minX +5;
        int height = maxY - minY +5;
        svg.setAttributeNS(null, "viewBox", minX + " " + minY + " " + Integer.toString(width) + " " + Integer.toString(height));
        
        // adjust width and height to adjustedDocumentWidth
        if ((adjustedDocumentWidth != null) && adjustedDocumentWidth < width) {
            double scale = (double) adjustedDocumentWidth / (double) width;
            width = adjustedDocumentWidth;
            height *= scale; 
        }
        
        //Sets the width and height
        svg.setAttributeNS(null, "width", Integer.toString(width));
        svg.setAttributeNS(null, "height", Integer.toString(height));
    }
    
    /**
     * Stream out SVG document into specified outputStream.
     * 
     * @param outputStream stream where we put resulted data. It can be null in case of RESULT_TYPE_DISPLAY
     * @param resultType one of SVGGenerator's constants: either RESULT_TYPE_SVG or RESULT_TYPE_PNG or RESULT_TYPE_DISPLAY
     * 
     * @throws TranscoderException
     * @throws IOException
     */
    public void streamOutDocument(OutputStream outputStream, int resultType) throws TranscoderException, IOException {
	
	switch (resultType) {
	case OUTPUT_FORMAT_SVG:
	    OutputFormat format = new OutputFormat(doc);
	    format.setLineWidth(65);
	    format.setIndenting(true);
	    format.setIndent(2);

	    XMLSerializer serializer = new XMLSerializer(outputStream, format);
	    serializer.serialize(doc);
	    outputStream.flush();
	    outputStream.close();
	    break;
	    
	case OUTPUT_FORMAT_PNG:
	    PNGTranscoder transcoder = new PNGTranscoder();
	    // Set the transcoder input and output.
	    TranscoderInput input = new TranscoderInput(doc);
	    TranscoderOutput output = new TranscoderOutput(outputStream);

	    // Perform the transcoding.
	    transcoder.transcode(input, output);
	    outputStream.flush();
	    outputStream.close();
	    break;
	    
//	// to see resulted SVG image in swing component     
//	case OUTPUT_FORMAT_DISPLAY:	    
//	    JSVGCanvas canvas = new JSVGCanvas();
//	    JFrame f = new JFrame();
//	    f.getContentPane().add(canvas);
//	    canvas.setSVGDocument(doc);
//	    f.pack();
//	    f.setSize(CANVAS_DEFAULT_WIDTH, CANVAS_DEFAULT_HEIGHT);
//	    f.setVisible(true);
//	    break;	    
	    
	default:
	    OutputFormat format2 = new OutputFormat(doc);
	    format2.setLineWidth(65);
	    format2.setIndenting(true);
	    format2.setIndent(2);
	    Writer out = new StringWriter();
	    XMLSerializer serializer2 = new XMLSerializer(out, format2);
	    serializer2.serialize(doc);
	    System.out.println(out.toString());
	}

    }
    
    //**************** Auxiliary methods for creating svg components ********************************************************
    
    private void createRectangle(ActivityTreeNode node, double x, double y, Element g) {
	AuthoringActivityDTO activity = node.getActivity();
	Dimension dimension = node.getActivityDimension();
	String style = node.getActivityCss();
	
	if (style == null) {
	    style = "";
	}
	
	Element rectangle = doc.createElementNS(SVG_NAMESPACE, "rect");
	if (activity.getActivityTypeID().equals(Activity.TOOL_ACTIVITY_TYPE)) {	
	    rectangle.setAttributeNS(null, "id", "act" + activity.getActivityID());
	}
	rectangle.setAttributeNS(null, "x", Double.toString(x));
	rectangle.setAttributeNS(null, "y", Double.toString(y));
	rectangle.setAttributeNS(null, "width", Double.toString(dimension.width));
	rectangle.setAttributeNS(null, "height", Double.toString(dimension.height));
	rectangle.setAttributeNS(null, "style", style);
	g.appendChild(rectangle);
    }
    
    private void createActvityHeader(ActivityTreeNode node, double x, double y, Element g) {
	AuthoringActivityDTO activity = node.getActivity();
	Dimension dimension = node.getActivityDimension();
	double height = (activity.getActivityTypeID().equals(Activity.PARALLEL_ACTIVITY_TYPE)) ? 23 : CONTAINER_HEADER_HEIGHT; 
	
	Element rectangle = doc.createElementNS(SVG_NAMESPACE, "rect");
	rectangle.setAttributeNS(null, "x", Double.toString(x +4));
	rectangle.setAttributeNS(null, "y", Double.toString(y +5));
	rectangle.setAttributeNS(null, "width", Double.toString(dimension.width -8));
	rectangle.setAttributeNS(null, "height", Double.toString(height));
	rectangle.setAttributeNS(null, "style", SVGConstants.CSS_CONTAINER);
	g.appendChild(rectangle);
    }    
    
    private void createGroupingEffect(ActivityTreeNode node, double x, double y, Element g) {
	AuthoringActivityDTO activity = node.getActivity();
	Dimension dimension = node.getActivityDimension();
	String style = node.getActivityCss();
	
	Element groupingRectangle = doc.createElementNS(SVG_NAMESPACE, "rect");
	groupingRectangle.setAttributeNS(null, "id", "grouping-" + activity.getActivityID());
	groupingRectangle.setAttributeNS(null, "x", Double.toString(x + 4));
	groupingRectangle.setAttributeNS(null, "y", Double.toString(y + 4));
	groupingRectangle.setAttributeNS(null, "width", Double.toString(dimension.width));
	groupingRectangle.setAttributeNS(null, "height",Double.toString(dimension.height));
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
    
    /**
     * Creates image for a tool.
     * 
     * @param node
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param g
     */
    private void createImage(ActivityTreeNode node, int x, int y, Element g) {

	AuthoringActivityDTO activity = node.getActivity();
	Dimension dimension = node.getActivityDimension();
	
	// Create image
	int imageX = x + (dimension.width / 2) - 15;
	int imageY = y + (dimension.height / 2) - 22;
	if (node.isOptionalSequenceActivityChild()) {
	    imageX += 2;
	    imageY += 7;
	}
	String imagePath = activity.getLibraryActivityUIImage();
	// if png_filename is empty then this is a grouping act:
	String imageFileName;
	if (! StringUtils.isBlank(imagePath)) {
	    imageFileName = FileUtil.getFileName(imagePath);
	    imageFileName = imageFileName.replaceFirst(".swf$", ".png");
	} else if (activity.getActivityTypeID().equals(Activity.SYNCH_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.SCHEDULE_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.PERMISSION_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.CONDITION_GATE_ACTIVITY_TYPE)) { 
	    imageFileName = "icon_gate.png";
    	} else if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) { 
	    imageFileName = "icon_branching.png";
    	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_WITH_SEQUENCES_TYPE)
		|| activity.getActivityTypeID().equals(Activity.OPTIONS_ACTIVITY_TYPE)) { 
	    imageFileName = "icon_urlcontentmessageboard.png";
    	} else {
	    imageFileName = "icon_grouping.png";
	}
	imageFileName = "http://lamscommunity.org/lamscentral/images/acts/" + imageFileName;
	Element imageNode = doc.createElementNS(SVG_NAMESPACE, "image");
	imageNode.setAttributeNS(null, "id", "image-" + activity.getActivityID());
	imageNode.setAttributeNS(null, "x", Integer.toString(imageX));
	imageNode.setAttributeNS(null, "y", Integer.toString(imageY));
	imageNode.setAttributeNS(SVG_NAMESPACE_XLINK, "xlink:href", imageFileName);
	imageNode.setAttributeNS(null, "width", Integer.toString(30));
	imageNode.setAttributeNS(null, "height", Integer.toString(30));
	g.appendChild(imageNode);
    }
    
}
