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
package org.lamsfoundation.lams.tool.mdlesn.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLesson;
import org.lamsfoundation.lams.tool.mdlesn.service.IMdlLessonService;

/**
 * Creates the output definitions for mdllesson tool Adapter tool.
 */
public class MdlLessonOutputFactory extends OutputFactory {

    protected final static String OUTPUT_NAME_LEARNER_GRADE = "learner.percentage.grade";

    public MdlLessonOutputFactory() {
    }

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject, int definitionType)
	    throws ToolException {
		TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	
		ToolOutputDefinition definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_GRADE, new Long(0), null);
		definitionMap.put(OUTPUT_NAME_LEARNER_GRADE, definition);
	
		return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMdlLessonService dlLessonService,
	    Long toolSessionId, Long learnerId, MdlLesson mdlLesson, Long extSessionId) {

		TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
		if (names == null || names.contains(OUTPUT_NAME_LEARNER_GRADE)) {
		    map.put(OUTPUT_NAME_LEARNER_GRADE, getExtToolOutput(OUTPUT_NAME_LEARNER_GRADE, dlLessonService,
			    mdlLesson, learnerId, extSessionId.toString(), toolSessionId));
		}
		return map;
    }

    public ToolOutput getToolOutput(String name, IMdlLessonService dlLessonService, Long toolSessionId, Long learnerId,
	    MdlLesson getToolOutput, Long extSessionId) {
		if (name != null) {
		    return getExtToolOutput(name, dlLessonService, getToolOutput, learnerId, extSessionId.toString(),
			    toolSessionId);
		}
		return null;
    }

    public ToolOutput getExtToolOutput(String outputName, IMdlLessonService mdlLessonService, MdlLesson mdlLesson, Long userId,
	    String extToolContentId, Long toolSessionId) {
		int number = mdlLessonService.getExternalToolOutputInt(outputName, mdlLesson, userId, extToolContentId,
			toolSessionId);
		return new ToolOutput(outputName, getI18NText(outputName, true), new Long(number));
    }

}
