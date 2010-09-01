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
     * Checks whether activity is a children of an optional sequence activity
     * 
     * @param node
     * @return
     */
    public boolean isOptionalSequenceActivityChild() {
	boolean isOptionalSequenceActivityChild = false;
	
	AuthoringActivityDTO activity = (AuthoringActivityDTO) userObject;
	if ((activity.getParentActivityID() != null)) {
	    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parent;
	    AuthoringActivityDTO parentActivity = (AuthoringActivityDTO) parentNode.getUserObject();
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
    
    public Dimension getActivityDimension() {
	AuthoringActivityDTO activity = (AuthoringActivityDTO) userObject;
	int childrenSize = getChildCount();
	
	// according to the type of activity, we need to set up difference width and height for the To and From
	// activity to draw the transition accordingly.
        int width;
        int height;
	if (activity.getActivityTypeID().equals(Activity.SYNCH_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.SCHEDULE_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.PERMISSION_GATE_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.CONDITION_GATE_ACTIVITY_TYPE)) {
	    // this is a gate activity
	    width = SVGConstants.GATE_WIDTH;
	    height = SVGConstants.GATE_HEIGHT;
	    
	} else if (activity.getActivityTypeID().equals(Activity.PARALLEL_ACTIVITY_TYPE)) {
	    // This is a parallel activity
	    // Given that for now all parallel activities are just two activities, we can hard code the width and height
	    width = SVGConstants.PARALLEL_OR_OPTIONS_ACTIVITY_WIDTH;
	    height = SVGConstants.PARALLEL_ACTIVITY_HEIGHT;	
	    
	} else if (activity.getActivityTypeID().equals(Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.GROUP_BRANCHING_ACTIVITY_TYPE)
		|| activity.getActivityTypeID().equals(Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) {
	    // This is a branching activity
	    width = SVGConstants.BRANCHING_ACTIVITY_WIDTH;
	    height = SVGConstants.BRANCHING_ACTIVITY_HEIGHT;	    
	    
	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_WITH_SEQUENCES_TYPE)) {
	    // This is an optional sequence
	    width = getOptionalSequenceActivityWidth();
	    height = (57 * childrenSize) + 49;
	    
	} else if (activity.getActivityTypeID().equals(Activity.SEQUENCE_ACTIVITY_TYPE)) {
	    // This is a sequence within an optional
	    ActivityTreeNode parentNode = (ActivityTreeNode) getParent();
	    width = parentNode.getOptionalSequenceActivityWidth() -8;
	    height = SVGConstants.TOOL_HEIGHT + 3;	    
	    
	} else if (activity.getActivityTypeID().equals(Activity.OPTIONS_ACTIVITY_TYPE)) {
	    // This is an optional activity	    
	    width = SVGConstants.PARALLEL_OR_OPTIONS_ACTIVITY_WIDTH;
	    height = SVGConstants.OPTIONS_ACTIVITY_HEIGHT_MULTIPLIER * childrenSize
		    + SVGConstants.OPTIONS_ACTIVITY_HEIGHT_ADD;
	    
	} else if (activity.getActivityTypeID().equals(Activity.FLOATING_ACTIVITY_TYPE)) {
	    // This is a support activity	
	    width = (SVGConstants.TOOL_WIDTH +7) * childrenSize + 11;	    
	    height = SVGConstants.OPTIONS_ACTIVITY_HEIGHT_MULTIPLIER + SVGConstants.OPTIONS_ACTIVITY_HEIGHT_ADD;
	    
	} else {
	    // This is a tool activity
	    width = SVGConstants.TOOL_WIDTH;
	    height = SVGConstants.TOOL_HEIGHT;
	    
	    // if this activity is a children of a sequence activity, if it is, then we need to change its size
	    if (isOptionalSequenceActivityChild()) {
		width = SVGConstants.TOOL_INSIDE_OPTIONAL_WIDTH;
		height = 43;		
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
     * @param node node containing optional sequence activity
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
