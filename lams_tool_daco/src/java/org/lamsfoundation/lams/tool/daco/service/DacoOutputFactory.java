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


package org.lamsfoundation.lams.tool.daco.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;

/**
 * Creates the output definitions for Daco. Currently it provides two types of output - an user answers represented by
 * an map "topic creation date" -> "all learner's answers to that topic" (output type "OUTPUT_COMPLEX") and range
 * definition for number of words entered by an user.
 */
public class DacoOutputFactory extends OutputFactory {

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {

	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition numberOfPostsDefinition = buildRangeDefinition(
		DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, new Long(0), null);
	definitionMap.put(DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, numberOfPostsDefinition);

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IDacoService dacoService, Long toolSessionId,
	    Long learnerId) {
	TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME)) {
	    map.put(DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME,
		    getNumRecords(dacoService, learnerId, toolSessionId));
	}
	return map;

    }

    public ToolOutput getToolOutput(String name, IDacoService dacoService, Long toolSessionId, Long learnerId) {

	if (name != null && name.equals(DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME)) {
	    return getNumRecords(dacoService, learnerId, toolSessionId);
	}
	return null;

    }

    private ToolOutput getNumRecords(IDacoService dacoService, Long learnerId, Long toolSessionId) {
	int num = dacoService.getRecordNum(learnerId, toolSessionId);
	return new ToolOutput(DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME,
		getI18NText(DacoConstants.LEARNER_NUM_POSTS_DEFINITION_NAME, true), new Long(num));
    }
}
