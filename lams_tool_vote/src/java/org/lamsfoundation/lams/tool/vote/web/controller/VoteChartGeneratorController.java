/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Prepares data for charts.
 *
 * @author Marcin Cieslak
 */
@Controller
public class VoteChartGeneratorController {
    @Autowired
    private IVoteService voteService;

    @RequestMapping(path = "/chartGenerator")
    @ResponseBody
    public String start(HttpServletRequest request, HttpServletResponse response) {
	String currentSessionId = request.getParameter("currentSessionId");

	Map<Long, String> nominationNames = new HashMap<>();
	Map<Long, Double> nominationVotes = new HashMap<>();

	//request for the all session summary
	if ("0".equals(currentSessionId)) {
	    long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	    LinkedList<SessionDTO> sessionDTOs = voteService.getSessionDTOs(toolContentID);

	    // check allSessionsSummary exists
	    SessionDTO allSessionsSummary = sessionDTOs.getFirst();
	    if ("0".equals(allSessionsSummary.getSessionId())) {
		nominationNames = allSessionsSummary.getMapStandardNominationsHTMLedContent();
		nominationVotes = allSessionsSummary.getMapStandardRatesContent();

		//replace all html tags
		for (Long index : nominationNames.keySet()) {
		    String name = nominationNames.get(index);
		    String noHTMLNomination = VoteUtils.stripHTML(name);
		    nominationNames.put(index, noHTMLNomination);
		}
	    }

	    //sessionId should not be blank
	} else if (!StringUtils.isBlank(currentSessionId)) {
	    VoteSession voteSession = voteService.getSessionBySessionId(new Long(currentSessionId));
	    VoteContent voteContent = voteSession.getVoteContent();

	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = voteService.prepareChartData(request,
		    voteContent.getVoteContentId(), voteSession.getUid(), new VoteGeneralLearnerFlowDTO());

	    nominationNames = voteGeneralLearnerFlowDTO.getMapStandardNominationsContent();
	    nominationVotes = voteGeneralLearnerFlowDTO.getMapStandardRatesContent();
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	for (Long index : nominationNames.keySet()) {
	    ObjectNode nomination = JsonNodeFactory.instance.objectNode();
	    // nominations' names and values go separately
	    nomination.put("name", nominationNames.get(index));
	    nomination.put("value", nominationVotes.get(index));
	    responseJSON.withArray("data").add(nomination);
	}

	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }
}