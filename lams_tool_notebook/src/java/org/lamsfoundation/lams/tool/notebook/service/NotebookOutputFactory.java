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
package org.lamsfoundation.lams.tool.notebook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;

/**
 * Output factory for Notebook tool. Currently it provides only one type of
 * output - the entry that user provided.
 * 
 * @author Marcin Cieslak
 */
public class NotebookOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject, int definitionType)
	    throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	if (toolContentObject != null) {
	    ToolOutputDefinition notebookEntryDefinition = buildStringOutputDefinition(NotebookConstants.TEXT_SEARCH_DEFINITION_NAME);
	    Notebook notebook = (Notebook) toolContentObject;
	    // adding all existing conditions
	    notebookEntryDefinition.setDefaultConditions(new ArrayList<BranchCondition>(notebook.getConditions()));
	    // if no conditions were created in the tool instance, a default condition is added;
	    if (notebookEntryDefinition.getDefaultConditions().isEmpty()) {
		NotebookCondition defaultCondition = createDefaultComplexCondition(notebook);
		notebook.getConditions().add(defaultCondition);
		notebookEntryDefinition.getDefaultConditions().add(defaultCondition);
	    }
	    notebookEntryDefinition.setShowConditionNameOnly(true);
	    definitionMap.put(NotebookConstants.TEXT_SEARCH_DEFINITION_NAME, notebookEntryDefinition);
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
	// cached tool output for all text search conditions
	ToolOutput notebookEntryOutput = null;
	if (names == null) {
	    // output will be set for all the existing conditions
	    Notebook notebook = notebookService.getSessionBySessionId(toolSessionId).getNotebook();
	    Set<NotebookCondition> conditions = notebook.getConditions();
	    for (NotebookCondition condition : conditions) {
		String name = condition.getName();
		if (isTextSearchConditionName(name) && notebookEntryOutput != null) {
		    outputs.put(name, notebookEntryOutput);
		} else {
		    ToolOutput output = getToolOutput(name, notebookService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    notebookEntryOutput = output;
			}
		    }
		}
	    }
	} else {
	    for (String name : names) {
		if (isTextSearchConditionName(name) && notebookEntryOutput != null) {
		    outputs.put(name, notebookEntryOutput);
		} else {
		    ToolOutput output = getToolOutput(name, notebookService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    notebookEntryOutput = output;
			}
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, INotebookService chatService, Long toolSessionId, Long learnerId) {
	if (isTextSearchConditionName(name)) {
	    // entry is loaded from DB
	    Notebook notebook = chatService.getSessionBySessionId(toolSessionId).getNotebook();

	    NotebookUser user = chatService.getUserByUserIdAndSessionId(learnerId, toolSessionId);

	    if (user != null) {
		NotebookEntry entry = chatService.getEntry(user.getEntryUID());

		String value = entry == null ? null : entry.getEntry();

		return new ToolOutput(name, getI18NText(NotebookConstants.TEXT_SEARCH_DEFINITION_NAME, true), value);
	    }
	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildConditionName(String uniquePart) {
	return super.buildConditionName(NotebookConstants.TEXT_SEARCH_DEFINITION_NAME, uniquePart);
    }

    private boolean isTextSearchConditionName(String name) {
	return name != null && name.startsWith(NotebookConstants.TEXT_SEARCH_DEFINITION_NAME);
    }

    /**
     * Creates a default condition so teachers know how to use complex
     * conditions for this tool.
     * 
     * @param notebook
     *                content of the tool
     * @return default notebook condition
     */
    protected NotebookCondition createDefaultComplexCondition(Notebook notebook) {
	String name = buildConditionName(NotebookConstants.TEXT_SEARCH_DEFINITION_NAME, notebook.getToolContentId()
		.toString());
	// Default condition checks if the text contains word "LAMS"
	return new NotebookCondition(null, null, 1, name, getI18NText(
		NotebookConstants.TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY, false), "LAMS", null, null, null);
    }
}