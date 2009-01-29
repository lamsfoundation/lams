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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.form.SubmitFilesPedagogicalPlannerForm;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/pedagogicalPlanner" name="SbmtPedagogicalPlannerForm" parameter="dispatch" scope="request"
 *                validate="false"
 * 
 * @struts.action-forward name="success" path="/authoring/pedagogicalPlannerForm.jsp"
 */
public class SubmitFilesPedagogicalPlannerAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(PedagogicalPlannerAction.class);

    public ISubmitFilesService submitFilesService;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (submitFilesService == null) {
	    submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
	}
	return initPedagogicalPlannerForm(mapping, form, request, response);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SubmitFilesPedagogicalPlannerForm plannerForm = (SubmitFilesPedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	SubmitFilesContent submitFiles = getSubmitFilesService().getSubmitFilesContent(toolContentID);
	String command = WebUtil.readStrParam(request, AttributeNames.PARAM_COMMAND, true);
	if (command == null) {
	    plannerForm.fillForm(submitFiles);
	    String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	    plannerForm.setContentFolderID(contentFolderId);
	    return mapping.findForward(SbmtConstants.SUCCESS);
	} else {
	    try {
		String onlineInstructions = submitFiles.getOnlineInstruction();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();

		if (AttributeNames.COMMAND_CHECK_EDITING_ADVICE.equals(command)) {
		    Integer activityIndex = WebUtil.readIntParam(request, AttributeNames.PARAM_ACTIVITY_INDEX);
		    String responseText = (StringUtils.isEmpty(submitFiles.getOnlineInstruction()) ? "NO" : "OK") + '&'
			    + activityIndex;
		    writer.print(responseText);

		} else if (AttributeNames.COMMAND_GET_EDITING_ADVICE.equals(command)) {
		    writer.print(onlineInstructions);
		}
	    } catch (IOException e) {
		SubmitFilesPedagogicalPlannerAction.logger.error(e);
	    }
	    return null;
	}
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	SubmitFilesPedagogicalPlannerForm plannerForm = (SubmitFilesPedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    String content = plannerForm.getInstruction();
	    Long toolContentID = plannerForm.getToolContentID();
	    SubmitFilesContent submitFiles = getSubmitFilesService().getSubmitFilesContent(toolContentID);
	    submitFiles.setInstruction(content);
	    getSubmitFilesService().saveOrUpdateContent(submitFiles);
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward(SbmtConstants.SUCCESS);
    }

    private ISubmitFilesService getSubmitFilesService() {
	if (submitFilesService == null) {
	    submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
	}
	return submitFilesService;
    }
}