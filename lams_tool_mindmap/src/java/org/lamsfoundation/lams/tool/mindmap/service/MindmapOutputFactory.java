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


package org.lamsfoundation.lams.tool.mindmap.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;

/**
 * Output factory for Mindmap tool. Currently it provides only one type of output - the entry that user provided.
 * 
 * @author Marcin Cieslak
 */
public class MindmapOutputFactory extends OutputFactory {

    /** The number of nodes the learner has created in one Mindmap activity. */
    protected final static String OUTPUT_NAME_LEARNER_NUM_NODES = "number.of.nodes";

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {

	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition definition1 = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_NODES, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_NODES, definition1);

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMindmapService mindmapService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_NODES)) {
	    map.put(OUTPUT_NAME_LEARNER_NUM_NODES, getNumNodes(mindmapService, learnerId, toolSessionId));
	}
	return map;
    }

    public ToolOutput getToolOutput(String name, IMindmapService mindmapService, Long toolSessionId, Long learnerId) {

	if (name != null && name.equals(OUTPUT_NAME_LEARNER_NUM_NODES)) {
	    return getNumNodes(mindmapService, learnerId, toolSessionId);
	}

	return null;
    }

    private ToolOutput getNumNodes(IMindmapService mindmapService, Long learnerId, Long toolSessionId) {
	int num = mindmapService.getNumNodes(learnerId, toolSessionId);
	return new ToolOutput(OUTPUT_NAME_LEARNER_NUM_NODES, getI18NText(OUTPUT_NAME_LEARNER_NUM_NODES, true),
		new Long(num));
    }
}
