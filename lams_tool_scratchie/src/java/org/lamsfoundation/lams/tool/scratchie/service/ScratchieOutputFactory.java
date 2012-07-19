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
package org.lamsfoundation.lams.tool.scratchie.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;

public class ScratchieOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject, int definitionType)
	    throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	switch (definitionType) {
	case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
	    break;
	case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
	    ToolOutputDefinition definition = buildRangeDefinition(ScratchieConstants.LEARNER_NUMBER_ATTEMPTS,
		    new Long(0), null);
	    definitionMap.put(ScratchieConstants.LEARNER_NUMBER_ATTEMPTS, definition);
	    break;
	}
	return definitionMap;
    }

    /**
     * Follows {@link PixlrService#getToolOutput(List, Long, Long)}.
     * 
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IScratchieService scratchieService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(ScratchieConstants.LEARNER_NUMBER_ATTEMPTS)) {
	    outputs.put(
		    ScratchieConstants.LEARNER_NUMBER_ATTEMPTS,
		    getToolOutput(ScratchieConstants.LEARNER_NUMBER_ATTEMPTS, scratchieService, toolSessionId,
			    learnerId));
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, IScratchieService scratchieService, Long toolSessionId, Long learnerId) {
	if (name.equals(ScratchieConstants.LEARNER_NUMBER_ATTEMPTS)) {
	    return getNumberAttempts(scratchieService, toolSessionId, learnerId);
	}

	return null;
    }

    /**
     * Get the number of attempts done by user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumberAttempts(IScratchieService scratchieService, Long toolSessionId, Long learnerId) {

	int numberAttempts = scratchieService.getNumberAttempts(learnerId, toolSessionId);

	return new ToolOutput(ScratchieConstants.LEARNER_NUMBER_ATTEMPTS, getI18NText(
		ScratchieConstants.LEARNER_NUMBER_ATTEMPTS, true), numberAttempts);

    }
}
