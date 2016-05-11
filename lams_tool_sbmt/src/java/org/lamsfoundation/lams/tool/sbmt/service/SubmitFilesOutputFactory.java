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


package org.lamsfoundation.lams.tool.sbmt.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class SubmitFilesOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	Class arrayOfSimpleUrlArrayClass = SimpleURL[][].class;
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition itemsSubmittedDefinition = buildComplexOutputDefinition(
			SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME, arrayOfSimpleUrlArrayClass);
		definitionMap.put(SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME, itemsSubmittedDefinition);
		break;
	}
	return definitionMap;
    }

    /**
     * Follows {@link PixlrService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, ISubmitFilesService submitFilesService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();
	if (names == null) {
	    outputs.put(SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME, getToolOutput(
		    SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME, submitFilesService, toolSessionId, learnerId));
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, submitFilesService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, ISubmitFilesService submitFilesService, Long toolSessionId,
	    Long learnerId) {
	if (name != null) {
	    String[] nameParts = splitConditionName(name);
	    if (SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME.equals(nameParts[0])) {

		SortedMap<SubmitUserDTO, List<FileDetailsDTO>> filesUploadedBySession = submitFilesService
			.getFilesUploadedBySession(toolSessionId, null);
		if (!filesUploadedBySession.isEmpty()) {
		    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		    SimpleURL[][] usersAndUrls = new SimpleURL[filesUploadedBySession.keySet().size()][];
		    int userIndex = 0;
		    for (SubmitUserDTO userDTO : filesUploadedBySession.keySet()) {
			List<FileDetailsDTO> files = filesUploadedBySession.get(userDTO);
			if (!files.isEmpty()) {
			    SimpleURL[] urlArray = new SimpleURL[files.size()];
			    int urlIndex = 0;
			    for (FileDetailsDTO filesDetailsDTO : files) {
				String fileUrl = "javascript:var dummy = window.open('" + serverUrl + "download/?uuid="
					+ filesDetailsDTO.getUuID() + "&preferDownload=false&"
					+ AttributeNames.PARAM_TOOL_CONTENT_HANDLER_NAME + "="
					+ SbmtConstants.TOOL_CONTENT_HANDLER_NAME + "','"
					+ filesDetailsDTO.getFileDescription() + "','resizable,scrollbars')";

				SimpleURL url = new SimpleURL(filesDetailsDTO.getFileDescription(), fileUrl);
				urlArray[urlIndex] = url;
				urlIndex++;
			    }
			    usersAndUrls[userIndex] = urlArray;
			}
			userIndex++;
		    }
		    return new ToolOutput(SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME,
			    getI18NText(SbmtConstants.SUBMITTED_ITEMS_DEFINITION_NAME, true), usersAndUrls, false);
		}
	    }
	}
	return null;
    }
}
