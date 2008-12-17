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
package org.lamsfoundation.lams.tool.mdchce.service;

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
import org.lamsfoundation.lams.tool.mdchce.dto.MdlChoiceOutputDTO;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoice;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceUser;


/**
 * Creates the output definitions for mdlchoice tool Adapter tool.
 */
public class MdlChoiceOutputFactory extends OutputFactory {

    protected final static String LEARNER_CHOICE_OUTPUT = "learner.choice.output";

    public MdlChoiceOutputFactory() {
    }

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
	    throws ToolException {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	if (toolContentObject != null) {
	    List<MdlChoiceOutputDTO> choices = (List<MdlChoiceOutputDTO>) toolContentObject;
	    
	    if (choices == null || choices.size() == 0)
	    {
		log.error("Unable to build output definitions for moodel choice, no options have been added yet");
	    }
	    else{
		ToolOutputDefinition definition = buildBooleanSetOutputDefinition(LEARNER_CHOICE_OUTPUT);
		    if (definition.getDefaultConditions() == null)
			definition.setDefaultConditions(new ArrayList<BranchCondition>());

		    List<BranchCondition> defaultConditions = definition.getDefaultConditions();

		    int order = 1;
		    for (MdlChoiceOutputDTO dto : choices) {
			String name = buildConditionName(LEARNER_CHOICE_OUTPUT, "" + dto.getId());
			defaultConditions.add(new BranchCondition(null, null, new Integer(order++), name, dto.getChoice(),
				OutputType.OUTPUT_BOOLEAN.toString(), null, null, Boolean.TRUE.toString()));
			order++;
		    }
		    
		    definition.setShowConditionNameOnly(Boolean.TRUE);
		    definitionMap.put(LEARNER_CHOICE_OUTPUT, definition);
	    }
	} else {
	    log.error("Unable to build output definitions for Vote as no tool content object supplied.");
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMdlChoiceService dlChoiceService,
	    Long toolSessionId, Long learnerId, MdlChoice mdlChoice, Long extSessionId) {

	TreeMap<String, ToolOutput> map = new TreeMap<String, ToolOutput>();
	if (names == null || names.contains(LEARNER_CHOICE_OUTPUT)) {
	    map.put(LEARNER_CHOICE_OUTPUT, getExtToolOutput(LEARNER_CHOICE_OUTPUT, dlChoiceService,
		    mdlChoice, learnerId, extSessionId.toString(), toolSessionId));
	}
	return map;

    }

    public ToolOutput getToolOutput(String name, IMdlChoiceService dlChoiceService, Long toolSessionId, Long learnerId,
	    MdlChoice getToolOutput, Long extSessionId) {
	if (name != null) {
	    return getExtToolOutput(name, dlChoiceService, getToolOutput, learnerId, extSessionId.toString(),
		    toolSessionId);
	}
	return null;

    }

    public ToolOutput getExtToolOutput(String outputName, IMdlChoiceService mdlChoiceService, MdlChoice mdlChoice,
	    Long userId, String extToolContentId, Long toolSessionId) {
	
	MdlChoiceUser user = mdlChoiceService.getUserByUserIdAndSessionId(userId, toolSessionId);
	boolean condition = this.isChoiceSelected(outputName, mdlChoiceService, mdlChoice, userId, extToolContentId, toolSessionId);
	return new ToolOutput(outputName, getI18NText(outputName, true), condition);
    }
    
    
    private boolean isChoiceSelected(String outputName, IMdlChoiceService mdlChoiceService, MdlChoice mdlChoice,
	    Long userId, String extToolContentId, Long toolSessionId)
    {
	String[] dcNames = splitConditionName(outputName);
	if (dcNames[1] == null || dcNames[1].length() == 0) {
	    log.error("Unable to convert the display order to an int for tool output "
		    + LEARNER_CHOICE_OUTPUT
		    + ". Returning false. Name doesn't contain the display order. Condition name was: " + outputName);
	    return false;
	}
	
	String choiceOutputName = "";
	String choiceId = "";
	try {
	    choiceOutputName = dcNames[0];
	    choiceId = dcNames[1];
	} catch (Exception e) {
	    log.error("Problem retrieving outputs"
		    + LEARNER_CHOICE_OUTPUT
		    + ". Returning false. Number format exception thrown. Condition name was: " + outputName, e);
	    return false;
	}

	if (userId != null) {
	    
	    return mdlChoiceService.getExternalToolOutputBoolean(choiceOutputName, mdlChoice, userId, extToolContentId,
			toolSessionId, choiceId); 
	}
	return false;
    }
    

}
