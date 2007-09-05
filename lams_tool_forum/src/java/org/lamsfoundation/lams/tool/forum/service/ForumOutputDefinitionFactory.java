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
package org.lamsfoundation.lams.tool.forum.service;

import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputDefinitionFactory;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;

/**
 * Creates the output definitions for forum. Eventually there will be a definition 
 * that outputs some or all of the forum queries, but for now there are just a couple of
 * simple definitions so that we can try various features of the tool output based 
 * branching.
 */
public class ForumOutputDefinitionFactory extends OutputDefinitionFactory {

	/** The number of posts the learner has made in one forum activity. */
	protected final static String OUTPUT_NAME_LEARNER_NUM_POSTS = "learner.number.of.posts";
	
	/** 
	 * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(
			Object toolContentObject) throws ToolException {
		TreeMap<String, ToolOutputDefinition> definitionMap =  new TreeMap<String, ToolOutputDefinition>();
		
		ToolOutputDefinition definition = buildLongOutputDefinition(OUTPUT_NAME_LEARNER_NUM_POSTS);
		definitionMap.put(OUTPUT_NAME_LEARNER_NUM_POSTS, definition);

		return definitionMap;
	}

}
