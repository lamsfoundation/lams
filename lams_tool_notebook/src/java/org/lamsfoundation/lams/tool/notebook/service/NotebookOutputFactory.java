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


package org.lamsfoundation.lams.tool.notebook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;

/**
 * Output factory for Notebook tool. Currently it provides only one type of output - the entry that user provided.
 *
 * @author Marcin Cieslak
 */
public class NotebookOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	Class stringArrayClass = new String[] {}.getClass();
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		if (toolContentObject != null) {
		    ToolOutputDefinition notebookEntryDefinition = buildStringOutputDefinition(
			    NotebookConstants.USER_ENTRY_DEFINITION_NAME);
		    Notebook notebook = (Notebook) toolContentObject;
		    // adding all existing conditions
		    notebookEntryDefinition
			    .setConditions(new ArrayList<BranchCondition>(notebook.getConditions()));
		    notebookEntryDefinition.setShowConditionNameOnly(true);
		    definitionMap.put(NotebookConstants.USER_ENTRY_DEFINITION_NAME, notebookEntryDefinition);
		}
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition allUsersEntriesDefinition = buildComplexOutputDefinition(
			NotebookConstants.ALL_USERS_ENTRIES_DEFINITION_NAME, stringArrayClass);
		definitionMap.put(NotebookConstants.ALL_USERS_ENTRIES_DEFINITION_NAME, allUsersEntriesDefinition);
		break;
	}

	return definitionMap;
    }

    /**
     * Follows {@link NotebookService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, INotebookService notebookService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();
	if (names == null) {
	    // output will be set for all the existing conditions
	    Notebook notebook = notebookService.getSessionBySessionId(toolSessionId).getNotebook();
	    Set<NotebookCondition> conditions = notebook.getConditions();
	    for (NotebookCondition condition : conditions) {
		String name = condition.getName();
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, notebookService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, notebookService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, INotebookService notebookService, Long toolSessionId, Long learnerId) {
	String[] nameParts = splitConditionName(name);
	if (NotebookConstants.USER_ENTRY_DEFINITION_NAME.equals(nameParts[0])) {
	    // entry is loaded from DB
	    Notebook notebook = notebookService.getSessionBySessionId(toolSessionId).getNotebook();

	    NotebookUser user = notebookService.getUserByUserIdAndSessionId(learnerId, toolSessionId);

	    if (user != null) {
		String value = user.getNotebookEntry();	

		return new ToolOutput(name, getI18NText(NotebookConstants.USER_ENTRY_DEFINITION_NAME, true), value);
	    }
	} else if (NotebookConstants.ALL_USERS_ENTRIES_DEFINITION_NAME.equals(nameParts[0])) {
	    Set<NotebookUser> users = notebookService.getSessionBySessionId(toolSessionId).getNotebookUsers();
	    String[] usersEntries = new String[users.size()];
	    int userIndex = 0;
	    for (NotebookUser user : users) {
		usersEntries[userIndex] = user.getNotebookEntry();
		userIndex++;
	    }
	    return new ToolOutput(name, getI18NText(NotebookConstants.ALL_USERS_ENTRIES_DEFINITION_NAME, true),
		    usersEntries, false);
	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildUserEntryConditionName(String uniquePart) {
	return super.buildConditionName(NotebookConstants.USER_ENTRY_DEFINITION_NAME, uniquePart);
    }
}