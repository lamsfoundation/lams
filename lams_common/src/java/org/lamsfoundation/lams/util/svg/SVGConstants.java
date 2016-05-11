package org.lamsfoundation.lams.util.svg;

/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * ============================================================= License Information:
 * http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License version 2.0 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt ****************************************************************
 */



public class SVGConstants {

    public static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    public static final String SVG_NAMESPACE_XLINK = "http://www.w3.org/1999/xlink";

    public static final String PATH_TO_LAMSCOMMUNITY_SVG_IMAGES = "http://lamscommunity.org/lamscentral/images/acts/";
    public static final String PATH_TO_LAMS_CENTRAL_SVG_IMAGES = "/lams-central.war/images/svg/";

    public static final String ROOT_ELEMENT_ID = "rootElement";

    // gate dimensions
    public static final int GATE_WIDTH = 30;
    public static final int GATE_HEIGHT = 30;
    // These are the octogon proportions so according to one point we calculate the new dimensions/proportions
    public static final double[][] GATE_PROPORTIONS = { { 13.6, 34.4 }, { 1, 34.4 }, { -9.4, 24 }, { -9.4, 11.4 },
	    { 1, 1 }, { 13.6, 1 }, { 24, 11.4 }, { 24, 24 } };

    // tool dimensions
    public static final int TOOL_WIDTH = 125;
    public static final int TOOL_HEIGHT = 50;
    public static final int TOOL_INSIDE_OPTIONAL_WIDTH = 60;

    public static final int PARALLEL_ACTIVITY_HEIGHT = 172;
    public static final int OPTIONS_ACTIVITY_HEIGHT_MULTIPLIER = 63;
    public static final int OPTIONS_ACTIVITY_HEIGHT_ADD = 53;
    public static final int PARALLEL_OR_OPTIONS_ACTIVITY_WIDTH = 141;
    public static final int CONTAINER_HEADER_HEIGHT = 39;

    public static final int BRANCHING_ACTIVITY_HEIGHT = 100;
    public static final int BRANCHING_ACTIVITY_WIDTH = 165;

    public static final double BRANCHING_ACTIVITY_POINT = 8.5;
    // distance between points in branching activity
    public static final int BRANCHING_STEP = 15;

    // css
    public static final String CSS_CONTAINER = "fill:#A9C8FD;stroke:#E1F0FD;stroke-width:2.2;opacity:1";
}