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

package org.lamsfoundation.lams.tool.noticeboard.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.web.form.NbPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedagogicalPlanner")
public class PedagogicalPlannerController {
    private static Logger logger = Logger.getLogger(PedagogicalPlannerController.class);

    @Autowired
    private INoticeboardService nbService;

    protected String unspecified(@ModelAttribute NbPedagogicalPlannerForm plannerForm, HttpServletRequest request) {
	return initPedagogicalPlannerForm(plannerForm, request);
    }

    @RequestMapping("/init")
    public String initPedagogicalPlannerForm(@ModelAttribute NbPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	NoticeboardContent noticeboard = nbService.retrieveNoticeboard(toolContentID);
	plannerForm.fillForm(noticeboard);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "authoring/pedagogicalPlannerForm";

    }

    @RequestMapping("/saveOrUpdate")
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute NbPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = plannerForm.validate(null);
	if (errorMap.isEmpty()) {
	    String content = plannerForm.getBasicContent();
	    Long toolContentID = plannerForm.getToolContentID();
	    NoticeboardContent noticeboard = nbService.retrieveNoticeboard(toolContentID);
	    noticeboard.setContent(content);
	    nbService.saveNoticeboard(noticeboard);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}

	return "authoring/pedagogicalPlannerForm";
    }

}