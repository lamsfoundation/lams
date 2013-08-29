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
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;

public class ScratchieOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject, int definitionType)
	    throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	ToolOutputDefinition definition = buildRangeDefinition(ScratchieConstants.LEARNER_MARK, new Long(0), null);
	definitionMap.put(ScratchieConstants.LEARNER_MARK, definition);

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
	if (names == null || names.contains(ScratchieConstants.LEARNER_MARK)) {
	    outputs.put(
		    ScratchieConstants.LEARNER_MARK,
		    getToolOutput(ScratchieConstants.LEARNER_MARK, scratchieService, toolSessionId,
			    learnerId));
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, IScratchieService scratchieService, Long toolSessionId, Long learnerId) {
	if (name.equals(ScratchieConstants.LEARNER_MARK)) {
	    return getUserMark(scratchieService, toolSessionId, learnerId);
	}

	return null;
    }

    /**
     * Get the mark scored by user. Will always return a ToolOutput object.
     */
    private ToolOutput getUserMark(IScratchieService scratchieService, Long toolSessionId, Long learnerId) {

	ScratchieSession session = scratchieService.getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser user = scratchieService.getUserByIDAndSession(learnerId, toolSessionId);
	int userMark = scratchieService.getUserMark(session, user.getUid());

	return new ToolOutput(ScratchieConstants.LEARNER_MARK, getI18NText(
		ScratchieConstants.LEARNER_MARK, true), userMark);

    }
}
