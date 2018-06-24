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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.mindmap.dto;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON Class for returning update actions to the client for multiuser mode
 */
public class NotifyActionJSON extends ObjectNode {

    // Poll server response
    public static final String REQUEST_ID_KEY = "actionId"; // Global ID from request table.
    public static final String NODE_ID_KEY = "nodeId"; // Node ID
    public static final String TYPE_KEY = "type"; // 0 - delete; 1 - create node; 2 - change color; 3 - change text
    public static final String CHILD_NODE_ID_KEY = "childNodeId"; // Child Node ID // 1 - create node
    public static final String TITLE_KEY = "title"; // Text (if type is 3, 1)
    public static final String BACKGROUND_COLOR = "color"; // Color (if type is 2, maybe 1)
    public static final String CREATOR = "creator"; // Creator description (type 1) - not used at present

    public NotifyActionJSON(Long requestId, Long nodeId, int type, Long childNodeId, String text, String color,
	    String creator) {
	super(JsonNodeFactory.instance);
	this.put(REQUEST_ID_KEY, requestId);
	this.put(NODE_ID_KEY, nodeId);
	this.put(TYPE_KEY, type);
	this.put(CHILD_NODE_ID_KEY, childNodeId);
	this.put(TITLE_KEY, text);
	this.put(BACKGROUND_COLOR, color);
	this.put(CREATOR, creator);
    }

}
