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
package org.lamsfoundation.lams.tool.mdscrm.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdscrm.model.MdlScorm;

/**
 * Creates the output definitions for mdlscorm tool Adapter tool.
 */
public class MdlScormOutputFactory extends OutputFactory {

    protected final static String OUTPUT_NAME_LEARNER_NUM_SUBS = "learner.number.of.files.submitted";

    public MdlScormOutputFactory() {
    }

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
	    throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_SUBS, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_SUBS, definition);

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMdlScormService dlScormService,
	    Long toolSessionId, Long learnerId, MdlScorm mdlScorm, Long extSessionId) {

	TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_SUBS)) {
	    map.put(OUTPUT_NAME_LEARNER_NUM_SUBS, getExtToolOutput(OUTPUT_NAME_LEARNER_NUM_SUBS, dlScormService,
		    mdlScorm, learnerId, extSessionId.toString(), toolSessionId));
	}
	return map;

    }

    public ToolOutput getToolOutput(String name, IMdlScormService dlScormService, Long toolSessionId, Long learnerId,
	    MdlScorm getToolOutput, Long extSessionId) {
	if (name != null) {
	    return getExtToolOutput(name, dlScormService, getToolOutput, learnerId, extSessionId.toString(),
		    toolSessionId);
	}
	return null;

    }

    public ToolOutput getExtToolOutput(String outputName, IMdlScormService mdlScormService, MdlScorm mdlScorm,
	    Long userId, String extToolContentId, Long toolSessionId) {
	int number = mdlScormService.getExternalToolOutputInt(outputName, mdlScorm, userId, extToolContentId,
		toolSessionId);
	return new ToolOutput(outputName, getI18NText(outputName, true), new Long(number));
    }

}
