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


package org.lamsfoundation.lams.tool.pixlr.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Output factory for Pixlr tool. Currently it provides only one type of output - the URL to the edited image.
 *
 * @author lfoxton
 */
public class PixlrOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition imageUrlDefinition = buildComplexOutputDefinition(
			PixlrConstants.IMAGE_URL_DEFINITION_NAME, SimpleURL[].class);
		definitionMap.put(PixlrConstants.IMAGE_URL_DEFINITION_NAME, imageUrlDefinition);
		break;
	}
	return definitionMap;
    }

    /**
     * Follows {@link PixlrService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IPixlrService pixlrService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();
	if (names == null) {
	    outputs.put(PixlrConstants.IMAGE_URL_DEFINITION_NAME,
		    getToolOutput(PixlrConstants.IMAGE_URL_DEFINITION_NAME, pixlrService, toolSessionId, learnerId));
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, pixlrService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, IPixlrService pixlrService, Long toolSessionId, Long learnerId) {
	if (name != null) {
	    String[] nameParts = splitConditionName(name);
	    if (PixlrConstants.IMAGE_URL_DEFINITION_NAME.equals(nameParts[0])) {
		PixlrSession session = pixlrService.getSessionBySessionId(toolSessionId);

		if (session != null) {
		    Set<PixlrUser> users = session.getPixlrUsers();
		    Set<SimpleURL> urls = new HashSet<SimpleURL>(users.size());
		    for (PixlrUser user : users) {
			if (user.getImageFileName() != null) {
			    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
			    String imageName = user.getLastName() + "_" + user.getFirstName();
			    String imageUrl = "javascript:var dummy = window.open('" + serverUrl + "www/images/pixlr/"
				    + user.getImageFileName() + "','" + imageName + "','resizable,width="
				    + user.getImageWidth() + ",height=" + user.getImageHeight() + ",scrollbars')";

			    SimpleURL url = new SimpleURL(imageName, imageUrl);
			    urls.add(url);
			}
		    }
		    SimpleURL[] urlArray = urls.toArray(new SimpleURL[] {});
		    return new ToolOutput(PixlrConstants.IMAGE_URL_DEFINITION_NAME,
			    getI18NText(PixlrConstants.IMAGE_URL_DEFINITION_NAME, true), urlArray, false);
		}
	    }
	}
	return null;
    }
}
