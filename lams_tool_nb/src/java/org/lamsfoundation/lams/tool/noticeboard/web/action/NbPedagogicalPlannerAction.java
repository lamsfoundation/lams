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


package org.lamsfoundation.lams.tool.noticeboard.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.web.form.NbPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 */
public class NbPedagogicalPlannerAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(PedagogicalPlannerAction.class);

    public INoticeboardService noticeboardService;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (noticeboardService == null) {
	    noticeboardService = NoticeboardServiceProxy.getNbService(this.getServlet().getServletContext());
	}
	return initPedagogicalPlannerForm(mapping, form, request, response);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	NbPedagogicalPlannerForm plannerForm = (NbPedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	NoticeboardContent noticeboard = getNoticeboardService().retrieveNoticeboard(toolContentID);
	plannerForm.fillForm(noticeboard);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward("success");

    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	NbPedagogicalPlannerForm plannerForm = (NbPedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    String content = plannerForm.getBasicContent();
	    Long toolContentID = plannerForm.getToolContentID();
	    NoticeboardContent noticeboard = getNoticeboardService().retrieveNoticeboard(toolContentID);
	    noticeboard.setContent(content);
	    getNoticeboardService().saveNoticeboard(noticeboard);
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward("success");
    }

    private INoticeboardService getNoticeboardService() {
	if (noticeboardService == null) {
	    noticeboardService = NoticeboardServiceProxy.getNbService(this.getServlet().getServletContext());
	}
	return noticeboardService;
    }
}