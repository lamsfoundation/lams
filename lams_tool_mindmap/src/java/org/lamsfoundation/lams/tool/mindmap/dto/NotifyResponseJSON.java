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
 * JSON Class for returning responding to a multi user change sent from the client.
 */
public class NotifyResponseJSON extends ObjectNode {

    // Poll server response
    public static final String REQUEST_ID_KEY = "requestId"; // Request ID
    public static final String NODE_ID_KEY = "nodeId"; // Node ID
    public static final String OK = "ok";

    public NotifyResponseJSON(int ok, Long requestId, Long nodeId) {
	super(JsonNodeFactory.instance);
	this.put(OK, ok);
	this.put(REQUEST_ID_KEY, requestId);
	this.put(NODE_ID_KEY, nodeId);
    }

}
