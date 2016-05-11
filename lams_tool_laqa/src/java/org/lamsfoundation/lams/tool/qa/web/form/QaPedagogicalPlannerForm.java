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


package org.lamsfoundation.lams.tool.qa.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerActivityForm;

public class QaPedagogicalPlannerForm extends PedagogicalPlannerActivityForm {
    private List<String> question;
    private String contentFolderID;

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
	if (question != null && !question.isEmpty()) {
	    for (String item : question) {
		if (!StringUtils.isEmpty(item)) {
		    allEmpty = false;
		    break;
		}
	    }
	}
	if (allEmpty) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    valid = false;
	    question = null;
	}

	setValid(valid);
	return errors;
    }

    public void fillForm(QaContent qaContent) {
	if (qaContent != null) {
	    setToolContentID(qaContent.getQaContentId());

	    question = new ArrayList<String>();
	    Set questions = qaContent.getQaQueContents();
	    if (questions != null) {
		int topicIndex = 0;
		for (QaQueContent message : (Set<QaQueContent>) questions) {
		    setQuestion(topicIndex++, message.getQuestion());
		}
	    }
	}
    }

    public void setQuestion(int number, String Questions) {
	if (question == null) {
	    question = new ArrayList<String>();
	}
	while (number >= question.size()) {
	    question.add(null);
	}
	question.set(number, Questions);
    }

    public String getQuestion(int number) {
	if (question == null || number >= question.size()) {
	    return null;
	}
	return question.get(number);
    }

    public Integer getQuestionCount() {
	return question == null ? 0 : question.size();
    }

    public boolean removeQuestion(int number) {
	if (question == null || number >= question.size()) {
	    return false;
	}
	question.remove(number);
	return true;
    }

    public List<String> getQuestionList() {
	return question;
    }
}