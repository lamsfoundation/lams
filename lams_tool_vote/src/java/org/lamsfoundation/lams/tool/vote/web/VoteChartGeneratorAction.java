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

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Prepares data for charts.
 *
 * @author Marcin Cieslak
 */
public class VoteChartGeneratorAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(VoteChartGeneratorAction.class.getName());
    private static IVoteService voteService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String currentSessionId = request.getParameter("currentSessionId");

	Map<Long, String> nominationNames = new HashMap<>();
	Map<Long, Double> nominationVotes = new HashMap<>();

	//request for the all session summary
	if ("0".equals(currentSessionId)) {
	    long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	    LinkedList<SessionDTO> sessionDTOs = getVoteService().getSessionDTOs(toolContentID);

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
	    logger.warn("Session Id should not be blank");
	    VoteSession voteSession = getVoteService().getSessionBySessionId(new Long(currentSessionId));
	    VoteContent voteContent = voteSession.getVoteContent();

	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = getVoteService().prepareChartData(request,
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

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private IVoteService getVoteService() {
	if (voteService == null) {
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}
	return voteService;
    }
}