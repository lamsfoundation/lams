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

package org.lamsfoundation.lams.tool.peerreview.service;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.util.EmailAnalysisBuilder;

import java.util.*;

public class PeerreviewOutputFactory extends OutputFactory {

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) {
	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<>();
	if (definitionType == ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION) {
	    ToolOutputDefinition definition = buildRangeDefinition(
		    PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE, 0L, 5L);
	    definitionMap.put(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE, definition);
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IPeerreviewService peerreviewService,
	    Long toolSessionId, Long learnerId) {
	TreeMap<String, ToolOutput> output = new TreeMap<>();
	Peerreview peerreview = peerreviewService.getPeerreviewBySessionId(toolSessionId);

	if (names == null || names.contains(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE)) {
	    ToolOutput toolOutput = buildLearnerRatingAverageOutput(peerreviewService, peerreview.getContentId(),
		    toolSessionId, learnerId);
	    output.put(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE, toolOutput);
	}

	return output;
    }

    public ToolOutput getToolOutput(String name, IPeerreviewService peerreviewService, Long toolSessionId,
	    Long learnerId) {
	Peerreview peerreview = peerreviewService.getPeerreviewBySessionId(toolSessionId);

	if (name != null && name.equals(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE)) {
	    return buildLearnerRatingAverageOutput(peerreviewService, peerreview.getContentId(), toolSessionId,
		    learnerId);
	}

	return null;
    }

    public List<ToolOutput> getToolOutputs(String name, IPeerreviewService peerreviewService, Long toolContentId) {
	List<ToolOutput> outputs = new ArrayList<>();

	if (name != null && name.equals(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE)) {
	    List<PeerreviewSession> sessions = peerreviewService.getPeerreviewSessionsByConentId(toolContentId);
	    for (PeerreviewSession session : sessions) {

		double rating = 0d;
		Map<Long, EmailAnalysisBuilder.LearnerData> learnersData = peerreviewService.getLearnerData(
			toolContentId, session.getSessionId());
		for (EmailAnalysisBuilder.LearnerData learnerData : learnersData.values()) {
		    int criteriaCount = 0;
		    for (EmailAnalysisBuilder.LearnerData.SingleCriteriaData criteriaData : learnerData.criteriaDataMap.values()) {
			if (criteriaData.peerRatingExcSelf != null) {
			    rating += criteriaData.peerRatingExcSelf;
			    criteriaCount++;
			}
		    }
		    if (criteriaCount > 0) {
			rating = rating / criteriaCount;
		    }
		}
		outputs.add(new ToolOutput(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE,
			getI18NText(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE, true), rating));

	    }
	}
	return outputs;
    }

    private ToolOutput buildLearnerRatingAverageOutput(IPeerreviewService peerreviewService, Long toolContentId,
	    Long toolSessionId, Long learnerId) {
	double rating = 0d;
	Map<Long, EmailAnalysisBuilder.LearnerData> learnersData = peerreviewService.getLearnerData(toolContentId,
		toolSessionId);
	EmailAnalysisBuilder.LearnerData learnerData = learnersData.get(learnerId);
	if (learnerData != null) {
	    int criteriaCount = 0;
	    for (EmailAnalysisBuilder.LearnerData.SingleCriteriaData criteriaData : learnerData.criteriaDataMap.values()) {
		if (criteriaData.peerRatingExcSelf != null) {
		    rating += criteriaData.peerRatingExcSelf;
		    criteriaCount++;
		}
	    }
	    if (criteriaCount > 0) {
		rating = rating / criteriaCount;
	    }
	}
	return new ToolOutput(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE,
		getI18NText(PeerreviewConstants.OUTPUT_NAME_LEARNER_RATING_AVERAGE, true), rating);
    }
}