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
package org.lamsfoundation.lams.tool.mc.service;

import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputDefinitionFactory;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;

public class MCOutputDefinitionFactory extends OutputDefinitionFactory {

	protected static final String OUTPUT_NAME_LEARNER_MARK = "learner.mark";
	protected static final String OUTPUT_NAME_LEARNER_ALL_CORRECT = "learner.all.correct";
	
	/** 
	 * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)  {
		
		TreeMap<String, ToolOutputDefinition> definitionMap =  new TreeMap<String, ToolOutputDefinition>();
		ToolOutputDefinition definition = buildBooleanOutputDefinition(OUTPUT_NAME_LEARNER_ALL_CORRECT);
		definitionMap.put(OUTPUT_NAME_LEARNER_ALL_CORRECT, definition);

		if ( toolContentObject != null ) {
			McContent content = (McContent) toolContentObject;
			
			definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_MARK,
					content.getMcQueContents().size() > 0 ? new Long(1) : new Long(0),
					new Long ( content.getTotalMarksPossible().longValue() ) );
			definitionMap.put(OUTPUT_NAME_LEARNER_MARK, definition);
		} else {
			log.error("Unable to build content based output definitions for Multiple Choice as no tool content object supplied. Only including the definitions that do not need any content.");
		}

		return definitionMap;
	}

}
