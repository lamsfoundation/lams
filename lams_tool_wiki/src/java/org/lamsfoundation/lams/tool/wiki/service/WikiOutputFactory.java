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


package org.lamsfoundation.lams.tool.wiki.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;

/**
 * Creates the output definitions for wiki. There are two types of outputs: number of edits and number of pages added
 */
public class WikiOutputFactory extends OutputFactory {

    /** The number of edit the learner has made in one wiki activity. */
    protected final static String OUTPUT_NAME_LEARNER_NUM_EDITS = "number.of.edits";

    /** The number of wiki pages added by the learner. */
    protected final static String OUTPUT_NAME_LEARNER_NUM_ADDS = "number.of.add";

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition definition1 = buildRangeDefinition(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS,
		new Long(0), null);
	definitionMap.put(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS, definition1);

	ToolOutputDefinition definition2 = buildRangeDefinition(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_ADDS,
		new Long(0), null);
	definitionMap.put(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_ADDS, definition2);

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IWikiService wikiService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS)) {
	    map.put(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS,
		    getNumEdits(wikiService, learnerId, toolSessionId));
	}
	if (names.contains(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_ADDS)) {
	    map.put(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_ADDS, getNumAdds(wikiService, learnerId, toolSessionId));
	}
	return map;
    }

    public ToolOutput getToolOutput(String name, IWikiService wikiService, Long toolSessionId, Long learnerId) {

	if (name != null && name.equals(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS)) {
	    return getNumEdits(wikiService, learnerId, toolSessionId);
	}

	if (name != null && name.equals(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_ADDS)) {
	    return getNumAdds(wikiService, learnerId, toolSessionId);
	}

	return null;
    }

    /**
     * Gets the number of edits by this user for tool outputs
     *
     * @param wikiService
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    private ToolOutput getNumEdits(IWikiService wikiService, Long learnerId, Long toolSessionId) {
	int num = wikiService.getEditsNum(learnerId, toolSessionId);
	return new ToolOutput(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS,
		getI18NText(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS, true), new Long(num));
    }

    /**
     * Gets the number of pages added by this user for tool outputs
     *
     * @param wikiService
     * @param learnerId
     * @param toolSessionId
     * @return
     */
    private ToolOutput getNumAdds(IWikiService wikiService, Long learnerId, Long toolSessionId) {
	int num = wikiService.getAddsNum(learnerId, toolSessionId);
	return new ToolOutput(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS,
		getI18NText(WikiOutputFactory.OUTPUT_NAME_LEARNER_NUM_EDITS, true), new Long(num));
    }
}
