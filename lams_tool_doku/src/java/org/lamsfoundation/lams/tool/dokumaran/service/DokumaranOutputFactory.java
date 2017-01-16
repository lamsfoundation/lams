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


package org.lamsfoundation.lams.tool.dokumaran.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;

public class DokumaranOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	Class simpleUrlArrayClass = SimpleURL[].class;
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition sharedItemsDefinition = buildComplexOutputDefinition(
			DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME, simpleUrlArrayClass);
		definitionMap.put(DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME, sharedItemsDefinition);
		break;
	}
	return definitionMap;
    }

    /**
     * Follows {@link PixlrService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IDokumaranService dokumaranService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();
	if (names == null) {
	    outputs.put(DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME, getToolOutput(
		    DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME, dokumaranService, toolSessionId, learnerId));
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, dokumaranService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, IDokumaranService dokumaranService, Long toolSessionId, Long learnerId) {
	if (name != null) {
	    String[] nameParts = splitConditionName(name);
//	    if (DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME.equals(nameParts[0])) {
//		List<DokumaranItem> items = dokumaranService.getDokumaranItemsBySessionId(toolSessionId);
//		List<DokumaranItem> uploadedItems = new ArrayList<DokumaranItem>(items.size());
//		for (DokumaranItem item : items) {
//		    if (!item.isCreateByAuthor()) {
//			uploadedItems.add(item);
//		    }
//		}
//		SimpleURL[] uploadedItemUrls = new SimpleURL[uploadedItems.size()];
//		int uploadedItemIndex = 0;
//		if (!uploadedItems.isEmpty()) {
//		    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
//		    for (DokumaranItem uploadedItem : uploadedItems) {
//
//			String path = uploadedItem.getUrl();
//			if (path == null) {
//			    path = serverUrl + "download/?uuid=" + uploadedItem.getFileUuid() + "&preferDownload=false&"
//				    + AttributeNames.PARAM_TOOL_CONTENT_HANDLER_NAME + "="
//				    + DokumaranConstants.TOOL_CONTENT_HANDLER_NAME;
//
//			}
//			path = "javascript:var dummy = window.open('" + path + "','" + uploadedItem.getTitle()
//				+ "','resizable,scrollbars')";
//			SimpleURL url = new SimpleURL(uploadedItem.getTitle(), path);
//			uploadedItemUrls[uploadedItemIndex] = url;
//			uploadedItemIndex++;
//		    }
//		    return new ToolOutput(DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME,
//			    getI18NText(DokumaranConstants.SHARED_ITEMS_DEFINITION_NAME, true), uploadedItemUrls, false);
//		}
//
//	    }

	}
	return null;
    }
}
