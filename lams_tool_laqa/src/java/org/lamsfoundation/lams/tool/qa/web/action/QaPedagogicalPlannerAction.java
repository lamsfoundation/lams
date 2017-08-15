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


package org.lamsfoundation.lams.tool.qa.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.web.form.QaPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class QaPedagogicalPlannerAction extends LamsDispatchAction {

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return initPedagogicalPlannerForm(mapping, form, request, response);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	QaPedagogicalPlannerForm plannerForm = (QaPedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent qaContent = getQaService().getQaContent(toolContentID);
	plannerForm.fillForm(qaContent);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward(QaAppConstants.SUCCESS);
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	QaPedagogicalPlannerForm plannerForm = (QaPedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {

	    QaContent qaContent = getQaService().getQaContent(plannerForm.getToolContentID());

	    int questionIndex = 0;
	    String question = null;

	    do {
		question = plannerForm.getQuestion(questionIndex);
		if (StringUtils.isEmpty(question)) {
		    plannerForm.removeQuestion(questionIndex);
		} else {
		    if (questionIndex < qaContent.getQaQueContents().size()) {
			QaQueContent qaQuestion = getQaService()
				.getQuestionByContentAndDisplayOrder((long) questionIndex + 1, qaContent.getUid());
			qaQuestion.setQuestion(question);
			getQaService().saveOrUpdateQuestion(qaQuestion);

		    } else {
			QaQueContent qaQuestion = new QaQueContent();
			qaQuestion.setDisplayOrder(questionIndex + 1);
			qaQuestion.setRequired(false);
			qaQuestion.setQaContent(qaContent);
			qaQuestion.setQuestion(question);
			getQaService().saveOrUpdateQuestion(qaQuestion);
		    }
		    questionIndex++;
		}
	    } while (question != null);
	    if (questionIndex < qaContent.getQaQueContents().size()) {
		getQaService().removeQuestionsFromCache(qaContent);
		getQaService().removeQaContentFromCache(qaContent);
		for (; questionIndex < qaContent.getQaQueContents().size(); questionIndex++) {
		    QaQueContent qaQuestion = getQaService()
			    .getQuestionByContentAndDisplayOrder((long) questionIndex + 1, qaContent.getUid());
		    getQaService().removeQuestion(qaQuestion);
		}
	    }
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward(QaAppConstants.SUCCESS);
    }

    public ActionForward createPedagogicalPlannerQuestion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	QaPedagogicalPlannerForm plannerForm = (QaPedagogicalPlannerForm) form;
	plannerForm.setQuestion(plannerForm.getQuestionCount().intValue(), "");
	return mapping.findForward(QaAppConstants.SUCCESS);
    }

    private IQaService getQaService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return QaServiceProxy.getQaService(getServlet().getServletContext());
    }
}