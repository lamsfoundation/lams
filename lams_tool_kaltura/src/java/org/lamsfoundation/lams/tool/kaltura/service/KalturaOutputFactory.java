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
package org.lamsfoundation.lams.tool.kaltura.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;

/**
 * Output factory for Kaltura tool. Currently it provides only one type of output - the entry that user provided.
 * 
 * @author Andrey Balan
 */
public class KalturaOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject, int definitionType)
	    throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	Class stringArrayClass = new String[] {}.getClass();
	switch (definitionType) {
	case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
	    if (toolContentObject != null) {
		ToolOutputDefinition kalturaEntryDefinition = buildStringOutputDefinition(KalturaConstants.USER_ENTRY_DEFINITION_NAME);
		kalturaEntryDefinition.setShowConditionNameOnly(true);
		definitionMap.put(KalturaConstants.USER_ENTRY_DEFINITION_NAME, kalturaEntryDefinition);
	    }
	case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
	    ToolOutputDefinition allUsersEntriesDefinition = buildComplexOutputDefinition(
		    KalturaConstants.ALL_USERS_ENTRIES_DEFINITION_NAME, stringArrayClass);
	    definitionMap.put(KalturaConstants.ALL_USERS_ENTRIES_DEFINITION_NAME, allUsersEntriesDefinition);
	    break;
	}

	return definitionMap;
    }

    /**
     * Follows {@link KalturaService#getToolOutput(List, Long, Long)}.
     * 
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IKalturaService kalturaService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<String, ToolOutput>();
	if (names == null) {
	    // TODO have a look if we'll have any outputs what should be done
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, kalturaService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IKalturaService kalturaService, Long toolSessionId, Long learnerId) {
	String[] nameParts = splitConditionName(name);
//	if (KalturaConstants.USER_ENTRY_DEFINITION_NAME.equals(nameParts[0])) {
//	    // entry is loaded from DB
//	    Kaltura kaltura = kalturaService.getSessionBySessionId(toolSessionId).getKaltura();
//
//	    KalturaUser user = kalturaService.getUserByUserIdAndSessionId(learnerId, toolSessionId);
//
//	    if (user != null) {
//		NotebookEntry entry = kalturaService.getEntry(user.getEntryUID());
//
//		String value = entry == null ? null : entry.getEntry();
//
//		return new ToolOutput(name, getI18NText(KalturaConstants.USER_ENTRY_DEFINITION_NAME, true), value);
//	    }
//	} else if (KalturaConstants.ALL_USERS_ENTRIES_DEFINITION_NAME.equals(nameParts[0])) {
//	    Set<KalturaUser> users = kalturaService.getSessionBySessionId(toolSessionId).getKalturaUsers();
//	    String[] usersEntries = new String[users.size()];
//	    int userIndex = 0;
//	    for (KalturaUser user : users) {
//		Long entryUid = user.getEntryUID();
//		if (entryUid != null) {
//		    NotebookEntry entry = kalturaService.getEntry(entryUid);
//		    usersEntries[userIndex] = entry.getEntry();
//		}
//		userIndex++;
//	    }
//	    return new ToolOutput(name, getI18NText(KalturaConstants.ALL_USERS_ENTRIES_DEFINITION_NAME, true),
//		    usersEntries, false);
//	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildUserEntryConditionName(String uniquePart) {
	return super.buildConditionName(KalturaConstants.USER_ENTRY_DEFINITION_NAME, uniquePart);
    }
}
