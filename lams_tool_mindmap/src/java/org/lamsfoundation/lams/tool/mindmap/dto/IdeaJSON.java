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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeConceptModel;
import org.lamsfoundation.lams.tool.mindmap.util.xmlmodel.NodeModel;
import org.lamsfoundation.lams.util.JsonUtil;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A JSONObject in the MapJs v3 format as per https://github.com/mindmup/mapjs/wiki/Data-Format.
 * Converts from our existing XML Model to JSON and back again.
 */

//    invididual ideas
//
//    {
//    id: _idea id_, /*alphanumeric */
//    title: _idea title_, /* string */
//    attr : {  /* key-value map of idea attributes, optional */
//       style: { }, /* key-value map of style properties, optional */
//       collapsed: true/false /* optional */
//       attachment: { contentType: _content type_, content: _content_ },
//       icon: { url: _icon url_, position: _icon position_, width: _icon width_, height: _icon height_ }
//    }

public class IdeaJSON extends ObjectNode {

    private static Logger logger = Logger.getLogger(IdeaJSON.class);

    public static final String MAPJS_JSON_ID_KEY = "id";
    public static final String MAPJS_JSON_TITLE_KEY = "title";
    public static final String MAPJS_JSON_IDEAS_KEY = "ideas";
    public static final String MAPJS_JSON_STYLE_KEY = "style";
    public static final String MAPJS_JSON_ATTRIBUTES_KEY = "attr";
    public static final String MAPJS_JSON_BACKGROUND_COLOR_KEY = "background";
    public static final String MAPJS_JSON_WIDTH_KEY = "width";
    public static final String MAPJS_JSON_ATTRIBUTES_CONTENTLOCKED = "contentLocked";

    public static final String DEFAULT_COLOR = "#ffffff";

    // TODO support more than one main node. mindmup does it, our database code does not
    /** Create JSON objects from the database model */
    public IdeaJSON(NodeModel node, int level, boolean includeCreator) {
	super(JsonNodeFactory.instance);

	this.put(MAPJS_JSON_ID_KEY, node.getConcept().getId());
	this.put(MAPJS_JSON_TITLE_KEY, node.getConcept().getText());

	if (includeCreator) {
	    // only needed for multi user maps
	    this.put("creator", node.getConcept().getCreator()); // LAMS custom value
	}

	ObjectNode attr = JsonNodeFactory.instance.objectNode();
	if (node.getConcept().isEdit() == 0) {
	    attr.put(MAPJS_JSON_ATTRIBUTES_CONTENTLOCKED, true);
	}
	ObjectNode style = JsonNodeFactory.instance.objectNode();

	int nextLevel = level + 1;
	if (!node.getBranch().isEmpty()) {
	    ObjectNode ideasList = JsonNodeFactory.instance.objectNode();
	    if (level == 0) {
		int rank = 1;
		for (NodeModel childNode : node.getBranch()) {
		    ideasList.set(String.valueOf(rank), new IdeaJSON(childNode, nextLevel, includeCreator));
		    rank = -rank;
		    if (rank > 0) {
			rank++;
		    }
		}
	    } else {
		int rank = 1;
		for (NodeModel childNode : node.getBranch()) {
		    ideasList.set(String.valueOf(rank++), new IdeaJSON(childNode, nextLevel, includeCreator));
		}
	    }
	    this.set(MAPJS_JSON_IDEAS_KEY, ideasList);
	}

	String color = node.getConcept().getColor();
	if (color == null || color.length() == 0) {
	    color = DEFAULT_COLOR;
	} else if (color != null && color.length() > 0 && !color.startsWith("#")) {
	    color = "#" + color;
	}
	style.put(MAPJS_JSON_BACKGROUND_COLOR_KEY, color);
	attr.set(MAPJS_JSON_STYLE_KEY, style);
	this.set(MAPJS_JSON_ATTRIBUTES_KEY, attr);
    }

    public static NodeModel toNodeModel(ObjectNode idea) {

	// Pull out the style attributes
	String color = null;
	if (idea.has(MAPJS_JSON_ATTRIBUTES_KEY)) {
	    ObjectNode attributes = JsonUtil.optObject(idea, MAPJS_JSON_ATTRIBUTES_KEY);
	    if (attributes.has(MAPJS_JSON_STYLE_KEY)) {
		ObjectNode styles = JsonUtil.optObject(attributes, MAPJS_JSON_STYLE_KEY);
		color = JsonUtil.optString(styles, MAPJS_JSON_BACKGROUND_COLOR_KEY);
	    }
	}
	if (color == null || color.length() == 0) {
	    color = DEFAULT_COLOR;
	}

	String creator = JsonUtil.optString(idea, "creator");
	NodeConceptModel nodeConceptModel = new NodeConceptModel(JsonUtil.optLong(idea, MAPJS_JSON_ID_KEY),
		JsonUtil.optString(idea, MAPJS_JSON_TITLE_KEY), color, creator);
	NodeModel nodeModel = new NodeModel(nodeConceptModel);

	if (idea.has(MAPJS_JSON_IDEAS_KEY)) {
	    ObjectNode ideas = JsonUtil.optObject(idea, MAPJS_JSON_IDEAS_KEY);
	    Iterator<String> keys = ideas.fieldNames();
	    while (keys.hasNext()) {
		String key = keys.next();
		nodeModel.addNode(IdeaJSON.toNodeModel(JsonUtil.optObject(ideas, key)));
	    }
	}

	return nodeModel;
    }

}
