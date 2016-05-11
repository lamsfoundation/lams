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


package org.eucm.lams.tool.eadventure.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureCondition;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureParam;
import org.eucm.lams.tool.eadventure.model.EadventureSession;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.model.EadventureVars;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;

public class EadventureOutputFactory extends OutputFactory {

    // don't translate these strings
    protected final static String OUTPUT_NAME_LEARNER_TOTAL_SCORE = EadventureConstants.VAR_NAME_SCORE;
    protected final static String OUTPUT_NAME_LEARNER_TOTAL_TIME = EadventureConstants.VAR_NAME_TOTAL_TIME;
    protected final static String OUTPUT_NAME_LEARNER_REAL_TIME = EadventureConstants.VAR_NAME_REAL_TIME;
    protected final static String OUTPUT_NAME_LEARNER_COMPLETED = EadventureConstants.VAR_NAME_COMPLETED;
    protected final static String OUTPUT_NAME_TOOL_CONDITION = "tool.condition";

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		ToolOutputDefinition definition = null;
		// add default outputs
		definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_TOTAL_SCORE, new Long(0), new Long(0), true);
		definitionMap.put(OUTPUT_NAME_LEARNER_TOTAL_SCORE, definition);
		definition = buildBooleanOutputDefinition(OUTPUT_NAME_LEARNER_COMPLETED);
		definitionMap.put(OUTPUT_NAME_LEARNER_COMPLETED, definition);
		definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_TOTAL_TIME, new Long(0), new Long(0), false);
		definitionMap.put(OUTPUT_NAME_LEARNER_TOTAL_TIME, definition);
		definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_REAL_TIME, new Long(0), new Long(0), false);
		definitionMap.put(OUTPUT_NAME_LEARNER_REAL_TIME, definition);

		Eadventure ead = (Eadventure) toolContentObject;
		// TODO cambiar por ead.getParamsWithoutDeafault();
		Set<EadventureParam> eadParams = ead.getParams();
		for (EadventureParam param : eadParams) {
		    String text;
		    // skip default outputs
		    if (isDefaultOutput(param.getType())) {
			if (param.getType().equals(EadventureConstants.PARAMS_TYPE_BOOLEAN)) {
			    definition = buildBooleanOutputDefinition(param.getName());
			    definition.setDescription(
				    getI18NText("output.desc.learner.user.defined", false) + param.getName());
			} else if (param.getType().equals(EadventureConstants.PARAMS_TYPE_INTEGER)) {
			    definition = buildRangeDefinition(param.getName(), new Long(0), null);
			    definition.setDescription(
				    getI18NText("output.desc.learner.user.defined", false) + param.getName());
			}
			if (param.getType().equals(EadventureConstants.PARAMS_TYPE_STRING)) {
			    definition = buildRangeDefinition(param.getName(), new Long(0), null);
			    definition.setDescription(
				    getI18NText("output.desc.learner.user.defined", false) + param.getName());
			}
			definitionMap.put(param.getName(), definition);
		    }
		}

		// add the conditions defined at authoring
		if (ead.getConditions() != null && ead.getConditions().size() > 0) {
		    ToolOutputDefinition definitionOthers = buildBooleanSetOutputDefinition(OUTPUT_NAME_TOOL_CONDITION);
		    if (definitionOthers.getDefaultConditions() == null) {
			definitionOthers.setDefaultConditions(new ArrayList<BranchCondition>());
		    }

		    List<BranchCondition> defaultConditions = definitionOthers.getDefaultConditions();
		    String trueString = Boolean.TRUE.toString();

		    Iterator<EadventureCondition> iter2 = ead.getConditions().iterator();
		    while (iter2.hasNext()) {
			EadventureCondition condition = iter2.next();
			String name = buildConditionName(OUTPUT_NAME_TOOL_CONDITION, condition.getName());
			defaultConditions.add(new BranchCondition(null, null, condition.getSequenceId(), name,
				condition.getName(), OutputType.OUTPUT_BOOLEAN.toString(), null, null, trueString));
		    }

		    definitionOthers.setShowConditionNameOnly(Boolean.TRUE);

		    definitionMap.put(OUTPUT_NAME_TOOL_CONDITION, definitionOthers);
		}
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		break;
	}

	return definitionMap;
    }

    private boolean isDefaultOutput(String output) {
	if (output.equals(OUTPUT_NAME_LEARNER_TOTAL_SCORE) || output.equals(OUTPUT_NAME_LEARNER_TOTAL_TIME)
		|| output.equals(OUTPUT_NAME_LEARNER_REAL_TIME) || output.equals(OUTPUT_NAME_LEARNER_COMPLETED)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Follows {@link EadventureServiceImp#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IEadventureService eadventureService,
	    Long toolSessionId, Long learnerId) {

	//TODO checkear el orden
	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();

	EadventureSession session = eadventureService.getEadventureSessionBySessionId(toolSessionId);

	if ((session != null) && (session.getEadventure() != null)) {
	    Eadventure ead = session.getEadventure();

	    TreeMap<String, ToolOutput> output = null;
	    if (names == null) {
		return output;
	    } else {
		output = new TreeMap<String, ToolOutput>();
		for (String name : names) {
		    ToolOutput newOutput = getToolOutput(name, eadventureService, toolSessionId, learnerId);
		    if (newOutput != null) {
			output.put(name, newOutput);
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IEadventureService eadventureService, Long toolSessionId,
	    Long learnerId) {

	if (name != null) {
	    EadventureSession session = eadventureService.getEadventureSessionBySessionId(toolSessionId);

	    EadventureUser eadU = eadventureService.getUser(learnerId);

	    if ((session != null) && (session.getEadventure() != null)) {
		Eadventure ead = //session.getEadventure();
			eadventureService.getEadventureBySessionId(toolSessionId);

		if (name.equals(OUTPUT_NAME_LEARNER_TOTAL_SCORE)) {
		    return getTotalScore(eadventureService, learnerId, ead, session.getSessionId());
		} else if (name.equals(OUTPUT_NAME_LEARNER_TOTAL_TIME)) {
		    return getTotalTime(eadventureService, learnerId, ead, session.getSessionId());
		} else if (name.equals(OUTPUT_NAME_LEARNER_REAL_TIME)) {
		    return getRealTime(eadventureService, learnerId, ead, session.getSessionId());
		} else if (name.equals(OUTPUT_NAME_LEARNER_COMPLETED)) {
		    return getCompleted(eadventureService, learnerId, ead, session.getSessionId());
		} else if (name.startsWith(OUTPUT_NAME_TOOL_CONDITION)) {
		    boolean check;
		    String[] dcNames = splitConditionName(name);
		    if (dcNames[1] == null || dcNames[1].length() == 0) {
			log.error("Wrong name for tool output " + OUTPUT_NAME_TOOL_CONDITION
				+ ". Returning false. Condition name was: " + name);
			check = false;
		    } else {
			check = eadventureService.checkCondition(dcNames[1], toolSessionId, learnerId);
		    }
		    return new ToolOutput(name, getI18NText(OUTPUT_NAME_TOOL_CONDITION, true), check);
		} else {
		    Set<EadventureParam> eadParams = ead.getParams();
		    for (EadventureParam param : eadParams) {
			if (name.equals(param.getName())) {
			    return getUserDefinedParam(eadventureService, learnerId, ead, session.getSessionId(),
				    param);
			}
		    }
		}
	    }
	}
	return null;

    }

    private ToolOutput getUserDefinedParam(IEadventureService eadventureService, Long learnerId, Eadventure ead,
	    Long sessionId, EadventureParam param) {
	EadventureUser user = eadventureService.getUserByIDAndSession(learnerId, sessionId);
	EadventureItemVisitLog log = eadventureService.getEadventureItemLog(ead.getUid(), user.getUserId());
	if (log == null) {
	    return new ToolOutput(OUTPUT_NAME_LEARNER_TOTAL_SCORE, getI18NText(OUTPUT_NAME_LEARNER_TOTAL_SCORE, true),
		    0);
	}
	EadventureVars var = eadventureService.getEadventureVars(log.getUid(), param.getName());

	if (param.getType().equals("boolean")) {
	    if (var != null) {
		return new ToolOutput(var.getName(), "description", Boolean.parseBoolean(var.getValue()));
	    } else {
		return new ToolOutput(param.getName(), "description", false);
	    }
	} else

	if (param.getType().equals("integer")) {
	    if (var != null) {
		return new ToolOutput(var.getName(), "description", Integer.parseInt(var.getValue()));
	    } else {
		return new ToolOutput(param.getName(), "description", 0);
	    }

	} else {
	    if (var != null) {
		return new ToolOutput(var.getName(), "description", var.getValue());
	    } else {
		return new ToolOutput(param.getName(), "description", "");
	    }
	}
    }

    /**
     * Get total score for a user. Will always return a ToolOutput object.
     */

    private ToolOutput getTotalScore(IEadventureService eadventureService, Long learnerId, Eadventure ead,
	    Long sessionId) {

	EadventureUser user = eadventureService.getUserByIDAndSession(learnerId, sessionId);

	//eadventureService.getUserByIDAndContent(learnerId, ead.getContentId());
	EadventureItemVisitLog log = eadventureService.getEadventureItemLog(ead.getUid(), user.getUserId());
	if (log == null) {
	    return new ToolOutput(OUTPUT_NAME_LEARNER_TOTAL_SCORE, getI18NText(OUTPUT_NAME_LEARNER_TOTAL_SCORE, true),
		    0);
	}
	EadventureVars var = eadventureService.getEadventureVars(log.getUid(), OUTPUT_NAME_LEARNER_TOTAL_SCORE);

	float totalScore = (var == null || var.getValue() == null) ? 0 : Integer.parseInt(var.getValue());

	return new ToolOutput(OUTPUT_NAME_LEARNER_TOTAL_SCORE, getI18NText(OUTPUT_NAME_LEARNER_TOTAL_SCORE, true),
		totalScore);

    }

    /**
     * Get time taken for a user. Will always return a ToolOutput object.
     */

    private ToolOutput getTotalTime(IEadventureService eadventureService, Long learnerId, Eadventure ead,
	    Long sessionId) {

	EadventureUser user = eadventureService.getUserByIDAndSession(learnerId, sessionId);

	//eadventureService.getUserByIDAndContent(learnerId, ead.getContentId());
	EadventureItemVisitLog log = eadventureService.getEadventureItemLog(ead.getUid(), user.getUserId());
	if (log == null) {
	    return new ToolOutput(OUTPUT_NAME_LEARNER_TOTAL_TIME, getI18NText(OUTPUT_NAME_LEARNER_TOTAL_TIME, true), 0);
	}
	EadventureVars var = eadventureService.getEadventureVars(log.getUid(), OUTPUT_NAME_LEARNER_TOTAL_TIME);

	float totalTime = (var == null || var.getValue() == null) ? 0 : Integer.parseInt(var.getValue());

	return new ToolOutput(OUTPUT_NAME_LEARNER_TOTAL_TIME, getI18NText(OUTPUT_NAME_LEARNER_TOTAL_TIME, true),
		totalTime);

    }

    /**
     * Get time taken for a user. Will always return a ToolOutput object.
     */

    private ToolOutput getRealTime(IEadventureService eadventureService, Long learnerId, Eadventure ead,
	    Long sessionId) {

	EadventureUser user = eadventureService.getUserByIDAndSession(learnerId, sessionId);

	//eadventureService.getUserByIDAndContent(learnerId, ead.getContentId());
	EadventureItemVisitLog log = eadventureService.getEadventureItemLog(ead.getUid(), user.getUserId());
	if (log == null) {
	    return new ToolOutput(OUTPUT_NAME_LEARNER_REAL_TIME, getI18NText(OUTPUT_NAME_LEARNER_REAL_TIME, true), 0);
	}
	EadventureVars var = eadventureService.getEadventureVars(log.getUid(), OUTPUT_NAME_LEARNER_REAL_TIME);

	float totalTime = (var == null || var.getValue() == null) ? 0 : Integer.parseInt(var.getValue());

	return new ToolOutput(OUTPUT_NAME_LEARNER_REAL_TIME, getI18NText(OUTPUT_NAME_LEARNER_REAL_TIME, true),
		totalTime);

    }

    /**
     * Completed when it is required. Will always return a ToolOutput object.
     */

    private ToolOutput getCompleted(IEadventureService eadventureService, Long learnerId, Eadventure ead,
	    Long sessionId) {

	EadventureUser user = eadventureService.getUserByIDAndSession(learnerId, sessionId);

	//eadventureService.getUserByIDAndContent(learnerId, ead.getContentId());
	EadventureItemVisitLog log = eadventureService.getEadventureItemLog(ead.getUid(), user.getUserId());
	if (log == null) {
	    return new ToolOutput(OUTPUT_NAME_LEARNER_COMPLETED, getI18NText(OUTPUT_NAME_LEARNER_COMPLETED, true), 0);
	}
	EadventureVars var = eadventureService.getEadventureVars(log.getUid(), OUTPUT_NAME_LEARNER_COMPLETED);

	boolean totalScore = (var == null || var.getValue() == null) ? false : Boolean.parseBoolean(var.getValue());

	return new ToolOutput(OUTPUT_NAME_LEARNER_COMPLETED, getI18NText(OUTPUT_NAME_LEARNER_COMPLETED, true),
		totalScore);

    }

}
