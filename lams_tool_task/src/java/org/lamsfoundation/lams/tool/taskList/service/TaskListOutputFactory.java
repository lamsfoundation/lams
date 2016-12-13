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


package org.lamsfoundation.lams.tool.taskList.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;

/**
 * Creates the output definitions for forum. Eventually there will be a definition
 * that outputs some or all of the forum queries, but for now there are just a couple of
 * simple definitions so that we can try various features of the tool output based
 * branching.
 *
 * @author Andrey Balan
 */
public class TaskListOutputFactory extends OutputFactory {

    /** The number of posts the learner has made in one forum activity. */
    protected final static String OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED = "learner.number.of.tasks.completed";

    protected final static String OUTPUT_NAME_TOOL_CONDITION = "tool.condition";

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition simpleDefinition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED,
		new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, simpleDefinition);

	if (toolContentObject != null) {

	    TaskList taskList = (TaskList) toolContentObject;
	    ToolOutputDefinition definition = buildBooleanSetOutputDefinition(OUTPUT_NAME_TOOL_CONDITION);
	    if (definition.getConditions() == null) {
		definition.setConditions(new ArrayList<BranchCondition>());
	    }

	    List<BranchCondition> conditions = definition.getConditions();
	    String trueString = Boolean.TRUE.toString();

	    Iterator<TaskListCondition> iter2 = taskList.getConditions().iterator();
	    while (iter2.hasNext()) {
		TaskListCondition condition = iter2.next();
		String name = buildConditionName(OUTPUT_NAME_TOOL_CONDITION, condition.getName());
		conditions.add(new BranchCondition(null, null, condition.getSequenceId(), name,
			condition.getName(), OutputType.OUTPUT_BOOLEAN.toString(), null, null, trueString));
	    }

	    definitionMap.put(OUTPUT_NAME_TOOL_CONDITION, definition);
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, ITaskListService taskListService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> output = null;
	if (names == null) {
	    output = createAllDisplayOrderOutputs(taskListService, toolSessionId, learnerId);
	} else {
	    output = new TreeMap<String, ToolOutput>();
	    for (String name : names) {
		ToolOutput newOutput = getToolOutput(name, taskListService, toolSessionId, learnerId);
		if (newOutput != null) {
		    output.put(name, newOutput);
		}
	    }
	}
	return output;

    }

    public ToolOutput getToolOutput(String name, ITaskListService taskListService, Long toolSessionId, Long learnerId) {

	if (name != null && name.equals(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED)) {

	    int num = taskListService.getNumTasksCompletedByUser(toolSessionId, learnerId);
	    return new ToolOutput(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED,
		    getI18NText(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, true), new Long(num));

	} else if (name.startsWith(OUTPUT_NAME_TOOL_CONDITION)) {

	    boolean check;
	    String[] dcNames = splitConditionName(name);
	    if (dcNames[1] == null || dcNames[1].length() == 0) {
		log.error("Wrong name for tool output " + OUTPUT_NAME_TOOL_CONDITION
			+ ". Returning false. Condition name was: " + name);
		check = false;
	    } else {
		check = taskListService.checkCondition(dcNames[1], toolSessionId, learnerId);
	    }
	    return new ToolOutput(name, getI18NText(OUTPUT_NAME_TOOL_CONDITION, true), check);

	}
	return null;

    }

    /**
     * Check the display order embedded in the condition name. The name MUST start with
     * OUTPUT_NAME_NOMINATION_SELECTION.
     */
    private TreeMap<String, ToolOutput> createAllDisplayOrderOutputs(ITaskListService taskListService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> output = null;

	int num = taskListService.getNumTasksCompletedByUser(toolSessionId, learnerId);
	ToolOutput toolOutput = new ToolOutput(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED,
		getI18NText(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, true), new Long(num));
	output.put(OUTPUT_NAME_LEARNER_NUM_TASKS_COMPLETED, toolOutput);

	String i18nDescription = getI18NText(OUTPUT_NAME_TOOL_CONDITION, true);
	TaskList taskList = taskListService.getTaskListBySessionId(toolSessionId);
	Set<TaskListCondition> conditions = taskList.getConditions();
	for (TaskListCondition condition : conditions) {
	    String name = buildConditionName(OUTPUT_NAME_TOOL_CONDITION, condition.getName());
	    boolean check = taskListService.checkCondition(condition.getName(), toolSessionId, learnerId);
	    output.put(name, new ToolOutput(name, i18nDescription, check));
	}

	return output;
    }

}
