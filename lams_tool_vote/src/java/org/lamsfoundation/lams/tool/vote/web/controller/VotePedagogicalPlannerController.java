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

package org.lamsfoundation.lams.tool.vote.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.web.form.VotePedagogicalPlannerForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedagogicalPlanner")
public class VotePedagogicalPlannerController {
    @Autowired
    private IVoteService voteService;

    @Autowired
    @Qualifier("lavoteMessageService")
    private MessageService messageService;

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(VotePedagogicalPlannerForm plannerForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteContent voteContent = voteService.getVoteContent(toolContentID);
	plannerForm.fillForm(voteContent);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/saveOrUpdatePedagogicalPlannerForm")
    public String saveOrUpdatePedagogicalPlannerForm(VotePedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {
	MultiValueMap<String, String> errorMap = plannerForm.validate(messageService);
	if (errorMap.isEmpty()) {
	    VoteContent voteContent = voteService.getVoteContent(plannerForm.getToolContentID());
	    voteContent.setInstructions(plannerForm.getInstructions());

	    int nominationIndex = 1;
	    String nomination = null;

	    do {
		nomination = plannerForm.getNomination(nominationIndex - 1);
		if (StringUtils.isEmpty(nomination)) {
		    plannerForm.removeNomination(nominationIndex - 1);
		} else {
		    if (nominationIndex <= voteContent.getVoteQueContents().size()) {
			VoteQueContent voteQueContent = voteService.getQuestionByDisplayOrder((long) nominationIndex,
				voteContent.getUid());
			voteQueContent.setQuestion(nomination);
			voteService.saveOrUpdateVoteQueContent(voteQueContent);

		    } else {
			VoteQueContent voteQueContent = new VoteQueContent();
			voteQueContent.setDisplayOrder(nominationIndex);
			voteQueContent.setVoteContent(voteContent);
			voteQueContent.setVoteContent(voteContent);
			voteQueContent.setQuestion(nomination);
			voteService.saveOrUpdateVoteQueContent(voteQueContent);
		    }
		    nominationIndex++;
		}
	    } while (nominationIndex <= plannerForm.getNominationCount());
	    if (nominationIndex <= voteContent.getVoteQueContents().size()) {
		voteService.removeQuestionsFromCache(voteContent);
		voteService.removeVoteContentFromCache(voteContent);
		for (; nominationIndex <= voteContent.getVoteQueContents().size(); nominationIndex++) {
		    VoteQueContent voteQueContent = voteService.getQuestionByDisplayOrder((long) nominationIndex,
			    voteContent.getUid());
		    voteService.removeVoteQueContent(voteQueContent);
		}
	    }
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/createPedagogicalPlannerQuestion")
    public String createPedagogicalPlannerQuestion(VotePedagogicalPlannerForm plannerForm) {
	plannerForm.setNomination(plannerForm.getNominationCount().intValue(), "");
	return "/authoring/pedagogicalPlannerForm";
    }
}