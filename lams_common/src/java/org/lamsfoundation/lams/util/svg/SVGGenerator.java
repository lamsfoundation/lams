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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityDTOOrderComparator;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.svg.SVGDocument;

/**
 * Generates SVG document based on exported learning design's xml file.
 * 
 * @author Andrey Balan
 */
public class SVGGenerator {
    private static class BranchDTO extends LinkedList<ActivityTreeNode> {
	private Long sequenceActivityId;
	private Boolean stopAfterActivity;
	private String branchTitle;
    }

    public final static int OUTPUT_FORMAT_SVG = 1;
    public final static int OUTPUT_FORMAT_PNG = 2;
    public final static int OUTPUT_FORMAT_SYSTEM_OUT = 3;
    public final static int OUTPUT_FORMAT_SVG_LAMS_COMMUNITY = 4;

    private SVGDocument doc;
    private Integer outputFormat;
    private Integer adjustedDocumentWidth;
    private final String localSvgIconsPath = Configuration.get(ConfigurationKeys.SERVER_URL) + "images/svg/";

    // fille only for branching DOM
    private Point branchingStartPoint;
    private Point branchingEndPoint;

    private static final Logger log = Logger.getLogger(SVGGenerator.class);
    // tools used in all instances
    private static final DOMImplementation svgDOMImplementation = SVGDOMImplementation.getDOMImplementation();
    private static final PNGTranscoder pngTranscoder = new PNGTranscoder();
    private static DOMImplementationLS lsDOMImplementation;
    private static LSSerializer serializer;

    static {
	try {
	    // initialize common tools
	    DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
	    SVGGenerator.lsDOMImplementation = (DOMImplementationLS) registry.getDOMImplementation("XML 1.0 LS");

	    SVGGenerator.serializer = SVGGenerator.lsDOMImplementation.createLSSerializer();
	    SVGGenerator.serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
	} catch (Exception e) {
	    SVGGenerator.log.error(e);
	}
    }

    /**
     * Adjusts resulted SVG with user-specific width
     * 
     * @param width
     *            user specific width
     */
    public void adjustDocumentWidth(Integer width) {
	this.adjustedDocumentWidth = width;
    }

    /**
     * @return actual width and height of resulted image
     */
    public Dimension getDocumentWidthHeight() {
	Element svg = doc.getDocumentElement();
	String widthStr = svg.getAttributeNS(null, "width");
	String heightStr = svg.getAttributeNS(null, "height");

	int width = Integer.parseInt(widthStr);
	int height = Integer.parseInt(heightStr);

	return new Dimension(width, height);
    }

    /**
     * Stream out SVG document into specified outputStream.
     * 
     * @param outputStream
     *            stream where we put resulted data. It can be null in case of RESULT_TYPE_DISPLAY
     * @param outputFormat
     *            one of SVGGenerator's constants: either RESULT_TYPE_SVG or RESULT_TYPE_PNG or RESULT_TYPE_DISPLAY
     * 
     * @throws TranscoderException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws ClassCastException
     */
    public void streamOutDocument(OutputStream outputStream) throws IOException {
	if (SVGGenerator.OUTPUT_FORMAT_PNG == outputFormat) {
	    // Set the transcoder input and output.
	    TranscoderInput input = new TranscoderInput(doc);
	    TranscoderOutput output = new TranscoderOutput(outputStream);

	    // Perform the transcoding.

	    try {
		SVGGenerator.pngTranscoder.transcode(input, output);
	    } catch (TranscoderException e) {
		throw new IOException("Error while transcoding SVG into PNG", e);
	    }
	} else {
	    LSOutput output = SVGGenerator.lsDOMImplementation.createLSOutput();
	    output.setEncoding("UTF-8");
	    output.setByteStream((SVGGenerator.OUTPUT_FORMAT_SVG == outputFormat)
		    || (SVGGenerator.OUTPUT_FORMAT_SVG_LAMS_COMMUNITY == outputFormat) ? outputStream : System.out);
	    SVGGenerator.serializer.write(doc, output);
	}

	if (outputStream != null) {
	    outputStream.flush();
	    outputStream.close();
	}
    }

    /**
     * Generates SVG image based on learning design provided.
     * 
     * @param learningDesign
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void generateLearningDesignDOM(LearningDesignDTO learningDesign, int outputFormat) throws IOException {
	this.outputFormat = outputFormat;
	initializeSvgDocument();

	ActivityTreeNode root = new ActivityTreeNode();
	Map<Long, ActivityTreeNode> allNodes = getActivityTree(learningDesign.getActivities(), root);

	// **************** Draw transitions********************************************************

	ArrayList<TransitionDTO> transitions = learningDesign.getTransitions();
	createActivityTransitionLines(allNodes, transitions);

	// **************** Draw activities ********************************************************
	treeTraverse(root);

	setUpDocumentWidthHeight(allNodes.values());
    }

    /**
     * Generates SVG image based on branching activity provided.
     */
    @SuppressWarnings("unchecked")
    public void generateBranchingDOM(LearningDesignDTO learningDesign, long branchingActivityId, int outputFormat)
	    throws IOException {
	this.outputFormat = outputFormat;
	initializeSvgDocument();

	ActivityTreeNode root = new ActivityTreeNode();
	List<BranchDTO> branches = new ArrayList<BranchDTO>();
	Map<Long, ActivityTreeNode> allNodes = getActivityTree(learningDesign.getActivities(), root,
		branchingActivityId, branches);

	// get only transitions between activities inside the given branching
	Set<TransitionDTO> interActivityTransitions = new HashSet<TransitionDTO>();
	Set<Long> allActivityIDs = allNodes.keySet();
	for (TransitionDTO transition : (ArrayList<TransitionDTO>) learningDesign.getTransitions()) {
	    if (allActivityIDs.contains(transition.getFromActivityID())
		    || allActivityIDs.contains(transition.getToActivityID())) {
		interActivityTransitions.add(transition);
	    }
	}

	// **************** Draw transitions********************************************************
	// draw initial lines
	createBranchingTransitionLines(branches, root);

	createActivityTransitionLines(allNodes, interActivityTransitions);

	// **************** Draw activities ********************************************************
	treeTraverse(root);

	setUpDocumentWidthHeight(allNodes.values());
    }

    /**
     * Sets up Svg root and defs.
     */
    private void initializeSvgDocument() {
	// Create an SVG document.
	doc = (SVGDocument) SVGGenerator.svgDOMImplementation.createDocument(SVGConstants.SVG_NAMESPACE, "svg", null);
	// Get the root element (the 'svg' element).
	Element svgRoot = doc.getDocumentElement();

	// create arrow definition
	Element defs = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "defs");
	svgRoot.appendChild(defs);
	Element marker = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "marker");
	marker.setAttributeNS(null, "id", "Triangle");
	marker.setAttributeNS(null, "viewBox", "0 0 10 10");
	marker.setAttributeNS(null, "refX", "0");
	marker.setAttributeNS(null, "refY", "5");
	marker.setAttributeNS(null, "markerUnits", "strokeWidth");
	marker.setAttributeNS(null, "markerWidth", "6");
	marker.setAttributeNS(null, "markerHeight", "5");
	marker.setAttributeNS(null, "orient", "auto");
	defs.appendChild(marker);
	Element path = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "path");
	path.setAttributeNS(null, "d", "M 0 0 L 10 5 L 0 10 z");
	marker.appendChild(path);

	// Create root g element
	Element g = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "g");
	g.setAttributeNS(null, "id", SVGConstants.ROOT_ELEMENT_ID);
	svgRoot.appendChild(g);
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

	// draw root's activity, unless it's the start root which doesn't contain activity
	if (activity != null) {
	    createActivity(node);

	    // in case of branching activity OR child of optional sequence activity OR optional actvity
	    // don't traverse child activities
	    if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		    || activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		    || activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)
		    || node.isOptionalSequenceActivityChild() || node.isOptionalActivityChild()) {
		return;
	    }
	}

	// traverse child subtrees
	for (ActivityTreeNode child : node.getChildren()) {
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
	Element g = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "g");
	String activityId = activity.getActivityID().toString();
	g.setAttributeNS(null, "id", activityId);
	String parentID = (activity.getParentActivityID() == null) ? "0" : activity.getParentActivityID().toString();
	g.setAttributeNS(null, "parentID", parentID);
	// Attach the g element to the root 'svg' element.
	Element svgRoot = doc.getElementById(SVGConstants.ROOT_ELEMENT_ID);
	svgRoot.appendChild(g);

	int x = node.getActivityCoordinates().x;
	int y = node.getActivityCoordinates().y;
	// activities with parents (paralles, optionals, branching, etc)
	if (activity.getParentActivityID() != null) {
	    AuthoringActivityDTO parentActivity = node.getParentActivity();
	    x += (parentActivity.getxCoord() == null) ? 0 : parentActivity.getxCoord();
	    y += (parentActivity.getyCoord() == null) ? 0 : parentActivity.getyCoord();
	}

	// if this activity is a children of a sequence activity, if it is, then we need to change its size
	if (node.isOptionalSequenceActivityChild()) {
	    createOptionalSequenceActivityChild(node, g, x, y);

	    // this activity is a children of an optional activity
	} else if (node.isOptionalActivityChild()) {
	    createOptionalActivityChild(activity, node, g, x, y);

	} else if (activity.getActivityTypeID().equals(Activity.SYNCH_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.SCHEDULE_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.PERMISSION_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.CONDITION_GATE_ACTIVITY_TYPE)) {
	    createGateActivity(activity, node, g, x, y);

	} else if (activity.getActivityTypeID().equals(Activity.PARALLEL_ACTIVITY_TYPE)) {
	    // This is a parallel activity
	    createParallelActivity(activity, node, g, x, y);

	} else if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) {
	    // This is a branching activity
	    createBranchingActivity(activity, node, g, x, y);

	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_WITH_SEQUENCES_TYPE)) {
	    // This is an optional sequence
	    createOptionalSequenceActivity(activity, node, g, x, y);

	} else if (activity.getActivityTypeID().equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {
	    // This is a sequence within an optional

	    // Create sequenceContainer
	    createRectangle(node, x, y, g);

	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_ACTIVITY_TYPE)) {
	    // This is an optional activity
	    createOptionalActivity(activity, node, g, x, y);

	} else if (activity.getActivityTypeID().equals(Activity.FLOATING_ACTIVITY_TYPE)) {
	    // This is a support activity
	    createSupportActivity(activity, node, g, x, y);

	} else {
	    // This is a tool activity
	    createToolActivity(activity, node, g, x, y);
	}
    }

    private void createOptionalActivityChild(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x,
	    int y) {
	// create activity's rectangle
	createRectangle(node, x, y, g);

	// Create text label

	String text = activity.getActivityTitle();
	if (text != null) {
	    Integer width = node.getActivityDimension().width;
	    Integer height = node.getActivityDimension().height;
	    int xText = x + (width / 2);
	    int yText = y + (height / 2) + 18;
	    createText("TextElement-" + activity.getActivityID(), xText, yText, "middle", "11.4", null, null, text, g);
	}

	createImage(node, x, y, g);
    }

    private void createOptionalSequenceActivityChild(ActivityTreeNode node, Element g, int x, int y) {
	ActivityTreeNode parentNode = (ActivityTreeNode) node.getParent();
	AuthoringActivityDTO grandParentActivity = parentNode.getParentActivity();
	x += (grandParentActivity.getxCoord() == null) ? 0 : grandParentActivity.getxCoord();
	y += (grandParentActivity.getyCoord() == null) ? 0 : grandParentActivity.getyCoord();

	// create activity's rectangle
	createRectangle(node, x, y, g);

	createImage(node, x, y, g);
    }

    private void createGateActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x, int y) {
	// if this is a stop gate we need to draw an octogon instead (and don't care about
	// SYSTEM_GATE_ACTIVITY_TYPE)
	x += 8;
	y -= 2;

	String finalProportions = "";
	for (double[] proportion : SVGConstants.GATE_PROPORTIONS) {
	    finalProportions += (x + proportion[0]) + "," + (y + proportion[1]) + " ";
	}

	Element polygon = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "polygon");
	polygon.setAttributeNS(null, "style", node.getActivityCss());
	polygon.setAttributeNS(null, "points", finalProportions);
	g.appendChild(polygon);

	// calculate midpoint for STOP text
	double x1 = x + SVGConstants.GATE_PROPORTIONS[6][0];
	double x2 = x + SVGConstants.GATE_PROPORTIONS[2][0];
	double midpointX = (x1 + x2) / 2;

	double y1 = y + SVGConstants.GATE_PROPORTIONS[6][1];
	double y2 = y + SVGConstants.GATE_PROPORTIONS[2][1];
	double midpointY = ((y1 + y2) / 2) + 3;

	createText("Gate_" + activity.getActivityID(), midpointX, midpointY, "middle", "10", null,
		"fill:#FFFFFF;stroke:#FFFFFF;stroke-width:.5;", "STOP", g);
    }

    private void createParallelActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x, int y) {
	// if the parallel is grouped, show it
	if (activity.getApplyGrouping()) {
	    createGroupingEffect(node, x, y, g);
	}

	// Create parallelContainer
	createRectangle(node, x, y, g);

	createActvityHeader(node, x, y, g);

	String text = activity.getActivityTitle();
	if (StringUtils.isNotEmpty(text)) {
	    createText("TextElement-" + activity.getActivityID(), x + 9, y + 19, "start", "12", "Arial",
		    "fill:#828990", text, g);
	}
    }

    private void createSupportActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x, int y) {
	// Create supportContainer
	createRectangle(node, x, y, g);

	createActvityHeader(node, x, y, g);

	String text = activity.getActivityTitle();
	if (StringUtils.isNotEmpty(text)) {
	    createText("TextElement-" + activity.getActivityID(), x + 9, y + 19, "start", "12", "Arial",
		    "fill:#828990", text, g);
	}

	int supportActivityChildrenSize = node.getChildCount();
	createText("Children-" + activity.getActivityID(), x + 9, y + (19 * 2) + 1, "start", "11", "Arial",
		"fill:#828990", supportActivityChildrenSize + " - Activities", g);
    }

    private void createToolActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x, int y) {
	// if activity uses a grouping we need to add a second rect layer to show that it's grouped
	if (activity.getApplyGrouping()) {
	    createGroupingEffect(node, x, y, g);
	}

	// create activity's rectangle
	createRectangle(node, x, y, g);

	// Create text label
	String text = activity.getActivityTitle();
	if (StringUtils.isNotEmpty(text)) {
	    Integer width = node.getActivityDimension().width;
	    Integer height = node.getActivityDimension().height;
	    int xText = x + (width / 2);
	    int yText = y + (height / 2) + 18;
	    createText("TextElement-" + activity.getActivityID(), xText, yText, "middle", "11.4", null, null, text, g);
	}

	createImage(node, x, y, g);
    }

    private void createOptionalActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x, int y) {
	int childActivitiesSize = node.getChildCount();

	// Create optionalContainer
	createRectangle(node, x, y, g);

	createActvityHeader(node, x, y, g);

	String text = activity.getActivityTitle();
	if (StringUtils.isNotEmpty(text)) {
	    createText("TextElement-" + activity.getActivityID(), x + 9, y + 19, "start", "12", "Arial",
		    "fill:#828990", text, g);
	}

	createText("Children-" + activity.getActivityID(), x + 9, y + (19 * 2) + 1, "start", "11", "Arial",
		"fill:#828990", childActivitiesSize + " - Activities", g);
    }

    private void createOptionalSequenceActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x,
	    int y) {
	// Create optionalContainer
	createRectangle(node, x, y, g);

	createActvityHeader(node, x, y, g);

	String text = activity.getActivityTitle();
	if (StringUtils.isNotEmpty(text)) {
	    createText("TextElement-" + activity.getActivityID(), x + 9, y + 19, "start", "12", "Arial",
		    "fill:#828990", text, g);
	}

	createText("Children-" + activity.getActivityID(), x + 9, y + (19 * 2) + 1, "start", "11", "Arial",
		"fill:#828990", node.getChildCount() + " - Sequences", g);
    }

    private void createBranchingActivity(AuthoringActivityDTO activity, ActivityTreeNode node, Element g, int x, int y) {
	// if the parallel is grouped, show it
	if (activity.getApplyGrouping()) {
	    createGroupingEffect(node, x, y, g);
	}

	// Create branchingContainer
	createRectangle(node, x, y, g);

	int startingPointX = x + 26;
	int startingPointY = y + 40;
	if (node.getChildCount() == 4) {
	    startingPointY -= 12;
	} else if (node.getChildCount() > 4) {
	    startingPointY -= 2 * 12;
	}

	Element startingPoint = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "rect");
	startingPoint.setAttributeNS(null, "x", Integer.toString(startingPointX));
	startingPoint.setAttributeNS(null, "y", Integer.toString(startingPointY));
	startingPoint.setAttributeNS(null, "width", Double.toString(SVGConstants.BRANCHING_ACTIVITY_POINT + 0.5));
	startingPoint.setAttributeNS(null, "height", Double.toString(SVGConstants.BRANCHING_ACTIVITY_POINT + 0.5));
	startingPoint.setAttributeNS(null, "style", "fill:#000000");
	g.appendChild(startingPoint);

	// sort sequences with their order
	TreeSet<ActivityTreeNode> sequenceNodeSet = new TreeSet<ActivityTreeNode>(new ActivityTreeNodeComparator());
	sequenceNodeSet.addAll(node.getChildren());
	LinkedList<ActivityTreeNode> sequenceNodeList = new LinkedList<ActivityTreeNode>();
	// empty branch (if exists) must be first
	for (ActivityTreeNode sequenceNode : sequenceNodeSet) {
	    if (sequenceNode.getChildCount() == 0) {
		sequenceNodeList.addFirst(sequenceNode);
	    } else {
		sequenceNodeList.add(sequenceNode);
	    }
	}

	int sequenceIndex = 0;

	for (ActivityTreeNode sequenceNode : sequenceNodeList) {
	    if (Math.abs(sequenceIndex) >= 3) {
		// no more than 5 branches in total
		break;
	    }

	    double previousActivityPointX = startingPointX + (SVGConstants.BRANCHING_ACTIVITY_POINT / 2);
	    double previousActivityPointY = startingPointY + (SVGConstants.BRANCHING_ACTIVITY_POINT / 2);

	    // Create the lines
	    Iterator<ActivityTreeNode> activityNodeIterator = sequenceNode.getChildren().iterator();
	    for (int activityIndex = 1; activityNodeIterator.hasNext() && (activityIndex <= 6); activityIndex++, activityNodeIterator
		    .next()) {
		double activityPointX = startingPointX + (activityIndex * SVGConstants.BRANCHING_STEP)
			+ (SVGConstants.BRANCHING_ACTIVITY_POINT / 2);
		double activityPointY = startingPointY + (sequenceIndex * SVGConstants.BRANCHING_STEP)
			+ (SVGConstants.BRANCHING_ACTIVITY_POINT / 2);

		Element line = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "line");
		line.setAttributeNS(null, "x1", Double.toString(previousActivityPointX));
		line.setAttributeNS(null, "y1", Double.toString(previousActivityPointY));
		line.setAttributeNS(null, "x2", Double.toString(activityPointX));
		line.setAttributeNS(null, "y2", Double.toString(activityPointY));
		line.setAttributeNS(null, "style", "stroke:black;stroke-width:1;");
		g.appendChild(line);

		previousActivityPointX = activityPointX;
		previousActivityPointY = activityPointY;
	    }

	    // check if we need to draw line connecting last activity with endingPoint
	    if (!sequenceNode.getActivity().getStopAfterActivity()) {
		Element line = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "line");
		line.setAttributeNS(null, "x1", Double.toString(previousActivityPointX));
		line.setAttributeNS(null, "y1", Double.toString(previousActivityPointY));
		line.setAttributeNS(null, "x2", Double.toString(x + 132 + (SVGConstants.BRANCHING_ACTIVITY_POINT / 2)));
		line.setAttributeNS(null, "y2",
			Double.toString(startingPointY + (SVGConstants.BRANCHING_ACTIVITY_POINT / 2)));
		line.setAttributeNS(null, "style", "stroke:black;stroke-width:1;");
		g.appendChild(line);

	    }

	    // Create activity points
	    activityNodeIterator = sequenceNode.getChildren().iterator();
	    for (int activityIndex = 1; activityNodeIterator.hasNext() && (activityIndex <= 6); activityIndex++) {
		ActivityTreeNode activityNode = activityNodeIterator.next();
		String activityStyle = sequenceNode.getActivity().getStopAfterActivity()
			&& !activityNodeIterator.hasNext() ? "stroke:red" : "stroke:black";
		activityStyle += ";stroke-width:0.8;opacity:1" + activityNode.getActivityColor();
		double activityPointX = startingPointX + (activityIndex * SVGConstants.BRANCHING_STEP);
		double activityPointY = startingPointY + (sequenceIndex * SVGConstants.BRANCHING_STEP);

		Element activityPoint = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "rect");
		activityPoint.setAttributeNS(null, "x", Double.toString(activityPointX));
		activityPoint.setAttributeNS(null, "y", Double.toString(activityPointY));
		activityPoint.setAttributeNS(null, "width", "" + SVGConstants.BRANCHING_ACTIVITY_POINT);
		activityPoint.setAttributeNS(null, "height", "" + SVGConstants.BRANCHING_ACTIVITY_POINT);
		activityPoint.setAttributeNS(null, "style", activityStyle);
		g.appendChild(activityPoint);
	    }

	    // it goes 0, -1, 1, -2, 2
	    if (sequenceIndex >= 0) {
		sequenceIndex++;
	    }
	    sequenceIndex *= -1;
	}

	Element endingPoint = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "rect");
	endingPoint.setAttributeNS(null, "x", Integer.toString(x + 132));
	endingPoint.setAttributeNS(null, "y", Integer.toString(startingPointY));
	endingPoint.setAttributeNS(null, "width", Double.toString(SVGConstants.BRANCHING_ACTIVITY_POINT + 0.5));
	endingPoint.setAttributeNS(null, "height", Double.toString(SVGConstants.BRANCHING_ACTIVITY_POINT + 0.5));
	endingPoint.setAttributeNS(null, "style", "fill:#000000");
	g.appendChild(endingPoint);

	String text = activity.getActivityTitle();
	if (StringUtils.isNotEmpty(text)) {
	    createText("TextElement-" + activity.getActivityID(), x + (SVGConstants.BRANCHING_ACTIVITY_WIDTH / 2),
		    y + 90, "middle", "11.4", null, null, text, g);
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

	for (Point branchingEdgePoint : new Point[] { branchingStartPoint, branchingEndPoint }) {
	    if (branchingEdgePoint != null) {
		int x = new Double(branchingEdgePoint.getX()).intValue();
		if ((x + 27) > maxX) {
		    maxX = x + 27;
		}

		if (x < minX) {
		    minX = x;
		}

		int y = new Double(branchingEdgePoint.getY()).intValue();
		if ((y + 27) > maxY) {
		    maxY = y + 27;
		}

		if (y < minY) {
		    minY = y;
		}
	    }
	}

	Element svg = doc.getDocumentElement();

	// Removes padding of the SVG image.
	minX--;
	minY--;
	int width = (maxX - minX) + 5;
	int height = (maxY - minY) + 5;
	svg.setAttributeNS(null, "viewBox",
		minX + " " + minY + " " + Integer.toString(width) + " " + Integer.toString(height));

	// adjust width and height to adjustedDocumentWidth
	if ((adjustedDocumentWidth != null) && (adjustedDocumentWidth < width)) {
	    double scale = (double) adjustedDocumentWidth / (double) width;
	    width = adjustedDocumentWidth;
	    height *= scale;
	}

	// Sets the width and height
	svg.setAttributeNS(null, "width", Integer.toString(width));
	svg.setAttributeNS(null, "height", Integer.toString(height));
    }

    // **************** Auxiliary methods for creating svg components
    // ********************************************************

    private void createRectangle(ActivityTreeNode node, double x, double y, Element g) {
	AuthoringActivityDTO activity = node.getActivity();
	Dimension dimension = node.getActivityDimension();
	String style = node.getActivityCss();

	if (style == null) {
	    style = "";
	}

	Element rectangle = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "rect");

	rectangle.setAttributeNS(null, "id", "act" + activity.getActivityID());
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
	double height = (activity.getActivityTypeID().equals(Activity.PARALLEL_ACTIVITY_TYPE)) ? 23
		: SVGConstants.CONTAINER_HEADER_HEIGHT;

	Element rectangle = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "rect");
	rectangle.setAttributeNS(null, "x", Double.toString(x + 4));
	rectangle.setAttributeNS(null, "y", Double.toString(y + 5));
	rectangle.setAttributeNS(null, "width", Double.toString(dimension.width - 8));
	rectangle.setAttributeNS(null, "height", Double.toString(height));
	rectangle.setAttributeNS(null, "style", SVGConstants.CSS_CONTAINER);
	g.appendChild(rectangle);
    }

    private void createGroupingEffect(ActivityTreeNode node, double x, double y, Element g) {
	AuthoringActivityDTO activity = node.getActivity();
	Dimension dimension = node.getActivityDimension();
	String style = node.getActivityCss();

	Element groupingRectangle = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "rect");
	groupingRectangle.setAttributeNS(null, "id", "grouping-" + activity.getActivityID());
	groupingRectangle.setAttributeNS(null, "x", Double.toString(x + 4));
	groupingRectangle.setAttributeNS(null, "y", Double.toString(y + 4));
	groupingRectangle.setAttributeNS(null, "width", Double.toString(dimension.width));
	groupingRectangle.setAttributeNS(null, "height", Double.toString(dimension.height));
	groupingRectangle.setAttributeNS(null, "style", style + ";stroke:#3b3b3b;stroke-width:3");

	g.appendChild(groupingRectangle);
    }

    private void createText(String id, double x, double y, String textAnchor, String fontSize, String fontFamily,
	    String style, String text, Element g) {
	if (textAnchor == null) {
	    textAnchor = "start";
	}
	if (fontSize == null) {
	    fontSize = "11.4";
	}
	if (fontFamily == null) {
	    fontFamily = "Verdana";
	}

	// trim text to fit into container
	if (text.length() > 20) {
	    text = text.substring(0, 19);
	}

	Element textNode = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "text");
	Node textContent = doc.createTextNode(text);
	textNode.appendChild(textContent);

	textNode.setAttributeNS(null, "id", id);
	textNode.setAttributeNS(null, "x", "" + x);
	textNode.setAttributeNS(null, "y", "" + y);
	textNode.setAttributeNS(null, "dy", "0");
	textNode.setAttributeNS(null, "text-anchor", textAnchor);
	textNode.setAttributeNS(null, "font-size", fontSize);
	textNode.setAttributeNS(null, "font-family", fontFamily);
	if (style != null) {
	    textNode.setAttributeNS(null, "style", style);
	}
	textNode.setAttributeNS(null, "stroke", "#3b3b3b");
	textNode.setAttributeNS(null, "stroke-width", "0.01");

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
	int imageX = (x + (dimension.width / 2)) - 15;
	int imageY = (y + (dimension.height / 2)) - 22;
	if (node.isOptionalSequenceActivityChild()) {
	    imageX += 2;
	    imageY += 7;
	}
	String imagePath = activity.getLibraryActivityUIImage();
	if (!StringUtils.isBlank(imagePath) && imagePath.endsWith("svg")) {
	    if (SVGGenerator.log.isDebugEnabled()) {
		SVGGenerator.log.debug("SVG image rendering is not supported at the moment. Skipping: " + imagePath);
	    }
	    return;
	}

	// if png_filename is empty then this is a grouping act:
	String imageFileName = null;
	if (!StringUtils.isBlank(imagePath)) {
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

	imagePath = (SVGGenerator.OUTPUT_FORMAT_SVG_LAMS_COMMUNITY == outputFormat ? SVGConstants.PATH_TO_LAMSCOMMUNITY_SVG_IMAGES
		: localSvgIconsPath)
		+ imageFileName;

	String imageId = "image-" + activity.getActivityID();
	createImage(g, imagePath, imageId, imageX, imageY);
    }

    private void createImage(Element g, String imageFileName, String id, int x, int y) {
	Element imageNode = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "image");
	imageNode.setAttributeNS(null, "id", id);
	imageNode.setAttributeNS(null, "x", Integer.toString(x));
	imageNode.setAttributeNS(null, "y", Integer.toString(y));
	imageNode.setAttributeNS(SVGConstants.SVG_NAMESPACE_XLINK, "xlink:href", imageFileName);
	imageNode.setAttributeNS(null, "width", Integer.toString(30));
	imageNode.setAttributeNS(null, "height", Integer.toString(30));
	if (g == null) {
	    Element svgRoot = doc.getDocumentElement();
	    svgRoot.appendChild(imageNode);
	} else {
	    g.appendChild(imageNode);
	}
    }

    private void createTransitionLine(String id, Point2D fromIntersection, Point2D toIntersection, String branchTitle,
	    String chosenColor) {
	// Create the line
	String color = chosenColor == null ? "#8C8FA6" : chosenColor;
	Element line = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "line");
	line.setAttributeNS(null, "id", id);
	line.setAttributeNS(null, "x1", Double.toString(fromIntersection.getX()));
	line.setAttributeNS(null, "y1", Double.toString(fromIntersection.getY()));
	line.setAttributeNS(null, "x2", Double.toString(toIntersection.getX()));
	line.setAttributeNS(null, "y2", Double.toString(toIntersection.getY()));
	line.setAttributeNS(null, "style", "stroke:" + color + ";stroke-width:2;opacity:1");
	line.setAttributeNS(null, "parentID", "0");

	double a = toIntersection.getX() - fromIntersection.getX();
	double b = toIntersection.getY() - fromIntersection.getY();
	double arrowShiftX = (((a * a) + (b * b)) == 0) ? 0 : (5 * a) / Math.sqrt((a * a) + (b * b));
	double arrowShiftY = (((a * a) + (b * b)) == 0) ? 0 : (5 * b) / Math.sqrt((a * a) + (b * b));
	double arrowEndX = ((fromIntersection.getX() + toIntersection.getX()) / 2) - arrowShiftX;
	double arrowEndY = ((fromIntersection.getY() + toIntersection.getY()) / 2) - arrowShiftY;
	// Create the arrowhead
	Element arrowhead = doc.createElementNS(SVGConstants.SVG_NAMESPACE, "line");
	arrowhead.setAttributeNS(null, "id", "arrowhead_" + id);
	arrowhead.setAttributeNS(null, "x1", Double.toString(fromIntersection.getX()));
	arrowhead.setAttributeNS(null, "y1", Double.toString(fromIntersection.getY()));
	arrowhead.setAttributeNS(null, "x2", Double.toString(arrowEndX));
	arrowhead.setAttributeNS(null, "y2", Double.toString(arrowEndY));
	arrowhead.setAttributeNS(null, "style", "fill:" + color + ";stroke:" + color + ";stroke-width:2;opacity:1");
	arrowhead.setAttributeNS(null, "marker-end", "url(#Triangle)");
	arrowhead.setAttributeNS(null, "parentID", "0");

	// Attach the line to the root 'svg' element.
	Element svgRoot = doc.getDocumentElement();
	svgRoot.appendChild(line);
	svgRoot.appendChild(arrowhead);

	// print sequence name, if given
	if (!StringUtils.isBlank(branchTitle)) {
	    // top sequences have title above, bottom ones below
	    double titleY = arrowEndY + (b < 0 ? -10 : 20);
	    createText("branch_title_" + id, arrowEndX, titleY, "end", null, null, null, branchTitle, svgRoot);
	}
    }

    /**
     * Gets a map ID -> node for all activities in Learning Design.
     */
    private Map<Long, ActivityTreeNode> getActivityTree(List<AuthoringActivityDTO> activities, ActivityTreeNode root) {
	// initialize all tree nodes
	Map<Long, ActivityTreeNode> allNodes = new HashMap<Long, ActivityTreeNode>();
	for (AuthoringActivityDTO activity : activities) {
	    ActivityTreeNode node = new ActivityTreeNode(activity);
	    allNodes.put(activity.getActivityID(), node);
	}

	// construct activities tree
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

	return allNodes;
    }

    /**
     * Gets map ID -> node for activities within branching. Also groups activities into sets the same way they are
     * organised in branches.
     */
    private Map<Long, ActivityTreeNode> getActivityTree(List<AuthoringActivityDTO> activities, ActivityTreeNode root,
	    long branchingActivityId, Collection<BranchDTO> branchesContainter) {
	// this set contains only activities with parents; other activities can not be part of branching
	Set<AuthoringActivityDTO> allChildActivities = new HashSet<AuthoringActivityDTO>();
	// maps Sequence Activity ID to activities it contains
	Map<Long, Set<AuthoringActivityDTO>> sequenceMapping = new HashMap<Long, Set<AuthoringActivityDTO>>();
	ActivityDTOOrderComparator orderComparator = new ActivityDTOOrderComparator();

	for (AuthoringActivityDTO activity : activities) {
	    Long activityId = activity.getActivityID();
	    Long parentActivityId = activity.getParentActivityID();
	    if (activityId.equals(branchingActivityId)) {
		// fill coordinates for doors on canvas
		branchingStartPoint = new Point(activity.getStartXCoord(), activity.getStartYCoord());
		branchingEndPoint = new Point(activity.getEndXCoord(), activity.getEndYCoord());
	    } else if (parentActivityId != null) {
		// get only activities with parents
		allChildActivities.add(activity);

		if (parentActivityId.equals(branchingActivityId)) {
		    // this is Sequence Activity, i.e. a branch
		    sequenceMapping.put(activityId, new TreeSet<AuthoringActivityDTO>(orderComparator));
		    BranchDTO branchDTO = new BranchDTO();
		    branchDTO.sequenceActivityId = activityId;
		    branchDTO.branchTitle = activity.getActivityTitle();
		    branchDTO.stopAfterActivity = activity.getStopAfterActivity();
		    branchesContainter.add(branchDTO);
		}
	    }
	}

	// now choose only the activities which belong to the given branching
	Map<Long, ActivityTreeNode> branchingNodes = new HashMap<Long, ActivityTreeNode>();
	for (AuthoringActivityDTO childActivity : allChildActivities) {
	    Long parentActivityId = childActivity.getParentActivityID();
	    // is the activity part of Sequence Activity, i.e. branch?
	    if (sequenceMapping.keySet().contains(parentActivityId)) {
		// branch parts become top-level activities
		childActivity.setParentActivityID(null);

		ActivityTreeNode node = new ActivityTreeNode(childActivity);
		root.add(node);
		branchingNodes.put(childActivity.getActivityID(), node);

		// add the given activity to its branch
		sequenceMapping.get(parentActivityId).add(childActivity);
	    }
	}

	// activities in sequenceMapping are sorted with Comparator
	// now convert them into a list of nodes
	for (Long sequenceActivityId : sequenceMapping.keySet()) {
	    for (BranchDTO branchDTO : branchesContainter) {
		if (sequenceActivityId.equals(branchDTO.sequenceActivityId)) {
		    Set<AuthoringActivityDTO> sequence = sequenceMapping.get(sequenceActivityId);
		    for (AuthoringActivityDTO childActivity : sequence) {
			ActivityTreeNode childAcitivityNode = branchingNodes.get(childActivity.getActivityID());
			branchDTO.add(childAcitivityNode);
		    }
		    break;
		}
	    }
	}

	// run the same code twice, for Optional Activities and Sequences
	// first iteration sets parents for Optional Activities and sequences within Optional Sequences
	for (AuthoringActivityDTO activity : allChildActivities) {
	    Long parentActivityId = activity.getParentActivityID();
	    Long activityId = activity.getActivityID();
	    if (branchingNodes.keySet().contains(parentActivityId) && !branchingNodes.keySet().contains(activityId)) {
		ActivityTreeNode node = new ActivityTreeNode(activity);
		ActivityTreeNode parent = branchingNodes.get(parentActivityId);
		parent.add(node);
		branchingNodes.put(activityId, node);
	    }
	}
	// second iteration sets parents for activities in sequences within Optional Sequences
	for (AuthoringActivityDTO activity : allChildActivities) {
	    Long parentActivityId = activity.getParentActivityID();
	    Long activityId = activity.getActivityID();
	    if (branchingNodes.keySet().contains(parentActivityId) && !branchingNodes.keySet().contains(activityId)) {
		ActivityTreeNode node = new ActivityTreeNode(activity);
		ActivityTreeNode parent = branchingNodes.get(parentActivityId);
		parent.add(node);
		branchingNodes.put(activityId, node);
	    }
	}

	return branchingNodes;
    }

    /**
     * Draws transitions between activities.
     */
    private void createActivityTransitionLines(Map<Long, ActivityTreeNode> nodes, Collection<TransitionDTO> transitions) {
	for (TransitionDTO transition : transitions) {
	    ActivityTreeNode fromActivity = nodes.get(transition.getFromActivityID());
	    ActivityTreeNode toActivity = nodes.get(transition.getToActivityID());
	    Point2D fromIntersection = SVGTrigonometryUtils.getActivityAndLineSegmentIntersection(fromActivity,
		    toActivity);
	    Point2D toIntersection = SVGTrigonometryUtils.getActivityAndLineSegmentIntersection(toActivity,
		    fromActivity);

	    // skip optional sequence's childs and lines between overlapped activities
	    if (fromActivity.isOptionalSequenceActivityChild() || (fromIntersection == null)
		    || (toIntersection == null)) {
		continue;
	    }

	    String id = transition.getFromActivityID() + "_to_" + transition.getToActivityID();
	    createTransitionLine(id, fromIntersection, toIntersection, null, null);
	}
    }

    /**
     * Draws initial (from door) and ending (to door) transition lines in branching.
     */
    private void createBranchingTransitionLines(Collection<BranchDTO> branches, ActivityTreeNode root) {
	String imageFolder = (SVGGenerator.OUTPUT_FORMAT_SVG_LAMS_COMMUNITY == outputFormat ? SVGConstants.PATH_TO_LAMSCOMMUNITY_SVG_IMAGES
		: Configuration.get(ConfigurationKeys.SERVER_URL) + "images/icons/");

	// first the lines from door
	String imageFileName = imageFolder + "door_out.png";
	String imageId = "image-start";
	createImage(null, imageFileName, imageId, new Double(branchingStartPoint.getX()).intValue(), new Double(
		branchingStartPoint.getY()).intValue());

	imageFileName = imageFolder + "door_in.png";
	imageId = "image-end";
	createImage(null, imageFileName, imageId, new Double(branchingEndPoint.getX()).intValue(), new Double(
		branchingEndPoint.getY()).intValue());

	Dimension edgePointsDimensions = new Dimension(27, 27);
	Rectangle startRectangle = new Rectangle(branchingStartPoint, edgePointsDimensions);
	Rectangle endRectangle = new Rectangle(branchingEndPoint, edgePointsDimensions);

	for (BranchDTO branch : branches) {
	    if (branch.isEmpty()) {
		// no activities in branch so just draw a line from door to door
		Point2D fromIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(startRectangle,
			endRectangle);
		Point2D toIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(endRectangle,
			startRectangle);

		// skip lines between overlapped activities
		if ((fromIntersection == null) || (toIntersection == null)) {
		    continue;
		}

		// now the last lines
		String id = "start_to_end";
		createTransitionLine(id, fromIntersection, toIntersection, branch.branchTitle, "#AFCE63");
	    } else {
		ActivityTreeNode branchFirstActivity = branch.getFirst();
		Rectangle branchFirstActivityRectangle = new Rectangle(branchFirstActivity.getActivityCoordinates().x,
			branchFirstActivity.getActivityCoordinates().y,
			branchFirstActivity.getActivityDimension().width,
			branchFirstActivity.getActivityDimension().height);

		Point2D fromIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(startRectangle,
			branchFirstActivityRectangle);
		Point2D toIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(
			branchFirstActivityRectangle, startRectangle);

		// skip lines between overlapped activities
		if ((fromIntersection == null) || (toIntersection == null)) {
		    continue;
		}

		// now the last lines
		String id = "start_to_" + branchFirstActivity.getActivity().getActivityID();
		createTransitionLine(id, fromIntersection, toIntersection, branch.branchTitle, "#AFCE63");

		ActivityTreeNode branchLastActivity = branch.getLast();
		if (!branch.stopAfterActivity) {
		    Rectangle branchLastActivityRectangle = new Rectangle(
			    branchLastActivity.getActivityCoordinates().x,
			    branchLastActivity.getActivityCoordinates().y,
			    branchLastActivity.getActivityDimension().width,
			    branchLastActivity.getActivityDimension().height);

		    fromIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(
			    branchLastActivityRectangle, endRectangle);
		    toIntersection = SVGTrigonometryUtils.getRectangleAndLineSegmentIntersection(endRectangle,
			    branchLastActivityRectangle);

		    // skip lines between overlapped activities
		    if ((fromIntersection == null) || (toIntersection == null)) {
			continue;
		    }

		    id = branchLastActivity.getActivity().getActivityID() + "_to_end";
		    createTransitionLine(id, fromIntersection, toIntersection, null, "#AFCE63");
		}
	    }
	}
    }
}