/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.vote.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.web.form.VotePedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class VotePedagogicalPlannerAction extends LamsDispatchAction {

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return initPedagogicalPlannerForm(mapping, form, request, response);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	VotePedagogicalPlannerForm plannerForm = (VotePedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteContent voteContent = getVoteService().getVoteContent(toolContentID);
	plannerForm.fillForm(voteContent);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward(VoteAppConstants.SUCCESS);
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	VotePedagogicalPlannerForm plannerForm = (VotePedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    VoteContent voteContent = getVoteService().getVoteContent(plannerForm.getToolContentID());
	    voteContent.setInstructions(plannerForm.getInstructions());

	    int nominationIndex = 1;
	    String nomination = null;

	    do {
		nomination = plannerForm.getNomination(nominationIndex - 1);
		if (StringUtils.isEmpty(nomination)) {
		    plannerForm.removeNomination(nominationIndex - 1);
		} else {
		    if (nominationIndex <= voteContent.getVoteQueContents().size()) {
			VoteQueContent voteQueContent = getVoteService()
				.getQuestionByDisplayOrder((long) nominationIndex, voteContent.getUid());
			voteQueContent.setQuestion(nomination);
			getVoteService().saveOrUpdateVoteQueContent(voteQueContent);

		    } else {
			VoteQueContent voteQueContent = new VoteQueContent();
			voteQueContent.setDisplayOrder(nominationIndex);
			voteQueContent.setVoteContent(voteContent);
			voteQueContent.setVoteContentId(voteContent.getVoteContentId());
			voteQueContent.setQuestion(nomination);
			getVoteService().saveOrUpdateVoteQueContent(voteQueContent);
		    }
		    nominationIndex++;
		}
	    } while (nominationIndex <= plannerForm.getNominationCount());
	    if (nominationIndex <= voteContent.getVoteQueContents().size()) {
		getVoteService().removeQuestionsFromCache(voteContent);
		getVoteService().removeVoteContentFromCache(voteContent);
		for (; nominationIndex <= voteContent.getVoteQueContents().size(); nominationIndex++) {
		    VoteQueContent voteQueContent = getVoteService().getQuestionByDisplayOrder((long) nominationIndex,
			    voteContent.getUid());
		    getVoteService().removeVoteQueContent(voteQueContent);
		}
	    }
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward(VoteAppConstants.SUCCESS);
    }

    public ActionForward createPedagogicalPlannerQuestion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	VotePedagogicalPlannerForm plannerForm = (VotePedagogicalPlannerForm) form;
	plannerForm.setNomination(plannerForm.getNominationCount().intValue(), "");
	return mapping.findForward(VoteAppConstants.SUCCESS);
    }

    private IVoteService getVoteService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return VoteServiceProxy.getVoteService(getServlet().getServletContext());
    }
}