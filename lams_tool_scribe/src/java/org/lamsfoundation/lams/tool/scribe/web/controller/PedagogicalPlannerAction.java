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


package org.lamsfoundation.lams.tool.scribe.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.web.forms.ScribePedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
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
public class PedagogicalPlannerAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(PedagogicalPlannerAction.class);
    public IScribeService scribeService;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return initPedagogicalPlannerForm(mapping, form, request, response);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ScribePedagogicalPlannerForm plannerForm = (ScribePedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scribe scribe = getScribeService().getScribeByContentId(toolContentID);
	plannerForm.fillForm(scribe);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward("success");
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	ScribePedagogicalPlannerForm plannerForm = (ScribePedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    Scribe scribe = getScribeService().getScribeByContentId(plannerForm.getToolContentID());

	    int headingIndex = 0;
	    String heading = null;
	    ScribeHeading scribeHeading = null;
	    List<ScribeHeading> newHeadings = new LinkedList<ScribeHeading>();
	    Iterator<ScribeHeading> scribeHeadingIterator = scribe.getScribeHeadings().iterator();
	    do {
		heading = plannerForm.getHeading(headingIndex);
		if (StringUtils.isEmpty(heading)) {
		    plannerForm.removeHeading(headingIndex);
		} else {
		    if (scribeHeadingIterator.hasNext()) {
			scribeHeading = scribeHeadingIterator.next();
			scribeHeading.setDisplayOrder(headingIndex);
		    } else {
			scribeHeading = new ScribeHeading();
			scribeHeading.setHeadingText(heading);
			scribeHeading.setDisplayOrder(headingIndex);

			newHeadings.add(scribeHeading);
			scribeHeading.setScribe(scribe);
		    }
		    headingIndex++;
		}

	    } while (heading != null);
	    while (scribeHeadingIterator.hasNext()) {
		scribeHeading = scribeHeadingIterator.next();
		scribeHeadingIterator.remove();
		getScribeService().deleteHeading(scribeHeading.getUid());
	    }
	    scribe.getScribeHeadings().addAll(newHeadings);
	    getScribeService().saveOrUpdateScribe(scribe);
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward("success");
    }

    public ActionForward createPedagogicalPlannerHeading(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ScribePedagogicalPlannerForm plannerForm = (ScribePedagogicalPlannerForm) form;
	plannerForm.setHeading(plannerForm.getHeadingCount().intValue(), "");
	return mapping.findForward("success");
    }

    private IScribeService getScribeService() {
	if (scribeService == null) {
	    scribeService = ScribeServiceProxy.getScribeService(this.getServlet().getServletContext());
	}
	return scribeService;
    }
}