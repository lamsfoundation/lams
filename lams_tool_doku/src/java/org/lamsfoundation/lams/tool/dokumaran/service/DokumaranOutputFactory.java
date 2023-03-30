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
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.exception.ToolException;

public class DokumaranOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<>();
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		Integer maxMark = 100;
		if (toolContentObject != null) {
		    Dokumaran dokumaran = (Dokumaran) toolContentObject;
		    maxMark = dokumaran.getMaxMark();
		}
		ToolOutputDefinition manualGradingDefinition = buildRangeDefinition(
			DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME, 0L, maxMark.longValue(), true);
		manualGradingDefinition.setWeightable(true);
		manualGradingDefinition.setShowConditionNameOnly(true);
		definitionMap.put(DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME, manualGradingDefinition);
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
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

	TreeMap<String, ToolOutput> outputs = new TreeMap<>();

	if (names == null) {
	    outputs.put(DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME, getToolOutput(
		    DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME, dokumaranService, toolSessionId, learnerId));
	} else {
	    // tool output cache
	    TreeMap<String, ToolOutput> baseOutputs = new TreeMap<>();
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

    public ToolOutput getToolOutput(String name, IDokumaranService dokumaranService, Long toolSessionId,
	    Long learnerId) {
	if (name != null) {
	    String[] nameParts = splitConditionName(name);
	    if (DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME.equals(nameParts[0])) {
		return new ToolOutput(DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME,
			getI18NText(DokumaranConstants.MANUAL_GRADING_DEFINITION_NAME, true), 0L);
	    }
	}
	return null;
    }
}
