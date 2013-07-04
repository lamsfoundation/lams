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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * Prepares data for charts.
 * 
 * @author Marcin Cieslak
 */
public class VoteChartGeneratorAction extends LamsDispatchAction {

    private static IVoteService voteService;
    private static MessageService messageService;

    @SuppressWarnings("unchecked")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	String currentSessionId = request.getParameter("currentSessionId");

	if (!StringUtils.isBlank(currentSessionId)) {
	    // it is a call from Monitor
	    VoteSession voteSession = getVoteService().retrieveVoteSession(new Long(currentSessionId));
	    VoteContent voteContent = voteSession.getVoteContent();

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	    MonitoringUtil.prepareChartData(request, getVoteService(), null, voteContent.getVoteContentId().toString(),
		    voteSession.getUid().toString(), null, voteGeneralMonitoringDTO, getMessageService());
	}

	Map<String, String> nominationNames = (Map<String, String>) request.getSession().getAttribute(
		VoteAppConstants.MAP_STANDARD_NOMINATIONS_CONTENT);
	Map<String, String> nominationVotes = (Map<String, String>) request.getSession().getAttribute(
		VoteAppConstants.MAP_STANDARD_RATES_CONTENT);

	JSONObject responseJSON = new JSONObject();
	for (String index : nominationNames.keySet()) {
	    JSONObject nomination = new JSONObject();
	    // nominations' names and values go separately
	    nomination.put("name", nominationNames.get(index));
	    nomination.put("value", nominationVotes.get(index));
	    responseJSON.append("data", nomination);
	}
	
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private MessageService getMessageService() {
	if (messageService == null) {
	    messageService = (MessageService) VoteServiceProxy.getMessageService(getServlet().getServletContext());
	}
	return messageService;
    }

    private IVoteService getVoteService() {
	if (voteService == null) {
	    voteService = (IVoteService) VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}
	return voteService;
    }
}