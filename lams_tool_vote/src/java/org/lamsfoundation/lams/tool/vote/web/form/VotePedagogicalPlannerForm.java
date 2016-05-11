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


package org.lamsfoundation.lams.tool.vote.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerActivityForm;

public class VotePedagogicalPlannerForm extends PedagogicalPlannerActivityForm {
    private List<String> nomination;
    private String contentFolderID;
    private String instructions;

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    @Override
    public ActionMessages validate() {
	ActionMessages errors = new ActionMessages();
	boolean valid = true;
	boolean allEmpty = true;
	if (nomination != null && !nomination.isEmpty()) {
	    for (String item : nomination) {
		if (!StringUtils.isEmpty(item)) {
		    allEmpty = false;
		    break;
		}
	    }
	}
	if (allEmpty) {
	    ActionMessage error = new ActionMessage("nominations.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    valid = false;
	    nomination = null;
	}

	setValid(valid);
	return errors;
    }

    public void fillForm(VoteContent voteContent) {
	if (voteContent != null) {
	    setToolContentID(voteContent.getVoteContentId());
	    setInstructions(voteContent.getInstructions());

	    nomination = new ArrayList<String>();
	    Set questions = voteContent.getVoteQueContents();
	    if (questions != null) {
		int topicIndex = 0;
		for (VoteQueContent message : (Set<VoteQueContent>) questions) {
		    setNomination(topicIndex++, message.getQuestion());
		}
	    }
	}
    }

    public void setNomination(int number, String nomination) {
	if (this.nomination == null) {
	    this.nomination = new ArrayList<String>();
	}
	while (number >= this.nomination.size()) {
	    this.nomination.add(null);
	}
	this.nomination.set(number, nomination);
    }

    public String getNomination(int number) {
	if (nomination == null || number >= nomination.size()) {
	    return null;
	}
	return nomination.get(number);
    }

    public Integer getNominationCount() {
	return nomination == null ? 0 : nomination.size();
    }

    public boolean removeNomination(int number) {
	if (nomination == null || number >= nomination.size()) {
	    return false;
	}
	nomination.remove(number);
	return true;
    }

    public List<String> getNominationList() {
	return nomination;
    }
}