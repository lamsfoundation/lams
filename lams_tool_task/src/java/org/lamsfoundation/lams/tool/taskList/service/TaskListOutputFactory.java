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
package org.lamsfoundation.lams.tool.taskList.service;  

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
 
/**
 * Creates the output definitions for forum. Eventually there will be a definition 
 * that outputs some or all of the forum queries, but for now there are just a couple of
 * simple definitions so that we can try various features of the tool output based 
 * branching.
 * 
 * @author Andrey Balan
 */
public class TaskListOutputFactory extends OutputFactory{

	/** The number of posts the learner has made in one forum activity. */
	protected final static String OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED = "learner.number.of.tasks.completed";
	
	/** 
	 * {@inheritDoc}
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject) throws ToolException {
		TreeMap<String, ToolOutputDefinition> definitionMap =  new TreeMap<String, ToolOutputDefinition>();
		
		ToolOutputDefinition definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, new Long(0), null);
		definitionMap.put(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, definition);

		return definitionMap;
	}

	public SortedMap<String, ToolOutput> getToolOutput(List<String> names, ITaskListService taskListService, Long toolSessionId, Long learnerId) {
		
		TreeMap<String,ToolOutput> map = new TreeMap<String,ToolOutput>();
		if ( names == null || names.contains(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED)) {
			map.put(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED,getNumTasksCompleted(taskListService, learnerId, toolSessionId));
		}
		return map;

	}

	public ToolOutput getToolOutput(String name, ITaskListService taskListService, Long toolSessionId, Long learnerId) {
		
		if ( name != null && name.equals(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED)) {
			 return getNumTasksCompleted(taskListService, learnerId, toolSessionId);
		}
		return null;

	}

	private ToolOutput getNumTasksCompleted(ITaskListService taskListService, Long learnerId, Long toolSessionId) {
		int num = taskListService.getNumTasksCompletedByUser(toolSessionId, learnerId);		
		return new ToolOutput(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, getI18NText(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, true), new Long(num));
	}
}
