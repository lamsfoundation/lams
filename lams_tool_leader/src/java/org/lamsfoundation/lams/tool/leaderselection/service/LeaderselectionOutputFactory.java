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


package org.lamsfoundation.lams.tool.leaderselection.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;

/**
 * Output factory for Leaderselection tool. Currently it provides only one output - boolean whether user is a leader and
 * another dataflow type of output - the leader's userId.
 */
public class LeaderselectionOutputFactory extends OutputFactory {

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	switch (definitionType) {
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		ToolOutputDefinition definition = buildBooleanOutputDefinition(
			LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER);
		definitionMap.put(LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER, definition);
		break;
	    case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		ToolOutputDefinition leaderDefinition = buildRangeDefinition(
			LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID, new Long(0), null);
		definitionMap.put(LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID, leaderDefinition);
		break;
	}

	return definitionMap;
    }

    /**
     * Follows {@link LeaderselectionService#getToolOutput(List, Long, Long)}.
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names,
	    ILeaderselectionService leaderselectionService, Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();

	if (names == null || names.contains(LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER)) {
	    outputs.put(LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER,
		    getToolOutput(LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER, leaderselectionService,
			    toolSessionId, learnerId));
	}
	if (names == null || names.contains(LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID)) {
	    outputs.put(LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID,
		    getToolOutput(LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID, leaderselectionService,
			    toolSessionId, learnerId));
	}

	return outputs;

    }

    public ToolOutput getToolOutput(String name, ILeaderselectionService leaderselectionService, Long toolSessionId,
	    Long learnerId) {
	if (name.equals(LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER)) {

	    boolean isUserLeader = leaderselectionService.isUserLeader(learnerId, toolSessionId);
	    return new ToolOutput(name, getI18NText(LeaderselectionConstants.OUTPUT_NAME_IS_LEARNER_A_LEADER, true),
		    isUserLeader);

	} else if (name.equals(LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID)) {
	    LeaderselectionSession session = leaderselectionService.getSessionBySessionId(toolSessionId);
	    LeaderselectionUser groupLeader = session.getGroupLeader();
	    if (groupLeader != null) {
		return new ToolOutput(name, getI18NText(LeaderselectionConstants.OUTPUT_NAME_LEADER_USERID, true),
			groupLeader.getUserId());
	    }

	}
	return null;
    }
}
