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


package org.lamsfoundation.lams.util.svg;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;

/**
 * @author Andrey Balan
 */
public class ActivityTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;

    public ActivityTreeNode() {
	super();
    }

    public ActivityTreeNode(AuthoringActivityDTO activity) {
	super(activity);
    }

    public AuthoringActivityDTO getActivity() {
	return (AuthoringActivityDTO) userObject;
    }

    public List<ActivityTreeNode> getChildren() {
	Enumeration<ActivityTreeNode> enumeration = children();
	return Collections.list(enumeration);
    }

    /**
     * Can be invoked only after the tree has been built already.
     *
     * @return
     */
    public AuthoringActivityDTO getParentActivity() {
	if (parent != null) {
	    ActivityTreeNode parentNode = (ActivityTreeNode) parent;
	    return (AuthoringActivityDTO) parentNode.getUserObject();
	}

	return null;
    }

    /**
     * Checks whether activity is a children of an optional activity
     *
     * @param node
     * @return
     */
    public boolean isOptionalActivityChild() {
	boolean isOptionalActivityChild = false;

	AuthoringActivityDTO activity = getActivity();
	if ((activity.getParentActivityID() != null)) {
	    ActivityTreeNode parentNode = (ActivityTreeNode) parent;
	    AuthoringActivityDTO parentActivity = parentNode.getActivity();
	    if (parentActivity.getActivityTypeID().equals(Activity.OPTIONS_ACTIVITY_TYPE)) {
		isOptionalActivityChild = true;
	    }
	}

	return isOptionalActivityChild;
    }

    /**
     * Checks whether activity is a children of an optional sequence activity
     *
     * @param node
     * @return
     */
    public boolean isOptionalSequenceActivityChild() {
	boolean isOptionalSequenceActivityChild = false;

	AuthoringActivityDTO activity = (AuthoringActivityDTO) userObject;
	if ((activity.getParentActivityID() != null)) {
	    ActivityTreeNode parentNode = (ActivityTreeNode) parent;
	    AuthoringActivityDTO parentActivity = parentNode.getActivity();
	    if (parentActivity.getActivityTypeID().equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {
		isOptionalSequenceActivityChild = true;
	    }
	}

	return isOptionalSequenceActivityChild;
    }

    /**
     * Checks whether activity is a children of an optional sequence activity
     *
     * @param node
     * @return
     */
    public String getActivityColor() {
	String color = "";

	switch (getActivity().getActivityCategoryID()) {
	    case Activity.CATEGORY_SYSTEM:
		color = ";fill:#d0defd";
		break;
	    case Activity.CATEGORY_COLLABORATION:
		color = ";fill:#fffccb";
		break;
	    case Activity.CATEGORY_ASSESSMENT:
		color = ";fill:#ece9f7";
		break;
	    case Activity.CATEGORY_CONTENT:
		color = ";fill:#fdf1d3";
		break;
	    case Activity.CATEGORY_SPLIT:
		color = ";fill:#FFFFFF";
		break;
	    case Activity.CATEGORY_RESPONSE:
		color = ";fill:#e9f9c0";
		break;
	}

	return color;
    }

    public String getActivityCss() {
	AuthoringActivityDTO activity = (AuthoringActivityDTO) userObject;
	final String STROKE_CSS = "stroke:black;stroke-width:1;opacity:1;";

	String style;
	switch (activity.getActivityTypeID()) {
	    // this is a gate activity
	    case Activity.SYNCH_GATE_ACTIVITY_TYPE:
	    case Activity.SCHEDULE_GATE_ACTIVITY_TYPE:
	    case Activity.PERMISSION_GATE_ACTIVITY_TYPE:
	    case Activity.CONDITION_GATE_ACTIVITY_TYPE:
		if (isOptionalSequenceActivityChild() || isOptionalActivityChild()) {
		    style = STROKE_CSS + "fill:#d0defd";
		    ;
		} else {
		    style = "fill:red;stroke:#000000;stroke-width:0.5px";
		}
		break;

	    // This is a parallel activity OR branching activity
	    case Activity.PARALLEL_ACTIVITY_TYPE:
	    case Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE:
	    case Activity.GROUP_BRANCHING_ACTIVITY_TYPE:
	    case Activity.TOOL_BRANCHING_ACTIVITY_TYPE:
		style = STROKE_CSS + "fill:#d0defd";
		break;

	    // This is a sequence within an optional    
	    case Activity.SEQUENCE_ACTIVITY_TYPE:
		ActivityTreeNode parentNode = (ActivityTreeNode) getParent();
		int indexInSiblings = parentNode.getIndex(this) % 6;
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
		style = "stroke:#E1F0FD;stroke-width:.4;opacity:1;fill:#" + color;
		break;

	    // This is an optional sequence OR optional activity OR support activity
	    case Activity.OPTIONS_WITH_SEQUENCES_TYPE:
	    case Activity.OPTIONS_ACTIVITY_TYPE:
	    case Activity.FLOATING_ACTIVITY_TYPE:
		style = "fill:#d0defd;";
		if (isOptionalSequenceActivityChild() || isOptionalActivityChild()) {
		    style += STROKE_CSS;
		}
		break;

	    // This is a tool activity    
	    default:
		style = STROKE_CSS + getActivityColor();
		break;
	}

	return style;
    }

    public Dimension getActivityDimension() {
	AuthoringActivityDTO activity = (AuthoringActivityDTO) userObject;
	int childrenSize = getChildCount();

	// according to the type of activity, we need to set up difference width and height for the To and From
	// activity to draw the transition accordingly.
	int width;
	int height;
	// if this activity is a children of a sequence activity, if it is, then we need to change its size
	if (isOptionalSequenceActivityChild()) {
	    width = SVGConstants.TOOL_INSIDE_OPTIONAL_WIDTH;
	    height = 43;

	    // if this activity is a children of an optional activity
	} else if (isOptionalActivityChild()) {
	    width = SVGConstants.TOOL_WIDTH;
	    height = SVGConstants.TOOL_HEIGHT;

	} else {
	    switch (activity.getActivityTypeID()) {
		// this is a gate activity
		case Activity.SYNCH_GATE_ACTIVITY_TYPE:
		case Activity.SCHEDULE_GATE_ACTIVITY_TYPE:
		case Activity.PERMISSION_GATE_ACTIVITY_TYPE:
		case Activity.CONDITION_GATE_ACTIVITY_TYPE:
		    width = SVGConstants.GATE_WIDTH;
		    height = SVGConstants.GATE_HEIGHT;
		    break;

		// This is a parallel activity
		case Activity.PARALLEL_ACTIVITY_TYPE:
		    // Given that for now all parallel activities are just two activities, we can hard code the width and
		    // height
		    width = SVGConstants.PARALLEL_OR_OPTIONS_ACTIVITY_WIDTH;
		    height = SVGConstants.PARALLEL_ACTIVITY_HEIGHT;
		    break;

		// This is a branching activity
		case Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE:
		case Activity.GROUP_BRANCHING_ACTIVITY_TYPE:
		case Activity.TOOL_BRANCHING_ACTIVITY_TYPE:
		    width = SVGConstants.BRANCHING_ACTIVITY_WIDTH;
		    height = SVGConstants.BRANCHING_ACTIVITY_HEIGHT;
		    break;

		// This is an optional sequence
		case Activity.OPTIONS_WITH_SEQUENCES_TYPE:
		    width = getOptionalSequenceActivityWidth();
		    height = (57 * childrenSize) + 49;
		    break;

		// This is a sequence within an optional
		case Activity.SEQUENCE_ACTIVITY_TYPE:
		    ActivityTreeNode parentNode = (ActivityTreeNode) getParent();
		    width = parentNode.getOptionalSequenceActivityWidth() - 8;
		    height = SVGConstants.TOOL_HEIGHT + 3;
		    break;

		// This is an optional activity
		case Activity.OPTIONS_ACTIVITY_TYPE:
		    width = SVGConstants.PARALLEL_OR_OPTIONS_ACTIVITY_WIDTH;
		    height = SVGConstants.OPTIONS_ACTIVITY_HEIGHT_MULTIPLIER * childrenSize
			    + SVGConstants.OPTIONS_ACTIVITY_HEIGHT_ADD;
		    break;

		// This is a support activity
		case Activity.FLOATING_ACTIVITY_TYPE:
		    width = (SVGConstants.TOOL_WIDTH + 7) * childrenSize + 11;
		    height = SVGConstants.OPTIONS_ACTIVITY_HEIGHT_MULTIPLIER + SVGConstants.OPTIONS_ACTIVITY_HEIGHT_ADD;
		    break;

		// This is a tool activity
		default:
		    width = SVGConstants.TOOL_WIDTH;
		    height = SVGConstants.TOOL_HEIGHT;

		    break;
	    }
	}

	return new Dimension(width, height);
    }

    /**
     * Returns activity's left upper point
     *
     * @return
     */
    public Point getActivityCoordinates() {
	AuthoringActivityDTO activity = getActivity();

	int x = (activity.getxCoord() == null) ? 0 : activity.getxCoord();
	int y = (activity.getyCoord() == null) ? 0 : activity.getyCoord();

	return new Point(x, y);
    }

    /**
     * Returns OptionalSequenceActivityWidth
     *
     * @param node
     *            node containing optional sequence activity
     * @return
     */
    private int getOptionalSequenceActivityWidth() {
	// now find out how many activities each of the subsequences have so we can calculate the width
	int maxChildren = 0;
	for (ActivityTreeNode childNode : getChildren()) {
	    int childrenSize = childNode.getChildCount();
	    if (childrenSize > maxChildren) {
		maxChildren = childrenSize;
	    }
	}
	maxChildren = (maxChildren < 2) ? 2 : maxChildren;
	int width = SVGConstants.TOOL_INSIDE_OPTIONAL_WIDTH * maxChildren + 20;

	return width;
    }

}
