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
package org.lamsfoundation.lams.tool.mdwiki.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdwiki.service.IMdlWikiService;
import org.lamsfoundation.lams.tool.mdwiki.model.MdlWiki;

/**
 * Creates the output definitions for mdlwiki tool Adapter tool.
 */
public class MdlWikiOutputFactory extends OutputFactory {

	protected final static String OUTPUT_NAME_LEARNER_NUM_ENTRIES = "learner.number.of.entries";
    protected final static String OUTPUT_NAME_LEARNER_NUM_PAGES = "learner.number.of.pages";
    protected final static String OUTPUT_NAME_LEARNER_NUM_ENTRIES_PAGES = "learner.number.of.entries.or.pages";

    public MdlWikiOutputFactory() {
    }

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
    throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	
	ToolOutputDefinition definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_ENTRIES, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_ENTRIES, definition);
	
	ToolOutputDefinition definition2 = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_PAGES, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_PAGES, definition2);
	
	ToolOutputDefinition definition3 = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_ENTRIES_PAGES, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_ENTRIES_PAGES, definition3);
	
	return definitionMap;
	}
	
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMdlWikiService dlWikiService,
	    Long toolSessionId, Long learnerId, MdlWiki mdlWiki, Long extSessionId) {
	
	TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_ENTRIES)) {
	    map.put(OUTPUT_NAME_LEARNER_NUM_ENTRIES, getExtToolOutput(OUTPUT_NAME_LEARNER_NUM_ENTRIES, dlWikiService,
		    mdlWiki, learnerId, extSessionId.toString(), toolSessionId));
	}
	if (names.contains(OUTPUT_NAME_LEARNER_NUM_PAGES)) {
	    map.put(OUTPUT_NAME_LEARNER_NUM_PAGES, getExtToolOutput(OUTPUT_NAME_LEARNER_NUM_PAGES, dlWikiService,
		    mdlWiki, learnerId, extSessionId.toString(), toolSessionId));
	}
	if (names.contains(OUTPUT_NAME_LEARNER_NUM_ENTRIES_PAGES)) {
	    map.put(OUTPUT_NAME_LEARNER_NUM_ENTRIES_PAGES, getExtToolOutput(OUTPUT_NAME_LEARNER_NUM_ENTRIES_PAGES, dlWikiService,
		    mdlWiki, learnerId, extSessionId.toString(), toolSessionId));
	}
	return map;
}

    public ToolOutput getToolOutput(String name, IMdlWikiService dlWikiService, Long toolSessionId, Long learnerId,
	    MdlWiki getToolOutput, Long extSessionId) {
		if (name != null) {
		    return getExtToolOutput(name, dlWikiService, getToolOutput, learnerId, extSessionId.toString(),
			    toolSessionId);
		}
		return null;
    }

    public ToolOutput getExtToolOutput(String outputName, IMdlWikiService mdlWikiService, MdlWiki mdlWiki, Long userId,
	    String extToolContentId, Long toolSessionId) {
		int number = mdlWikiService.getExternalToolOutputInt(outputName, mdlWiki, userId, extToolContentId,
			toolSessionId);
		return new ToolOutput(outputName, getI18NText(outputName, true), new Long(number));
    }

}
