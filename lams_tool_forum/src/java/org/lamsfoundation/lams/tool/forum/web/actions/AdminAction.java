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

package org.lamsfoundation.lams.tool.forum.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.forum.persistence.ForumConfigItem;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.web.forms.AdminForm;

/**
 * @author Marcin Cieslak
 */
public class AdminAction extends Action {
    private IForumService forumService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("saveContent")) {
	    return saveContent(mapping, form, request, response);
	}

	return start(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	if (forumService == null) {
	    forumService = ForumServiceProxy.getForumService(this.getServlet().getServletContext());
	}

	AdminForm adminForm = (AdminForm) form;

	ForumConfigItem keepLearnerContent = forumService.getConfigItem(ForumConfigItem.KEY_KEEP_LEARNER_CONTENT);
	if (keepLearnerContent != null) {
	    adminForm.setKeepLearnerContent(Boolean.valueOf(keepLearnerContent.getConfigValue()));
	}

	request.setAttribute("error", false);
	return mapping.findForward("config");
    }

    public ActionForward saveContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AdminForm adminForm = (AdminForm) form;

	if (forumService == null) {
	    forumService = ForumServiceProxy.getForumService(this.getServlet().getServletContext());
	}

	ForumConfigItem keepLearnerContent = forumService.getConfigItem(ForumConfigItem.KEY_KEEP_LEARNER_CONTENT);
	keepLearnerContent.setConfigValue(String.valueOf(adminForm.getKeepLearnerContent()));
	forumService.saveForumConfigItem(keepLearnerContent);

	request.setAttribute("savedSuccess", true);
	return mapping.findForward("config");
    }
}